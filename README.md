# 🔁 Recursion — Complete Reference Guide

> **A function that calls itself to solve a smaller version of the same problem.**

---

## 📚 Table of Contents

1. [What is Recursion?](#1-what-is-recursion)
2. [Base Condition](#2-base-condition)
3. [Where to Use Recursion](#3-where-to-use-recursion)
4. [How to Use Recursion](#4-how-to-use-recursion)
5. [Essential Features](#5-essential-features)
6. [Recursion Tree](#6-recursion-tree)
7. [Memory / Stack Representation](#7-memory--stack-representation)
8. [Common Errors](#8-common-errors)
9. [Types of Recursion](#9-types-of-recursion)
10. [Recursion vs Iteration](#10-recursion-vs-iteration)
11. [Time & Space Complexity](#11-time--space-complexity)
12. [Repository Structure](#12-repository-structure)

---

## 1. What is Recursion?

Recursion is a programming technique where **a function calls itself** as part of its own definition. Each call works on a **smaller or simpler sub-problem** until a trivial case (base condition) is reached.

```
Problem(n)
  └── Problem(n-1)
        └── Problem(n-2)
              └── ... 
                    └── Problem(base)  ← stops here
```

### Real-world Analogy

Think of a **Russian nesting doll (Matryoshka)**:  
- You open the outer doll to find a smaller one inside.  
- You keep opening until you reach the smallest, solid doll — that is the **base case**.  
- Then you reassemble (unwind) each layer back to the outermost doll.

### Formal Definition

A recursive function must:
1. **Call itself** with a modified argument.
2. **Make progress** toward the base case with every call.
3. **Have a base case** that terminates the recursion.

```java
// Generic recursive template
returnType solve(parameters) {
    // Step 1: Base condition
    if (base_case_met) {
        return base_value;
    }

    // Step 2: Recursive call (smaller problem)
    return combine(currentWork, solve(smallerProblem));
}
```

---

## 2. Base Condition

The **base condition** (also called base case) is the **termination condition** — the simplest form of the problem that can be answered directly without further recursion.

### Why Is It Critical?

Without a base case, recursion never stops → **infinite recursion** → **StackOverflowError**.

### Rules for a Good Base Case

| Rule | Explanation |
|------|-------------|
| Must be reachable | Every possible input path must eventually hit the base case |
| Must be correct | The base case must return the correct answer for that input |
| Must terminate | After the base case, no further recursive call is made |

### Examples

```java
// Factorial: base case is n == 0 or n == 1
int factorial(int n) {
    if (n <= 1) return 1;          // BASE CASE
    return n * factorial(n - 1);   // Recursive case
}

// Sum of array: base case is empty array
int sum(int[] arr, int i) {
    if (i == arr.length) return 0; // BASE CASE
    return arr[i] + sum(arr, i+1);
}

// Binary search: base case is lo > hi (not found) or element found
int binarySearch(int[] arr, int lo, int hi, int target) {
    if (lo > hi) return -1;        // BASE CASE: not found
    int mid = (lo + hi) / 2;
    if (arr[mid] == target) return mid; // BASE CASE: found
    if (arr[mid] < target) return binarySearch(arr, mid+1, hi, target);
    return binarySearch(arr, lo, mid-1, target);
}
```

---

## 3. Where to Use Recursion

Recursion is best when the **problem has a naturally recursive structure** — i.e., it can be broken into smaller identical sub-problems.

### ✅ Use Recursion When:

| Domain | Examples |
|--------|----------|
| **Tree / Graph traversal** | Inorder, Preorder, Postorder, DFS |
| **Divide & Conquer** | Merge Sort, Quick Sort, Binary Search |
| **Combinatorics** | Subsets, Permutations, Combinations |
| **Backtracking** | N-Queens, Sudoku, Maze solving |
| **Dynamic Programming** | Fibonacci, Knapsack (with memoization) |
| **Mathematical sequences** | Factorial, GCD, Power, Fibonacci |
| **String problems** | Palindrome check, Reverse string |
| **Linked Lists** | Reverse LL, Detect cycle |

### ❌ Avoid Recursion When:

- A simple loop solves it more clearly.
- The recursion depth is very large (risk of stack overflow).
- Performance is critical and no memoization is used.
- The language/environment has a very limited call stack.

---

## 4. How to Use Recursion

### Step-by-Step Approach (3-Step Method)

```
Step 1 → Identify the base case
Step 2 → Identify the recursive case (smaller sub-problem)
Step 3 → Combine the result of the recursive call with the current step
```

### Example: Print numbers from N down to 1

```
Step 1: Base case → if n == 0, stop (return)
Step 2: Recursive case → call print(n-1)
Step 3: Print n, then recurse (or recurse, then print — depends on order)
```

```java
// Print N to 1
void printDescending(int n) {
    if (n == 0) return;      // Base case
    System.out.print(n + " ");
    printDescending(n - 1);  // Recursive call
}

// Print 1 to N (just swap order!)
void printAscending(int n) {
    if (n == 0) return;
    printAscending(n - 1);   // Recursive call FIRST
    System.out.print(n + " ");
}
```

### The "Trust the Recursion" Mindset

When writing recursive code, **trust that your recursive call works correctly** for smaller inputs. Focus only on:
- What is the base case?
- What should happen at the current level?
- What should I pass to the recursive call?

---

## 5. Essential Features of Recursion

### 5.1 Self-Reference
A recursive function **references itself** in its body.

### 5.2 Termination (Base Case)
Every recursion **must terminate** — there must be at least one path that doesn't recurse.

### 5.3 Progress
Each recursive call must move **closer to the base case**. The argument must change so the base case is eventually reached.

### 5.4 Divide and Conquer Nature
Recursion naturally embodies divide and conquer:
- **Divide**: Break the problem into sub-problems.
- **Conquer**: Solve each sub-problem recursively.
- **Combine**: Merge results back.

### 5.5 Implicit Stack Usage
Every function call is stored on the **call stack**. Recursion uses this stack to remember where to return after each call.

### 5.6 Two Phases: Call Phase & Return Phase

```
Call Phase  →  Functions are called one by one
Return Phase ← Results are returned and combined
```

```java
void func(int n) {
    if (n == 0) return;
    // Code here runs DURING CALL PHASE (going down)
    System.out.println("Before: " + n);
    func(n - 1);
    // Code here runs DURING RETURN PHASE (coming back up)
    System.out.println("After: " + n);
}
// For func(3):
// Output: Before 3, Before 2, Before 1, After 1, After 2, After 3
```

---

## 6. Recursion Tree

A **recursion tree** is a visual diagram showing all the recursive calls made during execution.

### How to Draw a Recursion Tree

1. The root is the **initial call**.
2. Each node's children are the **recursive calls** made from that node.
3. Leaf nodes are **base cases**.

### Example: `factorial(4)`

```
                    factorial(4)
                         |
                  4 * factorial(3)
                         |
                  3 * factorial(2)
                         |
                  2 * factorial(1)
                         |
                      returns 1    ← BASE CASE

Return phase (unwinding):
  factorial(1) = 1
  factorial(2) = 2 * 1 = 2
  factorial(3) = 3 * 2 = 6
  factorial(4) = 4 * 6 = 24
```

### Example: `fib(4)` (Fibonacci)

```
                        fib(4)
                       /      \
                  fib(3)      fib(2)
                 /     \      /    \
            fib(2)  fib(1) fib(1) fib(0)
            /    \
        fib(1)  fib(0)
```

Total calls for fib(4) = **9 nodes** → exponential growth → why memoization matters!

### Example: `subsets([1,2,3])` (Take / Not Take)

```
                        []
                    /        \
               [1]             []
              /   \           /   \
          [1,2]  [1]        [2]    []
          /  \   / \        / \    / \
      [1,2,3][1,2][1,3][1][2,3][2][3][]
```

Each level decides: **Take** the element (left branch) or **Not Take** (right branch).

---

## 7. Memory / Stack Representation

### The Call Stack

Each function call creates a **stack frame** containing:
- Local variables
- Parameters
- Return address (where to go after this call returns)

The stack grows **downward** in memory (or conceptually stacks up).

### Stack Visualization: `factorial(4)`

```
┌─────────────────────────────────────────────────┐
│                  CALL STACK                      │
│                                                  │
│  ┌──────────────────────────┐  ← TOP (current)  │
│  │  factorial(1)             │                   │
│  │  n = 1                   │                   │
│  │  returns 1               │                   │
│  ├──────────────────────────┤                   │
│  │  factorial(2)             │                   │
│  │  n = 2                   │                   │
│  │  waiting for factorial(1)│                   │
│  ├──────────────────────────┤                   │
│  │  factorial(3)             │                   │
│  │  n = 3                   │                   │
│  │  waiting for factorial(2)│                   │
│  ├──────────────────────────┤                   │
│  │  factorial(4)             │                   │
│  │  n = 4                   │                   │
│  │  waiting for factorial(3)│                   │
│  ├──────────────────────────┤                   │
│  │  main()                  │  ← BOTTOM         │
│  └──────────────────────────┘                   │
│                                                  │
└─────────────────────────────────────────────────┘
```

### Stack Unwinding (Return Phase)

```
Step 1: factorial(1) returns 1        → pops off stack
Step 2: factorial(2) gets 1, returns 2*1=2  → pops off
Step 3: factorial(3) gets 2, returns 3*2=6  → pops off
Step 4: factorial(4) gets 6, returns 4*6=24 → pops off
Step 5: main() receives 24
```

### Stack Memory for `printAscending(3)`

```
TIME →

PUSH:  main → print(3) → print(2) → print(1) → BASE (return)
POP:                              ← print(1) prints 1
                         ← print(2) prints 2
              ← print(3) prints 3
← main continues

Output: 1 2 3
```

### Stack Depth = Recursion Depth

For input `n`, recursion depth is:
- `factorial(n)` → depth = n
- `fib(n)` → depth = n (but exponential calls)
- `binarySearch(n)` → depth = log₂(n)

Default Java stack size ≈ 512KB–1MB → can handle ~5,000–10,000 frames typically.

---

## 8. Common Errors

### 8.1 StackOverflowError / RecursionError

**Cause**: Missing or unreachable base case → infinite recursion.

```java
// ❌ WRONG: no base case
int factorial(int n) {
    return n * factorial(n - 1); // Never stops!
}

// ✅ CORRECT
int factorial(int n) {
    if (n <= 1) return 1;        // Base case
    return n * factorial(n - 1);
}
```

**Python equivalent**: `RecursionError: maximum recursion depth exceeded`

### 8.2 Wrong Base Case (Incorrect Answer)

```java
// ❌ WRONG base case for Fibonacci
int fib(int n) {
    if (n == 0) return 0;  // Missing n==1 case!
    return fib(n-1) + fib(n-2);
}
// fib(1) → fib(0) + fib(-1) → fib(-1) + fib(-2) → infinite!
```

### 8.3 Not Making Progress Toward Base Case

```java
// ❌ WRONG: n never decreases
int sum(int n) {
    if (n == 0) return 0;
    return n + sum(n);   // Should be sum(n-1)!
}
```

### 8.4 Off-By-One in Base Case

```java
// ❌ WRONG: base case too late
int factorial(int n) {
    if (n == 0) return 1;
    return n * factorial(n - 1);
}
// Works but factorial(-1) would recurse forever if called with negative input
// ✅ BETTER: guard against bad input
int factorial(int n) {
    if (n < 0) throw new IllegalArgumentException("n must be >= 0");
    if (n <= 1) return 1;
    return n * factorial(n - 1);
}
```

### 8.5 Modifying Shared State Incorrectly

```java
// ❌ WRONG: result list shared across calls but not backtracked
List<Integer> result = new ArrayList<>();
void subsets(int[] arr, int i) {
    if (i == arr.length) {
        print(result);
        return;
    }
    result.add(arr[i]);   // Take
    subsets(arr, i + 1);
    result.remove(result.size() - 1);  // Must BACKTRACK (undo)
    subsets(arr, i + 1);   // Not take
}
// Always undo changes made before a recursive call!
```

### 8.6 Exponential Time Without Memoization

```java
// ❌ SLOW: O(2^n) calls
int fib(int n) {
    if (n <= 1) return n;
    return fib(n-1) + fib(n-2);
}

// ✅ FAST: O(n) with memoization
Map<Integer,Integer> memo = new HashMap<>();
int fib(int n) {
    if (n <= 1) return n;
    if (memo.containsKey(n)) return memo.get(n);
    int result = fib(n-1) + fib(n-2);
    memo.put(n, result);
    return result;
}
```

### Error Quick Reference Table

| Error | Cause | Fix |
|-------|-------|-----|
| `StackOverflowError` | No base case / never reached | Add/fix base case |
| Wrong answer | Incorrect base case value | Verify base returns correct result |
| Infinite loop | No progress toward base | Change argument each call |
| Duplicate results | Missing backtrack | Undo state before returning |
| TLE (Time Limit Exceeded) | Overlapping sub-problems without memo | Use memoization / DP |

---

## 9. Types of Recursion

### 9.1 Linear Recursion
One recursive call per function.
```java
int factorial(int n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1); // ONE call
}
```

### 9.2 Binary / Multiple Recursion
Two or more recursive calls per function.
```java
int fib(int n) {
    if (n <= 1) return n;
    return fib(n-1) + fib(n-2); // TWO calls
}
```

### 9.3 Tail Recursion
The recursive call is the **last operation** in the function. Some compilers/runtimes optimize this into a loop (no stack growth).
```java
int factTail(int n, int acc) {
    if (n <= 1) return acc;
    return factTail(n - 1, n * acc); // Tail call — nothing after it
}
```

### 9.4 Head Recursion
The recursive call is the **first operation** (before any work at the current level).
```java
void printAscending(int n) {
    if (n == 0) return;
    printAscending(n - 1); // Recurse FIRST
    System.out.print(n);   // Work AFTER
}
```

### 9.5 Mutual Recursion
Two functions that call each other.
```java
boolean isEven(int n) { return n == 0 || isOdd(n - 1); }
boolean isOdd(int n)  { return n != 0 && isEven(n - 1); }
```

### 9.6 Tree Recursion
Recursion that branches like a tree (multiple sub-calls at each level).
Used in: Fibonacci, subsets, permutations.

---

## 10. Recursion vs Iteration

| Aspect | Recursion | Iteration |
|--------|-----------|-----------|
| Code clarity | Often cleaner for tree/graph | Cleaner for simple loops |
| Memory | O(depth) stack space | O(1) usually |
| Speed | Slightly slower (function call overhead) | Slightly faster |
| Stack overflow risk | Yes | No |
| Infinite loop risk | Yes (missing base case) | Yes (wrong loop condition) |
| Best for | Trees, graphs, backtracking | Arrays, counting, linear traversal |
| Tail recursion | Can be optimized to loop | Already a loop |

---

## 11. Time & Space Complexity

### Time Complexity

Use the **recurrence relation** and solve it:

| Problem | Recurrence | Complexity |
|---------|-----------|------------|
| Factorial | T(n) = T(n-1) + O(1) | O(n) |
| Fibonacci (naive) | T(n) = T(n-1) + T(n-2) | O(2ⁿ) |
| Fibonacci (memo) | T(n) = O(1) per state, n states | O(n) |
| Binary Search | T(n) = T(n/2) + O(1) | O(log n) |
| Merge Sort | T(n) = 2T(n/2) + O(n) | O(n log n) |
| Subsets | T(n) = 2ⁿ subsets | O(2ⁿ) |
| Permutations | n! permutations | O(n!) |

### Space Complexity (Call Stack)

Space = O(maximum depth of recursion tree)

| Problem | Space |
|---------|-------|
| Factorial(n) | O(n) |
| Fibonacci(n) naive | O(n) — max depth = n |
| Binary Search | O(log n) |
| Merge Sort | O(n) — auxiliary arrays + O(log n) stack |
| Subsets | O(n) — depth of recursion tree |

---

## 12. Repository Structure

```
recursion-repository/
│
├── README.md                    ← You are here (Recursion theory)
├── PROBLEMS.md                  ← Problems: Fibonacci, Palindrome, Subsets, etc.
│
├── java/
│   ├── Fibonacci.java           ← Fibonacci: recursion + DP + two-pointer
│   ├── PalindromeCheck.java     ← Palindrome check using recursion
│   ├── PowerSet.java            ← All subsets via take/not-take
│   ├── SubsequenceSum.java      ← Print subsequences with sum = K
│   └── ReachTarget.java         ← Count ways to reach a target
│
└── python/
    ├── fibonacci.py             ← Fibonacci: recursion + DP + two-pointer
    ├── palindrome_check.py      ← Palindrome check using recursion
    ├── power_set.py             ← All subsets via take/not-take
    ├── subsequence_sum.py       ← Print subsequences with sum = K
    └── reach_target.py         ← Count ways to reach a target
```

---

> 💡 **Golden Rule**: Every recursive solution has two parts — *what to do at this level* and *trust the recursion to handle the rest*.
