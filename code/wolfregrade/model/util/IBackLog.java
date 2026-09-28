package edu.ncsu.csc216.wolf_regrade.model.util;

/**
 * Interface for a list that maintains a backlog.  Items are added to the 
 * end of the list.  Clients can access and remove elements from anywhere 
 * in the list. Implements Iterable to reduce runtime when traversing read-only.
 *
 * @author Dr. Sarah Heckman
 *
 * @param <E> type for the list
 */
public interface IBackLog<E> extends Iterable<E> {

	/**
	 * Adds the element to the back of the list.
	 * @param element element to add
	 * @throws NullPointerException if element is null
	 * @throws IllegalArgumentException if the element is unable to be added
	 */
	void add(E element);

	/**
	 * Returns the element removed from the given index. 
	 * @param idx index to remove element from
	 * @return element at given index
	 * @throws IndexOutOfBoundsException if the idx is out of bounds
	 * 		for the list
	 */
	E remove(int idx);

	/**
	 * Replaces the element at the given index with the specified element
	 * and returns the original element.
	 * @param idx index of the element to replace
	 * @param element element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException if the idx is out of bounds
	 * 		for the list
	 * @throws NullPointerException if element is null
	 */
	E set(int idx, E element);

	/**
	 * Returns the element at the given index.
	 * @param idx index of the element to retrieve
	 * @return element at the given index
	 * @throws IndexOutOfBoundsException if the idx is out of bounds
	 * 		for the list
	 */
	E get(int idx);

	/**
	 * Returns the number of elements in the list.
	 * @return number of elements in the list
	 */
	int size();

}