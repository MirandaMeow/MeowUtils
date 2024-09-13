package fun.miranda.Murder;

import fun.miranda.Utils.Utils;
import org.bukkit.command.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CommandMurder implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Murder murder;
        if (args.length < 2) {
            sender.sendMessage("§e用法: §6/murder §b<distance> <EntityType...>");
            return true;
        }
        String distanceString = args[0];
        if (!Utils.isInt(distanceString)) {
            sender.sendMessage("§c输入的距离不正确");
            return true;
        }
        int distance = Integer.parseInt(distanceString);
        if (distance < 0) {
            sender.sendMessage("§c输入的距离不正确");
            return true;
        }
        if (sender instanceof Player) {
            murder = new Murder((Player) sender, distance);
        } else if (sender instanceof BlockCommandSender) {
            murder = new Murder((BlockCommandSender) sender, distance);
        } else {
            murder = new Murder();
        }
        int success = 0;
        for (int i = 1; i < args.length; i++) {
            success += murder.modifyType(args[i]);
        }
        if (success != 0) {
            sender.sendMessage("§c输入的实体不正确");
            return true;
        }
        int count = murder.kill();
        sender.sendMessage(String.format("§e清除了 §b%d §e个实体", count));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<EntityType> entityTypes = List.of(EntityType.values());
        List<String> entityTypesString = new ArrayList<>();
        for (EntityType type : entityTypes) {
            entityTypesString.add(type.name());
        }
        entityTypesString.add("ALL");
        if (args.length == 1) {
            return new ArrayList<>();
        }
        if (args.length > 1) {
            String arg = args[args.length - 1].toUpperCase(Locale.ROOT);
            if (arg.startsWith("-")) {
                for (int i = 0; i < entityTypesString.size(); i++) {
                    if (Objects.equals(entityTypesString.get(i), "ALL")) {
                        continue;
                    }
                    entityTypesString.set(i, "-" + entityTypesString.get(i));
                }
            }
            return Utils.listFilter(entityTypesString, arg);
        }
        return new ArrayList<>();
    }
}
