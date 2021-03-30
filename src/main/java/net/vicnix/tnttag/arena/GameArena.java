package net.vicnix.tnttag.arena;

import net.vicnix.tnttag.TNTTag;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class GameArena {

    private static final GameArena instance = new GameArena();

    private String arenaName;
    private String worldName;

    private Location lobbySpawn;
    private Location worldSpawn;

    private GameStatus status = GameStatus.WAITING;

    public static GameArena getInstance() {
        return instance;
    }

    public void init() throws ArenaException {
        FileConfiguration configuration = TNTTag.getInstance().getConfig();

        if (!configuration.contains("arenaName") ||
            !configuration.contains("worldName") ||
            !configuration.contains("lobbySpawn") ||
            !configuration.contains("worldSpawn")
        ) {
            throw new ArenaException("Arena data not found");
        }

        this.arenaName = configuration.getString("arenaName");
        this.worldName = configuration.getString("worldName");

        this.lobbySpawn = (Location) configuration.get("lobbySpawn");
        this.worldSpawn = (Location) configuration.get("worldSpawn");

        TNTTag.getInstance().getLogger().info("Arena loaded... Waiting for players");
    }

    public String getArenaName() {
        return this.arenaName;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public Location getLobbySpawn() {
        return this.lobbySpawn;
    }

    public Location getWorldSpawn() {
        return this.worldSpawn;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public void tickGame() {
        if (this.arenaName == null || this.worldName == null) {
            return;
        }

        if (this.status == GameStatus.WAITING) {
            this.tickWaiting();

            return;
        }

        if (this.status == GameStatus.IN_GAME) {
            this.tickIngame();
        }
    }

    private void tickWaiting() {

    }

    private void tickIngame() {

    }
}