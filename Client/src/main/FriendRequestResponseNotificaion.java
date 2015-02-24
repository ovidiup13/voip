package main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class FriendRequestResponseNotificaion {


	public  FriendRequestResponseNotificaion(String username, boolean accepted) {
		String message = null;
		ImageIcon icon = null;
		if (accepted) {
			message = "User " + username + " accepted your friend request";
			icon = createImageIcon("small-tick.png",
					"this is small tick");
		} else {
			message = "User " + username + " declined your friend request";
			icon = createImageIcon("small-x.png",
					"this is small x");
		}
		String header = "Message";
		final JFrame frame = new JFrame();
		frame.setSize(300, 125);
		frame.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		JLabel headingLabel = new JLabel(header);
		//resource = clientThread.getContextClassLoader().getResource("small-tick.png");
		//ImageIcon icon = new ImageIcon(resource);
		headingLabel.setIcon(icon);
		headingLabel.setOpaque(false);
		frame.add(headingLabel, constraints);
		constraints.gridx++;
		constraints.weightx = 0f;
		constraints.weighty = 0f;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.NORTH;
		JButton closeButton = new JButton("X");
		closeButton.setMargin(new Insets(1, 4, 1, 4));
		closeButton.setFocusable(false);
		frame.add(closeButton, constraints);
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		JLabel messageLabel = new JLabel("<Html><div style=\"text-align: center;\">" + message + "</html>");
		frame.add(messageLabel, constraints);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);

		closeButton = new JButton(new AbstractAction("x") {
			public void actionPerformed(final ActionEvent e) {
				frame.dispose();
			}
		});
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
		Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// height of the task bar
		frame.setLocation(scrSize.width - frame.getWidth(), scrSize.height - toolHeight.bottom - frame.getHeight());
		System.out.println(message);
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000); // time after which pop up will be disappeared.
					frame.dispose();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			;
		}.start();
	}
	protected static ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = GuiMainClient.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}


}