package fun.miranda.FlyTicket;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlyTicket {
    private final Integer flyTime;
    private final ItemStack flyTickItem;

    public FlyTicket(Integer flyTime) {
        this.flyTime = flyTime;
        this.flyTickItem = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = flyTickItem.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§9§l飞行券");
        List<String> lores = new ArrayList<>();
        lores.add("");
        lores.add("§6可增加飞行时长");
        lores.add(String.format("§e%d §b秒", flyTime));
        meta.setLore(lores);
        this.flyTickItem.setItemMeta(meta);
    }

    public FlyTicket(ItemStack flyTickItem) {
        this.flyTickItem = flyTickItem;
        ItemMeta meta = this.flyTickItem.getItemMeta();
        assert meta != null;
        List<String> lores = meta.getLore();
        assert lores != null;
        this.flyTime = this.getSeconds(lores.get(2));
    }

    public boolean giveFlyTicketItem(Player player) {
        return player.getInventory().addItem(this.flyTickItem).isEmpty();
    }

    public Integer getFlyTime() {
        return this.flyTime;
    }

    public static boolean isTicket(ItemStack itemStack) {
        if (itemStack.getType() != Material.PAPER) {
            return false;
        }
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        return meta.getDisplayName().equals("§9§l飞行券");
    }

    private Integer getSeconds(String string) {
        Pattern pattern = Pattern.compile("§e(\\d+) §b秒");
        Matcher matcher = pattern.matcher(string);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
    }
}
