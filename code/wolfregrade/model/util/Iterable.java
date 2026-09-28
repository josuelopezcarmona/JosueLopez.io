package edu.ncsu.csc216.wolf_regrade.model.util;

/**
 * Iterable interface for objects that provide an iterator.
 *
 * @author Dr. Sarah Heckman
 *
 * @param <E> type of elements returned by the iterator
 */
public interface Iterable<E> {

	/**
	 * Returns an iterator for the elements.
	 * @return an iterator to iterate over the elements
	 */
	Iterator<E> iterator();

}