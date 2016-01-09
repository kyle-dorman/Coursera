/**
 * Kyle Dorman
 * 10/3/15
 * Assignment 2 for coursera course
 * https://class.coursera.org/algs4partI-009
 *
 * A randomized queue is similar to a stack or queue, except that the item removed
 * is chosen uniformly at random from items in the data structure.
 *
 * Corner cases. The order of two or more iterators to the same randomized queue must be mutually
 * independent; each iterator must maintain its own random order. Throw a java.lang.NullPointerException
 * if the client attempts to add a null item; throw a java.util.NoSuchElementException if the client
 * attempts to sample or dequeue an item from an empty randomized queue; throw a
 * java.lang.UnsupportedOperationException if the client calls the remove() method in the iterator; throw
 * a java.util.NoSuchElementException if the client calls the next() method in the iterator and there
 * are no more items to return.
 *
 * Performance requirements. Your randomized queue implementation must support each randomized queue
 * operation (besides creating an iterator) in constant amortized time. That is, any sequence of M
 * randomized queue operations (starting from an empty queue) should take at most cM steps in the worst
 * case, for some constant c. A randomized queue containing N items must use at most 48N + 192 bytes of
 * memory. Additionally, your iterator implementation must support operations next() and hasNext() in
 * constant worst-case time; and construction in linear time; you may (and will need to) use a linear
 * amount of extra memory per iterator.
 */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private ResizingArray<Item> randomArray;

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue() {
        randomArray = new ResizingArray<>();
    }

    /**
     * Is the queue empty?
     * True if items counter is 0.
     * @return boolean
     */
    public boolean isEmpty() {
        return randomArray.isEmpty();
    }

    /**
     * Return the number of items on the queue
     * @return int number of items
     */
    public int size() {
        return randomArray.itemCount(); 
    }

    /**
     * Add the item
     * @param item Item
     * @throws NullPointerException when adding a null item
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException("Attempted to enqueue a null item to random queue.");
        }

        if (randomArray.emptySpaces() > 0) {
            int index = getRandomEmptyIndex();
            randomArray.add(item, index);
        } else {
            randomArray.add(item);
        }
    }

    /**
     * Remove and return a random item
     * @return
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty random queue when accessing dequeue.");
        }
        int index = getRandomFullIndex();
        Item item = randomArray.check(index);
        randomArray.remove(index);

        if (randomArray.isEmpty()) {
            randomArray = new ResizingArray<>();
        }

        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty random queue when accessing sample.");
        }
        return randomArray.check(getRandomFullIndex());
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(randomArray.compress());
    }

    private int getRandomEmptyIndex() {
        int index;
        if (randomArray.emptyIndexPointer == 0) {
            index = 0;
        } else {
            index = StdRandom.uniform(randomArray.emptyIndexPointer);
        }

        while (index < randomArray.size() && randomArray.check(index) != null) {
            index++;
        }

        if (index == randomArray.size()) {
            index = 0;
            while (randomArray.check(index) != null) {
                index++;
            }
        }
        return index;
    }

    private int getRandomFullIndex() {
        int index;
        if (randomArray.emptyIndexPointer == 0) {
            index = 0;
        } else {
            index = StdRandom.uniform(randomArray.emptyIndexPointer);
        }

        while (index < randomArray.size() && randomArray.check(index) == null) {
            index++;
        }

        if (index == randomArray.size()) {
            index = 0;
            while (randomArray.check(index) == null) {
                index++;
            }
        }
        return index;
    }

    private class RandomizedQueueIterator<Item> implements Iterator<Item> {
        private int index;
        private Item[] iteratorArray;

        public RandomizedQueueIterator(Item[] inputArr) {
            index = 0;
            iteratorArray = inputArr;

            for (int i = 0; i < inputArr.length; i++) {
                int j = StdRandom.uniform(i + 1);
                swap(i, j);
            }
        }

        public boolean hasNext() { return index < iteratorArray.length; }

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
            } else {
                Item item = iteratorArray[index];
                index++;
                return item;
            }
        }

        private void swap(int i, int j) {
            Item item = iteratorArray[i];
            iteratorArray[i] = iteratorArray[j];
            iteratorArray[j] = item;
        }
    }

    private class ResizingArray<Item> {
        private Item[] itemArray;
        private int numberOfItems;
        private int emptyIndexPointer;
        private int emptyCount;

        public ResizingArray() {
            itemArray = (Item[]) new Object[4];
            numberOfItems = 0;
            emptyIndexPointer = 0;
            emptyCount = 0;
        }

        public Item check(int index) {
            if (index >= itemArray.length) {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid index: ");
                sb.append(index);
                sb.append(". emptyIndexPointer is: ");
                sb.append(emptyIndexPointer);
                sb.append(". Array size is: ");
                sb.append(itemArray.length);
                throw new NoSuchElementException(sb.toString());
            }

            return itemArray[index];
        }

        public void add(Item item) {
            addItem(item, emptyIndexPointer);
            emptyIndexPointer++;
        }

        public void add(Item item, int index) {
            emptyCount--;
            addItem(item, index);
        }

        public void remove(int index) {
            removeAtIndex(index);
        }

        public boolean isEmpty() {
            return numberOfItems == 0;
        }

        public int itemCount() {
            return numberOfItems;
        }

        public int size() {
            return itemArray.length;
        }

        public int emptySpaces() {
            return emptyCount;
        }

        public Item[] compress() {
            return compress(numberOfItems);
        }

        private Item[] compress(int size) {
            Item[] copy = (Item[]) new Object[size];
            int copyIndex = 0;

            for (Item item : itemArray) {
                if (item != null) {
                    copy[copyIndex] = item;
                    copyIndex++;
                }
            }
            return copy;
        }

        private void removeAtIndex(int index) {
            if (index >= itemArray.length) {
                throw new NoSuchElementException();
            }

            itemArray[index] = null;
            numberOfItems = numberOfItems - 1;

            if (itemArray.length > 4 && numberOfItems < itemArray.length / 4) {
                resize(itemArray.length / 2);
            } else if (index == emptyIndexPointer - 1) {
                emptyIndexPointer--;
            } else {
                emptyCount++;
            }
        }

        private void addItem(Item item, int index) {
            if (index == itemArray.length) {
                resize(itemArray.length * 2);
            }
            numberOfItems = numberOfItems + 1;

            itemArray[index] = item;
        }

        private void resize(int N) {
            Item[] copy = (Item[]) new Object[N];
            int copyIndex = 0;

            // compress spaces in array
            for (int i = 0; i < itemArray.length; i++) {
                Item item = itemArray[i];
                if (item != null) {
                    copy[copyIndex] = item;
                    copyIndex++;
                }
            }

            emptyCount = 0;
//            numberOfItems = copyIndex;
            emptyIndexPointer = copyIndex;
            itemArray = copy;
        }
    }

    // unit testing
    public static void main(String[] args) {

    }
}
