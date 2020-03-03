import java.util.Vector;
import java.sql.*;
public class SQLCommands {
	
	private Vector<String> tables;
	private Vector<String> player = new Vector<String>(14);
	private Vector<String> team = new Vector<String>(4);
	private Vector<String> stadium = new Vector<String>(8);
	private Vector<String> conference = new Vector<String>(4);
	private Vector<String> games = new Vector<String>(8);
	private Vector<String> PGS = new Vector<String>(30);
	private Vector<String> TGS = new Vector<String>(34);
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
		player.add("playerCode");player.add("teamCode");player.add("lastName");player.add("firstName");
		player.add("uniformNumber");player.add("class");player.add("position");player.add("height");
		player.add("weight");player.add("homeTown");player.add("homeState");player.add("homeCountry");
		player.add("lastSchool");player.add("year");
		
		team.add("teamCode"); team.add("teamName"); team.add("conferenceCode"); team.add("year");
		stadium.add("stadiumCode"); stadium.add("stadiumName"); stadium.add("city");
		stadium.add("state");stadium.add("capacity"); stadium.add("surface");
		stadium.add("yearOpened"); stadium.add("year");
		
		conference.add("conferenceCode"); conference.add("conferenceName");
		conference.add("subdivision"); conference.add("year");
		
		games.add("gameCode"); games.add("date"); games.add("visitingTeamCode");
		games.add("homeTeamCode");games.add("stadiumCode"); games.add("attendance");
		games.add("duration"); games.add("year");
		
		PGS.add("playerCode"); PGS.add("gameCode"); PGS.add("rushAtt"); PGS.add("rushYard");
		PGS.add("passAtt"); PGS.add("passComp"); PGS.add("passYard"); PGS.add("passInt");
		PGS.add("rec");PGS.add("recYard"); PGS.add("kickoffRet");PGS.add("kickoffRetYard");
		PGS.add("puntRet"); PGS.add("puntRetYard"); PGS.add("fumRet"); PGS.add("fumRetYard");
		PGS.add("fieldGoalAtt"); PGS.add("fieldGoalMade"); PGS.add("offXPKickAtt"); PGS.add("offXPKickMade");
		PGS.add("points"); PGS.add("punt"); PGS.add("puntYard"); PGS.add("kickoff"); PGS.add("kickoffYard");
		PGS.add("fumble"); PGS.add("tackleSolo"); PGS.add("tackleAssist"); PGS.add("sackYard"); PGS.add("year");
		
