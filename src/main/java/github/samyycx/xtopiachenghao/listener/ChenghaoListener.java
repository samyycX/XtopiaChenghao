package github.samyycx.xtopiachenghao.listener;

import github.samyycx.xtopiachenghao.utils.TextUtils;
import github.samyycx.xtopiachenghao.chenghao.ChenghaoManager;
import github.samyycx.xtopiachenghao.utils.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ChenghaoListener implements Listener {


    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        //String msg = ChenghaoManager.getPlayerNowChenghao(e.getPlayer()) + A.a( " &r")+e.getFormat();
        //e.setFormat(msg);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        List<String> chenghao = ChenghaoManager.renderPlayerChenghao(e.getPlayer());

        TextUtils.changePlayerName(e.getPlayer(), chenghao);
    }

    @EventHandler
    public void onClickStorage(InventoryClickEvent e) {

        if (e.getAction() != null && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if (e.getWhoClicked().getOpenInventory().getTitle().equals(YamlUtils.getStorageTitle())) {
                e.setCancelled(true);
            }
        }


        if (e.getClickedInventory() != null && e.getClickedInventory().getTitle().equals(YamlUtils.getStorageTitle())) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {

                e.setResult(Event.Result.DENY);

                ItemStack item = e.getCurrentItem();

                if (item.getType() == Material.AIR) {
                    e.setCancelled(true);
                    return;
                }

                Player player = Bukkit.getPlayer(e.getWhoClicked().getUniqueId());

                if (item.getType() == Material.REDSTONE_BLOCK) {
                    ChenghaoManager.clearPlayerNowUsingChenghao(player);
                    return;
                }

                if (!item.getItemMeta().hasLore()) {
                    return;
                }

                if (item.getItemMeta().getLore().get(0).startsWith(TextUtils.colorize("&e剩余时间"))) {
                    ChenghaoManager.appendPlayerNowChenghao(player, e.getRawSlot());
                } else if (item.getItemMeta().getLore().get(0).startsWith(TextUtils.colorize("&e绑定权限"))) {
                    String perm = item.getItemMeta().getLore().get(0).replace(TextUtils.colorize("&e绑定权限: &c"), "");
                    ChenghaoManager.appendPlayerNowPermChenghao(player, perm);
                } else if (item.getItemMeta().getLore().get(0).startsWith(TextUtils.colorize("&e绑定Placeholder"))) {
                    String placeholder = item.getItemMeta().getLore().get(0).replace(TextUtils.colorize("&e绑定Placeholder: &c"), "");
                    ChenghaoManager.appendPlayerPlaceholderChenghao(player, placeholder);
                }

            }
        }
    }

}
