/*
Kyle Dorman
10/12/15.
Assignment 3 for coursera course
https://class.coursera.org/algs4partI-009


 */

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private Point[] points;
    private ArrayList<LineSegment> lineSegments;
    private ArrayList<SlopeStartEnd> slopeStartEnd = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] inputPoints) {
        validateInputArray(inputPoints);

        for (int i = 0; i < points.length - 1; i++) {
            checkForLineSegment(i);
        }

        lineSegments = new ArrayList<>();

        if (slopeStartEnd.size() == 0) {
            return;
        }

        // sort line segments by slope and starting point. similar slopes will group
        // and then we can eliminate sub segments
        SlopeStartEnd[] sortedSlopes = slopeStartEnd.toArray(new SlopeStartEnd[slopeStartEnd.size()]);
        Arrays.sort(sortedSlopes);

        SlopeStartEnd current = sortedSlopes[0];
        lineSegments.add(sortedSlopes[0].createLineSegment());
        int allSegmentsCount = slopeStartEnd.size();

        for (int i = 1; i < allSegmentsCount; i++) {
            if (sortedSlopes[i].slope() != current.slope() ||
                    sortedSlopes[i].endPoint().compareTo(current.endPoint()) != 0) {

                current = sortedSlopes[i];
                lineSegments.add(sortedSlopes[i].createLineSegment());
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

    private void checkForLineSegment(int i) {
        Point[] sortedPoints = new Point[points.length - i - 1];

        Point start = points[i];
        for (int j = i + 1; j < points.length; j++) {
            sortedPoints[j - i - 1] = points[j];
        }
        Arrays.sort(sortedPoints, start.slopeOrder());

        double currentSlope = start.slopeTo(sortedPoints[0]);
        int counter = 1;
        double slope;

        for (int j = 1; j < sortedPoints.length; j++) {
            slope = start.slopeTo(sortedPoints[j]);

            if (currentSlope != slope) {
                Point end = sortedPoints[j - 1];
                if (counter >= 3) {
                    addPoint(start, end);
                }
                currentSlope = slope;
                counter = 1;
            } else {
                counter++;
            }
        }

        Point end = sortedPoints[sortedPoints.length - 1];
        if (counter >= 3) {
            addPoint(start, end);
        }
    }

    private void addPoint(Point start, Point end) {
        double slope = start.slopeTo(end);

        slopeStartEnd.add(new SlopeStartEnd(slope, start, end));
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
     * Validate presence of input array
     * @throws NullPointerException and IllegalArgumentException
     */
    private void validateInputArray(Point[] inputPoints) {
        if (inputPoints == null) {
            throw new NullPointerException("Input array is null");
        }
        if (inputPoints.length < 1) {
            throw new NullPointerException("Input array is smaller than 4");
        }
        this.points = inputPoints.clone();
        Arrays.sort(points);

        for (int i = 0; i < points.length - 1; i++) {
            validatePoint(i);
            if (points[i].compareTo(points[i + 1]) == 0) {
                String ps1 = points[i].toString();
                String ps2 = points[i + 1].toString();
                throw new IllegalArgumentException("Point1 " + ps1 + " is equal to " + ps2);
            }
        }
        validatePoint(points.length - 1);
    }

    private class SlopeStartEnd implements Comparable<SlopeStartEnd> {
        private final double slope;
        private final Point start;
        private final Point end;

        public SlopeStartEnd(double inputSlope, Point startPoint, Point endPoint) {
            slope = inputSlope;
            start = startPoint;
            end = endPoint;
        }

        public int compareTo(SlopeStartEnd that) {
            if (Double.compare(this.slope, that.slope) == 1) {
                return 1;
            } else if (Double.compare(this.slope, that.slope) == -1) {
                return -1;
            } else {
                if (this.end.compareTo(that.end) != 0) {
                    return this.end.compareTo(that.end);
                } else {
                    return this.start.compareTo(that.start);
                }
            }
        }

        public double slope() {
            return slope;
        }

        public LineSegment createLineSegment() {
            return new LineSegment(start, end);
        }

        public Point endPoint() {
            return end;
        }
    }
}