		TGS.add("teamCode"); TGS.add("gameCode"); TGS.add("rushAtt"); TGS.add("rushYard");
		TGS.add("passAtt"); TGS.add("passComp"); TGS.add("passYard"); TGS.add("passInt");
		TGS.add("kickoffRet");TGS.add("kickoffRetYard");
		TGS.add("puntRet"); TGS.add("puntRetYard"); TGS.add("fumRet"); TGS.add("fumRetYard");
		TGS.add("fieldGoalAtt"); TGS.add("fieldGoalMade"); TGS.add("offXPKickAtt"); TGS.add("offXPKickMade");
		TGS.add("points"); TGS.add("punt"); TGS.add("puntYard"); TGS.add("kickoff"); TGS.add("kickoffYard");
		TGS.add("fumble"); TGS.add("tackleSolo"); TGS.add("tackleAssist"); TGS.add("sackYard");
		TGS.add("penalty");TGS.add("penaltyYard"); TGS.add("thirdDownAtt"); TGS.add("thirdDownConv");
		TGS.add("fourthDownAtt");TGS.add("fourthDownAtt");TGS.add("year");
		
		
	}
	
	public String playerInfo(String first, String last, String year, boolean csv) {
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
		       for(int i =2; i<player.size(); i++) {
		    		 if(!csv)
		    			 info += player.elementAt(i) + " ";
		    		 else
		    			 info += player.elementAt(i) + ",";
		    	 }
		       info += "\n";
		       while (result.next()) {
		    	 for(int i =2; i<player.size(); i++) {
		    		 if(!csv)
		    			 info += result.getString(player.elementAt(i)) + " ";
		    		 else
		    			 info += result.getString(player.elementAt(i)) + ",";
		    	 }
		    	 info += "\n";
		       }
		} catch (Exception e){
	     System.out.println("Error accessing Database.");
		}
		
		return info;
	}
	
	public String playerTeamInfo(String first, String last, String year, boolean csv) {
		String info = "";
		String sqlStatement = "SELECT player.\"firstName\", player.\"lastName\", team.\"teamName\", team.year FROM player ";
		sqlStatement += "INNER JOIN team ON team.\"teamCode\" = player.\"teamCode\" ";
		sqlStatement += "AND team.year = player.year AND player.\"lastName\" = " + last;
		sqlStatement += " AND player.\"firstName\" = " + first;
		 if(year != "all")
			    sqlStatement += " AND player.year = " + year;
		System.out.println(sqlStatement);
		
		try {
		     //create a statement object
		       Statement stmt = conn.createStatement();
		       //create an SQL statement
		       //send statement to DBMS
		       ResultSet result = stmt.executeQuery(sqlStatement);
		       
		       if(!csv)
	    		   info += "firstName" + " " + "lastName" + " " + "teamName" + " " + "year";
	    	   else
	    		   info += "firstName" + "," + "lastName" + "," + "teamName" + "," + "year";
	    	   info += "\n";

		       //OUTPUT
		       while (result.next()) {
		    	   if(!csv)
		    		   info += result.getString("firstName") + " " + result.getString("lastName") + " " + result.getString("teamName") + " " + result.getString("year");
		    	   else
		    		   info += result.getString("firstName") + "," + result.getString("lastName") + "," + result.getString("teamName") + "," + result.getString("year");
		    	   info += "\n";
		       }
		} catch (Exception e){
	     System.out.println("Error accessing Database.");
		}
		
		
		
		return info;
	}
	
	public String playerStats(String first, String last, String year, boolean csv) {
		String info = "";
		String sqlStatement = "SELECT player.\"firstName\", player.\"lastName\", game.date, \"playerGameStats\".\"rushAtt\", \"playerGameStats\".\"rushYard\", \"playerGameStats\".\"passAtt\", \"playerGameStats\".\"passComp\", \"playerGameStats\".\"passYard\", \"playerGameStats\".\"passInt\", \"playerGameStats\".rec, \"playerGameStats\".\"recYard\", \"playerGameStats\".\"kickoffRet\", \"playerGameStats\".\"kickoffRetYard\", \"playerGameStats\".\"puntRet\", \"playerGameStats\".\"puntRetYard\", \"playerGameStats\".\"fumRet\", \"playerGameStats\".\"fumRetYard\", \"playerGameStats\".\"fieldGoalAtt\", \"playerGameStats\".\"fieldGoalMade\", \"playerGameStats\".\"offXPKickAtt\", \"playerGameStats\".\"offXPKickMade\", \"playerGameStats\".points, \"playerGameStats\".punt, \"playerGameStats\".\"puntYard\", \"playerGameStats\".kickoff, \"playerGameStats\".\"kickoffYard\", \"playerGameStats\".fumble, \"playerGameStats\".\"tackleSolo\", \"playerGameStats\".\"tackleAssist\", \"playerGameStats\".\"sackYard\", \"playerGameStats\".year\n" + 
				"	FROM public.\"playerGameStats\" ";
		sqlStatement += "INNER JOIN player\n" + 
				"	ON player.\"playerCode\" = \"playerGameStats\".\"playerCode\"\n" + 
				"	AND player.year = \"playerGameStats\".year ";
		sqlStatement += "AND player.\"lastName\" = " + last;
		sqlStatement += " AND player.\"firstName\" = " + first;
		if(year != "all")
		    sqlStatement += "AND \"playerGameStats\".year = " + year;
		sqlStatement += " INNER JOIN game\n" + 
				"	ON \"playerGameStats\".\"gameCode\" = game.\"gameCode\"\n" + 
				"	AND \"playerGameStats\".year = game.year";
		System.out.println(sqlStatement);
		
		try {
		     //create a statement object
		       Statement stmt = conn.createStatement();
		       //create an SQL statement
		       //send statement to DBMS
		       ResultSet result = stmt.executeQuery(sqlStatement);

		       //OUTPUT
		       if(!csv) {
	    		   info += "firstName lastName date ";
	    	   }
	    	   else {
	    		   info += "firstName,lastName,date,";
	    	   }
	    	 for(int i =2; i< PGS.size(); i++) {
	    		 if(!csv) 
	    			 info += PGS.elementAt(i) + " ";
	    		 else
	    			 info += PGS.elementAt(i) + ",";
	    	 }
	    	 info += "\n";
		       while (result.next()) {
		    	   if(!csv) {
		    		   info += result.getString("firstName") + " ";
		    		   info += result.getString("lastName") +  " ";
		    		   info += result.getString("date") + " ";
		    	   }
		    	   else {
		    		   info += result.getString("firstName") + ",";
		    		   info += result.getString("lastName") +  ",";
		    		   info += result.getString("date") + ",";
		    	   }
		    	 for(int i =2; i< PGS.size(); i++) {
		    		 if(!csv) 
		    			 info += result.getString(PGS.elementAt(i)) + " ";
		    		 else
		    			 info += result.getString(PGS.elementAt(i)) + ",";
		    	 }
		    	 info += "\n";
		       }
		} catch (Exception e){
	     System.out.println("Error accessing Database.");
		}
		
		return info;
	}
	
	public String compareTwoPlayers(String first1, String last1, String year, String first2, String last2, boolean csv){
		String info = "";
		info = this.playerStats(first1, last1, year, csv);
		info += this.playerStats(first2, last2, year,csv);
		return info;
	}
	
	public String teamPlayer(String name, String year, boolean csv) {
		String info = "";
		String sqlStatement = "SELECT team.\"teamName\", player.\"firstName\", player.\"lastName\", team.year\n" + 
				"	FROM public.team\n" + 
				"	INNER JOIN player\n" + 
				"	ON team.\"teamCode\" = player.\"teamCode\"\n" + 
				"	AND team.year = player.year\n" +
				"	AND team.\"teamName\" =  " + name + "\n";
		if(year != "all")
		    sqlStatement += "	AND team.year = " + year;
		System.out.println(sqlStatement);
		
		try {
		     //create a statement object
		       Statement stmt = conn.createStatement();
		       //create an SQL statement
		       //send statement to DBMS
		       ResultSet result = stmt.executeQuery(sqlStatement);
		       System.out.println("hel");

		       //OUTPUT
		       if(!csv)
	    		   info += "teamName firstName lastName year";
	    	   else
	    		   info += "teamName,firstName,lastName,year";
		       info += "\n";
		       
		       while (result.next()) {
		    	   if(!csv) {
		    		   info += result.getString("teamName") + " ";
		    		   info += result.getString("firstName") +  " ";
		    		   info += result.getString("lastName") + " ";
		    		   info += result.getString("year");
		    		   info += "\n";
		    	   }
		    	   else {
		    		   info += result.getString("teamName") + ",";
		    		   info += result.getString("firstName") +  ",";
		    		   info += result.getString("lastName") + ",";
		    		   info += result.getString("year");
		    		   info += "\n";
		    	   }
		       }
		} catch (Exception e){
	     System.out.println("Error accessing Database.");
		}
		
		return info;
	}
	
	public String teamConference(String name, String year, boolean csv) {
		String info = "";
		String sqlStatement = "SELECT  team.\"teamName\", conference.\"conferenceName\", conference.\"subdivision\", team.year\n" + 
				"	FROM public.team\n" + 
				"	INNER JOIN conference\n" + 
				"	ON team.\"conferenceCode\" = conference.\"conferenceCode\"\n" + 
				"	AND team.year = conference.year\n" + 
				"	AND team.\"teamName\" = " + name + "\n";
		if(year != "all")
		    sqlStatement += "	AND team.year = " + year;
		System.out.println(sqlStatement);
		
		try {
		     //create a statement object
		       Statement stmt = conn.createStatement();
		       //create an SQL statement
		       //send statement to DBMS
		       ResultSet result = stmt.executeQuery(sqlStatement);

		       //OUTPUT
		       
		       if(!csv)
	    		   info += "teamName conferenceName subdivision year";
	    	   else
	    		   info += "teamName,conferenceName,subdivision,year";
		       info += "\n";
		       while (result.next()) {
		    	   if(!csv) {
		    		   info += result.getString("teamName") + " ";
		    		   info += result.getString("conferenceName") +  " ";
		    		   info += result.getString("subdivision") + " ";
		    		   info += result.getString("year");
		    		   info += "\n";
		    	   }
		    	   else {
		    		   info += result.getString("teamName") + ",";
		    		   info += result.getString("conferenceName") +  ",";
		    		   info += result.getString("subdivision") + ",";
		    		   info += result.getString("year");
		    		   info += "\n";
		    	   }
		       }
		} catch (Exception e){
	     System.out.println("Error accessing Database.");
		}
		
		return info;
	}
	
	public String teamStats(String name, String year, boolean csv) {
		String info = "";
		String sqlStatement = "SELECT team.\"teamName\", game.date, \"teamGameStats\".\"rushAtt\", \"teamGameStats\".\"rushYard\", \"teamGameStats\".\"passAtt\", \"teamGameStats\".\"passComp\", \"teamGameStats\".\"passYard\", \"teamGameStats\".\"passInt\", \"teamGameStats\".\"kickoffRet\", \"teamGameStats\".\"kickoffRetYard\", \"teamGameStats\".\"puntRet\", \"teamGameStats\".\"puntRetYard\", \"teamGameStats\".\"fumRet\", \"teamGameStats\".\"fumRetYard\", \"teamGameStats\".\"fieldGoalAtt\", \"teamGameStats\".\"fieldGoalMade\", \"teamGameStats\".\"offXPKickAtt\", \"teamGameStats\".\"offXPKickMade\", \"teamGameStats\".points, \"teamGameStats\".punt, \"teamGameStats\".\"puntYard\", \"teamGameStats\".kickoff, \"teamGameStats\".\"kickoffYard\", \"teamGameStats\".fumble, \"teamGameStats\".\"tackleSolo\", \"teamGameStats\".\"tackleAssist\", \"teamGameStats\".\"sackYard\", \"teamGameStats\".penalty, \"teamGameStats\".\"penaltyYard\", \"teamGameStats\".\"thirdDownAtt\", \"teamGameStats\".\"thirdDownConv\", \"teamGameStats\".\"fourthDownAtt\", \"teamGameStats\".\"fourthDownConv\", \"teamGameStats\".year\n" + 
				"	FROM public.\"teamGameStats\"\n" + 
				"	INNER JOIN team\n" + 
				"	ON team.\"teamCode\" = \"teamGameStats\".\"teamCode\"\n" + 
				"	AND team.year = \"teamGameStats\".year\n" + 
				"	AND team.\"teamName\" = " + name + "\n";
		if(year != "all")
		    sqlStatement += "	AND team.year = " + year;
		sqlStatement += "\n" + 
				"	INNER JOIN game\n" + 
				"	ON \"teamGameStats\".\"gameCode\" = game.\"gameCode\"\n" + 
				"	AND \"teamGameStats\".year = game.year";
		System.out.println(sqlStatement);
		
		try {
		     //create a statement object
		       Statement stmt = conn.createStatement();
		       //create an SQL statement
		       //send statement to DBMS
		       ResultSet result = stmt.executeQuery(sqlStatement);

		       //OUTPUT
		       if(!csv)
	    		   info += "teamName date ";
	    	   else 
	    		   info += "teamName,date,";
	    	   for(int i = 2; i < TGS.size(); i++) {
	    		   if(!csv)
	    			   info += TGS.elementAt(i) + " ";
	    		   else
	    			   info += TGS.elementAt(i) + ","; 
	    	   }
	    	   
	    	   info += "\n";
		       
		       while (result.next()) {
		    	   if(!csv) {
		    		   info += result.getString("teamName") + " ";
		    		   info += result.getString("date") +  " ";
		    	   }
		    	   else {
		    		   info += result.getString("teamName") + ",";
		    		   info += result.getString("date") +  ",";
		    	   }
		    	   for(int i = 2; i < TGS.size(); i++) {
		    		   if(!csv)
		    			   info += result.getString(TGS.elementAt(i)) + " ";
		    		   else
		    			   info += result.getString(TGS.elementAt(i)) + ","; 
		    	   }
		    	   
		    	   info += "\n";
		       }
		} catch (Exception e){
	     System.out.println("Error accessing Database.");
		}
		
		return info;
	}
	
	public String conferenceTeams(String conName, String year, boolean csv) {
	String info = "";
	String sqlStatement = "SELECT team.\"teamName\" ";
	sqlStatement += "FROM public.team ";
	sqlStatement += "INNER JOIN public.conference ";
	sqlStatement += "ON conference.\"conferenceName\"= " + conName + " ";
	sqlStatement += "AND team.\"conferenceCode\" = conference.\"conferenceCode\" ";
	sqlStatement += "AND conference.\"year\" =" + year + " ";
	sqlStatement += "AND team.\"year\" =" + year;
	
	System.out.println(sqlStatement);
	
	try {
	     //create a statement object
	       Statement stmt = conn.createStatement();
	       //create an SQL statement
	       //send statement to DBMS
	       ResultSet result = stmt.executeQuery(sqlStatement);
	       
	       
	       if(!csv) {
	    	   info += "teamName";
	       }
	       else {
	    	   info += "teamName";
	       }
	       info += "\n";
	       //OUTPUT
	       while (result.next()) {
	    	 if(!csv) {
		    	 info += result.getString("teamName"); 
	    	 }
	    	 else {
	    		 info += result.getString("teamName") + ","; 
	    	 }
	    	 info += "\n";
	       }
	} catch (Exception e){
     System.out.println("Error accessing Database.");
	}
	
	return info;
}

	public String allConferences(String year, boolean csv) {
	String info = "";
	String sqlStatement = "SELECT * FROM conference WHERE ";
	sqlStatement += "conference.year = " + year;
	System.out.println(sqlStatement);
	
	try {
	     //create a statement object
	       Statement stmt = conn.createStatement();
	       //create an SQL statement
	       //send statement to DBMS
	       ResultSet result = stmt.executeQuery(sqlStatement);
	       
	       info += "conferenceName\n";
	       //OUTPUT
	       while (result.next()) {
	    	 if(!csv) {
	    		 info += result.getString("conferenceName");
	    	 }
	    	 else {
	    		 info += result.getString("conferenceName");
	    	 }
	    	 info += "\n";
	       }
	} catch (Exception e){
     System.out.println("Error accessing Database.");
	}
	return info;
}

	public String gameInfo(String homeTeam, String visitingTeam, String year, boolean csv) {
	String info = "";
	
	String sqlStatement = "SELECT stadium.\"stadiumName\", game.\"date\",game.\"attendance\", game.\"duration\" ";;
	sqlStatement += "From public.game ";
	sqlStatement += "INNER JOIN public.team ";
	sqlStatement += "ON team.\"teamCode\" = game.\"visitingTeamCode\" ";
	sqlStatement += "AND game.\"year\" = team.\"year\" ";
	sqlStatement += "AND team.\"teamName\" =  " + visitingTeam + " ";
	sqlStatement += "AND game.\"year\" = " + year + " ";
	sqlStatement += "OR (team.\"teamCode\" = game.\"homeTeamCode\" AND game.\"year\" = team.\"year\" AND team.\"teamName\" = " + homeTeam + "  AND game.\"year\" = " + year + ") ";
	sqlStatement += "INNER JOIN public.stadium ";
	sqlStatement += "ON stadium.\"stadiumCode\" = game.\"stadiumCode\" ";
	sqlStatement += "AND stadium.\"year\" = " + year + " ";
	sqlStatement += "GROUP BY ";
	sqlStatement += "stadium.\"stadiumName\", game.\"date\", game.\"attendance\", game.\"duration\" ";
	sqlStatement += "HAVING  ";
	sqlStatement += "COUNT(*) > 1; ";

	System.out.println(sqlStatement);
	
	try {
	     //create a statement object
	       Statement stmt = conn.createStatement();
	       //create an SQL statement
	       //send statement to DBMS
	       ResultSet result = stmt.executeQuery(sqlStatement);
	       
	       if(!csv) {
	    	  info += "stadiumName" + " " + "date" + " " + "attendance" + " " + "duration\n"; 
	       }
	       else {
	    	   info += "stadiumName" + "," + "date" + "," + "attendance" + "," + "duration\n";
	       }
	       //OUTPUT
	       while (result.next()) {
	    	 if(!csv) {
	    		 info += result.getString("stadiumName") + " " + result.getString("date") + " " + result.getString("attendance") + " " + result.getString("duration");
	    	 }
	    	 else {
	    		 info += result.getString("stadiumName") + "," + result.getString("date") + "," + result.getString("attendance") + "," + result.getString("duration");
	    	 }
		     
	    	 
	       }
	       info += "\n";
	} catch (Exception e){
     System.out.println("Error accessing Database.");
	}
	
	return info;
}

	public String homeTeamStats(String homeTeam, String visitingTeam, String year, boolean csv) {
	String info = "";
	
	String sqlStatement = "SELECT game.\"date\",\"teamGameStats\".\"rushAtt\", \"teamGameStats\".\"rushYard\", \"teamGameStats\".\"passAtt\", \"teamGameStats\".\"passComp\", \"teamGameStats\".\"passYard\", \"teamGameStats\".\"passInt\", \"teamGameStats\".\"fieldGoalAtt\", \"teamGameStats\".\"fieldGoalMade\", \"teamGameStats\".\"points\", \"teamGameStats\".\"fumble\", \"teamGameStats\".\"penalty\", \"teamGameStats\".\"penaltyYard\" ";
	sqlStatement += "From public.game ";
	sqlStatement += "INNER JOIN public.team ";
	sqlStatement += "ON team.\"teamCode\" = game.\"visitingTeamCode\" ";
	sqlStatement += "AND game.\"year\" = team.\"year\" ";
	sqlStatement += "AND team.\"teamName\" =  " + visitingTeam + " ";
	sqlStatement += "AND game.\"year\" = " + year + " ";
	sqlStatement += "OR (team.\"teamCode\" = game.\"homeTeamCode\" AND game.\"year\" = team.\"year\" AND team.\"teamName\" = " + homeTeam + "  AND game.\"year\" = " + year + ") ";
	sqlStatement += "INNER JOIN public.\"teamGameStats\" ";
	sqlStatement += "ON \"teamGameStats\".\"gameCode\" = game.\"gameCode\" ";
	sqlStatement += "AND \"teamGameStats\".\"teamCode\" = game.\"homeTeamCode\" ";
	sqlStatement += "GROUP BY ";
	sqlStatement += "game.\"date\",\"teamGameStats\".\"rushAtt\", \"teamGameStats\".\"rushYard\", \"teamGameStats\".\"passAtt\", \"teamGameStats\".\"passComp\", \"teamGameStats\".\"passYard\", \"teamGameStats\".\"passInt\", \"teamGameStats\".\"fieldGoalAtt\", \"teamGameStats\".\"fieldGoalMade\", \"teamGameStats\".\"points\", \"teamGameStats\".\"fumble\", \"teamGameStats\".\"penalty\", \"teamGameStats\".\"penaltyYard\" ";
	sqlStatement += "HAVING  ";
	sqlStatement += "COUNT(*) > 1; ";

	System.out.println(sqlStatement);
	
	try {
	     //create a statement object
	       Statement stmt = conn.createStatement();
	       //create an SQL statement
	       //send statement to DBMS
	       ResultSet result = stmt.executeQuery(sqlStatement);

	       
	       if(!csv) {
	    	  info += "date" + " " + " rushAtt" + " " + "rushYard" + " " + "passAtt" + " " + "passComp" + " " + "passYard" + " " + "passInt" + " " + "fieldGoalAtt" + " " + "fieldGoalMade" + " " + "points" + " " + "fumble" + " " + "penalty" + " " + "penaltyYard"; 
	       }
	       else {
	    	   info += "date" + "," + " rushAtt" + "," + "rushYard" + "," + "passAtt" + "," + "passComp" + "," + "passYard" + "," + "passInt" + "," + "fieldGoalAtt" + "," + "fieldGoalMade" + "," + "points" + "," + "fumble" + "," + "penalty" + "," + "penaltyYard"; 
	       }
	       info += "\n";
	       //OUTPUT
	       while (result.next()) {
	    	 if(!csv) {
	    		 info += result.getString("date") + " " + result.getString("rushAtt") + " " + result.getString("rushYard") + " "  + result.getString("passAtt") + " " + result.getString("passComp") + " " + result.getString("passYard") + " " + result.getString("passInt") + " " + result.getString("fieldGoalAtt") + " " + result.getString("fieldGoalMade") + " " + result.getString("points") + " " + result.getString("fumble") + " " + result.getString("penalty") + " " + result.getString("penaltyYard");
	    	 }
	    	 else {
	    		 info += result.getString("date") + "," + result.getString("rushAtt") + "," + result.getString("rushYard") + ","  + result.getString("passAtt") + "," + result.getString("passComp") + "," + result.getString("passYard") + "," + result.getString("passInt") + "," + result.getString("fieldGoalAtt") + "," + result.getString("fieldGoalMade") + "," + result.getString("points") + "," + result.getString("fumble") + "," + result.getString("penalty") + "," + result.getString("penaltyYard");
	    	 }
	    	 info+= "\n";
	       }
	} catch (Exception e){
     System.out.println("Error accessing Database.");
	}
	
	return info;
}

	public String visitingTeamStats(String homeTeam, String visitingTeam, String year, boolean csv) {
	String info = "";
	
	String sqlStatement = "SELECT game.\"date\",\"teamGameStats\".\"rushAtt\", \"teamGameStats\".\"rushYard\", \"teamGameStats\".\"passAtt\", \"teamGameStats\".\"passComp\", \"teamGameStats\".\"passYard\", \"teamGameStats\".\"passInt\", \"teamGameStats\".\"fieldGoalAtt\", \"teamGameStats\".\"fieldGoalMade\", \"teamGameStats\".\"points\", \"teamGameStats\".\"fumble\", \"teamGameStats\".\"penalty\", \"teamGameStats\".\"penaltyYard\" ";
	sqlStatement += "From public.game ";
	sqlStatement += "INNER JOIN public.team ";
	sqlStatement += "ON team.\"teamCode\" = game.\"visitingTeamCode\" ";
	sqlStatement += "AND game.\"year\" = team.\"year\" ";
	sqlStatement += "AND team.\"teamName\" =  " + visitingTeam + " ";
	sqlStatement += "AND game.\"year\" = " + year + " ";
	sqlStatement += "OR (team.\"teamCode\" = game.\"homeTeamCode\" AND game.\"year\" = team.\"year\" AND team.\"teamName\" = " + homeTeam + "  AND game.\"year\" = " + year + ") ";
	sqlStatement += "INNER JOIN public.\"teamGameStats\" ";
	sqlStatement += "ON \"teamGameStats\".\"gameCode\" = game.\"gameCode\" ";
	sqlStatement += "AND \"teamGameStats\".\"teamCode\" = game.\"visitingTeamCode\" ";
	sqlStatement += "GROUP BY ";
	sqlStatement += "game.\"date\",\"teamGameStats\".\"rushAtt\", \"teamGameStats\".\"rushYard\", \"teamGameStats\".\"passAtt\", \"teamGameStats\".\"passComp\", \"teamGameStats\".\"passYard\", \"teamGameStats\".\"passInt\", \"teamGameStats\".\"fieldGoalAtt\", \"teamGameStats\".\"fieldGoalMade\", \"teamGameStats\".\"points\", \"teamGameStats\".\"fumble\", \"teamGameStats\".\"penalty\", \"teamGameStats\".\"penaltyYard\" ";
	sqlStatement += "HAVING  ";
	sqlStatement += "COUNT(*) > 1; ";

	System.out.println(sqlStatement);
	
	try {
	     //create a statement object
	       Statement stmt = conn.createStatement();
	       //create an SQL statement
	       //send statement to DBMS
	       ResultSet result = stmt.executeQuery(sqlStatement);

	       
	       if(!csv) {
	    	   info += "date" + " " + " rushAtt" + " " + "rushYard" + " " + "passAtt" + " " + "passComp" + " " + "passYard" + " " + "passInt" + " " + "fieldGoalAtt" + " " + "fieldGoalMade" + " " + "points" + " " + "fumble" + " " + "penalty" + " " + "penaltyYard";   
	       }
	       else {
	    	   info += "date" + "," + " rushAtt" + "," + "rushYard" + "," + "passAtt" + "," + "passComp" + "," + "passYard" + "," + "passInt" + "," + "fieldGoalAtt" + "," + "fieldGoalMade" + "," + "points" + "," + "fumble" + "," + "penalty" + "," + "penaltyYard"; 
	       }
	       info += "\n";
	       //OUTPUT
	       while (result.next()) {
	    	   if(!csv) {
	    		   info += result.getString("date") + " " + result.getString("rushAtt") + " " + result.getString("rushYard") + " "  + result.getString("passAtt") + " " + result.getString("passComp") + " " + result.getString("passYard") + " " + result.getString("passInt") + " " + result.getString("fieldGoalAtt") + " " + result.getString("fieldGoalMade") + " " + result.getString("points") + " " + result.getString("fumble") + " " + result.getString("penalty") + " " + result.getString("penaltyYard");
	    	   }
	    	   else {
	    		   info += result.getString("date") + "," + result.getString("rushAtt") + "," + result.getString("rushYard") + ","  + result.getString("passAtt") + "," + result.getString("passComp") + "," + result.getString("passYard") + "," + result.getString("passInt") + "," + result.getString("fieldGoalAtt") + "," + result.getString("fieldGoalMade") + "," + result.getString("points") + "," + result.getString("fumble") + "," + result.getString("penalty") + "," + result.getString("penaltyYard");
	    	   }
	    	   info += "\n";
	       }
	} catch (Exception e){
     System.out.println("Error accessing Database.");
	}
	
	return info;
}

	public String stadiumInfo(String stadiumName, String year, boolean csv) {
	String info = "";
	String sqlStatement ="SELECT stadium.\"stadiumName\", stadium.city, stadium.state, stadium.capacity, stadium.surface, stadium.\"yearOpened\" ";
	sqlStatement += "FROM public.stadium ";
	sqlStatement += "WHERE stadium.\"year\" = " + year + " ";
	sqlStatement += "AND stadium.\"stadiumName\" = " + stadiumName;
	System.out.println(sqlStatement);
	try {
	     //create a statement object
	       Statement stmt = conn.createStatement();
	       //create an SQL statement
	       //send statement to DBMS
	       ResultSet result = stmt.executeQuery(sqlStatement);

	       
	       if(!csv) {
	    	   info += "stadiumName" + " " + "city" + " " + "state" + " " + "capacity" + " " + "surface" + " " + "yearOpened";
	       }
	       else {
	    	   info += "stadiumName" + "," + "city" + "," + "state" + "," + "capacity" + "," + "surface" + "," + "yearOpened";
	       }
	       info += "\n";
	       //OUTPUT
	       while (result.next()) {
	    	   if(!csv) {
	    		   info += result.getString("stadiumName") + " " + result.getString("city") + " " + result.getString("state") + " " + result.getString("capacity") + " " + result.getString("surface") + " " + result.getString("yearOpened");
	    	   }
	    	   else {
	    		   info += result.getString("stadiumName") + "," + result.getString("city") + "," + result.getString("state") + "," + result.getString("capacity") + "," + result.getString("surface") + "," + result.getString("yearOpened");
	    	   }
	    	   info += "\n";
	       }
	} catch (Exception e){
     System.out.println("Error accessing Database.");
	}
	
	return info;
}

	public String gamesPlayedAtStadium(String stadiumName, String year, boolean csv) {
	String info = "";
	String sqlStatement = "SELECT stadium.\"stadiumName\", game.date, team.\"teamName\", stadium.capacity, game.attendance, stadium.surface,stadium.year ";
	sqlStatement += "FROM public.stadium ";
	sqlStatement += "INNER JOIN public.game ";
	sqlStatement += "ON stadium.\"stadiumCode\" = game.\"stadiumCode\" ";
	sqlStatement += "AND stadium.\"year\" = game.\"year\" ";
	sqlStatement += "AND stadium.year = " + year + " ";
	sqlStatement += "AND stadium.\"stadiumName\" = " + stadiumName + " ";
	sqlStatement += "INNER JOIN team ";
	sqlStatement += "ON team.\"teamCode\" = game.\"visitingTeamCode\" ";
	sqlStatement += "AND stadium.year = team.year ";
	System.out.println(sqlStatement);

	try {
	     //create a statement object
	       Statement stmt = conn.createStatement();
	       //create an SQL statement
	       //send statement to DBMS
	       ResultSet result = stmt.executeQuery(sqlStatement);

	       
	       if(!csv) {
	    	   info += "stadiumName" + " " + "date" + " " + "teamName" + " " + "capacity" + " " + "attendance" + " " + "surface" + " " + "year";
	       }
	       else {
	    	   info += "stadiumName" + "," + "date" + "," + "teamName" + "," + "capacity" + "," + "attendance" + "," + "surface" + "," + "year";
	       }
	       info += "\n";
	       //OUTPUT
	       while (result.next()) {
	    	   if(!csv) {
	    		   info += result.getString("stadiumName") + " " + result.getString("date") + " " + result.getString("teamName") + " " + result.getString("capacity") + " " + result.getString("attendance") + " " + result.getString("surface") + " " + result.getString("year");
	    	   }
	    	   else {
	    		   info += result.getString("stadiumName") + "," + result.getString("date") + "," + result.getString("teamName") + "," + result.getString("capacity") + "," + result.getString("attendance") + "," + result.getString("surface") + "," + result.getString("year");
	    	   }
			   info += "\n";

	       }
	} catch (Exception e){
     System.out.println("Error accessing Database.");
	}
	
		
	return info;
}

	public String allStadiumsInfo(String year, boolean csv) {
	String info = "";
	String sqlStatement ="SELECT stadium.\"stadiumName\", stadium.city, stadium.state, stadium.capacity, stadium.surface, stadium.\"yearOpened\" ";
	sqlStatement += "FROM public.stadium ";
	sqlStatement += "WHERE stadium.\"year\" = " + year;
	System.out.println(sqlStatement);
	try {
	     //create a statement object
	       Statement stmt = conn.createStatement();
	       //create an SQL statement
	       //send statement to DBMS
	       ResultSet result = stmt.executeQuery(sqlStatement);

	       
	       if(!csv) {
	    	   info += "stadiumName" + " " + "city" + " " + "state" + " " + "capacity" + " " + "surface" + " " + "yearOpened";
	       }
	       else {
	    	   info += "stadiumName" + "," + "city" + "," + "state" + "," + "capacity" + "," + "surface" + "," + "yearOpened";
	       }
	       info += "\n";
	       //OUTPUT
	       while (result.next()) {
	    	   if(!csv) {
	    		   info += result.getString("stadiumName") + " " + result.getString("city") + " " + result.getString("state") + " " + result.getString("capacity") + " " + result.getString("surface") + " " + result.getString("yearOpened"); 
	    	   }
	    	   else {
	    		   info += result.getString("stadiumName") + "," + result.getString("city") + "," + result.getString("state") + "," + result.getString("capacity") + "," + result.getString("surface") + "," + result.getString("yearOpened");
	    	   }
	    	   info += "\n";
	       }
	} catch (Exception e){
     System.out.println("Error accessing Database.");
	}
	
	
	return info;
}
}
