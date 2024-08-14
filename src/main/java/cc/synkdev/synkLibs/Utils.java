package cc.synkdev.synkLibs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Utils implements Listener {
    private static SynkLibs core = SynkLibs.getInstance();
    public static void log(String s) {
        Bukkit.getConsoleSender().sendMessage(core.pluginPrefix+" "+s);
    }

    public static void checkUpdate(String plugin, String ver, String download) {
        try {
            URL url = new URL("https://synkdev.cc/ver/"+plugin);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals(ver)) {
                    log(ChatColor.GREEN + Lang.translate("upToDate"));
                } else {
                    log(ChatColor.GREEN + Lang.translate("updateAvailable") + ": v" + inputLine);
                    log(ChatColor.GREEN + Lang.translate("downloadHere") + ": "+download);
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
            core.log(Lang.translate("updateAvailable") + " "+s+"!");
            core.log(Lang.translate("downloadHere")+": "+core.updateLink.get(s));
        });
    }
}
