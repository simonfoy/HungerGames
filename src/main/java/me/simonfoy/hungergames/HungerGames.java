package me.simonfoy.hungergames;

import me.simonfoy.hungergames.instance.Game;
import me.simonfoy.hungergames.instance.GameListener;
import me.simonfoy.hungergames.listener.ConnectListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HungerGames extends JavaPlugin {

    private Game game;

    @Override
    public void onEnable() {
        game = new Game(this);

        Bukkit.getPluginManager().registerEvents(new ConnectListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Game getGame() {
        return game;
    }
}
