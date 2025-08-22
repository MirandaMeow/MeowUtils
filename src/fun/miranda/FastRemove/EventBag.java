package fun.miranda.FastRemove;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;


public class EventBag implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    private void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String inventoryName = event.getView().getTitle();
        if (!inventoryName.equals("§9方块背包")) {
            return;
        }
        Selection.getSelection(player).getBlockBag().fillNow();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        String inventoryName = event.getView().getTitle();
        if (!inventoryName.equals("§9方块背包")) {
            return;
        }
        Selection.getSelection(player).getBlockBag().fillNow();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        String inventoryName = event.getView().getTitle();
        if (!inventoryName.equals("§9方块背包")) {
            return;
        }

        BlockBag blockBag = Selection.getSelection(player).getBlockBag();
        if (blockBag.isEmpty()) {
            Selection.getSelection(player).reset();
        } else {
            player.sendMessage(String.format("§e剩余物品: §b%d", blockBag.getCount()));
        }
    }
}
