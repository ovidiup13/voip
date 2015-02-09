package p2p;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

// plays received audio data in sequence. Initially sends silence to buffer before sequenced audio is received.
public class SimpleVoIPSequencer extends Thread {
	private AudioFormat format;
	private SimpleVoIPCall call;
	private byte sequence;
	private int repeat = 0;
	private static final int TIMEOUT_FRAMES = 50*5;
	private static final int REPEAT_FRAMES = 5;
	
	private volatile boolean running = true;
	public void terminate() {
		running = false;
	}
	
	public SimpleVoIPSequencer(AudioFormat format, SimpleVoIPCall call) {
		this.format = format;
		this.call = call;
	}
	
	public void run() {
        //retrieves sequenced audio and plays through the system speakers
		
        SourceDataLine line2;
        DataLine.Info info2 = new DataLine.Info(SourceDataLine.class, format);
        if (!AudioSystem.isLineSupported(info2)) {
        	//no output line??
        	call.fireCallFailed();
        	return;
        }
        try {
			line2 = (SourceDataLine) AudioSystem.getLine(info2);
			line2.open(format, (41000*2)/25);
		} catch (LineUnavailableException e) {
			//could not open output line
			call.fireCallFailed();
			return;
		}
        
        line2.start();
        
        byte[] empty = new byte[line2.getBufferSize()];
        byte[] data;
        byte[] last = empty;
        
        while (running) {
        	data = call.seq.get();
        	if (data != null) {
        		last = data;
        		repeat = 0;
        	} else {
        		if (repeat >= REPEAT_FRAMES) {
        			if (last != empty) {
	        			//only repeat the last sequence entry for a specified number of times. The repetition is to fill gaps,
	        			//but if the repeat happens for a whole second or two the user's ears will bleed, guaranteed.
	        			
	        			last = empty;
	        			System.out.println("nothing for 5 frames, no longer repeating last frame.");
        			}
        			if (repeat >= TIMEOUT_FRAMES) {
        				call.fireCallFailed();
        				return;
        			}
        		} else {
            		System.out.println("got nothing :'(");
        		}
        		repeat++;
        		data = last;
        	}
            line2.write(data, 0, data.length); //write back out to audio
        }
	}
}
