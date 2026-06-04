# 🔙 Backtracking — Complete Masterclass

> **The art of making choices, exploring deeply, and undoing mistakes.**
> A professional, in-depth guide to Backtracking — theory, patterns, visualizations, and four classic problems with full Java + Python code.

---

## 📚 Table of Contents

1. [What is Backtracking?](#1-what-is-backtracking)
2. [How Backtracking Differs from Plain Recursion](#2-how-backtracking-differs-from-plain-recursion)
3. [The 3-Phase Backtracking Template](#3-the-3-phase-backtracking-template)
4. [Backtracking Decision Tree — Visual Model](#4-backtracking-decision-tree--visual-model)
5. [The Pruning Concept](#5-the-pruning-concept)
6. [State & Restore — The Core Mechanic](#6-state--restore--the-core-mechanic)
7. [Backtracking on a Grid — Common Patterns](#7-backtracking-on-a-grid--common-patterns)
8. [Problem 1 — Knight's Tour](#8-problem-1--knights-tour)
9. [Problem 2 — Rat in a Maze](#9-problem-2--rat-in-a-maze)
10. [Problem 3 — Word Search in 2D Grid](#10-problem-3--word-search-in-2d-grid)
11. [Problem 4 — Hamiltonian Cycle](#11-problem-4--hamiltonian-cycle)
12. [Complexity Analysis](#12-complexity-analysis)
13. [Common Errors in Backtracking](#13-common-errors-in-backtracking)
14. [Backtracking vs Dynamic Programming](#14-backtracking-vs-dynamic-programming)
15. [Repository Structure](#15-repository-structure)

---

## 1. What is Backtracking?

**Backtracking** is a refined recursive strategy where you:

1. **Make a choice** (place a queen, move a knight, pick a direction)
2. **Explore** all consequences of that choice recursively
3. **Undo the choice** (backtrack) if it leads to a dead end
4. **Try the next alternative**

> 💡 **Analogy — The Maze Walker:**
> Imagine you're inside a maze. You pick a corridor, walk forward, and hit a wall. You *retrace your steps* back to the last junction and try a different corridor. You keep doing this until you either find the exit or exhaust every path.
> That "retrace your steps" moment is the **backtrack**.

> 💡 **Analogy — The Lock Combination:**
> You're cracking a 3-digit combination lock. You try `1-1-1`, it fails. You increment to `1-1-2`, fails. Eventually `7-3-9` works. You're systematically exploring all combinations and abandoning sequences the moment they're known to be wrong.

### Formal Definition

```
Backtracking is a depth-first search (DFS) over a decision tree where:
  - Each node = a partial solution (a state)
  - Each edge = a choice made
  - Leaf nodes = either complete solutions or dead ends
  - When a dead end is reached → undo the last choice → try the next branch
```

---

## 2. How Backtracking Differs from Plain Recursion

| Aspect | Plain Recursion | Backtracking |
|---|---|---|
| **Purpose** | Solve by breaking into subproblems | Explore all possibilities; find valid ones |
| **State** | Typically read-only (passed as args) | Mutable shared state that gets modified & restored |
| **Undo Step** | Not present | **Core mechanic** — always undoes choices |
| **Dead Ends** | Not applicable | Explicitly detected and pruned |
| **Result** | Single computed value | One or all valid configurations |
| **Example** | `factorial(n)`, `fibonacci(n)` | N-Queens, Knight's Tour, Sudoku |

### Code Shape Comparison

```java
// ── PLAIN RECURSION ──────────────────────────────────────────
int factorial(int n) {
    if (n == 0) return 1;           // base case
    return n * factorial(n - 1);    // sub-call, no undo needed
}

// ── BACKTRACKING ─────────────────────────────────────────────
void solve(State state) {
    if (isSolution(state)) {
        recordSolution(state);       // found a valid answer
        return;
    }
    for (Choice c : getPossibleChoices(state)) {
        if (isValid(state, c)) {
            applyChoice(state, c);   // ① MAKE choice
            solve(state);            // ② EXPLORE
            undoChoice(state, c);    // ③ UN-MAKE choice (BACKTRACK)
        }
    }
}
```

---

## 3. The 3-Phase Backtracking Template

Every backtracking problem follows this exact structure — memorize it:

```
┌─────────────────────────────────────────────────────────────┐
│                  BACKTRACKING TEMPLATE                       │
│                                                              │
│  function backtrack(state, choices):                         │
│                                                              │
│    ┌─ PHASE 0: BASE CASE ──────────────────────────────┐    │
│    │  if state is a complete/valid solution:            │    │
│    │      record or return the solution                 │    │
│    │      return                                        │    │
│    └───────────────────────────────────────────────────┘    │
│                                                              │
│    for each choice in available choices:                     │
│                                                              │
│      ┌─ PHASE 1: CHOOSE ─────────────────────────────┐      │
│      │  if choice is valid / safe:                    │      │
│      │      apply choice to state                     │      │
│      └───────────────────────────────────────────────┘      │
│                                                              │
│      ┌─ PHASE 2: EXPLORE ────────────────────────────┐      │
│      │      backtrack(updated state, remaining)       │      │
│      └───────────────────────────────────────────────┘      │
│                                                              │
│      ┌─ PHASE 3: UN-CHOOSE (BACKTRACK) ──────────────┐      │
│      │      undo the choice from state               │      │
│      └───────────────────────────────────────────────┘      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Java Template

```java
void backtrack(int[][] board, int row, List<List<Integer>> results) {

    // ── PHASE 0: Base Case ──────────────────────────────────
    if (row == board.length) {
        results.add(extractSolution(board)); // complete solution found
        return;
    }

    // ── Try every possible choice for this row ──────────────
    for (int col = 0; col < board.length; col++) {

        // ── PHASE 1: CHOOSE ─────────────────────────────────
        if (isSafe(board, row, col)) {
            board[row][col] = 1;             // make the choice

            // ── PHASE 2: EXPLORE ────────────────────────────
            backtrack(board, row + 1, results); // recurse deeper

            // ── PHASE 3: UN-CHOOSE ──────────────────────────
            board[row][col] = 0;             // undo the choice (backtrack)
        }
    }
}
```

### Python Template

```python
def backtrack(state, choices, results):

    # ── PHASE 0: Base Case ─────────────────────────────────
    if is_complete(state):
        results.append(copy_of(state))
        return

    for choice in choices:

        # ── PHASE 1: CHOOSE ────────────────────────────────
        if is_valid(state, choice):
            apply(state, choice)          # make choice

            # ── PHASE 2: EXPLORE ───────────────────────────
            backtrack(state, choices, results)

            # ── PHASE 3: UN-CHOOSE ─────────────────────────
            undo(state, choice)           # backtrack
```

---

## 4. Backtracking Decision Tree — Visual Model

Every backtracking execution can be visualized as a tree where:

- **Root** = initial empty state
- **Internal nodes** = partial states (some choices made)
- **Edges** = choices made at each step
- **Green leaves** = valid complete solutions ✅
- **Red leaves** = dead ends (invalid / pruned) ❌

### Example — Choose 2 items from {A, B, C} without repetition

```
                        START []
                     /     |     \
                 [A]       [B]     [C]
                /   \      |  \    |  \
            [A,B] [A,C] [B,A][B,C][C,A][C,B]
              ✅    ✅    ✅   ✅   ✅   ✅

All 6 permutations found. No pruning needed here.
```

### With a Constraint — "no adjacent same element"

```
                        START []
                     /     |     \
                 [A]       [B]     [C]
                /   \      |  \    |  \
            [A,B] [A,C] [B,A][B,C][C,A][C,B]
            ✅(OK) ✅   ✅   ✅   ✅   ✅

If constraint was "B cannot follow A":
            [A,B]  ← ❌ PRUNED immediately  ← doesn't explore further
            [A,C]  ← ✅ explore
```

---

## 5. The Pruning Concept

**Pruning** = cutting off branches of the decision tree early when you know they **cannot** lead to a valid solution.

Without pruning → explore every possible path → exponential time.
With pruning → skip invalid branches early → dramatically faster in practice.

```
WITHOUT PRUNING (Brute Force):
  Try all 8! = 40,320 arrangements of 8 queens
  Check each one at the end

WITH PRUNING (Backtracking):
  At row 0: place queen in column 0
  At row 1: columns 0,1,2 immediately conflict → skip them
            try column 3 → safe → continue
  At row 2: only valid columns explored
  → typically visits ~2000 nodes instead of 40,320
```

### Pruning Rule Structure

```java
if (!isValid(state, choice)) {
    continue;   // ← PRUNE: don't explore this subtree at all
}
// Only reach here if the choice is promising
applyChoice(state, choice);
backtrack(...);
undoChoice(state, choice);
```

---

## 6. State & Restore — The Core Mechanic

The most critical and often misunderstood part of backtracking is **properly restoring state** after a recursive call returns.

### What "State" Means

State is everything that describes the current configuration:

| Problem | State |
|---|---|
| Knight's Tour | The board (which cells visited), current position |
| Rat in a Maze | The solution path matrix, current (row, col) |
| Word Search | The `visited[][]` boolean grid |
| Hamiltonian Cycle | The `path[]` array, current vertex index |

### The Symmetry Rule

```
Every modification made BEFORE the recursive call
MUST be exactly undone AFTER the recursive call.
```

```java
// ✅ Correct — symmetric modify/restore
board[row][col] = VISITED;            // modify
backtrack(board, nextRow, nextCol);
board[row][col] = UNVISITED;          // restore (undo exactly)

// ❌ Wrong — restoring to wrong value
board[row][col] = VISITED;
backtrack(...);
board[row][col] = -1;                 // Bug! Was it 0 before? Store original!

// ✅ Correct when original value matters
int original = board[row][col];
board[row][col] = VISITED;
backtrack(...);
board[row][col] = original;           // restore to whatever it was
```

---

## 7. Backtracking on a Grid — Common Patterns

Many backtracking problems involve a 2D grid. These share common mechanics:

### Direction Arrays

```java
// 4-directional movement (up, down, left, right)
int[] dr = {-1, +1,  0,  0};
int[] dc = { 0,  0, -1, +1};

// 8-directional movement (includes diagonals)
int[] dr = {-1, -1, -1,  0,  0, +1, +1, +1};
int[] dc = {-1,  0, +1, -1, +1, -1,  0, +1};

// Knight moves (L-shapes)
int[] dr = {-2, -2, -1, -1, +1, +1, +2, +2};
int[] dc = {-1, +1, -2, +2, -2, +2, -1, +1};
```

### Boundary and Validity Check

```java
boolean isValid(int row, int col, int N) {
    return row >= 0 && row < N && col >= 0 && col < N;
}
```

### Visited Tracking Patterns

```java
// Pattern A — boolean visited matrix
boolean[][] visited = new boolean[N][N];
visited[row][col] = true;      // mark
backtrack(...);
visited[row][col] = false;     // unmark (backtrack)

// Pattern B — mark directly on the board
char original = board[row][col];
board[row][col] = '#';         // mark as visited
backtrack(...);
board[row][col] = original;    // restore

// Pattern C — integer step counter
board[row][col] = stepNumber;  // mark with move number
backtrack(...);
board[row][col] = -1;          // unmark
```

---

## 8. Problem 1 — Knight's Tour

### Problem Statement

> Place a chess knight on an N×N board. Move it according to chess rules (L-shaped moves). Find a sequence of moves such that the knight **visits every square exactly once**.

A valid 5×5 Knight's Tour might look like:

```
 1  16  11  6  21
10   5  20 15  22
17  12   7  2  23    ← numbers show the order of moves
 4   9  24 19  14
13  18   3  8  25
```

### Knight's 8 Possible Moves

```
         ┌─────────────────────┐
         │   . K . K .         │
         │   K . . . K         │  K = possible knight destinations
         │   . . ♞ . .         │  ♞ = knight's current position
         │   K . . . K         │
         │   . K . K .         │
         └─────────────────────┘

Move offsets (row, col):
  (-2,-1), (-2,+1)   ← 2 up, 1 side
  (-1,-2), (-1,+2)   ← 1 up, 2 side
  (+1,-2), (+1,+2)   ← 1 down, 2 side
  (+2,-1), (+2,+1)   ← 2 down, 1 side
```

### Backtracking Approach

```
State:   board[N][N] — stores move number (1 to N²) at each cell, -1 if unvisited
         current (row, col), moveCount

Base Case:   moveCount == N*N + 1  → All squares visited → Solution found! ✅

Choice:      Try all 8 knight moves from current position

Valid Move:  New position is within bounds AND not yet visited (board[r][c] == -1)

Make:        board[newRow][newCol] = moveCount
Explore:     solve(board, newRow, newCol, moveCount + 1)
Undo:        board[newRow][newCol] = -1
```

### Step-by-Step Trace (3×3 board, start at (0,0))

```
Initial state:
  [1][-1][-1]
  [-1][-1][-1]     Move 1 placed at (0,0)
  [-1][-1][-1]

Try move 1 from (0,0): destination options
  (-2,-1) → (-2,-1) ✗ out of bounds
  (-2,+1) → (-2, 1) ✗ out of bounds
  (-1,-2) → (-1,-2) ✗ out of bounds
  (-1,+2) → (-1, 2) ✗ out of bounds
  (+1,-2) → ( 1,-2) ✗ out of bounds
  (+1,+2) → ( 1, 2) ✓ valid!
  (+2,-1) → ( 2,-1) ✗ out of bounds
  (+2,+1) → ( 2, 1) ✓ valid!

Try (1,2):
  [1][-1][-1]
  [-1][-1][2]      Move 2 placed at (1,2)
  [-1][-1][-1]

  From (1,2) try moves...
  Some lead to dead ends → BACKTRACK → board[1][2] = -1

Try (2,1):
  [1][-1][-1]
  [-1][-1][-1]
  [-1][2][-1]      Move 2 placed at (2,1)

  Continue exploring... eventually finds a tour OR backtracks
```

### Warnsdorff's Heuristic (Optimization)

Instead of trying moves in fixed order, always **move to the square with the fewest onward moves** next. This dramatically reduces backtracking on large boards.

```java
// Sort candidate moves by number of their own future moves (ascending)
moves.sort((a, b) -> getDegree(board, a) - getDegree(board, b));
```

### Recursion Tree Shape

```
               solve(board, 0, 0, 1)
              /     |      |     \
           (1,2)  (2,1)  ...    (dead end)
           /  \    /  \
         ...  ...  ...  ...
         ↓
    [Complete tour found]  OR  [All paths exhausted → no solution]
```

### Complexity

```
Worst Case Time:  O(8^(N²))  — 8 choices at each of N² squares
With Warnsdorff: ~O(N²)      — almost linear in practice
Space:           O(N²)       — the board itself
```

**Full Code:** → [`KnightsTour.java`](./KnightsTour.java) | [`knights_tour.py`](./knights_tour.py)

---

## 9. Problem 2 — Rat in a Maze

### Problem Statement

> A rat starts at the **top-left corner** (0,0) of an N×N grid.  
> The goal is to reach the **bottom-right corner** (N-1, N-1).  
> Cells marked `1` are open paths. Cells marked `0` are walls/obstacles.  
> Find **all paths** the rat can take.

### Example Maze

```
Input Maze (1 = open, 0 = wall):
  1  0  0  0
  1  1  0  1
  0  1  0  0
  1  1  1  1

One valid path (marked with *):
  *  0  0  0
  *  *  0  1
  0  *  0  0
  1  *  *  *

Path as directions: D D R D R R D  (Down, Right)
```

### Backtracking Approach

```
State:   solution[N][N] — marks cells in the current path
         current (row, col)

Base Case:   (row, col) == (N-1, N-1) → Reached destination! → Print path ✅

Choices:     Move in 4 directions: Down (D), Up (U), Right (R), Left (L)
             (Some problems allow only Down and Right)

Valid Move:
  1. New position is within bounds
  2. maze[newRow][newCol] == 1    (not a wall)
  3. solution[newRow][newCol] == 0  (not already on our current path)

Make:    solution[newRow][newCol] = 1
Explore: solve(maze, solution, newRow, newCol)
Undo:    solution[newRow][newCol] = 0
```

### Recursion Tree for 3×3 maze

```
Maze:          Solution path tracking:
  1 0 0          _ _ _
  1 1 0          _ _ _
  0 1 1          _ _ _

solve(0,0)  → mark solution[0][0]=1
  try D → (1,0): maze[1][0]=1, not visited ✓
    solve(1,0) → mark solution[1][0]=1
      try D → (2,0): maze[2][0]=0 ✗ WALL, skip
      try U → (0,0): solution[0][0]=1 ✗ ALREADY VISITED, skip
      try R → (1,1): maze[1][1]=1, not visited ✓
        solve(1,1) → mark solution[1][1]=1
          try D → (2,1): maze[2][1]=1, not visited ✓
            solve(2,1) → mark solution[2][1]=1
              try R → (2,2): maze[2][2]=1, not visited ✓
                solve(2,2): row==N-1, col==N-1 → SOLUTION FOUND! ✅
                  Print: (0,0)→(1,0)→(1,1)→(2,1)→(2,2)
              undo solution[2][2]=0
              ... try other directions → dead ends
            undo solution[2][1]=0
          ... try other directions
        undo solution[1][1]=0
      try L → (1,-1) ✗ out of bounds
    undo solution[1][0]=0
  try R → (0,1): maze[0][1]=0 ✗ WALL, skip
  try U → (-1,0) ✗ out of bounds
  try L → (0,-1) ✗ out of bounds
```

### Stack Memory During Execution

```
╔═══════════════════════════════════════╗
║  solve(0,0) — path so far: [(0,0)]    ║  ← frame 1
╠═══════════════════════════════════════╣
║  solve(1,0) — path so far: [(0,0),(1,0)] ║  ← frame 2
╠═══════════════════════════════════════╣
║  solve(1,1) — path: [(0,0),(1,0),(1,1)] ║  ← frame 3
╠═══════════════════════════════════════╣
║  solve(2,1) — path: [...,(2,1)]        ║  ← frame 4
╠═══════════════════════════════════════╣
║  solve(2,2) → SOLUTION! ✅             ║  ← frame 5
╚═══════════════════════════════════════╝

Max depth = O(N²) in the worst case
```

### Complexity

```
Time:  O(4^(N²)) worst case (4 choices at each of N² cells)
       With obstacles and pruning → much less in practice
Space: O(N²) — solution matrix + recursion stack depth
```

**Full Code:** → [`RatInMaze.java`](./RatInMaze.java) | [`rat_in_maze.py`](./rat_in_maze.py)

---

## 10. Problem 3 — Word Search in 2D Grid

### Problem Statement

> Given a 2D grid of characters and a target word, determine if the word **exists in the grid**.  
> You may move **up, down, left, or right** from any cell.  
> You **cannot reuse the same cell** within one word path.

### Example

```
Grid:
  A  B  C  E
  S  F  C  S
  A  D  E  E

Word to find: "ABCCED"

Path found:
  A → B → C → C → E → D
  (0,0)→(0,1)→(0,2)→(1,2)→(2,2)→(2,1)  ✅
```

### Backtracking Approach

```
State:   visited[N][M] — tracks which cells are currently in our path
         current (row, col), index into target word

Base Case:   index == word.length() → Matched all characters! → return true ✅

Pruning:
  1. (row, col) out of bounds → return false
  2. visited[row][col] == true → already used → return false
  3. grid[row][col] != word[index] → character mismatch → return false

Make:    visited[row][col] = true
Explore: try all 4 directions for the next character (index + 1)
Undo:    visited[row][col] = false
```

### Recursion Tree for finding "SEE"

```
Grid:
  A  B  C  E
  S  F  C  S
  A  D  E  E

search("SEE", 0) — find 'S':
  Try every cell where grid[r][c] == 'S':
    (1,0) = 'S' ✓
    (1,3) = 'S' ✓

  From (1,0), find 'E' (index=1):
    Up    (0,0)='A' ✗
    Down  (2,0)='A' ✗
    Left  (1,-1) ✗ out of bounds
    Right (1,1)='F' ✗
    → All directions fail → BACKTRACK from (1,0)

  From (1,3), find 'E' (index=1):
    Up    (0,3)='E' ✓ → mark visited (0,3)
      From (0,3), find 'E' (index=2):
        Up    (-1,3) ✗ bounds
        Down  (1,3)='S'? But visited[1][3]=false and 'S'≠'E' ✗
        Left  (0,2)='C' ✗
        Right (0,4) ✗ bounds
      → Fail → BACKTRACK, unmark (0,3)
    Down  (2,3)='E' ✓ → mark visited (2,3)
      From (2,3), find 'E' (index=2):
        Up    (1,3) visited? No. 'S'≠'E' ✗
        Down  (3,3) ✗ bounds
        Left  (2,2)='E' ✓ → return TRUE! ✅
        
WORD "SEE" FOUND: (1,3) → (2,3) → (2,2)  ✅
```

### Key Insight — Why We Must Unmark Visited

```
Grid:       Word: "AAB"
  A A
  A B

From (0,0):
  Mark (0,0) visited
  Move right to (0,1): 'A' matches index 1
    Mark (0,1) visited
    Move down to (1,1): 'B' matches index 2 → FOUND ✅
    Unmark (0,1)
  Unmark (0,0)

If we didn't unmark → other starting paths would see (0,0)
as "already used" even though we've backtracked away from it.
```

### Complexity

```
Time:  O(N × M × 4^L)  where L = word length
       Start from each of N×M cells, 4 direction branches, depth L
Space: O(L)  — recursion depth equals word length
              + O(N×M) for visited array
```

**Full Code:** → [`WordSearch.java`](./WordSearch.java) | [`word_search.py`](./word_search.py)

---

## 11. Problem 4 — Hamiltonian Cycle

### Problem Statement

> Given an undirected graph with N vertices, determine if there exists a **Hamiltonian Cycle** — a path that:
> - Visits **every vertex exactly once**
> - **Returns to the starting vertex** at the end

### Example Graph

```
Vertices: 0, 1, 2, 3, 4
Edges:
  0 — 1
  0 — 3
  1 — 2
  1 — 3
  1 — 4
  2 — 4
  3 — 4

Adjacency Matrix:
     0  1  2  3  4
  0 [0, 1, 0, 1, 0]
  1 [1, 0, 1, 1, 1]
  2 [0, 1, 0, 0, 1]
  3 [1, 1, 0, 0, 1]
  4 [0, 1, 1, 1, 0]

Hamiltonian Cycle:
  0 → 1 → 2 → 4 → 3 → 0  ✅
```

### Hamiltonian Cycle Visualization

```
        0
       / \
      1   3
     /|\ / \
    2 | 4   |
     \|/    |
      +------+

Cycle: 0 → 1 → 2 → 4 → 3 → 0

   0 ──── 1
   |    / | \
   |   /  |  \
   3──+   2   4
   |           |
   └───────────┘
```

### Backtracking Approach

```
State:   path[N] — stores the vertex visited at each step
         positionIndex — how many vertices placed so far

Base Case:   positionIndex == N  (all vertices placed)
             AND graph[path[N-1]][path[0]] == 1  (last vertex connects back to start)
             → Hamiltonian Cycle found! ✅

Choice:      For position positionIndex, try each vertex v from 1 to N-1

Valid Vertex:
  1. graph[path[positionIndex - 1]][v] == 1  (edge exists from last placed vertex)
  2. v has not already been placed in path[]

Make:    path[positionIndex] = v
Explore: solveHamiltonian(graph, path, positionIndex + 1)
Undo:    path[positionIndex] = -1
```

### Step-by-Step Trace

```
Initial: path = [0, -1, -1, -1, -1]  (always start at vertex 0)

Position 1 — try vertices adjacent to 0:
  0→1: graph[0][1]=1 ✓, 1 not in path ✓
    path = [0, 1, -1, -1, -1]
    Position 2 — try vertices adjacent to 1:
      1→0: graph[1][0]=1 ✓, BUT 0 already in path ✗
      1→2: graph[1][2]=1 ✓, 2 not in path ✓
        path = [0, 1, 2, -1, -1]
        Position 3 — try vertices adjacent to 2:
          2→4: graph[2][4]=1 ✓, 4 not in path ✓
            path = [0, 1, 2, 4, -1]
            Position 4 — try vertices adjacent to 4:
              4→3: graph[4][3]=1 ✓, 3 not in path ✓
                path = [0, 1, 2, 4, 3]
                Position 5 == N → Check: graph[3][0]=1 ✓
                → HAMILTONIAN CYCLE FOUND! ✅
                → 0 → 1 → 2 → 4 → 3 → 0
```

### Stack Memory During Trace

```
╔═══════════════════════════════════════════════╗
║  hamiltonianCycle(graph, path=[0,-,-,-,-], 1) ║
╠═══════════════════════════════════════════════╣
║  hamiltonianCycle(graph, path=[0,1,-,-,-], 2) ║
╠═══════════════════════════════════════════════╣
║  hamiltonianCycle(graph, path=[0,1,2,-,-], 3) ║
╠═══════════════════════════════════════════════╣
║  hamiltonianCycle(graph, path=[0,1,2,4,-], 4) ║
╠═══════════════════════════════════════════════╣
║  hamiltonianCycle(path=[0,1,2,4,3], pos=5)    ║  ← SOLUTION! ✅
╚═══════════════════════════════════════════════╝
```

### Why Hamiltonian Cycle is Hard (NP-Complete)

```
For N vertices, naive backtracking tries up to (N-1)! permutations.
  N=10  →  9!   = 362,880  paths
  N=15  →  14!  = 87 billion paths  ← impractical without pruning
  N=20  →  19!  = astronomical

This is why no polynomial-time algorithm is known.
Backtracking with good pruning is the practical approach for small N.
```

### Complexity

```
Time:  O((N-1)!)  worst case  (try every permutation of vertices)
       With pruning: much less in practice
Space: O(N)  — path array + recursion depth
```

**Full Code:** → [`HamiltonianCycle.java`](./HamiltonianCycle.java) | [`hamiltonian_cycle.py`](./hamiltonian_cycle.py)

---

## 12. Complexity Analysis

### Summary Table

| Problem | Time Complexity | Space Complexity | Notes |
|---|---|---|---|
| **Knight's Tour** | O(8^(N²)) worst | O(N²) | Warnsdorff heuristic makes it near O(N²) |
| **Rat in a Maze** | O(4^(N²)) worst | O(N²) | Obstacles reduce this greatly |
| **Word Search** | O(N×M×4^L) | O(L) + O(N×M) | L = word length |
| **Hamiltonian Cycle** | O((N-1)!) worst | O(N) | NP-Complete problem |

### Understanding "Worst Case" vs "Practical"

```
Backtracking worst case = no pruning at all (brute force)
Backtracking actual = pruning eliminates most branches early

For well-constrained problems (dense grids, many obstacles):
  Actual running time ≈ 1% to 5% of worst case
```

---

## 13. Common Errors in Backtracking

### Error 1 — Forgetting to Undo (Most Common Bug!)

```java
// ❌ WRONG — missing the undo step
void solve(boolean[][] visited, int row, int col) {
    visited[row][col] = true;
    for (int[] dir : directions) {
        int nr = row + dir[0], nc = col + dir[1];
        if (isValid(nr, nc) && !visited[nr][nc]) {
            solve(visited, nr, nc);
        }
    }
    // ← BUG: never sets visited[row][col] = false!
    // All paths after the first will see cells incorrectly marked
}

// ✅ CORRECT
void solve(boolean[][] visited, int row, int col) {
    visited[row][col] = true;
    for (int[] dir : directions) {
        int nr = row + dir[0], nc = col + dir[1];
        if (isValid(nr, nc) && !visited[nr][nc]) {
            solve(visited, nr, nc);
        }
    }
    visited[row][col] = false;  // ← BACKTRACK: restore state
}
```

---

### Error 2 — Not Making a Deep Copy of the Solution

```java
// ❌ WRONG — adding reference, not a copy
void solve(List<Integer> current, List<List<Integer>> result) {
    if (isComplete(current)) {
        result.add(current);    // BUG! current will be modified later
        return;
    }
    // ...
}
// All entries in result will show the same (empty) list at the end!

// ✅ CORRECT
void solve(List<Integer> current, List<List<Integer>> result) {
    if (isComplete(current)) {
        result.add(new ArrayList<>(current));  // Add a COPY
        return;
    }
    // ...
}
```

---

### Error 3 — Checking Validity AFTER Making the Move

```java
// ❌ WRONG — validity check too late
board[row][col] = knight;                  // made the move
if (isValid(row, col)) {                   // check AFTER modifying
    backtrack(board, row, col, moveCount);
}
board[row][col] = -1;

// ✅ CORRECT — validity check BEFORE making the move
if (isValid(row, col) && board[row][col] == -1) {  // check first
    board[row][col] = moveCount;                    // then move
    backtrack(board, row, col, moveCount + 1);
    board[row][col] = -1;                           // undo
}
```

---

### Error 4 — Modifying the Wrong Variable

```java
// ❌ WRONG — undoing a different variable than what was modified
path[pos] = vertex;
backtrack(graph, path, pos + 1);
path[pos + 1] = -1;    // BUG: should be path[pos], not path[pos+1]

// ✅ CORRECT
path[pos] = vertex;
backtrack(graph, path, pos + 1);
path[pos] = -1;        // undo the EXACT modification made above
```

---

### Error 5 — Off-by-One in Base Case

```java
// ❌ WRONG — base case checks pos == N but N positions are 0..N-1
// so pos == N means we've filled all N positions correctly
// (this one is actually correct — but easy to confuse)

// The confusion: if path is 0-indexed and has N slots (0 to N-1),
// then pos == N means "past the last slot" = all filled = done ✅
// Don't accidentally use pos == N-1 (that only fills N-1 slots)
```

---

### Error Summary

| Error | Symptom | Fix |
|---|---|---|
| Missing undo/restore | Wrong results, paths bleed into each other | Always pair every `mark` with an `unmark` |
| Reference vs copy | All results show same (often empty) list | Use `new ArrayList<>(current)` |
| Validate after modify | Invalid states get explored | Check `isValid` before modifying state |
| Wrong undo target | Inconsistent state corrupts future paths | Match undo to the exact variable modified |
| Off-by-one in base | Misses solutions or crashes | Test base case manually with n=1 input |

---

## 14. Backtracking vs Dynamic Programming

Both explore subproblems, but their purpose and mechanics differ fundamentally:

| Aspect | Backtracking | Dynamic Programming |
|---|---|---|
| **Goal** | Find valid configurations | Optimize a value (min/max/count) |
| **Overlapping Subproblems** | Usually not present | Core requirement |
| **State Reuse** | No — each path is independent | Yes — cache and reuse |
| **Undo Step** | Essential | Not applicable |
| **Result** | All/any valid solutions | Optimal value or count |
| **Approach** | DFS over decision tree | Fill table / memoize |
| **Example** | N-Queens, Word Search | Longest Common Subsequence |

### The Relationship

```
Some problems can use EITHER approach:

Subset Sum with exact match:
  ─ Backtracking: print all subsets with sum=K  (explores every path)
  ─ DP:           count/check if ANY subset sums to K  (caches results)

Fibonacci:
  ─ Backtracking: N/A (no choices to make)
  ─ Recursion + Memoization (DP): cache fib(n) values

Rule of thumb:
  If you need ALL solutions or to EXPLORE all paths → Backtracking
  If you need optimal/count and have overlapping subproblems → DP
```

---

## 15. Repository Structure

```
recursion-masterclass/
│
├── README.md                        ← Core recursion guide
├── PROBLEMS.md                      ← Fibonacci, Palindrome, PowerSet, Subsequences
│
├── backtracking/
│   ├── README.md                    ← You are here (Full Backtracking Guide)
│   ├── KnightsTour.java             ← Knight's Tour — Java
│   ├── knights_tour.py              ← Knight's Tour — Python
│   ├── RatInMaze.java               ← Rat in a Maze — Java
│   ├── rat_in_maze.py               ← Rat in a Maze — Python
│   ├── WordSearch.java              ← Word Search — Java
│   ├── word_search.py               ← Word Search — Python
│   ├── HamiltonianCycle.java        ← Hamiltonian Cycle — Java
│   └── hamiltonian_cycle.py         ← Hamiltonian Cycle — Python
│
├── java/
│   ├── basics/
│   ├── fibonacci/
│   ├── palindrome/
│   ├── powerset/
│   └── subsequences/
│
└── python/
    ├── basics/
    ├── fibonacci/
    ├── palindrome/
    ├── powerset/
    └── subsequences/
```

---

## 🔗 Quick Reference Card

```
BACKTRACKING CHECKLIST ✅

Before writing code:
  □ What is the STATE? (What data changes during recursion?)
  □ What are the CHOICES at each step?
  □ What makes a choice VALID? (Pruning condition)
  □ What is the BASE CASE? (When am I done?)

In the code:
  □ MAKE the choice (modify state)
  □ RECURSE (explore this choice's consequences)
  □ UNDO the choice (restore state exactly)

Debugging:
  □ Are results wrong? → Probably missing the undo step
  □ Getting same result repeatedly? → Probably storing reference, not copy
  □ Stack overflow? → Base case not reached, or state not reducing
  □ Missing some solutions? → Pruning condition too aggressive
```

---

*Part of the **Recursion Masterclass** repository. See [README.md](../README.md) for the complete index.*
