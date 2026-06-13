# LCA — Lowest Common Ancestor

## Pre-requisite
- Binary Lifting — read [these notes](https://github.com/vishwesh-pujari/notes/blob/main/binary_lifting.md) first. LCA is basically binary lifting with a few extra steps on top.

---

## The Algorithm (after building the `up` table)

Once binary lifting is set up, finding LCA of nodes `u` and `v` is a 3-step process:

### Step 1 — Bring u and v to the same depth
Assume `u` is deeper than `v`. Calculate `k = depth[u] - depth[v]` and lift `u` up by `k` steps using binary representation of `k` (exact same trick as getKthAncestor).

### Step 2 — Check if u == v
If after leveling them, `u == v`, then `u` itself is the LCA. Done.

### Step 3 — Lift both until you're just below the LCA
Now both are at the same depth but different nodes. Start from the largest possible jump:
- If `up[u][j] == up[v][j]` → **don't jump** (we'd overshoot the LCA)
- If `up[u][j] != up[v][j]` → **jump both** (we're still below the LCA)

After this loop, `u` and `v` are sitting **just below** the LCA.
So `up[u][0]` (parent of `u`) is the answer.

---

## One Important Initialization

```
up[1][0] = 1  // root is its own parent
```

Without this, when you're lifting nodes and you hit the root, you'd get index 0 or something invalid. Making the root point to itself handles the boundary cleanly.

---

## Why Not Jump When `up[u][j] == up[v][j]`?

Because the LCA *might* be exactly `2^j` steps up — and if you jump there, you've gone past it (or landed on it too early, losing the precision to find the exact node). The goal is to stop just **one step below** the LCA so that `up[u][0]` gives you the answer.

---

## Reference Problem
- [LC 3067 — Number of Ways to Assign Edge Weights II](https://leetcode.com/problems/number-of-ways-to-assign-edge-weights-ii)

---

## Code

```java
class LCA {
    // We use binary lifting technique here to return LCA in O(logn) time
    // We use a sparse-table like data structure
    int[][] up; // up[i][j] is the (2^j)th ancestor of node i
    int[] LOG2; // floor of LOG to the base 2
    int[] depth; // depth of each node from root node

    public LCA(Map<Integer, List<Integer>> tree, int totalNodes) {
        computeLog(totalNodes);
        up = new int[totalNodes + 1][LOG2[totalNodes] + 1];
        up[1][0] = 1; // root is its own parent — important initialization!
        depth = new int[totalNodes + 1];
        dfs(tree, 1);
    }

    private void computeLog(int totalNodes) {
        LOG2 = new int[totalNodes + 1];
        LOG2[1] = 0;
        for (int i = 2; i <= totalNodes; i++)
            LOG2[i] = LOG2[i / 2] + 1; // <- Very important!
    }

    private void dfs(Map<Integer, List<Integer>> tree, int node) {
        for (int child : tree.get(node)) {
            up[child][0] = node; // parent of child is node
            depth[child] = depth[node] + 1; // elegance!
            for (int j = 1; j < up[child].length; j++) {
                up[child][j] = up[ up[child][j-1] ][j - 1]; // the recurrence
            }
            dfs(tree, child);
        }
    }

    public int lca(int u, int v) {
        // Step 1: bring u and v to the same depth (assume u is deeper)
        if (depth[u] < depth[v]) {
            int temp = u; u = v; v = temp;
        }
        int k = depth[u] - depth[v]; // lift u by k steps
        int log = up[0].length;
        for (int j = log - 1; j >= 0; j--) {
            if ( ((k >> j) & 1) == 1 ) // if jth bit is set in k
                u = up[u][j];
        }

        // Step 2: if they meet, u is the LCA
        if (u == v) return u;

        // Step 3: lift both until just below the LCA
        for (int j = log - 1; j >= 0; j--) {
            if (up[u][j] == up[v][j]) // jumping here would overshoot
                continue;

            // both land on different nodes → still below LCA, safe to jump
            // note: even after jumping, both u and v remain at the same depth
            u = up[u][j];
            v = up[v][j];
        }

        return up[u][0]; // u's parent is the LCA
    }

    public int depth(int node) {
        return depth[node];
    }
}
```

---

## Note on DFS vs Two For-loops

In the [binary lifting notes](https://github.com/vishwesh-pujari/notes/blob/main/binary_lifting.md), we used two for-loops to fill `up` because the tree was given as a `parent[]` array (already processed top-down).

Here the tree is given as an adjacency list, so we use **DFS** — and fill the entire `up[child]` row for a node right when we first visit it (before recursing into its children). This works because by the time we visit a child, its parent's row is already fully filled.

---

## Complexity

| | Time | Space |
|---|---|---|
| **Pre-processing** | O(N log N) | O(N log N) |
| **Query** | O(log N) | O(1) |

---

## Key Things to Remember

- `up[1][0] = 1` — root points to itself, don't forget this
- Level both nodes **before** the LCA search
- In Step 3: jump when ancestors are **different**, skip when they're the **same**
- After the loop, you're one step below the LCA → answer is `up[u][0]`
- DFS fills `up[child]` top-down; two for-loops work when you already have the `parent[]` array
