//Title:        SpaceRaze Client
//Author:       Paul Bodin
//Description:  Java-klienten för SpazeRaze. Är en Javaapplet.

package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.components.CheckBoxPanel;
import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.scrollable.ListPanel;
import sr.client.components.scrollable.TextAreaPanel;
import sr.enums.SpaceshipRange;
import sr.general.logging.Logger;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.Troop;
import sr.world.VIP;
import sr.world.comparator.PlanetNameComparator;
import sr.world.comparator.VIPNameComparator;
import sr.world.orders.Orders;

@SuppressWarnings("serial")
public class MiniVIPPanel extends SRBasePanel implements ActionListener,ListSelectionListener{
  List<VIP> allVIPs;
  SRLabel typeLabel,locationLabel,abilitiesLabel,destinationLabel,killsLabel,alignmentLabel;
  private SRLabel upkeepLabel;
  ComboBoxPanel destinationChoice;
  TextAreaPanel abilitiesTextArea;
  private SRButton detailsButton;
  ListPanel allVIPlist;
  Player player;
  List<VIP> VIPsInList;
  List<Object> destinationsInChoice;
  VIP currentVIP;
  CheckBoxPanel selfDestructCheckBox;
  SpaceRazeApplet client;
  Planet planet;
//  JScrollPane scrollPane1,scrollPane2;

  public MiniVIPPanel(List<VIP> allVIPs, Player aPlayer, SpaceRazeApplet client, Planet planet) {
    this.allVIPs = allVIPs;
    Collections.sort(allVIPs,new VIPNameComparator<VIP>());
    this.player = aPlayer;
    this.client = client;
    this.planet = planet;
    setLayout(null);

    VIPsInList = new ArrayList<VIP>();
    destinationsInChoice = new ArrayList<Object>();

    allVIPlist = new ListPanel();
    allVIPlist.setBounds(5,5,315,100);
    allVIPlist.setListSelectionListener(this);
    add(allVIPlist);

    // add VIPs to list
    addVIPs();

    typeLabel = new SRLabel();
    typeLabel.setBounds(5,110,300,20);
    typeLabel.setToolTipText("Name of the VIP type.");
    add(typeLabel);

    locationLabel = new SRLabel();
    locationLabel.setBounds(5,130,300,20);
    locationLabel.setToolTipText("The VIPs location (planet, ship, troop");
    add(locationLabel);

    alignmentLabel = new SRLabel();
    alignmentLabel.setBounds(5,150,300,20);
    alignmentLabel.setToolTipText("The VIPs alignmnet, See Databank/Alignmnets for more info");
    add(alignmentLabel);

    killsLabel = new SRLabel();
    killsLabel.setBounds(5,170,200,20);
    killsLabel.setToolTipText("Killing VIPs gives experens and bigger chans to kill next time.");
    add(killsLabel);

    destinationLabel = new SRLabel();     // "Select new destination:"
    destinationLabel.setBounds(5,200,150,20);
    destinationLabel.setToolTipText("VIPs can go to planets, ships and troops to give differents bonus.");
    add(destinationLabel);

    destinationChoice = new ComboBoxPanel();
    destinationChoice.setBounds(5,220,315,20);
    destinationChoice.addActionListener(this);
    destinationChoice.setToolTipText("VIPs can go to planets, ships and troops to give differents bonus.");
    add(destinationChoice);
    destinationChoice.setVisible(false);
    
    upkeepLabel = new SRLabel();
    upkeepLabel.setBounds(5,250,200,15);
    upkeepLabel.setToolTipText("VIPs upkeep / turn");
    add(upkeepLabel);

    abilitiesLabel = new SRLabel();
    abilitiesLabel.setBounds(5,280,200,20);
    add(abilitiesLabel);

    abilitiesTextArea = new TextAreaPanel();
    abilitiesTextArea.setBounds(5,300,315,227);
    abilitiesTextArea.setVisible(false);
    add(abilitiesTextArea);
    
    selfDestructCheckBox = new CheckBoxPanel("Retire");
    selfDestructCheckBox.setBounds(5,532,200,20);
    selfDestructCheckBox.setSelected(false);
    selfDestructCheckBox.addActionListener(this);
    selfDestructCheckBox.setVisible(false);
    selfDestructCheckBox.setToolTipText("Somtimes it could be good to fire the VIP if you are short by money and the VIP have a upkeep cost.");
    add(selfDestructCheckBox);
    
    detailsButton = new SRButton("View Details");
    detailsButton.setBounds(220,532,100,18);
    detailsButton.addActionListener(this);
    detailsButton.setVisible(false);
    detailsButton.setToolTipText("Press this button to view more details about this VIP.");
    add(detailsButton);
    
  }

