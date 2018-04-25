package sr.client;

import java.awt.Image;
import java.awt.MediaTracker;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sr.general.logging.Logger;

/**
 * Denna klass hanterar alla bilder som appleten använder. Alla bilder hämtas
 * direkt med hjälp av en MediaTracker och bilderna hämtas och identifieras 
 * med hjälp av textsträngar.
 * @author wmpabod
 */
public class ImageHandler {
  private List<Image> images;
  private List<String> imageNames;
  private MediaTracker tracker;
  private SpaceRazeApplet clientApplet;

  public ImageHandler(SpaceRazeApplet aClientApplet) {
	Logger.info("Time:" + new Date().toString());
    this.clientApplet = aClientApplet;
    images = new ArrayList<Image>();
    imageNames = new ArrayList<String>();
    tracker = new MediaTracker(clientApplet);
    // add all images to be loaded
    addImage("planetnormal","planetnormal.gif");
    addImage("planetrazed","planetrazed.gif");
    addImage("newMail","mail00.jpg");
    // force the loading of all images
    try{
    	Logger.info("Waiting for images...");
    	tracker.waitForAll();
    }catch(InterruptedException ie){}
	Logger.info("Finished. time:" + new Date().toString());
  }

  private void addImage(String imageName, String imageFileName){
    imageNames.add(imageName);
    Logger.info("clientApplet.getDocumentBase(): " + clientApplet.getDocumentBase() + " images/" + imageFileName);
    Image tempImage = null;
    if (clientApplet.isRunAsApplication()){
    	tempImage = clientApplet.getImage(clientApplet.getDocumentBase(),imageFileName);
    }else{
    	tempImage = clientApplet.getImage(clientApplet.getDocumentBase(),"images/" + imageFileName);
    }
    tracker.addImage(tempImage,1);
    images.add(tempImage);
	Logger.info("addImage Finished:" + imageFileName);
  }

  public Image getImage(String imageName){
    Image returnImage = null;
    int index = imageNames.indexOf(imageName);
    if (index > -1){
      returnImage = images.get(index);
    }
    return returnImage;
  }
}