package github.samyycx.xtopiachenghao.chenghao;

import github.samyycx.xtopiachenghao.utils.TextUtils;
import github.samyycx.xtopiachenghao.utils.YamlUtils;
import github.samyycx.xtopiachenghao.logger.ChenghaoLogger;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChenghaoManager {

    public static void givePlayerChenghao(CommandSender sender, Player player, String chenghao, int duration, boolean isDefault, int priority) {
        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());

        config.addDefault("AllChenghaos",new ArrayList<>());
        List<Map<?, ?>> chenghaos = config.getMapList("AllChenghaos");

        Chenghao newChenghao = new Chenghao(chenghao, System.currentTimeMillis(), duration == -1 ? -1 : duration*1000, isDefault, priority);

        chenghaos.add(newChenghao.toMap());

        config.set("AllChenghaos", chenghaos);

        YamlUtils.save(config, player.getUniqueId());

        ChenghaoLogger.success(sender, "添加成功!");
    }

    public static List<ChenghaoIndex> getPlayerNowChenghao(Player player) {
        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());

        return config.getMapList("NowUsing").stream().map(ChenghaoIndex::fromMap).collect(Collectors.toList());
    }

    public static void savePlayerNowUsing(Player player, List<ChenghaoIndex> nowUsings) {
        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());

        config.set("NowUsing", serializeNowUsings(nowUsings));

        YamlUtils.save(config, player.getUniqueId());
    }

    public static boolean playerCantUseMoreChenghao(Player player, int now) {

        int max = 1;
        if (player.hasPermission("xchenghao.maxusing.unlimited")) {
            return false;
        } else {
            for (int i = 100; i > 0; i--) {
                if (player.hasPermission("xchenghao.maxusing."+i)) {
                    max = i;
                    break;
                }
            }

            return (now >= max);
        }
    }

    public static void appendPlayerNowChenghao(Player player, int id) {

        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());

        List<Map<?, ?>> chenghaos = config.getMapList("AllChenghaos");

        List<ChenghaoIndex> nowUsings = getPlayerNowChenghao(player);

        if (chenghaos.size() <= id) {
            ChenghaoLogger.error(player, "输入的编号不存在");
            return;
        } else if (elemInIndexes(id, nowUsings)) {
            ChenghaoLogger.error(player, "您已经佩戴此称号");
            return;
        } else {
            ChenghaoLogger.success(player, "已成功替换!");
        }
        if (playerCantUseMoreChenghao(player, nowUsings.size())) {
            ChenghaoLogger.error(player, "您的称号佩戴已达上限，自动取消佩戴最后一个称号");
            nowUsings.remove(nowUsings.size()-1);
        }

        nowUsings.add(0,new ChenghaoIndex(id));

        config.set("NowUsing", serializeNowUsings(nowUsings));

        YamlUtils.save(config, player.getUniqueId());

        TextUtils.changePlayerName(player, ChenghaoManager.renderPlayerChenghao(player));
    }

    public static void clearPlayerNowUsingChenghao(Player player) {
        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());
        config.set("NowUsing", Collections.EMPTY_LIST);
        YamlUtils.save(config, player.getUniqueId());

        TextUtils.changePlayerName(player, Collections.EMPTY_LIST);
    }

    public static void appendPlayerNowPermChenghao(Player player, String permission) {
        if (!player.hasPermission(permission)) {
            ChenghaoLogger.error(player, "您没有权限 "+permission);
            return;
        }

        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());

        List<ChenghaoIndex> nowUsings = getPlayerNowChenghao(player);
        if (playerCantUseMoreChenghao(player, nowUsings.size())) {
            ChenghaoLogger.error(player, "您的称号佩戴已达上限，自动取消佩戴最后一个称号");
            nowUsings.remove(nowUsings.size()-1);
        }
        if (elemInIndexes(permission, nowUsings)) {
            ChenghaoLogger.error(player, "您已经佩戴此称号");
            return;
        }

        nowUsings.add(0,new ChenghaoIndex("permission", permission));

        config.set("NowUsing", serializeNowUsings(nowUsings));
        YamlUtils.save(config, player.getUniqueId());

        ChenghaoLogger.success(player, "已成功替换!");

        TextUtils.changePlayerName(player, ChenghaoManager.renderPlayerChenghao(player));
    }

    public static void appendPlayerPlaceholderChenghao(Player player, String placeholder) {

        PlaceholderChenghao pc = ChenghaoCache.PLACEHOLDER_CACHE.get(placeholder);
        if (!PlaceholderAPI.setPlaceholders(player, placeholder).equals(pc.getValue())) {
            ChenghaoLogger.error(player, "您的placeholder不符合");
            return;
        }

        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());

        List<ChenghaoIndex> nowUsings = getPlayerNowChenghao(player);
        if (playerCantUseMoreChenghao(player, nowUsings.size())) {
            ChenghaoLogger.error(player, "您的称号佩戴已达上限，自动取消佩戴最后一个称号");
            nowUsings.remove(nowUsings.size()-1);
        }
        if (elemInIndexes(placeholder, nowUsings)) {
            ChenghaoLogger.error(player, "您已经佩戴此称号");
            return;
        }

        nowUsings.add(0,new ChenghaoIndex("placeholder", placeholder));

        config.set("NowUsing", serializeNowUsings(nowUsings));
        YamlUtils.save(config, player.getUniqueId());

        ChenghaoLogger.success(player, "已成功替换!");

        TextUtils.changePlayerName(player, ChenghaoManager.renderPlayerChenghao(player));
    }

    public static void removePlayerChenghao(CommandSender sender, Player player, int id) {
        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());
        List<ChenghaoIndex> nowUsings = getPlayerNowChenghao(player);
        List<Map<?, ?>> chenghaos = config.getMapList("AllChenghaos");

        if (elemInIndexes(id, nowUsings)) {

            nowUsings = nowUsings.stream().filter(index -> !index.equals(id)).collect(Collectors.toList());
            config.set("NowUsing", nowUsings);
            YamlUtils.save(config, player.getUniqueId());

            TextUtils.changePlayerName(player, renderPlayerChenghao(player));
            ChenghaoLogger.error(player, "您的称号 "+chenghaos.get(id).get("name")+" &c已被移除");
        }
        if (chenghaos.size() <= id) {
            ChenghaoLogger.error(sender, "不存在此编号!");
        } else {
            chenghaos.remove(id);
            ChenghaoLogger.success(sender, "删除成功!");
        }
        config.set("AllChenghaos", chenghaos);

        YamlUtils.save(config, player.getUniqueId());
    }

    public static List<Chenghao> getPlayerAllChenghao(UUID uuid) {
        YamlConfiguration config = YamlUtils.getPlayerData(uuid);
        List<Chenghao> chenghaos = new ArrayList<>();

        for(Map<?, ?> map : config.getMapList("AllChenghaos")) {
            Chenghao chenghao = Chenghao.fromMap(map);
            chenghao.setName(TextUtils.colorize(chenghao.getName()));
            chenghaos.add(chenghao);
        }
        return chenghaos;
    }

    public static void orderNowUsingByPriority(Player player, List<ChenghaoIndex> nowUsing) {

        List<Chenghao> chenghaos = getPlayerAllChenghao(player.getUniqueId());
        Function<ChenghaoIndex, Integer> getPriority = (c) -> {

            switch (c.getType()) {
                case "custom":
                    return chenghaos.get(c.getIntValue()).getPriority();
                case "permission":
                    PermissionChenghao pc = ChenghaoCache.PERMISSION_CACHE.get(c.getStringValue());
                    return pc.getPriority();
                case "placeholder":
                    PlaceholderChenghao pc2 = ChenghaoCache.PLACEHOLDER_CACHE.get(c.getStringValue());
                    return pc2.getPriority();
                default:
                    return 0;
            }
        };

        nowUsing.sort((o1, o2) -> getPriority.apply(o2).compareTo(getPriority.apply(o1)));
    }

    public static List<String> renderPlayerChenghao(Player player) {

        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());
        List<ChenghaoIndex> nowUsings = getPlayerNowChenghao(player);
        List<String> chenghaos = new ArrayList<>();
        List<Map<?, ?>> maps = config.getMapList("AllChenghaos");

        orderNowUsingByPriority(player, nowUsings);

        if (nowUsings.isEmpty()) {
            return Collections.EMPTY_LIST;
        } else {
            for (ChenghaoIndex nowUsing : nowUsings) {
                switch (nowUsing.getType()) {
                    case "custom":
                        int customChenghao = nowUsing.getIntValue();
                        chenghaos.add(String.valueOf(maps.get(customChenghao).get("name")));
                        break;
                    case "permission":
                        PermissionChenghao permChenghao = ChenghaoCache.PERMISSION_CACHE.get(nowUsing.getStringValue());
                        chenghaos.add(permChenghao.getName());
                        break;
                    case "placeholder":
                        PlaceholderChenghao pc = ChenghaoCache.PLACEHOLDER_CACHE.get(nowUsing.getStringValue());
                        chenghaos.add(pc.getName());
                }
            }
        }
        return chenghaos;
    }

    private static boolean elemInIndexes(Object obj, List<ChenghaoIndex> indexes) {
        return indexes.stream().anyMatch(index -> index.equals(obj));
    }

    private static List<Map<String, String>> serializeNowUsings(List<ChenghaoIndex> nowUsings) {
        return nowUsings.stream().map(ChenghaoIndex::toMap).collect(Collectors.toList());
    }

}
