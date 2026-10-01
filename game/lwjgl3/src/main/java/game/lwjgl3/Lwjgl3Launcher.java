package game.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import game.TetrisGame;
import game.util.GameUtil;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("TETRIS");
        config.setWindowedMode(GameUtil.PANEL_X, GameUtil.PANEL_Y);
        config.setWindowPosition(300, 10);
        config.setResizable(false);
        config.useVsync(true);
        config.setForegroundFPS(60);
        config.setWindowIcon("logo.png");
        new Lwjgl3Application(new TetrisGame(), config);
    }
}
