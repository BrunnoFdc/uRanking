package ga.brunnofdc.uranking.utils;

import ga.brunnofdc.uranking.utils.enums.Property;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class SystemDefs {

    private static Map<Property, Boolean> defines = new HashMap<>();

    public static boolean getProp(Property property) {
        return defines.get(property);
    }

    public static void loadProps(FileConfiguration config) {
        for(Property prop : Property.values()) {
            boolean value = config.getBoolean(prop.configPath);
            defines.put(prop, value);
        }

    }


}
