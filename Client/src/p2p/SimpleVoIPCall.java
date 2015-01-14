package p2p;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;

public class SimpleVoIPCall implements VoIPCall {
	private List<CallListener> listeners;
	private boolean started;
	private DatagramSocket socket;
	private SimpleVoIPBroadcaster broadcaster;
	private SimpleVoIPListener listener;
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
		AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
		try {
			socket = new DatagramSocket(port);
			socket.connect(InetAddress.getByName(ip), port);
		} catch (Exception e) {
			return false;
		}
		
		this.callID = callID;
		broadcaster = new SimpleVoIPBroadcaster(socket, format, this);
		listener = new SimpleVoIPListener(socket, format, this);
		
		broadcaster.start();
		listener.start();
		
		started = true;
		return true;
	}

	@Override
	public void stop() {
		try {
			broadcaster.terminate();
			broadcaster.join();
			listener.terminate();
			listener.join();
			started = false;
		} catch (InterruptedException e) {
			// what should we do here? this should never happen unless we deliberately interrupt the threads.
			e.printStackTrace();
		}
	}

	@Override
	public boolean isActive() {
		return started;
	}
	
	void fireCallFailed() {
		if (started) stop();
		for (CallListener c:listeners) {
			c.callFailed();
		}
	}
}