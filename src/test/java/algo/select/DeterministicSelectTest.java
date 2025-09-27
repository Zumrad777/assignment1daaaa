package algo.select;

import metrics.PerformanceTracker;
import metrics.DepthTracker;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeterministicSelectTest {

    @Test
    void random_select_matches_sort() {
        int n = 1000;
        int[] a = new Random(1).ints(n, 0, 10_000).toArray();
        int[] copy = Arrays.copyOf(a, a.length);
        Arrays.sort(copy);

        var t = new PerformanceTracker();
        var d = new DepthTracker();

        for (int k = 0; k < 50; k++) {
            int result = DeterministicSelect.select(a, k, t, d);
            assertEquals(copy[k], result);
        }
    }
}
