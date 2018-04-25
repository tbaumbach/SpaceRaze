package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
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
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.BuildingType;
import sr.world.Faction;
import sr.world.Player;
import sr.world.ResearchAdvantage;
import sr.world.SpaceshipType;
import sr.world.TroopType;
import sr.world.VIPType;
import sr.world.comparator.FactionsComparator;
import sr.world.comparator.ResearchDevelopedComparator;
import sr.world.orders.ResearchOrder;

@SuppressWarnings("serial")
public class ResearchPanel extends SRBasePanel implements ListSelectionListener, SRUpdateablePanel, ActionListener {
	private String id;
	//private List<SRButton> parent, child;
	private ListPanel rotResearchAdvantageList,shipList,troopsList,vipsList, buildingsList;
	private List<Faction> factions;
	private ComboBoxPanel treeChoice;
	private SRLabel name, name2, childLabel, parentLabel,descriptionLabel,detailsLabel,shipListLabel,troopListLabel,vipListLabel,buildingListLabel ,turnInfo,costLabel;
	private JScrollPane scrollPaneDetails,scrollPaneDescription;
    private SRTextArea detailsArea,descriptionArea;
    private SRButton viewShipButton,viewTroopButton,viewVIPButton, viewBuildingButton, doResearchButton;
    private Player p;
    private static int column1X = 220, columnUnitX = 660;
    private int parentAndChildButtonMaxIndexNumber= 0;
    private int[] parentAndChildButtonXCordinate,parentAndChildButtonYCordinate;
    private SpaceRazeApplet client;
	
