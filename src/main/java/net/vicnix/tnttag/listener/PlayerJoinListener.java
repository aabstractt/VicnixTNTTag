package net.vicnix.tnttag.listener;

import net.vicnix.tnttag.session.SessionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerJoinEvent(PlayerJoinEvent ev) {
        ev.setJoinMessage(null);

        SessionManager.getInstance().createSession(ev.getPlayer());
    }
}