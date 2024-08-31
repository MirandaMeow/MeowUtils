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
    private boolean run = false;
    public static MeowUtils plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof ConsoleCommandSender console)) {
            sender.sendMessage("Only available in console.");
            return true;
        }
        if (!run) {
            TPSClock(plugin);
            run = true;
        }
        flag = !flag;
        sender.sendMessage(String.format("[TPSSpam] Set to %s", flag));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }

    public static void TPSClock(MeowUtils plugin) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (flag) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "tps");
            }
        }, 0L, 200L);
    }
}