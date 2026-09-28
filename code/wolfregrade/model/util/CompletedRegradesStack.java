/**
 * 
 */
package edu.ncsu.csc216.wolf_regrade.model.util;

import java.util.NoSuchElementException;

import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;

/**
 * implements the Interface IStack.
 * This class works with a list of RegradeRequest objects
 * @author Jouse Lopez
 */
public class CompletedRegradesStack implements IStack<RegradeRequest> {
	
	/** initial capacity of the list **/
	private static final int INITIAL_CAPACITY = 10;
	
	/** list of RegradeRquest s **/
	private RegradeRequest[] list;
	
	/** size of list **/
	private int size;
	
	/**
	 * Constructor for the CompletedRegradesStack class
	 */
	public CompletedRegradesStack() {
		list = new RegradeRequest[INITIAL_CAPACITY];
        size = 0;
	}

	/**
	 * Gets an Iterator over the stack 
	 * @return CompletedRegradesStackIterator to iterate through the stack
	 */
	@Override
	public Iterator<RegradeRequest> iterator() {
		return new CompletedRegradesStackIterator();
	}

	/**
	 * Adds the RegradeRequest to the top of the stack.
	 * @param request RegradeRequest to add
	 * @throws NullPointerException if element is null
	 * @throws IllegalArgumentException if RegradeRequest is unable to be added
	 */
	@Override
	public void push(RegradeRequest request) {
		if(request == null) throw new NullPointerException("Cannot add null element.");
		
		checkCapacity(size + 1);
		
		list[size] = request;
		size++;
		
	}

	/**
	 * Removes and returns the RegradeRequest at the top of the stack.
	 * @return RegradeRequest at the top of the stack
	 * @throws NoSuchElementException if the stack is empty
	 */
	@Override
	public RegradeRequest pop() {
		if(isEmpty()) throw new NoSuchElementException("Empty stack.");
		
		RegradeRequest top = list[size - 1];
		list[size - 1] = null;
		
		size--;
		return top;
	}

	/**
	 * Returns the RegradeRequest at the top of the stack without removing it.
	 * @return RegradeRequest at the top of the stack
	 * @throws NoSuchElementException if the stack is empty
	 */
	@Override
	public RegradeRequest peek() {
		if(isEmpty()) throw new NoSuchElementException("Empty stack.");
		
		return list[size - 1];
	}

	/**
	 * Returns the number of RegradeRequest in the stack.
	 * @return number of RegradeRequest in the stack
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Returns true if the stack is empty.
	 * @return true if the stack contains no RegradeRequest
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Checks to to see if the given index is out of bounds in the list
	 * @param idx index to check
	 */
	private void checkCapacity(int idx) {
		if(idx >= list.length) {
			RegradeRequest[] regrow = new RegradeRequest[list.length * 2];
			
			for(int i = 0; i < size; i++) {
				regrow[i] = list[i];
			}
			
			list = regrow;
		}
	}
	
	/**
	 * Inner class that helps traverse the list in CompleteRegradeStack
	 * @author Josue Lopez
	 */
	private class CompletedRegradesStackIterator implements Iterator<RegradeRequest> {
		
		/** current index Iterator is at in the list **/
		private int current;

		/**
		 * Constructor for the CompletedRegradesStackIterator class
		 */
		public CompletedRegradesStackIterator() { 
			current = size - 1; 
		}

		/**
		 * Returns true if there are more elements to iterate over.
		 * @return true if there is a next element
		 */
		@Override
		public boolean hasNext() {
			return current >= 0;
		}

		/**
		 * Returns the next element in the iteration.
		 * @return the next element
		 * @throws NoSuchElementException if there are no more elements
		 */
		@Override
		public RegradeRequest next() {
			if(!hasNext()) throw new NoSuchElementException();
			
			return list[current--];
		}
		
	}
}
