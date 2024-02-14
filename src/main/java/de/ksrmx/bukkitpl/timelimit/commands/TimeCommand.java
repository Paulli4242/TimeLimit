package de.ksrmx.bukkitpl.timelimit.commands;

import de.ksrmx.bukkitpl.timelimit.Messages;
import de.ksrmx.bukkitpl.timelimit.main.TimeLimiter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.ccffee.utils.iteration.ArrayUtils;

public class TimeCommand extends SuperCommand{

    private final Messages messages;

    public TimeCommand(TimeLimiter timeLimiter, Messages messages) {
        super(ArrayUtils.of("time-limit","tl"),
                new SeeCommand(timeLimiter,messages),
                new PayCommand(messages, timeLimiter),
                new GiveCommand(messages, timeLimiter),
                new TakeCommand(messages, timeLimiter)
        );
        this.messages = messages;
    }
}
