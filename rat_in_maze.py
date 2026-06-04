"""
============================================================
 RAT IN A MAZE — Python
============================================================
 Find ALL paths for a rat from (0,0) to (N-1, N-1).
 1 = open path, 0 = wall.
 Allowed moves: Down (D), Up (U), Right (R), Left (L).

 Approach : Backtracking
 Time     : O(4^(N²)) worst case
 Space    : O(N²)
============================================================
"""
from typing import List


# Direction names and their (row_delta, col_delta) pairs
DIRECTIONS = [
    ('D', +1,  0),
    ('L',  0, -1),
    ('R',  0, +1),
    ('U', -1,  0),
]


def find_all_paths(maze: List[List[int]]) -> List[str]:
    """
    Returns a list of direction strings for every valid path
    from (0,0) to (N-1, N-1) in the maze.

    Parameters
    ----------
    maze : N×N grid  (1=open, 0=wall)
    """
    n = len(maze)
    results: List[str] = []
    visited = [[False] * n for _ in range(n)]

    if maze[0][0] == 1:
        visited[0][0] = True
        _backtrack(maze, visited, 0, 0, n, [], results)

    return results


def _backtrack(maze: list, visited: list,
               row: int, col: int, n: int,
               path: list, results: list) -> None:
    """
    Core backtracking function.

    Parameters
    ----------
    maze    : The maze grid
    visited : Tracks cells on the current path
    row,col : Current rat position
    n       : Maze dimension
    path    : List of direction characters taken so far
    results : Accumulates valid path strings
    """

    # ── BASE CASE ────────────────────────────────────────────
    # Reached the destination
    if row == n - 1 and col == n - 1:
        results.append("".join(path))
        return

    # ── TRY ALL 4 DIRECTIONS ─────────────────────────────────
    for name, dr, dc in DIRECTIONS:
        next_row, next_col = row + dr, col + dc

        # ── PHASE 1: CHOOSE ──────────────────────────────────
        if _is_safe(maze, visited, next_row, next_col, n):

            visited[next_row][next_col] = True   # mark
            path.append(name)                    # record move

            # ── PHASE 2: EXPLORE ─────────────────────────────
            _backtrack(maze, visited, next_row, next_col, n, path, results)

            # ── PHASE 3: BACKTRACK ───────────────────────────
            visited[next_row][next_col] = False  # un-mark
            path.pop()                           # remove last move


def _is_safe(maze, visited, row, col, n) -> bool:
    """True if (row,col) is within bounds, open, and unvisited."""
    return (0 <= row < n and 0 <= col < n
            and maze[row][col] == 1
            and not visited[row][col])


def _print_results(maze: list, paths: List[str]) -> None:
    n = len(maze)
    print(f"\n  Maze ({n}×{n})  [S=start, E=end, 1=open, 0=wall]:")
    for r in range(n):
        print("  ", end="")
        for c in range(n):
            if r == 0 and c == 0:
                print("S", end=" ")
            elif r == n - 1 and c == n - 1:
                print("E", end=" ")
            else:
                print(maze[r][c], end=" ")
        print()
    print(f"\n  Paths found: {len(paths)}")
    for i, p in enumerate(paths, 1):
        print(f"  Path {i}: {p}")


# ── MAIN ─────────────────────────────────────────────────────
if __name__ == "__main__":
    print("╔══════════════════════════════════╗")
    print("║          Rat in a Maze           ║")
    print("╚══════════════════════════════════╝")

    maze1 = [
        [1, 0, 0, 0],
        [1, 1, 0, 1],
        [1, 1, 0, 0],
        [0, 1, 1, 1]
    ]
    paths1 = find_all_paths(maze1)
    _print_results(maze1, paths1)

    print()

    maze2 = [[1]*4 for _ in range(4)]
    paths2 = find_all_paths(maze2)
    print(f"\n  Open 4×4 maze — total paths: {len(paths2)}")
