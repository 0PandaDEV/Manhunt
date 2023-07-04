package tk.pandadev.manhunt.guis;

import dev.triumphteam.gui.guis.Gui;
import games.negative.framework.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tk.pandadev.manhunt.Main;
import tk.pandadev.manhunt.utils.Utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ManhuntGui {

    public static FileConfiguration config = Main.getInstance().getConfig();

    public static String owner;

    public static void mainGui(Player player) {

        if (config.get("target") == null) owner = "asdasdsa278596786dsdl121fgjs9f34qpfq902fj";
        else owner = Bukkit.getPlayer(UUID.fromString(config.getString("target"))).getName();

        Gui gui = Gui.gui()
                .title(Component.text("Manhunt"))
                .rows(5)
                .disableAllInteractions()
                .create();

        if (config.getString("time") == null) config.set("time", 0);

        LocalTime myDateObj = LocalTime.ofSecondOfDay(config.getInt("time"));
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = myDateObj.format(myFormatObj);

        //////////////////////////////

        ItemStack time_based = new ItemBuilder(Material.CLOCK)
                .setName("§eSet time")
                .addLoreLine("")
                .addLoreLine("§a" + time)
                .addLoreLine("")
                .addLoreLine("§8Set a time where the hunter have")
                .addLoreLine("§8to kill the target in")
                .build();

        gui.setItem(2, 2, dev.triumphteam.gui.builder.item.ItemBuilder.from(time_based).asGuiItem(event -> {
            timeChoose(player);
        }));


        ItemStack time_on = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .setName("§a✔ §8• §7Time based")
                .build();

        ItemStack time_off = new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .setName("§c❌ §8• §7Time based")
                .build();

        gui.setItem(2, 3, dev.triumphteam.gui.builder.item.ItemBuilder.from(config.getString("mode").equals("time_based") ? time_on : time_off).asGuiItem(event -> {
            config.set("mode", "time_based");
            Main.getInstance().saveConfig();
            mainGui(player);
        }));

        //////////////////////////////

        ItemStack target_death = new ItemBuilder(Material.NETHERITE_SWORD)
                .setName("§x§4§E§3§B§3§DTarget death")
                .addLoreLine("")
                .addLoreLine("§8The target has to be killed")
                .build();

        gui.setItem(4, 2, dev.triumphteam.gui.builder.item.ItemBuilder.from(target_death).asGuiItem());

        ItemStack death_on = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .setName("§a✔ §8• §7Target death")
                .build();

        ItemStack death_off = new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .setName("§c❌ §8• §7Target death")
                .build();

        gui.setItem(4, 3, dev.triumphteam.gui.builder.item.ItemBuilder.from(config.getString("mode").equals("target_death") ? death_on : death_off).asGuiItem(event -> {
            config.set("mode", "target_death");
            Main.getInstance().saveConfig();
            mainGui(player);
        }));

        //////////////////////////////

        ItemStack chose_player = new ItemBuilder(Material.PLAYER_HEAD).setName("§fSelect player").setSkullOwner(owner).build();

        gui.setItem(3, 5, dev.triumphteam.gui.builder.item.ItemBuilder.from(chose_player).asGuiItem(event -> {
            choosePlayer(player);
        }));

        gui.setItem(3, 6, dev.triumphteam.gui.builder.item.ItemBuilder.from(Utils.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ2NWY4MGJmMDJiNDA4ODg1OTg3YjAwOTU3Y2E1ZTllYjg3NGMzZmE4ODMwNTA5OTU5N2EzMzNhMzM2ZWUxNSJ9fX0=")).name(Component.text("§cRandom player")).asGuiItem(event -> {
            List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
            Player target = Utils.getRandomElement(players);
            config.set("target", String.valueOf(target.getUniqueId()));
            Main.getInstance().saveConfig();
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 1);
            mainGui(player);
        }));

        /////////////////////////////

        String player_string = "";
        if (config.getString("target") == null) {
            player_string = "not set";
        } else {
            player_string = Bukkit.getPlayer(UUID.fromString(config.getString("target"))).getName();
        }

        if (config.get("mode").equals("time_based")) {
            gui.setItem(3, 8, dev.triumphteam.gui.builder.item.ItemBuilder.from(Utils.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDMxMmNhNDYzMmRlZjVmZmFmMmViMGQ5ZDdjYzdiNTVhNTBjNGUzOTIwZDkwMzcyYWFiMTQwNzgxZjVkZmJjNCJ9fX0="))
                    .name(Component.text("§aDone"))
                    .addLore("")
                    .addLore("§8Mode: " + config.getString("mode").replace("_", " "))
                    .addLore("§8Time: " + time)
                    .addLore("§8Target: " + player_string)
                    .asGuiItem(event -> {
                        if (config.get("mode").equals("time_based")) {
                            if (config.getInt("time") == 0) {
                                player.sendMessage(Main.getPrefix() + "§cTime cannot be 00:00:00");
                                return;
                            }
                        }
                        config.set("ready", true);
                        Main.getInstance().saveConfig();
                        gui.close(player);
                    }));
        } else {
            gui.setItem(3, 8, dev.triumphteam.gui.builder.item.ItemBuilder.from(Utils.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDMxMmNhNDYzMmRlZjVmZmFmMmViMGQ5ZDdjYzdiNTVhNTBjNGUzOTIwZDkwMzcyYWFiMTQwNzgxZjVkZmJjNCJ9fX0="))
                    .name(Component.text("§aDone"))
                    .addLore("")
                    .addLore("§8Mode: " + config.getString("mode").replace("_", " "))
                    .addLore("§8Target: " + player_string)
                    .asGuiItem(event -> {
                        if (config.get("mode").equals("time_based")) {
                            if (config.getInt("time") == 0) {
                                player.sendMessage(Main.getPrefix() + "§cTime cannot be 00:00:00");
                                return;
                            }
                        }
                        config.set("ready", true);
                        Main.getInstance().saveConfig();
                        gui.close(player);
                    }));
        }

        gui.open(player);
    }

    public static void timeChoose(Player player) {
        Gui gui = Gui.gui()
                .title(Component.text("Choose Time"))
                .rows(3)
                .disableAllInteractions()
                .create();

        Main.time = config.getInt("time");
        LocalTime myDateObj = LocalTime.ofSecondOfDay(Main.time);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        String finaltime = myDateObj.format(myFormatObj);

        gui.setItem(13, dev.triumphteam.gui.builder.item.ItemBuilder.from(Material.CLOCK).name(Component.text("§f" + finaltime)).asGuiItem(event -> {
            new AnvilGUI.Builder()
                    .onComplete((completion) -> {
                        String timeStr = completion.getText();
                        String[] timeArr = timeStr.split(":");
                        int hours = Integer.parseInt(timeArr[0]);
                        int minutes = Integer.parseInt(timeArr[1]);
                        int seconds = Integer.parseInt(timeArr[2]);
                        int totalSeconds = hours * 3600 + minutes * 60 + seconds;
                        config.set("time", totalSeconds);
                        Main.getInstance().saveConfig();
                        timeChoose(player);
                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                    })
                    .text(finaltime)
                    .itemLeft(new ItemStack(Material.NAME_TAG))
                    .title("Enter a custom time")
                    .plugin(Main.getInstance())
                    .open(player);
        }));

        gui.setItem(14, dev.triumphteam.gui.builder.item.ItemBuilder.from(Material.LIME_DYE).name(Component.text("§a+10s")).asGuiItem(event -> {
            Main.time += 10;
            if (Main.time < 0) Main.time = 0;
            config.set("time", Main.time);
            Main.getInstance().saveConfig();
            timeChoose(player);
        }));

        gui.setItem(15, dev.triumphteam.gui.builder.item.ItemBuilder.from(Material.LIME_DYE).name(Component.text("§a+1m")).asGuiItem(event -> {
            Main.time += 60;
            if (Main.time < 0) Main.time = 0;
            config.set("time", Main.time);
            Main.getInstance().saveConfig();
            timeChoose(player);
        }));

        gui.setItem(16, dev.triumphteam.gui.builder.item.ItemBuilder.from(Material.LIME_DYE).name(Component.text("§a+30m")).asGuiItem(event -> {
            Main.time += 1800;
            if (Main.time < 0) Main.time = 0;
            config.set("time", Main.time);
            Main.getInstance().saveConfig();
            timeChoose(player);
        }));

        gui.setItem(12, dev.triumphteam.gui.builder.item.ItemBuilder.from(Material.RED_DYE).name(Component.text("§c-10s")).asGuiItem(event -> {
            Main.time -= 10;
            if (Main.time < 0) Main.time = 0;
            config.set("time", Main.time);
            Main.getInstance().saveConfig();
            timeChoose(player);
        }));

        gui.setItem(11, dev.triumphteam.gui.builder.item.ItemBuilder.from(Material.RED_DYE).name(Component.text("§c-1m")).asGuiItem(event -> {
            Main.time -= 60;
            if (Main.time < 0) Main.time = 0;
            config.set("time", Main.time);
            Main.getInstance().saveConfig();
            timeChoose(player);
        }));

        gui.setItem(10, dev.triumphteam.gui.builder.item.ItemBuilder.from(Material.RED_DYE).name(Component.text("§c-30m")).asGuiItem(event -> {
            Main.time -= 1800;
            if (Main.time < 0) Main.time = 0;
            config.set("time", Main.time);
            Main.getInstance().saveConfig();
            timeChoose(player);
        }));

        gui.setItem(26, dev.triumphteam.gui.builder.item.ItemBuilder.from(Utils.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDMxMmNhNDYzMmRlZjVmZmFmMmViMGQ5ZDdjYzdiNTVhNTBjNGUzOTIwZDkwMzcyYWFiMTQwNzgxZjVkZmJjNCJ9fX0=")).name(Component.text("§aDone")).asGuiItem(event -> {
            config.set("time", Main.time);
            Main.getInstance().saveConfig();
            mainGui(player);
        }));

        gui.open(player);
    }

    public static void choosePlayer(Player player) {
        Gui gui = Gui.gui()
                .disableAllInteractions()
                .rows(5)
                .title(Component.text("Choose Player"))
                .create();

        for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
            ItemStack choseplayer = new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(onlineplayer.getName()).setName(onlineplayer.getName()).build();
            gui.addItem(dev.triumphteam.gui.builder.item.ItemBuilder.from(choseplayer).asGuiItem(event -> {
                config.set("target", String.valueOf(onlineplayer.getUniqueId()));
                Main.getInstance().saveConfig();
                player.sendMessage(Main.getPrefix() + "§a" + onlineplayer.getName() + " §7was set as target");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 1);
                mainGui(player);
            }));
        }

        gui.setItem(5, 1, dev.triumphteam.gui.builder.item.ItemBuilder.from(Utils.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==")).name(Component.text("§fBack")).asGuiItem(event -> {
            mainGui(player);
        }));

        gui.open(player);
    }
}
