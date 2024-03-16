package de.ksrmx.bukkitpl.timelimit.time;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;

public class DynamicTimeSpanFormatter {

    private final EnumSet<TimeSpanUnit> usingUnits;
    private final int unitAmount;

    public DynamicTimeSpanFormatter(int unitAmount, EnumSet<TimeSpanUnit> usingUnits) {
        this.usingUnits = usingUnits;
        this.unitAmount = unitAmount;
    }

    public DynamicTimeSpanFormatter(int unitAmount,TimeSpanUnit firstUnit, TimeSpanUnit... restUnits) {
        this.usingUnits = EnumSet.of(firstUnit, restUnits);
        this.unitAmount = unitAmount;
    }

    public String format(long ms){
        int i = 0;
        StringBuilder format = new StringBuilder();

        Iterator<TimeSpanUnit> it = usingUnits.iterator();
        TimeSpanUnit tsu = TimeSpanUnit.MILLISECOND;
        while (it.hasNext()&&i<unitAmount){
            tsu = it.next();
            long amount = ms / tsu.getDivisor();
            if(amount!=0){
                ms = ms % tsu.getDivisor();
                format.append(" ").append(amount).append(tsu.getUnit(amount));
                i++;
            }
        }
        if(format.isEmpty()) {
            long amount = ms/tsu.getDivisor();
            format.append(amount).append(tsu.getUnit(amount));
        }
        return format.toString();
    }


}
