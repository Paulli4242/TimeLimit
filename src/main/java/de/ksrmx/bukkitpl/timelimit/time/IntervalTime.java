package de.ksrmx.bukkitpl.timelimit.time;

import de.ksrmx.bukkitpl.timelimit.timerules.IntervalType;

public class IntervalTime {

    protected IntervalType type;

    @SuppressWarnings("unused")
    protected int year,month,day,hour,minute,second;

    public IntervalTime(IntervalType type, int year, int month, int day, int hour, int minute, int second) {
        this.type = type;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public long nextInterval(long time){
        long out = 0;
        switch (type){
            case WEEKLY->{
                out = buildInterval((((time/86400000)+3)/7)*7-4);
                if(out<time)out = buildInterval((((time/86400000)+3)/7)*7+3);
            }
            case DAILY->{
                out = buildInterval(time/86400000);
                if(out<time) out = buildInterval(time/86400000+1);
            }
            case HOURLY->{
                out = buildInterval(time/3600000);
                if(out<time) out = buildInterval(time/3600000+1);
            }
            case MINUTELY->{
                out = buildInterval(time/60000);
                if(out<time) out = buildInterval(time/60000+1);
            }
            case SECONDLY->out = buildInterval(time/1000+1);
        }
        return out;
    }
    public long buildInterval(long start){
        switch (type){
            case MONTHLY:
            case WEEKLY:
                start+=day;
            case DAILY:
                start=start*24+hour;
            case HOURLY:
                start=start*60+minute;
            case MINUTELY:
                start=start*60+second;
        }
        return start*1000;
    }
}
