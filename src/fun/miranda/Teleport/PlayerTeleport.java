package fun.miranda.Teleport;

import fun.miranda.MeowUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerTeleport {
    private final UUID uuid;
    private final String path;

    public PlayerTeleport(UUID uuid) {
        this.uuid = uuid;
        this.path = "players." + uuid.toString() + ".homes";
        if (MeowUtils.plugin.getConfig().get(this.path) == null) {
            MeowUtils.plugin.getConfig().set(this.path, new ArrayList<>());
            MeowUtils.plugin.getConfig().set("players." + uuid + ".defaultHome", "");
            MeowUtils.plugin.saveConfig();
        }
    }

    public boolean addHome(Location location, String homeName) {
        if (MeowUtils.plugin.getConfig().getConfigurationSection(this.path) != null) {
            if (MeowUtils.plugin.getConfig().getConfigurationSection(this.path).getKeys(false).size() >= 3) {
                return false;
            }
        }

        String world = location.getWorld().getName();
        Double x = location.getX();
        Double y = location.getY();
        Double z = location.getZ();
        Float yaw = location.getYaw();
        Float pitch = location.getPitch();
        MeowUtils.plugin.getConfig().set(this.path + "." + homeName + ".world", world);
        MeowUtils.plugin.getConfig().set(this.path + "." + homeName + ".x", x);
        MeowUtils.plugin.getConfig().set(this.path + "." + homeName + ".y", y);
        MeowUtils.plugin.getConfig().set(this.path + "." + homeName + ".z", z);
        MeowUtils.plugin.getConfig().set(this.path + "." + homeName + ".yaw", yaw);
        MeowUtils.plugin.getConfig().set(this.path + "." + homeName + ".pitch", pitch);
        return true;
    }


    public boolean delHome(String homeName) {
        List<String> homeNames = this.getHomeNames();
        String defaultHome = MeowUtils.plugin.getConfig().getString("players." + uuid.toString() + ".defaultHome");
        if (!homeNames.contains(homeName)) {
            return false;
        }
        MeowUtils.plugin.getConfig().set(this.path + "." + homeName, null);
        if (defaultHome.equals(homeName)) {
            MeowUtils.plugin.getConfig().set("players." + uuid + ".defaultHome", "");
        }
        return true;
    }

    public String getDefaultHome() {
        return MeowUtils.plugin.getConfig().getString("players." + uuid.toString() + ".defaultHome");
    }

    public boolean setDefaultHome(String homeName) {
        List<String> homeNames = this.getHomeNames();
        if (!homeNames.contains(homeName)) {
            return false;
        }
        MeowUtils.plugin.getConfig().set("players." + uuid.toString() + ".defaultHome", homeName);
        return true;
    }

    public String teleportHome(String homeName, Player player) {
        List<String> homeNames = this.getHomeNames();
        if (homeName == "") {
            if (this.getDefaultHome() != "") {
                homeName = this.getDefaultHome();
            } else {
                return "";
            }
        }
        if (!homeNames.contains(homeName)) {
            return "";
        }
        player.teleport(this.getHome(homeName));
        return homeName;
    }

    private Location getHome(String homeName) {
        String world = MeowUtils.plugin.getConfig().getString(this.path + "." + homeName + ".world");
        double x = MeowUtils.plugin.getConfig().getDouble(this.path + "." + homeName + ".x");
        double y = MeowUtils.plugin.getConfig().getDouble(this.path + "." + homeName + ".y");
        double z = MeowUtils.plugin.getConfig().getDouble(this.path + "." + homeName + ".z");
        float yaw = Float.parseFloat(Double.toString(MeowUtils.plugin.getConfig().getDouble(this.path + "." + homeName + ".yaw")));
        float pitch = Float.parseFloat(Double.toString(MeowUtils.plugin.getConfig().getDouble(this.path + "." + homeName + ".pitch")));
        assert world != null;
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public List<String> getHomeNames() {
        if (MeowUtils.plugin.getConfig().getConfigurationSection(this.path) != null) {
            List<String> out = new ArrayList<>();
            out.addAll(MeowUtils.plugin.getConfig().getConfigurationSection(this.path).getKeys(false));
            return out;
        }
        return new ArrayList<>();
    }
}
