package gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*Main screen for VoIP application.
  From here user can view contacts, their details and make calls
  Last updated: 20/01/2015*/

//@SuppressWarnings("serial")
public class Home extends JFrame{
    //CallWindow call = new CallWindow();
    private final ImageIcon image1;
    private final JLabel label1;
    private final JPanel mainPanel;
    private final JLabel username;
    private final JLabel dob;
    private final JLabel email;
    private final JPanel infoPanel;
    private final JPanel backPanel;
    
    
    private static void createGUI() {
        //Create and set up the window.
        Home first = new Home();
        first.setTitle("Home");
        first.setSize(500, 800);
        first.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        first.setVisible(true);
     
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });
    }
    
    
    
    public Home(){ 
        
        
        //This is the list of contacts that the user can call.
        //Just now it is just a generic example, will need to be integrated to system
        //Clicking on a label will invoke the call window
  
        String labels[] = { "User1", "User2", "User3", "User4", "User5", "User6", "User7", "User8" };
        JList jlist = new JList(labels);
        JScrollPane scrollPane1 = new JScrollPane(jlist);
        
        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
        boolean adjust = listSelectionEvent.getValueIsAdjusting();
        
        if (!adjust) {
          JList list = (JList) listSelectionEvent.getSource();
          int selections[] = list.getSelectedIndices();
          for (int i = 0, n = selections.length; i < n; i++) {
            if (i == 0) {
              CallWindow call = new CallWindow();
              call.setSize(300, 400);
              call.setVisible(true);
            }
          }
        }
      }
    };
    jlist.addListSelectionListener(listSelectionListener);
        
        
        
        
        Menu menu1 = new Menu();
        menu1.menu(this);
        
        username = new JLabel("username");
        username.setSize(20, 20);
        dob = new JLabel("DoB");
        email = new JLabel("Email Address");
        
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        backPanel = new JPanel();
        backPanel.setLayout(new GridLayout(2, 1, 0, 0));
        
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
  
        
        image1 = new ImageIcon(getClass().getResource("icon_person7.gif"));
        label1 = new JLabel(image1);
        label1.setBorder(BorderFactory.createLineBorder(Color.blue));
        
        mainPanel.add(label1);
        infoPanel.add(username);
        infoPanel.add(dob);
        infoPanel.add(email);
        mainPanel.add(infoPanel);
        backPanel.add(mainPanel);
        backPanel.add(scrollPane1);
        label1.setSize(new Dimension(20, 50));
        add(backPanel);
      
        }

    class Menu extends JFrame implements ActionListener{
        //Creates a menu for the user, containing exiting abilities and user help
        
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem eMenuItem = new JMenuItem("Exit");
        JMenuItem eMenuItem2 = new JMenuItem("Help");
        public void menu(JFrame frame){
            
            file.setMnemonic(KeyEvent.VK_F);

            eMenuItem.setMnemonic(KeyEvent.VK_E);
            eMenuItem2.setMnemonic(KeyEvent.VK_H);
            eMenuItem.setToolTipText("Exit application");
            eMenuItem2.setToolTipText("User Manual");
            eMenuItem.addActionListener(this);
   
            eMenuItem2.addActionListener(this);
                
            file.add(eMenuItem);
            file.add(eMenuItem2);
            menuBar.add(file);
            frame.setJMenuBar(menuBar);
            frame.setTitle("VoIP");
            frame.setSize(1000,800);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
        }
    
        @Override
        public void actionPerformed(ActionEvent event){
            if (event.getSource() == eMenuItem){
                System.exit(0);
            }
            else if (event.getSource() == eMenuItem2){
                menuHelp help = new menuHelp();
            }
        }
    
   }
    
    class menuHelp{
         JFrame help = new JFrame();
         public menuHelp(){
            help.setTitle("Help");
            help.setSize(500, 500);
            help.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            help.setVisible(true);
         }
    }
}
        
    
    
 
