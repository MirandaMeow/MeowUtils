package fun.miranda;

import fun.miranda.Imprint.CommandImprint;
import fun.miranda.Imprint.CommandImprintSign;
import fun.miranda.Imprint.EventImprint;
import fun.miranda.Seal.CommandSeal;
import fun.miranda.Seal.EventSeal;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MeowUtils extends JavaPlugin {
    public static MeowUtils plugin;

    public void onEnable() {
        plugin = this;
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e正在启用");

        HashMap<String, TabExecutor> commands = new HashMap<>();
        commands.put("sealscroll", new CommandSeal());
        commands.put("imprint", new CommandImprint());
        commands.put("imprintsign", new CommandImprintSign());
        for (Map.Entry<String, TabExecutor> current : commands.entrySet()) {
            registerCommand(current.getKey(), current.getValue());
        }


        Bukkit.getServer().getPluginManager().registerEvents(new EventSeal(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EventImprint(), this);
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e已载入功能：");
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e  -- 烙印卷轴");
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e  -- 封印卷轴");
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e正在禁用");
    }

    private static void registerCommand(String command, TabExecutor executor) {
        PluginCommand cmd = MeowUtils.plugin.getCommand(command);
        assert cmd != null;
        cmd.setExecutor(executor);
    }
}
