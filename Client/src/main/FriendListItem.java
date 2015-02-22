package main;

public class FriendListItem {
	public FriendListItemMode mode;
	public String text; //username OR title text
	public int status;
	
	public FriendListItem(FriendListItemMode mode, String text, int status) {
		this.mode = mode;
		this.text = text;
		this.status = status;
	}
}
