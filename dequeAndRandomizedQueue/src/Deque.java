import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class represents the Deque, which is able to to add and remove the items
 * from the start and the end of it. The worst case time in both adding and
 * removing from any side is constant.
 * 
 * @author dpunosevac
 *
 */
public class Deque<Item> implements Iterable<Item> {
	private int count = 0;
	private Node<Item> firstNode;
	private Node<Item> lastNode;

	/**
	 * Construct an empty deque
	 */
	public Deque() {
		firstNode = null;
		lastNode = null;
	}

	/**
	 * Checks if the deque is empty
	 * 
	 * @return true if is empty, false otherwise
	 */
	public boolean isEmpty() {
		return firstNode == null && lastNode == null;
	}

	/**
	 * @return the number of items on the deque
	 */
	public int size() {
		return count;
	}

	/**
	 * Adds the item to the front
	 */
	public void addFirst(Item item) {
		checkIfNull(item);

		if (isEmpty()) {
			final Node<Item> elementNode = new Node<Item>(item, null, null);
			firstNode = elementNode;
			lastNode = elementNode;
			count++;

			return;
		}

		final Node<Item> oldFirstNode = firstNode;
		firstNode = new Node<Item>(item, null, oldFirstNode);
		count++;

		if (oldFirstNode != null) {
			oldFirstNode.previous = firstNode;
		}
	}

	/**
	 * Add the item to the end
	 */
	public void addLast(Item item) {
		checkIfNull(item);

		if (isEmpty()) {
			final Node<Item> elementNode = new Node<Item>(item, null, null);
			firstNode = elementNode;
			lastNode = elementNode;
			count++;

			return;
		}

		final Node<Item> newLastNode = new Node<Item>(item, lastNode, null);
		lastNode.next = newLastNode;
		lastNode = newLastNode;
	}

	/**
	 * Removes and returns the item from the front
	 */
	public Item removeFirst() {
		checkAvailability();
		final Node<Item> f = firstNode;
		firstNode = f.next;

		if (firstNode == null) {
			lastNode = null;
		} else {
			firstNode.previous = null;
		}

		return f.item;
	}

	/**
	 * Removes and returns the item from the end
	 */
	public Item removeLast() {
		checkAvailability();
		final Node<Item> l = lastNode;
		lastNode = l.previous;

		if (lastNode == null) {
			firstNode = null;
		} else {
			lastNode.next = null;
		}

		l.next = null;
		l.previous = null;

		return l.item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Item> iterator() {
		return new Iterator<Item>() {
			private Node<Item> current = firstNode;

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.Iterator#hasNext()
			 */
			@Override
			public boolean hasNext() {
				return current != null;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.Iterator#next()
			 */
			@Override
			public Item next() {
				if (current == null) {
					throw new NoSuchElementException();
				}

				Item item = current.item;
				current = current.next;

				return item;
			}
		};
	}

	/**
	 * Check if the item we try to add is null
	 * 
	 * @param item
	 *            generic item which we try to add in deque, throws
	 *            {@link NullPointerException} if the item is null
	 */
	private void checkIfNull(Item item) {
		if (item == null) {
			throw new NullPointerException();
		}
	}

	/**
	 * This method checks if first and last node is not null. If some of them is
	 * null, it throws NoSuchElementException.
	 */
	private void checkAvailability() {
		if (firstNode == null && lastNode == null) {
			throw new java.util.NoSuchElementException();
		}
	}

	private static class Node<Item> {
		private Node<Item> previous, next;
		private Item item;

		public Node(Item item, Node<Item> previous, Node<Item> next) {
			this.item = item;
			this.previous = previous;
			this.next = next;
		}
	}

	// unit testing
	public static void main(String[] args) {
		Deque<String> deque = new Deque<String>();
		deque.addFirst("First Node");
		deque.addLast("Last Node");

		System.out.print("Show Deque: ");
		for (String string : deque) {
			System.out.print(string + ", ");
		}

		System.out.println("\n---------------");

		deque.addFirst("First First Node");
		deque.addLast("Last Last Node");

		System.out.print("Show Deque: ");
		for (String string : deque) {
			System.out.print(string + ", ");
		}

		System.out.println("\n---------------");

		System.out.print("Removed: ");
		System.out.print(deque.removeFirst() + ", ");
		System.out.println(deque.removeLast());

		System.out.print("Show Deque: ");
		for (String string : deque) {
			System.out.print(string + ", ");
		}

		System.out.println("\n---------------");

		System.out.print("Removed: ");
		System.out.print(deque.removeFirst() + ", ");
		System.out.println(deque.removeFirst());

		try {
			deque.removeLast();
		} catch (Exception e) {
			System.out.println("Catched: " + e + ". Good!");
		}

		System.out.print("Show Deque: ");
		for (String string : deque) {
			System.out.print(string + ", ");
		}

		System.out.println("\n---------------");

		try {
			deque.addFirst(null);
		} catch (Exception e) {
			System.out.println("Catched: " + e + ". Very Good!");
		}
	}
}
