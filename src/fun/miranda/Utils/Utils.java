package fun.miranda.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isInt(String string) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

    public static List<String> listFilter(List<String> list, String select) {
        List<String> selected = new ArrayList<>();
        for (String s : list) {
            if (s.startsWith(select)) {
                selected.add(s);
            }
        }
        if (selected.size() != 0) {
            return selected;
        }
        return list;
    }
}
