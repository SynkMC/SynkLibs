package cc.synkdev.synkLibs;

import cc.synkdev.synkLibs.components.SynkPlugin;
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
    private static SynkLibs score = SynkLibs.getInstance();
    private SynkLibs core = SynkLibs.getInstance();
    SynkPlugin spl;
    public Utils(SynkPlugin spl) {
        this.spl = spl;
    }
    Lang lang = new Lang(core);
    public static void log(String s) {
        Bukkit.getConsoleSender().sendMessage(score.getSpl().prefix()+" "+s);
    }
    public void log(String s, Boolean prefix) {
        if (prefix) s = core.prefix+" "+s;
        Bukkit.getConsoleSender().sendMessage(s);
    }

    public void checkUpdate() {
        try {
            URL url = new URL("https://synkdev.cc/ver/"+spl.name());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals(spl.ver())) {
                    log(ChatColor.GREEN + spl.name() + " " + lang.translate("upToDate"));
                } else {
                    log(ChatColor.GREEN + lang.translate("updateAvailable") + " " + spl.name() + ": v" + inputLine);
                    log(ChatColor.GREEN + lang.translate("downloadHere") + ": "+spl.dlLink());
                    if (core.availableUpdates.containsKey(spl)) core.availableUpdates.replace(spl, inputLine);
                    else core.availableUpdates.put(spl, inputLine);
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
            core.log(lang.translate("downloadHere")+": "+s.dlLink());
        });
    }
}
