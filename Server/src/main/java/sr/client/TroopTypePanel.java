package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.client.components.scrollable.ListPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.enums.BattleGroupPosition;
import sr.enums.TypeOfTroop;
import sr.general.Functions;
import sr.world.Faction;
import sr.world.Player;
import sr.world.TroopType;
import sr.world.comparator.FactionsComparator;
import sr.world.comparator.trooptype.TroopTypeComparator;
import sr.world.comparator.trooptype.TroopTypeNameComparator;
import sr.world.comparator.trooptype.TroopTypeTypeAndBuildCostComparator;

public class TroopTypePanel extends SRBasePanel implements ListSelectionListener, SRUpdateablePanel, ActionListener {
	private static final long serialVersionUID = 1L;
	private List<TroopType> troopTypes;
    private List<Faction> factions;
    private String id;
    private Player p;
    // navigation
    private ListPanel troopTypeList = null;
    private ComboBoxPanel filterChoice;
    // left column labels
    private SRLabel typenamelbl, typenamelbl2, typenamelbl3, typenamelbl4;
    private SRLabel typeShortnamelbl, typeShortnamelbl2, typeShortnamelbl3, typeShortnamelbl4;
    private SRLabel troopTypeLbl,troopTypeLbl2, troopTypeLbl3,troopTypeLbl4; // infantry/armor (artillery? Screened?)
    private SRLabel targetingTypeLbl, targetingTypeLbl2,  targetingTypeLbl3, targetingTypeLbl4;
    private SRLabel positionLbl, positionLbl2, positionLbl3, positionLbl4; // screened by default
    private SRLabel damageCapacityLbl, damageCapacitylbl2, damageCapacityLbl3, damageCapacitylbl4;
    private SRLabel weaponsInfantryLbl,weaponsInfantryLbl2, weaponsInfantryLbl3,weaponsInfantryLbl4;
    private SRLabel weaponsTanksLbl,weaponsTanksLbl2, weaponsTanksLbl3,weaponsTanksLbl4;
    private SRLabel weaponsArtilleryLbl,weaponsArtilleryLbl2, weaponsArtilleryLbl3,weaponsArtilleryLbl4;
    private SRLabel spaceshipTravelLbl, spaceshipTravelLbl2, spaceshipTravelLbl3, spaceshipTravelLbl4;
    private SRLabel canbuildlbl, canbuildlbl2;
    
    //private SRLabel blackMarketFrequencyLbl, blackMarketFrequencyLbl2, blackMarketFrequencyLbl3, blackMarketFrequencyLbl4;
    private SRLabel uniqueLbl, uniqueLbl2, uniqueLbl3, uniqueLbl4;
    private SRLabel cloakingLbl,cloakingLbl2, cloakingLbl3,cloakingLbl4;
    private SRLabel buildCostLbl, buildCostLbl2, buildCostLbl3, buildCostLbl4;
    private SRLabel supplyCostLbl, supplyCostLbl2, supplyCostLbl3, supplyCostLbl4;
    // Description
    private SRLabel troopTypeInfoLbl;
    private SRTextArea troopTypeInfoTextArea;
    private SRScrollPane scrollPane;
    // used for computing components location'
    private final int yInterval = 18;
    private int yPosition = 10;
    
    private TroopType troopType;
    private SRButton buttonAddTroopToCompare;
    final int column1X = 195;
    final int column2X = 335;
    final int column3X = 500;
    final int column4X = 650;

