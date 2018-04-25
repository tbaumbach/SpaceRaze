//Title:        SpaceRaze Client
//Author:       Paul Bodin
//Description:  Java-klienten för SpazeRaze. Är en Javaapplet.

package sr.client.components.diplomacy;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.client.interfaces.SRUpdateablePanel;
import sr.enums.DiplomacyLevel;
import sr.general.logging.Logger;
import sr.world.Player;

@SuppressWarnings("serial")
public class DiplomacyPanel extends SRBasePanel implements SRUpdateablePanel, ItemListener{
    private String id;
    private Player p;
	private SRLabel titleLabel;
    private DiplomacyRowsPanel rowsPanel;
    private SRScrollPane scrollPane;
	private ComboBoxPanel playerChoice;
    private SRTextArea infoTA;
    // diplomacy level info
    private SRLabel levelTitleLbl,levelDurabilityLbl,gameDiplomacyLbl;
    private SRTextArea levelInfoTA;

    public DiplomacyPanel(Player p ,String id){
      this.id = id;
      this.p = p;
      this.setLayout(null);

      titleLabel = new SRLabel("Choose player");
      titleLabel.setBounds(10,10,100,20);
      add(titleLabel);

      playerChoice = new ComboBoxPanel();
      playerChoice.setBounds(130,10,220,20);
      for (Player aPlayer : p.getGalaxy().players) {
    	  if (!aPlayer.isDefeated()){
    		  playerChoice.addItem(aPlayer.getGovenorName() + " (" + aPlayer.getFaction().getName() + ")");
    	  }
      }
      playerChoice.setSelectedItem(p.getGovenorName() + " (" + p.getFaction().getName() + ")");
      playerChoice.addItemListener(this);
      add(playerChoice);
      
      gameDiplomacyLbl = new SRLabel();
      gameDiplomacyLbl.setText("Game Diplomacy:     " + p.getGalaxy().getDiplomacyGameType().getLongText());
      gameDiplomacyLbl.setBounds(520,10,250,20);
      add(gameDiplomacyLbl);

      showDiplomacy(p);
      
      // diplomacy level info, initialt är de dolda
      levelTitleLbl = new SRLabel();
      levelTitleLbl.setBounds(520, 35, 150, 20);
      levelTitleLbl.setVisible(false);
      add(levelTitleLbl);
      
      levelDurabilityLbl= new SRLabel();
      levelDurabilityLbl.setBounds(520, 60, 150, 20);
      levelDurabilityLbl.setVisible(false);
      add(levelDurabilityLbl);
      
      levelInfoTA = new SRTextArea();
      levelInfoTA.setBounds(520, 85, 330, 200);
      levelInfoTA.setVisible(false);
      add(levelInfoTA);

      String infoText = "";
      infoText = "Click on diplomacy level icons to issue or cancel diplomacy orders.\n";
      infoText += "\n";
      infoText += "All changes of diplomacy level is performed at the end of the turn.\n";
      infoText += "Diplomacy level can never change more than one step each turn.\n";
      infoText += "Moves to the left is always unilateral.\n";
      infoText += "Moves to the right must always be bilateral.";
      infoTA = new SRTextArea(infoText);
      infoTA.setBounds(520, 475, 330, 190);
      add(infoTA);
      
    }
    
    /**
     * Show currently selected player
     *
     */
    public void showDiplomacy(){
		String playerName = playerChoice.getSelectedItem().substring(0, playerChoice.getSelectedItem().indexOf(" ("));
		Player tmpPlayer = p.getGalaxy().getPlayerByGovenorName(playerName);
		showDiplomacy(tmpPlayer);
    }

    private void showDiplomacy(Player aPlayer){
    	boolean activePlayer = (aPlayer == p);
        rowsPanel = new DiplomacyRowsPanel(aPlayer,this,activePlayer);
        rowsPanel.setBorder(null);

        if (scrollPane != null){
        	remove(scrollPane);
        }
        
        scrollPane = new SRScrollPane(rowsPanel);
        scrollPane.setBounds(10,35,500,590);
        scrollPane.setAutoscrolls(true);
        add(scrollPane);

    }
    
    public void showLevelInfo(DiplomacyLevel aLevel){
    	levelTitleLbl.setText(aLevel.getName());
    	levelTitleLbl.setVisible(true);
    	levelDurabilityLbl.setText("Durability: " + aLevel.getDurability());
    	levelDurabilityLbl.setVisible(true);
    	levelInfoTA.setText(aLevel.getDesc());
    	levelInfoTA.setVisible(true);
    }

    public void hideLevelInfo(){
    	levelTitleLbl.setText("");
    	levelTitleLbl.setVisible(false);
    	levelDurabilityLbl.setText("");
    	levelDurabilityLbl.setVisible(false);
    	levelInfoTA.setText("");
    	levelInfoTA.setVisible(false);
    }

    public void itemStateChanged(ItemEvent ie){
    	if (ie.getStateChange() == ItemEvent.SELECTED){
    		if ((ComboBoxPanel)ie.getItemSelectable() == playerChoice){
    			Logger.info("Show player: " + playerChoice.getSelectedItem());
    			String playerName = playerChoice.getSelectedItem().substring(0, playerChoice.getSelectedItem().indexOf(" ("));
    			Player tmpPlayer = p.getGalaxy().getPlayerByGovenorName(playerName);
    			showDiplomacy(tmpPlayer);
    		}
    	}
    }

    public String getId(){
      return id;
    }

    public void updateData(){
    }

	public Player getPlayer() {
		return p;
	}
}
