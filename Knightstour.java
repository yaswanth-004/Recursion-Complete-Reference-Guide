package backtracking;

/**
 * ============================================================
 *  KNIGHT'S TOUR
 * ============================================================
 *  Move a chess knight across an N×N board such that it visits
 *  every single square exactly once.
 *
 *  Chess Knight Moves (L-shaped):
 *    (-2,-1) (-2,+1)
 *    (-1,-2) (-1,+2)
 *    (+1,-2) (+1,+2)
 *    (+2,-1) (+2,+1)
 *
 *  Approach : Backtracking
 *  Time     : O(8^(N²)) worst case  |  O(N²) with Warnsdorff heuristic
 *  Space    : O(N²)  — board storage + recursion depth
 * ============================================================
 */
public class KnightsTour {

    // ── 8 possible knight moves ───────────────────────────────
    private static final int[] ROW_MOVES = {-2, -2, -1, -1, +1, +1, +2, +2};
    private static final int[] COL_MOVES = {-1, +1, -2, +2, -2, +2, -1, +1};

    /**
     * Entry point: initialises the board and starts backtracking.
     *
     * @param n  Board dimension (n×n)
     * @return   true if a complete tour was found, false otherwise
     */
    public static boolean solveKnightsTour(int n) {
        int[][] board = new int[n][n];

        // Mark every cell as unvisited
        for (int[] row : board)
            java.util.Arrays.fill(row, -1);

        // Start at (0,0), move count = 1
        board[0][0] = 1;

        if (backtrack(board, 0, 0, 2, n)) {
            printBoard(board, n);
            return true;
        }

        System.out.println("No Knight's Tour exists for board size " + n);
        return false;
    }

    /**
     * Core backtracking function.
     *
     * @param board      N×N board storing move numbers (−1 = unvisited)
     * @param row        Current knight row
     * @param col        Current knight column
     * @param moveCount  The move number to place next (starts at 2)
     * @param n          Board dimension
     * @return           true if a complete tour is found from this state
     */
    private static boolean backtrack(int[][] board, int row, int col,
                                     int moveCount, int n) {

        // ── BASE CASE ────────────────────────────────────────
        // All N² squares have been numbered → tour complete!
        if (moveCount == n * n + 1) {
            return true;
        }

        // ── TRY ALL 8 KNIGHT MOVES ───────────────────────────
        for (int i = 0; i < 8; i++) {
            int nextRow = row + ROW_MOVES[i];
            int nextCol = col + COL_MOVES[i];

            // ── PHASE 1: CHOOSE ─────────────────────────────
            // Valid move = within bounds AND not yet visited
            if (isValidMove(board, nextRow, nextCol, n)) {

                board[nextRow][nextCol] = moveCount;   // mark with move number

                // ── PHASE 2: EXPLORE ────────────────────────
                if (backtrack(board, nextRow, nextCol, moveCount + 1, n)) {
                    return true;   // propagate success upward immediately
                }

                // ── PHASE 3: BACKTRACK ──────────────────────
                board[nextRow][nextCol] = -1;          // un-mark (restore)
            }
        }

        // No valid move found from this state → signal failure
        return false;
    }

    /**
     * Returns true if (row, col) is within the board and not yet visited.
     */
    private static boolean isValidMove(int[][] board, int row, int col, int n) {
        return row >= 0 && row < n
            && col >= 0 && col < n
            && board[row][col] == -1;
    }

    /**
     * Pretty-prints the board showing the move sequence.
     */
    private static void printBoard(int[][] board, int n) {
        System.out.println("\n Knight's Tour Solution (" + n + "×" + n + "):\n");
        for (int[] row : board) {
            for (int cell : row) {
                System.out.printf("%3d ", cell);
            }
            System.out.println();
        }
    }

    // ── MAIN ─────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║         Knight's Tour            ║");
        System.out.println("╚══════════════════════════════════╝");

        // 5×5 board — finds a solution quickly
        System.out.println("\nSolving 5×5 Knight's Tour...");
        solveKnightsTour(5);

        // 6×6 board — takes longer but backtracking finds it
        System.out.println("\nSolving 6×6 Knight's Tour...");
        long start = System.currentTimeMillis();
        solveKnightsTour(6);
        System.out.printf("Time taken: %d ms%n", System.currentTimeMillis() - start);
    }
}
