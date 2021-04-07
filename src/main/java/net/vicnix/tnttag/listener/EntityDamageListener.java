package net.vicnix.tnttag.listener;

import net.vicnix.tnttag.ArenaFactory;
import net.vicnix.tnttag.arena.GameStatus;
import net.vicnix.tnttag.session.Session;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamageListener implements Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    public void onEntityDamageEvent(EntityDamageEvent ev) {
        if (!(ev.getEntity() instanceof Player)) return;

        Session session = ArenaFactory.getInstance().getSessionPlayer((Player) ev.getEntity());

        if (session == null) return;

        if (session.getArena().getStatus() != GameStatus.IN_GAME) {
            ev.setCancelled(true);

            return;
        }

        if (ev.getCause() != DamageCause.ENTITY_ATTACK && ev.getCause() != DamageCause.FIRE && ev.getCause() != DamageCause.FIRE_TICK) {
            ev.setCancelled(true);

            return;
        }

        ev.setDamage(0);

        if (!(ev instanceof EntityDamageByEntityEvent)) return;

        if (!(((EntityDamageByEntityEvent) ev).getDamager() instanceof Player)) return;

        Session target = ArenaFactory.getInstance().getSessionPlayer((Player) ((EntityDamageByEntityEvent) ev).getDamager());

        if (target == null) return;

        if (!target.isTnt()) return;

        target.convertToDefault();

        session.convertToTnt();

        target.sendMessage(ChatColor.YELLOW + "Le has pasado la tnt a " + session.getSessionStorage().getName());
        session.sendMessage(ChatColor.RED + "Has sido golpeado por " + target.getSessionStorage().getName());
    }
}