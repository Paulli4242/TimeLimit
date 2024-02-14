package de.ksrmx.bukkitpl.timelimit.timerules;

import de.ksrmx.bukkitpl.timelimit.main.PlayerData;
import de.ksrmx.bukkitpl.timelimit.time.IntervalTime;
import de.ksrmx.bukkitpl.timelimit.time.IntervalTimeFormat;

import javax.annotation.Nullable;
import java.util.List;

public class IntervalAddTimeRule extends TimeRule<IntervalAddTimeRule>{

    protected static class Data extends TimeRuleData<IntervalAddTimeRule>{

        public Data(long nextInterval) {
            this.nextInterval = nextInterval;
        }

        long nextInterval;

    }

    protected long addTime;
    protected IntervalType interval;
    protected IntervalTimeFormat timeFormat;
    protected IntervalTime intTime;
    protected List<String> permissions;

    protected long sessionTime;
    protected boolean sessionGive;


    public IntervalAddTimeRule(String name, long addTime, IntervalType interval, IntervalTimeFormat timeFormat,
                               IntervalTime intTime, @Nullable List<String> permissions) {
        super(name);
        this.addTime = addTime;
        this.interval = interval;
        this.timeFormat = timeFormat;
        this.intTime = intTime;
        this.permissions = permissions;
    }

    @Override
    public long getTimeAddAmount(long currentTime, PlayerData player) {
        if(currentTime==sessionTime){
            if(sessionGive){
                if(permissions != null){
                    for (String p : permissions){
                        if(player.hasPermission(p)){
                            return addTime;
                        }
                    }
                    return 0;
                }else return addTime;
            }else return 0;
        }else {
            sessionTime = currentTime;
            sessionGive=false;
            if(data instanceof Data d && d.nextInterval<=currentTime){
                d.nextInterval = intTime.nextInterval(d.nextInterval);
                sessionGive=true;
                if(permissions != null){
                    for (String p : permissions){
                        if(player.hasPermission(p)){
                            return addTime;
                        }
                    }
                    return 0;
                }else return addTime;
            }else return 0;
        }
    }

    @Override
    public int useTime(long currentTime, PlayerData player) {
        return 0;
    }

    @Override
    public TimeRuleData<IntervalAddTimeRule> fromBytes(byte[] bytes) {
        return new Data(PlayerData.ByteConverter.LONG.fromBytes(bytes));
    }

    @Override
    public byte[] toBytes(TimeRuleData<IntervalAddTimeRule> data) {
        if(data instanceof Data d){
            return PlayerData.ByteConverter.LONG.toBytes(d.nextInterval);
        }
        return new byte[]{};
    }

    @Override
    protected TimeRuleData<IntervalAddTimeRule> getDefaultData() {
        return new Data(intTime.nextInterval(System.currentTimeMillis()));
    }
}