	public ResearchPanel(Player p, String id, SpaceRazeApplet client){
		this.client = client;
		this.id = id;
		this.p = p;
		
		this.setLayout(null);
		setBackground(StyleGuide.colorBackground);

		treeChoice = new ComboBoxPanel();
		treeChoice.setBounds(10,10,195,20);
		treeChoice.addActionListener(this);
		treeChoice.setToolTipText("Choice to see your research tree or factions tree. You can only research on your own tree.");
	    fillTreeList();
	    this.add(treeChoice);
	
	    // Ship List label
	    shipListLabel = new SRLabel();
	    shipListLabel.setBounds(columnUnitX,240,175,16);
	    shipListLabel.setText("New Space Ships:");
	    shipListLabel.setToolTipText("Shows new space ships model that will be added to you.");
	    shipListLabel.setVisible(false);
        add(shipListLabel);
	    
	    // Ship List
	    shipList = new ListPanel();
	    shipList.setBounds(columnUnitX,259,175,45);
	    shipList.setListSelectionListener(this);
	    shipList.setForeground(StyleGuide.colorCurrent);
	    shipList.setBackground(StyleGuide.colorBackground);
	    shipList.setBorder(new LineBorder(StyleGuide.colorCurrent));
	    shipList.setToolTipText("Mark a ship in the list and hit 'View Ship Details' button to view ship details");
	    shipList.setVisible(false);
	    add(shipList);
	    
	    //	  View Ship Info
	    viewShipButton = new SRButton("View Ship Details");
	    viewShipButton.setBounds(columnUnitX,307,175,20);
	    viewShipButton.addActionListener(this);
	    viewShipButton.setVisible(false);
	    viewShipButton.setToolTipText("Mark a ship in the list and hit this button to view ship details");
	    add(viewShipButton);

	    // Troop List label
	    troopListLabel = new SRLabel();
	    troopListLabel.setBounds(columnUnitX,253,175,16);
	    troopListLabel.setText("New Troops:");
	    troopListLabel.setToolTipText("Shows new troops that will be added to you.");
	    troopListLabel.setVisible(false);
        add(troopListLabel);
	    
	    // Troop List
        troopsList = new ListPanel();
	    troopsList.setBounds(columnUnitX,272,175,45);
	    troopsList.setListSelectionListener(this);
	    troopsList.setForeground(StyleGuide.colorCurrent);
	    troopsList.setBackground(StyleGuide.colorBackground);
	    troopsList.setBorder(new LineBorder(StyleGuide.colorCurrent));
	    troopsList.setToolTipText("Mark a troop in the list and hit 'View Troop Details' button to view troop details");
	    troopsList.setVisible(false);
	    add(troopsList);
	    
	    // View Troop Info
	    viewTroopButton = new SRButton("View Troop Details");
	    viewTroopButton.setBounds(columnUnitX,320,175,20);
	    viewTroopButton.addActionListener(this);
	    viewTroopButton.setToolTipText("Mark a Troop in the list and hit this button to view troop details");
	    viewTroopButton.setVisible(false);
	    add(viewTroopButton);
	    
	    //VIP List label
	    vipListLabel = new SRLabel();
	    vipListLabel.setBounds(columnUnitX,344,175,16);
	    vipListLabel.setText("New VIPs:");
	    vipListLabel.setToolTipText("Shows new VIPs that will be added to you.");
	    vipListLabel.setVisible(false);
        add(vipListLabel);
	    
	    // VIP List
        vipsList = new ListPanel();
        vipsList.setBounds(columnUnitX,363,175,45);
        vipsList.setListSelectionListener(this);
        vipsList.setForeground(StyleGuide.colorCurrent);
        vipsList.setBackground(StyleGuide.colorBackground);
        vipsList.setBorder(new LineBorder(StyleGuide.colorCurrent));
        vipsList.setToolTipText("Mark a VIP in the list and hit 'View VIP Details' button to view VIP details");
        vipsList.setVisible(false);
	    add(vipsList);
	    
	    // View VIP Info
	    viewVIPButton = new SRButton("View VIP Details");
	    viewVIPButton.setBounds(columnUnitX,411,175,20);
	    viewVIPButton.addActionListener(this);
	    viewVIPButton.setToolTipText("Mark a VIP in the list and hit this button to view VIP details");
	    viewVIPButton.setVisible(false);
	    add(viewVIPButton);
	    
	    // Building List label
	    buildingListLabel = new SRLabel();
	    buildingListLabel.setBounds(columnUnitX,344,175,16);
	    buildingListLabel.setText("New Buildings:");
	    buildingListLabel.setToolTipText("Shows new Buildings that will be added to you.");
	    buildingListLabel.setVisible(false);
        add(buildingListLabel);
	    
	    // Building List
        buildingsList = new ListPanel();
        buildingsList.setBounds(columnUnitX,363,175,45);
        buildingsList.setListSelectionListener(this);
        buildingsList.setForeground(StyleGuide.colorCurrent);
        buildingsList.setBackground(StyleGuide.colorBackground);
        buildingsList.setBorder(new LineBorder(StyleGuide.colorCurrent));
        buildingsList.setToolTipText("Mark a building in the list and hit 'View Building Details' button to view Building details");
        buildingsList.setVisible(false);
	    add(buildingsList);
	    
	    // View Building Info
	    viewBuildingButton = new SRButton("View Building Details");
	    viewBuildingButton.setBounds(columnUnitX,411,175,20);
	    viewBuildingButton.addActionListener(this);
	    viewBuildingButton.setToolTipText("Mark a building in the list and hit this button to view building details");
	    viewBuildingButton.setVisible(false);
	    add(viewBuildingButton);
	    
	    

	    // Right Column
	    // ************
	    
	    name = new SRLabel();
	    name.setBounds(column1X,10,100,16);
	    name.setText("Name:");
	    name.setToolTipText("The name on the research advantage");
        add(name);
        
        name2 = new SRLabel();
        name2.setBounds(column1X+50,10,290,16);
        name2.setToolTipText("The name on the research advantage");
        add(name2);
        
        

        childLabel = new SRLabel();
        childLabel.setBounds(column1X,74,300,16);
        childLabel.setText("Allows development of:");
        childLabel.setToolTipText("Gives this advantages to research on");
        add(childLabel);
        
        // Put in the childs as buttons. width= 112 hieght = 20 top= 37. Use index to remove and add.
		
        parentLabel = new SRLabel();
        parentLabel.setBounds(column1X,105,300,16);
        parentLabel.setText("Requirements:");
        parentLabel.setToolTipText("Complete the research on this advantage before you start to research on this.");
        add(parentLabel);
        
        // Put in the parent as buttons. width= 112 hieght = 20 top= 78. Use index to remove and add.
        
		
        descriptionLabel = new SRLabel();
		descriptionLabel.setBounds(column1X,156,200,16);
		descriptionLabel.setText("Description:");
		add(descriptionLabel);
        
        descriptionArea = new SRTextArea();
        
        descriptionArea.setToolTipText("Description text");
        descriptionArea.setEditable(true);

	    scrollPaneDescription = new SRScrollPane(descriptionArea);
	    scrollPaneDescription.setBounds(column1X,175,400,64);
	    scrollPaneDescription.setVisible(true);
	    scrollPaneDescription.setToolTipText("Description text");
	    add(scrollPaneDescription);
	    
	    detailsLabel = new SRLabel();
	    detailsLabel.setBounds(column1X,242,190,16);
	    detailsLabel.setText("Details:");
	    detailsLabel.setToolTipText("Shows info about propertys that will be changed.");
        add(detailsLabel);
		
        detailsArea = new SRTextArea();
        
        detailsArea.setToolTipText("Shows info about propertys that will be changed.");
        detailsArea.setEditable(true);

        scrollPaneDetails = new SRScrollPane(detailsArea);
        scrollPaneDetails.setBounds(column1X,260,400,175);
        scrollPaneDetails.setVisible(true);
        scrollPaneDetails.setToolTipText("Shows info about propertys that will be changed.");
	    add(scrollPaneDetails);
	    
	    turnInfo = new SRLabel();
	    turnInfo.setBounds(column1X,29,500,16);
	    turnInfo.setText("");
	    turnInfo.setVisible(false);
	    turnInfo.setToolTipText("Number of turn to research.");
        add(turnInfo);
        
        costLabel  = new SRLabel();
        costLabel.setBounds(column1X,48,500,20);
        costLabel.setText("");
        costLabel.setVisible(false);
        costLabel.setToolTipText("The Cost to research each turn.");
        add(costLabel);
	    
	    // Research or cancel it.
	    doResearchButton = new SRButton("Research");
	    doResearchButton.setBounds(columnUnitX,600,180,20);
	    doResearchButton.addActionListener(this);
	    doResearchButton.setVisible(false);
	    doResearchButton.setToolTipText("Hit this button to start or cancel researching");
	    add(doResearchButton); 
	    
	    rotResearchAdvantageList = new ListPanel();
//	    rotResearchAdvantageList.setBounds(10,40,155,230);
	  //  rotResearchAdvantageList.setBounds(10,40,155,170);
	    rotResearchAdvantageList.setBounds(10,35,195,585);
	    rotResearchAdvantageList.setListSelectionListener(this);
	    rotResearchAdvantageList.setForeground(StyleGuide.colorCurrent);
	    rotResearchAdvantageList.setBackground(StyleGuide.colorBackground);
	    rotResearchAdvantageList.setBorder(new LineBorder(StyleGuide.colorCurrent));
	    fillResearchAdvantageList();
	    rotResearchAdvantageList.updateScrollList();
	    rotResearchAdvantageList.setToolTipText("This is all roots advantages in the research tree and all yours on going advantages and all available advantages");
	    add(rotResearchAdvantageList);

	}
	
