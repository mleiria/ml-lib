package pt.mleiria.collections.immutable;

import java.util.function.Function;

import static pt.mleiria.collections.immutable.TailCall.ret;
import static pt.mleiria.collections.immutable.TailCall.sus;

public abstract class List<A> {

    public abstract A head();

    public abstract List<A> tail();

    public abstract List<A> drop(int n);

    public abstract List<A> dropWhile(Function<A, Boolean> f);

    public abstract List<A> reverse();

    public abstract List<A> init();

    public abstract int length();

    public abstract <B> B foldLeft(B identity, Function<B, Function<A, B>> f);

    public abstract boolean isEmpty();

    public abstract List<A> setHead(A head);

    @SuppressWarnings("rawtypes")
    public static final List NIL = new Nil();

    private List() {
    }

    private static class Nil<A> extends List<A> {
        private Nil() {
        }

        @Override
        public A head() {
            throw new IllegalStateException("head called on empty list");
        }

        @Override
        public List<A> tail() {
            throw new IllegalStateException("tail called on empty list");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public List<A> setHead(A head) {
            throw new IllegalStateException("setHead called on empty list");
        }

        @Override
        public List<A> drop(int n) {
            return this;
        }

        @Override
        public List<A> dropWhile(Function<A, Boolean> f) {
            return this;
        }

        @Override
        public List<A> reverse() {
            return this;
        }

        @Override
        public List<A> init() {
            throw new IllegalStateException("init called on an empty list");
        }

        public <B> B foldLeft(B identity, Function<B, Function<A, B>> f) {
            return identity;
        }

        @Override
        public int length() {
            return 0;
        }

        @Override
        public String toString() {
            return "[NIL]";
        }
    }

    private static class Cons<A> extends List<A> {
        private final A head;
        private final List<A> tail;

        private Cons(A head, List<A> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public A head() {
            return head;
        }

        @Override
        public List<A> tail() {
            return tail;
        }

        @Override
        public List<A> drop(int n) {
            return n <= 0
                    ? this
                    : drop(this, n).eval();
        }

        @Override
        public List<A> dropWhile(Function<A, Boolean> f) {
            return dropWhile(this, f).eval();
        }

        private TailCall<List<A>> dropWhile(List<A> list, Function<A, Boolean> f) {
            return !list.isEmpty() && f.apply(list.head())
                    ? sus(() -> dropWhile(list.tail(), f))
                    : ret(list);
        }

        public <B> B foldLeft(B identity, Function<B, Function<A, B>> f) {
            return foldLeft(this, identity, f).eval();
        }


        private <B> TailCall<B> foldLeft(List<A> list, B acc, Function<B, Function<A, B>> f) {
            return list.isEmpty()
                    ? ret(acc)
                    : sus(() -> foldLeft(list.tail(), f.apply(acc).apply(list.head()), f));
        }

        @Override
        public List<A> reverse() {
            return reverse(list(), this).eval();
        }

        private TailCall<List<A>> reverse(List<A> acc, List<A> list) {
            return list.isEmpty()
                    ? ret(acc)
                    : sus(() -> reverse(new Cons<>(list.head(), acc), list.tail()));
        }

        @Override
        public List<A> init() {
            return reverse().tail().reverse();
        }

        /**
         * Inefficient method
         *
         * @return
         */
        @Override
        public int length() {
            return foldLeft(0, x -> ignore -> x + 1);
        }

        private TailCall<List<A>> drop(List<A> list, int acc) {
            return acc == 0 || list.isEmpty()
                    ? ret(list)
                    : sus(() -> drop(list.tail(), acc - 1));
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public List<A> setHead(A head) {
            return new Cons<>(head, tail());
        }

        @Override
        public String toString() {
            return String.format("[%sNIL]", toString(new StringBuilder(), this).eval());
        }

        private TailCall<StringBuilder> toString(StringBuilder acc, List<A> list) {
            return list.isEmpty()
                    ? ret(acc)
                    : sus(() -> toString(acc.append(list.head()).append(", "), list.tail()));
        }
    }


    /**
     * A description of the entire Java function.
     *
     * @param <A> the type of elements in the list
     * @return a new empty list
     */
    @SuppressWarnings("unchecked")
    public static <A> List<A> list() {
        return NIL;
    }

    /**
     * Inefficient
     *
     * @param list
     * @param n
     * @param f
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> B foldRight(List<A> list, B n, Function<A, Function<B, B>> f) {
        return
                list.isEmpty()
                        ? n
                        : f.apply(list.head()).apply(foldRight(list.tail(), n, f));
    }




    /**
     * Creates a new list by adding the specified element at the beginning of the current list.
     *
     * @param elem the element to be added to the list
     * @return a new list with the specified element added at the beginning
     */
    public List<A> cons(A elem) {
        return new Cons<>(elem, this);
    }


    /**
     * Creates a list of elements.
     *
     * @param a the elements to be included in the list
     * @return a new list containing the given elements
     */
    @SafeVarargs
    public static <A> List<A> list(A... a) {
        List<A> n = list();
        for (int i = a.length - 1; i >= 0; i--) {
            n = new Cons<>(a[i], n);
        }
        return n;
    }

}
