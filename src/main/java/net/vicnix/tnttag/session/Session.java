package net.vicnix.tnttag.session;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Session {

    private final String name;
    private final UUID uuid;

    private Boolean spectator = false;
    private Boolean tnt = false;

    public Session(String name, UUID uuid) {
        this.name = name;

        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public Player getInstance() {
        return Bukkit.getServer().getPlayer(this.getUniqueId());
    }

    public Boolean isConnected() {
        return this.getInstance() != null;
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
        if (!this.isConnected()) return;

        this.getInstance().sendMessage(message);
    }

    public void sendMessage(BaseComponent... baseComponents) {
        if (!this.isConnected()) return;

        this.getInstance().spigot().sendMessage(baseComponents);
    }

    public void convertToDefault() {
        this.tnt = false;

        Player instance = this.getInstance();

        instance.getInventory().clear();

        instance.setPlayerListName(ChatColor.GREEN + instance.getName());
    }

    public void convertToSpectator() {
        this.spectator = true;

        this.tnt = false;
    }

    public void convertToTnt() {
        Player instance = this.getInstance();

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