package fun.miranda.TpsSpam;

import fun.miranda.MeowUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandTpsSpam implements TabExecutor {
    private static boolean flag = true;

    public static void TPSClock(MeowUtils plugin) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (flag) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "tps");
            }
        }, 0L, 200L);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("该命令只能由玩家执行");
            return true;
        }
        flag = !flag;
        sender.sendMessage(String.format("TPSSpam 开关设置为 %s", flag));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
