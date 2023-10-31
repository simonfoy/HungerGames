package me.simonfoy.hungergames.instance;

import me.simonfoy.hungergames.HungerGames;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class InvincibilityTimer extends BukkitRunnable {

    private HungerGames hungerGames;
    private Game game;

    private boolean isRunning = false;

    private int invincibilityTimerSeconds;

    public InvincibilityTimer(HungerGames hungerGames, Game game) {
        this.hungerGames = hungerGames;
        this.game = game;
        this.invincibilityTimerSeconds = 120;
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
        if (invincibilityTimerSeconds == 0) {
            isRunning = false;
            cancel();
            game.sendMessage(ChatColor.RED + "Invincibility is no longer active. Good luck!");
//            game.getFeastTimer().start();
            return;
        }

        if (invincibilityTimerSeconds <= 10) {
            game.sendMessage(ChatColor.GREEN + "Game will start in " + invincibilityTimerSeconds + " second" + (invincibilityTimerSeconds == 1 ? "" : "s") + ".");
        }

        game.sendTitle(ChatColor.GREEN.toString() + invincibilityTimerSeconds + " second" + (invincibilityTimerSeconds == 1 ? "" : "s"), ChatColor.GRAY + "until game starts");

        hungerGames.getGame().getScoreBoardManager().updateInvincibilityTimer();

        invincibilityTimerSeconds--;
    }

    public int getTimerSeconds() {
        return invincibilityTimerSeconds;
    }
    public boolean isRunning() {
        return isRunning;
    }
}
