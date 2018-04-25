package sr.client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import sr.client.components.SRBasePanel;
import sr.world.Message;

/**
 * <p>Description: Panel in the lower edge of the game gui, showing some information and the Send/finished button and the Abandon Game checkbox</p>
 * <p>Created: 2003</p>
 * @author Paul Bodin
 */
@SuppressWarnings("serial")
public class NewMessageAnimatedImagePanel extends SRBasePanel implements Runnable, MouseListener{
	private Image newMail;
	private Thread runner = null;
	private boolean imageVisible = false;
	private SpaceRazeApplet client;
	private boolean haveNewMail = false;

  public NewMessageAnimatedImagePanel(ImageHandler imageHandler, SpaceRazeApplet client) {
	  this.setLayout(null);
	  this.client =  client;
    setBackground(StyleGuide.colorBackground);
    addMouseListener(this);
 
    newMail = imageHandler.getImage("newMail");
    
    
    if(haveNewMessages()){
    	haveNewMail= true;
    	start();
    }
    
  }
  
  private boolean haveNewMessages(){
	  List<Message> messages = client.getReceivedMessage();
	  for (Message message : messages) {
		if(!message.isRead()){
			return true;
		}
	}
	  return false;
  }
  
  
  public void showNewMailImage(boolean show){
	  if(show){
		  haveNewMail= true;
		  start();
	  }else{
		  haveNewMail= false;
		  stop();
	  }
  }
  
  public void start() {

      // user visits the page, create a new thread

      if ( runner == null ) {

          runner = new Thread(this);
          runner.start();
      }
  }

  @SuppressWarnings("deprecation")
  public void stop() {
      // user leaves the page, stop the thread
      if ( runner != null && runner.isAlive() ){
          runner.stop();
      }
      runner = null;
  }


  public void run() {

      while (runner != null) {

          repaint();

          try {

              Thread.sleep(700);

          } catch ( InterruptedException e ) {

              // do nothing
          }
      }
  }

  public void paint(Graphics g){
	    super.paint(g);
	    if(!imageVisible && haveNewMail){
	    	g.drawImage(newMail, 0, 0, client);
	    	imageVisible = true;
	    }else{
	    	imageVisible = false;
	    }
	    
	    paintChildren(g);
  }

  public void mouseClicked(MouseEvent e) {
	  if (haveNewMail){
		  client.showUnreadMessage(e.getClickCount() == 2);
	  }
  }

  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
 
}