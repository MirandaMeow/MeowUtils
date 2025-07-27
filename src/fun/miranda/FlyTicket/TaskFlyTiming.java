package fun.miranda.FlyTicket;

import fun.miranda.MeowUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class TaskFlyTiming {
    private final BukkitTask task;
    private boolean isCancelled;

    public TaskFlyTiming(Player player) {
        this.isCancelled = false;
        this.task = Bukkit.getScheduler().runTaskTimer(MeowUtils.plugin, () -> {
            Integer flyTime = PlayerFlyTime.playerFlyTime.get(player);
            if (flyTime > 0) {
                PlayerFlyTime.playerFlyTime.put(player, PlayerFlyTime.playerFlyTime.get(player) - 1);
            } else {
                player.sendMessage("§e飞行时长用完了");
                PlayerFlyTime playerFlyTime = new PlayerFlyTime(player);
                playerFlyTime.stopFly();
                this.stop();
            }
        }, 0L, 20L);
    }

    public void stop() {
        this.task.cancel();
        this.isCancelled = true;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }
}
