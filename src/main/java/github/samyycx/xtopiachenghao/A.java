package github.samyycx.xtopiachenghao;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class A {

    public static String a(String b) {
        return ChatColor.translateAlternateColorCodes('&', b);
    }

    public static void changePlayerName(Player player, String chenghao) {
        if (chenghao.equals("")) {
            return;
        }
        String name = player.getPlayerListName();
        if (name.split(" ").length == 2) {
            name = name.split(" ")[1];
        }
        name = a(chenghao + " &r"+name);;
        player.setPlayerListName(name);
        player.setDisplayName(name);
    }
}
