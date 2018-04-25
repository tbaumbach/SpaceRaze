//Title:        SpaceRaze Client
//Author:       Paul Bodin
//Description:  Java-klienten för SpazeRaze. Är en Javaapplet.

package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

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
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.Building;
import sr.world.BuildingType;
import sr.world.Planet;
import sr.world.Player;
import sr.world.SpaceshipType;
import sr.world.TroopType;
import sr.world.VIP;
import sr.world.VIPType;
import sr.world.comparator.BuildingTypeBuildCostAndNameComparator;
import sr.world.comparator.VIPTypeComparator;
import sr.world.comparator.spaceshiptype.SpaceshipTypeSizeComparator;
import sr.world.comparator.trooptype.TroopTypeComparator;
import sr.world.orders.Orders;

public class MiniBuildingPanel extends SRBasePanel implements ActionListener,ListSelectionListener {
  private static final long serialVersionUID = 1L;
  List<Building> allBuildings = new ArrayList<Building>();
  ListPanel buildingList;
  SRLabel maxTonnageLabel, statusLabel, nameLabel, locationLabel, buildTypeLabel, slotsLabel, abilitiesLabel, currentBuildBuildingLabel,  newBuildBuildingLabel;
  //  sizeLabel,
  ComboBoxPanel[] shipTypeChoice = new ComboBoxPanel[5];
  ComboBoxPanel[] troopTypeChoice = new ComboBoxPanel[5];
  ComboBoxPanel[] VIPTypeChoice = new ComboBoxPanel[1];
  ComboBoxPanel[] upgradeBuildingTypeChoice = new ComboBoxPanel[1];
  private ComboBoxPanel buildnewBuildingChoice;
  private SRButton detailsButton, detailsNewBuildingChoice, detailsVIP, detailsUpgrade;
  private SRButton[] buttonsShipsUpgrade, buttonsTroopsUpgrade;
  Building currentBuilding = null;
  Player player;
  SpaceRazeApplet client;
  CheckBoxPanel selfDestructCheckBox;
  TextAreaPanel abilitiesTextArea;
  int x = -215;
  private boolean cleaning = false;
  private boolean noaction = false;
  private Planet aPlanet;

  public MiniBuildingPanel(Planet aPlanet, Player player, SpaceRazeApplet client) {
      this.player = player;
      this.aPlanet =aPlanet;
      this.allBuildings = aPlanet.getBuildings();
      this.setLayout(null);
      this.client = client;
      
      
      newBuildBuildingLabel = new SRLabel("Build new building:");
      newBuildBuildingLabel.setBounds(5,5,200,18);
      newBuildBuildingLabel.setToolTipText("Choose a new building to build.");
      //BuildBuildingLabel.setFont(new Font("Dialog",0,10));
      add(newBuildBuildingLabel);

      
      buildnewBuildingChoice = new ComboBoxPanel();
      buildnewBuildingChoice.setBounds(5,23,294,20);
      // ej kunna bygga ny vid abandon, belägring och fiendetrupper
      boolean enemyTroopsOnPlanet = player.getGalaxy().findOtherTroopsPlayersOnRazedPlanet(player, aPlanet).size() > 0;
      boolean underSiege = aPlanet.isBesieged();
      boolean abandonPlanet = player.getOrders().getAbandonPlanet(aPlanet);
      if (enemyTroopsOnPlanet | underSiege | abandonPlanet){
          buildnewBuildingChoice.setEnabled(false);
      }else{
    	  fillnewBuildingsChoice();
    	  buildnewBuildingChoice.addActionListener(this);
      }
      buildnewBuildingChoice.setToolTipText("Choose a new building to build.");
      this.add(buildnewBuildingChoice);
      
      detailsNewBuildingChoice = new SRButton("?");
      detailsNewBuildingChoice.setBounds(301,23,20,20);
      detailsNewBuildingChoice.addActionListener(this);
      detailsNewBuildingChoice.setVisible(false);
      detailsNewBuildingChoice.setToolTipText("Click for more details information about the building");
      add(detailsNewBuildingChoice);
     
      
      currentBuildBuildingLabel = new SRLabel("Buildings on planet:");
      currentBuildBuildingLabel.setBounds(5,55,200,18);
      currentBuildBuildingLabel.setToolTipText("Mark a building to build units or upgrade");
      //BuildBuildingLabel.setFont(new Font("Dialog",0,10));
      add(currentBuildBuildingLabel);

      buildingList = new ListPanel();
      buildingList.setBounds(5,75,315,140);
      buildingList.setListSelectionListener(this);
      buildingList.setToolTipText("Mark a building to build units or upgrade");
      add(buildingList);

      DefaultListModel dlm = (DefaultListModel)buildingList.getModel();
      // TODO (Tobbe)  hur löser vi detta?   sortering av ArrayList
      //Collections.sort(allBuildings);
      for(int i = 0; i < allBuildings.size(); i++){
        dlm.addElement(((Building)allBuildings.get(i)).getUniqueName());
        //dlm.addElement(((Building)allBuildings.get(i)).getBuildingType().getShortName());
      }
      buildingList.updateScrollList();

      nameLabel = new SRLabel();
      nameLabel.setBounds(220+x,220,200,20);
      nameLabel.setToolTipText("Building name");
      add(nameLabel);

      statusLabel = new SRLabel();
      statusLabel.setBounds(220+x,240,310,20);
      add(statusLabel);

      buildTypeLabel = new SRLabel();
      buildTypeLabel.setBounds(220+x,260,315,20);
      add(buildTypeLabel);

      for (int i = 0; i < shipTypeChoice.length; i++){
        shipTypeChoice[i] = new ComboBoxPanel();
        shipTypeChoice[i].setBounds(220+x,280 + (i*20),294,20);
        shipTypeChoice[i].addActionListener(this);
        if(i==0){
        	shipTypeChoice[i].setToolTipText("Choose to upgrade or build a new ship");
        }else{
        	shipTypeChoice[i].setToolTipText("Choose a ship to build");
        }
        add(shipTypeChoice[i]);
      }
      
      clearShipTypeChoices();
      
      for (int i = 0; i < troopTypeChoice.length; i++){
    	  troopTypeChoice[i] = new ComboBoxPanel();
    	  troopTypeChoice[i].setBounds(220+x,280 + (i*20),294,20);
    	  troopTypeChoice[i].addActionListener(this);
    	  if(i==0){
    		  troopTypeChoice[i].setToolTipText("Choose to upgrade or build a new troop unit");
          }else{
        	  troopTypeChoice[i].setToolTipText("Choose a troop unit to build");
          }
    	  add(troopTypeChoice[i]);
	  }
      clearTroopTypeChoices();
        
      for (int i = 0; i < VIPTypeChoice.length; i++){
    	  VIPTypeChoice[i] = new ComboBoxPanel();
    	  VIPTypeChoice[i].setBounds(220+x,280 + (i*20),294,20);
    	  VIPTypeChoice[i].addActionListener(this);
    	  VIPTypeChoice[i].setToolTipText("Choose to upgrade or recruit a new VIP");
    	  add(VIPTypeChoice[i]);
      }
      clearVIPTypeChoices();
      
      for (int i = 0; i < upgradeBuildingTypeChoice.length; i++){
    	  upgradeBuildingTypeChoice[i] = new ComboBoxPanel();
    	  upgradeBuildingTypeChoice[i].setBounds(220+x,280 + (i*20),294,20);
    	  upgradeBuildingTypeChoice[i].addActionListener(this);
    	  VIPTypeChoice[i].setToolTipText("Choose to upgrade this building");
    	  add(upgradeBuildingTypeChoice[i]);
      }
      
      clearUpgradeBuildingTypeChoice();
      
      detailsVIP = new SRButton("?");
      detailsVIP.setBounds(301,280,20,20);
      detailsVIP.addActionListener(this);
      detailsVIP.setVisible(false);
      detailsVIP.setToolTipText("Click for more details information about the VIP");
      add(detailsVIP);
      
      detailsUpgrade = new SRButton("?");
      detailsUpgrade.setBounds(301,280,20,20);
      detailsUpgrade.addActionListener(this);
      detailsUpgrade.setVisible(false);
      detailsUpgrade.setToolTipText("Click for more details information about the building");
      add(detailsUpgrade);
      
      abilitiesLabel = new SRLabel();
      abilitiesLabel.setBounds(220+x,380,200,20);
      abilitiesLabel.setToolTipText("The buildings abilities");
      add(abilitiesLabel);
      
      abilitiesTextArea = new TextAreaPanel();
      abilitiesTextArea.setBounds(220+x,400,315,125);
      abilitiesTextArea.setVisible(false);
      abilitiesTextArea.setToolTipText("The buildings abilities");
      add(abilitiesTextArea);

      selfDestructCheckBox = new CheckBoxPanel("Selfdestruct");
      selfDestructCheckBox.setBounds(220+x,532,200,20);
      selfDestructCheckBox.setSelected(false);
      selfDestructCheckBox.addActionListener(this);
      selfDestructCheckBox.setVisible(false);
      selfDestructCheckBox.setToolTipText("Check this to destroy this building");
      add(selfDestructCheckBox);
      
      detailsButton = new SRButton("View Details");
      detailsButton.setBounds(435+x,532,100,18);
      detailsButton.addActionListener(this);
      detailsButton.setVisible(false);
      detailsButton.setToolTipText("Click for more details information about the building");
      add(detailsButton);
      
      Orders o = player.getOrders();
      if (o.getNewBuilding(aPlanet) != null){
  		BuildingType buildingType = o.getNewBuilding(aPlanet);
  		VIP tempEngineer = player.getGalaxy().findVIPBuildingBuildBonus(aPlanet,player,player.getOrders());
  		int cost = buildingType.getBuildCost(tempEngineer);
  		buildnewBuildingChoice.setSelectedItem(buildingType.getName() + " (cost: " + cost + ")");
  		detailsNewBuildingChoice.setVisible(true);
  		//buildnewBuildingChoice.setVisible(true);
      }

  }

