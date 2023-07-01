package tk.pandadev.manhunt.utils;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tk.pandadev.manhunt.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ManhuntAPI {

    public static void readyManhunt(){
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()){
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

        List<String> hunters = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()){
            hunters.add(player.getName());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
        }

        hunters.remove(target.getName());

        for (String hunter : hunters) {
            Player finalHunter = Bukkit.getPlayer(hunter);
            finalHunter.sendTitle("§aHunter", "§7Kill the target", 0, 100, 20);
        }

        target.sendTitle("§cTarget", "§7Run away", 0, 100, 20);

        if (Main.getInstance().getConfig().get("mode").equals("time_based")){
            TimerAPI.startTimer();
        }

    }

    public static void stopManhunt(){
        Player target = Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target")));
        List<String> hunters = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()){
            hunters.add(player.getName());
            player.setGameMode(GameMode.SPECTATOR);
        }

        hunters.remove(target.getName());

        for (String hunter : hunters) {
            Player finalHunter = Bukkit.getPlayer(hunter);
            finalHunter.sendTitle("§aYou Win", "§7Target lost", 10, 200, 20);
        }

        target.sendTitle("§cYou Lose", "§7Hunters won", 10, 200, 20);

        RoleTags.setAllPlayerTeams();
    }

}
