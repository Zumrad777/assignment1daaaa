package algo.geometry;

import metrics.DepthTracker;
import metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClosestPairTest {
    private static double brute(Point2D[] pts) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double dx = pts[i].x() - pts[j].x();
                double dy = pts[i].y() - pts[j].y();
                double d = Math.hypot(dx, dy);
                if (d < best) best = d;
            }
        }
        return best;
    }

    @Test
    void small_n_matches_bruteforce() {
        int n = 1000;
        Random rnd = new Random(1);
        Point2D[] pts = new Point2D[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2D(rnd.nextDouble(1_000_000.0), rnd.nextDouble(1_000_000.0));
        }

        var t = new PerformanceTracker();
        var d = new DepthTracker();

        double fast = ClosestPair.distance(pts, t, d);
        double slow = brute(pts);

        assertEquals(slow, fast, 1e-9);
    }

    @Test
    void large_n_runs_and_depth_log() {
        int n = 50_000;
        Random rnd = new Random(2);
        Point2D[] pts = new Point2D[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2D(rnd.nextDouble(), rnd.nextDouble());
        }
        var t = new PerformanceTracker();
        var d = new DepthTracker();

        double ans = ClosestPair.distance(pts, t, d);
        int log2 = 1 + (31 - Integer.numberOfLeadingZeros(n));
        assertEquals(false, Double.isInfinite(ans));
        assertEquals(false, Double.isNaN(ans));
        // запас по глубине
        assert(d.getMaxDepth() <= log2 + 3);
    }
}
