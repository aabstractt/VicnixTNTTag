package net.vicnix.tnttag;

import net.vicnix.tnttag.arena.ArenaException;
import net.vicnix.tnttag.arena.GameArena;
import net.vicnix.tnttag.command.TNTTagCommand;
import net.vicnix.tnttag.listener.EntityDamageListener;
import net.vicnix.tnttag.listener.PlayerJoinListener;
import net.vicnix.tnttag.listener.PlayerQuitListener;
import org.bukkit.plugin.java.JavaPlugin;

public class TNTTag extends JavaPlugin {

    private static TNTTag instance;

    public static TNTTag getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        try {
            GameArena.getInstance().init();
        } catch (ArenaException e) {
            this.getLogger().warning(e.getMessage());
        }

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);

        this.getServer().getPluginCommand("tnttag").setExecutor(new TNTTagCommand());
    }

    public Integer getMinPlayers() {
        return 1;
    }
}