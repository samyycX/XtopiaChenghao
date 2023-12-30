package github.samyycx.xtopiachenghao.utils;

import github.samyycx.xtopiachenghao.chenghao.ChenghaoManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class TextUtils {

    public static String colorize(String b) {
        return ChatColor.translateAlternateColorCodes('&', b);
    }

    public static List<String> colorize(List<String> b) {
        return b.stream().map(TextUtils::colorize).collect(Collectors.toList());
    }

    public static void changePlayerName(Player player, List<String> chenghaos) {
        String name = player.getPlayerListName();
        if (name.split(" ").length >= 2) {
            String[] split = name.split(" ");
            name = split[split.length-1];
        }
        if (chenghaos.isEmpty()) {
            player.setDisplayName(name);
            player.setPlayerListName(name);
            return;
        }
        name = colorize(String.join(" ",chenghaos) + " &r"+name);;
        player.setPlayerListName(name);
        player.setDisplayName(name);
    }
}
