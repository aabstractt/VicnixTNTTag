package net.vicnix.tnttag.arena;

import net.vicnix.tnttag.TNTTag;
import org.bukkit.configuration.file.FileConfiguration;

public class GameArena {

    private static final GameArena instance = new GameArena();

    private String arenaName;
    private String worldName;

    public static GameArena getInstance() {
        return instance;
    }

    public void init() throws ArenaException {
        FileConfiguration configuration = TNTTag.getInstance().getConfig();

        if (!configuration.contains("arenaName") || configuration.contains("worldName")) {
            throw new ArenaException("Arena name not found");
        }

        this.arenaName = configuration.getString("arenaName");

        this.worldName = configuration.getString("worldName");

        TNTTag.getInstance().getLogger().info("Arena loaded... Waiting for players");
    }

    public String getArenaName() {
        return this.arenaName;
    }

    public String getWorldName() {
        return this.worldName;
    }
}