import java.util.Arrays;

public final class Benchmark {
    private static final int[] SIZES = { 1_000, 10_000, 100_000 };
    private static final int[] CUTOFFS = { 1, 4, 7, 10, 16, 24, 32, 48, 64 };
    private static final String[] TYPES = { "random", "sorted", "reverse", "duplicates" };
    private static final int WARMUP_RUNS = 10;
    private static final int TRIALS = 31;

    private Benchmark() {
    }

    public static void main(String[] args) throws Exception {
        try (var out = new java.io.PrintWriter("benchmark.csv")) {
            out.println("type,size,cutoff,median_ns");
            for (String type : TYPES) {
                for (int size : SIZES) {
                    int[] input = DataGenerator.create(type, size, 20261117L);
                    for (int cutoff : CUTOFFS) {
                        for (int i = 0; i < WARMUP_RUNS; i++)
                            measure(input, cutoff);

                        long[] times = new long[TRIALS];
                        for (int i = 0; i < TRIALS; i++)
                            times[i] = measure(input, cutoff);
                        Arrays.sort(times);
                        long median = times[TRIALS / 2];
                        out.printf("%s,%d,%d,%d%n", type, size, cutoff, median);
                        System.out.printf("%s n=%d cutoff=%d median=%.3f ms%n",
                                type, size, cutoff, median / 1_000_000.0);
                    }
                }
            }
        }
    }

    private static long measure(int[] input, int cutoff) {
        int[] copy = input.clone();
        long start = System.nanoTime();
        MergeSortCutoff.sort(copy, cutoff);
        long elapsed = System.nanoTime() - start;

        for (int i = 1; i < copy.length; i++) {
            if (copy[i - 1] > copy[i])
                throw new IllegalStateException("Kết quả chưa được sắp xếp");
        }
        return elapsed;
    }
}
