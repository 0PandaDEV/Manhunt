package tk.pandadev.manhunt.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tk.pandadev.manhunt.Main;
import tk.pandadev.manhunt.utils.ActionBarAPI;
import tk.pandadev.manhunt.utils.ManhuntAPI;
import tk.pandadev.manhunt.utils.TimerAPI;

import java.util.UUID;

public class ManhuntListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damagedPlayer = (Player) event.getEntity();

            if (damagedPlayer.equals(Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target"))))) {
                if (Main.getInstance().getConfig().getBoolean("ready")) {
                    ManhuntAPI.startManhunt();
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player damagedPlayer = event.getEntity();

        if (damagedPlayer.equals(Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target"))))) {
            if (Main.getInstance().getConfig().getBoolean("running")) {
                Main.getInstance().getConfig().set("running", false);
                Main.getInstance().saveConfig();
                ManhuntAPI.stopManhunt();
                ActionBarAPI.stopActionbar();
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (Main.getInstance().getConfig().getBoolean("running")) {
            if (!event.getPlayer().equals(Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target"))))) {
                System.out.println("respawn");
                ItemStack tracker = new ItemStack(Material.COMPASS);
                ItemMeta trackerMeta = tracker.getItemMeta();
                trackerMeta.setDisplayName("§fTracker");
                trackerMeta.setLocalizedName("010manhunt_tracker");
                tracker.setItemMeta(trackerMeta);

                event.getPlayer().getInventory().setItem(4, tracker);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getType().equals(EntityType.ENDER_DRAGON)) {
            Main.getInstance().getConfig().set("running", false);
            Main.getInstance().saveConfig();
            ManhuntAPI.dragonManhunt();
            ActionBarAPI.stopActionbar();
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getItemMeta().getLocalizedName().equals("010manhunt_tracker")) {
            event.setCancelled(true);
        }
    }
}
