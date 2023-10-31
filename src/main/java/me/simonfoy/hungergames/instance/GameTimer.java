package me.simonfoy.hungergames.instance;

import me.simonfoy.hungergames.HungerGames;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {

    private HungerGames hungerGames;
    private Game game;

    private boolean isRunning = false;

    private int gameTimer;

    public GameTimer(HungerGames hungerGames, Game game) {
        this.hungerGames = hungerGames;
        this.game = game;
        this.gameTimer = 0;
    }

    public void start() {
        isRunning = true;
        runTaskTimer(hungerGames, 0, 20);
    }

    public void stop() {
        if (isRunning) {
            cancel();
            isRunning = false;
        }
    }

    @Override
    public void run() {
        if (gameTimer == 2700) {
            isRunning = false;
            cancel();
            game.sendMessage(ChatColor.RED + "The game has ended!");
            game.end();
            return;
        }

        if (gameTimer == 2475) {
            game.sendMessage(ChatColor.RED + "The game will end in 5 minutes!");
        }

        if (gameTimer >= 120) {
            hungerGames.getGame().getScoreBoardManager().updateGameTimer();
        }

        gameTimer++;
    }

    public int getTimerSeconds() {
        return gameTimer;
    }
    public boolean isRunning() {
        return isRunning;
    }
}

