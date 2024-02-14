package de.ksrmx.bukkitpl.timelimit.commands;

import de.ksrmx.bukkitpl.timelimit.Messages;
import de.ksrmx.bukkitpl.timelimit.main.PlayTime;
import de.ksrmx.bukkitpl.timelimit.main.TimeLimiter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import de.ksrmx.libs.utils.iteration.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class GiveCommand extends ACommand {

    private Messages messages;
    private TimeLimiter timeLimiter;

    public GiveCommand(Messages messages, TimeLimiter timeLimiter) {
        super(ArrayUtils.of("give"),ArrayUtils.of("time-limit.*","time-limit.give"));
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
                        if(pt.addTime(timeLimiter,l*1000)){
                            messages.sendMessage(sender,"command.time-limit.give.success","%SECONDS%",Long.toString(l),"%PLAYER%", player.getName());
                        }else{
                            messages.sendMessage(sender,"command.time-limit.give.invalid-number","%NUMBER%", args[1]);
                        }
                    }catch (NumberFormatException e){
                        messages.sendMessage(sender,"command.time-limit.give.invalid-number","%NUMBER%", args[1]);
                    }
                }else messages.sendMessage(sender,"command.time-limit.give.missing-data","%PLAYER%", player.getName());
            }else{
                messages.sendMessage(sender,"command.time-limit.give.invalid-player", "%PLAYER%",args[0]);
            }
            messages.sendMessage(sender,"command.time-limit.give.arg-amount");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> out = new ArrayList<>();

        return out;
    }
}
