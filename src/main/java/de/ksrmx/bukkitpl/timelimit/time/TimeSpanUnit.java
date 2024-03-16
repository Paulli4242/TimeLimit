package de.ksrmx.bukkitpl.timelimit.time;


public enum TimeSpanUnit {

    CENTURY(3_155_760_000_000L," Century"," Centuries"),
    DECADE(315_576_000_000L," Decade"," Decades"),
    YEAR(31_557_600_000L," year"," years"),
    MONTH(2_629_800_000L, " month", " months"),
    WEEK(604_800_000L," week"," weeks"),
    DAY(86_400_000L,"d"),
    HOUR(3_600_000L,"h"),
    MINUTE(60_000L,"min"),
    SECOND(1_000L, "s"),
    MILLISECOND(1, "ms")
    ;




    private final long divisor;
    private final String unitSingular;
    private final String unitPlural;


    TimeSpanUnit(long divisor, String unitSingular) {
        this.divisor = divisor;
        this.unitSingular = unitSingular;
        this.unitPlural = unitSingular;
    }

    TimeSpanUnit(long divisor, String unitSingular, String unitPlural) {
        this.divisor = divisor;
        this.unitSingular = unitSingular;
        this.unitPlural = unitPlural;
    }

    public long getDivisor() {
        return divisor;
    }

    public String getUnitSingular() {
        return unitSingular;
    }

    public String getUnitPlural() {
        return unitPlural;
    }

    public String getUnit(long amount){
        return amount==1?unitSingular:unitPlural;
    }

}
