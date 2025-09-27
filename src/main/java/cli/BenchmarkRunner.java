package cli;

import algo.sort.MergeSort;
import algo.sort.QuickSort;
import algo.select.DeterministicSelect;
import algo.geometry.Point2D;
import algo.geometry.ClosestPair;
import metrics.PerformanceTracker;
import metrics.DepthTracker;
import Util.CsvWriter; // если у тебя пакет называется `util`, поменяй здесь на `util.CsvWriter`

import java.util.Arrays;
import java.util.Random;

public class BenchmarkRunner {

    public static void main(String[] args) {
        String algo = args.length > 0 ? args[0].toLowerCase() : "all";
        int[] ns = (args.length > 1)
                ? Arrays.stream(args[1].split(",")).mapToInt(Integer::parseInt).toArray()
                : new int[]{10_000, 50_000, 100_000};
        int trials = (args.length > 2) ? Integer.parseInt(args[2]) : 3;

        try (CsvWriter csv = new CsvWriter("results.csv")) {
            csv.header("algo,n,trial,time_ms,comparisons,swaps,array_accesses,allocations,max_depth,extra");

            for (int n : ns) {
                if (algo.equals("all") || algo.equals("mergesort")) benchMergeSort(n, trials, csv);
                if (algo.equals("all") || algo.equals("quicksort")) benchQuickSort(n, trials, csv);
                if (algo.equals("all") || algo.equals("select"))    benchSelect(n, trials, csv);
                if (algo.equals("all") || algo.equals("closest"))   benchClosest(n, trials, csv);
            }

            System.out.println("Wrote results.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void benchMergeSort(int n, int trials, CsvWriter csv) {
        Random rnd = new Random(1);
        for (int t = 1; t <= trials; t++) {
            int[] a = rnd.ints(n).toArray();
            var perf = new PerformanceTracker();
            var depth = new DepthTracker();
            MergeSort.sort(a, perf, depth);
            csv.row("mergesort", n, t, nsToMs(perf.getElapsedNs()), perf.getComparisons(),
                    perf.getSwaps(), perf.getArrayAccesses(), perf.getAllocations(),
                    depth.getMaxDepth(), "");
        }
    }

    private static void benchQuickSort(int n, int trials, CsvWriter csv) {
        Random rnd = new Random(2);
        for (int t = 1; t <= trials; t++) {
            int[] a = rnd.ints(n).toArray();
            var perf = new PerformanceTracker();
            var depth = new DepthTracker();
            QuickSort.sort(a, perf, depth);
            csv.row("quicksort", n, t, nsToMs(perf.getElapsedNs()), perf.getComparisons(),
                    perf.getSwaps(), perf.getArrayAccesses(), perf.getAllocations(),
                    depth.getMaxDepth(), "");
        }
    }

    private static void benchSelect(int n, int trials, CsvWriter csv) {
        Random rnd = new Random(3);
        int k = n / 2; // медиана
        for (int t = 1; t <= trials; t++) {
            int[] a = rnd.ints(n).toArray();
            var perf = new PerformanceTracker();
            var depth = new DepthTracker();
            int v = DeterministicSelect.select(a, k, perf, depth);
            csv.row("select", n, t, nsToMs(perf.getElapsedNs()), perf.getComparisons(),
                    perf.getSwaps(), perf.getArrayAccesses(), perf.getAllocations(),
                    depth.getMaxDepth(), "k=" + k + ";val=" + v);
        }
    }

    private static void benchClosest(int n, int trials, CsvWriter csv) {
        Random rnd = new Random(4);
        for (int t = 1; t <= trials; t++) {
            Point2D[] pts = new Point2D[n];
            for (int i = 0; i < n; i++) pts[i] = new Point2D(rnd.nextDouble(), rnd.nextDouble());
            var perf = new PerformanceTracker();
            var depth = new DepthTracker();
            double d = ClosestPair.distance(pts, perf, depth);
            csv.row("closest", n, t, nsToMs(perf.getElapsedNs()), perf.getComparisons(),
                    perf.getSwaps(), perf.getArrayAccesses(), perf.getAllocations(),
                    depth.getMaxDepth(), "dist=" + d);
        }
    }

    private static double nsToMs(long ns) {
        return ns / 1_000_000.0;
    }
}
