package mytree;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class BinaryTreeBenchmark {
    @Param({"32768"})
    private int capacity;

    private ArrayBinaryTree abt;
    private LinkedBinaryTree lbt;
    private int[] data;

    @Setup(Level.Trial)
    public void setupData() {
        data = new int[capacity];
        Random rand = new Random();
        for (int i = 0; i < capacity; i++) {
            data[i] = rand.nextInt();
        }
    }

    @Setup(Level.Invocation)
    public void setupTrees() {
        // Start @ 1024 capacity to force grow
        abt = new ArrayBinaryTree(1024);
        lbt = new LinkedBinaryTree();
    }

    @Benchmark
    public int testArrayBinaryTree() {
        for (int value : data) {
            abt.insert(value);
        }

        int sum1 = abt.getMaxPathSum();

        int limit = capacity / 2;
        for (int i = capacity / 4; i < limit; i++) {
            abt.delete(data[i]);
        }

        int sum2 = abt.getMaxPathSum();

        return sum1 + sum2;
    }

    @Benchmark
    public int testLinkedBinaryTree() {
        for (int value : data) {
            lbt.insert(value);
        }

        int sum1 = lbt.getMaxPathSum();

        int limit = capacity / 2;
        for (int i = capacity / 4; i < limit; i++) {
            lbt.delete(data[i]);
        }

        int sum2 = lbt.getMaxPathSum();

        return sum1 + sum2;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BinaryTreeBenchmark.class.getSimpleName())
                .forks(1)            // Use 1 fork for faster local testing
                .warmupIterations(2) // Give the JIT time to optimize
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }
}
