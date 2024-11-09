package fun.miranda.Teleport;

import fun.miranda.Utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static fun.miranda.MeowUtils.plugin;

public class CommandRandomTP implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player) && !(sender instanceof BlockCommandSender)) {
            sender.sendMessage("§c该命令只能由玩家或命令方块执行");
            return true;
        }
        if (args.length != 5) {
            sender.sendMessage("§e用法: §b/rtp §6<X> <Y> <Z> <radius> <player>");
            return true;
        }
        String x = args[0];
        String y = args[1];
        String z = args[2];
        String radius = args[3];
        String targetString = args[4];
        int X, Y, Z, RADIUS;
        try {
            X = Integer.parseInt(x);
            Y = Integer.parseInt(y);
            Z = Integer.parseInt(z);
            RADIUS = Integer.parseInt(radius);
            if (RADIUS <= 0) {
                sender.sendMessage("§c半径必须大于 0");
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("§c坐标和半径必须为数字");
            return true;
        }
        Location origin;
        if (sender instanceof Player) {
            origin = new Location(((Player) sender).getWorld(), X, Y, Z);
        } else {
            origin = new Location(((BlockCommandSender) sender).getBlock().getWorld(), X, Y, Z);
        }
        List<Entity> targets = plugin.getServer().selectEntities(sender, targetString);
        for (Entity target : targets) {
            if (!(target instanceof Player targetPlayer)) {
                continue;
            }
            float yaw = targetPlayer.getLocation().getYaw();
            float pitch = targetPlayer.getLocation().getPitch();
            Location destination = this.getRandomLocation(origin, RADIUS);
            destination.setYaw(yaw);
            destination.setPitch(pitch);
            targetPlayer.teleport(destination);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 5) {
            List<String> players = new ArrayList<>();
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                players.add(player.getName());
            }
            return Utils.listFilter(players, args[4]);
        }
        return List.of();
    }

    private Integer getRandomInteger(Integer range) {
        Random random = new Random();
        return random.nextInt(range);
    }

    private Location getRandomLocation(Location origin, int RADIUS) {
        int offsetX = this.getRandomInteger(RADIUS * 2) - RADIUS;
        int offsetZ = this.getRandomInteger(RADIUS * 2) - RADIUS;
        Location newLocation = origin.clone().add(offsetX, 0, offsetZ);
        while (!newLocation.getBlock().getType().isAir() || !newLocation.add(0, 1, 0).getBlock().getType().isAir()) {
            offsetX = this.getRandomInteger(RADIUS * 2) - RADIUS;
            offsetZ = this.getRandomInteger(RADIUS * 2) - RADIUS;
            newLocation = origin.clone().add(offsetX, 0, offsetZ);
        }
        return newLocation.add(0.5, -1, 0.5);
    }
}
