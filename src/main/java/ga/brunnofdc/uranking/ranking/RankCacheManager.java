package ga.brunnofdc.uranking.ranking;

import ga.brunnofdc.uranking.data.DataManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


public class RankCacheManager {

    private static Map<Player, RankedPlayer> rankedPlayers = new HashMap<>();

    public static void storePlayer(Player player, RankedPlayer rankedPlayer) {
        rankedPlayers.put(player, rankedPlayer);
    }

    public static void removePlayer(Player player) {
        rankedPlayers.remove(player);
    }

    public static void saveAllCachedData() {
        rankedPlayers.values().forEach(rp -> {
            DataManager.getDataSource().set(rp.getPlayerUUID(), rp.getRank().getID());
        });
    }

    public static void emptyCache() {
        saveAllCachedData();
        rankedPlayers.clear();
    }

    public static RankedPlayer getRankedPlayer(Player player) {
        return rankedPlayers.get(player);
    }

}
