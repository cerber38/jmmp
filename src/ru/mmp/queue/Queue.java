package ru.mmp.queue;

import java.util.Collection;

/**
 * 
 * @author Raziel
 */
public interface Queue extends Collection {

	public void push(Object obj);

	public Object poll();

	public boolean isEmpty();

}
