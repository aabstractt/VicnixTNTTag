package net.vicnix.tnttag.session;

import net.md_5.bungee.api.chat.BaseComponent;
import net.vicnix.tnttag.arena.GameArena;
import net.vicnix.tnttag.arena.GameStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class SessionManager {

    private static final SessionManager instance = new SessionManager();

    private final Map<UUID, Session> sessionMap = new HashMap<>();

    private final Scoreboard scoreboard;
    private Objective objective;

    public SessionManager() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public void createSession(Player player) {
        if (this.sessionMap.containsKey(player.getUniqueId())) return;

        Session session = new Session(player.getName(), player.getUniqueId());

        this.sessionMap.put(player.getUniqueId(), session);

        if (GameArena.getInstance().getLobbySpawn() == null) return;

        player.teleport(GameArena.getInstance().getLobbySpawn());

        if (GameArena.getInstance().getStatus() == GameStatus.IN_GAME) {
            session.convertToSpectator();
        }
    }

    public void closeSession(Player player) {
        if (!this.sessionMap.containsKey(player.getUniqueId())) return;

        this.sessionMap.remove(player.getUniqueId());
    }

    public Session getSessionPlayer(Player player) {
        return this.sessionMap.get(player.getUniqueId());
    }

    public void sendLobbyScoreboard(Boolean starting) {
        if (this.objective != null) this.objective.unregister();

        this.objective = this.scoreboard.registerNewObjective("TNTTag", "");

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lTNT TAG"));

        String arenaName = GameArena.getInstance().getArenaName();

        if (arenaName == null) {
            arenaName = "Unknown";
        }

        this.objective.getScore("  ").setScore(13);
        this.objective.getScore(ChatColor.WHITE + "Mapa: " + ChatColor.LIGHT_PURPLE + arenaName).setScore(12);
        this.objective.getScore(ChatColor.WHITE + "Jugadores: " + ChatColor.LIGHT_PURPLE + this.sessionMap.size() + "/28").setScore(11);
        this.objective.getScore(" ").setScore(10);
        this.objective.getScore(ChatColor.WHITE + (starting ? "Empezando en: " + ChatColor.GREEN + GameArena.getInstance().getLobbyCountdown() + "s" : "Esperando")).setScore(9);
        this.objective.getScore("").setScore(8);
        this.objective.getScore(ChatColor.GREEN + "mc.vicnix.net").setScore(7);

        Bukkit.getOnlinePlayers().forEach((p -> p.setScoreboard(this.scoreboard)));
    }

    public void sendGameScoreboard() {
        if (this.objective != null) this.objective.unregister();

        this.objective = this.scoreboard.registerNewObjective("TNTTag", "");

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lTNT TAG"));

        this.objective.getScore(ChatColor.GRAY + "Ronda #1").setScore(14);
        this.objective.getScore("   ").setScore(13);
        this.objective.getScore(ChatColor.YELLOW + "Explosion en: " + ChatColor.YELLOW + "10s").setScore(12);
        // TODO: Score #11 set when the scoreboard is send to the player
        this.objective.getScore("  ").setScore(10);
        this.objective.getScore(ChatColor.WHITE + String.format("Vivos: %s", this.getSessionsAlive().size())).setScore(9);
        this.objective.getScore("").setScore(8);
        this.objective.getScore(ChatColor.GREEN + "mc.vicnix.net").setScore(7);

        Bukkit.getOnlinePlayers().forEach(p -> {
            this.objective.getScore("Objetivo: " + ChatColor.GREEN + p.getName()).setScore(11);

            p.setScoreboard(this.scoreboard);
        });
    }

    public void broadcastMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(message));
    }

    public void broadcastMessage(BaseComponent... baseComponents) {
        Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(baseComponents));
    }

    public List<Session> getSessionsAlive() {
        List<Session> sessions = new ArrayList<>();

        for (Session session : this.sessionMap.values()) {
            if (!session.isAlive()) continue;

            sessions.add(session);
        }

        return sessions;
    }
}