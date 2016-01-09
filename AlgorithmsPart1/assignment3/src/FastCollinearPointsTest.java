/**
 * Created by kyledorman on 10/19/15.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class FastCollinearPointsTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullInout() throws Exception {
        new FastCollinearPoints(null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorEmptyInput() throws Exception {
        Point[] points = {};
        new FastCollinearPoints(points);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllSamePoints() throws Exception {
        Point[] points = {new Point(0, 0), new Point(0, 0), new Point(0, 0), new Point(0, 0), new Point(0, 0)};
        new FastCollinearPoints(points);
    }

    @Test
    public void testConstructorOnePoint() throws Exception {
        Point[] points = {new Point(0, 0)};
        FastCollinearPoints fast = new FastCollinearPoints(points);
        assertEquals(0, fast.numberOfSegments());
    }

    @Test
    public void testFourPointsInALine() throws Exception {
        Point[] points = {new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3)};
        FastCollinearPoints fast = new FastCollinearPoints(points);
        assertEquals(1, fast.numberOfSegments());
    }

    @Test
    public void testTwoLines() throws Exception {
        Point[] points = {new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3),
                new Point(0, 3), new Point(1, 2), new Point(2, 1), new Point(3, 0)};
        FastCollinearPoints fast = new FastCollinearPoints(points);
        assertEquals(2, fast.numberOfSegments());
    }

    @Test
    public void testOneLongLine() throws Exception {
        Point[] points = {new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4),
                new Point(5, 5), new Point(6, 6), new Point(7, 7), new Point(8, 8), new Point(9, 9)};
        FastCollinearPoints fast = new FastCollinearPoints(points);
        assertEquals(1, fast.numberOfSegments());
    }

    @Test
    public void testManyPoints() throws Exception {
        ArrayList pointsList = new ArrayList<Point>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                pointsList.add(new Point(i, j));
            }
        }
        Point[] points = new Point[pointsList.size()];
        pointsList.toArray(points);

        FastCollinearPoints fast = new FastCollinearPoints(points);
        assertEquals(10, fast.numberOfSegments());
    }

    @Test
    public void testManyManyPoints() throws Exception {
        ArrayList pointsList = new ArrayList<Point>();

        for (int i = -5; i < 6; i++) {
            for (int j = -5; j < 6; j++) {
                pointsList.add(new Point(i, j));
            }
        }
        Point[] points = new Point[pointsList.size()];
        pointsList.toArray(points);

        FastCollinearPoints fast = new FastCollinearPoints(points);
        assertEquals(232, fast.numberOfSegments());
    }

    @Test
    public void testNoLines() throws Exception {
        Point[] points = {new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(0, 3)};
        FastCollinearPoints fast = new FastCollinearPoints(points);
        assertEquals(0, fast.numberOfSegments());
    }

    @Test
    public void testinput10() throws Exception {
        Point[] points = {
                new Point(4000, 30000), new Point(3500, 28000), new Point(3000, 26000), new Point(2000, 22000),
                new Point(1000, 18000), new Point(13000, 21000), new Point(23000, 16000), new Point(28000, 13500),
                new Point(28000, 5000), new Point(28000, 1000)
        };
        FastCollinearPoints fast = new FastCollinearPoints(points);
        assertEquals(2, fast.numberOfSegments());

        String segment1 = fast.segments()[0].toString();
        String expectedSegment1 = "(28000, 13500) -> (3000, 26000)";
        String segment2 = fast.segments()[1].toString();
        String expectedSegment2 = "(1000, 18000) -> (4000, 30000)";

        assertEquals(expectedSegment1, segment1);
        assertEquals(expectedSegment2, segment2);
    }

    @Test
    public void testinput20() throws Exception {
        Point[] points = {
                new Point(4096, 20992), new Point(5120, 20992), new Point(6144, 20992), new Point(8128, 20992),
                new Point(4096, 22016), new Point(4096, 23040), new Point(4096, 24064), new Point(4096, 25088),
                new Point(5120, 25088), new Point(7168, 20992), new Point(6144, 29184), new Point(7168, 29184),
                new Point(8192, 28160), new Point(8192, 29184), new Point(4160, 29184), new Point(5120, 29184),
                new Point(7168, 25088), new Point(8192, 25088), new Point(8192, 26112), new Point(8192, 27136),
        };

        FastCollinearPoints fast = new FastCollinearPoints(points);
        assertEquals(5, fast.numberOfSegments());
    }
}
