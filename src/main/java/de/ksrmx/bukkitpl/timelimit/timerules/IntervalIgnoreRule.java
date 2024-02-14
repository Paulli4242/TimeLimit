package de.ksrmx.bukkitpl.timelimit.timerules;

import de.ksrmx.bukkitpl.timelimit.main.PlayerData;
import de.ksrmx.bukkitpl.timelimit.time.IntervalTime;
import de.ksrmx.bukkitpl.timelimit.time.IntervalTimeFormat;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class IntervalIgnoreRule extends TimeRule<IntervalIgnoreRule> {

    @Override
    protected TimeRuleData<IntervalIgnoreRule> getDefaultData() {
        return new Data(fromTime.nextInterval(System.currentTimeMillis()),toTime.nextInterval(System.currentTimeMillis()));
    }

    protected static class Data extends TimeRuleData<IntervalIgnoreRule>{

        public Data(long fromTime, long toTime) {
            this.fromTime = fromTime;
            this.toTime = toTime;
        }

        long fromTime;
        long toTime;

    }

    protected IntervalType interval;
    protected IntervalTimeFormat timeFormat;
    protected IntervalTime fromTime;
    protected IntervalTime toTime;
    protected List<String> permissions;

    protected long sessionTime;
    protected boolean sessionIgnore;

    public IntervalIgnoreRule(String name, IntervalType interval, IntervalTimeFormat timeFormat,
                              IntervalTime fromTime, IntervalTime toTime, @Nullable List<String> permissions) {
        super(name);
        this.interval=interval;
        this.timeFormat = timeFormat;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.permissions = permissions;
    }

    @Override
    public long getTimeAddAmount(long currentTime, PlayerData data) {
        return 0;
    }

    @Override
    public int useTime(long currentTime, PlayerData player) {
        if(currentTime==sessionTime){
            if(sessionIgnore){
                if(permissions != null){
                    for (String p : permissions){
                        if(player.hasPermission(p)){
                            return -1;
                        }
                    }
                    return 0;
                }else return -1;
            }else return 0;
        }else if(data instanceof Data d) {
            sessionTime = currentTime;
            sessionIgnore = false;
            if(d.toTime<currentTime){
                d.fromTime = fromTime.nextInterval((d.fromTime));
                d.toTime = toTime.nextInterval(d.toTime);
                return 0;
            }else if(d.fromTime<currentTime){
                sessionIgnore=true;
                if(permissions != null){
                    for (String p : permissions){
                        if(player.hasPermission(p)){
                            return -1;
                        }
                    }
                    return 0;
                }else return -1;
            }else return 0;
        }else return 0;
    }

    @Override
    public TimeRuleData<IntervalIgnoreRule> fromBytes(byte[] bytes) {
        return new Data(
                PlayerData.ByteConverter.LONG.fromBytes(Arrays.copyOf(bytes,8)),
                PlayerData.ByteConverter.LONG.fromBytes(Arrays.copyOfRange(bytes,8,16))
        );
    }

    @Override
    public byte[] toBytes(TimeRuleData<IntervalIgnoreRule> intervalIgnoreRuleTimeRuleData) {
        if(intervalIgnoreRuleTimeRuleData instanceof Data d){
            byte[] bts = new byte[16];
            System.arraycopy(PlayerData.ByteConverter.LONG.toBytes(d.fromTime),0, bts,0,8);
            System.arraycopy(PlayerData.ByteConverter.LONG.toBytes(d.toTime),0, bts,8,8);
            return bts;
        }
        return new byte[0];
    }

}
