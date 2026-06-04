package backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 *  RAT IN A MAZE
 * ============================================================
 *  Find ALL paths for a rat to travel from the top-left corner
 *  (0,0) to the bottom-right corner (N-1, N-1) of a maze.
 *
 *  Maze Cell Values:
 *    1 = open path
 *    0 = wall / obstacle
 *
 *  Allowed Moves: Down (D), Up (U), Right (R), Left (L)
 *
 *  Approach : Backtracking — try every direction, backtrack on walls
 *             and already-visited cells
 *  Time     : O(4^(N²)) worst case
 *  Space    : O(N²)  — solution matrix + visited matrix
 * ============================================================
 */
public class RatInMaze {

    // Direction names and their (row, col) deltas
    private static final char[]  DIR_NAMES  = {'D', 'L', 'R', 'U'};
    private static final int[]   DR         = {+1,  0,   0,  -1};  // row delta
    private static final int[]   DC         = { 0, -1,  +1,   0};  // col delta

    /**
     * Finds and prints all paths through the maze.
     *
     * @param maze  N×N integer matrix (1=open, 0=wall)
     */
    public static List<String> findAllPaths(int[][] maze) {
        int n = maze.length;
        boolean[][] visited = new boolean[n][n];
        List<String> results = new ArrayList<>();

        // Rat can only start if the entrance is open
        if (maze[0][0] == 1) {
            visited[0][0] = true;
            backtrack(maze, visited, 0, 0, n, new StringBuilder(), results);
        }

        return results;
    }

    /**
     * Core backtracking function.
     *
     * @param maze     The maze grid
     * @param visited  Tracks which cells are on the current path
     * @param row      Current rat position (row)
     * @param col      Current rat position (column)
     * @param n        Maze dimension
     * @param path     String of moves taken so far (e.g. "DDRR")
     * @param results  Accumulates all valid path strings
     */
    private static void backtrack(int[][] maze, boolean[][] visited,
                                   int row, int col, int n,
                                   StringBuilder path, List<String> results) {

        // ── BASE CASE ────────────────────────────────────────
        // Reached the bottom-right corner → valid path found!
        if (row == n - 1 && col == n - 1) {
            results.add(path.toString());
            return;
        }

        // ── TRY ALL 4 DIRECTIONS ─────────────────────────────
        for (int d = 0; d < 4; d++) {
            int nextRow = row + DR[d];
            int nextCol = col + DC[d];

            // ── PHASE 1: CHOOSE ─────────────────────────────
            if (isSafe(maze, visited, nextRow, nextCol, n)) {

                visited[nextRow][nextCol] = true;   // mark as on-path
                path.append(DIR_NAMES[d]);          // record move

                // ── PHASE 2: EXPLORE ────────────────────────
                backtrack(maze, visited, nextRow, nextCol, n, path, results);

                // ── PHASE 3: BACKTRACK ──────────────────────
                visited[nextRow][nextCol] = false;  // un-mark
                path.deleteCharAt(path.length() - 1); // remove last move
            }
        }
    }

    /**
     * Returns true if (row, col) is a valid, unvisited open cell.
     */
    private static boolean isSafe(int[][] maze, boolean[][] visited,
                                   int row, int col, int n) {
        return row >= 0 && row < n
            && col >= 0 && col < n
            && maze[row][col] == 1
            && !visited[row][col];
    }

    /**
     * Prints the maze and all found paths visually.
     */
    private static void printResults(int[][] maze, List<String> paths) {
        int n = maze.length;

        System.out.println("\n Maze (" + n + "×" + n + "):");
        System.out.println(" (1=open, 0=wall, S=start, E=end)\n");
        for (int r = 0; r < n; r++) {
            System.out.print("  ");
            for (int c = 0; c < n; c++) {
                if (r == 0 && c == 0) System.out.print("S ");
                else if (r == n-1 && c == n-1) System.out.print("E ");
                else System.out.print(maze[r][c] + " ");
            }
            System.out.println();
        }

        System.out.println("\n Paths found: " + paths.size());
        for (int i = 0; i < paths.size(); i++) {
            System.out.println("  Path " + (i+1) + ": " + paths.get(i));
        }
    }

    // ── MAIN ─────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║          Rat in a Maze           ║");
        System.out.println("╚══════════════════════════════════╝");

        // Example 1 — multiple paths exist
        int[][] maze1 = {
            {1, 0, 0, 0},
            {1, 1, 0, 1},
            {1, 1, 0, 0},
            {0, 1, 1, 1}
        };
        List<String> paths1 = findAllPaths(maze1);
        printResults(maze1, paths1);

        System.out.println();

        // Example 2 — 4×4 open maze (many paths)
        int[][] maze2 = {
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1}
        };
        List<String> paths2 = findAllPaths(maze2);
        System.out.println("\n Open 4×4 maze — total paths: " + paths2.size());
    }
}