    public TroopTypePanel(List<TroopType> aTroopTypes, Player p, String id){
      troopTypes = Functions.cloneList(aTroopTypes);
	  Collections.sort(troopTypes,new TroopTypeComparator());
      this.id = id;
      this.p = p;
      //r = p.getResearch();
      this.setLayout(null);
      setBackground(StyleGuide.colorBackground);
      
      // used for computing components location
      

      final int cHeight = 18;

      filterChoice = new ComboBoxPanel();
      filterChoice.setBounds(10,10,170,20);
      filterChoice.addActionListener(this);
      fillFilterList();
      this.add(filterChoice);

      fillTroopTypeList();
      
      // labels
      
      typenamelbl = new SRLabel();
      typenamelbl.setBounds(column1X,yPosition,190,cHeight);
      add(typenamelbl);
      typenamelbl2 = new SRLabel();
      typenamelbl2.setBounds(column2X,yPosition,200,cHeight);
      add(typenamelbl2);
      typenamelbl3 = new SRLabel();
      typenamelbl3.setSize(190,cHeight);
      typenamelbl3.setVisible(false);
      add(typenamelbl3);
      typenamelbl4 = new SRLabel();
      typenamelbl4.setSize(200,cHeight);
      typenamelbl4.setVisible(false);
      add(typenamelbl4);
      
      typeShortnamelbl = new SRLabel();
      typeShortnamelbl.setBounds(column1X,newLine(),190,cHeight);
      add(typeShortnamelbl);
      typeShortnamelbl2 = new SRLabel();
      typeShortnamelbl2.setBounds(column2X,yPosition,200,cHeight);
      add(typeShortnamelbl2);
      typeShortnamelbl3 = new SRLabel();
      typeShortnamelbl3.setSize(190,cHeight);
      typeShortnamelbl3.setVisible(false);
      add(typeShortnamelbl3);
      typeShortnamelbl4 = new SRLabel();
      typeShortnamelbl4.setSize(200,cHeight);
      typeShortnamelbl4.setVisible(false);
      add(typeShortnamelbl4);
      
      troopTypeLbl = new SRLabel();
      troopTypeLbl.setBounds(column1X,newLine(),190,cHeight);
      add(troopTypeLbl);
      troopTypeLbl2 = new SRLabel();
      troopTypeLbl2.setBounds(column2X,yPosition,200,cHeight);
      add(troopTypeLbl2);
      troopTypeLbl3 = new SRLabel();
      troopTypeLbl3.setSize(190,cHeight);
      troopTypeLbl3.setVisible(false);
      add(troopTypeLbl3);
      troopTypeLbl4 = new SRLabel();
      troopTypeLbl4.setSize(200,cHeight);
      troopTypeLbl4.setVisible(false);
      add(troopTypeLbl4);

      targetingTypeLbl = new SRLabel();
      targetingTypeLbl.setBounds(column1X,newLine(),190,cHeight);
      add(targetingTypeLbl);
      targetingTypeLbl2 = new SRLabel();
      targetingTypeLbl2.setBounds(column2X,yPosition,200,cHeight);
      add(targetingTypeLbl2);
      targetingTypeLbl3 = new SRLabel();
      targetingTypeLbl3.setSize(190,cHeight);
      targetingTypeLbl3.setVisible(false);
      add(targetingTypeLbl3);
      targetingTypeLbl4 = new SRLabel();
      targetingTypeLbl4.setSize(200,cHeight);
      targetingTypeLbl4.setVisible(false);
      add(targetingTypeLbl4);
      
      positionLbl = new SRLabel();
      positionLbl.setBounds(column1X,newLine(),150,cHeight);
      add(positionLbl);
      positionLbl2 = new SRLabel();
      positionLbl2.setBounds(column2X,yPosition,200,cHeight);
      add(positionLbl2);
      positionLbl3 = new SRLabel();
      positionLbl3.setSize(150,cHeight);
      positionLbl3.setVisible(false);
      add(positionLbl3);
      positionLbl4 = new SRLabel();
      positionLbl4.setSize(200,cHeight);
      positionLbl4.setVisible(false);
      add(positionLbl4);

      damageCapacityLbl = new SRLabel();
      damageCapacityLbl.setBounds(column1X,newLine(),150,cHeight);
      add(damageCapacityLbl);
      damageCapacitylbl2 = new SRLabel();
      damageCapacitylbl2.setBounds(column2X,yPosition,200,cHeight);
      add(damageCapacitylbl2);
      damageCapacityLbl3 = new SRLabel();
      damageCapacityLbl3.setSize(150,cHeight);
      damageCapacityLbl3.setVisible(false);
      add(damageCapacityLbl3);
      damageCapacitylbl4 = new SRLabel();
      damageCapacitylbl4.setSize(200,cHeight);
      damageCapacitylbl4.setVisible(false);
      add(damageCapacitylbl4);

      weaponsInfantryLbl = new SRLabel();
      weaponsInfantryLbl.setBounds(column1X,newLine(),150,cHeight);
      add(weaponsInfantryLbl);
      weaponsInfantryLbl2 = new SRLabel();
      weaponsInfantryLbl2.setBounds(column2X,yPosition,200,cHeight);
      add(weaponsInfantryLbl2);
      weaponsInfantryLbl3 = new SRLabel();
      weaponsInfantryLbl3.setSize(150,cHeight);
      weaponsInfantryLbl3.setVisible(false);
      add(weaponsInfantryLbl3);
      weaponsInfantryLbl4 = new SRLabel();
      weaponsInfantryLbl4.setSize(200,cHeight);
      weaponsInfantryLbl4.setVisible(false);
      add(weaponsInfantryLbl4);

      weaponsTanksLbl = new SRLabel();
      weaponsTanksLbl.setBounds(column1X,newLine(),150,cHeight);
      add(weaponsTanksLbl);
      weaponsTanksLbl2 = new SRLabel();
      weaponsTanksLbl2.setBounds(column2X,yPosition,200,cHeight);
      add(weaponsTanksLbl2);
      weaponsTanksLbl3 = new SRLabel();
      weaponsTanksLbl3.setSize(150,cHeight);
      weaponsTanksLbl3.setVisible(false);
      add(weaponsTanksLbl3);
      weaponsTanksLbl4 = new SRLabel();
      weaponsTanksLbl4.setSize(200,cHeight);
      weaponsTanksLbl4.setVisible(false);
      add(weaponsTanksLbl4);

      weaponsArtilleryLbl = new SRLabel();
      weaponsArtilleryLbl.setBounds(column1X,newLine(),150,cHeight);
      add(weaponsArtilleryLbl);
      weaponsArtilleryLbl2 = new SRLabel();
      weaponsArtilleryLbl2.setBounds(column2X,yPosition,200,cHeight);
      add(weaponsArtilleryLbl2);
      weaponsArtilleryLbl3 = new SRLabel();
      weaponsArtilleryLbl3.setSize(150,cHeight);
      weaponsArtilleryLbl3.setVisible(false);
      add(weaponsArtilleryLbl3);
      weaponsArtilleryLbl4 = new SRLabel();
      weaponsArtilleryLbl4.setSize(200,cHeight);
      weaponsArtilleryLbl4.setVisible(false);
      add(weaponsArtilleryLbl4);

      spaceshipTravelLbl = new SRLabel();
      spaceshipTravelLbl.setBounds(column1X,newLine(),150,cHeight);
      add(spaceshipTravelLbl);
      spaceshipTravelLbl2 = new SRLabel();
      spaceshipTravelLbl2.setBounds(column2X,yPosition,200,cHeight);
      add(spaceshipTravelLbl2);
      spaceshipTravelLbl3 = new SRLabel();
      spaceshipTravelLbl3.setSize(150,cHeight);
      spaceshipTravelLbl3.setVisible(false);
      add(spaceshipTravelLbl3);
      spaceshipTravelLbl4 = new SRLabel();
      spaceshipTravelLbl4.setSize(200,cHeight);
      spaceshipTravelLbl4.setVisible(false);
      add(spaceshipTravelLbl4);
     
      uniqueLbl = new SRLabel();
      uniqueLbl.setBounds(column1X,newLine(),150,cHeight);
      uniqueLbl.setToolTipText("Only one of this troop can be build at a level of World, Faction or Player");
      add(uniqueLbl);
      uniqueLbl2 = new SRLabel();
      uniqueLbl2.setBounds(column2X,yPosition,200,cHeight);
      uniqueLbl2.setToolTipText("Only one of this troop can be build at a level of World, Faction or Player");
      add(uniqueLbl2);
      uniqueLbl3 = new SRLabel();
      uniqueLbl3.setSize(150,cHeight);
      uniqueLbl3.setVisible(false);
      uniqueLbl3.setToolTipText("Only one of this troop can be build at a level of World, Faction or Player");
      add(uniqueLbl3);
      uniqueLbl4 = new SRLabel();
      uniqueLbl4.setSize(200,cHeight);
      uniqueLbl4.setVisible(false);
      uniqueLbl4.setToolTipText("Only one of this troop can be build at a level of World, Faction or Player");
      add(uniqueLbl4);

      cloakingLbl = new SRLabel();
      cloakingLbl.setBounds(column1X,newLine(),150,cHeight);
      cloakingLbl.setToolTipText("This troop is unvisible on enemys map");
      add(cloakingLbl);
      cloakingLbl2 = new SRLabel();
      cloakingLbl2.setBounds(column2X,yPosition,200,cHeight);
      cloakingLbl2.setToolTipText("This troop is unvisible on enemys map");
      add(cloakingLbl2);
      cloakingLbl3 = new SRLabel();
      cloakingLbl3.setSize(150,cHeight);
      cloakingLbl3.setToolTipText("This troop is unvisible on enemys map");
      cloakingLbl3.setVisible(true);
      add(cloakingLbl3);
      cloakingLbl4 = new SRLabel();
      cloakingLbl4.setSize(200,cHeight);
      cloakingLbl4.setToolTipText("This troop is unvisible on enemys map");
      cloakingLbl4.setVisible(true);
      add(cloakingLbl4);

      yPosition = yPosition + 10;
      
      buildCostLbl = new SRLabel();
      buildCostLbl.setBounds(column1X,newLine(),150,cHeight);
      add(buildCostLbl);
      buildCostLbl2 = new SRLabel();
      buildCostLbl2.setBounds(column2X,yPosition,200,cHeight);
      add(buildCostLbl2);
      buildCostLbl3 = new SRLabel();
      buildCostLbl3.setSize(150,cHeight);
      buildCostLbl3.setVisible(false);
      add(buildCostLbl3);
      buildCostLbl4 = new SRLabel();
      buildCostLbl4.setSize(200,cHeight);
      buildCostLbl4.setVisible(false);
      add(buildCostLbl4);

      supplyCostLbl = new SRLabel();
      supplyCostLbl.setBounds(column1X,newLine(),150,cHeight);
      add(supplyCostLbl);
      supplyCostLbl2 = new SRLabel();
      supplyCostLbl2.setBounds(column2X,yPosition,200,cHeight);
      add(supplyCostLbl2);
      supplyCostLbl3 = new SRLabel();
      supplyCostLbl3.setSize(150,cHeight);
      supplyCostLbl3.setVisible(false);
      add(supplyCostLbl3);
      supplyCostLbl4 = new SRLabel();
      supplyCostLbl4.setSize(200,cHeight);
      supplyCostLbl4.setVisible(false);
      add(supplyCostLbl4);
      
      canbuildlbl = new SRLabel();
      canbuildlbl.setBounds(column1X,newLine(),100,cHeight);
      add(canbuildlbl);
      canbuildlbl2 = new SRLabel();
      canbuildlbl2.setBounds(column2X,yPosition,300,cHeight);
      add(canbuildlbl2);
      
      buttonAddTroopToCompare = new SRButton("Compare this troop");
      buttonAddTroopToCompare.setToolTipText("Hit the button to get this troop values in a column at the right, to compare with other troops");
      buttonAddTroopToCompare.setBounds(column1X, 290, 180, 20);
      buttonAddTroopToCompare.addActionListener(this);
      buttonAddTroopToCompare.setVisible(false);
      add(buttonAddTroopToCompare);
      // right column
      yPosition = 46;

      yPosition = yPosition + 54 + (1*18);

      // Shiptype info textarea
      troopTypeInfoLbl = new SRLabel();
      troopTypeInfoLbl.setBounds(column1X,320,120,cHeight);
      add(troopTypeInfoLbl);

      troopTypeInfoTextArea = new SRTextArea();
      
      troopTypeInfoTextArea.setEditable(false);
 
      scrollPane = new SRScrollPane(troopTypeInfoTextArea);
      scrollPane.setBounds(column1X,340,650,260);
      scrollPane.setVisible(false);
      add(scrollPane);
    }
    
