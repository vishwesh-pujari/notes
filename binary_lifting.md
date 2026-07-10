# Binary Lifting

Similar problems - https://leetcode.com/problems/path-existence-queries-in-a-graph-ii 



## Pre-requisite
- [Sparse Table](https://github.com/vishwesh-pujari/notes/blob/main/sparse_tables.md) — make sure you understand this first before diving in.

---

## What Problem Does This Solve?

Binary lifting is used to answer queries like **"What is the Kth ancestor of a node in a tree?"** in **O(log K)** time — instead of walking up K steps one by one.

---

## The Core Idea

We build a table `up[][]` (just like a sparse table):

> `up[i][j]` = the **(2^j)th ancestor** of node `i`

So:
- `up[i][0]` = parent of `i` (2^0 = 1 step up)
- `up[i][1]` = grandparent of `i` (2^1 = 2 steps up)
- `up[i][2]` = 2^2 = 4 steps up
- ...and so on

---

## The Recurrence Relation (the heart of it)

```
up[i][j] = up[ up[i][j-1] ][j-1]
```

**In plain English:** To go 2^j steps up from node `i`, first go 2^(j-1) steps up, and then go 2^(j-1) steps up again from *that* node.

**Example:** Want to go 8 steps up? Go 4 steps up → from there, go 4 more steps up. Done.

---

## Filling the `up` Table

You can fill it using DFS or two for-loops, depending on how the tree is given to you.

For a tree given as a `parent[]` array (like in LeetCode), two for-loops work perfectly:
- Outer loop: over `j` (power of 2) — **must go from 1 to LOG**
- Inner loop: over `i` (each node)

---

## Answering Queries: getKthAncestor(node, k)

Once the table is filled, to find the Kth ancestor:
1. Look at the **binary representation of k**
2. Wherever there's a `1` bit, **make that jump**

Example: k = 13 = `1101` in binary → jump 8 steps, then 4 steps, then 1 step.

This is why it's called **binary lifting** — you lift yourself up the tree using powers of 2.

---

## Code

> 📺 Great reference video by Errichto: https://youtu.be/oib-XsjFa-M?si=YvZmmZLqUUc-GQwD

```java
class TreeAncestor {
    // To find kth ancestor of a tree node in O(logk), we need to use the concept of binary lifting
    // up is similar to a sparse table
    int[][] up; // up[i][j] is the (2^j)th ancestor of the ith node
    int[] LOG2; // we pre-compute LOG to base 2

    public TreeAncestor(int n, int[] parent) {
        computeLog(n);
        up = new int[n][LOG2[n] + 1];

        for (int i = 0; i < n; i++)
            up[i][0] = parent[i]; // the (2^0)th ancestor of a node is the parent

        for (int j = 1; j < up[0].length; j++) {
            for (int i = 0; i < n; i++) {
                if (up[i][j - 1] == -1)
                    up[i][j] = -1;
                else
                    up[i][j] = up[ up[i][j-1] ][j - 1]; // THE recurrence
            }
        }
    }

    private void computeLog(int n) {
        LOG2 = new int[n + 1];
        // LOG2[i] = how many times we divide i by 2 to reach 1
        LOG2[1] = 0;
        for (int i = 2; i <= n; i++)
            LOG2[i] = LOG2[i / 2] + 1; // very elegant and clever
    }

    public int getKthAncestor(int node, int k) {
        // check the binary representation of k and jump on powers of 2
        int log = up[0].length;
        for (int i = log - 1; i >= 0; i--) {
            if ( ((k >> i) & 1) == 1 )
                node = up[node][i];

            if (node == -1)
                return -1;
        }
        return node;
    }
}
```

---

## Complexity

| | Time | Space |
|---|---|---|
| **Pre-processing** | O(N log N) | O(N log N) |
| **Query** | O(log K) | O(1) |

---

## Key Things to Remember

- `up[i][j] = up[ up[i][j-1] ][j-1]` — internalize this, everything else follows
- Fill the table **j before i** (outer loop = j, inner loop = i)
- Query = scan bits of k from high to low, jump whenever you see a `1`
- The `LOG2` pre-computation trick (`LOG2[i] = LOG2[i/2] + 1`) is elegant — don't overlook it
