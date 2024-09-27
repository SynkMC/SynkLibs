package cc.synkdev.synkLibs.bukkit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@CommandAlias("slreport|sldump|synklibsreport|synklibsdump")
public class ReportCmd extends BaseCommand {
    private SynkLibs core;
    public ReportCmd(SynkLibs core) {
        this.core = core;
    }

    @Default
    @CommandPermission("synklibs.report")
    @Description("Send informations about your server for support")
    public void onReport(CommandSender sender) {
        try {
            URL url = new URL("https://dump.synkdev.cc/upload");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStream os = connection.getOutputStream();
            os.write(getJsonString().getBytes());
            os.flush();
            os.close();

            int resp = connection.getResponseCode();
            if (resp == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                org.json.JSONObject jsonResponse = new JSONObject(response.toString());
                String uuid = jsonResponse.getString("uuid");

                TextComponent comp = new TextComponent(core.prefix + ChatColor.GREEN + "Your report has been exported!\n"+core.prefix+ChatColor.GREEN+"Please save this code somewhere as it will be used by the support team: " + ChatColor.GOLD);
                TextComponent uuidComp = new TextComponent(ChatColor.GOLD+uuid);
                comp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, uuid));
                comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Click to open it in chat to copy it")}));
                comp.addExtra(uuidComp);

                if (sender instanceof Player) {
                    ((Player) sender).spigot().sendMessage(comp);
                } else {
                    sender.sendMessage(core.prefix + ChatColor.GREEN + "Your report has been exported!\n"+core.prefix+ChatColor.GREEN+"Please save this code somewhere as it will be used by the support team: " + ChatColor.GOLD+uuid);
                }
            } else {
                sender.sendMessage(core.prefix + ChatColor.RED + "There was an error while uploading your report!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getJsonString() {
        String s = "{\n" +
                "  \"serverVersion\": \""+ Bukkit.getServer().getVersion()+ "\",\n" +
                "  \"serverSoftware\": \""+ Bukkit.getServer().getBukkitVersion()+ "\",\n" +
                "  \"operatingSystem\": \""+ System.getProperty("os.name")+" "+System.getProperty("os.version")+" "+ System.getProperty("os.arch")+ "\",\n" +
                "  \"plugins\": [\n" +
                pluginsString() +
                "  ],\n" +
                "  \"additionalInfo\": {\n" +
                "    \"online-mode\": \""+Bukkit.getServer().getOnlineMode()+"\"\n" +
                "  },\n" +
                "  \"configs\": [\n" +
                configsString() +
                "  ],\n" +
                "  }\n" +
                "}\n";
        return s;
    }

    private String pluginsString() {
        List<String> list = new ArrayList<>();
        for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
            list.add("{\n" +
                    "      \"name\": \""+pl.getName()+"\",\n" +
                            "      \"version\": \""+pl.getDescription().getVersion()+"\"\n" +
                            "    }");
        }

        if (list.size() == 1) {
            return list.get(0);
        }
        StringBuilder sb = new StringBuilder();
        int index=0;
        for (int i = 0; i < list.size()-1; i++) {
            sb.append(list.get(i)).append(", \n");
            index = i;
        }
        sb.append(list.get(index+1));
        return sb.toString();
    }
    private String configsString() {
        List<String> list = new ArrayList<>();
        for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
            if (pl.getDescription().getAuthors().contains("Synk")) {

                list.add("{\n" +
                        "      \"name\": \""+pl.getName()+"\",\n" +
                        "      \"config\": \""+pl.getConfig().saveToString()+"\"\n" +
                        "    }");
            }

        }

        if (list.size() == 1) {
            return list.get(0);
        }
        StringBuilder sb = new StringBuilder();
        int index=0;
        for (int i = 0; i < list.size()-1; i++) {
            sb.append(list.get(i)).append(", \n");
            index = i;
        }
        sb.append(list.get(index+1));
        return sb.toString();
    }
}
