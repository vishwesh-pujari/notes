I have come up with a unique template in which Dijkstra's algorithm can be implemented using PriorityQueue

This template is slightly different from the typical Dijkstra's implementation

This template is very similar to how BFS would be implemented in a Graph like data structure

Very important point is to see how visited array is handled. We are only marking as visited AFTER POLLING from the queue and NOT WHILE ADDING.

Sample problems:

https://leetcode.com/problems/network-recovery-pathways

https://leetcode.com/problems/path-with-minimum-effort

https://leetcode.com/problems/find-a-safe-walk-through-a-grid

However, be informed that there are some problems where marking visited as true is important while adding in a queue - when we are doing level order traversal

https://leetcode.com/problems/find-the-safest-path-in-a-grid/


Also, do note that there are certain problems where you have to solve the problem using the "typical dijsktra's template", i.e. using the cost array and using a queue instead of PriorityQueue:

https://leetcode.com/problems/minimum-score-of-a-path-between-two-cities
