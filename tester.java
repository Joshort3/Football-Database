import java.sql.*;
public class tester {

	public static void main(String[] args) {
	     System.out.println("Opened database successfully");
	     String playerInfo = "";
	     SQLCommands x = new SQLCommands();
	     playerInfo = x.playerInfo("\'Alex\'", "\'Allen\'", "2008",true);
	     System.out.println(playerInfo);
	     String playerTeamInfo = x.playerTeamInfo("\'Alex\'", "\'Allen\'","2008",true);
	     System.out.println(playerTeamInfo);
	     String PGSInfo = x.playerStats("\'Alex\'", "\'Allen\'", "2008",true);
	     System.out.println(PGSInfo);
	     String twoPlayer = x.compareTwoPlayers("\'Alex\'", "\'Allen\'", "2008", "\'Brandon\'", "\'Anderson\'",true);
	     System.out.println(twoPlayer);
	     String teamPlayers = x.teamPlayer("\'Texas A&M\'", "2008",true);
	     System.out.println(teamPlayers);
	     String teamCon = x.teamConference("\'Texas A&M\'", "2008",true);
	     System.out.println(teamCon);
	     String teamStat = x.teamStats("\'Texas A&M\'", "2008",true);
	     System.out.println(teamStat);
	     String conferenceNames = x.allConferences("2009",true);
	     //System.out.println(conferenceNames);
	     String names = x.conferenceTeams("\'Big 12 Conference\'", "2008", true);
		//System.out.println(names);
	     String stadiumsInfo = x.allStadiumsInfo("2008",true);
		//System.out.println(stadiumsInfo);
		String stadium = x.stadiumInfo("\'Legion Field\'", "2005", true);
		//System.out.println(stadium);
		String gamesPlayed = x.gamesPlayedAtStadium("\'Kyle Field\'", "2008", true);
		//System.out.println(gamesPlayed);
			 
		String gameInfo = x.gameInfo("\'Alabama\'", "\'Texas A&M\'", "2012", true);
		// System.out.println(gameInfo);
			 
		String homeTeam = x.homeTeamStats("\'Alabama\'", "\'Texas A&M\'", "2012", true);
		//System.out.println(homeTeam);
			 
		String visitingTeam = x.visitingTeamStats("\'Alabama\'", "\'Texas A&M\'","2012", true); 
		//System.out.println(visitingTeam);
	     csvCreator y = new csvCreator();
	     //y.CSV(teamStat, "test.csv");
	     //y.CSV(teamCon, "test.csv");
	     //y.CSV(teamPlayers, "test.csv");
	     //y.CSV(twoPlayer, "test.csv");
	     //y.CSV(PGSInfo, "test.csv");
	    //y.CSV(playerTeamInfo, "test.csv");
	    //y.CSV(playerInfo, "test.csv");
	    // y.CSV(visitingTeam, "test.csv");
	     
	}
	

}
