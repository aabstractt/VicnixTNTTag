package net.vicnix.tnttag.listener;

import net.vicnix.tnttag.ArenaFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerListener implements Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent ev) {
        if (ArenaFactory.getInstance().getArena(ev.getEntity().getWorld()) == null) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerDropItemEvent(PlayerDropItemEvent ev) {
        if (ArenaFactory.getInstance().getArena(ev.getPlayer().getWorld()) == null) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onInventoryClickEvent(InventoryClickEvent ev) {
        if (ArenaFactory.getInstance().getArena(ev.getWhoClicked().getWorld()) == null) return;

        ev.setCancelled(true);
    }
}