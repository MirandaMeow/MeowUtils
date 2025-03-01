package fun.miranda.HealthSet;

import fun.miranda.Utils.Utils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

import static fun.miranda.MeowUtils.plugin;

public class CommandHealthSet implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§c该命令只能由Op执行");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("用法: /healthset <player> <value>");
            return true;
        }
        String playerName = args[0];
        String valueString = args[1];
        int value;
        Player player = plugin.getServer().getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("§c指定玩家不在线");
            return true;
        }
        try {
            value = Integer.parseInt(valueString);
        } catch (NumberFormatException e) {
            sender.sendMessage("§c参数错误");
            return true;
        }
        AttributeInstance playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert playerMaxHealth != null;
        playerMaxHealth.setBaseValue(value);
        player.setHealth(value);
        sender.sendMessage(String.format("§e将§b%s§e的生命值上线设为§b%d", playerName, value));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.isOp()) {
            return List.of();
        }
        if (args.length == 1) {
            return Utils.listFilter(Utils.getAllPlayersName(), args[0]);
        }
        return List.of();
    }
}
