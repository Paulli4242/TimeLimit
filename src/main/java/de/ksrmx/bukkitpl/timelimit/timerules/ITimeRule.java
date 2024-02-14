package de.ksrmx.bukkitpl.timelimit.timerules;

import de.ksrmx.bukkitpl.timelimit.main.PlayerData;

public interface ITimeRule {
    long getTimeAddAmount(long currentTime, PlayerData data);
    int useTime(long currentTime, PlayerData data);
}
