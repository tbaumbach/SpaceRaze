/*
 * Created on 2005-maj-16
 */
package sr.server.map;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import sr.general.StyleGuide;
import sr.general.logging.Logger;
import sr.server.properties.PropertiesHandler;
import sr.world.Map;
import sr.world.Planet;
import sr.world.PlanetConnection;

/**
 * @author WMPABOD
 *
 * This class can generate GIF images for Map objects
 */
public class MapImageCreator {
	private String completePath;
	private int densityLimit = 10;
	
	public Dimension createGifAndGetSize(String imageName, Map aMap){
		// get properties
		String basePath = PropertiesHandler.getProperty("basepath");
		Logger.finer("basePath: " + basePath);
		completePath = basePath + "webb2\\images\\maps\\";
		// create image
		Dimension d = createImageFile(imageName,aMap.getCopyFromFile());
		// return size
		return d;
	}

	private Dimension createImageFile(String imageName, Map aMap){
		Dimension d = null;
		// check if image exists already
		boolean imageExists = checkImage(imageName);
		// if not, create new image
//		boolean createNewGif = false;
		if (!imageExists){
			d = createImage(imageName,aMap);
			Logger.fine("Map image not found, creating new image");
		}else{
			d = getImageDimension(imageName);
			Logger.fine("Using old map image.");
		}
		return d;
	}

	private Dimension getImageDimension(String imageName){
		Dimension d = null;

		Frame f = new Frame("GIFTest");
		f.setBounds(-1000,-1000,200,20);
		f.setVisible(true);
		
		String filePath = completePath + "/" + imageName + ".gif";

		Image image = f.getToolkit().createImage(filePath);
		MediaTracker tracker = new MediaTracker(f);
		tracker.addImage(image,0);
		try {
		    tracker.waitForAll();
		} catch (InterruptedException e) {}
		
		d = new Dimension(image.getWidth(f),image.getHeight(f));

		// remove frame window
		f.setVisible(false);
		f.dispose();

		Logger.fine("Automatically determining map image dimension for image=" + filePath);

		return d;
	}

	private boolean checkImage(String imageName){
		String filePath = completePath + imageName + ".gif";
		File f2 = new File(filePath);
		Logger.fine("Checking file: " + filePath + ". File exists = " + f2.exists());
		return f2.exists();
	}

	public void setLimit(int customLimit){
		densityLimit = customLimit;
	}
	
