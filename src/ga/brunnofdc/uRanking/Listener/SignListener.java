package ga.brunnofdc.uRanking.Listener;

import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import ga.brunnofdc.uRanking.Main;
import ga.brunnofdc.uRanking.Core.Rank;
import ga.brunnofdc.uRanking.Core.RankManager;

@SuppressWarnings("unused")
public class SignListener extends Main implements Listener {
	
	Main plugin;
	FileConfiguration config = plugin.getConfig();
	public SignListener(Main m) {
		
		plugin = m;
		
	}
	
	
	@EventHandler
	public void onSignCreate(SignChangeEvent e) {
		Player p = e.getPlayer();
		if(e.getLine(0).equalsIgnoreCase("[rankup]") && e.getLine(1) == null) {
			if(p.hasPermission("uranking.admin")) {
				
				Sign s = (Sign) e.getBlock();
				s.setLine(0, config.getString("Placa.Linha1.Rankup").replace("&", "§"));
				s.setLine(1, config.getString("Placa.Linha2").replace("&", "§"));
				s.setLine(2, config.getString("Placa.Linha3").replace("&", "§"));
				s.setLine(3, config.getString("Placa.Linha4").replace("&", "§"));

				Main.setupHooks();
			}
			
		}
		
		
	}
	
	private String addPlaceholders(String s) {
		
		return s.replace("&", "§");

	}
	
	private String addPlaceholders(String s, String line2) {
		
		Rank rank = new RankManager().getRankById(line2);
		return s.replace("&", "§")
				.replace("@rank", rank.getRankName())
				.replace("@tag", rank.getTag())
				.replace("@rankid", line2);
		
		
		
	}

}
