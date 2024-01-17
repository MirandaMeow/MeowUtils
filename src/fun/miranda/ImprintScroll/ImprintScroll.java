package fun.miranda.ImprintScroll;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

public class ImprintScroll {
    public static ImprintScroll blankScroll = null;
    public final ItemStack scroll;
    public boolean isImprinted;
    private Location location;

    public ImprintScroll(ItemStack itemStack) {
        this.scroll = itemStack;
        ItemMeta scrollMeta = this.scroll.getItemMeta();
        assert scrollMeta != null;
        List<String> lores = scrollMeta.getLore();
        assert lores != null;
        this.isImprinted = lores.get(1).equals("§6[已缔结烙印]");
        if (this.isImprinted) {
            List<String> position = lores.subList(2, 8);
            String worldName = position.get(0).split(": §5")[1];
            String X = position.get(1).split(": §5")[1];
            String Y = position.get(2).split(": §5")[1];
            String Z = position.get(3).split(": §5")[1];
            String yaw = position.get(4).split(": §5")[1];
            String pitch = position.get(5).split(": §5")[1];
            this.location = new Location(Bukkit.getWorld(worldName), Double.parseDouble(X), Double.parseDouble(Y), Double.parseDouble(Z), Float.parseFloat(yaw), Float.parseFloat(pitch));
        } else {
            this.location = new Location(null, 0, 0, 0, 0, 0);
        }
    }

    public ImprintScroll() {
        this.scroll = new ItemStack(Material.PAPER, 1);
        ItemMeta scrollMeta = this.scroll.getItemMeta();
        assert scrollMeta != null;
        scrollMeta.setDisplayName("§9§l烙印卷轴");

        List<String> lores = new ArrayList<>();
        lores.add("");
        lores.add("§6[未缔结烙印]");
        lores.add("§a空白的烙印卷轴");
        lores.add("");
        lores.add("§7shift + 右击地面以缔结烙印");

        scrollMeta.setLore(lores);
        this.scroll.setItemMeta(scrollMeta);
        this.isImprinted = false;
        this.location = new Location(null, 0, 0, 0, 0, 0);
    }

    public void conclude(Player player) {
        if (this.isImprinted) {
            return;
        }
        ItemMeta scrollMeta = this.scroll.getItemMeta();
        assert scrollMeta != null;
        List<String> lores = scrollMeta.getLore();
        assert lores != null;
        ItemStack scroll = this.scroll.clone();
        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        Location location = player.getLocation();
        String worldName = location.getWorld().getName();
        String X = new Formatter().format("%.2f", location.getX()).toString();
        String Y = new Formatter().format("%.2f", location.getY()).toString();
        String Z = new Formatter().format("%.2f", location.getZ()).toString();
        String yaw = new Formatter().format("%.2f", location.getYaw()).toString();
        String pitch = new Formatter().format("%.2f", location.getPitch()).toString();
        lores.set(1, "§6[已缔结烙印]");
        lores.add(2, String.format(" §a世界: §5%s", worldName));
        lores.add(3, String.format(" §aX: §5%s", X));
        lores.add(4, String.format(" §aY: §5%s", Y));
        lores.add(5, String.format(" §aZ: §5%s", Z));
        lores.add(6, String.format(" §a角度: §5%s", yaw));
        lores.add(7, String.format(" §a俯仰角: §5%s", pitch));
        lores.remove(8);
        lores.set(lores.size() - 1, "§7右击以激活烙印");
        scrollMeta.setLore(lores);
        scrollMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        scroll.setItemMeta(scrollMeta);
        scroll.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        scroll.setAmount(1);
        player.getInventory().addItem(scroll);
        player.updateInventory();
        this.isImprinted = true;
        this.location = location;
        location.getWorld().playSound(location, Sound.BLOCK_PORTAL_TRIGGER, 1, 2);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location.add(0, 1, 0), 100);
        player.sendTitle("§l§c烙 印 已 缔 结", "", 10, 70, 20);
    }

    public void teleport(Player player) {
        if (this.isImprinted) {
            Location location = player.getLocation();
            location.getWorld().playEffect(player.getLocation().add(0, 1, 0), Effect.ENDER_SIGNAL, null);
            location.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_DEATH, 1, 2);
            player.teleport(this.location);
            player.teleport(this.location);
            location = player.getLocation();
            location.getWorld().playEffect(player.getLocation().add(0, 1, 0), Effect.ENDER_SIGNAL, null);
            location.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_DEATH, 1, 2);
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            player.sendTitle("§l§c烙 印 归 还", "", 10, 70, 20);
        }
    }

    public ItemStack getScroll() {
        return this.scroll;
    }

    public static boolean isScroll(ItemStack itemStack) {
        if (itemStack.getType().equals(Material.AIR)) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        return itemMeta.getDisplayName().equals("§9§l烙印卷轴");
    }

    public static String sign(Player player, String sign) {
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (!isScroll(handItem)) {
            return "§c手中物品不是烙印卷轴";
        }
        ItemMeta scrollMeta = handItem.getItemMeta();
        assert scrollMeta != null;
        List<String> lores = scrollMeta.getLore();
        assert lores != null;
        if (!lores.get(1).equals("§6[已缔结烙印]")) {
            return "§c还未缔结烙印";
        }
        if (lores.get(9).contains("镌刻")) {
            return "§c烙印卷轴已被镌刻";
        }
        lores.add(9, String.format("§6[由 §e%s §6镌刻]", player.getName()));
        lores.add(10, "§3" + sign);
        lores.add(11, "");
        scrollMeta.setLore(lores);
        handItem.setItemMeta(scrollMeta);
        return "§e成功镌刻铭文";
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

    public static ItemStack getBlankScroll() {
        if (blankScroll == null) {
            blankScroll = new ImprintScroll();
        }
        return blankScroll.scroll;
    }
}
