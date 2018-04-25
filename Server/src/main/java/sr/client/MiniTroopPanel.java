package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.components.CheckBoxPanel;
import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.client.components.scrollable.ListPanel;
import sr.enums.DiplomacyLevel;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.Troop;
import sr.world.VIP;
import sr.world.comparator.troop.TroopTypeAndBuildCostComparator;

/**
 * Shows all ships at a planet and player can give orders to those ships
 * @author wmpabod
 *
 */
public class MiniTroopPanel extends SRBasePanel implements ActionListener,ListSelectionListener {
  private static final long serialVersionUID = 1L;
  private List<Troop> troops = new LinkedList<Troop>();
  private ListPanel troopsList;
  private SRLabel destinationLabel,dcLabel,killsLabel,VIPinfoLabel;
  private SRLabel weaponsInfantryLabel,weaponsArmorLabel,weaponsArtilleryLabel;
  private SRLabel weaponsInfantryLabel2,weaponsArmorLabel2,weaponsArtilleryLabel2;
  private SRLabel upkeepLabel,nameLabel,locationLabel;
  private SRLabel weaponsLabel, weaponsDamageLabel;
  private SRButton detailsButton, battleSimBtn; 
  private Player player;
  private ComboBoxPanel destinationchoice;
  private Troop currentTroop;
  private CheckBoxPanel selfDestructCheckBox;
  private SRTextArea VIPInfoTextArea;
  private int x = 0;
  private List<Integer> lastSelection = null;
  private JScrollPane scrollPane2;
  private SpaceRazeApplet client;
  private Planet planet;

