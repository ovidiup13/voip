package database;

import database.ConnectToDb.TypeAction;


 /**
 * @author viktor
 * class for returning multiple parameters for a function 
 * used in addFriend and remove Friend to determine if the action was 
 * for addition : request for friendship or accepting the pending request
 * for deletion: declining the friendship or deleting existing friend 
 */
public class ResultPair {
    private boolean successful;
    private  TypeAction type;

    public ResultPair(boolean first, TypeAction responseFr) {
        this.successful = first;
        this.type = responseFr;
    }

    public boolean getSuccessful() {
        return successful;
    }

    public TypeAction getType() {
        return type;
    }
    
    
}


