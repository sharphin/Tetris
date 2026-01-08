package game.panel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import game.util.GameUtil;

public class Title extends JPanel implements MouseMotionListener{
    private int y = 60;
    public Title(){
        setSize(GameUtil.PANEL_X, GameUtil.PANEL_Y);
        setBackground(Color.BLACK);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.fillRect(250,235+y,180,6);
    }
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {
    }
}