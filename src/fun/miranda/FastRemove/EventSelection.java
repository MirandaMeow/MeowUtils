package fun.miranda.FastRemove;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class EventSelection implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    private void selection(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("meowutils.fastremove")) {
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD)) {
            return;
        }
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        event.setCancelled(true);
        Selection selection = Selection.getSelection(player);
        Block block = event.getClickedBlock();
        assert block != null;
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            selection.setPos1(block.getLocation());
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            player.sendMessage(String.format("§e选区的起始位置: §a(§b%d§e, §b%d§e, §b%d§a)", x, y, z));
        } else {
            selection.setPos2(block.getLocation());
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            player.sendMessage(String.format("§e选区的终止位置: §a(§b%d§e, §b%d§e, §b%d§a)", x, y, z));
        }
    }
}
