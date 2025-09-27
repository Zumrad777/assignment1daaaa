package algo.select;

import metrics.PerformanceTracker;
import metrics.DepthTracker;

public final class DeterministicSelect {

    private DeterministicSelect() {}

    // Находим k-й (0-based) порядок статистики
    public static int select(int[] a, int k, PerformanceTracker t, DepthTracker d) {
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        t.startTimer();
        d.enter();
        int result = selectRec(a, 0, a.length - 1, k, t, d);
        d.exit();
        t.stopTimer();
        return result;
    }

    private static int selectRec(int[] a, int lo, int hi, int k,
                                 PerformanceTracker t, DepthTracker d) {
        if (lo == hi) return a[lo];

        // 1) группируем по 5 и берём медианы
        int n = hi - lo + 1;
        int groups = (n + 4) / 5;
        int[] med = new int[groups];
        for (int i = 0; i < groups; i++) {
            int gLo = lo + i * 5;
            int gHi = Math.min(gLo + 4, hi);
            insertionSort(a, gLo, gHi); // корректности достаточно
            med[i] = a[gLo + (gHi - gLo) / 2];
        }

        // 2) медиана медиан как pivot-значение
        int pivot = (groups == 1) ? med[0] : selectRec(med, 0, groups - 1, groups / 2, t, d);

        // 3) трёхпутевое разбиение по pivot-значению
        int[] eqRange = partition3(a, lo, hi, pivot);
        int ltEnd = eqRange[0];   // последний индекс блока "< pivot"
        int gtBeg = eqRange[1];   // первый индекс блока "> pivot"

        int leftSize  = ltEnd - lo + 1;                 // может быть 0, если ltEnd < lo
        if (ltEnd < lo) leftSize = 0;
        int eqSize    = (gtBeg - 1) - (ltEnd + 1) + 1;  // размер блока "== pivot"
        if (eqSize < 0) eqSize = 0;

        // k — относительно левого края
        if (k < leftSize) {
            d.enter(); t.incRecursiveCalls();
            int res = selectRec(a, lo, ltEnd, k, t, d);
            d.exit();
            return res;
        } else if (k < leftSize + eqSize) {
            return pivot;
        } else {
            d.enter(); t.incRecursiveCalls();
            int res = selectRec(a, gtBeg, hi, k - (leftSize + eqSize), t, d);
            d.exit();
            return res;
        }
    }
    private static int[] partition3(int[] a, int lo, int hi, int pivot) {
        int lt = lo;
        int i  = lo;
        int gt = hi;
        while (i <= gt) {
            if (a[i] < pivot) {
                swap(a, lt, i);
                lt++; i++;
            } else if (a[i] > pivot) {
                swap(a, i, gt);
                gt--;
            } else {
                i++;
            }
        }
        return new int[]{lt - 1, gt + 1};
    }

    private static void swap(int[] a, int i, int j) {
        if (i == j) return;
        int tmp = a[i]; a[i] = a[j]; a[j] = tmp;
    }
    private static void insertionSort(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            int x = a[i];
            int j = i - 1;
            while (j >= lo && a[j] > x) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = x;
        }
    }
}
