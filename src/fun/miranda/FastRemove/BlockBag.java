package fun.miranda.FastRemove;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockBag {
    private final Inventory inventory;
    private final Map<Material, Integer> counts = new LinkedHashMap<>();

    public BlockBag(Map<Material, Integer> input) {
        this.inventory = Bukkit.createInventory(null, 54, "§9方块背包");

        // 只保留可生成物品的 Material
        for (Map.Entry<Material, Integer> e : input.entrySet()) {
            Material m = e.getKey();
            if (m == null || !m.isItem() || m.isAir()) continue;
            int amount = e.getValue() == null ? 0 : e.getValue();
            if (amount <= 0) continue;
            this.counts.merge(m, amount, Integer::sum);
        }
    }

    public void open(Player player) {
        fillNow();                 // 先填满
        player.openInventory(this.inventory); // 再打开
    }

    public void fillNow() {
        // 1) 补满已有未满的栈
        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            ItemStack item = this.inventory.getItem(slot);
            if (item == null || item.getType().isAir()) continue;

            int max = item.getMaxStackSize();
            if (item.getAmount() >= max) continue;

            Material m = item.getType();
            int available = this.counts.getOrDefault(m, 0);
            if (available <= 0) continue;

            int need = max - item.getAmount();
            int give = Math.min(need, available);
            item.setAmount(item.getAmount() + give);
            this.counts.put(m, available - give);
        }

        // 2) 填充空格
        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            ItemStack cur = this.inventory.getItem(slot);
            if (cur != null && !cur.getType().isAir()) continue;

            Material nextMat = null;
            int available = 0;
            for (Map.Entry<Material, Integer> e : this.counts.entrySet()) {
                if (e.getValue() > 0) {
                    nextMat = e.getKey();
                    available = e.getValue();
                    break;
                }
            }
            if (nextMat == null) break;

            int max = nextMat.getMaxStackSize();
            int give = Math.min(available, max);
            this.inventory.setItem(slot, new ItemStack(nextMat, give));
            this.counts.put(nextMat, available - give);
        }
    }

    public boolean isEmpty() {
        int countAir = 0;
        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            ItemStack item = this.inventory.getItem(slot);
            if (item == null || item.getType().isAir()) countAir++;
        }
        return countAir == 54;
    }

    public Integer getCount() {
        int count = 0;
        for (Map.Entry<Material, Integer> entry : this.counts.entrySet()) {
            count += entry.getValue();
        }
        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            ItemStack item = this.inventory.getItem(slot);
            if (item == null || item.getType().isAir()) continue;
            count += item.getAmount();
        }
        return count;
    }
}
