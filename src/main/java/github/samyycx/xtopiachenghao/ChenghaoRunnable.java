package github.samyycx.xtopiachenghao;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChenghaoRunnable implements Runnable {

    @Override
    public void run() {

        for (UUID uuid : YamlUtils.getAllUUID()) {
            List<Chenghao> chenghaos = ChenghaoManager.getPlayerAllChenghao(uuid);

            boolean notCompleted = !chenghaos.isEmpty();

            while (notCompleted) {

                if (chenghaos.size() == 0) {
                    notCompleted = false;
                }

                for (int index = 0; index < chenghaos.size(); index++) {
                    Chenghao chenghao = chenghaos.get(index);

                    int duration = chenghao.getDuration();

                    if (duration != -1) {
                        long timeOnCreated = chenghao.getTimeOnCreated();
                        if (timeOnCreated + duration < System.currentTimeMillis()) {
                            ChenghaoManager.removePlayerChenghao(null, Bukkit.getPlayer(uuid), index);
                            chenghaos.remove(index);
                            break;
                        }
                    }

                    if (index == chenghaos.size() - 1) {
                        notCompleted = false;
                    }


                }
            }

            YamlConfiguration playerData = YamlUtils.getPlayerData(uuid);
            int nowUsing = playerData.getInt("NowUsing");
            if (nowUsing == -2) {
                String perm = playerData.getString("NowUsingPermission");
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && !player.hasPermission(perm)) {
                    String chenghao = ChenghaoCache.PERMISSION_CACHE.get(perm);
                    if (playerData.getMapList("AllChenghaos").size() > 0) {
                        ChenghaoManager.switchPlayerNowChenghao(player, 0);
                    } else {
                        ChenghaoManager.switchPlayerNowChenghao(player, -1);
                    }
                    ChenghaoLogger.error(player, "您不再拥有权限 {p}, 称号 {c} &r&4已失效".replace("{p}", perm).replace("{c}", chenghao));
                }
            }
        }
    }
}
