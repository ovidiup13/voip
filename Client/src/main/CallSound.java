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
    	new Thread(
                new Runnable() {
                    public void run() {
                        try {
                        	play.start();
                            play.loop(Clip.LOOP_CONTINUOUSLY);
                            play.drain();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
    }

    public void initPlay() {
        play = null;
        try {
            File in = new File(filename);
            AudioInputStream soundIn = AudioSystem.getAudioInputStream(in);
            AudioFormat format = soundIn.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            play = (Clip)AudioSystem.getLine(info);
            play.open(soundIn);
            play.start();
 
            FloatControl volume = (FloatControl) play.getControl(FloatControl.Type.VOLUME);
            volume.setValue(1.0f); // Reduce volume by 10 decibels.
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }
    
    public void close(){
        play.close();
    }
    
    public void stop(){
    	play.stop();
    }
    
    public void flush(){
    	
    	play.flush();
    }
    
    public boolean isRunning() {
        return play.isRunning();
    }
    
   

}