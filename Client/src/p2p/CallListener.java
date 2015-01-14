package p2p;

public interface CallListener {
	public void callFailed(); //fired when call recieves no data for a long time duration. (assumed disconnected)
}
