package game_logic;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Next_mino {
    private int c = 7;
    private int[] Next = {0,1,2,3,4,5,6};
    public Next_mino() {
    }
    public int[] Next() {
        c++;
        if (c > 6) {
            Random rnd = ThreadLocalRandom.current();
            for (int i = Next.length - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);
                
                int tmp = Next[index];
                Next[index] = Next[i];
                Next[i] = tmp;
            }
            c = 0;
        }
        for (int i = 0;i < Next.length-1; i++) {
            int F = Next[i];
            Next[i] = Next[i+1];
            Next[i+1] = F;
        }
        return Next;
    }
}