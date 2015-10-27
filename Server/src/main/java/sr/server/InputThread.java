package sr.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputThread extends Thread{
    DataInputStream dis;
    SR_Server s;

    public InputThread(InputStream is, SR_Server s){
        dis = new DataInputStream(is);
        this.s = s;
    }

    @SuppressWarnings("deprecation")
	public void run(){
        String temp = null;
        int nr;
        for(;;){
            try{
                temp = dis.readLine();
                nr = Integer.parseInt(temp);
                for (int x = 0; x < nr; x++){
                    s.updateGalaxy(true);
                }
            }
            catch(IOException ioe){
                System.out.println("IOException: " + ioe.toString());
            }
            catch(NumberFormatException nfe){
              if (temp.equals("?")){
                System.out.println("Syntax: create/load/update nameOfGame port time");
                System.out.println("Possible names of games: test, wigge3, wigge6, wigge9");
              }else
              if (temp.equals("q")){
              	System.exit(0);
              }else{
                System.out.println("Failed to parse input: " + temp);
              }
            } catch (Exception e) {
            	System.out.println("Exception, probably while updating");
				e.printStackTrace();
			}
        }
    }
}