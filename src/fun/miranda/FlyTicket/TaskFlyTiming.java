package fun.miranda.FlyTicket;

import fun.miranda.MeowUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskFlyTiming {
    private final BukkitTask task;
    private boolean isCancelled;
    private final List<Integer> alarmFlyTime = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 10, 30, 60));

    public TaskFlyTiming(Player player) {
        this.isCancelled = false;
        this.task = Bukkit.getScheduler().runTaskTimer(MeowUtils.plugin, () -> {
            Integer flyTime = PlayerFlyTime.playerFlyTime.get(player);
            if (flyTime > 0) {
                flyTime -= 1;
                PlayerFlyTime.playerFlyTime.put(player, flyTime);
                if (alarmFlyTime.contains(flyTime)) {
                    player.sendMessage(String.format("§e飞行时长剩余 §b%d §e秒", flyTime));
                }
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