	private Dimension createImage(String imageName, Map aMap){
		Dimension d = null;
//		System.out.println("Creating new image with text: " + text);
		// need a component in order to use MediaTracker
		Frame f = new Frame("GIFTest");
		f.setBounds(-1000,-1000,200,20);
		f.setVisible(true);
		
		// determine width and height???
		int largestX = computeLargestX(aMap);
		int largestY = computeLargestY(aMap);
		int largestZ = computeLargestZ(aMap);
		int smallestX = computeSmallestX(aMap);
		int smallestY = computeSmallestY(aMap);
		int smallestZ = computeSmallestZ(aMap);
		Logger.finer(largestX + " " + largestY + " " + largestZ);
		Logger.finer(smallestX + " " + smallestY + " " + smallestZ);
		int mapWidth = largestX - smallestX;
		int mapHeight = largestY - smallestY;
		int mapDepth = largestZ - smallestZ;
		double mapRatio = -1;
		if ((mapWidth == 0) | (mapHeight == 0)){
			mapRatio = 1;
		}else{
			mapRatio = (mapWidth*1.0) / mapHeight;
		}
		
		int width = 800; // always use width 800
		int height = (int)Math.round(800/mapRatio);
		Logger.finer("Map height: " + height);
		double internalWidth = 700.0;
		
		// if needed, rescale width and height
		double density = -1;
		if (aMap.getNrPlanets() == 0){
			density = 800; // skall ej skalas om
		}else{
			density = (height*1.0)/aMap.getNrPlanets();
		}
		Logger.finer("Map density: " + density);
		int limit = densityLimit;
		if (density < limit){
			Logger.finer("Density rescaling: " + 15.0/density);
			double tmpMod = limit/density;
			if (tmpMod > 2){
				Logger.finer("tmpMod > 2, set to 2! Width=" + width);
				tmpMod = 2.0d;
			}
			width = (int)Math.round(width * tmpMod);
			height = (int)Math.round(height * tmpMod);
			internalWidth = internalWidth * tmpMod;
			Logger.finer("Rescaling finished");
			Logger.finer("After Rescaling finished, width = " + width);
			Logger.finer("After Rescaling finished, height = " + height);
		}
	
		// compute scale modifier
		double scaleMod = internalWidth/mapWidth;
		
		// create temp image to be able to create a FontMetrics object
//		BufferedImage tmpbi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
//		Graphics2D tmpg = tmpbi.createGraphics();
		
		// set dimension
		d = new Dimension(width,height);
		
		// creates a buffer image
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		// draw map on g
		// -------------
		
		// transform coors
		movePlanets(aMap,smallestX,smallestY,smallestZ);
		scalePlanets(aMap,scaleMod);
		movePlanets(aMap,-5,-30,0);
		
		// draw all connections
		List<PlanetConnection> allConnections = aMap.getConnections();
		// long range
		for (PlanetConnection aConnection : allConnections) {
			if (aConnection.isLongRange()){
				Planet tmpPlanet1 = aConnection.getPlanet1();
				Planet tmpPlanet2 = aConnection.getPlanet2();
				int tmpX1 = (int)Math.round(tmpPlanet1.getXcoor());
				int tmpY1 = (int)Math.round(tmpPlanet1.getYcoor());
				int tmpX2 = (int)Math.round(tmpPlanet2.getXcoor());
				int tmpY2 = (int)Math.round(tmpPlanet2.getYcoor());
				Color tmpColor = StyleGuide.colorMapLongRange;
				g.setColor(tmpColor);
				g.drawLine(tmpX1,tmpY1,tmpX2,tmpY2);
			}
		}
		// short range
		for (PlanetConnection aConnection : allConnections) {
			if (!aConnection.isLongRange()){
				Planet tmpPlanet1 = aConnection.getPlanet1();
				Planet tmpPlanet2 = aConnection.getPlanet2();
				int tmpX1 = (int)Math.round(tmpPlanet1.getXcoor());
				int tmpY1 = (int)Math.round(tmpPlanet1.getYcoor());
				int tmpX2 = (int)Math.round(tmpPlanet2.getXcoor());
				int tmpY2 = (int)Math.round(tmpPlanet2.getYcoor());
				Color tmpColor = StyleGuide.colorMapShortRange;
				g.setColor(tmpColor);
				g.drawLine(tmpX1,tmpY1,tmpX2,tmpY2);
			}
		}

		// draw all planets
		List<Planet> allPlanets = aMap.getPlanets();
		for (Planet aPlanet : allPlanets) {
			int tmpX = (int)Math.round(aPlanet.getXcoor());
			int tmpY = (int)Math.round(aPlanet.getYcoor());
			int tmpZ = (int)Math.round(aPlanet.getZcoor());
			// draw planet
			int size = 9; 
			size = (int)Math.round((size * (tmpZ*1.0/mapDepth))) + 3;
	        g.setColor(Color.WHITE);  // color not faded
	        g.fillOval(tmpX-(size/2), tmpY-(size/2), size, size);
			// draw planet name
	        g.setColor(StyleGuide.colorMapPlanetNames);  // ljusblå text
	        g.setFont(new Font("Helvetica",0,12));
	        g.drawString(aPlanet.getName(),(tmpX+(size/2)+2),(tmpY));
		}

		// write info text on map
		g.setColor(StyleGuide.colorMapInsets);
        g.setFont(new Font("Helvetica",0,12));
		g.drawString("Map: " + imageName,5,15);
		
		// encode the image as a GIF
		encodeGif(bi,imageName);
		
		// remove frame window
		f.setVisible(false);
		f.dispose();
		
		// message
		Logger.info("New map image created. Imagename=" + imageName);

		return d;
	}

