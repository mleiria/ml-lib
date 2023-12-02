package pt.mleiria.collections.immutable;

import java.util.function.Supplier;

public abstract class TailCall<T> {

    /**
     * Returns a TailCall object that represents the resumption of the function.
     *
     * @return a TailCall object representing the resumption of the function
     */
    public abstract TailCall<T> resume();

    /**
     * Evaluates the function and returns the result.
     *
     * @return the result of the evaluation
     */
    public abstract T eval();

    /**
     * Determines if the function is a suspend function.
     *
     * @return true if the function is a suspend function, otherwise false
     */
    public abstract boolean isSuspend();

    private TailCall() {
    }

    /**
     * Represents the last call, which is supposed to return
     * the result
     *
     * @param <T>
     */
    public static class Return<T> extends TailCall<T> {
        private final T t;

        public Return(T t) {
            this.t = t;
        }

        /**
         * A description of the entire Java function.
         *
         * @return description of return value
         */
        @Override
        public T eval() {
            return t;
        }

        /**
         * Determines if the function is suspended.
         *
         * @return false indicating that the function is not suspended
         */
        @Override
        public boolean isSuspend() {
            return false;
        }

        /**
         * A description of the entire Java function.
         *
         * @return description of return value
         */
        @Override
        public TailCall<T> resume() {
            throw new IllegalStateException("Return has no resume");
        }
    }

    /**
     * Represents an intermediate call, when the processing of one step is
     * suspended to call the method again for evaluating the next step.
     *
     * @param <T>
     */
    public static class Suspend<T> extends TailCall<T> {
        private final Supplier<TailCall<T>> resume;

        public Suspend(Supplier<TailCall<T>> resume) {
            this.resume = resume;
        }

        /**
         * Evaluates the function and returns the result.
         *
         * @return the result of the evaluation
         */
        @Override
        public T eval() {
            TailCall<T> tailRec = this;
            while (tailRec.isSuspend()) {
                tailRec = tailRec.resume();
            }
            return tailRec.eval();
        }

        /**
         * Returns whether the function is suspended or not.
         *
         * @return true if the function is suspended, false otherwise
         */
        @Override
        public boolean isSuspend() {
            return true;
        }

        /**
         * Resumes the execution of the Java function.
         *
         * @return the result of resuming the function execution
         */
        @Override
        public TailCall<T> resume() {
            return resume.get();
        }
    }

    /**
     * Creates a new Return object with the given value.
     *
     * @param t the value to be wrapped in the Return object
     * @return a new Return object containing the given value
     */
    public static <T> Return<T> ret(T t) {
        return new Return<>(t);
    }

    /**
     * Generates a Suspend object given a Supplier of TailCall objects.
     *
     * @param s a Supplier of TailCall objects
     * @return a new Suspend object
     */
    public static <T> Suspend<T> sus(Supplier<TailCall<T>> s) {
        return new Suspend<>(s);
    }

}
