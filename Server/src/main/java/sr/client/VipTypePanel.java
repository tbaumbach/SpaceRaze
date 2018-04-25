package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.client.components.scrollable.ListPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.Functions;
import sr.world.Alignment;
import sr.world.Faction;
import sr.world.Player;
import sr.world.VIPType;
import sr.world.comparator.FactionsComparator;
import sr.world.comparator.VIPTypeComparator;

@SuppressWarnings("serial")
public class VipTypePanel extends SRBasePanel implements ListSelectionListener, SRUpdateablePanel, ActionListener{
    private List<VIPType> viptypes;
    private List<Faction> factions;
    private ListPanel viptypelist;
    private SRLabel nameLbl, shortNameLbl, alignmentLbl, frequencyLbl;
    private SRLabel nameLbl2, shortNameLbl2, alignmentLbl2, frequencyLbl2;
    private SRLabel supplyCostLabel,buildCostLabel;
    private SRLabel supplyCostLabel2,buildCostLabel2;
//    private SRLabel canbuildlbl2,canbuildlbl;
    
    private ComboBoxPanel filterChoice;
    
    // right column
    private SRLabel abilitiesLbl;
    private SRTextArea abilitiesTextArea;
    private SRScrollPane scrollPane;
    private SRLabel descriptionLbl;
    private SRTextArea descriptionTextArea;
    private SRScrollPane scrollPane2;
    private String id;
    private Player p;
    List<Alignment> alignments;
    // used for computing components location
    private int column1X = 205;
    private int column2X = 310;
//    private int column3X = 370;
    private int column4X = 400;
    private int column5X = 503;
    private int yPosition = 10;
    private final int yInterval = 20;
    
    public VipTypePanel(Player p, String id){
      viptypes = Functions.cloneList(p.getGalaxy().getGameWorld().getVipTypes());
	  Collections.sort(viptypes,new VIPTypeComparator());
      this.id = id;
      this.p = p;

      this.setLayout(null);
      setBackground(StyleGuide.colorBackground);
      
      int cHeight = 18;
      
      filterChoice = new ComboBoxPanel();
      filterChoice.setBounds(10,10,170,20);
      filterChoice.addActionListener(this);
      fillFilterList();
      this.add(filterChoice);

      fillVIPTypeList();

      nameLbl = new SRLabel();
      nameLbl.setBounds(column1X,yPosition,100,cHeight);
      add(nameLbl);
      nameLbl2 = new SRLabel();
      nameLbl2.setBounds(column2X,yPosition,190,cHeight);
      add(nameLbl2);

      shortNameLbl = new SRLabel();
      shortNameLbl.setBounds(column1X,newLine(),100,cHeight);
      add(shortNameLbl);
      shortNameLbl2 = new SRLabel();
      shortNameLbl2.setBounds(column2X,yPosition,100,cHeight);
      add(shortNameLbl2);

      alignmentLbl = new SRLabel();
      alignmentLbl.setBounds(column1X,newLine(),100,cHeight);
      add(alignmentLbl);
      alignmentLbl2 = new SRLabel();
      alignmentLbl2.setBounds(column2X,yPosition,100,cHeight);
      add(alignmentLbl2);

      frequencyLbl = new SRLabel();
      frequencyLbl.setBounds(column1X,newLine(),150,cHeight);
      add(frequencyLbl);
      frequencyLbl2 = new SRLabel();
      frequencyLbl2.setBounds(column2X,yPosition,150,cHeight);
      add(frequencyLbl2);
      
      // viptype abilities textarea
      abilitiesLbl = new SRLabel();
      abilitiesLbl.setBounds(column1X,newLine(),120,cHeight);
      add(abilitiesLbl);

      abilitiesTextArea = new SRTextArea();
      
      abilitiesTextArea.setEditable(false);
 
      scrollPane = new SRScrollPane(abilitiesTextArea);
      scrollPane.setBounds(column1X,newLine(),350,178);
      scrollPane.setVisible(false);
      add(scrollPane);

      yPosition = yPosition + 168;
      
      // viptype abilities textarea
      descriptionLbl = new SRLabel();
      descriptionLbl.setBounds(column1X,newLine(),120,cHeight);
      add(descriptionLbl);

      descriptionTextArea = new SRTextArea();
      
      descriptionTextArea.setEditable(false);
 
      scrollPane2 = new SRScrollPane(descriptionTextArea);
      scrollPane2.setBounds(column1X,newLine(),350,70);
      scrollPane2.setVisible(false);
      add(scrollPane2);
      
      //    right column
      yPosition = 10;
      //newLine();
      /*
      canbuildlbl = new SRLabel();
      canbuildlbl.setBounds(column4X,newLine(),100,cHeight);
      add(canbuildlbl);
      canbuildlbl2 = new SRLabel();
      canbuildlbl2.setBounds(column5X,yPosition,100,cHeight);
      add(canbuildlbl2);
      */
      buildCostLabel = new SRLabel();
      buildCostLabel.setBounds(column4X,newLine(),100,cHeight);
      add(buildCostLabel);
      buildCostLabel2 = new SRLabel();
      buildCostLabel2.setBounds(column5X,yPosition,100,cHeight);
      add(buildCostLabel2);

      supplyCostLabel = new SRLabel();
      supplyCostLabel.setBounds(column4X,newLine(),100,cHeight);
      add(supplyCostLabel);
      supplyCostLabel2 = new SRLabel();
      supplyCostLabel2.setBounds(column5X,yPosition,100,cHeight);
      add(supplyCostLabel2);
      
      

    }
    
