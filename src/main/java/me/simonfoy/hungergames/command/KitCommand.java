package me.simonfoy.hungergames.command;

import me.simonfoy.hungergames.GameState;
import me.simonfoy.hungergames.HungerGames;
import me.simonfoy.hungergames.instance.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {

    private HungerGames hungerGames;
    private Game game;
    public KitCommand(HungerGames hungerGames, Game game) {
        this.hungerGames = hungerGames;
        this.game = game;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0 && args[0].equalsIgnoreCase("kit")) {
                if (game.getState() != GameState.IN_PROGRESS) {

                } else {
                    player.sendMessage(ChatColor.RED + "You cannot select a kit at this time.");
                }
            }
        }
        return false;
    }
}
