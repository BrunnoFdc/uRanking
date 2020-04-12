package ga.brunnofdc.uranking.utils;

import ga.brunnofdc.uranking.uRanking;
import ga.brunnofdc.uranking.utils.enums.ErrorType;

public class ErrorReporter {

    public static void sendReport(ErrorType type, Exception exception) {
        uRanking.getInstance().getLogger().severe(type.getMessage() + " Detalhes do erro:");
        exception.printStackTrace();
    }

}
