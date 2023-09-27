package pt.mleiria.collections.immutable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static pt.mleiria.collections.immutable.TailCall.ret;
import static pt.mleiria.collections.immutable.TailCall.sus;

public class CollectionUtilities {

    private CollectionUtilities() {
    }

    public static final Function<double[], List<Double>> dblArrToListFunc =
            vector -> DoubleStream.of(vector).boxed().toList();

    /**
     * @param <T>
     * @return
     */
    public static <T> List<T> list() {
        return Collections.emptyList();
    }

    /**
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> list(final T t) {
        return Collections.singletonList(t);
    }

    /**
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> List<T> list(final List<T> ts) {
        return Collections.unmodifiableList(new ArrayList<>(ts));
    }

    /**
     * @param t
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> List<T> list(final T... t) {
        return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(t, t.length)));
    }

    /**
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T head(final List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalStateException("Head of Empty list");
        }
        return list.get(0);
    }

    private static <T> List<T> copy(final List<T> ts) {
        return new ArrayList<>(ts);
    }

    /**
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> tail(final List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalStateException("Tail of Empty list");
        }
        final List<T> workList = copy(list);
        workList.remove(0);
        return Collections.unmodifiableList(workList);
    }

    /**
     * @param ts
     * @param identity
     * @param f
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> U foldLeft(final List<T> ts, final U identity, final Function<U, Function<T, U>> f) {
        return foldLeftTailRec(ts, identity, f).eval();
    }

    private static <T, U> TailCall<U> foldLeftTailRec(final List<T> ts, final U identity, final Function<U, Function<T, U>> f) {
        return
                ts.isEmpty()
                        ? ret(identity)
                        : sus(() -> foldLeftTailRec(tail(ts), f.apply(identity).apply(head(ts)), f));
    }

    /**
     * When considering using foldRight, you should do one of the following:
     * - Not care about performance
     * - Change the function (if possible) and use foldLeft
     * - Use foldRight only with small lists
     * - Use an imperative implementation
     * @param ts
     * @param identity
     * @param f
     * @return
     * @param <T>
     * @param <U>
     */
    public static <T, U> U foldRight(final List<T> ts, final U identity, final Function<T, Function<U, U>> f) {
        return foldRightTailRec(identity, reverse(ts), f).eval();
    }

    private static <T, U> TailCall<U> foldRightTailRec(final U acc, final List<T> ts, final Function<T, Function<U, U>> f) {
        return
                ts.isEmpty()
                        ? ret(acc)
                        : sus(() -> foldRightTailRec(f.apply(head(ts)).apply(acc), tail(ts), f));
    }


    /**
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> reverse(final List<T> list) {
        return foldLeft(list, list(), x -> y -> prepend(y, x));
    }

    /**
     * @param list
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> append(final List<T> list, final T t) {
        final List<T> ts = copy(list);
        ts.add(t);
        return Collections.unmodifiableList(ts);
    }

    /**
     * @param t
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> prepend(final T t, final List<T> list) {
        return foldLeft(list, list(t), x -> y -> append(x, y));
    }

    /**
     * @param start
     * @param end
     * @return
     */
    public static List<Integer> range(final int start, final int end) {
        return unfold(start, x -> x + 1, x -> x < end);
    }

    /**
     * @param seed
     * @param f
     * @param p
     * @param <T>
     * @return
     */
    public static <T> List<T> unfold(T seed, Function<T, T> f, Function<T, Boolean> p) {
        return unfoldTailRec(list(), seed, f, p).eval();
    }

    private static <T> TailCall<List<T>> unfoldTailRec(List<T> acc, T seed, Function<T, T> f, Function<T, Boolean> p) {
        return
                p.apply(seed)
                        ? sus(() -> unfoldTailRec(append(acc, seed), f.apply(seed), f, p))
                        : ret(acc);
    }
}
