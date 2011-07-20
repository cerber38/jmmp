package ru.mmp.queue;

import java.util.LinkedList;

/**
 * 
 * @author Raziel
 */
public class MMPQueue extends LinkedList implements Queue {
	public synchronized void push(Object obj) {
		super.addLast(obj);
	}

	public synchronized Object poll() {
		Object element = super.getFirst();
		super.remove(element);
		return element;
	}

	public synchronized boolean isEmpty() {
		return super.size() < 1;
	}

}
