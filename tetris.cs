using System;
using System.Linq;
using System.Drawing;
using System.Threading.Tasks;
using System.Windows.Forms;

class Program : Form
{
        int X = 4;    int Y = 0;    int Time = 500;
        int x = 4;    int y = 0;    int line = 0;
        int S = 0;    int s = 0;

        int Gra = 0;     int Level = 1;
        int Line = 0;    int score = 0;    int normal_score = 0;
 
        readonly int size = 20;

        int []Next = new int[]{0,4,8,12,16,20,24};
        int a = 7;    int b = 0;
        int Drop = 0; 

        bool end = true;
        bool clear = true;
        
        bool fix = false;      bool redrop = false;

        bool Down = false;     int drop_score = 0;

        bool Space = false;    int Hold = 1;

        bool Up = false;       int shadow = 0;    int fast = 0;

        int [,]FIELD = {
        {8,0,0,8,0,0,0,0,8,0,0,8},{8,0,0,8,0,0,0,0,8,0,0,8},{8,8,8,8,0,0,0,0,8,8,8,8},
        {8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},
        {8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},
        {8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},
        {8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},
        {8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},
        {8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},
        {8,0,0,0,0,0,0,0,0,0,0,8},{8,0,0,0,0,0,0,0,0,0,0,8},{8,8,8,8,8,8,8,8,8,8,8,8},
        };
        public static readonly int [,,]Mino = {
        {
        {0,0,0,0},
        {1,1,0,0},
        {0,1,1,0},
        {0,0,0,0},
      },{
        {0,0,0,0},
        {0,0,1,0},
        {0,1,1,0},
        {0,1,0,0},
      },{
        {0,0,0,0},
        {0,0,0,0},
        {1,1,0,0},
        {0,1,1,0},
      },{
        {0,0,0,0},
        {0,1,0,0},
        {1,1,0,0},
        {1,0,0,0},
      },{
        {0,0,0,0},
        {0,1,1,0},
        {1,1,0,0},
        {0,0,0,0},
      },{
        {0,0,0,0},
        {0,1,0,0},
        {0,1,1,0},
        {0,0,1,0},
      },{
        {0,0,0,0},
        {0,0,0,0},
        {0,1,1,0},
        {1,1,0,0},
      },{
        {0,0,0,0},
        {1,0,0,0},
        {1,1,0,0},
        {0,1,0,0},
      },{
        {0,0,0,0},
        {1,0,0,0},
        {1,1,1,0},
        {0,0,0,0},
      },{
        {0,0,0,0},
        {0,1,1,0},
        {0,1,0,0},
        {0,1,0,0},
      },{
        {0,0,0,0},
        {0,0,0,0},
        {1,1,1,0},
        {0,0,1,0},
      },{
        {0,0,0,0},
        {0,1,0,0},
        {0,1,0,0},
        {1,1,0,0},
      },{
        {0,0,0,0},
        {0,0,1,0},
        {1,1,1,0},
        {0,0,0,0},
      },{
        {0,0,0,0},
        {0,1,0,0},
        {0,1,0,0},
        {0,1,1,0},
      },{
        {0,0,0,0},
        {0,0,0,0},
        {1,1,1,0},
        {1,0,0,0},
      },{
        {0,0,0,0},
        {1,1,0,0},
        {0,1,0,0},
        {0,1,0,0},
      },{
        {0,0,0,0},
        {0,1,0,0},
        {1,1,1,0},
        {0,0,0,0},
      },{
        {0,0,0,0},
        {0,1,0,0},
        {0,1,1,0},
        {0,1,0,0},
      },{
        {0,0,0,0},
        {0,0,0,0},
        {1,1,1,0},
        {0,1,0,0},
      },{
        {0,0,0,0},
        {0,1,0,0},
        {1,1,0,0},
        {0,1,0,0},
      },{
        {0,0,0,0},
        {1,1,1,1},
        {0,0,0,0},
        {0,0,0,0},
      },{
        {0,0,1,0},
        {0,0,1,0},
        {0,0,1,0},
        {0,0,1,0},
      },{
        {0,0,0,0},
        {0,0,0,0},
        {1,1,1,1},
        {0,0,0,0},
      },{
        {0,1,0,0},
        {0,1,0,0},
        {0,1,0,0},
        {0,1,0,0},
      },{
        {0,0,0,0},
        {0,1,1,0},
        {0,1,1,0},
        {0,0,0,0},
      },{
        {0,0,0,0},
        {0,1,1,0},
        {0,1,1,0},
        {0,0,0,0},
      },{
        {0,0,0,0},
        {0,1,1,0},
        {0,1,1,0},
        {0,0,0,0},
      },{
        {0,0,0,0},
        {0,1,1,0},
        {0,1,1,0},
        {0,0,0,0},
      }};

