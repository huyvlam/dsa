package myhash.probing;

import myhash.probing.mono.DoubleMap;
import myhash.probing.mono.LinearMap;
import myhash.probing.mono.QuadraticMap;
import myhash.probing.poly.PolyFlatMap;
import org.junit.jupiter.api.Disabled;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Disabled
public class FlatMapBenchmark {
    @Param({"65536", "131072", "262144"})
    private int size;

    @Param({"0.5", "0.8", "0.95"})
    private double loadFactor;

    private LinearMap<Integer, Integer> mLinear;
    private QuadraticMap<Integer, Integer> mQuad;
    private DoubleMap<Integer, Integer> mDouble;
    private PolyFlatMap<Integer, Integer> pLinear;
    private PolyFlatMap<Integer, Integer> pQuad;
    private PolyFlatMap<Integer, Integer> pDouble;

    private Integer[] keys;

    @Setup
    public void setup() {
        mLinear = new LinearMap<>(size);
        mQuad = new QuadraticMap<>(size);
        mDouble = new DoubleMap<>(size);
        pLinear = new PolyFlatMap<>(PolyFlatMap.Probe.LINEAR, size);
        pQuad = new PolyFlatMap<>(PolyFlatMap.Probe.QUADRATIC, size);
        pDouble = new PolyFlatMap<>(PolyFlatMap.Probe.DOUBLE, size);

        int fillCount = (int) (size * loadFactor);
        keys = new Integer[fillCount];

        for (int i = 0; i < fillCount; i++) {
            keys[i] = i;
            mLinear.put(i, i);
            mDouble.put(i, i);
            mQuad.put(i, i);
            pLinear.put(i, i);
            pQuad.put(i, i);
            pDouble.put(i, i);
        }
    }

    @Benchmark
    public Integer testMonoLinearGet() {
        return mLinear.get(keys[keys.length / 2]);
    }

    @Benchmark
    public Integer testMonoQuadraticGet() {
        return mQuad.get(keys[keys.length / 2]);
    }

    @Benchmark
    public Integer testMonoDoubleGet() {
        return mDouble.get(keys[keys.length / 2]);
    }

    @Benchmark
    public Integer testPolyLinearGet() {
        return pLinear.get(keys[keys.length / 2]);
    }

    @Benchmark
    public Integer testPolyQuadraticGet() {
        return pQuad.get(keys[keys.length / 2]);
    }

    @Benchmark
    public Integer testPolyDoubleGet() {
        return pDouble.get(keys[keys.length / 2]);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FlatMapBenchmark.class.getSimpleName())
                .forks(1)            // Use 1 fork for faster local testing
                .warmupIterations(2) // Give the JIT time to optimize
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }
}
