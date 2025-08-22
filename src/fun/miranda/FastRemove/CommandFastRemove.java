package fun.miranda.FastRemove;

import fun.miranda.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandFastRemove implements TabExecutor {
    private final ArrayList<String> legalCommand = new ArrayList<>(Arrays.asList("confirm", "execute", "reset", "items"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("该命令只能由玩家执行");
            return true;
        }
        if (args.length != 1) {
            player.sendMessage("§c参数错误");
            return true;
        }
        String option = args[0];
        if (!legalCommand.contains(option)) {
            player.sendMessage("§c参数错误");
            return true;
        }
        Selection selection = Selection.getSelection(player);
        switch (option) {
            case "confirm":
                int selectionSize = selection.confirm();
                if (selectionSize == -1) {
                    player.sendMessage("§e选区无效");
                    return true;
                }
                player.sendMessage(String.format("§e选区大小: §b%d", selectionSize));
                break;
            case "execute":
                boolean executeResult = selection.execute();
                if (!executeResult) {
                    player.sendMessage("§c选区未确认");
                    return true;
                }
                player.sendMessage("§e正在清除选区");
                break;
            case "reset":
                selection.reset();
                player.sendMessage("§e选区已重置");
                break;
            case "items":
                boolean success = selection.showItems(player);
                if (!success) {
                    player.sendMessage("§c清除尚未完成");
                }
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            return Utils.listFilter(legalCommand, args[0]);
        }
        return new ArrayList<>();
    }
}
