# Assignment 1 
## Overview
This project implements and benchmarks several classic algorithms in Java:

- **MergeSort** – recursive divide-and-conquer sorting with reusable buffer to reduce allocations.
- **QuickSort** – randomized pivot, recursion into smaller partition first to minimize stack depth.
- **Deterministic Select (Median-of-Medians)** – selects the k-th order statistic in Θ(n).
- **Closest Pair of Points** – divide-and-conquer algorithm for geometric distance problem.

---

##  Architecture
The system consists of:
- **PerformanceTracker** – counts comparisons, swaps, array accesses, allocations, and runtime.
- **DepthTracker** – tracks maximum recursion depth.
- **CsvWriter** – writes benchmark results into `results.csv`.
- **BenchmarkRunner** – CLI tool that parses arguments, runs algorithms, and collects metrics.

Memory optimizations:
- MergeSort reuses a buffer array to save allocations.
- QuickSort always recurses into the smaller partition first, reducing stack depth.

---

##  Recurrences and Complexities
- **MergeSort**:  
  T(n) = 2T(n/2) + Θ(n) → Θ(n log n) (Master theorem, case 2)

- **QuickSort (randomized)**:  
  Expected Θ(n log n), recursion depth O(log n) with high probability.

- **Select (MoM5)**:  
  T(n) = T(n/5) + T(7n/10) + Θ(n) → Θ(n)

- **Closest Pair of Points**:  
  T(n) = 2T(n/2) + Θ(n) → Θ(n log n)

---

## How to Run Benchmarks
Use the CLI entry point `BenchmarkRunner`.
## CSV Output Format
Each benchmark appends a row to `results.csv`.

**Columns:**
- `algo` – algorithm name (`mergesort`, `quicksort`, `select`, `closest`)
- `n` – input size
- `trial` – trial number
- `time_ms` – runtime in milliseconds
- `comparisons` – number of comparisons
- `swaps` – number of swaps (sorting only)
- `array_accesses` – total array reads/writes
- `allocations` – number of allocated arrays/objects
- `max_depth` – maximum recursion depth
- `extra` – additional info (k & value for Select, distance for Closest)

Planned Graphs

Runtime vs Input Size – compare all algorithms.

Recursion Depth vs Input Size – compare MergeSort and QuickSort.

Discussion – constant-factor effects such as cache locality and garbage collection overhead.

Examples:
```bash
# Run all algorithms on n=1000 and 5000, 1 trial each
all 1000,5000 1

# Run only QuickSort on n=10000 and 20000, 2 trials
quicksort 10000,20000 2

## Summary

- **MergeSort** and **Closest Pair** both demonstrated the expected Θ(n log n) behavior. The runtime grows nearly linearly on a logarithmic scale, matching the theoretical recurrence.
- **QuickSort** also shows expected average-case Θ(n log n) runtime, but its recursion depth is higher than MergeSort. On larger inputs, effects of random pivots and cache locality become visible.
- **Deterministic Select (Median-of-Medians)** runs in Θ(n), but due to large constant factors it is slower than sorting on small to medium inputs.
- The differences observed in the plots reflect both asymptotic complexity and constant-factor effects such as allocations, cache behavior, and garbage collection.


