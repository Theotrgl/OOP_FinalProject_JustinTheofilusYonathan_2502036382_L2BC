package newfp;

//Imported Classes
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Logic extends JPanel implements KeyListener, ActionListener {
    
    //Initializing required variables
    private boolean Run = false;
    private boolean Pause = false;
    private int score = 0;
    
    private int numBricks = 21;
    
    private final Timer timer;
    private final int delay = 8;
    
    private int slider_x = 310;
    
    private int ball_x = 350;
    private int ball_y = 350;
    Random random = new Random();
    int n = random.nextInt(-5, 5);  

    private int ballx_dir = n ;
    private int bally_dir = -5;
    
    private MapMaker map;
    
    //Class constructor
    public Logic(){
        map = new MapMaker(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }
    
    @Override
    public void paint(Graphics g){
        //Drawing background
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);
        
        //Drawing map
        map.drawBricks((Graphics2D)g);
        
        //Drawing borders
        g.setColor(Color.white);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(682,0,3,592);
        
        //Drawing scores
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD, 25));
        g.drawString("score: "+ score, 550, 30);
        
        //Drawing slider
        g.setColor(Color.white);
        g.fillRect(slider_x,550,100,8);
        
        //Drawing ball
        g.setColor(Color.yellow);
        g.fillOval(ball_x,ball_y,20,20);
        
        //Drawing text for the win screen
        if(numBricks <= 0){
            Run = false;
            Pause = false;
            ballx_dir = 0;
            bally_dir = 0;
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won!! Final Score: "+score, 150, 300);
            
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart!",230,350); 
        }
        //Drawing text for the lose screen
        if(ball_y > 570){
            Run = false;
            Pause = false;
            ballx_dir = 0;
            bally_dir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Final Score: "+score, 190, 300);
            
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart!",260,350);
        }
        //Drawing text for the pause screen
        if(Pause){
            if (!Run) {
                g.setColor(Color.blue);
                g.setFont(new Font("serif", Font.BOLD, 30));          
                g.drawString("Game Paused", 270, 300);
            }
        }
        
        g.dispose();
        
        
    }
    //Required Methods by ActionListener and KeyListener
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(Run){
            //Implementing collision between ball and slider
            if(new Rectangle(ball_x, ball_y, 20, 20).intersects(new Rectangle(slider_x,550,100,8))){
                bally_dir = - bally_dir;
            }
            
            A: for(int i = 0; i < map.map.length; i++){
                for(int j = 0; j < map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                        int brick_x = j*map.brickWidth + 80;
                        int brick_y = i*map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;
                        //Initializing rectangle which acts as the sensor for the collision
                        Rectangle rect = new Rectangle(brick_x,brick_y,brickWidth,brickHeight);
                        Rectangle ball_rect = new Rectangle(ball_x,ball_y,20,20);
                        Rectangle brick_rect = rect;
                        
                        //Implementing collision between bricks and ball
                        if(ball_rect.intersects(brick_rect)){
                            //Making broken bricks disapear on collision
                            map.setBrickValue(0, i, j);
                            numBricks --;
                            score += 5;
                            //Implementing direction change of ball
                            if(ball_x + 19 <= brick_rect.x || ball_x + 1 >= brick_rect.x + brick_rect.width){
                                ballx_dir = -ballx_dir;
                            }else{
                                bally_dir = -bally_dir;
                            }
                            break A;
                        }
                    }
                }
            }
            
            //Moving the ball
            ball_x += ballx_dir;
            ball_y += bally_dir;
            
            
            //Setting collision points and changing ball direction
            //Collision for left border
            if(ball_x < 0){
                ballx_dir = -ballx_dir;
            //Collition for top border
            }if(ball_y < 0){
                bally_dir = -bally_dir;
            //Collision for right border
            }if(ball_x > 665){
                ballx_dir = -ballx_dir;
            }
        }
        
        repaint();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
    }
     @Override
    public void keyTyped(KeyEvent e) {
    }
    
    //Reading key press input
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            //Setting limits of slider movement
            if(slider_x >+ 580){
                slider_x = 582;
            }else{
                moveRight();
            }            
        }if(e.getKeyCode() == KeyEvent.VK_LEFT){
            //Setting limits of slider movement
            if(slider_x < 5){
                slider_x = 4;
            }else{
                moveLeft();
            }
        //Restarting the game when user wins or loses
        }if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!Run){
                Run = true;
                ball_x = 350;
                ball_y = 350;
                ballx_dir = n;
                bally_dir = -5;
                slider_x = 310;                
                score = 0;
                numBricks = 21;
                map = new MapMaker(3,7);
            }
        //Pausing the game
        }if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            Run = false;
            Pause = true;            
        }
        repaint();
    }
    
    //Method to move sliders
    public void moveRight(){
        Run = true;
        slider_x += 20;
    }
    
    public void moveLeft(){
        Run = true;
        slider_x -= 20;
    }   
}
