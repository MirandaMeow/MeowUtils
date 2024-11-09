package fun.miranda.CloudChest;

import fun.miranda.MeowUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class CloudChest {
    private static CloudChest cloudChest = null;
    private final Inventory chestInv;

    private CloudChest() {
        this.chestInv = this.read();
    }

    public static CloudChest getChest() {
        if (cloudChest == null) {
            cloudChest = new CloudChest();
        }
        return cloudChest;
    }

    public void showChest(Player player) {
        player.openInventory(this.chestInv);
    }

    public void save() {
        String contents = this.contentsToString(this.chestInv.getContents());
        MeowUtils.plugin.config.set("chest", contents);
    }

    private Inventory read() {
        Inventory chestInv = Bukkit.createInventory(null, 54, "§9云箱子");
        String contents = MeowUtils.plugin.config.getString("chest");
        assert contents != null;
        if (!contents.equals("")) {
            ItemStack[] itemStacks = this.stringToContents(contents);
            assert itemStacks != null;
            chestInv.setContents(itemStacks);
        }
        return chestInv;
    }

    private String contentsToString(ItemStack[] itemStacks) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BukkitObjectOutputStream data = new BukkitObjectOutputStream(stream);
            data.writeObject(itemStacks);
            data.close();
            return Base64.getEncoder().encodeToString(stream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private ItemStack[] stringToContents(String contents) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(contents));
            BukkitObjectInputStream data = new BukkitObjectInputStream(stream);
            ItemStack[] itemStacks = (ItemStack[]) data.readObject();
            data.close();
            return itemStacks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