	 private void fillTreeList(){
		 treeChoice.addItem("My tree");
		 
		 factions = Functions.cloneList(p.getGalaxy().getGameWorld().getFactions());
	    	Collections.sort(factions,new FactionsComparator());
	    	for (Faction aFaction : factions) {
				treeChoice.addItem(aFaction.getName());
			}
	    }
	 
	 private void fillResearchAdvantageList(){
		 removeResearchAdvantage();
		 DefaultListModel dlm = (DefaultListModel)rotResearchAdvantageList.getModel();
		 dlm.removeAllElements();
		 List<ResearchAdvantage> tmpAdvantages = null;
		 if (treeChoice.getSelectedIndex() > 0){
    		// get faction root ResearchAdvantages
			 Faction showOnlyFaction = factions.get(treeChoice.getSelectedIndex() - 1);
			 tmpAdvantages = showOnlyFaction.getResearch().getAdvantages();
			 
			 for(int i = 0; i < tmpAdvantages.size(); i++){
				 dlm.addElement(tmpAdvantages.get(i).getName());
			 }
			 
		 }else{
			 tmpAdvantages = p.getResearch().getAllAdvantagesThatIsReadyToBeResearchOn();
			 Collections.sort(tmpAdvantages,new ResearchDevelopedComparator());
			 boolean developedFound = false;
			 dlm.add(0,"-Ready For Research-");
			 for(int i = 0; i < tmpAdvantages.size(); i++){				 
				 
				 if(p.getOrders().checkResearchOrder(tmpAdvantages.get(i).getName())){
					 if(dlm.get(0).toString().equalsIgnoreCase("---Ongoing---")){
						 dlm.add(1,tmpAdvantages.get(i).getName());
					 }else{
						 dlm.add(0,"---Ongoing---");
						 dlm.add(1, tmpAdvantages.get(i).getName());
					 }
					 
				 }else{
					 if (!developedFound & tmpAdvantages.get(i).isDeveloped()){
						 dlm.addElement("---Finished---");
						 developedFound = true;
					 }
					 dlm.addElement(tmpAdvantages.get(i).getName());
				 }
				 
			 }
			 
		 }
		 rotResearchAdvantageList.clearSelected();
		 
	 }
	 
