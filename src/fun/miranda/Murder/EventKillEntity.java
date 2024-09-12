package fun.miranda.Murder;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

public class EventKillEntity implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    private void killEntity(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking() || player.getInventory().getItemInMainHand().getType() != Material.BLAZE_ROD) {
            return;
        }
        event.setCancelled(true);
        Entity targetEntity = getPlayerSeeEntity(player);
        if (targetEntity == null) {
            return;
        }
        if (!(targetEntity instanceof Damageable damageableTarget)) {
            return;
        }
        if (targetEntity instanceof Player) {
            return;
        }
        damageableTarget.remove();
        player.getServer().broadcastMessage(String.format("§e%s: §bAvada Kedavra!", player));
    }

    private Entity getPlayerSeeEntity(Player player) {
        Location playerEyeLocation = player.getEyeLocation();
        Location adjustedLocation = playerEyeLocation.add(playerEyeLocation.getDirection().multiply(0.62));
        RayTraceResult result = player.getWorld().rayTraceEntities(adjustedLocation, playerEyeLocation.getDirection(), 10);
        return result.getHitEntity();
    }
}