	private void clearShipTypeChoices(){
		for(int i =shipTypeChoice.length-1;i >= 0; i--){
			shipTypeChoice[i].setVisible(false);
			cleaning = true;
			shipTypeChoice[i].removeAllItems();
			cleaning = false;
			if(buttonsShipsUpgrade != null && i < buttonsShipsUpgrade.length){
				remove(buttonsShipsUpgrade[i]);
			}
			
		}
		buttonsShipsUpgrade = null;
	}
    
    private void clearTroopTypeChoices(){
    	for (int i = 0; i < troopTypeChoice.length; i++){
    	  troopTypeChoice[i].setVisible(false);
    	  cleaning = true;
    	  troopTypeChoice[i].removeAllItems();
    	  cleaning = false;
    	  if(buttonsTroopsUpgrade != null && i < buttonsTroopsUpgrade.length){
				remove(buttonsTroopsUpgrade[i]);
			}
    	}
    	buttonsTroopsUpgrade = null;
    }
    
    private void clearVIPTypeChoices(){
    	for (int i = 0; i < VIPTypeChoice.length; i++){
    		VIPTypeChoice[i].setVisible(false);
    	  cleaning = true;
    	  VIPTypeChoice[i].removeAllItems();
    	  cleaning = false;
    	}
    }
    
    private void clearUpgradeBuildingTypeChoice(){
    	for (int i = 0; i < upgradeBuildingTypeChoice.length; i++){
    		upgradeBuildingTypeChoice[i].setVisible(false);
    	  cleaning = true;
    	  upgradeBuildingTypeChoice[i].removeAllItems();
    	  cleaning = false;
    	}
    }
    

  public void actionPerformed(ActionEvent ae){
//System.out.println("actionPerformed" + ae.toString() + " xxx " + ae.getID() + " xxx " + ae.getModifiers());
    if (ae.toString().indexOf("invalid,hidden") > -1){
//System.out.println("ae.toString().indexOf('invalid,hidden') > -1");
      // gör inget, detta är ingen riktig action
    }else
    if (ae.getSource() instanceof CheckBoxPanel){
      newOrder((CheckBoxPanel)ae.getSource());
    }else
    if (ae.getSource() instanceof ComboBoxPanel && (ComboBoxPanel)ae.getSource() == buildnewBuildingChoice){
    	newBuildingOrder();
    }else
    if (ae.getSource() instanceof SRButton){
    	Logger.finer("ae.getSource() instanceof SRButton");
    	
    	if (ae.getActionCommand().equalsIgnoreCase("View Details")){
    		client.showBuildingTypeDetails(currentBuilding.getBuildingType().getName(), "Yours");
    	}else if((SRButton)ae.getSource() == detailsNewBuildingChoice){
    		showNewBuildingDetails();
    	}else if((SRButton)ae.getSource() == detailsVIP){
    		showVIPDetails();
    	}else if((SRButton)ae.getSource() == detailsUpgrade){
    		showUpgradeBuildingDetails();
    	}else{
    		if(currentBuilding.getBuildingType().isShipBuilder()){
    			for(int i=0;i< buttonsShipsUpgrade.length;i++){
    				if((SRButton)ae.getSource() == buttonsShipsUpgrade[i]) {
    					client.showShiptypeDetails(getComboBoxValue(shipTypeChoice[i].getSelectedItem()), "Yours");
    				}
    			}
    			 
    		}else if(currentBuilding.getBuildingType().isTroopBuilder()){
    			for(int i=0;i< buttonsTroopsUpgrade.length;i++){
    				if((SRButton)ae.getSource() == buttonsTroopsUpgrade[i]) {
    					client.showTroopTypeDetails(getComboBoxValue(troopTypeChoice[i].getSelectedItem()), "Yours");
    				}
    			}
    		}
    	}
    }else{
    	if (currentBuilding.getBuildingType().isShipBuilder()){
    		System.out.println("currentBuilding.getBuildingType().isShipBuilder();" + currentBuilding.getBuildingType().getName() + " " +currentBuilding.getBuildingType().isShipBuilder());
	        //  {  leta rätt på vilken choice som har valts
	        int found = -1;
	        int i = 0;
	        while ( (i < 5) & (found == -1)) {
	          if ((ComboBoxPanel)ae.getSource() == shipTypeChoice[i]) {
	            found = i;
	          }
	          else {
	            i++;
	          }
	        }
	        if (found > -1) {
	          if (!cleaning & !noaction){
	//System.out.println("newOrder(found);" + cleaning + " " +noaction);
	        	  System.out.println("MiniBuildingPanel: actionPerformed: newOrder(found, 'Ship') found= " + found);
	            newOrder(found, "Ship");
	          }
	        }
      }else
      if (currentBuilding.getBuildingType().isTroopBuilder()){
          //  {  leta rätt på vilken choice som har valts
          int found = -1;
          int i = 0;
          while ( (i < 5) & (found == -1)) {
            if ((ComboBoxPanel)ae.getSource() == troopTypeChoice[i]) {
              found = i;
            }
            else {
              i++;
            }
          }
          if (found > -1) {
            if (!cleaning & !noaction){
//  System.out.println("newOrder(found);" + cleaning + " " +noaction);
              newOrder(found, "Troop");
            }
          }
        }else
      if (currentBuilding.getBuildingType().isVIPBuilder()){
          //  {  leta rätt på vilken choice som har valts
          int found = -1;
          int i = 0;
          while ( (i < 5) & (found == -1)) {
            if ((ComboBoxPanel)ae.getSource() == VIPTypeChoice[i]) {
              found = i;
            }
            else {
              i++;
            }
          }
          if (found > -1) {
            if (!cleaning & !noaction){
//  System.out.println("newOrder(found);" + cleaning + " " +noaction);
              newOrder(found, "VIP");
            }
          }
        }else{
//          {  leta rätt på vilken choice som har valts
            int found = -1;
            int i = 0;
            while ( (i < 5) & (found == -1)) {
              if ((ComboBoxPanel)ae.getSource() == upgradeBuildingTypeChoice[i]) {
                found = i;
              }
              else {
                i++;
              }
            }
            if (found > -1) {
              if (!cleaning & !noaction){
//    System.out.println("newOrder(found);" + cleaning + " " +noaction);
                newOrder(found, "Building");
              }
            }
        	
        }
    }
  }

