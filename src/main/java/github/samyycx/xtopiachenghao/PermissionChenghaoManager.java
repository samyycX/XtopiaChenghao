package github.samyycx.xtopiachenghao;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

public class PermissionChenghaoManager {

    public static void addPermissionChenghao(CommandSender sender, String permission, String chenghao) {
        YamlConfiguration config = YamlUtils.getPermData();

        config.set(permission, chenghao);

        String oldChenghao = ChenghaoCache.PERMISSION_CACHE.getOrDefault(permission, "");

        ChenghaoCache.PERMISSION_CACHE.put(permission, A.a(chenghao));

        if (!oldChenghao.equals("")) {
            ChenghaoCache.CACHE.replaceAll((uuid, old) -> {
                if (old.equals(oldChenghao)) {
                    return A.a(chenghao);
                } else {
                    return old;
                }
            });
        }

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
                    if (playerData.getMapList("AllChenghaos").size() > 0) {
                        ChenghaoManager.switchPlayerNowChenghao(Bukkit.getPlayer(uuid), 0);
                    } else {
                        ChenghaoManager.switchPlayerNowChenghao(Bukkit.getPlayer(uuid), -1);
                    }
                }
            }
        }

        ChenghaoLogger.success(sender, "删除成功!");
    }

}
