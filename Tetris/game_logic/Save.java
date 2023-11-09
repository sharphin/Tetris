package game_logic;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class Save {
    private int score; 
    public Save(int sscore) {
        score = sscore;
        input_savedata();
    }
    static String save_list[] = {"","","","","","","","","",""};
    private void save_data_read() {
        Path path = Paths.get("saved_score/saveData.txt");
        Charset charset = Charset.forName("shift-JIS");
        try {
      	    List<String> lineList = Files.readAllLines(path,charset);
            int i = -1;
            for (String line : lineList) {
                i++;
                save_list[i] = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void input_savedata() {
        save_data_read();
        List<String> list = new ArrayList<String>(10);
        boolean saved = false;
        try {
            Path path = Paths.get("saved_score/savedata.txt");
            for(int i = 0; i < save_list.length; i++) { 
                if(save_list[i].equals("0")) {
                    save_list[i] = score+"";
                    saved = true;
                    break;
                }
            }
            if(!saved) {
                for(int i = 0; i < save_list.length; i++) { 
                    int w = Integer.parseInt(save_list[i]);
                    if(w < score) {
                        save_list[i] = score+"";
                        saved = true;
                        break;
                    }
                }
            }
            list = Arrays.asList(save_list);
            Files.write(path, list, Charset.forName("shift-JIS"));
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}