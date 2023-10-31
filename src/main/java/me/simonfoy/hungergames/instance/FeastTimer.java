package me.simonfoy.hungergames.instance;

import me.simonfoy.hungergames.HungerGames;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class FeastTimer extends BukkitRunnable {

    private HungerGames hungerGames;
    private Game game;

    private boolean isRunning = false;

    private int feastTimerSeconds;

    public FeastTimer(HungerGames hungerGames, Game game) {
        this.hungerGames = hungerGames;
        this.game = game;
        this.feastTimerSeconds = 900;
    }

    public void start() {
        game.sendMessage(ChatColor.RED + "The feast will spawn in 15 minutes!");
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
        if (feastTimerSeconds == 0) {
            isRunning = false;
            cancel();
            game.sendMessage(ChatColor.RED + "The feast has spawned!");
            return;
        }

        if (feastTimerSeconds <= 10) {
            game.sendMessage(ChatColor.GREEN + "Game will start in " + feastTimerSeconds + " second" + (feastTimerSeconds == 1 ? "" : "s") + ".");
        }

        game.sendTitle(ChatColor.GREEN.toString() + feastTimerSeconds + " second" + (feastTimerSeconds == 1 ? "" : "s"), ChatColor.GRAY + "until game starts");

        hungerGames.getGame().getScoreBoardManager().updateFeastTimer();

        feastTimerSeconds--;
    }

    public int getFeastTimerSeconds() {
        return feastTimerSeconds;
    }
    public boolean isRunning() {
        return isRunning;
    }
}
