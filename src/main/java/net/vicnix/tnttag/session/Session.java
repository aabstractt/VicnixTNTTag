package net.vicnix.tnttag.session;

import net.vicnix.tnttag.arena.GameArena;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Session {

    private final SessionStorage sessionStorage;
    private final GameArena arena;

    private Boolean tnt = false;

    public Session(SessionStorage sessionStorage, GameArena arena) {
        this.sessionStorage = sessionStorage;

        this.arena = arena;
    }

    public SessionStorage getSessionStorage() {
        return this.sessionStorage;
    }

    public GameArena getArena() {
        return this.arena;
    }

    public Boolean isSpectator() {
        return this.arena.inArenaAsSpectator(this.sessionStorage.getInstance());
    }

    public Boolean isTnt() {
        return this.tnt;
    }

    public void sendMessage(String message) {
        this.getSessionStorage().sendMessage(message);
    }

    public void convertToDefault() {
        this.tnt = false;

        Player instance = this.getSessionStorage().getInstance();

        instance.getInventory().clear();

        instance.setPlayerListName(ChatColor.GREEN + instance.getName());
    }

    public void convertToSpectator() {
        Player instance = this.getSessionStorage().getInstance();

        instance.setWalkSpeed(.1f);

        instance.getInventory().clear();

        instance.setPlayerListName(ChatColor.GRAY + instance.getName());

        instance.setGameMode(GameMode.SPECTATOR);

        instance.getWorld().createExplosion(instance.getLocation(), 1, false);

        this.hideFromAll();

        this.spawnTo((List<Session>) this.arena.getSpectators().values());

        this.arena.addSpectator(this);
        this.arena.removeSession(this.sessionStorage.getInstance());

        this.tnt = false;
    }

    public void convertToTnt() {
        Player instance = this.getSessionStorage().getInstance();

        instance.setWalkSpeed(.5f);

        instance.getInventory().clear();
        instance.getInventory().setHelmet(new ItemStack(Material.TNT));
        instance.getInventory().setItem(0, new ItemStack(Material.TNT, Material.TNT.getMaxStackSize()));

        instance.updateInventory();

        instance.setPlayerListName(ChatColor.RED + instance.getName());

        instance.sendMessage(ChatColor.RED + "¡Ahora eres la tnt! ¡Golpea a alguien antes de que se acabe el tiempo!");

        instance.playSound(instance.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);

        this.tnt = true;
    }

    public void hideFromAll() {
        Bukkit.getOnlinePlayers().forEach(player -> player.hidePlayer(this.sessionStorage.getInstance()));
    }

    public void spawnTo(List<Session> sessions) {
        sessions.forEach(session -> session.getSessionStorage().getInstance().showPlayer(this.sessionStorage.getInstance()));
    }
}