package net.vicnix.tnttag;

import net.vicnix.tnttag.arena.ArenaException;
import net.vicnix.tnttag.arena.GameArena;
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

            this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

            this.getServer().getScheduler().runTaskTimer(this, () -> GameArena.getInstance().tickGame(), 0, 20);
        } catch (ArenaException e) {
            this.getLogger().warning(e.getMessage());
        }
    }
}