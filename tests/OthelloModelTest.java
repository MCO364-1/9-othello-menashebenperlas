import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.awt.Point;
import java.util.List;

public class OthelloModelTest {

    @Test
    public void testInitialSetup() {
        OthelloModel m = new OthelloModel();
        // 2 black, 2 white
        assertEquals(2, m.getScore(OthelloModel.BLACK));
        assertEquals(2, m.getScore(OthelloModel.WHITE));
        // Black has exactly 4 valid moves
        List<Point> moves = m.getValidMoves(OthelloModel.BLACK);
        assertEquals(4, moves.size());
        assertTrue(moves.contains(new Point(2,3)));
        assertTrue(moves.contains(new Point(3,2)));
        assertTrue(moves.contains(new Point(4,5)));
        assertTrue(moves.contains(new Point(5,4)));
    }

    @Test
    public void testMakeMoveAndFlip() {
        OthelloModel m = new OthelloModel();
        boolean ok = m.makeMove(2, 3); // one of the initial valid moves
        assertTrue(ok);
        // That spot is now black
        assertEquals(OthelloModel.BLACK, m.getCell(2,3));
        // The disc at (3,3) must have flipped to black
        assertEquals(OthelloModel.BLACK, m.getCell(3,3));
        // Scores: black=4, white=1
        assertEquals(4, m.getScore(OthelloModel.BLACK));
        assertEquals(1, m.getScore(OthelloModel.WHITE));
    }

    @Test
    public void testNoMoveOnOccupied() {
        OthelloModel m = new OthelloModel();
        // Try placing on initial white at (3,3)
        assertFalse(m.makeMove(3, 3));
    }

    @Test
    public void testHasValidMove() {
        OthelloModel m = new OthelloModel();
        assertTrue(m.hasValidMove(OthelloModel.BLACK));
        assertTrue(m.hasValidMove(OthelloModel.WHITE));
    }

    @Test
    public void testGameNotOverInitial() {
        OthelloModel m = new OthelloModel();
        assertFalse(m.isGameOver());
    }

    @Test
    public void testComputerGreedyMoveNotNull() {
        OthelloModel m = new OthelloModel();
        // make one human move, switch to computer
        assertTrue(m.makeMove(2,3));
        m.switchPlayer();
        Point cp = m.getComputerMove();
        assertNotNull(cp);
        // must be a valid move for white
        List<Point> whiteMoves = m.getValidMoves(OthelloModel.WHITE);
        assertTrue(whiteMoves.contains(cp));
    }
}