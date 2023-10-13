package pt.mleiria.core;

public final class StopWatch {

    private final long start;

    public StopWatch() {
        start = System.nanoTime();
    }

    /**
     * Calculates the elapsed time in milliseconds since the start of the function.
     *
     * @return the elapsed time in milliseconds
     */
    public String elapsedTime() {
        final long duration = (System.nanoTime() - start) / 1_000_000;
        return " {Running Time: " + duration + " msecs";
    }
}
