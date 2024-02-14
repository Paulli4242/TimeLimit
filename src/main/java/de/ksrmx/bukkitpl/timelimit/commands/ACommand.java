package de.ksrmx.bukkitpl.timelimit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public abstract class ACommand implements TabExecutor {

    protected String[] names;
    protected String[] permissions;

    public ACommand(String... names) {
        this(new String[0],names);
    }
    public ACommand(String[] names, String[] permissions) {
        this.permissions = permissions;
        this.names = names;
    }

    public String[] getNames() {
        return names;
    }

    public boolean hasPermission(CommandSender sender){
        if(permissions.length == 0)return true;
        for(String p : permissions){
            if(sender.hasPermission(p))return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}
