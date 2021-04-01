package net.vicnix.tnttag.session;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Session {

    private final SessionStorage sessionStorage;

    private Boolean spectator = false;
    private Boolean tnt = false;

    public Session(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    public SessionStorage getSessionStorage() {
        return this.sessionStorage;
    }

    public Boolean isSpectator() {
        return this.spectator;
    }

    public Boolean isTnt() {
        return this.tnt;
    }

    public Boolean isAlive() {
        return !this.isTnt() && !this.isSpectator();
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

        instance.getInventory().clear();

        instance.setPlayerListName(ChatColor.GREEN + instance.getName());

        instance.setGameMode(GameMode.SPECTATOR);

        instance.getWorld().createExplosion(instance.getLocation(), 1, false);

        this.spectator = true;

        this.tnt = false;
    }

    public void convertToTnt() {
        Player instance = this.getSessionStorage().getInstance();

        instance.setWalkSpeed(.5f);

        instance.getInventory().clear();
        instance.getInventory().setHelmet(new ItemStack(Material.TNT));
        instance.getInventory().setItem(0, new ItemStack(Material.TNT, 576));

        instance.updateInventory();

        instance.setPlayerListName(ChatColor.RED + instance.getName());

        instance.sendMessage(ChatColor.RED + "¡Ahora eres la tnt! ¡Golpea a alguien antes de que se acabe el tiempo!");

        instance.playSound(instance.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);

        this.tnt = true;
    }
}