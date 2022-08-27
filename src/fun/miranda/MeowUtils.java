package fun.miranda;

import fun.miranda.CloudChest.CloudChest;
import fun.miranda.CloudChest.CommandCloudChest;
import fun.miranda.ImprintScroll.CommandImprint;
import fun.miranda.ImprintScroll.CommandImprintSign;
import fun.miranda.ImprintScroll.EventImprint;
import fun.miranda.SealScroll.CommandSeal;
import fun.miranda.SealScroll.EventSeal;
import fun.miranda.ShowWhoTamed.EventShowWhoTamed;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeowUtils extends JavaPlugin {
    public static MeowUtils plugin;
    public static YamlConfiguration cloudchest;
    private static File file;


    /**
     * 插件启用
     */
    public void onEnable() {
        plugin = this;
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e正在启用");

        // 初始化命令
        HashMap<String, TabExecutor> commands = new HashMap<>();
        commands.put("sealscroll", new CommandSeal());
        commands.put("imprint", new CommandImprint());
        commands.put("imprintsign", new CommandImprintSign());
        commands.put("cc", new CommandCloudChest());
        for (Map.Entry<String, TabExecutor> current : commands.entrySet()) {
            registerCommand(current.getKey(), current.getValue());
        }

        // 初始化监听器
        List<Listener> listeners = new ArrayList<>();
        listeners.add(new EventImprint());
        listeners.add(new EventSeal());
        listeners.add(new EventShowWhoTamed());
        for (Listener listener : listeners) {
            registerEvent(listener);
        }

        // 初始化配置文件
        file = new File(this.getDataFolder(), "cloudchest.yml");
        if (!file.exists()) {
            this.saveResource("cloudchest.yml", false);
        }
        cloudchest = YamlConfiguration.loadConfiguration(file);
        if (cloudchest.getString("chest") == null) {
            cloudchest.set("chest", "");
            save();
        }

        // 输出加载信息
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e已载入功能：");
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e  -- 烙印卷轴");
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e  -- 封印卷轴");
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e  -- 云箱子");
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e  -- 驯兽提示");
    }

    /**
     * 插件禁用
     */
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e正在禁用");
        Bukkit.getConsoleSender().sendMessage("§b[§6猫子组件§b] §e正在保存云箱子");
        CloudChest.getChest().save();
    }

    /**
     * 注册命令
     *
     * @param command  命令字符串
     * @param executor 命令
     */
    private static void registerCommand(String command, TabExecutor executor) {
        PluginCommand cmd = MeowUtils.plugin.getCommand(command);
        assert cmd != null;
        cmd.setExecutor(executor);
    }

    /**
     * 注册监听器
     *
     * @param listener 监听器
     */
    private static void registerEvent(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, MeowUtils.plugin);
    }

    /**
     * 保存云箱子数据
     */
    public static void save() {
        try {
            cloudchest.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
