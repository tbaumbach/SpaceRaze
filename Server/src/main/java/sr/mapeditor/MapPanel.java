/*
 * Created on 2005-jun-16
 */
package sr.mapeditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;

import sr.client.StyleGuide;
import sr.world.Map;
import sr.world.Planet;
import sr.world.PlanetConnection;

/**
 * @author WMPABOD
 *
 * Panel where the map is shown
 */
@SuppressWarnings("serial")
public class MapPanel extends JPanel implements MouseMotionListener, MouseInputListener{
	private Map theMap;
	/**
	 * 1 = *1 = 2 px for each LY between planets
	 * 2 = *2 = 4 px for each LY between planets
	 * etc..
	 */
	private int zoom = 1;
	// 0 = center in the middle of the map panel. Measured in LY
	private int xOffset,yOffset,startX,startY,xDragOffset,yDragOffset,xOldOffset,yOldOffset;
//	private final int MAX = 558; 
	private final int CENTER = 279;
	private final int MOVE_STEP = 64;
	private EditorGUIPanel guiPanel;
	private Planet chosenPlanet;

	public MapPanel(Map aMap, EditorGUIPanel guiPanel){
		theMap = aMap;
		this.guiPanel = guiPanel;
		
		setBorder(new LineBorder(StyleGuide.colorNeutral));
		
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	public void paintComponent(Graphics g){
		g.setColor(StyleGuide.colorBackground);
		g.fillRect(0,0,getSize().width,getSize().height);
		// get pixels from zoom
		int scale = getScaleFromZoom();
		// paint grid
		int minX = (xOffset * scale) + CENTER - (100 * scale);
		int maxX = (xOffset * scale) + CENTER + (100 * scale);
		int minY = (yOffset * scale) + CENTER - (100 * scale);
		int maxY = (yOffset * scale) + CENTER + (100 * scale);
//		System.out.println(minX + " " + maxX);
		g.setColor(StyleGuide.colorMapEditorGrid.darker().darker().darker().darker());
		for (int i = 0; i < 201; i++){
			g.drawLine(minX,minY + (i*scale),maxX,minY + (i*scale));
		}
		for (int i = 0; i < 201; i++){
			g.drawLine(minX + (i*scale),minY,minX + (i*scale),maxY);
		}
		g.setColor(StyleGuide.colorMapEditorGrid.darker().darker().darker());
		for (int i = 0; i < 21; i++){
			g.drawLine(minX,minY + (i*10*scale),maxX,minY + (i*10*scale));
		}
		for (int i = 0; i < 21; i++){
			g.drawLine(minX + (i*10*scale),minY,minX + (i*10*scale),maxY);
		}
		g.setColor(StyleGuide.colorMapEditorGrid.darker().darker());
		g.drawLine(minX,minY,maxX,minY);
		g.drawLine(minX,maxY,maxX,maxY);
		g.drawLine(minX,minY,minX,maxY);
		g.drawLine(maxX,minY,maxX,maxY);
		g.drawLine(minX,minY + (100*scale),maxX,minY + (100*scale));
		g.drawLine(minX + (100*scale),minY,minX + (100*scale),maxY);
		
		// draw coordinates next to grid
		
		// draw connections
		List<PlanetConnection> allConnections = theMap.getConnections();
		// long range
		for (PlanetConnection aConnection : allConnections) {
			if (aConnection.isLongRange()){
				Planet tmpPlanet1 = aConnection.getPlanet1();
				Planet tmpPlanet2 = aConnection.getPlanet2();
				int tmpX1 = (int)Math.round(tmpPlanet1.getXcoor());
				int tmpY1 = (int)Math.round(tmpPlanet1.getYcoor());
				int tmpX2 = (int)Math.round(tmpPlanet2.getXcoor());
				int tmpY2 = (int)Math.round(tmpPlanet2.getYcoor());
				tmpX1 = (xOffset * scale) + CENTER + (tmpX1 * scale);
				tmpY1 = (yOffset * scale) + CENTER + (tmpY1 * scale);
				tmpX2 = (xOffset * scale) + CENTER + (tmpX2 * scale);
				tmpY2 = (yOffset * scale) + CENTER + (tmpY2 * scale);
				g.setColor(StyleGuide.colorMapLongRange);
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
				tmpX1 = (xOffset * scale) + CENTER + (tmpX1 * scale);
				tmpY1 = (yOffset * scale) + CENTER + (tmpY1 * scale);
				tmpX2 = (xOffset * scale) + CENTER + (tmpX2 * scale);
				tmpY2 = (yOffset * scale) + CENTER + (tmpY2 * scale);
				g.setColor(StyleGuide.colorMapShortRange);
				g.drawLine(tmpX1,tmpY1,tmpX2,tmpY2);
			}
		}

		// draw all planets
		List<Planet> allPlanets = theMap.getPlanets();
		for (Planet aPlanet : allPlanets) {
			int tmpX = (int)Math.round(aPlanet.getXcoor());
			int tmpY = (int)Math.round(aPlanet.getYcoor());
			int tmpZ = (int)Math.round(aPlanet.getZcoor());
			tmpX = (xOffset * scale) + CENTER + (tmpX * scale);
			tmpY = (yOffset * scale) + CENTER + (tmpY * scale);
	        // planet is chosen?
	        if (aPlanet == chosenPlanet){
	        	drawChosenPlanet(g,tmpX,tmpY);
	        }
			// draw planet
	        // size rely on that z is between -20 & 20
			int size = (int)Math.round((tmpZ+20)*5.0/40.0) + 3;
	        g.setColor(Color.WHITE);  // color not faded
	        g.fillOval(tmpX-(size/2), tmpY-(size/2), size, size);
			// draw planet name
	        g.setColor(StyleGuide.colorMapPlanetNames);  // ljusblå text
	        g.setFont(new Font("Helvetica",0,12));
	        g.drawString(aPlanet.getName(),(tmpX+(size/2)+2),(tmpY));
		}
		
	}
	
    private void drawChosenPlanet(Graphics g,int x,int y){
    	double animation = Math.PI/2;
        int nrLines = 12;
        int lineLength = 14;
        for (int i = 0; i < nrLines; i++){ // iterate over the lines
        	double angleRad = 2*Math.PI*i/nrLines;
        	int relX = (int)Math.round(lineLength*Math.sin(angleRad));
        	int relY = (int)Math.round(lineLength*Math.cos(angleRad));
        	int colorValue = (int)Math.abs(Math.round(200*Math.sin(animation+angleRad)))+55;
        	drawFadingLine(g,x,y,x+relX,y+relY,new Color(colorValue,colorValue,0));
        }
    }

    private void drawFadingLine(Graphics g,int startX,int startY,int endX,int endY,Color aColor){
    	int nrShades = 8;
    	int curX = startX;
    	int curY = startY;
    	int diffX = startX - endX;
    	int diffY = startY - endY;
    	int oldDistX = 0;
    	int oldDistY = 0;
    	for (int j = 0; j < nrShades; j++){ // iterate over the "shades" of the line
    		int newR = aColor.getRed()*j/(nrShades-1);
    		int newG = aColor.getGreen()*j/(nrShades-1);
    		int newB = aColor.getBlue()*j/(nrShades-1);
    		g.setColor(new Color(newR,newG,newB));
    		int distX = (int)Math.round((diffX*(j+1.0)/nrShades));
    		int newX = curX + distX - oldDistX;
    		int distY = (int)Math.round((diffY*(j+1.0)/nrShades));
    		int newY = curY + distY - oldDistY;
    		g.drawLine(curX,curY,newX,newY);
    		oldDistX = distX;
    		oldDistY = distY;
    		curX = newX;
    		curY = newY;
    	}
    }
	
	/**
	 * Get the number of pixels for each LY
	 * @return
	 */
	public int getScaleFromZoom(){
		int scale = 2;
		for (int i = 1; i < zoom; i++){
			scale = scale * 2;
		}
		return scale;
	}
	
	private int getStepFromZoom(){
		return MOVE_STEP / getScaleFromZoom();
	}
	
	public void incZoom(){
		if (zoom < 5){
			zoom++;
		}
		repaint();
	}

	public void decZoom(){
		if (zoom > 1){
			zoom--;
		}
		repaint();
	}
	
	public void moveRight(){
		xOffset = xOffset + getStepFromZoom();
		repaint();
	}

	public void moveLeft(){
		xOffset = xOffset - getStepFromZoom();
		repaint();
	}

	public void moveDown(){
		yOffset = yOffset + getStepFromZoom();
		repaint();
	}

	public void moveUp(){
		yOffset = yOffset - getStepFromZoom();
		repaint();
	}
	
	public void reset(){
		xOffset = 0;
		yOffset = 0;
		zoom = 1;
		repaint();
	}
	
	public int getZoom(){
		return zoom;
	}
	
	public boolean canZoomIn(){
		return zoom < 5;
	}

	public boolean canZoomOut(){
		return zoom > 1;
	}
	
	public void setChosenPlanet(Planet newChosenPlanet){
		chosenPlanet = newChosenPlanet;
	}
	
	/** 
	 * Not used, must be present due to Interface MouseMotionListener
	 */
	public void mouseDragged(MouseEvent me) {
	    if (me.getModifiers() == 16){ // left mousebutton dragged
	    	int newX = me.getX();
	    	int newY = me.getY();
	    	xDragOffset = (newX - startX)/getScaleFromZoom();
	    	yDragOffset = (newY - startY)/getScaleFromZoom();
			xOffset = xOldOffset + xDragOffset; 
			yOffset = yOldOffset + yDragOffset; 
	    	repaint();
//	    	System.out.println(xDragOffset + " " + yDragOffset);
	    }
	}

	public void mousePressed(MouseEvent me) {
		// needed for dragging
		startX = me.getX();
		startY = me.getY();
		xOldOffset = xOffset;
		yOldOffset = yOffset;
	}

	public void mouseReleased(MouseEvent me) {
		if (xDragOffset != 0){
			xOffset = xOldOffset + xDragOffset; 
			yOffset = yOldOffset + yDragOffset; 
			xDragOffset = 0;
			yDragOffset = 0;
			repaint();
		}else{
			int x = me.getX();
			int y = me.getY();
			// compute mouse location in LY
			int scale = getScaleFromZoom();
			
			int xLY = (int)Math.round((x - CENTER) / (scale*1.0d));
			int yLY = (int)Math.round((y - CENTER) / (scale*1.0d));

			xLY = xLY - (xOffset);
			yLY = yLY - (yOffset);

			if (guiPanel.getCreateShortConnection()){
				Planet closestPlanet = theMap.findClosestPlanet(xLY,yLY);
				//  check that map already have a connection or that the chosenplanet is clicked
				if (closestPlanet == chosenPlanet){
					// show error dialog
					JOptionPane.showMessageDialog(this,chosenPlanet.getName() + " can not connect to itself");
				}else
				if (theMap.findConnection(closestPlanet,chosenPlanet) != null){
					// show error dialog
					JOptionPane.showMessageDialog(this,chosenPlanet.getName() + " already have a connection to " + closestPlanet.getName());
				}else{
					theMap.addNewConnection(chosenPlanet,closestPlanet,false);
					guiPanel.addShortConnection(chosenPlanet);
					repaint();
				}
			}else
			if (guiPanel.getCreateLongConnection()){
				Planet closestPlanet = theMap.findClosestPlanet(xLY,yLY);
				//  check that map already have a connection or that the chosenplanet is clicked
				if (closestPlanet == chosenPlanet){
					// show error dialog
					JOptionPane.showMessageDialog(this,chosenPlanet.getName() + " can not connect to itself");
				}else
				if (theMap.findConnection(closestPlanet,chosenPlanet) != null){
					// show error dialog
					JOptionPane.showMessageDialog(this,chosenPlanet.getName() + " already have a connection to " + closestPlanet.getName());
				}else{
					theMap.addNewConnection(chosenPlanet,closestPlanet,true);
					guiPanel.addLongConnection(chosenPlanet);
					repaint();
				}
			}else
			if (guiPanel.getCreateNewPlanet()){
				guiPanel.createNewPlanet(xLY,yLY);
			}else
			if (guiPanel.getMovePlanet()){
				guiPanel.movePlanet(xLY,yLY);
			}else{ // select planet
				if (theMap.getPlanets().size() > 0){
					Planet closestPlanet = theMap.findClosestPlanet(xLY,yLY);
					guiPanel.selectPlanet(closestPlanet);
					chosenPlanet = closestPlanet;
					repaint();
				}
			}
		}
	}

	public void mouseMoved(MouseEvent me) {
		/*		int x = me.getX();
				int y = me.getY();
				// compute mouse location in LY
				int scale = getScaleFromZoom();
				
				int xLY = (int)Math.round((x - CENTER) / (scale*1.0d));
				int yLY = (int)Math.round((y - CENTER) / (scale*1.0d));

				xLY = xLY - (xOffset);
				yLY = yLY - (yOffset);
		*/
//				System.out.println(x + " " + y + " <> " + xLY + " " + yLY);
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
}
