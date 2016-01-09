import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Kyle Dorman
 * 10/4/15
 * Assignment 2 for coursera course
 * https://class.coursera.org/algs4partI-009
 *
 * Write a client program Subset.java that takes a command-line integer k; reads in a sequence of
 * N strings from standard input using StdIn.readString(); and prints out exactly k of them, uniformly
 * at random. Each item from the sequence can be printed out at most once. You may assume that 0 ≤ k ≤ N,
 * where N is the number of string on standard input.
 */
public class Subset {
    private RandomizedQueue<String> queue;

    public Subset() {
        queue = new RandomizedQueue<>();
    }

    private void add(String str) {
        queue.enqueue(str);
    }

    private String remove() {
        return queue.dequeue();
    }

    public static void main(String[] args) {
        Subset subset = new Subset();
        int k = Integer.parseInt(args[0]);

        while (!StdIn.isEmpty()) {
            String input = StdIn.readString();
            subset.add(input);
        }

        for (int i = 0; i < k; i++) {
            String output = subset.remove();
            StdOut.println(output);
        }
    }
}
