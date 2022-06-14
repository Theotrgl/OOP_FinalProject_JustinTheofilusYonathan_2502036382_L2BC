package newfp;
//Imported Class
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        //Creating object instance
        JFrame obj = new JFrame();
        Logic game = new Logic();        
        //Setting the window size
        obj.setBounds(10,10,700,600);
        //Setting title
        obj.setTitle("BrickBreaker");
        //Others
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(game);
        
    }
    
}
