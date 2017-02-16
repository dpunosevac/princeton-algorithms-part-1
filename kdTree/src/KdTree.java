import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * <i>KdTree</i> class represents 2d-tree data type for solving range search and
 * nearest neighbor search problems. A 2d-tree is generalization of a BST to
 * two-dimensional keys. The idea is to build a BST with points in the nodes,
 * using the x and y-coordinates of the points as keys in strict alternating
 * sequence.
 * 
 * @author pdusan
 *
 */
public class KdTree {
	private static final double XMIN = 0;
	private static final double YMIN = 0;
	private static final double XMAX = 1;
	private static final double YMAX = 1;
	private Node root;
	private int size;

	private static class Node {
		private Point2D point;
		private RectHV rect;
		private Node left, right;

		public Node(Point2D point, RectHV rect) {
			this.point = point;
			this.rect = rect;
			this.left = null;
			this.right = null;
		}
	}

	/**
	 * Constructs an empty set of points
	 */
	public KdTree() {
		size = 0;
	}

	/**
	 * Checks if the set is empty
	 * 
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * @return number of points in the set
	 */
	public int size() {
		return size;
	}

	/**
	 * Adds the point to the set (if it is not already in the set)
	 * 
	 * @param p
	 *            represents {@link Point2D} which we want to add
	 */
	public void insert(Point2D p) {
		root = put(root, p, XMIN, YMIN, XMAX, YMAX, 0);
	}

	private Node put(Node node, Point2D point, double xmin, double ymin, double xmax, double ymax, int level) {
		if (node == null) {
			size++;
			return new Node(point, new RectHV(xmin, ymin, xmax, ymax));
		}

		int cmp = comparePoints(point, node.point, level);

		if (cmp < 0) {
			if (level % 2 == 0)
				node.left = put(node.left, point, xmin, ymin, node.point.x(), ymax, level + 1);
			else
				node.left = put(node.left, point, xmin, ymin, xmax, node.point.y(), level + 1);
		} else if (cmp > 0) {
			if (level % 2 == 0)
				node.right = put(node.right, point, node.point.x(), ymin, xmax, ymax, level + 1);
			else
				node.right = put(node.right, point, xmin, node.point.y(), xmax, ymax, level + 1);
		}

		return node;
	}

	private int comparePoints(Point2D a, Point2D b, int level) {
		if (level % 2 == 0)
			return Double.compare(a.x(), b.x());

		return Double.compare(a.y(), b.y());
	}

	/**
	 * Checks if the set contain point p
	 * 
	 * @param p
	 *            represents {@link Point2D} which we check
	 * @return true if it is in the set, false otherwise
	 */
	public boolean contains(Point2D p) {
		return get(root, p, 0) != null;
	}

	private Point2D get(Node node, Point2D point, int level) {
		if (node == null)
			return null;

		int cmp = comparePoints(point, node.point, level);

		if (cmp < 0)
			return get(node.left, point, level + 1);
		else if (cmp > 0)
			return get(node.right, point, level + 1);
		else
			return node.point;
	}

	/**
	 * Draws all points to standard draw
	 */
	public void draw() {
		drawLine(root, 0);
	}

	private void drawLine(Node node, int level) {
		if (node != null) {
			drawLine(node.left, level + 1);

			StdDraw.setPenRadius();
			if (level % 2 == 0) {
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
			} else {
				StdDraw.setPenColor(StdDraw.BLUE);
				StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
			}

			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(0.01);
			node.point.draw();

			drawLine(node.right, level + 1);
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
		Queue<Point2D> queue = new Queue<Point2D>();

		rangeAdd(root, rect, queue);

		return queue;
	}

	private void rangeAdd(Node node, RectHV rect, Queue<Point2D> queue) {
		if (node != null && rect.intersects(node.rect)) {
			if (rect.contains(node.point)) {
				queue.enqueue(node.point);
			}

			rangeAdd(node.left, rect, queue);
			rangeAdd(node.right, rect, queue);
		}
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
		if (isEmpty()) {
			return null;
		} else {
			Point2D result = null;

			result = nearest(root, p, result);

			return result;
		}
	}

	private Point2D nearest(Node node, Point2D point, Point2D min) {
		if (node != null) {
			if (min == null) {
				min = node.point;
			}

			// If the current min point is closer to query than the current
			// point
			if (min.distanceSquaredTo(point) >= node.rect.distanceSquaredTo(point)) {
				if (node.point.distanceSquaredTo(point) < min.distanceSquaredTo(point)) {
					min = node.point;
				}

				// Check in which order should we iterate
				if (node.right != null && node.right.rect.contains(point)) {
					min = nearest(node.right, point, min);
					min = nearest(node.left, point, min);
				} else {
					min = nearest(node.left, point, min);
					min = nearest(node.right, point, min);
				}
			}
		}

		return min;
	}
}