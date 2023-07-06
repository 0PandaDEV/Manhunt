package tk.pandadev.manhunt.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import tk.pandadev.manhunt.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ManhuntAPI {

    public static FileConfiguration config = Main.getInstance().getConfig();

    private static String owner;

    public static void readyManhunt() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            World world = Bukkit.getWorlds().get(0);
            onlinePlayer.teleport(world.getSpawnLocation());

            onlinePlayer.setGameMode(GameMode.SURVIVAL);
        }

        Player target = Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target")));

        PotionEffect glowEffect = new PotionEffect(PotionEffectType.GLOWING, 1000000000, 0);
        target.addPotionEffect(glowEffect);

        Bukkit.broadcastMessage(Main.getPrefix() + "§7Manhunt is ready. To start it just hit the target once. The target is outlined until it starts.");
    }

    public static void startManhunt() {
        Main.getInstance().getConfig().set("ready", false);
        Main.getInstance().getConfig().set("running", true);
        Main.getInstance().saveConfig();

        Player target = Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target")));
        target.removePotionEffect(PotionEffectType.GLOWING);

        ItemStack tracker = new ItemStack(Material.COMPASS);
        ItemMeta trackerMeta = tracker.getItemMeta();
        trackerMeta.setDisplayName("§fTracker");
        trackerMeta.setLocalizedName("010manhunt_tracker");
        tracker.setItemMeta(trackerMeta);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
            player.getInventory().clear();
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
        }

        for (Player hunter : getHunters()) {
            hunter.sendTitle("§aHunter", "§7Kill the target", 0, 100, 20);
            hunter.getInventory().setItem(4, tracker);
        }

        target.sendTitle("§cTarget", "§7Run away", 0, 100, 20);

        ActionBarAPI.actionBar();

        run(target);

    }

    public static void stopManhunt() {
        Player target = Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target")));

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SPECTATOR);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 0.5f);
        }

        for (Player hunter : getHunters()) {
            hunter.sendTitle("§aYou Win", "§7Target lost", 10, 200, 20);
        }

        target.sendTitle("§cYou Lose", "§7Hunters won", 10, 200, 20);

        RoleTags.setAllPlayerTeams();

    }

    public static void dragonManhunt() {
        Player target = Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target")));

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SPECTATOR);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 0.5f);
        }

        for (Player hunter : getHunters()) {
            hunter.sendTitle("§cYou Lose", "§7Target won", 10, 200, 20);
        }
        target.sendTitle("§aYou Win", "§7Hunters lost", 10, 200, 20);


        RoleTags.setAllPlayerTeams();

    }

    public static List<Player> getHunters() {
        List<String> hunters = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            hunters.add(player.getName());
        }
        List<Player> final_hunters = new ArrayList<>();

        if (getTarget() != null){
            Player target = getTarget();
            hunters.remove(target.getName());
        }

        for (String hunter : hunters) {
            Player finalHunter = Bukkit.getPlayer(hunter);
            final_hunters.add(finalHunter);
        }

        return final_hunters;
    }


    public static Player getTarget(){
        if (config.get("target") == null) return null;
        return Bukkit.getPlayer(UUID.fromString(config.getString("target")));
    }

    private static void run(Player target) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player hunter : getHunters()) {
                    hunter.setCompassTarget(target.getLocation());
                }
            }
        }.runTaskTimer(Main.getInstance(), 1, 1);
    }

}
