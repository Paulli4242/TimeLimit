package de.ksrmx.bukkitpl.timelimit.timerules;

import static de.ksrmx.bukkitpl.timelimit.time.IntervalTimeFormat.Component.*;

public enum IntervalType {

    SECONDLY(0),
    MINUTELY(SECOND.FLAG),
    HOURLY(SECOND.FLAG | MINUTE.FLAG),
    DAILY(SECOND.FLAG | MINUTE.FLAG | HOUR.FLAG),
    WEEKLY(SECOND.FLAG | MINUTE.FLAG | HOUR.FLAG | WEEK_DAY.FLAG),
    MONTHLY(SECOND.FLAG | MINUTE.FLAG | HOUR.FLAG | DAY.FLAG),
    @SuppressWarnings("unused")
    YEARLY(SECOND.FLAG | MINUTE.FLAG | HOUR.FLAG | DAY.FLAG | MONTH.FLAG);



    private final int usableFormat;

    IntervalType(int usableFormat){
        this.usableFormat = usableFormat;
    }

    public int getUsableFormat() {
        return usableFormat;
    }
}
