package github.samyycx.xtopiachenghao.chenghao;

import github.samyycx.xtopiachenghao.logger.ChenghaoLogger;
import github.samyycx.xtopiachenghao.utils.TextUtils;
import github.samyycx.xtopiachenghao.utils.YamlUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlaceholderChenghaoManager {

    public static void addPlaceholderChenghao(CommandSender cs, String placeholder, String value, String chenghao, int priority) {
        YamlConfiguration config = YamlUtils.getPapiData();

        PlaceholderChenghao pc = new PlaceholderChenghao(TextUtils.colorize(chenghao), placeholder, value, priority);

        config.set(placeholder, pc.toMap());
        ChenghaoCache.PLACEHOLDER_CACHE.put(placeholder, pc);
        YamlUtils.savePapiData(config);
        ChenghaoLogger.success(cs, "设置成功!");
    }

    public static void removePlaceholderChenghao(CommandSender sender, String placeholder) {
        YamlConfiguration config = YamlUtils.getPapiData();

        if (!config.contains(placeholder)) {
            ChenghaoLogger.error(sender, "PlaceholderAPI变量不存在");
            return;
        }

        config.set(placeholder, null);
        ChenghaoCache.PLACEHOLDER_CACHE.remove(placeholder);

        YamlUtils.savePapiData(config);
    }

    }