    private void fillFilterList(){
    	filterChoice.addItem("All");
//    	filterChoice.addItem("Yours");
    	alignments = p.getGalaxy().getGameWorld().getAlignments().getAllAlignments();
    	for (Alignment alignment : alignments) {
    		filterChoice.addItem("Alignment: " + alignment.getName());
		}
    	factions = Functions.cloneList(p.getGalaxy().getGameWorld().getFactions());
    	Collections.sort(factions,new FactionsComparator());
    	for (Faction aFaction : factions) {
			filterChoice.addItem("Faction: " + aFaction.getName());
		}
    }
    
    private List<Alignment> getCanHaveAlignments(Alignment anAlignment){
    	List<Alignment> tmpAlignments = new LinkedList<Alignment>();
    	for (Alignment alignment : alignments) {
			if (anAlignment.canHaveVip(alignment.getName())){
				tmpAlignments.add(alignment);
			}
		}
    	return tmpAlignments;
    }
    
    private void fillVIPTypeList(){
    	
    	if(viptypelist != null){
    		remove(viptypelist);
    	}
    	viptypelist = new ListPanel();
        viptypelist.setBounds(10,40,170,560);
        viptypelist.setListSelectionListener(this);
        viptypelist.setForeground(StyleGuide.colorCurrent);
        viptypelist.setBackground(StyleGuide.colorBackground);
        viptypelist.setBorder(new LineBorder(StyleGuide.colorCurrent));
           	
    	
        DefaultListModel dlm = (DefaultListModel)viptypelist.getModel();
        dlm.removeAllElements();
    	List<VIPType> tempVIPTypeList = null;
//    	List<String> tempVIPTypeListName = new ArrayList<String>();
    	int selIndex = filterChoice.getSelectedIndex();
    	if (selIndex > (alignments.size())){// faction
    		// get faction to show ships from
    		tempVIPTypeList = new LinkedList<VIPType>();
    		Faction showOnlyFaction = factions.get(filterChoice.getSelectedIndex() - (alignments.size() + 1));
    		System.out.println("showOnlyFaction: " + showOnlyFaction.getName());
    		List<Alignment> canHaveAlignments = getCanHaveAlignments(showOnlyFaction.getAlignment());
    		System.out.println("canHaveAlignments: " + canHaveAlignments.size());
    		for (Alignment showOnlyAlignment : canHaveAlignments) {
    			List<VIPType> alignmentVIPTypeList = p.getGalaxy().getVIPType(showOnlyAlignment);
        		System.out.println("alignmentVIPTypeList: " + alignmentVIPTypeList.size());
    			for (VIPType aVipType : alignmentVIPTypeList) {
    				System.out.println("aVipType: " + aVipType.getName());
    				tempVIPTypeList.add(aVipType);
				}
			}
//    		tempVIPTypeList = showOnlyFaction.getVIPTypes();
    	}else 
    	if(selIndex > 0){//alignment
    		Alignment showOnlyAlignment = alignments.get(filterChoice.getSelectedIndex() - 1);
    		tempVIPTypeList = p.getGalaxy().getVIPType(showOnlyAlignment);
    	}
    	else{
    		tempVIPTypeList = viptypes;// all
    	}
    	// add to model
		for(int i=0;i < tempVIPTypeList.size();i++){
			dlm.addElement(tempVIPTypeList.get(i).getName());
		}
		
		viptypelist.updateScrollList();
        add(viptypelist);
    }
    
