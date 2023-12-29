package github.samyycx.xtopiachenghao;

import github.samyycx.xtopiachenghao.other.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class ChenghaoManager {

    public static void givePlayerChenghao(CommandSender sender, Player player, String chenghao, int duration, boolean isDefault) {
        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());

        config.addDefault("AllChenghaos",new ArrayList<>());
        List<Map<?, ?>> chenghaos = config.getMapList("AllChenghaos");

        Chenghao newChenghao = new Chenghao(chenghao, System.currentTimeMillis(), duration == -1 ? -1 : duration*1000, isDefault);

        chenghaos.add(newChenghao.toMap());

        config.set("AllChenghaos", chenghaos);

        YamlUtils.save(config, player.getUniqueId());

        ChenghaoLogger.success(sender, "添加成功!");
    }

    public static String getPlayerNowChenghao(Player player) {

        if (ChenghaoCache.CACHE.containsKey(player.getUniqueId())) {
            return ChenghaoCache.CACHE.get(player.getUniqueId());
        }

        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());

        int nowUsing = config.getInt("NowUsing");
        String chenghao;

        if (nowUsing == -1) {
            chenghao = "";
        } else if (nowUsing == -2) {
            chenghao = ChenghaoCache.PERMISSION_CACHE.get(config.getString("NowUsingPermission"));
        } else {
            if (config.contains("AllChenghaos")) {
                List<Map<?, ?>> maps = config.getMapList("AllChenghaos");
                if (maps.size() > 0) {
                    chenghao = String.valueOf(maps.get(nowUsing).get("name"));
                } else {
                    chenghao = "";
                }
            } else {
                chenghao = "";
            }

        }

        ChenghaoCache.CACHE.put(player.getUniqueId(), chenghao);

        return A.a(chenghao);

    }

    public static void switchPlayerNowChenghao(Player player, int id) {

        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());

        List<Map<?, ?>> chenghaos = config.getMapList("AllChenghaos");

        if (chenghaos.size() <= id) {
            ChenghaoLogger.error(player, "输入的编号不存在");
            return;
        } else {
            ChenghaoLogger.success(player, "已成功替换!");
        }
        String chenghao = id == -1 ? "" : (String) chenghaos.get(id).get("name");
        ChenghaoCache.CACHE.put(player.getUniqueId(), A.a(chenghao));

        config.set("NowUsing", id);
        config.set("NowUsingPermission","");

        A.changePlayerName(player, chenghao);

        YamlUtils.save(config, player.getUniqueId());
    }

    public static void switchPlayerNowPermChenghao(Player player, String permission) {
        if (!player.hasPermission(permission)) {
            ChenghaoLogger.error(player, "您没有权限 "+permission);
            return;
        }

        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());

        config.set("NowUsing", -2);
        config.set("NowUsingPermission", permission);

        ChenghaoCache.CACHE.put(player.getUniqueId(), ChenghaoCache.PERMISSION_CACHE.get(permission));

        YamlUtils.save(config, player.getUniqueId());

        A.changePlayerName(player, ChenghaoCache.PERMISSION_CACHE.get(permission));

        ChenghaoLogger.success(player, "已成功替换!");
    }

    public static void removePlayerChenghao(CommandSender sender, Player player, int id) {
        YamlConfiguration config = YamlUtils.getPlayerData(player.getUniqueId());

        List<Map<?, ?>> chenghaos = config.getMapList("AllChenghaos");

        if (chenghaos.size() <= id) {
            ChenghaoLogger.error(sender, "不存在此编号!");
        } else {
            chenghaos.remove(id);
            ChenghaoLogger.success(sender, "删除成功!");
        }

        if (config.getInt("NowUsing") == id) {
            if (chenghaos.size() > 0) {
                switchPlayerNowChenghao(player, 0);
                ChenghaoLogger.error(player, "由于您的称号已过期或被删除，已自动为您替换为您背包中第一个称号");
            } else {
                switchPlayerNowChenghao(player, -1);
                ChenghaoLogger.error(player, "由于您的称号已过期或被删除，且您没有其他称号，目前称号已被替换为空");
            }
        }

        config.set("AllChenghaos", chenghaos);

        YamlUtils.save(config, player.getUniqueId());
    }

    public static List<Chenghao> getPlayerAllChenghao(UUID uuid) {
        YamlConfiguration config = YamlUtils.getPlayerData(uuid);
        List<Chenghao> chenghaos = new ArrayList<>();

        for(Map<?, ?> map : config.getMapList("AllChenghaos")) {
            Chenghao chenghao = Chenghao.fromMap(map);
            chenghao.setName(A.a(chenghao.getName()));
            chenghaos.add(chenghao);
        }
        return chenghaos;
    }

}
