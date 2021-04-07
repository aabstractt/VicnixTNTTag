package net.vicnix.tnttag.listener;

import net.vicnix.tnttag.ArenaFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlockListener implements Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockBreakEvent(BlockBreakEvent ev) {
        if (ArenaFactory.getInstance().getArena(ev.getBlock().getWorld()) == null) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockPlaceEvent(BlockPlaceEvent ev) {
        if (ArenaFactory.getInstance().getArena(ev.getBlock().getWorld()) == null) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockUpdateEvent(BlockBurnEvent ev) {
        if (ArenaFactory.getInstance().getArena(ev.getBlock().getWorld()) == null) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockExplodeEvent(BlockExplodeEvent ev) {
        if (ArenaFactory.getInstance().getArena(ev.getBlock().getWorld()) == null) return;

        ev.setYield(0);

        ev.blockList().clear();

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void oBlockSpreadEvent(BlockSpreadEvent ev) {
        if (ArenaFactory.getInstance().getArena(ev.getBlock().getWorld()) == null) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockFadeEvent(BlockFadeEvent ev) {
        if (ArenaFactory.getInstance().getArena(ev.getBlock().getWorld()) == null) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockFormEvent(BlockFromToEvent ev) {
        if (ArenaFactory.getInstance().getArena(ev.getBlock().getWorld()) == null) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onBlockFromToEvent(BlockFromToEvent ev) {
        if (ArenaFactory.getInstance().getArena(ev.getBlock().getWorld()) == null) return;

        ev.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onLeavesDecayEvent(LeavesDecayEvent ev) {
        if (ArenaFactory.getInstance().getArena(ev.getBlock().getWorld()) == null) return;

        ev.setCancelled(true);
    }
}