  public MiniTroopPanel(List<Troop> troops, Player player, SpaceRazeApplet client, Planet aPlanet) {
      this.troops = troops;
      Collections.sort(this.troops,new TroopTypeAndBuildCostComparator());
      this.player = player;
      this.setLayout(null);
      this.client = client;
      this.planet = aPlanet;
      
      troopsList = new ListPanel();
      troopsList.setBounds(5,5,315,100);
      troopsList.setListSelectionListener(this);
      troopsList.setMultipleSelect(true);
      add(troopsList);
      fillList();
      
      battleSimBtn = new SRButton("Add to battleSim");
      battleSimBtn.setBounds(168,110,150,14);
      battleSimBtn.addActionListener(this);
      battleSimBtn.setToolTipText("Press this button to add selected troops to battle sim");
      if(getSelectedTroops().size()> 0){
    	  battleSimBtn.setVisible(true);
      }else{
    	  battleSimBtn.setVisible(false);
      }
      add(battleSimBtn);

      dcLabel = new SRLabel();
      dcLabel.setBounds(5+x,160,90,15);
      dcLabel.setToolTipText("Current hits / maximum hits");
      add(dcLabel);

      nameLabel = new SRLabel();
      nameLabel.setBounds(5+x,130,190,15);
      nameLabel.setToolTipText("The troops name.");
      add(nameLabel);

      locationLabel = new SRLabel();
      locationLabel.setBounds(5+x,145,190,15);
      locationLabel.setToolTipText("The location of the troop (possible on plantes or ships)");
      add(locationLabel);

      destinationLabel = new SRLabel();
      destinationLabel.setBounds(5+x,180,85,15);
      destinationLabel.setToolTipText("Choose a new destination");
      add(destinationLabel);
      
      weaponsLabel = new SRLabel("Weapons");
      weaponsLabel.setToolTipText("The troop Armaments");
      weaponsLabel.setBounds(5,275,140,15);
      weaponsLabel.setVisible(false);
      add(weaponsLabel);
      
      weaponsDamageLabel = new SRLabel("Damge");
      weaponsDamageLabel.setBounds(150,275,60,15);
      weaponsDamageLabel.setToolTipText("Damage the weapon type do");
      weaponsDamageLabel.setVisible(false);
      add(weaponsDamageLabel);

      weaponsInfantryLabel = new SRLabel();
      weaponsInfantryLabel.setToolTipText("Weapons against infantry units");
      weaponsInfantryLabel.setBounds(5+x,295,130,15);
      add(weaponsInfantryLabel);

      weaponsInfantryLabel2 = new SRLabel();
      weaponsInfantryLabel2.setBounds(150,295,30,15);
      weaponsInfantryLabel2.setToolTipText("Damge against infantry units");
      add(weaponsInfantryLabel2);

      weaponsArmorLabel = new SRLabel();
      weaponsArmorLabel.setToolTipText("Weapons against armored units");
      weaponsArmorLabel.setBounds(5+x,310,130,15);
      add(weaponsArmorLabel);

      weaponsArmorLabel2 = new SRLabel();
      weaponsArmorLabel2.setToolTipText("Damge against armored units");
      weaponsArmorLabel2.setBounds(150,310,30,15);
      add(weaponsArmorLabel2);

      weaponsArtilleryLabel = new SRLabel();
      weaponsArtilleryLabel.setToolTipText("Artillery");
      weaponsArtilleryLabel.setBounds(5+x,325,210,15);
      add(weaponsArtilleryLabel);

      weaponsArtilleryLabel2 = new SRLabel();
      weaponsArtilleryLabel2.setToolTipText("Artillery damage");
      weaponsArtilleryLabel2.setBounds(150,325,210,15);
      add(weaponsArtilleryLabel2);

      upkeepLabel = new SRLabel();
      upkeepLabel.setBounds(5+x,370,150,15);
      upkeepLabel.setToolTipText("The upkeep each turn ");
      add(upkeepLabel);
      
      killsLabel = new SRLabel();
      killsLabel.setBounds(130,370,150,15);
      killsLabel.setToolTipText("For each kill the troop gives 10% more in attack.");
      add(killsLabel);
      
      
   // VIPs info textarea
      VIPinfoLabel = new SRLabel("VIPs on this troops");
      VIPinfoLabel.setBounds(5,452,120,15);
      VIPinfoLabel.setVisible(false);
      add(VIPinfoLabel);

      VIPInfoTextArea = new SRTextArea();
      //VIPInfoTextArea.setBounds(5,407,250,180);
      VIPInfoTextArea.setEditable(false);
      VIPInfoTextArea.setVisible(false);

      scrollPane2 = new SRScrollPane(VIPInfoTextArea);
      scrollPane2.setBounds(5,467,315,60);
      scrollPane2.setVisible(false);
      add(scrollPane2);
      
      selfDestructCheckBox = new CheckBoxPanel("Selfdestruct");
      selfDestructCheckBox.setBounds(5,532,200,20);
      selfDestructCheckBox.setSelected(false);
      selfDestructCheckBox.addActionListener(this);
      selfDestructCheckBox.setVisible(false);
      selfDestructCheckBox.setToolTipText("Could somtimes be a good ide to lower the total upkeep cost.");
      add(selfDestructCheckBox);
      
      detailsButton = new SRButton("View Details");
      detailsButton.setBounds(220,532,100,18);
      detailsButton.addActionListener(this);
      detailsButton.setVisible(false);
      detailsButton.setToolTipText("Press this button to view more details about this troop.");
      add(detailsButton);
     
  }
  

