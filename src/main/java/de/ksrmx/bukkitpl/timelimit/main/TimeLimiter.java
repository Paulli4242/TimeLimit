package de.ksrmx.bukkitpl.timelimit.main;

import de.ksrmx.bukkitpl.timelimit.Messages;
import de.ksrmx.bukkitpl.timelimit.TimeLimitPlugin;
import de.ksrmx.bukkitpl.timelimit.timerules.FirstJoinAddTimeRule;
import de.ksrmx.bukkitpl.timelimit.timerules.IJoinTimeRule;
import de.ksrmx.bukkitpl.timelimit.timerules.TimeRule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.ccffee.utils.io.Data;
import org.ccffee.utils.io.DataLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class TimeLimiter implements Runnable{


    protected File saveFile;
    protected Messages messages;
    protected TimeLimitPlugin plugin;
    protected ArrayList<TimeRule<?>> rulesList;//init
    protected HashSet<IJoinTimeRule> joinRulesSet;
    protected PriorityQueue<KickTime> kickTimeQueue;
    protected HashMap<UUID, PlayerData> uuidPlayTimeMap;
    protected HashSet<UUID> gotKicked = new HashSet<>();
    protected int task;

    public TimeLimiter(File saveFile, TimeLimitPlugin plugin){
        this.saveFile = saveFile;
        this.plugin = plugin;
    }

    public void init(Collection<? extends Player> onlinePlayers, ArrayList<TimeRule<?>> rulesList, int updateInterval, Messages messages) {
        this.messages = messages;
        if(kickTimeQueue==null){
            joinRulesSet = new HashSet<>();
            this.rulesList = rulesList;

            for(TimeRule<?> rule : rulesList)
                if(rule instanceof IJoinTimeRule j)joinRulesSet.add(j);
            int size = onlinePlayers.size();
            kickTimeQueue = new PriorityQueue<>(Math.max(size, 1));
            try {
                loadData();
            } catch (IOException e) {
                uuidPlayTimeMap = new HashMap<>();
                for (TimeRule<?> r : rulesList){
                    r.initDefaultData();
                    if(r instanceof FirstJoinAddTimeRule q) {
                        try{
                            Field f = q.getClass().getDeclaredField("time");
                            f.setAccessible(true);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
                plugin.getLogger().log(Level.WARNING,"Could not load data (Normal on first start)",e);
            }
            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,this,0,updateInterval* 20L);
        }else throw new IllegalStateException("Already initialized TimeLimiter");
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        if(kickTimeQueue!=null && rulesList !=null && uuidPlayTimeMap!=null){
            for(PlayerData t : uuidPlayTimeMap.values()){
                long add = 0;
                boolean use = true;
                OfflinePlayer p = t.getOfflinePlayer();
                for(TimeRule<?> r : rulesList){
                    add += r.getTimeAddAmount(currentTime,t);
                    int i = r.useTime(currentTime,t);
                    if(i!=0){
                        use = i>0;
                    }
                }
                KickTime k = t.getKickTime();
                if(p.isOnline()){
                    if(use){
                        t.setPlayableTime(t.getPlayableTime()+add);
                        if(k!=null&&add!=0){
                            kickTimeQueue.remove(k);
                            k.setKickTime(k.getKickTime()+add);
                            kickTimeQueue.add(k);
                        }else if(k==null){
                            k = new KickTime(p.getPlayer(),t,currentTime+t.getPlayableTime()-t.getTimePlayed());
                            t.setKickTime(k);
                        }
                    }else {
                        if(k!=null){
                            kickTimeQueue.remove(k);
                            t.setTimePlayed(currentTime+t.getPlayableTime()-k.getKickTime());
                            t.setKickTime(null);
                        }
                        t.setPlayableTime(t.getPlayableTime()+add);
                    }
                }else{
                    t.setPlayableTime(t.getPlayableTime()+add);
                }
            }

            gotKicked.clear();
            KickTime kickTime = kickTimeQueue.peek();
            if(kickTime!=null)
            while (kickTime!=null && kickTime.getKickTime()<=currentTime){
                kickTimeQueue.poll();
                PlayerData pt = kickTime.getPlayTime();
                pt.setKickTime(null);
                pt.setTimePlayed(currentTime+pt.getPlayableTime()-kickTime.getKickTime());
                gotKicked.add(kickTime.getPlayer().getUniqueId());
                kickTime.getPlayer().kickPlayer(messages.buildMessage("time-limit.kick"));
                kickTime = kickTimeQueue.peek();
            }

        }else throw new IllegalStateException("Not initialized yet");
    }

    public void disable() {
        Bukkit.getScheduler().cancelTask(task);
        try {
            saveData();
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING,"Could not save data",e);
        }
    }

    public void onPlayerQuit(PlayerQuitEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        PlayerData pt = uuidPlayTimeMap.get(uuid);
        if(!gotKicked.remove(uuid)){
            KickTime kt = pt.getKickTime();
            if(kt!=null){
                pt.setTimePlayed(System.currentTimeMillis()+pt.getPlayableTime()-kt.getKickTime());
                kickTimeQueue.remove(kt);
                pt.setKickTime(null);
            }
        }
        pt.updatePermissions(e.getPlayer());
    }

    public void onPlayerJoin(PlayerJoinEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        PlayerData pd = uuidPlayTimeMap.get(uuid);
        long currentTime = System.currentTimeMillis();
        if(pd==null){
            pd = new PlayerData(uuid, new OfflinePlayerData(),0,0);
            long add = 0;
            for(IJoinTimeRule jtr : joinRulesSet){
                add+=jtr.getTimeAddOnJoin(currentTime,pd);
            }
            uuidPlayTimeMap.put(uuid,pd);
            pd.setPlayableTime(add);
        }else pd.updateOfflinePlayer();
    }

    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e){
        PlayerData pd = uuidPlayTimeMap.get(e.getUniqueId());
        if(pd!=null&&pd.getPlayableTime()<=pd.getTimePlayed()){
            long currentTime = System.currentTimeMillis();
            for(TimeRule<?> r : rulesList){
                if(r.useTime(currentTime,pd)<0)
                    return;
            }
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,"Time is up");
        }
    }

    protected void loadData() throws IOException {
        Data[][] data = DataLoader.loadData(saveFile);
        if(data.length==2){

            PlayerData.ByteConverter bc = new PlayerData.ByteConverter();
            uuidPlayTimeMap = new HashMap<>(data[1].length);
            for(Data d : data[0]){
                PlayerData pt = d.get(bc);
                uuidPlayTimeMap.put(pt.getUuid(),pt);
            }


            for(Data d : data[1]){
                String header = TimeRule.loadHeader(d.toByteArray());
                for (TimeRule<?> r : rulesList) {
                    if (r.getHeader().equals(header)) {
                        r.initData(d.toByteArray());
                        break;
                    }
                }
            }

            for (TimeRule<?> r : rulesList){
                if(!r.isInitialized()){
                    r.initDefaultData();
                }
            }

        }else plugin.getLogger().log(Level.INFO,"Could not find any data. Creating new File on Save.");
    }



    protected void saveData() throws IOException {
        Data[] ptd = new Data[uuidPlayTimeMap.size()];
        int i = 0;
        PlayerData.ByteConverter bc = new PlayerData.ByteConverter();
        for(PlayerData pt : uuidPlayTimeMap.values()){
            ptd[i++] = Data.from(bc,pt);
        }

        Data[] trd = new Data[rulesList.size()];
        i=0;
        for (TimeRule<?> tr : rulesList){
            byte[] b = tr.saveData();
            if(b.length>0)
                trd[i++] = new Data(tr.saveData());
        }
        DataLoader.save(new Data[][]{ptd,Arrays.copyOf(trd,i)},saveFile);
    }

    public PlayTime getPlayTime(UUID uuid){
        return uuidPlayTimeMap.get(uuid);
    }

    public void updateKickTimeQueue(KickTime kickTime){
        kickTimeQueue.remove(kickTime);
        kickTimeQueue.add(kickTime);
    }

}
