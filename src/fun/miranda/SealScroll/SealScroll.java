package fun.miranda.SealScroll;

import fun.miranda.Utils.NBTEditor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SealScroll {
    public ItemStack scroll;
    public boolean isSealed;
    private String seal;
    private EntityType type;
    public static SealScroll blankScroll = null;

    public static ItemStack getBlankScroll() {
        if (blankScroll == null) {
            blankScroll = new SealScroll();
        }
        return blankScroll.scroll;
    }

    public SealScroll() {
        this.scroll = new ItemStack(Material.PAPER, 1);
        ItemMeta scrollMeta = this.scroll.getItemMeta();
        assert scrollMeta != null;
        scrollMeta.setDisplayName("§9§l封印卷轴");

        List<String> lores = new ArrayList<>();
        lores.add("");
        lores.add("§6[未封印]");
        lores.add("§a空白的封印卷轴");
        lores.add("");
        lores.add("§7shift + 右击生物以将其封入卷轴");

        scrollMeta.setLore(lores);
        this.scroll.setItemMeta(scrollMeta);
        this.isSealed = false;
        this.seal = "";
        this.type = null;
    }

    public SealScroll(ItemStack itemStack) {
        this.scroll = itemStack;
        if (NBTEditor.contains(this.scroll, "seal")) {
            String seal = NBTEditor.getString(this.scroll, "seal");
            this.isSealed = true;
            this.seal = seal;
            this.type = EntityType.valueOf(this.scroll.getItemMeta().getLore().get(2).split(" §5")[1]);
        } else {
            this.isSealed = false;
            this.seal = "";
            this.type = null;
        }
    }

    public static int giveScroll(Player player, int amount) {
        ItemStack item = getBlankScroll().clone();
        item.setAmount(amount);
        HashMap<Integer, ItemStack> result = player.getInventory().addItem(item);
        if (result.isEmpty()) {
            return 0;
        }
        return result.get(0).getAmount();
    }

    public void seal(Entity entity, Player player) {
        if (this.isSealed) {
            return;
        }
        NBTEditor.NBTCompound compound = NBTEditor.getEntityNBTTag(entity);
        this.seal = compound.toJson();
        this.scroll = NBTEditor.set(this.scroll, this.seal, "seal");
        this.isSealed = true;
        this.type = entity.getType();
        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        entity.remove();
        ItemMeta scrollMeta = this.scroll.getItemMeta();
        assert scrollMeta != null;
        List<String> lores = new ArrayList<>();
        lores.add("");
        lores.add("§6[已封印]");
        lores.add(String.format("§a封印了 §5%s", entity.getType()));
        lores.add("");
        lores.add("§7右击将生物从卷轴中释放出来");
        scrollMeta.setLore(lores);
        scrollMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.scroll.setItemMeta(scrollMeta);
        ItemStack scroll = this.scroll.clone();
        scroll.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        scroll.setAmount(1);
        player.getInventory().addItem(scroll);
        player.updateInventory();
    }

    public void unleash(Player player) {
        if (!this.isSealed) {
            return;
        }
        NBTEditor.NBTCompound compound = NBTEditor.NBTCompound.fromJson(this.seal);
        Entity entity = player.getWorld().spawnEntity(player.getLocation(), this.type);
        NBTEditor.set(entity, compound);
        entity.teleport(player.getLocation());
        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
    }

    public static boolean isSealScroll(ItemStack itemStack) {
        if (itemStack.getType().equals(Material.AIR)) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        return itemMeta.getDisplayName().equals("§9§l封印卷轴");
    }
}
