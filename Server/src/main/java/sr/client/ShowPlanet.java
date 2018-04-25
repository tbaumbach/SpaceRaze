package sr.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sr.client.color.ColorConverter;
import sr.client.components.SRBasePanel;
import sr.client.components.SRTabbedPane;
import sr.client.components.SRTabbedPaneUI;
import sr.general.logging.Logger;
import sr.world.Faction;
import sr.world.Planet;
import sr.world.PlanetInfos;
import sr.world.Player;
import sr.world.Spaceship;

/**
 * Base panel for the right part of the game gui, which can show 
 * the mini planet, ship, vip or wharf panels.
 * @author wmpabod
 *
 */
public class ShowPlanet extends SRBasePanel implements ChangeListener{
  private static final long serialVersionUID = 1L;
  private Planet aPlanet;
  private Player aPlayer;
  private String planetName;
  private MiniPlanetPanel miniPlanetPanel;
  private MiniShipPanel miniShipPanel;
  private MiniVIPPanel miniVIPPanel;
  private MiniBuildingPanel miniBuildingPanel;
  private MiniTroopPanel miniTroopPanel;
  private SpaceRazeApplet client;
  private Image planetNormal,planetRazed;
  private final int MINI_PANEL_HEIGHT = 552;//470
  private final int MINI_PANEL_WIDTH = 322;
  private SRTabbedPane tabbedPanel;

  public ShowPlanet(Planet aPlanet, Player aPlayer, SpaceRazeApplet client, ImageHandler imageHandler) {
    this.aPlanet = aPlanet;
    this.aPlayer = aPlayer;
    planetName = aPlanet.getName();
    this.client = client;
    setLayout(null);
    setOpaque(true);
    setBackground(StyleGuide.colorBackground);
    setBorder(new LineBorder(StyleGuide.colorCurrent));
    planetNormal = imageHandler.getImage("planetnormal");
    planetRazed = imageHandler.getImage("planetrazed");
    showPlanet();
  }

  public void showShip(Planet aPlanet, Spaceship aShip){
	  removOldMiniPanel();
	  if (aPlayer.getGalaxy().isPlayerShipAtPlanet(aPlayer,aPlanet)){
		  miniShipPanel = new MiniShipPanel(aPlayer.getGalaxy().getPlayersSpaceshipsOnPlanet(aPlayer,aPlanet),aPlayer,client,aPlanet);
		  miniShipPanel.setBounds(1,89,MINI_PANEL_WIDTH,MINI_PANEL_HEIGHT);
		  if (aShip != null){
			  miniShipPanel.showSpaceship(aShip);
		  }
		  add(miniShipPanel);
		  SelectTabb("Ships");
	  }
  }

  public void showShips(Planet aPlanet){
	  showShip(aPlanet,null);
  }
  
  public void SelectTabb(String panelid){
	  tabbedPanel.removeChangeListener(this);
	  for(int i= 0; i < tabbedPanel.getTabCount(); i++){
		  if(tabbedPanel.getTitleAt(i).equals(panelid)){
			  tabbedPanel.setSelectedIndex(i);
		  }
	  }
	  tabbedPanel.addChangeListener(this);
  }

