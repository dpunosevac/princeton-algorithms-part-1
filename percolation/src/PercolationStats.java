import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * PercolationStats class represents data type used to estimate threshold,
 * considering the computational experiment known as <b>Monte Carlo
 * simulation</b>:
 * <ul>
 * <li>Initialize all sites to be blocked.</li>
 * <li>Repeat the following until the system percolates:</li>
 * <ul>
 * <li>Choose a site uniformly at random among all blocked sites.</li>
 * <li>Open the site.</li>
 * </ul>
 * <li>The fraction of sites that are opened when the system percolates provides
 * an estimate of the percolation threshold.</li>
 * </ul>
 * 
 * @author pdusan
 *
 */
public class PercolationStats {
	private double[] treshold;
	private int t;

	/**
	 * Performs T independent computational experiments on an N-by-N grid
	 * 
	 * @param N
	 *            length of square matrix
	 * @param T
	 *            number of computational experiments
	 */
	public PercolationStats(int N, int T) {
		if (N < 1 || T < 1) {
			throw new IllegalArgumentException();
		}
		t = T;
		treshold = new double[t];

		for (int i = 0; i < treshold.length; i++) {
			treshold[i] = calcTreshold(N);
		}
	}

	private double calcTreshold(int n) {
		double counter = 0;
		int i, j;
		Percolation perc = new Percolation(n);
		while (!perc.percolates()) {
			i = StdRandom.uniform(n) + 1;
			j = StdRandom.uniform(n) + 1;
			if (!perc.isOpen(i, j)) {
				counter++;
				perc.open(i, j);
			}
		}
		return counter / (n * n);
	}

	/**
	 * @return sample mean of percolation threshold
	 */
	public double mean() {
		return StdStats.mean(treshold);
	}

	/**
	 * @return sample standard deviation of percolation threshold
	 */
	public double stddev() {
		return StdStats.stddev(treshold);
	}

	/**
	 * @return lower bound of the 95% confidence interval
	 */
	public double confidenceLo() {
		return mean() - (1.96 * stddev()) / (Math.sqrt(t));
	}

	/**
	 * @return upper bound of the 95% confidence interval
	 */
	public double confidenceHi() {
		return mean() + (1.96 * stddev()) / (Math.sqrt(t));
	}

	// test client, described below
	public static void main(String[] args) {
		PercolationStats stats = new PercolationStats(100, 50);
		System.out.println("mean                    = " + stats.mean());
		System.out.println("stddev                  = " + stats.stddev());
		System.out.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());
	}
}