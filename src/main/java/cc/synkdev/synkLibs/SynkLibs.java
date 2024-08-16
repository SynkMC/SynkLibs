package cc.synkdev.synkLibs;

import cc.synkdev.synkLibs.components.SynkPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class SynkLibs extends JavaPlugin implements SynkPlugin {
    @Getter private static SynkLibs instance;
    @Setter String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&6SynkLibs&8] » &r");
    @Setter @Getter SynkPlugin spl = null;
    public Map<SynkPlugin, String> availableUpdates = new HashMap<>();
    public void log(String s) {
        Bukkit.getConsoleSender().sendMessage(prefix+" "+s);
    }

    @Override
    public void onEnable() {
        instance = this;
        setSpl(this);
        new Lang(this);
        new Metrics(this, 23015);
        Bukkit.getPluginManager().registerEvents(new Utils(this), this);
        new Utils(this).checkUpdate();
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
        return "1.2";
    }

    @Override
    public String dlLink() {
        return "https://modrinth.com/plugin/synklibs";
    }

    @Override
    public String prefix() {
        return prefix;
    }
}
