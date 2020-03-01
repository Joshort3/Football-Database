package exampleDBGUI;

import java.sql.*;
//import javax.swing.JOptionPane;
import javax.swing.*; //gives me more access to GUI options

//from Radio help
import java.awt.*;
import java.awt.event.*;

public class FrontFrame {
	
	public static void main(String args[]) {
		dbSetupExample dbAccess = new dbSetupExample(); //variable to hold Joseph's login info
		Connection connect = null; //variable to track connection status
		
		try { //connect here
			//Class.forName("org.postgresql.Driver"); //I have no idea what this does
			connect = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/cfb_20",
			           dbAccess.user, dbAccess.pswd); //access our database using login info
		} catch (Exception e) { //handle connect failure here
			e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        System.exit(0);
		}
		
		JOptionPane.showMessageDialog(null, "Opened Database Successfully"); //Database open success message
		
		//https://www.zentut.com/java-swing/joptionpane/
		//Above link was helpful for implementing buttons
		
		final JFrame frontFrame = new JFrame("Front Frame"); //Parameter is box title
		
		//
		class MyItemListener implements ItemListener { 
            public void itemStateChanged(ItemEvent ev) {
                boolean selected = (ev.getStateChange() == ItemEvent.SELECTED);
                AbstractButton button = (AbstractButton) ev.getItemSelectable();
                String command = button.getActionCommand();
                if (selected) {
                    int messageType = -1;
                    String message = "";
                    if (command.equals("PLAYER")) {
                        messageType = JOptionPane.INFORMATION_MESSAGE; //change this to get a real frame
                        message = "Selecting this radio should show player search options";
                    } else if (command.equals("TEAM")) {
                        messageType = JOptionPane.INFORMATION_MESSAGE;
                        message = "Selecting this radio should show team search options";
                    } else if (command.equals("CONFERENCE")) {
                        messageType = JOptionPane.INFORMATION_MESSAGE;
                        message = "Selecting this radio should show conference search options";
                    } else if (command.equals("STADIUM")) {
                        messageType = JOptionPane.INFORMATION_MESSAGE;
                        message = "Selecting this radio should show stadium search options";
                    } else if (command.equals("GAMES")) {
                    	messageType = JOptionPane.INFORMATION_MESSAGE;
                    	message = "Selecting this radio should show game search information";
                    }
                    // show message
                    JOptionPane.showMessageDialog(frontFrame,
                            message,
                            "Message Dialog",
                            messageType);
                }
            }
        }
		
		JRadioButton r1 = new JRadioButton("Player");
		r1.setActionCommand("PLAYER");
		
		JRadioButton r2 = new JRadioButton("Team");
		r2.setActionCommand("TEAM");
		
		JRadioButton r3 = new JRadioButton("Conference");
		r3.setActionCommand("CONFERENCE");
		
		JRadioButton r4 = new JRadioButton("Stadium");
		r4.setActionCommand("STADIUM");
		
		JRadioButton r5 = new JRadioButton("Games");
		r5.setActionCommand("GAMES");
		
		//add event listener
		MyItemListener myItemListener = new MyItemListener();
        r1.addItemListener(myItemListener);
        r2.addItemListener(myItemListener);
        r3.addItemListener(myItemListener);
        r4.addItemListener(myItemListener);
        r5.addItemListener(myItemListener);
 
        // add radio buttons to a ButtonGroup
        final ButtonGroup group = new ButtonGroup();
        group.add(r1);
        group.add(r2);
        group.add(r3);
        group.add(r4);
		group.add(r5);
		
		frontFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frontFrame.setSize(300, 200);
        Container cont = frontFrame.getContentPane();
 
        cont.setLayout(new GridLayout(0, 1));
        cont.add(new JLabel("Please choose the message type:"));
        cont.add(r1);
        cont.add(r2);
        cont.add(r3);
        cont.add(r4);
 
        frontFrame.setVisible(true);
		
		System.out.println("End of code so far");
	}

}
