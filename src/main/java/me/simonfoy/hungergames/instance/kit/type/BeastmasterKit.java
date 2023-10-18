package me.simonfoy.hungergames.instance.kit.type;

import me.simonfoy.hungergames.HungerGames;
import me.simonfoy.hungergames.instance.kit.Kit;
import me.simonfoy.hungergames.instance.kit.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BeastmasterKit extends Kit {
    public BeastmasterKit(HungerGames hungerGames, UUID uuid) {
        super(hungerGames, KitType.BEASTMASTER, uuid);
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().addItem(new ItemStack(Material.WOLF_SPAWN_EGG, 3));
        player.getInventory().addItem(new ItemStack(Material.BONE, 4));
    }
}
