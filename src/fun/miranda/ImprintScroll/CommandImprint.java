package fun.miranda.ImprintScroll;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandImprint implements TabExecutor {
    public static boolean isInt(String string) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§e用法 §6/imprint §b[player] <amount>");
            return true;
        }
        String inputName = args[0];
        String amount = args[1];
        Player player = Bukkit.getPlayer(inputName);
        if (player == null) {
            sender.sendMessage("§c指定玩家不在线");
            return true;
        }
        String playerName = player.getName();
        if (!isInt(amount) || amount.equals("0")) {
            sender.sendMessage("§c数量不正确");
            return true;
        }
        int miss = ImprintScroll.giveScroll(player, Integer.parseInt(amount));
        if (miss == 0) {
            sender.sendMessage(String.format("§e将 §b%s §e个§9§l烙印卷轴§r§e给 §b%s", amount, playerName));
            player.sendMessage(String.format("§e收到了 §b%s §e个§9§l烙印卷轴§r", amount));
        } else {
            sender.sendMessage(String.format("§e将 §b%s §e个§9§l烙印卷轴§r§e给 §b%s §e, 其中 §b%d §e个因为空间不足已丢失", amount, playerName, miss));
            player.sendMessage(String.format("§e收到了 §b%s §e个§9§l烙印卷轴§r§e, 其中 §b%d §e个因为空间不足已丢失", amount, miss));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        if (args.length == 1) {
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                players.add(player.getName());
            }
            return players;
        }
        if (args.length == 2) {
            return new ArrayList<>(Arrays.asList("1", "5", "10", "64"));
        }
        return new ArrayList<>();
    }
}
