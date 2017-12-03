package ga.brunnofdc.uRanking.Core;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import ga.brunnofdc.uRanking.Main;


public class LocaleManager {


	private static File file;
	private static FileConfiguration userfile;
	
	public static void setupMensagens(Main plugin) {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
	
	    file = new File(plugin.getDataFolder(), "mensagens.yml");
	    
	    if(!file.exists()) {
	    	try {
		        
		    	  plugin.saveResource("mensagens.yml", false);
		        
	    	} catch (Exception localException1) {
		    	  
		    	  Bukkit.getConsoleSender().sendMessage("§cNão foi possível criar o arquivo mensagens.yml!");
		    	  localException1.printStackTrace();
		    	  
		    }
	    
	    }
	
	userfile = YamlConfiguration.loadConfiguration(file);
	
	
	}

	public static void reload() {
		
		userfile = YamlConfiguration.loadConfiguration(file);
		
		
	}
	
	public static FileConfiguration getMensagens() {
	    
		return userfile;
	  
	}
	
}
