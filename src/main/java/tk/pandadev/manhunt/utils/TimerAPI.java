package tk.pandadev.manhunt.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import tk.pandadev.manhunt.Main;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimerAPI {

    private static BukkitTask bukkitrunnable;
    public static void stopTimer(){
        bukkitrunnable.cancel();
    }
    public static void startTimer(){
        bukkitrunnable = new BukkitRunnable() {
            int time = Main.getInstance().getConfig().getInt("time");
            @Override
            public void run() {
                if (time == 0) {
                    cancel();
                    Main.getInstance().getConfig().set("running", false);
                    Main.getInstance().saveConfig();
                    for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                        onlineplayer.sendTitle("§cTarget won", "§7Time ran out", 10, 200, 20);
                        onlineplayer.playSound(onlineplayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
                        onlineplayer.setGameMode(GameMode.SPECTATOR);
                        RoleTags.setAllPlayerTeams();
                    }
                }
                LocalTime myDateObj = LocalTime.ofSecondOfDay(time);
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
                String finaltime = myDateObj.format(myFormatObj);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§x§6§2§0§0§F§F§l" + finaltime));
                }
                time--;
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

}
