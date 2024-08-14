package cc.synkdev.synkLibs;

import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class SynkLibs extends JavaPlugin {
    @Getter private static SynkLibs instance;
    @Setter String pluginPrefix = null;
    @Setter String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&6SynkLibs&8] » &r");
    public Map<String, String> availableUpdates = new HashMap<>();
    public Map<String, String> updateLink = new HashMap<>();
    public void log(String s) {
        Bukkit.getConsoleSender().sendMessage(prefix+" "+s);
    }

    @Override
    public void onEnable() {
        instance = this;
        new Metrics(this, 23015);
        Bukkit.getPluginManager().registerEvents(new Utils(), this);
        Utils.checkUpdate("SynkLibs", "1.0", "https://modrinth.com/plugin/synklibs");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
