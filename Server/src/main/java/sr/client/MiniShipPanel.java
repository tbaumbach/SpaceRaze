package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
import sr.enums.SpaceshipRange;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.Troop;
import sr.world.VIP;
import sr.world.comparator.spaceship.SpaceshipTypeAndBuildCostComparator;

/**
 * Shows all ships at a planet and player can give orders to those ships
 * @author wmpabod
 *
 */
public class MiniShipPanel extends SRBasePanel implements ActionListener,ListSelectionListener {
  private static final long serialVersionUID = 1L;
  private List<Spaceship> spaceships = new ArrayList<Spaceship>();
  private ListPanel shiplist;
  private SRLabel destinationLabel,shieldsLabel,dcLabel,killsLabel,VIPinfoLabel,weaponsSquadronLabel,weaponsSquadronLabel2;
  private SRLabel weaponsSmallLabel,weaponsMediumLabel,weaponsLargeLabel,weaponsHugeLabel;
  private SRLabel weaponsSmallLabel2,weaponsMediumLabel2,weaponsLargeLabel2,weaponsHugeLabel2;
  private SRLabel weaponsMediumLabel3,weaponsLargeLabel3,weaponsHugeLabel3;
  private SRLabel weaponsLabel, weaponsDamageLabel, weaponsAmmoLabel;
  private SRLabel upkeppLabel,nameLabel;
  private SRLabel carrierInfo, motherShipInfo2, motherShipInfo, troopInfo;
//  private SRLabel troopInfo2;
  private SRButton detailsButton, autoFillCarrierButton, autoFillCarrierButtonTroops, battleSimBtn;
  private Player player;
  private ComboBoxPanel destinationchoice;
  private Spaceship currentss;
  private CheckBoxPanel selfDestructCheckBox,screenedCheckBox;
  private SRLabel screenedLabel;
  private SRTextArea VIPInfoTextArea;
  private int x = 0;
  private List<Integer> lastSelection = null;
  private JScrollPane scrollPane2;
  private SpaceRazeApplet client;
  private Planet planet;

  public MiniShipPanel(List<Spaceship> spaceships, Player player, SpaceRazeApplet client, Planet aPlanet) {
      this.spaceships = spaceships;
      Collections.sort(spaceships,new SpaceshipTypeAndBuildCostComparator());
      this.player = player;
      this.setLayout(null);
      this.client = client;
      this.planet = aPlanet;
 
      shiplist = new ListPanel();
      shiplist.setBounds(5,5,315,100);
      shiplist.setListSelectionListener(this);
      shiplist.setMultipleSelect(true);
      add(shiplist);
      fillList();
      
      battleSimBtn = new SRButton("Add to battleSim");
      battleSimBtn.setBounds(168,110,152,14);
      battleSimBtn.addActionListener(this);
      battleSimBtn.setToolTipText("Press this button to add selected ships to battle sim");
      if(getSelectedSpaceships().size()> 0){
    	  battleSimBtn.setVisible(true);
      }else{
    	  battleSimBtn.setVisible(false);
      }
      add(battleSimBtn);
      
      nameLabel = new SRLabel();
      nameLabel.setBounds(5+x,130,210,15);
      nameLabel.setToolTipText("The name of the ship");
      add(nameLabel);

      shieldsLabel = new SRLabel();
      shieldsLabel.setBounds(5+x,145,90,15);
      shieldsLabel.setToolTipText("Shield size");
      add(shieldsLabel);

      dcLabel = new SRLabel();
      dcLabel.setBounds(165,145,130,15);
      dcLabel.setToolTipText("Current hits / maximum hits");
      add(dcLabel);

      destinationLabel = new SRLabel();
      destinationLabel.setBounds(5+x,160,85,15);
      destinationLabel.setToolTipText("Choose a new destination");
      add(destinationLabel);
 
      screenedCheckBox = new CheckBoxPanel("Screened if possible");
      screenedCheckBox.setBounds(5+x,220,200,15);
      screenedCheckBox.setSelected(false);
      screenedCheckBox.addActionListener(this);
      screenedCheckBox.setToolTipText("You can screen this ship in battle if you are afrid loosing it.");
      add(screenedCheckBox);
      screenedCheckBox.setVisible(false);

      screenedLabel = new SRLabel("Screened if possible");
      screenedLabel.setBounds(5+x,220,200,15);
      add(screenedLabel);
      screenedLabel.setVisible(false);
      
      weaponsLabel = new SRLabel("Weapons");
      weaponsLabel.setToolTipText("The Ship Armaments");
      weaponsLabel.setBounds(5,240,100,15);
      weaponsLabel.setVisible(false);
      add(weaponsLabel);
      
      weaponsDamageLabel = new SRLabel("Damge");
      weaponsDamageLabel.setBounds(130,240,60,15);
      weaponsDamageLabel.setToolTipText("Damage the weapon type do");
      weaponsDamageLabel.setVisible(false);
      add(weaponsDamageLabel);
      
      weaponsAmmoLabel = new SRLabel("Ammo");
      weaponsAmmoLabel.setBounds(195,240,80,15);
      weaponsAmmoLabel.setToolTipText("Current ammo / maximum ammo");
      weaponsAmmoLabel.setVisible(false);
      add(weaponsAmmoLabel);
      
      weaponsSquadronLabel = new SRLabel();
      weaponsSquadronLabel.setBounds(5+x,255,120,15);
      weaponsSquadronLabel.setToolTipText("Weapons against squadrons (infinite ammo)");
      add(weaponsSquadronLabel);

      weaponsSquadronLabel2 = new SRLabel();
      weaponsSquadronLabel2.setBounds(135+x,255,50,15);
      weaponsSquadronLabel2.setToolTipText("Damage against squadrons");
      add(weaponsSquadronLabel2);
      
      weaponsSmallLabel = new SRLabel();
      weaponsSmallLabel.setBounds(5+x,270,120,15);
      weaponsSmallLabel.setToolTipText("Weapons against small and bigger capitals ship (infinite ammo)");
      add(weaponsSmallLabel);

      weaponsSmallLabel2 = new SRLabel();
      weaponsSmallLabel2.setBounds(135+x,270,30,15);
      weaponsSmallLabel2.setToolTipText("Damage against small and bigger capitals ship");
      add(weaponsSmallLabel2);

      weaponsMediumLabel = new SRLabel();
      weaponsMediumLabel.setBounds(5+x,285,120,15);
      weaponsMediumLabel.setToolTipText("Weapons against medium and bigger capitals ship");
      add(weaponsMediumLabel);

      weaponsMediumLabel2 = new SRLabel();
      weaponsMediumLabel2.setBounds(135+x,285,50,15);
      weaponsMediumLabel2.setToolTipText("Damage against medium and bigger capitals ship");
      add(weaponsMediumLabel2);

      weaponsMediumLabel3 = new SRLabel();
      weaponsMediumLabel3.setBounds(200+x,285,80,15);
      weaponsMediumLabel3.setToolTipText("Reloads on plantes with medium or bigger wharfs building");
      add(weaponsMediumLabel3);

      weaponsLargeLabel = new SRLabel();
      weaponsLargeLabel.setBounds(5+x,300,120,15);
      weaponsLargeLabel.setToolTipText("Weapons against large and bigger capitals ship");
      add(weaponsLargeLabel);

      weaponsLargeLabel2 = new SRLabel();
      weaponsLargeLabel2.setBounds(135+x,300,50,15);
      weaponsLargeLabel2.setToolTipText("Damage against large and bigger capitals ship");
      add(weaponsLargeLabel2);

      weaponsLargeLabel3 = new SRLabel();
      weaponsLargeLabel3.setBounds(200+x,300,80,15);
      weaponsLargeLabel3.setToolTipText("Reloads on plantes with large or bigger wharfs building");
      add(weaponsLargeLabel3);

      weaponsHugeLabel = new SRLabel();
      weaponsHugeLabel.setBounds(5+x,315,120,15);
      weaponsHugeLabel.setToolTipText("Weapons against huge capitals ship");
      add(weaponsHugeLabel);
      
      weaponsHugeLabel2 = new SRLabel();
      weaponsHugeLabel2.setBounds(135+x,315,50,15);
      weaponsHugeLabel2.setToolTipText("Damage against huge capitals ship");
      add(weaponsHugeLabel2);
      
      weaponsHugeLabel3 = new SRLabel();
      weaponsHugeLabel3.setBounds(200+x,315,80,15);
      weaponsHugeLabel3.setToolTipText("Reloads on plantes with huge wharfs building");
      add(weaponsHugeLabel3);
      
      upkeppLabel = new SRLabel();
      upkeppLabel.setBounds(5+x,345,100,15);
      upkeppLabel.setToolTipText("Ships upkeep / turn");
      add(upkeppLabel);
      
      killsLabel = new SRLabel();
      killsLabel.setBounds(130,345,45,15);
      killsLabel.setToolTipText("Number of ships shoot down.");
      add(killsLabel);
      
      carrierInfo = new SRLabel("");
      carrierInfo.setBounds(5,370,150,15);
      carrierInfo.setVisible(false);
      add(carrierInfo);
      
      autoFillCarrierButton = new SRButton("Auto add squadrons");
      autoFillCarrierButton.setBounds(168,370,152,14);
      autoFillCarrierButton.addActionListener(this);
      autoFillCarrierButton.setToolTipText("Press this button to fill this carrier with planet based squadrons");
      autoFillCarrierButton.setVisible(false);
      add(autoFillCarrierButton);
      
      troopInfo = new SRLabel("");
      troopInfo.setBounds(5,385,135,15);
      troopInfo.setVisible(false);
      add(troopInfo);
      
      autoFillCarrierButtonTroops = new SRButton("Auto add troops");
      autoFillCarrierButtonTroops.setBounds(168,390,152,14);
      autoFillCarrierButtonTroops.addActionListener(this);
      autoFillCarrierButtonTroops.setToolTipText("Press this button to fill this ship with planet based troops");
      autoFillCarrierButtonTroops.setVisible(false);
      add(autoFillCarrierButtonTroops);
      
      motherShipInfo = new SRLabel("");
      motherShipInfo.setBounds(5,422,315,15);
      motherShipInfo.setVisible(false);
      add(motherShipInfo);
      
      motherShipInfo2 = new SRLabel("");
      motherShipInfo2.setBounds(5,437,315,15);
      motherShipInfo2.setVisible(false);
      add(motherShipInfo2);
      

      // VIPs info textarea
      VIPinfoLabel = new SRLabel("VIPs on this ship");
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
      add(selfDestructCheckBox);
      
      detailsButton = new SRButton("View Details");
      detailsButton.setBounds(220,532,100,18);
      detailsButton.addActionListener(this);
      detailsButton.setVisible(false);
      detailsButton.setToolTipText("Press this button to view more details about this ship.");
      add(detailsButton);
      
     
  }

