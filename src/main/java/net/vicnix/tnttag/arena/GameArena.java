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

    private int lobbyCountdown = 60;

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

            if (session == null) continue;

            if (session.isTnt()) continue;

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

    public int getLobbyCountdown() {
        return this.lobbyCountdown;
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
            if (this.lobbyCountdown > 0) {
                SessionManager.getInstance().broadcastMessage(ChatColor.GREEN + "Iniciando juego en " + ChatColor.LIGHT_PURPLE + this.lobbyCountdown + ChatColor.GREEN + " segundos");
            } else {
                this.setStatus(GameStatus.IN_GAME);

                this.startGame();
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
    }
}