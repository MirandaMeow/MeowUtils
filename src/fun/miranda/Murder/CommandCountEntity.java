package fun.miranda.Murder;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.*;

import static fun.miranda.MeowUtils.plugin;

public class CommandCountEntity implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("该命令只能由控制台执行");
            return true;
        }

        for (World world : plugin.getServer().getWorlds()) {
            HashMap<EntityType, Integer> count = new HashMap<>();
            List<Entity> entities = world.getEntities();
            plugin.logger.info(String.format("世界 %s:", world.getName()));
            for (Entity entity : entities) {
                if (!count.containsKey(entity.getType())) {
                    count.put(entity.getType(), 1);
                } else {
                    int current = count.get(entity.getType());
                    count.put(entity.getType(), current + 1);
                }
            }
            List<Map.Entry<EntityType, Integer>> sorted = sort(count);
            for (Map.Entry<EntityType, Integer> entry : sorted) {
                plugin.logger.info(String.format("  - %s - %d", entry.getKey(), entry.getValue()));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }

    private List<Map.Entry<EntityType, Integer>> sort(HashMap<EntityType, Integer> map) {
        List<HashMap.Entry<EntityType, Integer>> list = new ArrayList<>(map.entrySet());
        Comparator<Map.Entry<EntityType, Integer>> a = (o1, o2) -> o2.getValue().compareTo(o1.getValue());
        list.sort(a);
        return list;
    }
}
