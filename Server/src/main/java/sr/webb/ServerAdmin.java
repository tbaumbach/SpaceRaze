/*
 * Created on 2005-jan-14
 */
package sr.webb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import sr.general.logging.Logger;
import sr.world.Player;

/**
 * @author WMPABOD
 *
 * This class controls the server from a web page.
 */
public class ServerAdmin {
	
	/**
	 * Används inte... denna klass kan tas bort, ServerHandler används istället...
	 * @param nrTurns
	 * @param port
	 */
	public static void updateServer(int nrTurns, int port){
//		int port = 6793;
		Logger.finer("");
		Logger.finer("serveradmin - starting update sequence: " + nrTurns + " turn(s)");
		// Koppla upp till servern
		try {
			Logger.finer("Host: " + InetAddress.getLocalHost());
			Socket s = new Socket(InetAddress.getLocalHost(), port);
			Logger.finer(s.toString());
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			Logger.finer("Connected to " + s.getInetAddress().getHostName() + ":" + s.getPort());
			int turn = ( (Integer) ois.readObject()).intValue();
			Logger.finer("Turn data recieved successfully, turn is: " + turn);
			// send update message to server
			oos.writeObject("update " + nrTurns);
			Player p = (Player)ois.readObject();
			Logger.finer("Player recieved, errmsg: " + p.getErrorMessage());
			oos.writeObject(new Player("Update finished. Bye."));
			String msg = (String)ois.readObject();
			Logger.finer("Server sais: " + msg);
			Logger.finer("Update finished");
		}
		catch (IOException e) {
			Logger.finer("Error while connecting to Server, CAUSE OF ERROR: " + e.toString());
			Logger.finer("IOException: " + e.toString());
		}
		catch (ClassNotFoundException cnfe) {
			Logger.finer(cnfe.toString());
		}
	}

}
