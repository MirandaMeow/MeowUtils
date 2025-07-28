package fun.miranda.FlyTicket;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class EventFlyTicket implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    private void playerUseTicket(PlayerInteractEvent event) {
        if (event.getHand() == null) {
            return;
        }
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (!FlyTicket.isTicket(handItem)) {
            return;
        }
        FlyTicket flyTicket = new FlyTicket(handItem);
        PlayerFlyTime playerFlyTime = new PlayerFlyTime(player);
        Integer flyTime = flyTicket.getFlyTime();
        playerFlyTime.addFlyTime(flyTime);
        handItem.setAmount(handItem.getAmount() - 1);
        Integer newFlyTime = playerFlyTime.getFlyTime();
        player.sendMessage(String.format("§e飞行时长增加了 §b%d §e秒, 现在飞行时长为 §b%d §e秒", flyTime, newFlyTime));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerFlyTime playerFlyTime = new PlayerFlyTime(player);
        if (playerFlyTime.isFlyEnabled()) {
            playerFlyTime.stopFly();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void playerDead(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerFlyTime playerFlyTime = new PlayerFlyTime(player);
        if (playerFlyTime.isFlyEnabled()) {
            playerFlyTime.stopFly();
        }
    }
}
