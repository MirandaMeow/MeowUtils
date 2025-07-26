package fun.miranda.Teleport;

import fun.miranda.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandTPHere implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("该命令只能由玩家执行");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§c用法: /tphere <player>");
            return true;
        }

        String target = args[0];

        if (!Utils.getAllPlayersName().contains(target)) {
            sender.sendMessage("§c指定玩家不在线");
        } else {
            Player targetPlayer = Bukkit.getPlayer(target);
            assert targetPlayer != null;
            targetPlayer.teleport(player.getLocation());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                players.add(player.getName());
            }
            return players;
        }
        return new ArrayList<>();
    }
}
