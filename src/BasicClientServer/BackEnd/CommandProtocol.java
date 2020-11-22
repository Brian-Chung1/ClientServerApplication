package BasicClientServer.BackEnd;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;

/**
 * @author reinhart
 *
 */
public class CommandProtocol {

	/**
	 * commands and their responses
	 */
	private static HashMap<String, String> commands;	
	static {
	    commands = new HashMap<>();
	    commands.put("disconnect", "");
	    commands.put("hello", "world!");
	}
	
	

	/**
	 * process commands sent to the server
	 * @param cmd: command to be processed
	 * @param na: NetworkAccess object for communication
	 * @param ch: ClientHandler object requesting the processing
	 * @return
	 */
	public static void processCommand(String cmd, NetworkAccess na, ClientHandler ch) throws SQLException {
		String[] commandString = cmd.split(",");
		cmd = commandString[0];
		System.out.println("SERVER receive: " + cmd);
		
		if (cmd.equals("disconnect")) {
			na.close();
			ch.getServer().removeClientConnection(ch.getID());
			ch.Stop();
		}
		else if (cmd.equals("connect")) {
			System.out.println("Command Processor has received Connect");
			na.sendString("Success", true);
			ch.getServer().peerconnection(na.getSocket());
		}
		else if (cmd.equals("login")) {
			System.out.println("System has received login");
			String username = commandString[1];
			String password = commandString[2];

			//database checking
			DatabaseConnection db=new DatabaseConnection();
			db.processLogin(username,password,na, ch);
		}

		else if (cmd.equals("logout")) {
			//dont forget to delete them from the logged client vector in databaseconnection function in server

		}
		else if (cmd.equals("changepassword")) {
			String username = commandString[1];
			String oldPassword = commandString[2];
			String newPassword = commandString[3];

		}
		else if (cmd.equals("forgotpassword")) {
			//make sure we set lockcount = 0 if forgotpassword is successful

		}
		else if (cmd.equals("newaccount")) {
			String username = commandString[1];
			String password = commandString[2];
			String email = commandString[3];
		}
		else {
			
			na.sendString(cmd + "\n", false);
			
		}		
	}
	
	/**
	 * for testing the capabilities of the HashMap
	 * Not used in the actual Client/Server system
	 * 
	 * @param args: command line arguments (unused)
	 */
	public static void main (String[] args) {
		System.out.println(commands.get("disconnect"));
		System.out.println(commands.get("hello"));
		
		Set<String>keys = commands.keySet();
		for (String key : keys) {
			System.out.println(key);
		}
		
		System.out.println(commands.get("goodbye"));		
	}
}
