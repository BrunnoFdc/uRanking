package ga.brunnofdc.uranking.hooks;

import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.ranking.RankedPlayer;
import ga.brunnofdc.uranking.routines.progress.ComputeProgressPercentageRoutine;
import ga.brunnofdc.uranking.routines.progress.GetPercentageAsProgressBarRoutine;
import ga.brunnofdc.uranking.routines.progress.GetPercentageAsTextRoutine;
import ga.brunnofdc.uranking.exceptions.MaxRankException;
import ga.brunnofdc.uranking.exceptions.MinRankException;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ga.brunnofdc.uranking.utils.Language.getSingleLineMessage;
import static ga.brunnofdc.uranking.utils.enums.SingleLineMessage.*;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

public interface PlaceholderMapper extends Function<RankedPlayer, String> {

    String apply(RankedPlayer player);

    Map<String, PlaceholderMapper> PLACEHOLDERS = Stream.of(new Object[][] {
            { "rankname", (PlaceholderMapper) player -> {
                Rank rank = player.getRank();
                return rank.getName();
            }},
            { "rankid", (PlaceholderMapper) player -> {
                Rank rank = player.getRank();
                return rank.getID();
            }},
            { "rankprefix", (PlaceholderMapper) player -> {
                Rank rank = player.getRank();
                return rank.getPrefix();
            }},
            { "rankpos", (PlaceholderMapper) player -> {
                Rank rank = player.getRank();
                return valueOf(rank.getPosition());
            }},
            { "rankprice", (PlaceholderMapper) player -> {
                Rank rank = player.getRank();
                return valueOf(rank.getPrice());
            }},
            { "nextrankname", (PlaceholderMapper) player -> {
                try {
                    Rank rank = RankUtils.getNextRank(player.getRank());
                    return rank.getName();
                } catch (MaxRankException e) {
                    return getSingleLineMessage(NO_NEXT_RANK);
                }
            }},
            { "nextrankid", (PlaceholderMapper) player -> {
                try {
                    Rank rank = RankUtils.getNextRank(player.getRank());
                    return rank.getID();
                } catch (MaxRankException e) {
                    return getSingleLineMessage(NO_NEXT_RANK);
                }
            }},
            { "nextrankprefix", (PlaceholderMapper) player -> {
                try {
                    Rank rank = RankUtils.getNextRank(player.getRank());
                    return valueOf(rank.getPrefix());
                } catch (MaxRankException e) {
                    return getSingleLineMessage(NO_NEXT_RANK);
                }
            }},
            { "nextrankpos", (PlaceholderMapper) player -> {
                try {
                    Rank rank = RankUtils.getNextRank(player.getRank());
                    return valueOf(rank.getPosition());
                } catch (MaxRankException e) {
                    return getSingleLineMessage(NO_NEXT_RANK);
                }
            }},
            { "nextrankprice", (PlaceholderMapper) player -> {
                try {
                    Rank rank = RankUtils.getNextRank(player.getRank());
                    return valueOf(rank.getPrice());
                } catch (MaxRankException e) {
                    return getSingleLineMessage(NO_NEXT_RANK);
                }
            }},
            { "oldrankname", (PlaceholderMapper) player -> {
                try {
                    Rank rank = RankUtils.getOldRank(player.getRank());
                    return rank.getName();
                } catch (MinRankException e) {
                    return getSingleLineMessage(NO_OLD_RANK);
                }
            }},
            { "oldrankid", (PlaceholderMapper) player -> {
                try {
                    Rank rank = RankUtils.getOldRank(player.getRank());
                    return rank.getID();
                } catch (MinRankException e) {
                    return getSingleLineMessage(NO_OLD_RANK);
                }
            }},
            { "oldrankprefix", (PlaceholderMapper) player -> {
                try {
                    Rank rank = RankUtils.getOldRank(player.getRank());
                    return rank.getPrefix();
                } catch (MinRankException e) {
                    return getSingleLineMessage(NO_OLD_RANK);
                }
            }},
            { "oldrankpos", (PlaceholderMapper) player -> {
                try {
                    Rank rank = RankUtils.getOldRank(player.getRank());
                    return valueOf(rank.getPosition());
                } catch (MinRankException e) {
                    return getSingleLineMessage(NO_OLD_RANK);
                }
            }},
            { "oldrankprice", (PlaceholderMapper) player -> {
                try {
                    Rank rank = RankUtils.getOldRank(player.getRank());
                    return valueOf(rank.getPrice());
                } catch (MinRankException e) {
                    return getSingleLineMessage(NO_OLD_RANK);
                }
            }},
            { "progress", (PlaceholderMapper) player ->
                    ofNullable(new ComputeProgressPercentageRoutine().compute(player))
                        .map(new GetPercentageAsTextRoutine())
                        .orElse(getSingleLineMessage(NO_NEXT_RANK))
            },
            { "progressbar", (PlaceholderMapper) player -> ofNullable(new ComputeProgressPercentageRoutine().compute(player))
                        .map(new GetPercentageAsProgressBarRoutine())
                        .orElse(getSingleLineMessage(NO_NEXT_RANK))
            },

    }).collect(Collectors.toMap(data -> (String) data[0], data -> (PlaceholderMapper) data[1]));

}
