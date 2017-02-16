import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
	private Point[] aux;
	private List<LineSegment> lineSegment = new ArrayList<LineSegment>();

	/**
	 * Finds all line segments containing 4 or more points
	 * 
	 * @param points
	 *            array of points which we process
	 */
	public FastCollinearPoints(Point[] points) {
		validateInput(points);
		int n = points.length;
		aux = Arrays.copyOf(points, n);

		for (int i = 0; i < n - 3; i++) {
			// Every time we iterate to the next point in array, we want to sort
			// them by
			// natural order. We do this to dodge cases where points
			// won't be in order, and line segments won't have full
			// length (example: You have next points: (0, 15000), (15000, 0),
			// (10000, 5000), (5000, 10000). If we do not sort them by natural
			// order, line segment will be created between the points (0, 15000)
			// and (5000, 10000), but instead it should be created between
			// points (0, 15000) and (15000, 0).
			Arrays.sort(aux);

			// We want to split points when we do sort them. We split them by
			// points which we process and
			// which we processed already, so we do not include points which we
			// already processed
			Arrays.sort(aux, i + 1, n, aux[i].slopeOrder());
			Arrays.sort(aux, 0, i, aux[i].slopeOrder());
			int head = i + 1;
			int tail = i + 2;
			int pHead = 0; // point head

			while (tail < n) {
				double currentSlope = aux[i].slopeTo(aux[head]);

				while (tail < n && currentSlope == aux[i].slopeTo(aux[tail]))
					tail++;

				// if difference between head and tail is higher then 2, we want
				// to check point heads to see if we already have line segment
				// which we want to add. We do this, by checking the slope
				// between currently processed point and points we processed
				// (this is the reason why we split sort)
				if (tail - head > 2) {
					double pSlope = Double.NEGATIVE_INFINITY;
					while (pHead < i) {
						pSlope = aux[i].slopeTo(aux[pHead]);
						if (pSlope < currentSlope)
							pHead++;
						else
							break;
					}

					if (pSlope != currentSlope) {
						lineSegment.add(new LineSegment(aux[i], aux[tail - 1]));
					}
				}

				head = tail++;
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
		return lineSegment.size();
	};

	/**
	 * @return the line segments
	 */
	public LineSegment[] segments() {
		LineSegment[] segments = lineSegment.toArray(new LineSegment[lineSegment.size()]);

		return segments;
	};
}
