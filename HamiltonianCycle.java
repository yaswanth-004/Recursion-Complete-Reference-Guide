package backtracking;

import java.util.Arrays;

/**
 * ============================================================
 *  HAMILTONIAN CYCLE
 * ============================================================
 *  Given an undirected graph with N vertices, determine if it
 *  contains a Hamiltonian Cycle — a path that visits every
 *  vertex exactly once and returns to the starting vertex.
 *
 *  Graph Representation: Adjacency Matrix
 *    adj[i][j] = 1  →  edge exists between vertex i and j
 *    adj[i][j] = 0  →  no edge
 *
 *  Approach : Backtracking — build path vertex by vertex,
 *             backtrack when no valid next vertex exists
 *  Time     : O((N-1)!)  worst case  (NP-Complete problem)
 *  Space    : O(N)  — path array + recursion depth
 * ============================================================
 */
public class HamiltonianCycle {

    private static final int NOT_PLACED = -1;

    /**
     * Solves the Hamiltonian Cycle problem for the given graph.
     *
     * @param graph  N×N adjacency matrix
     * @return       true if a Hamiltonian Cycle exists
     */
    public static boolean solveHamiltonianCycle(int[][] graph) {
        int n = graph.length;
        int[] path = new int[n];
        Arrays.fill(path, NOT_PLACED);

        // Always start at vertex 0
        path[0] = 0;

        if (backtrack(graph, path, 1, n)) {
            printCycle(path);
            return true;
        }

        System.out.println("  No Hamiltonian Cycle exists in this graph.");
        return false;
    }

    /**
     * Core backtracking function.
     *
     * @param graph    Adjacency matrix
     * @param path     Current path of placed vertices
     * @param pos      Index of the next slot to fill in path[]
     * @param n        Total number of vertices
     * @return         true if a complete Hamiltonian Cycle is found
     */
    private static boolean backtrack(int[][] graph, int[] path, int pos, int n) {

        // ── BASE CASE ────────────────────────────────────────
        // All vertices placed — check if there's an edge back to start
        if (pos == n) {
            // The last vertex must connect back to vertex 0 (path[0])
            return graph[path[pos - 1]][path[0]] == 1;
        }

        // ── TRY EACH VERTEX AS THE NEXT IN THE PATH ──────────
        for (int vertex = 1; vertex < n; vertex++) {   // 0 is always the start

            // ── PHASE 1: CHOOSE ─────────────────────────────
            if (isSafe(graph, path, vertex, pos)) {

                path[pos] = vertex;   // place this vertex at position pos

                // ── PHASE 2: EXPLORE ────────────────────────
                if (backtrack(graph, path, pos + 1, n)) {
                    return true;  // found a complete cycle!
                }

                // ── PHASE 3: BACKTRACK ──────────────────────
                path[pos] = NOT_PLACED;   // un-place, try next vertex
            }
        }

        // No vertex worked in this position → backtrack to previous call
        return false;
    }

    /**
     * Returns true if 'vertex' can be placed at path[pos].
     *
     * Conditions:
     *   1. There must be an edge from the previously placed vertex to 'vertex'
     *   2. 'vertex' must not already be in the path
     */
    private static boolean isSafe(int[][] graph, int[] path, int vertex, int pos) {
        // Condition 1: edge from path[pos-1] → vertex must exist
        if (graph[path[pos - 1]][vertex] == 0) {
            return false;
        }
        // Condition 2: vertex must not already be in path[0..pos-1]
        for (int i = 0; i < pos; i++) {
            if (path[i] == vertex) {
                return false;
            }
        }
        return true;
    }

    /**
     * Pretty-prints the Hamiltonian Cycle.
     */
    private static void printCycle(int[] path) {
        System.out.print("\n  Hamiltonian Cycle found:\n  ");
        for (int vertex : path) {
            System.out.print(vertex + " → ");
        }
        System.out.println(path[0] + "  (back to start)\n");
    }

    /**
     * Prints the adjacency matrix of the graph.
     */
    private static void printGraph(int[][] graph) {
        int n = graph.length;
        System.out.println("\n  Adjacency Matrix (" + n + " vertices):");
        System.out.print("     ");
        for (int i = 0; i < n; i++) System.out.printf("%2d ", i);
        System.out.println();
        System.out.print("     ");
        System.out.println("─".repeat(n * 3));
        for (int i = 0; i < n; i++) {
            System.out.printf("  %d │ ", i);
            for (int j = 0; j < n; j++) System.out.printf("%2d ", graph[i][j]);
            System.out.println();
        }
    }

    // ── MAIN ─────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║        Hamiltonian Cycle         ║");
        System.out.println("╚══════════════════════════════════╝");

        // ── Graph 1: Hamiltonian Cycle EXISTS ────────────────
        // Cycle: 0 → 1 → 2 → 4 → 3 → 0
        int[][] graph1 = {
            {0, 1, 0, 1, 0},   // vertex 0 connects to 1, 3
            {1, 0, 1, 1, 1},   // vertex 1 connects to 0, 2, 3, 4
            {0, 1, 0, 0, 1},   // vertex 2 connects to 1, 4
            {1, 1, 0, 0, 1},   // vertex 3 connects to 0, 1, 4
            {0, 1, 1, 1, 0}    // vertex 4 connects to 1, 2, 3
        };

        System.out.println("\n─── Example 1: Cycle exists ───");
        printGraph(graph1);
        solveHamiltonianCycle(graph1);

        // ── Graph 2: No Hamiltonian Cycle ────────────────────
        int[][] graph2 = {
            {0, 1, 0, 1, 0},
            {1, 0, 1, 1, 1},
            {0, 1, 0, 0, 1},
            {1, 1, 0, 0, 0},   // removed edge 3-4
            {0, 1, 1, 0, 0}    // removed edge 4-3
        };

        System.out.println("\n─── Example 2: No cycle ───");
        printGraph(graph2);
        solveHamiltonianCycle(graph2);
    }
}
