package fun.miranda.Teleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class EventPlayerDeath implements Listener {
    public static HashMap<Player, Location> playerDeathLocation = new HashMap<>();

    @EventHandler(priority = EventPriority.NORMAL)
    private void logPlayerDeathLocation(PlayerDeathEvent event) {
        Player player = event.getEntity();
        playerDeathLocation.put(player, player.getLocation());
    }
}