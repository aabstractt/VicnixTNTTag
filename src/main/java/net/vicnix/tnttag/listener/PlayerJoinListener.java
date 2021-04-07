package net.vicnix.tnttag.listener;

import net.vicnix.tnttag.ArenaFactory;
import net.vicnix.tnttag.TNTTag;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerJoinEvent(PlayerJoinEvent ev) {
        ev.setJoinMessage(null);

        Bukkit.getScheduler().runTaskAsynchronously(TNTTag.getInstance(), () -> ArenaFactory.getInstance().joinArena(ev.getPlayer()));
    }
}