package main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Ovidiu on 24/02/2015.
 */
public class CallSound {

    private Clip play;
    private String filename;
    
    public CallSound(String filename){
        this.filename = filename;
        initPlay();
    }
    
    public void start() {
        play.loop(3);
        play.start();
        // Loop until the Clip is not longer running.
        // We loop this way to allow the line to fill, otherwise isRunning will
        // return false
        play.drain();
    }

    public void initPlay() {
        play = null;
        try {
            File in = new File(filename);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(in);
            play = AudioSystem.getClip();
            play.open(audioInputStream);
            FloatControl volume = (FloatControl) play.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(1.0f); // Reduce volume by 10 decibels.
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }
    
    public void close(){
        play.close();
    }
    
    public boolean isRunning() {
        return play.isRunning();
    }
    
    public static void main(String[] args){
        CallSound sound = new CallSound("Client\\src\\main\\skype.wav");
        
        sound.start();
        
        try{
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        sound.close();

    }

}