package ga.brunnofdc.uRanking.Core.Player;

import java.util.UUID;

import ga.brunnofdc.uRanking.Main;
import ga.brunnofdc.uRanking.Core.Rank;
import ga.brunnofdc.uRanking.Core.RankManager;


public class PlayerInfo {

	private UUID player;

	public PlayerInfo(UUID player) {
		this.player = player;
	}

	public UUID getPlayer() {
		return this.player;
	}

	public void removeRank() {
		if (RankManager.PLAYER_RANKS.containsKey(player)) {
			RankManager.PLAYER_RANKS.remove(player);
		}
	
	}

	public Rank getPlayerRank() {
	
		return RankManager.PLAYER_RANKS.containsKey(player) ? RankManager.PLAYER_RANKS.get(player) : null;
	
	}

	public Rank getNextRank() {
	
		return Main.RANKS_ORDERED.get(RankManager.PLAYER_RANKS.get(player).getPosition() + 1);
	
	}
	
}
