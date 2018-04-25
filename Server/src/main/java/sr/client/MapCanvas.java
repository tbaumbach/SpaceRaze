package sr.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.border.LineBorder;

import sr.client.color.ColorConverter;
import sr.client.components.SRBasePanel;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.Coors;
import sr.world.Planet;
import sr.world.PlanetConnection;
import sr.world.PlanetInfos;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.Troop;
import sr.world.VIP;
import sr.world.comparator.spaceship.SpaceshipSizeAndBuildCostComparator;
import sr.world.comparator.troop.TroopTypeAndBuildCostComparator;
import sr.world.orders.PlanetNotesChange;

public class MapCanvas extends SRBasePanel implements Runnable, MouseListener, MouseMotionListener{
	private static final long serialVersionUID = 1L;
	List<Planet> planets;
	List<Coors> pcoors;
	List<Spaceship> spaceships;
	List<VIP> vips;
	List<VIP> allVips;
	List<PlanetConnection> connections;
	private List<Troop> troops;
    int xoffset, yoffset, roffset, oldX, oldY, initialZoom = -6;
    double zvalue = 100.0, widthoffset, heightoffset, zoffset, animation = Math.PI/2, tempWidthOffset = 0, tempHeightOffset = 0, tempzoffset = 0;
    Player player;
    Thread t;
    ShowMapPlanet aShowMapPlanet;
    String chosenCoors = "", newCenterString = "";
    Coors origo;
    boolean once = true, doChange = false, doReset = false, startChange = false, mouseDragged = false, stopChange = false, showVips = false;
    private boolean showPlanetNotes = true;
    private boolean showLastKnownProdRes = true;
//    private MapControls controls;
    private boolean drawEdge;
    private String initialSetCenter,highlightPlayer;

    public MapCanvas(Player player, ShowMapPlanet aShowMapPlanet){
    	this(player,aShowMapPlanet,false);
    }
    
   	public MapCanvas(Player player, ShowMapPlanet aShowMapPlanet, boolean drawEdge){
      this.aShowMapPlanet = aShowMapPlanet;
      this.player = player;
      this.drawEdge = drawEdge;
      newPcoors();

      //setBackground(Color.cyan);
      //setDoubleBuffered(true);
      setBorder(new LineBorder(StyleGuide.colorCurrent));
      
      /*
       controls = new MapControls(player,aMapPanel,this);
      controls.setBounds(1,65,120,370);
      add(controls);*/

      this.addMouseListener(this);
      this.addMouseMotionListener(this);
    }
   	
   	public void setInitialZoom(int initialZoom){
   		this.initialZoom = initialZoom;
   	}

    public void newPcoors(){
      pcoors = new LinkedList<Coors>();
      Coors c = new Coors(0, 0, 0);
      origo = c;
      c.setName("");
      c.setOrigo(true);
      c.setColor(new Color(0,127,0)); // mörkgrön
      insertCoorSorted(c, pcoors);
    }

    public void computeNewOrigo(){
      int max_x = Integer.MIN_VALUE;
      int max_y = Integer.MIN_VALUE;
      int min_x = Integer.MAX_VALUE;
      int min_y = Integer.MAX_VALUE;
      Coors origoCoor = null;
      for (int i = 0; i < pcoors.size(); i++){
        Coors tempCoor = (Coors)pcoors.get(i);
        if (origoCoor == null){
          if (tempCoor.isOrigo()){
            origoCoor = tempCoor;
          }
        }else
        if (!tempCoor.isConnection()){  // is a planet
          int planet_x = (int)Math.round(tempCoor.getX());
          int planet_y = (int)Math.round(tempCoor.getY());
          if (planet_x > max_x){
            max_x = planet_x;
          }
          if (planet_y > max_y){
            max_y = planet_y;
          }
          if (planet_x < min_x){
            min_x = planet_x;
          }
          if (planet_y < min_y){
            min_y = planet_y;
          }
        }
      }
      origoCoor.setX((max_x + min_x)/2.0);
      origoCoor.setY((max_y + min_y)/2.0);
    }

    public void run(){
      for(;;){
        try{
          Thread.sleep(50);
          //t.sleep(100);
        }catch(InterruptedException ie){
        }
        updateChosenPlanetAnimation();
        if (once){
          //doSetCenter(newCenterString);
          //doChange(0,0,initialZoom,0,0,0);
          once = false;
        }
        if (doReset){
          reset();
        }
        if (!newCenterString.equalsIgnoreCase("")){
          setCenter(newCenterString);
        }
        if (doChange){
          computeNewCoors();
          doChange = false;
          stopchange();
        }
        if (startChange){
          computeNewCoors();
        }else
        if (stopChange){
          stopChange = false;
          stopchange();
        }
        if (mouseDragged){
          //widthoffset = tempWidthOffset;
          //heightoffset = tempHeightOffset;
          //zoffset = tempzoffset;
          tempWidthOffset = 0;
          tempHeightOffset = 0;
          tempzoffset = 0;
          computeNewCoors();
          widthoffset = 0;
          heightoffset = 0;
          zoffset = 0;
          mouseDragged = false;
        }
//System.out.println("Values: " + xoffset + yoffset + roffset + oldX + oldY + widthoffset + heightoffset + " z: " + zoffset);
        //paint(getGraphics());
        repaint(0,0,getSize().width,getSize().height);
        //aMapPanel.update(aMapPanel.getGraphics());
      }
    }

  public void mouseClicked(MouseEvent me){
  }

  public void mouseExited(MouseEvent me){
  }

  public void mouseEntered(MouseEvent me){
  }

  public void mousePressed(MouseEvent me){
//System.out.println("mousePressed");
    //stopChange = false;
    //startChange = true;
    oldX = me.getX();
    oldY = me.getY();
//System.out.println("me.getModifiers(): " + me.getModifiers());
//System.out.println("me.getY(): " + me.getY());
    Coors foundCoors = searchCoors(me.getX(),me.getY());
//System.out.println("foundCoors: " + foundCoors);
    if (foundCoors != null){
      animation = Math.PI/2;
      chosenCoors = foundCoors.getName();
      if (me.getModifiers() == 16){ // if left mousebutton pressed
//System.out.println("showPlanet: " + me.getModifiers());
        aShowMapPlanet.showPlanet(foundCoors.getName());
      }else
      if (me.getModifiers() == 4){ // if right mousebutton pressed
//System.out.println("showPlanetShips: " + me.getModifiers());
    	if (!player.isDefeated() & !player.getGalaxy().isGameOver()){  
    		aShowMapPlanet.showPlanetShips(foundCoors.getName());
    	}
      }
      //paint(getGraphics());
    }
  }

