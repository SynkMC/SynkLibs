package cc.synkdev.synkLibs.bukkit;

import cc.synkdev.synkLibs.bukkit.commands.ReportCmd;
import cc.synkdev.synkLibs.bukkit.commands.SlCmd;
import cc.synkdev.synkLibs.components.GlobalErrorHandler;
import cc.synkdev.synkLibs.components.SynkPlugin;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class SynkLibs extends JavaPlugin implements SynkPlugin {
    @Getter private static SynkLibs instance;
    @Setter String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&6SynkLibs&8] » &r");
    @Setter @Getter static SynkPlugin spl = null;
    public static Map<SynkPlugin, String> availableUpdates = new HashMap<>();
    private final File configFile = new File(getDataFolder(), "config.yml");
    public FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
    public static String lang = "en";
    private PaperCommandManager pcm;
    @Getter @Setter private static Boolean loopReport = false;
    public static Map<String, String> langMap = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        langMap.clear();
        langMap.putAll(Lang.init(this, new File(getDataFolder(), "lang.json")));

        pcm = new PaperCommandManager(this);
        Thread.setDefaultUncaughtExceptionHandler(new GlobalErrorHandler("https://discord.com/api/webhooks/1294577862359257129/W7BssLiR8LpvfA7KeiAsBerXMHGvxB-1o0lKL70ly5RviPKwM4omvnXibqsKHkhsYAHW"));
        pcm.setDefaultExceptionHandler(new GlobalErrorHandler("https://discord.com/api/webhooks/1294577862359257129/W7BssLiR8LpvfA7KeiAsBerXMHGvxB-1o0lKL70ly5RviPKwM4omvnXibqsKHkhsYAHW"));

        setSpl(this);
        new Metrics(this, 23015);
        Bukkit.getPluginManager().registerEvents(new Utils(this), this);
        Utils.checkUpdate(this);

        pcm.enableUnstableAPI("help");
        pcm.registerCommand(new ReportCmd(this));
        pcm.registerCommand(new SlCmd(this));

        loadConfig();
    }

    public void loadConfig() {
        try {
            if (!configFile.getParentFile().exists()) configFile.getParentFile().mkdirs();
            if (!configFile.exists()) {
                configFile.createNewFile();
                return;
            }

            config = YamlConfiguration.loadConfiguration(configFile);
            config = Utils.loadWebConfig("https://synkdev.cc/storage/config-libs.php", configFile);
            lang = config.getString("lang");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public String name() {
        return "SynkLibs";
    }

    @Override
    public String ver() {
        return "1.6";
    }

    @Override
    public String dlLink() {
        return "https://modrinth.com/plugin/synklibs";
    }

    @Override
    public String prefix() {
        return prefix;
    }

    @Override
    public String lang() {
        return "https://synkdev.cc/storage/translations/lang-pld/SynkLibs/lang-libs.json";
    }

    @Override
    public Map<String, String> langMap() {
        return langMap;
    }
}
