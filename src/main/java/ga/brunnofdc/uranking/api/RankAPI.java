package ga.brunnofdc.uranking.api;

import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankCacheManager;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.utils.exceptions.MaxRankException;
import ga.brunnofdc.uranking.utils.exceptions.MinRankException;
import org.bukkit.entity.Player;

public class RankAPI {

    private Rank rank;

    public RankAPI(Player player) {
        this.rank = RankCacheManager.getRankedPlayer(player).getRank();
    }

    private RankAPI(Rank rank) {
        this.rank = rank;
    }

    public int getPosition() {
        return rank.getPosition();
    }

    public String getDisplayname() {
        return rank.getName();

    }

    public String getID() {

        return rank.getID();

    }

    public String getTag() {

        return rank.getPrefix();

    }

    public double getPrice() {

        return rank.getPrice();

    }

    public RankAPI getNextRank() {
        try {
            return new RankAPI(RankUtils.getNextRank(this.rank));
        } catch (MaxRankException e) {
            return null;
        }
    }

    public RankAPI getOldRank() {
        try {
            return new RankAPI(RankUtils.getOldRank(this.rank));
        } catch (MinRankException e) {
            return null;
        }
    }

}
