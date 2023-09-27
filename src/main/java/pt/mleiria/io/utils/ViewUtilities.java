package pt.mleiria.io.utils;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.logging.Logger;

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

}
