import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <i>BruteCollinearPoints</i> class represents data type for solving pattern
 * recognition problem using brute force algorithm with complexity O(n^4)
 * 
 * @author pdusan
 *
 */
public class BruteCollinearPoints {
	private Point[] a;
	private List<LineSegment> lineSegments = new ArrayList<LineSegment>();

	/**
	 * Finds all line segments containing 4 points
	 * 
	 * @param points
	 *            array of points which we process for pattern recognition
	 */
	public BruteCollinearPoints(Point[] points) {
		validateInput(points);
		int n = points.length;
		a = Arrays.copyOf(points, n);
		
		for (int p = 0; p < n; p++) {
			for (int q = p + 1; q < n; q++) {
				for (int r = q + 1; r < n; r++) {
					for (int s = r + 1; s < n; s++) {
						if (a[p].slopeTo(a[q]) == a[p].slopeTo(a[r])
								&& a[p].slopeTo(a[q]) == a[p].slopeTo(a[s])) {
							lineSegments.add(new LineSegment(a[p], a[s]));
						}
					}
				}
			}
		}
	};

	private void validateInput(Point[] points) {
		if (points == null)
			throw new NullPointerException("You cannot provide the empty array!");

		for (int i = 0; i < points.length; i++) {
			if (points[i] == null)
				throw new NullPointerException("You cannot have null point inside the array!");

			for (int j = i + 1; j < points.length - 1; j++) {
				if (points[i] == points[j])
					throw new IllegalArgumentException("You cannot have duplicate points inside the array");
			}
		}
	}

	/**
	 * @return the number of line segments
	 */
	public int numberOfSegments() {
		return lineSegments.size();
	};

	/**
	 * @return the line segments
	 */
	public LineSegment[] segments() {
		LineSegment[] segments = lineSegments.toArray(new LineSegment[lineSegments.size()]);

		return segments;
	};
}
