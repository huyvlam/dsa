package mytree;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.PriorityQueue;
import java.util.Random;

@State(Scope.Benchmark)
@Disabled
public class HeapViewBenchmark {
    @Param({"1000000", "10000000"})
    private int capacity;

    private int[] primData;
    private Integer[] boxedData;

    private ArrayBinaryTree abt;
    private PriorityQueue<Integer> pq;

    @Setup(Level.Trial)
    public void setupData() {
        primData = new int[capacity];
        boxedData = new Integer[capacity];
        Random rand = new Random();
        for (int i = 0; i < capacity; i++) {
            int val = rand.nextInt();
            primData[i] = val;
            boxedData[i] = val;
        }
    }

    @Setup(Level.Iteration)
    public void prepare() {
        abt = new ArrayBinaryTree(capacity);
        pq = new PriorityQueue<>(capacity);
    }

    @Benchmark
    public void mhvThroughputPrimitive(Blackhole bh) {
        try (MaxHeapView mhv = abt.createMaxHeap().get()) {
            for (int p : primData) {
                mhv.insert(p);
            }
            for (int b : boxedData) {
                if (b > mhv.peek()) {
                    bh.consume(mhv.replace(b));
                }
            }
            while (!abt.isEmpty()) {
                bh.consume(mhv.pop());
            }
        }
    }

    @Benchmark
    public void mhvThroughputBoxed(Blackhole bh) {
        try (MaxHeapView mhv = abt.createMaxHeap().get()) {
            for (Integer b : boxedData) {
                mhv.insert(b);
            }
            for (int p : primData) {
                if (p > mhv.peek()) {
                    bh.consume(mhv.replace(p));
                }
            }
            while (!abt.isEmpty()) {
                bh.consume(mhv.pop());
            }
        }
    }

    @Benchmark
    public void pqThroughputPrimitive(Blackhole bh) {
        for (int p : primData) {
            pq.add(p);
        }
        for (int b : boxedData) {
            if (!pq.isEmpty() && b > pq.peek()) {
                bh.consume(pq.poll());
                bh.consume(pq.add(b));
            }
        }
        while (!pq.isEmpty()) {
            bh.consume(pq.poll());
        }
    }

    @Benchmark
    public void pqThroughputBoxed(Blackhole bh) {
        for (Integer b : boxedData) {
            pq.add(b);
        }
        for (int p : primData) {
            if (!pq.isEmpty() && p > pq.peek()) {
                bh.consume(pq.poll());
                bh.consume(pq.add(p));
            }
        }
        while (!pq.isEmpty()) {
            bh.consume(pq.poll());
        }
    }

    @State(Scope.Benchmark)
    public static class FullTree {
        @Param({"1000000", "10000000"})
        public int capacity;

        public ArrayBinaryTree abt;

        @Setup(Level.Trial)
        public void setup() {
            abt = new ArrayBinaryTree(capacity);
            Random rand = new Random();

            for (int i = 0; i < capacity; i++) {
                abt.insert(rand.nextInt());
            }
        }
    }

    @Benchmark
    public void createMHV(FullTree ft, Blackhole bh) {
        try (MaxHeapView mhv = ft.abt.createMaxHeap().get()) {
            bh.consume(mhv);
        }
    }

    // Add these flags to VM Options before running: -Xms4G -Xmx4G -XX:+AlwaysPreTouch
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(HeapViewBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(2)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }
}