    private void fillList(){
    	DefaultListModel dlm = (DefaultListModel)troopsList.getModel();
      for (Troop aTroop : troops) {
      	String prefix = "";
      	// add hits/ammo data
      	String dataStr = "�";
      	dataStr += Functions.getDataValue(aTroop.getCurrentDC(), aTroop.getMaxDC());
      	dataStr += "�";
      	prefix += dataStr;
      	if (player.checkTroopToPlanetMove(aTroop)){
      		prefix += "*";
      		dlm.addElement(prefix + aTroop.getUniqueName() + " (--> " + player.getTroopDestinationPlanetName(aTroop) + ")");
      	}else
   		if (player.checkTroopToCarrierMove(aTroop)){
      		prefix += "*";
      		dlm.addElement(prefix + aTroop.getUniqueName() + " (--> " + player.getTroopDestinationCarrierName(aTroop) + ")");
      	}else{
      		if (!aTroop.isSpaceshipTravel()){
      			prefix += "-";
      		}else{
      			prefix += " ";
      		}
      		if(player.getTroopSelfDestruct(aTroop)){
      			dlm.addElement(prefix + aTroop.getUniqueName() + " (selfdestruct)");
      		}else{
      			String carrierString = "";
      			Spaceship tempcarrier = aTroop.getShipLocation();
      			if (tempcarrier != null){
      				carrierString = "(" + tempcarrier.getName() + ")";
      			}
      			dlm.addElement(prefix + aTroop.getUniqueName() + " " + carrierString);
      		}
      	}
      }
      troopsList.updateScrollList();
    }
    
  public void actionPerformed(ActionEvent ae){
	String action = ae.getActionCommand();
  	Logger.finer("actionPerformed: " + ae.toString());
    if (ae.getSource() instanceof CheckBoxPanel){
      	Logger.finer("ae.getSource() instanceof CheckBoxPanel");
      	newOrder((CheckBoxPanel)ae.getSource());
    }else
    if (ae.getSource() instanceof SRButton){
    	Logger.finer("ae.getSource() instanceof SRButton");
    	
    	if (action.equalsIgnoreCase("View Details")){
    		client.showTroopTypeDetails(currentTroop.getTroopType().getUniqueName(), "Yours");
    	}else
    	if (action.equalsIgnoreCase("Add to battleSim")){
    		client.addToLandBattleSim(getTroopsAsString(),"A");
    		client.showLandBattleSim();
    	}
    }else
    if ((ComboBoxPanel)ae.getSource() == destinationchoice){
      	Logger.finer("(ComboBoxPanel)ae.getSource() == destinationchoice");
      	Logger.finer("destinationchoice.getSelectedItem(): " + (String)destinationchoice.getSelectedItem());
		newDestinationOrder((String)destinationchoice.getSelectedItem());
		showTroop(lastSelection);
    }
	emptyList();
	fillList();
	paintComponent(getGraphics());
	paintChildren(getGraphics());

	if(getSelectedTroops().size()> 0){
    	battleSimBtn.setVisible(true);
    }else{
  	  battleSimBtn.setVisible(false);
    }
  }
  
  private String getTroopsAsString(){
	  StringBuffer sb = new StringBuffer();
	  //List<Spaceship> allShips = spaceships;
	  List<Troop> selectedTroops = getSelectedTroops();
	  boolean semicolon = false;
	  for (Troop aTroop : selectedTroops) {
		  if (semicolon){
			  sb.append(";");
		  }
		  sb.append(aTroop.getTroopType().getUniqueName());
		  String abilities = aTroop.getBattleSimAbilities();
		  // append () if needed
		  String vips = player.getGalaxy().getAllBattleSimVipsOnTroop(aTroop);
		  if (!vips.equals("")){
			  if (!abilities.equals("")){
				  abilities += ",";
			  }
			  abilities += vips;
		  }
		  if (!abilities.equals("")){
			  sb.append("(");
			  sb.append(abilities);
			  sb.append(")");
		  }
		  semicolon = true;
	  }
	  return sb.toString();
  }

    
  public void valueChanged(ListSelectionEvent lse){
    if (lse.getSource() instanceof ListPanel){
      if (lse.getValueIsAdjusting()){
      	lastSelection = troopsList.getSelectedItems();
        showTroop(lastSelection);
        if(getSelectedTroops().size()> 0){
        	battleSimBtn.setVisible(true);
        }else{
      	  battleSimBtn.setVisible(false);
        }
      }
    }
  }

