import java.util.Vector;
import java.sql.*;
public class SQLCommands {
	
	private Vector<String> tables;
	private Vector<String> player = new Vector<String>(14);
	private Vector<String> team = new Vector<String>(4);
	private Vector<String> stadium = new Vector<String>(8);
	private Vector<String> conference = new Vector<String>(4);
	private Vector<String> games = new Vector<String>(8);
	private Vector<String> playerGameStats = new Vector<String>(30);
	private Vector<String> teamGameStats = new Vector<String>(34);
	private Connection conn;
	
	public SQLCommands() {
		dbSetup my = new dbSetup();
	    //Building the connection
	     conn = null;
	     try {
	        Class.forName("org.postgresql.Driver");
	        conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/cfb_20",
	           my.user, my.pswd);
	     } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        System.exit(0);
	     }//end try catch
	     System.out.println("Opened database successfully");
		player.add("playerCode");
		player.add("teamCode");
		player.add("lastName");
		player.add("firstName");
		player.add("uniformNumber");
		player.add("class");
		player.add("position");
		player.add("height");
		player.add("weight");
		player.add("homeTown");
		player.add("homeState");
		player.add("homeCountry");
		player.add("lastSchool");
		player.add("year");
		team.add("teamCode"); team.add("name"); team.add("conferenceCode"); team.add("year");
		stadium.add("stadiumCode"); stadium.add("name"); stadium.add("city");
		stadium.add("state");stadium.add("capacity"); stadium.add("surface");
		stadium.add("yearOpened"); stadium.add("year");
		conference.add("conferenceCode"); conference.add("name");
		conference.add("subdivision"); conference.add("year");
		games.add("gameCode"); games.add("date"); games.add("visitingTeamCode");
		games.add("homeTeamCode");games.add("stadiumCode"); games.add("attendance");
		games.add("duration"); games.add("year");
	}
	
	public String playerInfo(String first, String last, String year) {
		String info = "";
		String sqlStatement = "SELECT * FROM player WHERE player.\"firstName\" = " + first;
	    sqlStatement += " AND player.\"lastName\" = " + last;
	    if(year != "all")
	    sqlStatement += " AND year = " + year;
	    System.out.println(sqlStatement);
		try {
		     //create a statement object
		       Statement stmt = conn.createStatement();
		       //create an SQL statement
		       //send statement to DBMS
		       ResultSet result = stmt.executeQuery(sqlStatement);

		       //OUTPUT
		       while (result.next()) {
		    	 for(int i =2; i<player.size(); i++) {
			         info += result.getString(player.elementAt(i)) + " ";
		    	 }
		    	 info += "\n";
		       }
		} catch (Exception e){
	     System.out.println("Error accessing Database.");
		}
		
		return info;
	}
}
