package sr.client;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sr.client.components.SRBasePanel;
import sr.client.components.SRTabbedPane;
import sr.client.components.SRTabbedPaneUI;
import sr.client.interfaces.SRUpdateablePanel;
import sr.world.Player;

/**
 * This panel contains the different data panels, and the navigation buttons
 * for the databank panels.
 * 
 * @author wmpabod
 *
 */
public class DataBankPanel extends SRBasePanel implements SRUpdateablePanel, ChangeListener{
	private static final long serialVersionUID = 1L;
	private GamePanel gp;
	private ShiptypePanel sstp;
	private TroopTypePanel ttp;
	private VipTypePanel vtp;
	private FactionPanel fp;
	private GameWorldPanel gwp;
	private BuildingTypePanel btp;
	private AlignmentsPanel ap;
	private String id;
	private SRTabbedPane tabbedPanel;
	private Player p;

  public DataBankPanel(Player p,GameGUIPanel gameGuiPanel, String id, SpaceRazeApplet client) {
      this.id = id;
      setLayout(null);
      setBackground(StyleGuide.colorBackground);

      this.p = p;

      // create all panels
      gp = new GamePanel(p, "Game");
 
      gwp = new GameWorldPanel(p.getGalaxy().getGameWorld(), "GameWorld");
     
      fp = new FactionPanel(p, "Factions", client);
    
      sstp = new ShiptypePanel(p.getGalaxy().getSpaceshipTypes(), p,"ships",p.getGalaxy().getGameWorld().hasSquadrons());

      ttp = new TroopTypePanel(p.getGalaxy().getTroopTypes(), p,"troops");

      vtp = new VipTypePanel(p,"VIPs");
    
      btp = new BuildingTypePanel(p,"Buildings", client);

      ap = new AlignmentsPanel(p,"Alignments");
     
      
      tabbedPanel = new SRTabbedPane("");
      SRTabbedPaneUI tpui = new SRTabbedPaneUI();
      tabbedPanel.setUI(tpui);
      tabbedPanel.setFont(StyleGuide.buttonFont);
      
      tabbedPanel.setBackground(StyleGuide.colorCurrent.darker().darker().darker().darker());
      tabbedPanel.setForeground(StyleGuide.colorCurrent);
      
      // create all panels
      
      tabbedPanel.addTab("Game", gp);
      tabbedPanel.setToolTipTextAt(0, "Game details");
      //tabbedPanel.setForegroundAt(0, StyleGuide.colorCurrent);
      
      tabbedPanel.addTab("GameWorld", gwp);
      tabbedPanel.setToolTipTextAt(1, "Game details");
      
      tabbedPanel.addTab("Factions", fp);
      tabbedPanel.setToolTipTextAt(2, "Factions details");
      
      tabbedPanel.addTab("Ships", sstp);
      tabbedPanel.setToolTipTextAt(3, "Ship types details");
      
      int tabbelIndex = 4;
      if (p.getGalaxy().getGameWorld().isTroopGameWorld()){
    	  tabbedPanel.addTab("Troops", ttp);
          tabbedPanel.setToolTipTextAt(4, "Troop types details");
          tabbelIndex++;
      }
      
      tabbedPanel.addTab("VIPs", vtp);
      tabbedPanel.setToolTipTextAt(tabbelIndex, "Do VIP types details");
      tabbelIndex++;
      
      tabbedPanel.addTab("Buildings", btp);
      tabbedPanel.setToolTipTextAt(tabbelIndex, "Bulding types details");
      tabbelIndex++;
      
      tabbedPanel.addTab("Alignments", ap);
      tabbedPanel.setToolTipTextAt(tabbelIndex, "Alignments details");
      tabbelIndex++;
      
      tabbedPanel.setBounds(0,0,860,633);
      tabbedPanel.addChangeListener(this);
      add(tabbedPanel);
      
  }

  public void showShiptypeDetails(String aShipType, String faction){
	  tabbedPanel.setSelectedIndex(3);
	  sstp.showSpaceshipType(aShipType, faction);
  }

  public void showTroopTypeDetails(String aTroopType, String faction){
	  tabbedPanel.setSelectedIndex(4);
	  ttp.showTroopType(aTroopType, faction);
  }

  public void showVIPTypeDetails(String aVIPType, String faction){
	  if(p.getGalaxy().getGameWorld().isTroopGameWorld()){
		  tabbedPanel.setSelectedIndex(5);
	  }else{
		  tabbedPanel.setSelectedIndex(4);
	  }
	  vtp.showVIPType(aVIPType, faction);
	}
  
  public void showBuildingTypeDetails(String aBuildingType, String faction){
	  if(p.getGalaxy().getGameWorld().isTroopGameWorld()){
		  tabbedPanel.setSelectedIndex(6);
	  }else{
		  tabbedPanel.setSelectedIndex(5);
	  }
	  btp.showBuilding(aBuildingType, faction);
	}
  
  public void stateChanged(ChangeEvent e) {
  }
  

  
  public String getId(){
      return id;
  }

  public void updateData(){
  }
}