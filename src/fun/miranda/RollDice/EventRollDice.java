package fun.miranda.RollDice;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class EventRollDice implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    private void playerRollDice(AsyncPlayerChatEvent event) {
        String chat = event.getMessage();
        if (!chat.startsWith(".")) {
            return;
        }
        RollDice rollDice = new RollDice(event.getPlayer(), chat);
        if (rollDice.validate) {
            Bukkit.getServer().broadcastMessage(rollDice.result);
            event.setCancelled(true);
        }
    }
}
