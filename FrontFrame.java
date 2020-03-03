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
		
		SQLCommands x = new SQLCommands();
		
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
		
		String[] years = {"2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013"};
		String[] outputType = {"Text Box", "CVS"}; //Text Box is 0, CSV is 1
		
		//Input Fields for Conference
		JTextField inputConferenceName = new JTextField(15);
		inputConferenceName.setText("Conference Name");
		JComboBox<String> yearChoices = new JComboBox<String>(years);
		JComboBox<String> outputChoices = new JComboBox<String>(outputType);
		JButton getOneConference = new JButton();
		getOneConference.addActionListener(new ActionListener() {
			//for one conference
			public void actionPerformed(ActionEvent e) {
				//get data to db
				String confName = inputConferenceName.getText();
				confName = "'" + confName + "'";
				int index = yearChoices.getSelectedIndex();
				String yearChoice = years[index];
				index = outputChoices.getSelectedIndex();
				boolean outChoice = false; //text box
				if(index == 1) { //CVS
					outChoice = true;
				}
				//send to one conference function
				String output = x.conferenceTeams(confName, yearChoice, outChoice);
				if(outChoice == false) {
					JOptionPane.showMessageDialog(null, output);
				} else {
					csvCreator c = new csvCreator();
					String fileOutputName = confName + ".csv";
					c.CSV(output, fileOutputName);
				}
			}
		});
		getOneConference.setText("Conference Teams");
		JButton getAllConferences = new JButton(); //expecting the string "all"
		getAllConferences.addActionListener(new ActionListener() {
			//for all conferences
			public void actionPerformed(ActionEvent e) {
				//get data to db
				int index = yearChoices.getSelectedIndex();
				String yearChoice = years[index];
				index = outputChoices.getSelectedIndex();
				boolean outChoice = false; //text box
				if(index == 1) { //CVS
					outChoice = true;
				}
				//send to all conference function
				String output = x.allConferences(yearChoice, outChoice);
				if(outChoice == false) {
					JOptionPane.showMessageDialog(null, output);
				} else {
					csvCreator c = new csvCreator();
					String fileOutputName = "all_conferences_" + yearChoice + ".csv";
					c.CSV(output, fileOutputName);
				}
			}
		});
		getAllConferences.setText("All Conferences");
		
		//Conference Panel
		JPanel conferencePane = new JPanel();
		//playerPane.add(new JLabel("First Name:")); labels aren't necessary or consistent with current design
		conferencePane.add(inputConferenceName);
		conferencePane.add(Box.createHorizontalStrut(10));
		conferencePane.add(yearChoices);
		conferencePane.add(Box.createHorizontalStrut(10));
		conferencePane.add(outputChoices);
		conferencePane.add(Box.createHorizontalStrut(10));
		conferencePane.add(getOneConference);
		conferencePane.add(Box.createHorizontalStrut(10));
		conferencePane.add(getAllConferences);
		
		//Input Fields for Games
		JTextField homeTeamName = new JTextField(15);
		homeTeamName.setText("Enter Home Team");
		JTextField visitingTeamName = new JTextField(15);
		visitingTeamName.setText("Enter Visiting Team");
		JButton gameInfo = new JButton();
		gameInfo.addActionListener(new ActionListener() {
			//get game information
			public void actionPerformed(ActionEvent e) {
				String homeName = homeTeamName.getText();
				homeName = "'" + homeName + "'";
				String visitingName = visitingTeamName.getText();
				visitingName = "'" + visitingName + "'";
				int index = yearChoices.getSelectedIndex();
				String yearChoice = years[index];
				index = outputChoices.getSelectedIndex();
				boolean outChoice = false; //text box
				if(index == 1) { //CVS
					outChoice = true;
				}
				//send to get game information SQL
				String output = x.gameInfo(homeName, visitingName, yearChoice, outChoice);
				if(outChoice == false) {
					JOptionPane.showMessageDialog(null, output);
				} else {
					csvCreator c = new csvCreator();
					String fileOutputName = homeName + "_" + visitingName + ".csv";
					c.CSV(output, fileOutputName);
				}
			}
		});
		gameInfo.setText("Get Game Information");
		JButton homeInfo = new JButton();
		homeInfo.addActionListener(new ActionListener() {
			//get home team information
			public void actionPerformed(ActionEvent e) {
				String homeName = homeTeamName.getText();
				homeName = "'" + homeName + "'";
				String visitingName = visitingTeamName.getText();
				visitingName = "'" + visitingName + "'";
				int index = yearChoices.getSelectedIndex();
				String yearChoice = years[index];
				index = outputChoices.getSelectedIndex();
				boolean outChoice = false; //text box
				if(index == 1) { //CVS
					outChoice = true;
				}
				//send to get home team information SQL
				String output = x.homeTeamStats(homeName, visitingName, yearChoice, outChoice);
				if(outChoice == false) {
					JOptionPane.showMessageDialog(null, output);
				} else {
					csvCreator c = new csvCreator();
					String fileOutputName = homeName + ".csv";
					c.CSV(output, fileOutputName);
				}
			}
		});
		homeInfo.setText("Home Team Stats");
		JButton visitingInfo = new JButton();
		visitingInfo.addActionListener(new ActionListener() {
			//get visiting team information
			public void actionPerformed(ActionEvent e) {
				String homeName = homeTeamName.getText();
				homeName = "'" + homeName + "'";
				String visitingName = visitingTeamName.getText();
				visitingName = "'" + visitingName + "'";
				int index = yearChoices.getSelectedIndex();
				String yearChoice = years[index];
				index = outputChoices.getSelectedIndex();
				boolean outChoice = false; //text box
				if(index == 1) { //CVS
					outChoice = true;
				}
				//send to get visiting team information SQL
				String output = x.visitingTeamStats(homeName, visitingName, yearChoice, outChoice);
				if(outChoice == false) {
					JOptionPane.showMessageDialog(null, output);
				} else {
					csvCreator c = new csvCreator();
					String fileOutputName = visitingName + ".csv";
					c.CSV(output, fileOutputName);
				}
			}
		});
		visitingInfo.setText("Visiting Team Stats");
		JComboBox<String> yearChoices2 = new JComboBox<String>(years);
		JComboBox<String> outputChoices2 = new JComboBox<String>(outputType);
		
		//Games Panel
		JPanel gamePanel = new JPanel();
		gamePanel.add(homeTeamName);
		gamePanel.add(Box.createHorizontalStrut(10));
		gamePanel.add(visitingTeamName);
		gamePanel.add(Box.createHorizontalStrut(10));
		gamePanel.add(yearChoices2);
		gamePanel.add(Box.createHorizontalStrut(10));
		gamePanel.add(outputChoices2);
		gamePanel.add(Box.createHorizontalStrut(10));
		gamePanel.add(gameInfo);
		gamePanel.add(Box.createHorizontalStrut(10));
		gamePanel.add(homeInfo);
		gamePanel.add(Box.createHorizontalStrut(10));
		gamePanel.add(visitingInfo);
		
		//Input Fields for Stadium
		JTextField inputStadiumField = new JTextField(15);
		inputStadiumField.setText("Stadium Name");
		JButton stadiumInfo = new JButton();
		stadiumInfo.addActionListener(new ActionListener() {
			//get stadium information
			public void actionPerformed(ActionEvent e) {
				String stadiumName = inputStadiumField.getText();
				stadiumName = "'" + stadiumName + "'";
				int index = yearChoices.getSelectedIndex();
				String yearChoice = years[index];
				index = outputChoices.getSelectedIndex();
				boolean outChoice = false; //text box
				if(index == 1) { //CVS
					outChoice = true;
				}
				//send to get stadium info SQL
				String output = x.stadiumInfo(stadiumName, yearChoice, outChoice);
				if(outChoice == false) {
					JOptionPane.showMessageDialog(null, output);
				} else {
					csvCreator c = new csvCreator();
					String fileOutputName = stadiumName + ".csv";
					c.CSV(output, fileOutputName);
				}
			}
		});
		stadiumInfo.setText("Stadium Info");
		JButton gamesAtStadium = new JButton();
		gamesAtStadium.addActionListener(new ActionListener() {
			//get stadium game information
			public void actionPerformed(ActionEvent e) {
				String stadiumName = inputStadiumField.getText();
				stadiumName = "'" + stadiumName + "'";
				int index = yearChoices.getSelectedIndex();
				String yearChoice = years[index];
				index = outputChoices.getSelectedIndex();
				boolean outChoice = false; //text box
				if(index == 1) { //CVS
					outChoice = true;
				}
				//send to get stadium info SQL
				String output = x.gamesPlayedAtStadium(stadiumName, yearChoice, outChoice);
				if(outChoice == false) {
					JOptionPane.showMessageDialog(null, output);
				} else {
					csvCreator c = new csvCreator();
					String fileOutputName = stadiumName + "_games" + ".csv";
					c.CSV(output, fileOutputName);
				}
			}
		});
		gamesAtStadium.setText("Games Played at Stadium");
		JButton allStadiums = new JButton();
		allStadiums.addActionListener(new ActionListener() {
			//get all stadium information
			public void actionPerformed(ActionEvent e) {
				int index = yearChoices.getSelectedIndex();
				String yearChoice = years[index];
				index = outputChoices.getSelectedIndex();
				boolean outChoice = false; //text box
				if(index == 1) { //CVS
					outChoice = true;
				}
				//send to get all stadium SQL
				String output = x.allStadiumsInfo(yearChoice, outChoice);
				if(outChoice == false) {
					JOptionPane.showMessageDialog(null, output);
				} else {
					csvCreator c = new csvCreator();
					String fileOutputName = "all_stadiums_" + yearChoice + ".csv";
					c.CSV(output, fileOutputName);
				}
			}
		});
		allStadiums.setText("Get All Stadiums & Info");
		JComboBox<String> yearChoices3 = new JComboBox<String>(years);
		JComboBox<String> outputChoices3 = new JComboBox<String>(outputType);
		
		//Stadium Panel
		JPanel stadiumPanel = new JPanel();
		stadiumPanel.add(inputStadiumField);
		stadiumPanel.add(Box.createHorizontalStrut(10));
		stadiumPanel.add(yearChoices3);
		stadiumPanel.add(Box.createHorizontalStrut(10));
		stadiumPanel.add(outputChoices3);
		stadiumPanel.add(Box.createHorizontalStrut(10));
		stadiumPanel.add(stadiumInfo);
		stadiumPanel.add(Box.createHorizontalStrut(10));
		stadiumPanel.add(gamesAtStadium);
		stadiumPanel.add(Box.createHorizontalStrut(10));
		stadiumPanel.add(allStadiums);
		
		class MyItemListener implements ItemListener { 
            public void itemStateChanged(ItemEvent ev) {
                boolean selected = (ev.getStateChange() == ItemEvent.SELECTED);
                AbstractButton button = (AbstractButton) ev.getItemSelectable();
                String command = button.getActionCommand();
                if (selected) {
                    int messageType = -1;
                    String message = "";
                    if (command.equals("PLAYER")) {
                    	playerSearchFrame psf = new playerSearchFrame();
                  	  psf.setVisible(true);
                    } else if (command.equals("TEAM")) {
                    	teamSearchFrame tsf = new teamSearchFrame();
                  	  tsf.setVisible(true);
                    } else if (command.equals("CONFERENCE")) {
                    	messageType = JOptionPane.showConfirmDialog(null, conferencePane, "Conference",
                    			JOptionPane.DEFAULT_OPTION);
                        //messageType = JOptionPane.INFORMATION_MESSAGE;
                        //message = "Selecting this radio should show conference search options";
                    } else if (command.equals("STADIUM")) {
                    	messageType = JOptionPane.showConfirmDialog(null, stadiumPanel, "Stadium",
                    			JOptionPane.DEFAULT_OPTION);
                        //messageType = JOptionPane.INFORMATION_MESSAGE;
                        //message = "Selecting this radio should show stadium search options";
                    } else if (command.equals("GAMES")) {
                    	messageType = JOptionPane.showConfirmDialog(null, gamePanel, "Games",
                    			JOptionPane.DEFAULT_OPTION);
                    	//messageType = JOptionPane.INFORMATION_MESSAGE;
                    	//message = "Selecting this radio should show game search information";
                    }
                    
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
        cont.add(r5);
 
        frontFrame.setVisible(true);
	}

}