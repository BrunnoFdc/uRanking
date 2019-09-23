package ga.brunnofdc.uranking.api;

import java.util.UUID;

import org.bukkit.entity.Player;

import ga.brunnofdc.uranking.core.player.PlayerInfo;

public class RankAPI {
	
	
	public UUID pUUID; 
	public RankAPI(Player p) {
		
		this.pUUID = p.getUniqueId();
		
		
	}
	
	PlayerInfo pInfo = new PlayerInfo(pUUID);

	public String getRank() {
		
		return pInfo.getPlayerRank().getRankId();
		
	}
	
	public String getRankName() {
		
		return pInfo.getPlayerRank().getRankName();
		
	}
	
	public String getRankTag() {
		
		return pInfo.getPlayerRank().getTag();
		
	}
	
	public String getNextRank() {
		
		return pInfo.getNextRank().getRankId();
		
	}
	
	public String getNextRankName() {
		
		return pInfo.getNextRank().getRankName();
		
	}
	
	public String getNextRankTag() {
		
		return pInfo.getNextRank().getTag();
		
	}
	
	public double getNextRankPrice() {
		
		return pInfo.getNextRank().getPreco();
		
	}

}
