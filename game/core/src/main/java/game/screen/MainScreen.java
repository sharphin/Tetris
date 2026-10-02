package game.screen;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.logic.Board;
import game.logic.Mino;
import game.util.GameUtil;

public class MainScreen extends ScreenAdapter {
    final int TILE_SIZE = 20;
    final int CELL = 30;
    final int FIELD_X = 160;
    final int FIELD_Y = 40;
    final int SHIFT = 3;

    private Board board = new Board();
    // y 軸下向き (Swing / WinForms と同じ座標系) のカメラ
    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(GameUtil.PANEL_X, GameUtil.PANEL_Y, camera);
    private final ShapeRenderer shapes = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font = new BitmapFont(true);

    public MainScreen() {
        camera.setToOrtho(true, GameUtil.PANEL_X, GameUtil.PANEL_Y);
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int key) {
                switch (key) {
                    case Input.Keys.LEFT -> board.moveLeft();
                    case Input.Keys.RIGHT -> board.moveRight();
                    case Input.Keys.G -> board.rotate(1);
                    case Input.Keys.F -> board.rotate(2);
                    case Input.Keys.DOWN -> board.setSoftDrop(true);
                    case Input.Keys.UP -> board.hardDrop();
                    case Input.Keys.SPACE -> board.hold();
                    case Input.Keys.ENTER -> {
                        if (board.isFinished()) board = new Board();
                    }
                    default -> { return false; }
                }
                return true;
            }

            @Override
            public boolean keyUp(int key) {
                if (key == Input.Keys.DOWN) board.setSoftDrop(false);
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        board.update(delta);

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        shapes.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        drawWalls();
        drawBlocks();
        drawText();
    }

    /** 壁 (塗りつぶし)。 */
    private void drawWalls() {
        int[][] field = board.getField();
        shapes.begin(ShapeType.Filled);
        shapes.setColor(Color.WHITE);
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == Board.WALL) {
                    shapes.rect(CELL * j + FIELD_X, CELL * i + FIELD_Y, TILE_SIZE, TILE_SIZE);
                }
            }
        }
        shapes.end();
    }

    /** 枠線で描くもの全部 (積まれたブロック・落下中・ゴースト・NEXT・HOLD)。 */
    private void drawBlocks() {
        int[][] field = board.getField();
        shapes.begin(ShapeType.Line);
        shapes.setColor(Color.WHITE);
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                int px = CELL * j + FIELD_X;
                int py = CELL * i + FIELD_Y;
                if (field[i][j] == Board.WALL) {
                    shapes.rect(px + SHIFT, py + SHIFT, TILE_SIZE, TILE_SIZE);
                } else if (field[i][j] == Board.BLOCK) {
                    drawTile(px, py, TILE_SIZE);
                }
            }
        }

        if (board.canControl()) {
            int[][] shape = board.getCurrentShape();
            int ghost = board.getGhostY();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (shape[i][j] == 0) continue;
                    int px = (board.getX() + j) * CELL + FIELD_X;
                    drawTile(px, (board.getY() + i) * CELL + FIELD_Y, TILE_SIZE);
                    if (ghost > board.getY()) {
                        shapes.rect(px + SHIFT, (ghost + i) * CELL + FIELD_Y + SHIFT, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }

        // NEXT: 1 つ目は大きく、残りは小さく
        List<Integer> next = board.getNext();
        int top = 90;
        for (int n = 0; n < next.size(); n++) {
            boolean first = n == 0;
            drawMino(next.get(n), 540, top, first ? CELL : CELL - 10, first ? TILE_SIZE : TILE_SIZE - 5);
            top += 90;
        }

        if (board.getHold() >= 0) {
            drawMino(board.getHold(), 40, 130, CELL, TILE_SIZE);
        }
        shapes.end();
    }

    private void drawMino(int type, int left, int top, int cell, int size) {
        int[][] shape = Mino.shape(type, 0);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (shape[i][j] != 0) drawTile(j * cell + left, i * cell + top, size);
            }
        }
    }

    /** 少しずらした枠を 2 つ重ねたタイル。 */
    private void drawTile(float px, float py, float size) {
        shapes.rect(px, py, size, size);
        shapes.rect(px + SHIFT, py + SHIFT, size, size);
    }

    private void drawText() {
        batch.begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(1f);
        font.draw(batch, "HOLD", 40, 95);
        font.draw(batch, "NEXT", 540, 55);
        font.draw(batch, "LINES", 30, 310);
        font.draw(batch, "LEVEL", 30, 380);
        font.draw(batch, "SCORE", 30, 450);

        font.getData().setScale(1.4f);
        font.draw(batch, String.valueOf(board.getLines()), 30, 330);
        font.draw(batch, String.valueOf(board.getLevel()), 30, 400);
        font.draw(batch, String.valueOf(board.getScore()), 30, 470);

        if (board.isFinished()) {
            font.getData().setScale(2.2f);
            font.setColor(board.isGameOver() ? Color.RED : Color.WHITE);
            font.draw(batch, board.isGameOver() ? "GAME OVER" : "GAME CLEAR", 200, 400);
            font.getData().setScale(1f);
            font.setColor(Color.WHITE);
            font.draw(batch, "PRESS ENTER", 245, 450);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        shapes.dispose();
        batch.dispose();
        font.dispose();
    }
}
