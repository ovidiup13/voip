package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

public class FriendsCellRenderer extends JPanel implements ListCellRenderer<FriendListItem> {

	private static ImageIcon onlineStatus[];	
	private static ImageIcon acceptIcon;
	private static ImageIcon declineIcon;
	
	private JLabel label;
	
	private JLabel acceptLabel;
	private JLabel declineLabel;
	private JLabel lastLogin ;
	
	private JPanel buttonsPanel;
	private BoxLayout buttonsLayout;
	
	private BorderLayout layout;
	
	public FriendsCellRenderer() {
		layout = new BorderLayout();
		label = new JLabel();
		this.setLayout(layout);
		
		add(label, BorderLayout.CENTER);
		if (onlineStatus == null) {
			//load status images
			//0 - offline, 1 - online, 2 - unavailable for whatever reason
			onlineStatus = new ImageIcon[3];
			for (int i=0; i<3; i++) {
				onlineStatus[i] = createImageIcon("status"+i+".png", "status");
			}
			//load accept/decline too
			acceptIcon = createImageIcon("acceptn.png", "accept");
			declineIcon = createImageIcon("declinen.png", "decline");
		}
		
		buttonsPanel = new JPanel();
		buttonsLayout = new BoxLayout(buttonsPanel, BoxLayout.X_AXIS);
		buttonsPanel.setBorder(null);
		buttonsPanel.setOpaque(false);
		
		setOpaque(true);
		acceptLabel = new JLabel();
		acceptLabel.setIcon(acceptIcon);
		acceptLabel.setOpaque(false);
		declineLabel = new JLabel();
		declineLabel.setIcon(declineIcon);
		declineLabel.setOpaque(false);
		
		lastLogin= new JLabel();
	
	//	add(lastLogin, BorderLayout.EAST);
		add(buttonsPanel, BorderLayout.LINE_END);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends FriendListItem> list,
			FriendListItem value, int i, boolean selected, boolean focused) {
		
		label.setText(value.text);	
		buttonsPanel.removeAll();
		
		if (value.mode != FriendListItemMode.TITLE) {
			buttonsPanel.add(lastLogin);
			label.setFont(new Font("Verdana", Font.PLAIN, 12));
			label.setForeground((value.status == 0)?Color.GRAY:Color.BLACK); //offline users are grayed out
			lastLogin.setText((value.status == 0)?value.lastSeen:"ONLINE");
			lastLogin.setFont((value.status == 0)?new Font("Serif", Font.ITALIC, 10):new Font("Serif", Font.ITALIC, 10));
			lastLogin.setForeground((value.status == 0)?Color.GRAY:new Color(0x2693FF));
			label.setIcon(onlineStatus[value.status]); //temporary, in future list should contain a special "friends list" object
			if (selected) {
				setBackground(new Color(0xB3ECFF));
			} else {
				setBackground(Color.WHITE);
			}
			
			if (value.mode == FriendListItemMode.REQUEST) {
				buttonsPanel.removeAll(); //remove lastLogin if it is request (LastLogin not visible if pending
				buttonsPanel.add(declineLabel);
				buttonsPanel.add(acceptLabel);
			}
		} else {
			label.setFont(new Font("Verdana", Font.BOLD, 14));
			label.setForeground(new Color(0x2693FF));
			label.setIcon(null);
			setBackground(Color.WHITE);
		}
		
		Border paddingBorder = BorderFactory.createEmptyBorder((value.mode == FriendListItemMode.TITLE)?6:1,3,1,3); //some padding
		Border border = BorderFactory.createLineBorder(Color.WHITE); //might use this in future

		setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
		
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
