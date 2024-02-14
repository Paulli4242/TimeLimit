package de.ksrmx.bukkitpl.timelimit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class PermissionTabExecutor implements TabExecutor {

    public PermissionTabExecutor(ACommand command) {
        this.cmd = command;
    }

    protected ACommand cmd;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(cmd.hasPermission(sender))
            return cmd.onCommand(sender, command, label, args);
        else return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(cmd.hasPermission(sender))
            return cmd.onTabComplete(sender, command, label, args);
        else return new ArrayList<>();
    }
}
