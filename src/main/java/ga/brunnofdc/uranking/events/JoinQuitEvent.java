package ga.brunnofdc.uranking.events;

import ga.brunnofdc.uranking.data.DataManager;
import ga.brunnofdc.uranking.ranking.RankCacheManager;
import ga.brunnofdc.uranking.ranking.RankedPlayer;
import ga.brunnofdc.uranking.uRanking;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(uRanking.getInstance(), () -> {
            RankedPlayer ranked = new RankedPlayer(player);
            RankCacheManager.storePlayer(player, ranked);
        });

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        Player player = e.getPlayer();
        RankedPlayer rankedPlayer = RankCacheManager.getRankedPlayer(player);

        if(rankedPlayer != null) {
            DataManager.getDataSource().set(player, rankedPlayer.getRank().getID());
            RankCacheManager.removePlayer(player);
        }

    }

}