  public void mouseDragged(MouseEvent me){
//System.out.println("mouseDragged");
    /*if (t == null){
      t = new Thread(this);
      t.start();
    }else{
      t.stop();
      t = new Thread(this);
      t.start();
    }*/

    if (me.getModifiers() == 16){ // left mousebutton dragged
//      mouseDragged = true;
      double zoomFactor = zvalue/270.0;
      tempWidthOffset = tempWidthOffset + (oldX-me.getX())*zoomFactor;
      tempHeightOffset = tempHeightOffset + (oldY-me.getY())*zoomFactor;
//System.out.println("widthoffset: " + widthoffset + " " + (oldX-me.getX())*zoomFactor);
//System.out.println("heightoffset: " + heightoffset + " " + (oldY-me.getY())*zoomFactor);
      //computeNewCoors();
      widthoffset = tempWidthOffset;
      heightoffset = tempHeightOffset;
      tempWidthOffset = 0;
      tempHeightOffset = 0;
      computeNewCoors();
      widthoffset = 0;
      heightoffset = 0;
      update(getGraphics());
      oldX = me.getX();
      oldY = me.getY();
    }
    if (me.getModifiers() == 4){ // right mousebutton dragged
      //mouseDragged = true;
/*      if ((oldY-me.getY()) > 0){
        this.zoffset = -1;
      }else{
        this.zoffset = 1;
      }*/
      tempzoffset = tempzoffset - (oldY-me.getY())/30.0;
      zoffset = tempzoffset;
      tempzoffset = 0;
      computeNewCoors();
      zoffset = 0;
      oldY = me.getY();
      update(getGraphics());
    }
  }

  public void mouseMoved(MouseEvent me){
  }

  private Coors searchCoors(int find_x, int find_y){
    Dimension d = getSize();
    int min = Math.min(d.width,d.height);
    Coors findCoor = null;
    int i = 0;
    while ((findCoor == null) & (i < pcoors.size())){
      Coors aCoors = (Coors)pcoors.get(i);
      if ((!aCoors.isOrigo()) & (!aCoors.isConnection())){
        int real_x = (int)Math.round(d.width/2 + ((min/2)*((aCoors.getX()))/(86*(zvalue/100.0))));
        int real_y = (int)Math.round(d.height/2 + ((min/2)*((aCoors.getY())/(86*(zvalue/100.0)))));
        int x_diff = Math.abs(real_x - find_x);
        int y_diff = Math.abs(real_y - find_y);
        if ((x_diff < 8) & (y_diff < 8)){  // om spelaren klickat inom 8 pixlar från en planet
          findCoor = aCoors;
        }
      }
      i++;
    }
    return findCoor;
  }

  public void mouseReleased(MouseEvent me){
//System.out.println("mouseReleased");
    startChange = false;
    stopChange = false;
    this.widthoffset = 0;
    this.heightoffset = 0;
    this.zoffset = 0;
    /*if (t != null){
      t.stop();
      t = null;
    }*/
  }

    public void doReset(){
      doReset = true;
    }

    private void reset(){
      doReset = false;
      newPcoors();
      setPlanets(planets, player);
      setSpaceships(spaceships);
      setOwnVips(vips);
      setOwnTroops(troops);
      setOthersVips(allVips);
      setConnections(connections);
      computeNewOrigo();
      zvalue = 100;
    }

    public void doSetCenter(String cname){
    	newCenterString = cname;
    }

    public void doSetHighlight(String pname){
    	highlightPlayer = pname;
    }

    private void setCenter(String cname){
      Logger.finer("setCenter called, cname=" + cname);
      double newcenterx = 0.0;
      double newcentery = 0.0;
      double newcenterz = 0.0;
      Coors newcentercoor = findCoors(cname);
      if (newcentercoor != null){
        newcenterx = newcentercoor.getX();
        newcentery = newcentercoor.getY();
        newcenterz = newcentercoor.getZ();
      }
      for (int i = 0; i < pcoors.size(); i++){
        Coors tempc = (Coors)pcoors.get(i);
        tempc.setX(tempc.getX() - newcenterx);
        tempc.setY(tempc.getY() - newcentery);
        tempc.setZ(tempc.getZ() - newcenterz);
        if (tempc.isConnection()){
          tempc.setX2(tempc.getX2() - newcenterx);
          tempc.setY2(tempc.getY2() - newcentery);
          tempc.setZ2(tempc.getZ2() - newcenterz);
        }
      }
      newCenterString = "";
      //paint(getGraphics());
    }

    private Coors findCoors(String cname){
      Coors found = null;
      int i = 0;
      while ((i < pcoors.size()) & (found == null)){
        Coors thiscoor = (Coors)pcoors.get(i);
        if (thiscoor.getName().equalsIgnoreCase(cname)){
          found = thiscoor;
        }else
        if ((cname.equalsIgnoreCase("Sector origo")) & (thiscoor.isOrigo())){
          found = thiscoor;
        }else{
          i++;
        }
      }
      return found;
    }

