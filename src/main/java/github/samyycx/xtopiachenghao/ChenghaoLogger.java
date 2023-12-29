package github.samyycx.xtopiachenghao;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChenghaoLogger {

    public static String PREFIX;

    public static void init() {
        PREFIX = A.a(XtopiaChenghao.INSTANCE.getConfig().getString("Prefix"));
    }

    public static void error(CommandSender sender, String err) {
        if (sender == null) return;
        sender.sendMessage(A.a(PREFIX+"&4"+err));
    }

    public static void error(Player player, String err) {
        if (player == null) return;
        if (player.isOnline()) {
            player.sendMessage(A.a(PREFIX+"&4"+err));
        }
    }

    public static void success(CommandSender sender, String msg) {
        if (sender == null) return;
        sender.sendMessage(A.a(PREFIX+"&a"+msg));
    }

}
