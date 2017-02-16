import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * This class is used as a client to run tests for {@link Deque} and
 * {@link RandomizedQueue}
 * 
 * @author dpunosevac
 *
 */
public class Subset {

	public static void main(String[] args) {
		RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();

		// read strings from std input:
		while (!StdIn.isEmpty()) {
			String s = StdIn.readString();
			randomizedQueue.enqueue(s);
		}

		int k = Integer.parseInt(args[0]);
		for (int i = 0; i < k; i++) {
			StdOut.println(randomizedQueue.dequeue());
		}
	}
}