package net.vicnix.tnttag.arena;

import net.vicnix.tnttag.TNTTag;
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

    private int lobbyCountdown = 60;
    private int tntCountdown = 60;

    public static GameArena getInstance() {
        return instance;
    }

    public void init() throws ArenaException {
        FileConfiguration configuration = TNTTag.getInstance().getConfig();

        if (!configuration.contains("arenaName") ||
            !configuration.contains("lobbySpawn") ||
            !configuration.contains("worldSpawn")
        ) {
            throw new ArenaException("Arena data not found");
        }

        this.arenaName = configuration.getString("arenaName");

        this.lobbySpawn = (Location) configuration.get("lobbySpawn");
        this.worldSpawn = (Location) configuration.get("worldSpawn");

        Bukkit.getScheduler().runTaskTimer(TNTTag.getInstance(), () -> GameArena.getInstance().tickGame(), 0, 20);

        TNTTag.getInstance().getLogger().info("Arena loaded... Waiting for players");
    }

    public void findPlayers() {
        List<Session> sessionsAlive = SessionManager.getInstance().getSessionsAlive();

        int taggers = 1;

        while (taggers <= (sessionsAlive.size() / 2)) {
            System.out.println(sessionsAlive.size() + " DIVIDE: " + sessionsAlive.size() / 2 + ", TAGGERS: "  + taggers);

            Random random = new Random();

            int index = random.nextInt(sessionsAlive.size());

            Session session = sessionsAlive.get(index);

            if (session == null || session.isTnt()) continue;

            session.convertToTnt();

            taggers++;
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
        Boolean starting = SessionManager.getInstance().getSessionsAlive().size() >= TNTTag.getInstance().getMinPlayers();

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
    }

    private void tickIngame() {
        SessionManager.getInstance().sendGameScoreboard();

        if (this.tntCountdown == 0) {
            for (Session session : SessionManager.getInstance().getSessions().values()) {
                if (!session.isTnt()) continue;

                session.convertToSpectator();

                SessionManager.getInstance().broadcastMessage(ChatColor.RED + session.getName() + ChatColor.YELLOW + " ha explotado!");
            }

            if (SessionManager.getInstance().getSessionsAlive().size() == 0) {
                this.setStatus(GameStatus.RESTARTING);

                SessionManager.getInstance().broadcastMessage(ChatColor.LIGHT_PURPLE + SessionManager.getInstance().getWinner().getName() + ChatColor.GREEN + " ha ganado el juego!");

                return;
            }

            this.findPlayers();

            this.currentRound++;

            this.tntCountdown = 60;

            return;
        }

        this.tntCountdown--;
    }
}