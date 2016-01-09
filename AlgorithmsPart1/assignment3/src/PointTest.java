import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import java.util.Comparator;

/**
 * Created by kdorman on 10/12/15.
 *
 * FROM: ~/Documents/Algorithms-Part-1/assignment3/src
 * javac Point.java PointTest.java -Xlint:deprecation
 * java org.junit.runner.JUnitCore PointTest
 */
public class PointTest {
    private Point zero;
    private Point oneOne;
    private Point zeroOne;
    private Point oneZero;
    private Point twoTwo;

    @Before
    public void setUp() throws Exception {
        zero = new Point(0, 0);
        oneOne = new Point(1, 1);
        zeroOne = new Point(0, 1);
        oneZero = new Point(1, 0);
        twoTwo = new Point(2, 2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = NullPointerException.class)
    public void testSlopeToNullPoint() throws Exception {
        zero.slopeTo(null);
    }

    @Test
    public void testSlopeTo() throws Exception {
        assertEquals(zero.slopeTo(zero), Double.NEGATIVE_INFINITY, 0.01);

        assertEquals(zero.slopeTo(zeroOne), Double.POSITIVE_INFINITY, 0.01);

        assertEquals(zero.slopeTo(oneZero), 0.0, 0.01);

        assertEquals(zero.slopeTo(oneOne), 1.0, 0.01);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToNull() throws Exception {
        zero.compareTo(null);
    }

    @Test
    public void testCompareToEqual() throws Exception {
        Point p = new Point(0, 0);
        assertEquals(0, p.compareTo(p));
    }

    @Test
    public void testCompareToThatYGreater() throws Exception {
        assertEquals(-1, zero.compareTo(zeroOne));
    }

    @Test
    public void testCompareToThatYLess() throws Exception {
        assertEquals(1, zeroOne.compareTo(zero));
    }

    @Test
    public void testCompareToYEqualThatXGreater() throws Exception {
        assertEquals(-1, zeroOne.compareTo(oneOne));
    }

    @Test
    public void testCompareToYEqualThatXLess() throws Exception {
        assertEquals(1, oneOne.compareTo(zeroOne));
    }

    @Test(expected = NullPointerException.class)
    public void testSlopeOrderNullFirstItem() throws Exception {
        Comparator<Point> comparator = zero.slopeOrder();

        comparator.compare(null, oneOne);
    }

    @Test(expected = NullPointerException.class)
    public void testSlopeOrderNullSecondItem() throws Exception {
        Comparator<Point> comparator = zero.slopeOrder();

        comparator.compare(oneOne, null);
    }

    @Test
    public void testSlopeOrderLessThan() throws Exception {
        Comparator<Point> comparator = zero.slopeOrder();

        // oneOne: 1, oneZero: 0; 0 < 1
        assertEquals(-1, comparator.compare(oneZero, oneOne));
    }

    @Test
    public void testSlopeOrderGreaterThan() throws Exception {
        Comparator<Point> comparator = zero.slopeOrder();

        // oneOne: 1, oneZero: 0; 1 > 0
        assertEquals(1, comparator.compare(oneOne, oneZero));
    }

    @Test
    public void testSlopeOrderGreaterThanInfinity() throws Exception {
        Comparator<Point> comparator = zero.slopeOrder();

        // zeroOne: Infinity, oneZero: 0; 0 < Infinity. Infinity = 2147483647 in int
        assertEquals(-1, comparator.compare(oneZero, zeroOne));
    }

    @Test
    public void testSlopeOrderGreaterThanNegativeInfinity() throws Exception {
        Comparator<Point> comparator = zero.slopeOrder();

        // zero: Negative_Infinity, oneZero: 0; 0 > NegativeInfinity
        assertEquals(1, comparator.compare(oneZero, zero));
    }

    @Test
    public void testSlopeOrderSameSlope() throws Exception {
        Comparator<Point> comparator = zero.slopeOrder();

        // zero: Negative_Infinity, oneZero: 0; NegativeInfinity - 0
        assertEquals(0, comparator.compare(oneOne, twoTwo));
    }
}