package game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test void appHasAGreeting() {
        TetrisGame classUnderTest = new TetrisGame();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }
}
