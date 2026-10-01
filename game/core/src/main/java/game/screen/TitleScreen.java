package game.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.util.GameUtil;

public class TitleScreen extends ScreenAdapter {
    private int y = 60;
    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(GameUtil.PANEL_X, GameUtil.PANEL_Y, camera);
    private final ShapeRenderer shapes = new ShapeRenderer();

    public TitleScreen() {
        camera.setToOrtho(true, GameUtil.PANEL_X, GameUtil.PANEL_Y);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeType.Filled);
        shapes.setColor(Color.WHITE);
        shapes.rect(250, 235 + y, 180, 6);
        shapes.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        shapes.dispose();
    }
}
