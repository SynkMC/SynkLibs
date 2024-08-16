package cc.synkdev.synkLibs.bukkit;

import cc.synkdev.synkLibs.components.SynkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Utils implements Listener {
    private static SynkLibs score = SynkLibs.getInstance();
    private SynkLibs core = SynkLibs.getInstance();
    SynkPlugin spl;
    static Lang slang;
    public Utils(SynkPlugin spl) {
        this.spl = spl;
    }
    Lang lang;
    public static void log(String s) {
        Bukkit.getConsoleSender().sendMessage(score.getSpl().prefix()+" "+s);
    }
    public void log(String s, Boolean prefix) {
        if (prefix) s = core.prefix+" "+s;
        Bukkit.getConsoleSender().sendMessage(s);
    }

    public static void checkUpdate(SynkPlugin spl, JavaPlugin plugin) {
        checkUpdate(spl, plugin.getDataFolder());
    }

    private static void checkUpdate(SynkPlugin spl, File dataFolder) {
        slang = new Lang(dataFolder);
        try {
            URL url = new URL("https://synkdev.cc/ver/"+spl.name());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals(spl.ver())) {
                    log(ChatColor.GREEN + spl.name() + " " + slang.translate("upToDate"));
                } else {
                    log(ChatColor.GREEN + slang.translate("updateAvailable") + " " + spl.name() + ": v" + inputLine);
                    log(ChatColor.GREEN + slang.translate("downloadHere") + ": "+spl.dlLink());
                    if (SynkLibs.availableUpdates.containsKey(spl)) SynkLibs.availableUpdates.replace(spl, inputLine);
                    else SynkLibs.availableUpdates.put(spl, inputLine);
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
        lang = new Lang(core.getDataFolder());
        if (((OfflinePlayer) event.getPlayer()).isOp()) core.availableUpdates.forEach((s, s2) -> {
            Player p = event.getPlayer();
            p.sendMessage(lang.translate("updateAvailable") + " "+s+"!");
            p.sendMessage(lang.translate("downloadHere")+": "+s.dlLink());
        });
    }
}
