import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * <i>RangeSearchVisualizer</i> class represents client for testing. It reads a
 * sequence of points from a file (specified as a command-line argument) and
 * inserts those points into a 2d-tree. Then, it performs range searches on the
 * axis-aligned rectangles dragged by the user in the standard drawing window.
 * 
 * @author pdusan
 *
 */
public class RangeSearchVisualizer {
	public static void main(String[] args) {

		String filename = args[0];
		In in = new In(filename);

		StdDraw.enableDoubleBuffering();

		// initialize the data structures with N points from standard input
		PointSET brute = new PointSET();
		KdTree kdtree = new KdTree();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			kdtree.insert(p);
			brute.insert(p);
		}

		double x0 = 0.0, y0 = 0.0; // initial endpoint of rectangle
		double x1 = 0.0, y1 = 0.0; // current location of mouse
		boolean isDragging = false; // is the user dragging a rectangle

		// draw the points
		StdDraw.clear();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		brute.draw();
		StdDraw.show();

		while (true) {

			// user starts to drag a rectangle
			if (StdDraw.mousePressed() && !isDragging) {
				x0 = StdDraw.mouseX();
				y0 = StdDraw.mouseY();
				isDragging = true;
				continue;
			}

			// user is dragging a rectangle
			else if (StdDraw.mousePressed() && isDragging) {
				x1 = StdDraw.mouseX();
				y1 = StdDraw.mouseY();
				continue;
			}

			// mouse no longer pressed
			else if (!StdDraw.mousePressed() && isDragging) {
				isDragging = false;
			}

			RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1), Math.max(x0, x1), Math.max(y0, y1));
			// draw the points
			StdDraw.clear();
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(0.01);
			brute.draw();

			// draw the rectangle
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius();
			rect.draw();

			// draw the range search results for brute-force data structure in
			// red
			StdDraw.setPenRadius(0.03);
			StdDraw.setPenColor(StdDraw.RED);
			for (Point2D p : brute.range(rect))
				p.draw();

			// draw the range search results for kd-tree in blue
			StdDraw.setPenRadius(0.02);
			StdDraw.setPenColor(StdDraw.BLUE);
			for (Point2D p : kdtree.range(rect))
				p.draw();

			StdDraw.show();
			StdDraw.pause(40);
		}
	}
}