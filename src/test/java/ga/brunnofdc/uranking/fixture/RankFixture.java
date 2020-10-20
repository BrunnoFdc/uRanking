package ga.brunnofdc.uranking.fixture;

import ga.brunnofdc.uranking.ranking.Rank;

import java.lang.reflect.Field;
import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.math.RandomUtils.nextDouble;
import static org.apache.commons.lang.math.RandomUtils.nextInt;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class RankFixture {

    private Rank instance;

    public static RankFixture init() {
        return new RankFixture();
    }

    public RankFixture price(final Double price) {
        try {
            Field priceField = Rank.class.getDeclaredField("price");
            priceField.setAccessible(true);
            priceField.set(this.instance, price);
        } catch (Exception ignored) {}
        return this;
    }

    public RankFixture random() {
        this.instance = new Rank(
                randomAlphabetic(6),
                randomAlphabetic(16),
                randomAlphabetic(18),
                nextInt(),
                nextDouble(),
                asList(randomAlphabetic(18), randomAlphabetic(18)));
        return this;
    }

    public Rank get() {
        return this.instance;
    }

}
