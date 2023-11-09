package sub_panel;
import java.awt.*;

import java.awt.event.*;
import javax.swing.JPanel;
public class Title extends JPanel{

    private int y = 50;
    static boolean manual_view;
    static boolean record_view;
    
    private Color black = new Color(0, 0, 0, 200);
    private Color cyan = new Color(0,174,239,100);    

    private final Font font = new Font("Serif", Font.BOLD, 28);
    private final Font font_n = new Font("HGPGothicM", Font.PLAIN, 15);
    private Image picture = Toolkit.getDefaultToolkit().getImage("image/how_to_use.jpg");
    private Image title = Toolkit.getDefaultToolkit().getImage("image/title.jpg");
    Score_view_panel svp = new Score_view_panel(512, 480);
 
    public Title(){
        setSize(512, 480);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        if(record_view) {
            svp.paint_record(g, font_n);
            return;
        }
        if(manual_view) {
           g.drawImage(picture,0,0,this);
           return;
        }
        g.drawImage(title,0,0,this);
        g.setColor(black);
        g.fillRoundRect(180,350,200,240,70,70);
        g.setColor(Color.WHITE);
        g.drawRoundRect(180,350,200,240,70,70);
        g.setColor(Color.BLACK);
        g.drawString("START ",230,400);
        g.drawString("RECORD",220,450);
        g.drawString("MANUAL",220,500);
        g.drawString("QUIT ",240,550);
        g.setColor(cyan);
        g.fillRect(190,315+y,180,50);
    }
    public void aaa(int p) {
       y = 50 * p;
    }
    public void conrtoll(KeyEvent e, int key, int v, Boolean on) {
        if(key == KeyEvent.VK_ENTER) {
            if(v == 2) {
                record_view = on;
            } else if(v == 3){
                manual_view = on;
            }
        }
        repaint();
    }
    public boolean record_view_flag() {
        return record_view;
    }
    public boolean manual_view_flag() {
        return manual_view;
    }
}