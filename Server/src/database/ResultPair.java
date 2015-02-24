package database;

 public class ResultPair {
    private boolean successful;
    private  String type;

    public ResultPair(boolean first, String type2) {
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
