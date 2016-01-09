import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;

/**
 * Created by kyledorman on 10/26/15.
 */

public class Solver {
    private Node solutionNode;
    private int movesResult = 0;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        validateBoard(initial);
//        Node blankBoard = new Node(null, null, 0);
        solutionNode = new Node(initial, null, 0);

        MinPQ<Node> originalPQ = new MinPQ<>(new ManhattanBoardComparator());
        MinPQ<Node> twinPQ = new MinPQ<>(new ManhattanBoardComparator());

        originalPQ.insert(new Node(initial, null, 0));
        twinPQ.insert(new Node(initial.twin(), null, 0));

        while (true) {
            if (originalPQ.min().getBoard().isGoal()) {
                solutionNode = originalPQ.min();
                movesResult = solutionNode.moves;
                break;
            }

            takeTurn(originalPQ);

            if (twinPQ.min().getBoard().isGoal()) {
                movesResult = -1;
                break;
            }
            takeTurn(twinPQ);
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() { return movesResult != -1; }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() { return movesResult; }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) { return null; }
        Node node = solutionNode;
        Stack<Board> stack = new Stack<>();
        while (node != null) {
            stack.push(node.getBoard());
            node = node.getPreviousNode();
        }
        return stack;
    }

    private void takeTurn(MinPQ<Node> pq) {
        Node node = pq.delMin();
        for (Board board : node.getBoard().neighbors()) {
            if (node.getPreviousNode() == null || !board.equals(node.getPreviousNode().getBoard())) {
                pq.insert(new Node(board, node, node.getMoves() + 1));
            }
        }
    }

    private void validateBoard(Board initial) {
        if (initial == null) {
            throw new NullPointerException("Input is null");
        }
    }

    private static class ManhattanBoardComparator implements Comparator<Node> {
        public ManhattanBoardComparator() { }

        public int compare(Node node1, Node node2) {
            int score1 = node1.getBoard().manhattan() + node1.getMoves();
            int score2 = node2.getBoard().manhattan() + node2.getMoves();

            if (score2 == score1) {
                return 0;
            } else if (score1 > score2) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    private static class Node {
        private Board board;
        private Node previousNode;
        private int moves;

        public Node(Board currentBoard, Node n, int moves) {
            this.board = currentBoard;
            this.previousNode = n;
            this.moves = moves;
        }

        public Board getBoard() { return board; }

        public Node getPreviousNode() { return previousNode; }

        public int getMoves() { return moves; }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
