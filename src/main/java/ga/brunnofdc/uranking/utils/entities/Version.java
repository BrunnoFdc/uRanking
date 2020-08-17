package ga.brunnofdc.uranking.utils.entities;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Version implements Comparable<Version> {

    private String value;
    private int[] numbers;

    public Version(String value) {
        numbers = Arrays.stream(value.split("\\."))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(@NotNull Version anotherVersion) {
        final int maxLength = Math.max(numbers.length, anotherVersion.numbers.length);
        for (int i = 0; i < maxLength; i++) {
            final int actual = i < numbers.length ? numbers[i] : 0;
            final int another = i < anotherVersion.numbers.length ? anotherVersion.numbers[i] : 0;
            if (actual != another) {
                return actual < another ? -1 : 1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return value;
    }

}
