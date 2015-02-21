package main;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

public class FriendsCellRenderer extends JLabel implements ListCellRenderer<Object> {

	private static ImageIcon onlineStatus[];	
	
	public FriendsCellRenderer() {
		if (onlineStatus == null) {
			//load status images
			//0 - offline, 1 - online, 2 - unavailable for whatever reason
			onlineStatus = new ImageIcon[3];
			for (int i=0; i<3; i++) {
				onlineStatus[i] = createImageIcon("status"+i+".png", "status");
			}
		}
		
		setOpaque(true);
		
		Border paddingBorder = BorderFactory.createEmptyBorder(1,3,1,3); //some padding
		Border border = BorderFactory.createLineBorder(Color.WHITE); //might use this in future

		setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list,
			Object value, int i, boolean selected, boolean focused) {
		setText(value.toString());
		setIcon(onlineStatus[i%3]); //temporary, in future list should contain a special "friends list" object
		if (selected) {
			setBackground(new Color(0xB3ECFF));
		} else {
			setBackground(Color.WHITE);
		}
		
		return this;
	}
	
	private static ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = FriendsCellRenderer.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

}
