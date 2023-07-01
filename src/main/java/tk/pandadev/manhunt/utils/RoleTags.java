package tk.pandadev.manhunt.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import tk.pandadev.manhunt.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoleTags {
    public static void setAllPlayerTeams() {
        Bukkit.getOnlinePlayers().forEach(RoleTags::setPlayerTeams);
    }

    public static void setPlayerTeams(Player player){
        Scoreboard scoreboard = player.getScoreboard();

        Team hunter = scoreboard.getTeam("010manhunt_hunter");
        Team target = scoreboard.getTeam("010manhunt_target");

        if (hunter == null) hunter = scoreboard.registerNewTeam("010manhunt_hunter");
        if (target == null) target = scoreboard.registerNewTeam("010manhunt_target");

        if (Main.getInstance().getConfig().getBoolean("ready")){
            hunter.setSuffix(" §a[HUNTER]");
            target.setSuffix(" §c[TARGET]");
        } else {
            hunter.setSuffix("");
            target.setSuffix("");
        }


        Player target_player = Bukkit.getPlayer(UUID.fromString(Main.getInstance().getConfig().getString("target")));
        List<String> hunters = new ArrayList<>();

        for (Player player1 : Bukkit.getOnlinePlayers()){
            hunters.add(player1.getName());
        }

        hunters.remove(target.getName());

        for (String hunter1 : hunters) {
            Player finalHunter = Bukkit.getPlayer(hunter1);
            hunter.addEntry(finalHunter.getName());
        }

        target.addEntry(target_player.getName());
    }
}
