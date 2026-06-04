# 📘 Recursion Problems — Detailed Explanations

> Covering: Fibonacci Series · Palindrome Check · Power Set (Take/Not-Take) · Subsequences with Sum=K · Reach Target

---

## 📚 Table of Contents

1. [Fibonacci Series](#1-fibonacci-series)
2. [Palindrome Check (Recursion)](#2-palindrome-check-using-recursion)
3. [Basic Recursion Patterns](#3-basic-recursion-patterns)
4. [Power Set — Take / Not Take Algorithm](#4-power-set--take--not-take-algorithm)
5. [Print Subsequences with Sum = K](#5-print-subsequences-with-sum--k)
6. [Reach the Target (Count Ways)](#6-reach-the-target-count-ways)

---

## 1. Fibonacci Series

### What is Fibonacci?

The Fibonacci sequence: **0, 1, 1, 2, 3, 5, 8, 13, 21, ...**

Each number = sum of the two before it.

```
F(0) = 0
F(1) = 1
F(n) = F(n-1) + F(n-2)   for n >= 2
```

---

### Method 1: Pure Recursion

```
fib(5)
├── fib(4)
│   ├── fib(3)
│   │   ├── fib(2) → fib(1)+fib(0) = 1
│   │   └── fib(1) = 1
│   └── fib(2) → 1
└── fib(3)
    ├── fib(2) → 1
    └── fib(1) = 1
```

**Problem**: fib(3) computed **twice**, fib(2) computed **three times** → exponential work!

**Time**: O(2ⁿ)  
**Space**: O(n) call stack

---

### Method 2: Memoization (Top-Down DP)

Store computed results in a table (array or map).  
If `memo[n]` exists, return it immediately instead of recomputing.

```
fib(5) → not in memo
  fib(4) → not in memo
    fib(3) → not in memo
      fib(2) → not in memo → computes, stores memo[2]=1
      fib(1) → returns 1
    stores memo[3]=2
    fib(2) → IN MEMO → returns 1 instantly ✓
  stores memo[4]=3
  fib(3) → IN MEMO → returns 2 instantly ✓
stores memo[5]=5
```

**Time**: O(n)  
**Space**: O(n) memo table + O(n) stack

---

### Method 3: Tabulation (Bottom-Up DP)

Build a dp table from the ground up (no recursion needed):

```
dp[0]=0, dp[1]=1
dp[2] = dp[1]+dp[0] = 1
dp[3] = dp[2]+dp[1] = 2
dp[4] = dp[3]+dp[2] = 3
dp[5] = dp[4]+dp[3] = 5
```

**Time**: O(n)  
**Space**: O(n)

---

### Method 4: Two-Pointer / Space-Optimized DP

Since fib(n) only needs the **last two values**, we don't need the full array:

```
prev2=0, prev1=1
n=2: curr = 0+1 = 1  → prev2=1, prev1=1
n=3: curr = 1+1 = 2  → prev2=1, prev1=2
n=4: curr = 1+2 = 3  → prev2=2, prev1=3
n=5: curr = 2+3 = 5  → answer!
```

**Time**: O(n)  
**Space**: O(1) ← best!

---

### Comparison Table

| Method | Time | Space | Notes |
|--------|------|-------|-------|
| Recursion (naive) | O(2ⁿ) | O(n) | Simple but slow |
| Memoization | O(n) | O(n) | Fast, easy to write |
| Tabulation | O(n) | O(n) | Iterative, no stack risk |
| Two-pointer | O(n) | O(1) | **Best for large n** |

---

## 2. Palindrome Check Using Recursion

### What is a Palindrome?

A string that reads the **same forwards and backwards**.

```
"racecar" → palindrome ✓
"madam"   → palindrome ✓
"hello"   → NOT a palindrome ✗
"abcba"   → palindrome ✓
```

---

### Recursive Approach (Two-Pointer with Recursion)

**Key Idea**:
- Compare the **first** and **last** characters.
- If they match, **recurse** on the inner substring.
- Base case: empty string or single character is always a palindrome.

```
isPalindrome("racecar")
  → 'r' == 'r'? YES → isPalindrome("aceca")
      → 'a' == 'a'? YES → isPalindrome("cec")
          → 'c' == 'c'? YES → isPalindrome("e")
              → length 1 → return true  ← BASE CASE
          ← return true
      ← return true
  ← return true
```

```
isPalindrome("hello")
  → 'h' == 'o'? NO → return false immediately
```

---

### Call Stack for "racecar"

```
┌──────────────────────────────────────────────────┐
│ isPalindrome("racecar", 0, 6)  [lo=0, hi=6]      │
│   ├── 'r' == 'r' ✓                               │
│   └── calls isPalindrome("racecar", 1, 5)        │
│         ├── 'a' == 'a' ✓                         │
│         └── calls isPalindrome("racecar", 2, 4)  │
│               ├── 'c' == 'c' ✓                   │
│               └── calls isPalindrome("racecar",3,3)│
│                     └── lo >= hi → return true   │
└──────────────────────────────────────────────────┘

Return phase: true ← true ← true ← true
```

---

### Variations

**Case-insensitive**: Convert to lowercase before checking.  
**Ignore spaces**: Strip non-alphanumeric characters first.  
**Integer palindrome**: Convert to string, then check.

---

## 3. Basic Recursion Patterns

### Pattern 1: Print 1 to N

```
printAscending(3):
  recurse → recurse → BASE (n=0) → print 1 → print 2 → print 3
```

The print happens in the **return phase** → ascending order.

### Pattern 2: Print N to 1

```
printDescending(3):
  print 3 → recurse → print 2 → recurse → print 1 → BASE
```

The print happens in the **call phase** → descending order.

### Pattern 3: Sum of N natural numbers

```
sum(5) = 5 + sum(4)
       = 5 + 4 + sum(3)
       = 5 + 4 + 3 + sum(2)
       = 5 + 4 + 3 + 2 + sum(1)
       = 5 + 4 + 3 + 2 + 1 + sum(0)
       = 5 + 4 + 3 + 2 + 1 + 0 = 15
```

### Pattern 4: Reverse a String

```
reverse("hello")
  → 'h' + reverse("ello")
         → 'e' + reverse("llo")
                 → 'l' + reverse("lo")
                         → 'l' + reverse("o")
                                 → "o" (BASE)
                         → "ol"
                 → "oll"
         → "olle"
  → "olleh"
```

---

## 4. Power Set — Take / Not Take Algorithm

### What is a Power Set?

The **power set** of a set S is the collection of **all possible subsets** of S.

```
S = {1, 2, 3}
Power Set = { {}, {3}, {2}, {2,3}, {1}, {1,3}, {1,2}, {1,2,3} }
Total = 2³ = 8 subsets
```

For a set of size n → **2ⁿ subsets**.

---

### The Take / Not-Take Algorithm

At each element, make a binary decision:
- **Take** the element → include it in the current subset
- **Not Take** the element → skip it

This generates a **binary decision tree** with 2ⁿ leaves (subsets).

---

### Recursion Tree for `{1, 2, 3}`

```
                         f(0, [])
                        /         \
              Take 1               Not Take 1
           f(1, [1])                f(1, [])
           /       \                /       \
      Take 2    Not Take 2     Take 2    Not Take 2
    f(2,[1,2])  f(2,[1])     f(2,[2])    f(2,[])
     /    \      /    \       /    \       /    \
  T3    NT3   T3   NT3    T3   NT3    T3   NT3
[1,2,3][1,2][1,3] [1]  [2,3] [2]   [3]   []

Leaves (base cases, i == arr.length): print the current list
```

---

### Algorithm (Pseudocode)

```
generateSubsets(arr, index, currentSubset, allSubsets):
    if index == arr.length:
        allSubsets.add(copy of currentSubset)
        return

    // Take: include arr[index]
    currentSubset.add(arr[index])
    generateSubsets(arr, index+1, currentSubset, allSubsets)
    
    // Not Take: backtrack (remove) and skip arr[index]
    currentSubset.remove(last element)  // BACKTRACK
    generateSubsets(arr, index+1, currentSubset, allSubsets)
```

**Critical**: The `remove(last element)` step is the **backtrack** — undoing the "take" choice before exploring the "not take" branch.

---

### Why Backtracking is Needed

```
Before take: currentSubset = [1]
After take:  currentSubset = [1, 2]   ← added 2
Explore take branch...
BACKTRACK:   currentSubset = [1]      ← removed 2
Explore not-take branch...
```

Without backtracking, the "not take" branch would incorrectly include element 2.

---

### Time & Space

**Time**: O(2ⁿ × n) — 2ⁿ subsets, each up to length n  
**Space**: O(n) recursion depth + O(2ⁿ × n) for output

---

## 5. Print Subsequences with Sum = K

### Problem Statement

Given an array `arr[]` and integer `K`, print **all subsequences** of `arr` whose elements sum to exactly `K`.

```
arr = [1, 2, 1],  K = 2
Subsequences with sum 2: [1,1], [2]
```

### What is a Subsequence?

A subsequence is derived from the array by **deleting some elements** (possibly none) while **maintaining the original order**.

```
arr = [1, 2, 1]
All subsequences: [], [1], [2], [1], [1,2], [1,1], [2,1], [1,2,1]
```

---

### Algorithm (Built on Take / Not-Take)

Extension of the Power Set algorithm with an additional condition:

```
printSubsequences(arr, index, currentSubset, currentSum, K):
    if index == arr.length:
        if currentSum == K:
            print currentSubset
        return

    // Take arr[index]
    currentSubset.add(arr[index])
    printSubsequences(arr, index+1, currentSubset, currentSum + arr[index], K)
    currentSubset.remove(last)  // BACKTRACK
    
    // Not Take arr[index]
    printSubsequences(arr, index+1, currentSubset, currentSum, K)
```

---

### Recursion Tree for arr=[1,2,1], K=2

```
                       f(0, [], sum=0)
                       /              \
                T(1)                   NT
          f(1,[1], sum=1)          f(1,[], sum=0)
          /           \            /            \
        T(2)          NT         T(2)            NT
  f(2,[1,2],sum=3) f(2,[1],sum=1) f(2,[2],sum=2) f(2,[],sum=0)
    /     \          /     \          /     \         /     \
  T(1)   NT       T(1)    NT       T(1)   NT       T(1)   NT
  [1,2,1] [1,2]  [1,1] [1]       [2,1] [2]       [1]    []
  sum=4   sum=3  sum=2✓ sum=1   sum=3  sum=2✓   sum=1   sum=0

Printed: [1,1] and [2]
```

---

### Optimization: Early Pruning

If `currentSum > K` (for non-negative arrays), no need to explore further:

```java
if (currentSum > K) return; // Prune — sum exceeded already
```

---

### Variation: Count Subsequences (not print)

Instead of printing, return a count:
```
countSubsequences(arr, index, currentSum, K):
    if index == arr.length:
        return (currentSum == K) ? 1 : 0
    take = countSubsequences(arr, index+1, currentSum+arr[index], K)
    notTake = countSubsequences(arr, index+1, currentSum, K)
    return take + notTake
```

---

## 6. Reach the Target (Count Ways)

### Problem Statement

Given a starting value and target, count the number of ways to reach the target using allowed moves.

**Classic version**: Count ways to reach `n`-th stair where you can climb 1 or 2 steps at a time.

```
stairs(4):
  1+1+1+1
  1+1+2
  1+2+1
  2+1+1
  2+2
→ 5 ways
```

---

### Recursive Formulation

```
ways(n):
    if n == 0: return 1  // Reached target exactly
    if n < 0:  return 0  // Overshot — invalid
    return ways(n-1) + ways(n-2)  // Take 1 step or 2 steps
```

---

### Recursion Tree for `stairs(4)`

```
                        ways(4)
                      /        \
                ways(3)        ways(2)
               /      \        /     \
           ways(2)  ways(1)  ways(1) ways(0)=1
           /   \    /   \    /   \
       ways(1) w(0) w(0) w(-1) w(0) w(-1)
       /   \    =1   =1   =0    =1   =0
    ways(0) w(-1)
      =1     =0
```

Total leaves with value 1 = **5 ways**.

---

### Generalization: Reach Target with Arbitrary Moves

```
Given moves = [1, 3, 5], count ways to reach target T.

ways(T, moves):
    if T == 0: return 1
    if T < 0:  return 0
    total = 0
    for each move in moves:
        total += ways(T - move, moves)
    return total
```

---

### With Memoization

```
memo = {}
ways(T, moves):
    if T in memo: return memo[T]
    if T == 0: return 1
    if T < 0:  return 0
    total = sum(ways(T - m, moves) for m in moves)
    memo[T] = total
    return total
```

---

### Related Problems

| Problem | Approach |
|---------|----------|
| Coin Change (count ways) | Same as reach target with coin denominations |
| 0/1 Knapsack | Take/not-take on items with weight limit |
| Subset Sum (exists?) | Take/not-take, return true/false |
| Subset Sum (count) | Take/not-take, return count |
| Print all subsets with sum=K | Take/not-take, print when base reached |
| Permutations | Choose each unused element at each step |

---

## 🗺️ How Problems Connect

```
Basic Recursion
      │
      ▼
Take / Not-Take
      │
      ├──► Power Set (all subsets)
      │
      ├──► Subsequences with Sum = K
      │         (Power Set + sum condition)
      │
      └──► Reach Target
                (simplified subsequence)
                     │
                     ▼
               Coin Change / Knapsack
               (add memoization → DP)
```

---

> 📌 **Remember**: Every problem in backtracking/subsets follows the same template:
> 1. Base case: `index == arr.length` → check/add result
> 2. Take: add element, recurse with `index+1`
> 3. Backtrack: remove element
> 4. Not-Take: recurse with `index+1` (without element)
