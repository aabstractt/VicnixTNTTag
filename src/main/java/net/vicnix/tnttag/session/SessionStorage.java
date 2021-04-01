package net.vicnix.tnttag.session;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SessionStorage {

    private final String name;
    private final UUID uuid;

    private int gamesPlayed = 0;
    private int wins = 0;
    private int currentWinStreak = 0;

    public SessionStorage(String name, UUID uuid) {
        this.name = name;

        this.uuid = uuid;
    }

    public SessionStorage(String name, UUID uuid, Integer gamesPlayed, Integer wins, Integer currentWinStreak) {
        this(name, uuid);

        this.gamesPlayed = gamesPlayed;

        this.wins = wins;

        this.currentWinStreak = currentWinStreak;
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

    public Integer getGamesPlayed() {
        return this.gamesPlayed;
    }

    public Integer getWins() {
        return this.wins;
    }

    public Integer getCurrentWinStreak() {
        return this.currentWinStreak;
    }

    public void increaseGamesPlayed() {
        this.gamesPlayed++;
    }

    public void increaseWins() {
        this.wins++;
    }

    public void updateCurrentWinStreak() {
        if (this.currentWinStreak > this.wins) return;

        this.currentWinStreak = this.wins;
    }

    public Boolean isConnected() {
        return this.getInstance() != null;
    }

    public void sendMessage(String message) {
        if (!this.isConnected()) return;

        this.getInstance().sendMessage(message);
    }
}