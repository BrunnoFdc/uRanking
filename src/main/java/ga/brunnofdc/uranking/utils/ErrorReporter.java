package ga.brunnofdc.uranking.utils;

import ga.brunnofdc.uranking.uRanking;
import ga.brunnofdc.uranking.utils.enums.ErrorType;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ErrorReporter {

    public static void sendReport(ErrorType type, Exception exception) {
        Logger logger = uRanking.getInstance().getLogger();
        logger.severe(type.getMessage() + " Detalhes do erro:");
        logger.log(Level.SEVERE, "", exception.getCause());
    }

}
