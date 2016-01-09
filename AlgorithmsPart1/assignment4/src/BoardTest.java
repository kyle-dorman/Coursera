import edu.princeton.cs.algs4.StdOut;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by kyledorman on 10/25/15.
 */
public class BoardTest {
    private Board solved;
    private int[][] solvedArray;
    private Board solved2;
    private Board unsolved;
    private int[][] unsolvedArray;
    private Board unsolvedMiddle;
    private int[][] unsolvedMiddleArray;

    @Before
    public void setUp() throws Exception {
        solvedArray = new int[][] {
                new int[] { 1, 2, 3},
                new int[] { 4, 5, 6},
                new int[] { 7, 8, 0}
        };

        solved = new Board(solvedArray);

        solved2 = new Board(solvedArray);

        unsolvedArray = new int[][] {
                new int[] { 0, 2, 3},
                new int[] { 4, 5, 6},
                new int[] { 7, 8, 1}
        };

        unsolved = new Board(unsolvedArray);

        unsolvedMiddleArray = new int[][] {
                new int[] { 8, 1, 3},
                new int[] { 7, 0, 2},
                new int[] { 6, 4, 5}
        };

        unsolvedMiddle = new Board(unsolvedMiddleArray);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSolvedManhattanScore() throws Exception {
//        StdOut.println("Solved:");
//        StdOut.println(solved.toString());
        assertEquals(0, solved.manhattan());
    }

    @Test
    public void testSolvedHammingScore() throws Exception {
        assertEquals(0, solved.hamming());
    }

//    @Test
//    public void testSolvedToString() throws Exception {
//        assertEquals("3 \n 1 2 3 \n 4 5 6 \n 7 8 0", solved.toString());
//    }

    @Test
    public void testUnsolvedIsGoal() throws Exception {
        assertEquals(true, solved.isGoal());
    }

    @Test
    public void testUnsolvedManhattanScore() throws Exception {
//        StdOut.println("Unsolved:");
//        StdOut.println(unsolved.toString());
        assertEquals(4, unsolved.manhattan());
    }

    @Test
    public void testUnsolvedHammingScore() throws Exception {
        assertEquals(1, unsolved.hamming());
    }

    @Test
    public void testUnsolvedIsNotGoal() throws Exception {
        assertEquals(false, unsolved.isGoal());
    }

//    @Test
//    public void testUnsolvedToString() throws Exception {
//        assertEquals("3 \n 0 2 3 \n 4 5 6 \n 7 8 1", solved.toString());
//    }

    @Test
    public void testIsEqualToSelf() throws Exception {
        assertEquals(true, solved.equals(solved));
    }

    @Test
    public void testIsEqualDuplicate() throws Exception {
        assertEquals(true, solved.equals(solved2));
    }

    @Test
    public void testIsEqualNotEqual() throws Exception {
        assertEquals(false, solved.equals(unsolved));
    }

    @Test
    public void testIsEqualOtherObject() throws Exception {
        assertEquals(false, unsolved.equals(new Double(4)));
    }

    @Test
    public void testDimension() throws Exception {
        assertEquals(3, solved.dimension());
    }

    @Test
    public void testTwin() throws Exception {
        Board twin = solved.twin();
        assertEquals(false, solved.equals(twin));
    }

    @Test
    public void testTwinUnsolved() throws Exception {
        Board twin = unsolved.twin();
        assertEquals(false, unsolved.equals(twin));
    }

    @Test
    public void testNeighborsSolved() throws Exception {
        Iterable<Board> neighbors = solved.neighbors();
        int count = 0;
        for (Board neighbor : neighbors) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testNeighborsUnsolved() throws Exception {
        Iterable<Board> neighbors = unsolved.neighbors();
        int count = 0;
        for (Board neighbor : neighbors) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testNeighborsUnsolvedMiddle() throws Exception {
        Iterable<Board> neighbors = unsolvedMiddle.neighbors();
        int count = 0;
        for (Board neighbor : neighbors) {
            count++;
        }
        assertEquals(4, count);
    }


}