  public void paint(Graphics g){
    if (once){
    	once = false;
    	doReset();
    	String setCenterName = null;
    	if (initialSetCenter != null){
    		setCenterName = initialSetCenter;
    	}else{ // start on homeplanet
    		setCenterName = player.getHomeplanet().getName();
        	chosenCoors = setCenterName;
    	}
    	doSetCenter(setCenterName);
    	doChange(0,0,initialZoom,0,0,0);
    //}
  //    zoffset = 0;
//      t = new Thread(this);
//      t.start();
    }else{
      Dimension d = getSize();
      Image buffer = createImage(d.width, d.height);
      if (buffer != null){ // denna komponent är ej synlig, avbryt paint och avsluta tråden
        Graphics bg = buffer.getGraphics();
        bg.setColor(Color.black);
        bg.fillRect(0,0,d.width,d.height);
        if (drawEdge){
            bg.setColor(StyleGuide.colorCurrent);
            bg.drawRect(0,0,d.width-1,d.height-1);
        }
        int min = Math.min(d.width,d.height);
        for (int i = 0; i < pcoors.size(); i++){
            Coors c = (Coors)pcoors.get(i);
            Color cc = c.getColor();
            int size = (int)Math.round(((1.7*c.getZ()) + 100)/15.0);
            int newx = (int)Math.round(d.width/2 + ((min/2)*((c.getX()))/(86*(zvalue/100.0))));
            int newy = (int)Math.round(d.height/2 + ((min/2)*((c.getY())/(86*(zvalue/100.0)))));
            int newx2 = 0;
            int newy2 = 0;
//            int size2 = 0;
            if (c.isConnection()){
//              size2 = (int)Math.round(((1.7*c.getZ2()) + 100)/15.0);
              newx2 = (int)Math.round(d.width/2 + ((min/2)*((c.getX2()))/(86*(zvalue/100.0))));
              newy2 = (int)Math.round(d.height/2 + ((min/2)*((c.getY2())/(86*(zvalue/100.0)))));
            }
            //bg.setColor(new Color(((15*size*cc.getRed())/255),((15*size*cc.getGreen())/255),((15*size*cc.getBlue())/255)));
            bg.setColor(cc);  // ej fade:a färgen
            if (!c.isOrigo()){
                if (!c.isConnection()){
                    Planet tempPlanet = player.getGalaxy().findPlanet(c.getName());
                    String planetOwner = null;
                    if (tempPlanet.getPlayerInControl() != null){
                    	planetOwner = tempPlanet.getPlayerInControl().getGovenorName();
                    }
                    boolean spy = (player.getGalaxy().findVIPSpy(tempPlanet,player) != null);
                    boolean shipInSystem = player.getGalaxy().playerHasShipsInSystem(player,tempPlanet);
                    boolean troopInSystem = false;
                    if(player.getGalaxy().getTroopsOnPlanet(tempPlanet, player).size() > 0){
                    	troopInSystem = true;
                    }
                    if ((planetOwner != null) && (planetOwner.equals(highlightPlayer))){
                    	if (c.isOpen() | planetOwner.equals(player.getGovenorName()) | spy | shipInSystem | troopInSystem){
                    		// highlight planet
                    		drawSelectedPlanet(bg, newx, newy, cc);
                    	}
                    }
                    // if chosen coors show it
                    if (chosenCoors.equalsIgnoreCase(c.getName())){
                    	drawChosenPlanet(bg,newx,newy);
                    }
                    //bg.setColor(new Color(cc.getRed()/2,cc.getGreen()/2,cc.getBlue()/2));  // faded ring to make softer edge on planets
                    //bg.fillOval(newx-(size/2)+1, newy-(size/2)+1, size-2, size-2);
                    bg.setColor(cc);  // color not faded
                    bg.fillOval(newx-(size/2), newy-(size/2), size, size);
                    boolean lastKnownRazed = player.getPlanetInfos().getLastKnownRazed(tempPlanet.getName());
                    boolean razed = tempPlanet.isRazed() & (tempPlanet.getPlayerInControl() == null);
                	if ((razed & (shipInSystem | spy | troopInSystem)) | lastKnownRazed){
                		bg.setColor(new Color(255,63,63));
                		bg.drawLine(newx-(size/2)-2, newy-(size/2)-2,newx+(size/2)+2, newy+(size/2)+2);
                		bg.drawLine(newx-(size/2)-2, newy+(size/2)+2,newx+(size/2)+2, newy-(size/2)-2);
                	}else
                    if (c.isBesieged()){
                        bg.setColor(new Color(255,191,63));
                        bg.drawOval(newx-(size/2)-2, newy-(size/2)-2, size+5, size+5);
                    }
                    //bg.setColor(new Color(((15*size*cc.getRed())/255),((15*size*cc.getGreen())/255),((15*size*cc.getBlue())/255)));
                    bg.setColor(new Color(41,198,255));  // ljusblå text
                    bg.setFont(new Font("Helvetica",0,12));
                    bg.drawString(c.getName(),(newx+(size/2)+2),(newy));
                	// create the buildings string
                	String buildings = c.getBuildingsString(spy,shipInSystem, troopInSystem, tempPlanet,player);
                    // draw production and resistance
                    if (c.showValues()){
                    	String troopsString = "";
                    	Planet p = player.getGalaxy().findPlanet(c.getName());
                    	if (p.getPlayerInControl() != player){
                    		if (c.getNrTroops() > 0){
                    			troopsString = " (" + c.getNrTroops() + " troops)";
                    		}
                    	}
                        bg.setColor(cc);
                    	bg.setFont(new Font("Helvetica",0,10));
                    	if (c.isRazed()){
                    		if (c.isRazedAndUninfected()){
                        		bg.drawString("- / -",(newx+(size/2)+2)+6,(newy-12));                    			
                    		}else{
                    			// alien player
                        		bg.drawString("- / " + String.valueOf(c.getResistance()) + troopsString,(newx+(size/2)+2)+6,(newy-12));
                    		}
                    	}else{
                    		bg.drawString(String.valueOf(c.getProduction()) + " / " + String.valueOf(c.getResistance()) + troopsString,(newx+(size/2)+2)+6,(newy-12));
                    	}
//                        bg.setFont(new Font("Helvetica",0,12));
//                        bg.setColor(new Color(41,198,255));  // ljusblå text
                    	
                    	
                    }else
                    if (c.showLastKnownValues()){
                        bg.setColor(Color.GRAY);
                    	bg.setFont(new Font("Helvetica",0,10));
                    	String tmpProd = String.valueOf(c.getLastKnownProd());
                    	if (c.getLastKnownProd() == 0){
                    		tmpProd = "-";
                    	}
                    	String tmpTroops = String.valueOf(c.getLastKnownTroopsNr());
                    	if (c.getLastKnownTroopsNr() == 0){
                    		tmpTroops = "";
                    	}else{
                    		tmpTroops = " (" + tmpTroops + " troops)";
                    	}
//                		bg.drawString(tmpProd + " / " + String.valueOf(c.getLastKnownRes()),(newx+(size/2)+2)+6,(newy-12));
                		bg.drawString(tmpProd + " / " + String.valueOf(c.getLastKnownRes()) + tmpTroops,(newx+(size/2)+2)+6,(newy-12));
//                        bg.setFont(new Font("Helvetica",0,12));
//                        bg.setColor(new Color(41,198,255));  // ljusblå text
                    }
                    // draw Troops, from other players if troop fight and not planet owner troops.
					drawOtherTroops(c, (newx+(size/2)+2)+28, (newy-21), bg);
                    
                    //bg.setFont(new Font("Helvetica",0,12));
                    // maybe show wharf info anyway
                    //Logger.finest("Planet: " + tempPlanet.getName() + " buildings: " + buildings);
                    if (!buildings.equals("")){
                        bg.setColor(cc);
                    	bg.setFont(new Font("Helvetica",0,10));
                        FontMetrics fm = bg.getFontMetrics();
                        int wWidth = fm.stringWidth(buildings);
                    	bg.drawString(buildings,newx-(size/2)-3-wWidth,(newy-12));
                		// handle that one maybe have info on old planetbased buildings?
                    	if ((c.getLastKnownBuildingsString() != null) && !c.getLastKnownBuildingsString().equals("")){
                    		Logger.finest("c.getLastKnownBuildingsString(): " + c.getLastKnownBuildingsString());
                    		// there exist old info about surface buildings that should be shown in gray
                    		bg.setColor(Color.GRAY);
                    		FontMetrics fm2 = bg.getFontMetrics();
                    		int wWidth2 = fm2.stringWidth(c.getLastKnownBuildingsString()+", ");
                    		bg.drawString(c.getLastKnownBuildingsString()+", ",newx-(size/2)-3-wWidth-wWidth2,(newy-12));
                    	}
                    }else{ // show any last known buildings
                    	//Logger.finest("else");
                    	if ((c.getLastKnownBuildingsString() != null) && !c.getLastKnownBuildingsString().equals("")){
                    		Logger.finest("c.getLastKnownBuildingsString() 2: " + c.getLastKnownBuildingsString());
                    		bg.setColor(Color.GRAY);
                    		bg.setFont(new Font("Helvetica",0,10));
                    		FontMetrics fm = bg.getFontMetrics();
                    		int wWidth = fm.stringWidth(c.getLastKnownBuildingsString());
                    		bg.drawString(c.getLastKnownBuildingsString(),newx-(size/2)-3-wWidth,(newy-12));
                    	}
                    }
                    // draw max size mod in gray in paranthesis
            		bg.setFont(new Font("Helvetica",0,12));
                    FontMetrics fm2 = bg.getFontMetrics();
                    int strWidth2 = fm2.stringWidth(c.getName());
                    String sizeStr = "";
                    // rita ut rosa kors bakom namnet
//                    int strWidthSize = fm2.stringWidth(sizeStr);
                    int tmpX = newx + strWidth2 + 8;
                    FontMetrics fm3 = bg.getFontMetrics();
                    if (c.isOpen()){  
                      int strWidth = fm3.stringWidth(sizeStr) + strWidth2 + 2;
                      tmpX += fm3.stringWidth(sizeStr) + 8;
                      bg.setColor(new Color(255,24,247));
                      bg.drawLine((newx+(size/2)+2+strWidth+4),(newy-1),(newx+(size/2)+2+strWidth+4),(newy-9));
                      bg.drawLine((newx+(size/2)+2+strWidth+1),(newy-5),(newx+(size/2)+2+strWidth+7),(newy-5));
                      bg.fillRect((newx+(size/2)+2+strWidth+3),(newy)-6,3,3);
                    }else{
                      tmpX += fm3.stringWidth(sizeStr);
                    }
                    if (showPlanetNotes){
                    	PlanetNotesChange aPlanetNotesChange = player.getOrders().getPlanetNotesChange(tempPlanet); 
                    	String planetNotes = null;
                    	if (aPlanetNotesChange != null){
                    		planetNotes = aPlanetNotesChange.getNotesText();
                    	}else{
                    		planetNotes = player.getPlanetInfos().getNotes(c.getName());
                    	}
                    	if ((planetNotes != null) & !("".equals(planetNotes))){
                    		bg.setColor(new Color(150,150,150));
                    		bg.drawString("(" + planetNotes + ")",tmpX,newy);
                    	}
                    }
                    int currenty = newy+10;
                    bg.setFont(new Font("Helvetica",0,10));

                    // get players spaceships on this planet
                    List<Spaceship> tempshortsShips = c.getShortNameablesShips();
                    // sort the ships
                    Collections.sort(tempshortsShips,new SpaceshipSizeAndBuildCostComparator());
                    int currentx = 0;
                    int shipCount = 0;
                    List<VIP> tmpVips = new ArrayList<VIP>();
                    for (int l = 0; l < tempshortsShips.size(); l++){
                      Spaceship tempss = tempshortsShips.get(l);
                      shipCount++;
                      addVIPsOnShip(tempss,c,tmpVips);
                      //bg.setColor(c.getColor());
                   	  bg.setColor(ColorConverter.getColorFromHexString(tempss.getOwner().getFaction().getPlanetHexColor()));
                      boolean drawNow = false;
                      if (l == (tempshortsShips.size() - 1)){
                      	drawNow = true;
                      }else{
                        Spaceship tempss2 = tempshortsShips.get(l+1);
                      	if (!tempss2.getSpaceshipType().getName().equals(tempss.getSpaceshipType().getName())){
                      		drawNow = true;
                      	}
                      }
                      if (drawNow){
                      	String tmpShipStr = tempss.getSpaceshipType().getShortName();
                      	if (shipCount > 1){
                      		tmpShipStr = shipCount + " " + tmpShipStr;
                      	}
                      	if (tmpVips.size() > 0){
                      		tmpShipStr = tmpShipStr + " (";
                      	}
                      	if (tmpVips.size() > 0){
//                     		System.out.println("tmpVips.size() = " + tmpVips.size());
                      		for (Iterator<VIP> iter = tmpVips.iterator(); iter.hasNext();) {
								VIP aVIP = iter.next();
								tmpShipStr = tmpShipStr + aVIP.getShortName();
								if (iter.hasNext()){
									tmpShipStr = tmpShipStr + ",";
								}
							}
//                      		tmpShipStr = tmpShipStr + ")";
                      	}
                      	if (tmpVips.size() > 0){
                      		tmpShipStr = tmpShipStr + ")";
                      	}
                      	bg.drawString(tmpShipStr,newx+(size/2)+8+currentx,currenty);
                        currenty = currenty + 9;
                        shipCount = 0;
                        tmpVips = new ArrayList<VIP>();
                      }
                    }
                    // show troops on planet (not in ships at planet)
                    List<Troop> tempTroops = c.getTroops();
                    Collections.sort(tempTroops, new TroopTypeAndBuildCostComparator());
                    // only keep those that are on the planet
                    LinkedHashMap<String,Integer> troopsMap = new LinkedHashMap<String,Integer>(); // String contains unique name of trooptype of troop
                    for (Troop troop : tempTroops) {
                    	String name = troop.getTroopType().getUniqueShortName();
                    	Integer tempCounter = troopsMap.get(name);
                    	if (tempCounter == null){
                    		tempCounter = 1; 
                    	}else{
                    		tempCounter = tempCounter + 1;
                    	}
                    	troopsMap.put(troop.getTroopType().getUniqueShortName(),tempCounter);
					}
                    if ((tempTroops.size() > 0) & (tempshortsShips.size() > 0)){
                    	// draw line between ships and troops
                    	bg.setColor(StyleGuide.colorCurrent.darker());
                		bg.drawLine(newx+12, currenty-6, newx+30, currenty-6);
                        currenty = currenty + 5;
                    }
					if (tempTroops.size() > 0){
                    	bg.setColor(StyleGuide.colorCurrent);
                    	for (Iterator<String> iter = troopsMap.keySet().iterator(); iter.hasNext();) {
							String aTroopTypeName = (String) iter.next();
							String tmpTroopStr = "";
							if (troopsMap.get(aTroopTypeName) > 1){
								tmpTroopStr = troopsMap.get(aTroopTypeName).toString() + " ";
							}
							tmpTroopStr = tmpTroopStr + aTroopTypeName;
							// get vips on troops
							List<VIP> vipsOnTroops = new LinkedList<VIP>();
							for (Troop aTroop : tempTroops) {
//								System.out.println("looking for vips in troops " + aTroop);
								if (aTroop.getTroopType().getUniqueShortName().equals(aTroopTypeName)){
									addVIPsOnTroop(aTroop,c,vipsOnTroops);
								}
							}
	                      	if (vipsOnTroops.size() > 0){
	                      		tmpTroopStr = tmpTroopStr + " (";
	                      		for (Iterator<VIP> iter2 = vipsOnTroops.iterator(); iter2.hasNext();) {
									VIP aVIP = iter2.next();
									tmpTroopStr = tmpTroopStr + aVIP.getShortName();
									if (iter2.hasNext()){
										tmpTroopStr = tmpTroopStr + ",";
									}
								}
	                      		tmpTroopStr = tmpTroopStr + ")";
	                      	}
	                      	// draw troops string on map
							bg.drawString(tmpTroopStr,newx+11,currenty);
							currenty = currenty + 9;
                    	}
                    }
					
					// draw fleets, neutral or from other players
					drawOtherFleets(c,newx+(size/2)+8,currenty,bg);
					// draw last known max ship size
					if ((c.getLastKnownMaxShipSize() != null) && !c.getLastKnownMaxShipSize().equals("")){
                		bg.setColor(Color.GRAY);
                		bg.drawString(c.getLastKnownMaxShipSize(),newx+(size/2)+8,currenty);						
					}
                    // show vips (not in ships)
                    List<VIP> tempshortsVIPs = c.getShortNameablesVIPs();
                    currenty = newy+10;
                    for (int l = 0; l < tempshortsVIPs.size(); l++){
                      VIP tempv = tempshortsVIPs.get(l);
//                      System.out.println("VIP location:  " + tempv.getLocation().getName() + " " + c.getName());
//                      System.out.println("tempv.getShipLocation(): " + tempv.getShipLocation());
                      if ((tempv.getShipLocation() == null) & (tempv.getTroopLocation() == null)){
//                      	System.out.println("tempv.getBoss(): " + tempv.getBoss().getGovenorName());
//                      	System.out.println("player: " + player.getGovenorName());
//                      	System.out.println("c.isOpen(): " + c.isOpen());
                      	if ((tempv.getBoss() == player) | (c.isOpen())){
                      		bg.setColor(ColorConverter.getColorFromHexString(tempv.getBoss().getFaction().getPlanetHexColor()));
                      		String tmpName = tempv.getShortName();
                      		FontMetrics fm = g.getFontMetrics();
                      		int tmpWidth = fm.stringWidth(tmpName);
                      		bg.drawString(tmpName,newx-(size/2)-3-tmpWidth,currenty);
                      		currenty = currenty + 9;
                      	}
                      }
                    }
                }else{
                  bg.drawLine(newx,newy,newx2,newy2);
                }
            }else{
                //bg.drawOval(newx-((int)Math.round((min*(100.0/zvalue)))/2),newy - ((int)Math.round((min*(100.0/zvalue)))/2),(int)Math.round(min*(100.0/zvalue)),(int)Math.round(min*(100.0/zvalue)));
                bg.drawOval(newx-2,newy-2,4,4);
            }
        }
        // skriv ut zoom
        bg.setColor(StyleGuide.colorCurrent.darker());  // grön text
        bg.setFont(new Font("Helvetica",0,12));
        bg.drawString("Map: " + player.getGalaxy().getMapNameFull(),5,13);
        bg.drawString("Zoom: " + Functions.formatString(String.valueOf(zvalue),2),5,26);
        bg.drawString("X offset: " + Functions.formatString(String.valueOf(origo.getX()),2),5,37);
        bg.drawString("Y offset: " + Functions.formatString(String.valueOf(origo.getY()),2),5,48);
        bg.drawString("Z offset: " + Functions.formatString(String.valueOf(origo.getZ()),2),5,59);
        String showing = "Showing ships";
        if (showVips){
        	showing = "Showing VIPs";
        }
        bg.drawString(showing,5,70);
        
        /*
        map control buttons.
        
        bg.setColor(StyleGuide.colorCurrent);
        bg.setFont(new Font("Dialog",1,12));
        bg.drawRect(30, 80, 40, 20);
        bg.drawString("Up",45,94);
        
        bg.drawRect(10, 100, 40, 20);
        bg.drawString("Left",20,114);
        
        bg.drawRect(50, 100, 40, 20);
        bg.drawString("Right",57,114);
        
        bg.drawRect(30, 120, 40, 20);
        bg.drawString("Down",35,134);
        
        */
        
        
        //bg.drawString("R offset: " + Functions.formatString(String.valueOf(roffset),2),5,59);
        // rita ut bufferten
        g.drawImage(buffer,0,0,this);
      }
    }
  }
  
