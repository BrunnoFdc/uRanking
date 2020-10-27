package ga.brunnofdc.uranking.routines.progress;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

import static ga.brunnofdc.uranking.config.ProgressBarConfig.*;
import static java.math.BigDecimal.valueOf;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class GetPercentageAsProgressBarRoutine implements Function<Double, String> {

    @Override
    public String apply(final Double percentage) {

        BigDecimal totalChars = BigDecimal.valueOf(getCharacterAmount());
        BigDecimal actualCharPosition = totalChars.multiply(valueOf(percentage)).setScale(0 , RoundingMode.HALF_UP);

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < totalChars.intValue(); i++) {
            char color;

            if(i > actualCharPosition.intValue()) {
                color = getRemaningColor();
            } else if(i < actualCharPosition.intValue()) {
                color = getFilledColor();
            } else {
                color = getActualCharColor();
            }

            builder.append(colored(color, getCharacter()));
        }


        return builder.toString();
    }


    private static String colored(char color, char progressBarCharacter) {
        return translateAlternateColorCodes(color, String.valueOf(progressBarCharacter));
    }

}
