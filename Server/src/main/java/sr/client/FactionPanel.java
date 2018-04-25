package sr.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.scrollable.ListPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.Functions;
import sr.world.BuildingType;
import sr.world.Faction;
import sr.world.Player;
import sr.world.SpaceshipType;
import sr.world.TroopType;
import sr.world.VIPType;
import sr.world.comparator.FactionsComparator;

/**
 * A panel showing a list with all factions in the gameworld of the current game.
 * If one faction is chosen in the list, all relevant data about it is displayed. 
 * 
 * @author wmpabod
 *
 */
public class FactionPanel extends SRBasePanel implements ListSelectionListener, SRUpdateablePanel{
	private static final long serialVersionUID = 1L;
	private List<Faction> factions;
    private ListPanel factionlist;
    private ListPanel startingUnitsList;
    private List<Object> startingUnits;
    private SRLabel startingUnitsLbl;
    private int column1X = 195;
    private int column1width = 170;
    private String id;
    private Player p;
    private FactionDetailInfoPanel detailPanel;
    private String playerListText = "You";
    private SpaceRazeApplet spaceRazeApplet;

    public FactionPanel(Player p, String id, SpaceRazeApplet spaceRazeApplet){
      factions = p.getGalaxy().getGameWorld().getFactions();

      this.id = id;
      this.p = p;
      this.spaceRazeApplet = spaceRazeApplet;
      
      startingUnits= new ArrayList<Object>();
      

      //r = p.getResearch();
      this.setLayout(null);
      setBackground(StyleGuide.colorBackground);
      
      int cHeight = 18;

      factionlist = new ListPanel();
      factionlist.setBounds(10,5,170,220);
      factionlist.setListSelectionListener(this);
      //shiptypelist.addItemListener(this);
      factionlist.setForeground(StyleGuide.colorCurrent);
      factionlist.setBackground(StyleGuide.colorBackground);
      factionlist.setBorder(new LineBorder(StyleGuide.colorCurrent));
      // fill list
      DefaultListModel dlm = (DefaultListModel)factionlist.getModel();
      List<Faction> factionsCopy = Functions.cloneList(factions);
      Collections.sort(factionsCopy,new FactionsComparator());
      
      // Adding player
      dlm.addElement(playerListText);
      
      for (Faction faction : factionsCopy) {
          dlm.addElement(faction.getName());		
      }

      factionlist.updateScrollList();
      add(factionlist);
      
      
      startingUnitsList = new ListPanel();
      startingUnitsList.setBounds(10,255,170,335);
      startingUnitsList.setListSelectionListener(this);
      //shiptypelist.addItemListener(this);
      startingUnitsList.setForeground(StyleGuide.colorCurrent);
      startingUnitsList.setBackground(StyleGuide.colorBackground);
      startingUnitsList.setBorder(new LineBorder(StyleGuide.colorCurrent));
      
      add(startingUnitsList);
      
      
      // Detail
      detailPanel = new FactionDetailInfoPanel();
      detailPanel.setBounds(column1X,0,660,600);
      detailPanel.setVisible(false);
      add(detailPanel);
      
      //Shiptype info textarea
      startingUnitsLbl = new SRLabel();
      startingUnitsLbl.setBounds(10,235,column1width,cHeight);
      startingUnitsLbl.setVisible(false);
      add(startingUnitsLbl);

      
      startingUnitsLbl.setText("Starting units");
      
    }
    
    public void valueChanged(ListSelectionEvent lse){
      if (lse.getSource() instanceof ListPanel && (ListPanel)lse.getSource() == factionlist){
    	  showFaction(factionlist.getSelectedItem());
    	  
        
      }else if(lse.getSource() instanceof ListPanel && (ListPanel)lse.getSource() == startingUnitsList){
    	  Object startingUnit = getStartingUnit(startingUnitsList.getSelectedItem());
    	  if (startingUnit instanceof VIPType){
    		  this.spaceRazeApplet.showVIPTypeDetails(((VIPType)startingUnit).getName(), "Faction: " +factionlist.getSelectedItem());
	      }else
	      if (startingUnit instanceof SpaceshipType){
	    	  this.spaceRazeApplet.showShiptypeDetails(((SpaceshipType)startingUnit).getName(), factionlist.getSelectedItem());
	      }else
	      if (startingUnit instanceof TroopType){ 
	    	  this.spaceRazeApplet.showTroopTypeDetails(((TroopType)startingUnit).getUniqueName(), factionlist.getSelectedItem());
	      }else
	      if (startingUnit instanceof BuildingType){ 
	    	  this.spaceRazeApplet.showBuildingTypeDetails(((BuildingType)startingUnit).getName(), factionlist.getSelectedItem());
	      }
      }
    }

