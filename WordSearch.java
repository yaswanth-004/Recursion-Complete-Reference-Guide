package backtracking;

/**
 * ============================================================
 *  WORD SEARCH IN A 2D GRID
 * ============================================================
 *  Given a 2D character grid and a target word, determine if the
 *  word exists in the grid by moving UP, DOWN, LEFT, or RIGHT —
 *  without reusing the same cell twice in one path.
 *
 *  Approach : Backtracking — try every starting cell; at each
 *             step try all 4 directions; backtrack on mismatch
 *  Time     : O(N × M × 4^L)  where L = word length
 *  Space    : O(L)  recursion depth  +  O(N×M) visited array
 * ============================================================
 */
public class WordSearch {

    // 4-directional movement: up, down, left, right
    private static final int[] DR = {-1, +1,  0,  0};
    private static final int[] DC = { 0,  0, -1, +1};

    /**
     * Returns true if 'word' exists anywhere in 'grid'.
     *
     * @param grid  2D character board
     * @param word  Target word to find
     */
    public static boolean exist(char[][] grid, String word) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];

        // Try every cell as a potential starting point
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == word.charAt(0)) {   // first char match
                    if (backtrack(grid, visited, r, c, word, 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Core backtracking function — tries to match word[index..end]
     * starting from cell (row, col).
     *
     * @param grid    The character grid
     * @param visited Tracks cells used on the current path
     * @param row     Current cell row
     * @param col     Current cell column
     * @param word    Target word
     * @param index   Which character of 'word' we need to match next
     * @return        true if the remaining word is found from this cell
     */
    private static boolean backtrack(char[][] grid, boolean[][] visited,
                                      int row, int col,
                                      String word, int index) {

        // ── BASE CASE ────────────────────────────────────────
        // All characters matched successfully
        if (index == word.length()) {
            return true;
        }

        // ── PRUNING ──────────────────────────────────────────
        int rows = grid.length, cols = grid[0].length;
        if (row < 0 || row >= rows) return false;   // out of bounds
        if (col < 0 || col >= cols) return false;   // out of bounds
        if (visited[row][col]) return false;         // already on path
        if (grid[row][col] != word.charAt(index)) return false;  // mismatch

        // ── PHASE 1: CHOOSE ──────────────────────────────────
        visited[row][col] = true;    // mark current cell as used

        // ── PHASE 2: EXPLORE ─────────────────────────────────
        for (int d = 0; d < 4; d++) {
            int nextRow = row + DR[d];
            int nextCol = col + DC[d];
            if (backtrack(grid, visited, nextRow, nextCol, word, index + 1)) {
                visited[row][col] = false;  // still restore before returning
                return true;               // found! propagate success
            }
        }

        // ── PHASE 3: BACKTRACK ───────────────────────────────
        visited[row][col] = false;   // un-mark: this path didn't work
        return false;
    }

    /**
     * Finds and prints the coordinates of the word path (first occurrence).
     */
    public static void findAndPrintPath(char[][] grid, String word) {
        int rows = grid.length, cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        java.util.List<int[]> path = new java.util.ArrayList<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == word.charAt(0)) {
                    if (findPath(grid, visited, r, c, word, 0, path)) {
                        System.out.print("  Path for \"" + word + "\": ");
                        for (int[] coord : path) {
                            System.out.printf("(%d,%d)", coord[0], coord[1]);
                            if (coord != path.get(path.size() - 1))
                                System.out.print(" → ");
                        }
                        System.out.println();
                        return;
                    }
                }
            }
        }
        System.out.println("  \"" + word + "\" not found in grid.");
    }

    private static boolean findPath(char[][] grid, boolean[][] visited,
                                     int row, int col, String word, int index,
                                     java.util.List<int[]> path) {
        int rows = grid.length, cols = grid[0].length;
        if (row < 0 || row >= rows || col < 0 || col >= cols) return false;
        if (visited[row][col]) return false;
        if (grid[row][col] != word.charAt(index)) return false;

        visited[row][col] = true;
        path.add(new int[]{row, col});

        if (index == word.length() - 1) return true;  // all matched

        for (int d = 0; d < 4; d++) {
            if (findPath(grid, visited, row + DR[d], col + DC[d], word, index + 1, path)) {
                visited[row][col] = false;
                return true;
            }
        }

        path.remove(path.size() - 1);
        visited[row][col] = false;
        return false;
    }

    private static void printGrid(char[][] grid) {
        System.out.println("\n  Grid:");
        for (char[] row : grid) {
            System.out.print("  ");
            for (char c : row) System.out.print(c + " ");
            System.out.println();
        }
    }

    // ── MAIN ─────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║       Word Search in Grid        ║");
        System.out.println("╚══════════════════════════════════╝");

        char[][] grid = {
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };
        printGrid(grid);
        System.out.println();

        String[] words = {"ABCCED", "SEE", "ABCB", "SFCS", "HELLO"};
        for (String word : words) {
            boolean found = exist(grid, word);
            System.out.printf("  %-10s → %s%n", "\"" + word + "\"",
                found ? "✅ FOUND" : "❌ NOT FOUND");
            if (found) findAndPrintPath(grid, word);
        }
    }
}
