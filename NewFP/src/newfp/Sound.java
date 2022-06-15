package newfp;

//Imported Classes
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    //Class Constructor
    public Sound(){
    }
    //initializing clip variable
    Clip clip;
    //Method to set a certain audio file to play
    public void setFile(String soundFileName){
        try{
                File file = new File(soundFileName);
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
        }
        catch(Exception e){
                
        }
    }               
    //Method to play the audio file
    public void play(){
        clip.setFramePosition(0);
        clip.start();
    }
}
