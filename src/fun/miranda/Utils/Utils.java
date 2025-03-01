package fun.miranda.Utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static fun.miranda.MeowUtils.plugin;

public class Utils {
    public static boolean isInt(String string) {
        Pattern pattern = Pattern.compile("^-?[0-9]+$");
        return pattern.matcher(string).matches();
    }

    public static List<String> listFilter(List<String> list, String select) {
        List<String> selected = new ArrayList<>();
        for (String s : list) {
            if (s.startsWith(select)) {
                selected.add(s);
            }
        }
        if (!selected.isEmpty()) {
            return selected;
        }
        return list;
    }

    public static List<String> getAllPlayersName() {
        List<String> out = new ArrayList<>();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            out.add(player.getName());
        }
        return out;
    }
}