  private void showPlanet() {
//	int width = Math.round((326)/5)-3;    
    
    tabbedPanel = new SRTabbedPane("noBorders");
    SRTabbedPaneUI tpui = new SRTabbedPaneUI();
    tabbedPanel.setUI(tpui);
    tabbedPanel.setFont(StyleGuide.buttonFont);
    
    tabbedPanel.setBackground(StyleGuide.colorCurrent.darker().darker().darker().darker());
    tabbedPanel.setForeground(StyleGuide.colorCurrent);
    
    // create all panels
    
    int panelIndex = 0;
    
    tabbedPanel.addTab("Planet", tempPanel("Planet"));
    tabbedPanel.setToolTipTextAt(panelIndex, "Planet");
    panelIndex++;
    
    if(aPlanet.isPlanetOwner(aPlayer) && !aPlanet.isRazedAndUninfected() && !(aPlayer.isDefeated() | aPlayer.getGalaxy().isGameOver())){
	    tabbedPanel.addTab("Buildings", tempPanel("Buildings"));
	    tabbedPanel.setToolTipTextAt(panelIndex, "Buildings");
	    panelIndex++;
    }
    
    if(aPlayer.getGalaxy().isPlayerShipAtPlanet(aPlayer,aPlanet) && !(aPlayer.isDefeated() | aPlayer.getGalaxy().isGameOver())){
	    tabbedPanel.addTab("Ships", tempPanel("Ships"));
	    tabbedPanel.setToolTipTextAt(panelIndex, "Ships");
	    panelIndex++;
    }
    
    if(aPlayer.getGalaxy().isPlayerTroopsAtPlanet(aPlayer,aPlanet) && !(aPlayer.isDefeated() | aPlayer.getGalaxy().isGameOver())){
	    tabbedPanel.addTab("Troops", tempPanel("Troops"));
	    tabbedPanel.setToolTipTextAt(panelIndex, "Troops");
	    panelIndex++;
    }
    
    if(aPlayer.getGalaxy().isPlayerVIPAtPlanet(aPlayer,aPlanet) && !(aPlayer.isDefeated() | aPlayer.getGalaxy().isGameOver())){
	    tabbedPanel.addTab("VIPs", tempPanel("VIPs"));
	    tabbedPanel.setToolTipTextAt(panelIndex, "VIPs");
	    panelIndex++;
    }
    
    tabbedPanel.setBounds(2,66,323,24);
    tabbedPanel.addChangeListener(this);
    add(tabbedPanel);

    Logger.fine("Create new MiniPlanetPanel");
    miniPlanetPanel = new MiniPlanetPanel(aPlayer,client,aPlanet);
    miniPlanetPanel.setBounds(1,89,MINI_PANEL_WIDTH,MINI_PANEL_HEIGHT);
    add(miniPlanetPanel);
    
  }

