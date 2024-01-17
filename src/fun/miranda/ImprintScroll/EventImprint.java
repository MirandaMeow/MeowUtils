package fun.miranda.ImprintScroll;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Random;

public class EventImprint implements Listener {
    private final Random random = new Random();

    @EventHandler(priority = EventPriority.NORMAL)
    private void conclude(PlayerInteractEvent event) {
        if (Objects.equals(event.getHand(), EquipmentSlot.OFF_HAND)) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (!player.isSneaking()) {
            return;
        }
        if (!ImprintScroll.isScroll(handItem)) {
            return;
        }
        ImprintScroll scroll = new ImprintScroll(handItem);
        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage("§c背包里没有空位了");
            return;
        }
        scroll.conclude(player);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void use(PlayerInteractEvent event) {
        if (event.getHand() == null) {
            return;
        }
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (!ImprintScroll.isScroll(handItem)) {
            return;
        }
        if (player.isSneaking()) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        ImprintScroll scroll = new ImprintScroll(handItem);
        if (!scroll.isImprinted) {
            return;
        }
        if (TaskCooldown.isCoolDown(player)) {
            player.sendMessage("§c烙印过于活跃");
            return;
        }
        new TaskCooldown(player);
        scroll.teleport(player);
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
            entity.getWorld().dropItem(entity.getLocation(), ImprintScroll.getBlankScroll());
        }
    }
}
