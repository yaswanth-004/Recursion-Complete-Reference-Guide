"""
============================================================
 HAMILTONIAN CYCLE — Python
============================================================
 Given an undirected graph (adjacency matrix), determine
 whether a Hamiltonian Cycle exists — a path that visits
 every vertex exactly once and returns to the start.

 Approach : Backtracking
 Time     : O((N-1)!)  — NP-Complete problem
 Space    : O(N)
============================================================
"""
from typing import List, Optional

NOT_PLACED = -1


def solve_hamiltonian_cycle(graph: List[List[int]]) -> Optional[List[int]]:
    """
    Finds a Hamiltonian Cycle in the graph, if one exists.

    Parameters
    ----------
    graph : N×N adjacency matrix  (1=edge, 0=no edge)

    Returns
    -------
    List of vertices forming the cycle, or None if none exists.
    """
    n = len(graph)
    path = [NOT_PLACED] * n
    path[0] = 0   # always start at vertex 0

    if _backtrack(graph, path, pos=1, n=n):
        return path
    return None


def _backtrack(graph: list, path: list, pos: int, n: int) -> bool:
    """
    Core backtracking function.

    Parameters
    ----------
    graph : Adjacency matrix
    path  : Current path of placed vertices
    pos   : Index of the next slot to fill
    n     : Total number of vertices
    """

    # ── BASE CASE ────────────────────────────────────────────
    # All vertices placed — verify edge from last vertex back to start
    if pos == n:
        return graph[path[pos - 1]][path[0]] == 1

    # ── TRY EACH VERTEX AS NEXT IN PATH ──────────────────────
    for vertex in range(1, n):   # vertex 0 is always the start

        # ── PHASE 1: CHOOSE ──────────────────────────────────
        if _is_safe(graph, path, vertex, pos):

            path[pos] = vertex   # place vertex

            # ── PHASE 2: EXPLORE ─────────────────────────────
            if _backtrack(graph, path, pos + 1, n):
                return True      # complete cycle found!

            # ── PHASE 3: BACKTRACK ───────────────────────────
            path[pos] = NOT_PLACED   # un-place

    return False   # no vertex works here


def _is_safe(graph: list, path: list, vertex: int, pos: int) -> bool:
    """
    Returns True if 'vertex' can be placed at path[pos].

    Two conditions:
    1. There must be an edge from path[pos-1] to vertex.
    2. vertex must not already be in path.
    """
    # Condition 1: edge must exist
    if graph[path[pos - 1]][vertex] == 0:
        return False
    # Condition 2: vertex not already placed
    return vertex not in path[:pos]


def _print_result(path: Optional[List[int]]) -> None:
    if path is None:
        print("  No Hamiltonian Cycle exists.")
    else:
        cycle = " → ".join(str(v) for v in path) + f" → {path[0]}"
        print(f"\n  Hamiltonian Cycle found:\n  {cycle}\n")


def _print_graph(graph: List[List[int]]) -> None:
    n = len(graph)
    print(f"\n  Adjacency Matrix ({n} vertices):")
    header = "     " + "  ".join(str(i) for i in range(n))
    print(header)
    print("     " + "─" * (n * 3))
    for i, row in enumerate(graph):
        print(f"  {i}  │  " + "  ".join(str(v) for v in row))


# ── MAIN ─────────────────────────────────────────────────────
if __name__ == "__main__":
    print("╔══════════════════════════════════╗")
    print("║        Hamiltonian Cycle         ║")
    print("╚══════════════════════════════════╝")

    # Graph 1 — cycle exists: 0 → 1 → 2 → 4 → 3 → 0
    graph1 = [
        [0, 1, 0, 1, 0],
        [1, 0, 1, 1, 1],
        [0, 1, 0, 0, 1],
        [1, 1, 0, 0, 1],
        [0, 1, 1, 1, 0]
    ]
    print("\n─── Example 1: Cycle exists ───")
    _print_graph(graph1)
    result1 = solve_hamiltonian_cycle(graph1)
    _print_result(result1)

    # Graph 2 — no cycle (edge 3-4 removed)
    graph2 = [
        [0, 1, 0, 1, 0],
        [1, 0, 1, 1, 1],
        [0, 1, 0, 0, 1],
        [1, 1, 0, 0, 0],
        [0, 1, 1, 0, 0]
    ]
    print("─── Example 2: No cycle ───")
    _print_graph(graph2)
    result2 = solve_hamiltonian_cycle(graph2)
    _print_result(result2)