    private VIPType findVIPType(String findname){
        VIPType vt = null;
        vt = p.getGalaxy().findVIPType(findname);
        return vt;
      }
    
    private int newLine(){
    	yPosition = yPosition + yInterval;
    	return yPosition;
    }

    public void valueChanged(ListSelectionEvent lse){
      if (lse.getSource() instanceof ListPanel){
        showVIPType(viptypelist.getSelectedItem(), null);
      }
    }

    public void showVIPType(String name, String faction){
    	if(faction != null){
    		filterChoice.setSelectedItem(faction);
    		fillVIPTypeList();
    		
    	}

    	VIPType vt = null;  
    	vt = findVIPType(name);
    	
    	Enumeration<?> elements = viptypelist.getModel().elements();
    	int selectIndex = -1; 
    	int index = 0;
    	
    	while(elements.hasMoreElements()){
    		String nextElement = (String)elements.nextElement();
    		
    		if(nextElement.equalsIgnoreCase(name)){
    			selectIndex = index;
    		}
    		index++;
    	}
    	if(index > 0){
    		viptypelist.setSelected(selectIndex);
    	}
    	
    	
    	
      
      nameLbl.setText("Name: ");
      shortNameLbl.setText("Short name: ");
      alignmentLbl.setText("Alignment: ");
      frequencyLbl.setText("Frequency: ");
      abilitiesLbl.setText("Abilities: ");
      descriptionLbl.setText("Description: ");
      supplyCostLabel.setText("Supply cost: ");
      buildCostLabel.setText("Build cost: ");
     
      if (vt != null){
          nameLbl2.setText(vt.getName());
          shortNameLbl2.setText(vt.getShortName());
          alignmentLbl2.setText(vt.getAlignmentString());
          frequencyLbl2.setText(vt.getFrequencyString());
          /*
          if (filterChoice.getSelectedIndex() >= 1){
        	  canbuildlbl2.setText(Functions.getYesNo(vt.isAvailableToBuild()));
          }else{
        	  canbuildlbl2.setText("");
          }
          */
          supplyCostLabel2.setText(String.valueOf(vt.getUpkeep()));
          buildCostLabel2.setText(String.valueOf(vt.getBuildCost()));
          
          // abilities textarea
          abilitiesTextArea.setText("");
          List<String> ablities = vt.getAbilitiesStrings();
          for (String anAbility : ablities) {
        	  abilitiesTextArea.append(anAbility + "\n");
          }
          abilitiesTextArea.setVisible(true);
          scrollPane.setVisible(true);
          // description textarea
          descriptionTextArea.setText(vt.getDescription());
          descriptionTextArea.setVisible(true);
          scrollPane2.setVisible(true);
      }
    }

    public String getId(){
        return id;
    }

    public void updateData(){
    }
    
    public void actionPerformed(ActionEvent arg0) {
		fillVIPTypeList();
	}
}