  private void addVIPsOnShip(Spaceship tempss,Coors c,List<VIP> tmpVips){
  	List<VIP> vipsAtPlanet = c.getShortNameablesVIPs();
  	for (VIP aVIP : vipsAtPlanet) {
		if (aVIP.getShipLocation() == tempss){
			tmpVips.add(aVIP);
		}
	}
  }

  private void addVIPsOnTroop(Troop aTroop,Coors c,List<VIP> tmpVips){
	  List<VIP> vipsAtPlanet = c.getShortNameablesVIPs();
	  for (VIP aVIP : vipsAtPlanet) {
		  if (aVIP.getTroopLocation() == aTroop){
			  tmpVips.add(aVIP);
		  }
	  }
  }

    private void updateChosenPlanetAnimation(){
      animation = animation + Math.PI/20;
      if (animation > (Math.PI*2)){
        animation = animation - Math.PI*2;
      }
    }

    private void drawChosenPlanet(Graphics g,int x,int y){
      int nrLines = 12;
      int lineLength = 14;
      for (int i = 0; i < nrLines; i++){ // iterate over the lines
        double angleRad = 2*Math.PI*i/nrLines;
        int relX = (int)Math.round(lineLength*Math.sin(angleRad));
//System.out.println("relX: " + relX);
        int relY = (int)Math.round(lineLength*Math.cos(angleRad));
        int colorValue = (int)Math.abs(Math.round(200*Math.sin(animation+angleRad)))+55;
        drawFadingLine(g,x,y,x+relX,y+relY,new Color(colorValue,colorValue,0));
      }
    }

