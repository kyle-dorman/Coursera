/**
 * Kyle Dorman
 * 10/3/2015
 * Assignment 2 for coursera course
 * https://class.coursera.org/algs4partI-009
 *
 * A double-ended queue or deque (pronounced "deck") is a generalization of a stack and a
 * queue that supports adding and removing items from either the front or the back of the
 * data structure.
 *
 * Corner cases. Throw a java.lang.NullPointerException if the client attempts to add a null
 * item; throw a java.util.NoSuchElementException if the client attempts to remove an item
 * from an empty deque; throw a java.lang.UnsupportedOperationException if the client calls
 * the remove() method in the iterator; throw a java.util.NoSuchElementException if the client
 * calls the next() method in the iterator and there are no more items to return.
 *
 * Performance requirements.   Your deque implementation must support each deque operation in
 * constant worst-case time. A deque containing N items must use at most 48N + 192 bytes of
 * memory. and use space proportional to the number of items currently in the deque. Additionally,
 * your iterator implementation must support each operation (including construction) in constant
 * worst-case time.
 */

import java.util.Iterator;
import java.util.NoSuchElementException;
//import java.lang.Iterable;

public class Deque<Item> implements Iterable<Item> {
    private Node root; // dummy node used to keep track of the first and last node.
    private int size; // number of items in the deque

    /**
     * Constructor, construct an empty deque
     */
    public Deque() {
        root = new Node<Item>(null);
        root.connect(root);
        size = 0;
    }

    /**
     * Is the deque empty?
     * @return boolean true is deque is empty
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Return the number of items in the deque
     * @return int size of deque
     */
    public int size() {
        return size;
    }

    /**
     * Add the item to the front
     * @param item Item
     * @throws NullPointerException if item is null
     */
    public void addFirst(Item item) {
        validateItemAddition(item);
        Node n = new Node<>(item);

        n.connect(root.next);
        root.connect(n);

        size++;
    }

    /**
     * Add the item to the end of the deque.
     * @param item Item
     * @throws NullPointerException if item is null
     */
    public void addLast(Item item) {
        validateItemAddition(item);
        Node n = new Node<>(item);

        Node previousLast = root.previous;
        previousLast.connect(n);
        n.connect(root);

        size++;
    }

    /**
     * Remove and return the item from the front.
     * @return Item
     * @throws NoSuchElementException deque is empty
     */
    public Item removeFirst() {
        validateItemRemoval();

        Node<Item> n = root.next;
        Node<Item> nextFirst = n.next;

        n.disconnect();
        root.disconnect();
        root.connect(nextFirst);

        size--;
        return n.getItem();
    }

    /**
     * Remove and return the item from the end.
     * @return Item
     * @throws NoSuchElementException deque is empty
     */
    public Item removeLast() {
        validateItemRemoval();

        Node<Item> n = root.previous;
        Node<Item> nextLast = n.previous;

        nextLast.disconnect();
        n.disconnect();
        nextLast.connect(root);

        size--;

        return n.getItem();
    }

    /**
     * Return an iterator over items in order from front to end.
     * @return Iterator
     */
    public Iterator<Item> iterator() { return new DequeIterator(); }

    /**
     * Class that implements Iterator interface.
     */
    private class DequeIterator<Item> implements Iterator<Item> {
        private Node<Item> current = root.next; // current node

        /**
         * True if this is not the last node in the deque.
         * Test is node is the root.
         * @return boolean
         */
        public boolean hasNext() { return !current.isRoot(); }

        /**
         * Not supported.
         * @throws UnsupportedOperationException always
         */
        public void remove() { throw new UnsupportedOperationException(); }

        /**
         * Get current item and move reference to next node.
         *
         * @return Item
         * @throws NoSuchElementException if last node in deque
         */
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = current.getItem();
            current = current.next;
            return item;
        }
    }

    /**
     * Validate the removal of an item form front or back.
     * Valid as long as deque is not empty
     * @throws NoSuchElementException deque is empty
     */
    private void validateItemRemoval() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
    }

    /**
     * Validate the addition of an item.
     * Invalid if item is null.
     * @param item Item
     * @throws NullPointerException if item is null
     */
    private void validateItemAddition(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
    }

    /**
     * Inner Node class.
     */
    private class Node<Item> {
        public Node next;
        public Node previous;
        private Item item;

        /**
         * Construtor. Set private item variable.
         * @param item Item
         */
        public Node(Item item) {
            this.item = item;
        }

        /**
         * Connect Node to input Node (n)
         * 'this' Node comes before input node (n)
         *
         * @param n Node
         */
        public void connect(Node n) {
            this.next = n;
            n.previous = this;
        }

        /**
         * Disconnect this Node for next Node
         * and next Node from this node
         */
        public void disconnect() {
            Node n = this.next;
            this.next = null;
            n.previous = null;
        }

        /**
         * Get the private item variable.
         * @return Item item
         */
        public Item getItem() {
            return item;
        }

        /**
         * Determine if not is root node.
         * True if item is null. Protected by wrapper class for inputs.
         * @return boolean true if item is null.
         */
        private boolean isRoot() {
            return item == null;
        }
    }

    // unit testing. sigh.
    public static void main(String[] args) {

    }
}