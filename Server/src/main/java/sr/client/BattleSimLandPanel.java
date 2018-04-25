package sr.client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.battlesim.BattleSimGraph;
import sr.client.battlesim.BattleSimLand;
import sr.client.battlesim.BattleSimLandCosts;
import sr.client.battlesim.BattleSimLandListener;
import sr.client.battlesim.BattleSimLandResult;
import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.client.components.SRTextField;
import sr.client.components.scrollable.ListPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.enums.BattleGroupPosition;
import sr.enums.TypeOfTroop;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.Faction;
import sr.world.Galaxy;
import sr.world.Player;
import sr.world.TroopType;
import sr.world.VIPType;
import sr.world.comparator.FactionsComparator;
import sr.world.comparator.VIPTypeComparator;
import sr.world.comparator.trooptype.TroopTypeComparator;
import sr.world.comparator.trooptype.TroopTypeNameComparator2;
import sr.world.comparator.trooptype.TroopTypeTypeAndBuildCostComparator;

public class BattleSimLandPanel extends SRBasePanel implements ListSelectionListener, SRUpdateablePanel, ActionListener, BattleSimLandListener {
	private static final long serialVersionUID = 1L;
    private String id;
	private List<TroopType> troopTypes;
	private List<VIPType> vipTypes;
    private List<Faction> factions;
    private ListPanel troopTypeList = null,vipsList;
    private ComboBoxPanel filterChoice;
    private SRLabel techLbl,nrLbl,killsLbl,damagedLbl,vipsListLbl,aForceLbl,bForceLbl,resLbl;
    private SRLabel aBuildCostLbl,bBuildCostLbl,aSupplyCostLbl,bSupplyCostLbl,aWinsLbl,bWinsLbl;
    private SRLabel averageLbl;
    private SRTextField techTF,nrTF,killsTF,damagedTF,resTF;
    private SRButton addABtn,addBBtn,startSimBtn,stopSimBtn;
    private Player p;
    private SRTextArea aForceTA,bForceTA;
    private JScrollPane aForceScrollPane,bForceScrollPane;
    private BattleSimLand landBattleSim; 
    private BattleSimGraph graph;
    private List<Integer> graphPoints;
    
