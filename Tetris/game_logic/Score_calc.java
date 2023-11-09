package game_logic;
public class Score_calc {
    private int SCORE;
    public Score_calc() {
    }
    public void score_calcu(int lines, boolean harddrop) {
        switch(lines) {
            case 1: SCORE += 2;
            case 2: SCORE += 3;
            case 3: SCORE += 4;
            case 4: SCORE += 8;
        }
        if(harddrop) SCORE+=2;
    }
    public int score() { 
        return SCORE;
    }
    
}