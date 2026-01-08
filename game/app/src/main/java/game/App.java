package game;

import game.frame.BaseFrame;
public class App {
    public String getGreeting() {
        return "WELCOME TO GAME!";
    }
    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        BaseFrame.frame_generator();
    }
}
