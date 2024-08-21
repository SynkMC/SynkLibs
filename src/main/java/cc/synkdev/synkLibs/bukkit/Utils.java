package cc.synkdev.synkLibs.bukkit;

import cc.synkdev.synkLibs.components.SynkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
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
    public static FileConfiguration loadWebConfig(String url, File file) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        File temp = new File(file.getParentFile(), "temp-"+System.currentTimeMillis()+".yml");
        try {
            URL uri = new URL(url);
            if (!temp.exists()) temp.createNewFile();
            BufferedReader reader = new BufferedReader(new InputStreamReader(uri.openStream()));

            BufferedWriter writer = new BufferedWriter(new FileWriter(temp));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] lines = line.split("<br>");
                for (String liness : lines) {
                    String[] split = liness.split(";");
                    if (split.length == 3) {
                        if (!config.contains(split[1])) {
                            writer.write("# " + split[0]);
                            writer.newLine();
                            writer.write(split[1] + ": " + split[2]);
                            writer.newLine();
                        } else {
                            writer.write("# " + split[0]);
                            writer.newLine();
                            writer.write(split[1] + ": " + config.get(split[1]));
                            writer.newLine();
                        }
                    } else {
                        if (!config.contains(split[0])) {
                            writer.write(split[0] + ": " + split[1]);
                            writer.newLine();
                        } else {
                            writer.write(split[0] + ": " + config.get(split[0]));
                            writer.newLine();
                        }
                    }
                }
            }

            reader.close();
            writer.close();
            temp.renameTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
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
