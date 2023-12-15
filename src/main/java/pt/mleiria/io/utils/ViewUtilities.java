package pt.mleiria.io.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ViewUtilities {

    private static final Logger LOG = Logger.getLogger(ViewUtilities.class.getName());

    private ViewUtilities(){
    }

    public static DoubleConsumer printWith2Decimals = x -> LOG.info(String.format("%.2f", x));

    public static Function<Double, String> formatWith4Decimals = x -> String.format("%.2e", x);

    public static <T> void forEach(Collection<T> ts, Consumer<T> effect){
        for(final T t : ts) effect.accept(t);
    }

    public static void forEach(Collection<Double> ts){
        for(final Double t : ts) printWith2Decimals.accept(t);
    }
    public static void print(final double[][] matrix){
        final String content =
        Arrays.stream(matrix)
                .map(Arrays::toString)
                .collect(Collectors.joining("\n"));
        LOG.info("\n[\n" + content + "\n]");
    }


}