    private void drawSelectedPlanet(Graphics g,int x,int y,Color aColor){
        int nrLines = 12;
        int lineLength = 14;
        double animation2 = Math.PI;
        for (int i = 0; i < nrLines; i++){ // iterate over the lines
        	double angleRad = 2*Math.PI*(i+0.5)/nrLines;
        	int relX = (int)Math.round(lineLength*Math.sin(angleRad));
        	int relY = (int)Math.round(lineLength*Math.cos(angleRad));
        	int colorValueRed = (int)Math.abs(Math.round(aColor.getRed()/2*Math.sin(animation2+angleRad))) + aColor.getRed()/2;
        	int colorValueGreen = (int)Math.abs(Math.round(aColor.getGreen()/2*Math.sin(animation2+angleRad))) + aColor.getGreen()/2;
        	int colorValueBlue = (int)Math.abs(Math.round(aColor.getBlue()/2*Math.sin(animation2+angleRad))) + aColor.getBlue()/2;
        	Color newColor = new Color(colorValueRed,colorValueGreen,colorValueBlue);
        	drawFadingLine(g,x,y,x+relX,y+relY,newColor);
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
//System.out.println("newX: " + newX);
        int distY = (int)Math.round((diffY*(j+1.0)/nrShades));
        int newY = curY + distY - oldDistY;
        g.drawLine(curX,curY,newX,newY);
        oldDistX = distX;
        oldDistY = distY;
        curX = newX;
        curY = newY;
      }
    }

