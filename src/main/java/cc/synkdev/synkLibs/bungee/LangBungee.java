package cc.synkdev.synkLibs.bungee;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.net.URL;

public class LangBungee {
    public File file;
    SynkLibsBungee core = SynkLibsBungee.getInstance();
    public LangBungee(File dataFolder) {
        file = new File(dataFolder.getParent(), "SynkLibs");
        init();
    }

    public Configuration config;

    public void init() {
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(file, "lang.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            load();
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        File tempFile = new File(file.getParent(), "temp-"+System.currentTimeMillis()+".yml");
        try {
            URL url = new URL("https://synkdev.cc/storage/lang-libs-bungee.php");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                for (String s : line.split("<br>")) {
                    String id = s.split(":")[0];
                    if (config.contains(id)) {
                        writer.write(id + ": " + config.getString(id));
                        writer.newLine();
                    } else {
                        writer.write(s);
                        writer.newLine();
                    }
                }
            }
            reader.close();
            writer.close();
            tempFile.renameTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String translate(String s) {
        return removeEnds(config.getString(s));
    }

    public String translate(String s, String s1) {
        return translate(s).replace("%s1%", s1);
    }

    public String translate(String s, String s1, String s2) {
        return translate(s, s1).replace("%s2%", s2);
    }

    public String translate(String s, String s1, String s2, String s3) {
        return translate(s, s1, s2).replace("%s3%", s3);
    }

    public String removeEnds(String s) {
        return s.split("\"")[0];
    }
}
