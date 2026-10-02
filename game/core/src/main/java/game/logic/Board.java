package game.logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 描画に依存しないゲームロジック (C# 版 tetris.cs の移植)。
 */
public class Board {
    public static final int ROWS = 24;
    public static final int COLS = 12;
    public static final int EMPTY = 0;
    public static final int BLOCK = 1;
    public static final int WALL = 8;

    public static final int SPAWN_X = 4;
    public static final int SPAWN_Y = 0;
    /** 最上段から数えてこの行までが出現エリア。ここにブロックが残るとゲームオーバー。 */
    public static final int SPAWN_ROW = 2;
    public static final int NEXT_COUNT = 6;
    public static final int MAX_LINES = 200;
    public static final int MAX_SCORE = 9_999_999;
    public static final float SOFT_DROP_INTERVAL = 0.05f;
    private static final int[] LINE_SCORES = {0, 40, 100, 300, 1200};

    private final int[][] field = createField();
    private final Random random;
    private final ArrayDeque<Integer> queue = new ArrayDeque<>();

    private int type;
    private int rotation;
    private int x = SPAWN_X;
    private int y = SPAWN_Y;
    private int hold = -1;
    private boolean holdUsed;

    private boolean softDrop;
    private int dropScore;
    /** 接地した。次の落下タイミングで固定される (移動・回転で解除)。 */
    private boolean landed;
    private float elapsed;

    /** ライン消去の演出中に揃った行。演出が終わったら詰める。 */
    private final List<Integer> clearing = new ArrayList<>();
    private float clearTimer;

    private int level = 1;
    private int lines;
    private int linesInLevel;
    private int score;
    private boolean gameOver;
    private boolean gameClear;

    public Board() {
        this(new Random());
    }

    public Board(Random random) {
        this.random = random;
        spawn(nextFromQueue());
    }

    private static int[][] createField() {
        int[][] f = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            f[i][0] = WALL;
            f[i][COLS - 1] = WALL;
        }
        for (int j = 0; j < COLS; j++) f[ROWS - 1][j] = WALL;
        // 出現エリアの左右の囲い
        for (int i = 0; i <= SPAWN_ROW; i++) {
            f[i][3] = WALL;
            f[i][8] = WALL;
        }
        for (int j : new int[] {1, 2, 9, 10}) f[SPAWN_ROW][j] = WALL;
        return f;
    }

    // ---- 時間経過 ----

    public void update(float delta) {
        if (isFinished()) return;
        if (isClearing()) {
            clearTimer -= delta;
            if (clearTimer <= 0) finishClear();
            return;
        }
        elapsed += delta;
        while (!isFinished() && !isClearing() && elapsed >= getInterval()) {
            elapsed -= getInterval();
            tick();
        }
    }

    void tick() {
        if (landed) {
            lock();
            return;
        }
        if (canMove(x, y + 1, rotation)) {
            y++;
            if (softDrop) dropScore++;
        } else {
            landed = true;
        }
    }

    /** 現在の落下間隔 (秒)。レベルが上がるほど短くなる。 */
    public float getInterval() {
        if (softDrop) return SOFT_DROP_INTERVAL;
        return Math.max(500 - 40 * (level - 1), 20) / 1000f;
    }

    // ---- 操作 ----

    public boolean canControl() {
        return !isFinished() && !isClearing();
    }

    public void moveLeft() {
        move(-1);
    }

    public void moveRight() {
        move(1);
    }

    private void move(int dx) {
        if (!canControl()) return;
        landed = false;
        if (canMove(x + dx, y, rotation)) x += dx;
    }

    /**
     * dire: 1 = 右回転, 2 = 左回転。
     * スーパーローテーション: ぶつかる場合は壁蹴りテーブルの順にずらして試し、どこにも置けなければ回転しない。
     */
    public void rotate(int dire) {
        if (!canControl()) return;
        landed = false;
        boolean clockwise = dire == 1;
        int next = (rotation + (clockwise ? 1 : 3)) % 4;
        for (int[] kick : SuperRotation.kicks(type, rotation, clockwise)) {
            int nx = x + kick[0];
            int ny = y - kick[1]; // テーブルは y 上向き、フィールドは y 下向き
            if (canMove(nx, ny, next)) {
                x = nx;
                y = ny;
                rotation = next;
                return;
            }
        }
    }

    public void setSoftDrop(boolean pressed) {
        softDrop = pressed;
    }

    public void hardDrop() {
        if (!canControl()) return;
        y = getGhostY();
        lock();
    }

    /** 1 ミノにつき 1 回だけホールドできる。最初のホールドでは NEXT から出す。 */
    public void hold() {
        if (!canControl() || holdUsed) return;
        int current = type;
        if (hold < 0) {
            spawn(nextFromQueue());
        } else {
            spawn(hold);
        }
        hold = current;
        holdUsed = true;
    }

    // ---- 内部処理 ----

    private int nextFromQueue() {
        while (queue.size() <= NEXT_COUNT) {
            List<Integer> bag = new ArrayList<>();
            for (int t : Mino.TYPES) bag.add(t);
            Collections.shuffle(bag, random);
            queue.addAll(bag);
        }
        return queue.poll();
    }

    private void spawn(int newType) {
        type = newType;
        rotation = 0;
        x = SPAWN_X;
        y = SPAWN_Y;
        landed = false;
        dropScore = 0;
        elapsed = 0;
        if (!canMove(x, y, rotation)) endGame();
    }

    private void lock() {
        int[][] shape = Mino.shape(type, rotation);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (shape[i][j] != 0) field[y + i][x + j] = BLOCK;
            }
        }
        landed = false;

        int cleared = 0;
        for (int i = SPAWN_ROW + 1; i < ROWS - 1 && lines < MAX_LINES; i++) {
            if (!isFullRow(i)) continue;
            for (int j = 1; j < COLS - 1; j++) field[i][j] = EMPTY;
            clearing.add(i);
            cleared++;
            lines++;
            linesInLevel++;
            if (linesInLevel >= 10) {
                linesInLevel = 0;
                level++;
            }
        }
        addScore(cleared);
        holdUsed = false;

        if (cleared > 0) {
            clearTimer = Math.max(300 - 20 * (level - 1), 0) / 1000f;
            if (clearTimer <= 0) finishClear();
        } else {
            afterLock();
        }
    }

    private boolean isFullRow(int row) {
        for (int j = 1; j < COLS - 1; j++) {
            if (field[row][j] == EMPTY) return false;
        }
        return true;
    }

    private void finishClear() {
        for (int row : clearing) {
            for (int k = row - 1; k > SPAWN_ROW; k--) {
                System.arraycopy(field[k], 1, field[k + 1], 1, COLS - 2);
            }
            for (int j = 1; j < COLS - 1; j++) field[SPAWN_ROW + 1][j] = EMPTY;
        }
        clearing.clear();
        clearTimer = 0;
        afterLock();
    }

    private void afterLock() {
        for (int j = SPAWN_X; j < SPAWN_X + 3; j++) {
            if (field[SPAWN_ROW][j] != EMPTY) {
                endGame();
                return;
            }
        }
        if (lines >= MAX_LINES) {
            gameClear = true;
            return;
        }
        spawn(nextFromQueue());
    }

    private void addScore(int cleared) {
        long total = (long) score + (long) LINE_SCORES[cleared] * level;
        if (softDrop) total += dropScore;
        score = (int) Math.min(total, MAX_SCORE);
    }

    private void endGame() {
        gameOver = true;
        for (int[] row : field) {
            for (int j = 0; j < row.length; j++) {
                if (row[j] == BLOCK) row[j] = WALL;
            }
        }
    }

    boolean canMove(int x, int y, int rotation) {
        int[][] shape = Mino.shape(type, rotation);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (shape[i][j] == 0) continue;
                int fy = y + i;
                int fx = x + j;
                if (fy < 0 || fy >= ROWS || fx < 0 || fx >= COLS) return false;
                if (field[fy][fx] != EMPTY) return false;
            }
        }
        return true;
    }

    /** テスト用: 落下中のミノを差し替える。 */
    void setCurrent(int newType) {
        spawn(newType);
    }

    /** テスト用: 落下中のミノを任意の位置・向きに置く。 */
    void place(int newType, int newRotation, int newX, int newY) {
        spawn(newType);
        rotation = newRotation;
        x = newX;
        y = newY;
    }

    // ---- 参照 ----

    public int getGhostY() {
        int g = y;
        while (canMove(x, g + 1, rotation)) g++;
        return g;
    }

    public int[][] getCurrentShape() { return Mino.shape(type, rotation); }
    public int getType() { return type; }
    public int getRotation() { return rotation; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int[][] getField() { return field; }
    /** ホールド中のミノ。なければ -1。 */
    public int getHold() { return hold; }
    public List<Integer> getNext() {
        return new ArrayList<>(queue).subList(0, NEXT_COUNT);
    }
    public int getLevel() { return level; }
    public int getLines() { return lines; }
    public int getScore() { return score; }
    public boolean isClearing() { return !clearing.isEmpty(); }
    public boolean isGameOver() { return gameOver; }
    public boolean isGameClear() { return gameClear; }
    public boolean isFinished() { return gameOver || gameClear; }
}
