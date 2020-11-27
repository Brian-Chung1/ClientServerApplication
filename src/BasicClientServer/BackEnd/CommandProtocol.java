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
	private static String[] commandString;
	
	

	/**
	 * process commands sent to the server
	 * @param cmd: command to be processed
	 * @param na: NetworkAccess object for communication
	 * @param ch: ClientHandler object requesting the processing
	 * @return
	 */
	public static void processCommand(String cmd, NetworkAccess na, ClientHandler ch) throws SQLException {
		commandString = cmd.split(",");
		cmd = commandString[0];
		System.out.println("SERVER receive: " + cmd);

		if (cmd.equals("disconnect")) {
			na.close();
			ch.getServer().removeClientConnection(ch.getID());
			ch.Stop();
		}
		else if (cmd.equals("connect")) {
			na.sendString("Success", false);
		}
		else if (cmd.equals("login")) {
			String username = commandString[1];
			String password = commandString[2];

			//database checking
			DatabaseConnection db = ch.getServer().getDB();
			db.processLogin(username, password, na, ch);
		}

		else if (cmd.equals("logout")) {
			ch.getServer().removeLoggedInClient(ch);
		}
		else if (cmd.equals("changepassword")) {
			String username = commandString[1];
			String oldPassword = commandString[2];
			String newPassword = commandString[3];

			DatabaseConnection db = ch.getServer().getDB();
			db.processChangePassword(username, oldPassword, newPassword, na, ch);

		}
		else if (cmd.equals("forgotpassword")) {
			String username = commandString[1];
			DatabaseConnection db = ch.getServer().getDB();
			db.processPasswordRecovery(username, na, ch);
		}
		else if (cmd.equals("newaccount")) {
			String email = commandString[1];
			String username = commandString[2];
			String password = commandString[3];
			DatabaseConnection db = ch.getServer().getDB();
			db.processAccountCreation(email, username, password, na, ch);
		}
		else {
			
			na.sendString(cmd + "\n", false);
			
		}		
	}

}
