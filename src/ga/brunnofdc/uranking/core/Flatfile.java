package ga.brunnofdc.uranking.core;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import ga.brunnofdc.uranking.Main;

public class Flatfile {
	
	private static FileConfiguration userfile;
	private static File file;

	
	public static FileConfiguration getData() {
	    
		return userfile;
	  
	}
	
	public static void setupDataFile(Main plugin) {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		
		file = new File(plugin.getDataFolder(), "userdata.yml");
	
		if (!file.exists()) {
			try {
			
				file.createNewFile();
		
			} catch (IOException e) {
			
				Bukkit.getLogger().severe("[uRanking] Não foi possível criar o arquivo userdata.yml!");
				e.printStackTrace();
			}
		}
	
	userfile = YamlConfiguration.loadConfiguration(file);
		
	}
	
	public static void saveData() {
		try {
			
			userfile.save(file);
			
		} catch(Exception e) {
			
			Bukkit.getLogger().severe("[uRanking] Não foi possível salvar o arquivo userdata.yml");
			e.printStackTrace();
			
		}
		
	}
	

}
