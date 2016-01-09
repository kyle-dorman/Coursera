/*
Kyle Dorman
10/12/15.
Assignment 3 for coursera course
https://class.coursera.org/algs4partI-009

Given a set of N distinct points in the plane, find every (maximal) line segment that
connects a subset of 4 or more of the points.


 */
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertcal;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * (y1 − y0) / (x1 − x0)
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     * @throws NullPointerException when that is null
     */
    public double slopeTo(Point that) {
        validatePresence(that);

        if (this.y == that.y) {
            if (this.x == that.x) {
                return Double.NEGATIVE_INFINITY;
            }
            return +0.0;
        } else if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        }

        double heightDifference = that.y - this.y;

        return heightDifference / (that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        validatePresence(that);

        if (that.y > this.y || that.y == this.y && that.x > this.x) {
            return -1;
        } else if (this.y > that.y || this.y == that.y && this.x > that.x) {
            return 1;
        }
        return 0;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeComparator(this);
    }

    private class SlopeComparator implements Comparator<Point> {
        private final Point invokingPoint;

        public SlopeComparator(Point invokingPoint) {
            this.invokingPoint = invokingPoint;
        }

        public int compare(Point point1, Point point2) {
            validatePresence(point1);
            validatePresence(point2);

            if (invokingPoint.slopeTo(point2) == invokingPoint.slopeTo(point1)) {
                return 0;
            } else if (invokingPoint.slopeTo(point1) > invokingPoint.slopeTo(point2)) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    private void validatePresence(Point point) {
        if (point == null) {
            throw new NullPointerException();
        }
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}