    private void fillFilterList(){
    	filterChoice.addItem("All (sort by name)");
    	filterChoice.addItem("All (sort by class & size)");
    	filterChoice.addItem("Yours");
    	factions = Functions.cloneList(p.getGalaxy().getGameWorld().getFactions());
    	Collections.sort(factions,new FactionsComparator());
    	for (Faction aFaction : factions) {
			filterChoice.addItem(aFaction.getName());
		}
    }
    
    private void fillTroopTypeList(){
    	
    	if(troopTypeList != null){
    		remove(troopTypeList);
    	}
    	troopTypeList = new ListPanel();
        troopTypeList.setBounds(10,40,170,560);
        troopTypeList.setListSelectionListener(this);
        troopTypeList.setForeground(StyleGuide.colorCurrent);
        troopTypeList.setBackground(StyleGuide.colorBackground);
        troopTypeList.setBorder(new LineBorder(StyleGuide.colorCurrent));
        
        DefaultListModel dlm = (DefaultListModel)troopTypeList.getModel();
        dlm.removeAllElements();
//    	List<SpaceshipType> tempSstList = new LinkedList<SpaceshipType>();
    	List<TroopType> tempTtList = null;
    	List<String> tempTtListName = new ArrayList<String>();
    	if (filterChoice.getSelectedIndex() > 2){
    		// get faction to show ships from
    		Faction showOnlyFaction = factions.get(filterChoice.getSelectedIndex() - 3);
    		tempTtList = showOnlyFaction.getTroopTypes(); // borde funka som == 2 nedan
    	}else 
    	if(filterChoice.getSelectedIndex() == 2){
    		tempTtList = p.getTroopTypes();
    	}
    	else{
    		tempTtList = troopTypes;
    	}    	
    	// add to name list
    	if (filterChoice.getSelectedIndex() == 0){
    		for (TroopType aTroopType : tempTtList) {
    			tempTtListName.add(aTroopType.getUniqueName());
			}
    		Collections.sort(tempTtListName,new TroopTypeNameComparator());
    	}else{
    		Collections.sort(tempTtList,new TroopTypeTypeAndBuildCostComparator());
    		
    		List<String> types = new ArrayList<String>();
    		types.add("defense");
    		types.add("support");
    		types.add("armor");
    		types.add("Infantry");
    		
    		for (TroopType aTroopType : tempTtList) {
    			
    			String troopTypename = checkIfNewTroopType(aTroopType, types);
    			if(troopTypename != null){
    				tempTtListName.add("------------" + troopTypename + "--------------------");
    			}
    			
    			if (filterChoice.getSelectedIndex() == 1){
        			tempTtListName.add(aTroopType.getUniqueName());
        		}else{
            		if(aTroopType.isCanBuild()){
        				tempTtListName.add(aTroopType.getUniqueName());
        			}else{
        				tempTtListName.add("*" + aTroopType.getUniqueName());
        			}
        		}
			}
    	}
    	for (String aTroopTypeName : tempTtListName) {
    		dlm.addElement(aTroopTypeName);
		}
    	troopTypeList.updateScrollList();
        add(troopTypeList);
    }
    
