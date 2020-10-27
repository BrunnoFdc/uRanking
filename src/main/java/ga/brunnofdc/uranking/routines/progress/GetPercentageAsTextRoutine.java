package ga.brunnofdc.uranking.routines.progress;

import java.util.function.Function;

public class GetPercentageAsTextRoutine implements Function<Double, String> {

    @Override
    public String apply(final Double percentage) {
        Double friendlyPercentage = percentage * 100;
        return friendlyPercentage + "%";
    }

}
