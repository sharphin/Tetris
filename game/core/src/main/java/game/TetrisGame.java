package game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import game.screen.MainScreen;
import game.screen.TitleScreen;

public class TetrisGame extends Game {
    public String getGreeting() {
        return "WELCOME TO GAME!";
    }

    @Override
    public void create() {
        System.out.println(getGreeting());
        setScreen(new MainScreen());
    }

    public void back_title() {
        Screen old = getScreen();
        setScreen(new TitleScreen());
        if (old != null) old.dispose();
    }
}
