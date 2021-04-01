package net.vicnix.tnttag.listener;

import net.vicnix.tnttag.arena.GameArena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlockListener implements Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockBreakEvent(BlockBreakEvent ev) {
        if (!GameArena.getInstance().wasLoaded()) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockPlaceEvent(BlockPlaceEvent ev) {
        if (!GameArena.getInstance().wasLoaded()) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockUpdateEvent(BlockBurnEvent ev) {
        if (!GameArena.getInstance().wasLoaded()) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockExplodeEvent(BlockExplodeEvent ev) {
        if (!GameArena.getInstance().wasLoaded()) return;

        ev.setYield(0);

        ev.blockList().clear();

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void oBlockSpreadEvent(BlockSpreadEvent ev) {
        if (!GameArena.getInstance().wasLoaded()) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockFadeEvent(BlockFadeEvent ev) {
        if (!GameArena.getInstance().wasLoaded()) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockFormEvent(BlockFromToEvent ev) {
        if (!GameArena.getInstance().wasLoaded()) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockFromToEvent(BlockFromToEvent ev) {
        if (!GameArena.getInstance().wasLoaded()) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onLeavesDecayEvent(LeavesDecayEvent ev) {
        if (!GameArena.getInstance().wasLoaded()) return;

        ev.setCancelled(true);
    }
}