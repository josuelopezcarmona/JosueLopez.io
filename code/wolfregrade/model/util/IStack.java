package edu.ncsu.csc216.wolf_regrade.model.util;

/**
 * Interface for a stack.  Elements can only be added to and removed from 
 * the top of the stack.  Extends Iterable for viewing contents.
 *
 * @author Dr. Sarah Heckman
 *
 * @param <E> type for the IStack
 */
public interface IStack<E> extends Iterable<E> {

	/**
	 * Adds the element to the top of the stack.
	 * @param element element to add
	 * @throws NullPointerException if element is null
	 * @throws IllegalArgumentException if the element is unable to be added
	 */
	void push(E element);

	/**
	 * Removes and returns the element at the top of the stack.
	 * @return element at the top of the stack
	 * @throws NoSuchElementException if the stack is empty
	 */
	E pop();

	/**
	 * Returns the element at the top of the stack without removing it.
	 * @return element at the top of the stack
	 * @throws NoSuchElementException if the stack is empty
	 */
	E peek();

	/**
	 * Returns the number of elements in the stack.
	 * @return number of elements in the stack
	 */
	int size();

	/**
	 * Returns true if the stack is empty.
	 * @return true if the stack contains no elements
	 */
	boolean isEmpty();

}