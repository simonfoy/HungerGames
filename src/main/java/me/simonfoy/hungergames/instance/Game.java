package me.simonfoy.hungergames.instance;

import me.simonfoy.hungergames.GameState;
import me.simonfoy.hungergames.HungerGames;
import me.simonfoy.hungergames.instance.kit.Kit;
import me.simonfoy.hungergames.instance.kit.KitType;
import me.simonfoy.hungergames.instance.kit.type.ArcherKit;
import me.simonfoy.hungergames.instance.kit.type.BeastmasterKit;
import me.simonfoy.hungergames.instance.kit.type.GrandpaKit;
import me.simonfoy.hungergames.manager.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Game {

    private HungerGames hungerGames;
    private Location hub;
    private Location spawn;
    private GameState state;
    private List<UUID> players;
    private int requiredPlayers;
    private HashMap<UUID, Kit> kits;
    private Countdown countdown;
    private GameTimer gameTimer;
    private InvincibilityTimer invincibilityTimer;
    private FeastTimer feastTimer;
    private HungerGamesGame hungerGamesGame;
    private ScoreboardManager scoreBoardManager;

    public Game(HungerGames hungerGames) {
        this.hungerGames = hungerGames;
        this.hub = new Location(Bukkit.getWorld("world"), 0, 70, 0);
        this.spawn = new Location(Bukkit.getWorld("world"), 0, 250, 0);
        this.state = GameState.PREPARING;
        this.players = new ArrayList<>();
        this.requiredPlayers = 24;
        this.kits = new HashMap<>();
        this.countdown = new Countdown(hungerGames, this);
        this.gameTimer = new GameTimer(hungerGames, this);
        this.invincibilityTimer = new InvincibilityTimer(hungerGames, this);
        this.feastTimer = new FeastTimer(hungerGames, this);
        this.hungerGamesGame = new HungerGamesGame(hungerGames, this);
        this.scoreBoardManager = new ScoreboardManager(hungerGames);
    }

    /* BASE GAME INSTANCE LOGIC */

    public void start() {
        if (state != GameState.PREPARING && state != GameState.PRE_START) {
            return;
        }
        hungerGamesGame.start();
        setState(GameState.IN_PROGRESS);

        for (Player players : Bukkit.getOnlinePlayers()) {
            getScoreBoardManager().clearScoreboard(players);
            getScoreBoardManager().setupGameStartScoreboard(players);
        }
        sendMessage(ChatColor.YELLOW + "Game is now in IN_PROGRESS State");
    }

    public void end() {
        if (state != GameState.IN_PROGRESS) {
            return;
        }
        setState(GameState.ENDING);
        sendMessage(ChatColor.YELLOW + "Game is now in ENDING State");
        sendMessage(ChatColor.RED + "Game will reset in 10 seconds...");

        Bukkit.getScheduler().scheduleSyncDelayedTask(hungerGames, new Runnable() {
            public void run() {
                cleanUp();
            }
        }, 200L);
    }

    public void cleanUp() {
        if (state != GameState.ENDING) {
            return;
        }
        setState(GameState.CLEANING_UP);
        sendMessage(ChatColor.YELLOW + "Game is now in CLEANING_UP State");

        reset(true);
    }

    public void reset(boolean removePlayers) {
        if (removePlayers) {
            for (UUID uuid : players) {
                Player player = Bukkit.getPlayer(uuid);
                player.teleport(hub);
                removeKit(player.getUniqueId());
            }
            players.clear();
        }
        kits.clear();
        sendTitle("", "");
        hungerGamesGame.unregister();
        setState(GameState.PREPARING);

        for (Player players : Bukkit.getOnlinePlayers()) {
            getScoreBoardManager().clearScoreboard(players);
            getScoreBoardManager().setupGamePreparingScoreboard(players);
        }

        if (countdown.isRunning()) {
            countdown.stop();
        }

        if (gameTimer.isRunning()) {
            gameTimer.stop();
        }

        if (invincibilityTimer.isRunning()) {
            invincibilityTimer.stop();
        }

        if (feastTimer.isRunning()) {
            feastTimer.stop();
        }
        countdown = new Countdown(hungerGames, this);
        gameTimer = new GameTimer(hungerGames, this);
        invincibilityTimer = new InvincibilityTimer(hungerGames, this);
        feastTimer = new FeastTimer(hungerGames, this);
        hungerGamesGame = new HungerGamesGame(hungerGames, this);
    }

    /* UTILITIES */

    public void restart() {
        int countdownTime = 10;
        new BukkitRunnable() {
            int remaining = countdownTime;

            @Override
            public void run() {
                if (remaining > 0) {
                    Bukkit.broadcastMessage(ChatColor.RED + "Server is restarting in " + remaining + " seconds...");
                    remaining--;
                } else {
                    cancel();
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
                }
            }
        }.runTaskTimer(hungerGames, 0L, 20L);
    }

    public void sendMessage(String message) {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }

    public void sendTitle(String title, String subtitle) {
        for (UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendTitle(title, subtitle);
        }
    }

    /* PLAYER LOGIC */

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.teleport(spawn);
        player.closeInventory();
        player.getInventory().clear();
        getScoreBoardManager().clearScoreboard(player);
        getScoreBoardManager().setupGamePreparingScoreboard(player);
        player.sendMessage(ChatColor.GREEN + "Choose your kit with /kit!");
        kits.put(player.getUniqueId(), new ArcherKit(hungerGames, player.getUniqueId()));

        if (state.equals(GameState.PREPARING) && players.size() == requiredPlayers) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                getScoreBoardManager().clearScoreboard(players);
                getScoreBoardManager().setupGamePreStartScoreboard(players);
            }
            countdown.start();
            setState(GameState.PRE_START);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        getScoreBoardManager().updatePlayerCounter();
        player.teleport(hub);
        getScoreBoardManager().clearScoreboard(player);
        player.sendTitle("", "");

        removeKit(player.getUniqueId());

        if (state == GameState.PRE_START && players.size() < requiredPlayers) {
            sendMessage(ChatColor.RED + "There is not enough players. Countdown stopped.");
            reset(false);
            return;
        }

        if (state == GameState.IN_PROGRESS && players.size() < requiredPlayers) {
            sendMessage(ChatColor.RED + "The game has ended as too many players have left.");
            end();
        }
    }

    public Location getSpawn() { return spawn; }
    public GameState getState() { return state; }
    public List<UUID> getPlayers() { return players; }
    public int getRequiredPlayers() { return requiredPlayers; }
    public HungerGamesGame getHungerGamesGame() { return hungerGamesGame; }
    public Countdown getCountdown() { return countdown; }
    public GameTimer getGameTimer() { return gameTimer; }
    public InvincibilityTimer getInvincibilityTimer() { return invincibilityTimer; }
    public FeastTimer getFeastTimer() { return feastTimer; }
    public ScoreboardManager getScoreBoardManager() { return scoreBoardManager; }
    public void setState(GameState state) { this.state = state; }
    public HashMap<UUID, Kit> getKits() { return kits; }

    public void removeKit(UUID uuid) {
        if (kits.containsKey(uuid)) {
            kits.get(uuid).remove();
            kits.remove(uuid);
        }
    }
    public void setKit(UUID uuid, KitType type) {
        removeKit(uuid);
        if (type == KitType.ARCHER) {
            kits.put(uuid, new ArcherKit(hungerGames, uuid));
        } else if (type == KitType.BEASTMASTER) {
            kits.put(uuid, new BeastmasterKit(hungerGames, uuid));
        } else if (type == KitType.GRANDPA) {
            kits.put(uuid, new GrandpaKit(hungerGames, uuid));
        }
    }

    public KitType getKitType(Player player) {
        return kits.containsKey(player.getUniqueId()) ? kits.get(player.getUniqueId()).getType() : null;
    }
}
