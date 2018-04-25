package sr.client;

import java.awt.Graphics;

import javax.swing.JPanel;

import sr.client.components.SRBasePanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.logging.Logger;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Spaceship;

/**
 * The panel which contains the game gui in the client.
 * 
 * @author wmpabod
 *
 */
public class GameGUIPanel extends JPanel implements SRUpdateablePanel, ShowMapPlanet{
  private static final long serialVersionUID = 1L;
  private String id;
  private Player p;
  private SpaceRazeApplet client;
  private ImageHandler imageHandler;
  // Fasta Panels
  private ShowPlanet showPlanetPanel;
  private MapCanvas map;
  private MapControls controls;
  private TurnInfoPanel turnInfoPanel;
  private NavBarPanel navBarPanel;
  // tillfälliga paneler
  private JPanel currentPanel;
  // coordinates
  private int mapx,mapy,mapw,maph;
  private SRBasePanel mapHolder;
  
  public GameGUIPanel(String id, Player p, SpaceRazeApplet client, ImageHandler imageHandler){
    this.id = id;
    this.p = p;
    this.client = client;
    this.imageHandler = imageHandler;
    setLayout(null);
    setBackground(StyleGuide.colorBackground);

    /*
    emblemPanel = new EmblemPanel();
    emblemPanel.setBounds(1,0,120,45);
    add(emblemPanel);
    */
 
    mapHolder = new SRBasePanel();
    mapHolder.setBounds(4, 32, 860, 635);
    
    map = new MapCanvas(p,this);
    mapx = 128;
    mapy = 1;
    mapw = 735;
    maph = 640;
    
    /*
    mapx = 128;
    mapy = 29;
    mapw = 732;
    maph = 580;
     * 
     */
    map.setBounds(mapx,mapy,mapw,maph);
    map.setPlanets(p.getGalaxy().getPlanets(),p);
    map.setSpaceships(p.getGalaxy().getPlayersSpaceships(p));
    map.setOwnVips(p.getGalaxy().getPlayersVips(p));
    map.setOwnTroops(p.getGalaxy().getPlayersTroops(p));
    map.setOthersVips(p.getGalaxy().getAllVIPs());
    map.setConnections(p.getGalaxy().getPlanetConnections());
    map.computeNewOrigo();
    mapHolder.add(map);

    controls = new MapControls(p,map);
    controls.setBounds(1,1,120,370);
    mapHolder.add(controls);
    
    
    add(mapHolder);
 
 
    turnInfoPanel = new TurnInfoPanel(client,p, this,imageHandler);
    turnInfoPanel.setBounds(1,674,1197,26);
    add(turnInfoPanel);
    
    navBarPanel = new NavBarPanel(p,this,client);
    navBarPanel.setBounds(0,0,1198,27);
    add(navBarPanel);

    showPlanetPanel = new ShowPlanet(p.getHomeplanet(),p,client,imageHandler);
    showPlanetPanel.setBounds(872,27,326,643);
    add(showPlanetPanel);
    
    

    navBarPanel.showPanel("Highlights");
  }
  
  public void setCurrentPanel(JPanel newPanel){
    if (currentPanel != null){
      currentPanel.setVisible(false);
      remove(currentPanel);
    }
    
    mapHolder.setVisible(false);
    currentPanel = newPanel;
    //currentPanel.setBounds(mapx,mapy,mapw,maph);
    currentPanel.setBounds(6, 32, 860, 635);
    currentPanel.setVisible(true);
    add(currentPanel);
    update(getGraphics());
  }

  public void showMap(){
    if (currentPanel != null){
      currentPanel.setVisible(false);
    }
    mapHolder.setVisible(true);
  }
  
  public String getNotes(){
    return navBarPanel.getNotes();
  }

  public String getPlanetNotes(){
	  return showPlanetPanel.getPlanetNotes();
  }

  public Planet getCurrentPlanet(){
	  return showPlanetPanel.getPlanet();
  }
  
  public void hidePanels(){
  	navBarPanel.showPanel("dölj alla paneler");
  }

