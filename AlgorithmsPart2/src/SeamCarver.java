import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

/**
 * Created by kyledorman on 12/6/15.
 */
public class SeamCarver {
    private int[][] pixels;
    private int width;
    private int height;
    private boolean inverted = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new NullPointerException("null picture.");
        }
        height = picture.height();
        width = picture.width();
        pixels = new int[height][width];


        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                pixels[h][w] = picture.get(w, h).getRGB();
            }
        }
    }

    // current picture
    public Picture picture() {
        if (inverted) invert();

        Picture newPicture = new Picture(width, height);
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                Color c = new Color(pixels[h][w]);
                newPicture.set(w, h, c);
            }
        }
        return newPicture;
    }

    // width of current picture
    public int width() {
        if (inverted) return height;
        return width;
    }

    // height of current picture
    public int height() {
        if (inverted) return width;
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        verifyPoint(x, y);
        if (inverted) return calculateEnergy(y, x);
        else return calculateEnergy(x, y);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!inverted) invert();
        return findGeneralSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (inverted) invert();
        return findGeneralSeam();
    }

    private int[] findGeneralSeam() {
        boolean[][] marked = new boolean[height][width];
        double[][] distTo = new double[height][width];
        Double[][] energy = new Double[height][width];
        // inverted so we can return the whole row at the end
        int[][] nextPoint = new int[height][width];

        double minValue = Double.POSITIVE_INFINITY;
        int rowIndex = -1;

        for (int w = 0; w < width; w++) {
            energy[0][w] = calculateEnergy(w, 0);
            dfs(0, w, marked, distTo, nextPoint, energy);

            if (minValue > distTo[0][w]) {
                minValue = distTo[0][w];
                rowIndex = w;
            }
        }

        int[] result = new int[height];
        result[0] = rowIndex;
        for (int i = 0; i < height - 1; i++) {
            rowIndex = nextPoint[i][rowIndex];
            result[i + 1] = rowIndex;
        }

        return result;
    }

    private void dfs(int h, int w, boolean[][] marked, double[][] distTo, int[][] nextPoint, Double[][] energy) {
        if (!marked[h][w]) {
            marked[h][w] = true;

            for (Point p : adj(h, w)) {
                dfs(p.getY(), p.getX(), marked, distTo, nextPoint, energy);
            }

            double minDistTo = Double.POSITIVE_INFINITY;
            int next = -1;
            for (Point p : adj(h, w)) {
                double pointEnergy = distTo[p.getY()][p.getX()];
                if (minDistTo > pointEnergy) {
                    minDistTo = pointEnergy;
                    next = p.getX();
                }
            }

            if (minDistTo == Double.POSITIVE_INFINITY) {
                minDistTo = 0.0; // for bottom row
                next = w;
            }

            if (energy[h][w] == null) {
                energy[h][w] = calculateEnergy(w, h);
            }
            distTo[h][w] = energy[h][w] + minDistTo;
            nextPoint[h][w] = next;
        }
    }

    private Point[] adj(int h, int w) {
        if (h == height - 1 || width == 1) {
            return new Point[] {};
        } else if (w == 0) {
            return new Point[] { new Point(w, h + 1), new Point(w + 1, h + 1) };
        } else if (w == width - 1) {
            return new Point[] { new Point(w - 1, h + 1), new Point(w, h + 1) };
        } else {
            return new Point[] { new Point(w - 1, h + 1), new Point(w, h + 1), new Point(w + 1, h + 1) };
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!inverted) invert();
        generalRemoveSeam(seam);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (inverted) invert();
        generalRemoveSeam(seam);
    }

    private void generalRemoveSeam(int[] seam) {
        verifyRemoveSeam(seam);
        int previousPoint = -1;
//        ArrayList<Point> pointsToRecalculate = new ArrayList<Point>();

        for (int i = 0; i < height; i++) {
            // validate point
            verifySeamPoint(seam[i], previousPoint);

            int index = seam[i];
            int[] pixelRow = pixels[i];
            System.arraycopy(pixelRow, index + 1, pixelRow, index, width - index - 1);
            previousPoint = index;
        }
        width--;
    }

    private void invert() {
        int[][] pixelsClone = new int[width][height];

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                pixelsClone[w][h] = pixels[h][w];
            }
        }

        int hold = width;
        width = height;
        height = hold;
        inverted = !inverted;

        pixels = pixelsClone;
    }

    private double calculateEnergy(int x, int y) {
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            return 1000.0;
        } else {
            int left = pixels[y][x - 1];
            int right = pixels[y][x + 1];
            int above = pixels[y - 1][x];
            int below = pixels[y + 1][x];

            double energyX = getEnergyGradient(left, right);
            double energyY = getEnergyGradient(above, below);

            return Math.sqrt(energyX + energyY);
        }
    }

    private double getEnergyGradient(int lhs, int rhs) {
        double red = (getRed(lhs) - getRed(rhs)) * (getRed(lhs) - getRed(rhs));
        double blue = (getBlue(lhs) - getBlue(rhs)) * (getBlue(lhs) - getBlue(rhs));
        double green = (getGreen(lhs) - getGreen(rhs)) * (getGreen(lhs) - getGreen(rhs));
        return red + blue + green;
    }

    private void verifyPoint(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IndexOutOfBoundsException("Invalid point. x:" + x + " y:" + y + ".");
        }

        if (!inverted) {
            if (x > width - 1 || y > height - 1) {
                throw new IndexOutOfBoundsException("Invalid point. x:" + x + " y:" + y + ".");
            }
        } else {
            if (y > width - 1 || x > height - 1) {
                throw new IndexOutOfBoundsException("Invalid point. x:" + y + " y:" + x + ".");
            }
        }
    }

    private void verifyRemoveSeam(int[] seam) {
        if (width <= 1) {
            String s;
            if (inverted) s = "height";
            else s = "width";
            throw new IllegalArgumentException("Illegal seam removal. Seam has " + s + " of 1.");
        }

        if (seam == null) {
            throw new NullPointerException("null seam.");
        }

        if (seam.length != height) {
            throw new IllegalArgumentException("Seam is not the correct height");
        }
    }

    private void verifySeamPoint(int p, int previousPoint) {
        if (p < 0 || p > width - 1) {
            throw new IllegalArgumentException("seam value " + p + " is outside Picture.");
        }
        if (previousPoint != -1 && Math.abs(previousPoint - p) > 1) {
            throw new IllegalArgumentException("two adjacent entries differ by more than 1.");
        }
    }

    private int getRed(int colorInt) {
        return (colorInt >> 16) & 0xFF;
    }

    private int getGreen(int colorInt) {
        return (colorInt >> 8) & 0xFF;
    }

    private int getBlue(int colorInt) {
        return (colorInt >> 0) & 0xFF;
    }

    private final class Point {
        private final int x;
        private final int y;

        Point(int intX, int intY) {
            this.x = intX;
            this.y = intY;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public static void main(String[] args) {
        Picture p = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(p);
        int[] svp;
        int[] shp;

        svp = sc.findVerticalSeam();

        for (int i : svp) {
            StdOut.println(i);
        }
//        svp = new int[]{ -1, 0, 0, 1, 0, 1, 2, 2, 2, 3 };
//        sc.removeVerticalSeam(svp);
//
        shp = sc.findHorizontalSeam();
        StdOut.println(shp.length);
        sc.removeHorizontalSeam(shp);
//
        svp = sc.findVerticalSeam();
        sc.removeVerticalSeam(svp);
//
        shp = sc.findHorizontalSeam();
        sc.removeHorizontalSeam(shp);
//
////        StdOut.println(sc.picture());
//        StdOut.println(sc.width());
//        StdOut.println(sc.height());
//        StdOut.println(sc.energy(0, 0));
    }
}