  private void addVIPs(){
    DefaultListModel dlm = (DefaultListModel)allVIPlist.getModel();
    for (int i = 0; i < allVIPs.size(); i++){
      VIP aVIP = allVIPs.get(i);
      if (aVIP.getBoss() == player){
        VIPsInList.add(aVIP);
        String tempDest = player.getVIPDestinationName(aVIP,true);
        if (!tempDest.equalsIgnoreCase("")){
        	dlm.addElement(aVIP.getName() + " (--> " + tempDest + ")");
        }else{
        	dlm.addElement(aVIP.getName());
        }
      }
    }
    allVIPlist.updateScrollList();
  }

  private void emptyList(){
    DefaultListModel dlm = (DefaultListModel)allVIPlist.getModel();
    dlm.removeAllElements();
  }

  public void actionPerformed(ActionEvent ae){
	  if (ae.getSource() instanceof ComboBoxPanel && (ComboBoxPanel)ae.getSource() == destinationChoice){
		  Logger.finer("destinationChoice.getSelectedIndex(): " + destinationChoice.getSelectedIndex());
		  newOrder(destinationChoice.getSelectedIndex());
	  }else if (ae.getSource() instanceof CheckBoxPanel){
			  newOrder((CheckBoxPanel)ae.getSource());
	  }else if (ae.getSource() instanceof SRButton){
	    	client.showVIPTypeDetails(currentVIP.getTypeName(), "All");
	  }
  }

  public void valueChanged(ListSelectionEvent lse){
    if (lse.getSource() instanceof ListPanel){
      showVIP(allVIPlist.getSelectedIndex());
      paintChildren(getGraphics());
      paintChildren(getGraphics()); // second paintchildren is needed to update scroller correctly... not beautiful, but works...
    }
  }
  
  
  private Object getDestinationsInChoice(String choice){
	  Object choicedObject = null;
	  for(Object destObject: destinationsInChoice){
		  if (destObject instanceof Planet){
	        if(((Planet)destObject).getName().equalsIgnoreCase(choice)){
	        	choicedObject = destObject;
	        }
	      }else
	      if (destObject instanceof Spaceship){
	    	  if(((Spaceship)destObject).getName().equalsIgnoreCase(choice)){
		        	choicedObject = destObject;
	    	  }
	      }else
	      if (destObject instanceof Troop){ // is Troop instance
	    	  if(((Troop)destObject).getUniqueName().equalsIgnoreCase(choice)){
		        	choicedObject = destObject;
	    	  }
	      }
	  }
	  return choicedObject;
  }


  private void newOrder(int destIndex){
	  
	  String choice = destinationChoice.getSelectedItem();
	  
    if (!(choice.equalsIgnoreCase("None") || choice.equalsIgnoreCase("------------------   Planets   ------------------") || choice.equalsIgnoreCase("------------------   Ships   ------------------") || choice.equalsIgnoreCase("------------------   Troops   ------------------"))){
    	Object destObject = getDestinationsInChoice(choice);
      if (destObject instanceof Planet){
        player.addVIPMove(currentVIP,(Planet)destObject);
      }else
      if (destObject instanceof Spaceship){
        player.addVIPMove(currentVIP,(Spaceship)destObject);
      }else
      if (destObject instanceof Troop){ // is Troop instance
    	  player.addVIPMove(currentVIP,(Troop)destObject);
      }
      // check if current vip is:
      // - a FTL master 
      // - he is on a ship 
      // - ship has short range
      // - the ship has a move order with long range
      // ==> remove that order
      if (currentVIP.isFTLbonus()){
    	  Spaceship shipLocation = currentVIP.getShipLocation();
    	  if (shipLocation != null){
    		  SpaceshipRange range = shipLocation.getSpaceshipType().getRange();
    		  if (range == SpaceshipRange.SHORT){
    			  Orders orders = player.getOrders();
    			  Planet shipDestination = orders.getDestination(shipLocation, player.getGalaxy());
    			  if (shipDestination != null){
    				  SpaceshipRange distance = currentVIP.getBoss().getGalaxy().getDistance(shipDestination,shipLocation.getLocation());
    				  if (distance == SpaceshipRange.LONG){
    					  // remove move order for ship
    					  orders.addNewShipMove(shipLocation,null);
    					  // a popup to the player about the removed move order should be displayed
    					  String title = "Ship move deleted";
    					  String message = "The ship " + shipLocation.getName() + " can no longer move long range, so it's move to " + shipDestination.getName() + " has been deleted.";
    					  Logger.finer("openMessagePopup called: " + message);
    					  GeneralMessagePopupPanel popup = new GeneralMessagePopupPanel(title,this,message);
    					  popup.setPopupSize(650,110);
    					  popup.open(this);
    				  }
    			  }
    		  }
    	  }
      }
    }else{ // "lägg till" tom order för att rensa bort eventuella gamla orders
      player.addVIPMove(currentVIP,null);
    }
    // update list
    emptyList();
    addVIPs();
	//paintComponent(getGraphics());
	//paintChildren(getGraphics());
    // uppdatera "left to spend" och send-knappen
    client.updateTreasuryLabel();
  }
  
