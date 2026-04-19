package myhash.offheap;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 1, jvmArgs = {
        "--add-opens", "java.base/java.nio=ALL-UNNAMED",
        "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED"
})
@State(Scope.Thread)
public class OffHeapBenchmark {
//    @Param({"131072", "262144", "524288"})
    @Param({"524288"})
    private int size;

    @Param({"0.6", "0.4", "0.75"})
    private double delFactor;

    @Param({"0.5", "0.2", "0.7"})
    private double reFactor;

    private TheBeast beast;
    private LinearOffHeapMap offheap;
    private ConcurrentHashMap<Long, Long> concurrent;

    private long[] keys;
    int keysSize, i;
    long start, end;

    @Setup
    public void setup() {
        beast = new TheBeast(size);
        offheap = new LinearOffHeapMap(size);
        concurrent = new ConcurrentHashMap<>(size);

        keysSize = (int) Math.floor(size * 0.9);
        keys = new long[keysSize];

        Random rand = new Random(256);
        for (i = 0; i < keysSize; i++) {
            keys[i] = rand.nextLong();
            beast.put(keys[i], keys[i]);
            offheap.put(keys[i], keys[i]);
            concurrent.put(keys[i], keys[i]);
        }
    }

    @Benchmark
    public Double beastGet() {
        start = System.nanoTime();
        for (i = (int) Math.floor(keysSize * 0.2); i < (int) Math.floor(keysSize * 0.7); i++) {
            beast.get(keys[i]);
        }
        end = System.nanoTime();
        return (end - start) / (double) keysSize;
    }

    @Benchmark
    public Double offheapGet() {
        start = System.nanoTime();
        for (i = (int) Math.floor(keysSize * 0.2); i < (int) Math.floor(keysSize * 0.7); i++) {
            offheap.get(keys[i]);
        }
        end = System.nanoTime();
        return (end - start) / (double) keysSize;
    }

    @Benchmark
    public Double concurrentGet() {
        start = System.nanoTime();
        for (i = (int) Math.floor(keysSize * 0.2); i < (int) Math.floor(keysSize * 0.7); i++) {
            concurrent.get(keys[i]);
        }
        end = System.nanoTime();
        return (end - start) / (double) keysSize;
    }

    @TearDown
    public void tearDown() {
        beast.close();
        concurrent.clear();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(OffHeapBenchmark.class.getSimpleName())
                .forks(1)            // Use 1 fork for faster local testing
                .warmupIterations(2) // Give the JIT time to optimize
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }
}
