package net.vicnix.tnttag;

import net.vicnix.tnttag.arena.GameArena;
import net.vicnix.tnttag.arena.GameLevel;
import net.vicnix.tnttag.arena.GameStatus;
import net.vicnix.tnttag.session.Session;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaFactory {

    private static final ArenaFactory instance = new ArenaFactory();

    private final Map<Integer, GameArena> arenas = new HashMap<>();

    private int gamesPlayed = 0;

    public static ArenaFactory getInstance() {
        return instance;
    }

    public GameArena createArena() {
        GameLevel level = LevelFactory.getInstance().getRandomLevel();

        if (level == null) {
            return null;
        }

        GameArena arena = new GameArena(this.gamesPlayed++, level);

        return this.arenas.put(arena.getId(), arena);
    }

    public List<GameArena> getArenasByLevel(GameLevel level) {
        List<GameArena> arenas = new ArrayList<>();

        for (GameArena arena : this.arenas.values()) {
            if (!arena.getLevel().getFolderName().equalsIgnoreCase(level.getFolderName())) continue;

            arenas.add(arena);
        }

        return arenas;
    }

    public GameArena getRandomArena() {
        GameArena betterArena = null;

        for (GameArena arena : this.arenas.values()) {
            if (arena.getStatus() != GameStatus.WAITING) continue;

            if (arena.getSessions().size() >= TNTTag.getInstance().getMaxPlayers()) continue;

            if (betterArena == null) {
                betterArena = arena;

                continue;
            }

            if (arena.getSessions().size() > betterArena.getSessions().size()) continue;

            betterArena = arena;
        }

        return betterArena;
    }

    public GameArena getArena(Player player) {
        for (GameArena arena : this.arenas.values()) {
            if (!arena.inArena(player)) continue;

            return arena;
        }

        return null;
    }

    public GameArena getArena(World world) {
        for (GameArena arena : this.arenas.values()) {
            if (!arena.getWorldName().equals(world.getName())) continue;

            return arena;
        }

        return null;
    }

    public Session getSessionPlayer(Player player) {
        GameArena arena = this.getArena(player);

        if (arena == null) {
            return null;
        }

        return arena.getSessionPlayer(player);
    }

    public void joinArena(Player player) {
        if (this.getArena(player) != null) {
            player.sendMessage("Ya te encuentras en una arena...");

            return;
        }

        GameArena arena = this.getRandomArena();

        if (arena == null) {
            TNTTag.getInstance().getLogger().info("Arena not found... Creating new arena");

            arena = this.createArena();
        }

        if (arena == null) {
            if (!player.isOp()) {
                player.kickPlayer("TNTTag games not available...");
            }

            return;
        }

        arena.addSession(player);

        player.teleport(arena.getLevel().getLobbySpawn());

        player.setGameMode(GameMode.SURVIVAL);

        player.getInventory().clear();

        player.setPlayerListName(ChatColor.GRAY + player.getName());
    }

    public void removePlayer(Player player) {
        GameArena arena = this.getArena(player);

        if (arena == null) return;

        arena.forceRemove(player);
    }
}