  public void valueChanged(ListSelectionEvent lse){
//System.out.println("valueChanged" + lse.toString() + lse.getValueIsAdjusting());
    try{
      if (lse.getSource() instanceof ListPanel){
        if ((ListPanel)lse.getSource() == buildingList){
          if (lse.getValueIsAdjusting()){
        	  noaction = true;
        	  showBuilding(buildingList.getSelectedIndex());
        	  noaction = false;
          }
        }
      }
    }
    catch(NumberFormatException nfe){
    }
  }

    private void newOrder(int choiceIndex, String unitType){
      // först ta bort alla gamla orders för det aktuella varvet
    	System.out.println("newOrder currentBuilding: " + currentBuilding.getUniqueId());
    	
      player.getOrders().removeUpgradeBuilding(currentBuilding, player.getGalaxy());
      
      if(unitType.equalsIgnoreCase("Ship")){
    	  player.getOrders().removeAllBuildShip(currentBuilding, player.getGalaxy());
	      // lopa igenom alla choisar till och med den nyligen valda
    	  for (int i = 0; i <= choiceIndex; i++){
    		  String selected = (String)shipTypeChoice[i].getSelectedItem();
    		  // remove paranthesis with cost...
    		  int index = selected.indexOf("(");
    		  if (index > -1){
    			  selected = selected.substring(0,index-1);
    		  }
    		  if (selected.equalsIgnoreCase("None") || selected.startsWith("---")){
    			  // gör inget
    		  }else{
    			  if(i == 0 && shipTypeChoice[i].getSelectedIndex() <= currentBuilding.getBuildingType().getUpgradebleBuildingTypes(currentBuilding).size() +1) {// i == 0 är första valet(selectboxen) i comboBoxen och där fins möjligheten att göra en uppdatering till en ny byggnad.
    				  // Is a upgrade (Building)
    				  System.out.println("currentBuilding " + currentBuilding.getUniqueId() + " " + currentBuilding.getBuildingType().getName());
    				  System.out.println("currentBuilding.getBuildingType().getNextBuildingType(selected) " + currentBuilding.getBuildingType().getNextBuildingType(selected, player).getName());
    				  player.addUppgradeBuilding(currentBuilding,player.getBuildings().getBuildingType(selected));
    			  }else{ // eller skeppsbygge */
    				  SpaceshipType tempsst = getShipType(selected);
    				  player.addBuildShip(currentBuilding,tempsst);  // lägg till en ny order för denna choice
    			  }
    		  }
	      }
      }else if(unitType.equalsIgnoreCase("Troop")){
    	  player.getOrders().removeAllBuildTroop(currentBuilding, player.getGalaxy());
    	  for (int i = 0; i <= choiceIndex; i++){
    		  String selected = (String)troopTypeChoice[i].getSelectedItem();
    		  // remove paranthesis with cost...
    		  int index = selected.indexOf("(");
    		  if (index > -1){
    			  selected = selected.substring(0,index-1);
    		  }
    		  if (selected.equalsIgnoreCase("None") || selected.startsWith("---")){
    			  // gör inget
    		  }else{
    			  if(i == 0 && troopTypeChoice[i].getSelectedIndex() <= currentBuilding.getBuildingType().getUpgradebleBuildingTypes(currentBuilding).size() + 1 && currentBuilding.getBuildingType().getUpgradebleBuildingTypes(currentBuilding).size() > 0) {// i == 0 är första valet(selectboxen) i comboBoxen och där fins möjligheten att göra en uppdatering till en ny byggnad.
    				  // Is a upgrade (Building)
    				  System.out.println("currentBuilding " + currentBuilding.getUniqueId() + " " + currentBuilding.getBuildingType().getName());
    				  System.out.println("currentBuilding.getBuildingType().getNextBuildingType(selected) " + currentBuilding.getBuildingType().getNextBuildingType(selected, player).getName());
    				  player.addUppgradeBuilding(currentBuilding,player.getBuildings().getBuildingType(selected));
    			  }else{ // eller troopbygge */
    				  TroopType tempTT = getTroopType(selected);
    				  player.addBuildTroop(currentBuilding,tempTT);  // lägg till en ny order för denna choice
    				  
    			  }
    		  }
	      }
    	  
      }else if(unitType.equalsIgnoreCase("VIP")){// VIP
    	  player.getOrders().removeBuildVIP(currentBuilding, player.getGalaxy());
    	  
		  String selected = (String)VIPTypeChoice[0].getSelectedItem();
		  // remove paranthesis with cost...
		  int index = selected.indexOf("(");
		  if (index > -1){
			  selected = selected.substring(0,index-1);
		  }
		  if (selected.equalsIgnoreCase("None") || selected.startsWith("---")){
			  // gör inget
		  }else{
			  if(VIPTypeChoice[0].getSelectedIndex() <= currentBuilding.getBuildingType().getUpgradebleBuildingTypes(currentBuilding).size()) {// i == 0 är första valet(selectboxen) i comboBoxen och där fins möjligheten att göra en uppdatering till en ny byggnad.
				  // Is a upgrade (Building)
				  System.out.println("currentBuilding " + currentBuilding.getUniqueId() + " " + currentBuilding.getBuildingType().getName());
				  System.out.println("currentBuilding.getBuildingType().getNextBuildingType(selected) " + currentBuilding.getBuildingType().getNextBuildingType(selected, player).getName());
				  player.addUppgradeBuilding(currentBuilding,player.getBuildings().getBuildingType(selected));
			  }else{ // eller VIPsbygge */
				  VIPType vipType = getVIPType(selected);
				  player.addBuildVIP(currentBuilding,vipType);  // lägg till en ny order för denna choice
				  
			  }
		  }
	      
    	  
      }else{ // Building
    	  
		  String selected = (String)upgradeBuildingTypeChoice[0].getSelectedItem();
		  // remove paranthesis with cost...
		  int index = selected.indexOf("(");
		  if (index > -1){
			  selected = selected.substring(0,index-1);
		  }
		  if (selected.equalsIgnoreCase("None") || selected.startsWith("---")){
			  // gör inget
		  }else{
			  // Is a upgrade (Building)
			  System.out.println("currentBuilding " + currentBuilding.getUniqueId() + " " + currentBuilding.getBuildingType().getName());
			  System.out.println("currentBuilding.getBuildingType().getNextBuildingType(selected) " + player.getBuildings().getBuildingType(selected).getName());
			  
			  player.addUppgradeBuilding(currentBuilding,player.getBuildings().getBuildingType(selected));
			  
		  }
      }
      
      
      
      // uppdatera treasurylabeln
      client.updateTreasuryLabel();
      // töm choisarna
      //clearShipTypeChoices();
      // visa choisarna, nu med den nya ordern
      showBuilding(buildingList.getSelectedIndex());
    }

