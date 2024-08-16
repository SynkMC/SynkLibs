package cc.synkdev.synkLibs;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Utils implements Listener {
    private static SynkLibs score = SynkLibs.getInstance();
    private SynkLibs core = SynkLibs.getInstance();
    Lang lang = new Lang(core);
    @Setter private static String pluginPrefix = null;
    public static void log(String s) {
        Bukkit.getConsoleSender().sendMessage(pluginPrefix+" "+s);
    }
    public void log(String s, Boolean prefix) {
        if (prefix) s = core.pluginPrefix+" "+s;
        Bukkit.getConsoleSender().sendMessage(s);
    }

    public void checkUpdate(String plugin, String ver, String download) {
        core.setPluginPrefix(core.prefix);
        try {
            URL url = new URL("https://synkdev.cc/ver/"+plugin);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals(ver)) {
                    log(ChatColor.GREEN + plugin + " " + lang.translate("upToDate"));
                } else {
                    log(ChatColor.GREEN + lang.translate("updateAvailable") + " " + plugin + ": v" + inputLine);
                    log(ChatColor.GREEN + lang.translate("downloadHere") + ": "+download);
                    if (core.availableUpdates.containsKey(plugin)) core.availableUpdates.replace(plugin, ver);
                    else core.availableUpdates.put(plugin, ver);
                    if (core.updateLink.containsKey(plugin)) core.updateLink.replace(plugin, download);
                    else core.updateLink.put(plugin, download);
                }
                break;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void join (PlayerJoinEvent event) {
        if (((OfflinePlayer) event.getPlayer()).isOp()) core.availableUpdates.forEach((s, s2) -> {
            Player p = event.getPlayer();
            core.log(lang.translate("updateAvailable") + " "+s+"!");
            core.log(lang.translate("downloadHere")+": "+core.updateLink.get(s));
        });
    }
}
