package apps;

import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Vertex;


public class PartialTreeList implements Iterable<PartialTree> {

	/**
	 * Inner class - to build the partial tree circular linked list
	 *
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;

		/**
		 * Next node in linked list
		 */
		public Node next;

		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 *
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;

	/**
	 * Number of nodes in the CLL
	 */
	private int size;

	/**
	 * Initializes this list to empty
	 */
	public PartialTreeList() {
		rear = null;
		size = 0;
	}

	/**
	 * Adds a new tree to the end of the list
	 *
	 * @param tree Tree to be added to the end of the list
	 */
	public void append(PartialTree tree) {
		Node ptr = new Node(tree);
		if (rear == null) {
			ptr.next = ptr;
		} else {
			ptr.next = rear.next;
			rear.next = ptr;
		}
		rear = ptr;
		size++;
	}

	/**
	 * Removes the tree that is at the front of the list.
	 *
	 * @return The tree that is removed from the front
	 * @throws NoSuchElementException If the list is empty
	 */
	public PartialTree remove()
			throws NoSuchElementException {
		if( rear == null){
			return null;
		}
		if ( size==1){
			Node ptr = rear;
			rear=null;
			size--;
			return ptr.tree;

		}
		Node ptr = rear.next;
		if(size==2){
			rear.next=rear;
			size--;
			return ptr.tree;
		}
		rear.next=ptr.next;
		/* COMPLETE THIS METHOD */
		size--;
		return ptr.tree;
	}

	/**
	 * Removes the tree in this list that contains a given vertex.
	 *
	 * @param vertex Vertex whose tree is to be removed
	 * @return The tree that is removed
	 * @throws NoSuchElementException If there is no matching tree
	 */
	public PartialTree removeTreeContaining(Vertex vertex)
			throws NoSuchElementException {
		Node prv= rear;
		Node ptr = rear;

		Boolean in =false;
		int count = 0;
		while( !(ptr.tree.getRoot().name.equals(vertex.name)) ){

			Vertex ptr1= vertex;
			while( true ) { // checks if vertex is in the tree
				if(ptr.tree.getRoot().equals(ptr1)){
					in = true;
					break;
				}
				if( ptr1.parent.equals(ptr1)){
					break;
				}
				ptr1=ptr1.parent;
			}
			if(in == true){
				break;
			}
			if( ptr.tree.getRoot().name.equals(rear.tree.getRoot().name) && count!=0){
				ptr=null;
				break;
			}
			prv=ptr;
			ptr=ptr.next;
			count++;


		}
		if(ptr==null){
			return null;
		}
		if(ptr.equals(prv)){
			prv=prv.next;
			while ( !prv.next.equals(rear)){

				prv=prv.next;
			}
		}
		if( ptr.tree.getRoot().name.equals(rear.tree.getRoot().name)){
			rear=prv;
			rear.next=ptr.next;
		}else{
			prv.next=ptr.next;
		}
		size--;
		/* COMPLETE THIS METHOD */

		return ptr.tree;
	}

	/**
	 * Gives the number of trees in this list
	 *
	 * @return Number of trees
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns an Iterator that can be used to step through the trees in this list.
	 * The iterator does NOT support remove.
	 *
	 * @return Iterator for this list
	 */
	public Iterator<PartialTree> iterator() {
		return new PartialTreeListIterator(this);
	}

	private class PartialTreeListIterator implements Iterator<PartialTree> {

		private PartialTreeList.Node ptr;
		private int rest;

		public PartialTreeListIterator(PartialTreeList target) {
			rest = target.size;
			ptr = rest > 0 ? target.rear.next : null;
		}

		public PartialTree next()
				throws NoSuchElementException {
			if (rest <= 0) {
				throw new NoSuchElementException();
			}
			PartialTree ret = ptr.tree;
			ptr = ptr.next;
			rest--;
			return ret;
		}

		public boolean hasNext() {
			return rest != 0;
		}

		public void remove()
				throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

	}
}


