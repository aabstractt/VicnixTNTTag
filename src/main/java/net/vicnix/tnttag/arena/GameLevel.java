package net.vicnix.tnttag.arena;

import net.vicnix.tnttag.LevelFactory;
import org.bukkit.Location;

public class GameLevel {

    private final String folderName;

    private Location lobbySpawn;
    private Location worldSpawn;

    public GameLevel(String folderName, Location lobbySpawn, Location worldSpawn) {
        this.folderName = folderName;

        this.lobbySpawn = lobbySpawn;

        this.worldSpawn = worldSpawn;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public Location getLobbySpawn() {
        return this.lobbySpawn;
    }

    public Location getWorldSpawn() {
        return this.worldSpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public void setWorldSpawn(Location worldSpawn) {
        this.worldSpawn = worldSpawn;
    }

    public void handleUpdate() {
        LevelFactory.getInstance().saveLevels();
    }
}