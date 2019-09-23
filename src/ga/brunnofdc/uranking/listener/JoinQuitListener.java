package ga.brunnofdc.uranking.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ga.brunnofdc.uranking.Main;
import ga.brunnofdc.uranking.core.RankManager;

public class JoinQuitListener implements Listener {
	
	Main plugin;
	public JoinQuitListener(Main m) {
		
	   plugin = m;
	
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
				
		RankManager rank = new RankManager();
		rank.setRank(e.getPlayer());
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
	
		if(RankManager.PLAYER_RANKS.containsKey(e.getPlayer().getUniqueId())) {
			
			RankManager.PLAYER_RANKS.remove(e.getPlayer().getUniqueId());
		
		}
		
	}

}
