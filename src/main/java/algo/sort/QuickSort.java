package algo.sort;

import metrics.PerformanceTracker;
import metrics.DepthTracker;
import java.util.Random;

public final class QuickSort {
    private static final int CUTOFF = 32;
    private static final Random RAND = new Random(1);

    private QuickSort() {}

    public static void sort(int[] a, PerformanceTracker t, DepthTracker d) {
        if (a == null || a.length <= 1) return;
        t.startTimer();
        d.enter();
        quicksort(a, 0, a.length - 1, t, d);
        d.exit();
        t.stopTimer();
    }

    private static void quicksort(int[] a, int lo, int hi,
                                  PerformanceTracker t, DepthTracker d) {
        if (hi - lo + 1 <= CUTOFF) {
            insertion(a, lo, hi, t);
            return;
        }

        int pivotIndex = lo + RAND.nextInt(hi - lo + 1);
        int pivot = a[pivotIndex];
        t.incArrayAccesses(1);

        int i = lo, j = hi;
        while (i <= j) {
            while (a[i] < pivot) { i++; t.incComparisons(); t.incArrayAccesses(1); }
            while (a[j] > pivot) { j--; t.incComparisons(); t.incArrayAccesses(1); }
            if (i <= j) {
                swap(a, i, j, t);
                i++;
                j--;
            }
        }

        if (lo < j) { d.enter(); t.incRecursiveCalls(); quicksort(a, lo, j, t, d); d.exit(); }
        if (i < hi) { d.enter(); t.incRecursiveCalls(); quicksort(a, i, hi, t, d); d.exit(); }
    }

    private static void insertion(int[] a, int lo, int hi, PerformanceTracker t) {
        for (int i = lo + 1; i <= hi; i++) {
            int x = a[i];
            t.incArrayAccesses(1);
            int j = i - 1;
            while (j >= lo && a[j] > x) {
                t.incComparisons();
                a[j + 1] = a[j];
                t.incArrayAccesses(2);
                j--;
            }
            a[j + 1] = x;
            t.incArrayAccesses(1);
        }
    }

    private static void swap(int[] a, int i, int j, PerformanceTracker t) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
        t.incSwaps();
        t.incArrayAccesses(4);
    }
}
