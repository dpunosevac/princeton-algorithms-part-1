import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * <i>Solver</i> class represents data type for solving the 8puzzle problem
 * combining heuristic and A* algorithm.
 * 
 * @author pdusan
 * @see <a href=
 *      "http://theory.stanford.edu/~amitp/GameProgramming/AStarComparison.html">A*
 *      algorithm</a>
 */
public class Solver {
	private Node lastNode;

	private class Node implements Comparable<Node> {
		private Node previous;
		private Board board;
		private int numMoves = 0;

		public Node(Board board) {
			this.board = board;
		}

		public Node(Board board, Node previous) {
			this.board = board;
			this.previous = previous;
			this.numMoves = previous.numMoves + 1;
		}

		@Override
		public int compareTo(Node node) {
			return this.board.manhattan() - node.board.manhattan() + this.numMoves - node.numMoves;
		}

	}

	/**
	 * Find a solution to the initial board (using the A* algorithm)
	 * 
	 * @param initial
	 *            {@link Board}
	 */
	public Solver(Board initial) {
		MinPQ<Node> nodes = new MinPQ<Node>();
		nodes.insert(new Node(initial));

		MinPQ<Node> twinNodes = new MinPQ<Node>();
		twinNodes.insert(new Node(initial.twin()));

		while (true) {
			lastNode = expandNodes(nodes);
			if (lastNode != null || expandNodes(twinNodes) != null)
				return;
		}
	};

	private Node expandNodes(MinPQ<Node> nodes) {
		if (nodes.isEmpty())
			return null;

		Node bestNode = nodes.delMin();
		if (bestNode.board.isGoal())
			return bestNode;

		for (Board neighbor : bestNode.board.neighbors())
			if (bestNode.previous == null || !neighbor.equals(bestNode.previous.board))
				nodes.insert(new Node(neighbor, bestNode));

		return null;
	}

	/**
	 * Check if the initial board is solvable
	 * 
	 * @return true if it is, false otherwise
	 */
	public boolean isSolvable() {
		return lastNode != null;
	};

	/**
	 * @return minimum number of moves to solve initial board; -1 if unsolvable
	 */
	public int moves() {
		return isSolvable() ? lastNode.numMoves : -1;
	};

	/**
	 * @return sequence of boards in a shortest solution; null if unsolvable
	 */
	public Iterable<Board> solution() {
		if (!isSolvable())
			return null;

		Stack<Board> moves = new Stack<Board>();
		while (lastNode != null) {
			moves.push(lastNode.board);
			lastNode = lastNode.previous;
		}

		return moves;
	};

	// solve a slider puzzle (given below)
	public static void main(String[] args) {

		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	};
}
