package ga.brunnofdc.uRanking.Listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import ga.brunnofdc.uRanking.Main;

public class GUIListener implements Listener {
	
	
	Main plugin;
	public GUIListener(Main m) {
		
		plugin = m;
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		String name = e.getInventory().getName();
		FileConfiguration config = plugin.getConfig();
		if(name.equalsIgnoreCase(config.getString("GUI.Nome").replace("&", "§"))) {
			
			e.setCancelled(true);
			
		}
		
	}

}
