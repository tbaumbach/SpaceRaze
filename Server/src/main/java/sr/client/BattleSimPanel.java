package sr.client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.battlesim.BattleSim;
import sr.client.battlesim.BattleSimGraph;
import sr.client.battlesim.BattleSimListener;
import sr.client.battlesim.BattleSimResult;
import sr.client.battlesim.BattleSimTfCosts;
import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.client.components.SRTextField;
import sr.client.components.scrollable.ListPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.Faction;
import sr.world.Galaxy;
import sr.world.Player;
import sr.world.SpaceshipType;
import sr.world.VIPType;
import sr.world.comparator.FactionsComparator;
import sr.world.comparator.VIPTypeComparator;
import sr.world.comparator.spaceshiptype.SpaceshipTypeComparator;
import sr.world.comparator.spaceshiptype.SpaceshipTypeNameComparator;

public class BattleSimPanel extends SRBasePanel implements ListSelectionListener, SRUpdateablePanel, ActionListener, BattleSimListener {
	private static final long serialVersionUID = 1L;
    private String id;
	private List<SpaceshipType> militaryShiptypes;
	private List<VIPType> vipTypes;
    private List<Faction> factions;
    private ListPanel shiptypelist= null,vipsList;
    private ComboBoxPanel filterChoice;
    private SRLabel techLbl,nrLbl,killsLbl,damagedLbl,vipsListLbl,aForceLbl,bForceLbl;
    private SRLabel aBuildCostLbl,bBuildCostLbl,aSupplyCostLbl,bSupplyCostLbl,aWinsLbl,bWinsLbl;
    private SRTextField techTF,nrTF,killsTF,damagedTF;
    private SRButton addABtn,addBBtn,startSimBtn,stopSimBtn;
//    private CheckBoxPanel screenedCB;
    private Player p;
    private SRTextArea aForceTA,bForceTA;
    private JScrollPane aForceScrollPane,bForceScrollPane;
    private BattleSim battleSim; 
    private BattleSimGraph graph;
    private List<Integer> graphPoints;
    
