package ga.brunnofdc.uRanking.Hook;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.net.fabiozumbi12.UltimateChat.API.SendChannelMessageEvent;
import ga.brunnofdc.uRanking.Main;
import ga.brunnofdc.uRanking.Core.Player.PlayerInfo;

public class UltimateChat implements Listener {
	
	Main plugin;
	public UltimateChat(Main m) {
	
		plugin = m;
		
	}
	
	@EventHandler
	public void onChat(SendChannelMessageEvent e) {
		
		Player p = (Player) e.getSender();
		PlayerInfo pInfo = new PlayerInfo(p.getUniqueId());
		e.addTag("{rank}", pInfo.getPlayerRank().getTag());
		e.addTag("{rank_name}", pInfo.getPlayerRank().getRankName());
		e.addTag("{rank_id}", pInfo.getPlayerRank().getRankId());
		
	}

}
