import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

/**
 * <i>PointSET</i> class represents data type for set of points in unit square.
 * It uses Red Black BST to support efficient range search (find all of the
 * points contained in a query rectangle) and nearest neighbor search (find a
 * closest point to a query point). This is <b>brute force</b> algorithm.
 * 
 * @author pdusan
 *
 */
public class PointSET {
	private SET<Point2D> pointSet;

	/**
	 * Construct an empty set of points
	 */
	public PointSET() {
		pointSet = new SET<Point2D>();
	}

	/**
	 * Checks if the set is empty
	 * 
	 * @return true if it is empty set, false otherwise
	 */
	public boolean isEmpty() {
		return pointSet.isEmpty();
	}

	/**
	 * @return number of points in the set
	 */
	public int size() {
		return pointSet.size();
	}

	/**
	 * Adds the point to the set (if it is not already in the set)
	 * 
	 * @param p
	 *            represents {@link Point2D} which we want to add
	 */
	public void insert(Point2D p) {
		pointSet.add(p);
	}

	/**
	 * Checks if the set contain point p
	 * 
	 * @param p
	 *            represents {@link Point2D} which we check
	 * @return true if it is in the set, false otherwise
	 */
	public boolean contains(Point2D p) {
		return pointSet.contains(p);
	}

	/**
	 * Draws all points to standard draw
	 */
	public void draw() {
		StdDraw.clear();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);

		for (Point2D point : pointSet) {
			point.draw();
		}
	}

	/**
	 * Returns all the points which is contained in the query rectangle
	 * 
	 * @param rect
	 *            represents {@link RectHV}
	 * @return all points that are inside the rectangle
	 */
	public Iterable<Point2D> range(RectHV rect) {
		Queue<Point2D> rectPoints = new Queue<Point2D>();

		for (Point2D point : pointSet)
			if (rect.contains(point))
				rectPoints.enqueue(point);

		return rectPoints;
	}

	/**
	 * Returns the nearest point from the set, for the given query point
	 * 
	 * @param p
	 *            represents {@link Point2D}
	 * @return a nearest neighbor in the set to point p; null if the set is
	 *         empty
	 */
	public Point2D nearest(Point2D p) {
		if (isEmpty())
			return null;

		Point2D minPoint = null;

		for (Point2D point : pointSet) {
			if (minPoint == null || point.distanceSquaredTo(p) < minPoint.distanceSquaredTo(p))
				minPoint = point;
		}

		return minPoint;
	}
}
