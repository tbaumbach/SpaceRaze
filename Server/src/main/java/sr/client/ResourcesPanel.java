package sr.client;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sr.client.components.SRBasePanel;
import sr.client.components.SRTabbedPane;
import sr.client.components.SRTabbedPaneUI;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.logging.Logger;
import sr.world.Player;

/**
 * This panel contains the different data panels, and the navigation buttons
 * for the databank panels.
 * 
 * @author wmpabod
 *
 */
@SuppressWarnings("serial")
public class ResourcesPanel extends SRBasePanel implements SRUpdateablePanel, ChangeListener{
	private List<SRUpdateablePanel> panels = new ArrayList<SRUpdateablePanel>();
	private ShipsPanel sp;
	private VIPsPanel vp;
	private PlanetsPanel rpp;
	private RetreatsPanel retp;
	private String id;
	private SRTabbedPane tabbedPanel;

  public ResourcesPanel(Player p,GameGUIPanel gameGuiPanel, String id) {
      this.id = id;
      setLayout(null);
      setBackground(StyleGuide.colorBackground);
           
      // create all panels
      rpp = new PlanetsPanel(p.getGalaxy(), "Planets", p, gameGuiPanel);
      rpp.setName("Databank");
      add(rpp);
      panels.add(rpp);
      
      vp = new VIPsPanel(p.getGalaxy(), "VIPs", p, gameGuiPanel);
      vp.setName("Databank");
      add(vp);
      panels.add(vp);
      
      sp = new ShipsPanel(p.getGalaxy(), "Ships", p, gameGuiPanel);
      sp.setName("Databank");
      add(sp);
      panels.add(sp);
      
      retp = new RetreatsPanel("Retreats", p);
      retp.setName("Databank");
      add(retp);
      panels.add(retp);
      
      tabbedPanel = new SRTabbedPane("");
      SRTabbedPaneUI tpui = new SRTabbedPaneUI();
      tabbedPanel.setUI(tpui);
      tabbedPanel.setFont(StyleGuide.buttonFont);
      
      tabbedPanel.setBackground(StyleGuide.colorCurrent.darker().darker().darker().darker());
      tabbedPanel.setForeground(StyleGuide.colorCurrent);
      
      // create all panels      
      tabbedPanel.addTab("Planets", rpp);
      tabbedPanel.setToolTipTextAt(0, "Information about yours planets");
      
      tabbedPanel.addTab("VIPs", vp);
      tabbedPanel.setToolTipTextAt(1, "Information about yours VIPs");
      
      tabbedPanel.addTab("Ships", sp);
      tabbedPanel.setToolTipTextAt(2, "Information about yours ships");
      
      tabbedPanel.addTab("Retreats", retp);
      tabbedPanel.setToolTipTextAt(3, "Information about yours retreating ships");
      
      tabbedPanel.setBounds(0,0,860,633);
      add(tabbedPanel);
      
      showPanel("Ships");
  }

  public void actionPerformed(ActionEvent ae){
	  Logger.finer("ae.getActionCommand(): " + ae.getActionCommand());
      showPanel(ae.getActionCommand());
  }

  public void showPanel(String panelid){
	  
	  for(int i= 0; i < tabbedPanel.getTabCount(); i++){
		  if(tabbedPanel.getTitleAt(i).equals(panelid)){
			  tabbedPanel.setSelectedIndex(i);
		  }
	  }
	  
	  for (int i = 0; i < panels.size(); i++){
		  SRUpdateablePanel p = panels.get(i);
		  if (p.getId().equalsIgnoreCase(panelid)){
			  Logger.finest("p.getId(): " + p.getId());
			  p.updateData();
		  }
	  }
  }
 
  public void stateChanged(ChangeEvent e) {
	  
	  if(tabbedPanel.getSelectedComponent() != null){
		  showPanel(tabbedPanel.getSelectedComponent().getName());
	  }
  }
  

  public String getId(){
      return id;
  }

  public void updateData(){
	  for (int i = 0; i < panels.size(); i++){
		  SRUpdateablePanel p = panels.get(i);
		  p.updateData();
	  }
  }
  
}