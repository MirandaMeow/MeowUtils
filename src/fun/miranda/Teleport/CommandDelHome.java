package fun.miranda.Teleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandDelHome implements TabExecutor {
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
            sender.sendMessage("§e用法: §e/delhome <homename>");
            return true;
        }
        String homeName = args[0];
        Player player = (Player) sender;
        PlayerTeleport teleport = new PlayerTeleport(player.getUniqueId());
        boolean result = teleport.delHome(homeName);
        if (result) {
            sender.sendMessage("§e成功删除家 §b" + homeName);
        } else {
            sender.sendMessage("§c不存在这个家");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            Player player = (Player) sender;
            return new PlayerTeleport(player.getUniqueId()).getHomeNames();
        }
        return new ArrayList<>();
    }
}
