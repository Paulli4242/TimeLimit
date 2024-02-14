package de.ksrmx.bukkitpl.timelimit.commands;

import de.ksrmx.bukkitpl.timelimit.Messages;
import de.ksrmx.bukkitpl.timelimit.main.PlayTime;
import de.ksrmx.bukkitpl.timelimit.main.TimeLimiter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ccffee.utils.iteration.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class TakeCommand extends ACommand {

    private Messages messages;
    private TimeLimiter timeLimiter;

    public TakeCommand(Messages messages, TimeLimiter timeLimiter) {
        super(ArrayUtils.of("take"),ArrayUtils.of("time-limit.*","time-limit.take"));
        this.messages = messages;
        this.timeLimiter = timeLimiter;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==2){
            Player player = Bukkit.getPlayerExact(args[0]);
            if(player!=null){
                PlayTime pt = timeLimiter.getPlayTime(player.getUniqueId());
                if(pt!=null){
                    try{
                        long l = Long.parseLong(args[1]);
                        if(pt.removeTime(timeLimiter,l*1000)){
                            messages.sendMessage(sender,"command.time-limit.take.success","%SECONDS%",Long.toString(l),"%PLAYER%", player.getName());
                        }else{
                            messages.sendMessage(sender,"command.time-limit.take.invalid-number","%NUMBER%", args[1]);
                        }
                    }catch (NumberFormatException e){
                        messages.sendMessage(sender,"command.time-limit.take.invalid-number","%NUMBER%", args[1]);
                    }
                }else messages.sendMessage(sender,"command.time-limit.take.missing-data","%PLAYER%", player.getName());
            }else{
                messages.sendMessage(sender,"command.time-limit.take.invalid-player", "%PLAYER%",args[0]);
            }
            messages.sendMessage(sender,"command.time-limit.take.arg-amount");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> out = new ArrayList<>();

        return out;
    }
}
