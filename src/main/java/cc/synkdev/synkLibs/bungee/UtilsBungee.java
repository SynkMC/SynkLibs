package cc.synkdev.synkLibs.bungee;

import cc.synkdev.synkLibs.components.SynkPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UtilsBungee implements Listener {
    private final SynkLibsBungee bcore = SynkLibsBungee.getInstance();
    private static final SynkLibsBungee score = SynkLibsBungee.getInstance();
    SynkPlugin spl;
    static LangBungee slang;
    public UtilsBungee(SynkPlugin spl) {
        this.spl = spl;
    }
    LangBungee lang;
    public static void log(String s) {
        score.getProxy().getConsole().sendMessage(score.getSpl().prefix()+" "+s);
    }

    public static void checkUpdate(SynkPlugin spl, Plugin plugin) {
        checkUpdate(spl, plugin.getDataFolder());
    }

    private static void checkUpdate(SynkPlugin spl, File dataFolder) {
        slang = new LangBungee(dataFolder);
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
                    if (SynkLibsBungee.availableUpdates.containsKey(spl)) SynkLibsBungee.availableUpdates.replace(spl, inputLine);
                    else SynkLibsBungee.availableUpdates.put(spl, inputLine);
                }
                break;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @EventHandler
    public void joinBungee (PostLoginEvent event) {
        lang = new LangBungee(bcore.getDataFolder());
        if (event.getPlayer().hasPermission("synklibs.bungee.updatenotifier")) bcore.availableUpdates.forEach((s, s2) -> {
            ProxiedPlayer p = event.getPlayer();
            p.sendMessage(lang.translate("updateAvailable") + " "+s+"!");
            p.sendMessage(lang.translate("downloadHere")+": "+s.dlLink());
        });
    }
}
