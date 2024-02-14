package de.ksrmx.bukkitpl.timelimit.main;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.Permissible;
import de.ksrmx.libs.utils.io.ByteUtils;

import java.util.UUID;

public class PlayerData implements PlayTime {

    private OfflinePlayer offlinePlayer;
    private final UUID uuid;
    private final OfflinePlayerData opd;
    private KickTime kickTime;
    private long timePlayed;
    private long playableTime;


    public PlayerData(UUID uuid, OfflinePlayerData opd, long timePlayed, long playableTime) {
        this.uuid = uuid;
        this.opd = opd;
        this.timePlayed = timePlayed;
        this.playableTime = playableTime;
    }

    public OfflinePlayer getOfflinePlayer() {
        if(offlinePlayer==null){
            updateOfflinePlayer();
        }
        return offlinePlayer;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(long timePlayed) {
        this.timePlayed = timePlayed;
    }

    public void updateTimePlayed(){
        if(kickTime!=null)
            timePlayed = System.currentTimeMillis()+playableTime-kickTime.getKickTime();
    }

    public long getPlayableTime() {
        return playableTime;
    }

    public void setPlayableTime(long playableTime) {
        this.playableTime = playableTime;
    }

    @SuppressWarnings("unused")
    public void setOfflinePlayer(OfflinePlayer offlinePlayer) {
        this.offlinePlayer = offlinePlayer;
    }

    public KickTime getKickTime() {
        return kickTime;
    }

    public void setKickTime(KickTime kickTime) {
        this.kickTime = kickTime;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void updateOfflinePlayer(){
        offlinePlayer=Bukkit.getOfflinePlayer(uuid);
    }

    @SuppressWarnings({"unused","ConstantConditions"})
    public boolean isPermissionSet(String name) {
        return offlinePlayer.isOnline()?offlinePlayer.getPlayer().isPermissionSet(name): opd.isPermissionSet(name);
    }

    @SuppressWarnings("ConstantConditions")
    public boolean hasPermission(String name) {
        return offlinePlayer.isOnline()?offlinePlayer.getPlayer().hasPermission(name): opd.hasPermission(name);
    }

    public void updatePermissions(Permissible p) {
        opd.updatePermissions(p);
    }


    @Override
    public long getTime() {
        updateTimePlayed();
        return playableTime-timePlayed;
    }

    @Override
    public boolean setTime(TimeLimiter timeLimiter, long time) {
        if(time>-1){
            updateTimePlayed();
            playableTime=timePlayed+time;
            if(kickTime!=null){
                kickTime.setKickTime(System.currentTimeMillis()+time);
                timeLimiter.updateKickTimeQueue(kickTime);
            }
            return true;
        }else return false;
    }

    @Override
    public boolean addTime(TimeLimiter timeLimiter, long time) {
        if(time>-1){
            playableTime+=time;
            if(kickTime!=null){
                kickTime.setKickTime(kickTime.getKickTime()+time);
                timeLimiter.updateKickTimeQueue(kickTime);
            }
            return true;
        }else return false;
    }

    @Override
    public boolean removeTime(TimeLimiter timeLimiter, long time) {
        if(time>-1){
            playableTime-=time;
            if(kickTime!=null){
                kickTime.setKickTime(kickTime.getKickTime()-time);
                timeLimiter.updateKickTimeQueue(kickTime);
            }
            return true;
        }else return false;
    }

    public static final class ByteConverter implements de.ksrmx.libs.utils.io.ByteConverter<PlayerData> {

        @Override
        public PlayerData fromBytes(byte[] bytes) {
            byte[][] matrix = ByteUtils.unpack(bytes);
            System.out.println(matrix.length);
            if(matrix.length!=4)throw new IllegalArgumentException("unable to deserialize");
            return new PlayerData(
                    UUID.fromString(ByteConverter.STRING.fromBytes(matrix[0])),
                    OfflinePlayerData.fromBytes(matrix[3]),
                    ByteConverter.LONG.fromBytes(matrix[1]),
                    ByteConverter.LONG.fromBytes(matrix[2])
            );
        }

        @Override
        public byte[] toBytes(PlayerData d) {

            return ByteUtils.pack(
                    ByteConverter.STRING.toBytes(d.offlinePlayer.getUniqueId().toString()),
                    ByteConverter.LONG.toBytes(d.timePlayed),
                    ByteConverter.LONG.toBytes(d.playableTime),
                    OfflinePlayerData.toBytes(d.opd)
            );
        }
    }
}

