//Title:        SpaceRaze Client
//Author:       Paul Bodin
//Description:  Denna klass innehåller knappar etc för att styra utseendet på kartan.

package sr.client;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;

import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.general.Functions;
import sr.world.Planet;
import sr.world.Player;
import sr.world.comparator.PlanetNameComparator;
import sr.world.comparator.PlayerNameComparator;

public class MapControls extends SRBasePanel implements ItemListener, MouseListener {
  private static final long serialVersionUID = 1L;
  int speed = 2,initialZoom = -6,sectorZoom = 4;
  SRLabel centerlbl,movelbl,turnlbl;
  ComboBoxPanel centerchoice,highlightChoice;
  SRButton rightbtn,leftbtn,upbtn,downbtn,zoominbtn,zoomoutbtn,resetbtn,cwbtn,anticwbtn,moverightbtn,moveleftbtn,moveupbtn,movedownbtn,sectorButton,showmapbtn;
  Player p;
  MapCanvas map;
  boolean once = true;
  boolean running = false, doChange = false;

  public MapControls(Player p, MapCanvas map) {
    this.p = p;
    this.map = map;

    // to correct x position of all components
    int x = -796;
    
    setLayout(null);
    //setBorder(new LineBorder(StyleGuide.colorCurrent));
    setBackground(StyleGuide.colorBackground);
    //setDoubleBuffered(true);

    centerlbl = new SRLabel("Center on:");
    centerlbl.setBounds(825+x,170,150,20);
    add(centerlbl);

    movelbl = new SRLabel("Move map:");
    movelbl.setBounds(827+x,25,70,20);
    add(movelbl);

    turnlbl = new SRLabel("Turn map:");
    turnlbl.setBounds(825+x,115,70,20);
    add(turnlbl);

    /*
    showmapbtn = new SRButton("Show Map");
    showmapbtn.setBounds(815+x,-60+y,80,35);
    showmapbtn.setVisible(true);
    showmapbtn.addMouseListener(this);
    add(showmapbtn);*/

    centerchoice = new ComboBoxPanel();
    centerchoice.setBounds(800+x,190,110,20);
    addPlanets(p.getGalaxy().getPlanets(),centerchoice);
    centerchoice.setVisible(true);
    centerchoice.addItemListener(this);
    add(centerchoice);

    moverightbtn = new SRButton("Right");
    moverightbtn.setBounds(860+x,65,40,20);
    moverightbtn.setVisible(true);
    moverightbtn.addMouseListener(this);
    add(moverightbtn);

    moveleftbtn = new SRButton("Left");
    moveleftbtn.setBounds(810+x,65,40,20);
    moveleftbtn.setVisible(true);
    moveleftbtn.addMouseListener(this);
    add(moveleftbtn);

    moveupbtn = new SRButton("Up");
    moveupbtn.setBounds(835+x,45,40,20);
    moveupbtn.setVisible(true);
    moveupbtn.addMouseListener(this);
    add(moveupbtn);

    movedownbtn = new SRButton("Down");
    movedownbtn.setBounds(835+x,85,40,20);
    movedownbtn.setVisible(true);
    movedownbtn.addMouseListener(this);
    add(movedownbtn);

    /*
    rightbtn = new SRButton("Right");
    rightbtn.setBounds(860+x,120+y,40,20);
    rightbtn.setVisible(true);
    rightbtn.addMouseListener(this);
    add(rightbtn);

    leftbtn = new SRButton("Left");
    leftbtn.setBounds(810+x,120+y,40,20);
    leftbtn.setVisible(true);
    leftbtn.addMouseListener(this);
    add(leftbtn);

    upbtn = new SRButton("Up");
    upbtn.setBounds(835+x,100+y,40,20);
    upbtn.setVisible(true);
    upbtn.addMouseListener(this);
    add(upbtn);

    downbtn = new SRButton("Down");
    downbtn.setBounds(835+x,140+y,40,20);
    downbtn.setVisible(true);
    downbtn.addMouseListener(this);
    add(downbtn);
    */

    zoominbtn = new SRButton("Zoom in");
    zoominbtn.setBounds(800+x,140,50,20);
    zoominbtn.setVisible(true);
    zoominbtn.addMouseListener(this);
    add(zoominbtn);

    zoomoutbtn = new SRButton("Zoom out");
    zoomoutbtn.setBounds(860+x,140,50,20);
    zoomoutbtn.setFont(new Font("Dialog",0,11));
    zoomoutbtn.setVisible(true);
    zoomoutbtn.addMouseListener(this);
    add(zoomoutbtn);

    resetbtn = new SRButton("Homeplanet");
    resetbtn.setBounds(815+x,230,80,20);
    resetbtn.setVisible(true);
    resetbtn.addMouseListener(this);
    add(resetbtn);

    sectorButton = new SRButton("Sector");
    sectorButton.setBounds(815+x,260,80,20);
    sectorButton.setVisible(true);
    sectorButton.addMouseListener(this);
    add(sectorButton);
/*
    cwbtn = new SRButton("Clockwise");
    cwbtn.setBounds(860+x,170,50,20);
    cwbtn.setFont(new Font("Dialog",0,10));
    cwbtn.setVisible(true);
    cwbtn.addMouseListener(this);
    add(cwbtn);

    anticwbtn = new SRButton("Anti cw");
    anticwbtn.setBounds(800+x,170,50,20);
    anticwbtn.setVisible(true);
    anticwbtn.addMouseListener(this);
    add(anticwbtn);*/

    centerlbl = new SRLabel("Highlight player:");
    centerlbl.setBounds(810+x,290,150,20);
    add(centerlbl);

    highlightChoice = new ComboBoxPanel();
    highlightChoice.setBounds(800+x,310,110,20);
    addPlayers(p.getGalaxy().getPlayers(),highlightChoice);
    highlightChoice.setVisible(true);
    highlightChoice.addItemListener(this);
    add(highlightChoice);

    centerchoice.setSelectedItem(p.getHomeplanet().getName());
  }

