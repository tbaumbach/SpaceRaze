package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Enumeration;
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
import sr.world.VIPType;
import sr.world.comparator.FactionsComparator;

public class BuildingTypePanel extends SRBasePanel implements ListSelectionListener, SRUpdateablePanel, ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private ListPanel rotBuildingList = null,vipsList;
	private List<Faction> factions;
	private ComboBoxPanel filterChoice;
	private SRLabel name, name2, childLabel, parentLabel,descriptionLabel,detailsLabel,vipListLabel,costLabel, canbuildlbl;
	private JScrollPane scrollPaneDetails,scrollPaneDescription;
    private SRTextArea detailsArea,descriptionArea;
    private SRButton viewVIPButton, parentbutton;
    private Player p;
    private int column1X = 200, parentAndChildButtonMaxIndexNumber= 0;
    private SRButton[] buttons;
    private SpaceRazeApplet client;
    
    public BuildingTypePanel(Player p, String id, SpaceRazeApplet client){
		this.client = client;
		this.id = id;
		this.p = p;
		
		this.setLayout(null);
		setBackground(StyleGuide.colorBackground);

		filterChoice = new ComboBoxPanel();
		filterChoice.setBounds(10,10,170,20);
		filterChoice.addActionListener(this);
		filterChoice.setToolTipText("Choice to see yours buildings or a factions buildings.");
	    fillTreeList();
	    this.add(filterChoice);
	    
	
	    //VIP List label
	    vipListLabel = new SRLabel();
	    vipListLabel.setBounds(660,211,170,16);
	    vipListLabel.setText("Add VIPs:");
	    vipListLabel.setToolTipText("Shows VIPs that can be build at this building (if you have right research level.");
	    vipListLabel.setVisible(false);
        add(vipListLabel);
	    
	    // VIP List
        vipsList = new ListPanel();
        vipsList.setBounds(660,230,175,270);
        vipsList.setListSelectionListener(this);
        vipsList.setForeground(StyleGuide.colorCurrent);
        vipsList.setBackground(StyleGuide.colorBackground);
        vipsList.setBorder(new LineBorder(StyleGuide.colorCurrent));
        vipsList.setToolTipText("Mark a VIP in the list and hit 'View VIP Details' button to view VIP details");
        vipsList.setVisible(false);
	    add(vipsList);
	    
	    // View VIP Info
	    viewVIPButton = new SRButton("View VIP Details");
	    viewVIPButton.setBounds(660,505,175,20);
	    viewVIPButton.addActionListener(this);
	    viewVIPButton.setToolTipText("Mark a VIP in the list and hit this button to view VIP details");
	    viewVIPButton.setVisible(false);
	    add(viewVIPButton);

	    // Right Column
	    // ************
	    
	    name = new SRLabel();
	    name.setBounds(column1X,10,50,16);
	    name.setText("Name:");
	    name.setToolTipText("The name on the building");
        add(name);
        
        name2 = new SRLabel();
        name2.setBounds(column1X+50,10,200,16);
        name2.setToolTipText("The name on the building");
        add(name2);
        
        costLabel  = new SRLabel();
        costLabel.setBounds(column1X,30,90,16);
        costLabel.setText("");
        costLabel.setVisible(false);
        costLabel.setToolTipText("Build cost: ");
        add(costLabel);
        
        canbuildlbl  = new SRLabel();
        canbuildlbl.setBounds(column1X,50,210,20);
        canbuildlbl.setText("");
        canbuildlbl.setVisible(false);
        canbuildlbl.setToolTipText("");
        add(canbuildlbl);
        
        childLabel = new SRLabel();
        childLabel.setBounds(column1X,74,300,16);
        childLabel.setText("Possible to upgrade to this:");
        childLabel.setToolTipText("A List on alla possible buildings this building can be upgrade to.");
        add(childLabel);
        
        // Put in the childs as buttons. width= 112 hieght = 20 top= 37. Use index to remove and add.
		
        parentLabel = new SRLabel();
        parentLabel.setBounds(column1X,105,300,16);
        parentLabel.setText("Upgrade from this building");
        parentLabel.setToolTipText("Click on the button to see info about parent building.");
        add(parentLabel);
        
        parentbutton = new SRButton();
        parentbutton.setBounds(column1X, 76, 112, 20);
        parentbutton.addActionListener(this);
        add(parentbutton);
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
	    detailsLabel.setToolTipText("Shows info about the propertys this building have.");
        add(detailsLabel);
		
        detailsArea = new SRTextArea();
        
        detailsArea.setToolTipText("Shows info about the propertys this building have");
        detailsArea.setEditable(true);

        scrollPaneDetails = new SRScrollPane(detailsArea);
        scrollPaneDetails.setBounds(column1X,260,400,175);
        scrollPaneDetails.setVisible(true);
        scrollPaneDetails.setToolTipText("Shows info about the propertys this building have");
	    add(scrollPaneDetails);
	    
        
        fillRootBuildingList();

	}

    private void fillTreeList(){
    	filterChoice.addItem("Yours");
		 
		 factions = Functions.cloneList(p.getGalaxy().getGameWorld().getFactions());
	    	Collections.sort(factions,new FactionsComparator());
	    	for (Faction aFaction : factions) {
				filterChoice.addItem(aFaction.getName());
			}
	    }
	 
	 private void fillRootBuildingList(){
		 removeBuildingType();
		 
		 
		 if(rotBuildingList != null){
			 remove(rotBuildingList);
		 }
		 
		 rotBuildingList = new ListPanel();
        rotBuildingList.setBounds(10,40,170,560);
        rotBuildingList.setListSelectionListener(this);
        rotBuildingList.setForeground(StyleGuide.colorCurrent);
        rotBuildingList.setBackground(StyleGuide.colorBackground);
        rotBuildingList.setBorder(new LineBorder(StyleGuide.colorCurrent));
	    rotBuildingList.setToolTipText("This is all buildings that can be build (no upgrades)");
	    
		 
		 DefaultListModel dlm = (DefaultListModel)rotBuildingList.getModel();
		 dlm.removeAllElements();
		 List<BuildingType> tmpRootBuildings = null;
		 if (filterChoice.getSelectedIndex() > 0){
   		// get faction root ResearchAdvantages
			 Faction showOnlyFaction = factions.get(filterChoice.getSelectedIndex() - 1);
			 tmpRootBuildings = showOnlyFaction.getBuildings().getRootBuildings();
		 }else{
			 tmpRootBuildings = p.getBuildings().getRootBuildings();
		 }
   	
		 for(int i = 0; i < tmpRootBuildings.size(); i++){
			 dlm.addElement(tmpRootBuildings.get(i).getName());
		 }
		 
		 rotBuildingList.updateScrollList();
		 add(rotBuildingList);
	 }
	 
	 public void valueChanged(ListSelectionEvent lse){
		 if (lse.getSource() == rotBuildingList){
			 showBuilding(rotBuildingList.getSelectedItem());
		 }
		 
		 if (lse.getSource() == vipsList){
			 Logger.fine("viewVIPButton.setVisible(true)");
			 viewVIPButton.setVisible(true);
			 repaint();
		 }
	 }
	 
	 public void showBuilding(String BuildingName, String faction){
		 
		 if(faction != null){
	    		filterChoice.setSelectedItem(faction);
	    		fillRootBuildingList();
	    }
		showBuilding(BuildingName);
	 }
	 
	 public void showBuilding(String buildingName){
		 BuildingType buildingType;
		 // remove the old data.
		 removeBuildingType();
		 
		 List<BuildingType> nextBuildingTypes;
		 
		 
		 buildingType = null;  
	    	
		 Logger.fine("showBuilding(String BuildingName) " + buildingName);
		 if(filterChoice.getSelectedIndex() == 0){
			 buildingType = p.findBuildingType(buildingName);
			 nextBuildingTypes = p.getBuildings().getNextBuildingSteps(buildingType);
		 }
		 else{
			 buildingType = p.getGalaxy().getFaction(filterChoice.getSelectedItem()).getBuildings().getBuildingType(buildingName);
			 nextBuildingTypes = p.getGalaxy().getFaction(filterChoice.getSelectedItem()).getBuildings().getNextBuildingSteps(buildingType);
		 }
		 
    	
    	Enumeration<?> elements = rotBuildingList.getModel().elements();
    	int selectIndex = -1; 
    	int index = 0;
    	
    	
    	
    	while(elements.hasMoreElements()){
    		String nextElement = (String)elements.nextElement();
    		
    		if(nextElement.equalsIgnoreCase(buildingName)){
    			selectIndex = index;
    		}
    		index++;
    	}
    	if(index > 0){
    		rotBuildingList.setSelected(selectIndex);
    	}
	    	
	    if(buildingType.getBuildVIPTypes().size() > 0){
			 DefaultListModel dlm = (DefaultListModel)vipsList.getModel();
			 dlm.removeAllElements();
			 List<VIPType> tmpVIPs = null;
			 tmpVIPs = buildingType.getBuildVIPTypes();
			 for (VIPType aVIPType : tmpVIPs) {
				 dlm.addElement(aVIPType.getName());
			 }
			 vipsList.updateScrollList();
			 vipListLabel.setVisible(true);
			 vipsList.setVisible(true);
			 viewVIPButton.setVisible(true);
		 }else{
			 vipListLabel.setVisible(false);
			 vipsList.setVisible(false);
			 viewVIPButton.setVisible(false);
		 }

		 name2.setText(buildingType.getName());
		 
		 int length = nextBuildingTypes.size();
		 buttons = new SRButton[length];
		 
		 int tmpY = 92;
		 int offsetY = 25; // extra pixels for each row of advantages
		 int buttonWidth= 205;
		 
		 // Adding buttons to go against Next BuildingTypes
		 if(nextBuildingTypes.size() > 0){
			 Logger.fine("nextBuildingTypes.size() " + nextBuildingTypes.size());
			 
			 int tempchildButtonXCordinat = column1X;
			 int columnCounter = 1;
			 
			 for(int i=0; i < nextBuildingTypes.size();i++){
				 Logger.fine("buildingType.getNextBuildingTypes().get(i).getName() " + nextBuildingTypes.get(i).getName());
				 
				 if (columnCounter == 4){ // start on a new row
					 columnCounter = 1;
					 tempchildButtonXCordinat = column1X;
					 tmpY += offsetY;
				 }
				 
				 SRButton tempbutton = new SRButton(nextBuildingTypes.get(i).getName());
				 tempbutton.setBounds(tempchildButtonXCordinat, tmpY, buttonWidth, 20);
				 tempbutton.addActionListener(this);
				 tempbutton.setToolTipText("Hit this button to see info about the building: " + nextBuildingTypes.get(i).getName());
				 parentAndChildButtonMaxIndexNumber++;
				 buttons[i] = tempbutton;
				 add(tempbutton);
				 tempchildButtonXCordinat+= buttonWidth +5;
				 
				 columnCounter++;
			 }
		 }
		 
		 tmpY += 23;

		 parentLabel.setLocation(column1X,tmpY);

		 tmpY += 18;
		 
		 if(buildingType.getParentBuilding() != null){
			 parentbutton.setToolTipText("Hit this button to see the parent building: " + buildingType.getParentBuilding().getName());
			 parentbutton.setText(buildingType.getParentBuilding().getName());
			 parentbutton.setBounds(column1X, tmpY, buttonWidth, 20);
			 parentbutton.setVisible(true);
			 
		 }
		 
		 
		 tmpY += 23;
		 descriptionLabel.setLocation(column1X,tmpY);
		 
		 tmpY += 19;
		 scrollPaneDescription.setLocation(column1X,tmpY);
		 descriptionArea.setText(buildingType.getDescription());
		 
		 tmpY += 5 + scrollPaneDescription.getHeight();
		 detailsLabel.setLocation(column1X,tmpY);
		 
		 tmpY += 19;
		 scrollPaneDetails.setLocation(column1X,tmpY);
		 
		 List<String> allStrings = buildingType.getAbilitiesStrings();
	        for (int i = 0; i < allStrings.size(); i++){
	        	detailsArea.append(allStrings.get(i) + "\n");
	        }
		 
		if (filterChoice.getSelectedIndex() == 0){
      	  canbuildlbl.setText("Can build: " + Functions.getYesNo(buildingType.isDeveloped()));
        }else{
      	  canbuildlbl.setText("Can build from start: " + Functions.getYesNo(buildingType.isDeveloped()));
        }
        canbuildlbl.setVisible(true);
	        
        costLabel.setText("Build Cost: " + buildingType.getBuildCost(null));
		costLabel.setVisible(true);
		 
		repaint();
		 
	 }
	 
	 public void removeBuildingType(){
		 
		vipListLabel.setVisible(false);
		vipsList.setVisible(false);
		name2.setText("");
		
		if(buttons != null){
			
			for(int i =buttons.length-1;i >= 0; i--){
				 
				//	LoggingHandler.fine("removeResearchAdvantage remove Button " + getComponentAt(parentAndChildButtonXCordinate[i], parentAndChildButtonYCordinate[i]).toString());
				//	remove(getComponentAt(parentAndChildButtonXCordinate[i], parentAndChildButtonYCordinate[i]));
				Logger.fine("removeButtons remove index " + i);
					Logger.fine("removeButtons remove Button " + buttons[i].toString());
					remove(buttons[i]);
					
					
				}
			buttons = null;
		}
		
		parentbutton.setVisible(false);
		parentAndChildButtonMaxIndexNumber = 0;
		descriptionArea.setText("");
		detailsArea.setText("");
		viewVIPButton.setVisible(false);
		costLabel.setVisible(false);
		canbuildlbl.setVisible(false);
		repaint();
		 
	 }
	
	public void actionPerformed(ActionEvent arg0) {
		Object o = arg0.getSource();
		
		if (o == filterChoice){
			fillRootBuildingList();
		}else
		if (o == viewVIPButton && vipsList.getSelectedItem() != null){
			if(filterChoice.getSelectedItem().equalsIgnoreCase("Yours")){
				client.showVIPTypeDetails(vipsList.getSelectedItem(), "All");
			}else{
				client.showVIPTypeDetails(vipsList.getSelectedItem(), filterChoice.getSelectedItem());
			}
			
	  	}else if(o == parentbutton){
	  		showBuilding(parentbutton.getText());
	  	}
		
		if(buttons != null){
			for(int i=0;i < buttons.length; i++){
				if(buttons[i] == o){
					showBuilding(buttons[i].getText());
				}
			}
		}
		
	}
	
	public String getId(){
       return id;
   }
	
	public void updateData(){
   }
	
}
