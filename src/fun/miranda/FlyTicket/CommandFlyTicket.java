package fun.miranda.FlyTicket;

import fun.miranda.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandFlyTicket implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§e用法 §6/flyticket §b<player> <flyTime>");
            return true;
        }
        String playerName = args[0];
        String flyTimeString = args[1];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("§c指定玩家不在线");
            return true;
        }
        if (!Utils.isInt(flyTimeString)) {
            sender.sendMessage("§c飞行时长不正确");
            return true;
        }
        Integer flyTime = Integer.parseInt(flyTimeString);
        if (flyTime < 1 || flyTime > 65536) {
            sender.sendMessage("§c飞行时长范围应为 1 - 65535 之间");
            return true;
        }
        FlyTicket flyTicket = new FlyTicket(flyTime);
        boolean success = flyTicket.giveFlyTicketItem(player);
        if (success) {
            sender.sendMessage(String.format("§e给玩家 §b%s §e发放了时长为 §b%d §e秒的飞行券", playerName, flyTime));
            player.sendMessage(String.format("§e收到了时长为 §b%d §e秒的飞行券", flyTime));
        } else {
            sender.sendMessage(String.format("§e玩家 §b%s §e的背包没有空格了", playerName));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            return Utils.listFilter(Utils.getAllPlayersName(), args[0]);
        }
        return new ArrayList<>();
    }
}
