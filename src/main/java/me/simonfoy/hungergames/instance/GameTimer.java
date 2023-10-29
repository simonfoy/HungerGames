package me.simonfoy.hungergames.instance;

import me.simonfoy.hungergames.GameState;
import me.simonfoy.hungergames.HungerGames;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {

    private HungerGames hungerGames;
    private Game game;

    private boolean isRunning = false;

    private int timerSeconds;

    public GameTimer(HungerGames hungerGames, Game game) {
        this.hungerGames = hungerGames;
        this.game = game;
        this.timerSeconds = 120;
    }

    public void start() {
        game.sendMessage(ChatColor.RED + "Everyone is invincible for 2 minutes.");
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
        if (timerSeconds == 0) {
            isRunning = false;
            cancel();
            return;
        }

        if (timerSeconds <= 10) {
            game.sendMessage(ChatColor.GREEN + "Game will start in " + timerSeconds + " second" + (timerSeconds == 1 ? "" : "s") + ".");
        }

        game.sendTitle(ChatColor.GREEN.toString() + timerSeconds + " second" + (timerSeconds == 1 ? "" : "s"), ChatColor.GRAY + "until game starts");

        hungerGames.getGame().getScoreBoardManager().updateTimer();

        timerSeconds--;
    }

    public int getTimerSeconds() {
        return timerSeconds;
    }
    public boolean isRunning() {
        return isRunning;
    }
}
