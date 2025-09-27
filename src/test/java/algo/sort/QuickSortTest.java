package algo.sort;

import metrics.PerformanceTracker;
import metrics.DepthTracker;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuickSortTest {
    @Test
    void random_is_sorted_and_depth_log() {
        int n = 100_000;
        int[] a = new Random(1).ints(n).toArray();
        var t = new PerformanceTracker();
        var d = new DepthTracker();

        QuickSort.sort(a, t, d);

        assertTrue(isSorted(a));
        int log2 = 1 + (31 - Integer.numberOfLeadingZeros(n));
        assertTrue(d.getMaxDepth() <= log2 * 4);
    }

    private static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) if (a[i] < a[i - 1]) return false;
        return true;
    }
}