    private Faction findFaction(String findname){
      Faction f = null;
      int i = 0;
      while ((f == null) & (i<factions.size())){
    	  Faction temp = (Faction)factions.get(i);
    	  if (temp.getName().equalsIgnoreCase(findname)){
    		  f = temp;
    	  }
    	  i++;
      }
      return f;
    }

    public void showFaction(String name){
    	Faction f;
    	if(name.equals(playerListText)){
    		f = findFaction(p.getFaction().getName());
    	}else{
    		f = findFaction(name);
    	}
    	
    	startingUnits.clear();
    	addStartingUnits(f);
        
        if(f != null){
        	if(name.equals(playerListText)){
				detailPanel.showFaction(f,p);
			}else{
				detailPanel.showFaction(f);
			}
			
			detailPanel.setVisible(true);
	    	startingUnitsLbl.setVisible(true);
	    	
	    }
    }
    
    private void addStartingUnits(Faction f){
        
    	boolean addVIPInfoText=true;
        boolean addShipInfoText=true;
        boolean addTroopInfoText=true;
        boolean addBuildingInfoText=true;
        
        startingUnitsLbl.setVisible(false);
        
        DefaultListModel dlm = (DefaultListModel)startingUnitsList.getModel();
        dlm.removeAllElements();
        
        List<VIPType> allVIPs = f.getStartingVIPTypes();
        for (VIPType vipType : allVIPs) {
    		if(addVIPInfoText){
    			dlm.addElement("------   Specific VIPs   -----------");
    			addVIPInfoText = false;
    		}
    		dlm.addElement(vipType.getName());
    		startingUnits.add(vipType);
        }
        
        List<SpaceshipType> allShips = f.getStartingShipTypes();
        for (SpaceshipType spaceshipType : allShips) {
    		if(addShipInfoText){
    			dlm.addElement("------   Ships   -------------------");
    			addShipInfoText = false;
    		}
    		dlm.addElement(spaceshipType.getName());
    		startingUnits.add(spaceshipType);
        }
        	
        List<TroopType> troops = f.getStartingTroops();
        for (TroopType troopType : troops) {
        	if(addTroopInfoText){
        		dlm.addElement("------   Troops   ------------------");
    			addTroopInfoText = false;
    		}
        	dlm.addElement(troopType.getUniqueName());
        	startingUnits.add(troopType);
		}
        
        List<BuildingType> buildings = f.getStartingBuildings();
        for (BuildingType buildingType : buildings) {
        	if(addBuildingInfoText){
        		dlm.addElement("------   Buildings   ---------------");
        		addBuildingInfoText = false;
    		}
        	dlm.addElement(buildingType.getName());
        	startingUnits.add(buildingType);
		}
        
        startingUnitsList.updateScrollList();
      }
    
    private Object getStartingUnit(String choice){
  	  Object choicedObject = null;
  	  for(Object destObject: startingUnits){
  		  if (destObject instanceof VIPType){
  	        if(((VIPType)destObject).getName().equalsIgnoreCase(choice)){
  	        	choicedObject = destObject;
  	        }
  	      }else
  	      if (destObject instanceof SpaceshipType){
  	    	  if(((SpaceshipType)destObject).getName().equalsIgnoreCase(choice)){
  		        	choicedObject = destObject;
  	    	  }
  	      }else
  	      if (destObject instanceof TroopType){ // is Troop instance
  	    	  if(((TroopType)destObject).getUniqueName().equalsIgnoreCase(choice)){
  		        	choicedObject = destObject;
  	    	  }
  	      }else
  	      if (destObject instanceof BuildingType){ // is Building instance
  	    	  if(((BuildingType)destObject).getName().equalsIgnoreCase(choice)){
  		        	choicedObject = destObject;
  	    	  }
  	      }
  	  }
  	  return choicedObject;
    }

    public String getId(){
        return id;
    }

    public void updateData(){
    }
}
