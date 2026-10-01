package game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.logic.Board;
import game.util.GameUtil;

public class MainScreen extends ScreenAdapter {
    final int TILE_SIZE = 20;
    final int TILE_MARGIN = 5;

    private final Board board = new Board();
    // y 軸下向き (Swing と同じ座標系) のカメラ
    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(GameUtil.PANEL_X, GameUtil.PANEL_Y, camera);
    private final ShapeRenderer shapes = new ShapeRenderer();

    public MainScreen() {
        camera.setToOrtho(true, GameUtil.PANEL_X, GameUtil.PANEL_Y);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int key) {
                if (key == Input.Keys.G) board.rotate(1);
                if (key == Input.Keys.F) board.rotate(2);
                if (key == Input.Keys.RIGHT) board.moveRight();
                if (key == Input.Keys.LEFT) board.moveLeft();
                if (key == Input.Keys.DOWN) board.setSoftDrop(true);
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
        int tmp = TILE_MARGIN + TILE_SIZE;
        int[][] field = board.getField();
        int[][] mino = board.getDropingMino();

        shapes.begin(ShapeType.Filled);
        shapes.setColor(Color.WHITE);
        for (int i = 1; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == 0) continue;
                shapes.rect((j * tmp) + 90, (i * tmp) + 60, TILE_SIZE, TILE_SIZE);
            }
        }
        shapes.end();

        shapes.begin(ShapeType.Line);
        shapes.setColor(Color.WHITE);
        shapes.line(100, 108, 375, 108);
        for (int i = 1; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == 0) continue;
                shapes.rect((j * tmp) + 92, (i * tmp) + 62, TILE_SIZE, TILE_SIZE);
            }
        }
        int x = board.getX();
        int y = board.getY();
        for (int i = 0; i < mino.length; i++) {
            for (int j = 0; j < mino[i].length; j++) {
                if (mino[i][j] == 0) continue;
                shapes.rect(((x + j) * tmp) + 90, ((y + i) * tmp) + 60, TILE_SIZE, TILE_SIZE);
                shapes.rect(((x + j) * tmp) + 92, ((y + i) * tmp) + 62, TILE_SIZE, TILE_SIZE);
            }
        }
        shapes.end();
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
    }
}
