package ga.brunnofdc.uranking.exceptions;

import ga.brunnofdc.uranking.ranking.Rank;

import static ga.brunnofdc.uranking.utils.Language.getSingleLineMessage;
import static ga.brunnofdc.uranking.utils.enums.SingleLineMessage.*;

public class MaxRankException extends Exception {

    private final Rank rank;

    public MaxRankException(Rank maxRank) {
        this.rank = maxRank;
    }

    public Rank getRank() {
        return this.rank;
    }

    @Override
    public String getMessage() {
        return getSingleLineMessage(NO_NEXT_RANK);
    }

}
