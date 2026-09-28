/**
 * 
 */
package edu.ncsu.csc216.wolf_regrade.model.util;

import java.util.NoSuchElementException;

/**
 * Implements the IBackLog interface. Includes BackLogIterator that implements Iterator as an inner class.
 * Items are added to the end of the list.  Clients can access and remove elements from anywhere in the list
 * @author Josue Lopez
 * @param <E> type for the list
 */
public class BackLog<E extends Comparable<E>> implements IBackLog<E> {
	
	/** Size of the list **/
	private int size;
	/** front of the list **/
	private ListNode<E> front;
	/** back of the list **/
	private ListNode<E> back;

	/**
	 * Constructor for the BackLog class
	 */
	public BackLog() {
		this.size = 0;
		this.front = null;
		this.back = null;
	}

	/**
	 * Adds the element to the back of the list.
	 * @param element element to add
	 * @throws NullPointerException if element is null
	 * @throws IllegalArgumentException if the element is unable to be added
	 */
	@Override
	public void add(E element) {
		if(element == null) throw new NullPointerException("Cannot add null element.");
		
		ListNode<E> toAdd = new ListNode<E>(element);
		
		if(front == null) front = toAdd;
		else back.next = toAdd;
		
		back = toAdd;
		size++;
		
	}

	/**
	 * Returns the element removed from the given index. 
	 * @param idx index to remove element from
	 * @return element at given index
	 * @throws IndexOutOfBoundsException if the idx is out of bounds for the list
	 */
	@Override
	public E remove(int idx) {
		checkIndex(idx);
		
		E removed;
		
		if(idx == 0) {
			removed = front.data;
			front = front.next;
			
			if(front == null) back = null;
		}
		else {
			ListNode<E> prev = front;
			
	        for(int i = 0; i < idx - 1; i++) {
	            prev = prev.next;
	        }
	        
            removed = prev.next.data;
            prev.next = prev.next.next;
            
            if(prev.next == null) back = prev;
		}
		
		size--;
		return removed;
	}

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
	@Override
	public E set(int idx, E element) {
		if(element == null) throw new NullPointerException("Cannot add null element.");
		checkIndex(idx);
		
		ListNode<E> current = front;
		
        for(int i = 0; i < idx; i++) {
            current = current.next;
        }
        
        E replaced = current.data;
        current.data = element;
        
		return replaced;
	}

	/**
	 * Returns the element at the given index.
	 * @param idx index of the element to retrieve
	 * @return element at the given index
	 * @throws IndexOutOfBoundsException if the idx is out of bounds
	 * 		for the list
	 */
	@Override
	public E get(int idx) {
		checkIndex(idx);
		
		ListNode<E> current = front;
		
        for(int i = 0; i < idx; i++) {
            current = current.next;
        }
        
        return current.data;
	}

	/**
	 * Returns the number of elements in the list.
	 * @return number of elements in the list
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Helper method thats check is the index is out of bounds
	 * @param idx index to check
	 */
	private void checkIndex(int idx) {
		if(idx < 0 || idx >= size) throw new IndexOutOfBoundsException("Invalid index.");
	}

	/**
	 * creates an Iterator to traverse the list
	 * @return Iterator that traverses the list
	 */
	@Override
	public Iterator<E> iterator() {
		return new BackLogIterator();
	}

	/**
	 * Inner class that implements the Iterator Interface
	 * @author Josue Lopez
	 */
	private class BackLogIterator implements Iterator<E> {
		
		/** current node in the Iterator **/
		private ListNode<E> current;
		
		/**
		 * Constructor for the BackLogIterator class
		 */
		public BackLogIterator() {
			current = front;
		}

		/**
		 * Returns true if there are more elements to iterate over.
		 * @return true if there is a next element
		 */
		@Override
		public boolean hasNext() {
			return current != null;
		}

		/**
		 * Returns the next element in the iteration.
		 * @return the next element
		 * @throws NoSuchElementException if there are no more elements
		 */
		@Override
		public E next() {
			if(!hasNext()) throw new NoSuchElementException();
			
			E data = current.data;
			current = current.next;
			
			return data;
		}
		
	}
	
	/**
	 * Inner class which represents a single node in the list
	 * @param <E> Generic type
	 * @author Josue Lopez-Carmona
	 */
	private static class ListNode<E> {
		
		/** generic data type data **/
		public E data;
		/** next value of data **/
		private ListNode<E> next;
		
		/**
        * Constructs a node with given data
        * @param data the data stored in the node
        */
       public ListNode(E data) {
           this.data = data;
           this.next = null;
       }
		
//		/**
//		 * constructor for the ListNode class which sets the data and next field
//		 * @param data data stored in the node 
//		 * @param next next reference to the node
//		 */
//		public ListNode(E data, ListNode<E> next) {
//           this.data = data;
//           this.next = next;
//       }
	}
}
