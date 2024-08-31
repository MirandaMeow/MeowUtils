package fun.miranda.Murder;

import fun.miranda.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandMurder implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("该命令只能由玩家执行");
            return true;
        }
        if (args.length < 2) {
            player.sendMessage("§e用法: §6/murder §b<distance> <EntityType...>");
            return true;
        }
        String distanceString = args[0];
        if (!Utils.isInt(distanceString)) {
            player.sendMessage("§c输入的距离不正确");
            return true;
        }
        int distance = Integer.parseInt(distanceString);
        if (distance <= 0) {
            player.sendMessage("§c输入的距离不正确");
            return true;
        }
        Murder murder = new Murder(player, distance);
        int success = 0;
        for (int i = 1; i < args.length; i++) {
            success += murder.modifyType(args[i]);
        }
        if (success != 0) {
            player.sendMessage("§c输入的实体不正确");
            return true;
        }
        int count = murder.kill();
        player.sendMessage(String.format("§e清除了 §b%d §e个实体", count));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<EntityType> entityTypes = List.of(EntityType.values());
        List<String> entityTypesString = new ArrayList<>();
        for (EntityType type : entityTypes) {
            entityTypesString.add(type.name());
        }
        if (args.length == 1) {
            return new ArrayList<>();
        }
        if (args.length > 1) {
            return Utils.listFilter(entityTypesString, args[args.length - 1].toUpperCase(Locale.ROOT));
        }
        return new ArrayList<>();
    }
}
