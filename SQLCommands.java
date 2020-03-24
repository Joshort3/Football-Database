
import java.util.Vector;
import java.util.Collections;
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
		dbSetupExample my = new dbSetupExample();
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

	public String mostRushYards(String name) {
		String yds = "";
		int givenTeamCode = 0;
		//get team code given name
		String sqlStatement = "SELECT team.\"teamCode\" FROM team WHERE team.\"teamName\" = " + name;
		System.out.println(sqlStatement);
		try {
		    //create a statement object
			Statement stmt = conn.createStatement();
		    //create an SQL statement
		    //send statement to DBMS
		    ResultSet result = stmt.executeQuery(sqlStatement);
		    //OUTPUT
		    while (result.next()) {
		    	givenTeamCode = result.getInt(1);
			    System.out.println("Team code is: " + givenTeamCode);
			    break;
		    }
		} catch (Exception e){
			System.out.println("Error accessing Database.");
		}
		//get all game codes from team game stats that have that team code
		String sqlStatement2 = "SELECT \"teamGameStats\".\"gameCode\" FROM \"teamGameStats\" WHERE \"teamGameStats\".\"teamCode\" = " + givenTeamCode;
		System.out.println(sqlStatement2);
		int size = 0;
		String[] gameCodes = null;
		try {
		    //create a statement object
			Statement stmt2 = conn.createStatement();
		    //create an SQL statement
		    //send statement to DBMS
		    ResultSet result2 = stmt2.executeQuery(sqlStatement2);
		    int sz = 0;
		    //source: https://www.javamadesoeasy.com/2015/11/how-to-get-lengthsize-of-resultset-in.html
		    while (result2.next()) {
		    	sz = sz + 1;
		    }
		    System.out.println("Number of games to check: " + sz);
		    size = sz;
		    //OUTPUT
		    gameCodes = new String[sz];
			int i = 0;
			result2 = stmt2.executeQuery(sqlStatement2);
		    while (result2.next()) {
		    	gameCodes[i] = result2.getString(1);
		    	//gc[i] = gameCodes[i];
			    //System.out.println(gameCodes[i]);
			    i = i + 1;
		    }
		} catch (Exception e){
			System.out.println("Error accessing Database.");
		}
		//get all opposing teams with same game code
		int maxYards = 0;
		for (int j = 0; j < size; j++) {
			String sqlStatement3 = "SELECT \"teamGameStats\".\"rushYard\" FROM \"teamGameStats\" WHERE \"teamGameStats\".\"gameCode\" = " + gameCodes[j];
			sqlStatement3 += " AND NOT \"teamGameStats\".\"teamCode\" = " + givenTeamCode;
			//System.out.println(sqlStatement3);
			try {
			    //create a statement object
				Statement stmt3 = conn.createStatement();
			    //create an SQL statement
			    //send statement to DBMS
			    ResultSet result3 = stmt3.executeQuery(sqlStatement3);
			    //OUTPUT
			    while (result3.next()) {
			    	int rushYard = result3.getInt(1);
			    	if (rushYard > maxYards) {
			    		maxYards = rushYard;
			    	}
			    }
			} catch (Exception e){
				System.out.println("Error accessing Database.");
			}
		}
		yds = "The max rush yards is " + maxYards;
		//System.out.println(yds);
		//find highest rushing yards from those games made by opposing team
		return yds;
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
	
	//Victory Chain Functions
	public String victoryChainInfo(String winner, String loser, String year, boolean csv) {
		String infoStart = "Victory Chain for " + winner.replace("'", "") + " over " + loser.replace("'", "")
			+ " is:\n"; //output variable
		String winnerTeamCode = "";
		String loserTeamCode = "";
		String info = "";
		
		try {
			winnerTeamCode += getTeamCode(winner, year);
			//System.out.println(winnerTeamCode);
			
			loserTeamCode += getTeamCode(loser, year);
			//System.out.println(loserTeamCode);
			
			String winnerTeamGameCodesPlayed[] = getGameCodes(winnerTeamCode).split(",");
			String loserTeamGameCodesPlayed[] = getGameCodes(loserTeamCode).split(",");
			/*System.out.println(winnerTeamGameCodesPlayed.length);
			for(int i = 0; i < winnerTeamGameCodesPlayed.length; i++) {
				System.out.println(winnerTeamGameCodesPlayed[i]);
			}*/
			//direct chain
			for(int i = 0; i < winnerTeamGameCodesPlayed.length; i++) {
				for(int j = 0; j < loserTeamGameCodesPlayed.length; j++) {
					if(winnerTeamGameCodesPlayed[i].equals(loserTeamGameCodesPlayed[j])) {
						//System.out.println("Direct chain");
						//determine who won
						//if desired winner won, we have our chain
						if(didWinnerWinGame(winnerTeamCode, loserTeamCode, winnerTeamGameCodesPlayed[i])) {
							//System.out.println(winnerTeamGameCodesPlayed[i]);
							info += loser.replace("'", "") + ", " + getGameYear(winnerTeamGameCodesPlayed[i]) + "\n";
							//System.out.println(info);
							return infoStart + info;
						}
					}
				}
			}
			//2 chain
			info += deeperChain(winnerTeamGameCodesPlayed, winnerTeamCode, loserTeamGameCodesPlayed, loserTeamCode, loser);
			
			//3 chain
			/* Commented out due to taking over 10 minutes to run/test
			if(info.equals("")) {
				System.out.println("Potential 3 chain");
				String teamsWinnerPlayed[][] = new String[winnerTeamGameCodesPlayed.length][2];
				for(int i = 0; i < teamsWinnerPlayed.length; i++) {
					teamsWinnerPlayed[i][0] = getTeamFromGame(winnerTeamGameCodesPlayed[i], winnerTeamCode);
					teamsWinnerPlayed[i][1] = getGameYear(winnerTeamGameCodesPlayed[i]);
				}
				String listWinnerWon = "";
				String listWinnerWonYear = "";
				for(int i = 0; i < teamsWinnerPlayed.length; i++) {
					if(didWinnerWinGame(winnerTeamCode, teamsWinnerPlayed[i][0], winnerTeamGameCodesPlayed[i])) {
						listWinnerWon += teamsWinnerPlayed[i][0] + ",";
						listWinnerWonYear += teamsWinnerPlayed[i][1] + ",";
					}
				}
				String listWinnerWonArray[] = listWinnerWon.split(","); //array of all teamCodes winnerTeam beat
				String listWinnerWonArrayYear[] = listWinnerWonYear.split(","); //array of all years
				for(int i = 0; i < listWinnerWonArray.length; i++) {
					System.out.println(listWinnerWonArray[i]);
					String temp = getTeamNameFromCode(listWinnerWonArray[i]) + ", " + listWinnerWonArrayYear[i] + "\n"; //setup top level of chain
					String teamGameCodesPlayed_lvl2[] = getGameCodes(listWinnerWonArray[i]).split(",");
					info += deeperChain(teamGameCodesPlayed_lvl2, listWinnerWonArray[i], loserTeamGameCodesPlayed, loserTeamCode, loser);
					if(!info.equals("")) {
						info += temp + info;
						return info;
					}
				}
			}*/
			if(info.equals("")) {
				info += "The chain is too deep to be found in a timely manner.";
			}
		} catch(Exception e) {
			System.out.println("Error accessing database");
		}
		
		return infoStart + info;
	}
	
	private String getTeamCode(String teamName, String year) {
		String teamCodeString = "";
		String sqlTeamCode = "SELECT team.\"teamCode\" FROM public.team";
		sqlTeamCode += " WHERE team.\"teamName\" = " + teamName;
		sqlTeamCode += " AND team.year = " + year;
		//System.out.println(teamName);
		try {
			Statement stmt = conn.createStatement();
			
			ResultSet teamCodeResult = stmt.executeQuery(sqlTeamCode);			
			while(teamCodeResult.next()) {
				teamCodeString += teamCodeResult.getString("teamCode");
			}
			
		} catch (Exception e) {
			System.out.println("Could not get Team Code");
		}
		return teamCodeString;
	}
	
	private String getGameCodes(String teamCode) { 
		String gameCodesString = "";
		String sqlGameCodes = "SELECT game.\"gameCode\" FROM public.game";
		sqlGameCodes += " WHERE (game.\"homeTeamCode\" = " + teamCode + " OR game.\"visitingTeamCode\" = " + teamCode + ")";
		
		try {
			Statement stmt = conn.createStatement();
			
			for(int i = 2005; i <= 2013; i++) {
				String temp = sqlGameCodes + " AND game.year = " + i;
				
				ResultSet gameCodeResults = stmt.executeQuery(temp);
				while(gameCodeResults.next()) {
					gameCodesString += gameCodeResults.getString("gameCode") + ",";
				}
			}
		} catch(Exception e) {
			System.out.println("Could not get Game Codes");
			System.out.println("TC: "+ teamCode);
		}
		//System.out.println(gameCodesString);
		return gameCodesString;
	}
	
	private boolean didWinnerWinGame(String winnerTeam, String loserTeam, String gameCode) {
		boolean didWinnerWin = true;
		try {
			String winnerPoints = getGamePoints(gameCode, winnerTeam);
			String loserPoints = getGamePoints(gameCode, loserTeam);
			if(Integer.parseInt(winnerPoints) < Integer.parseInt(loserPoints)) {
				didWinnerWin = false;
			}
		} catch(Exception e) {
			System.out.println("Cannot determine a winner");
		}
		return didWinnerWin;
	}
	
	private String getGamePoints(String gameCode, String teamCode) {
		String points = "";
		String sqlGetPoints = "SELECT \"teamGameStats\".points FROM public.\"teamGameStats\"";
		sqlGetPoints += " WHERE \"teamGameStats\".\"gameCode\" = " + gameCode;
		sqlGetPoints += " AND \"teamGameStats\".\"teamCode\" = " + teamCode;
		//System.out.println(sqlGetPoints);
		try {
			Statement stmt = conn.createStatement();
			ResultSet gamePointsResult = stmt.executeQuery(sqlGetPoints);
			while(gamePointsResult.next()) {
				points += gamePointsResult.getString("points");
			}
		} catch(Exception e) {
			System.out.println("Could not get points");
		}
		
		return points;
	}
	
	private String getGameYear(String gameCode) {
		String year = "";
		String sqlGetYear = "SELECT game.year FROM public.game";
		sqlGetYear += " WHERE game.\"gameCode\" = " + gameCode;
		try {
			Statement stmt = conn.createStatement();
			ResultSet gameYearResult = stmt.executeQuery(sqlGetYear);
			while(gameYearResult.next()) {
				year += gameYearResult.getString("year");
			}
		} catch(Exception e) {
			System.out.println("Could not get year");
		}
		return year;
	}
	
	private String getTeamFromGame(String gameCode, String redundantTeam) {
		String teamCode = "";
		String sqlGetTeam = "SELECT \"teamGameStats\".\"teamCode\" from public.\"teamGameStats\"";
		sqlGetTeam += " WHERE \"teamGameStats\".\"gameCode\" = " + gameCode;
		sqlGetTeam += " AND (NOT \"teamGameStats\".\"teamCode\" = " + redundantTeam + ")";
		//System.out.println(sqlGetTeam);
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet teamCodeResult = stmt.executeQuery(sqlGetTeam);
			while(teamCodeResult.next()) {
				teamCode += teamCodeResult.getString("teamCode");
			}
		} catch(Exception e) {
			System.out.println("Cannot get teamCode from gameCode");
		}
		
		return teamCode;
	}
	
	private String getTeamNameFromCode(String teamCode) {
		String teamName = "";
		String sqlGetTeamName = "SELECT team.\"teamName\" FROM public.team";
		sqlGetTeamName += " WHERE team.\"teamCode\" = " + teamCode;
		sqlGetTeamName += " AND team.year = 2005";
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet teamNameResult = stmt.executeQuery(sqlGetTeamName);
			while(teamNameResult.next()) {
				teamName += teamNameResult.getString("teamName");
			}
		} catch(Exception e) {
			System.out.println("Could not get teamName from teamCode");
		}
		
		return teamName;
	}
	
	private String deeperChain(String winnerTeamGameCodesPlayed[], String winnerTeamCode, String loserTeamGameCodesPlayed[], String loserTeamCode, String loser) {
		String info = "";
		String teamsWinnerPlayed[][] = new String[winnerTeamGameCodesPlayed.length][2];
		for(int i = 0; i < teamsWinnerPlayed.length; i++) {
			teamsWinnerPlayed[i][0] = getTeamFromGame(winnerTeamGameCodesPlayed[i], winnerTeamCode);
			teamsWinnerPlayed[i][1] = getGameYear(winnerTeamGameCodesPlayed[i]);
		}
		String listWinnerWon = "";
		String listWinnerWonYear = "";
		for(int i = 0; i < teamsWinnerPlayed.length; i++) {
			if(didWinnerWinGame(winnerTeamCode, teamsWinnerPlayed[i][0], winnerTeamGameCodesPlayed[i])) {
				listWinnerWon += teamsWinnerPlayed[i][0] + ",";
				listWinnerWonYear += teamsWinnerPlayed[i][1] + ",";
			}
		}
		String listWinnerWonArray[] = listWinnerWon.split(","); //array of all teamCodes winnerTeam beat
		String listWinnerWonArrayYear[] = listWinnerWonYear.split(","); //array of all years
		for(int i = 0; i < listWinnerWonArray.length; i++) {
			String gamesWinnerWonTeamPlayed[] = getGameCodes(listWinnerWonArray[i]).split(",");
			for(int j = 0; j < gamesWinnerWonTeamPlayed.length; j++) {
				for(int k = 0; k < loserTeamGameCodesPlayed.length; k++) {
					if(gamesWinnerWonTeamPlayed[j].equals(loserTeamGameCodesPlayed[k])) {
						if(didWinnerWinGame(listWinnerWonArray[i], loserTeamCode, loserTeamGameCodesPlayed[k])) {
							String winnerWonTeamCode = getTeamFromGame(loserTeamGameCodesPlayed[k], loserTeamCode);
							String winnerWonTeamName = getTeamNameFromCode(winnerWonTeamCode);
							info += winnerWonTeamName + ", " + listWinnerWonArrayYear[i] + "\n";
							info += loser.replace("'", "") + ", " + getGameYear(loserTeamGameCodesPlayed[k]);
							return info;
						}
					}
				}
			}
		}
		return info;
	}
	
	public String playerConnections(String first1, String last1,String first2, String last2) {
	String info = "";
	String info1 = teamCon(first1, last1, first2, last2);
	info = info1;
	if(info.equals("") || info.substring(info.length() -1 ).equals(2)) {	
		String info2 = hometownCon(first1, last1, first2, last2);
		String info3 = gameCon(first1, last1, first2, last2);
		if(!info2.equals("") && !info3.equals("")) {
			if(info2.substring(info2.length()-1).compareTo(info3.substring(info3.length()-1)) <= 0) {
				if(info1.equals("") || (info2.substring(info2.length()-1).compareTo(info1.substring(info1.length()-1)) < 0) )
				info = info2;
				else {
					info = info3;
				}
			}
			else {
				info = info3;
			}
		}
	}
	
	if(info.equals(""))
		info = "No connection between players.";
	else
		info = info.substring(0,info.length() -1);
	return info;
}
public String hometownCon(String first1, String last1,String first2, String last2) {
	String info = "";
	String sqlStatement1 = "SELECT  \"teamCode\", \"lastName\", \"firstName\", \"homeTown\", \"homeState\", year\n" + 
			"	FROM public.player\n" + 
			"	WHERE \"player\".\"firstName\" = ";
	sqlStatement1 += first1  + '\n' +
			"	AND \"lastName\" = ";
	sqlStatement1 += last1  + ";";
	
	String sqlStatement2 = "SELECT  \"teamCode\", \"lastName\", \"firstName\", \"homeTown\", \"homeState\", year\n" + 
			"	FROM public.player\n" + 
			"	WHERE \"player\".\"firstName\" = ";
	sqlStatement2 += first2  + '\n' +
			"	AND \"lastName\" = ";
	sqlStatement2 += last2  + ";";
	//System.out.println(sqlStatement1);
	//System.out.println(sqlStatement2);
	try {
	     //create a statement object
	       Statement stmt1 = conn.createStatement();
	       //create an SQL statement
	       //send statement to DBMS
	       ResultSet result1 = stmt1.executeQuery(sqlStatement1);
	       Statement stmt2 = conn.createStatement();
	       ResultSet result2 = stmt2.executeQuery(sqlStatement2);
	       //OUTPUT
	       String hometown1 = "";
	       String homeState1 = "";
	       String hometown2 = "";
	       String homeState2 = "";
	       String teamCode2 = " ";
	       while(result1.next()) {
	    	   if(result1.getString("homeState") != null) {
	    		   homeState1 = result1.getString("homeState");
	    		   hometown1 = result1.getString("homeTown");
	    	   }
	       }
	       while(result2.next()) {
	    	   teamCode2 = result2.getString("teamCode");
	    	   if(result2.getString("homeState") != null) {
	    		   homeState2 = result2.getString("homeState");
	    		   hometown2 = result2.getString("homeTown");
	    		   teamCode2 =result2.getString("teamCode");
	    	   }
	       }
	       
	       if(hometown1.equals(hometown2) && homeState1.equals(homeState2)) {
	    	   info = first1 + " " + last1 + " and " + first2 + " " + last2 + " are from the same hometown of " + hometown1 + "," + homeState1 + ".1";
	       }
	       
	       else {
	    	 String infoPoss = "";
	    	  String connection = "5";
	    	  String  sqlHomeTeam = "SELECT  \"teamCode\", \"lastName\", \"firstName\",\"homeTown\", \"homeState\", \"homeCountry\", year\n" + 
	    	   		"	FROM public.player\n" + 
	    	   		"	Where \"homeTown\" = '" + hometown1 + "';";
	    	   Statement stmt = conn.createStatement();
		       ResultSet result = stmt.executeQuery(sqlHomeTeam);
		       String first = "";
		       String last = ""; 
		       while(result.next()) {
		    	   //System.out.println(connection.compareTo(infoPoss.substring(infoPoss.length() - 1)) < 0);
		    	   if(teamCode2.equals(result.getString("teamCode")) && (infoPoss.isEmpty() || (connection.compareTo(infoPoss.substring(infoPoss.length() - 1)) < 0))) {
		    		   first = "'" + result.getString("firstName") + "'";
		    		   last =  "'" + result.getString("lastName") + "'";
		    		   infoPoss = teamCon(first, last, first2, last2); 
		    		   connection = infoPoss.substring(infoPoss.length()-1);
		    	   }
		       }
		       if (!infoPoss.isEmpty()) {
		    	   connection = Integer.toString(Integer.parseInt(connection)+ 1);
		    	   info = first1 + " " + last1 + " and " + first + " " + last + " are from the same hometown of " + hometown1 + "," + homeState1 + ", ";
		    	   info += infoPoss.substring(0, infoPoss.length()-1) + (connection);
		    	   //System.out.println(info); 
		    	    
		       }
	       }

	} catch (Exception e){
     System.out.println("Error accessing Database.");
	}
	
	return info;
}
public String teamCon(String first1, String last1,String first2, String last2) {
	String info = "";
	String sqlStatement1 = "SELECT  \"teamCode\", \"lastName\", \"firstName\", year\n" + 
			"	FROM public.player\n" + 
			"	WHERE \"firstName\" = ";
	sqlStatement1 += first1  + '\n' +
			"	AND \"lastName\" = ";
	sqlStatement1 += last1  + ";";
	
	String sqlStatement2 = "SELECT  \"teamCode\", \"lastName\", \"firstName\", year\n" + 
			"	FROM public.player\n" + 
			"	WHERE \"firstName\" = ";
	sqlStatement2 += first2  + '\n' +
			"	AND \"lastName\" = ";
	sqlStatement2 += last2  + ";";
	
	try {
	     //create a statement object
	       Statement stmt1 = conn.createStatement();
	       //create an SQL statement
	       //send statement to DBMS
	       ResultSet result1 = stmt1.executeQuery(sqlStatement1);
	       Statement stmt2 = conn.createStatement();
	       ResultSet result2 = stmt2.executeQuery(sqlStatement2);
	       //OUTPUT
	       result1.next();
	       result2.next();
	       if(result1.getInt("teamCode") == result2.getInt("teamCode")) {
	    	   //System.out.println(sqlStatement1);
	    	   //System.out.println(sqlStatement2);
	    	   String sqlTeamStatment = "SELECT \"teamName\"\n" + 
	    	   		"	FROM public.team\n" + 
	    	   		"	WHERE \"teamCode\" = ";
	    	   sqlTeamStatment += result1.getString("teamCode");
	    	   Statement stmt3 = conn.createStatement();
		       ResultSet result3 = stmt3.executeQuery(sqlTeamStatment);
		       result3.next();
	    	   Vector<String> years1 = new Vector<String>();
	    	   Vector<String> years2 = new Vector<String>();
	    	   years1.add(result1.getString("year"));
	    	   while(result1.next()) {
	    		   years1.add(result1.getString("year"));
	    	   }
	    	   years2.add(result2.getString("year"));
	    	   while(result2.next()) {
	    		   years2.add(result2.getString("year"));
	    	   }
	    	   Collections.sort(years1);
	    	   Collections.sort(years2);
	    	   int idx = -1;
	    	   for(int i = 0; i < years1.size(); i++) {
	    		   for(int j = 0; j < years2.size(); j++) {
	    			   if(years1.elementAt(i).equals(years2.elementAt(j))) {
	    				   idx = i;

	    			   }
		    	   }
	    	   }
	    	   if(idx == -1) {
	    		   info = first1 + " " + last1 + " was on " + result3.getString("teamName") + " team from " + years1.elementAt(0);
	    		   info += "-" + years1.elementAt(years1.size() - 1) + ", while " + first2 + " " + last2 + " was on the same team from ";
	    		   info += years2.elementAt(0) + "-" +  years2.elementAt(years2.size() - 1) + ".2";
	    	   }
	    	   else {
	    		   info = first1 + " " + last1 + " and  " + first2 + " " + last2 + " both were on " + result3.getString("teamName");
	    		   info += " in " + years1.elementAt(idx) + ".1";
	    	   }
	       }

	} catch (Exception e){
     System.out.println("Error accessing Database.");
	}
	
	return info;
}

