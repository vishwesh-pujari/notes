The video that I've watched to understand Digit DP is by Code with Mik on the problem "3753. Total Waviness of Numbers in Range II"
Problem : https://leetcode.com/problems/total-waviness-of-numbers-in-range-ii/description/
Video : https://youtu.be/POvt5kXDesE?si=aZsz_IkevXG9fmgb

Another problem where I've applied the same template: https://leetcode.com/problems/count-good-integers-in-a-range

Follow the template above for Digit DP problems.
Some of the key concepts to remember:
1. problem(l..r) = digit_dp(r) - digit_dp(l - 1)
2. We need to keep track of the variable isLeadingZero
3. We need to keep track of the variable doesPrefixMatch
