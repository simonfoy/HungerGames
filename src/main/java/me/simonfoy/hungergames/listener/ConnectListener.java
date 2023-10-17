package me.simonfoy.hungergames.listener;

import me.simonfoy.hungergames.HungerGames;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectListener implements Listener {

    private HungerGames hungerGames;

    public ConnectListener(HungerGames hungerGames) {
        this.hungerGames = hungerGames;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        handlePlayerJoin(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        handlePlayerQuit(event.getPlayer());
    }

    public void handlePlayerJoin(Player player) {
        hungerGames.getGame().addPlayer(player);
    }

    public void handlePlayerQuit(Player player) {
        hungerGames.getGame().removePlayer(player);
    }

}
