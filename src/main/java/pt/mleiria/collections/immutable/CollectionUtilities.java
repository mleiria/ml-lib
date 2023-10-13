package pt.mleiria.collections.immutable;

import pt.mleiria.dto.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static pt.mleiria.collections.immutable.TailCall.ret;
import static pt.mleiria.collections.immutable.TailCall.sus;

public class CollectionUtilities {

    private CollectionUtilities() {
    }

    public static final Function<double[], List<Double>> dblArrToListFunc =
            vector -> DoubleStream.of(vector).boxed().toList();

    /**
     * Creates and returns an empty list.
     *
     * @param <T> the type of elements in the list
     * @return an empty list
     */
    public static <T> List<T> list() {
        return Collections.emptyList();
    }

    /**
     * Returns a list containing a single element.
     *
     * @param t   the element to be added to the list
     * @param <T> the type of the element
     * @return a list containing only the specified element
     */
    public static <T> List<T> list(final T t) {
        return Collections.singletonList(t);
    }

    /**
     * Returns an unmodifiable list containing the elements of the input list.
     *
     * @param ts  the list of elements
     * @param <T> the type of elements in the list
     * @return an unmodifiable list containing the elements of the input list
     */
    public static <T> List<T> list(final List<T> ts) {
        return Collections.unmodifiableList(new ArrayList<>(ts));
    }

