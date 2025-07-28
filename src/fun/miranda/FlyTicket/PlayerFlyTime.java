package fun.miranda.FlyTicket;

import fun.miranda.MeowUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerFlyTime {
    public static Map<Player, Integer> playerFlyTime = new HashMap<>();
    public static Map<Player, TaskFlyTiming> tasks = new HashMap<>();

    private final String path;
    private final Player player;
    public boolean flyEnabled;

    public PlayerFlyTime(Player player) {
        UUID uuid = player.getUniqueId();
        this.path = "players." + uuid + ".flytime";
        this.player = player;

        if (!tasks.containsKey(player)) {
            this.flyEnabled = false;
        } else {
            TaskFlyTiming task = tasks.get(player);
            this.flyEnabled = !task.isCancelled();
        }

        if (MeowUtils.plugin.config.get(this.path) == null) {
            MeowUtils.plugin.config.set(this.path, 0);
            MeowUtils.plugin.saveConfig();
        }
        Integer flyTime = MeowUtils.plugin.config.getInt(this.path);
        if (!playerFlyTime.containsKey(player)) {
            playerFlyTime.put(player, flyTime);
        }
    }

    public Integer getFlyTime() {
        return playerFlyTime.get(this.player);
    }

    public void saveFlyTime() {
        MeowUtils.plugin.config.set(this.path, playerFlyTime.get(this.player));
        MeowUtils.plugin.saveConfig();
    }

    public static void saveALL() {
        for (Player player : playerFlyTime.keySet()) {
            PlayerFlyTime playerFlyTime = new PlayerFlyTime(player);
            playerFlyTime.saveFlyTime();
        }
    }

    public void addFlyTime(Integer addFlyTime) {
        playerFlyTime.put(player, playerFlyTime.get(player) + addFlyTime);
        this.saveFlyTime();
    }

    public void startFly() {
        if (this.flyEnabled) {
            return;
        }
        if (this.getFlyTime() > 0) {
            this.player.setAllowFlight(true);
            this.player.setFlying(true);
            TaskFlyTiming task = new TaskFlyTiming(this.player);
            tasks.put(player, task);
            this.flyEnabled = true;
        }
    }

    public void stopFly() {
        if (!this.flyEnabled) {
            return;
        }
        TaskFlyTiming task = tasks.get(this.player);
        if (task != null) {
            if (!task.isCancelled()) {
                task.stop();
                this.player.setAllowFlight(false);
                this.player.setFlying(false);
            }
        }
        this.saveFlyTime();
        this.flyEnabled = false;
    }
}
