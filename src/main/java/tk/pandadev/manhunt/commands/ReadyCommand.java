package tk.pandadev.manhunt.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.pandadev.manhunt.Main;
import tk.pandadev.manhunt.utils.ManhuntAPI;
import tk.pandadev.manhunt.utils.RoleTags;

import java.util.UUID;

public class ReadyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getPrefix() + "§6This command can only be run by a player!");
            return false;
        }

        Player player = (Player) (sender);

        if (args.length == 0){
            if (Main.getInstance().getConfig().getString("target") == null){ player.sendMessage(Main.getPrefix() + "§cThe manhunt has to be configured before starting it with §6/manhunt"); return false;}

            if (player.equals(Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target"))))){
                ManhuntAPI.readyManhunt();
                Main.getInstance().getConfig().set("ready", true);
                Main.getInstance().saveConfig();
                RoleTags.setAllPlayerTeams();
                Bukkit.getOnlinePlayers().forEach(player1 -> {player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 1);});
            } else {
                player.sendMessage(Main.getPrefix() + "§cThis command can only be run by the hunted player");
            }
        }

        return false;
    }

}