package database;

 public class addFriendResult {
    private boolean successful;
    private  String type;

    public addFriendResult(boolean first, String type2) {
        this.successful = first;
        this.type = type2;
    }

    public boolean getSuccessful() {
        return successful;
    }

    public String getType() {
        return type;
    }
}
