package github.samyycx.xtopiachenghao.command;

import github.samyycx.xtopiachenghao.XtopiaChenghao;
import github.samyycx.xtopiachenghao.chenghao.*;
import github.samyycx.xtopiachenghao.gui.ChenghaoGUI;
import github.samyycx.xtopiachenghao.logger.ChenghaoLogger;
import github.samyycx.xtopiachenghao.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*

/ch help
/ch storage
/ch give <ID> <名称> <时间>
/ch perm <Permission> <名称>
 */
public class ChCommandExecutor implements CommandExecutor {

    public static List<String> helps = new ArrayList<String>() {{
        add("&a===============&cXtopiaChenghao&a===============");
        add("&b/ch help &a显示当前帮助");
        add("&b/ch storage &a玩家指令，打开当前背包");
        add("&c/ch give <玩家名> <称号名称> <时间> <优先级> &aOP指令，给予玩家称号，时间单位为秒，设置为-1为永久");
        add("&c/ch perm <权限> <称号名称> <优先级> &aOP指令，设置拥有此权限的玩家的称号");
        add("&c/ch papi <Placeholder名> <Placeholder值> <称号名称> <优先级> &aOP指令，设置Placeholder变量称号");
        add("&c/ch show <玩家名> &aOP指令，显示玩家的所有称号");
        add("&c/ch permlist &aOP指令，显示权限对应的所有称号");
        add("&c/ch papilist &aOP指令，显示Papi对应的所有称号");
        add("&c/ch remove <玩家名> <编号> &aOP指令，删除玩家的某个称号，编号通过&b/ch show&a获取");
        add("&c/ch removeperm <权限名> &aOP指令，删除某个权限对应的称号");
        add("&c/ch removepapi &aOP指令，删除某个papi变量对应的称号");
        add("&c/ch reload &aOP指令，重载插件");
        add("&a============================================");
    }};
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            showHelps(sender);
            return true;
        }

        switch (args[0]) {
            case "help":
                showHelps(sender);
                return true;
            case "storage":
                if (sender instanceof Player) {
                    ChenghaoGUI.showPlayer((Player) sender);
                }
                return true;
            case "give":

                if (!sender.isOp()) {
                    noPerm(sender);
                    break;
                }

                if (args.length < 5) {
                    showError(sender);
                } else {
                    Player player = Bukkit.getPlayer(args[1]);
                    ChenghaoManager.givePlayerChenghao(sender, player, args[2], Integer.parseInt(args[3]), false, Integer.parseInt(args[4]));
                }
                return true;
            case "perm":

                if (!sender.isOp()) {
                    noPerm(sender);
                    break;
                }

                if (args.length < 4) {
                    showError(sender);
                } else {
                    PermissionChenghaoManager.addPermissionChenghao(sender, args[1], args[2], Integer.parseInt(args[3]));
                }
                return true;
            case "papi":
                if (!sender.isOp()) {
                    noPerm(sender);
                    break;
                }

                if (args.length < 5) {
                    showError(sender);
                } else {
                    PlaceholderChenghaoManager.addPlaceholderChenghao(sender, args[1], args[2], args[3], Integer.parseInt(args[4]));
                }
                return true;
            case "show":

                if (!sender.isOp()) {
                    noPerm(sender);
                    break;
                }

                if (args.length < 2) {
                    showError(sender);
                } else {
                    sender.sendMessage(TextUtils.colorize("&a===============&cXtopiaChenghao&a==============="));
                    int index = 0;
                    for (Chenghao ch : ChenghaoManager.getPlayerAllChenghao(Bukkit.getPlayer(args[1]).getUniqueId())) {
                        sender.sendMessage(TextUtils.colorize("&b"+index+". "+ch.getName()));
                        index++;
                    }
                    sender.sendMessage(TextUtils.colorize("&a============================================"));
                }
                return true;
            case "permlist":
                if (!sender.isOp()) {
                    noPerm(sender);
                    break;
                }

                sender.sendMessage(TextUtils.colorize("&a===============&cXtopiaChenghao&a==============="));
                ChenghaoCache.PERMISSION_CACHE.forEach((perm, chenghao) -> {
                    sender.sendMessage(TextUtils.colorize("&b{p} &e-> &r{c}").replace("{p}", perm).replace("{c}", chenghao.getName()));
                });
                sender.sendMessage(TextUtils.colorize("&a============================================"));
                return true;
            case "papilist":
                if (!sender.isOp()) {
                    noPerm(sender);
                    break;
                }

                sender.sendMessage(TextUtils.colorize("&a===============&cXtopiaChenghao&a==============="));
                ChenghaoCache.PLACEHOLDER_CACHE.forEach((placeholder, pc) -> {
                    sender.sendMessage(TextUtils.colorize("&b{k}=&d{v} &e-> &r{c}").replace("{p}", pc.getPlaceholder()).replace("{v}", pc.getValue()).replace("{c}", pc.getName()));
                });
                sender.sendMessage(TextUtils.colorize("&a============================================"));
                return true;
            case "remove":
                if (!sender.isOp()) {
                    noPerm(sender);
                    break;
                }

                if (args.length < 3) {
                    showError(sender);
                } else {
                    ChenghaoManager.removePlayerChenghao(sender, Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]));
                }
                return true;
            case "removeperm":
                if (!sender.isOp()) {
                    noPerm(sender);
                    break;
                }

                if (args.length < 2) {
                    showError(sender);
                } else {
                    PermissionChenghaoManager.removePermissionChenghao(sender, args[1]);
                }
                return true;
            case "removepapi":
                if (!sender.isOp()) {
                    noPerm(sender);
                    break;
                }

                if (args.length < 2) {
                    showError(sender);
                } else {
                    PlaceholderChenghaoManager.removePlaceholderChenghao(sender, args[1]);
                }
                return true;
            case "reload":
                if (!sender.isOp()) {
                    noPerm(sender);
                    break;
                }

                XtopiaChenghao.reload();
                ChenghaoLogger.success(sender, "重载成功!");
                return true;
            default:
                showError(sender);
                return true;
        }
        return true;
    }

    private void showHelps(CommandSender sender) {
        for (String help : helps) {
            sender.sendMessage(TextUtils.colorize(help));
        }
    }

    private void showError(CommandSender sender) {
        ChenghaoLogger.error(sender, "命令使用方法有误，请使用&b/ch help&4获取帮助");
    }

    private void noPerm(CommandSender sender) {
        ChenghaoLogger.error(sender, "你没有权限使用此指令");
    }


}
