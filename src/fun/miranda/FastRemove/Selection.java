package fun.miranda.FastRemove;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static fun.miranda.MeowUtils.plugin;

public class Selection {
    private Location pos1;
    private Location pos2;
    private final List<Block> blocks;
    private boolean canExecute = false;
    private boolean needNew;
    private HashMap<Material, Integer> materials;
    private BlockBag blockBag;

    private static final HashMap<Player, Selection> selections = new HashMap<>();

    public static Selection getSelection(Player player) {
        if (!selections.containsKey(player)) {
            selections.put(player, new Selection());
        }
        return selections.get(player);
    }

    private Selection() {
        this.pos1 = null;
        this.pos2 = null;
        this.canExecute = false;
        this.blocks = new ArrayList<>();
        this.materials = new HashMap<>();
        this.needNew = true;
        this.blockBag = null;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
        this.canExecute = false;
        this.needNew = true;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
        this.canExecute = false;
        this.needNew = true;
    }

    public int confirm() {
        if (this.pos1 == null || this.pos2 == null) {
            return -1;
        }
        World world1 = this.pos1.getWorld();
        World world2 = this.pos2.getWorld();
        assert world1 != null;
        assert world2 != null;
        if (!world1.equals(world2)) {
            return -1;
        }
        this.canExecute = true;
        AtomicInteger count = new AtomicInteger();
        plugin.getServer().getScheduler().runTaskTimer(plugin, task -> {
            if (count.get() < 10) {
                this.showCuboidBorder(this.pos1, this.pos2);
                count.addAndGet(1);
            } else {
                task.cancel();
            }
        }, 0, 10L);


        this.collectBlocks();
        return this.blocks.size();
    }

    private void collectBlocks() {
        World world = this.pos1.getWorld();
        assert world != null;
        int x1 = this.pos1.getBlockX();
        int y1 = this.pos1.getBlockY();
        int z1 = this.pos1.getBlockZ();
        int x2 = this.pos2.getBlockX();
        int y2 = this.pos2.getBlockY();
        int z2 = this.pos2.getBlockZ();
        int xMin = Math.min(x1, x2);
        int yMin = Math.min(y1, y2);
        int zMin = Math.min(z1, z2);
        int xMax = Math.max(x1, x2);
        int yMax = Math.max(y1, y2);
        int zMax = Math.max(z1, z2);
        this.blocks.clear();
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    this.blocks.add(block);
                }
            }
        }
    }

    public boolean execute() {
        if (!this.canExecute) {
            return false;
        }
        this.removeBlocksAsync(this.blocks, 2000);
        this.pos1 = null;
        this.pos2 = null;
        this.blocks.clear();
        this.canExecute = false;
        this.needNew = true;
        return true;
    }

    public boolean showItems(Player player) {
        if (this.materials.isEmpty()) {
            return false;
        }
        if (this.blockBag == null) {
            this.blockBag = new BlockBag(this.materials);
            this.needNew = false;
        } else {
            if (this.needNew) {
                this.blockBag = new BlockBag(this.materials);
                this.needNew = false;
            }
        }
        this.blockBag.open(player);
        return true;
    }

    public void reset() {
        this.pos1 = null;
        this.pos2 = null;
        this.canExecute = false;
        this.blocks.clear();
        this.materials.clear();
        this.needNew = true;
        this.blockBag = null;
    }

    public BlockBag getBlockBag() {
        return this.blockBag;
    }

    private List<ItemStack> convertToItemStacks(Map<Material, Integer> map) {
        List<ItemStack> result = new ArrayList<>();
        for (Map.Entry<Material, Integer> entry : map.entrySet()) {
            Material material = entry.getKey();
            int totalAmount = entry.getValue();
            int maxStack = material.getMaxStackSize();
            if (!material.isItem()) {
                continue;
            }

            while (totalAmount > 0) {
                int stackAmount = Math.min(totalAmount, maxStack);
                ItemStack item = new ItemStack(material, stackAmount);
                result.add(item);
                totalAmount -= stackAmount;
            }
        }

        return result;
    }


    private void removeBlocksAsync(List<Block> blocks, int interval) {
        this.materials.clear();
        HashMap<Material, Integer> map = new HashMap<>();
        Queue<Block> queue = new LinkedList<>(blocks);
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            int count = 0;

            while (!queue.isEmpty() && count < interval) {
                Block block = queue.poll();
                if (block != null) {
                    Material material = block.getType();
                    if (material.isAir()) {
                        continue;
                    }
                    if (!map.containsKey(material)) {
                        map.put(material, 0);
                    }
                    map.compute(material, (k, amount) -> amount + 1);
                    block.setType(Material.AIR, false);
                }
                count++;
            }
            if (queue.isEmpty()) {
                this.materials = map;
                task.cancel();
            }
        }, 0L, 1L);
    }

    public void showCuboidBorder(Location loc1, Location loc2) {
        World world = loc1.getWorld();
        if (world == null || !world.equals(loc2.getWorld())) return;

        double minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        double minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        double minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        double maxX = Math.max(loc1.getBlockX(), loc2.getBlockX()) + 1; // 包含右边方块
        double maxY = Math.max(loc1.getBlockY(), loc2.getBlockY()) + 1;
        double maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ()) + 1;

        // 八个顶点（偏移 0.5，指向方块中心）
        Location[] corners = new Location[]{
                new Location(world, minX + 0.5, minY + 0.5, minZ + 0.5),
                new Location(world, minX + 0.5, minY + 0.5, maxZ - 0.5),
                new Location(world, minX + 0.5, maxY - 0.5, minZ + 0.5),
                new Location(world, minX + 0.5, maxY - 0.5, maxZ - 0.5),
                new Location(world, maxX - 0.5, minY + 0.5, minZ + 0.5),
                new Location(world, maxX - 0.5, minY + 0.5, maxZ - 0.5),
                new Location(world, maxX - 0.5, maxY - 0.5, minZ + 0.5),
                new Location(world, maxX - 0.5, maxY - 0.5, maxZ - 0.5)
        };

        int[][] edges = {
                {0, 1}, {0, 2}, {0, 4},
                {7, 6}, {7, 5}, {7, 3},
                {1, 3}, {1, 5},
                {2, 3}, {2, 6},
                {4, 5}, {4, 6}
        };

        for (int[] edge : edges) {
            Location start = corners[edge[0]];
            Location end = corners[edge[1]];
            this.drawLine(world, start, end);
        }
    }

    private void drawLine(World world, Location start, Location end) {
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        double dz = end.getZ() - start.getZ();

        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        int points = (int) Math.floor(length / 0.25);

        double stepX = dx / points;
        double stepY = dy / points;
        double stepZ = dz / points;

        for (int i = 0; i <= points; i++) {
            double x = start.getX() + stepX * i;
            double y = start.getY() + stepY * i;
            double z = start.getZ() + stepZ * i;
            world.spawnParticle(Particle.VILLAGER_HAPPY, x, y, z, 1, 0, 0, 0, 0);
        }
    }
}
