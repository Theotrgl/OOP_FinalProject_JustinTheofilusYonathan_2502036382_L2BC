package fp;

import javax.swing.JFrame;

public class Main extends JFrame{
    public Main(){
        add(new Logic());
    }
    public static void main(String[] args) {
        Main game = new Main();
        game.setVisible(true);
        game.setTitle("Not-Pacman");
        game.setSize(380, 420);
        game.setDefaultCloseOperation(EXIT_ON_CLOSE);
        game.setLocationRelativeTo(null);
    }
    
}
