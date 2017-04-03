package GaltonBoard;

import java.util.Iterator;
import java.util.LinkedList;

public class SQueue {

	private LinkedList<Circle> elements = new LinkedList<Circle>();

	public void enqueue(Circle element) {
		elements.add(element);
	}

	public Circle dequeue() {
		return elements.removeFirst();
	}

	public Circle get(int i) {
		return elements.get(i);
	}

	public Circle peek() {
		return elements.getFirst();
	}

	public void clear() {
		elements.clear();
	}

	public int size() {
		return elements.size();
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public Iterator<Circle> iterator() {
		return elements.iterator();
	}

}
