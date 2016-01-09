import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by kdorman on 11/28/15.
 */
public class Outcast {
    private WordNet w;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        w = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDistance = Integer.MIN_VALUE;
        String outcastNoun = "NA";

        for (int i = 0; i < nouns.length; i++) {
            int total = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i != j) {
                    total += w.distance(nouns[i], nouns[j]);
                }
            }
            if (maxDistance < total) {
                maxDistance = total;
                outcastNoun = nouns[i];
            }
        }
        return outcastNoun;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}