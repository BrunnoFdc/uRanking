package ga.brunnofdc.uranking.utils;

import ga.brunnofdc.uranking.uRanking;
import ga.brunnofdc.uranking.utils.enums.Message;
import ga.brunnofdc.uranking.utils.enums.SingleLineMessage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Language {

    private static FileConfiguration cfg;
    private static File file;

    public static void setup(Plugin plugin) {

        String langID = uRanking.getInstance().getConfig().getString("System-Settings.Language");
        String langFileName = "language_" + langID  + ".yml";

        file = new File(plugin.getDataFolder(), langFileName);
        if (!file.exists()) {
            InputStream langFile = plugin.getResource("languages/" + langFileName);
            if (langFile != null) {
                saveResource(langFile, file);
                uRanking.getInstance().getLogger().info("Created localization file for the language: " + langID);
            } else {
                saveResource(plugin.getResource("languages/language_en.yml"), file);
                uRanking.getInstance().getLogger().info("Created localization file with the default language.");
            }
        }

        cfg = YamlConfiguration.loadConfiguration(file);

    }

    public static StringList getMessage(Message message) {
        StringList result = new StringList(cfg.getStringList(message.getPath()));
        return result.translateColors();
    }

    public static String getSingleLineMessage(SingleLineMessage message) {
        String msg = cfg.getString(message.getPath());
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    private static void saveResource(InputStream file, File saveTo) {
        try {
            FileOutputStream fos = new FileOutputStream(saveTo);
            while (file.available() > 0) {
                fos.write(file.read());
            }
            fos.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload() {
        cfg = YamlConfiguration.loadConfiguration(file);
    }

}
