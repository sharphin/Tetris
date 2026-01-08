package game.frame;
import javax.swing.JFrame;

import game.panel.Main_panel;
import game.panel.Title;
import game.util.GameUtil;

public class BaseFrame extends JFrame{
    Title ti;
    Main_panel mp;
    private static BaseFrame frame = new BaseFrame();

    public static BaseFrame frame_generator() {
        return frame;
    }
    private BaseFrame() {
        mp = new Main_panel();
        mp.requestFocus();
        add(mp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(300,10,GameUtil.FRAME_X, GameUtil.FRAME_Y);
        setTitle("TETRIS");
        setVisible(true);
        setResizable(false);
    }
    public void back_title(Title ti) {
        this.ti = ti;
        frame_generator().getContentPane().removeAll();	
        frame_generator().add(ti);
        //this.requestFocus();
        repaint();
    }
}
