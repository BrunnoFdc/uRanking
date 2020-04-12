package ga.brunnofdc.uranking.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MiscUtils {

    public static ItemStack getItemStackByItemID(String itemID) {
        String[] materialID;

        if(itemID.contains(":")) {
            materialID = itemID.split(":");
        } else {
            materialID = new String[]{itemID};
        }

        if(isIntParseable(materialID[0])) {
            int id = Integer.parseInt(materialID[0]);
            if(materialID.length > 1) {
                return new ItemStack(Material.getMaterial(id), 1, Short.parseShort(materialID[1]));
            } else {
                return new ItemStack(Material.getMaterial(id));
            }
        } else {
            if(materialID.length > 1) {
                return new ItemStack(Material.getMaterial(materialID[0]), 1, Short.parseShort(materialID[1]));
            } else {
                return new ItemStack(Material.getMaterial(materialID[0]));
            }
        }

    }

    private static boolean isIntParseable(String parse) {
        try {
            int i = Integer.parseInt(parse);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}
