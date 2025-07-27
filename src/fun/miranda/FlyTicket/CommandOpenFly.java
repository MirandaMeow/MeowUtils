package fun.miranda.FlyTicket;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandOpenFly implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("该命令只能由玩家执行");
            return true;
        }
        PlayerFlyTime playerFlyTime = new PlayerFlyTime(player);
        Integer flyTime = playerFlyTime.getFlyTime();
        if (playerFlyTime.flyEnabled) {
            playerFlyTime.stopFly();
            player.sendMessage(String.format("§e飞行设置为§b关§e, 剩余时长 §b%d §e秒", flyTime));
        } else {
            if (playerFlyTime.getFlyTime() <= 0) {
                player.sendMessage("§e飞行时长用完了");
                return true;
            }
            playerFlyTime.startFly();
            player.sendMessage(String.format("§e飞行设置为§b开§e, 剩余时长 §b%d §e秒", flyTime));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        return List.of();
    }
}
