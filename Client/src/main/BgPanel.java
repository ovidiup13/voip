package main;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class BgPanel extends JPanel {
    private Image background;
    
    public void setBackground(Image i) {
    	background = i;
    }
    
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
