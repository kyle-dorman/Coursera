/*
Kyle Dorman
9/23/2015
Assignment 1 for coursera course
https://class.coursera.org/algs4partI-009

Percolation. Given a composite systems comprised of randomly distributed insulating and metallic materials:
what fraction of the materials need to be metallic so that the composite system is an electrical conductor?
Given a porous landscape with water on the surface (or oil below), under what conditions will the water be
able to drain through to the bottom (or the oil to gush through to the surface)? Scientists have defined
an abstract process known as percolation to model such situations.

The model. We model a percolation system using an N-by-N grid of sites. Each site is either open or blocked.
A full site is an open site that can be connected to an open site in the top row via a chain of neighboring
(left, right, up, down) open sites. We say the system percolates if there is a full site in the bottom row.
In other words, a system percolates if we fill all open sites connected to the top row and that process fills
some open site on the bottom row. (For the insulating/metallic materials example, the open sites correspond
to metallic materials, so that a system that percolates has a metallic path from top to bottom, with full
sites conducting. For the porous substance example, the open sites correspond to empty space through which
water might flow, so that a system that percolates lets water fill open sites, flowing from top to bottom.)

The problem. In a famous scientific problem, researchers are interested in the following question: if sites
are independently set to be open with probability p (and therefore blocked with probability 1 − p), what is
the probability that the system percolates? When p equals 0, the system does not percolate; when p equals 1,
the system percolates. The plots below show the site vacancy probability p versus the percolation probability
for 20-by-20 random grid (left) and 100-by-100 random grid (right).
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] dataSet;
    private int lastIndex;
    private int N;
    private WeightedQuickUnionUF percolatesUnionFind;
    private WeightedQuickUnionUF isFullUnionFind;

    /**
     * Initializes a percolation structure with N sites
     * 0 through N-1. Structure consists of two data models.
     * 2D array to keep track of the open and closed sites.
     * WeightedQuickUnionUF data structure to keep track of
     * the connections between the different open nodes.
     *
     * @param  N the number of sites
     * @throws IllegalArgumentException if N <= 0
     */
    public Percolation(int N) {
        if (N <= 0) {
            StringBuilder strBuilder = new StringBuilder("Illegal input ");
            strBuilder.append(N).append(". Number of sites must be greater than 0.");
            throw new IllegalArgumentException(strBuilder.toString());
        }

        dataSet = new boolean[N][N]; // initialized to false by default
        this.N = N;
        lastIndex = N * N + 1;
        percolatesUnionFind = new WeightedQuickUnionUF(lastIndex + 1);
        isFullUnionFind = new WeightedQuickUnionUF(lastIndex + 1);

    }

    /**
     * Opens a node.
     * In array, node is set to true if it is not already.
     * In unionFind, node is connected to open adjacent nodes.
     *
     * @param  i the integer representing the row
     * @param  j the integer representing the column
     * @throws IndexOutOfBoundsException unless 1 < i < N && 1 < j < N
     */
    public void open(int i, int j) {
        validate(i, j);

        if (isOpen(i, j)) {
            return;
        }

        dataSet[i - 1][j - 1] = true;

        if (i > 1) {
            if (isOpen(i - 1, j)) {
                percolatesUnionFind.union(arrayIndex(i, j), arrayIndex(i - 1, j));
                isFullUnionFind.union(arrayIndex(i, j), arrayIndex(i - 1, j));
            }
        } else {
            percolatesUnionFind.union(0, arrayIndex(i, j));
            isFullUnionFind.union(0, arrayIndex(i, j));
        }

        if (i < N) {
            if (isOpen(i + 1, j)) {
                percolatesUnionFind.union(arrayIndex(i, j), arrayIndex(i + 1, j));
                isFullUnionFind.union(arrayIndex(i, j), arrayIndex(i + 1, j));
            }
        }
        else {
            percolatesUnionFind.union(lastIndex, arrayIndex(i, j));
//            isFullUnionFind.union(lastIndex, arrayIndex(i, j));
        }

        if (j > 1 && isOpen(i, j - 1)) {
            percolatesUnionFind.union(arrayIndex(i, j), arrayIndex(i, j - 1));
            isFullUnionFind.union(arrayIndex(i, j), arrayIndex(i, j - 1));
        }

        if (j < N && isOpen(i, j + 1)) {
            percolatesUnionFind.union(arrayIndex(i, j), arrayIndex(i, j + 1));
            isFullUnionFind.union(arrayIndex(i, j), arrayIndex(i, j + 1));
        }
    }

    /**
     * Returns if the site is open.
     *
     * @param  i the integer representing the row
     * @param  j the integer representing the column
     * @return true if site is open, otherwise false
     * @throws IndexOutOfBoundsException unless 1 < i < N && 1 < j < N
     */
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return dataSet[i - 1][j - 1];
    }

    /**
     * Returns if the site is full.
     * Site is "full" if the site is connected to the last row and
     * is thus connected to the end dummy node.
     *
     * @param  i the integer representing the row
     * @param  j the integer representing the column
     * @return true if site is full. Otherwise false.
     * @throws IndexOutOfBoundsException unless 1 < i < N && 1 < j < N
     */
    public boolean isFull(int i, int j) {
        validate(i, j);

        return isFullUnionFind.connected(0, arrayIndex(i, j));
    }

    /**
     * Returns true if system percolates.
     * System percolates if any node in the first row is connected to any node in the last row.
     * In the program, that means the starting dummy node is connected to the end dummy now.
     *
     * @return true if system percolates. Otherwise false.
     */
    public boolean percolates() {
//        int headNode = unionFind.find(0);
//
//
//        for (int k = 1; k <= N; k++) {
//            if (isOpen(N, k) && unionFind.find(arrayIndex(N, k)) == headNode) {
//                return true;
//            }
//        }
//        return false;
        return percolatesUnionFind.connected(0, lastIndex);
    }

    /**
     * Returns the 1D array equivalent node in the UnionFind system.
     *
     * @param  i the integer representing the row
     * @param  j the integer representing the column
     * @return integer of node in 1D  UnionFind array.
     * @throws IndexOutOfBoundsException unless 1 < i < N && 1 < j < N
     */
    private int arrayIndex(int i, int j) {
        validate(i, j);
        return (i - 1) * N + j;
    }

    /**
     * Validates that the row and column are within the system bounds.
     *
     * @param  i the integer representing the row
     * @param  j the integer representing the column
     * @throws IndexOutOfBoundsException unless 1 < i < N && 1 < j < N
     */
    private void validate(int i, int j) {
        if (i < 1 || i > N) {
            throw new IndexOutOfBoundsException("Index i out of bounds: " + i);
        }
        if (j < 1 || j > N) {
            throw new IndexOutOfBoundsException("Index j out of bounds: " + j);
        }
    }

    /**
     * Reads in a sequence of pairs of integers (between 0 and N-1) from standard input,
     * where each integer represents some object;
     * if the sites are in different components, merge the two components
     * and print the pair to standard output.
     */
    public static void main(String[] args) {
        int N = StdIn.readInt();
        Percolation percolation = new Percolation(N);

        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            percolation.open(p, q);
            if (percolation.percolates()) {
                StdOut.println("System percolates!");
            } else {
                StdOut.println("System does not percolate yet.");
            }
            StdOut.println(p + " " + q);
        }
        StdOut.println("Finished!");
    }
}