  public void itemStateChanged(ItemEvent ie){
	  if (ie.getSource() == centerchoice){
		  if (ie.getStateChange() == ItemEvent.SELECTED){
			  String cname = (String)centerchoice.getSelectedItem();
			  map.doSetCenter(cname);
			  map.doChange(0,0,initialZoom,0,0,0);
		  }
	  }else{ // must be highlightChoice
		  if (ie.getStateChange() == ItemEvent.SELECTED){
			  String pname = (String)highlightChoice.getSelectedItem();
			  map.doSetHighlight(pname);
//			  map.doChange(0,0,0,0,0,0);
			  map.repaint();
		  }
	  }
  }

  public void actionPerformed(SRButton aButton){
//System.out.println("action performed");
    if (aButton == moverightbtn){
      map.startchange(0,0,0,0,-2,0);
    }else
    if (aButton == moveleftbtn){
      map.startchange(0,0,0,0,2,0);
    }else
    if (aButton == moveupbtn){
      map.startchange(0,0,0,0,0,2);
    }else
    if (aButton == movedownbtn){
      map.startchange(0,0,0,0,0,-2);
    }else
    if (aButton.getText().equalsIgnoreCase("Right")){
      map.startchange(3,0,0,0,0,0);
    }else
    if (aButton.getText().equalsIgnoreCase("left")){
      map.startchange(-3,0,0,0,0,0);
    }else
    if (aButton.getText().equalsIgnoreCase("up")){
      map.startchange(0,-3,0,0,0,0);
    }else
    if (aButton.getText().equalsIgnoreCase("down")){
      map.startchange(0,3,0,0,0,0);
    }else
    if (aButton.getText().equalsIgnoreCase("zoom in")){
      map.startchange(0,0,-1,0,0,0);
    }else
    if (aButton.getText().equalsIgnoreCase("zoom out")){
      map.startchange(0,0,1,0,0,0);
    }else
    if (aButton.getText().equalsIgnoreCase("homeplanet")){
      doChange = false;
      map.doReset();
      map.doSetCenter(p.getHomeplanet().getName());
      map.doChange(0,0,initialZoom,0,0,0);
    }else
    if (aButton.getText().equalsIgnoreCase("sector")){
      doChange = false;
      map.doReset();
      map.doSetCenter("Sector origo");
      map.doChange(0,0,sectorZoom,0,0,0);
    }else
    if (aButton.getText().equalsIgnoreCase("clockwise")){
      map.startchange(0,0,0,-2,0,0);
    }else
    if (aButton.getText().equalsIgnoreCase("anti cw")){
      map.startchange(0,0,0,2,0,0);
    }/*else if (aButton.getText().equalsIgnoreCase("show map")){
      	mapPanel.hidePanels();
      	mapPanel.showMap();
      }*/
  }
  
  public void mouseClicked(MouseEvent me){
  }

  public void mouseExited(MouseEvent me){
    //map.doStopChange();
  }

  public void mouseEntered(MouseEvent me){
  }

  public void mousePressed(MouseEvent me){
    actionPerformed((SRButton)me.getComponent());
//    map.startchange(0,0,0,2,0,0);
  }

  public void mouseReleased(MouseEvent me){
    if (doChange){
      doChange = false;
    }else{
      map.doStopChange();
    }
  }

  private void addPlanets(List<Planet> pplanets, ComboBoxPanel thischoice){
    thischoice.addItem("Sector origo");
    List<Planet> sortedPlanets = Functions.cloneList(pplanets);
    Collections.sort(sortedPlanets, new PlanetNameComparator<Planet>());
    for (Planet planet : sortedPlanets) {
        thischoice.addItem(planet.getName());
	}
  }

  private void addPlayers(List<Player> players, ComboBoxPanel thischoice){
	    thischoice.addItem("None");
	    List<Player> sortedPlayers = Functions.cloneList(players);
	    Collections.sort(sortedPlayers, new PlayerNameComparator<Player>());
	    for (Player player : sortedPlayers) {
	        thischoice.addItem(player.getGovenorName());
		}
	  }

  public void updateData(){
    map.doSetCenter(p.getHomeplanet().getName());
/*    if (once){
      map.doSetCenter(p.getHomeplanet().getName());
      map.doChange(0,0,initialZoom,0,0,0);
      once = false;
    }*/
  }
}