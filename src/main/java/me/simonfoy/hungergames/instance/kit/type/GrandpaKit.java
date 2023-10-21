package me.simonfoy.hungergames.instance.kit.type;

import me.simonfoy.hungergames.HungerGames;
import me.simonfoy.hungergames.instance.kit.Kit;
import me.simonfoy.hungergames.instance.kit.KitType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class GrandpaKit extends Kit {
    public GrandpaKit(HungerGames hungerGames, UUID uuid) {
        super(hungerGames, KitType.GRANDPA, uuid);
    }

    @Override
    public void onStart(Player player) {
        ItemStack knockbackStick = new ItemStack(Material.STICK);
        ItemMeta knockbackStickMeta = knockbackStick.getItemMeta();

        knockbackStickMeta.addEnchant(Enchantment.KNOCKBACK, 2, true);
        knockbackStick.setItemMeta(knockbackStickMeta);

        player.getInventory().addItem(knockbackStick);
    }
}