	/**
	 * Creates a map image to be shown when creating a new game in the notifier
	 * @param aMap
	 * @return
	 */
	public Image createImage(Map aMap){
//		Dimension d = null;
//		System.out.println("Creating new image with text: " + text);
		// need a component in order to use MediaTracker
//		Frame f = new Frame("GIFTest");
//		f.setBounds(-1000,-1000,200,20);
//		f.setVisible(true);
//		Logger.fine("aMap: " + aMap.getNameFull());
		// determine width and height???
		int largestX = computeLargestX(aMap);
		int largestY = computeLargestY(aMap);
		int largestZ = computeLargestZ(aMap);
		int smallestX = computeSmallestX(aMap);
		int smallestY = computeSmallestY(aMap);
		int smallestZ = computeSmallestZ(aMap);
		Logger.finer(largestX + " " + largestY + " " + largestZ);
		Logger.finer(smallestX + " " + smallestY + " " + smallestZ);
		int mapWidth = largestX - smallestX;
		int mapHeight = largestY - smallestY;
//		int mapDepth = largestZ - smallestZ;
		double mapRatio = -1;
		if ((mapWidth == 0) | (mapHeight == 0)){
			mapRatio = 1;
		}else{
			mapRatio = (mapWidth*1.0) / mapHeight;
		}
		
		int MAX_WIDTH = 500;
		int width = -1;
		int height = -1;
		if (mapRatio > 1){
			width = MAX_WIDTH; // always use width 500
			height = (int)Math.round(MAX_WIDTH/mapRatio);
		}else{ // mapRatio < 1
			width = (int)Math.round(MAX_WIDTH*mapRatio); // always use width 500
			height = MAX_WIDTH;
		}
//		Logger.finer("Map height: " + height);
//		double internalWidth = Math.round(width*0.9);
		double internalWidth = Math.round(width*0.92);
		
		// if needed, rescale width and height
//		double density = -1;
//		if (aMap.getNrPlanets() == 0){
//			density = 500; // skall ej skalas om
//		}else{
//			density = (height*1.0)/aMap.getNrPlanets();
//		}
//		Logger.finer("Map density: " + density);
//		int limit = densityLimit;
//		if (density < limit){
//			Logger.finer("Density rescaling: " + 15.0/density);
//			double tmpMod = limit/density;
//			if (tmpMod > 2){
//				Logger.finer("tmpMod > 2, set to 2! Width=" + width);
//				tmpMod = 2.0d;
//			}
//			width = (int)Math.round(width * tmpMod);
//			height = (int)Math.round(height * tmpMod);
//			internalWidth = internalWidth * tmpMod;
//			Logger.finer("Rescaling finished");
//			Logger.finer("After Rescaling finished, width = " + width);
//			Logger.finer("After Rescaling finished, height = " + height);
//		}
	
		// compute scale modifier
		double scaleMod = internalWidth/mapWidth;
		
		// create temp image to be able to create a FontMetrics object
//		BufferedImage tmpbi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
//		Graphics2D tmpg = tmpbi.createGraphics();
		
		// set dimension
//		d = new Dimension(width,height);
		
		// creates a buffer image
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		// draw map on g
		// -------------
		
		// transform coors
		movePlanets(aMap,smallestX,smallestY,smallestZ);
		scalePlanets(aMap,scaleMod);
		movePlanets(aMap,-20,-20,0);
		
		// draw all connections
		List<PlanetConnection> allConnections = aMap.getConnections();
		// long range
		for (PlanetConnection aConnection : allConnections) {
			if (aConnection.isLongRange()){
				Planet tmpPlanet1 = aConnection.getPlanet1();
				Planet tmpPlanet2 = aConnection.getPlanet2();
				int tmpX1 = (int)Math.round(tmpPlanet1.getXcoor());
				int tmpY1 = (int)Math.round(tmpPlanet1.getYcoor());
				int tmpX2 = (int)Math.round(tmpPlanet2.getXcoor());
				int tmpY2 = (int)Math.round(tmpPlanet2.getYcoor());
				Color tmpColor = StyleGuide.colorMapLongRange;
				g.setColor(tmpColor);
				g.drawLine(tmpX1,tmpY1,tmpX2,tmpY2);
			}
		}
		// short range
		for (PlanetConnection aConnection : allConnections) {
			if (!aConnection.isLongRange()){
				Planet tmpPlanet1 = aConnection.getPlanet1();
				Planet tmpPlanet2 = aConnection.getPlanet2();
				int tmpX1 = (int)Math.round(tmpPlanet1.getXcoor());
				int tmpY1 = (int)Math.round(tmpPlanet1.getYcoor());
				int tmpX2 = (int)Math.round(tmpPlanet2.getXcoor());
				int tmpY2 = (int)Math.round(tmpPlanet2.getYcoor());
				Color tmpColor = StyleGuide.colorMapShortRange;
				g.setColor(tmpColor);
				g.drawLine(tmpX1,tmpY1,tmpX2,tmpY2);
			}
		}

		// draw all planets
		List<Planet> allPlanets = aMap.getPlanets();
		// compute planet size on map
		int size = (int)Math.round(7 - Math.sqrt(allPlanets.size())/4.0);
//		Logger.fine("size (" + aMap.getNameFull() + "): " + size);
		for (Planet aPlanet : allPlanets) {
			int tmpX = (int)Math.round(aPlanet.getXcoor());
			int tmpY = (int)Math.round(aPlanet.getYcoor());
//			int tmpZ = (int)Math.round(aPlanet.getZcoor());
			// draw planet
//			int size = 9; 
//			size = (int)Math.round((size * (tmpZ*1.0/mapDepth))) + 3;
	        g.setColor(Color.WHITE);  // color not faded
	        g.fillOval(tmpX-(size/2), tmpY-(size/2), size, size);
			// draw planet name
//	        g.setColor(StyleGuide.colorMapPlanetNames);  // ljusblå text
//	        g.setFont(new Font("Helvetica",0,12));
//	        g.drawString(aPlanet.getName(),(tmpX+(size/2)+2),(tmpY));
		}

		// write info text on map
//		g.setColor(StyleGuide.colorMapInsets);
//        g.setFont(new Font("Helvetica",0,12));
//		g.drawString("Map: " + aMap.getNameFull(),5,15);
		
		// encode the image as a GIF
//		encodeGif(bi,imageName);
		
		// remove frame window
//		f.setVisible(false);
//		f.dispose();
		
		// message
//		Logger.info("New map image created. Imagename=" + imageName);

		return bi;
	}
	
