/*
Kyle Dorman
10/12/15.
Assignment 3 for coursera course
https://class.coursera.org/algs4partI-009

Examines 4 points at a time and checks whether they all lie on the same line segment,
returning all such line segments. To check whether the 4 points p, q, r, and s are collinear,
check whether the three slopes between p and q, between p and r, and between p and s are all equal.
 */

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegments;
    private Point[] points;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] inputPoints) {
        validateInputArray(inputPoints);

        lineSegments = new ArrayList<LineSegment>();

        for (int i = 0; i < this.points.length - 3; i++) {
            for (int j = i + 1; j < this.points.length - 2; j++) {
                for (int k = j + 1; k < this.points.length - 1; k++) {
                    for (int l = k + 1; l < this.points.length; l++) {
                        checkForLineSegment(i, j, k, l);
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    private void checkForLineSegment(int i, int j, int k, int l) {
        double slope1 = points[i].slopeTo(points[j]);
        double slope2 = points[i].slopeTo(points[k]);
        double slope3 = points[i].slopeTo(points[l]);

        if (slope1 != slope2 ||
            slope1 != slope3) {
            return;
        }

        Point[] sortedPoints = {points[i], points[j], points[k], points[l]};
        Arrays.sort(sortedPoints);

        lineSegments.add(new LineSegment(sortedPoints[0], sortedPoints[3]));
    }

    /**
     * Validate presence of point
     *
     * @param i int reference to point in points array
     * @throws NullPointerException
     */
    private void validatePoint(int i) {
        if (points[i] == null) {
            throw new NullPointerException("Point in array is null");
        }
    }

    /**
     * Validate presence of points and that the points are not the same
     *
     * @param i int reference to point in "points" array
     * @param j int reference to point in "points" array
     * @throws NullPointerException and IllegalArgumentException
     */
    private void validatePoints(int i, int j) {
        validatePoint(i);
        validatePoint(j);
        if (points[i].compareTo(points[j]) == 0) {
            throw new IllegalArgumentException("Point1 " + points[i].toString() + " is equal to " + points[j].toString());
        }
    }

    /**
     * Validate presence of input array
     * @param points array reference
     * @throws NullPointerException
     */
    private void validateInputArray(Point[] inputPoints) {
        if (inputPoints == null) {
            throw new NullPointerException("Input array is null");
        }
        if (inputPoints.length < 1) {
            throw new NullPointerException("Input array is smaller than 4");
        }

        this.points = inputPoints.clone();

        Arrays.sort(this.points);

        if (points.length > 1) {
            for (int i = 0; i < this.points.length - 1; i++)
            validatePoints(i, i + 1);
        }
    }
}