    private int newLine(){
    	yPosition = yPosition + yInterval;
    	return yPosition;
    }
    
    private String checkIfNewTroopType(TroopType troopType, List<String> types){
    	String type=null;
    	if((!troopType.isSpaceshipTravel() && types.contains("defense")) ||
    			(troopType.getDefaultPosition() == BattleGroupPosition.SUPPORT && types.contains("support") && troopType.isSpaceshipTravel()) ||
    			( troopType.isArmor() && types.contains("armor") && troopType.isSpaceshipTravel()) ||
    			(troopType.getTypeOfTroop() == TypeOfTroop.INFANTRY && troopType.isSpaceshipTravel() && types.contains("Infantry")) ){
    		if(!troopType.isSpaceshipTravel()){
    			types.remove("defense");
    			type = "Defense";
    		}else if(troopType.getDefaultPosition() == BattleGroupPosition.SUPPORT){
    			types.remove("support");
    			type = "Support";
    		}else if( troopType.isArmor()){
    			types.remove("armor");
    			type = "Armor";
    		}else{
    			types.remove("Infantry");
    			type = "Infantry";
    		}
    	}
    	return type;
    }

    public void valueChanged(ListSelectionEvent lse){
      if (lse.getSource() instanceof ListPanel){
        showTroopType(troopTypeList.getSelectedItem(), null);
      }
    }

