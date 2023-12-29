package github.samyycx.xtopiachenghao;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class ChenghaoListener implements Listener {


    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        //String msg = ChenghaoManager.getPlayerNowChenghao(e.getPlayer()) + A.a( " &r")+e.getFormat();
        //e.setFormat(msg);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        String chenghao = ChenghaoManager.getPlayerNowChenghao(e.getPlayer());

        A.changePlayerName(e.getPlayer(), chenghao);
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

                if (!item.getItemMeta().hasLore()) {
                    return;
                }

                if (item.getItemMeta().getLore().get(0).startsWith(A.a("&e剩余时间"))) {
                    ChenghaoManager.switchPlayerNowChenghao(player, e.getRawSlot());
                } else if (item.getItemMeta().getLore().get(0).startsWith(A.a("&e绑定权限"))) {
                    String perm = item.getItemMeta().getLore().get(0).replace(A.a("&e绑定权限: &c"), "");
                    ChenghaoManager.switchPlayerNowPermChenghao(player, perm);
                }

            }
        }
    }

}
