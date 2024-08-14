package cc.synkdev.synkLibs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Lang {
    public Lang() {
        init();
    }

    public static FileConfiguration config;
    SynkLibs core = SynkLibs.getInstance();
    public File file = new File(core.getDataFolder(), "lang.yml");

    public void init() {
        if (!core.getDataFolder().exists()) {
            core.getDataFolder().mkdirs();
        }
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
        config.addDefault("upToDate", "The plugin is up to date!");
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

    public static String translate(String s) {
        return removeEnds(config.getString(s));
    }

    public static String removeEnds(String s) {
        return s.split("\"")[0];
    }
}
