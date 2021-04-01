package net.vicnix.tnttag.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerListener implements Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerDropItemEvent(PlayerDropItemEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onInventoryClickEvent(InventoryClickEvent ev) {
        ev.setCancelled(true);
    }
}
