package ga.brunnofdc.uranking.routines.progress;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

import static ga.brunnofdc.uranking.config.ProgressBarConfig.*;
import static java.math.BigDecimal.valueOf;

public class GetPercentageAsProgressBarRoutine implements Function<Double, String> {

    @Override
    public String apply(final Double percentage) {

        BigDecimal totalChars = BigDecimal.valueOf(getCharacterAmount());
        int actualCharPosition = totalChars.multiply(valueOf(percentage))
                .setScale(0 , RoundingMode.HALF_UP)
                .intValue();

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < totalChars.intValue(); i++) {
            char color;

            if(i > actualCharPosition) {
                color = getRemaningColor();
            } else if(i < actualCharPosition) {
                color = getFilledColor();
            } else {
                color = getActualCharColor();
            }

            builder.append(colored(color, getCharacter()));
        }


        return builder.toString();
    }


    private static String colored(char color, char progressBarCharacter) {
        return "§" + color + progressBarCharacter;
    }

}
