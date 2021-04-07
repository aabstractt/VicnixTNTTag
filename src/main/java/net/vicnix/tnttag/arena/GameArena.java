package net.vicnix.tnttag.arena;

import net.md_5.bungee.api.chat.BaseComponent;
import net.vicnix.tnttag.TNTTag;
import net.vicnix.tnttag.provider.MongoDBProvider;
import net.vicnix.tnttag.session.Session;
import net.vicnix.tnttag.session.SessionStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class GameArena {

    private final Integer id;
    private final GameLevel level;
    private final Scoreboard scoreboard;
    private Objective objective;
    private final BukkitTask task;

    private final Map<UUID, Session> sessions = new HashMap<>();
    private final Map<UUID, Session> spectators = new HashMap<>();

    private GameStatus status = GameStatus.WAITING;

    private int currentRound = 1;

    private int lobbyCountdown = 20;
    private int tntCountdown = 60;

    public GameArena(Integer id, GameLevel level) {
        this.id = id;

        this.level = level;

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        this.task = Bukkit.getScheduler().runTaskTimer(TNTTag.getInstance(), this::tickGame, 0, 20);
    }

    public Integer getId() {
        return this.id;
    }

    public GameLevel getLevel() {
        return this.level;
    }

    public String getWorldName() {
        return "Match-" + this.id;
    }

    public BukkitTask getTask() {
        return this.task;
    }

    public void findPlayers() {
        List<Session> sessionsAlive = this.getSessionsAlive();

        int taggers = 0;

        while (taggers < this.getTntCounts()) {
            Random random = new Random();

            int index = random.nextInt(sessionsAlive.size());

            Session session = sessionsAlive.get(index);

            if (session == null || session.isTnt()) continue;

            session.convertToTnt();

            taggers++;

            System.out.println(sessionsAlive.size() + " DIVIDE: " + this.getTntCounts() + ", TAGGERS: "  + taggers);
        }
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Integer getLobbyCountdown() {
        return this.lobbyCountdown;
    }

    public Integer getTntCountdown() {
        return this.tntCountdown;
    }

    public Integer getCurrentRound() {
        return this.currentRound;
    }

    public Map<UUID, Session> getSessions() {
        return this.sessions;
    }

    public void tickGame() {
        if (this.status == GameStatus.WAITING) {
            this.tickWaiting();

            return;
        }

        if (this.status == GameStatus.IN_GAME) {
            this.tickIngame();
        }
    }

    private void tickWaiting() {
        Boolean starting = this.sessions.size() >= TNTTag.getInstance().getMinPlayers();

        this.sendLobbyScoreboard(starting);

        if (starting) {
            // TODO: lobbyCountdown not is a variable
            if (lobbyCountdown == 60 || lobbyCountdown == 50 || lobbyCountdown == 40 || lobbyCountdown == 30 || lobbyCountdown == 20 || lobbyCountdown == 10 || (lobbyCountdown > 0 && lobbyCountdown < 6)) {
                this.broadcastMessage(ChatColor.GREEN + "Iniciando juego en " + ChatColor.LIGHT_PURPLE + this.lobbyCountdown + ChatColor.GREEN + " segundos");
            }

            if (this.lobbyCountdown == 0) {
                this.setStatus(GameStatus.IN_GAME);

                this.startGame();

                return;
            }

            this.lobbyCountdown--;
        }
    }

    private void startGame() {
        Bukkit.getOnlinePlayers().forEach(p -> p.teleport(this.level.getWorldSpawn()));

        this.findPlayers();

        this.sessions.values().forEach(session -> session.getSessionStorage().increaseGamesPlayed());
    }

    private void tickIngame() {
        this.sendGameScoreboard();

        if (this.tntCountdown == 0) {
            for (Session session : this.sessions.values()) {
                if (!session.isTnt()) continue;

                session.convertToSpectator();

                this.broadcastMessage(ChatColor.RED + session.getSessionStorage().getName() + ChatColor.YELLOW + " ha explotado!");
            }

            this.findPlayers();

            this.currentRound++;

            this.tntCountdown = 60;

            return;
        }

        Session winner = this.getWinner();

        if (winner == null && this.sessions.size() <= 0) {
            this.broadcastMessage("Ningun sobreviviente encontrado");

            this.setStatus(GameStatus.RESTARTING);

            return;
        } else if (winner != null) {
            winner.getSessionStorage().increaseWins();

            this.broadcastMessage(ChatColor.LIGHT_PURPLE + winner.getSessionStorage().getName() + ChatColor.GREEN + " ha ganado el juego!");

            this.setStatus(GameStatus.RESTARTING);

            return;
        }

        this.tntCountdown--;
    }

    public Integer getTntCounts() {
        int sessionsPlaying = this.sessions.size();

        if (sessionsPlaying == 30) {
            return 6;
        } else if (sessionsPlaying > 24) {
            return 5;
        } else if (sessionsPlaying > 19) {
            return 4;
        } else if (sessionsPlaying > 15) {
            return 3;
        } else if (sessionsPlaying > 13) {
            return 2;
        }

        return 1;
    }

    public void addSession(Player player) {
        SessionStorage sessionStorage = MongoDBProvider.getInstance().loadSessionStorage(player.getName(), player.getUniqueId());

        if (sessionStorage == null) {
            sessionStorage = new SessionStorage(player.getName(), player.getUniqueId());
        }

        this.sessions.put(player.getUniqueId(), new Session(sessionStorage, this));
    }

    public void removeSession(Player player) {
        if (!this.inArenaAsSession(player)) return;

        SessionStorage sessionStorage = this.sessions.remove(player.getUniqueId()).getSessionStorage();

        Bukkit.getScheduler().runTaskAsynchronously(TNTTag.getInstance(), () -> MongoDBProvider.getInstance().saveSessionStorage(sessionStorage));
    }

    public void addSpectator(Session session) {
        if (this.inArenaAsSpectator(session.getSessionStorage().getInstance())) return;

        this.spectators.put(session.getSessionStorage().getUniqueId(), session);
    }

    public Map<UUID, Session> getSpectators() {
        return this.spectators;
    }

    public Session getSessionPlayer(Player player) {
        if (this.inArenaAsSession(player)) {
            return this.sessions.get(player.getUniqueId());
        }

        if (this.inArenaAsSpectator(player)) {
            return this.spectators.get(player.getUniqueId());
        }

        return null;
    }

    public Map<UUID, Session> getEveryone() {
        return new HashMap<UUID, Session>() {{
            putAll(GameArena.this.sessions);
            putAll(GameArena.this.spectators);
        }};
    }

    public void forceRemove(Player player) {
        if (this.inArenaAsSession(player)) {
            this.removeSession(player);

            return;
        }

        if (this.inArenaAsSpectator(player)) {
            // TODO: Remove player from spectator
        }
    }

    public Boolean inArena(Player player) {
        return this.inArenaAsSession(player) || this.inArenaAsSpectator(player);
    }

    public Boolean inArenaAsSession(Player player) {
        return this.sessions.containsKey(player.getUniqueId());
    }

    public Boolean inArenaAsSpectator(Player player) {
        return this.spectators.containsKey(player.getUniqueId());
    }

    public void sendLobbyScoreboard(Boolean starting) {
        if (this.objective != null) this.objective.unregister();

        this.objective = this.scoreboard.registerNewObjective("TNTTag", "");

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lTNT TAG"));

        this.objective.getScore("  ").setScore(13);
        this.objective.getScore(ChatColor.WHITE + "Mapa: " + ChatColor.LIGHT_PURPLE + this.level.getFolderName()).setScore(12);
        this.objective.getScore(ChatColor.WHITE + "Jugadores: " + ChatColor.LIGHT_PURPLE + this.sessions.size() + "/28").setScore(11);
        this.objective.getScore(" ").setScore(10);
        this.objective.getScore(ChatColor.WHITE + (starting ? "Empezando en: " + ChatColor.GREEN + this.getLobbyCountdown() + "s" : "Esperando")).setScore(9);
        this.objective.getScore("").setScore(8);
        this.objective.getScore(ChatColor.GREEN + "mc.vicnix.net").setScore(7);

        Bukkit.getOnlinePlayers().forEach((p -> p.setScoreboard(this.scoreboard)));
    }

    public void sendGameScoreboard() {
        if (this.objective != null) this.objective.unregister();

        this.objective = this.scoreboard.registerNewObjective("TNTTag", "");

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lTNT TAG"));

        this.objective.getScore(ChatColor.GRAY + "Ronda #" + this.getCurrentRound()).setScore(14);
        this.objective.getScore("   ").setScore(13);
        this.objective.getScore(ChatColor.YELLOW + "Explosión en: " + ChatColor.YELLOW + this.getTntCountdown() + "s").setScore(12);
        // TODO: Score #11 set when the scoreboard is send to the player
        this.objective.getScore("  ").setScore(10);
        this.objective.getScore(ChatColor.WHITE + String.format("Vivos: %s", this.getSessionsAlive().size())).setScore(9);
        this.objective.getScore("").setScore(8);
        this.objective.getScore(ChatColor.GREEN + "mc.vicnix.net").setScore(7);

        for (Session session : this.getEveryone().values()) {
            if (!session.isSpectator()) {
                this.objective.getScore("Objetivo: " + (session.isTnt() ? ChatColor.RED + "¡Golpea a alguien!" : ChatColor.GREEN + "¡Huye!")).setScore(11);
            }

            session.getSessionStorage().getInstance().setScoreboard(this.scoreboard);
        }
    }

    public void broadcastMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(message));
    }

    public void broadcastMessage(BaseComponent... baseComponents) {
        Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(baseComponents));
    }

    public List<Session> getSessionsAlive() {
        List<Session> sessions = new ArrayList<>();

        for (Session session : this.sessions.values()) {
            if (!session.isTnt()) {
                sessions.add(session);
            }
        }

        return sessions;
    }

    public Session getWinner() {
        if (this.sessions.size() != 1) {
            return null;
        }

        return this.sessions.values().iterator().next();
    }
}