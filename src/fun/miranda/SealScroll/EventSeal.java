package fun.miranda.SealScroll;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Random;

public class EventSeal implements Listener {
    private final Random random = new Random();

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
        if (!SealScroll.isSealScroll(handItem)) {
            return;
        }
        SealScroll scroll = new SealScroll(handItem);
        if (scroll.isSealed) {
            player.sendMessage("§c卷轴已经封印了生物");
            return;
        }
        if (!(event.getRightClicked() instanceof LivingEntity entity)) {
            player.sendMessage("§c该实体不是活物");
            return;
        }
        if (entity instanceof Player) {
            player.sendMessage("§c不能封印玩家");
            return;
        }
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
        if (!SealScroll.isSealScroll(handItem)) {
            return;
        }
        if (player.isSneaking()) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        SealScroll scroll = new SealScroll(handItem);
        if (!scroll.isSealed) {
            return;
        }
        scroll.unleash(player);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void dropItem(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Damageable entity)) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            return;
        }
        if (entity.getHealth() > event.getFinalDamage()) {
            return;
        }
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            return;
        }
        int chance = random.nextInt(100) + 1;
        if (chance == 10) {
            entity.getWorld().dropItem(entity.getLocation(), SealScroll.getBlankScroll());
        }
    }
}
