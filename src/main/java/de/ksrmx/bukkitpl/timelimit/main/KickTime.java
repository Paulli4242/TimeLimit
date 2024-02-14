package de.ksrmx.bukkitpl.timelimit.main;

import org.bukkit.entity.Player;

public class KickTime implements Comparable<KickTime> {

    private final Player player;
    private final PlayerData playTime;
    private long kickTime;

    public KickTime(Player player, PlayerData playTime, long kickTime) {
        this.player = player;
        this.playTime = playTime;
        this.kickTime = kickTime;
    }


    public Player getPlayer() {
        return player;
    }

    public long getKickTime() {
        return kickTime;
    }

    public void setKickTime(long kickTime) {
        this.kickTime = kickTime;
    }

    public PlayerData getPlayTime() {
        return playTime;
    }

    @Override
    public int compareTo(KickTime o) {
        long diff = kickTime-o.kickTime;
        if(diff>Integer.MAX_VALUE)return Integer.MAX_VALUE;
        else if(diff<Integer.MIN_VALUE)return Integer.MIN_VALUE+1;
        else return  (int) diff;
    }
}
