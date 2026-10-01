package game.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test void minoFallsWithGravity() {
        Board board = new Board();
        board.update(Board.GRAVITY);
        assertEquals(Board.START_Y + 1, board.getY());
    }

    @Test void softDropHalvesGravity() {
        Board board = new Board();
        board.setSoftDrop(true);
        assertEquals(Board.GRAVITY / 2, board.getGravity());
        board.setSoftDrop(false);
        assertEquals(Board.GRAVITY, board.getGravity());
    }

    @Test void wallsStopHorizontalMovement() {
        Board board = new Board();
        for (int i = 0; i < 20; i++) board.moveLeft();
        assertEquals(1, board.getX());
        for (int i = 0; i < 20; i++) board.moveRight();
        assertEquals(8, board.getX());
    }

    @Test void rotateRightThenLeftRestoresShape() {
        Board board = new Board();
        int[][] before = board.getDropingMino();
        board.rotate(1);
        assertFalse(java.util.Arrays.deepEquals(before, board.getDropingMino()));
        board.rotate(2);
        assertArrayEquals(before, board.getDropingMino());
    }

    @Test void landedMinoRespawnsAtTop() {
        Board board = new Board();
        for (int i = 0; i < 100 && board.getY() < 20; i++) board.step();
        board.step();
        assertEquals(Board.START_X, board.getX());
        assertEquals(Board.START_Y, board.getY());
        assertFalse(board.isGameOver());
    }
}