    public BattleSimPanel(List<SpaceshipType> allShiptypes, Player p, String id){
      militaryShiptypes = getMilitaryShiptypes(allShiptypes);
	  Collections.sort(militaryShiptypes,new SpaceshipTypeComparator());
      this.id = id;
      this.p = p;

      this.setLayout(null);
      setBackground(StyleGuide.colorBackground);
      
      filterChoice = new ComboBoxPanel();
      filterChoice.setBounds(10,10,200,20);
      filterChoice.addActionListener(this);
      fillFilterList();
      this.add(filterChoice);

      
      fillShiptypeList();
      
      
      graph = new BattleSimGraph();
      graph.setBounds(10, 490, 200, 100);
      add(graph);

      int x1 = 315;
      int x2 = 405;
      int y = 40;
      
      nrLbl = new SRLabel("Nr ships:");
      nrLbl.setBounds(x1,y,100,20);
      add(nrLbl);
      nrTF = new SRTextField("1");
      nrTF.setBounds(x2,y,100,20);
      add(nrTF);
      
      y += 30;

      techLbl = new SRLabel("Tech:");
      techLbl.setBounds(x1,y,100,20);
      add(techLbl);
      techTF = new SRTextField("0");
      techTF.setBounds(x2,y,100,20);
      add(techTF);

      y += 30;

      killsLbl = new SRLabel("Kills:");
      killsLbl.setBounds(x1,y,100,20);
      add(killsLbl);
      killsTF = new SRTextField("0");
      killsTF.setBounds(x2,y,100,20);
      add(killsTF);

      y += 30;

      damagedLbl = new SRLabel("Damaged(%):");
      damagedLbl.setBounds(x1,y,100,20);
      add(damagedLbl);
      damagedTF = new SRTextField("0");
      damagedTF.setBounds(x2,y,100,20);
      add(damagedTF);

      y += 30;

//      screenedCB = new CheckBoxPanel("Screened");
//      screenedCB.setBounds(x2, y, 100, 20);
//      add(screenedCB);

      vipsListLbl = new SRLabel("Select VIPs");
      vipsListLbl.setBounds(555,20,155,20);
      add(vipsListLbl);
      
      vipsList = new ListPanel();
      vipsList.setBounds(555,40,200,150);
      vipsList.setListSelectionListener(this);
      vipsList.setForeground(StyleGuide.colorCurrent);
      vipsList.setBackground(StyleGuide.colorBackground);
      vipsList.setBorder(new LineBorder(StyleGuide.colorCurrent));
      vipsList.setMultipleSelect(true);
      fillVipsList();
      vipsList.updateScrollList();
      add(vipsList);      
      
      int x = 280;
      
      // A force
      aForceLbl = new SRLabel("Attackers:");
      aForceLbl.setBounds(x+25,230,100,20);
      add(aForceLbl);

      aBuildCostLbl = new SRLabel("Build Cost:");
      aBuildCostLbl.setBounds(x+135,230,100,20);
      add(aBuildCostLbl);

      aSupplyCostLbl = new SRLabel("Supply Cost:");
      aSupplyCostLbl.setBounds(x+255,230,100,20);
      add(aSupplyCostLbl);
      
      addABtn = new SRButton("Add");
      addABtn.setBounds(x-28, 312, 46, 20);
      addABtn.addActionListener(this);
      add(addABtn);

      aForceTA = new SRTextArea();
      
      aForceTA.setFont(new Font("monospaced",Font.PLAIN,14));
 
      aForceScrollPane = new SRScrollPane(aForceTA);
      aForceScrollPane.setBounds(x+25,255,450,140);
      add(aForceScrollPane);

      // B force
      bForceLbl = new SRLabel("Defenders:");
      bForceLbl.setBounds(x+25,425,100,20);
      add(bForceLbl);

      bBuildCostLbl = new SRLabel("Build Cost:");
      bBuildCostLbl.setBounds(x+135,425,100,20);
      add(bBuildCostLbl);

      bSupplyCostLbl = new SRLabel("Supply Cost:");
      bSupplyCostLbl.setBounds(x+255,425,100,20);
      add(bSupplyCostLbl);
      
      addBBtn = new SRButton("Add");
      addBBtn.setBounds(x-28, 507, 46, 20);
      addBBtn.addActionListener(this);
      add(addBBtn);

      bForceTA = new SRTextArea();
      
      bForceTA.setFont(new Font("monospaced",Font.PLAIN,14));
 
      bForceScrollPane = new SRScrollPane(bForceTA);
      bForceScrollPane.setBounds(x+25,450,450,140);
      add(bForceScrollPane);

      // wins lbls
      aWinsLbl = new SRLabel("Wins:");
      aWinsLbl.setBounds(x+375,230,70,20);
      add(aWinsLbl);

      bWinsLbl = new SRLabel("Wins:");
      bWinsLbl.setBounds(x+375,425,70,20);
      add(bWinsLbl);

      // start sim button
      startSimBtn = new SRButton("Start");
      startSimBtn.setBounds(x+495, 570, 60, 20);
      startSimBtn.addActionListener(this);
      add(startSimBtn);

      // stop sim button
      stopSimBtn = new SRButton("Stop");
      stopSimBtn.setBounds(x+495, 540, 60, 20);
      stopSimBtn.addActionListener(this);
      stopSimBtn.setEnabled(false);
      add(stopSimBtn);
    }
    
    private void fillVipsList(){
    	List<VIPType> tmpVipTypes = p.getGalaxy().getGameWorld().getBattleVIPtypes();
    	Collections.sort(tmpVipTypes,new VIPTypeComparator());
        DefaultListModel dlm = (DefaultListModel)vipsList.getModel();
        for(int i = 0; i < tmpVipTypes.size(); i++){
          dlm.addElement(tmpVipTypes.get(i).getName());
        }
        vipTypes = tmpVipTypes;
        vipsList.updateScrollList();
        vipsList.clearSelected();
    }

    
    private List<SpaceshipType> getMilitaryShiptypes(List<SpaceshipType> allShipTypes){
    	List<SpaceshipType> milShiptypes = new LinkedList<SpaceshipType>();
    	for (SpaceshipType aShiptype : allShipTypes) {
			if (!aShiptype.isCivilian()){
				milShiptypes.add(aShiptype);
			}
		}
    	return milShiptypes;
    }
    
    private void fillFilterList(){
    	filterChoice.addItem("All (sort by name)");
    	filterChoice.addItem("All (sort by class & size)");
    	factions = Functions.cloneList(p.getGalaxy().getGameWorld().getFactions());
    	Collections.sort(factions,new FactionsComparator());
    	for (Faction aFaction : factions) {
			filterChoice.addItem(aFaction.getName());
		}
    }
    
