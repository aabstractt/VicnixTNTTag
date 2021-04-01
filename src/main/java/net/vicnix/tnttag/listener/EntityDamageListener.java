package net.vicnix.tnttag.listener;

import net.vicnix.tnttag.arena.GameArena;
import net.vicnix.tnttag.arena.GameStatus;
import net.vicnix.tnttag.session.Session;
import net.vicnix.tnttag.session.SessionManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent ev) {
        ev.setDamage(0);

        if (!(ev.getEntity() instanceof Player) || !(ev.getDamager() instanceof Player)) return;

        if (GameArena.getInstance().getStatus() != GameStatus.IN_GAME) {
            ev.setCancelled(true);

            return;
        }

        Session session = SessionManager.getInstance().getSessionPlayer((Player) ev.getEntity());

        if (session == null) return;

        Session target = SessionManager.getInstance().getSessionPlayer((Player) ev.getDamager());

        if (target == null) return;

        if (!target.isTnt()) return;

        target.convertToDefault();

        session.convertToTnt();

        target.sendMessage(ChatColor.YELLOW + "Le has pasado la tnt a " + session.getName());
        session.sendMessage(ChatColor.RED + "Has sido golpeado por " + target.getName());
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onEntityDamageEvent(EntityDamageEvent ev) {
        if (!(ev instanceof EntityDamageByEntityEvent)) {
            ev.setCancelled(true);
        }
    }
}