package net.vicnix.tnttag.arena;

import net.vicnix.tnttag.TNTTag;
import net.vicnix.tnttag.provider.RedisServer;
import net.vicnix.tnttag.session.Session;
import net.vicnix.tnttag.session.SessionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Random;

public class GameArena {

    private static final GameArena instance = new GameArena();

    private String arenaName;

    private Location lobbySpawn;
    private Location worldSpawn;

    private GameStatus status = GameStatus.WAITING;

    private int currentRound = 1;

    private int lobbyCountdown = 20;
    private int tntCountdown = 60;

    public static GameArena getInstance() {
        return instance;
    }

    public void init() {
        FileConfiguration configuration = TNTTag.getInstance().getConfig();

        if (!configuration.contains("arenaName") ||
            !configuration.contains("lobbySpawn") ||
            !configuration.contains("worldSpawn")
        ) {
            TNTTag.getInstance().getLogger().warning("Arena data not found");

            return;
        }

        this.arenaName = configuration.getString("arenaName");

        this.lobbySpawn = (Location) configuration.get("lobbySpawn");
        this.worldSpawn = (Location) configuration.get("worldSpawn");

        Bukkit.getScheduler().runTaskTimer(TNTTag.getInstance(), () -> GameArena.getInstance().tickGame(), 0, 20);

        TNTTag.getInstance().getLogger().info("Arena loaded... Waiting for players");

        RedisServer.getInstance().startServer();
    }

    public void findPlayers() {
        List<Session> sessionsAlive = SessionManager.getInstance().getSessionsAlive();

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

    public String getArenaName() {
        return this.arenaName;
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

    public Integer getLobbyCountdown() {
        return this.lobbyCountdown;
    }

    public Integer getTntCountdown() {
        return this.tntCountdown;
    }

    public Integer getCurrentRound() {
        return this.currentRound;
    }

    public void tickGame() {
        if (this.arenaName == null) return;

        if (this.status == GameStatus.WAITING) {
            this.tickWaiting();

            return;
        }

        if (this.status == GameStatus.IN_GAME) {
            this.tickIngame();
        }
    }

    private void tickWaiting() {
        Boolean starting = SessionManager.getInstance().getSessionsPlaying().size() >= TNTTag.getInstance().getMinPlayers();

        SessionManager.getInstance().sendLobbyScoreboard(starting);

        if (starting) {
            // TODO: lobbyCountdown not is a variable
            if (lobbyCountdown == 60 || lobbyCountdown == 50 || lobbyCountdown == 40 || lobbyCountdown == 30 || lobbyCountdown == 20 || lobbyCountdown == 10 || (lobbyCountdown > 0 && lobbyCountdown < 6)) {
                SessionManager.getInstance().broadcastMessage(ChatColor.GREEN + "Iniciando juego en " + ChatColor.LIGHT_PURPLE + this.lobbyCountdown + ChatColor.GREEN + " segundos");
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
        Bukkit.getOnlinePlayers().forEach(p -> p.teleport(this.getWorldSpawn()));

        this.findPlayers();

        SessionManager.getInstance().getSessionsPlaying().forEach(session -> session.getSessionStorage().increaseGamesPlayed());
    }

    private void tickIngame() {
        SessionManager.getInstance().sendGameScoreboard();

        if (this.tntCountdown == 0) {
            for (Session session : SessionManager.getInstance().getSessionsPlaying()) {
                if (!session.isTnt()) continue;

                session.convertToSpectator();

                SessionManager.getInstance().broadcastMessage(ChatColor.RED + session.getSessionStorage().getName() + ChatColor.YELLOW + " ha explotado!");
            }

            this.findPlayers();

            this.currentRound++;

            this.tntCountdown = 60;

            return;
        }

        Session winner = SessionManager.getInstance().getWinner();

        if (winner == null && SessionManager.getInstance().getSessionsPlaying().size() <= 0) {
            SessionManager.getInstance().broadcastMessage("Ningun sobreviviente encontrado");

            this.setStatus(GameStatus.RESTARTING);

            return;
        } else if (winner != null) {
            winner.getSessionStorage().increaseWins();

            SessionManager.getInstance().broadcastMessage(ChatColor.LIGHT_PURPLE + winner.getSessionStorage().getName() + ChatColor.GREEN + " ha ganado el juego!");

            this.setStatus(GameStatus.RESTARTING);

            return;
        }

        this.tntCountdown--;
    }

    public Integer getTntCounts() {
        int sessionsPlaying = SessionManager.getInstance().getSessionsPlaying().size();

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

    public Boolean wasLoaded() {
        return this.arenaName != null && this.lobbySpawn != null && this.worldSpawn != null;
    }
}