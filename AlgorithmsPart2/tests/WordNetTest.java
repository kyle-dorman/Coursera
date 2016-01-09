import edu.princeton.cs.algs4.StdOut;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//import assignment6;

import static org.junit.Assert.assertEquals;

/**
 * Created by kdorman on 11/27/15.
 */
public class WordNetTest {
    String basePath = "../../resources/wordnet/";

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test100SubGraph() throws Exception {
        WordNet wordNet100 = new WordNet(basePath + "synsets100-subgraph.txt", basePath + "hypernyms100-subgraph.txt");
        StdOut.print(wordNet100.nouns());
    }
}
