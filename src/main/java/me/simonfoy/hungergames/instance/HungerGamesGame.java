package me.simonfoy.hungergames.instance;

import me.simonfoy.hungergames.GameState;
import me.simonfoy.hungergames.HungerGames;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class HungerGamesGame extends GameListener {

    public HungerGamesGame(HungerGames hungerGames, Game game) {
        super(hungerGames, game);
    }

    @Override
    public void onStart() {
        startHungerGamesGame();
    }

    public void startHungerGamesGame() {

        onHungerGamesGameStart();
    }

    public void endHungerGamesGame() {

        onHungerGamesGameEnd();
    }

    public void onHungerGamesGameStart() {

    }

    public void onHungerGamesGameEnd() {
        game.end();
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {

        ItemStack droppedItem = event.getItemDrop().getItemStack();

        if (game != null && game.getState().equals(GameState.IN_PROGRESS)) {
            if (droppedItem.getType() == Material.STICK) {
                endHungerGamesGame(); // Assuming your game object has an end method
                event.getPlayer().sendMessage("You dropped a stick! Hunger Games Game over.");
            }
        }
    }
}
