package sr.client;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sr.client.components.SRBasePanel;
import sr.client.components.SRTabbedPane;
import sr.client.components.SRTabbedPaneUI;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.logging.Logger;
import sr.world.Player;

/**
 * This panel contains the different battle sim panels, and the navigation buttons
 * for the battle sim panels.
 * 
 * @author wmpabod
 *
 */
@SuppressWarnings("serial")
public class BattleSimsPanel extends SRBasePanel implements ChangeListener,SRUpdateablePanel{
	private BattleSimPanel bsp;
	private BattleSimLandPanel bslp;
	private String id;
	
	private SRTabbedPane tabbedPanel;

  public BattleSimsPanel(Player p,GameGUIPanel gameGuiPanel, String id) {
      this.id = id;
      setLayout(null);
      setBackground(StyleGuide.colorBackground);
        
      tabbedPanel = new SRTabbedPane("");
      SRTabbedPaneUI tpui = new SRTabbedPaneUI();
      tabbedPanel.setUI(tpui);
      
      tabbedPanel.setFont(StyleGuide.buttonFont);
      tabbedPanel.setBackground(StyleGuide.colorCurrent.darker().darker().darker().darker());
      tabbedPanel.setForeground(StyleGuide.colorCurrent);
      
      // create all panels
      bsp = new BattleSimPanel(p.getGalaxy().getSpaceshipTypes(),p,"Space Battles");
      
      tabbedPanel.addTab("Space Battles", bsp);
      tabbedPanel.setToolTipTextAt(0, "Do spaceships battle simulations");
      
      if (p.getGalaxy().getGameWorld().isTroopGameWorld()){
    	  bslp = new BattleSimLandPanel(p.getGalaxy().getTroopTypes(),p,"Land Battles");
    	  
    	  tabbedPanel.addTab("Land Battles", bslp);
          tabbedPanel.setToolTipTextAt(1, "Do land battle simulations");
      }
      
      tabbedPanel.setBounds(0,0,860,633);
      tabbedPanel.addChangeListener(this);
      add(tabbedPanel);
     
  }

  public void stopBattleSim(){
	  if (bsp.isVisible()){
		  bsp.stopBattleSim();
	  }else{
		  bslp.stopBattleSim();
	  }
  }

  public void addToBattleSim(String shipsString, String side){
	  bsp.addToBattleSim(shipsString, side);
  }

  public void addToBattleSimLand(String troopString, String side){
	  bslp.addToBattleSim(troopString, side);
  }

  public String getId(){
      return id;
  }

  public void updateData(){
  }

  public void stateChanged(ChangeEvent e) {
  }
  
  public void showSpaceshipSim(boolean showSpaceshipSim){
	  Logger.fine("showSpaceshipSim: " + showSpaceshipSim);
	  if (showSpaceshipSim){
		  tabbedPanel.setSelectedIndex(0);
	  }else{
		  tabbedPanel.setSelectedIndex(1);
	  }
  }
  
}