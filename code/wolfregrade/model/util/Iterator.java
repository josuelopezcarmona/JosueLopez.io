package edu.ncsu.csc216.wolf_regrade.model.util;

import java.util.NoSuchElementException;

/**
 * Iterator interface for traversing elements in a collection.
 * 
 * @author Dr. Sarah Heckman
 *
 * @param <E> type of elements returned by this iterator
 */
public interface Iterator<E> {

	/**
	 * Returns true if there are more elements to iterate over.
	 * @return true if there is a next element
	 */
	boolean hasNext();

	/**
	 * Returns the next element in the iteration.
	 * @return the next element
	 * @throws NoSuchElementException if there are no more elements
	 */
	E next();

}