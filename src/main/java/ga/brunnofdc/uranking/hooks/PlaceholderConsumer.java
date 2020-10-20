package ga.brunnofdc.uranking.hooks;

import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.ranking.RankedPlayer;
import ga.brunnofdc.uranking.utils.Language;
import ga.brunnofdc.uranking.exceptions.MaxRankException;
import ga.brunnofdc.uranking.exceptions.MinRankException;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ga.brunnofdc.uranking.utils.Language.getSingleLineMessage;
import static ga.brunnofdc.uranking.utils.enums.SingleLineMessage.*;
import static java.lang.String.valueOf;

@FunctionalInterface
public interface PlaceholderConsumer {

    String getValue(RankedPlayer player);

    Map<String, PlaceholderConsumer> PLACEHOLDERS = Stream.of(new Object[][] {
            { "rankname", (PlaceholderConsumer) player -> {
                Rank rank = player.getRank();
                return rank.getName();
            }},
            { "rankid", (PlaceholderConsumer) player -> {
                Rank rank = player.getRank();
                return rank.getID();
            }},
            { "rankprefix", (PlaceholderConsumer) player -> {
                Rank rank = player.getRank();
                return rank.getPrefix();
            }},
            { "rankpos", (PlaceholderConsumer) player -> {
                Rank rank = player.getRank();
                return valueOf(rank.getPosition());
            }},
            { "rankprice", (PlaceholderConsumer) player -> {
                Rank rank = player.getRank();
                return valueOf(rank.getPrice());
            }},
            { "nextrankname", (PlaceholderConsumer) player -> {
                try {
                    Rank rank = RankUtils.getNextRank(player.getRank());
                    return rank.getName();
                } catch (MaxRankException e) {
                    return getSingleLineMessage(NO_NEXT_RANK);
                }
            }},
            { "nextrankid", (PlaceholderConsumer) player -> {
                try {
                    Rank rank = RankUtils.getNextRank(player.getRank());
                    return rank.getID();
                } catch (MaxRankException e) {
                    return getSingleLineMessage(NO_NEXT_RANK);
                }
            }},
            { "nextrankprefix", (PlaceholderConsumer) player -> {
                try {
                    Rank rank = RankUtils.getNextRank(player.getRank());
                    return valueOf(rank.getPrefix());
                } catch (MaxRankException e) {
                    return getSingleLineMessage(NO_NEXT_RANK);
                }
            }},
            { "nextrankpos", (PlaceholderConsumer) player -> {
                try {
                    Rank rank = RankUtils.getNextRank(player.getRank());
                    return valueOf(rank.getPosition());
                } catch (MaxRankException e) {
                    return getSingleLineMessage(NO_NEXT_RANK);
                }
            }},
            { "nextrankprice", (PlaceholderConsumer) player -> {
                try {
                    Rank rank = RankUtils.getNextRank(player.getRank());
                    return valueOf(rank.getPrice());
                } catch (MaxRankException e) {
                    return getSingleLineMessage(NO_NEXT_RANK);
                }
            }},
            { "oldrankname", (PlaceholderConsumer) player -> {
                try {
                    Rank rank = RankUtils.getOldRank(player.getRank());
                    return rank.getName();
                } catch (MinRankException e) {
                    return getSingleLineMessage(NO_OLD_RANK);
                }
            }},
            { "oldrankid", (PlaceholderConsumer) player -> {
                try {
                    Rank rank = RankUtils.getOldRank(player.getRank());
                    return rank.getID();
                } catch (MinRankException e) {
                    return getSingleLineMessage(NO_OLD_RANK);
                }
            }},
            { "oldrankprefix", (PlaceholderConsumer) player -> {
                try {
                    Rank rank = RankUtils.getOldRank(player.getRank());
                    return rank.getPrefix();
                } catch (MinRankException e) {
                    return getSingleLineMessage(NO_OLD_RANK);
                }
            }},
            { "oldrankpos", (PlaceholderConsumer) player -> {
                try {
                    Rank rank = RankUtils.getOldRank(player.getRank());
                    return valueOf(rank.getPosition());
                } catch (MinRankException e) {
                    return getSingleLineMessage(NO_OLD_RANK);
                }
            }},
            { "oldrankprice", (PlaceholderConsumer) player -> {
                try {
                    Rank rank = RankUtils.getOldRank(player.getRank());
                    return valueOf(rank.getPrice());
                } catch (MinRankException e) {
                    return getSingleLineMessage(NO_OLD_RANK);
                }
            }},
            { "progress", (PlaceholderConsumer) player -> {
                try {
                    Rank nextRank = RankUtils.getNextRank(player.getRank());
                    return "";//TODO: Use routines logic
                } catch (MaxRankException e) {
                    return getSingleLineMessage(NO_NEXT_RANK);
                }
            }},
            { "progressbar", (PlaceholderConsumer) player -> {
                try {
                    Rank nextRank = RankUtils.getNextRank(player.getRank());
                    return "";//TODO: Use routines logic
                } catch (MaxRankException e) {
                    return getSingleLineMessage(NO_NEXT_RANK);
                }
            }},

    }).collect(Collectors.toMap(data -> (String) data[0], data -> (PlaceholderConsumer) data[1]));

}
