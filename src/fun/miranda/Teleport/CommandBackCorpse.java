package fun.miranda.Teleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandBackCorpse implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("该命令只能由玩家执行");
            return true;
        }
        if (!EventPlayerDeath.playerDeathLocation.containsKey(player)) {
            player.sendMessage("§c没有死亡点");
            return true;
        }
        if (EventPlayerDeath.playerDeathLocation.get(player) == null) {
            player.sendMessage("§c只能回去一次死亡点");
            return true;
        }
        player.teleport(EventPlayerDeath.playerDeathLocation.get(player));
        EventPlayerDeath.playerDeathLocation.put(player, null);
        player.sendMessage("§e已经回到死亡点");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
