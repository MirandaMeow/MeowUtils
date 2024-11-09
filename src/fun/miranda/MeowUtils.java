package fun.miranda;

import fun.miranda.CloudChest.CloudChest;
import fun.miranda.CloudChest.CommandCloudChest;
import fun.miranda.ImprintScroll.CommandImprint;
import fun.miranda.ImprintScroll.CommandImprintSign;
import fun.miranda.ImprintScroll.EventImprint;
import fun.miranda.Murder.CommandCountEntity;
import fun.miranda.Murder.CommandMurder;
import fun.miranda.Murder.EventKillEntity;
import fun.miranda.Murder.EventShowEntityType;
import fun.miranda.RollDice.EventRollDice;
import fun.miranda.SealScroll.CommandSeal;
import fun.miranda.SealScroll.EventSeal;
import fun.miranda.ShowWhoTamed.EventShowWhoTamed;
import fun.miranda.Teleport.*;
import fun.miranda.TpsSpam.CommandTpsSpam;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public class MeowUtils extends JavaPlugin {
    public static MeowUtils plugin;
    public Logger logger;
    public FileConfiguration config;

    /**
     * 插件载入
     */
    @Override
    public void onLoad() {
        logger = this.getServer().getLogger();
        this.saveDefaultConfig();
        this.config = this.getConfig();
    }

    /**
     * 插件启用
     */
    @Override
    public void onEnable() {
        plugin = this;
        if (this.config.get("players") == null) {
            this.config.set("players", new ArrayList<>());
        }
        logger.info("§b[§6猫子组件§b] §e正在启用");
        logger.info("§b[§6猫子组件§b] §e已载入功能：");

        if (getModuleEnabled("cloudchest")) {
            setupCloudChest();
        }
        if (getModuleEnabled("rolldice")) {
            setupRollDice();
        }
        if (getModuleEnabled("imprint")) {
            setupImprint();
        }
        if (getModuleEnabled("seal")) {
            setupSeal();
        }
        if (getModuleEnabled("teleport")) {
            setupTeleport();
        }
        if (getModuleEnabled("showwhotamed")) {
            setupShowWhoTamed();
        }
        if (getModuleEnabled("tpsspam")) {
            setupTpsSpam();
        }
        if (getModuleEnabled("murder")) {
            setupMurder();
        }
    }

    /**
     * 插件禁用
     */
    public void onDisable() {
        logger.info("§b[§6猫子组件§b] §e正在禁用");
        if (getModuleEnabled("cloudchest")) {
            saveCloudChest();
        }
        if (getModuleEnabled("teleport")) {
            saveTeleport();
        }
        this.saveConfig();
    }

    /**
     * 初始化烙印卷轴
     */
    private void setupImprint() {
        registerCommand("imprint", new CommandImprint());
        registerCommand("imprintsign", new CommandImprintSign());
        registerEvent(new EventImprint());
        logger.info("§b[§6猫子组件§b] §e  -- 烙印卷轴");
    }

    /**
     * 初始化封印卷轴
     */
    private void setupSeal() {
        registerCommand("sealscroll", new CommandSeal());
        registerEvent(new EventSeal());
        logger.info("§b[§6猫子组件§b] §e  -- 封印卷轴");
    }

    /**
     * 初始化云箱子
     */
    private void setupCloudChest() {
        registerCommand("cc", new CommandCloudChest());
        if (this.config.getString("chest") == null) {
            this.config.set("chest", "");
        }
        logger.info("§b[§6猫子组件§b] §e  -- 云箱子");
    }

    /**
     * 初始化骰娘
     */
    private void setupRollDice() {
        registerEvent(new EventRollDice());
        logger.info("§b[§6猫子组件§b] §e  -- 骰娘");
    }

    /**
     * 初始化驯兽提示
     */
    private void setupShowWhoTamed() {
        registerEvent(new EventShowWhoTamed());
        logger.info("§b[§6猫子组件§b] §e  -- 驯兽提示");
    }

    /**
     * 初始化传送功能，包括设置家，返回家，传送到指定玩家
     */
    private void setupTeleport() {
        registerCommand("sethome", new CommandSetHome());
        registerCommand("delhome", new CommandDelHome());
        registerCommand("defhome", new CommandDefaultHome());
        registerCommand("home", new CommandHome());
        registerCommand("backcorpse", new CommandBackCorpse());
        registerCommand("rtp", new CommandRandomTP());
        registerEvent(new EventPlayerDeath());
        logger.info("§b[§6猫子组件§b] §e  -- 传送");
    }


    /**
     * 初始化持续在控制台持续显示 tps
     */
    private void setupTpsSpam() {
        registerCommand("tpsspam", new CommandTpsSpam());
        CommandTpsSpam.TPSClock(this);
        logger.info("§b[§6猫子组件§b] §e  -- Tps 持续显示");
    }

    /**
     * 初始化实体清理
     */
    private void setupMurder() {
        registerCommand("murder", new CommandMurder());
        registerCommand("ce", new CommandCountEntity());
        registerEvent(new EventShowEntityType());
        registerEvent(new EventKillEntity());
        logger.info("§b[§6猫子组件§b] §e  -- 实体清理");
    }


    /**
     * 保存云箱子
     */
    private void saveCloudChest() {
        CloudChest.getChest().save();
        logger.info("§b[§6猫子组件§b] §e正在保存云箱子");
    }

    /**
     * 保存传送信息
     */
    private void saveTeleport() {
        logger.info("§b[§6猫子组件§b] §e正在保存玩家传送信息");
    }

    /**
     * 注册命令
     *
     * @param command  命令字符串
     * @param executor 命令
     */
    private void registerCommand(String command, TabExecutor executor) {
        PluginCommand cmd = this.getCommand(command);
        assert cmd != null;
        cmd.setExecutor(executor);
    }

    /**
     * 注册监听器
     *
     * @param listener 监听器
     */
    private void registerEvent(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * @param moduleName 指定的模块
     * @return 返回指定模块是否启用
     */
    private boolean getModuleEnabled(String moduleName) {
        return this.config.getBoolean(String.format("modules.%s", moduleName));
    }
}
