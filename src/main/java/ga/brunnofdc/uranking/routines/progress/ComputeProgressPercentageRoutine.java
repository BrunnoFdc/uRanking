package ga.brunnofdc.uranking.routines.progress;

import ga.brunnofdc.uranking.economy.EconomicUnit;
import ga.brunnofdc.uranking.exceptions.MaxRankException;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.ranking.RankedPlayer;
import ga.brunnofdc.uranking.uRanking;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.min;
import static java.math.BigDecimal.valueOf;

public class ComputeProgressPercentageRoutine {

    public Integer compute(RankedPlayer rankedPlayer) {

        try {
            EconomicUnit unit = uRanking.getInstance().getEconomicUnit();
            BigDecimal playerBalance = valueOf(unit.getBalance(rankedPlayer.getPlayer()));
            BigDecimal nextRankPrice = valueOf(RankUtils.getNextRank(rankedPlayer.getRank()).getPrice());

            BigDecimal brutePercentage = playerBalance.divide(nextRankPrice, 2, RoundingMode.HALF_UP);
            BigDecimal friendlyPercentage = brutePercentage.multiply(valueOf(100));

            return min(friendlyPercentage.intValue(), 100);
        } catch (MaxRankException e) {
            return null;
        }

    }

}