  public void showPlanet(String aPlanetName){
    // hämta planeten
    Planet aPlanet = p.getGalaxy().findPlanet(aPlanetName);
    // ta bort ev. gammal panel
    if (showPlanetPanel != null){
    	MiniPlanetPanel miniPlanetPanel = showPlanetPanel.getMiniPlanetPanel();
    	if (miniPlanetPanel != null){
    		miniPlanetPanel.saveNotesUpdateMap(false);
    	}
    	remove(showPlanetPanel);
    }
    // isåfall skapa MapShowPlanet med den planeten
    showPlanetPanel = new ShowPlanet(aPlanet,p,client,imageHandler);
    showPlanetPanel.setBounds(872,27,326,643);
    add(showPlanetPanel);
    update(getGraphics());
  }
  
  public void savePlanetNotes(){
	  if (showPlanetPanel != null){
		  MiniPlanetPanel miniPlanetPanel = showPlanetPanel.getMiniPlanetPanel();
		  if (miniPlanetPanel != null){
			  // om en planet visas, spara notestext
			  miniPlanetPanel.saveNotesUpdateMap(false);
		  }
		  remove(showPlanetPanel);
	  }
  }

  public void showShipOnPlanet(String aPlanetName, Spaceship aShip){
	  // hämta planeten
	  Planet aPlanet = p.getGalaxy().findPlanet(aPlanetName);
	  // ta bort ev. gammal panel
	  if (showPlanetPanel != null){
		  Logger.finest("showPlanetShips: " + aPlanetName + " curNotes: " + showPlanetPanel.getPlanetNotes());
		  MiniPlanetPanel miniPlanetPanel = showPlanetPanel.getMiniPlanetPanel();
		  if (miniPlanetPanel != null){
			  // om en planet visas, spara notestext
			  miniPlanetPanel.saveNotesUpdateMap(false);
		  }
//		  p.getPlanetInfos().setNotes(showPlanetPanel.getPlanet().getName(),showPlanetPanel.getPlanetNotes());
		  remove(showPlanetPanel);
	  }
	  // isåfall skapa MapShowPlanet med den planeten
	  showPlanetPanel = new ShowPlanet(aPlanet,p,client,imageHandler);
	  showPlanetPanel.setBounds(872,27,326,643);
	  showPlanetPanel.showShip(aPlanet,aShip);
	  add(showPlanetPanel);
	  update(getGraphics());
  }

  public void showPlanetShips(String aPlanetName){
	  showShipOnPlanet(aPlanetName, null);
  }

  public MapCanvas getMap(){
    return map;
  }
  
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    g.setColor(StyleGuide.colorCurrent);
    //g.drawRoundRect(mapx-2,mapy-2,mapw+3,maph+3,5,5);
   
    g.drawLine(2, 669, 872, 669);
    g.drawLine(1, 27, 1, 669);
    //g.drawRoundRect(1,27,863,642,5,5);
  }

  public String getId(){
    return id;
  }

  public void updateData(){
	  controls.updateData();
    
	  if (showPlanetPanel != null){
		  showPlanetPanel.updateData();
	  }
  }

  public void updateTreasuryLabel(){
    turnInfoPanel.updateTreasuryLabel();
    if (currentPanel != null){
    	((SRUpdateablePanel)currentPanel).updateData();
    }
  }
  
  public void showUnreadMessage(boolean openPopup){
	  navBarPanel.showUnreadMessage(openPopup);
  }

  public void showShiptypeDetails(String aShipType, String faction){
	  navBarPanel.showShiptypeDetails(aShipType, faction);
  }

  public void showTroopTypeDetails(String aTroopType, String faction){
	  navBarPanel.showTroopTypeDetails(aTroopType, faction);
  }

  public void showVIPTypeDetails(String aVIPType, String faction){
	  navBarPanel.showVipTypeDetails(aVIPType, faction);
  }
  
  public void showBuildingTypeDetails(String aBiuldingType, String faction){
	  navBarPanel.showBuildingTypeDetails(aBiuldingType, faction);
  }

  public void showBattleSim(){
	  navBarPanel.showBattleSim();
  }

  public void showLandBattleSim(){
	  navBarPanel.showLandBattleSim();
  }

  public void updateMap(){
	  map.updatePcoors();
  }
  
  public void showSendButton(){
	  turnInfoPanel.enableSendBtn();
  }
  
  public void showNewMailImage(boolean show) {
	  turnInfoPanel.showNewMailImage(show);
	}

  public void addToBattleSim(String shipsString, String side){
	  navBarPanel.addToBattleSim(shipsString, side);
  }

  public void addToLandBattleSim(String troopsString, String side){
	  navBarPanel.addToLandBattleSim(troopsString, side);
  }
  
  public JPanel getCurrentPanel() {
	  return currentPanel;
  }

}
