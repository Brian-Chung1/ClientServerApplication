package BasicClientServer.BackEnd;

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
	public static void processCommand(String cmd, NetworkAccess na, ClientHandler ch)
	{
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
