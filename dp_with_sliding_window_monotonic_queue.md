There are certain problems where a Dynamic Programming problem cannot be solved efficiently using plain memoization and tabulation and when a "Sliding Window" like structure is present.

Most of the times, dp[i] depends on max{dp[i+1], dp[i+2], ...., dp[i+k]}

To find this maximum in a sliding window, first learn the monotonic deque technique by solving this problem https://leetcode.com/problems/sliding-window-maximum/

In short this is the information about sliding window maximum using monotonic deque:
1. Maintain a monotonic deque of indices, by adding elements in a monotonic fashion from the rear.
2. The maximum value index must be present in the front.
3. Elements outside of the window should be removed from the front.

This technique we need to apply on DP array that we form.
Step by Step approach:
1. First write the vanilla Memoization approach (which TLEs)
2. Then write the corresponding Tabulation approach.
3. Then convert the tabulation approach into Sliding window maximum using deque.

Problems to practice:
https://leetcode.com/problems/jump-game-vi/

https://leetcode.com/problems/maximum-sum-of-m-non-overlapping-subarrays-i/
