package pt.mleiria.io.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static pt.mleiria.io.utils.FileUtilities.strArrToDbl;
import static pt.mleiria.io.utils.FileUtilities.strToStrArr;

class FileUtilitiesTest {

    private final ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void load() {
        try {
            final List<double[]> data = FileUtilities.load(
                    Objects.requireNonNull(classLoader.getResource("house_prices.txt")).getPath(),
                    strToStrArr.andThen(strArrToDbl));
            assertFalse(data.isEmpty());
        } catch (Exception e) {
            fail(e);
        }
    }
}