    private void newOrder(CheckBoxPanel cb){
      if (cb == selfDestructCheckBox){
        if (cb.isSelected()){
          // först ta bort alla gamla orders för det aktuella varvet
          player.getOrders().removeUpgradeBuilding(currentBuilding, player.getGalaxy());
          player.getOrders().removeAllBuildShip(currentBuilding, player.getGalaxy());
          player.getOrders().removeAllBuildTroop(currentBuilding, player.getGalaxy());
          player.getOrders().removeBuildVIP(currentBuilding, player.getGalaxy());
          // töm choisarna
          clearShipTypeChoices();
          clearTroopTypeChoices();
          clearVIPTypeChoices();
          clearUpgradeBuildingTypeChoice();
          
          // disabla översta choicen
          shipTypeChoice[0].setEnabled(false);
          troopTypeChoice[0].setEnabled(false);
          VIPTypeChoice[0].setEnabled(false);
          upgradeBuildingTypeChoice[0].setEnabled(false);
         // add selfdestruct order
          player.addBuildingSelfDestruct(currentBuilding);
          // visa choisarna, nu med den nya ordern
          showBuilding(buildingList.getSelectedIndex());
          // ändra översta choicen till "none"
          if(currentBuilding.getBuildingType().isShipBuilder()){
        	  shipTypeChoice[0].setSelectedIndex(0);
          }else
    	  if(currentBuilding.getBuildingType().isTroopBuilder()){
    		  troopTypeChoice[0].setSelectedIndex(0);
          }else
    	  if(currentBuilding.getBuildingType().isVIPBuilder()){
    		  VIPTypeChoice[0].setSelectedIndex(0);
          }else{
        	  upgradeBuildingTypeChoice[0].setSelectedIndex(0);
          }
          
          
          
         }else{
          // enabla översta choicen
          shipTypeChoice[0].setEnabled(true);
          troopTypeChoice[0].setEnabled(true);
          shipTypeChoice[0].setEnabled(true);
          upgradeBuildingTypeChoice[0].setEnabled(true);
          // remove selfdestruct order
          player.removeBuildingSelfDestruct(currentBuilding);
        }
        // update treasury label...
        client.updateTreasuryLabel();
      }
    }
    
    private void newBuildingOrder(){
      player.removeNewBuilding(aPlanet);
  	  String selected = (String)buildnewBuildingChoice.getSelectedItem();
	  // remove paranthesis with cost...
	  int index = selected.indexOf("(");
	  if (index > -1){
		  selected = selected.substring(0,index-1);
	  }
	  if (selected.equalsIgnoreCase("None") || selected.startsWith("---")){
		  detailsNewBuildingChoice.setVisible(false);
		  // gör inget
	  }else{
		  player.addNewBuilding(aPlanet, player.getBuildings().getBuildingType(selected));
		  detailsNewBuildingChoice.setVisible(true);
		  //uppdatera "left to spend" och send-knappen
		  
	  }
	  client.updateTreasuryLabel();
        
    }
    
    private void showNewBuildingDetails(){
    	String selected = (String)buildnewBuildingChoice.getSelectedItem();
    	int index = selected.indexOf("(");
  	  	if (index > -1){
  	  		selected = selected.substring(0,index-1);
  	  	}
  	  	if (selected.equalsIgnoreCase("None") || selected.startsWith("---")){
  	  	}else{
  	  	client.showBuildingTypeDetails(selected, "Yours");
  	  	}
    }
    
    private void showVIPDetails(){
    	String selected = (String)VIPTypeChoice[0].getSelectedItem();
    	int index = selected.indexOf("(");
  	  	if (index > -1){
  	  		selected = selected.substring(0,index-1);
  	  	}
  	  	if (selected.equalsIgnoreCase("None") || selected.startsWith("---")){
  	  	}else{
  	  	client.showVIPTypeDetails(selected, "Yours");
  	  	}
    }
    
    private void showUpgradeBuildingDetails(){
    	if(currentBuilding.getBuildingType().isShipBuilder()){
    		client.showBuildingTypeDetails(getComboBoxValue(shipTypeChoice[0].getSelectedItem()), "Yours");
    	}else if(currentBuilding.getBuildingType().isTroopBuilder()){
    		client.showBuildingTypeDetails( getComboBoxValue(troopTypeChoice[0].getSelectedItem()), "Yours");
    	}else if(currentBuilding.getBuildingType().isVIPBuilder()){
    		client.showBuildingTypeDetails( getComboBoxValue(VIPTypeChoice[0].getSelectedItem()), "Yours");
    	}else{
    		client.showBuildingTypeDetails( getComboBoxValue(upgradeBuildingTypeChoice[0].getSelectedItem()), "Yours");
    	}
    }
    
    private String getComboBoxValue(String text){
    	int index = text.indexOf("(");
  	  	if (index > -1){
  	  		return text.substring(0,index-1);
  	  	}
  	  	return text;
    }

