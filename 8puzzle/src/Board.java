import java.util.Arrays;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * <i>Board</i> class represents data type which is used in solving 8puzzle
 * problem. Board is represent as array of blocks, where 0 is empty space, and
 * once we have array in order from 1 to n - 1, and last element in array is 0,
 * we can consider it solved. We use hamming and manhattan calculations for
 * Heuristic technique to find the best solutions using A* algorithm
 * 
 * @author pdusan
 *
 */
public class Board {
	private int[] board;
	private int n;

	/**
	 * Construct a board from an n-by-n array of blocks
	 * 
	 * @param blocks
	 *            array which represents puzzle blocks
	 */
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {
		int index = 0;
		n = blocks.length;
		board = new int[n * n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[index++] = blocks[i][j];
			}
		}
	};

	/**
	 * @return board dimension n
	 */
	public int dimension() {
		return n;
	};

	/**
	 * @return number of blocks out of place
	 */
	public int hamming() {
		int hammingCount = 0;

		for (int i = 0; i < board.length; i++) {
			if (board[i] != 0 && board[i] != i + 1)
				hammingCount++;
		}

		return hammingCount;
	};

	/**
	 * @return sum of Manhattan distances between blocks and goal
	 */
	public int manhattan() {
		int manhattonSum = 0;

		int[][] blocks = convertTo2dArray(board);

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				manhattonSum += calculateManhattanDistance(blocks[i][j], i, j);
			}
		}

		return manhattonSum;
	};

	private int[][] convertTo2dArray(int[] board1d) {
		int[][] board2d = new int[n][n];
		int index = 0;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board2d[i][j] = board1d[index++];
			}
		}

		return board2d;
	}

	private int calculateManhattanDistance(int block, int currentRow, int currentCol) {
		int row = (block - 1) % n;
		int column = (block - 1) / n;

		return block == 0 ? 0 : Math.abs(currentRow - row) + Math.abs(currentCol - column);
	}

	/**
	 * Checks if this board is the goal board
	 * 
	 * @return true if it is goal, false otherwise
	 */
	public boolean isGoal() {
		return hamming() == 0;
	};

	/**
	 * @return a board that is obtained by exchanging any pair of blocks
	 */
	public Board twin() {
		for (int i = 0; i < board.length; i++)
			if (board[i] != 0 && board[i + 1] != 0) {
				int row = getRow(i);
				int col = getCol(i);

				return new Board(swap(row, col, row, col + 1));
			}

		return null;
	};

	private int getRow(int index) {
		return index / n;
	}

	private int getCol(int index) {
		return index % n;
	}

	private int[][] swap(int row, int col, int nextRow, int nextCol) {
		int[][] copy = convertTo2dArray(board);
		int temp = copy[row][col];
		copy[row][col] = copy[nextRow][nextCol];
		copy[nextRow][nextCol] = temp;

		return copy;
	}

	/**
	 * Checks if this board is equal to y
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (!Arrays.equals(board, other.board))
			return false;
		if (n != other.n)
			return false;
		return true;
	}

	/**
	 * Returns all possible changes we can make from the current position of the
	 * blocks
	 * 
	 * @return all neighboring boards
	 */
	public Iterable<Board> neighbors() {
		Queue<Board> neighbors = new Queue<Board>();
		int row = 0;
		int col = 0;

		for (int i = 0; i < board.length; i++) {
			if (board[i] == 0) {
				row = getRow(i);
				col = getCol(i);
				break;
			}
		}

		if (row > 0)
			neighbors.enqueue(new Board(swap(row, col, row - 1, col)));

		if (row < n - 1)
			neighbors.enqueue(new Board(swap(row, col, row + 1, col)));

		if (col > 0)
			neighbors.enqueue(new Board(swap(row, col, row, col - 1)));

		if (col < n - 1)
			neighbors.enqueue(new Board(swap(row, col, row, col + 1)));

		return neighbors;
	};

	/**
	 * @return {@link String} representation of this board (in the output format specified below)
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(n + "\n");
		for (int i = 0; i < board.length; i++) {
			s.append(String.format("%2d ", board[i]));

			if ((i + 1) % n == 0)
				s.append("\n");
		}

		return s.toString();
	};

	// unit tests (not graded)
	public static void main(String[] args) {
		int n = 4;
		int[][] blocks = new int[n][n];
		blocks[0][0] = 0;
		blocks[0][1] = 2;
		blocks[0][2] = 3;
		blocks[0][3] = 1;
		blocks[1][0] = 5;
		blocks[1][1] = 6;
		blocks[1][2] = 7;
		blocks[1][3] = 8;
		blocks[2][0] = 9;
		blocks[2][1] = 10;
		blocks[2][2] = 11;
		blocks[2][3] = 12;
		blocks[3][0] = 13;
		blocks[3][1] = 14;
		blocks[3][2] = 15;
		blocks[3][3] = 4;

		Board board = new Board(blocks);
		StdOut.println(board.hamming());
		StdOut.println(board.manhattan());
		StdOut.println(board.dimension());
		StdOut.println(board.toString());
		StdOut.println();
		StdOut.println(board.twin().toString());
	};
}
