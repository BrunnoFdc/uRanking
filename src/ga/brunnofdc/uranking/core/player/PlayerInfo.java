package ga.brunnofdc.uranking.core.player;

import java.util.UUID;

import ga.brunnofdc.uranking.Main;
import ga.brunnofdc.uranking.core.Rank;
import ga.brunnofdc.uranking.core.RankManager;


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
