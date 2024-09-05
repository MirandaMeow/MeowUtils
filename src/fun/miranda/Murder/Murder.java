package fun.miranda.Murder;

import fun.miranda.MeowUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Murder {
    private List<EntityType> targetEntityTypes;
    private final List<EntityType> entityTypes = List.of(EntityType.values());
    private final List<Entity> entities;

    public Murder(Player player, int distance) {
        Location location = player.getLocation();
        this.targetEntityTypes = new ArrayList<>();
        this.entities = (List<Entity>) location.getWorld().getNearbyEntities(location, distance, 255, distance);
    }

    public Murder(BlockCommandSender block, int distance) {
        Location location = block.getBlock().getLocation();
        this.targetEntityTypes = new ArrayList<>();
        this.entities = (List<Entity>) location.getWorld().getNearbyEntities(location, distance, 255, distance);
    }

    public Murder() {
        this.targetEntityTypes = new ArrayList<>();
        this.entities = new ArrayList<>();
        for (World world : MeowUtils.plugin.getServer().getWorlds()) {
            this.entities.addAll(world.getEntities());
        }
    }

    public int modifyType(String type) {
        if (type.equalsIgnoreCase("all")) {
            this.targetEntityTypes = new ArrayList<>();
            this.targetEntityTypes.addAll(entityTypes);
            return 0;
        }
        try {
            if (type.endsWith("*")) {
                return this.operateWithStar(type);
            } else {
                return this.operateWithoutStar(type);
            }
        } catch (IllegalArgumentException e) {
            return 1;
        }
    }

    private int operateWithStar(String type) {
        try {
            if (type.startsWith("-")) {
                type = type.substring(1, type.length() - 1).toUpperCase(Locale.ROOT);
                for (EntityType entityType : this.entityTypes) {
                    if (entityType.name().startsWith(type)) {
                        this.targetEntityTypes.remove(entityType);
                    }
                }
            } else {
                for (EntityType entityType : this.entityTypes) {
                    if (entityType.name().startsWith(type.substring(0, type.length() - 1))) {
                        this.targetEntityTypes.add(entityType);
                    }
                }
            }
            return 0;
        } catch (IllegalArgumentException e) {
            return 1;
        }
    }

    private int operateWithoutStar(String type) {
        try {
            if (type.startsWith("-")) {
                type = type.substring(1).toUpperCase(Locale.ROOT);
                EntityType entityType = EntityType.valueOf(type);
                this.targetEntityTypes.remove(entityType);
                return 0;
            } else {
                EntityType entityType = EntityType.valueOf(type.toUpperCase(Locale.ROOT));
                if (!this.targetEntityTypes.contains(entityType)) {
                    this.targetEntityTypes.add(entityType);
                    return 0;
                }
            }
        } catch (IllegalArgumentException e) {
            return 1;
        }
        return 1;
    }

    public Integer kill() {
        int count = 0;
        for (Entity currentEntity : this.entities) {
            EntityType currentEntityType = currentEntity.getType();
            if (currentEntityType == EntityType.PLAYER) {
                continue;
            }
            if (this.targetEntityTypes.contains(currentEntityType)) {
                currentEntity.remove();
                count++;
            }
        }
        return count;
    }
}
