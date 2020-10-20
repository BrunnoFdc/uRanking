package ga.brunnofdc.uranking.exceptions;

import ga.brunnofdc.uranking.ranking.Rank;

import static ga.brunnofdc.uranking.utils.Language.getSingleLineMessage;
import static ga.brunnofdc.uranking.utils.enums.SingleLineMessage.NO_OLD_RANK;

public class MinRankException extends Exception {

    private final Rank rank;

    public MinRankException(Rank minRank) {
        this.rank = minRank;
    }

    public Rank getRank() {
        return this.rank;
    }

    @Override
    public String getMessage() {
        return getSingleLineMessage(NO_OLD_RANK);
    }

}
