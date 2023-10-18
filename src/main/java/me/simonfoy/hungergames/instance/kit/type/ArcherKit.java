package me.simonfoy.hungergames.instance.kit.type;

import me.simonfoy.hungergames.HungerGames;
import me.simonfoy.hungergames.instance.kit.Kit;
import me.simonfoy.hungergames.instance.kit.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ArcherKit extends Kit {
    public ArcherKit(HungerGames hungerGames, UUID uuid) {
        super(hungerGames, KitType.ARCHER, uuid);
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().addItem(new ItemStack(Material.BOW));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 10));

    }
}
