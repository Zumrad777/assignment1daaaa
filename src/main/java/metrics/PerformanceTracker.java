package metrics;

public class PerformanceTracker {
    private long comparisons, swaps, arrayAccesses, allocations, recursiveCalls;
    private long startNs, elapsedNs;

    public void incComparisons()         { comparisons++; }
    public void incSwaps()               { swaps++; }
    public void incArrayAccesses(long n) { arrayAccesses += n; }
    public void incAllocations(long n)   { allocations += n; }
    public void incRecursiveCalls()      { recursiveCalls++; }

    public void startTimer() { startNs = System.nanoTime(); }
    public void stopTimer()  { elapsedNs = System.nanoTime() - startNs; }

    public long getComparisons()    { return comparisons; }
    public long getSwaps()          { return swaps; }
    public long getArrayAccesses()  { return arrayAccesses; }
    public long getAllocations()    { return allocations; }
    public long getRecursiveCalls() { return recursiveCalls; }
    public long getElapsedNs()      { return elapsedNs; }

    public void reset() {
        comparisons = swaps = arrayAccesses = allocations = recursiveCalls = 0;
        startNs = elapsedNs = 0;
    }
}
