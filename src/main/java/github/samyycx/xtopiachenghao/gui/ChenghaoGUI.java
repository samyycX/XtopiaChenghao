package github.samyycx.xtopiachenghao.gui;

import github.samyycx.xtopiachenghao.chenghao.*;
import github.samyycx.xtopiachenghao.utils.TextUtils;
import github.samyycx.xtopiachenghao.utils.YamlUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChenghaoGUI {

    public static void showPlayer(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 6*9, YamlUtils.getStorageTitle());

        int index = 0;

        for (Chenghao chenghao : ChenghaoManager.getPlayerAllChenghao(player.getUniqueId())) {
            ItemStack item = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(chenghao.getName());

            String remainTime;
            if (chenghao.getDuration() == -1) {
                remainTime = TextUtils.colorize("&c永久");
            } else {
                long distance = chenghao.getTimeOnCreated() + chenghao.getDuration() - System.currentTimeMillis();
                long second = distance / 1000;

                long days = TimeUnit.SECONDS.toDays(second);
                long hours = TimeUnit.SECONDS.toHours(second) - TimeUnit.DAYS.toHours(days);
                long minutes = TimeUnit.SECONDS.toMinutes(second) - TimeUnit.HOURS.toMinutes(hours) - TimeUnit.DAYS.toMinutes(days);
                long seconds = second - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.DAYS.toSeconds(days) - TimeUnit.HOURS.toSeconds(hours);

                remainTime = TextUtils.colorize(String.format("&c%d&b天&c%02d&b小时&c%02d&b分钟&c%02d&b秒", days, hours, minutes, seconds));
            }

            meta.setLore(new ArrayList<String>() {{
                add(TextUtils.colorize("&e剩余时间: &r")+remainTime);
            }});

            item.setItemMeta(meta);
            inventory.setItem(index, item);

            index++;
        }

        for (Map.Entry<String, PermissionChenghao> entry : ChenghaoCache.PERMISSION_CACHE.entrySet()) {
            String permission = entry.getKey();
            PermissionChenghao chenghao = entry.getValue();

            if (player.hasPermission(permission)) {

                ItemStack item = new ItemStack(Material.NAME_TAG);
                ItemMeta meta = item.getItemMeta();

                meta.setDisplayName(chenghao.getName());

                meta.setLore(new ArrayList<String>() {{
                    add(TextUtils.colorize("&e绑定权限: &c" + permission));
                }});

                item.setItemMeta(meta);

                inventory.setItem(index, item);

                index++;
            }
        }

        for (Map.Entry<String, PlaceholderChenghao> entry : ChenghaoCache.PLACEHOLDER_CACHE.entrySet()) {

            String placeholder = entry.getKey();
            PlaceholderChenghao pc = entry.getValue();

            System.out.println(PlaceholderAPI.setPlaceholders(player, placeholder));
            if (pc.getValue().equals(PlaceholderAPI.setPlaceholders(player, placeholder))) {
                ItemStack item = new ItemStack(Material.NAME_TAG);
                ItemMeta meta = item.getItemMeta();

                meta.setDisplayName(pc.getName());

                meta.setLore(new ArrayList<String>() {{
                    add(TextUtils.colorize("&e绑定Placeholder: &c"+placeholder));
                }});

                item.setItemMeta(meta);

                inventory.setItem(index, item);

                index++;
            }
        }

        ItemStack clearAll = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta meta = clearAll.getItemMeta();
        meta.setDisplayName(TextUtils.colorize("&4点我取消佩戴当前所有称号"));
        clearAll.setItemMeta(meta);
        inventory.setItem(53, clearAll);
        player.openInventory(inventory);
    }

}
