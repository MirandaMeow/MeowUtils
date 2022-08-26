package fun.miranda.Seal;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class EventSeal implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    private void seal(PlayerInteractEntityEvent event) {
        if (Objects.equals(event.getHand(), EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (!player.isSneaking()) {
            return;
        }
        if (!Scroll.isSealScroll(handItem)) {
            return;
        }
        if (!(event.getRightClicked() instanceof LivingEntity entity)) {
            return;
        }
        Scroll scroll = new Scroll(handItem);
        scroll.seal(entity, player);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void unleash(PlayerInteractEvent event) {
        if (event.getHand() == null) {
            return;
        }
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (!Scroll.isSealScroll(handItem)) {
            return;
        }
        if (player.isSneaking()) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Scroll scroll = new Scroll(handItem);
        if (!scroll.isSealed) {
            return;
        }
        assert event.getClickedBlock() != null;
        scroll.unleash(player, event.getClickedBlock().getLocation().add(0, 1, 0));
    }
}
