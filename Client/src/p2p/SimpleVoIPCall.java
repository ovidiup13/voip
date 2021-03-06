package p2p;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;

public class SimpleVoIPCall implements VoIPCall {
	
	public static int bufferSize = 5000;
	public static int packetSize = 41000/30;
	
	private List<CallListener> listeners;
	private boolean started;
	private DatagramSocket socket;
	private SimpleVoIPBroadcaster broadcaster;
	private SimpleVoIPListener listener;
	private SimpleVoIPSequencer sequencer;
	SimpleVoIPSequence seq;
	int callID = -1;
	
	public SimpleVoIPCall() {
		listeners = new ArrayList<CallListener>();
		started = false;
	}
	
	public void addCallFailedListener(CallListener target) {
		listeners.add(target);
	}

	@Override
	public boolean start(String ip, int port, int callID) { //port should be agreed by both parties
		//we want to define a format that is the same for both parties:
		AudioFormat format = new AudioFormat(41000, 16, 1, true, true);
		try {
			socket = new DatagramSocket(port);
			socket.connect(InetAddress.getByName(ip), port);
		} catch (Exception e) {
			System.out.println("can't connect :'((((((");
			return false;
		}
		
		this.callID = callID;
		broadcaster = new SimpleVoIPBroadcaster(socket, format, this);
		listener = new SimpleVoIPListener(socket, format, this);
		sequencer = new SimpleVoIPSequencer(format, this);
		seq = new SimpleVoIPSequence(4);
		
		broadcaster.start();
		listener.start();
		sequencer.start();
		
		started = true;
		return true;
	}

	@Override
	public void stop() {
		if (started) {
			started = false;
			(new SimpleVoIPCallEnder()).start();
		}
	}

	@Override
	public boolean isActive() {
		return started;
	}
	
	synchronized void fireCallFailed() {
		System.out.println("stopping");
		if (started) {
			started = false;
			stop();
		}
	}
	
	private class SimpleVoIPCallEnder extends Thread {
		@Override
		public void run() {
			try {
				broadcaster.terminate();
				listener.terminate();
				sequencer.terminate();
				socket.close();
				
				broadcaster.join();
				listener.join();
				sequencer.join();
			} catch (InterruptedException e) {
				// what should we do here? this should never happen unless we deliberately interrupt the threads.
				e.printStackTrace();
			}
			
			//notify listeners
			for (CallListener c:listeners) {
				c.callFailed();
			}
			System.out.println("ended");
		}
	}
}