    private void showBuilding(int index){
      Logger.fine("showBuilding");
      //currentBuilding = findWharf(index);
      currentBuilding = allBuildings.get(index);
      if (currentBuilding != null){
    	Logger.finer("currentBuilding: " + currentBuilding.getBuildingType().getName());
    	nameLabel.setText("Name: " + currentBuilding.getBuildingType().getName());
//        maxTonnageLabel.setText("Max tonnage: " + currentBuilding.getMaxTonnage());
    //    sizeLabel.setText("Size: " + currentBuilding.getBuildingType().getSizeString());
     //   slotsLabel.setText("");
   /*     if(currentBuilding.getBuildingType().isShipBuilder()){
        	slotsLabel.setText("Slots: " + currentBuilding.getBuildingType().getWharfSize());
        }else
    	if(currentBuilding.getBuildingType().isTroopBuilder()){
        	slotsLabel.setText("Slots: " + currentBuilding.getBuildingType().getTroopSize());
        }else
    	if(currentBuilding.getBuildingType().isVIPBuilder()){
        	slotsLabel.setText("Slots: 1");
        }*/
        
        detailsUpgrade.setVisible(false);
        detailsVIP.setVisible(false);
        
        clearShipTypeChoices();
        clearTroopTypeChoices();
        clearVIPTypeChoices();
        clearUpgradeBuildingTypeChoice();
        
        Logger.finer("efter clear: ");
        
        String statusString = "";
        // Kolla om man kan bygga i byggnaden
        boolean enemyTroopsOnPlanet = player.getGalaxy().findOtherTroopsPlayersOnRazedPlanet(player, currentBuilding.getLocation()).size() > 0;
        boolean underSiege = currentBuilding.getLocation().isBesieged() && currentBuilding.getBuildingType().isInOrbit();
        boolean abandonPlanet = player.getOrders().getAbandonPlanet(aPlanet);
        if (enemyTroopsOnPlanet){
            statusString = "Can't build any units if planet have ongoing ground battles.";
            Logger.finer("Planet have ongoing ground battles so buildigns can not build any units at this time.");
            noBuildingAllowed();
        }else
        if (underSiege){
            statusString = "Buildigns in orbit can't build units on blocked/undersiege plantes";
            Logger.finer("Planet blocked/undersiege so buildigns in orbit can not build any units at this time.");
            noBuildingAllowed();
        }else
        if (abandonPlanet){
        	statusString = "Abandoned planet can't build units.";
        	Logger.finer("Planet is to be abandoned so buildings can not build any units at this time.");
        	noBuildingAllowed();
        }else{ // ok to use building to build
//        if (player.getGalaxy().findOtherTroopsPlayersOnRazedPlanet(player, currentBuilding.getLocation()).size() > 0){
//            statusString = "Planet have ongoing ground battles so buildigns can not build any units at this time.";
//            Logger.finer("Planet have ongoing ground battles so buildigns can not build any units at this time.");
//            if(currentBuilding.getBuildingType().isShipBuilder()){
//            	shipTypeChoice[0].addItem("None");
//                shipTypeChoice[0].setEnabled(false);
//                shipTypeChoice[0].setVisible(true);
//                troopTypeChoice[0].setVisible(false);
//                VIPTypeChoice[0].setVisible(false);
//                upgradeBuildingTypeChoice[0].setVisible(false);
//            }else
//            if(currentBuilding.getBuildingType().isTroopBuilder()){
////            	buildTypeLabel.setText("Build new troops or upgrade the building:");
//            	
//            	troopTypeChoice[0].addItem("None");
//            	troopTypeChoice[0].setEnabled(false);
//            	troopTypeChoice[0].setVisible(true);
//            	shipTypeChoice[0].setVisible(false);
//                VIPTypeChoice[0].setVisible(false);
//                upgradeBuildingTypeChoice[0].setVisible(false);
//            }else
//            if(currentBuilding.getBuildingType().isVIPBuilder()){
////            	buildTypeLabel.setText("Recruite new VIP or upgrade the building:");
//            	
//            	VIPTypeChoice[0].addItem("None");
//            	VIPTypeChoice[0].setEnabled(false);
//            	VIPTypeChoice[0].setVisible(true);
//                troopTypeChoice[0].setVisible(false);
//                shipTypeChoice[0].setVisible(false);
//                upgradeBuildingTypeChoice[0].setVisible(false);
//            }else{ // upgrade building
////            	buildTypeLabel.setText("Upgrade building to:");
//            	
//            	upgradeBuildingTypeChoice[0].addItem("None");
//            	upgradeBuildingTypeChoice[0].setEnabled(false);
//            	upgradeBuildingTypeChoice[0].setVisible(true);
//                troopTypeChoice[0].setVisible(false);
//                shipTypeChoice[0].setVisible(false);
//                VIPTypeChoice[0].setVisible(false);
//            }
//            
//        }else
//        if (currentBuilding.getLocation().isBesieged() && currentBuilding.getBuildingType().isInOrbit()){
//          statusString = "Planet blocked/undersiege so buildigns in orbit can not build any units at this time.";
//          Logger.finer("Planet blocked/undersiege so buildigns in orbit can not build any units at this time.");
//          if(currentBuilding.getBuildingType().isShipBuilder()){
//        	  shipTypeChoice[0].addItem("None");
//              shipTypeChoice[0].setEnabled(false);
//              shipTypeChoice[0].setVisible(true);
//              troopTypeChoice[0].setVisible(false);
//              VIPTypeChoice[0].setVisible(false);
//              upgradeBuildingTypeChoice[0].setVisible(false);
//          }else
//          if(currentBuilding.getBuildingType().isTroopBuilder()){
//        //  	buildTypeLabel.setText("Build troop type or upgrade:");
//          	
//          	troopTypeChoice[0].addItem("None");
//          	troopTypeChoice[0].setEnabled(false);
//          	troopTypeChoice[0].setVisible(true);
//          	shipTypeChoice[0].setVisible(false);
//            VIPTypeChoice[0].setVisible(false);
//            upgradeBuildingTypeChoice[0].setVisible(false);
//          }else
//          if(currentBuilding.getBuildingType().isVIPBuilder()){
//         // 	buildTypeLabel.setText("Build VIP type or upgrade:");
//          	
//          	VIPTypeChoice[0].addItem("None");
//          	VIPTypeChoice[0].setEnabled(false);
//          	VIPTypeChoice[0].setVisible(true);
//            troopTypeChoice[0].setVisible(false);
//            shipTypeChoice[0].setVisible(false);
//            upgradeBuildingTypeChoice[0].setVisible(false);
//          }else{
//        	  upgradeBuildingTypeChoice[0].addItem("None");
//        	  upgradeBuildingTypeChoice[0].setEnabled(false);
//        	  upgradeBuildingTypeChoice[0].setVisible(true);
//              troopTypeChoice[0].setVisible(false);
//              shipTypeChoice[0].setVisible(false);
//              VIPTypeChoice[0].setVisible(false);
//          }

        	Logger.finer("ingen blockad eller fiender trupper.");
        	if(currentBuilding.getBuildingType().isShipBuilder()){
        		// visa alla tidigare valda skeppsorder samt ev. en till med ytterligare tillgängliga alternativ
	            showShipTypeChoices(currentBuilding);
	            if (selfDestructCheckBox.isSelected()){  // om denna building är satt att selfdestructa, disabla översta choicen
	              shipTypeChoice[0].setEnabled(false);
	            }else{
	              shipTypeChoice[0].setEnabled(true);
	            }
	            troopTypeChoice[0].setVisible(false);
	            VIPTypeChoice[0].setVisible(false);
	            upgradeBuildingTypeChoice[0].setVisible(false);
	            shipTypeChoice[0].setVisible(true);
	            buildTypeLabel.setVisible(true);
        	}else
            if(currentBuilding.getBuildingType().isTroopBuilder()){
            	// visa alla tidigare valda troopsorder samt ev. en till med ytterligare tillgängliga alternativ
	            showTroopTypeChoices(currentBuilding);
	            if (selfDestructCheckBox.isSelected()){  // om denna building är satt att selfdestructa, disabla översta choicen
	            	troopTypeChoice[0].setEnabled(false);
	            }else{
	            	troopTypeChoice[0].setEnabled(true);
	            }
	            shipTypeChoice[0].setVisible(false);
	            VIPTypeChoice[0].setVisible(false);
	            upgradeBuildingTypeChoice[0].setVisible(false);
	            troopTypeChoice[0].setVisible(true);
	            buildTypeLabel.setVisible(true);
            }else
        	if(currentBuilding.getBuildingType().isVIPBuilder()){
        		// visa alla tidigare valda VIPsorder samt ev. en till med ytterligare tillgängliga alternativ
	            showVIPTypeChoices(currentBuilding);
	            if (selfDestructCheckBox.isSelected()){  // om denna building är satt att selfdestructa, disabla översta choicen
	            	VIPTypeChoice[0].setEnabled(false);
	            }else{
	            	VIPTypeChoice[0].setEnabled(true);
	            }
	            troopTypeChoice[0].setVisible(false);
	            shipTypeChoice[0].setVisible(false);
	            upgradeBuildingTypeChoice[0].setVisible(false);
	            VIPTypeChoice[0].setVisible(true);
	            buildTypeLabel.setVisible(true);
        	}else{
        		showUpgradeBuildingTypeChoice(currentBuilding);
	            if (selfDestructCheckBox.isSelected()){  // om denna building är satt att selfdestructa, disabla översta choicen
	            	upgradeBuildingTypeChoice[0].setEnabled(false);
	            }else{
	            	upgradeBuildingTypeChoice[0].setEnabled(true);
	            }
	            troopTypeChoice[0].setVisible(false);
	            shipTypeChoice[0].setVisible(false);
	            VIPTypeChoice[0].setVisible(false);
	            if(upgradeBuildingTypeChoice[0].getItemCount()> 1){
	            	upgradeBuildingTypeChoice[0].setVisible(true);
	            	buildTypeLabel.setVisible(true);
	            }else{
	            	upgradeBuildingTypeChoice[0].setVisible(false);
	            	buildTypeLabel.setVisible(false);
	            }
        	}
        }        
        
        statusLabel.setText(statusString);
//        locationLabel.setText("Location: " + currentBuilding.getLocation().getName());
        if(currentBuilding.getBuildingType().isShipBuilder()){
        	buildTypeLabel.setText("Build new ship or upgrade the building:");
        }else
        if(currentBuilding.getBuildingType().isTroopBuilder()){
        	buildTypeLabel.setText("Build new troop or upgrade the building:");
        }else
        if(currentBuilding.getBuildingType().isVIPBuilder()){
        	buildTypeLabel.setText("Recruite new VIP or upgrade the building:");
        }else{
        	buildTypeLabel.setText("Upgrade building to:");
        }

        // show and set selfdestruct cb
        selfDestructCheckBox.setSelected(player.getBuildingSelfDestruct(currentBuilding));
        boolean destructable= false;
        if(currentBuilding.getBuildingType().isSelfDestructable()){
        	destructable= true;
        }
        
        if (currentBuilding.getLocation().isBesieged() && currentBuilding.getBuildingType().isInOrbit()){
        	destructable=false;
        }
        
        if(!currentBuilding.getBuildingType().isInOrbit() && player.getGalaxy().isOngoingGroundBattle(currentBuilding.getLocation(), player)){
        	destructable=false;
        }
        
        selfDestructCheckBox.setEnabled(destructable);
        selfDestructCheckBox.setVisible(true);
        
        detailsButton.setVisible(true);

        abilitiesLabel.setText("Building abilities:");
        abilitiesTextArea.setText("");
        List<String> allStrings = currentBuilding.getBuildingType().getAbilitiesStrings();
        for (int i = 0; i < allStrings.size(); i++){
        	abilitiesTextArea.append(allStrings.get(i) + "\n");
        	Logger.finer("Building abilities: " + allStrings.get(i));
        }
        abilitiesTextArea.setVisible(true);
        abilitiesTextArea.repaint();
        
      }else{
        nameLabel.setText("");
    //    maxTonnageLabel.setText("");
    //    sizeLabel.setText("");
     //   slotsLabel.setText("");
        statusLabel.setText("");
   //     locationLabel.setText("");
        buildTypeLabel.setText("");
        clearShipTypeChoices();
        clearTroopTypeChoices();
        clearVIPTypeChoices();
        clearUpgradeBuildingTypeChoice();
        selfDestructCheckBox.setVisible(false);
        detailsButton.setVisible(false);
        abilitiesLabel.setText("");
        abilitiesTextArea.setText("");
        abilitiesTextArea.setVisible(false);

      }
    }

