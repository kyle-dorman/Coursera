import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kdorman on 11/27/15.
 */
public class WordNet {
    private Map<String, Bag<Integer>> nounList = new HashMap<String, Bag<Integer>>();
    private Map<Integer, String> synsetList = new HashMap<Integer, String>();
    private SAP relationships;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        validatePresence(synsets);
        validatePresence(hypernyms);

        int setCount = 0;
        In synsetsIn = new In(synsets);
        In hypernumsIn = new In(hypernyms);

        while (synsetsIn.hasNextLine()) {
            parseSynsetsLine(synsetsIn.readLine());
            setCount++;
        }

        Digraph G = new Digraph(setCount);
        while (hypernumsIn.hasNextLine()) {
            parseHypernumLine(G, hypernumsIn.readLine());
        }
        validateDigraph(G);

        relationships = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounList.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        validatePresence(word);
        return nounList.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validatePresence(nounA);
        validatePresence(nounB);
        validateNoun(nounA);
        validateNoun(nounB);

        return relationships.length(nounList.get(nounA), nounList.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validatePresence(nounA);
        validatePresence(nounB);
        validateNoun(nounA);
        validateNoun(nounB);

        return synsetList.get(relationships.ancestor(nounList.get(nounA), nounList.get(nounB)));
    }

    private void validatePresence(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
    }

    private void validateNoun(String str) {
        if (!isNoun(str)) {
            throw new IllegalArgumentException(str + " is not in nounList");
        }
    }

    private  void validateDigraph(Digraph G) {
        if ((new DirectedCycle(G)).hasCycle()) {
            throw new IllegalArgumentException("Not a DAG");
        }

        int rooted = 0;
        for (int i = 0; i < G.V(); i++) {
            if (!G.adj(i).iterator().hasNext()) {
                rooted++;
            }
        }

        if (rooted != 1) {
            throw new IllegalArgumentException("Not a rooted DAG");
        }

    }

    private void parseSynsetsLine(String str) {
        String[] synsetLine = str.split(",");
        int id = Integer.parseInt(synsetLine[0]);
        synsetList.put(id, synsetLine[1]);

        for (String word : synsetLine[1].split("\\s+")) {
            if (nounList.containsKey(word)) {
                nounList.get(word).add(id);
            } else {
                Bag<Integer> s = new Bag<Integer>();
                s.add(id);
                nounList.put(word, s);
            }
        }
    }

    private void parseHypernumLine(Digraph G, String str) {
        String[] hypernumLine = str.split(",");
        int id = Integer.parseInt(hypernumLine[0]);

        for (int i = 1; i < hypernumLine.length; i++) {
            int hyperId = Integer.parseInt(hypernumLine[i]);
            G.addEdge(id, hyperId);
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String basePath = "../resources/wordnet/";

        StdOut.println("Testing 100-subgroup:");
        WordNet wordNet100 = new WordNet(basePath + "synsets.txt", basePath + "hypernyms.txt");

        StdOut.println("Distance:");
        StdOut.println(wordNet100.distance("phosphor", "case_study"));
        StdOut.println("SAP:");
        StdOut.println(wordNet100.sap("phosphor", "case_study"));
    }
}
