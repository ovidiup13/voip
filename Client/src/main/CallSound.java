package main;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Ovidiu on 24/02/2015.
 */
public class CallSound extends Thread {

    private String sound;
    private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private DataLine.Info info;
    private Clip clip;
    
    private String filename;
    
    public CallSound(String filename){
        this.filename = filename;
    }
    
    public void run() {
        playSound();
    }
    
    public void playMP3(){
        Media hit = new Media(filename);
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }

    public void playSound() {

        try {
            audioStream = AudioSystem.getAudioInputStream(new File(filename));
            audioFormat = audioStream.getFormat();
            info = new DataLine.Info(Clip.class, audioFormat);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        clip.start();
    }
    
    public void close(){
        clip.close();
    }
    
    
    
    public static void main(String[] args){
        CallSound sound = new CallSound("Client/src/main/skype.wav");
        sound.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        sound.close();

    }
}