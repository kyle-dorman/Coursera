/**
 * Kyle Dorman
 * 10/25/15
 *
 * Assignment 4 for Coursera course
 * https://class.coursera.org/algs4partI-009
 */

import java.util.ArrayList;

public class Board {
    private final int[][] blocks;
    private int totalManhattanDistance;
    private int hammingNumber;

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] b) {
        blocks = new int[b.length][b[0].length];
        hammingNumber = 0;
        totalManhattanDistance = 0;

        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                int value = b[i][j];
                blocks[i][j] = value;
                hammingNumber += hammingNumber(i, j, value);
                totalManhattanDistance += manhattanDistance(i, j, value);
            }
        }
    }

    // board dimension N
    public int dimension() {
        return blocks.length;
    }

    // number of blocks out of place
    public int hamming() {
        return hammingNumber;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return totalManhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hammingNumber == 0 && totalManhattanDistance == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[] firstSwap = new int[] { -1, -1 };
        int[] secondSwap = new int[] { -1, -1 };

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                if (blocks[i][j] != 0) {
                    if (firstSwap[0] == -1) {
                        firstSwap = new int[] { i, j };
                    } else if (secondSwap[0] == -1) {
                        secondSwap = new int[] { i, j };
                    } else {
                        break;
                    }
                }
            }
        }
        swap(firstSwap[0], firstSwap[1], secondSwap[0], secondSwap[1]);
        Board twin = new Board(blocks);
        swap(firstSwap[0], firstSwap[1], secondSwap[0], secondSwap[1]);
        return twin;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == this) { return true; }

        if (y == null) { return false; }

        if (y.getClass() != this.getClass()) { return false; }

        Board that = (Board) y;

        if (this.dimension() != that.dimension()) { return false; }

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                if (that.blocks[i][j] != this.blocks[i][j]) { return false; }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> boardList = new ArrayList<>();
        int[] emptyLocation = new int[] { 0, 0 };

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] == 0) {
                    emptyLocation = new int[] { i, j };
                    break;
                }
            }
        }

        int emptyRow = emptyLocation[0];
        int emptyColumn = emptyLocation[1];

        int startI = Math.max(emptyRow - 1, 0);
        int endI = Math.min(emptyRow + 1, dimension() - 1);
        int startJ = Math.max(emptyColumn - 1, 0);
        int endJ = Math.min(emptyColumn + 1, dimension() - 1);

        for (int i = startI; i <= endI; i++) {
            for (int j = startJ; j <= endJ; j++) {
                if ((i == emptyRow || j == emptyColumn) && !(i == emptyRow && j == emptyColumn)) {
                    swap(emptyRow, emptyColumn, i, j);
                    boardList.add(new Board(blocks));
                    swap(emptyRow, emptyColumn, i, j);
                }
            }
        }

        return boardList;
    }

    // string representation of this board (in the output format specified below)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension());
        sb.append(" \n ");

        for (int[] row : blocks) {
            for (int value : row) {
                sb.append(String.format("%2d ", value));
            }
            sb.append("\n ");
        }
        return sb.toString();
    }

    // row i column j
    private int correctValue(int i, int j) {
        int d = dimension();
        if (i == d && j == d) { return 0; }
        return (i * d) + j + 1;
    }

    private int[] correctIndices(int value) {
        int alteredValue = value - 1;
        int d = dimension();
        if (value == 0) { return new int[] {d, d}; }

        double dValue = (double) alteredValue;
        int j = ((alteredValue) % d);
        int i = (int) Math.floor(dValue / d);
        return new int[] {i, j};
    }

    private int manhattanDistance(int i, int j, int value) {
        if (value == 0) { return 0; }
        int[] correctIndices = correctIndices(value);
        return Math.abs(i - correctIndices[0]) + Math.abs(j - correctIndices[1]);
    }

    // row i column j
    private int hammingNumber(int i, int j, int value) {
        if (value == 0 || correctValue(i, j) == value) { return 0; }

        return 1;
    }

    private void swap(int i, int j, int nextI, int nextJ) {
        int hold = blocks[i][j];
        blocks[i][j] = blocks[nextI][nextJ];
        blocks[nextI][nextJ] = hold;
    }

    // unit tests (not graded)
    public static void main(String[] args) {

    }
}