    private void drawOtherFleets(Coors aCoors, int x,int y, Graphics g){
      // hämta planeten
      Planet tempPlanet = player.getGalaxy().findPlanet(aCoors.getName());
      String shipSize = "";
      
      
      boolean troopInSystem = false;
      if(player.getGalaxy().getTroopsOnPlanet(tempPlanet, player).size() > 0){
      	troopInSystem = true;
      }
      
      
//      int counter = 0;
      if ((player.getGalaxy().isPlayerShipAtPlanet(player,tempPlanet)) | troopInSystem | (tempPlanet.getPlayerInControl() == player) | (tempPlanet.isOpen()) | (player.getGalaxy().findVIPSpy(tempPlanet,player) != null)){
        List<Player> allp = player.getGalaxy().getPlayers();
        // loopa igenom alla spelare och kolla efter flottor
        for (int i = 0; i < allp.size(); i++){
          Player tempPlayer = (Player)allp.get(i);
          if (tempPlayer != player){
            shipSize = player.getGalaxy().getLargestShipSizeOnPlanet(tempPlanet,tempPlayer,false);
            boolean civilianExists = !player.getGalaxy().getLargestShipSizeOnPlanet(tempPlanet,tempPlayer,true).equals("");
            if (civilianExists){
            	if (shipSize.equals("")){
            		shipSize = "civ";
            	}else{
            		shipSize = shipSize + "+civ";
            	}
            }
            // skriv ut största skeppsstorleken + (GovName) i rätt färg
            if (!shipSize.equalsIgnoreCase("")){
              g.setColor(ColorConverter.getColorFromHexString(tempPlayer.getFaction().getPlanetHexColor()));
              g.drawString(shipSize + " (" + tempPlayer.getGovenorName() + ")",x,y);
              y = y + 9;
            }
          }
        }
        // kolla efter neutrala skepp
        shipSize = player.getGalaxy().getLargestShipSizeOnPlanet(tempPlanet,null,false);
        if (!shipSize.equalsIgnoreCase("")){
          g.setColor(StyleGuide.colorNeutralWhite);
//          g.drawString(shipSize + " (Neutral)",x,y);
          g.drawString(shipSize,x,y);
        }
      }
    }
    
    private void drawOtherTroops(Coors aCoors, int x,int y, Graphics g){
        // hämta planeten
        Planet tempPlanet = player.getGalaxy().findPlanet(aCoors.getName());
        String troopString = "";
        
        boolean surveyShip = (player.getGalaxy().findSurveyShip(tempPlanet,player) != null);
        boolean surveyVIP = (player.getGalaxy().findSurveyVIPonShip(tempPlanet,player) != null);
        
        if ((tempPlanet.getPlayerInControl() == player) | (player.getGalaxy().findVIPSpy(tempPlanet,player) != null) | surveyShip | surveyVIP){
          List<Player> allp = player.getGalaxy().getPlayers();
          // loopa igenom alla spelare och kolla efter flottor
          for (int i = 0; i < allp.size(); i++){
            Player tempPlayer = (Player)allp.get(i);
            if (tempPlayer != player && tempPlayer != tempPlanet.getPlayerInControl()){
            	int numberOfTroops = player.getGalaxy().getTroopsOnPlanet(tempPlanet, tempPlayer).size();
            	if(numberOfTroops > 0){
            		troopString = numberOfTroops + " troops";
            		g.setColor(ColorConverter.getColorFromHexString(tempPlayer.getFaction().getPlanetHexColor()));
                    g.drawString(troopString + " (" + tempPlayer.getGovenorName() + ")",x,y);
                    y = y - 9;
                    troopString= "";
            	}
            }
          }
        }
    }

    @SuppressWarnings("deprecation")
	public void startchange(int xoffset, int yoffset, int zoffset, int roffset, int widthoffset, int heightoffset){
//System.out.println("startchange called" + t);
      startChange = true;
      this.xoffset = xoffset;
      this.yoffset = yoffset;
      this.zoffset = zoffset;
      this.roffset = roffset;
      this.widthoffset = widthoffset;
      this.heightoffset = heightoffset;
      if (t != null){
//System.out.println("startchange is not null" + t);
        if (t.isAlive()){
//System.out.println("startchange isAlive" + t);
          t.stop();
        }
        t = new Thread(this);
        t.start();
      }else{
//System.out.println("startchange 2" + t);
        t = new Thread(this);
//System.out.println("startchange 3" + t);
        t.start();
//System.out.println("startchange 4" + t);
      }
    }

    public void doChange(int xoffset, int yoffset, int zoffset, int roffset, int widthoffset, int heightoffset){
      Logger.finer("doChange called, zoffset=" + zoffset);
      //doChange = true;
      this.xoffset = xoffset;
      this.yoffset = yoffset;
      this.zoffset = zoffset;
      this.roffset = roffset;
      this.widthoffset = widthoffset;
      this.heightoffset = heightoffset;
      reset();
      if (!newCenterString.equalsIgnoreCase("")){
        setCenter(newCenterString);
      }
      computeNewCoors();
//      paint(getGraphics());
      repaint();
    }

    public void doStopChange(){
//System.out.println("doStopChange called");
      startChange = false;
      stopChange = true;
    }

    @SuppressWarnings("deprecation")
	private void stopchange(){
//System.out.println("stopchange called");
      this.xoffset = 0;
      this.yoffset = 0;
      this.zoffset = 0;
      this.roffset = 0;
      this.widthoffset = 0;
      this.heightoffset = 0;
      if (t != null){
        t.stop();
        t = null;
      }
    }