    private void fillList(){
      DefaultListModel dlm = (DefaultListModel)shiplist.getModel();
      for(int i = 0; i < spaceships.size(); i++){
      	Spaceship tempss = spaceships.get(i);
      	String prefix = "";
      	String slotsString = getCapacitySlotsString(tempss);
      	// add hits/ammo data
      	String dataStr = "¤";
      	dataStr += Functions.getDataValue(tempss.getCurrentDc(), tempss.getDamageCapacity());
      	dataStr += ";";
      	dataStr += Functions.getDataValue(tempss.getWeaponsSalvoesMedium(), tempss.getSpaceshipType().getWeaponsMaxSalvoesMedium());
      	dataStr += ";";
      	dataStr += Functions.getDataValue(tempss.getWeaponsSalvoesLarge(), tempss.getSpaceshipType().getWeaponsMaxSalvoesLarge());
      	dataStr += ";";
      	dataStr += Functions.getDataValue(tempss.getWeaponsSalvoesHuge(), tempss.getSpaceshipType().getWeaponsMaxSalvoesHuge());
      	dataStr += "¤";
      	prefix += dataStr;
      	// add ship to list
      	if (player.checkShipMove(tempss)){
      		prefix += "*";
      		dlm.addElement(prefix + tempss.getName() + slotsString + " --> " + player.getShipDestinationName(tempss));
      	}else
   		if (player.checkShipToCarrierMove(tempss)){
      		prefix += "*";
      		dlm.addElement(prefix + tempss.getName() + slotsString + " --> " + player.getShipDestinationCarrierName(tempss));
      	}else{
      		if (tempss.getRange() == SpaceshipRange.NONE){
      			prefix += "-";
      		}else{
      			prefix += " ";
      		}
      		if(player.getShipSelfDestruct(tempss)){
      			dlm.addElement(prefix + tempss.getName() + slotsString + " (selfdestruct)");
      		}else{
      			if (tempss.isSquadron()){
          			String sqdString = "";
          			Spaceship tempcarrier = tempss.getCarrierLocation();
          			if (tempcarrier != null){
          				sqdString = " (" + tempcarrier.getUniqueName() + ")";
          			}
          			dlm.addElement(prefix + tempss.getName() + slotsString + sqdString);
      			}else{
      				dlm.addElement(prefix + tempss.getName() + slotsString);
      			}
      		}
      	}
      }
      shiplist.updateScrollList();
    }
    
    private String getCapacitySlotsString(Spaceship aSpaceship){
//		Logger.fine("getCarrierSlotsString called");
		String slotsString = "";
		if (aSpaceship.isCarrier()){
//			
			slotsString = "S:" + getCarrierSlotsString(aSpaceship);
		}
		if (aSpaceship.isTroopCarrier()){
			
			slotsString = slotsString + "T:" + getTroopCapacitySlotsString(aSpaceship);
		}
		if (!slotsString.equals("")){
			slotsString = " [" + slotsString + "]";
		}
    	return slotsString;
    }
    
    private String getCarrierSlotsString(Spaceship aSpaceship){
    	String slotsString = "";
		int capacity = aSpaceship.getSquadronCapacity();
    	slotsString =  getTakenSlots(aSpaceship) + "/" + capacity;
    	return slotsString;
    }
    
    private int getTakenSlots(Spaceship aSpaceship){
    	int nrSquadronsAssigned = player.getGalaxy().getNoSquadronsAssignedToCarrier(aSpaceship);
		int nrSquadronsOrdered = player.countShipToCarrierMoves(aSpaceship);
		int takenSlots = nrSquadronsAssigned + nrSquadronsOrdered;
		
		return takenSlots;
    }
   
    
    private String getTroopCapacitySlotsString(Spaceship aSpaceship){
    	String slotsString = "";
    	
		
		int capacity = aSpaceship.getTroopCapacity();
		if (!slotsString.equals("")){
			slotsString = slotsString + "-";
		}
    	slotsString = getTroopsOnShip(aSpaceship) + "/" + capacity;
    	return slotsString;
    }
    
