package de.ksrmx.bukkitpl.timelimit.timerules;

import de.ksrmx.bukkitpl.timelimit.FormatException;
import de.ksrmx.bukkitpl.timelimit.TimeLimitPlugin;
import de.ksrmx.bukkitpl.timelimit.time.IntervalTimeFormat;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class RuleResolver {

    protected ConfigurationSection config;
    protected TimeLimitPlugin plugin;

    public RuleResolver(ConfigurationSection config, TimeLimitPlugin plugin){
        this.config = config;
        this.plugin = plugin;
    }

    @SuppressWarnings("ConstantConditions")
    public ArrayList<TimeRule<?>> resolveRules(){
        ArrayList<TimeRule<?>> rules = new ArrayList<>();
        for(String key : config.getKeys(false)){
            try{
                ConfigurationSection section = config.getConfigurationSection(key);
                switch (section.getString("type")){
                    case "interval-add" -> {
                        long addTime = section.getLong("add-time");
                        IntervalType intervalType = IntervalType.valueOf(section.getString("interval-type").toUpperCase());
                        IntervalTimeFormat format = IntervalTimeFormat.of(section.getString("interval-format"), intervalType.getUsableFormat());
                        String intTime = section.getString("interval-time");
                        List<String> permissions;
                        if (section.contains("permissions")) {
                            permissions = section.getStringList("permissions");
                        } else {
                            permissions = null;
                        }
                        rules.add(new IntervalAddTimeRule(key, addTime * 1000, intervalType, format, format.format(intTime, intervalType), permissions));
                    }
                    case "interval-ignore" -> {
                        IntervalType intervalType = IntervalType.valueOf(section.getString("interval-type").toUpperCase());
                        IntervalTimeFormat format = IntervalTimeFormat.of(section.getString("interval-format"), intervalType.getUsableFormat());
                        String fromTime = section.getString("from-time");
                        String toTime = section.getString("to-time");
                        List<String> permissions;
                        if (section.contains("permissions")) {
                            permissions = section.getStringList("permissions");
                        } else {
                            permissions = null;
                        }
                        rules.add(new IntervalIgnoreRule(key, intervalType, format, format.format(fromTime, intervalType), format.format(toTime, intervalType), permissions));
                    }
                    case "first-join-add" -> {
                        long addTime = section.getLong("add-time");
                        List<String> permissions;
                        if (section.contains("permissions")) {
                            permissions = section.getStringList("permissions");
                        } else {
                            permissions = null;
                        }
                        rules.add(new FirstJoinAddTimeRule(key, addTime * 1000, permissions));
                    }

                }
            }catch (FormatException e){
                plugin.getLogger().log(Level.WARNING,"Could not load TimeRule \""+key+"\":",e);
            }catch (NullPointerException e){
                plugin.getLogger().log(Level.WARNING,"Could not load TimeRule \""+key+"\":", new FormatException("missing essential field"));
            }
        }
        return rules;
    }
}
