package de.ksrmx.bukkitpl.timelimit.timerules;

import de.ksrmx.bukkitpl.timelimit.main.PlayerData;

public interface IJoinTimeRule extends ITimeRule {

    long getTimeAddOnJoin(long currentTime, PlayerData data);

}
