package ga.brunnofdc.uranking.hooks;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankCacheManager;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.utils.Language;
import ga.brunnofdc.uranking.utils.enums.SingleLineMessage;
import ga.brunnofdc.uranking.utils.exceptions.MaxRankException;
import ga.brunnofdc.uranking.utils.exceptions.MinRankException;
import org.bukkit.plugin.java.JavaPlugin;

public class MVdWPlugins implements Hook {
    @Override
    public String getRelativePlugin() {
        return "MVdWPlaceholderAPI";
    }

    @Override
    public void setupHook(JavaPlugin plugin) {
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_rankname", event -> {
            Rank rank = RankCacheManager.getRankedPlayer(event.getPlayer()).getRank();
            return rank.getName();
        });
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_rankid", event -> {
            Rank rank = RankCacheManager.getRankedPlayer(event.getPlayer()).getRank();
            return rank.getID();
        });
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_rankprefix", event -> {
            Rank rank = RankCacheManager.getRankedPlayer(event.getPlayer()).getRank();
            return rank.getPrefix();
        });
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_rankpos", event -> {
            Rank rank = RankCacheManager.getRankedPlayer(event.getPlayer()).getRank();
            return String.valueOf(rank.getPosition());
        });
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_rankprice", event -> {
            Rank rank = RankCacheManager.getRankedPlayer(event.getPlayer()).getRank();
            return String.valueOf(rank.getPrice());
        });

        PlaceholderAPI.registerPlaceholder(plugin, "uranking_nextrankname", event -> {
            try {
                Rank rank = RankUtils.getNextRank(RankCacheManager.getRankedPlayer(event.getPlayer()).getRank());
                return rank.getName();
            } catch (MaxRankException e) {
                return Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK);
            }

        });
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_nextrankid", event -> {
            try {
                Rank rank = RankUtils.getNextRank(RankCacheManager.getRankedPlayer(event.getPlayer()).getRank());
                return rank.getID();
            } catch (MaxRankException e) {
                return Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK);
            }
        });
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_nextrankprefix", event -> {
            try {
                Rank rank = RankUtils.getNextRank(RankCacheManager.getRankedPlayer(event.getPlayer()).getRank());
                return rank.getPrefix();
            } catch (MaxRankException e) {
                return Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK);
            }
        });
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_nextrankpos", event -> {
            Rank actualRank = RankCacheManager.getRankedPlayer(event.getPlayer()).getRank();
            try {
                Rank rank = RankUtils.getNextRank(actualRank);
                return String.valueOf(rank.getPosition());
            } catch (MaxRankException e) {
                return Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK);
            }
        });
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_nextrankprice", event -> {
            Rank actualRank = RankCacheManager.getRankedPlayer(event.getPlayer()).getRank();
            try {
                Rank rank = RankUtils.getNextRank(actualRank);
                return String.valueOf(rank.getPrice());
            } catch (MaxRankException e) {
                return "0.00";
            }
        });

        PlaceholderAPI.registerPlaceholder(plugin, "uranking_oldrankname", event -> {
            try {
                Rank rank = RankUtils.getOldRank(RankCacheManager.getRankedPlayer(event.getPlayer()).getRank());
                return rank.getName();
            } catch (MinRankException e) {
                return Language.getSingleLineMessage(SingleLineMessage.NO_OLD_RANK);
            }

        });
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_oldrankid", event -> {
            try {
                Rank rank = RankUtils.getOldRank(RankCacheManager.getRankedPlayer(event.getPlayer()).getRank());
                return rank.getID();
            } catch (MinRankException e) {
                return Language.getSingleLineMessage(SingleLineMessage.NO_OLD_RANK);
            }
        });
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_oldrankprefix", event -> {
            try {
                Rank rank = RankUtils.getOldRank(RankCacheManager.getRankedPlayer(event.getPlayer()).getRank());
                return rank.getPrefix();
            } catch (MinRankException e) {
                return Language.getSingleLineMessage(SingleLineMessage.NO_OLD_RANK);
            }
        });
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_oldrankpos", event -> {
            Rank actualRank = RankCacheManager.getRankedPlayer(event.getPlayer()).getRank();
            try {
                Rank rank = RankUtils.getOldRank(actualRank);
                return String.valueOf(rank.getPosition());
            } catch (MinRankException e) {
                return Language.getSingleLineMessage(SingleLineMessage.NO_OLD_RANK);
            }
        });
        PlaceholderAPI.registerPlaceholder(plugin, "uranking_oldrankprice", event -> {
            Rank actualRank = RankCacheManager.getRankedPlayer(event.getPlayer()).getRank();
            try {
                Rank rank = RankUtils.getOldRank(actualRank);
                return String.valueOf(rank.getPrice());
            } catch (MinRankException e) {
                return "0.00";
            }
        });

    }
}