  private void newOrder(CheckBoxPanel cb){
      if (cb == selfDestructCheckBox){
        if (cb.isSelected()){
        	player.getOrders().addVIPSelfDestruct(currentVIP);
        	destinationChoice.setSelectedIndex(0);
        	destinationChoice.setEnabled(false);
          
        }else{
        	destinationChoice.setEnabled(true);
        	player.getOrders().removeVIPSelfDestruct(currentVIP);
        }
        // update treasury label...
        client.updateTreasuryLabel();
      }
    }

  private void showVIP(int index){
    VIP tempVIP = VIPsInList.get(index);
    if (tempVIP != null){
      currentVIP = tempVIP;
      typeLabel.setText("Type: " + currentVIP.getName() + " (" + currentVIP.getShortName() + ")");
      alignmentLabel.setText("Alignment: " + currentVIP.getAlignmentString());
      String locStr = "";
      if (tempVIP.onPlanet()){
        locStr = tempVIP.getPlanetLocation().getName();
      }else
      if(tempVIP.onShip()){
        Spaceship tempss = tempVIP.getShipLocation();
        if (tempss.getLocation() != null){
//          locStr = tempss.getName() + " at " + tempss.getLocation().getName();
          locStr = tempss.getShortName();
        }else{
          locStr = tempss.getShortName() + " in deep space retreating.";
        }
      }else{ // must be on troop
    	  locStr = tempVIP.getTroopLocation().getUniqueName();
      }
      locationLabel.setText("Location: " + locStr); // add location. can be null...
      abilitiesLabel.setText("VIP abilities:");
      destinationLabel.setText("Select destination:");
      if (tempVIP.isAssassin() | tempVIP.isDuellist()){
        killsLabel.setText("Kills: " + tempVIP.getKills());
      }else{
        killsLabel.setText("");
      }
      destinationChoice.removeAllItems();
      destinationsInChoice.clear();
      addDestinations();
      String tempDest = player.getVIPDestinationName(currentVIP,true);
      Logger.fine("tempDest: " + tempDest);
      if (!tempDest.equalsIgnoreCase("")){
      	destinationChoice.setSelectedItem(tempDest);
      }else{
        destinationChoice.setSelectedItem("None");
      }
      if (player.isRetreatingGovenor()){
        destinationChoice.setEnabled(false);
      }else
      if (player.isBrokeClient()){
        destinationChoice.setEnabled(false);
      }else
      if (currentVIP.getPlanetLocation() != null){  // om vipen är på en planet...
        if (currentVIP.getPlanetLocation().isBesieged()){  // och planeten är belägrad...
          if (!currentVIP.getCanVisitEnemyPlanets()){  // vipen är varken spion eller assassin...
            destinationChoice.setEnabled(false);  // vipen får ej flytta sig
          }else{
            destinationChoice.setEnabled(true);
          }
        }else{
          destinationChoice.setEnabled(true);
        }
      }else{
        destinationChoice.setEnabled(true);
      }
      destinationChoice.setVisible(true);
      upkeepLabel.setText("Upkeep: " + currentVIP.getUpkeep());
      
      abilitiesTextArea.setText("");
      List<String> allStrings = currentVIP.getAbilitiesStrings();
      for (String string : allStrings) {
    	  abilitiesTextArea.append(string + "\n");
      }
      abilitiesTextArea.setVisible(true);
//      scrollPane2.setVisible(true);   
      
//    show and set selfdestruct cb
      if(currentVIP.isGovernor()){
    	  selfDestructCheckBox.setVisible(false);
      }else{
    	  selfDestructCheckBox.setSelected(player.getOrders().getVIPSelfDestruct(currentVIP));
    	  if(player.getOrders().getVIPSelfDestruct(currentVIP)){
    		  destinationChoice.setEnabled(false);
    	  }
          selfDestructCheckBox.setVisible(true);
      }
      detailsButton.setVisible(true);
    }
    repaint();
  }

