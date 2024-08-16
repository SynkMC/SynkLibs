package cc.synkdev.synkLibs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Lang {
    public File file;
    SynkLibs core = SynkLibs.getInstance();
    public Lang(File dataFolder) {
        file = new File(dataFolder.getParent(), "SynkLibs");
        init();
    }

    public FileConfiguration config;

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
        config = YamlConfiguration.loadConfiguration(file);

        config.addDefault("error", "An error has occurred. Please check the console for errors");
        config.addDefault("updateAvailable", "An update is available for");
        config.addDefault("downloadHere", "Download it here");
        config.addDefault("upToDate", "is up to date!");
        config.addDefault("success", "Success!");

        config.options().copyDefaults(true);
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public String translate(String s) {
        return removeEnds(config.getString(s));
    }

    public String removeEnds(String s) {
        return s.split("\"")[0];
    }
}