    private TroopType findTroopType(String findname){
      TroopType tt = null;
      int i = 0;
      if(filterChoice.getSelectedIndex() == 2){
    	  tt = p.findTroopType(findname);
      }else
      if(filterChoice.getSelectedIndex() > 2){
    	  Faction aFaction = factions.get(filterChoice.getSelectedIndex() - 3);
    	  tt = aFaction.getTroopTypeByName(findname);
//    	  tt = p.getFaction().getTroopTypeByName(findname);
      }else{
	      while ((tt == null) & (i < troopTypes.size())){
	    	  TroopType temp = troopTypes.get(i);
	    	  if (temp.getUniqueName().equalsIgnoreCase(findname)){
	    		  tt = temp;
	    	  }else{
	    		  i++;
	    	  }
	      }
      }
      return tt;
    }

    public void showTroopType(String name, String faction){
    	if(faction != null){
    		filterChoice.setSelectedItem(faction);
    		fillTroopTypeList();
    		
    	}
    	
    	
    	
    	troopType = null;  
    	if(!name.contains("---")){
	    	if(name.contains("*")){
	    		name = name.substring(1);
	    	}
	    	troopType = findTroopType(name);
	    	
	    	Enumeration<?> elements = troopTypeList.getModel().elements();
	    	int selectIndex = -1; 
	    	int index = 0;
	    	
	    	if(troopTypeList.getModel().contains("*" + name)){
	    		name = "*" +name;
	    	}
	    	
	    	while(elements.hasMoreElements()){
	    		String nextElement = (String)elements.nextElement();
	    		
	    		if(nextElement.equalsIgnoreCase(name)){
	    			selectIndex = index;
	    		}
	    		index++;
	    	}
	    	if(index > 0){
	    		troopTypeList.setSelected(selectIndex);
	    	}
    	}else{
    		troopTypeList.clearSelected();
    	}
    	
    	
    	if (troopType != null){
	    	
    		yPosition = 10;
	        
	        typenamelbl.setText("Name: ");
	        typenamelbl2.setText(troopType.getUniqueName());
	        newLine();
	        
	        typeShortnamelbl.setText("Short name: ");
	        typeShortnamelbl2.setText(troopType.getUniqueShortName());
	        newLine();
	        
	        troopTypeLbl.setText("Type: ");
	        troopTypeLbl2.setText(troopType.getTypeOfTroop().toString());
	        newLine();
	        
	        targetingTypeLbl.setText("Targeting type: ");
	        targetingTypeLbl2.setText(troopType.getTargetingType().toString());
	        newLine();
	        
	        positionLbl.setText("Battle position: ");
	        positionLbl.setLocation(column1X, yPosition);
	        positionLbl2.setText(troopType.getDefaultPosition().toString());
	        positionLbl2.setLocation(column2X, yPosition);
	        newLine();
	        
	        damageCapacityLbl.setText("Damage capacity: ");
	        damageCapacityLbl.setLocation(column1X, yPosition);
	        damageCapacitylbl2.setText(String.valueOf(troopType.getDamageCapacity()));
	        damageCapacitylbl2.setLocation(column2X, yPosition);
	        newLine();
	        weaponsInfantryLbl.setText("Attack vs Infantry: ");
	        weaponsInfantryLbl.setLocation(column1X, yPosition);
	        weaponsInfantryLbl2.setText(String.valueOf(troopType.getAttackInfantry()));
	        weaponsInfantryLbl2.setLocation(column2X, yPosition);
	        newLine();
	        weaponsTanksLbl.setText("Attack vs Tanks: ");
	        weaponsTanksLbl.setLocation(column1X, yPosition);
	        weaponsTanksLbl2.setText(String.valueOf(troopType.getAttackArmored()));
	        weaponsTanksLbl2.setLocation(column2X, yPosition);
	        newLine();
	        if(troopType.getAttackArtillery() > 0){
	        	weaponsArtilleryLbl.setText("Artillery Attack: ");
	        	weaponsArtilleryLbl.setLocation(column1X, yPosition);
	        	weaponsArtilleryLbl.setVisible(true);
	        	weaponsArtilleryLbl2.setText(String.valueOf(troopType.getAttackArtillery()));
	        	weaponsArtilleryLbl2.setLocation(column2X, yPosition);
	        	weaponsArtilleryLbl2.setVisible(true);
	            newLine();
	        }else{
	        	weaponsArtilleryLbl.setVisible(false);
	        	weaponsArtilleryLbl2.setVisible(false);
	        	
	        }
	        spaceshipTravelLbl.setText("Travels in spaceships: ");
	        spaceshipTravelLbl.setLocation(column1X, yPosition);
	        spaceshipTravelLbl2.setText(Functions.getYesNo(troopType.isSpaceshipTravel()));
	        spaceshipTravelLbl2.setLocation(column2X, yPosition);
	        newLine();
		        
	        if(troopType.isWorldUnique() || troopType.isFactionUnique() || troopType.isPlayerUnique()){
	        	uniqueLbl.setText("Unique grade: ");
		        uniqueLbl.setLocation(column1X, yPosition);
		        uniqueLbl.setVisible(true);
		        if(troopType.isWorldUnique()){
	        		uniqueLbl2.setText("World");
	        		
	        	}else if(troopType.isFactionUnique()){
	        		uniqueLbl2.setText("Faction");
	        		
	        	}else if(troopType.isPlayerUnique()){
	        		uniqueLbl2.setText("Player");
	        		
	        	}
		        uniqueLbl2.setLocation(column2X, yPosition);
		        uniqueLbl2.setVisible(true);
		        newLine();
	        }else{
	        	uniqueLbl.setVisible(false);
	        	uniqueLbl2.setVisible(false);
	        }
	        
	        if(!troopType.isVisible()){
	        	cloakingLbl.setText("Cloaking:");
	    		cloakingLbl.setLocation(column1X, yPosition);
	    		cloakingLbl2.setText(Functions.getYesNo(!troopType.isVisible()));
	    		cloakingLbl2.setLocation(column2X, yPosition);
	    		cloakingLbl.setVisible(true);
	    		cloakingLbl2.setVisible(true);
	    		newLine();
	        }else{
	        	cloakingLbl.setVisible(false);
	    		cloakingLbl2.setVisible(false);
	    	}
	        
	        buildCostLbl.setText("Build Cost: ");
	        buildCostLbl.setLocation(column1X, yPosition);
	        buildCostLbl2.setText(String.valueOf(troopType.getCostBuild(null)));
	        buildCostLbl2.setLocation(column2X, yPosition);
	        newLine();
	        
	        supplyCostLbl.setText("Supply Cost: ");
	        supplyCostLbl.setLocation(column1X, yPosition);
	        supplyCostLbl2.setText(String.valueOf(troopType.getUpkeep()));
	        supplyCostLbl2.setLocation(column2X, yPosition);
	        newLine();
	        
	        if (filterChoice.getSelectedIndex() == 2){
	            canbuildlbl.setText("Can build: ");
	            canbuildlbl2.setText(Functions.getYesNo(troopType.isCanBuild()));
	            canbuildlbl.setLocation(column1X,yPosition);
	        	canbuildlbl2.setLocation(column2X,yPosition);
	            canbuildlbl.setVisible(true);
	            canbuildlbl2.setVisible(true);
	            newLine();
	        }else if (filterChoice.getSelectedIndex() > 2){
	            canbuildlbl.setText("Build from start: ");
	            canbuildlbl2.setText(Functions.getYesNo(troopType.isCanBuild()));
	            canbuildlbl.setLocation(column1X,yPosition);
	        	canbuildlbl2.setLocation(column2X,yPosition);
	            canbuildlbl.setVisible(true);
	            canbuildlbl2.setVisible(true);
	            newLine();
	        }else{
	            canbuildlbl.setVisible(false);
	            canbuildlbl2.setVisible(false);
	        }
        
	        // description textarea
        	troopTypeInfoLbl.setText("Description:");
        	troopTypeInfoTextArea.setText(troopType.getTotalDescription());
        	troopTypeInfoTextArea.setVisible(true);
        	scrollPane.setVisible(true);
        	buttonAddTroopToCompare.setVisible(true);
        }
 
    }
    
