package game.logic;

import java.util.Random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SuperRotationTest {
    private static Board newBoard() {
        return new Board(new Random(1));
    }

    /** フィールドの中を全部埋めたうえで、指定したミノの位置だけ空ける。 */
    private static void fillExcept(Board board, int[]... pieces) {
        int[][] field = board.getField();
        for (int i = Board.SPAWN_ROW + 1; i < Board.ROWS - 1; i++) {
            for (int j = 1; j < Board.COLS - 1; j++) field[i][j] = Board.BLOCK;
        }
        for (int[] p : pieces) {
            int[][] shape = Mino.shape(p[0], p[1]);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (shape[i][j] != 0) field[p[3] + i][p[2] + j] = Board.EMPTY;
                }
            }
        }
    }

    @Test void rotatesInPlaceWhenThereIsRoom() {
        Board board = newBoard();
        board.place(Mino.T, 0, 4, 10);
        board.rotate(1);
        assertEquals(1, board.getRotation());
        assertEquals(4, board.getX());
        assertEquals(10, board.getY());
    }

    @Test void tKicksAwayFromLeftWall() {
        Board board = newBoard();
        // R 向きの T を左の壁にくっつける (縦棒が列 1)
        board.place(Mino.T, 1, 0, 10);
        board.rotate(1); // R -> 2 はその場だと壁にめり込むので右へ 1
        assertEquals(2, board.getRotation());
        assertEquals(1, board.getX());
        assertEquals(10, board.getY());
    }

    @Test void iKicksAwayFromRightWall() {
        Board board = newBoard();
        // R 向きの I を右の壁にくっつける (縦棒が列 10)
        board.place(Mino.I, 1, 8, 10);
        board.rotate(1); // R -> 2: (0,0) は壁、次の (-1,0) で入る
        assertEquals(2, board.getRotation());
        assertEquals(7, board.getX());
        assertEquals(10, board.getY());
    }

    @Test void iUsesSecondKickWhenFirstIsBlocked() {
        Board board = newBoard();
        board.place(Mino.I, 1, 8, 10);
        board.getField()[12][7] = Board.BLOCK; // (-1,0) を塞ぐ
        board.rotate(1); // 次の (+2,0) は右の壁、その次の (-1,+2) で 2 段上に入る
        assertEquals(2, board.getRotation());
        assertEquals(7, board.getX());
        assertEquals(8, board.getY());
    }

    @Test void tSpinUsesLastKick() {
        Board board = newBoard();
        // 0 -> R の最後の候補 (-1,-2) = 左に 1、下に 2 の位置だけ空いている
        fillExcept(board, new int[] {Mino.T, 0, 4, 10}, new int[] {Mino.T, 1, 3, 12});
        board.place(Mino.T, 0, 4, 10);
        board.rotate(1);
        assertEquals(1, board.getRotation());
        assertEquals(3, board.getX());
        assertEquals(12, board.getY());
    }

    @Test void counterClockwiseKicks() {
        Board board = newBoard();
        // 0 -> L の最後の候補 (+1,-2) = 右に 1、下に 2
        fillExcept(board, new int[] {Mino.T, 0, 4, 10}, new int[] {Mino.T, 3, 5, 12});
        board.place(Mino.T, 0, 4, 10);
        board.rotate(2);
        assertEquals(3, board.getRotation());
        assertEquals(5, board.getX());
        assertEquals(12, board.getY());
    }

    @Test void staysWhenNoKickFits() {
        Board board = newBoard();
        fillExcept(board, new int[] {Mino.T, 0, 4, 10});
        board.place(Mino.T, 0, 4, 10);
        board.rotate(1);
        board.rotate(2);
        assertEquals(0, board.getRotation());
        assertEquals(4, board.getX());
        assertEquals(10, board.getY());
    }

    @Test void oNeverMoves() {
        Board board = newBoard();
        board.place(Mino.O, 0, 0, 10);
        board.rotate(1);
        assertEquals(1, board.getRotation());
        assertEquals(0, board.getX());
        assertEquals(10, board.getY());
    }
}