  public void paint(Graphics g){
    super.paint(g);
    Dimension d = getSize();
    int textX = 66;
    Image buffer = createImage(d.width-2, d.height-2);
    Graphics bg = buffer.getGraphics();
    bg.setColor(Color.black);
    bg.fillRect(0,0,d.width,d.height);
    // draw planet
    boolean spy = aPlayer.getGalaxy().findVIPSpy(aPlanet,aPlayer) != null;;
    boolean shipInSystem = aPlayer.getGalaxy().playerHasShipsInSystem(aPlayer,aPlanet);
    boolean lastKnownRazed = aPlayer.getPlanetInfos().getLastKnownRazed(aPlanet.getName());
    boolean surveyShip = (aPlayer.getGalaxy().findSurveyShip(aPlanet,aPlayer) != null);
    boolean surveyVIP = (aPlayer.getGalaxy().findSurveyVIPonShip(aPlanet,aPlayer) != null);
    boolean razed = aPlanet.isRazed() & (aPlanet.getPlayerInControl() == null);
    // if player is present in system or was it razed at the last visit
	if ((razed & (shipInSystem | spy)) | lastKnownRazed){
    	bg.drawImage(planetRazed,1,1,client);
		bg.setColor(new Color(255,63,63));
		bg.drawLine(2,2,62,62);
		bg.drawLine(3,2,62,61);
		bg.drawLine(2,3,61,62);
		bg.drawLine(2,62,62,2);
		bg.drawLine(3,62,62,3);
		bg.drawLine(2,61,61,2);
	}else{
    	bg.drawImage(planetNormal,1,1,client);
    }
    // draw name
    bg.setFont(new Font("Helvetica",1,14));
    bg.setColor(new Color(41,198,255));
    bg.drawString(planetName,textX,16);
    if (aPlanet.isOpen()){
      // rita ut rosa kors efter namnet
      FontMetrics fm = bg.getFontMetrics();
      int strWidth = fm.stringWidth(planetName);
      bg.setColor(new Color(255,24,247));
      bg.drawLine(textX+strWidth+4,6,textX+strWidth+4,14);
      bg.drawLine(textX+strWidth+1,10,textX+strWidth+7,10);
      bg.fillRect(textX+strWidth+3,9,3,3);
    }
    bg.setFont(new Font("Helvetica",0,12));
	bg.setColor(new Color(41,198,255));
	PlanetInfos pi = aPlayer.getPlanetInfos();
	int prod = pi.getLastKnownProd(aPlanet.getName());
	int res = pi.getLastKnownRes(aPlanet.getName());
    if ((aPlanet.isOpen()) | (aPlanet.getPlayerInControl() == aPlayer) | spy | surveyShip | surveyVIP){
    	// always show correct values
        if (razed){
        	bg.drawString("- / -",textX,35);
        }else{
        	if (aPlanet.getPopulation() > 0){
        		bg.drawString(aPlanet.getPopulation() + " / " + aPlanet.getResistance(),textX,35);
        	}else{ // planet is infested with aliens
        		bg.drawString("- / " + aPlanet.getResistance(),textX,35);
        	}
        }
    }else
    if (shipInSystem){                         
    	// if player has ships in system he knows if it is razed, otherwise show last known values 
        if (razed){
    		bg.drawString("- / -",textX,35);
        }else{
    		if ((prod == -1) & (res == -1)){
    			bg.drawString("? / ?",textX,35);
    		}else{
    			bg.drawString(prod + "? / " + res + "?",textX,35);
    		}
        }
    }else{
        if (lastKnownRazed){
        	bg.drawString("-? / -?",textX,35);
        }else{
    		if ((prod == -1) & (res == -1)){
    			bg.drawString("? / ?",textX,35);
    		}else{
    			if (prod == 0){ // must have been an alien planet
    				bg.drawString("-? / " + res + "?",textX,35);
    			}else{
    				bg.drawString(prod + "? / " + res + "?",textX,35);
    			}
    		}
        }
    }
    // Rita ut ring om planeten är belägrad 
    if ((aPlayer.getGalaxy().isPlayerShipAtPlanet(aPlayer,aPlanet)) | (aPlanet.getPlayerInControl() == aPlayer) | (spy)){
      if (aPlanet.isBesieged()){
        bg.setColor(new Color(255,191,63));
        bg.drawOval(6,6,54,54);
      }
    }
	// print owner / razed status
    if (aPlanet.getPlayerInControl() == aPlayer){
        bg.setColor(ColorConverter.getColorFromHexString(aPlayer.getFaction().getPlanetHexColor()));
        bg.drawString("Planet is under your control",textX,54);
    }else
    if (aPlanet.isOpen() | spy | shipInSystem){
        if(aPlanet.isRazed() & (aPlanet.getPlayerInControl() == null)){
    		bg.setColor(StyleGuide.colorNeutralWhite);
    		bg.drawString("Planet is Razed",textX,54);
        }else{
            if (aPlanet.getPlayerInControl() == null){
                // neutral planet
                bg.setColor(StyleGuide.colorNeutralWhite);
                bg.drawString("Neutral",textX,54);
            }else{
            	String tmpOwner = aPlanet.getPlayerInControl().getFaction().getName();
            	tmpOwner = tmpOwner + " (" + aPlanet.getPlayerInControl().getGovenorName() + ")";
            	// planet belonging to other player
            	bg.setColor(ColorConverter.getColorFromHexString(aPlanet.getPlayerInControl().getFaction().getPlanetHexColor()));
            	bg.drawString(tmpOwner,textX,54);
            }
        }
    }else
    if(lastKnownRazed){
		bg.setColor(StyleGuide.colorNeutralWhite);
		bg.drawString("Planet is Razed?",textX,54);
    }else{
        String lastKnownOwner = pi.getLastKnownOwner(aPlanet.getName());
        if (lastKnownOwner.equalsIgnoreCase("Neutral")){
          bg.setColor(StyleGuide.colorNeutralWhite);
          bg.drawString("Neutral? (info from turn " + pi.getLastInfoTurn(aPlanet.getName()) +  ")",textX,54);
        }else{
        	Faction lastKnownFaction = aPlayer.getGalaxy().findPlayerFaction(lastKnownOwner);
//          bg.setColor(aPlanet.getPlayerInControl().getFaction().getPlanetColor());
          bg.setColor(ColorConverter.getColorFromHexString(lastKnownFaction.getPlanetHexColor()));
          bg.drawString(aPlayer.getGalaxy().findPlayerFaction(pi.getLastKnownOwner(aPlanet.getName())).getName() + "? (info from turn " + pi.getLastInfoTurn(aPlanet.getName()) +  ")",textX,54);
        }
    }
    // draw buffer
    g.drawImage(buffer,1,1,this);
    paintChildren(g);
  }

