package net.vicnix.tnttag;

import net.vicnix.tnttag.arena.GameLevel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LevelFactory {

    private static final LevelFactory instance = new LevelFactory();

    private final Map<String, GameLevel> levels = new HashMap<>();

    public static LevelFactory getInstance() {
        return instance;
    }

    public void init() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(TNTTag.getInstance().getDataFolder() + "/levels.yml"));

        for (String folderName : config.getKeys(false)) {
            this.loadLevel(folderName, (ConfigurationSection) config.get(folderName));
        }
    }

    public void loadLevel(String folderName, ConfigurationSection section) {
        this.levels.put(folderName, new GameLevel(folderName, (Location) section.get("lobbySpawn"), (Location) section.get("worldSpawn")));
    }

    public GameLevel loadLevel(String folderName, Map<String, Object> data) {
        return this.levels.put(folderName, new GameLevel(folderName, (Location) data.get("lobbySpawn"), (Location) data.get("worldSpawn")));
    }

    public Map<String, GameLevel> getLevels() {
        return this.levels;
    }

    public GameLevel getLevelWorld(World world) {
        return this.levels.get(world.getName());
    }

    public void saveLevels() {
        File file = new File(TNTTag.getInstance().getDataFolder() + "/levels.yml");

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        this.levels.forEach((folderName, level) -> config.set(folderName, new HashMap<String, Object>() {{
            put("lobbySpawn", level.getLobbySpawn());
            put("worldSpawn", level.getWorldSpawn());
        }}));

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameLevel getRandomLevel() {
        GameLevel betterLevel = null;

        for (GameLevel level : this.levels.values()) {
            if (betterLevel == null) {
                betterLevel = level;

                continue;
            }

            if (ArenaFactory.getInstance().getArenasByLevel(level).size() > ArenaFactory.getInstance().getArenasByLevel(betterLevel).size()) continue;

            betterLevel = level;
        }

        return betterLevel;
    }
}