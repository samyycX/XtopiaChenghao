package github.samyycx.xtopiachenghao.logger;

import github.samyycx.xtopiachenghao.utils.TextUtils;
import github.samyycx.xtopiachenghao.XtopiaChenghao;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChenghaoLogger {

    public static String PREFIX;

    public static void init() {
        PREFIX = TextUtils.colorize(XtopiaChenghao.INSTANCE.getConfig().getString("Prefix"));
    }

    public static void error(CommandSender sender, String err) {
        if (sender == null) return;
        sender.sendMessage(TextUtils.colorize(PREFIX+"&4"+err));
    }

    public static void error(Player player, String err) {
        if (player == null) return;
        if (player.isOnline()) {
            player.sendMessage(TextUtils.colorize(PREFIX+"&4"+err));
        }
    }

    public static void success(CommandSender sender, String msg) {
        if (sender == null) return;
        sender.sendMessage(TextUtils.colorize(PREFIX+"&a"+msg));
    }

}
