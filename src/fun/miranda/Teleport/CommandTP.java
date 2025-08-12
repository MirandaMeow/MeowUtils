package fun.miranda.Teleport;

import fun.miranda.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandTP implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("该命令只能由玩家执行");
            return true;
        }
        switch (args.length) {
            case 1:
                String target = args[0];
                if (!Utils.getAllPlayersName().contains(target)) {
                    sender.sendMessage("§c指定玩家不在线");
                } else {
                    player.teleport(Bukkit.getPlayer(target).getLocation());
                }
                break;
            case 3:
                String x = args[0];
                String y = args[1];
                String z = args[2];
                try {
                    double dx = Double.parseDouble(x);
                    double dy = Double.parseDouble(y);
                    double dz = Double.parseDouble(z);
                    Location location = new Location(player.getWorld(), dx, dy, dz);
                    player.teleport(location);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§c坐标不正确");
                    return true;
                }
                break;
            default:
                sender.sendMessage("§c用法: /tp <player> | <x> <y> <z>");
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            List<String> players = Utils.getAllPlayersName();
            return Utils.listFilter(players, args[0]);
        }
        return new ArrayList<>();
    }
}
