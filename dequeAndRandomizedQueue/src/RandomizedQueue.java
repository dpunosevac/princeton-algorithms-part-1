import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;


/**
 * This class is used as a queue which is dequeuing items randomly from it
 * 
 * @author dpunosevac
 *
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
	private int numOfelements = 0;
	private Item[] elements;

	// construct an empty randomized queue
	public RandomizedQueue() {
		elements = (Item[]) new Object[1];
	}

	// is the queue empty?
	public boolean isEmpty() {
		return numOfelements == 0;
	}

	// return the number of items on the queue
	public int size() {
		return numOfelements;
	}

	// add the item
	public void enqueue(Item item) {
		if (item == null) {
			throw new NullPointerException();
		}

		if (numOfelements == elements.length) {
			resize(2 * elements.length);
		}

		elements[numOfelements++] = item;
	}

	private void resize(int capacity) {
		Item[] copy = (Item[]) new Object[capacity];

		for (int i = 0; i < numOfelements; i++) {
			copy[i] = elements[i];
		}

		elements = copy;
	}

	// remove and return a random item
	public Item dequeue() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		int rand = StdRandom.uniform(numOfelements);
		final Item item = elements[rand];
		elements[rand] = elements[numOfelements - 1];
		elements[--numOfelements] = null;

		if (numOfelements > 0 && numOfelements == elements.length / 4) {
			resize(elements.length / 2);
		}

		return item;
	}

	// return (but do not remove) a random item
	public Item sample() {
		if (isEmpty()) {
			throw new java.util.NoSuchElementException();
		}

		return elements[StdRandom.uniform(numOfelements)];
	}

	// return an independent iterator over items in random order
	@Override
	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}

	private class RandomizedQueueIterator implements Iterator<Item> {
		private int current = 0;
		private int[] shuffledIndexes = new int[numOfelements];

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			if (current == 0) {
				for (int i = 0; i < numOfelements; i++) {
					shuffledIndexes[i] = i;
				}

				StdRandom.shuffle(shuffledIndexes);
			}

			return current < numOfelements;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Item next() {
			if (current == 0) {
				for (int i = 0; i < numOfelements; i++) {
					shuffledIndexes[i] = i;
				}

				StdRandom.shuffle(shuffledIndexes);
			}

			if (current >= numOfelements || size() == 0) {
				throw new java.util.NoSuchElementException();
			}

			return elements[shuffledIndexes[current++]];
		}

	}

	// unit testing
	public static void main(String[] args) {
		RandomizedQueue<String> randomQueue = new RandomizedQueue<String>();
		randomQueue.enqueue("First element");
		randomQueue.enqueue("Second element");
		randomQueue.enqueue("Third element");
		randomQueue.enqueue("Fourth element");
		randomQueue.enqueue("Fifth element");

		System.out.println("Is the queue empty: " + randomQueue.isEmpty());
		System.out.println("Size of a queue is: " + randomQueue.size());
		System.out.println("Sample of random element from queue: " + randomQueue.sample());
		System.out.println("Size of a queue is: " + randomQueue.size());

		for (String s : randomQueue) {
			System.out.println(s);
		}

		while (!randomQueue.isEmpty()) {
			System.out.println(randomQueue.dequeue());
		}
		
		System.out.println("Is the queue empty: " + randomQueue.isEmpty());
	}
}
