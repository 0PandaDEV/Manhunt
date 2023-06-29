package tk.pandadev.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    public static Main instance;
    private static String Prefix = "§9§lChallenge §8» ";
    private static String NoPerm = Prefix + "§cYou do not have permission to do this";
    private static String InvalidPlayer = Prefix + "§cThis player is not online";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        Bukkit.getConsoleSender().sendMessage(Prefix + "§aActivated");
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public void registerCommands() {
    }

    public void registerListeners(){
        PluginManager pluginManager = Bukkit.getPluginManager();
    }

    public static Main getInstance() {
        return instance;
    }

    public static String getInvalidPlayer() {
        return InvalidPlayer;
    }

    public static String getNoPerm() {
        return NoPerm;
    }

    public static String getPrefix() {
        return Prefix;
    }
}
