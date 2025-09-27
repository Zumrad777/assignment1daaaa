package algo.sort;

import metrics.PerformanceTracker;
import metrics.DepthTracker;

public final class MergeSort {
    private static final int CUTOFF = 32; // до этого размера используем вставки

    private MergeSort() {}

    public static void sort(int[] a, PerformanceTracker t, DepthTracker d) {
        if (a == null || a.length <= 1) return;
        t.incAllocations(1);
        int[] buf = new int[a.length];
        t.startTimer();
        d.enter();
        mergesort(a, 0, a.length, buf, t, d);
        d.exit();
        t.stopTimer();
    }

    private static void mergesort(int[] a, int lo, int hi, int[] buf,
                                  PerformanceTracker t, DepthTracker d) {
        int n = hi - lo;
        if (n <= CUTOFF) {
            insertion(a, lo, hi, t);
            return;
        }
        int mid = lo + (n >> 1);

        d.enter(); t.incRecursiveCalls();
        mergesort(a, lo, mid, buf, t, d);
        d.exit();

        d.enter(); t.incRecursiveCalls();
        mergesort(a, mid, hi, buf, t, d);
        d.exit();

        merge(a, lo, mid, hi, buf, t);
    }

    private static void merge(int[] a, int lo, int mid, int hi, int[] buf, PerformanceTracker t) {
        int i = lo, j = mid, k = 0;
        for (int p = lo; p < mid; p++) {
            buf[k++] = a[p];
            t.incArrayAccesses(2);
        }
        int leftSize = k;
        k = 0;
        int idx = lo;
        while (k < leftSize && j < hi) {
            t.incComparisons();
            if (buf[k] <= a[j]) {
                a[idx++] = buf[k++];
                t.incArrayAccesses(2);
            } else {
                a[idx++] = a[j++];
                t.incArrayAccesses(2);
            }
        }
        while (k < leftSize) {
            a[idx++] = buf[k++];
            t.incArrayAccesses(2);
        }
    }

    private static void insertion(int[] a, int lo, int hi, PerformanceTracker t) {
        for (int i = lo + 1; i < hi; i++) {
            int x = a[i];
            t.incArrayAccesses(1);
            int j = i - 1;
            while (j >= lo) {
                t.incComparisons();
                if (a[j] <= x) break;
                a[j + 1] = a[j];
                t.incArrayAccesses(2);
                j--;
            }
            a[j + 1] = x;
            t.incArrayAccesses(1);
        }
    }
}
