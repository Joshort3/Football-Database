import java.sql.*;
public class tester {

	public static void main(String[] args) {
	     System.out.println("Opened database successfully");
	     String playerInfo = "";
	     SQLCommands x = new SQLCommands();
	     playerInfo = x.playerInfo("\'Alex\'", "\'Allen\'", "2008");
	     System.out.println(playerInfo);
	}

}
