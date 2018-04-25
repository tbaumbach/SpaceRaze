//Title:        SpaceRaze Client
//Author:       Paul Bodin
//Description:  Denna klass innehåller knappar etc för att styra utseendet på kartan.

package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.border.LineBorder;

import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.world.Player;

public class ResearchLeftPanel extends SRBasePanel implements ItemListener, ActionListener {
  private static final long serialVersionUID = 1L;
  int speed = 2,initialZoom = -6,sectorZoom = 4;
  SRLabel onGoingPanel;
  ComboBoxPanel centerchoice;
  SRButton researchbtn;
  Player p;
  GameGUIPanel aGameGUIPanel;
  boolean once = true;
  boolean running = false, doChange = false;
  ResearchPanel aResearchPanel;

  public ResearchLeftPanel(Player p, GameGUIPanel aGameGUIPanel, SpaceRazeApplet client) {
    this.p = p;
    this.aGameGUIPanel = aGameGUIPanel;
    
    
    
    setLayout(null);
    setBorder(new LineBorder(StyleGuide.colorCurrent));
    setBackground(StyleGuide.colorBackground);
    
    researchbtn = new SRButton("Research");
    researchbtn.setBounds(5,10,110,20);
    researchbtn.addActionListener(this);
    this.add(researchbtn);

    
    onGoingPanel = new SRLabel("Present research");
    onGoingPanel.setBounds(5,40,110,20);
    add(onGoingPanel);
  }

  public void itemStateChanged(ItemEvent ie){
    if (ie.getStateChange() == ItemEvent.SELECTED){
//      String cname = (String)centerchoice.getSelectedItem();
      
    }
  }


	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equalsIgnoreCase("Research")){
			aResearchPanel.updateData();
		//	aGameGUIPanel.setCurrentPanel(aResearchPanel);
		}
	}
}