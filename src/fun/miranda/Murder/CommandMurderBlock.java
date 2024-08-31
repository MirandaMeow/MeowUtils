package fun.miranda.Murder;

import fun.miranda.Utils.Utils;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandMurderBlock implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof BlockCommandSender block)) {
            sender.sendMessage("该命令只能由命令方块执行");
            return true;
        }
        if (args.length < 2) {
            block.sendMessage("§e用法: §6/murderblock §b<distance> <EntityType...>");
            return true;
        }
        String distanceString = args[0];
        if (!Utils.isInt(distanceString)) {
            block.sendMessage("§c输入的距离不正确");
            return true;
        }
        int distance = Integer.parseInt(distanceString);
        if (distance <= 0) {
            block.sendMessage("§c输入的距离不正确");
            return true;
        }
        Murder murder = new Murder(block, distance);
        int success = 0;
        for (int i = 1; i < args.length; i++) {
            success += murder.modifyType(args[i]);
        }
        if (success != 0) {
            block.sendMessage("§c输入的实体不正确");
            return true;
        }
        int count = murder.kill();
        block.sendMessage(String.format("§e清除了 §b%d §e个实体", count));
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
