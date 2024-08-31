package fun.miranda.Murder;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EventShowEntityType implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    private void showEntityType(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }
        if (!player.isOp()) {
            return;
        }
        if (!player.getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_HOE)) {
            return;
        }
        player.sendMessage(event.getEntity().getName());
        event.setCancelled(true);
    }
}
