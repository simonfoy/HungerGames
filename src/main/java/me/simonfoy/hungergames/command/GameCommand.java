package me.simonfoy.hungergames.command;

import me.simonfoy.hungergames.GameState;
import me.simonfoy.hungergames.HungerGames;
import me.simonfoy.hungergames.instance.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand implements CommandExecutor {

    private HungerGames hungerGames;
    private Game game;
    public GameCommand(HungerGames hungerGames, Game game) {
        this.hungerGames = hungerGames;
        this.game = game;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You do not have permission to control the game.");
            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("start")) {
                if (game.getState() == GameState.PREPARING || game.getState() == GameState.PRE_START) {
                    game.start();
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The game has been started by " + player.getName() + ".");
                } else {
                    player.sendMessage(ChatColor.RED + "The game is not in a state that allows it to be started.");
                }
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /" + label + " start");
                return true;
            }
        }

        return false;
    }
}
