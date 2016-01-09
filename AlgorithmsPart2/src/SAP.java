import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Kyle Dorman on 11/27/15.
 */
public class SAP {
    private Digraph graph;
//    private HashMap<Integer, BreadthFirstDirectedPaths> cache;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        validatePresence(G);
        graph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Bag<Integer> bagV = new Bag<Integer>();
        bagV.add(v);
        Bag<Integer> bagW = new Bag<Integer>();
        bagW.add(w);
        return length(bagV, bagW);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Bag<Integer> bagV = new Bag<Integer>();
        bagV.add(v);
        Bag<Integer> bagW = new Bag<Integer>();
        bagW.add(w);
        return ancestor(bagV, bagW);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validatePresence(v);
        validatePresence(w);

        Hefty h = new Hefty(graph, v, w);
        return h.getShortestPath();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validatePresence(v);
        validatePresence(w);

        Hefty h = new Hefty(graph, v, w);
        return h.getAncestor();
    }

    private void validatePresence(Digraph G) {
        if (G == null) {
            throw new NullPointerException();
        }
    }

    private void validatePresence(Iterable<Integer> i) {
        if (i == null) {
            throw new NullPointerException();
        }
    }

    private static class Hefty {
        private BreadthFirstDirectedPaths bfdpV;
        private BreadthFirstDirectedPaths bfdpW;
        private int ancestor;
        private int shortestPath;

        public Hefty(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {
            validate(G, v, w);

            bfdpV = new BreadthFirstDirectedPaths(G, v);
            bfdpW = new BreadthFirstDirectedPaths(G, w);
            ancestor = -1;
            shortestPath = Integer.MAX_VALUE;
            Bag<Integer> ancestors = new Bag<Integer>();

            for (int i = 0; i < G.V(); i++) {
                if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                    ancestors.add(i);
                }
            }

            for (int i : ancestors) {
                if ((bfdpV.distTo(i) + bfdpW.distTo(i)) < shortestPath) {
                    shortestPath = (bfdpV.distTo(i) + bfdpW.distTo(i));
                    ancestor = i;
                }
            }

            if (ancestor == -1) {
                shortestPath = -1;
            }
        }

        public int getAncestor() {
            return ancestor;
        }

        public int getShortestPath() {
            return shortestPath;
        }

        private void validate(Digraph G, int v) {
            if (v < 0 || v > G.V() - 1) {
                throw new IndexOutOfBoundsException();
            }
        }

        private void validate(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {
            for (int integer : w) { validate(G, integer); }
            for (int integer : v) { validate(G, integer); }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
