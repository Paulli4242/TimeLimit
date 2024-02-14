package de.ksrmx.bukkitpl.timelimit.main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    private final TimeLimiter limiter;

    public EventListener(TimeLimiter limiter){
        this.limiter = limiter;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        limiter.onPlayerJoin(e);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        limiter.onPlayerQuit(e);
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e){
        limiter.onPlayerPreLogin(e);
    }

}
