
import main_panel.Game_view;
import sub_panel.Title;
import javax.swing.*;
import java.awt.event.*;

public final class Tetris extends JFrame implements KeyListener, Runnable{
    public static final int WIDTH = 600;
    public static final int HEIGHT = 690;
    
    private int v = 1;

    Game_view gv;
    Title ti = new Title();

    private static Tetris frame = new Tetris();
    public static void main(String[] args) {
        frame_generator();
    }
    public static Tetris frame_generator() {
        return frame;
    }
    private Tetris() {
        super.add(ti);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH+16, HEIGHT+39);
        setTitle("TETRIS");
        setVisible(true);
        setResizable(false);
        addKeyListener(this);
        Thread th = new Thread(this);
        th.start();
    }
    private void panel_change(JPanel panel, int v) {
        frame_generator().getContentPane().removeAll();	
        frame_generator().add(panel);
        if(v == 1) panel.requestFocusInWindow();
        repaint();
    }
    public void keyPressed(KeyEvent e) {
        int dire = 0;
        int key = e.getKeyCode();
        if(ti.manual_view_flag() || ti.record_view_flag()) {
            ti.conrtoll(e, key, v, false);
        } else {
            if(key == KeyEvent.VK_UP)      dire = 3;
            if(key == KeyEvent.VK_DOWN)    dire = 4;

            if(dire == 3 && v > 1) v--;
            if(dire == 4 && v < 4) v++;
        
            ti.aaa(v);
            if(key == KeyEvent.VK_ENTER) {
                if(v == 4) {
                    System.exit(0);
                } else if(v == 3 || v == 2) {
                    ti.conrtoll(e, key, v, true);
                } else {
                    panel_change(gv = new Game_view(HEIGHT,WIDTH,1), v);
                }
            } 
        }
        repaint();
    } 
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void run() {
        while(true) {
            if(Game_view.game_end()) { 
                try{Thread.sleep(5000);
                } catch(InterruptedException e) {} 
                panel_change(ti, v);
                this.requestFocus();
                gv = null;
            }
            try{
                Thread.sleep(50);
            } catch(InterruptedException e) {}
        }
    }
}