    private void noBuildingAllowed(){
        if(currentBuilding.getBuildingType().isShipBuilder()){
        	shipTypeChoice[0].addItem("None");
            shipTypeChoice[0].setEnabled(false);
            shipTypeChoice[0].setVisible(true);
            troopTypeChoice[0].setVisible(false);
            VIPTypeChoice[0].setVisible(false);
            upgradeBuildingTypeChoice[0].setVisible(false);
        }else
        if(currentBuilding.getBuildingType().isTroopBuilder()){
        	troopTypeChoice[0].addItem("None");
        	troopTypeChoice[0].setEnabled(false);
        	troopTypeChoice[0].setVisible(true);
        	shipTypeChoice[0].setVisible(false);
            VIPTypeChoice[0].setVisible(false);
            upgradeBuildingTypeChoice[0].setVisible(false);
        }else
        if(currentBuilding.getBuildingType().isVIPBuilder()){
        	VIPTypeChoice[0].addItem("None");
        	VIPTypeChoice[0].setEnabled(false);
        	VIPTypeChoice[0].setVisible(true);
            troopTypeChoice[0].setVisible(false);
            shipTypeChoice[0].setVisible(false);
            upgradeBuildingTypeChoice[0].setVisible(false);
        }else{ // upgrade building
        	upgradeBuildingTypeChoice[0].addItem("None");
        	upgradeBuildingTypeChoice[0].setEnabled(false);
        	upgradeBuildingTypeChoice[0].setVisible(true);
        	troopTypeChoice[0].setVisible(false);
            shipTypeChoice[0].setVisible(false);
            VIPTypeChoice[0].setVisible(false);
        }
    }
    
  private void showShipTypeChoices(Building currentBuilding){
    // get orders
    Orders playersOrders = player.getOrders();
	// kolla först om det finns en engineer vid planeten
//	boolean engineer = false;
	VIP tempBuild;
//	VIP tempUpgrade = player.getGalaxy().findVIPUpgradeWharfBonus(currentBuilding.getLocation(),player,player.getOrders());
    // visa bara den översta choicen om den är satt till upgrade
	// TODO (Tobbe) Fixa så att vald upgrade byggnad blir byggd (MinBuildingPanel.java) och visas
	
	int index = 0;
    boolean showUpgrade = true; // skall endast visas i den översta choicen
	int slotsleft = currentBuilding.getBuildingType().getWharfSize();
    int tempMaxTonnage = slotsleft*300;
	
	
	if (playersOrders.alreadyUpgrading(currentBuilding)){
		BuildingType buildingType = playersOrders.getUppgradeBuilding(currentBuilding);
		tempBuild = player.getGalaxy().findVIPBuildingBuildBonus(currentBuilding.getLocation(),player,player.getOrders());
		int cost = buildingType.getBuildCost(tempBuild);
		addShipTypes(shipTypeChoice[index],showUpgrade,tempMaxTonnage);
		shipTypeChoice[0].setSelectedItem(buildingType.getName() + " (cost: " + cost + ") " + buildingType.getUniqueString());
		shipTypeChoice[0].setVisible(true);
		detailsUpgrade.setVisible(true);
    }else{ 
      List<SpaceshipType> buildsst = playersOrders.getAllShipBuilds(currentBuilding);
//System.out.println("buildsst: " + buildsst.size());
      
      if(buildsst.size() > 0){
    	  buttonsShipsUpgrade =  new SRButton[buildsst.size()];
      }
      
      while (index < buildsst.size()){
//System.out.println("index: " + index);
//System.out.println("tempMaxTonnage: " + tempMaxTonnage);
//System.out.println("slotsleft: " + slotsleft);
//System.out.println("");
        SpaceshipType tempsst = buildsst.get(index);
        addShipTypes(shipTypeChoice[index],showUpgrade,tempMaxTonnage);
        if (showUpgrade){  // sätt till false så att endast den första choicen visar upgrade
          showUpgrade = false;
        }
        // beräkna hur många slots det finns kvar efter detta skeppsbygge
        slotsleft = slotsleft - tempsst.getSlots();
//System.out.println("slotsleft: " + slotsleft);
        tempMaxTonnage = slotsleft*300;
        // compute cost
        tempBuild = player.getGalaxy().findVIPShipBuildBonus(currentBuilding.getLocation(),player,player.getOrders());
		int cost = tempsst.getBuildCost(tempBuild);
		// set selected
        shipTypeChoice[index].setSelectedItem(tempsst.getName() + " (cost: " + cost + ") " + tempsst.getUniqueString());
        shipTypeChoice[index].setVisible(true);
        
        // show detailsButton
        buttonsShipsUpgrade[index] = new SRButton("?");
        buttonsShipsUpgrade[index].setBounds(301,280 + (index*20),20,20);
        buttonsShipsUpgrade[index].addActionListener(this);
        buttonsShipsUpgrade[index].setVisible(true);
        buttonsShipsUpgrade[index].setToolTipText("Click for more details information about the ship");
        add(buttonsShipsUpgrade[index]);
        
        index++;
      }
      // om det finns slots kvar visa en till choice som inte har något valt alternativ
      if (slotsleft > 0){
//System.out.println("slotsleft > 0: ");
//System.out.println("tempMaxTonnage: " + tempMaxTonnage);
//System.out.println("slotsleft: " + slotsleft);
//System.out.println("");
        addShipTypes(shipTypeChoice[index],showUpgrade,tempMaxTonnage);
        shipTypeChoice[index].setVisible(true);
      }
    }

  }
  
