package p2p;

public interface VoIPCall {
	public void addCallFailedListener(CallListener target);
	public boolean start(String ip, int port, int callID);
	public void stop();
	public boolean isActive();
}