  private void newDestinationOrder(String destinationName){
    if (destinationName.equalsIgnoreCase("(choose destination)")){
    	// do nothing
    	Logger.finer("Do nothing");
    }else{
        List<Troop> selectedTroops = getSelectedTroops();
    	if (destinationName.equalsIgnoreCase("None")){
    		for (Troop aTroop : selectedTroops) {
				if (!player.getTroopSelfDestruct(aTroop)){
					player.addTroopToCarrierMove(aTroop,null);
					player.addTroopToPlanetMove(aTroop,null);
					Logger.finest("New order, remove");
				}
			}
    	}else{ 
    		// destination is maybe a planet
    		Planet newDestination = player.getGalaxy().findPlanet(destinationName);
    		if (newDestination != null){
    			// destination is a planet
        		for (Troop aTroop : selectedTroops) {
    				if (!player.getTroopSelfDestruct(aTroop)){
    					player.addTroopToCarrierMove(aTroop,null);
    					player.addTroopToPlanetMove(aTroop,newDestination);
    					Logger.finest("New order, add " + destinationName + " " + aTroop.getUniqueShortName());
    				}
    			}
    		}else{
    			// destination is a carrier
        		Spaceship destinationCarrier = findSpaceship(destinationName);
        		for (Troop aTroop : selectedTroops) {
    				if (!player.getTroopSelfDestruct(aTroop)){
    					player.addTroopToPlanetMove(aTroop,null);
    					player.addTroopToCarrierMove(aTroop,destinationCarrier);
    					Logger.finest("New order, add carrier move" + destinationName + " " + aTroop.getUniqueShortName());
    				}
    			}
    		}
    	}
    }
    client.updateTreasuryLabel();
  }


  private List<Troop> getSelectedTroops(){
  	List<Troop> selectedTroops = new ArrayList<Troop>();
  	List<Integer> selectedIndexes = troopsList.getSelectedItems();
  	for (Integer anIndex : selectedIndexes) {		
		int tmpIndex = anIndex.intValue();
		selectedTroops.add(troops.get(tmpIndex));
	}
  	return selectedTroops;
  }
  
    private void newOrder(CheckBoxPanel cb){
      if (cb == selfDestructCheckBox){
        if (cb.isSelected()){
          // set up troop for destruction
          player.addTroopSelfDestruct(currentTroop);
          // remove any old moveorder for that ship
          player.addTroopToPlanetMove(currentTroop,null);
          player.addTroopToCarrierMove(currentTroop,null);
          // set choice to "none"
          if (destinationchoice.getItemCount() > 0){
          	destinationchoice.setSelectedIndex(0);
          }
          // disable destinationchoice
          destinationchoice.setEnabled(false);
        }else{
          // remove this troop from selfdestruction
          player.removeTroopSelfDestruct(currentTroop);
          // enable destinationchoice
          if (currentTroop.isSpaceshipTravel()){
          	destinationchoice.setEnabled(true);
          }
        }
      }
      client.updateTreasuryLabel();
    }
    

    private Spaceship findSpaceship(String findName){
      Spaceship ss = null;
      int i = 0;
      List<Spaceship> spaceships = player.getGalaxy().getPlayersSpaceshipsOnPlanet(player,planet);
      while ((ss == null) & (i<spaceships.size())){
        Spaceship temp = spaceships.get(i);
        if (temp.getName().equalsIgnoreCase(findName)){
          ss = temp;
        }
        i++;
      }
      return ss;
    }

    private List<Troop> getTroopsList(List<Integer> selectedTroopIndexes){
    	List<Troop> tmpTroops = new LinkedList<Troop>();
    	for (Integer index : selectedTroopIndexes) {
            Troop aTroop = troops.get(index);
            tmpTroops.add(aTroop);
		}
    	return tmpTroops;
    }
    
