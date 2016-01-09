/**
 * Created by kyledorman on 11/1/15.
 */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private int size = 0;
    private Node2D root;
    private double minX = Double.POSITIVE_INFINITY;
    private double maxX = Double.NEGATIVE_INFINITY;
    private double minY = Double.POSITIVE_INFINITY;
    private double maxY = Double.NEGATIVE_INFINITY;

    // construct an empty set of points
    public KdTree() { }

    // is the set empty?
    public boolean isEmpty() { return size == 0; }

    // number of points in the set
    public int size() { return size; }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validatePoint(p);

        if (root == null) {
            root = new Node2D(p, 0);
            size++;
            updateRect(p);
            return;
        }

        Node2D current = root;
        Node2D next = current.next(p);
        boolean samePoint = current.getValue().compareTo(p) == 0;

        while (next != null && !samePoint) {
            current = next;
            next = current.next(p);
            samePoint = current.getValue().compareTo(p) == 0;
        }

        if (!samePoint) {
            current.add(p);
            size++;
            updateRect(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validatePoint(p);

        Node2D current = root;
        Node2D next;
        if (root == null) {
            next = null;
        } else {
            next = root.next(p);
        }

        boolean samePoint = root != null && root.getValue().compareTo(p) == 0;

        while (next != null && !samePoint) {
            current = next;
            next = current.next(p);
            samePoint = current.getValue().compareTo(p) == 0;
        }

        return samePoint;
    }

    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        validateRect(rect);
        Stack<Point2D> stack = new Stack<Point2D>();

        if (root != null) {
            root.containedIn(rect, stack, new RectHV(minX, minY, maxX, maxY));
        }

        return stack;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validatePoint(p);
        if (root == null) return null;

        return null;

//        Node2D current = root;
//        Point2D nearest = root.getValue();
//
//        while (current != null) {
//
//        }
    }

    private class Node2D {
        private Node2D left;
        private Node2D right;
        private Point2D value;
        private int height;

        public Node2D(Point2D p, int h) {
            this.value = p;
            this.height = h;
        }

        public Node2D getLeft() { return left; }

        public Node2D getRight() { return right; }

        public Point2D getValue() { return value; }

        public void add(Point2D p) {
            Node2D n = new Node2D(p, height + 1);

            if (height % 2 == 0) {
                if (p.x() < getValue().x()) {
                    left = n;
                } else {
                    right = n;
                }
            } else {
                if (p.y() < getValue().y()) {
                    left = n;
                } else {
                    right = n;
                }
            }
        }

        public Node2D next(Point2D p) {
            if (value.compareTo(p) == 0) return this;

            if (height % 2 == 0) {
                if (p.x() < getValue().x()) {
                    return getLeft();
                } else {
                    return  getRight();
                }
            } else {
                if (p.y() < getValue().y()) {
                    return getLeft();
                } else {
                    return getRight();
                }
            }
        }

        public void containedIn(RectHV inputRect, Stack<Point2D> stack, RectHV nodeRect) {
            if (inputRect.contains(value)) { stack.push(value); }

            if (!nodeRect.intersects(inputRect)) { return; }

            if (height % 2 == 0) {
                if (left != null) {
                    RectHV leftRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), value.x(), nodeRect.ymax());
                    left.containedIn(inputRect, stack, leftRect);
                }
                if (right != null) {
                    RectHV rightRect = new RectHV(value.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());
                    right.containedIn(inputRect, stack, rightRect);
                }
            } else {
                if (left != null) {
                    RectHV leftRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), value.y());
                    left.containedIn(inputRect, stack, leftRect);
                }
                if (right != null) {
                    RectHV rightRect = new RectHV(nodeRect.xmin(), value.y(), nodeRect.xmax(), nodeRect.ymax());
                    right.containedIn(inputRect, stack, rightRect);
                }
            }
        }

        public Point2D nearest(Point2D p, Point2D currentNearest) {
            return value;
        }
    }

    private void updateRect(Point2D p) {
        if (minX > p.x()) { minX = p.x(); }
        if (minY > p.y()) { minY = p.y(); }
        if (maxX < p.x()) { maxX = p.x(); }
        if (maxY < p.y()) { maxY = p.y(); }
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
    public static void main(String[] args) {

    }
}