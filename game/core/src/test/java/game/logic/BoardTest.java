package game.logic;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private static Board newBoard() {
        return new Board(new Random(1));
    }

    /** 最下段を左右 3 マスずつ埋め、中央 4 マス (列 4〜7) だけ空ける。 */
    private static void fillBottomExceptCenter(Board board, int row) {
        int[][] field = board.getField();
        for (int j = 1; j < Board.COLS - 1; j++) {
            if (j < 4 || j > 7) field[row][j] = Board.BLOCK;
        }
    }

    @Test void sevenBagContainsEveryMino() {
        Board board = newBoard();
        Set<Integer> types = new HashSet<>();
        types.add(board.getType());
        List<Integer> next = board.getNext();
        types.addAll(next.subList(0, 6));
        assertEquals(7, types.size());
    }

    @Test void minoFallsWithGravity() {
        Board board = newBoard();
        board.update(board.getInterval());
        assertEquals(Board.SPAWN_Y + 1, board.getY());
    }

    @Test void softDropIsFasterAndScores() {
        Board board = newBoard();
        board.setCurrent(Mino.T);
        board.setSoftDrop(true);
        assertEquals(Board.SOFT_DROP_INTERVAL, board.getInterval());
        board.update(Board.SOFT_DROP_INTERVAL * 3 + 0.001f);
        assertEquals(3, board.getY());
        board.hardDrop();
        assertEquals(3, board.getScore());
        board.setSoftDrop(false);
        assertEquals(0.5f, board.getInterval());
    }

    @Test void hardDropLocksAtBottom() {
        Board board = newBoard();
        board.setCurrent(Mino.O);
        board.hardDrop();
        int[][] field = board.getField();
        assertEquals(Board.BLOCK, field[22][5]);
        assertEquals(Board.BLOCK, field[22][6]);
        assertEquals(Board.BLOCK, field[21][5]);
        assertEquals(Board.BLOCK, field[21][6]);
        assertEquals(Board.SPAWN_Y, board.getY());
    }

    @Test void minoLocksOneTickAfterLanding() {
        Board board = newBoard();
        board.setCurrent(Mino.O);
        int ghost = board.getGhostY();
        while (board.getY() < ghost) board.tick();
        board.tick(); // 接地
        assertEquals(Board.EMPTY, board.getField()[22][5]);
        board.tick(); // 固定
        assertEquals(Board.BLOCK, board.getField()[22][5]);
    }

    @Test void wallsStopHorizontalMovement() {
        Board board = newBoard();
        board.setCurrent(Mino.T);
        board.update(2f); // 出現エリアの囲いより下まで落とす
        for (int i = 0; i < 20; i++) board.moveLeft();
        assertEquals(1, board.getX());
        for (int i = 0; i < 20; i++) board.moveRight();
        assertEquals(8, board.getX());
    }

    @Test void rotateRightThenLeftRestoresShape() {
        Board board = newBoard();
        board.setCurrent(Mino.T);
        board.update(2f);
        board.rotate(1);
        assertEquals(1, board.getRotation());
        board.rotate(2);
        assertEquals(0, board.getRotation());
        board.rotate(2);
        assertEquals(3, board.getRotation());
    }

    @Test void singleLineClearScoresAndCollapses() {
        Board board = newBoard();
        fillBottomExceptCenter(board, 22);
        board.getField()[21][1] = Board.BLOCK;
        board.setCurrent(Mino.I);
        board.hardDrop();
        assertEquals(1, board.getLines());
        assertEquals(40, board.getScore());
        assertTrue(board.isClearing());
        board.update(1f);
        assertFalse(board.isClearing());
        assertEquals(Board.BLOCK, board.getField()[22][1]); // 上の段が落ちてくる
        assertEquals(Board.EMPTY, board.getField()[22][2]);
    }

    @Test void tetrisScores1200() {
        Board board = newBoard();
        for (int row = 19; row <= 22; row++) {
            int[][] field = board.getField();
            for (int j = 1; j < Board.COLS - 1; j++) {
                if (j != 6) field[row][j] = Board.BLOCK;
            }
        }
        board.setCurrent(Mino.I);
        board.rotate(1); // 縦向き (列 X+2 = 6)
        board.hardDrop();
        assertEquals(4, board.getLines());
        assertEquals(1200, board.getScore());
    }

    @Test void levelUpEveryTenLines() {
        Board board = newBoard();
        for (int n = 0; n < 10; n++) {
            fillBottomExceptCenter(board, 22);
            board.setCurrent(Mino.I);
            board.hardDrop();
            board.update(1f);
        }
        assertEquals(10, board.getLines());
        assertEquals(2, board.getLevel());
        assertEquals(9 * 40 + 40 * 2, board.getScore());
        assertEquals(0.46f, board.getInterval(), 1e-6);
    }

    @Test void holdOncePerMino() {
        Board board = newBoard();
        int first = board.getType();
        int next = board.getNext().get(0);
        board.hold();
        assertEquals(first, board.getHold());
        assertEquals(next, board.getType());
        board.hold(); // 同じミノでは 2 回目は無効
        assertEquals(first, board.getHold());
        assertEquals(next, board.getType());
        board.hardDrop();
        int current = board.getType();
        board.hold(); // 固定後はまたホールドできる
        assertEquals(current, board.getHold());
        assertEquals(first, board.getType());
    }

    @Test void stackingIntoSpawnAreaIsGameOver() {
        Board board = newBoard();
        for (int row = 3; row < 23; row++) board.getField()[row][5] = Board.BLOCK;
        board.setCurrent(Mino.T);
        board.hardDrop();
        assertTrue(board.isGameOver());
        assertEquals(Board.WALL, board.getField()[10][5]);
        board.moveLeft();
        board.update(5f);
        assertTrue(board.isFinished());
    }
}
