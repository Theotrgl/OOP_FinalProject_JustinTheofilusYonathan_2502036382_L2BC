package newfp;

//Imported Classes
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapMaker2 {
    //Initializing variables
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    //Class constructor
    public MapMaker2 (int row, int col){
        map = new int[row][col];
        //Returning whether each box in table exists or not
        //1 = Exists
        //0 = Doesn't Exist
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                map[i][j] = 1;
            }
        }
    
        
        brickWidth = 540/col;
        brickHeight = 150/row;
    }
    //Method to draw the bricks
    public void drawBricks(Graphics2D g){
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                if(map[i][j] > 0){
                    //Drawing big rectangle
                    g.setColor(Color.red);
                    g.fillRect(j*brickWidth +80, i*brickHeight + 50, brickWidth, brickHeight);
                    
                    //Separating big rectangle into smaller bricks
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j*brickWidth +80, i*brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }
    
    //Method to allow bricks to disappear
    public void setBrickValue(int value, int row, int col){
        map[row][col] = value;
    }
}
