package sub_panel;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Arrays;
import java.awt.*;

import javax.swing.JPanel;

public class Score_view_panel extends JPanel{

    static boolean load_panel_open;
    private int score_list[] = new int[10];

    public Score_view_panel(int WIDTH, int HEIGHT) {
        setSize(WIDTH, HEIGHT);
        setFocusable(true);
        inputArray_saveData();
    }
    public void paint_record(Graphics g, Font font) {
        for(int i = 0; i < 10; i++) {
            g.drawString((i+1)+"    "+score_list[i],190,90+(i*50));
        }
    }
    private void array_sort() { 
        Arrays.sort(score_list);
        for (int f = 0, l = score_list.length - 1; f < l; f++, l--){
            int temp = score_list[f];
            score_list[f]  = score_list[l];
            score_list[l] = temp;
        }
    }
    public void inputArray_saveData() {
        Path path = Paths.get("saved_score/saveData.txt");
        Charset charset = Charset.forName("shift-JIS");
        try {
      	    List<String> line_list = Files.readAllLines(path,charset);
            String str[] = line_list.toArray(new String[0]);     

            for(int i = 0; i < str.length;i++) {
                score_list[i] = Integer.parseInt(str[i]);
            }
            array_sort();
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }
}