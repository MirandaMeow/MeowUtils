package fun.miranda.Murder;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Murder {
    private List<EntityType> targetEntityTypes;
    private final Location location;
    private final int distance;
    private final List<EntityType> entityTypes = List.of(EntityType.values());

    public Murder(Player player, int distance) {
        this.location = player.getLocation();
        this.distance = distance;
        this.targetEntityTypes = new ArrayList<>();
    }

    public Murder(BlockCommandSender block, int distance) {
        this.location = block.getBlock().getLocation();
        this.distance = distance;
        this.targetEntityTypes = new ArrayList<>();
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
        List<Entity> entities = (List<Entity>) this.location.getWorld().getNearbyEntities(this.location, this.distance, 255, this.distance);
        for (Entity currentEntity : entities) {
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
