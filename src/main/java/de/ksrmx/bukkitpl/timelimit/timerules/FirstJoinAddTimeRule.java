package de.ksrmx.bukkitpl.timelimit.timerules;

import de.ksrmx.bukkitpl.timelimit.main.PlayerData;

import javax.annotation.Nullable;
import java.util.List;

public class FirstJoinAddTimeRule extends TimeRule<FirstJoinAddTimeRule> implements IJoinTimeRule {

    protected long time;
    protected List<String> permissions;

    public FirstJoinAddTimeRule(String name, long time, @Nullable List<String> permissions) {
        super(name);
        this.time = time;
        this.permissions = permissions;
    }

    @Override
    public long getTimeAddOnJoin(long currentTime, PlayerData player) {
        if(permissions != null){
            for(String p : permissions){
                if(player.hasPermission(p))return time;
            }
            return 0;
        }else return time;
    }

    @Override
    public long getTimeAddAmount(long currentTime, PlayerData player) {
        return 0;
    }

    @Override
    public int useTime(long currentTime, PlayerData player) {
        return 0;
    }

    @Override
    public TimeRuleData<FirstJoinAddTimeRule> fromBytes(byte[] bytes) {
        return null;
    }

    @Override
    public byte[] toBytes(TimeRuleData<FirstJoinAddTimeRule> firstJoinRuleTimeRuleData) {
        return new byte[0];
    }

    @Override
    protected TimeRuleData<FirstJoinAddTimeRule> getDefaultData() {
        return null;
    }
}
