package ga.brunnofdc.uranking.hooks;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankCacheManager;
import ga.brunnofdc.uranking.ranking.RankedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Legendchat implements Listener, Hook {

    @EventHandler
    public void onChat(ChatMessageEvent e) {

        Player p = e.getSender();

        RankedPlayer rankedPlayer = RankCacheManager.getRankedPlayer(p);
        if(rankedPlayer != null) {

            Rank rank = rankedPlayer.getRank();
            e.addTag("rank", rank.getPrefix());

        }

    }

    @Override
    public String getRelativePlugin() {
        return "Legendchat";
    }

    @Override
    public void setupHook(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