  private void showTroopTypeChoices(Building currentBuilding){
    Orders playersOrders = player.getOrders();
	// kolla först om det finns en engineer vid planeten
//		boolean engineer = false;
	VIP tempBuild;
	

    // visa bara den översta choicen om den är satt till upgrade
	// TODO (Tobbe) Fixa så att vald upgrade byggnad blir byggd (MinBuildingPanel.java) och visas
	
	int index = 0;
    boolean showUpgrade = true; // skall endast visas i den översta choicen
	System.out.println("playersOrders.alreadyUpgrading(currentBuilding): " + playersOrders.alreadyUpgrading(currentBuilding));
	if (playersOrders.alreadyUpgrading(currentBuilding)){
		BuildingType buildingType = playersOrders.getUppgradeBuilding(currentBuilding);
		tempBuild = player.getGalaxy().findVIPBuildingBuildBonus(currentBuilding.getLocation(),player,player.getOrders());
		int cost = buildingType.getBuildCost(tempBuild);
		addTroopTypes(troopTypeChoice[index],showUpgrade);
		troopTypeChoice[0].setSelectedItem(buildingType.getName() + " (cost: " + cost + ") " + buildingType.getUniqueString());
		troopTypeChoice[0].setVisible(true);
		detailsUpgrade.setVisible(true);
    }else{
    	System.out.println("hämtar alla trupper håller på att byggas: ");
      List<TroopType> buildTroopType = playersOrders.getAllTroopBuilds(currentBuilding);
      
      if(buildTroopType.size() > 0){
    	  buttonsTroopsUpgrade =  new SRButton[buildTroopType.size()];
      }
      
      
      System.out.println("buildTroopType.size(): " + buildTroopType.size());
      int slotsleft = currentBuilding.getBuildingType().getTroopSize();
      
      while (index < buildTroopType.size()){

    	  TroopType troopType = buildTroopType.get(index);
    	  System.out.println("troopType: " + troopType.getUniqueName());
    	  addTroopTypes(troopTypeChoice[index],showUpgrade);
    	  if (showUpgrade){  // sätt till false så att endast den första choicen visar upgrade
    		  showUpgrade = false;
    	  }
        // beräkna hur många slots det finns kvar efter detta skeppsbygge
        slotsleft = slotsleft - 1;
//	System.out.println("slotsleft: " + slotsleft);
        // compute cost
        
        tempBuild = player.getGalaxy().findVIPTroopBuildBonus(currentBuilding.getLocation(),player,player.getOrders());
        int cost = troopType.getCostBuild(tempBuild);
		// set selected
		troopTypeChoice[index].setSelectedItem(troopType.getUniqueName() + " (cost: " + cost + ") " + troopType.getUniqueString());
		troopTypeChoice[index].setVisible(true);
		
		
		//show detailsButton
		buttonsTroopsUpgrade[index] = new SRButton("?");
		buttonsTroopsUpgrade[index].setBounds(301,280 + (index*20),20,20);
		buttonsTroopsUpgrade[index].addActionListener(this);
		buttonsTroopsUpgrade[index].setVisible(true);
		buttonsTroopsUpgrade[index].setToolTipText("Click for more details information about the troop");
        add(buttonsTroopsUpgrade[index]);
        
        
        index++;
      }
      // om det finns slots kvar visa en till choice som inte har något valt alternativ
      if (slotsleft > 0){

        addTroopTypes(troopTypeChoice[index],showUpgrade);
        troopTypeChoice[index].setVisible(true);
      }
    }

  }
  
  
  
  private void showVIPTypeChoices(Building currentBuilding){
    Orders playersOrders = player.getOrders();
	// kolla först om det finns en engineer vid planeten
//			boolean engineer = false;
//	VIP tempBuild = player.getGalaxy().findVIPBuildBuildingBonus(currentBuilding.getLocation(),player,player.getOrders());

    // visa bara den översta choicen om den är satt till upgrade
	// TODO (Tobbe) Fixa så att vald upgrade byggnad blir byggd (MinBuildingPanel.java) och visas
    
    String vipTypeName = playersOrders.getVIPBuild(currentBuilding);
    
//	int slotsleft = 1;
  //boolean showUpgrade = true; // skall endast visas i den översta choicen
  
	VIPTypeChoice[0].addItem("None");
	
    boolean underSiege = currentBuilding.getLocation().isBesieged() && currentBuilding.getBuildingType().isInOrbit();
    if (!underSiege){
    	addUpgradeBuildTypes(VIPTypeChoice[0], true);
    }
  
	List<VIPType> copyAllTypes = Functions.deepClone(currentBuilding.getBuildingType().getBuildVIPTypes());
	// TODO (Tobbe) Fixa till VIPTypeComparator så att de tar en castad lista + att VIPar sorteras på kostnad om det nu är nödvändigt.
	Collections.sort(copyAllTypes, new VIPTypeComparator());
	Collections.reverse(copyAllTypes);
	
	//	System.out.println("efter alltypes");
	
	if(VIPTypeChoice[0].getItemCount() > 1 && copyAllTypes.size() > 0){
		VIPTypeChoice[0].addItem(getItemDescription("VIPs"));
    }
	
	VIPType toBuild = null;
	for (VIPType tempVIP : copyAllTypes) {
		VIPType vipToBuild = getVIPType(tempVIP.getName());
		if(vipToBuild != null){
			int cost = vipToBuild.getBuildCost();
			VIPTypeChoice[0].addItem(vipToBuild.getName() + " (cost: " + cost + ") " + vipToBuild.getUniqueString());
			
			if(vipToBuild.getName().equalsIgnoreCase(vipTypeName)){
				toBuild = vipToBuild;
			}
		}
	}
	
	if (playersOrders.alreadyUpgrading(currentBuilding)){
		BuildingType buildingType = playersOrders.getUppgradeBuilding(currentBuilding);
		VIP tempBuild = player.getGalaxy().findVIPBuildingBuildBonus(currentBuilding.getLocation(),player,player.getOrders());
		int cost = buildingType.getBuildCost(tempBuild);
		VIPTypeChoice[0].setSelectedItem(buildingType.getName() + " (cost: " + cost + ") " + buildingType.getUniqueString());
		VIPTypeChoice[0].setVisible(true);
		detailsUpgrade.setVisible(true);
    }else if(vipTypeName != null && !vipTypeName.equalsIgnoreCase("")){
    		VIPTypeChoice[0].setSelectedItem(vipTypeName + " (cost: " + getVIPType(vipTypeName).getBuildCost() + ") " + toBuild.getUniqueString());
    		VIPTypeChoice[0].setVisible(true);
    		detailsVIP.setVisible(true);
    }
  }
  
  private String getItemDescription(String text){
	  return "--------------------- " + text + " --------------------------------------";
  }
  
  private void showUpgradeBuildingTypeChoice(Building currentBuilding){
	    Orders playersOrders = player.getOrders();
		// kolla först om det finns en engineer vid planeten
//				boolean engineer = false;
//		VIP tempBuild = player.getGalaxy().findVIPBuildBuildingBonus(currentBuilding.getLocation(),player,player.getOrders());

	    VIP tempBuild = player.getGalaxy().findVIPBuildingBuildBonus(currentBuilding.getLocation(),player,player.getOrders());
	    
	    // visa bara den översta choicen om den är satt till upgrade
		// TODO (Tobbe) Fixa så att vald upgrade byggnad blir byggd (MinBuildingPanel.java) och visas
	    
	  //boolean showUpgrade = true; // skall endast visas i den översta choicen
	  
	  
		upgradeBuildingTypeChoice[0].addItem("None");
		addUpgradeBuildTypes(upgradeBuildingTypeChoice[0], false);
	  
		//	System.out.println("efter alltypes");
		
		if (playersOrders.alreadyUpgrading(currentBuilding)){
			BuildingType buildingType = playersOrders.getUppgradeBuilding(currentBuilding);
			int cost = buildingType.getBuildCost(tempBuild);
			upgradeBuildingTypeChoice[0].setSelectedItem(buildingType.getName() + " (cost: " + cost + ") " + buildingType.getUniqueString());
			upgradeBuildingTypeChoice[0].setVisible(true);
			detailsUpgrade.setVisible(true);
	    }
	  }
  
  
  private void addUpgradeBuildTypes(ComboBoxPanel unitTypeChoice, boolean addDescriptionitem){
	  
	  if(currentBuilding.getBuildingType().getUpgradebleBuildingTypes(currentBuilding)!= null){
		  List<BuildingType> alltypes = Functions.deepClone(currentBuilding.getBuildingType().getUpgradebleBuildingTypes(currentBuilding));
		  Collections.sort(alltypes, new BuildingTypeBuildCostAndNameComparator());
		  Collections.reverse(alltypes);
		  
		  VIP tempVIP = player.getGalaxy().findVIPBuildingBuildBonus(currentBuilding.getLocation(),player,player.getOrders());
		  
		  if(alltypes.size() > 0 && addDescriptionitem){
			  unitTypeChoice.addItem(getItemDescription("buildings"));
		  }
		  
		  for(int i=0; i< alltypes.size();i++){
			  int cost = alltypes.get(i).getBuildCost(tempVIP);
			  unitTypeChoice.addItem(alltypes.get(i).getName() + " (cost: " + cost + ") " + alltypes.get(i).getUniqueString());
		  }
	  }
	  
  }

