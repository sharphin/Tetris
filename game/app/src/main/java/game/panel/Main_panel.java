package game.panel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

import game.logic.Mino;
import game.util.GameUtil;

public class Main_panel extends JPanel implements KeyListener,Runnable{
    Mino mino = new Mino();
    final int  TILE_SIZE = 20;
    final int  TILE_MARGIN = 5;
    private int x = 4;
    private int y = 0;
    private int droping_mino[][];
    private int gravity = 500;
    private boolean down; 
    private int field[][] = new int[][]{{2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,0,0,0,0,0,0,0,0,0,0,2},
                                        {2,2,2,2,2,2,2,2,2,2,2,2},
    };
    public Main_panel() {
        setSize(GameUtil.PANEL_X, GameUtil.PANEL_Y);
        addKeyListener(this);
        setFocusable(true);
        Thread th = new Thread(this);
        th.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillRect(0, 0, GameUtil.PANEL_X, GameUtil.PANEL_Y);
        g.setColor(Color.WHITE);
        g.drawLine(100,108,375,108);
        for(int i = 1; i < field.length; i++) {
            for(int j = 0; j < field[i].length;j++) {
                if(field[i][j] == 0) continue;
                int tmp = TILE_MARGIN+TILE_SIZE;
                g.fillRect((j*tmp)+90, (i*tmp)+60, TILE_SIZE, TILE_SIZE);
                g.drawRect((j*tmp)+92, (i*tmp)+62, TILE_SIZE, TILE_SIZE);
            }
        }
        for(int i = 0; i < droping_mino.length; i++) {
            for(int j = 0; j < droping_mino[i].length;j++) {
                if(droping_mino[i][j] == 0) continue;
                int tmp = TILE_MARGIN+TILE_SIZE;
                g.drawRect(((x+j)*tmp)+90, ((y+i)*tmp)+60, TILE_SIZE, TILE_SIZE);
                g.drawRect(((x+j)*tmp)+92, ((y+i)*tmp)+62, TILE_SIZE, TILE_SIZE);
            }
        }
    }
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        int direction = 0;
        int fake_x = x;
        int fake_y = y;
        if(key == KeyEvent.VK_G) direction = 1;
        if(key == KeyEvent.VK_F) direction = 2;
        if(direction!= 0) mino_rotate(direction);

        if(key == KeyEvent.VK_RIGHT && x < 10) fake_x++;
        if(key == KeyEvent.VK_LEFT && x > 0) fake_x--;
        if(key == KeyEvent.VK_DOWN && !down) {
            gravity = gravity/2;
            down = true;
        }
        if(can_move(fake_x,fake_y)) {
            x = fake_x;
        }
        repaint();
    }
    private boolean can_move(int x, int y) {
        for(int i = 0; i < droping_mino.length; i++) {
            for(int j = 0; j < droping_mino[i].length;j++) {
                if(droping_mino[i][j] == 0) continue;
                if(field[i+y][j+x] != 0) return false;
            }
        }
        return true;
    }
    private void mino_rotate(int dire) {
        int len = droping_mino.length;
        int rotated[][] = new int[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if(dire == 1)rotated[j][len - 1 - i] = droping_mino[i][j];
                if(dire == 2)rotated[len - 1 - j][i] = droping_mino[i][j];
            }
        }
        droping_mino = rotated;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_DOWN) {
            down = false;
            gravity = gravity * 2;
        }
    }
    public void keyTyped(KeyEvent e) {}
    
    public void run() {
        IO.println("hello thread");
        droping_mino = Mino.minos[0];
        boolean cant_move = false;
        while(true) {
            if(can_move(x, y+1)) {
                y++;
            } else {
                cant_move = true;
            }
            repaint();
            sleep(gravity);
            if(cant_move) {
                if(y == 1) break;
                x = 4;
                y = 0;
                cant_move = false;
            }
        }
        IO.println("bye thread");
    }
    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch(InterruptedException e) {}
    }
}
