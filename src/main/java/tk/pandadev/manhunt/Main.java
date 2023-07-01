package tk.pandadev.manhunt;

import games.negative.framework.BasePlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import tk.pandadev.manhunt.commands.ManhuntCommand;
import tk.pandadev.manhunt.commands.ReadyCommand;
import tk.pandadev.manhunt.listener.ManhuntListener;
import tk.pandadev.manhunt.utils.RoleTags;

public final class Main extends BasePlugin {

    public static Main instance;
    private static String Prefix = "§x§6§2§0§0§F§F§lManhunt §8» ";
    private static String NoPerm = Prefix + "§cYou do not have permission to do this";
    private static String InvalidPlayer = Prefix + "§cThis player is not online";

    public static int time = 0;

    @Override
    public void onEnable() {
        super.onEnable();
        saveDefaultConfig();
        registerCommands();
        registerListeners();
        instance = this;
        Bukkit.getConsoleSender().sendMessage(Prefix + "§aActivated");

        RoleTags.setAllPlayerTeams();
    }

    @Override
    public void onDisable() {
        RoleTags.setAllPlayerTeams();
        instance = null;
    }

    public void registerCommands() {
        getCommand("manhunt").setExecutor(new ManhuntCommand());
        getCommand("ready").setExecutor(new ReadyCommand());
    }

    public void registerListeners(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ManhuntListener(), this);
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