    private void fillShiptypeList(){
    	if(shiptypelist != null){
    		remove(shiptypelist);
    	}
    	shiptypelist = new ListPanel();
        shiptypelist.setBounds(10,40,200,440);
        shiptypelist.setListSelectionListener(this);
        shiptypelist.setForeground(StyleGuide.colorCurrent);
        shiptypelist.setBackground(StyleGuide.colorBackground);
        shiptypelist.setBorder(new LineBorder(StyleGuide.colorCurrent));
    	
    	
        DefaultListModel dlm = (DefaultListModel)shiptypelist.getModel();
        dlm.removeAllElements();
//    	List<SpaceshipType> tempSstList = new LinkedList<SpaceshipType>();
    	List<SpaceshipType> tempSstList = null;
    	List<String> tempSstListName = new ArrayList<String>();
    	if (filterChoice.getSelectedIndex() > 1){
    		// get faction to show ships from
    		Faction showOnlyFaction = factions.get(filterChoice.getSelectedIndex() - 2);
    		tempSstList = showOnlyFaction.getSpaceshipTypes();
    	}else{
    		tempSstList = militaryShiptypes;
    	}
    	
    	
    	
    	if (filterChoice.getSelectedIndex() == 0){
    		
    		for(int i=0;i < tempSstList.size();i++){
        		String tempName ="";
    			tempName = tempSstList.get(i).getName();
        		tempSstListName.add(tempName);
        	}
    		
    		Collections.sort(tempSstListName,new SpaceshipTypeNameComparator());
    	//	Collections.sort(tempSstList,new SpaceshipTypeNameComparator());
    	}else{
    		Collections.sort(tempSstList,new SpaceshipTypeComparator());
    		
    		List<String> types = new ArrayList<String>();
    		types.add("defense");
    		types.add("civilan");
    		types.add("squadron");
    		types.add("small");
    		types.add("medium");
    		types.add("large");
    		types.add("huge");
    		
    		for(int i=0;i < tempSstList.size();i++){
    			String shipTypename = checkIfNewShipType(tempSstList.get(i), types);
    			if(shipTypename != null){
    				tempSstListName.add("------------" + shipTypename + "--------------------");
    			}
        		String tempName ="";
    			tempName = tempSstList.get(i).getName();
    		
        		tempSstListName.add(tempName);
        	}
    	}
        for(int i = 0; i < tempSstListName.size(); i++){
        	dlm.addElement(tempSstListName.get(i));
       // 	dlm.addElement(((SpaceshipType)tempSstList.get(i)).getName());
        }
        
        shiptypelist.updateScrollList();
        add(shiptypelist);
    }

    
    public void valueChanged(ListSelectionEvent lse){

    }


    public String getId(){
        return id;
    }
    
    private String checkIfNewShipType(SpaceshipType spaceshipType, List<String> types){
    	String type=null;
    	if((spaceshipType.isDefenceShip() && types.contains("defense")) ||
    			(spaceshipType.isCivilian() && types.contains("civilan")) ||
    			(spaceshipType.isSquadron() && types.contains("squadron")) ||
    			(!spaceshipType.isDefenceShip() && !spaceshipType.isSquadron() && !spaceshipType.isCivilian() 
    					&& types.contains(spaceshipType.getSizeString())) ){
    		if(spaceshipType.isDefenceShip()){
    			types.remove("defense");
    			type = "defense";
    		}else if(spaceshipType.isCivilian()){
    			types.remove("civilan");
    			type = "civilan";
    		}else if(spaceshipType.isSquadron()){
    			types.remove("squadron");
    			type = "squadron";
    		}else{
    			types.remove(spaceshipType.getSizeString());
    			type = spaceshipType.getSizeString();
    		}
    	}
    	return type;
    }

    public void updateData(){
    }
    
    /**
     * This method is called by the battle sim each time a new simulation is 
     * finished
     * @param bsr
     */
    public void battleSimPerformed(BattleSimResult battleSimResult){
    	long tf1winsPercent = Math.round(battleSimResult.getTf1wins()*100);
    	long tf2winsPercent = Math.round(battleSimResult.getTf2wins()*100);
    	graphPoints.add(0,new Integer((int)tf2winsPercent));
    	graph.drawGraph(battleSimResult.getIterations());
    	aWinsLbl.setText("Wins: " + tf1winsPercent + "%");
    	bWinsLbl.setText("Wins: " + tf2winsPercent + "%");
    }
    
    public void battleSimFinished(){
    	startSimBtn.setEnabled(true);
    	stopSimBtn.setEnabled(false);
    }
    
    private void startSim(){
    	battleSim = new BattleSim(this,p.getGalaxy().getGameWorld());
    	String tf1ships = aForceTA.getText();
    	String tf2ships = bForceTA.getText();
    	// compute costs
    	BattleSimTfCosts costs = battleSim.getTfCosts(tf1ships, tf2ships);
    	if (costs.getMessage() == null){
        	startSimBtn.setEnabled(false);
    		aBuildCostLbl.setText("Build Cost: " + costs.getTf1CostBuy());
    		aSupplyCostLbl.setText("Supply cost: " + costs.getTf1CostSupply());
    		bBuildCostLbl.setText("Build Cost: " + costs.getTf2CostBuy());
    		bSupplyCostLbl.setText("Supply cost: " + costs.getTf2CostSupply());
    		graphPoints = new LinkedList<Integer>();
    		graph.setPointsList(graphPoints);
    		stopSimBtn.setEnabled(true);
    		battleSim.simulateBattles(tf1ships, tf2ships, 9999, 1000, 0);
    	}else{
    		// show error message
    		String title = "Error when parsing ships";
    		String message = costs.getMessage();
    		Logger.fine("openMessagePopup called: " + message);
    		GeneralMessagePopupPanel popup = new GeneralMessagePopupPanel(title,this,message);
    		popup.setPopupSize(650,110);
    		popup.open(this);
    	}
    }

