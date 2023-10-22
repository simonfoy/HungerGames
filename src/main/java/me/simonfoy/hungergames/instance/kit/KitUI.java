package me.simonfoy.hungergames.instance.kit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class KitUI {

    public KitUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Kit Selection");

        for (KitType type : KitType.values()) {
            ItemStack itemStack = new ItemStack(type.getMaterial());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(type.getDisplay());
            itemMeta.setLore(Arrays.asList(type.getDescription()));
            itemMeta.setLocalizedName(type.name());
            itemStack.setItemMeta(itemMeta);

            gui.addItem(itemStack);

        }

        player.openInventory(gui);
    }

}
