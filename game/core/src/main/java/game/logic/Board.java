package game.logic;

/**
 * 描画に依存しないゲームロジック (旧 Main_panel のフィールド・移動・回転・落下処理)。
 */
public class Board {
    public static final int START_X = 4;
    public static final int START_Y = 0;
    public static final float GRAVITY = 0.5f; // 秒

    private int x = START_X;
    private int y = START_Y;
    private int droping_mino[][] = Mino.minos[0];
    private float gravity = GRAVITY;
    private boolean down;
    private boolean gameOver;
    private float elapsed;
    private final int field[][] = createField();

    private static int[][] createField() {
        int[][] f = new int[23][12];
        for (int i = 0; i < f.length; i++) {
            for (int j = 0; j < f[i].length; j++) {
                boolean wall = j == 0 || j == f[i].length - 1 || i == f.length - 1;
                f[i][j] = wall ? 2 : 0;
            }
        }
        return f;
    }

    /** 経過時間を進め、重力の間隔ごとにミノを1マス落とす。 */
    public void update(float delta) {
        if (gameOver) return;
        elapsed += delta;
        while (elapsed >= gravity && !gameOver) {
            elapsed -= gravity;
            step();
        }
    }

    /** 1マス落下。これ以上落ちられなければ次のミノを出す。 */
    void step() {
        if (can_move(x, y + 1)) {
            y++;
            return;
        }
        if (y == START_Y + 1) {
            gameOver = true;
            return;
        }
        x = START_X;
        y = START_Y;
    }

    public void moveRight() {
        if (x < 10 && can_move(x + 1, y)) x++;
    }

    public void moveLeft() {
        if (x > 0 && can_move(x - 1, y)) x--;
    }

    /** dire: 1 = 右回転, 2 = 左回転。回転後にぶつかる場合は回転しない。 */
    public void rotate(int dire) {
        int len = droping_mino.length;
        int rotated[][] = new int[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (dire == 1) rotated[j][len - 1 - i] = droping_mino[i][j];
                if (dire == 2) rotated[len - 1 - j][i] = droping_mino[i][j];
            }
        }
        int[][] before = droping_mino;
        droping_mino = rotated;
        if (!can_move(x, y)) droping_mino = before;
    }

    public void setSoftDrop(boolean pressed) {
        if (pressed == down) return;
        down = pressed;
        gravity = pressed ? GRAVITY / 2 : GRAVITY;
    }

    boolean can_move(int x, int y) {
        for (int i = 0; i < droping_mino.length; i++) {
            for (int j = 0; j < droping_mino[i].length; j++) {
                if (droping_mino[i][j] == 0) continue;
                int fy = i + y;
                int fx = j + x;
                if (fy < 0 || fy >= field.length || fx < 0 || fx >= field[fy].length) return false;
                if (field[fy][fx] != 0) return false;
            }
        }
        return true;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int[][] getField() { return field; }
    public int[][] getDropingMino() { return droping_mino; }
    public float getGravity() { return gravity; }
    public boolean isGameOver() { return gameOver; }
}