    private void showTroop(List<Integer> selectedTroopIndexes){
      List<Troop> selectedTroops = getTroopsList(selectedTroopIndexes);
      if (selectedTroops.size() == 1){ // if only 1 troop selected
        Troop aTroop = selectedTroops.get(0);
        currentTroop = aTroop;

        weaponsLabel.setVisible(true);
        weaponsDamageLabel.setVisible(true);
        nameLabel.setText(aTroop.getUniqueName() + " (" + aTroop.getUniqueShortName()+ ")");
        locationLabel.setText("Location: " + aTroop.getLocationString());
        weaponsInfantryLabel.setText("vs Infantry:");
        weaponsInfantryLabel2.setText(String.valueOf(aTroop.getAttackInfantry()));
        weaponsArmorLabel.setText("vs Armor:");
        weaponsArmorLabel2.setText(String.valueOf(aTroop.getAttackArmored()));
        if (aTroop.getAttackArtillery() > 0){
        	weaponsArtilleryLabel.setText("Artillery:");
        	weaponsArtilleryLabel2.setText(String.valueOf(aTroop.getAttackArtillery()));
        }else{
        	weaponsArtilleryLabel.setText("");
        	weaponsArtilleryLabel2.setText("");
        }
        dcLabel.setText("Hits: " + aTroop.getCurrentDC()+ "/" + aTroop.getMaxDC());
        destinationLabel.setText("Destination: ");
        upkeepLabel.setText("Upkeep: " + aTroop.getUpkeep());
        killsLabel.setText("Kills: " + aTroop.getKills());
        detailsButton.setVisible(true);

        // show and set selfdestruct cb
        selfDestructCheckBox.setSelected(player.getTroopSelfDestruct(aTroop));
        selfDestructCheckBox.setVisible(true);
        
        // remove old destinationchoice
        if (destinationchoice != null){
          remove(destinationchoice);
          destinationchoice = null;
        }
        // create new destinationchoice
        destinationchoice = new ComboBoxPanel();
        destinationchoice.setBounds(5,195,315,20);
        destinationchoice.addActionListener(this);
        this.add(destinationchoice);
        destinationchoice.setVisible(true);

        // set properties and initial value
        if (player.isBrokeClient() | player.isRetreatingGovenor() | !aTroop.isSpaceshipTravel() | player.getTroopSelfDestruct(aTroop)){
        	destinationchoice.setEnabled(false);
        }else{
        	destinationchoice.setEnabled(true);
        	// add possible destinations for this troop
        	addDestinations(destinationchoice, selectedTroops);
            // if a troop has a full carrier as destination, we must add the carrier otherwise to the combobox
            String tempDest = player.getTroopDestinationCarrierName(aTroop);
            if (!tempDest.equals("") & !destinationchoice.contains(tempDest)){
            	destinationchoice.addItem(tempDest);
            }
        }
        String tempDest = player.getTroopDestinationPlanetName(aTroop);
        if (tempDest.equals("")){
        	tempDest = player.getTroopDestinationCarrierName(aTroop);
        }
        if (!tempDest.equalsIgnoreCase("")){
        	destinationchoice.setSelectedItem(tempDest);
        }else{
        	destinationchoice.setSelectedItem("None");
        }
        addVIPs();
        VIPinfoLabel.setVisible(true);
        VIPInfoTextArea.setVisible(true);
        scrollPane2.setVisible(true);
      }else{ // multiple troops are selected
      	Logger.finest("show multiple troops");

        nameLabel.setText("");
        locationLabel.setText("");
        weaponsLabel.setVisible(false);
        weaponsDamageLabel.setVisible(false);
        weaponsInfantryLabel.setText("");
        weaponsInfantryLabel2.setText("");
        weaponsArmorLabel.setText("");
        weaponsArmorLabel2.setText("");
        weaponsArtilleryLabel.setText("");
        weaponsArtilleryLabel2.setText("");
        weaponsArtilleryLabel.setText("");
        weaponsArtilleryLabel2.setText("");
        dcLabel.setText("");
        destinationLabel.setText("");
        upkeepLabel.setText("");
        killsLabel.setText("");
        detailsButton.setVisible(true);

        if (destinationchoice != null){
        	remove(destinationchoice);
        	destinationchoice = null;
        }

        destinationchoice = new ComboBoxPanel();
        destinationchoice.setBounds(5,195,315,20);;
        destinationchoice.addActionListener(this);
        this.add(destinationchoice);
        destinationchoice.setVisible(true);
    	
        selfDestructCheckBox.setVisible(false);
        detailsButton.setVisible(false);
        VIPinfoLabel.setVisible(false);
        VIPInfoTextArea.setVisible(false);
        scrollPane2.setVisible(false);
        // set destination
        destinationLabel.setText("Destination: ");
     //   positionLabel.setText("Battle position");
        boolean noSelfdestruct = true;
        boolean allCanMove = true;
        for (Troop aTroop : selectedTroops) {
			if (!aTroop.isSpaceshipTravel()){
				allCanMove = false;
			}
			if (player.getTroopSelfDestruct(aTroop)){
				noSelfdestruct = false;
			}
		}
        if (player.isBrokeClient() | player.isRetreatingGovenor() | allCanMove | noSelfdestruct){
            destinationchoice.setEnabled(false);
        }else{
        	destinationchoice.setVisible(true);
        	addDestinations(destinationchoice, selectedTroops);
        }
      }
      repaint();
    }
    
