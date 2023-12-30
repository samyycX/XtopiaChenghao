package github.samyycx.xtopiachenghao.chenghao;

import github.samyycx.xtopiachenghao.utils.TextUtils;
import github.samyycx.xtopiachenghao.utils.YamlUtils;
import github.samyycx.xtopiachenghao.logger.ChenghaoLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

public class PermissionChenghaoManager {

    public static void addPermissionChenghao(CommandSender sender, String permission, String chenghao, int priority) {
        YamlConfiguration config = YamlUtils.getPermData();

        PermissionChenghao pc = new PermissionChenghao(TextUtils.colorize(chenghao), priority);

        config.set(permission, pc.toStringList());
        ChenghaoCache.PERMISSION_CACHE.put(permission, pc);
        YamlUtils.savePermData(config);
        ChenghaoLogger.success(sender, "设置成功!");
    }

    public static void removePermissionChenghao(CommandSender sender, String permission) {
        YamlConfiguration config = YamlUtils.getPermData();

        if (!config.contains(permission)) {
            ChenghaoLogger.error(sender, "权限不存在");
            return;
        }

        config.set(permission, null);
        ChenghaoCache.PERMISSION_CACHE.remove(permission);

        YamlUtils.savePermData(config);

        for (UUID uuid : YamlUtils.getAllUUID()) {
            YamlConfiguration playerData = YamlUtils.getPlayerData(uuid);

            if (playerData.getInt("NowUsing") == -2) {
                if (permission.equals(playerData.getString("NowUsingPermission"))) {
                    if (!playerData.getMapList("AllChenghaos").isEmpty()) {
                        ChenghaoManager.appendPlayerNowChenghao(Bukkit.getPlayer(uuid), 0);
                    } else {
                        ChenghaoManager.appendPlayerNowChenghao(Bukkit.getPlayer(uuid), -1);
                    }
                }
            }
        }

        ChenghaoLogger.success(sender, "删除成功!");
    }

}
