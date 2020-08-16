package ga.brunnofdc.uranking.hooks;

import br.net.fabiozumbi12.UltimateChat.Bukkit.API.SendChannelMessageEvent;
import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankCacheManager;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.ranking.RankedPlayer;
import ga.brunnofdc.uranking.utils.Language;
import ga.brunnofdc.uranking.utils.enums.SingleLineMessage;
import ga.brunnofdc.uranking.utils.exceptions.MaxRankException;
import ga.brunnofdc.uranking.utils.exceptions.MinRankException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class UltimateChat implements Listener, Hook {

    //TODO: Modify to use the new abstracted placeholders logic
    @EventHandler
    public void onChat(SendChannelMessageEvent e) {

        Player p = (Player) e.getSender();
        RankedPlayer rankedPlayer = RankCacheManager.getRankedPlayer(p);
        if(rankedPlayer != null) {

            Rank rank = rankedPlayer.getRank();
            Rank nextRank = null;
            Rank oldRank = null;

            try {
                nextRank = RankUtils.getNextRank(rank);
                oldRank = RankUtils.getOldRank(rank);
            } catch (MaxRankException | MinRankException ignored) {}

            e.addTag("{rank}", rank.getPrefix());
            e.addTag("{rank_name}", rank.getName());
            e.addTag("{rank_id}", rank.getID());
            e.addTag("{rank_position}", String.valueOf(rank.getPosition()));
            e.addTag("{rank_price}", String.valueOf(rank.getPrice()));

            e.addTag("{oldrank}", (oldRank != null) ? oldRank.getPrefix() : Language.getSingleLineMessage(SingleLineMessage.NO_OLD_RANK));
            e.addTag("{oldrank_name}", (oldRank != null) ? oldRank.getName() : Language.getSingleLineMessage(SingleLineMessage.NO_OLD_RANK));
            e.addTag("{oldrank_id}", (oldRank != null) ? oldRank.getID() : Language.getSingleLineMessage(SingleLineMessage.NO_OLD_RANK));
            e.addTag("{oldrank_position}", (oldRank != null) ? String.valueOf(oldRank.getPosition()) : Language.getSingleLineMessage(SingleLineMessage.NO_OLD_RANK));
            e.addTag("{oldrank_price}", (oldRank != null) ? String.valueOf(oldRank.getPrice()) : Language.getSingleLineMessage(SingleLineMessage.NO_OLD_RANK));

            e.addTag("{nextrank}", (nextRank != null) ? nextRank.getPrefix() : Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK));
            e.addTag("{nextrank_name}", (nextRank != null) ? nextRank.getName() : Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK));
            e.addTag("{nextrank_id}", (nextRank != null) ? nextRank.getID() : Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK));
            e.addTag("{nextrank_position}", (nextRank != null) ? String.valueOf(nextRank.getPosition()) : Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK));
            e.addTag("{nextrank_price}", (nextRank != null) ? String.valueOf(nextRank.getPrice()) : Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK));


        }
    }

    @Override
    public String getRelativePlugin() {
        return "UltimateChat";
    }

    @Override
    public void setupHook(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