	/**
	 * Moves all planets so that upper and leftmost planets are 0 in x and y
	 */
	private void movePlanets(Map aMap, int xOffset, int yOffset, int zOffset){
		List<Planet> allPlanets = aMap.getPlanets();
		for (Planet aPlanet : allPlanets) {
			aPlanet.setX(aPlanet.getXcoor() - xOffset);
			aPlanet.setY(aPlanet.getYcoor() - yOffset);
			aPlanet.setZ(aPlanet.getZcoor() - zOffset);
		}
	}

	private void scalePlanets(Map aMap, double scaleMod){
		List<Planet> allPlanets = aMap.getPlanets();
		for (Planet aPlanet : allPlanets) {
			aPlanet.changeScale(scaleMod);
		}
	}

	private int computeSmallestX(Map aMap){
		int smallest = Integer.MAX_VALUE;
		List<Planet> allPlanets = aMap.getPlanets();
		for (Planet aPlanet : allPlanets) {
			int tmpX = (int)Math.round(aPlanet.getXcoor());
			if (tmpX < smallest){
				smallest = tmpX;
			}
		}
		return smallest;
	}

	private int computeSmallestY(Map aMap){
		int smallest = Integer.MAX_VALUE;
		List<Planet> allPlanets = aMap.getPlanets();
		for (Planet aPlanet : allPlanets) {
			int tmpY = (int)Math.round(aPlanet.getYcoor());
			if (tmpY < smallest){
				smallest = tmpY;
			}
		}
		return smallest;
	}

	private int computeSmallestZ(Map aMap){
		int smallest = Integer.MAX_VALUE;
		List<Planet> allPlanets = aMap.getPlanets();
		for (Planet aPlanet : allPlanets) {
			int tmpZ = (int)Math.round(aPlanet.getZcoor());
			if (tmpZ < smallest){
				smallest = tmpZ;
			}
		}
		return smallest;
	}

	private int computeLargestX(Map aMap){
		int largest = Integer.MIN_VALUE;
		List<Planet> allPlanets = aMap.getPlanets();
		for (Planet aPlanet : allPlanets) {
			int tmpX = (int)Math.round(aPlanet.getXcoor());
			if (tmpX > largest){
				largest = tmpX;
			}
		}
		return largest;
	}

	private int computeLargestY(Map aMap){
		int largest = Integer.MIN_VALUE;
		List<Planet> allPlanets = aMap.getPlanets();
		for (Planet aPlanet : allPlanets) {
			int tmpY = (int)Math.round(aPlanet.getYcoor());
			if (tmpY > largest){
				largest = tmpY;
			}
		}
		return largest;
	}

	private int computeLargestZ(Map aMap){
		int largest = Integer.MIN_VALUE;
		List<Planet> allPlanets = aMap.getPlanets();
		for (Planet aPlanet : allPlanets) {
			int tmpZ = (int)Math.round(aPlanet.getZcoor());
			if (tmpZ > largest){
				largest = tmpZ;
			}
		}
		return largest;
	}

	private void encodeGif(BufferedImage bi, String imageName){
		GIFEncoder encode;
		try {
			encode = new GIFEncoder(bi);
			String filePath = completePath + imageName + ".gif";
			Logger.finest("filepath: " + filePath);
			OutputStream output = new BufferedOutputStream(new FileOutputStream(filePath));
			encode.Write(output);
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Map aMap = MapHandler.getMap("solen15");
		MapImageCreator mic = new MapImageCreator();
		mic.setLimit(15);
//		mic.createGifAndGetSize("solen15","C:\\Program Files\\Tomcat 4.1\\webapps\\SpaceRaze\\",aMap);
		mic.createGifAndGetSize("solen15",aMap);
	}

}