        Pen p = new Pen(Color.White,1);
        Pen p1 = new Pen(Color.Red,1);
        SolidBrush white = new SolidBrush(Color.White);
        SolidBrush red = new SolidBrush(Color.Red);
        Font font = new Font("Arial", 30);
        Font font1 = new Font("Arial", 20);
        //Image img;

    public Program() {
        this.Text = "TETRIS";
        this.BackColor = Color.Black;
    }

    void next() { 
        if (clear && GameOver()) {
                hardDrop();
            if (a > 6) {
                a = 0;
                int[] ary = new int[] {0,4,8,12,16,20,24};
                Next = ary.OrderBy(i => Guid.NewGuid()).ToArray();
            }
            if (b == 0) {
                Drop = Next[0];    b = 1;
            }
            if (redrop) {
                next_hold();
                Reset();
                Space = false;
                a++;
            }
        }
    }

    void hold() {
        switch (Hold){
            case 1:Hold = Drop;
                   next_hold();
                   break;
            default: int v = Drop;
                Drop = Hold;
                Hold = v;
                break;
        }
        Reset();
    }

    void next_hold() {
        fix = false;    redrop = false;
        for (int i = 0;i < Next.Length-1; i++) { 
            int F = Next[i];
            Next[i] = Next[i+1];
            Next[i+1] = F;
        }
        Drop = Next[0];     
    }

    bool GameOver() {
        for(int J = 4; J < 7; J++) {
            if (FIELD[2,J] != 0) {
                end = false;
            }
        }
        for(int i  = 0; i < 24; i++) {
            for(int j = 0; j < 12; j++) {
                if (end == false && FIELD[i,j] != 0) {
                    FIELD[i,j] = 8;
                    return false;
                }
            }
        }
        return true;
    }
    void Line_Remove(){
        int how_many_line = 0;
        //redrop = true;

        for(int i = 3; i < 23; i++) {
            int check = 0;
            for(int j = 1; j < 11; j++) {
                if (FIELD[i,j] != 0) {
                    check++;
                }
            }
            if (check == 10 && Line < 200) {
                for(int j = 1; j < 11; j++) {
                    FIELD[i,j] = 0;
                }
                Task.Delay (300-Gra).Wait();
                for(int k = i - 1;k >= 3;k-- ) {
                    for(int j = 0;j < 12;j++ ) {
                        FIELD[k+1,j] = FIELD[k,j];
                    }
                }
                line++;    Line++;    how_many_line++;
                if (Line >= 200) clear = false;
            }
            if (line >= 10) {
                line = 0;
                Level++;
                Gra += 20;
            }
        }
        redrop = true;
        Score(how_many_line);
        how_many_line = 0;
    }
    void blockset() {
        if (fix) {
            Task.Delay (50).Wait();
            fix = false;
            for(int I = 0; I < 4; I++) {
                for(int J = 0; J < 4; J++) {
                    if (Mino[Drop+S,I,J] != 0) {
                        FIELD[I+Y,J+X] = Mino[Drop+S,I,J];
                    }
                }
            }
            Line_Remove();    GameOver();
        }
    }

    void hardDrop() {
        while (Shadow(fast+1)) {
            fast++;
        }
        shadow = fast;
        if (Up) {
            Up = false;
            Y = shadow;
            fix = true;
        }
    }

    void Score(int senn) {
        if (score + (normal_score*Level)+drop_score < 10000000) {
            
            if (senn == 1) normal_score = 40;
            if (senn == 2) normal_score = 100;
            if (senn == 3) normal_score = 300;
            if (senn == 4) normal_score = 1200;

            if (senn != 0)  {
                int n = normal_score*Level;
                score = score + n;
                n = 0;
            }
            if (Down) {
                score =  score + drop_score;
            }
        } else {
            score = 9999999;
        }
    }

    void Reset() {
        X = 4;        x = 4;    Y = 0;    y = 0;
        S = 0;        s = 0;
        shadow = 0;    drop_score = 0;
        fast = 0;     
    }   
 
