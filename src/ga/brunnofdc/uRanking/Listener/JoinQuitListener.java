package ga.brunnofdc.uRanking.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ga.brunnofdc.uRanking.Main;
import ga.brunnofdc.uRanking.Core.RankManager;
import ga.brunnofdc.uRanking.Util.Update;

public class JoinQuitListener implements Listener {
	
	Main plugin;
	public JoinQuitListener(Main m) {
		
	   plugin = m;
	
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		Update.avisoUpdates(e.getPlayer());
		
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