    private void computeNewCoors(){
      List<Coors> newcoors = new ArrayList<Coors>();
      double dist;
      double angle;
      double newangle;
      double radx;
      double rady;
      double radr;
      Coors c;
      for (int i = 0; i < pcoors.size(); i++){
        c = (Coors)pcoors.get(i);
        radx = (xoffset*2*Math.PI)/360;
        rady = (yoffset*2*Math.PI)/360;
        radr = (roffset*2*Math.PI)/360;
        if (c.isConnection()){ // räkna ut det andra uppsättningen koordinater för linjer
          if ((widthoffset != 0) | (heightoffset != 0)){
            c.setX2(c.getX2() - widthoffset);
            c.setY2(c.getY2() - heightoffset);
//          }else
  //        if (heightoffset != 0){
    //        c.setY2(c.getY2() - heightoffset);
          }else
          if (xoffset != 0){
            dist = Math.sqrt(Math.pow(c.getX2(),2) + Math.pow(c.getZ2(),2));
            if (dist > (1/1000.0)){
              angle = Math.asin(c.getZ2()/dist);
              if (c.getX2() < 0){
                angle = Math.PI - angle;
              }
              newangle = angle - radx;
              c.setX2(dist*Math.cos(newangle));
              c.setZ2(dist*Math.sin(newangle));
            }
          }else
          if (yoffset != 0){
            dist = Math.sqrt(Math.pow(c.getY2(),2) + Math.pow(c.getZ2(),2));
            if (dist > (1/1000.0)){
              angle = Math.asin(c.getZ2()/dist);
              if (c.getY2() < 0){
                angle = Math.PI - angle;
              }
              newangle = angle - rady;
              c.setY2(dist*Math.cos(newangle));
              c.setZ2(dist*Math.sin(newangle));
            }
          }else
          if (roffset != 0){
            dist = Math.sqrt(Math.pow(c.getY2(),2) + Math.pow(c.getX2(),2));
            if (dist > (1/1000.0)){
              angle = Math.asin(c.getY2()/dist);
              if (c.getX2() < 0){
                angle = Math.PI - angle;
              }
              newangle = angle - radr;
              c.setX2(dist*Math.cos(newangle));
              c.setY2(dist*Math.sin(newangle));
            }
          }else
          if (zoffset != 0){
            zvalue = zvalue*(1+(zoffset/4000.0));
            checkZ();
          }
        }
        // ordinarie koordinater
        if ((widthoffset != 0) | (heightoffset != 0)){
          c.setX(c.getX() - widthoffset);
          c.setY(c.getY() - heightoffset);
    //    }else
  //      if (heightoffset != 0){
//          c.setY(c.getY() - heightoffset);
        }else
        if (xoffset != 0){
          dist = Math.sqrt(Math.pow(c.getX(),2) + Math.pow(c.getZ(),2));
          if (dist > (1/1000.0)){
            angle = Math.asin(c.getZ()/dist);
            if (c.getX() < 0){
              angle = Math.PI - angle;
            }
            newangle = angle - radx;
            c.setX(dist*Math.cos(newangle));
            c.setZ(dist*Math.sin(newangle));
          }
        }else
        if (yoffset != 0){
          dist = Math.sqrt(Math.pow(c.getY(),2) + Math.pow(c.getZ(),2));
          if (dist > (1/1000.0)){
            angle = Math.asin(c.getZ()/dist);
            if (c.getY() < 0){
              angle = Math.PI - angle;
            }
            newangle = angle - rady;
            c.setY(dist*Math.cos(newangle));
            c.setZ(dist*Math.sin(newangle));
          }
        }else
        if (roffset != 0){
          dist = Math.sqrt(Math.pow(c.getY(),2) + Math.pow(c.getX(),2));
          if (dist > (1/1000.0)){
            angle = Math.asin(c.getY()/dist);
            if (c.getX() < 0){
              angle = Math.PI - angle;
            }
            newangle = angle - radr;
            c.setX(dist*Math.cos(newangle));
            c.setY(dist*Math.sin(newangle));
          }
        }else
        if (zoffset != 0){
          zvalue = zvalue*(1+(zoffset/4000.0));
          checkZ();
        }
        insertCoorSorted(c, newcoors);
      }
      pcoors = newcoors;
    }

    private void checkZ(){
      if (zvalue > 200){
        zvalue = 200;
      }else
      if (zvalue < 5){
        zvalue = 5;
      }
    }

    private void insertCoorSorted(Coors c, List<Coors> inscoors){
      boolean found = false;
      if (c.isConnection()){
        inscoors.add(0,c);
      }else{
        int i = 0;
        while ((!found) & (i < inscoors.size())){
          Coors temp = (Coors)inscoors.get(i);
          if (!temp.isConnection() & !temp.isOrigo()){ // om ej connection eller origo kolla z-värdet
            if (c.getZ() < temp.getZ()){
              inscoors.add(i,c);
              found = true;
            }else{
              i++;
            }
          }else{  // connections och origo skall alltid ligga först
            i++;
          }
        }
        if (!found){
          inscoors.add(c);
        }
      }
    }

    public void setPlanets(List<Planet> planets, Player aPlayer){
      this.planets = planets;
      Coors c;
      PlanetInfos pi = aPlayer.getPlanetInfos();
      for (int i = 0; i < planets.size(); i++){
        Planet p = (Planet)planets.get(i);
        boolean spy = (aPlayer.getGalaxy().findVIPSpy(p,aPlayer) != null);
        
        boolean troopInSystem = false;
        if(player.getGalaxy().getTroopsOnPlanet(p, aPlayer).size() > 0){
        	troopInSystem = true;
        }
        boolean surveyShip = (aPlayer.getGalaxy().findSurveyShip(p,aPlayer) != null);
        boolean surveyVIP = (aPlayer.getGalaxy().findSurveyVIPonShip(p,aPlayer) != null);
        //c = new Coors(p.getXcoor()-50, p.getYcoor()-50, p.getZcoor()-50);  varför -50?
        c = new Coors(p.getXcoor(), p.getYcoor(), p.getZcoor());
        c.setName(p.getName());
        c.setOpen(p.isOpen());
        // set prod / res
        if ((p.isOpen()) | (p.getPlayerInControl() == aPlayer) | (spy) | (surveyShip | surveyVIP | troopInSystem)){
        	c.setProduction(p.getPopulation());
        	c.setResistance(p.getResistance());
        	int nrTroops = aPlayer.getGalaxy().getNrTroops(p);
        	c.setNrTroops(nrTroops);
        }else{
        	// set last known prod & res
        	int tmpProd = pi.getLastKnownProd(p.getName());
        	int tmpRes = pi.getLastKnownRes(p.getName());
        	c.setLastKnownProd(tmpProd);
        	c.setLastKnownRes(tmpRes);
        	c.setLastKnownTroopsNr(pi.getLastKnownTroopsNr(p.getName()));
        }
        // maybe set last known max ship size
        if ((p.getPlayerInControl() != aPlayer) & !aPlayer.getGalaxy().playerHasShipsInSystem(aPlayer,p) & !p.isOpen() & !spy & !troopInSystem){
        	c.setLastKnownMaxShipSize(pi.getLastKnownMaxShipSize(p.getName()));
        }
        // set building info
        if ((p.isOpen()) | (p.getPlayerInControl() == aPlayer) | (spy) | troopInSystem){
            if (p.getBuildings().size() > 0){
            	c.setBuilding(p.getBuildings());
            }
        }else
        if(aPlayer.getGalaxy().playerHasShipsInSystem(aPlayer,p)){
        	c.setBuilding(p.getBuildingsInOrbit());
        	// handle that one maybe have info on old planetbased buildings
            c.setLastKnownBuildingsString(pi.getLastKnownBuildingsOnSurface(p.getName()));
        	
        }else{
        	c.setLastKnownBuildingsString(pi.getLastKnownBuildings(p.getName()));
        }
        if (p.isBesieged() & ((aPlayer == p.getPlayerInControl()) | (aPlayer.getGalaxy().playerHasShipsInSystem(aPlayer,p) | spy | troopInSystem))){
          c.setBesieged();
        }
        if (p.isRazed()){
          c.setRazed(true);
        }
        if (p.isRazedAndUninfected()){
          c.setRazedAndUninfected(true);
        }
        Player tempPlayer = p.getPlayerInControl();
        if ((tempPlayer == aPlayer) | (p.isOpen()) | (aPlayer.getGalaxy().playerHasShipsInSystem(aPlayer,p)) | spy | troopInSystem){ // spelarens egen planet, visa rätt färg
          if (p.getPlayerInControl() != null){
            c.setColor(ColorConverter.getColorFromHexString(tempPlayer.getFaction().getPlanetHexColor()));
          }else{
            c.setColor(StyleGuide.colorNeutralWhite);
          }
        }else{ //  annans eller neutral planet, visa senaste kända info/färg
//          PlanetInfos pi = aPlayer.getPlanetInfos();
          if (pi.getLastKnownOwner(p.getName()).equalsIgnoreCase("Neutral")){ // neutral planet
            c.setColor(StyleGuide.colorNeutralWhite);
          }else{
            c.setColor(ColorConverter.getColorFromHexString(aPlayer.getGalaxy().getPlayer(pi.getLastKnownOwner(p.getName())).getFaction().getPlanetHexColor()));
          }
        }
       insertCoorSorted(c, pcoors);
      }
    }
    
