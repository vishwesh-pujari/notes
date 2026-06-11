# Sparse Tables

> 📅 **11th June 2026** — Notes from [this brilliant video by Errichto Algorithms](https://youtu.be/0jWeUdxrGm4?si=Og3jsMjAPIGdT8iG)

---

## What are Sparse Tables?

Sparse tables help answer **range queries** like min, max, gcd in **O(1)** time.

---

## Two Conditions to Remember

1. **Array values should remain the same throughout. Updates aren't supported.**
2. **Range queries for whom overlapping doesn't affect can only be answered.**
   - ✅ Possible: `min`, `max`, `gcd` — overlapping doesn't change the answer
   - ❌ Not possible: `sum` — double-counting overlapping elements gives wrong answer

> **Why does overlap matter?** When querying, we use two possibly-overlapping ranges that together cover `[L, R]`. For `min`/`max`, an element counted twice doesn't affect the result. For `sum`, it would.

---

## Complexity

| Operation | Time Complexity |
|-----------|----------------|
| Construction | O(n log n) |
| Query | O(1) |

Space Complexity: **O(n log n)**

---

## Key Definition — Logarithm

> **log₂(n)** means: *how many times would you have to divide n by 2 to get 1?*

Hence:
- `log₂(1) = 0`
- `log₂(n) = log₂(n/2) + 1` ← **Very important!**

This recurrence is what makes computing LOG values efficiently possible (see `computeLOG` below).

---

## Dense Table vs Sparse Table

**Dense table** — `D[i][j]` stores the answer for every possible `(i, j)` index pair.
- Space: **O(n²)**

**Sparse table** — `S[i][j]` only stores answers for ranges whose lengths are powers of 2.
- `j` doesn't go up to `n`, it only goes up to `log₂(n) + 1`
- Space: **O(n log n)** — much better!

---

## What Does S[i][j] Mean?

> **`S[i][j]` = value of the range starting at index `i` and having length `2^j`**

Examples:
- `S[3][0]` → range `[3, 3]` (length = 2⁰ = 1) → just `nums[3]`
- `S[3][1]` → range `[3, 4]` (length = 2¹ = 2)
- `S[3][2]` → range `[3, 6]` (length = 2² = 4)

---

## Building the Sparse Table (DP)

The clever part: a range of length `2^j` is just **two non-overlapping halves**, each of length `2^(j-1)`.

```
Range [i, i + 2^j - 1]
       = [i, i + 2^(j-1) - 1]   ← left half
       + [i + 2^(j-1), i + 2^j - 1]  ← right half
```

So: `S[i][j] = combine(S[i][j-1], S[i + 2^(j-1)][j-1])`

This is the same DP recurrence for both MIN and MAX.

---

## Querying in O(1)

For a query `[L, R]`:
1. Find the **largest power of 2** that fits inside the range length: `log = LOG2[R - L + 1]`
2. Take two (possibly overlapping) ranges, each of length `2^log`:
   - Range 1: starts at `L` → `[L, L + 2^log - 1]`
   - Range 2: ends at `R` → `[R - 2^log + 1, R]`
3. Since overlap is fine for `min`/`max`, combine both answers.

> These two ranges always fully cover `[L, R]` — that's the guarantee. Even if they overlap in the middle, min/max of the union is still correct.

---

## Java Code

```java
class SparseTable {
    // S[i][j] means the value of the range starting from index i and of length 2^j

    private int[] LOG2; // floor of log
    private int[][] MIN; // MIN sparse table
    private int[][] MAX; // MAX sparse table

    public SparseTable(int[] nums) { // constructor
        int n = nums.length;
        
        computeLOG(n);
        
        MIN = new int[n][LOG2[n] + 1];
        MAX = new int[n][LOG2[n] + 1];

        buildSparseTable(nums);
    }

    private void computeLOG(int n) {
        // LOG means how many times we need to divide number by 2 to get 1
        LOG2 = new int[n + 1];
        LOG2[1] = 0;

        for (int i = 2; i <= n; i++)
            LOG2[i] = LOG2[i/2] + 1; // very clever!
    }

    private void buildSparseTable(int[] nums) {
        // S[i][j] means the value of the range starting from index i and of length 2^j
        // if j = 0, then it means all ranges of length 1
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            MIN[i][0] = MAX[i][0] = nums[i];
        }

        // Dynamic programming approach
        for (int j = 1; j < MIN[0].length; j++) { // range lengths 2, 4, 8, ...
            for (int i = 0; i + (1 << j) <= n; i++) { // for all indices
                // range is [i, i + (1 << j) - 1]
                // We break the range in 2 equal halves. So range of length 8 is broken in 2 ranges of length 4,
                // and we previously have computed the answer for 4 ranges

                MIN[i][j] = Math.min(MIN[i][j - 1], MIN[i + (1 << (j-1))][j - 1]);
                MAX[i][j] = Math.max(MAX[i][j - 1], MAX[i + (1 << (j-1))][j - 1]);
            }
        }
    }

    public int query(int L, int R) {
        int len = R - L + 1;

        // We find the largest log that fits inside this length
        // Then we take the 2 ranges:
        // 1. Starting at L and having length (2^log)
        // 2. Ending at R and having length (2^log)
        // Note: overlaps are there.

        int log = LOG2[len];
        int max = Math.max(MAX[L][log], MAX[R - (1 << log) + 1][log]);
        int min = Math.min(MIN[L][log], MIN[R - (1 << log) + 1][log]);
        return max - min;  // returns max - min of range [L, R]
    }
}
```

---

## Quick Summary

```
Array:          [2, 4, 3, 1, 6, 7, 8, 9, 1, 7]
                 |
                 ▼
Build S[i][j]  O(n log n)   ← precompute all power-of-2 ranges
                 |
                 ▼
Query [L, R]   O(1)         ← pick largest power-of-2 block, cover [L,R] with 2 overlapping blocks
```

> The whole magic of sparse tables is: precompute cleverly so queries become dead simple.
