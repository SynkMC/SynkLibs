package cc.synkdev.synkLibs.bungee;

import cc.synkdev.synkLibs.components.SynkPlugin;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

import java.util.HashMap;
import java.util.Map;

public class SynkLibsBungee extends Plugin implements SynkPlugin {
    @Getter private static SynkLibsBungee instance;
    @Getter String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&6SynkLibs&8] » &r");
    @Setter @Getter static SynkPlugin spl = null;
    static public Map<SynkPlugin, String> availableUpdates = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        setSpl(this);
        new Metrics(this, 23042);
        getProxy().getPluginManager().registerListener(this, new UtilsBungee(this));
        UtilsBungee.checkUpdate(this, this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public String name() {
        return "SynkLibs";
    }

    @Override
    public String ver() {
        return "1.3";
    }

    @Override
    public String dlLink() {
        return "https://modrinth.com/plugin/synklibs";
    }

    @Override
    public String prefix() {
        return getPrefix();
    }
}
