package pt.mleiria.core;

public final class StopWatch {

    private final long start;

    public StopWatch(){
        start = System.nanoTime();
    }

    public String elapsedTime(){
        final long duration = (System.nanoTime() - start) / 1_000_000;
        return " {Running Time: " + duration + " msecs";
    }
}
