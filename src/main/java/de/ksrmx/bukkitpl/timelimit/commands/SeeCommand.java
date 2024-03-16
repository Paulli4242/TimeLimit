package de.ksrmx.bukkitpl.timelimit.commands;

import de.ksrmx.bukkitpl.timelimit.Messages;
import de.ksrmx.bukkitpl.timelimit.main.TimeLimiter;
import de.ksrmx.bukkitpl.timelimit.time.DynamicTimeSpanFormatter;
import de.ksrmx.bukkitpl.timelimit.time.TimeSpanUnit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import de.ksrmx.libs.utils.iteration.ArrayUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class SeeCommand extends ACommand {

    private final TimeLimiter timeLimiter;
    private final Messages messages;

    private final DynamicTimeSpanFormatter timeSpanFormatter = new DynamicTimeSpanFormatter(3, EnumSet.allOf(TimeSpanUnit.class));

    public SeeCommand(TimeLimiter timeLimiter, Messages messages) {
        super(ArrayUtils.of("see"), ArrayUtils.of("time-limit.*","time-limit.see"));
        this.timeLimiter = timeLimiter;
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0){
            if(sender instanceof Player p){
                long time = timeLimiter.getPlayTime(p.getUniqueId()).getTime();
                messages.sendMessage(sender,"command.time-limit.see.success","%TIME%", timeSpanFormatter.format(time));
            }else{
                messages.sendMessage(sender, "command.time-limit.see.not-player");
            }
        }else if(args.length==1 && (sender.hasPermission("time-limit.*") || sender.hasPermission("time-limit.see.others"))){
            boolean success = false;
            for(OfflinePlayer p : Bukkit.getOfflinePlayers()){
                if(Objects.requireNonNull(p.getName()).equalsIgnoreCase(args[0])){
                    long time = timeLimiter.getPlayTime(p.getUniqueId()).getTime();
                    messages.sendMessage(sender,"command.time-limit.see.others.success", "%PLAYER%", p.getName(),"%TIME%", timeSpanFormatter.format(time));
                    success = true;
                    break;
                }
            }
            if(!success){
                messages.sendMessage(sender,"command.time-limit.see.others.unknown","%PLAYER%", args[0]);
            }
        }else messages.sendMessage(sender, "command.time-limit.see.invalid");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestion = new ArrayList<>();
        if(args.length==1){
            String arg = args[0].toLowerCase();
            for (Player p : Bukkit.getOnlinePlayers()){
                if(p.getName().toLowerCase().startsWith(arg)){
                    suggestion.add(p.getName());
                }
            }
        }

        return suggestion;
    }



}
