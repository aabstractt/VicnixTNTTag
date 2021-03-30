package net.vicnix.tnttag.listener;

import net.vicnix.tnttag.session.SessionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerQuitEvent(PlayerQuitEvent ev) {
        ev.setQuitMessage(null);

        SessionManager.getInstance().closeSession(ev.getPlayer());
    }
}
