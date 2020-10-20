package ga.brunnofdc.uranking.routines.progress;

import java.util.function.Function;

public class GetPercentageAsTextRoutine implements Function<Integer, String> {

    @Override
    public String apply(final Integer percentage) {
        return percentage.toString() + "%";
    }

}