    private void addVIPs(){
      List<VIP> vipsOnTroop = player.getGalaxy().findAllVIPsOnTroop(currentTroop);
      if (vipsOnTroop.size() == 0){
        VIPInfoTextArea.setText("None");
      }else{
        VIPInfoTextArea.setText("");
        for (VIP aVip : vipsOnTroop) {
        	VIPInfoTextArea.append(aVip.getName() + "\n");
		}
      }
    }
    
    private int countNrTroopsOnPlanet(List<Troop> troops){
    	int count = 0;
    	Logger.fine("countNrTroopsOnPlanet. troops.size(): " + troops.size());
    	for (Troop aTroop : troops) {
			if (aTroop.getPlanetLocation() != null){
				count++;
			}
		}
    	Logger.fine("count: " + count);
    	return count;
    	
    }
    
    private boolean checkPlanetMoveOk(List<Troop> troops){
    	boolean planetMoveOk = true;
    	Logger.fine("checkPlanetMoveOk: " + planet.getPlayerInControl());
    	// check if any of the troops already are on the planet
    	if (countNrTroopsOnPlanet(troops) > 0){
    		Logger.fine("countNrTroopsOnPlanet(troops): " + countNrTroopsOnPlanet(troops));
        	// if so, planet move is not ok
    		planetMoveOk = false;
    	}else
    	// else if planet is razed, planet move may not be ok
    	if (planet.isRazed()){
    		// if the player is alone at the razed planet move is ok
    		if (!playerIsAloneAtPlanet(planet)){
        		// if the player already have troops at the planet move is ok
        		if (!playerHaveTroopsOnPlanet(planet)){
            		planetMoveOk = false;
        		}
    		}
    	}else
    	// else if planet is friendly (and non-neutral), planet move is not ok
    	if ((planet.getPlayerInControl() != null) && (planet.getPlayerInControl() != player) && player.getGalaxy().getDiplomacy().getDiplomacyState(player,planet.getPlayerInControl()).getCurrentLevel().isHigher(DiplomacyLevel.WAR)){
    		Logger.fine("Planet friendly (or at least not war/ewar).");
    		planetMoveOk = false;
    	}else
    	// else if planet is neutral or enemy...
    		//TODO  Denna borde nte fungera d� man inte kan lita p� att det �r en annan faction som �r fienden.
    	if (planet.isEnemyOrNeutralPlanet(player)){
    		Logger.fine("planet.isEnemyOrNeutralPlanet(player.getFaction()): " + planet.isEnemyOrNeutralPlanet(player));
    		// ...check if there are any defenders
    		if (player.getGalaxy().getShips(planet).size() > 0){
    			Logger.fine("player.getGalaxy().getShips(planet).size(): " + player.getGalaxy().getShips(planet).size());
    			// if there are, planet move is not ok, unless...
    			// ... the attacking player already have players on the planet planet move is ok
        		if (!playerHaveTroopsOnPlanet(planet)){
        			Logger.fine("!playerHaveTroopsOnPlanet(planet): " + !playerHaveTroopsOnPlanet(planet));
        			planetMoveOk = false;
        		}
    		}/*else
    	    // else if the moves exceed the drop limit of the carriers the troops are in, move is not ok
    	    if (dropLimitExceeded(troops)){
    	    	Logger.fine("dropLimitExceeded(troops): " + dropLimitExceeded(troops));
        		planetMoveOk = false;
    	    }*/
    	}
    	// check if planet is neutral and "attack if neutral" is checked
    	if((!player.getPlanetOrderStatuses().isAttackIfNeutral(planet.getName()) && planet.getPlayerInControl() == null)){
    		Logger.fine("player.getPlanetInfos().getAttackIfNeutral(planet.getName()) && planet.getPlayerInControl() == null): " + !(player.getPlanetOrderStatuses().isAttackIfNeutral(planet.getName()) && planet.getPlayerInControl() == null));
    		
    		Logger.fine("player.getPlanetInfos().getAttackIfNeutral(planet.getName()): " + player.getPlanetOrderStatuses().isAttackIfNeutral(planet.getName()));
    		Logger.fine("planet.getPlayerInControl() == null: " + (planet.getPlayerInControl() == null));
    		
    		planetMoveOk = false;
    	}	// check if planet in a fi player control and "DoNotBesiege" is unchecked.
    	if((planet.getPlayerInControl() != null && player.getPlanetOrderStatuses().isDoNotBesiege(planet.getName()))){
    		Logger.fine("!(planet.getPlayerInControl() != null && player.getPlanetInfos().getDoNotBesiege(planet.getName())): " + (planet.getPlayerInControl() != null && player.getPlanetOrderStatuses().isDoNotBesiege(planet.getName())));
    		
    		planetMoveOk = false;
    	}
    		
    	
    	return planetMoveOk;
    }
    
