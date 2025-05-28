import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class OthelloModel {
    public static final int EMPTY = 0;
    public static final int BLACK = 1;  // human
    public static final int WHITE = 2;  // computer

    private static final int SIZE = 8;
    private static final int[][] DIRS = {
            { -1, -1 }, { -1, 0 }, { -1, 1 },
            {  0, -1 },           {  0, 1 },
            {  1, -1 }, {  1, 0 }, {  1, 1 }
    };

    private final int[][] board = new int[SIZE][SIZE];
    private int currentPlayer;

    public OthelloModel() {
        // Initial four discs
        board[3][3] = WHITE;
        board[3][4] = BLACK;
        board[4][3] = BLACK;
        board[4][4] = WHITE;
        currentPlayer = BLACK;
    }

    public int getCell(int row, int col) {
        return board[row][col];
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == BLACK ? WHITE : BLACK);
    }

    public List<Point> getValidMoves(int player) {
        List<Point> moves = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == EMPTY && !getFlips(r, c, player).isEmpty()) {
                    moves.add(new Point(r, c));
                }
            }
        }
        return moves;
    }

    private List<Point> getFlips(int row, int col, int player) {
        List<Point> flips = new ArrayList<>();
        int opponent = (player == BLACK ? WHITE : BLACK);
        for (int[] d : DIRS) {
            int r = row + d[0], c = col + d[1];
            List<Point> line = new ArrayList<>();
            while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == opponent) {
                line.add(new Point(r, c));
                r += d[0];
                c += d[1];
            }
            if (!line.isEmpty()
                    && r >= 0 && r < SIZE && c >= 0 && c < SIZE
                    && board[r][c] == player) {
                flips.addAll(line);
            }
        }
        return flips;
    }

    /**
     * Attempts to place a disc for currentPlayer at (row,col).
     * If legal, flips all captured discs, leaves currentPlayer unchanged,
     * and returns true. Otherwise returns false.
     */
    public boolean makeMove(int row, int col) {
        List<Point> flips = getFlips(row, col, currentPlayer);
        if (board[row][col] != EMPTY || flips.isEmpty()) {
            return false;
        }
        board[row][col] = currentPlayer;
        for (Point p : flips) {
            board[p.x][p.y] = currentPlayer;
        }
        return true;
    }

    public boolean hasValidMove(int player) {
        return !getValidMoves(player).isEmpty();
    }

    public boolean isGameOver() {
        return !hasValidMove(BLACK) && !hasValidMove(WHITE);
    }

    public int getScore(int player) {
        int cnt = 0;
        for (int[] row : board) for (int cell : row) {
            if (cell == player) cnt++;
        }
        return cnt;
    }

    /**
     * Greedy computer: pick the valid move that flips the most discs.
     * Returns null if no valid moves.
     */
    public Point getComputerMove() {
        List<Point> moves = getValidMoves(WHITE);
        Point best = null;
        int bestFlips = -1;
        for (Point m : moves) {
            int flips = getFlips(m.x, m.y, WHITE).size();
            if (flips > bestFlips) {
                bestFlips = flips;
                best = m;
            }
        }
        return best;
    }
}