  private void removOldMiniPanel(){
	  Logger.fine("removOldMiniPanel");
	  if (miniPlanetPanel != null){
		  // first save any notes
		  Logger.fine("Saving Notes");
		  miniPlanetPanel.saveNotesUpdateMap(false);
		  //      String notes = miniPlanetPanel.getNotes();
		  //      Logger.finest("Notes: " + notes);
		  //      aPlayer.getPlanetInfos().setNotes(aPlanet.getName(),notes);
		  // remove panel
		  remove(miniPlanetPanel);
		  miniPlanetPanel = null;
	  }else
	  if (miniShipPanel != null){
		  remove(miniShipPanel);
		  miniShipPanel = null;
	  }else
      if (miniTroopPanel != null){
    	  remove(miniTroopPanel);
    	  miniTroopPanel = null;
      }else
      if (miniVIPPanel != null){
    	  remove(miniVIPPanel);
    	  miniVIPPanel = null;
      }else
      if (miniBuildingPanel != null){
    	  remove(miniBuildingPanel);
    	  miniBuildingPanel = null;
      }
  }
  
  public void stateChanged(ChangeEvent e) {
	  Logger.finer("stateChanged: " + tabbedPanel.getSelectedComponent().getName());
	  if(tabbedPanel.getSelectedComponent() != null){
		  removOldMiniPanel();
		if (tabbedPanel.getSelectedComponent().getName().equalsIgnoreCase("planet")){
	      miniPlanetPanel = new MiniPlanetPanel(aPlayer,client,aPlanet);
	      miniPlanetPanel.setBounds(1,89,MINI_PANEL_WIDTH,MINI_PANEL_HEIGHT);
	      add(miniPlanetPanel);
	    }else
	    if (tabbedPanel.getSelectedComponent().getName().equalsIgnoreCase("ships")){
	      miniShipPanel = new MiniShipPanel(aPlayer.getGalaxy().getPlayersSpaceshipsOnPlanet(aPlayer,aPlanet),aPlayer,client,aPlanet);
	      miniShipPanel.setBounds(1,89,MINI_PANEL_WIDTH,MINI_PANEL_HEIGHT);
	      add(miniShipPanel);
	    }else
	    if (tabbedPanel.getSelectedComponent().getName().equalsIgnoreCase("troops")){
	    	miniTroopPanel = new MiniTroopPanel(aPlayer.getGalaxy().getPlayersTroopsOnPlanet(aPlayer,aPlanet),aPlayer,client,aPlanet);
	    	miniTroopPanel.setBounds(1,89,MINI_PANEL_WIDTH,MINI_PANEL_HEIGHT);
	    	add(miniTroopPanel);
	    }else
	    if (tabbedPanel.getSelectedComponent().getName().equalsIgnoreCase("vips")){
	      miniVIPPanel = new MiniVIPPanel(aPlayer.getGalaxy().findPlayersVIPsOnPlanetOrShipsOrTroops(aPlanet,aPlayer),aPlayer,client, aPlanet);
	      miniVIPPanel.setBounds(1,89,MINI_PANEL_WIDTH,MINI_PANEL_HEIGHT);
	      add(miniVIPPanel);
	    }else
	    if (tabbedPanel.getSelectedComponent().getName().equalsIgnoreCase("buildings")){
	    	miniBuildingPanel = new MiniBuildingPanel(aPlanet,aPlayer,client);
	    	miniBuildingPanel.setBounds(1,89,MINI_PANEL_WIDTH,MINI_PANEL_HEIGHT);
	    	add(miniBuildingPanel);
	    }
		update(getGraphics());
	  }
	    
  }
  
  public void updateData(){
	  Logger.fine("updateData()");
	  if (miniPlanetPanel != null){
		  miniPlanetPanel.updateData();
	  }else
	  if (miniShipPanel != null){
		  miniShipPanel.updateData();
	  }else
	  if (miniVIPPanel != null){
		  miniVIPPanel.updateData();
	  }else
	  if (miniBuildingPanel != null){
		  miniBuildingPanel.updateData();
	  }
  }

  public String getPlanetNotes(){
	  String retVal = null;
	  if (miniPlanetPanel != null){
		  retVal = miniPlanetPanel.getNotes();
	  }else{
		  retVal = aPlayer.getPlanetInfos().getNotes(planetName);
	  }
	  return retVal;
  }
  
  /** 
   * Return null if not miniplanetpanel is shown
   * 
   * @return MiniPlanelPanel if it is currently shown
   */
  public MiniPlanetPanel getMiniPlanetPanel(){ 
	  MiniPlanetPanel mpp = null;
	  if (miniPlanetPanel != null){
		  mpp = miniPlanetPanel;
	  }
	  return mpp;
  }
  
  public Planet getPlanet(){
	  return aPlanet;
  }
  
  private SRBasePanel tempPanel(String name){
	  SRBasePanel tempPanel = new SRBasePanel();
	  tempPanel.setName(name);
	  return tempPanel;
  }
}