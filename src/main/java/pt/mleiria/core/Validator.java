package pt.mleiria.core;

import java.util.Objects;
import java.util.function.Predicate;

public class Validator {

    private static final Predicate<String> isNull = Objects::isNull;

    private static final Predicate<String> isEmpty = String::isEmpty;

    /**
     * Returns a Predicate that checks if a String is present.
     *
     * @return a Predicate that checks if a String is present
     */
    public static Predicate<String> isPresent() {
        return isNull.negate().and(isEmpty.negate());
    }

    /**
     * A function that returns a Predicate object.
     *
     * @return a Predicate object
     */
    public static Predicate<String> isNotPresent() {
        return isNull.or(isEmpty);
    }


}
