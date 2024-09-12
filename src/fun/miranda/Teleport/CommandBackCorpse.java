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
            sender.sendMessage("后台怎么个死法？");
            return true;
        }
        if (!EventPlayerDeath.playerDeathLocation.containsKey(player)) {
            player.sendMessage("§c可惜，本次登录还没死过");
            return true;
        }
        if (EventPlayerDeath.playerDeathLocation.get(player) == null) {
            player.sendMessage("§c你已回上辈子终点探望过了");
            return true;
        }
        player.teleport(EventPlayerDeath.playerDeathLocation.get(player));
        EventPlayerDeath.playerDeathLocation.put(player, null);
        player.sendMessage("§e回到了上辈子的终点");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
