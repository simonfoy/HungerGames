package me.simonfoy.hungergames.manager;

import me.simonfoy.hungergames.HungerGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreboardManager {

    private HungerGames hungerGames;

    public ScoreboardManager(HungerGames hungerGames) {
        this.hungerGames = hungerGames;
    }

    public void setupGamePreparingScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Preparing_HungerGames", "dummy",
                ChatColor.YELLOW + "" + ChatColor.BOLD + "HUNGER GAMES");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = formatter.format(today);
        Score date = objective.getScore(ChatColor.GRAY + formattedDate);
        date.setScore(10);

        Score space1 = objective.getScore(" ");
        space1.setScore(9);

        Team playerCount = scoreboard.registerNewTeam("playerCount");
        playerCount.addEntry(ChatColor.GOLD.toString());
        playerCount.setPrefix(ChatColor.WHITE + "Players: ");
        playerCount.setSuffix(ChatColor.GREEN + "" + "NumberOfPlayers" + "/" + "RequiredPlayers");
        objective.getScore(ChatColor.GOLD.toString()).setScore(8);

        Score space2 = objective.getScore("  ");
        space2.setScore(7);

        Score waiting = objective.getScore(ChatColor.WHITE + "Waiting...");
        waiting.setScore(6);

        Score space3 = objective.getScore("   ");
        space3.setScore(5);

        Score mode = objective.getScore(ChatColor.WHITE + "Mode: " + ChatColor.GREEN + "Hunger Games");
        mode.setScore(4);

        Score version = objective.getScore(ChatColor.WHITE + "Version: " + ChatColor.GRAY + "v1.0");
        version.setScore(3);

        Score space4 = objective.getScore("    ");
        space4.setScore(2);

        Score website = objective.getScore(ChatColor.YELLOW + "www.simonfoy.com");
        website.setScore(1);

        player.setScoreboard(scoreboard);
    }

    public void clearScoreboard(Player player) {
        Scoreboard clearScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(clearScoreboard);
    }
}