  private void addDestinations(){
    destinationChoice.addItem("None");
    Spaceship currentShip = currentVIP.getShipLocation();
    Planet currentPlanet = currentVIP.getPlanetLocation();
    // planet moves
    // vip is on planet, can move to other planets
    boolean addPlanetInfoText=true;
    boolean addShipInfoText=true;
    boolean addTroopInfoText=true;
    if (currentPlanet != null){
    	
        List<Planet> allDest = player.getGalaxy().getAllDestinations(currentPlanet,true);
        Collections.sort(allDest,new PlanetNameComparator<Planet>());
        for (int i = 0; i < allDest.size(); i++){
        	Planet tempPlanet = allDest.get(i);
        	if (canMoveToPlanet(tempPlanet,false,currentPlanet)){
        		if(addPlanetInfoText){
        			destinationChoice.addItem("------------------   Planets   ------------------");
        			addPlanetInfoText = false;
        		}
        		destinationChoice.addItem(tempPlanet.getName());
        		destinationsInChoice.add(tempPlanet);
        	}
        }
    }else{
    	// vip is on ship/troop, can move to planet?
    	Planet tmpLocation = currentVIP.getLocation();
        if (canMoveToPlanet(tmpLocation,true,currentPlanet)){ // vipen får flytta till planeten
        	if(addPlanetInfoText){
    			destinationChoice.addItem("------------------   Planets   ------------------");
    			addPlanetInfoText = false;
    		}
            destinationChoice.addItem(tmpLocation.getName());
            destinationsInChoice.add(tmpLocation);
        }
    }
    // add all ships (except maybe the one vip already is on)
    List<Spaceship> allShips = player.getGalaxy().getSpaceships();
    for (int i = 0; i < allShips.size(); i++){
    	Spaceship tempss = allShips.get(i);
    	// get planet location
    	Planet tmpLocation = currentVIP.getLocation();
    	if ((tempss.getLocation() == tmpLocation) & (tempss.getOwner() == player) & (tempss != currentShip)){
    		if(addShipInfoText){
    			destinationChoice.addItem("------------------   Ships   ------------------");
    			addShipInfoText = false;
    		}
    		destinationChoice.addItem(tempss.getName());
    		destinationsInChoice.add(tempss);
    	}
    }    
    // add all troops (except maybe the one vip already is on)
    List<Troop> troops = player.getGalaxy().getPlayersTroopsOnPlanet(player, planet);
    for (Troop aTroop : troops) {
		if (currentVIP.getTroopLocation() != aTroop && currentVIP.isTroopVIP()){
			if(addTroopInfoText){
    			destinationChoice.addItem("------------------   Troops   ------------------");
    			addTroopInfoText = false;
    		}
			destinationChoice.addItem(aTroop.getUniqueName());
			destinationsInChoice.add(aTroop);
		}
	}
  }

  // kolla om currentVIP kan flytta till denna planet
  private boolean canMoveToPlanet(Planet aPlanet, boolean checkNeutral, Planet originPlanet){
    boolean ok = false;
    if(currentVIP.canVisitEnemyPlanets()){ // om VIPen kan besöka andra planeter än ens egna
        ok = true;
    }else // kolla om planeten är egen
    if (aPlanet.getPlayerInControl() == player){
    	// om man står på en nuetral planet och ej kan besöka fiendeplaneter: då måste vippen ha canVisitNeutralPlanets och kan ej flytta direkt till en egen planet
    	if (((originPlanet != null) && (originPlanet.getPlayerInControl() == null))){
    		ok = false;
    	}else // can not move to besieged planets if not canVisitEnemyPlanets() 
    	if (((originPlanet != null) && (originPlanet.getPlayerInControl() == player)) & aPlanet.isBesieged()){
    		ok = false;
    	}else
    	if (!(checkNeutral & player.getOrders().getAbandonPlanet(aPlanet))){ // kan bara flytta från skepp (eller trupp) till egen planet om det ej finns ej abandon-order
    		ok = true;
    	}
    }else
    if (checkNeutral){
      if(currentVIP.canVisitNeutralPlanets() & aPlanet.getPlayerInControl() == null){ // om VIPen kan besöka neutrala planeter är ens egna
      	if (!aPlanet.isRazed()){ // razed planets are not counted as neutral
      		Logger.finer("Planet is not razed");
      		ok = true;
      	}
      }
    }
    return ok;
  }

  public void updateData(){
    if (currentVIP != null){
      showVIP(allVIPlist.getSelectedIndex());
    }
  }
  
}
