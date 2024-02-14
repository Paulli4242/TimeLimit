package de.ksrmx.bukkitpl.timelimit.commands;

import de.ksrmx.bukkitpl.timelimit.Messages;
import de.ksrmx.bukkitpl.timelimit.main.TimeLimiter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.ccffee.utils.iteration.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class SeeCommand extends ACommand {

    private final TimeLimiter timeLimiter;
    private final Messages messages;

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
                messages.sendMessage(sender,"command.time-limit.see.success","%TIME%",Long.toString(time));
            }else{
                messages.sendMessage(sender, "command.time-limit.see.not-player");
            }
        }else if(args.length==1 && (sender.hasPermission("time-limit.*") || sender.hasPermission("time-limit.see.others"))){
            boolean success = false;
            for(OfflinePlayer p : Bukkit.getOfflinePlayers()){
                if(p.getName().equalsIgnoreCase(args[0])){
                    long time = timeLimiter.getPlayTime(p.getUniqueId()).getTime();
                    messages.sendMessage(sender,"command.time-limit.see.others.success", "%PLAYER%", p.getName(),"%TIME%", Long.toString(time));
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
