package fun.miranda.Teleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandSetHome implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("该命令只能由玩家执行");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§c未指定家的名称");
            return true;
        }
        if (args.length > 1) {
            sender.sendMessage("§e用法: §e/sethome <homename>");
            return true;
        }
        String homeName = args[0];
        Player player = (Player) sender;
        PlayerTeleport teleport = new PlayerTeleport(player.getUniqueId());
        boolean result = teleport.addHome(player.getLocation(), homeName);
        if (result) {
            sender.sendMessage("§e设置家 §b" + homeName + " §e成功");
        } else {
            sender.sendMessage("§c已经设置了 3 个家, 无法继续设置了");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
