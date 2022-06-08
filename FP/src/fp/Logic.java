package fp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Logic extends JPanel implements ActionListener{
    
    private Dimension d;
    private final Font smallFont = new Font ("Arial", Font.BOLD, 14); 
    private boolean Run = false;
    private boolean Die = false;
    
    private final int BlockSize = 24;
    private final int numBlocks = 15;
    private final int ScreenSize = BlockSize * numBlocks;
    private final int MaxEnemy = 12;
    private final int PlayerSpeed = 6;
    
    private int numEnemy = 6;
    private int lives, score;
    private int [] dx, dy;
    private int [] Enemy_x, Enemy_y, Enemy_dx, Enemy_dy, EnemySpeed;
    
    private Image heart, Enemy;
    private Image up, down, left, right;
    
    private int Player_x, Player_y, Player_dx, Player_dy;
    private int req_dx, req_dy;
    
    private final int validSpeeds[] = {1,2,3,4,6,8};
    private final int maxSpeed = 6;
    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;
    
    private final short levelData[] = {
        19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        25, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
        0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
        19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
        17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 20,
        17, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20,
        21, 0,  0,  0,  0,  0,  0,   0, 17, 16, 16, 16, 16, 16, 20,
        17, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        25, 24, 24, 24, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };
    
    public Logic(){
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }
    
    private void loadImages(){
        down = new ImageIcon("C:\\Users\\WINDOWS10\\Desktop\\Java\\FP\\src\\Images\\down.gif").getImage();
        up = new ImageIcon("C:\\Users\\WINDOWS10\\Desktop\\Java\\FP\\src\\Images\\up.gif").getImage();
        left = new ImageIcon("C:\\Users\\WINDOWS10\\Desktop\\Java\\FP\\src\\Images\\left.gif").getImage();
        right = new ImageIcon("C:\\Users\\WINDOWS10\\Desktop\\Java\\FP\\src\\Images\\right.gif").getImage();
        Enemy = new ImageIcon("C:\\Users\\WINDOWS10\\Desktop\\Java\\FP\\src\\Images\\ghost.png").getImage();
        heart = new ImageIcon("C:\\Users\\WINDOWS10\\Desktop\\Java\\FP\\src\\Images\\heart.png").getImage();
    }
    
    public void ShowIntroScreen(Graphics2D g2d){
        String start = "Press SPACE to start";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, ScreenSize / 4, 150);
    }
    public void drawScore(Graphics2D g2d){
        g2d.setFont(smallFont);
        g2d.setColor(new Color(5,151,79));
        String s = "Score: " + score;
        g2d.drawString(s,ScreenSize/2 +96,ScreenSize + 16);
        
        for(int i = 0; i<lives; i++){
            g2d.drawImage(heart, i*28 + 8, ScreenSize+1, this);
        }
    }
    
    private void initVariables(){
        screenData = new short[numBlocks * numBlocks];
        d = new Dimension(400,400);
        Enemy_x = new int[MaxEnemy];
        Enemy_dx = new int[MaxEnemy];
        Enemy_y = new int[MaxEnemy];
        Enemy_dy = new int[MaxEnemy];
        EnemySpeed = new int[MaxEnemy];
        
        dx = new int [4];
        dy = new int [4];
        
        timer = new Timer(40, this);
        timer.restart();
    }
    
    private void initGame(){
        lives = 3;
        score = 0;
        initLevel();
        numEnemy = 6;
        currentSpeed = 3;
    }
    
    private void initLevel(){
        int i;
        for(i = 0; i < numBlocks*numBlocks; i++){
            screenData[i] = levelData[i];
        }
    }
    
    private void playGame(Graphics2D g2d){
        if (Die){
            death();
            
        }else {
            movePlayer();
            drawPlayer(g2d);
            moveEnemy(g2d);
            checkMaze();
        }
    }
    
    public void movePlayer(){
        int pos;
        short ch;
        
        if (Player_x % BlockSize == 0 && Player_x % BlockSize == 0){
            pos = Player_x / BlockSize + numBlocks * (int)(Player_y / BlockSize);
            ch = screenData[pos];
            
            if((ch & 16) != 0){
                screenData[pos] = (short) (ch & 15);
                score ++;
            }
            if (req_dx != 0 || req_dy != 0){
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) !=0)
                || (req_dy == 1 && req_dy == 0 && (ch & 4)!=0)
                || (req_dx == 0 && req_dy == -1 && (ch & 2)!=0)
                || (req_dx == 0 && req_dy == 1 && (ch & 8)!=0))){
                    Player_dx = req_dx;
                    Player_dy = req_dy;
                }
            }
            if ((Player_dx == -1 & Player_dy == 0 && (ch & 1) !=0)
            || (Player_dy == 1 && Player_dy == 0 && (ch & 4)!=0)
            ||(Player_dx == 0 && Player_dy == -1 && (ch & 2)!=0)
            ||(Player_dx == 0 && req_dy == 1 && (ch & 8)!=0)){
                Player_dx = 0;
                Player_dy = 0;
            }
        }
        Player_x = Player_x + PlayerSpeed * Player_dx;
        Player_y = Player_y + PlayerSpeed * Player_dy;
    }
    
    public void drawPlayer(Graphics2D g2d){
        if (req_dx == -1){
            g2d.drawImage(left, Player_x+1, Player_y+1, this);
        }else if (req_dx == 1){
            g2d.drawImage(right, Player_x+1, Player_y+1, this);
        }else if (req_dy == -1){
            g2d.drawImage(up, Player_x+1, Player_y+1, this);
        }else if (req_dy == 1){
            g2d.drawImage(down, Player_x+1, Player_y+1, this);
        }
    }
    
    public void moveEnemy(Graphics2D g2d){
        int pos;
        int count;
        
        for(int i = 0; i<numEnemy; i++){
            if(Enemy_x[i] % BlockSize == 0 && Enemy_y[i] % BlockSize == 0){
                pos = Enemy_x[i] / BlockSize + numBlocks * (int) (Enemy_y[i] / BlockSize);
                
                count = 0;
                if((screenData[pos] & 1) == 0 && Enemy_dx[i] != 1){
                    dx[count] = -1;
                    dy[count] = 0;
                    count ++;
                }if((screenData[pos] & 2) == 0 && Enemy_dy[i] != 1){
                    dx[count] = 0;
                    dy[count] = -1;
                    count ++;
                }if((screenData[pos] & 4) == 0 && Enemy_dx[i] != -1){
                    dx[count] = 1;
                    dy[count] = 0;
                    count ++;
                }if((screenData[pos] & 8) == 0 && Enemy_dx[i] != -1){
                    dx[count] = 0;
                    dy[count] = 1;
                    count ++;
                } 
                
                if(count == 0){
                    if((screenData[pos] & 15)== 15){
                        Enemy_dy[i] = 0;
                        Enemy_dx[i] = 0;
                    }else {
                        Enemy_dy[i] = -Enemy_dy[i];
                        Enemy_dx[i] = -Enemy_dx[i];
                    }
                }else{
                    count = (int) (Math.random() * count);
                    
                    if(count > 3){
                        count = 3;
                    }
                    
                    Enemy_dx[i] = dx[count];
                    Enemy_dy[i] = dy[count];
                }
            }
            Enemy_x[i] = Enemy_x[i] + (Enemy_dx[i] * EnemySpeed[i]);
            Enemy_y[i] = Enemy_y[i] + (Enemy_dy[i] * EnemySpeed[i]);
            drawEnemy(g2d, Enemy_x[i] + 1, Enemy_y[i] + 1);
            
            if(Player_x > (Enemy_x[i]-12) && Player_x < (Enemy_x[i]+12) 
            && Player_y > (Enemy_y[i]-12) && Player_y < (Enemy_y[i]+12)&& Run){
                Die = true;
            }
        }
    }
    
    public void drawEnemy(Graphics2D g2d, int x, int y){
        g2d.drawImage(Enemy, x, y,this);
    }
    
    public void checkMaze(){
        int i = 0;
        boolean finished = true;
        
        while(i < numBlocks * numBlocks && finished){
            if((screenData[i] & 48)!=0){
                finished = false;
            }
        }i++;
    if (finished){
        score += 50;
        if(numEnemy < MaxEnemy){
            numEnemy++;
        }
        if (currentSpeed < maxSpeed){
            currentSpeed++;
        }
    }       initLevel();
    }
    
    private void death(){
        lives--;
        if(lives == 0){
            Run = false;
        }
        continueLevel();
    }
                
    private void continueLevel(){
        int dx = 1;
        int random;
        for (int i = 0; i < numEnemy; i++){
            Enemy_y[i] = 4 * BlockSize;
            Enemy_x[i] = 4 * BlockSize;
            Enemy_dy[i] = 0;
            Enemy_dx[i] = dx;
            dx = -dx;
            random = (int)(Math.random() * currentSpeed + 1);
            
            if (random > currentSpeed){
                random = currentSpeed;
            }
            
            EnemySpeed[i] = validSpeeds[random];
            
        }
        Player_x = 7 * BlockSize;
        Player_y = 11 * BlockSize;
        Player_dx = 0;
        Player_dy = 0;
        req_dx = 0;
        req_dy = 0;
        Die = false;
    }
    
    public void drawMaze(Graphics2D g2d){
        short i = 0;
        int x,y;
        
        for(y = 0; y<ScreenSize; y+= BlockSize){
            for(x = 0; x<ScreenSize; x+=BlockSize){
                g2d.setColor(new Color(0,72,251));
                g2d.setStroke(new BasicStroke(5));
                
                if((screenData[i] == 0)){
                    g2d.fillRect(x,y,BlockSize, BlockSize); 
                }
                if((screenData[i] & 1)!=0){
                    g2d.drawLine(x, y, x, y + BlockSize -1);
                }
                if ((screenData[i] & 2)!=0){
                    g2d.drawLine(x,y, x + BlockSize  -1, y);
                }
                if ((screenData[i] & 4)!= 0){
                    g2d.drawLine(x + BlockSize - 1, y, x + BlockSize - 1, y + BlockSize - 1);
                }
                if ((screenData[i] & 8)!= 0){
                    g2d.drawLine(x, y + BlockSize - 1, x + BlockSize - 1, y + BlockSize - 1);
                }
                if ((screenData[i] & 16)!= 0){
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x+10, y+10, 6, 6);
                }
                i++;
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);
        
        drawMaze(g2d);
        drawScore(g2d);
        
        if(Run){
            playGame(g2d);
            
        }else {
            ShowIntroScreen(g2d);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            
            if(Run){
                if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT){
                    req_dx = -1;
                    req_dy = 0;
                }
                else if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT){
                    req_dx = 1;
                    req_dy = 0;
                }
                else if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP){
                    req_dx = 0;
                    req_dy = -1;
                }
                else if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN){
                    req_dx = 0;
                    req_dy = 1;
                }
                else if(key == KeyEvent.VK_ESCAPE && timer.isRunning()){
                    Run = false;
                }
            }else if(key == KeyEvent.VK_SPACE){
                Run = true;
                initGame();
            }
        }
    }   
}


