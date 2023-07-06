package tk.pandadev.manhunt.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import tk.pandadev.manhunt.Main;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ActionBarAPI {

    private static BukkitTask bukkitRunnable;

    public static FileConfiguration config = Main.getInstance().getConfig();

    public static void stopActionbar() {
        bukkitRunnable.cancel();
    }

    public static void actionBar() {
        bukkitRunnable = new BukkitRunnable() {
            int time = Main.getInstance().getConfig().getInt("time");

            private String finaltime;

            @Override
            public void run() {
                Player target = Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target")));

                if (config.get("mode").equals("time_based")) {
                    if (time == 0) {
                        cancel();
                        Main.getInstance().getConfig().set("running", false);
                        Main.getInstance().saveConfig();
                        for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                            for (Player hunter : ManhuntAPI.getHunters()) {
                                hunter.sendTitle("§cYou Lose", "§7Time ran out", 10, 200, 20);
                            }
                            target.sendTitle("§aYou Win", "§7Time ran out", 10, 200, 20);

                            onlineplayer.playSound(onlineplayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 0.5f);
                            onlineplayer.setGameMode(GameMode.SPECTATOR);
                            RoleTags.setAllPlayerTeams();
                        }
                    }
                    LocalTime myDateObj = LocalTime.ofSecondOfDay(time);
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
                    finaltime = myDateObj.format(myFormatObj);
                }


                target.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§x§6§2§0§0§F§FActive Manhunt" + (config.get("mode").equals("time_based") ? "  §8-  §x§6§2§0§0§F§F§l" + finaltime : "")));

                for (Player hunter : ManhuntAPI.getHunters()) {
                    int distance = (int) Math.round(hunter.getLocation().distance(target.getLocation()));
                    hunter.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§x§6§2§0§0§F§FYour Target: " + distance + "m" + (config.get("mode").equals("time_based") ? "  §8-  §x§6§2§0§0§F§F§l" + finaltime : "")));
                }
                time--;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }
}