    @SuppressWarnings("deprecation")
	private void stopSim(){
    	stopSimBtn.setEnabled(false);
    	battleSim.stop();
    	graphPoints = null;
    	startSimBtn.setEnabled(true);
    }

    private void addShips(SRTextArea aTA){
    	String shiptypeName = shiptypelist.getSelectedItem();
    	if(!shiptypeName.contains("---")){
	    	if(shiptypeName.contains("*")){
	    		shiptypeName = shiptypeName.substring(1);
	    	}
	    	Galaxy g = p.getGalaxy();
	    	SpaceshipType sst = g.findSpaceshipType(shiptypeName);
	    	String appendString = "";
	    	if (!aTA.getText().equals("")){
	    		appendString = ";";
	    	}
	    	if (!nrTF.getText().equals("1")){
	    		appendString += "[" + nrTF.getText() + "]";
	    	}
			appendString += sst.getName();
			boolean addParanthesis = checkAddParanthesis();
			boolean addComma = false;
			if (addParanthesis){
				appendString += "(";
			}
//			if (screenedCB.isSelected()){
//				appendString += "s";
//				addComma = true;
//			}
			if (!techTF.getText().equals("0")){
				if (addComma){
					appendString += ",";
				}else{
					addComma = true;
				}
				appendString += "t:" + techTF.getText();
			}
			// kills
			if (!killsTF.getText().equals("0")){
				if (addComma){
					appendString += ",";
				}else{
					addComma = true;
				}
				appendString += "k:" + killsTF.getText();
			}
			// damaged
			if (!damagedTF.getText().equals("0")){
				if (addComma){
					appendString += ",";
				}else{
					addComma = true;
				}
				appendString += "d:" + damagedTF.getText();
			}
			// VIPs
			List<Integer> selectedVips = vipsList.getSelectedItems();
			if (selectedVips.size() > 0){
				for (Integer integer : selectedVips) {
			      	int index = integer.intValue();
			        VIPType aVipType = vipTypes.get(index);
					String vipShortName = aVipType.getShortName();
					if (addComma){
						appendString += ",";
					}else{
						addComma = true;
					}
					appendString += vipShortName;
				}
			}
			// end paranthesis
			if (addParanthesis){
				appendString += ")";
			}
	    	aTA.append(appendString);
	    	// clear components
	    	nrTF.setText("1");
	    	techTF.setText("0");
//	    	screenedCB.setSelected(false);
	    	killsTF.setText("0");
	    	damagedTF.setText("0");
	    	vipsList.clearSelected();
    	}else{
    		shiptypelist.clearSelected();
    	}
    }
    
    private boolean checkAddParanthesis(){
    	boolean addParanthesis = false;
//    	if (screenedCB.isSelected()){
//    		addParanthesis = true;
//    	}else
    	if (!techTF.getText().equals("0")){
    		addParanthesis = true;
    	}else
		if (!killsTF.getText().equals("0")){
    		addParanthesis = true;
		}else
		if (!damagedTF.getText().equals("0")){
    		addParanthesis = true;
		}else
		if (vipsList.getSelectedItems().size() > 0){
			addParanthesis = true;
		}
    	return addParanthesis;
    }

	public void actionPerformed(ActionEvent arg0) {
		Object o = arg0.getSource();
		if (o == filterChoice){
			fillShiptypeList();
		}else
		if (o == addABtn){
			addShips(aForceTA);
		}else
		if (o == addBBtn){
			addShips(bForceTA);
		}else
		if (o == startSimBtn){
			startSim();
		}else
		if (o == stopSimBtn){
			stopSim();
		}
	}
	
	  public void addToBattleSim(String shipsString, String side){
		  if(side.equalsIgnoreCase("A")){
			  if (!aForceTA.getText().equals("")){
				  aForceTA.append(";");
			  }
			  aForceTA.append(shipsString);
		  }else{
			  if (!bForceTA.getText().equals("")){
				  bForceTA.append(";");
			  }
			  bForceTA.append(shipsString);
		  }
	  }

	  public void stopBattleSim(){
		  if (battleSim != null){
			  stopSim();
		  }
	  }
}