    bool Shadow(int fast) {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if (Mino[Drop+S,i,j] != 0) {
                    int fake_x = X + j;
                    int fake_y = fast + i;
                    if (FIELD[fake_y,fake_x] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    bool canmove(int x, int y) {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if (Mino[Drop+S,i,j] != 0) {
                    int fake_x = x + j;
                    int fake_y = y + i;
                    if (FIELD[fake_y,fake_x] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    bool canspin(int x, int y,int s) {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if (Mino[s,i,j] != 0) {
                    int fake_x = x + j;
                    int fake_y = y + i;
                    if (FIELD[fake_y,fake_x] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    protected override void OnKeyDown(KeyEventArgs e) {
        if (clear && GameOver()) {
            fast = 3;    shadow = 0;
            if (e.KeyCode == Keys.Left) {
                x = X - 1;    fix = false;
                if (canmove(x,Y)) {
                    X--;
                }
            hardDrop();
            }
            if (e.KeyCode == Keys.Right) {
                x = X + 1;    fix = false;
                if (canmove(x,Y)) {
                    X++;
                }
            hardDrop();
            }
            if (e.KeyCode == Keys.G) {
                s = S + 1;    fix = false;
                if (canspin(X,Y,Drop+s)) {
                    S++;
                }
            hardDrop();
            }
            if (e.KeyCode == Keys.F) {
                s = S + 3;    fix = false;
                if (canspin(X,Y,Drop+s)) {
                    S += 3;
                }
            hardDrop();
            }
            if (e.KeyCode == Keys.Down) {
                Time = 50 + Gra;    Down = true;
            }
            if (e.KeyCode == Keys.Up) {
                Up = true;
            }
            if (e.KeyCode == Keys.Space && Space == false) {
                hold();
                Space = true;
            }
            if (S >= 4) S -= 4;
            if (s >= 4) s -= 4;
        }
    }
    protected override void OnKeyUp(KeyEventArgs e) {
        Time = 500-Gra;    Up = false;    Down = false;
    }

    protected override void OnLoad(EventArgs e) {
        Task.Run(() => {
            while(GameOver() && clear) {
                Task.Delay (Time-Gra).Wait();
                blockset();
                y = Y + 1;
                if (canmove(X,y)) {
                    if (Down) drop_score++;
                    Y++;
                } else {
                    fix = true;
                }
            }
        });
    }

    protected override void OnPaint(PaintEventArgs e)
    {
        this.ClientSize = new Size(700, 820);
        //base.OnPaint (e);
        //if (img == null) img = Image.FromFile("back.jpg");
        //e.Graphics.DrawImage(img, 0, 0,1230,820); 

        DoubleBuffered = true;

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 12; j++) {
                if (FIELD[i,j] == 8)  {
                    e.Graphics.FillRectangle(white,(30*j)+160,(30*i)+40,size,size);
                    e.Graphics.DrawRectangle(p,j*30+163,i*30+43,size,size);
                }
                if (FIELD[i,j] == 1)  {
                    e.Graphics.DrawRectangle(p,(30*j)+160,(30*i)+40,size,size);
                    e.Graphics.DrawRectangle(p,j*30+163,i*30+43,size,size);
                 }
            }
        }
        if (clear && GameOver()) {
            next();    
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (Mino[Drop+S,i,j] != 0)  {
                    e.Graphics.DrawRectangle(p,(X+j)*30+163,(Y+i)*30+43,size,size);
                    e.Graphics.DrawRectangle(p,(X+j)*30+160,(Y+i)*30+40,size,size);
                   
                    if (shadow > Y) e.Graphics.DrawRectangle(p,(X+j)*30+163,(shadow+i)*30+43,size,size);
                }
            }
        }
        int takasa = 90;int wh = 0;    int mar = 0;
        for(int I = 1; I < Next.Length; I++) {
            for(int i  = 0; i < 4; i++) {
                for(int j = 0; j < 4; j++) {
                    if (Mino[Next[I],i,j] != 0) {
                        e.Graphics.DrawRectangle(p,j*(30-mar)+540,i*(30-mar)+takasa,size-wh,size-wh);
                        e.Graphics.DrawRectangle(p,j*(30-mar)+543,i*(30-mar)+takasa+3,size-wh,size-wh);
                    }  
                }
            }
                        takasa += 90;    wh = 5;    mar = 10;
        }
        for(int i  = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if (Mino[Hold,i,j] != 0 && Hold != 1) {
                    e.Graphics.DrawRectangle(p,j * 30+40,i * 30 + 130,size,size);
                    e.Graphics.DrawRectangle(p,j * 30+43,i * 30 + 133,size,size);
                }  
            }
        }
        if (GameOver() == false) e.Graphics.DrawString("GAME OVER",font,red,200,400);

        if (clear == false) e.Graphics.DrawString("GAME CLEAR",font,white,200,400);
            e.Graphics.DrawString(" "+ Level,font1,white,25,400);
            e.Graphics.DrawString(" "+ Line,font1,white,25,330);
            e.Graphics.DrawString(" "+ score,font1,white,25,470);
        Invalidate();
    }

    static void Main()
    {
        System.Windows.Forms.Application.Run(new Program()); 
    }
}
