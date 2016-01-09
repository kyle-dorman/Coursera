/**
 * Created by kyledorman on 10/19/15.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class BruteCollinearPointsTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullInout() throws Exception {
        new BruteCollinearPoints(null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorEmptyInput() throws Exception {
        Point[] points = {};
        new BruteCollinearPoints(points);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllSamePoints() throws Exception {
        Point[] points = {new Point(0, 0), new Point(0, 0), new Point(0, 0), new Point(0, 0), new Point(0, 0)};
        new BruteCollinearPoints(points);
    }

    @Test
    public void testConstructorOnePoint() throws Exception {
        Point[] points = {new Point(0, 0)};
        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        assertEquals(0, brute.numberOfSegments());
    }

    @Test
    public void testFourPointsInALine() throws Exception {
        Point[] points = {new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3)};
        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        assertEquals(1, brute.numberOfSegments());
    }

    @Test
    public void testTwoLines() throws Exception {
        Point[] points = {new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3),
                          new Point(0, 3), new Point(1, 2), new Point(2, 1), new Point(3, 0)};
        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        assertEquals(2, brute.numberOfSegments());
    }

    @Test
    public void testFiveLines() throws Exception {
        Point[] points = {new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4)};
        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        assertEquals(5, brute.numberOfSegments());
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

        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        assertEquals(10, brute.numberOfSegments());
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

        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        assertEquals(10428, brute.numberOfSegments());
    }

    @Test
    public void testNoLines() throws Exception {
        Point[] points = {new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(0, 3)};
        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        assertEquals(0, brute.numberOfSegments());
    }
}