    public BattleSimLandPanel(List<TroopType> allTroopTypes, Player p, String id){
	  Collections.sort(allTroopTypes,new TroopTypeComparator());
	  troopTypes = allTroopTypes;
      this.id = id;
      this.p = p;

      this.setLayout(null);
      setBackground(StyleGuide.colorBackground);
      
      filterChoice = new ComboBoxPanel();
      filterChoice.setBounds(10,10,200,20);
      filterChoice.addActionListener(this);
      fillFilterList();
      this.add(filterChoice);

      fillTroopTypeList();
      
      graph = new BattleSimGraph();
      graph.setBounds(10, 490, 200, 100);
      add(graph);

      int x1 = 315;
      int x2 = 405;
      int y = 40;
      
      nrLbl = new SRLabel("Nr troops:");
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

      

      resLbl = new SRLabel("Resistance:");
      resLbl.setBounds(x1,y,100,20);
      add(resLbl);
      resTF = new SRTextField("1");
      resTF.setBounds(x2,y,100,20);
      add(resTF);

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
      aForceLbl.setBounds(x+25,215,100,20);
      add(aForceLbl);

      aBuildCostLbl = new SRLabel("Build Cost:");
      aBuildCostLbl.setBounds(x+135,215,100,20);
      add(aBuildCostLbl);

      aSupplyCostLbl = new SRLabel("Supply Cost:");
      aSupplyCostLbl.setBounds(x+255,215,100,20);
      add(aSupplyCostLbl);
      
      addABtn = new SRButton("Add");
      addABtn.setBounds(x-28, 297, 46, 20);
      addABtn.addActionListener(this);
      add(addABtn);

      aForceTA = new SRTextArea();
      
      aForceTA.setFont(new Font("monospaced",Font.PLAIN,14));
 
      aForceScrollPane = new SRScrollPane(aForceTA);
      aForceScrollPane.setBounds(x+25,240,450,140);
      add(aForceScrollPane);

      // B force
      bForceLbl = new SRLabel("Defenders:");
      bForceLbl.setBounds(x+25,390,100,20);
      add(bForceLbl);

      bBuildCostLbl = new SRLabel("Build Cost:");
      bBuildCostLbl.setBounds(x+135,390,100,20);
      add(bBuildCostLbl);

      bSupplyCostLbl = new SRLabel("Supply Cost:");
      bSupplyCostLbl.setBounds(x+255,390,100,20);
      add(bSupplyCostLbl);
      
      addBBtn = new SRButton("Add");
      addBBtn.setBounds(x-28, 472, 46, 20);
      addBBtn.addActionListener(this);
      add(addBBtn);

      bForceTA = new SRTextArea();
      
      bForceTA.setFont(new Font("monospaced",Font.PLAIN,14));
 
      bForceScrollPane = new SRScrollPane(bForceTA);
      bForceScrollPane.setBounds(x+25,415,450,140);
      add(bForceScrollPane);

      // wins lbls
      aWinsLbl = new SRLabel("Wins:");
      aWinsLbl.setBounds(x+375,215,70,20);
      add(aWinsLbl);

      bWinsLbl = new SRLabel("Wins:");
      bWinsLbl.setBounds(x+375,390,70,20);
      add(bWinsLbl);

      averageLbl = new SRLabel("Average number of turns:");
      averageLbl.setBounds(x+275,570,200,20);
      add(averageLbl);

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
    	List<VIPType> tmpVipTypes = p.getGalaxy().getGameWorld().getLandBattleVIPtypes();
    	Collections.sort(tmpVipTypes,new VIPTypeComparator());
        DefaultListModel dlm = (DefaultListModel)vipsList.getModel();
        for(int i = 0; i < tmpVipTypes.size(); i++){
        	dlm.addElement(tmpVipTypes.get(i).getName());
        }
        vipTypes = tmpVipTypes;
        vipsList.updateScrollList();
        vipsList.clearSelected();
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
    
    private void fillTroopTypeList(){
    	if(troopTypeList != null){
    		remove(troopTypeList);
    	}
    	troopTypeList = new ListPanel();
        troopTypeList.setBounds(10,40,200,440);
        troopTypeList.setListSelectionListener(this);
        troopTypeList.setForeground(StyleGuide.colorCurrent);
        troopTypeList.setBackground(StyleGuide.colorBackground);
        troopTypeList.setBorder(new LineBorder(StyleGuide.colorCurrent));
        
    	
        DefaultListModel dlm = (DefaultListModel)troopTypeList.getModel();
        dlm.removeAllElements();
    	List<TroopType> tempTtList = null;
    	List<String> tempListNames = new ArrayList<String>();
    	if (filterChoice.getSelectedIndex() == 0){
    		tempTtList = troopTypes;
    	}else
    	if (filterChoice.getSelectedIndex() == 1){
    		tempTtList = troopTypes;
    	}else{
    		Faction showOnlyFaction = factions.get(filterChoice.getSelectedIndex() - 2);
    		tempTtList = showOnlyFaction.getTroopTypes();
    	}
    	// sort lists
    	if (filterChoice.getSelectedIndex() == 0){
    		Collections.sort(tempTtList,new TroopTypeNameComparator2());
    	}else{
    		Collections.sort(tempTtList,new TroopTypeTypeAndBuildCostComparator());
    	}
    	
    	List<String> types = new ArrayList<String>();
		types.add("defense");
		types.add("support");
		types.add("armor");
		types.add("Infantry");
		
    	// add to names list
    	for (TroopType aTT : tempTtList) {
        	if (filterChoice.getSelectedIndex() == 1){
        		String troopTypename = checkIfNewTroopType(aTT, types);
    			if(troopTypename != null){
    				tempListNames.add("------------" + troopTypename + "--------------------");
    			}
        	}
    		String tempName ="";
			tempName = aTT.getUniqueName();
    		tempListNames.add(tempName);
    	}

    	for (String aTtName : tempListNames) {
    		Logger.finer("aTtName: " + aTtName);
        	dlm.addElement(aTtName);
        }
    	troopTypeList.updateScrollList();
        add(troopTypeList);
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
    	if (lse.getSource() == troopTypeList){
    		showTroopType(troopTypeList.getSelectedItem());
    	}
    }

    @SuppressWarnings("unused")
	private TroopType findTroopType(String findname){
    	TroopType tt = null;
    	int i = 0;
    	while ((tt == null) & (i<troopTypes.size())){
    		TroopType temp = troopTypes.get(i);
    		if (temp.getUniqueName().equalsIgnoreCase(findname)){
    			tt = temp;
    		}else{
    			i++;
    		}
    	}
    	return tt;
    }

    public void showTroopType(String name){
    	if(name.contains("*")){
    		name = name.substring(1);
    	}
    }

    public String getId(){
        return id;
    }

    public void updateData(){
    }
    
    /**
     * This method is called by the battle sim each time a new simulation is 
     * finished
     * @param bsr
     */
    public void battleSimPerformed(BattleSimLandResult battleSimResult){
    	long tf1winsPercent = Math.round(battleSimResult.getTf1wins()*100);
    	long tf2winsPercent = Math.round(battleSimResult.getTf2wins()*100);
    	NumberFormat nf = NumberFormat.getInstance();
    	nf.setMaximumFractionDigits(1);
    	String avgStr = nf.format(battleSimResult.getAverageNrRounds());
    	graphPoints.add(0,new Integer((int)tf2winsPercent));
    	graph.drawGraph(battleSimResult.getIterations());
    	aWinsLbl.setText("Wins: " + tf1winsPercent + "%");
    	bWinsLbl.setText("Wins: " + tf2winsPercent + "%");
    	averageLbl.setText("Average number of turns: " + avgStr);
    }
    
    public void battleSimFinished(){
    	startSimBtn.setEnabled(true);
    	stopSimBtn.setEnabled(false);
    }
    
    private String getTroops(String unitsToParse, boolean returnTroops){
    	System.out.println("getTroops, unitsToParse: " + unitsToParse + " returnTroops: " + returnTroops);
    	String returnString = "";
    	StringTokenizer st = new StringTokenizer(unitsToParse,";");
    	while(st.hasMoreTokens()){
    		String aUnit = st.nextToken(); // value that may be added to return string
    		String tmpUnitName = aUnit;
    		
    		// remove abilities part
    		int otherAbilitiesStart = aUnit.indexOf("(");
    		if (otherAbilitiesStart > -1){
    			// vips/tech exist
    			tmpUnitName = tmpUnitName.substring(0,otherAbilitiesStart);
    		}
    		// check if tmpUnitName is a troop
    		TroopType tt = p.getGalaxy().getTroopType(tmpUnitName);
    		if (tt == null){
    			// try to find by short name
    			tt = p.getGalaxy().getTroopTypeByShortName(tmpUnitName);
    		}
    		// add if match to returnTroops
    		System.out.println(tmpUnitName + " " + tt + " " + returnTroops);
    		if (((tt != null) & returnTroops)){
    			System.out.println("added!");
    			if (!returnString.equals("")){
    				returnString += ";";
    			}
    			returnString += aUnit;
    		}
    	}
    	System.out.println(returnString);
    	return returnString;
    }

    private String getUnknownUnits(String unitsToParse){
    	System.out.println("getUnknownUnits, unitsToParse: " + unitsToParse);
    	String returnString = "";
    	StringTokenizer st = new StringTokenizer(unitsToParse,";");
    	while(st.hasMoreTokens()){
    		String aUnit = st.nextToken(); // value that may be added to return string
    		String tmpUnitName = aUnit;
    		
    		// remove abilities part
    		int otherAbilitiesStart = aUnit.indexOf("(");
    		if (otherAbilitiesStart > -1){
    			// vips/tech exist
    			tmpUnitName = tmpUnitName.substring(0,otherAbilitiesStart);
    		}
    		// check if tmpUnitName is a troop
    		TroopType tt = p.getGalaxy().getTroopType(tmpUnitName);
    		if (tt == null){
    			// try to find by short name
    			tt = p.getGalaxy().getTroopTypeByShortName(tmpUnitName);
    		}
    		// add if match to returnTroops
    		if (tt == null){
    			System.out.println("added unknown! " + tmpUnitName);
    			if (!returnString.equals("")){
    				returnString += ";";
    			}
    			returnString += aUnit;
    		}
    	}
    	System.out.println(returnString);
    	return returnString;
    }

    private void startSim(){
    	landBattleSim = new BattleSimLand(this,p.getGalaxy().getGameWorld());
    	String tf1troops = getTroops(aForceTA.getText(), true);
    	String tf1unknown = getUnknownUnits(aForceTA.getText());
    	String tf2troops = getTroops(bForceTA.getText(), true);
    	String tf2unknown = getUnknownUnits(bForceTA.getText());
    	System.out.println("tf1troops: " + tf1troops);
    	System.out.println("tf1unknown: " + tf1unknown);
    	System.out.println("tf2troops: " + tf2troops);
    	System.out.println("tf2unknown: " + tf2unknown);
    	String message = null;
    	if (!tf1unknown.equals("")){
    		message = "Unknown units in attacking force: " + tf1unknown;
    	}else
    	if (!tf2unknown.equals("")){
    		message = "Unknown units in defending force: " + tf2unknown;
    	}else{
    		// compute costs
    		BattleSimLandCosts costs = landBattleSim.getCosts(tf1troops, tf2troops);
        	if (costs.getMessage() != null){
        		message = costs.getMessage();
        	}else{
        		aBuildCostLbl.setText("Build Cost: " + costs.getAttTroopsCostBuy());
        		aSupplyCostLbl.setText("Supply cost: " + costs.getAttTroopsCostSupply());
        		bBuildCostLbl.setText("Build Cost: " + costs.getDefTroopsCostBuy());
        		bSupplyCostLbl.setText("Supply cost: " + costs.getDefTroopsCostSupply());
        	}
    	}
    	if (message == null){
    		startSimBtn.setEnabled(false);
    		graphPoints = new LinkedList<Integer>();
    		graph.setPointsList(graphPoints);
    		stopSimBtn.setEnabled(true);
    		int res = Integer.parseInt(resTF.getText());
    		landBattleSim.simulateBattles(tf1troops,tf2troops,9999,1000,0,false,res);
    	}else{
    		// show error message
    		String title = "Error when parsing ships";
    		Logger.fine("openMessagePopup called: " + message);
    		GeneralMessagePopupPanel popup = new GeneralMessagePopupPanel(title,this,message);
    		popup.setPopupSize(650,110);
    		popup.open(this);
    	}
    }

    @SuppressWarnings("deprecation")
	private void stopSim(){
    	stopSimBtn.setEnabled(false);
    	landBattleSim.stop();
    	graphPoints = null;
    	startSimBtn.setEnabled(true);
    }

    private void addTroops(SRTextArea aTA){
    	String typeName = troopTypeList.getSelectedItem();

    	if(!typeName.contains("---")){
	    	Galaxy g = p.getGalaxy();
	    	TroopType tt = g.findTroopType(typeName);
	    	
	    	String appendString = "";
	    	if (!aTA.getText().equals("")){
	    		appendString = ";";
	    	}
	    	if (!nrTF.getText().equals("1")){
	    		appendString += "[" + nrTF.getText() + "]";
	    	}
	    	if (tt != null){
	    		appendString += tt.getUniqueName();
	    	}
			boolean addParanthesis = checkAddParanthesis();
			boolean addComma = false;
			if (addParanthesis){
				appendString += "(";
			}
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
	    	killsTF.setText("0");
	    	damagedTF.setText("0");
	    	vipsList.clearSelected();
    	}else{
    		troopTypeList.clearSelected();
    	}
    }
    
    private boolean checkAddParanthesis(){
    	boolean addParanthesis = false;
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
			fillTroopTypeList();
		}else
		if (o == addABtn){
			addTroops(aForceTA);
		}else
		if (o == addBBtn){
			addTroops(bForceTA);
		}else
		if (o == startSimBtn){
			startSim();
		}else
		if (o == stopSimBtn){
			stopSim();
		}
	}
	
	  public void addToBattleSim(String troopsString, String side){
		  if(side.equalsIgnoreCase("A")){
			  if (!aForceTA.getText().equals("")){
				  aForceTA.append(";");
			  }
			  aForceTA.append(troopsString);
		  }else{
			  if (!bForceTA.getText().equals("")){
				  bForceTA.append(";");
			  }
			  bForceTA.append(troopsString);
		  }
	  }

	  public void stopBattleSim(){
		  if (landBattleSim != null){
			  stopSim();
		  }
	  }
}
