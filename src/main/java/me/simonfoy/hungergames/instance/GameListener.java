package me.simonfoy.hungergames.instance;

import me.simonfoy.hungergames.GameState;
import me.simonfoy.hungergames.HungerGames;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class GameListener implements Listener {

    protected Game game;

    public GameListener(HungerGames hungerGames, Game game) {
        this.game = game;
        Bukkit.getPluginManager().registerEvents(this, hungerGames);
    }

    public void start() {
        game.setState(GameState.IN_PROGRESS);
        game.sendMessage("Game has started!");

        onStart();
    }

    public abstract void onStart();

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

}
