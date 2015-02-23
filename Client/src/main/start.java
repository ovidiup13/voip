package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;



// optional class used to display the client in a new layout
// gui main method parameters need to be removed and the constructor uncommented, in order for this to work
// 
public class start {

	public static void main(String[] args) {
		
		try {
			
			 // setTheme(String themeName, String licenseKey, String logoString)
           com.jtattoo.plaf.acryl.AcrylLookAndFeel.setTheme("Small-Font", "INSERT YOUR LICENSE KEY HERE", "my company");
           
           // select Look and Feel
           UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
          GuiMainClient.main(new String[0]);
       }
       catch (Exception ex) {
           ex.printStackTrace();
       }
	}

}
