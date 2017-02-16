import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * The Percolation class is used to model a percolation system
 * 
 * @author pdusan
 *
 */
public class Percolation {

	private int n;
	private int topSite;
	private int bottomSite;
	private WeightedQuickUnionUF uf;
	private WeightedQuickUnionUF ufPerc;
	private byte[] site; // 0 - closed, 1 - open site, 2 - full site

	/**
	 * Create n-by-n grid, with all sites blocked
	 * 
	 * @param N
	 *            square matrix length
	 */
	public Percolation(int N) {
		n = N;
		uf = new WeightedQuickUnionUF(n * n + 2);
		ufPerc = new WeightedQuickUnionUF(n * n + 2);
		site = new byte[n * n];
		topSite = n * n;
		bottomSite = n * n + 1;
	}

	/**
	 * Open site with given row and column, if it is not open already
	 * 
	 * @param row
	 *            of the block which we want to open
	 * @param col
	 *            column of the block which we want to open
	 */
	public void open(int row, int col) {
		isInBound(row, col);

		if (isOpen(row, col)) {
			return;
		}

		int currentSite = convert2dTo1dCoord(row, col);
		this.site[currentSite] = 1;

		// union with top virtuall cell
		if (row == 1 && !uf.connected(currentSite, topSite)) {
			uf.union(currentSite, topSite);
			ufPerc.union(currentSite, topSite);
		}

		// union with bottom artificial cell
		if (row == n) {
			ufPerc.union(currentSite, bottomSite);
		}

		// union with bottom cell
		if (row < n) {
			if (isOpen(row + 1, col)) {
				uf.union(currentSite, convert2dTo1dCoord(row + 1, col));
				ufPerc.union(currentSite, convert2dTo1dCoord(row + 1, col));
			}
		}
		// union with upper cell
		if (row > 1) {
			if (isOpen(row - 1, col)) {
				uf.union(currentSite, convert2dTo1dCoord(row - 1, col));
				ufPerc.union(currentSite, convert2dTo1dCoord(row - 1, col));
			}
		}
		// union with left cell
		if (col > 1) {
			if (isOpen(row, col - 1)) {
				uf.union(currentSite, convert2dTo1dCoord(row, col - 1));
				ufPerc.union(currentSite, convert2dTo1dCoord(row, col - 1));
			}
		}
		// union with right cell
		if (col < n) {
			if (isOpen(row, col + 1)) {
				uf.union(currentSite, convert2dTo1dCoord(row, col + 1));
				ufPerc.union(currentSite, convert2dTo1dCoord(row, col + 1));
			}
		}
	}

	/**
	 * Checks if site with given row and column is open
	 * 
	 * @param row
	 *            of the given site
	 * @param col
	 *            of the given site
	 * @return true if the site is open, false otherwise
	 */
	public boolean isOpen(int row, int col) {
		isInBound(row, col);

		if (site[convert2dTo1dCoord(row, col)] == 1) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if the site with given row and column is full (connected with top)
	 * 
	 * @param row
	 *            of the given site
	 * @param col
	 *            of the given site
	 * @return true if it is full, false otherwise
	 */
	public boolean isFull(int row, int col) {
		isInBound(row, col);

		if (!isOpen(row, col)) {
			return false;
		}

		int currentSite = convert2dTo1dCoord(row, col);
		if (uf.connected(topSite, currentSite)) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if the system percolate
	 * 
	 * @return true if percolates, false otherwise
	 */
	public boolean percolates() {
		if (ufPerc.connected(topSite, bottomSite)) {
			return true;
		}

		return false;
	}

	private boolean isInBound(int row, int col) {
		if (row < 1 || row > n || col < 1 || col > n) {
			throw new IndexOutOfBoundsException();
		}

		return true;
	}

	private int convert2dTo1dCoord(int row, int col) {
		int pos = n * (row - 1) + col - 1;

		return pos;
	}
}