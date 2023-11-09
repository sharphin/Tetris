package main_panel;
import game_logic.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;
public class Game_view extends JPanel implements KeyListener{

    private static int control;
    private static int game_state;
    private int x, y,spin,drop, shadow;  
    private int time = 500; 

    private int next_mino[] = {0,1,2,3,4,5,6};
    private int hold = -1;
    private int level = 1, line, lines = 10, gravity,count;

    private final int SIZE = 22;
    private final Font font = new Font("HGPGothicM", Font.PLAIN, 20);
    private final Font font1 = new Font("HGPGothicM", Font.PLAIN, 36);
    private boolean space, harddrop,active_mino_visible = true;
    private String signal[] = {"  READY","   GO!","GAMEOVER!","  CLEAR"};
    
    private int[][] FIELD = new int[][]
         {{8,8,8,0,8,0,0,0,0,0,0,8,0,8,8,8},
          {8,8,8,8,8,0,0,0,0,0,0,8,8,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,0,0,0,0,0,0,0,0,0,0,8,8,8},
          {8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8},
          {8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8},
          {8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8}};

    Next_mino nm = new Next_mino();
    Score_calc sc = new Score_calc();
    Timer timer;
    public Game_view(int height, int width,int game_stat) {
        game_state = game_stat;
        setSize(width, height);
        setFocusable(true);
        addKeyListener(this);
        mino_init();
        hard_drop();
        next_mino_suffle();
        timer = new Timer();
        timer.schedule(timer_task,2500);
    }
    private void mino_init() {
        x = 6; y = 0; spin = 1;
        shadow = 0;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,600,690);
        g.setColor(Color.WHITE);
        for(int i  = 0; i < FIELD.length-2; i++) {
            for(int j = 2; j < FIELD[i].length-2; j++) {
                switch (FIELD[i][j]){
                    case 8: g.fillRect(j * 26 + 89,i * 26 + 41,SIZE,SIZE); 
                            g.drawRect(j * 26 + 91,i * 26 + 43,SIZE,SIZE);
                            break;
                    case 1: g.drawRect(j * 26 + 89,i * 26 + 41,SIZE,SIZE); 
                            g.drawRect(j * 26 + 91,i * 26 + 43,SIZE,SIZE);
                            break;
                }
            }
        }
        g.setFont(font1);
        if (game_state == 1) {
            g.setColor(Color.RED);
            g.drawString(signal[0], 220,250);
            g.setColor(Color.WHITE);
            return;
        } else if (game_state == 2) {
            g.setColor(Color.RED);
            g.drawString(signal[1], 220,250);
            g.setColor(Color.WHITE);
            return;
        } else if (game_state == 4) {
            g.setColor(Color.RED);
            g.drawString(signal[3], 220,250);
            g.setColor(Color.WHITE);
        }
        for(int i  = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if ((Mino.Block[drop][i][j] & spin) == spin && 
                     active_mino_visible) {
                    g.drawRect((x+j) * 26 + 89,(y+i) * 26 + 41,SIZE,SIZE); 
                    g.drawRect((x+j) * 26 + 91,(y+i) * 26 + 43,SIZE,SIZE);      
                    if(y < shadow) {
                        g.drawRect((x+j) * 26 + 91,(shadow+i) * 26 + 43,SIZE,SIZE);
                    }     
                }   
            }
        }
        if(hold != -1) {
            for(int i  = 0; i < 4; i++) {
                for(int j = 0; j < 4; j++) {
                    if ((Mino.Block[hold][i][j] & 1) == 1) {
                        g.drawRect(j * 26 + 26,i * 26 + 41,SIZE,SIZE); 
                        g.drawRect(j * 26 + 28,i * 26 + 43,SIZE,SIZE); 
                    }               
                }
            }
        }
        int yyy = 41;     int wh = 0;    int mar = 0;
        for(int I = 0; I < next_mino.length-2; I++) {
            for(int i  = 0; i < 4; i++) {
                for(int j = 0; j < 4; j++) {
                    if ((Mino.Block[next_mino[I]][i][j] & 1) == 1) {
                        g.drawRect(j*(26-mar)+480,i*(26-mar)+yyy,SIZE-wh,SIZE-wh);
                        g.drawRect(j*(26-mar)+482,i*(26-mar)+yyy+2,SIZE-wh,SIZE-wh);
                    }  
                }
            }
            yyy += 90;    wh = 5;    mar = 10;
        }
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("NEXT", 500,50);
        g.drawString("HOLD", 50,50);
        //g.drawString("LEVEL: "+level,35,250);
        //g.drawString("LINES: "+lines,35,300);
        if(game_state == 8) {
            g.setFont(font1);
            g.setColor(Color.RED);
            g.drawString(signal[2], 200,250);
        }
    }
    private void hold() {
        if(hold == -1) {
            hold = drop;
            mino_init();
            drop = next_mino[0];
            next_mino_suffle();
            hard_drop();
        } else {
            int w = hold;
            hold = drop;
            drop = w;
        }
    }
    private void next_mino_suffle() {
        next_mino = nm.Next();
        hard_drop();
    }
    private int mino_landing(int count, int contr) {
        if (count < 40 || contr != 0) {
            count++;
        }
        if (count >= 40 || contr == 0) {
            if(contr == 5) sleep(500);
            mino_set();
            return 0;
        }
        return count;
    }
    private void mino_set() {
        if(game_state == 8) return;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if ((Mino.Block[drop][i][j] & spin) == spin) {
                    FIELD[i+y][j+x] = 1;
                }
            }
        }
        space = false;
        delete_line();
        game_over();
        mino_init();
        drop = next_mino[0];
        next_mino_suffle();
    }
    private void delete_line() {
        int lines_count = 0;
        active_mino_visible = false;
        for(int i = 2; i < 22; i++) {
            int check = 0;
            for(int j = 3; j < 13; j++) {
                if (FIELD[i][j] != 0) {
                    check++;
                }
            }
            if (check == 10 && lines <= 200) {
                lines_count++;
                for(int j = 3; j < 13; j++) {
                    FIELD[i][j] = 0;
                }
                for(int k = i - 1;k >= 2;k-- ) {
                    for(int j = 0;j < 13;j++ ) {
                        FIELD[k+1][j] = FIELD[k][j];
                    }
                }
                sleep(300-gravity);
                paintImmediately(0,0,600,690);
            }
        }
        line += lines_count;
        lines += lines_count; 
        if (lines >= 200) game_state = 4;
        if (line >= 10) {
            line = 0;
            level++;
            gravity += 20;
        }
        sc.score_calcu(lines_count,false);
        active_mino_visible = true;
    }
    private boolean can_move(int xx, int yy, int drop,int spin_flag) {
        spin_flag = spin_direction(spin_flag);
        boolean move = true;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if ((Mino.Block[drop][i][j] & spin_flag) == spin_flag) {
                    int fake_x = xx + j;
                    int fake_y = yy + i;
                    if (FIELD[fake_y][fake_x] != 0) {
                        move = super_lotation(fake_x, fake_y, spin_flag);
                    }
                }
            } 
        }
        return move;
    }
    private boolean super_lotation(int fake_x, int fake_y, int spin_flag) {
        int x_corr = 1;
        switch(drop) {
            case 0:
                if(spin == spin_flag) return false;
                if (FIELD[fake_y][fake_x] != 0) {
                    if(x == 10 || x == 11) x_corr = -1;
                    if (spin_flag == 1) {
                        x+=x_corr;
                    } else if (spin_flag == 4) {
                        x+=x_corr;
                    } else if (spin == 1 || spin == 4) --y;
                } else {
                    return false;
                }
                break;
            case 1:return false;
            case 2:return false;
            case 3:return false;
            case 4:return false;
            case 5:return false;
            case 6:return false;
        }
        return true;
    }
    private void hard_drop() {
        int fast = y;
        while (shadow(fast+1)) {
            fast++;
        }
        shadow = fast;
        if (control == 5 && harddrop) {
            y = shadow;
            sc.score_calcu(0,true);
            harddrop = false;
        }
    } 
    private boolean shadow(int fast) {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if ((Mino.Block[drop][i][j] & spin) == spin) {
                    int fake_x = x + j;
                    int fake_y = fast + i;
                    if (FIELD[fake_y][fake_x] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private void mino_move() {
        switch (control){
            case 1: if (can_move(x-1,y,drop,spin))    x--; 
                    gravity = 450;
                    break;
            case 2: if (can_move(x+1,y,drop,spin))    x++;
                    gravity = 450;
                    break;
            case 3: if (can_move(x,y,drop,spin>>1)) spin = spin>>1;
                    break;
            case 4: if (can_move(x,y,drop,spin<<1)) spin = spin<<1;
                    break;
        }
        spin = spin_direction(spin);
        hard_drop();
    }
    private int spin_direction(int s) {
        if(s >= 16) s = 1;
        else if(s <= 0) s = 8;
        return s;
    }
    public static boolean game_end() {
        if (game_state == 8) return true;
        return false;
    }
    private boolean game_over() {
        for(int j = 5; j < 11; j++) {
            if (FIELD[2][j] != 0) {
                game_state = 8;
            }
        }
        if((game_state & 8) == 0) return false;
        for(int i  = 0; i < FIELD.length; i++) {
            for(int j = 0; j < FIELD[i].length; j++) {
                if (FIELD[i][j] != 0) {
                    FIELD[i][j] = 8;
                }
            }
        }
        if(game_state == 8) return true;
        return false;
    }
    public void keyPressed(KeyEvent e) {
        if(game_state != 16) return;
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_LEFT)  control = 1;
        if(key == KeyEvent.VK_RIGHT) control = 2;
        if(key == KeyEvent.VK_F)     control = 3;
        if(key == KeyEvent.VK_G)     control = 4;
        if(key == KeyEvent.VK_DOWN) gravity = 450;
        if(key == KeyEvent.VK_UP && control == 0) {
            control = 5;
            harddrop = true;
            hard_drop();
            count = mino_landing(15, 0);
        }
        if(key == KeyEvent.VK_SPACE && space == false) {
            hold();
            space = true;
        }
        mino_move();
        repaint();
    } 
    public void keyReleased(KeyEvent e) {
        control = 0;
        gravity = level*20;
        harddrop = false;
    }
    public void keyTyped(KeyEvent e) {}

    private void game_ready_go() {
        repaint();
        game_state = 2;
        sleep(1000);
        game_state = 16;
    } 
    private void sleep(int ms) {
        try{Thread.sleep(ms);
        } catch(InterruptedException e) {}
    }
    TimerTask timer_task = new TimerTask() {
        public void run() {
            game_ready_go();
            while(true) {
                if(game_state == 8) break;
                if(control == 0 && can_move(x,y+1, drop,spin)) y++;
                else {
                    count = mino_landing(count,control);
                }
                System.out.println(count);
                repaint();
                sleep(time-gravity);
            }
            new Save(sc.score());
            timer.cancel();
            timer = null;
            game_state = 1;
        }
    };
}
