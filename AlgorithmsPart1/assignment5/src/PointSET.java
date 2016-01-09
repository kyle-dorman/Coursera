/**
 * Created by kyledorman on 11/1/15.
 */
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() { return set.isEmpty(); }

    // number of points in the set
    public int size() { return set.size(); }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validatePoint(p);

        if (!set.contains(p)) set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validatePoint(p);
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : set) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        validateRect(rect);
        Stack<Point2D> stack = new Stack<>();

        for (Point2D p : set) {
            if (rect.contains(p)) stack.push(p);
        }
        return stack;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validatePoint(p);

        double distance = Double.POSITIVE_INFINITY;
        double iterableDistance;
        Point2D result = null;

        for (Point2D point : set) {
            iterableDistance = point.distanceTo(p);
            if (iterableDistance < distance) {
                distance = iterableDistance;
                result = point;
            }
        }
        return result;
    }

    private void validatePoint(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Invalid Point");
        }
    }

    private void validateRect(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException("Invalid Rect");
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[]args) {

    }
}