package database;

public enum ClientStatus {
	NOT_LOGGED_IN(-1), IDLE(0), IN_CALL(1), WAITING(2);
	
    private int numVal;

    ClientStatus(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