    private void showTroopTypeToCompare(TroopType troopTypeToCompare){
    	
    	if (troopTypeToCompare != null){
	    	
    		yPosition = 10;
	        
	        typenamelbl3.setText("Name: ");
	        typenamelbl3.setVisible(true);
	        typenamelbl3.setLocation(column3X, yPosition);
	        typenamelbl4.setText(troopTypeToCompare.getUniqueName());
	        typenamelbl4.setVisible(true);
	        typenamelbl4.setLocation(column4X, yPosition);
	        newLine();
	        
	        typeShortnamelbl3.setText("Short name: ");
	        typeShortnamelbl3.setVisible(true);
	        typeShortnamelbl3.setLocation(column3X, yPosition);
	        typeShortnamelbl4.setText(troopTypeToCompare.getUniqueShortName());
	        typeShortnamelbl4.setVisible(true);
	        typeShortnamelbl4.setLocation(column4X, yPosition);
	        newLine();
	        
	        troopTypeLbl3.setText("Type: ");
	        troopTypeLbl3.setVisible(true);
	        troopTypeLbl3.setLocation(column3X, yPosition);
	        troopTypeLbl4.setText(troopTypeToCompare.getTypeOfTroop().toString());
	        troopTypeLbl4.setVisible(true);
	        troopTypeLbl4.setLocation(column4X, yPosition);
	        newLine();
	        
	        targetingTypeLbl3.setText("Targeting type: ");
	        targetingTypeLbl3.setVisible(true);
	        targetingTypeLbl3.setLocation(column3X, yPosition);
	        targetingTypeLbl4.setText(troopTypeToCompare.getTargetingType().toString());
	        targetingTypeLbl4.setVisible(true);
	        targetingTypeLbl4.setLocation(column4X, yPosition);
	        newLine();
	        
	        positionLbl3.setText("Battle position: ");
	        positionLbl3.setLocation(column3X, yPosition);
	        positionLbl3.setVisible(true);
	        positionLbl4.setText(troopTypeToCompare.getDefaultPosition().toString());
	        positionLbl4.setLocation(column4X, yPosition);
	        positionLbl4.setVisible(true);
	        newLine();
	        
	        damageCapacityLbl3.setText("Damage capacity: ");
	        damageCapacityLbl3.setLocation(column3X, yPosition);
	        damageCapacityLbl3.setVisible(true);
	        damageCapacitylbl4.setText(String.valueOf(troopTypeToCompare.getDamageCapacity()));
	        damageCapacitylbl4.setLocation(column4X, yPosition);
	        damageCapacitylbl4.setVisible(true);
	        newLine();
	        weaponsInfantryLbl3.setText("Attack vs Infantry: ");
	        weaponsInfantryLbl3.setLocation(column3X, yPosition);
	        weaponsInfantryLbl3.setVisible(true);
	        weaponsInfantryLbl4.setText(String.valueOf(troopTypeToCompare.getAttackInfantry()));
	        weaponsInfantryLbl4.setLocation(column4X, yPosition);
	        weaponsInfantryLbl4.setVisible(true);
	        newLine();
	        weaponsTanksLbl3.setText("Attack vs Tanks: ");
	        weaponsTanksLbl3.setLocation(column3X, yPosition);
	        weaponsTanksLbl3.setVisible(true);
	        weaponsTanksLbl4.setText(String.valueOf(troopTypeToCompare.getAttackArmored()));
	        weaponsTanksLbl4.setLocation(column4X, yPosition);
	        weaponsTanksLbl4.setVisible(true);
	        newLine();
	        if(troopTypeToCompare.getAttackArtillery() > 0){
	        	weaponsArtilleryLbl3.setText("Artillery Attack: ");
	        	weaponsArtilleryLbl3.setLocation(column3X, yPosition);
	        	weaponsArtilleryLbl3.setVisible(true);
	        	weaponsArtilleryLbl4.setText(String.valueOf(troopTypeToCompare.getAttackArtillery()));
	        	weaponsArtilleryLbl4.setLocation(column4X, yPosition);
	        	weaponsArtilleryLbl4.setVisible(true);
	            newLine();
	        }else{
	        	weaponsArtilleryLbl3.setVisible(false);
	        	weaponsArtilleryLbl4.setVisible(false);
	        	
	        }
	        spaceshipTravelLbl3.setText("Travels in spaceships: ");
	        spaceshipTravelLbl3.setLocation(column3X, yPosition);
	        spaceshipTravelLbl3.setVisible(true);
	        spaceshipTravelLbl4.setText(Functions.getYesNo(troopTypeToCompare.isSpaceshipTravel()));
	        spaceshipTravelLbl4.setLocation(column4X, yPosition);
	        spaceshipTravelLbl4.setVisible(true);
	        newLine();
	        
	   //     attackScreenedLbl.setText("Can attack screened: ");
	        /*
	        blackMarketFrequencyLbl3.setText("Black Market Freq.: ");
	        blackMarketFrequencyLbl3.setLocation(column3X, yPosition);
	        blackMarketFrequencyLbl3.setVisible(true);
	        blackMarketFrequencyLbl4.setText(troopTypeToCompare.getBlackMarketFrequency().toString());
	        blackMarketFrequencyLbl4.setLocation(column4X, yPosition);
	        blackMarketFrequencyLbl4.setVisible(true);
	        newLine();
	        */
	        
	        
	        if(troopTypeToCompare.isWorldUnique() || troopTypeToCompare.isFactionUnique() || troopTypeToCompare.isPlayerUnique()){
	        	uniqueLbl3.setText("Unique grade: ");
		        uniqueLbl3.setLocation(column3X, yPosition);
		        uniqueLbl3.setVisible(true);
		        if(troopTypeToCompare.isWorldUnique()){
	        		uniqueLbl4.setText("World");
	        		
	        	}else if(troopTypeToCompare.isFactionUnique()){
	        		uniqueLbl4.setText("Faction");
	        		
	        	}else if(troopTypeToCompare.isPlayerUnique()){
	        		uniqueLbl4.setText("Player");
	        		
	        	}
		        uniqueLbl4.setLocation(column4X, yPosition);
		        uniqueLbl4.setVisible(true);
		        newLine();
	        }else{
	        	uniqueLbl3.setVisible(false);
	        	uniqueLbl4.setVisible(false);
	        }
	        
	        
	        if(!troopTypeToCompare.isVisible()){
	        	cloakingLbl3.setText("Cloaking:");
	    		cloakingLbl3.setLocation(column1X, yPosition);
	    		cloakingLbl4.setText(Functions.getYesNo(!troopTypeToCompare.isVisible()));
	    		cloakingLbl4.setLocation(column2X, yPosition);
	    		cloakingLbl3.setVisible(true);
	    		cloakingLbl4.setVisible(true);
	    		newLine();
	        }else{
	        	cloakingLbl3.setVisible(false);
	    		cloakingLbl3.setVisible(false);
	    	}
	        
	        buildCostLbl3.setText("Build Cost: ");
	        buildCostLbl3.setLocation(column3X, yPosition);
	        buildCostLbl3.setVisible(true);
	        buildCostLbl4.setText(String.valueOf(troopTypeToCompare.getCostBuild(null)));
	        buildCostLbl4.setLocation(column4X, yPosition);
	        buildCostLbl4.setVisible(true);
	        newLine();
	        supplyCostLbl3.setText("Supply Cost: ");
	        supplyCostLbl3.setLocation(column3X, yPosition);
	        supplyCostLbl3.setVisible(true);
	        supplyCostLbl4.setText(String.valueOf(troopTypeToCompare.getUpkeep()));
	        supplyCostLbl4.setLocation(column4X, yPosition);
	        supplyCostLbl4.setVisible(true);
	        newLine();

        }
    
    }
    
    
    

    public String getId(){
        return id;
    }

    public void updateData(){
    }

	public void actionPerformed(ActionEvent arg0) {
		
		if(arg0.getSource().equals(buttonAddTroopToCompare)){
			showTroopTypeToCompare(troopType);
		}else{
			fillTroopTypeList();
		}
		
	}
}
