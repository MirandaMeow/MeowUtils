package fun.miranda.Teleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandHome implements TabExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("该命令只能由玩家执行");
            return true;
        }
        if (args.length > 1) {
            sender.sendMessage("§e用法: §e/home [homename]");
            return true;
        }
        String homeName = args.length == 1 ? args[0] : "";
        PlayerHome playerHome = new PlayerHome(player);
        String result = playerHome.teleportHome(homeName, player);
        if (!result.equals("")) {
            sender.sendMessage("§e已传送到家 §b" + result);
        } else {
            sender.sendMessage("§c未设置默认家或者家不存在");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            Player player = (Player) sender;
            return new PlayerHome(player).getHomeNames();
        }
        return new ArrayList<>();
    }
}