    /**
     * Return true if the player have at least one troop on thePlanet
     * @param thePlanet
     * @return
     */
    private boolean playerHaveTroopsOnPlanet(Planet thePlanet){
    	List<Troop> troopsOnPlanet = player.getGalaxy().findAllTroopsOnPlanet(planet);
    	boolean found = false;
    	int index = 0;
    	while (!found & (index < troopsOnPlanet.size())){
    		if (troopsOnPlanet.get(index).getOwner() == player){
    			found = true;
    		}else{
    			index++;
    		}
    	}
    	return found;
    }

    /**
     * Returns true if therer are at least one military ship belonging to another player or are neutral
     * @param thePlanet
     * @return
     */
    private boolean playerIsAloneAtPlanet(Planet thePlanet){
		List<Spaceship> shipsAtPlanet = player.getGalaxy().getShips(planet, false);
    	boolean found = false;
    	int index = 0;
    	while (!found & (index < shipsAtPlanet.size())){
    		if (shipsAtPlanet.get(index).getOwner() != player){
    			found = true;
    		}else{
    			index++;
    		}
    	}
    	return !found;
    }

    private void addDestinations(ComboBoxPanel dc, List<Troop> selectedTroops){
    	// if multiple selection show "(choose destination)" in top of list
        if (selectedTroops.size() > 1){
          dc.addItem("(choose destination)");
        }
        // check if "none" is ok, if ok add it to list
        if (checkNoneOk(selectedTroops)){
        	dc.addItem("None");
        }
        // check if move to planet is ok (limited moves when attacking an enemy planet)
        boolean planetMoveOk = checkPlanetMoveOk(selectedTroops);
        Logger.fine("checkPlanetMoveOk(selectedTroops) called : " + checkPlanetMoveOk(selectedTroops));
        // add planet if it is ok
        if (planetMoveOk){
        	Logger.fine("adding planet name : " + planet.getName());
        	dc.addItem(planet.getName());
        }
        // get all carriers with enough free slots to carry all selected ships, and add then to a tmp list
        List<Spaceship> carriers = getCarriers(selectedTroops.size());
//        Logger.fine("carriers: " + carriers.size() + " " + carriers);
        for (Spaceship aCarrier : carriers) {
        	// for each carrier chack that none of the selected troops already are in the carrier
        	if (checkCarrierLocation(aCarrier,selectedTroops)){
        		// add carrier to the list
        		dc.addItem(aCarrier.getName());
        	}
        }
    }

    
    /**
     * Return false if any troops are in the carrier
     * @return
     */
    private boolean checkCarrierLocation(Spaceship aCarrier,List<Troop> troops){
    	boolean noneInCarrier = true;
    	for (Troop aTroop : troops) {
			if (aTroop.getShipLocation() == aCarrier){
				noneInCarrier = false;
			}
		}
    	return noneInCarrier;
    }
    
