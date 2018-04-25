package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JScrollPane;

import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.logging.Logger;
import sr.world.Faction;
import sr.world.GameWorld;

/**
 * A panel showing a list with all factions in the gameworld of the current game.
 * If one faction is chosen in the list, all relevant data about it is displayed. 
 * 
 * @author wmpabod
 *
 */
public class LoginFactionPanel extends SRBasePanel implements SRUpdateablePanel, ActionListener{
	private static final long serialVersionUID = 1L;
	private List<Faction> factions;
//    private ListPanel factionlist;
    private SRLabel factionInfoLbl;
    private JScrollPane scrollPane;
    private SRTextArea factionInfoTextArea;
    private SRButton overviewButton;
    // skipped for now startingVIPTypes,spaceshipsTypes,startingShipTypes
    private int column1X = 235;
    private int column1width = 220;
    private String id;
    private FactionOverviewPanel overviewPanel;
    private FactionDetailInfoPanel detailPanel;
    Faction f;
 
    public LoginFactionPanel(GameWorld aGameWorld){
      factions = aGameWorld.getFactions();

      this.setLayout(null);
      setBackground(StyleGuide.colorBackground);
      
      int cHeight = 18;

      // Detail
      detailPanel = new FactionDetailInfoPanel();
      detailPanel.setBounds(column1X,0,580,505);
      detailPanel.setVisible(false);
      add(detailPanel);


      // Overview
      overviewButton = new SRButton("View Overview");
      overviewButton.setBounds(10,385,155,20);
      overviewButton.addActionListener(this);
      overviewButton.setVisible(false);
      add(overviewButton);
      
      overviewPanel = new FactionOverviewPanel();
      overviewPanel.setBounds(column1X,0,380,405);
      overviewPanel.setVisible(false);
      add(overviewPanel);
      
      //Shiptype info textarea
      factionInfoLbl = new SRLabel();
      factionInfoLbl.setBounds(10,205,column1width,cHeight);
      factionInfoLbl.setVisible(false);
      add(factionInfoLbl);

      factionInfoTextArea = new SRTextArea();
      
      factionInfoTextArea.setEditable(false);
 
      scrollPane = new SRScrollPane(factionInfoTextArea);
      scrollPane.setBounds(10,225,155,150);
      scrollPane.setVisible(false);
      add(scrollPane);
      
      factionInfoLbl.setText("Short Description:");
      
    }
    
    public void valueChanged(String factionName){
      
		  if(!overviewButton.isVisible()){
			// TODO (Tobbe) fixa panelens utsende o aktivera detta igen
			 // showFaction(factionName,"");
		  }
		  else{
			  if(overviewPanel.isVisible()){
				//  showFaction(factionName,"overview");
			  }else{
				//  showFaction(factionName,"detail");
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
    
    public void actionPerformed(ActionEvent ae){
      	Logger.fine("actionPerformed: " + ae.getActionCommand() + " " + ae.getSource().getClass().getName());
      
      	// TODO (Tobbe) fixa panelens utsende o aktivera detta igen
    //  	showFaction(f.getName(),"");
      }

    public void showFaction(String name, String isVisble){
    	
    	f = findFaction(name);
    	
        if(f != null){
        	
        	if(!overviewButton.isVisible()){
        		detailPanel.showFaction(f);
        		
        		
            	detailPanel.setVisible(true);
              
            	// short description textarea
            	factionInfoTextArea.setText(f.getShortDescription());
            	factionInfoTextArea.setVisible(true);
            	factionInfoLbl.setVisible(true);
            	scrollPane.setVisible(true);
            	overviewButton.setVisible(true);
        		
        	}
        	else{
        		if(isVisble.equals("overview") || (isVisble.equals("") && !overviewPanel.isVisible())){
		        	//History textarea
	        		detailPanel.setVisible(false);
	        		overviewPanel.showFaction(f);
		            overviewPanel.setVisible(true);
		            overviewButton.setText("View Details");
	        	}
	        	else{
	        		overviewPanel.setVisible(false);
	        		detailPanel.showFaction(f);
	        		//detailPanel.showFaction(f);
	        		detailPanel.setVisible(true);
	        		overviewButton.setText("View Overview");
	        		//short description textarea
	            	factionInfoTextArea.setText(f.getShortDescription());
	            	factionInfoTextArea.setVisible(true);
	            	scrollPane.setVisible(true);
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
