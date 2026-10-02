package game.logic;

import java.util.Random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TSpinTest {
    private static Board newBoard() {
        return new Board(new Random(1));
    }

    private static void fillRow(Board board, int row, int... holes) {
        int[][] field = board.getField();
        outer:
        for (int j = 1; j < Board.COLS - 1; j++) {
            for (int h : holes) if (h == j) continue outer;
            field[row][j] = Board.BLOCK;
        }
    }

    /**
     * T スピンダブルの形:
     *   行 20: 列 4 に屋根
     *   行 21: 列 4〜6 が空き
     *   行 22: 列 5 が空き
     */
    private static Board tSpinDoubleSetup() {
        Board board = newBoard();
        board.getField()[20][4] = Board.BLOCK;
        fillRow(board, 21, 4, 5, 6);
        fillRow(board, 22, 5);
        return board;
    }

    @Test void tSpinDouble() {
        Board board = tSpinDoubleSetup();
        board.place(Mino.T, 1, 4, 19);
        board.rotate(1); // R -> 2 (下向き) で穴にはまる
        board.hardDrop();
        assertEquals(TSpin.FULL, board.getLastTSpin());
        assertEquals(2, board.getLastCleared());
        assertEquals(1200, board.getScore());
    }

    @Test void droppingIntoSlotWithoutRotationIsNotTSpin() {
        Board board = newBoard();
        fillRow(board, 21, 4, 5, 6);
        fillRow(board, 22, 5);
        board.place(Mino.T, 2, 4, 10);
        board.hardDrop();
        assertEquals(TSpin.NONE, board.getLastTSpin());
        assertEquals(2, board.getLastCleared());
        assertEquals(100, board.getScore());
    }

    /** 右向き T: 背中側 2 隅と右上が埋まり、右下 (凸側) が空いている。 */
    private static Board miniSetup() {
        Board board = newBoard();
        int[][] field = board.getField();
        field[20][4] = Board.BLOCK;
        field[22][4] = Board.BLOCK;
        field[20][6] = Board.BLOCK;
        return board;
    }

    @Test void tSpinMini() {
        Board board = miniSetup();
        board.place(Mino.T, 0, 4, 19);
        board.rotate(1); // 0 -> R
        board.hardDrop();
        assertEquals(TSpin.MINI, board.getLastTSpin());
        assertEquals(0, board.getLastCleared());
        assertEquals(100, board.getScore());
    }

    @Test void fallingAfterRotationCancelsTSpin() {
        Board board = miniSetup();
        board.place(Mino.T, 0, 4, 17);
        board.rotate(1);
        board.hardDrop(); // 回転のあと 2 段落ちている
        assertEquals(TSpin.NONE, board.getLastTSpin());
        assertEquals(0, board.getScore());
    }

    @Test void movingAfterRotationCancelsTSpin() {
        Board board = newBoard();
        board.place(Mino.T, 0, 4, 10);
        board.rotate(1);
        assertTrue(board.isLastMoveRotation());
        board.moveLeft();
        assertFalse(board.isLastMoveRotation());
        board.rotate(2);
        assertTrue(board.isLastMoveRotation());
        board.tick(); // 重力で 1 段落ちても解除
        assertFalse(board.isLastMoveRotation());
    }

    @Test void lastKickUpgradesMiniToFull() {
        Board board = newBoard();
        int[][] field = board.getField();
        // 列 10 は空けておき、ラインは消えないようにする
        for (int i = 9; i <= 16; i++) {
            for (int j = 1; j < Board.COLS - 2; j++) field[i][j] = Board.BLOCK;
        }
        // 回転前の T (0 向き, x=4, y=10) と、5 番目の壁蹴り先 (R 向き, x=3, y=12) を空ける
        int[][] empty = {{11, 5}, {12, 4}, {12, 5}, {12, 6}, {13, 4}, {14, 4}, {14, 5}, {15, 4}};
        for (int[] c : empty) field[c[0]][c[1]] = Board.EMPTY;
        field[15][5] = Board.EMPTY; // 凸側の隅を 1 つ空ける → 本来なら Mini
        board.place(Mino.T, 0, 4, 10);
        board.rotate(1);
        assertEquals(3, board.getX());
        assertEquals(12, board.getY());
        board.hardDrop();
        assertEquals(TSpin.FULL, board.getLastTSpin());
        assertEquals(400, board.getScore());
    }

    @Test void otherMinosNeverTSpin() {
        Board board = tSpinDoubleSetup();
        board.place(Mino.L, 0, 4, 10);
        board.rotate(1);
        board.hardDrop();
        assertEquals(TSpin.NONE, board.getLastTSpin());
    }
}
