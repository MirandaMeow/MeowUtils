package fun.miranda.Utils;

import java.util.regex.Pattern;

public class Utils {
    public static boolean isInt(String string) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(string).matches();
    }
}
