package net.vicnix.tnttag.session;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Session {

    private final String name;
    private final UUID uuid;

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

    public void initTagged() {
        Player instance = this.getInstance();

        instance.setWalkSpeed(.5f);

        instance.getInventory().clear();
        instance.getInventory().setHelmet(new ItemStack(Material.TNT));
        instance.getInventory().setItem(0, new ItemStack(Material.TNT, 576));
    }
}