    public void updatePcoors(){
 
        repaint();
    }

    public void setSpaceships(List<Spaceship> spaceships){
      this.spaceships = spaceships;
      Coors foundCoor = null;
      Spaceship tempss = null;
      for (int i = 0; i < spaceships.size(); i++){
        tempss = (Spaceship)spaceships.get(i);
        if (tempss.getLocation() != null){   // skepp som flyr syns ej på kartan
          foundCoor = findCoors(tempss.getLocation().getName());
          if (foundCoor != null){
            foundCoor.addShip(tempss);
          }
        }
      }
    }

    public void setOwnVips(List<VIP> vips){
        this.vips = vips;
        Coors foundCoor = null;
        VIP tempv = null;
        for (int i = 0; i < vips.size(); i++){
          tempv = (VIP)vips.get(i);
//          LoggingHandler.fine("VIP == null: " + (tempv == null));
//          LoggingHandler.fine("VIP location == null: " + (tempv.getLocation() == null));
//          LoggingHandler.fine("VIP: " + tempv.getName() + " " + tempv.getLocationString());
          if (tempv.getLocation() != null){
          	foundCoor = findCoors(tempv.getLocation().getName());
          	if (foundCoor != null){
//          		LoggingHandler.fine("coor found!");
          		foundCoor.addVip(tempv);
          	}
          }
        }
    }

    /**
     * Add all vips that are on planets (or in ship at planet) of corresponding coors
     * @param troops all troops belonging to current player
     */
    public void setOwnTroops(List<Troop> ownTroops){
    	this.troops = ownTroops;
        Coors foundCoor = null;
        for (Troop aTroop : troops) {
        	if (aTroop.getPlanetLocation() != null){
        		foundCoor = findCoors(aTroop.getPlanetLocation().getName());
        		if (foundCoor != null){
        			foundCoor.addTroop(aTroop);
        		}
        	}else
        	if (aTroop.getShipLocation() != null){
        		if (aTroop.getShipLocation().getLocation() != null){
            		foundCoor = findCoors(aTroop.getShipLocation().getLocation().getName());
            		if (foundCoor != null){
            			foundCoor.addTroop(aTroop);
            		}
        		}
        	}
        }
    }

    /**
     * 
     * @param allVips all vips in game (=galaxy)
     */
    public void setOthersVips(List<VIP> allVips){
    	this.allVips = allVips;
        Coors foundCoor = null;
        VIP tempv = null;
        for (int i = 0; i < allVips.size(); i++){
          tempv = (VIP)allVips.get(i);
//          LoggingHandler.fine("-VIP == null: " + (tempv == null));
//          LoggingHandler.fine("-VIP location == null: " + (tempv.getLocation() == null));
//          LoggingHandler.fine("-VIP: " + tempv.getName() + " " + tempv.getLocationString());
          if (tempv.getLocation() != null){
          	foundCoor = findCoors(tempv.getLocation().getName());
          	if (foundCoor != null){
          		if (!tempv.getBoss().isPlayer(player)){
//          			LoggingHandler.fine("other player found!");
          			if (tempv.getShowOnOpenPlanet()){
//              			LoggingHandler.fine("show on open planet!");
              			foundCoor.addVip(tempv);
          			}
          		}
          	}
          }
        }
    }

    public void setConnections(List<PlanetConnection> connections){
      this.connections = connections;
      Coors c;
      for (int i = 0; i < connections.size(); i++){
        PlanetConnection pc = (PlanetConnection)connections.get(i);
        Planet p1 = pc.getPlanet1();
        Planet p2 = pc.getPlanet2();
        c = new Coors(p1.getXcoor(), p1.getYcoor(), p1.getZcoor(), p2.getXcoor(), p2.getYcoor(), p2.getZcoor(),pc.isLongRange());
        c.setName("");
        if (!pc.isLongRange()){
            c.setColor(StyleGuide.colorMapShortRange); // mörkröd
        }else{
        	// check if spacestations make this connection short range anyway
        	boolean isSpaceStation = false;
        	if ((p1.getPlayerInControl() != null) & (p2.getPlayerInControl() != null)){ // none of the planets are neutral
    			if ((p1.hasSpacePort()) & (p2.hasSpacePort())){ // both have a spacestation
    				if (player.getGalaxy().getDiplomacy().friendlySpaceports(p1.getPlayerInControl(),p2.getPlayerInControl())){
        				if (player.getGalaxy().getDiplomacy().friendlySpaceports(player,p1.getPlayerInControl())){
            				if (player.getGalaxy().getDiplomacy().friendlySpaceports(player,p2.getPlayerInControl())){
            					isSpaceStation = true;
            				}	
            			}
            		}
        		}
        	}
        	// set coor values
        	if (isSpaceStation){
        		c.setColor(StyleGuide.colorMapSpacePortsRange);  // ljusröd
        	}else{
        		c.setColor(StyleGuide.colorMapLongRange);  // mörkblå
        	}
        }
        insertCoorSorted(c, pcoors);
      }
    }
    
    public void setShowVips(boolean sv){
    	showVips = sv;
    }

	public boolean isShowPlanetNotes() {
		return showPlanetNotes;
	}

	public void setShowPlanetNotes(boolean showPlanetNotes) {
		this.showPlanetNotes = showPlanetNotes;
	}

	public boolean isShowLastKnownProdRes() {
		return showLastKnownProdRes;
	}

	public void setShowLastKnownProdRes(boolean showLastKnownProdRes) {
		this.showLastKnownProdRes = showLastKnownProdRes;
	}

	public void setInitialSetCenter(String initialSetCenter) {
		this.initialSetCenter = initialSetCenter;
	}

	public void setChosenCoors(String chosenCoors) {
		this.chosenCoors = chosenCoors;
	}

}