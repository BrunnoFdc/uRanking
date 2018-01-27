package ga.brunnofdc.uRanking.Hook;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.net.fabiozumbi12.UltimateChat.Bukkit.API.SendChannelMessageEvent;
import ga.brunnofdc.uRanking.Main;
import ga.brunnofdc.uRanking.Core.RankManager;
import ga.brunnofdc.uRanking.Core.Player.PlayerInfo;

public class UltimateChat implements Listener {
	
	Main plugin;
	public UltimateChat(Main m) {
	
		plugin = m;
		
	}
	
	@EventHandler
	public void onChat(SendChannelMessageEvent e) {
		
		Player p = (Player) e.getSender();
		
		if(!RankManager.PLAYER_RANKS.containsKey(p.getUniqueId())) {
			
			p.sendMessage("§cNão foi possível encontrar seu rank, por favor, relogue!");
			
		} else {
			
			PlayerInfo pInfo = new PlayerInfo(p.getUniqueId());
			if(!p.hasPermission("uranking.hidetag")) {
				
				e.addTag("{rank}", pInfo.getPlayerRank().getTag());
				e.addTag("{rank_name}", pInfo.getPlayerRank().getRankName());
				e.addTag("{rank_id}", pInfo.getPlayerRank().getRankId());
				
			}
			
		}
		
		
		
	}

}
