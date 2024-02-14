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

public class PayCommand extends ACommand {

    private Messages messages;
    private TimeLimiter timeLimiter;

    public PayCommand(Messages messages, TimeLimiter timeLimiter) {
        super(ArrayUtils.of("pay"),ArrayUtils.of("time-limit.*","time-limit.pay"));
        this.messages = messages;
        this.timeLimiter = timeLimiter;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==2){
            if(sender instanceof Player p) {
                Player player = Bukkit.getPlayerExact(args[0]);
                if (player != null) {
                    PlayTime pts = timeLimiter.getPlayTime(p.getUniqueId());
                    PlayTime ptr = timeLimiter.getPlayTime(player.getUniqueId());
                    if (ptr != null) {
                        try {
                            long l = Long.parseLong(args[1]);
                            if (pts.getTime() >= l * 1000) {
                                if (pts.removeTime(timeLimiter, l * 1000)) {
                                    ptr.addTime(timeLimiter, l*1000);
                                    messages.sendMessage(sender, "command.time-limit.pay.success", "%SECONDS%", Long.toString(l), "%PLAYER%", player.getName());
                                    messages.sendMessage(player, "command.time-limit.pay.success-target","%PLAYER%",p.getName(),"%SECONDS%",Long.toString(l));
                                } else {
                                    messages.sendMessage(sender, "command.time-limit.pay.invalid-number", "%NUMBER%", args[1]);
                                }
                            } else messages.sendMessage(sender, "command.time-limit.pay.insufficient","%TIME%");
                        } catch (NumberFormatException e) {
                            messages.sendMessage(sender, "command.time-limit.pay.invalid-number", "%NUMBER%", args[1]);
                        }
                    } else messages.sendMessage(sender, "command.time-limit.pay.missing-data", "%PLAYER%", player.getName());
                } else {
                    messages.sendMessage(sender, "command.time-limit.pay.invalid-player", "%PLAYER%", args[0]);
                }
                messages.sendMessage(sender, "command.time-limit.pay.arg-amount");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> out = new ArrayList<>();

        return out;
    }
}