    private Set<Spaceship> getCarriers(List<Troop> troops){
    	// create list of the carriers the troops are in
    	Set<Spaceship> carriers = new LinkedHashSet<Spaceship>();
    	for (Troop aTroop : troops) {
    		carriers.add(aTroop.getShipLocation());
    	}
    	return carriers;
    }

    /**
     * If there are a troop who has been ordered to move from a carrier,
     * selecting "none" can cause that carrier to carry more troops than
     * its capacity, and then "none" can not be allowed
     * @param selectedTroops
     * @return true if "None" is allowed for this selection of troops
     */
    private boolean checkNoneOk(List<Troop> selectedTroops){
	  Logger.fine("checkNoneOk called");
	  boolean noneOk = true;
	  // create a list with all troops in carriers who have been given orders to move away
	  List<Troop> movingTroops = new LinkedList<Troop>();
	  for (Troop aTroop : selectedTroops) {
		  if (aTroop.getShipLocation() != null){
			  if (player.checkTroopMove(aTroop)){
				  movingTroops.add(aTroop);
			  }
		  }
	  }
	  // if list is not empty 
	  if (movingTroops.size() > 0){
		  // create list of the carriers the troops are in
		  Set<Spaceship> carriers = getCarriers(movingTroops);
	  	  // for each carrier
		  for (Spaceship aCarrier : carriers) {
	  		  // count the number of free slots (including troops moving to the carrier)
			  int nrTroopsAssigned = player.getGalaxy().getNoTroopsAssignedToCarrier(aCarrier);
			  int nrTroopsOrdered = player.countTroopToCarrierMoves(aCarrier);
			  int freeSlots = aCarrier.getTroopCapacity() - nrTroopsAssigned - nrTroopsOrdered;
	  		  // count the number of selected troops in that carrier (who have been given order to move away)
			  int nrTroops = 0;
			  for (Troop aTroop : movingTroops) {
				  if (aTroop.getShipLocation() == aCarrier){
					  nrTroops++;
				  }
			  }
	  		  // if there are fewer slots than troops that have been ordered to move away
			  if (nrTroops > freeSlots){
				  // none is not ok
				  noneOk = false;
			  }
		  }
	  }
	  return noneOk;
    }

    private List<Spaceship> getCarriers(int minFreeSlots){
    	List<Spaceship> carriers = player.getGalaxy().getCarriersWithFreeTroopSlotsInSystem(planet, player, minFreeSlots);
    	return carriers;
    }

    private void emptyList(){
      DefaultListModel dlm = (DefaultListModel)troopsList.getModel();
      dlm.removeAllElements();
    }

    public void updateData(){
      if (currentTroop != null){
        showTroop(lastSelection);
        emptyList();
        fillList();
        
      }
    }
    
    
}
