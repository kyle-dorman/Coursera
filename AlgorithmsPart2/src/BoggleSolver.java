import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Kyle Dorman
 * Assignment 9
 * BoggleSolver
 *
 * Find all possible words on a Boggle board.
 */
public class BoggleSolver
{
    private static final char Q = 'Q';
    private static final String QU = "QU";
    private KDTrie D = new KDTrie();
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String s : dictionary) {
            KDStringArray sa = new KDStringArray(s.length());
            sa.add(s);
            D.put(sa);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        KDTrie validWords = new KDTrie();
        boolean[][] visited = new boolean[board.rows()][board.cols()];

        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                search(row, col, board, new KDStringArray(board.cols() * board.rows()), visited, validWords);
            }
        }
        return validWords.keys();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word.length() < 3 || !D.contains(word)) {
            return 0;
        }
        switch (word.length()) {
            case 3: return 1;
            case 4: return 1;
            case 5: return 2;
            case 6: return 3;
            case 7: return 5;
            default: return 11;
        }
    }

    private void search(int row, int col, BoggleBoard board, KDStringArray word,
                        boolean[][] visited, KDTrie validWords) {
        if (visited[row][col]) { return; }

        visited[row][col] = true;
        String letters = getLetter(row, col, board);
        word.add(letters);

        if (word.length() > 0) {
        /*
            Transaction values:
            odd has child
            even no children
            greater than 1, match
            less than 1, no match
         */
            int transaction = D.longestPrefixOf(word);
            if (transaction == -1) {
                word.pop();
                visited[row][col] = false;
                return;
            } else if (transaction > 1) {
                validWords.put(word);
            }
            // has no children
            if (transaction != 1 && transaction != 3) {
                word.pop();
                visited[row][col] = false;
                return;
            }
        }

        for (int[] point : adj(row, col, board.rows(), board.cols())) {
            if (point[0] != -1) {
                if (!visited[point[0]][point[1]]) {
                    search(point[0], point[1], board, word, visited, validWords);
                }
            }
        }

        word.pop();
        visited[row][col] = false;
    }

    private int[][] adj(int row, int col, int maxRow, int maxCol) {
        int[][] list = new int[8][2];
        Arrays.fill(list, new int[]{-1 , -1});

        if (row > 0) {
            list[0] = new int[]{row - 1, col};
            if (col > 0) {
                list[1] = new int[]{row - 1, col - 1};
            }
            if (col < maxCol - 1) {
                list[2] = new int[]{row - 1, col + 1};
            }
        }
        if (row < maxRow - 1) {
            list[3] = new int[]{row + 1, col};
            if (col > 0) {
                list[4] = new int[]{row + 1, col - 1};
            }
            if (col < maxCol - 1) {
                list[5] = new int[]{row + 1, col + 1};
            }
        }
        if (col > 0) {
            list[6] = new int[]{row, col - 1};
        }
        if (col < maxCol - 1) {
            list[7] = new int[]{row, col + 1};
        }
        return list;
    }

    private String getLetter(int row, int col, BoggleBoard board) {
        char letter = board.getLetter(row, col);
        switch (letter) {
            case BoggleSolver.Q:
                return BoggleSolver.QU;
            default:
                return Character.toString(letter);
        }
    }

    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        StdOut.println("Finished creating board");
        int score = 0;
        int wordCount = 0;
        StdOut.println(board.toString());

        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            wordCount++;
            score += solver.scoreOf(word);
        }
        StdOut.println("RunningTime: " + (System.currentTimeMillis() - startTime));
        StdOut.println("WordCount = " + wordCount);
        StdOut.println("Score = " + score);
    }
}