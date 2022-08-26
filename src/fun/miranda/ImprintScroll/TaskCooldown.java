package fun.miranda.ImprintScroll;

import fun.miranda.MeowUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class TaskCooldown {
    public static HashMap<Player, Boolean> cooldownMap = new HashMap<>();

    public TaskCooldown(Player player) {
        if (cooldownMap.containsKey(player)) {
            return;
        }
        cooldownMap.put(player, true);
        BukkitTask task = Bukkit.getScheduler().runTaskLater(MeowUtils.plugin, () -> {
            cooldownMap.remove(player);
        }, 100L);
    }

    public static boolean isCoolDown(Player player) {
        return cooldownMap.containsKey(player);
    }
}
