package me.simonfoy.hungergames.instance;

import me.simonfoy.hungergames.GameState;
import me.simonfoy.hungergames.HungerGames;
import me.simonfoy.hungergames.instance.kit.KitType;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class HungerGamesGame extends GameListener {

    private HungerGames hungerGames;

    public HungerGamesGame(HungerGames hungerGames, Game game) {
        super(hungerGames, game);

        this.hungerGames = hungerGames;
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
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                game.getKits().get(uuid).onStart(player);
                player.closeInventory();
                player.teleport(game.getSpawn());
                player.setGameMode(GameMode.SURVIVAL);
                player.setHealth(20);
                player.setFoodLevel(20);

                ItemStack trackerCompass = new ItemStack(Material.COMPASS, 1);
                ItemMeta meta = trackerCompass.getItemMeta();
                meta.setDisplayName("Player Tracker");
                trackerCompass.setItemMeta(meta);
                player.getInventory().setItem(8, trackerCompass);

            }
        }
        Bukkit.getWorld("world").setTime(1000);
        game.getGameTimer().start();
        game.getInvincibilityTimer().start();
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
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (game.getInvincibilityTimer().isRunning() || !game.getState().equals(GameState.IN_PROGRESS)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (game.getInvincibilityTimer().isRunning() || !game.getState().equals(GameState.IN_PROGRESS)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.COMPASS && item.hasItemMeta() &&
                item.getItemMeta().getDisplayName().equals("Player Tracker") &&
                (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            updateCompassTracker(player);

            event.setCancelled(true);
        }
    }

    private void updateCompassTracker(Player player) {
        List<UUID> playersInGame = game.getPlayers();
        if (playersInGame.isEmpty()) {
            return;
        }

        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;

        for (UUID playerID : playersInGame) {
            if (playerID.equals(player.getUniqueId())) {
                continue;
            }

            Player target = Bukkit.getPlayer(playerID);
            if (target != null && target.isOnline()) {
                double distance = player.getLocation().distanceSquared(target.getLocation());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestPlayer = target;
                }
            }
        }

        if (nearestPlayer != null) {
            player.setCompassTarget(nearestPlayer.getLocation());
        } else {
            player.sendMessage(ChatColor.RED + "No other players were found.");
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UUID playerUUID = player.getUniqueId();

            if (player.getHealth() - event.getFinalDamage() <= 0) {
                event.setCancelled(true);

                player.setGameMode(GameMode.SPECTATOR);

                game.getPlayers().remove(playerUUID);
                game.getScoreBoardManager().updatePlayerCounter();

                if (game.getPlayers().size() == 1) {
                    Player winner = Bukkit.getPlayer(game.getPlayers().get(0));
                    if (winner != null) {
                        launchFireworks(winner.getLocation(), 5);

                        for (int i = 0; i < 5; i++) {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(hungerGames, () -> {
                                Bukkit.broadcastMessage(ChatColor.GOLD + winner.getName() + " has won the Hunger Games!");
                            }, 20L * i);
                        }
                        endHungerGamesGame();
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You have been defeated but you can stay and watch the game as a spectator.");
                }
            }
        }
    }

    private void launchFireworks(Location location, int amount) {
        for (int i = 0; i < amount; i++) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(hungerGames, () -> {
                Firework fw = location.getWorld().spawn(location, Firework.class);
                FireworkMeta fwm = fw.getFireworkMeta();
                fwm.setPower(1);
                fwm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE).build());
                fw.setFireworkMeta(fwm);
                fw.detonate();
            }, 20L * i);
        }
    }
}
