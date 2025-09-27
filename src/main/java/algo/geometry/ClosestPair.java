package algo.geometry;

import metrics.DepthTracker;
import metrics.PerformanceTracker;

import java.util.Arrays;

public final class ClosestPair {
    private ClosestPair() {}

    public static double distance(Point2D[] pts, PerformanceTracker t, DepthTracker d) {
        if (pts == null || pts.length < 2) return Double.POSITIVE_INFINITY;
        Point2D[] px = Arrays.copyOf(pts, pts.length);
        Arrays.sort(px, (a, b) -> Double.compare(a.x(), b.x()));
        t.incAllocations(1);

        // отдельный массив, отсортированный по y (на выходе рекурсии поддерживаем порядок y)
        Point2D[] py = Arrays.copyOf(px, px.length);
        Arrays.sort(py, (a, b) -> Double.compare(a.y(), b.y()));

        d.enter();
        double ans = rec(px, py, new Point2D[px.length], 0, px.length - 1, t, d);
        d.exit();
        return ans;
    }

    // Рекурсия: px отсортирован по x; py — тот же диапазон [lo..hi], но отсортирован по y
    private static double rec(Point2D[] px, Point2D[] py, Point2D[] aux,
                              int lo, int hi, PerformanceTracker t, DepthTracker d) {
        if (hi - lo <= 3) {
            double best = brute(py, lo, hi, t);
            // отсортировать py[lo..hi] по y (вставками)
            for (int i = lo + 1; i <= hi; i++) {
                Point2D v = py[i];
                int j = i - 1;
                while (j >= lo && py[j].y() > v.y()) { py[j + 1] = py[j]; j--; }
                py[j + 1] = v;
            }
            return best;
        }

        int mid = (lo + hi) >>> 1;
        double midX = px[mid].x();

        // Разделим py на левую/правую части, сохраняя порядок по y
        int l = lo, r = mid + 1;
        for (int i = lo; i <= hi; i++) {
            if (py[i].x() <= midX) aux[l++] = py[i];
            else                   aux[r++] = py[i];
        }
        // скопируем обратно
        for (int i = lo; i <= mid; i++) py[i] = aux[i];
        for (int i = mid + 1; i <= hi; i++) py[i] = aux[i];

        d.enter(); t.incRecursiveCalls();
        double dl = rec(px, py, aux, lo, mid, t, d);
        d.exit();

        d.enter(); t.incRecursiveCalls();
        double dr = rec(px, py, aux, mid + 1, hi, t, d);
        d.exit();

        double dmin = Math.min(dl, dr);

        // Сформируем "полосу" ширины dmin из py (уже отсортирован по y)
        int m = 0;
        for (int i = lo; i <= hi; i++) {
            if (Math.abs(py[i].x() - midX) < dmin) aux[m++] = py[i];
        }

        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m && (aux[j].y() - aux[i].y()) < dmin; j++) {
                double dist = dist(aux[i], aux[j], t);
                if (dist < dmin) dmin = dist;
            }
        }
        return dmin;
    }

    private static double brute(Point2D[] py, int lo, int hi, PerformanceTracker t) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = lo; i <= hi; i++) {
            for (int j = i + 1; j <= hi; j++) {
                double d = dist(py[i], py[j], t);
                if (d < best) best = d;
            }
        }
        return best;
    }

    private static double dist(Point2D a, Point2D b, PerformanceTracker t) {
        // считаем как sqrt((dx)^2 + (dy)^2)
        double dx = a.x() - b.x();
        double dy = a.y() - b.y();
        t.incComparisons(); // считаем одну "сравнительную" операцию на пару (условно)
        return Math.hypot(dx, dy);
    }
}