  private void addShipTypes(ComboBoxPanel shiptypechoice, boolean showUpgrade, int currentMaxTonnage){
//System.out.println("addShipTypes " + showUpgrade + " " + shiptypechoice.getItemCount());
    shiptypechoice.addItem("None");
//System.out.println("efter none");
	// kolla först om det finns en engineer vid planeten
//	boolean engineer = false;
    
    VIP tempBuild = player.getGalaxy().findVIPShipBuildBonus(currentBuilding.getLocation(),player,player.getOrders());
//	VIP tempUpgrade = player.getGalaxy().findVIPUpgradeWharfBonus(currentBuilding.getLocation(),player,player.getOrders());
    boolean underSiege = currentBuilding.getLocation().isBesieged() && currentBuilding.getBuildingType().isInOrbit();
	if (showUpgrade & !underSiege){
		addUpgradeBuildTypes(shiptypechoice, true);
	}
//System.out.println("efter upgrade");
  //  Vector alltypes = player.getSpaceshipTypes(); Old 
    List<SpaceshipType> alltypes = player.getAvailableSpaceshipTypes();
    List<SpaceshipType> copyAllTypes = Functions.cloneList(alltypes);
    Collections.sort(copyAllTypes, new SpaceshipTypeSizeComparator());
    //Collections.reverse(copyAllTypes);
//System.out.println("efter alltypes");
    String shipSize = "";
    for (SpaceshipType tempsst : copyAllTypes) {
        if (tempsst.getTonnage() <= currentMaxTonnage){
        	int cost = tempsst.getBuildCost(tempBuild);
        	
        	
        	if(!tempsst.getSizeString().equals(shipSize) && !(shipSize.equals("squadron") && tempsst.isSquadron())){
        		if(tempsst.isSquadron()){
        			shipSize = "squadron";
        		}else{
        			shipSize = tempsst.getSizeString();
        		}
        		shiptypechoice.addItem(getItemDescription(shipSize));
        	}
        	shiptypechoice.addItem(tempsst.getName() + " (cost: " + cost + ") " + tempsst.getUniqueString());
        }
	}
/*    for (int x = 0; x < alltypes.size(); x++){
      SpaceshipType tempsst = (SpaceshipType)alltypes.elementAt(x);
//System.out.println("x: " + x + " tempsst: " + tempsst.getName() + " " + tempsst.getTonnage() + " <=  " + currentMaxTonnage);
      if (tempsst.getTonnage() <= currentMaxTonnage){
//System.out.println("adding");
		int cost = tempsst.getBuildCost(tempBuild);
        shiptypechoice.addItem(tempsst.getName() + " (cost: " + cost + ")");
      }
    }
*/    alltypes = new ArrayList<SpaceshipType>();
    currentMaxTonnage = 0;
//dumpdata(shiptypechoice);
  }
  
  
  
  private void addTroopTypes(ComboBoxPanel trooptypechoice, boolean showUpgrade){
//	System.out.println("addShipTypes " + showUpgrade + " " + shiptypechoice.getItemCount());
	  trooptypechoice.addItem("None");
//	System.out.println("efter none");
	    
	   // VIP tempBuild = player.getGalaxy().findVIPBuildBuildingBonus(currentBuilding.getLocation(),player,player.getOrders());
	    boolean underSiege = currentBuilding.getLocation().isBesieged() && currentBuilding.getBuildingType().isInOrbit();
		if (showUpgrade & !underSiege){
			addUpgradeBuildTypes(trooptypechoice, true);
		}
//	System.out.println("efter upgrade");
	    List<TroopType> alltypes = player.getAvailableTroopTypes();
	    Logger.finer("player.getAvailableTroopTypes().size(): " + alltypes.size());
	    List<TroopType> copyAllTypes = Functions.cloneList(alltypes);
	    Collections.sort(copyAllTypes, new TroopTypeComparator());
	    Collections.reverse(copyAllTypes);
	    
	    VIP tempBuild = player.getGalaxy().findVIPTroopBuildBonus(currentBuilding.getLocation(),player,player.getOrders());
	    
	    
	    if(trooptypechoice.getItemCount() > 1 && copyAllTypes.size() > 0){
	    	trooptypechoice.addItem(getItemDescription("Troops"));
	    }
	    
//	System.out.println("efter alltypes");
	    for (TroopType tempTP : copyAllTypes) {
	    	if(currentBuilding.getBuildingType().canBuildTypeOfTroop(tempTP.getTypeOfTroop())){
	    		int cost = tempTP.getCostBuild(tempBuild);
		        trooptypechoice.addItem(tempTP.getUniqueName() + " (cost: " + cost + ") " + tempTP.getUniqueString());
	    	}
	    }
  }
  
/*
  private void dumpdata(ComboBoxPanel shiptypechoice){
    System.out.print("dumpdata: ");
    for (int i = 0; i < shiptypechoice.getItemCount(); i++){
      System.out.print(shiptypechoice.getItemAt(i) + " ");
    }
    System.out.println("");
  }
*/
  
  private TroopType getTroopType(String typeName){
	  	String aTypeName = typeName;
	  	// find shiptype
	    TroopType tt = null;
	   // Vector allshiptypes = player.getSpaceshipTypes(); old
	    Vector<TroopType> allTrooptypes = player.getAvailableTroopTypes();
	    int i = 0;
	    while (tt == null){
	      TroopType temp = allTrooptypes.elementAt(i);
	      if (temp.getUniqueName().equalsIgnoreCase(aTypeName)){
	    	  tt = temp;
	      }
	      i++;
	    }
	    return tt;
	  }

  private SpaceshipType getShipType(String typeName){
  	String aTypeName = typeName;
  	// find shiptype
    SpaceshipType st = null;
   // Vector allshiptypes = player.getSpaceshipTypes(); old
    Vector<SpaceshipType> allshiptypes = player.getAvailableSpaceshipTypes();
    int i = 0;
    while (st == null && i < allshiptypes.size()){
      SpaceshipType temp = allshiptypes.elementAt(i);
      if (temp.getName().equalsIgnoreCase(aTypeName)){
        st = temp;
      }
      i++;
    }
    return st;
  }
  
  private VIPType getVIPType(String typeName){
  	String aTypeName = typeName;
  	// find shiptype
    VIPType vipType = null;
   // Vector allshiptypes = player.getSpaceshipTypes(); old
    List<VIPType> allVIPTypes = player.getGalaxy().getGameWorld().getVipTypes();
    int i = 0;
    while (vipType == null && i < allVIPTypes.size()){
      VIPType temp = (VIPType)allVIPTypes.get(i);
      if (temp.getName().equalsIgnoreCase(aTypeName) && (temp.isConstructible(player) || aTypeName.equalsIgnoreCase(player.getOrders().getVIPBuild(currentBuilding)))){
    	  vipType = temp;
      }
      i++;
    }
    return vipType;
  }
  /*
  private VIPType getVIPType(String typeName){
	  	String aTypeName = typeName;
	  	// find shiptype
	    VIPType vipType = null;
	   // Vector allshiptypes = player.getSpaceshipTypes(); old
	    Vector<VIPType> allVIPTypes = player.getAvailableVIPTypes();
	    int i = 0;
	    while (vipType == null && i < allVIPTypes.size()){
	      VIPType temp = allVIPTypes.elementAt(i);
	      if (temp.getName().equalsIgnoreCase(aTypeName) && temp.isConstructible()){
	    	  vipType = temp;
	      }
	      i++;
	    }
	    return vipType;
	  }
*/
  
  private void fillnewBuildingsChoice(){
  	buildnewBuildingChoice.removeAllItems();
  	VIP tempEngineer = player.getGalaxy().findVIPBuildingBuildBonus(aPlanet,player,player.getOrders());
  	
  	Vector <BuildingType>  tempBuildingTypes = player.getAvailableNewBuildings(aPlanet);
  	buildnewBuildingChoice.addItem("None");
  	for(int i=0; i< tempBuildingTypes.size();i++){
  		int cost = tempBuildingTypes.get(i).getBuildCost(tempEngineer);
  		buildnewBuildingChoice.addItem(tempBuildingTypes.get(i).getName() + " (cost: " + cost + ")");
  	}
  	
  }
  
  public void updateData(){
    if (currentBuilding != null){
      showBuilding(buildingList.getSelectedIndex());
    }
  }
}
