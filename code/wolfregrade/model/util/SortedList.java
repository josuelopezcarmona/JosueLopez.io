/**
 * 
 */
package edu.ncsu.csc216.wolf_regrade.model.util;

/**
 * Implements the ISortedList interface using a linkedList
 * @author Josue Lopez
 * @param <E> generic type
 */
public class SortedList<E extends Comparable<E>> implements ISortedList<E> {
	
	/** Size of the list **/
	private int size;
	/** front of the list **/
	private ListNode<E> front;
	
	/**
	 * Constructor for the SortedList class
	 */
	public SortedList() {
		this.size = 0;
		this.front = null;
	}

	/**
	 * Adds element to the list in sorted order
	 * @param element is the object to add to the list
	 * @throws NullPointerException if element is null
	 * @throws IllegalArgumentException if the element is a duplicate
	 */
	@Override
	public void add(E element) {
		
		if(element == null) throw new NullPointerException("Cannot add null element.");
		
		ListNode<E> current = front;
		
		ListNode<E> prev = null;
		
		while(current != null) {
			if(element.compareTo(current.data) == 0) throw new IllegalArgumentException("Cannot add duplicate element.");
			
			if(element.compareTo(current.data) < 0) break;
			
			prev = current;
			
			current = current.next;
		}
		
		ListNode<E> newNode = new ListNode<E>(element, current);
		
        if(prev == null) front = newNode; 
        else prev.next = newNode;
        
        size++;
		
	}

	/**
	 * removes the element at the index from the list
	 * @return removed element E
	 * @throws IndexOutOfBoundsException if the index is out of bounds
	 */
	@Override
	public E remove(int idx) {
		checkIndex(idx);
		 
        E removed;
        if(idx == 0) {
            removed = front.data;
            front = front.next;
        } 
        else {
            ListNode<E> prev = front;
            
            for(int i = 0; i < idx - 1; i++) {
                prev = prev.next;
            }
            
            removed = prev.next.data;
            prev.next = prev.next.next;
        }
        size--;
        return removed;
	}

	/**
	 * Traverses through the list to find the given element
	 * @return true the element is in the list, else, false
	 */
	@Override
	public boolean contains(E element) {
		if(element == null) return false;
		
		ListNode<E> current = front;
		
        while(current != null) {
            if(current.data.compareTo(element) == 0) return true;
            
            current = current.next;
        }
        return false;
	}

	/**
	 * Gets the element at the index of the list
	 * @return element at index
	 * @throws IndexOutOfBoundsException if the index is out of bounds
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
	 * Gets the size of the list
	 * @return size of the list
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
		 * constructor for the ListNode class which sets the data and next field
		 * @param data data stored in the node 
		 * @param next next reference to the node
		 */
		public ListNode(E data, ListNode<E> next) {
            this.data = data;
            this.next = next;
        }
	}
}
