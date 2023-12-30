package github.samyycx.xtopiachenghao.chenghao;

import github.samyycx.xtopiachenghao.utils.TextUtils;
import github.samyycx.xtopiachenghao.utils.YamlUtils;
import github.samyycx.xtopiachenghao.logger.ChenghaoLogger;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChenghaoRunnable implements Runnable {

    @Override
    public void run() {

        for (UUID uuid : YamlUtils.getAllUUID()) {
            List<Chenghao> chenghaos = ChenghaoManager.getPlayerAllChenghao(uuid);

            boolean notCompleted = !chenghaos.isEmpty();

            while (notCompleted) {

                if (chenghaos.isEmpty()) {
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

            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                continue;
            }
            List<ChenghaoIndex> nowUsing = ChenghaoManager.getPlayerNowChenghao(player);
            List<ChenghaoIndex> nowUsingClone = new ArrayList<>(nowUsing);
            for (ChenghaoIndex ci : nowUsing) {
                switch (ci.getType()) {
                    case "permission":
                        if (!player.hasPermission(ci.getStringValue())) {
                            PermissionChenghao pc_ = ChenghaoCache.PERMISSION_CACHE.get(ci.getStringValue());
                            ChenghaoLogger.error(player, "&4称号 {c} &r&4已失效".replace("{c}", pc_.getName()));
                            nowUsingClone.removeIf(c -> c.equals(ci.getStringValue()));
                        }
                        continue;
                    case "placeholder":
                        PlaceholderChenghao pc = ChenghaoCache.PLACEHOLDER_CACHE.get(ci.getStringValue());
                        if (!pc.getValue().equals(PlaceholderAPI.setPlaceholders(player, pc.getPlaceholder()))) {
                            nowUsingClone.removeIf(c -> c.equals(ci.getStringValue()));
                            ChenghaoLogger.error(player, "&4称号 {c} &r&4已失效".replace("{c}", pc.getName()));
                        }
                        continue;
                }

            }

            if (nowUsingClone.size() < nowUsing.size()) {
                ChenghaoManager.savePlayerNowUsing(player, nowUsingClone);
                TextUtils.changePlayerName(player, ChenghaoManager.renderPlayerChenghao(player));
            }
        }
    }
}
