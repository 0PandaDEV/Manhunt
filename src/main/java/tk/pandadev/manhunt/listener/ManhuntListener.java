package tk.pandadev.manhunt.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import tk.pandadev.manhunt.Main;
import tk.pandadev.manhunt.utils.ManhuntAPI;
import tk.pandadev.manhunt.utils.TimerAPI;

import java.util.UUID;

public class ManhuntListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damagedPlayer = (Player) event.getEntity();

            if (damagedPlayer.equals(Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target"))))){
                if (Main.getInstance().getConfig().getBoolean("ready")){
                    ManhuntAPI.startManhunt();
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player damagedPlayer = event.getEntity();

        if (damagedPlayer.equals(Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target"))))){
            if (Main.getInstance().getConfig().getBoolean("running")){
                Main.getInstance().getConfig().set("running", false);
                Main.getInstance().saveConfig();
                ManhuntAPI.stopManhunt();
                if (Main.getInstance().getConfig().getString("mode").equals("time_based")){
                    TimerAPI.stopTimer();
                }
            }
        }
    }
}
