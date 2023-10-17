package me.simonfoy.hungergames.instance;

import me.simonfoy.hungergames.GameState;
import me.simonfoy.hungergames.HungerGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Game {

    private HungerGames hungerGames;
    private Location hub;
    private Location spawn;
    private GameState state;
    private List<UUID> players;
    private Countdown countdown;
    private HungerGamesGame hungerGamesGame;

    public Game(HungerGames hungerGames) {
        this.hungerGames = hungerGames;
        this.hub = new Location(Bukkit.getWorld("world"), 0, 70, 0);
        this.spawn = new Location(Bukkit.getWorld("world"), -27, 90, 0);
        this.state = GameState.PREPARING;
        this.players = new ArrayList<>();
        this.countdown = new Countdown(hungerGames, this);
        this.hungerGamesGame = new HungerGamesGame(hungerGames, this);
    }

    /* BASE GAME INSTANCE LOGIC */

    public void start() {
        if (state != GameState.PREPARING && state != GameState.PRE_START) {
            return;
        }
        hungerGamesGame.start();
        setState(GameState.IN_PROGRESS);
        sendMessage(ChatColor.YELLOW + "Game is now in IN_PROGRESS State");
    }

    public void end() {
        if (state != GameState.IN_PROGRESS) {
            return;
        }
        setState(GameState.ENDING);
        sendMessage(ChatColor.YELLOW + "Game is now in ENDING State");

        cleanUp();
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
            }
            players.clear();

        }
        sendTitle("", "");
        hungerGamesGame.unregister();
        setState(GameState.PREPARING);
        if (countdown.isRunning()) {
            countdown.stop();
        }
        countdown = new Countdown(hungerGames, this);
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
        player.teleport(spawn);

        if (state.equals(GameState.PREPARING) && players.size() == 1) {
            countdown.start();
            setState(GameState.PRE_START);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        player.teleport(hub);
        player.sendTitle("", "");

        if (state == GameState.PRE_START && players.size() < 1) {
            sendMessage(ChatColor.RED + "There is not enough players. Countdown stopped.");
            reset(false);
            return;
        }

        if (state == GameState.IN_PROGRESS && players.size() < 1) {
            sendMessage(ChatColor.RED + "The game has ended as too many players have left.");
            end();
        }
    }

    public GameState getState() { return state; }
    public List<UUID> getPlayers() { return players; }
    public HungerGamesGame getHungerGamesGame() { return hungerGamesGame; }
    public void setState(GameState state) { this.state = state; }
    public Countdown getCountdown() { return countdown; }
}
