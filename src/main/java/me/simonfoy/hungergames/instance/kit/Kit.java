package me.simonfoy.hungergames.instance.kit;

import me.simonfoy.hungergames.HungerGames;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;

public abstract class Kit implements Listener {

    protected KitType type;
    protected UUID uuid;

    public Kit(HungerGames hungerGames, KitType type, UUID uuid) {
        this.type = type;
        this.uuid = uuid;

        Bukkit.getPluginManager().registerEvents(this, hungerGames);

    }

    public UUID getUUID() { return uuid; }
    public KitType getType() { return type; }

    public abstract void onStart(Player player);

    public void remove() {
        HandlerList.unregisterAll(this);
    }


}
