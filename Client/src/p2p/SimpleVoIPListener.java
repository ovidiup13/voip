package p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

class SimpleVoIPListener extends Thread {
	private AudioFormat format;
	private DatagramSocket socket;
	private SimpleVoIPCall call;
	private byte sequence;
	
	private int readInt32(byte[] array, int i) {
		return ((array[i]<<24) | (array[i+1]<<16) | (array[i+2]<<8) | (array[i+3]));
	}
	
	public SimpleVoIPListener(DatagramSocket socket, AudioFormat format, SimpleVoIPCall call) {
		this.format = format;
		this.socket = socket;
		this.call = call;
	}
	
	private volatile boolean running = true;
	public void terminate() {
		running = false;
	}
	
	public void run() {
        //listens for packets and sends them to the audio sequencer.
		
        //all packets are prefaced with:
        //4 bytes: "VoIP"
        //4 bytes: Call ID (big endian)
        //1 byte: Sequence Number
        
        byte[] data = new byte[(SimpleVoIPCall.packetSize)+9];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        
        while (running) {
            try {
				socket.receive(packet); //retrieve input byte stream
				byte[] pDat = packet.getData();
				
				//we need to check if this is a valid packet, and its sequence number.
				String magic = new String(new char[]{(char)pDat[0], (char)pDat[1], (char)pDat[2], (char)pDat[3]});
				if (!"VoIP".equals(magic)) {
					System.out.println("recieved bad packet, expected VoIP but got "+magic);
					continue;
				}
				
				//check packet's call id to make sure it matches.
				
				if (call.callID != readInt32(pDat, 4)) {
					System.out.println("recieved packet for wrong call: "+readInt32(pDat, 4));
					continue;
				}
				
				//eventually we will do something with this, but right now just print sequence difference.
				byte recSeq = pDat[8];
				byte sequenceDiff = (byte)(recSeq-sequence);
				if (sequenceDiff != 1) System.out.println("packet sequence change was "+sequenceDiff+" instead of 1. data skip/out of order!");
				sequence = recSeq;
				
				call.seq.add(Arrays.copyOfRange(pDat, 9, pDat.length), sequenceDiff);
			} catch (IOException e) {
				if (running) {
					e.printStackTrace();
					return;
				}
			}
        }
	}
}
