import java.util.Vector;


import java.sql.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class Q4 {
	private Connection conn;
	DecimalFormat value = new DecimalFormat("##.#");
	
	public Q4(){
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
	}
     
	public Vector<String> conferenceTeams(String conName, String year) {
		Vector<String> info = new Vector<String>();
		String sqlStatement = "SELECT team.\"teamName\" ";
		sqlStatement += "FROM public.team ";
		sqlStatement += "INNER JOIN public.conference ";
		sqlStatement += "ON conference.\"conferenceCode\" = team.\"conferenceCode\" ";
		sqlStatement += "AND team.\"conferenceCode\" = conference.\"conferenceCode\" ";
		sqlStatement += "AND conference.\"year\" =" + year + " ";
		sqlStatement += "AND team.\"year\" =" + year;
		sqlStatement += "AND conference.\"conferenceName\" = " + conName + " ";
		
		//System.out.println(sqlStatement);
		
		try {
		     //create a statement object
		       Statement stmt = conn.createStatement();
		       //create an SQL statement
		       //send statement to DBMS
		       ResultSet result = stmt.executeQuery(sqlStatement);

		       //OUTPUT
		       while (result.next()) {
			    	 info.add(result.getString("teamName"));
		       }
		} catch (Exception e){
	     System.out.println("Error accessing Database.");
		}
		
		return info;
		
	}
	
	public Vector<String> getHomeTeamPoints(String teamName, String year, String homeOrVisit){
		Vector<String> info = new Vector<String>();
		String sqlStatement = "SELECT \"teamGameStats\".\"points\" ";
		sqlStatement += "FROM public.team ";
		sqlStatement += "INNER JOIN public.game ";
		sqlStatement += "on game. " +homeOrVisit + " " + " = team.\"teamCode\" ";
		sqlStatement += "AND game.\"year\" = " + year + " ";
		sqlStatement += "AND game.\"year\" = team.\"year\" ";
		sqlStatement += "AND team.\"teamName\" = " + teamName + " ";
		sqlStatement += "INNER JOIN public.\"teamGameStats\" ";
		sqlStatement += "on \"teamGameStats\".\"gameCode\" = game.\"gameCode\" ";
		sqlStatement += "AND \"teamGameStats\".\"teamCode\" = game.\"homeTeamCode\" ";
		sqlStatement += "ORDER BY ";
		sqlStatement += "game.\"date\" ";
			
		
		try {
		     //create a statement object
		       Statement stmt = conn.createStatement();
		       //create an SQL statement
		       //send statement to DBMS
		       ResultSet result = stmt.executeQuery(sqlStatement);

		       //OUTPUT
		       while (result.next()) {
			    	 info.add(result.getString("points"));
		       }
		} catch (Exception e){
	     System.out.println("Error accessing Database.");
		}
		
		return info;
	}
	
	
	public Vector<String> getVisitingTeamPoints(String teamName, String year, String homeOrVisit){
		Vector<String> info = new Vector<String>();
		
		String sqlStatement = "SELECT \"teamGameStats\".\"points\" ";
		sqlStatement += "FROM public.team ";
		sqlStatement += "INNER JOIN public.game ";
		sqlStatement += "on game. " + homeOrVisit + " " + " = team.\"teamCode\" ";
		sqlStatement += "AND game.\"year\" = " + year + " ";
		sqlStatement += "AND game.\"year\" = team.\"year\" ";
		sqlStatement += "AND team.\"teamName\" = " + teamName + " ";
		sqlStatement += "INNER JOIN public.\"teamGameStats\" ";
		sqlStatement += "on \"teamGameStats\".\"gameCode\" = game.\"gameCode\" ";
		sqlStatement += "AND \"teamGameStats\".\"teamCode\" = game.\"visitingTeamCode\" ";
		sqlStatement += "ORDER BY ";
		sqlStatement += "game.\"date\" ";
			
		
		try {
		     //create a statement object
		       Statement stmt = conn.createStatement();
		       //create an SQL statement
		       //send statement to DBMS
		       ResultSet result = stmt.executeQuery(sqlStatement);

		       //OUTPUT
		       while (result.next()) {
			    	 info.add(result.getString("points"));
		       }
		} catch (Exception e){
	     System.out.println("Error accessing Database.");
		}
		
		return info;
	}
	
	//GET THE SCORES RETURNED IN A VECTOR OF PAIRS FOR A SELECTED TEAM
	
	public Vector<Pair<String,String>> getTeamScores(String teamName, String year, String homeOrVisiting) {
		Vector<Pair<String,String>> info = new Vector<Pair<String, String>>();
		Vector<String> home = getHomeTeamPoints(teamName,year,homeOrVisiting);
		Vector<String> visiting = getVisitingTeamPoints(teamName,year, homeOrVisiting);
		
		
		
		for(int i=0 ; i < home.size(); i++) {
			Pair<String,String> a  = new Pair<String, String>(home.get(i),visiting.get(i));
			info.add(a);
		}
		
		return info;
	}
	
	
	//GET THE ALL THE SCORES FOR ALL THE TEAMS RETURNED IN A VECTOR OF VECTOR OF PAIRS, THE FIRST NUMBER BEING THE POINTS BY THE SELECTED TEAM
	
	public Vector<Vector<Pair<String,String>>> getAllTeamScores(String confName, String year, String homeOrVisiting){
		Vector<String> names = conferenceTeams(confName,year);
		Vector<Vector<Pair<String,String>>> info = new Vector<Vector<Pair<String, String>>>();
		for(int i = 0; i < names.size(); i++) {
			Vector<Pair<String,String>> a = getTeamScores("\'"+names.get(i)+"\'",year,homeOrVisiting);
			info.add(a);
		}
		
		
		return info;
		
	}
	 
	
	//ALGORITHM FOR TEAM WHEN IS AT HOME
	
	
	// CHART TO SEE HOW GOOD A WIN IS:
	// 1-3 POINTS = Extremely Close game	(.6)	
	// 4-7 POINTS = Very Close game		(.7)	
	// 8-14 POINTS = Close game		(.8)
	// 15-21 POINTS = Not Close game	(.90)
	// 21+ POINTS = Blow-out	(1)
	
	public Vector<Double> algorithmAtHome(String confName, String year){
		Vector<Double> info = new Vector<Double>();
		Vector<Vector<Pair<String,String>>> games = getAllTeamScores(confName,year,"\""+"homeTeamCode"+"\"");
		Vector<Pair<String,String>> temp = new Vector<Pair<String,String>>();
		
		for(int i = 0; i < games.size(); i++) {
			double won = 0;
			double result = 0;
			int diff = 0;
			temp = games.get(i);
			for(int j = 0; j < temp.size(); j++) {
				int num1 = Integer.parseInt(temp.get(j).getElement0());
				int num2 = Integer.parseInt(temp.get(j).getElement1());
				if(num1 > num2) {
					diff = num1 - num2;
					if(diff <= 3) {
						won = won + .75;
					}
					else if(diff <= 7  ) {
						won = won + .8;
					}
					else if(diff <= 14  ) {
						won = won + .85;
					}
					else if(diff <= 21  ) {
						won = won + .9;
					}
					else if(diff > 21  ) {
						won++;
					}
				}

					
			}
			result = (won/temp.size())*100;
			info.add(result);
		}
		
		return info;
	}
	
	
	//ALGORITHM FOR TEAM WHEN VISITING
	
	public Vector<Double> algorithmVisiting(String confName, String year){
		Vector<Double> info = new Vector<Double>();
		Vector<Vector<Pair<String,String>>> games = getAllTeamScores(confName,year,"\""+"visitingTeamCode"+"\"");
		Vector<Pair<String,String>> temp = new Vector<Pair<String,String>>();
		
		for(int i = 0; i < games.size(); i++) {
			double won = 0;
			double result = 0;
			int diff = 0;
			temp = games.get(i);
			for(int j = 0; j < temp.size(); j++) {
				int num1 = Integer.parseInt(temp.get(j).getElement0());
				int num2 = Integer.parseInt(temp.get(j).getElement1());
				if(num2 > num1) {
					diff = num1 - num2;
					if(diff <= 3) {
						won = won + .75;
					}
					else if(diff <= 7  ) {
						won = won + .8;
					}
					else if(diff <= 14  ) {
						won = won + .85;
					}
					else if(diff <= 21  ) {
						won = won + .9;
					}
					else if(diff > 21  ) {
						won++;
					}
				}

					
			}
			result = (won/temp.size())*100;
			info.add(result);
		}
		return info;
	}
	
	
	//LIST OF ALL TEAMS WITH THEIR RESPECTIVE HOMETEAM ADVANTAGE
	public Pair<String, String>[] result(String confName, String year){
		Vector<Pair<String,String>> info = new Vector<Pair<String,String>>();
		Vector<String> teams = new Vector<String>();
		Vector<Double> homePercentage = new Vector<Double>();
		Vector<Double> visitingPercentage = new Vector<Double>();
	
		
		teams = conferenceTeams(confName, year);
		homePercentage = algorithmAtHome(confName, year);
		visitingPercentage = algorithmVisiting(confName,year);
		
		for(int i = 0; i < teams.size(); i++) {
			String team = teams.elementAt(i);
			double rank = (homePercentage.get(i)*.60) + (visitingPercentage.elementAt(i)*.40);
			String x = value.format(rank);
			Pair<String,String> a = new Pair<String,String>(team,x);
			info.add(a);
		}
		
		
		Pair<String,String>[] arr;
		arr = new Pair[info.size()];
		for(int i = 0; i < info.size(); i++) {
			arr[i] = info.elementAt(i);
		}
		
		for(int i = 0; i < arr.length; i++) {
			for(int j = arr.length-1; j > i; j--) {
				//System.out.println(Double.valueOf(info.elementAt(j).getElement1()));
				if(Double.valueOf(arr[j].getElement1()) > Double.valueOf(arr[j-1].getElement1())){
					Pair<String,String> temp = arr[j-1];
					arr[j-1] = arr[j];
					arr[j] = temp;
				}
			}
		}
			
		return arr;
	}
}