    private int getTroopsOnShip(Spaceship aSpaceship){
    	int nrTroopsAssigned = player.getGalaxy().getNoTroopsAssignedToCarrier(aSpaceship);
		int nrTroopsOrdered = player.countTroopToCarrierMoves(aSpaceship);
		int takenSlots = nrTroopsAssigned + nrTroopsOrdered;
		
		return takenSlots;
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
    		client.showShiptypeDetails(currentss.getSpaceshipType().getName(), "Yours");
    	}else
    	if (action.equalsIgnoreCase("Auto add squadrons")){
//    		 Auto Fill
    		autoFillCarrier();
    		showSpaceship(lastSelection);
    	}else
    	if (action.equalsIgnoreCase("Auto add troops")){
//       		 Auto Fill Carrier
    		autoFillCarrierTroops();
    		showSpaceship(lastSelection);
       	}else
    	if (action.equalsIgnoreCase("Add to battleSim")){
    		client.addToBattleSim(getShipsAsString(), "A");
    		client.showBattleSim();
    	}
    }else
    if ((ComboBoxPanel)ae.getSource() == destinationchoice){
      	Logger.finer("(ComboBoxPanel)ae.getSource() == destinationchoice");
      	Logger.finer("destinationchoice.getSelectedItem(): " + (String)destinationchoice.getSelectedItem());
		newOrder((String)destinationchoice.getSelectedItem());
		showSpaceship(lastSelection);
    }
	emptyList();
	fillList();
	paintComponent(getGraphics());
	paintChildren(getGraphics());
    
    if(getSelectedSpaceships().size()> 0){
    	battleSimBtn.setVisible(true);
    }else{
    	battleSimBtn.setVisible(false);
    }
    
  }
  
  private String getShipsAsString(){
	  StringBuffer sb = new StringBuffer();
	  //List<Spaceship> allShips = spaceships;
	  List<Spaceship> selectedShips = getSelectedSpaceships();
	  boolean semicolon = false;
	  for (Spaceship aShip : selectedShips) {
		  if (!aShip.isCivilian()){
			  if (semicolon){
				  sb.append(";");
			  }
			  sb.append(player.getGalaxy().findSpaceshipType(aShip.getTypeName()).getName());
			  String abilities = aShip.getBattleSimAbilities();
			  // append () if needed
			  String vips = player.getGalaxy().getAllBattleSimVipsOnShip(aShip);
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
	  }
	  return sb.toString();
  }

  public void valueChanged(ListSelectionEvent lse){
    if (lse.getSource() instanceof ListPanel){
      if (lse.getValueIsAdjusting()){
      	lastSelection = shiplist.getSelectedItems();
        showSpaceship(lastSelection);
        if(getSelectedSpaceships().size()> 0){
        	battleSimBtn.setVisible(true);
        }else{
      	  battleSimBtn.setVisible(false);
        }
      }
    }
  }
  
  public void showSpaceship(Spaceship aShip){
	  int shipIndex = spaceships.indexOf(aShip);
	  List<Integer> indexList = new LinkedList<Integer>();
	  indexList.add(shipIndex);
	  lastSelection = indexList;
//	  currentss = aShip;
	  showSpaceship(indexList);
	  shiplist.setSelected(shipIndex);
  }

  private void newOrder(String destinationName){
    Logger.finer("newOrder 2: " + destinationName);
    if (destinationName.equalsIgnoreCase("(choose destination)")){
    	// do nothing
    	Logger.finer("Do nothing");
    }else{
        List<Spaceship> selectedShips = getSelectedSpaceships();
    	if (destinationName.equalsIgnoreCase("None")){
    		for (Iterator<Spaceship> iter = selectedShips.iterator(); iter.hasNext();) {
				Spaceship aShip = iter.next();
				if (!player.getShipSelfDestruct(aShip)){
					player.addShipToCarrierMove(aShip,null);
					player.addShipMove(aShip,null);
					Logger.finest("New order, remove");
				}
			}
    	}else{ 
    		// destination is maybe a planet
    		Planet newDestination = player.getGalaxy().findPlanet(destinationName);
    		if (newDestination != null){
    			// destination is a planet
    			for (Spaceship aShip : selectedShips) {
    				if (!player.getShipSelfDestruct(aShip)){
    					player.addShipToCarrierMove(aShip,null);
    					player.addShipMove(aShip,newDestination);
    					Logger.finest("New order, add " + destinationName + " " + aShip.getShortName());
    				}
    			}
    		}else{
    			// destination is a carrier
        		Spaceship destinationCarrier = findSpaceship(destinationName);
    			for (Spaceship aShip : selectedShips) {
    				if (!player.getShipSelfDestruct(aShip)){
    					player.addShipMove(aShip,null);
    					player.addShipToCarrierMove(aShip,destinationCarrier);
    					Logger.finest("New order, add carrier move" + destinationName + " " + aShip.getShortName());
    				}
    			}
    		}
    	}
    }
    client.updateTreasuryLabel();
  }

  private List<Spaceship> getSelectedSpaceships(){
  	List<Spaceship> selectedShips = new ArrayList<Spaceship>();
  	List<Integer> selectedIndexes = shiplist.getSelectedItems();
  	for (Integer anIndex : selectedIndexes) {
		int tmpIndex = anIndex.intValue();
		Spaceship ss = (Spaceship)spaceships.get(tmpIndex);
		selectedShips.add(ss);
	}
  	return selectedShips;
  }
  
    private void newOrder(CheckBoxPanel cb){
      if (cb == selfDestructCheckBox){
        if (cb.isSelected()){
          // set up ship for destruction
          player.addShipSelfDestruct(currentss);
          // remove any old moveorder for that ship
          player.addShipMove(currentss,null);
          player.addShipToCarrierMove(currentss,null);
          // set choice to "none"
          if (destinationchoice.getItemCount() > 0){
          	destinationchoice.setSelectedIndex(0);
          }
          // disable destinationchoice
          destinationchoice.setEnabled(false);
        }else{
          // remove this ship from selfdestruction
          player.removeShipSelfDestruct(currentss);
          // enable destinationchoice
          if (currentss.getRange().greaterThan(SpaceshipRange.NONE)){
          	destinationchoice.setEnabled(true);
          }
        }
      }else
      if (cb == screenedCheckBox){
        player.getOrders().addOrRemoveScreenedShip(currentss);
      }
      client.updateTreasuryLabel();
    }

    private Spaceship findSpaceship(String findName){
      Spaceship ss = null;
      int i = 0;
      while ((ss == null) & (i<spaceships.size())){
        Spaceship temp = spaceships.get(i);
       /* if (temp.getShortName().equalsIgnoreCase(findShortName)){
          ss = temp;
        }*/
        if (temp.getName().equalsIgnoreCase(findName)){
            ss = temp;
          }
        i++;
      }
      return ss;
    }
/*
    public void paintComponent(Graphics g){
      super.paintComponent(g);
      if ((lastSelection != null) && (lastSelection.size() == 1)){
        g.setFont(new Font("Helvetica",1,12));
        g.setColor(player.getFaction().getPlanetColor());
        g.drawString(currentss.getName(),5,139);
      }
    }
*/
    private void showSpaceship(List<Integer> selectedSpaceships){
      //Spaceship ss = findSpaceship(name);
      if (selectedSpaceships == null){ // kan detta inträffa?
    	nameLabel.setText("");
    	weaponsLabel.setVisible(false);
    	weaponsDamageLabel.setVisible(false);
    	weaponsAmmoLabel.setVisible(false);
        weaponsSmallLabel.setText("");
        weaponsSmallLabel2.setText("");
        weaponsMediumLabel.setText("");
        weaponsMediumLabel2.setText("");
        weaponsMediumLabel3.setText("");
        weaponsLargeLabel.setText("");
        weaponsLargeLabel2.setText("");
        weaponsLargeLabel3.setText("");
        weaponsHugeLabel.setText("");
        weaponsHugeLabel2.setText("");
        weaponsHugeLabel3.setText("");
        destinationLabel.setText("");
        shieldsLabel.setText("");
        dcLabel.setText("");
        upkeppLabel.setText("");
        killsLabel.setText("");
        if (destinationchoice != null){
        	remove(destinationchoice);
        	destinationchoice = null;
        }
        selfDestructCheckBox.setVisible(false);
        detailsButton.setVisible(false);
        VIPinfoLabel.setVisible(false);
        VIPInfoTextArea.setVisible(false);
        scrollPane2.setVisible(false);
        carrierInfo.setVisible(false);
        motherShipInfo2.setVisible(false);
        motherShipInfo.setVisible(false);
        troopInfo.setVisible(false);
  //      troopInfo2.setVisible(false);
      }else
      if (selectedSpaceships.size() == 1){ // if only 1 ship selected
      	Integer indexObject = (Integer)selectedSpaceships.get(0);
      	int index = indexObject.intValue();
        Spaceship ss = spaceships.get(index);
        currentss = ss;
        nameLabel.setText(currentss.getName() + " (" + currentss.getShortName() + ")");
        weaponsLabel.setVisible(true);
    	weaponsDamageLabel.setVisible(true);
    	weaponsAmmoLabel.setVisible(true);
        weaponsSquadronLabel.setText("Squadron:");
        weaponsSquadronLabel2.setText(String.valueOf(ss.getWeaponsStrengthSquadron()));
        weaponsSmallLabel.setText("Small:");
        weaponsSmallLabel2.setText(String.valueOf(ss.getWeaponsStrengthSmall()));
        if (ss.getWeaponsStrengthMedium() > 0){
        	weaponsMediumLabel.setText("Medium:");
        	weaponsMediumLabel2.setText(String.valueOf(ss.getWeaponsStrengthMedium()));
        	if (ss.getMaxWeaponsSalvoesMedium() < Integer.MAX_VALUE){
        		weaponsMediumLabel3.setText("(" + ss.getWeaponsSalvoesMedium() + "/" + ss.getMaxWeaponsSalvoesMedium() + ")");
        	}else{
        		weaponsMediumLabel3.setText("");
        	}
        }else{
        	weaponsMediumLabel.setText("");
        	weaponsMediumLabel2.setText("");
    		weaponsMediumLabel3.setText("");
        }
        if (ss.getWeaponsStrengthLarge() > 0){
        	weaponsLargeLabel.setText("Large:");
        	weaponsLargeLabel2.setText(String.valueOf(ss.getWeaponsStrengthLarge()));
        	if (ss.getMaxWeaponsSalvoesLarge() < Integer.MAX_VALUE){
        		weaponsLargeLabel3.setText("(" + ss.getWeaponsSalvoesLarge() + "/" + ss.getMaxWeaponsSalvoesLarge() + ")");
        	}else{
        		weaponsLargeLabel3.setText("");
        	}
        }else{
        	weaponsLargeLabel.setText("");
        	weaponsLargeLabel2.setText("");
    		weaponsLargeLabel3.setText("");
        }
        if (ss.getWeaponsStrengthHuge() > 0){
        	weaponsHugeLabel.setText("Huge:");
        	weaponsHugeLabel2.setText(String.valueOf(ss.getWeaponsStrengthHuge()));
        	Logger.fine(ss.getMaxWeaponsSalvoesHuge() + " Int.Max=" + Integer.MAX_VALUE);
        	if (ss.getMaxWeaponsSalvoesHuge() < Integer.MAX_VALUE){
        		weaponsHugeLabel3.setText("(" + ss.getWeaponsSalvoesHuge() + "/" + ss.getMaxWeaponsSalvoesHuge() + ")");
        	}else{
        		weaponsHugeLabel3.setText("");
        	}
        }else{
        	weaponsHugeLabel.setText("");
        	weaponsHugeLabel2.setText("");
    		weaponsHugeLabel3.setText("");
        }
        shieldsLabel.setText("Shields: " + ss.getShields());
        dcLabel.setText("Hits: " + ss.getCurrentDc()+ "/" +ss.getDamageCapacity());
        destinationLabel.setText("Destination: ");
        upkeppLabel.setText("Upkeep: " + ss.getUpkeep());
        killsLabel.setText("Kills: " + ss.getKills());
        detailsButton.setVisible(true);

        // show and set selfdestruct cb
        selfDestructCheckBox.setSelected(player.getShipSelfDestruct(ss));
        selfDestructCheckBox.setVisible(true);

        if (player.getGalaxy().getGameWorld().isAdjustScreenedStatus()){
	        // show and set screened cb
	        boolean tempScreened = ss.getScreened();
	        if (player.getOrders().getScreenedShip(ss)){
	          tempScreened = !tempScreened;
	        }
	        screenedCheckBox.setSelected(tempScreened);
        	screenedCheckBox.setVisible(true);
        }else{
        	screenedLabel.setVisible(ss.getScreened());
        }

        // remove old destinationchoice
        if (destinationchoice != null){
          remove(destinationchoice);
          destinationchoice = null;
        }
        // create new destinationchoice
        destinationchoice = new ComboBoxPanel();
        destinationchoice.setBounds(5,175,315,20);
        destinationchoice.addActionListener(this);
        this.add(destinationchoice);
        destinationchoice.setVisible(true);

        // set properties and initial value
        if (player.getGalaxy().getTurn() == 0){
          destinationchoice.setEnabled(false);
          selfDestructCheckBox.setEnabled(false);
        }else
        if ((ss.getRange() == SpaceshipRange.NONE) & (!ss.isSquadron())){
          destinationchoice.setEnabled(false);
        }else
        if (player.isBrokeClient()){
            destinationchoice.setEnabled(false);
        }else
        if (player.isRetreatingGovenor()){
            destinationchoice.setEnabled(false);
        }else{
          if (ss.isRetreating()){ // cannot happen, retreating ships are not included in selectable list?
            if (ownPlanetsWithinRange(ss)){
              destinationchoice.setEnabled(true);
              // add possible destinations for this ship
              addRetreatingDestinations(destinationchoice, ss);
              destinationchoice.setSelectedItem(ss.getRetreatingTo().getName());
            }else{
              destinationchoice.setEnabled(false);
              destinationchoice.addItem(ss.getRetreatingTo().getName());
              destinationchoice.setSelectedItem(ss.getRetreatingTo().getName());
            }
            // ships cannot be selfdestructed when running away
            selfDestructCheckBox.setEnabled(false);
          }else{
            if (player.getShipSelfDestruct(ss)){  // if this ship is to be destroyed no destination can be set
              destinationchoice.setEnabled(false);
            }else{
              destinationchoice.setEnabled(true);
            }
            // add possible destinations for this ship
            addDestinations(destinationchoice, ss.getLocation(), ss.getRange(),selectedSpaceships);
            // if a squadron has a full carrier as destination, we must add the carrier otherwise to the combobox
            String tempDest = player.getShipDestinationCarrierName(ss);
            Logger.fine("ss.isSquadron(): " + ss.isSquadron());
            Logger.fine("tempDest: " + tempDest);
            Logger.fine("!destinationchoice.contains(tempDest): " + !destinationchoice.contains(tempDest));
            if (ss.isSquadron() & !tempDest.equals("") & !destinationchoice.contains(tempDest)){
            	destinationchoice.addItem(tempDest);
            }
          }
          String tempDest = player.getShipDestinationName(ss);
          if (tempDest.equals("")){
        	  tempDest = player.getShipDestinationCarrierName(ss);
          }
          if (!tempDest.equalsIgnoreCase("")){
	          destinationchoice.setSelectedItem(tempDest);
          }else{
            destinationchoice.setSelectedItem("None");
          }
        }
        addVIPs();
        VIPinfoLabel.setVisible(true);
        VIPInfoTextArea.setVisible(true);
        scrollPane2.setVisible(true);
        
        carrierInfo.setVisible(false);
    	troopInfo.setVisible(false);
    	motherShipInfo.setVisible(false);
		motherShipInfo2.setVisible(false);
//		troopInfo2.setVisible(false);
        
        if(ss.isCarrier()){
        	carrierInfo.setText("Sqd capacity:    " + getCarrierSlotsString(ss));
        	carrierInfo.setToolTipText(getTakenSlots(ss)  + " squodron/s are carried and the maximum capacity is " + ss.getSquadronCapacity());
        	carrierInfo.setVisible(true);
        	
        }
        if(ss.isSquadron()){
        	
        	if(player.checkShipMove(ss)){
        		
        		for(int i=0; i < player.getGalaxy().getPlanets().size(); i++){
        			Planet destanationPlanet = ((Planet)player.getGalaxy().getPlanets().get(i));
        			if(destanationPlanet.getName().equalsIgnoreCase(player.getShipDestinationName(ss))){
        				boolean spy = (player.getGalaxy().findVIPSpy(destanationPlanet,player) != null);
        				if(destanationPlanet.isFactionPlanet(player.getFaction())){
        					if(destanationPlanet.isPlayerPlanet() || destanationPlanet.isOpen() || (player.getGalaxy().playerHasShipsInSystem(player,planet)) || spy){
        						motherShipInfo.setText("Will be supplyed by planet");
            					motherShipInfo.setToolTipText("A sqd needs supply to survive");
            					motherShipInfo2.setText(player.getShipDestinationName(ss));
            					motherShipInfo2.setToolTipText("The destination is friendly planet");
            					motherShipInfo.setVisible(true);
            					motherShipInfo2.setVisible(true);
            					
        					}else{ // No info about the planet
        						
        					}
        					
        				}else{
        					if(destanationPlanet.isOpen() || (player.getGalaxy().playerHasShipsInSystem(player,planet)) || spy){
        						motherShipInfo.setText("Will lost supply!");
            					motherShipInfo.setToolTipText("Be sure to take the planet or to have a free slot in a carrier");
            					motherShipInfo2.setText("A sqd dies without supply");
            					motherShipInfo2.setToolTipText("Be sure to take the planet or to have a free slot in a carrier");
            					motherShipInfo.setVisible(true);
            					motherShipInfo2.setVisible(true);
            					
        					}else{// No info about the planet
        						
        					}
        				}
        				break;
        			}
        		}
        	}else if(player.checkShipToCarrierMove(ss) || ss.getCarrierLocation() != null){
        		motherShipInfo.setText("Carried and supplyed by");
        		motherShipInfo.setToolTipText("A sqd needs supply to survive");
        		
      			Spaceship tempcarrier = ss.getCarrierLocation();
      			if (tempcarrier != null){
      				motherShipInfo2.setText(tempcarrier.getName());
      			}else{
      				motherShipInfo2.setText(player.getShipDestinationCarrierName(ss));
      			}
          		motherShipInfo2.setToolTipText("A sqd needs a carrier at non friendly planets");
        		motherShipInfo.setVisible(true);
        		motherShipInfo2.setVisible(true);
        		
        	}else{// No orders and on the planet.
        		if(planet.isFactionPlanet(player.getFaction())){
        			motherShipInfo.setText("Supplyed by planet.");
        			motherShipInfo.setToolTipText("A sqd needs supply to survive");
        			motherShipInfo.setVisible(true);
        			
        		}else{ // enemy or neutral  planet
        			// Skall inte kunna hända.
        		}
        	}
        }
        if(ss.isTroopCarrier()){
        	troopInfo.setText("Troop capacity:    " + getTroopCapacitySlotsString(ss));
        	troopInfo.setToolTipText("This ship have "+ getTroopsOnShip(ss) + " troops on board and have place for " + (ss.getTroopCapacity() - getTroopsOnShip(ss)) + " more troops");
       // 	troopInfo2.setText("Troop download capacity:    " + ss.getTroopLaunchCapacity());
       // 	troopInfo2.setToolTipText("This ship can download " + ss.getTroopLaunchCapacity() + " troops each turn to a hostile planet that are under yours besige.");
        	troopInfo.setVisible(true);
    //    	troopInfo2.setVisible(true);
        }
        
        
        if(haveSqdOutSideCarrier(spaceships, ss)){
        	autoFillCarrierButton.setVisible(true);
        }else{
        	autoFillCarrierButton.setVisible(false);
        }
        if(haveTroopsOutsideCarrier(ss)){
        	autoFillCarrierButtonTroops.setVisible(true);
        }else{
        	autoFillCarrierButtonTroops.setVisible(false);
        }
//        paintChildren(getGraphics());
//        shiplist.paintAll(shiplist.getGraphics());
      }else{ // multiple ships are selected
      	Logger.finest("show multiple ships");
      	nameLabel.setText("");
      	weaponsLabel.setVisible(false);
    	weaponsDamageLabel.setVisible(false);
    	weaponsAmmoLabel.setVisible(false);
        weaponsSquadronLabel.setText("");
        weaponsSquadronLabel2.setText("");
        weaponsSmallLabel.setText("");
        weaponsSmallLabel2.setText("");
        weaponsMediumLabel.setText("");
        weaponsMediumLabel2.setText("");
        weaponsMediumLabel3.setText("");
        weaponsLargeLabel.setText("");
        weaponsLargeLabel2.setText("");
        weaponsLargeLabel3.setText("");
        weaponsHugeLabel.setText("");
        weaponsHugeLabel2.setText("");
        weaponsHugeLabel3.setText("");
        shieldsLabel.setText("");
        dcLabel.setText("");
        upkeppLabel.setText("");
        killsLabel.setText("");
        carrierInfo.setVisible(false);
        troopInfo.setVisible(false);
    //    troopInfo2.setVisible(false);
        autoFillCarrierButton.setVisible(false);
        autoFillCarrierButtonTroops.setVisible(false);
        motherShipInfo.setVisible(false);
		motherShipInfo.setVisible(false);
        if (destinationchoice != null){
        	remove(destinationchoice);
        	destinationchoice = null;
            destinationchoice = new ComboBoxPanel();
            destinationchoice.setBounds(5,175,315,20);
            destinationchoice.addActionListener(this);
            this.add(destinationchoice);
        }
        selfDestructCheckBox.setVisible(false);
        screenedCheckBox.setVisible(false);
        detailsButton.setVisible(false);
        VIPinfoLabel.setVisible(false);
        VIPInfoTextArea.setVisible(false);
        scrollPane2.setVisible(false);
        if (player.isBrokeClient()){
            destinationLabel.setText("Destination: ");
            destinationchoice.setEnabled(false);
        }else
        if (player.isRetreatingGovenor()){
            destinationLabel.setText("Destination: ");
            destinationchoice.setEnabled(false);
        }else{
            SpaceshipRange shortestRange = SpaceshipRange.NONE;
        	// check shortest range on selected ships
        	shortestRange = getShortestRange(selectedSpaceships);
        	Logger.finest("shortestRange: " + shortestRange);
//            if (shortestRange.canMove()){
                destinationLabel.setText("Destination: ");
                // create new destinationchoice
                destinationchoice.setVisible(true);
                // add possible destinations for this ship
                Planet aLocation = ((Spaceship)spaceships.get(0)).getLocation();
//                addDestinations(destinationchoice, aLocation, shortestRange,true);
                addDestinations(destinationchoice, aLocation, shortestRange,selectedSpaceships);
//            }else{
//                destinationchoice.setVisible(false);
//            	destinationLabel.setText("");
//            }
        }
        // create destinationchoice based on shortest range
      }
      repaint();
      //paint(getGraphics());
    }
    
    private SpaceshipRange getShortestRange(List<Integer> selectedSpaceships){
    	SpaceshipRange shortestRange = SpaceshipRange.LONG;
//    	DefaultListModel dlm = shiplist.getModel();
    	for (Integer index : selectedSpaceships) {
			Spaceship tempss = spaceships.get(index.intValue());
			if (tempss.getRange().lesserThan(shortestRange)){
				shortestRange = tempss.getRange();
			}
		}
    	return shortestRange;
    }

    private void addVIPs(){
      List<VIP> allVIPs = player.getGalaxy().findAllVIPsOnShip(currentss);
      if (allVIPs.size() == 0){
        VIPInfoTextArea.setText("None");
      }else{
        VIPInfoTextArea.setText("");
        for (VIP aVIP : allVIPs){
          VIPInfoTextArea.append(aVIP.getName() + "\n");
        }
      }
    }

    private void addDestinations(ComboBoxPanel dc, Planet location, SpaceshipRange range, List<Integer> selectedShips){
    	Logger.fine("addDestinations called: " + location.getName() + " " + selectedShips.size());
        if (selectedShips.size() > 1){
          dc.addItem("(choose destination)");
        }
        int countSquadrons = 0;
        int countCapitalShips = 0;
        for (Object object : selectedShips) {
        	int index = (Integer)object;
			Spaceship ss = (Spaceship)spaceships.get(index);
        	Logger.fine("foreach: " + ss.getName());
			if (ss.isSquadron()){
				countSquadrons++;
	        	Logger.fine("countSquadrons: " + countSquadrons);
			}else{
				countCapitalShips++;
	        	Logger.fine("countCapitals: " + countCapitalShips);
			}
		}
        if (checkNoneOk(selectedShips,countSquadrons)){
        	dc.addItem("None");
        }
        if (range.greaterThan(SpaceshipRange.NONE)){
        	List<String> alldest = player.getAllDestinations(location,range == SpaceshipRange.LONG);
        	Collections.sort(alldest);
        	for (int x = 0; x < alldest.size(); x++){
        		String temp = alldest.get(x);
        		if (!temp.equalsIgnoreCase(location.getName())){
        			dc.addItem(temp);
        		}
        	}
        }
        if (countCapitalShips == 0){
        	Logger.fine("countCapitalShips == 0, nrSq=" + countSquadrons);
        	// only squadrons, check for carriers with free slots
        	boolean noSquadronsAtPlanet = getNoSquadronsAtPlanet(selectedShips);
        	Logger.fine("noSquadronsAtPlanet: " + noSquadronsAtPlanet);
        	// if no selected ships at planet, add planet to destinations list
        	if (noSquadronsAtPlanet){
                dc.addItem(planet.getName());
        	}
        	// add all cariers (where no selected ships are...) to list
        	List<Spaceship> carriers = getCarriers(countSquadrons,selectedShips);
        	for (Spaceship aCarrier : carriers) {
        		Logger.fine("adding!");
                dc.addItem(aCarrier.getName());
			}
        }
    }
    
    /**
     * If there are a squadron who has been ordered to move from a carrier,
     * selecting "none" can cause that carrier to carry more squadrons than
     * its capacity, and then none can not be allowed
     * @param selectedShips
     * @param nrSquadrons
     * @return true if "None" is allowed for this selection of ships
     */
    private boolean checkNoneOk(List<Integer> selectedShipsIndexes, int nrSquadrons){
    	Logger.fine("checkNoneOk called");
    	boolean noneOk = true;
    	// if there are at least one squadron
    	if (nrSquadrons > 0){
    		// create a list with all carriers in system
    		List<Spaceship> allCarriers = new ArrayList<Spaceship>();
    		for (Object spaceshipObject : spaceships) {
    			Spaceship aShip = (Spaceship)spaceshipObject;
				if (aShip.isCarrier()){
					allCarriers.add(aShip);
				}
			}
    		// if at least one carrier
    		if (allCarriers.size() > 0){
    	    	Logger.fine("allCarriers.size(): " + allCarriers.size());
    			int[] squadronsMovingAway = new int[allCarriers.size()];
    			// count all selected ships that are squadrons and already
    			// have a move order from a carrier
    	    	for (Object object : selectedShipsIndexes) {
    	    		int index = (Integer)object;
    	          	Spaceship tmpSqd = (Spaceship)spaceships.get(index);
    	          	if (tmpSqd.getCarrierLocation() != null){ // ship is in a carrier
    	          		// check if squadron have a move order
    	          		boolean hasMoveOrder = player.checkMove(tmpSqd);
    	          		if (hasMoveOrder){
    	          			// find index of the carrierLocation
    	          			int carrierIndex = findCarrierIndexInList(tmpSqd.getCarrierLocation(),allCarriers);
    	          			squadronsMovingAway[carrierIndex]++;
    	          		}
    	          	}
    	    	}    	
        		// compare capacities of all carriers in system
        		for (int i = 0; i < squadronsMovingAway.length; i++) {
					int nrSqdMovedAway = squadronsMovingAway[i];
					Spaceship aCarrier = allCarriers.get(i);
					int nrSquadronsAssigned = player.getGalaxy().getNoSquadronsAssignedToCarrier(aCarrier);
					int nrSquadronsOrdered = player.countShipToCarrierMoves(aCarrier);
					int takenSlots = nrSquadronsAssigned + nrSquadronsOrdered;
					// if slotsTaken + nrSqdMovedAway > allCarriers.getSquadronCapacity
					//    none is not ok
					Logger.fine("NoneOk? " + aCarrier.getSquadronCapacity() + " " + takenSlots + " " + nrSqdMovedAway);
					if (aCarrier.getSquadronCapacity() < (takenSlots + nrSqdMovedAway)){
						noneOk = false;
					}
				}
    		}
    	}
    	return noneOk;
    }
    
    private int findCarrierIndexInList(Spaceship aCarrier, List<Spaceship> carriers){
    	int index = 0;
    	int found = -1;
    	for (Spaceship aCarrierInList : carriers) {
			if (aCarrier == aCarrierInList){
				found = index;
			}
			index++;
		}
    	return found;
    }
    
    private boolean getNoSquadronsAtPlanet(List<Integer> selectedShipsIndexes){
    	boolean noSquadronsAtPlanet = true;
    	for (Object object : selectedShipsIndexes) {
    		int index = (Integer)object;
          	Spaceship tmpss = (Spaceship)spaceships.get(index);
        	Logger.fine("tmpss.getCarrierLocation(): " + tmpss.getCarrierLocation() + " ss: " + tmpss.getShortName());
          	if (tmpss.getCarrierLocation() == null){ // ship not in a carrier, must be at a planet
          		noSquadronsAtPlanet = false;
          	}
    	}    	
    	return noSquadronsAtPlanet;
    }

    private boolean checkNoSqdAtCarrier(Spaceship aCarrier,List<Integer> selectedShipsIndexes){
    	boolean noSqdAtCarrier = true;
    	for (Object object : selectedShipsIndexes) {
    		int index = (Integer)object;
          	Spaceship tmpss = (Spaceship)spaceships.get(index);
          	if (tmpss.getCarrierLocation() == aCarrier){ // ship is in the carrier
          		noSqdAtCarrier = false;
          	}
    	}    	
    	return noSqdAtCarrier;
    }
    
    private List<Spaceship> getCarriers(int minFreeSlots, List<Integer> selectedShipsIndexes){
    	Logger.fine("getCarriers: " + minFreeSlots);
    	List<Spaceship> carriers = new ArrayList<Spaceship>();
    	// iterate through all ships
    	for (Object anObject : spaceships) {
    		Spaceship aSpaceship = (Spaceship)anObject;
    		Logger.fine("foreach ship: " + aSpaceship.getName());
    		// if ship is a carrier
    		if (aSpaceship.isCarrier()){
    			Logger.fine("is carrier");
    			boolean noSqdAtCarrier = checkNoSqdAtCarrier(aSpaceship,selectedShipsIndexes);
    			if (noSqdAtCarrier){
    				// x=count number of squadrons who already are assigned to the ship
    				int nrSquadronsAssigned = player.getGalaxy().getNoSquadronsAssignedToCarrier(aSpaceship);
    				// y=count number of squadrons who have move orders to the ship
    				int nrSquadronsOrdered = player.countShipToCarrierMoves(aSpaceship);
    				// if ((slots-(x+y)) >= minFeeSlots)
    				int freeSlots = aSpaceship.getSquadronCapacity() - (nrSquadronsAssigned + nrSquadronsOrdered);
    				Logger.fine(aSpaceship.getSquadronCapacity() + " - (" + nrSquadronsAssigned + "+" + nrSquadronsOrdered + ")=" + freeSlots);
    				if (freeSlots >= minFreeSlots){
    					Logger.fine("adding carrier!");
    					// lägg till i listan
    					carriers.add(aSpaceship);
    				}
    			}
    		}
		}
    	return carriers;
    }
/*    
    private int getNoSquadronsAssignedToCarrier(Spaceship carrier){
    	int count = 0;
    	for (Object aSpaceshipObject : spaceships) {
			Spaceship sstemp = (Spaceship)aSpaceshipObject;
			if (sstemp.getCarrierLocation() == carrier){
				// check if sstemp has a move order
				boolean moveToPlanetOrder = player.checkShipMove(sstemp);
				boolean moveToCarrierOrder = player.checkShipToCarrierMove(sstemp);
				// if not, inc counter
				if (!(moveToCarrierOrder | moveToPlanetOrder)){
					count++;
				}
			}
		}
    	return count;
    }
*/
    /*
    private void addDestinations(ComboBoxPanel dc, Planet location, SpaceshipRange range, boolean multiple){
      if (multiple){
        dc.addItem("(choose destination)");
      }
      dc.addItem("None");
      if (range.greaterThan(SpaceshipRange.NONE)){
        List<String> alldest = player.getAllDestinations(location,range == SpaceshipRange.LONG);
        for (int x = 0; x < alldest.size(); x++){
          String temp = alldest.get(x);
          if (!temp.equalsIgnoreCase(location.getName())){
            dc.addItem(temp);
          }
        }
      }
    }
*/
    private void addRetreatingDestinations(ComboBoxPanel dc, Spaceship aSpaceship){
      // hämta alla möjliga destinationer till skeppet
      List<Planet> allDestinations = player.getGalaxy().getAllDestinations(aSpaceship.getOldLocation(),aSpaceship.getRange() == SpaceshipRange.LONG);
      for (int x = 0; x < allDestinations.size(); x++){
        Planet temp = allDestinations.get(x);
        // om denna planet är en av spelarens egna...
        if (temp.getPlayerInControl() == aSpaceship.getOwner()){
          // ... lägg till den i listan
          dc.addItem(temp.getName());
        }
      }
    }

    private boolean ownPlanetsWithinRange(Spaceship aSpaceship){
      boolean returnValue = false;
      List<Planet> allDestinations = player.getGalaxy().getAllDestinations(aSpaceship.getOldLocation(),aSpaceship.getRange() == SpaceshipRange.LONG);
      for (int x = 0; x < allDestinations.size(); x++){
        Planet temp = allDestinations.get(x);
        if (temp.getPlayerInControl() == aSpaceship.getOwner()){
          returnValue = true;
        }
      }
      return returnValue;
    }

    private void emptyList(){
      DefaultListModel dlm = (DefaultListModel)shiplist.getModel();
      dlm.removeAllElements();
    }

    public void updateData(){
      if (currentss != null){
        showSpaceship(lastSelection);
        emptyList();
        fillList();
        
      }
    }
    
    public void autoFillCarrier(){
    	List<Spaceship> carriers = getSelectedSpaceships();
    	List<Spaceship> squdrons = getSqudronsWithNoHostCarrierOrMoveOrder(spaceships);
    	
    	while(squdrons.size() > 0 & carriers.size() > 0 ){
    		//Logger.fine("Add " + squdrons.get(0).getName() + " to Carrier"+ carriers.get(0).getName());
    		player.addShipToCarrierMove(squdrons.get(0),carriers.get(0));
    		squdrons.remove(0);
    		if(getNumberOfCarriersEmptySlots(carriers.get(0)) == 0){
    			carriers.remove(0);
    		}
    	}
        client.updateTreasuryLabel();
    }
    
    private List<Spaceship> getSqudronsWithNoHostCarrierOrMoveOrder(List<Spaceship> spaceships){
    	List<Spaceship> squadrons = new ArrayList<Spaceship>();
    	// iterate through all ships
    	for(int i = 0; i < spaceships.size(); i++){
    		Spaceship aSpaceship = spaceships.get(i);
    		Logger.fine("foreach ship: " + aSpaceship.getName());
    		// if ship is a squadron
    		if (aSpaceship.isSquadron() &&
    				aSpaceship.getCarrierLocation() == null &&
    				player.getShipDestinationCarrierName(aSpaceship).equals("") && 
    				player.getShipDestinationName(aSpaceship).equals("")){
    			squadrons.add(aSpaceship);
    		}
		}
    	return squadrons;
    }
    
    private int getNumberOfCarriersEmptySlots(Spaceship inCarrier){
    	int numberOfEmpty = 0;
    	
    	numberOfEmpty = inCarrier.getSquadronCapacity();
			
			for(int j = 0; j < spaceships.size(); j++){
	    		Spaceship tmpss = spaceships.get(j);
	          	if (tmpss.getCarrierLocation() == inCarrier){ // ship is in the carrier
	          		numberOfEmpty--;
	          		Logger.fine(tmpss.getName() + " is on carrier: " + inCarrier.getName());
	          	}	// Ship move have order to the carrier.
	          	else if(player.getShipDestinationCarrierName(tmpss).equals(inCarrier.getName())){
	          		Logger.fine(tmpss.getName() + " have move order to the carrier: " + inCarrier.getName());
	          		numberOfEmpty--;
	          	}
	    	}
    	return numberOfEmpty;
    }
    
    @SuppressWarnings("unused")
	private List<Spaceship> getCarriersWithEmtySlots(List<Spaceship> spaceships){
    	List<Spaceship> carriers = new ArrayList<Spaceship>();
    	// iterate through all ships
    	for(int i = 0; i < spaceships.size(); i++){
    		Spaceship aSpaceship = spaceships.get(i);
    		Logger.fine("foreach ship: " + aSpaceship.getName());
    		// if ship is a carrier
    		int totalSqdInTheCarrier = 0;
    		if (aSpaceship.isCarrier()){
    			totalSqdInTheCarrier = aSpaceship.getSquadronCapacity();
    			
    			
    			for(int j = 0; j < spaceships.size(); j++){
    	    		Spaceship tmpss = spaceships.get(j);
    	          	if (tmpss.getCarrierLocation() == aSpaceship){ // ship is in the carrier
    	          		totalSqdInTheCarrier--;
    	          		Logger.fine(tmpss.getName() + " is on carrier: " + aSpaceship.getName());
    	          	}	// Ship move have order to the carrier.
    	          	else if(player.getShipDestinationCarrierName(tmpss).equals(aSpaceship.getName())){
    	          		Logger.fine(tmpss.getName() + " have move order to the carrier: " + aSpaceship.getName());
    	          		totalSqdInTheCarrier--;
    	          	}
    	    	}    	
    	    	
    			if(totalSqdInTheCarrier > 0){
    				Logger.fine(" adding carrier to the list " + aSpaceship.getName());
    				carriers.add(aSpaceship);
    			}
    			
    			/*
    			Logger.fine("is carrier");
    			boolean noSqdAtCarrier = checkNoSqdAtCarrier(aSpaceship,spaceships);
    			if (noSqdAtCarrier){
    				// x=count number of squadrons who already are assigned to the ship
    				int nrSquadronsAssigned = player.getGalaxy().getNoSquadronsAssignedToCarrier(aSpaceship);
    				// y=count number of squadrons who have move orders to the ship
    				int nrSquadronsOrdered = player.countShipToCarrierMoves(aSpaceship);
    				// if ((slots-(x+y)) >= minFeeSlots)
    				int freeSlots = aSpaceship.getSquadronCapacity() - (nrSquadronsAssigned + nrSquadronsOrdered);
    				Logger.fine(aSpaceship.getSquadronCapacity() + " - (" + nrSquadronsAssigned + "+" + nrSquadronsOrdered + ")=" + freeSlots);
    				if (freeSlots >= minFreeSlots){
    					Logger.fine("adding carrier!");
    					// lägg till i listan
    					carriers.add(aSpaceship);
    				}
    			}*/
    		}
		}
    	return carriers;
    }
    
    private boolean haveSqdOutSideCarrier(List<Spaceship> spaceships, Spaceship carrier){
		Logger.fine("Startar loopen");		
		if(carrier.getSquadronCapacity() > getTakenSlots(carrier)){
	    	for(int i = 0; i < spaceships.size(); i++){
	        	Spaceship tempss = ((Spaceship)spaceships.get(i));
	        	Logger.fine("tempss.isSquadron() " + tempss.isSquadron());
	        	Logger.fine("tempss.getCarrierLocation " +tempss.getCarrierLocation());
	        	Logger.fine("player.getShipSelfDestruct(tempss) " + player.getShipSelfDestruct(tempss));
	        	Logger.fine("player.getShipDestinationCarrierName(tempss) " + player.getShipDestinationCarrierName(tempss));
	        	Logger.fine("player.getShipDestinationName(tempss) " + player.getShipDestinationName(tempss));
	        	if(tempss.isSquadron() && 
	        			tempss.getCarrierLocation() == null &&
	        			!player.getShipSelfDestruct(tempss) &&
	        			player.getShipDestinationCarrierName(tempss) == "" &&
	        			player.getShipDestinationName(tempss) == ""){
	        		Logger.fine("sqd = true");
	        		return true;
	        	}
	        }
		}
		return false;
    }
    
    
    public void autoFillCarrierTroops(){
    	List<Spaceship> carriers = getSelectedSpaceships();
    	List<Troop> troopsUnloaded = getTroopsOnPlanetWithNoMoveOrder();
    	while(troopsUnloaded.size() > 0 & carriers.size() > 0 ){
    		player.addTroopToCarrierMove(troopsUnloaded.get(0),carriers.get(0));
    		troopsUnloaded.remove(0);
    		if(getNumberOfTroopCarriersEmptySlots(carriers.get(0)) == 0){
    			carriers.remove(0);
    		}
    	}
//        client.updateTreasuryLabel();
    }
    
    private int getNumberOfTroopCarriersEmptySlots(Spaceship aSpaceship){
    	int capacity = aSpaceship.getTroopCapacity();
    	int troopsOnShip = getTroopsOnShip(aSpaceship);
    	
    	int freeSlots = capacity - troopsOnShip;
    	if(freeSlots < 0){
    		freeSlots = 0;
    	}
		
		return freeSlots;
    	
    }
    
    
    
    private boolean haveTroopsOutsideCarrier(Spaceship carrier){
    	Logger.finer("  ---  ");
    	Logger.finer("haveTroopsOutSideCarrier() - Startar loopen");
    	if(carrier.getTroopCapacity() > getTroopsOnShip(carrier)){
    		if (getTroopsOnPlanetWithNoMoveOrder().size() > 0){
    			return true;
    		}
    	}
    	return false;
    }
    
    private List<Troop> getTroopsOnPlanetWithNoMoveOrder(){
    	List<Troop> troopsUnloaded = new LinkedList<Troop>();
    	for (Troop aTroop :  player.getGalaxy().getPlayersTroopsOnPlanet(player,planet)) {
			if (aTroop.getShipLocation() == null && aTroop.isSpaceshipTravel()){
				// check if there exist a move order already for this troop to a carrier
				String destName = player.getTroopDestinationCarrierName(aTroop);
				Logger.finer("destName: " + destName);
				if (destName.equals("")){
					troopsUnloaded.add(aTroop);
				}
			}
		}
    	return troopsUnloaded;
    }
}
