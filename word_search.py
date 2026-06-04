"""
============================================================
 WORD SEARCH IN A 2D GRID — Python
============================================================
 Given a 2D character grid and a target word, find whether
 the word exists by moving UP / DOWN / LEFT / RIGHT,
 without reusing any cell in a single path.

 Approach : Backtracking
 Time     : O(N × M × 4^L)  L = word length
 Space    : O(L + N×M)
============================================================
"""
from typing import List, Optional, Tuple


# 4-directional movement (row_delta, col_delta)
DIRECTIONS = [(-1, 0), (+1, 0), (0, -1), (0, +1)]


def exist(grid: List[List[str]], word: str) -> bool:
    """
    Returns True if 'word' exists anywhere in 'grid'.

    Parameters
    ----------
    grid : 2D character board
    word : Target word to search for
    """
    rows, cols = len(grid), len(grid[0])
    visited = [[False] * cols for _ in range(rows)]

    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == word[0]:          # potential starting cell
                if _backtrack(grid, visited, r, c, word, 0):
                    return True
    return False


def _backtrack(grid: list, visited: list,
               row: int, col: int,
               word: str, index: int) -> bool:
    """
    Tries to match word[index:] starting from cell (row, col).

    Parameters
    ----------
    grid    : Character grid
    visited : Cells on the current path
    row,col : Current cell
    word    : Target word
    index   : Which character we're trying to match now
    """

    # ── BASE CASE ────────────────────────────────────────────
    if index == len(word):
        return True   # all characters matched!

    # ── PRUNING ──────────────────────────────────────────────
    rows, cols = len(grid), len(grid[0])
    if row < 0 or row >= rows:    return False   # out of bounds
    if col < 0 or col >= cols:    return False   # out of bounds
    if visited[row][col]:         return False   # already on path
    if grid[row][col] != word[index]: return False   # char mismatch

    # ── PHASE 1: CHOOSE ──────────────────────────────────────
    visited[row][col] = True   # mark cell as used

    # ── PHASE 2: EXPLORE ─────────────────────────────────────
    for dr, dc in DIRECTIONS:
        if _backtrack(grid, visited, row + dr, col + dc, word, index + 1):
            visited[row][col] = False   # restore before returning
            return True

    # ── PHASE 3: BACKTRACK ───────────────────────────────────
    visited[row][col] = False   # un-mark
    return False


def find_path(grid: List[List[str]],
              word: str) -> Optional[List[Tuple[int, int]]]:
    """
    Returns the list of (row, col) coordinates for the first
    occurrence of 'word' in the grid, or None if not found.
    """
    rows, cols = len(grid), len(grid[0])
    visited = [[False] * cols for _ in range(rows)]
    path: List[Tuple[int, int]] = []

    def dfs(r, c, idx):
        if idx == len(word): return True
        if r < 0 or r >= rows or c < 0 or c >= cols: return False
        if visited[r][c]: return False
        if grid[r][c] != word[idx]: return False

        visited[r][c] = True
        path.append((r, c))

        for dr, dc in DIRECTIONS:
            if dfs(r + dr, c + dc, idx + 1):
                visited[r][c] = False
                return True

        path.pop()
        visited[r][c] = False
        return False

    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == word[0] and dfs(r, c, 0):
                return path

    return None


def _print_grid(grid: List[List[str]]) -> None:
    print("\n  Grid:")
    for row in grid:
        print("  " + " ".join(row))


# ── MAIN ─────────────────────────────────────────────────────
if __name__ == "__main__":
    print("╔══════════════════════════════════╗")
    print("║       Word Search in Grid        ║")
    print("╚══════════════════════════════════╝")

    grid = [
        ['A','B','C','E'],
        ['S','F','C','S'],
        ['A','D','E','E']
    ]
    _print_grid(grid)
    print()

    test_words = ["ABCCED", "SEE", "ABCB", "SFCS", "HELLO"]
    for word in test_words:
        found = exist(grid, word)
        status = "✅ FOUND" if found else "❌ NOT FOUND"
        print(f"  {word!r:<12} → {status}")
        if found:
            coords = find_path(grid, word)
            if coords:
                path_str = " → ".join(f"({r},{c})" for r, c in coords)
                print(f"             Path: {path_str}")
