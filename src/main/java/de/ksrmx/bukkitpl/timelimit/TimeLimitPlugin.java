package de.ksrmx.bukkitpl.timelimit;

import de.ksrmx.bukkitpl.timelimit.commands.TimeCommand;
import de.ksrmx.bukkitpl.timelimit.main.EventListener;
import de.ksrmx.bukkitpl.timelimit.main.TimeLimiter;
import de.ksrmx.bukkitpl.timelimit.timerules.RuleResolver;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TimeLimitPlugin extends JavaPlugin {

    private static TimeLimitPlugin plugin;
    private static TimeLimiter timeLimiter;
    private File messageFile;
    private YamlConfiguration messagesConfig;

    private Messages messages;

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        plugin = this;
        timeLimiter = new TimeLimiter(new File(getDataFolder(),"data.dat"),this);
        Bukkit.getPluginManager().registerEvents(new EventListener(timeLimiter),this);

        //load Config
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        //load messages
        messageFile = new File(getDataFolder(),"messages.yml");
        if(!messageFile.isFile())
            saveResource("messages.yml",false);
        messagesConfig = YamlConfiguration.loadConfiguration(messageFile);
        InputStream defMessages = getResource("message.yml");
        if(defMessages != null){
            messagesConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defMessages)));
        }
        messages = new Messages(messagesConfig);
        //init commands
        TimeCommand cmd = new TimeCommand(timeLimiter, messages);
        Objects.requireNonNull(Bukkit.getPluginCommand("time-limit")).setExecutor(cmd);

        //init time limiter
        int interval = config.getInt("update-interval",1);
        timeLimiter.init(Bukkit.getOnlinePlayers(),new RuleResolver(config.getConfigurationSection("rules"),this).resolveRules(),interval,messages);
    }


    @Override
    public void onDisable() {
        timeLimiter.disable();
    }

    @SuppressWarnings("unused")
    public static TimeLimitPlugin getPlugin() {
        return plugin;
    }

    @SuppressWarnings("unused")
    public static TimeLimiter getTimeLimiter() {
        return timeLimiter;
    }
}
