package de.ksrmx.bukkitpl.timelimit.main;

public interface PlayTime {
    long getTime();
    boolean setTime(TimeLimiter timeLimiter, long time);
    boolean addTime(TimeLimiter timeLimiter, long time);
    boolean removeTime(TimeLimiter timeLimiter, long time);
}
