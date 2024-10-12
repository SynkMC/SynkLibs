package cc.synkdev.synkLibs.bukkit;

import cc.synkdev.synkLibs.components.SynkPlugin;
import com.google.gson.Gson;
import org.bukkit.ChatColor;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Lang {
    public static String getToken() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://synkdev.cc/storage/token-crowdin.php").openStream()));
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> init(SynkPlugin plugin, File langFile) {
        Map<String, String> map = new HashMap<>();
        SynkLibs.getInstance().reloadConfig();
        String globLang = SynkLibs.getInstance().getConfig().getString("lang");
        if (globLang.equalsIgnoreCase("custom")) {
            if (langFile.exists()) {
                try {
                    Map<String, String> curr = load(langFile);
                    File temp = new File(langFile.getParentFile(), "temp-" + System.currentTimeMillis() + ".json");
                    temp.createNewFile();

                    BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(plugin.lang().replace("lang-pld", "en")).openStream()));

                    String ln;
                    while ((ln = reader.readLine()) != null) {
                        writer.write(ln);
                        writer.newLine();
                    }
                    writer.close();

                    Map<String, String> tempMap = new HashMap<>(load(temp));
                    for (Map.Entry<String, String> entry : tempMap.entrySet()) {
                        if (!curr.containsKey(entry.getKey())) {
                            curr.put(entry.getKey(), entry.getValue());
                        }
                    }
                    temp.delete();
                    save(langFile, curr);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    if (!langFile.getParentFile().exists()) langFile.getParentFile().mkdirs();
                    langFile.createNewFile();

                    BufferedWriter writer = new BufferedWriter(new FileWriter(langFile));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(plugin.lang().replace("lang-pld", "en")).openStream()));

                    String ln;
                    while ((ln = reader.readLine()) != null) {
                        writer.write(ln);
                        writer.newLine();
                    }
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            map.putAll(load(langFile));
        } else {
            String lang = globLang;
            if (!folderExists(globLang)) {
                Utils.log(ChatColor.RED+"The language "+globLang+" doesn't exist! Visit https://synkdev.cc/storage/translations for the full list! Using english as a fallback language.");
                lang = "en";
            }
            try {
                File temp = new File(langFile.getParentFile(), "temp-"+System.currentTimeMillis()+".json");
                if (!temp.getParentFile().exists()) temp.getParentFile().mkdirs();
                temp.createNewFile();

                BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(plugin.lang().replace("lang-pld", lang)).openStream()));

                String ln;
                while ((ln = reader.readLine()) != null) {
                    writer.write(ln);
                    writer.newLine();
                }
                writer.close();
                map.putAll(load(temp));
                temp.delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return map;
    }

    public static void save(File file, Map<String, String> map) {

    }

    public static Boolean folderExists(String lang) {
        try {
            URL url = new URL("https://synkdev.cc/storage/translations/"+lang);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Map<String, String> load(File file) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            return new HashMap<String, String>(gson.fromJson(reader, HashMap.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String translate(String key, SynkPlugin spl, String... placeholders) {
        String translatedString = ChatColor.translateAlternateColorCodes('&', spl.langMap().getOrDefault(key, "Invalid translation!"));

        for (int i = 0; i < placeholders.length; i++) {
            translatedString = translatedString.replace("%s" + (i + 1) + "%", placeholders[i]);
        }

        return translatedString;
    }

    public String removeEnds(String s) {
        return s.split("\"")[0];
    }
}
