package me.simonfoy.hungergames.instance.kit;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum KitType {

    ARCHER(ChatColor.GOLD + "Archer", Material.BOW, "This is the Archer kit!"),
    BEASTMASTER(ChatColor.GOLD + "Beastmaster", Material.BONE, "This is the Beastmaster kit!"),
    GRANDPA(ChatColor.GOLD + "Grandpa", Material.STICK, "This is the Grandpa kit!");

    private String display, description;
    private Material material;

    KitType(String display, Material material, String description) {
        this.display = display;
        this.material = material;
        this.description = description;
    }

    public String getDisplay() { return display; }
    public Material getMaterial() { return material; }
    public String getDescription() { return description; }

}