	 public void valueChanged(ListSelectionEvent lse){
		 if (lse.getSource() == rotResearchAdvantageList){
			 if(!rotResearchAdvantageList.getSelectedItem().equalsIgnoreCase("-Ready For Research-") && !rotResearchAdvantageList.getSelectedItem().equalsIgnoreCase("---Ongoing---")){
				 showResearchAdvantage(rotResearchAdvantageList.getSelectedItem());
			 }
			 
		 }
		 if (lse.getSource() == shipList){
			 Logger.fine("viewShipButton.setVisible(true)");
			 viewShipButton.setVisible(true);
			 repaint();
		 }
		 if (lse.getSource() == troopsList){
			 Logger.fine("viewTroopButton.setVisible(true)");
			 viewTroopButton.setVisible(true);
			 repaint();
		 }
		 if (lse.getSource() == vipsList){
			 Logger.fine("viewVIPButton.setVisible(true)");
			 viewVIPButton.setVisible(true);
			 repaint();
		 }
		 if (lse.getSource() == buildingsList){
			 Logger.fine("viewBuildingButton.setVisible(true)");
			 viewBuildingButton.setVisible(true);
			 repaint();
		 }
	 }
	 
	 public void showResearchAdvantage(String researchAdvantagename){
		 ResearchAdvantage researchAdvantage;
		 // remove the old data.
		 removeResearchAdvantage();
		 
		 Logger.fine("showResearchAdvantage(String researchAdvantagename) " + researchAdvantagename);
		 if(treeChoice.getSelectedIndex() == 0){
			 researchAdvantage = p.getResearch().getAdvantage(researchAdvantagename);
			 Logger.fine("p.getResearch().getAdvantage(researchAdvantagename) (own) " + researchAdvantage.getName());
		 }
		 else{
			 researchAdvantage = p.getGalaxy().getFaction(treeChoice.getSelectedItem()).getResearch().getAdvantage(researchAdvantagename);
			 Logger.fine("ReserachPanel Faction " + researchAdvantage.getName());
		 }
		 
		 int xpos=0;
		 
		 if(researchAdvantage.getShips().size() > 0){
			 DefaultListModel dlm = (DefaultListModel)shipList.getModel();
			 dlm.removeAllElements();
			 List<SpaceshipType> tmpShips = null;
			 tmpShips = researchAdvantage.getShips();
		    	
			 for(int i=0;i<tmpShips.size();i++){
				 dlm.addElement(tmpShips.get(i).getName());
			 }
			 shipList.updateScrollList();
			 shipListLabel.setVisible(true);
			 shipList.setVisible(true);
			 //viewShipButton.setVisible(true);
			 
			 xpos=90;
		 }
		 else{
			 shipListLabel.setVisible(false);
			 shipList.setVisible(false);
			 viewShipButton.setVisible(false);
		 }

		 if(researchAdvantage.getTroopTypes().size() > 0){
			 DefaultListModel dlm = (DefaultListModel)troopsList.getModel();
			 dlm.removeAllElements();
			 List<TroopType> tmpTroops = null;
			 tmpTroops = researchAdvantage.getTroopTypes();
			 for (TroopType aTroopType : tmpTroops) {
				 dlm.addElement(aTroopType.getUniqueName());
			 }
			 troopsList.setLocation(columnUnitX , 259+ xpos);
			 troopListLabel.setLocation(columnUnitX, 240+ xpos);
			 viewTroopButton.setLocation(columnUnitX, 307+ xpos);
			 troopsList.updateScrollList();
			 troopListLabel.setVisible(true);
			 troopsList.setVisible(true);
			 //viewTroopButton.setVisible(true);
			 
			 xpos+=90;
		 }else{
			 troopListLabel.setVisible(false);
			 troopsList.setVisible(false);
			 viewTroopButton.setVisible(false);
		 }
		 
		 if(researchAdvantage.getVIPTypes().size() > 0){
			 DefaultListModel dlm = (DefaultListModel)vipsList.getModel();
			 dlm.removeAllElements();
			 List<VIPType> tmpVIPs = null;
			 tmpVIPs = researchAdvantage.getVIPTypes();
			 for (VIPType aVIPType : tmpVIPs) {
				 dlm.addElement(aVIPType.getName());
			 }
			 vipsList.setLocation(columnUnitX, 259+ xpos);
			 vipListLabel.setLocation(columnUnitX, 240+ xpos);
			 viewVIPButton.setLocation(columnUnitX, 307+ xpos);
			 vipsList.updateScrollList();
			 vipListLabel.setVisible(true);
			 vipsList.setVisible(true);
			 //viewVIPButton.setVisible(true);
			 
			 xpos+=90;
		 }else{
			 vipListLabel.setVisible(false);
			 vipsList.setVisible(false);
			 viewVIPButton.setVisible(false);
		 }
		 
		 if(researchAdvantage.getBuildingTypes().size() > 0){
			 DefaultListModel dlm = buildingsList.getModel();
			 dlm.removeAllElements();
			 List<BuildingType> tmpBuildings = null;
			 tmpBuildings = researchAdvantage.getBuildingTypes();
			 for (BuildingType aBuildingType : tmpBuildings) {
				 dlm.addElement(aBuildingType.getName());
			 }
			 buildingsList.setLocation(columnUnitX, 259+ xpos);
			 buildingListLabel.setLocation(columnUnitX , 240+ xpos);
			 viewBuildingButton.setLocation(columnUnitX, 307+ xpos);
			 buildingsList.updateScrollList();
			 buildingListLabel.setVisible(true);
			 buildingsList.setVisible(true);
			 //viewBuildingButton.setVisible(true);
			 
			 xpos+=90;
		 }else{
			 buildingListLabel.setVisible(false);
			 buildingsList.setVisible(false);
			 viewBuildingButton.setVisible(false);
		 }

		 name2.setText(researchAdvantage.getName());
		 
		 
		 parentAndChildButtonXCordinate = new int[researchAdvantage.getChildren().size()+ researchAdvantage.getParents().size()];
		 parentAndChildButtonYCordinate = new int[researchAdvantage.getChildren().size()+ researchAdvantage.getParents().size()];
		 
		 int tmpY = 92;
		 int offsetY = 25; // extra pixels for each row of advantages
//		 int rowCounter = 0; // number of extra rows
		 int buttonWidth= 202;
		 
		 // Adding buttons to go against childs researchAdvantage
		 if(researchAdvantage.getChildren().size() > 0){
			 Logger.fine("researchAdvantage.getChildren().size() " + researchAdvantage.getChildren().size());
			 int tempchildButtonXCordinat = column1X;
			 int columnCounter = 1;
			 
			 for(int i=0; i < researchAdvantage.getChildren().size();i++){
				 Logger.fine("researchAdvantage.getChildren().get(i).getName() " + researchAdvantage.getChildren().get(i).getName());
				 
				 if (columnCounter == 4){ // start on a new row
					 columnCounter = 1;
					 tempchildButtonXCordinat = column1X;
					 tmpY += offsetY;
				 }
				 
				 SRButton tempbutton = new SRButton(researchAdvantage.getChildren().get(i).getName());
				 tempbutton.setBounds(tempchildButtonXCordinat, tmpY, buttonWidth, 20);
				 tempbutton.addActionListener(this);
				 tempbutton.setToolTipText("Hit this button to see the child research advantage: " + researchAdvantage.getChildren().get(i).getName());
				 parentAndChildButtonXCordinate[parentAndChildButtonMaxIndexNumber] = tempchildButtonXCordinat;
				 parentAndChildButtonYCordinate[parentAndChildButtonMaxIndexNumber] = tmpY;
				 parentAndChildButtonMaxIndexNumber++;
				 add(tempbutton);
				 tempchildButtonXCordinat+= buttonWidth +5;
				 
				 columnCounter++;
				 
			 }
		 }

		 tmpY += 23;

		 parentLabel.setLocation(column1X,tmpY);

		 tmpY += 18;
		 
		 // Adding buttons to go against parents researchAdvantage
		 if(researchAdvantage.getParents().size() > 0){
			 Logger.fine("researchAdvantage.getParents().size() " + researchAdvantage.getParents().size());
			 int tempParentButtonXCordinat = column1X;
			 int columnCounter = 1;
			 
			 for(int i=0; i < researchAdvantage.getParents().size();i++){
				 Logger.fine("researchAdvantage.getParents().get(i).getName() " + researchAdvantage.getParents().get(i).getName());
				 
				 SRButton tempbutton = new SRButton(researchAdvantage.getParents().get(i).getName());
				 tempbutton.setBounds(tempParentButtonXCordinat, tmpY, buttonWidth, 20);
				 tempbutton.addActionListener(this);
				 tempbutton.setToolTipText("Hit this button to see the parent research advantage: " + researchAdvantage.getParents().get(i).getName());
				 add(tempbutton);
				 parentAndChildButtonXCordinate[parentAndChildButtonMaxIndexNumber] = tempParentButtonXCordinat;
				 parentAndChildButtonYCordinate[parentAndChildButtonMaxIndexNumber] = tmpY;
				 parentAndChildButtonMaxIndexNumber++;
				 tempParentButtonXCordinat+= buttonWidth + 5;
				 				 
				 columnCounter++;
				 if (columnCounter == 4){ // start on a new row
					 tmpY += offsetY;
					 columnCounter = 1;
					 tempParentButtonXCordinat = column1X;
				 }
			 }
		 }
		 
		 tmpY += 23;
		 descriptionLabel.setLocation(column1X,tmpY);
		 
		 tmpY += 19;
		 scrollPaneDescription.setLocation(column1X,tmpY);
		 descriptionArea.setText(researchAdvantage.getDescription());
		 
		 tmpY += 5 + scrollPaneDescription.getHeight();
		 detailsLabel.setLocation(column1X,tmpY);
		 
		 tmpY += 19;
		 scrollPaneDetails.setLocation(column1X,tmpY);
		 detailsArea.setText(researchAdvantage.getResearchText());
		 
		 if(treeChoice.getSelectedIndex() == 0){
			 if(researchAdvantage.isDeveloped()){
				 turnInfo.setText("This advantage is done");
			 }else{
				 if(researchAdvantage.isReadyToBeResearchedOn()){
					 
					 turnInfo.setText("Develop time:   " + new Integer(researchAdvantage.getTimeToResearch()).toString() + " turns (" + new Integer(researchAdvantage.getTimeToResearch()-researchAdvantage.getResearchedTurns()).toString() + " turns left)");
					 
					 Logger.fine(" checking if researchAdvantage.getName() is ongoing " + researchAdvantage.getName());
					 
					 
					 // if(p.getResearch().isOnGoingResearchedAdvantage(researchAdvantage))
					 
					 if(p.getOrders().checkResearchOrder(researchAdvantage.getName())){
						 doResearchButton.setText("Cancel the research");
						 doResearchButton.setVisible(true);
					 }else{
						 if(researchAdvantage.getResearchedTurns() > 0){
							 doResearchButton.setText("Continue the research");
						 }else{
							 doResearchButton.setText("Start to research");
						 }
						 doResearchButton.setVisible(true);
					 }
				 
				 }else{
					 if(researchAdvantage.getTimeToResearch() == 0){
						 turnInfo.setText("Will be done then all the parent is developed");
					 }else{
						 turnInfo.setText("Develop time:   " + new Integer(researchAdvantage.getTimeToResearch()).toString() + " turns");
					 }
					 
				 }
				 
			 }
			 
			 
		 }
		 else{
			 if(researchAdvantage.getTimeToResearch() == 0){
				 turnInfo.setText("Will be done then all the parent is developed");
			 }else{
				 turnInfo.setText("Develop time:   " + new Integer(researchAdvantage.getTimeToResearch()).toString() + " turns");
			 }
		 }
		 
		 tmpY += 5 + scrollPaneDetails.getHeight();
		 turnInfo.setVisible(true);
		 
		 if(researchAdvantage.getCostToResearchOneTurn() > 0 || researchAdvantage.getCostToResearchOneTurnInProcent() > 0){
			 
			 if(researchAdvantage.getCostToResearchOneTurn() > 0){
				 costLabel.setText("Cost:   " + researchAdvantage.getCostToResearchOneTurn() +" each turn");
			 }else{
				 
				 //p.getGalaxy().getPlayerIncome(p)
				 
				if(treeChoice.getSelectedIndex() == 0){
					int price = countCost(researchAdvantage);
					 if(price < 1){
						 price = 1;
					 }
					 costLabel.setText("Cost:   " + researchAdvantage.getCostToResearchOneTurnInProcent() + "% of total incom = " + price + " each turn");
			 
				 }else{
					 costLabel.setText("Cost:   " + researchAdvantage.getCostToResearchOneTurnInProcent() + "% of income each turn");
				 }
			//	 costLabel.setText("Cost " + researchAdvantage.getCostToResearchOneTurnInProcent() + "% of total income each turn");
			 }
			 
			 
			 costLabel.setVisible(true);
		 }
		 
		 tmpY += 23;
		 
		 repaint();
		 
	 }
	 
