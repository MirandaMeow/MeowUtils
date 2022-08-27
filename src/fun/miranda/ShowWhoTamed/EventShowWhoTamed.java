package fun.miranda.ShowWhoTamed;

import org.bukkit.Material;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EventShowWhoTamed implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    private void BoneTorture(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }
        if (!(event.getEntity() instanceof Tameable entity)) {
            return;
        }
        if (!player.getInventory().getItemInMainHand().getType().equals(Material.BONE)) {
            return;
        }
        if (entity.isTamed()) {
            AnimalTamer owner = entity.getOwner();
            assert owner != null;
            player.sendMessage(String.format("§e这是 §b%s §e驯服的 §b%s", owner.getName(), entity.getType()));
        } else {
            player.sendMessage(String.format("§b%s §e没有被驯服", entity.getType()));
        }
        event.setCancelled(true);
    }
}
