package ga.brunnofdc.uranking.config;

import ga.brunnofdc.uranking.uRanking;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ProgressBarConfig {

    private ProgressBarConfig() {}

    private static final FileConfiguration config = uRanking.getInstance().getConfig();
    private static final ConfigurationSection section = config.getConfigurationSection("Misc.Progress-Bar");

    public static char getCharacter() {
        return section.getString("Character", "|").charAt(0);
    }

    public static int getCharacterAmount() {
        return section.getInt("Character-Amount", 20);
    }

    public static char getActualCharColor() {
        return section.getString("Color-Actual-Char", "2").charAt(0);
    }

    public static char getFilledColor() {
        return section.getString("Color-Filled", "a").charAt(0);
    }

    public static char getRemaningColor() {
        return section.getString("Color-Remaining", "8").charAt(0);
    }

}