	 public void removeResearchAdvantage(){
		 
		shipListLabel.setVisible(false);
		shipList.setVisible(false);
		troopListLabel.setVisible(false);
		troopsList.setVisible(false);
		vipListLabel.setVisible(false);
		vipsList.setVisible(false);
		buildingListLabel.setVisible(false);
		buildingsList.setVisible(false);
		name2.setText("");
		 
		for(int i =0;i < parentAndChildButtonMaxIndexNumber; i++){
			 
			Logger.fine(" testResearch removeResearchAdvantage remove Button " + getComponentAt(parentAndChildButtonXCordinate[i], parentAndChildButtonYCordinate[i]).toString());
			remove(getComponentAt(parentAndChildButtonXCordinate[i], parentAndChildButtonYCordinate[i]));
		}
		parentAndChildButtonMaxIndexNumber = 0;
		descriptionArea.setText("");
		viewShipButton.setVisible(false);
		viewTroopButton.setVisible(false);
		viewVIPButton.setVisible(false);
		doResearchButton.setVisible(false);
		turnInfo.setVisible(false);
		costLabel.setVisible(false);
		repaint();
		 
	 }
	
	public void actionPerformed(ActionEvent arg0) {
		Object o = arg0.getSource();
		
		if (o == treeChoice){
			fillResearchAdvantageList();
		}else
		if (o == viewShipButton){
			if(treeChoice.getSelectedItem().equalsIgnoreCase("My tree")){
				client.showShiptypeDetails(shipList.getSelectedItem(), "Yours");
			}else{
				client.showShiptypeDetails(shipList.getSelectedItem(), treeChoice.getSelectedItem());
			}
			
	  	}else
		if (o == viewTroopButton){
			if(treeChoice.getSelectedItem().equalsIgnoreCase("My tree")){
				client.showTroopTypeDetails(troopsList.getSelectedItem(), "Yours");
			}else{
				client.showTroopTypeDetails(troopsList.getSelectedItem(), treeChoice.getSelectedItem());
			}
			
	  	}else
		if (o == viewVIPButton){
			// fixa så att man tittar på sinna egna vipar i player eller i faction för resten.
			if(treeChoice.getSelectedItem().equalsIgnoreCase("My tree")){
				client.showVIPTypeDetails(vipsList.getSelectedItem(), "All");
			}else{
				client.showVIPTypeDetails(vipsList.getSelectedItem(), treeChoice.getSelectedItem());
			}
			
	  	}else
		if (o == viewBuildingButton){
			if(treeChoice.getSelectedItem().equalsIgnoreCase("My tree")){
				if (troopsList.isVisible()){
					client.showTroopTypeDetails(troopsList.getSelectedItem(), "Yours");
				}else
				if (buildingsList.isVisible()){
					client.showBuildingTypeDetails(buildingsList.getSelectedItem(),  "Yours");
				}
			}else{
				client.showBuildingTypeDetails(buildingsList.getSelectedItem(), treeChoice.getSelectedItem());
			}
			
		}else
		if (o == doResearchButton){
			if(doResearchButton.getText().equals("Cancel the research")){
				Logger.fine("(ResearchPanel.java) Cancel the research p.getOrders().getResearchOrders().size()" + p.getOrders().getResearchOrders().size());
			//	p.getResearch().removeOnGoingResearchedAdvantage(p.getResearch().getAdvantage(name2.getText()));
				
				// removes Order text in Order Panel
				p.getOrders().removeResearchOrder(name2.getText(), p.getGalaxy());
				// ta bort denna lop. körs aldrig.
			/*	for(int i=0; i < p.getOrders().getResearchOrders().size();i++){
					ResearchOrder ro = (ResearchOrder)p.getOrders().getResearchOrders().get(i);
					
					LoggingHandler.fine("(ResearchPanel.java) Cancel the research ro.getAdvantageName() " + ro.getAdvantageName());
				}*/
				Logger.fine("(ResearchPanel.java) Cancel the research p.getOrders().getResearchOrders().size()" + p.getOrders().getResearchOrders().size());
			}else{
				if(p.getResearch().getNumberOfSimultaneouslyResearchAdvantages() == 1){
				//	p.getResearch().removeAllOnGoingResearchedAdvantage();
					if(p.getOrders().getResearchOrders().size() > 0){
						p.getOrders().getResearchOrders().remove(0);
					}
					
					//p.getOrders().removeResearchOrder(new ResearchOrder(name2.getText()));
					//TODO (Tobbe) check if true or false;
					//p.getResearch().setOnGoingResearchedAdvantage(p.getResearch().getAdvantage(name2.getText()));
//					TODO (Tobbe) add Order text in Order Panel
					
					int cost = countCost(p.getResearch().getAdvantage(name2.getText()));
					
					Logger.fine("ResearchPanel cost " + cost);
					p.getOrders().addResearchOrder(new ResearchOrder(name2.getText(), cost),p);
					//ResearchOrder researchOrder, Player p, int sum)
				}
				else{
					int tempNumberOfSimultaneouslyResearchAdvantages = p.getResearch().getNumberOfSimultaneouslyResearchAdvantages();
					int tempNumbersOfResearchOrders =  p.getOrders().getResearchOrders().size();
					
					if(tempNumbersOfResearchOrders < tempNumberOfSimultaneouslyResearchAdvantages){
						int cost = countCost(p.getResearch().getAdvantage(name2.getText()));
						Logger.fine("ResearchPanel cost " + cost);
						p.getOrders().addResearchOrder(new ResearchOrder(name2.getText(),cost),p);
						//p.getOrders().addResearchOrder(new ResearchOrder(name2.getText()));
					}
					else{
						/*
						String ongoingReserachAdvantages = "";
						
						for(int i=0;i < tempNumbersOfResearchOrders;i++){
							ongoingReserachAdvantages+= "\n" + ((ResearchOrder)p.getOrders().getResearchOrders().get(i)).getAdvantageName();
						}
						*/
						String title = "Remove Research";
  					  //	String message = "You can only research on " + tempNumberOfSimultaneouslyResearchAdvantages + " advantages at the same time.\nTo begin research on " + name2.getText() + " you have to cancel the research on one off this advantage:\n\n" + ongoingReserachAdvantages;
  					  	String message = "You can only research on " + tempNumberOfSimultaneouslyResearchAdvantages + " advantages at the same time.";
					  	Logger.fine("openMessagePopup called: " + message);
  					  	GeneralMessagePopupPanel popup = new GeneralMessagePopupPanel(title,this,message);
  					  	popup.setPopupSize(650,110);
  					  	popup.open(this);
					}
				}
				
				
				
			//	p.getOrders().addNewBlackMarketBid(tempSum,currentOffer,null,p);
			   
			   
			}
			
			String tempName = name2.getText();
			client.updateTreasuryLabel();
			Logger.fine("(ResearchPanel.java) p.getOrders().getResearchOrders().size() " + p.getOrders().getResearchOrders().size());
			fillResearchAdvantageList();
			showResearchAdvantage(tempName);
		}
		
		
		for(int i=0;i < parentAndChildButtonMaxIndexNumber; i++){
			
			SRButton tempButton = (SRButton)getComponentAt(parentAndChildButtonXCordinate[i], parentAndChildButtonYCordinate[i]);
			if(tempButton == o){
				showResearchAdvantage(tempButton.getText());
			}
		//	LoggingHandler.fine("removeResearchAdvantage remove Component " + getComponentAt(parentAndChildButtonXCordinate[i], parentAndChildButtonYCordinate[i]).toString());
		//	remove(getComponentAt(parentAndChildButtonXCordinate[i], parentAndChildButtonYCordinate[i]));
		}
		/*
		for(int i =0;i < parentAndChildButtonMaxIndexNumber; i++){
			if(getComponent(i) == o){
				LoggingHandler.fine("getComponent(i) " + getComponent(i).getName() + " " + getComponent(i).toString());
				SRButton tempButton = (SRButton)getComponent(i);
				showResearchAdvantage(tempButton.getText());
			}
		}*/
		/*
		else
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
		}*/
	}
	
	public String getId(){
        return id;
    }
	
	public void updateData(){
    }
	public int countCost(ResearchAdvantage researchAdvantage){
		
		if(researchAdvantage.getCostToResearchOneTurn() > 0 || researchAdvantage.getCostToResearchOneTurnInProcent() > 0){
			 
			 if(researchAdvantage.getCostToResearchOneTurn() > 0){
				 return researchAdvantage.getCostToResearchOneTurn();
			 }else{
				 
				 double treasury = p.getGalaxy().getPlayerIncome(p,false);
				 double procent = researchAdvantage.getCostToResearchOneTurnInProcent();
				 
				 
					 int price = (int) Math.round((treasury * procent) /100);
					 if(price < 1){
						 price = 1;
					 }
				return price;
			}
		}
		return -1;
	}
}
