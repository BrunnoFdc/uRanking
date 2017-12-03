package ga.brunnofdc.uRanking.Hook;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import ga.brunnofdc.uRanking.Main;
import ga.brunnofdc.uRanking.Core.Player.PlayerInfo;

public class Legendchat implements Listener {
	
	Main plugin;
	public Legendchat(Main m) {
	
		plugin = m;
		
	}
	
	@EventHandler
	public void onChat(ChatMessageEvent e) {
		
		Player p = e.getSender();
		PlayerInfo pInfo = new PlayerInfo(p.getUniqueId());
		e.setTagValue("rank", pInfo.getPlayerRank().getTag());
	}

}
