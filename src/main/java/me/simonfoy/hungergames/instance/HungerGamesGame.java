package me.simonfoy.hungergames.instance;

import me.simonfoy.hungergames.GameState;
import me.simonfoy.hungergames.HungerGames;
import me.simonfoy.hungergames.instance.kit.KitType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

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
        for (UUID uuid : game.getKits().keySet()) {
            game.getKits().get(uuid).onStart(Bukkit.getPlayer(uuid));
            Bukkit.getPlayer(uuid).closeInventory();
        }
    }

    public void onHungerGamesGameEnd() {
        game.end();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().contains("Kit Selection") && event.getInventory() != null && event.getCurrentItem() != null) {
            event.setCancelled(true);

            KitType type = KitType.valueOf(event.getCurrentItem().getItemMeta().getLocalizedName());

            if (game != null) {
                KitType activated = game.getKitType(player);
                if (activated != null && activated == type) {
                    player.sendMessage(ChatColor.RED + "You already have this kit equipped!");
                } else {
                    player.sendMessage(ChatColor.GREEN + "You have equipped the " + type.getDisplay() + ChatColor.GREEN + " kit!");
                    game.setKit(player.getUniqueId(), type);
                }

                player.closeInventory();
            }
        }
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
