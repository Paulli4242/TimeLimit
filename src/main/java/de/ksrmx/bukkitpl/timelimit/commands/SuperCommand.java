package de.ksrmx.bukkitpl.timelimit.commands;

import de.ksrmx.bukkitpl.timelimit.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SuperCommand extends ACommand {

    private ACommand[] subCommand;
    protected Messages messages;



    public SuperCommand(String[] names,String[] permissions, Messages messages, ACommand... subCommand){
        super(names,permissions);
        this.messages = messages;
        this.subCommand = subCommand;
    }
    public SuperCommand(String[] names, Messages messages, ACommand... subCommand){
        super(names,new String[0]);
        this.subCommand = subCommand;
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length>0){
            ACommand cmd = getCommand(args[0]);
            if(cmd!=null&&cmd.hasPermission(sender)){
                return cmd.onCommand(sender, command, label, Arrays.copyOfRange(args,1,args.length));
            }else{
               return alternateExecution(sender, command, label, args);
            }
        }else
        return true;
    }

    public boolean alternateExecution(CommandSender sender, Command command, String label, String[] args){
        messages.sendMessage(sender,"command.time-limit.invalid");
        return true;
    }

    @Nullable
    protected ACommand getCommand(String name){
        Objects.requireNonNull(name);
        for(ACommand cmd : subCommand){
            for(String s : cmd.getNames()){
                if(name.equals(s)) return cmd;
            }
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==1){
            ArrayList<String> list = new ArrayList<>();
            for(ACommand cmd : subCommand){
                if(cmd.hasPermission(sender)){
                    for(String name : cmd.getNames()){
                        if(name.startsWith(args[0])){
                            list.add(name);
                        }
                    }
                }
            }
            list.addAll(alternateCompletion(sender, command, label, args));
            return list;
        }else if(args.length>1){
            ACommand cmd = getCommand(args[0]);
            if(cmd!=null&& cmd.hasPermission(sender)){
                return cmd.onTabComplete(sender, command, label, Arrays.copyOfRange(args,1,args.length));
            }else return alternateCompletion(sender, command, label, args);
        }
        return new ArrayList<>();
    }

    public List<String> alternateCompletion(CommandSender sender, Command command, String label, String[] args){
        return new ArrayList<>();
    }

}
