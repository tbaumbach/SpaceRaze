package sr.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import spaceraze.world.Player;

public class Connection implements Runnable{
    protected Socket client;
    protected ObjectOutputStream oos;
    protected ObjectInputStream ois;
    SR_Server s;
    Thread t;
    String filename,command;
    int nrPlanets = 0;

    public Connection(Socket client_socket, SR_Server s){
        this.s = s;
        client = client_socket;
        t = new Thread(this);
        t.start();
    }
    
    @SuppressWarnings("deprecation")
	public void stopThreads(){
    	t.stop();
    	try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void run(){
        try{
            oos = new ObjectOutputStream(client.getOutputStream());
            ois = new ObjectInputStream(client.getInputStream());
            oos.writeObject(s.getTurn());
            String message = (String)ois.readObject();
            if (!message.equals("checkStatus")){
            	System.out.println("Mottar meddelande: " + message);
            }
            Player sendplayer = s.getPlayer(message);
            oos.writeObject(sendplayer);
            Player p = (Player)ois.readObject();
            String statusmsg = s.updatePlayer(p);
            oos.writeObject(statusmsg);
        }
        catch(IOException e){
            try{client.close();}catch(IOException e2){}
        }
        catch(ClassNotFoundException cnfe){
            System.out.println("Class not found...\n");
        } catch (Exception e) {
			System.out.println("Exception, probably while updating");
			e.printStackTrace();
		}
    }
}