    /**
     * A function that creates an unmodifiable list from the given array of elements.
     *
     * @param t an array of elements
     * @return an unmodifiable list containing the elements from the input array
     */
    @SafeVarargs
    public static <T> List<T> list(final T... t) {
        return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(t, t.length)));
    }

    /**
     * Returns the first element of the given list.
     *
     * @param list the list from which to retrieve the first element
     * @return the first element of the list
     */
    public static <T> T head(final List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalStateException("Head of Empty list");
        }
        return list.get(0);
    }

    /**
     * Copies a list of elements.
     *
     * @param ts  the list of elements to be copied
     * @param <T> the type of elements in the list
     * @return a new list containing the same elements as the input list
     */
    private static <T> List<T> copy(final List<T> ts) {
        return new ArrayList<>(ts);
    }

    /**
     * Returns a new list containing all elements of the input list except for the first element.
     *
     * @param list the input list
     * @param <T>  the type of elements in the list
     * @return a new list with all elements except the first element of the input list
     * @throws IllegalStateException if the input list is empty
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
     * A function that performs a left fold operation on a list.
     * <p>
     * This code snippet defines a function called foldLeft that performs a left fold operation on a list.
     * The function takes in four parameters: ts, which is the list to be folded, identity, which is the
     * initial value for the fold operation, f, which is the function that combines the current accumulated
     * value with the next element of the list, and <T> and <U>, which are the types of the elements in the
     * list and the accumulated value, respectively.
     * <p>
     * The function uses tail recursion to implement the fold operation and returns the result of the fold operation.
     *
     * @param ts       the list to be folded
     * @param identity the initial value for the fold operation
     * @param f        the function that combines the current accumulated value with the next element of the list
     * @param <T>      the type of the elements in the list
     * @param <U>      the type of the accumulated value
     * @return the result of the fold operation
     */
    public static <T, U> U foldLeft(final List<T> ts, final U identity, final Function<U, Function<T, U>> f) {
        return foldLeftTailRec(ts, identity, f).eval();
    }

    /**
     * Generate the function comment for the given function body in a markdown code block with the correct language syntax.
     *
     * @param ts       the list of values to be folded
     * @param identity the initial value for folding
     * @param f        the function for folding
     * @return the result of folding the list of values
     * <p>
     * This code snippet is a method called foldLeftTailRec that performs a tail-recursive
     * fold operation on a list of values.
     * <p>
     * It takes three parameters: ts (the list of values to be folded), identity (the initial value for folding),
     * and f (the function for folding).
     * <p>
     * The method uses a ternary operator to check if the list of values is empty.
     * If it is, it returns the initial value. Otherwise, it uses a suspended computation to recursively
     * call the foldLeftTailRec method with the tail of the list, the result of applying the
     * folding function to the initial value and the head of the list, and the folding function itself.
     * <p>
     * The method returns a TailCall object, which is a way to perform tail recursion in Java.
     */
    private static <T, U> TailCall<U> foldLeftTailRec(final List<T> ts, final U identity, final Function<U, Function<T, U>> f) {
        return
                ts.isEmpty()
                        ? ret(identity)
                        : sus(() -> foldLeftTailRec(tail(ts), f.apply(identity).apply(head(ts)), f));
    }

    /**
     * A function that folds the elements of a list from right to left using a binary operator function.
     * This code snippet is a Java function called foldRight that performs a right-to-left fold operation on a list.
     * It takes four parameters: the list to be folded (ts), the initial value of the fold operation (identity),
     * the binary operator function to apply to the elements of the list (f), and the types of the elements in
     * the list (T) and the result (U).
     * <p>
     * The function uses tail recursion to perform the fold operation efficiently. It first reverses the
     * list using the reverse function, and then calls the foldRightTailRec function with the reversed list,
     * the initial value, and the binary operator function. The result of the fold operation is obtained by
     * calling the eval method on the returned value of foldRightTailRec.
     * <p>
     * In summary, this code snippet provides a generic implementation of a right-to-left fold operation on a list.
     *
     * @param ts       the list to be folded
     * @param identity the initial value of the fold operation
     * @param f        the binary operator function to apply to the elements of the list
     * @param <T>      the type of the elements in the list
     * @param <U>      the type of the result
     * @return the result of folding the elements of the list from right to left using the binary operator function
     */
    public static <T, U> U foldRight(final List<T> ts, final U identity, final Function<T, Function<U, U>> f) {
        return foldRightTailRec(identity, reverse(ts), f).eval();
    }

    /**
     * A recursive tail-call optimized function that performs a right fold operation on a list.
     * This code snippet defines a recursive function called foldRightTailRec that performs a right fold
     * operation on a list. The function takes an initial accumulator value, a list of elements to fold,
     * and a function to apply to each element. It returns the final result of folding the list from right to left.
     * The function is tail-call optimized, meaning it uses tail recursion to optimize memory usage.
     * The function is generic and can work with any type of elements in the list and any type of
     * accumulator and return value.
     *
     * @param acc the initial accumulator value
     * @param ts  the list of elements to fold
     * @param f   the function to apply to each element in the list
     * @param <T> the type of elements in the list
     * @param <U> the type of the accumulator and return value
     * @return the final result of folding the list from right to left
     */
    private static <T, U> TailCall<U> foldRightTailRec(final U acc, final List<T> ts, final Function<T, Function<U, U>> f) {
        return
                ts.isEmpty()
                        ? ret(acc)
                        : sus(() -> foldRightTailRec(f.apply(head(ts)).apply(acc), tail(ts), f));
    }


    /**
     * Reverses the given list.
     * This code defines a method called reverse that takes a list as input and returns a
     * reversed version of that list. It uses a functional programming approach by utilizing a
     * foldLeft function to iterate over the elements of the list and construct a new list in reverse order.
     * The prepend function is used to add each element to the front of the new list. The method is generic,
     * meaning it can work with lists of any type.
     *
     * @param list the list to be reversed
     * @return the reversed list
     */
    public static <T> List<T> reverse(final List<T> list) {
        return foldLeft(list, list(), x -> y -> prepend(y, x));
    }

    /**
     * Appends an element to a list and returns a new list with the element added.
     * This code snippet defines a method called append that takes a list and an element as input.
     * It creates a copy of the list, adds the element to the copy, and returns the new list with the element added.
     * The returned list is unmodifiable, meaning that its contents cannot be changed.
     *
     * @param list the list to append to
     * @param t    the element to append
     * @return a new list with the element added
     */
    public static <T> List<T> append(final List<T> list, final T t) {
        final List<T> ts = copy(list);
        ts.add(t);
        return Collections.unmodifiableList(ts);
    }

    /**
     * Prepend an element to a list.
     * This code snippet defines a method called prepend that takes an element t and a
     * list as parameters. It returns a new list with the element t prepended to the original list.
     * The method uses a foldLeft function to iterate over the original list and append each element to a
     * new list, starting with the element t. The method is generic, meaning it can work with any type of elements.
     *
     * @param t    the element to prepend
     * @param list the list to prepend to
     * @return the new list with the element prepended
     */
    public static <T> List<T> prepend(final T t, final List<T> list) {
        return foldLeft(list, list(t), x -> y -> append(x, y));
    }

    /**
     * Generates a list of integers in a given range.
     *
     * @param start the starting value of the range
     * @param end   the ending value of the range
     * @return a list of integers in the range [start, end)
     */
    public static List<Integer> range(final int start, final int end) {
        return unfold(start, x -> x + 1, x -> x < end);
    }


    public static <T> List<T> unfold(T seed, Function<T, T> f, Function<T, Boolean> p) {
        return unfoldTailRec(list(), seed, f, p).eval();
    }

    /**
     * Generates a tail-recursive list using the given seed, function, and predicate.
     *
     * @param acc  the accumulated list
     * @param seed the seed value
     * @param f    the function to generate the next value
     * @param p    the predicate to determine when to stop generating values
     * @param <T>  the type of elements in the list
     * @return the generated list
     */
    private static <T> TailCall<List<T>> unfoldTailRec(List<T> acc, T seed, Function<T, T> f, Function<T, Boolean> p) {
        return
                p.apply(seed)
                        ? sus(() -> unfoldTailRec(append(acc, seed), f.apply(seed), f, p))
                        : ret(acc);
    }

    /**
     * Generates a list of pairs by combining the elements of two lists.
     * This code snippet defines a zip method that generates a list of pairs by combining the elements of
     * two input lists. The method takes in two lists, ts and us, and returns a list of pairs where each
     * pair contains an element from the first list and an element from the second list. The zip method
     * uses the IntStream.range method to iterate over the indices of the two lists and creates pairs by retrieving
     * the corresponding elements at each index using the get method. Finally, the method converts the stream of
     * pairs to a list using the toList method.
     *
     * @param ts the first list of elements
     * @param us the second list of elements
     * @return a list of pairs, where each pair contains an element from the first list and an element from the second list
     */
    public static <T, U> List<Pair<T, U>> zip(final List<T> ts, final List<U> us) {
        final int tSize = ts.size();
        final int uSize = us.size();
        return
                IntStream.range(0, Math.min(tSize, uSize))
                        .mapToObj(i -> new Pair<>(ts.get(i), us.get(i)))
                        .toList();
    }

    /**
     * Generates a list of pairs, where each pair consists of an element from the input list
     * and its corresponding index.
     * This code snippet defines a zipWithIndex method that generates a list of pairs, where each pair
     * consists of an element from the input list and its corresponding index. The method takes in a list of elements
     * ts and returns a list of pairs where each pair consists of an element from the input list and its corresponding index.
     *
     * @param ts  the input list of elements
     * @param <T> the type of elements in the list
     * @return a list of pairs, where each pair consists of an element from the input list
     * and its corresponding index
     */
    public static <T> List<Pair<T, Integer>> zipWithIndex(final List<T> ts) {
        return
                IntStream.range(0, ts.size())
                        .mapToObj(i -> new Pair<>(ts.get(i), i))
                        .toList();
    }
}
