package ga.brunnofdc.uRanking.Util;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Title {

    /**
    * Send a title to player
    * @param player Player to send the title to
    * @param title The text displayed in the title
    * @param subTitle The subtext of the title
    */
    public static void sendTitleSubTitle(Player player, String title, String subTitle)
    {
        try
        {
            Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}");
            Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + subTitle + "\"}");
            

            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object Title = titleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle, 20, 40, 30);
            Object SubTitle = titleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatSubtitle, 20, 40, 30);

            sendPacket(player, Title);
            sendPacket(player, SubTitle);
        }

        catch (Exception ex)
        {
            //Do something
        }
    }

    private static void sendPacket(Player player, Object packet)
    {
        try
        {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch(Exception ex)
        {
            //Do something
        }
    }

    /**
    * Get NMS class using reflection
    * @param name Name of the class
    * @return Class
    */
    private static Class<?> getNMSClass(String nmsClassString) {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = "net.minecraft.server." + version + nmsClassString;
        Class<?> nmsClass = null;
		try {
			
			nmsClass = Class.forName(name);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
        
		return nmsClass;
		
    }
 
	
}
