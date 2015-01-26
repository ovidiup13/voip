package p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

class SimpleVoIPBroadcaster extends Thread {
	private AudioFormat format;
	private DatagramSocket socket;
	private SimpleVoIPCall call;
	private byte sequence;
	
	public SimpleVoIPBroadcaster(DatagramSocket socket, AudioFormat format, SimpleVoIPCall call) {
		this.format = format;
		this.socket = socket;
		this.call = call;
	}
	
	private volatile boolean running = true;
	public void terminate() {
		running = false;
	}
	
	private void insertInt32(byte[] array, int i, int value) {
		array[i++] = (byte)(value >>> 24);
		array[i++] = (byte)(value >>> 16);
		array[i++] = (byte)(value >>> 8);
		array[i++] = (byte)value;
	}
	
	public void run() {
		
		//records audio, broadcasts to target
		
        TargetDataLine line;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
        	call.fireCallFailed();
        	return;
        }
        
        try {
        	line = (TargetDataLine) AudioSystem.getLine(info);
        	line.open(format, (44100*2)/25);
        } catch (Exception e) {
        	//line failed to open!
        	call.fireCallFailed();
        	return;
        }
        
        line.start();
        
        //all packets are prefaced with:
        //4 bytes: "VoIP"
        //4 bytes: Call ID (big endian)
        //1 byte: Sequence Number
        
        byte[] data = new byte[line.getBufferSize()+9];
        data[0] = (byte)'V'; data[1] = (byte)'o'; data[2] = (byte)'I'; data[3] = (byte)'P';
        insertInt32(data, 4, call.callID);
        
        DatagramPacket packet;
        try {
        	packet = new DatagramPacket(data, data.length, socket.getRemoteSocketAddress());
        } catch (Exception e) {
        	call.fireCallFailed();
        	return;
        }
        
        while (running) {
            try {
            	line.read(data, 9, data.length-9);
            	data[8] = sequence++;
            	socket.send(packet);
			} catch (IOException e) {
				if (running) {
					e.printStackTrace();
					call.fireCallFailed();
		        	return;
				}
			}
        }
        
	}
}