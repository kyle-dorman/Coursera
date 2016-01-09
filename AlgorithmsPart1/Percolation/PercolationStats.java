/*
Kyle Dorman
9/23/2015
Assignment 1 for coursera course
https://class.coursera.org/algs4partI-009

By repeating this computation experiment T times and averaging the results,
we obtain a more accurate estimate of the percolation threshold.
*/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double mean;
    private double stddev;
    private int T;

    /**
     * Perform T independent experiments on an N-by-N grid.
     * Determines mean, standard deviation, and confidence interval.
     *
     * @param N the number of sites
     * @param T the number of experiments to run
     * @throws IllegalArgumentException if N <= 0 || T <= 0
     */
    public PercolationStats(int N, int T)  {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Illegal input argument.");
        }
        this.T = T;

        double[] results = runMonteCarlo(N);
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
    }

    /**
     * Sample mean of percolation threshold.
     *
     * @return double
     */
    public double mean() {
        return mean;
    }

    /**
     * Sample standard deviation of percolation threshold.
     *
     * @return double
     */
    public double stddev() {
        return stddev;
    }

    /**
     * Low endpoint of 95% confidence interval
     *
     * @return double
     */
    public double confidenceLo() {
        return mean - confidenceInterval();
    }

    /**
     * High endpoint of 95% confidence interval
     *
     * @return double
     */
    public double confidenceHi() {
        return mean + confidenceInterval();
    }

    /**
     * 95% confidence interval
     *
     * @return double
     */
    private double confidenceInterval() {
        return 1.96 * stddev / Math.sqrt(this.T);
    }

    /**
     * Run T MonteCarlo simulations for a size N Percolation system
     *
     * @return int[] representing the results of the T simulations
     */
    private double[] runMonteCarlo(int N) {
        double[] results = new double[T];
        for (int i = 0; i < T; i++) {
            results[i] = runMonteCarloInstance(N);
        }
        return results;
    }

    /**
     * Runs one MonteCarlo simulation for a size N Percolation system
     *
     * @param N int size of percolation system
     * @return int representing the number of open sites required to make system percolate.
     */
    private double runMonteCarloInstance(int N) {
        Percolation percolation = new Percolation(N);
        double openSites = 0.0;
        while (!percolation.percolates()) {
            int i = StdRandom.uniform(1, N + 1);
            int j = StdRandom.uniform(1, N + 1);
            if (!percolation.isOpen(i, j)) {
                percolation.open(i, j);
                openSites++;
            }
        }
        return openSites / (N * N);
    }

    /**
     * Runs the percolation test T times for an N size Percolation system.
     * Prints out mean, stddev, and confidence intervals.
     *
     * @param args input array. First is size N, second is number of simulations, T.
     */
    public static void main(String[] args) {
        int N = StdIn.readInt();
        int T = StdIn.readInt();

        PercolationStats stats = new PercolationStats(N, T);
        StdOut.println("mean = " + stats.mean());
        StdOut.println("stddev = " + stats.stddev());
        StdOut.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}
