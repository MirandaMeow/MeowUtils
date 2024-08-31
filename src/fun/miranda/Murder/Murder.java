package fun.miranda.Murder;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Murder {
    private List<EntityType> types;
    private final Player player;
    private final int distance;
    private final List<EntityType> entityTypes = List.of(EntityType.values());

    public Murder(Player player, int distance) {
        this.player = player;
        this.distance = distance;
        this.types = new ArrayList<>();
    }

    public int modifyType(String type) {
        if (type.equalsIgnoreCase("all")) {
            this.types = new ArrayList<>();
            this.types.addAll(entityTypes);
            return 0;
        }
        if (type.startsWith("-")) {
            String temp = type.substring(1);
            try {
                EntityType entityType = EntityType.valueOf(temp.toUpperCase(Locale.ROOT));
                if (this.types.contains(entityType)) {
                    this.types.remove(entityType);
                    return 0;
                } else {
                    return 1;
                }
            } catch (IllegalArgumentException e) {
                return 1;
            }
        }
        try {
            EntityType entityType = EntityType.valueOf(type.toUpperCase(Locale.ROOT));
            if (!this.types.contains(entityType)) {
                this.types.add(entityType);
                return 0;
            }
        } catch (IllegalArgumentException e) {
            return 1;
        }
        return 1;
    }

    public Integer kill() {
        int count = 0;
        List<Entity> entities = this.player.getNearbyEntities(this.distance, 255, this.distance);
        for (Entity currentEntity : entities) {
            for (EntityType entityType : this.types) {
                if (currentEntity.getType() == entityType) {
                    if (currentEntity.getType() == EntityType.PLAYER) {
                        continue;
                    }
                    currentEntity.remove();
                    count++;
                }
            }
        }
        return count;
    }
}
