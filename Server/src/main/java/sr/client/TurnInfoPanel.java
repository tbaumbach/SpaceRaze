package sr.client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import sr.client.components.CheckBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.world.Player;

/**
 * <p>Description: Panel in the lower edge of the game gui, showing some information and the Send/finished button and the Abandon Game checkbox</p>
 * <p>Created: 2003</p>
 * @author Paul Bodin
 */
@SuppressWarnings("serial")
public class TurnInfoPanel extends SRBasePanel implements ActionListener{
	private SRLabel treasuryLabel;
	private SRLabel infoLabel;
	private SRButton sendbtn;
	private SpaceRazeApplet client;
	private Player p;
	private CheckBoxPanel abandonGameCheckBox;
	private CheckBoxPanel turnFinishedCheckBox;
	private NewMessageAnimatedImagePanel newMessageAnimatedImagePanel;
	ResearchPanel aResearchPanel;
	//private SRButton showmapbtn;
	//private GameGUIPanel aGameGUIPanel;

  public TurnInfoPanel(SpaceRazeApplet client,Player p, GameGUIPanel aGameGUIPanel, ImageHandler imageHandler) {
    this.p = p;
    this.client = client;
 //   this.aGameGUIPanel = aGameGUIPanel;
    this.setLayout(null);
    setBackground(StyleGuide.colorBackground);
    setBorder(new LineBorder(StyleGuide.colorCurrent));
    
    SRBasePanel tempPanel = new SRBasePanel();
    FlowLayout aFlowLayout = new FlowLayout();
    aFlowLayout.setAlignment(FlowLayout.LEFT);
    aFlowLayout.setVgap(0);
    tempPanel.setLayout(aFlowLayout);
//    tempPanel.setBounds(5, 5, 905, 18);
    tempPanel.setBounds(5, 5, 770, 18);
    add(tempPanel);
    
    infoLabel = new SRLabel(creatGameInfo());
    infoLabel.setSize(820,18);
    tempPanel.add(infoLabel);
    
    treasuryLabel = new SRLabel("Left to spend: ");
    treasuryLabel.setSize(120,18);
    treasuryLabel.setHorizontalAlignment(JLabel.CENTER);
    treasuryLabel.setForeground(StyleGuide.colorNeutral);
    //treasuryLabel.setBorder(new LineBorder(StyleGuide.colorCurrent.darker().darker()));
    tempPanel.add(treasuryLabel);

    abandonGameCheckBox = new CheckBoxPanel("Abandon Game");
//    abandonGameCheckBox.setSize(110,15);
    abandonGameCheckBox.setBounds(780, 5, 110,15);
    abandonGameCheckBox.setSelected(p.getOrders().isAbandonGame());
    abandonGameCheckBox.addActionListener(this);
    if (p.isDefeated() | p.getGalaxy().gameEnded){
    	abandonGameCheckBox.setEnabled(false);
    }
    add(abandonGameCheckBox);
    
    newMessageAnimatedImagePanel = new NewMessageAnimatedImagePanel(imageHandler, client);
    newMessageAnimatedImagePanel.setBounds(920, 6, 30,16);
    add(newMessageAnimatedImagePanel);
    
    turnFinishedCheckBox = new CheckBoxPanel("Turn finished");
    turnFinishedCheckBox.setBounds(960, 5, 90,19);
    turnFinishedCheckBox.setSelected(true);
    turnFinishedCheckBox.addActionListener(this);
    add(turnFinishedCheckBox);
    
    sendbtn = new SRButton("Save & Close",2);
    sendbtn.setBounds(1067,4,120,18);
    sendbtn.setGreen();
    sendbtn.addActionListener(this);
    sendbtn.setEnabled(!p.isDefeated());
    this.add(sendbtn);
 
    /*
    showmapbtn = new SRButton("Show Map");
    showmapbtn.setBounds(755,6,80,35);
    showmapbtn.setVisible(true);
    showmapbtn.addActionListener(this);
    add(showmapbtn);*/

    // set initial left to spend
    updateTreasuryLabel();
  }
    
  private String creatGameInfo(){
	  String gameInfo = "";
	  
	  gameInfo = "Turn: " + p.getGalaxy().turn;
//	  gameInfo += "  |  " + "Map: " + p.getGalaxy().getMapNameFull();
	  gameInfo += "  |  " + "Governor: " + p.getGovenorName();
	  gameInfo += "  |  " + "Faction: " + p.getFaction().getName();
//	  gameInfo += "  |  " + "Game world: " + p.getGalaxy().getGameWorld().getFullName();
	  gameInfo += "  |  " + "Game: " + p.getGalaxy().getGameName();
	  gameInfo += "  | ";

	  return gameInfo;
  }

  /**
   * set initial left to spend & enable/disable sendBtn
   */
  public void updateTreasuryLabel(){
//System.out.println("updateTreasuryLabel: " + p.getSum());
    if (p.isDefeated()){
      treasuryLabel.setText("Left to spend: -");
    }else{
      if (p.isBrokeClient()){
        treasuryLabel.setText("Broke!");
      }else{
        treasuryLabel.setText("Left to spend: " + p.getSum());
      }
    }
    // om spelaren är pank och har utgifter eller om han inte är pank men har för höga utgifter
    if (((p.isBrokeClient()) & (p.getOrders().getExpensesCost(p.getGalaxy()) > 0)) | ((!p.isBrokeClient()) & (p.getSum() < 0))){
      sendbtn.setEnabled(false);
    }else{
      if(!client.getFinished()){
        sendbtn.setEnabled(true);
      }
    }
  }

  public void actionPerformed(ActionEvent ae){
	  if ((ae.getSource() instanceof SRButton) & (ae.getActionCommand().equalsIgnoreCase("ok"))){ // anv. har tryckt ok i confirmpopup
		  p.setAbandonGame(true);
		  client.updateTreasuryLabel();
	  }else
	  if ((ae.getSource() instanceof SRButton) & (ae.getActionCommand().equalsIgnoreCase("cancel"))){ // anv. har tryckt cancel i confirmpopup
		  abandonGameCheckBox.setSelected(false);
	  }else
	  if (ae.getSource() == abandonGameCheckBox){
		  if (abandonGameCheckBox.isSelected()){
			  GeneralConfirmPopupPanel popup = new GeneralConfirmPopupPanel("Abandon game?",this,"Do you really want to abandon this game?");
			  popup.setPopupSize(350,110);
			  popup.open(this);
		  }else{
			  p.setAbandonGame(false);
			  client.updateTreasuryLabel();
		  }
	  }else 
	  if (ae.getSource() == turnFinishedCheckBox){
		  p.setFinishedThisTurn(turnFinishedCheckBox.isSelected());
	  }/*else if(ae.getSource() == showmapbtn){
		  aGameGUIPanel.hidePanels();
		  aGameGUIPanel.showMap();
	  }*/else{ // user pressed Finished/Send	  
		  sendbtn.setEnabled(false);
		  client.finishedSendPressed();
	  }	  
  }
  
  public void enableSendBtn(){
	  sendbtn.setEnabled(true);
  }
  

public void showNewMailImage(boolean show) {
	newMessageAnimatedImagePanel.showNewMailImage(show);
}


}