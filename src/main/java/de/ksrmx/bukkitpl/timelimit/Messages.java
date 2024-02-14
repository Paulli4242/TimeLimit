package de.ksrmx.bukkitpl.timelimit;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.module.Configuration;

public class Messages {
    protected final ConfigurationSection messages;

    public Messages(ConfigurationSection messages) {
        this.messages = messages;
    }

    public String buildMessage(String path, String... replacements){
        String message = messages.getString(path);
        if(replacements.length%2!=0) throw new IllegalArgumentException("replacements' length has to be a multiple of 2 ");
        for(int i = 0; i<replacements.length;i+=2){
            message = message.replace(replacements[i], replacements[i+1]);
        }
        if(message==null)return "";
        else return message;
    }

    public void sendMessage(CommandSender sender, String path, String... replacements){
        sender.sendMessage(buildMessage(path,replacements));
    }

}
