package net.vicnix.tnttag;

import net.vicnix.tnttag.command.TNTTagCommand;
import net.vicnix.tnttag.listener.*;
import net.vicnix.tnttag.provider.MongoDBProvider;
import net.vicnix.tnttag.provider.RedisServer;
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

        LevelFactory.getInstance().init();
        MongoDBProvider.getInstance().init();

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        this.getServer().getPluginCommand("tnttag").setExecutor(new TNTTagCommand());
    }

    public void onDisable() {
        RedisServer.getInstance().closeServer();
    }

    public Integer getMinPlayers() {
        return 1;
    }

    public Integer getMaxPlayers() {
        return 24;
    }
}