public String gameCon(String first1, String last1,String first2, String last2) {
	String info = "";
	String sqlStatement1 = "SELECT \"team\".\"teamName\", \"player\".\"teamCode\", \"player\".\"lastName\", \"player\".\"firstName\", game.\"gameCode\", team.\"teamCode\", game.\"visitingTeamCode\", game.\"homeTeamCode\", \"player\".year\n" + 
			"	FROM public.player\n" + 
			"	INNER JOIN team\n" + 
			"	ON team.year = \"player\".year\n" + 
			"	AND \"player\".\"teamCode\" = team.\"teamCode\"\n" + 
			"	AND \"player\".\"firstName\" = ";
	sqlStatement1 += first1  + '\n' +
			"	AND \"lastName\" = ";
	sqlStatement1 += last1  + "\n" + 
			"	INNER JOIN game\n" + 
			"	ON game.year = team.year\n" + 
			"	AND (game.\"visitingTeamCode\" = team.\"teamCode\"\n" + 
			"	OR game.\"homeTeamCode\" = team.\"teamCode\");";
	
	String sqlStatement2 = "SELECT \"team\".\"teamName\", \"player\".\"teamCode\", \"player\".\"lastName\", \"player\".\"firstName\", game.\"gameCode\", team.\"teamCode\", game.\"visitingTeamCode\", game.\"homeTeamCode\", \"player\".year\n" + 
			"	FROM public.player\n" + 
			"	INNER JOIN team\n" + 
			"	ON team.year = \"player\".year\n" + 
			"	AND \"player\".\"teamCode\" = team.\"teamCode\"\n" + 
			"	AND \"player\".\"firstName\" = ";
	sqlStatement2 += first2  + '\n' +
			"	AND \"lastName\" = ";
	sqlStatement2 += last2  + "\n" + 
			"	INNER JOIN game\n" + 
			"	ON game.year = team.year\n" + 
			"	AND (game.\"visitingTeamCode\" = team.\"teamCode\"\n" + 
			"	OR game.\"homeTeamCode\" = team.\"teamCode\");";
	
	try {
	     //create a statement object
	       Statement stmt1 = conn.createStatement();
	       //create an SQL statement
	       //send statement to DBMS
	       ResultSet result1 = stmt1.executeQuery(sqlStatement1);
	       Statement stmt2 = conn.createStatement();
	       ResultSet result2 = stmt2.executeQuery(sqlStatement2);
	       //OUTPUT
	       Vector<String> years1 = new Vector<String>(1000);
	       Vector<String> game1 = new Vector<String>(1000);
	       Vector<String> years2 = new Vector<String>(1000);
	       Vector<String> game2 = new Vector<String>(1000);
	       Vector<String> homeCode1 = new Vector<String>(1000);
	       Vector<String> visitingCode1 = new Vector<String>(1000);
	       Vector<String> homeCode2 = new Vector<String>(1000);
	       Vector<String> visitingCode2 = new Vector<String>(1000);
	       
	       String team1 = "";
	       String teamCode1 = "";
	       String team2 = "";
	       String teamCode2 = "";
	       while(result1.next()) {
	    	   years1.add(result1.getString("year"));
	    	   game1.add(result1.getString("gameCode"));
	    	   homeCode1.add(result1.getString("homeTeamCode"));
	    	   visitingCode1.add(result1.getString("visitingTeamCode"));
	    	   if(team1 == "") {
	    		   team1 = result1.getString("teamName");
	    		   teamCode1 = result1.getString("teamCode");
	    	   }
	       }
	       
	       while(result2.next()) {
	    	   years2.add(result2.getString("year"));
	    	   game2.add(result2.getString("gameCode"));
	    	   homeCode2.add(result2.getString("homeTeamCode"));
	    	   visitingCode2.add(result2.getString("visitingTeamCode"));
	    	   if(team2 == "") {
	    		   team2 = result2.getString("teamName");
	    		   teamCode2 = result2.getString("teamCode");
	    	   }
	       }
	
	       String year = "";
	       String code = "";
	       
	       for(int i = 0; i < years1.size(); i++) {
    		   for(int j = 0; j < years2.size(); j++) {
    			   if(game1.elementAt(i).equals(game2.elementAt(j)) && years1.elementAt(i).equals(years2.elementAt(j))) {
    				  // if(year.compareTo(years1.elementAt(i)) == -1){
    					   year = years1.elementAt(i);
    					   code = game1.elementAt(i);
    				   //}

    			   }
	    	   }
	       }
	       if(year != "") {
	    	   info = first1 + " " + last1 + " played for " + team1 + " in " + year +", which played against " + team2 + " in " + year;
	    	   info += ", which had " + first2 + " " + last2 + " in the game.1";
	       }
	       else {
	    	   for(int i = 0; i < years1.size(); i++) {
	    		   for(int j = 0; j < years2.size(); j++) {
	    			   if(homeCode1.elementAt(i).equals(teamCode2) || visitingCode1.elementAt(i).equals(teamCode2)) {
	    				  // if(year.compareTo(years1.elementAt(i)) == -1){
	    					   info = first1 + " " + last1 + " played for " + team1 + " in " + years1.elementAt(i) + ", which played against " + team2;
	    					   info += " in " + years1.elementAt(i) +  ", which is the team " + first2 + " " + last2 + " played for in " + years2.elementAt(0) + ".2";
	    					   return info;
	    				   //}
	    			   }
	    			   
	    			   else if ((homeCode1.elementAt(i).equals(homeCode2.elementAt(j)) || visitingCode1.elementAt(i).equals(visitingCode2.elementAt(j))) && info.equals("")) {
	    				   String interTeamCode = " ";
	    				   if (!homeCode1.elementAt(i).equals(teamCode1))
	    					   interTeamCode = homeCode1.elementAt(i);
	    				   else
	    					   interTeamCode = visitingCode1.elementAt(i);
	    				   String sqlTeamName = "SELECT \"teamName\"\n" + 
	    				   		"	FROM public.team\n" + 
	    				   		"	WHERE \"teamCode\" = " + interTeamCode;
	    				   Statement stmt = conn.createStatement();
	    			       ResultSet result = stmt.executeQuery(sqlTeamName);
	    			       result.next();
	    				   info = first1 + " " + last1 + " played for " + team1 + " in " + years1.elementAt(i) + ", which played against " + result.getString("teamName");
	    				   info += " in " + years1.elementAt(i) + " , which played " + team2 + "in " + years2.elementAt(j) + ", which is the team ";
	    				   info += first2 + " " + last2 + " played for in " + years2.elementAt(j) + ".3";
	    			   }
		    	   }
		       }
	       }

	} catch (Exception e){
     System.out.println("Error accessing Database.");
	}
	
	return info;
}
	//End Victory Chain Functions
}