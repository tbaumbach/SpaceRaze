package sr.client;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRTabbedPane;
import sr.client.components.SRTabbedPaneUI;
import sr.client.components.diplomacy.DiplomacyPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.logging.Logger;
import sr.world.Player;


@SuppressWarnings("serial")
public class NavBarPanel extends SRBasePanel implements ChangeListener{
  SRButton panelbtn1,panelbtn2,panelbtn3,panelbtn4,panelbtn5,panelbtn6,panelbtn7,panelbtn8,panelbtn9,panelbtn10,panelbtn11,panelbtn12,panelbtn13,panelbtn14;
  List<SRUpdateablePanel> panels = new ArrayList<SRUpdateablePanel>();
  GameGUIPanel gameGuiPanel;
 
DataBankPanel dbp;
  OrdersPanel op;
  NotesPanel np;
  IncomeExpensesPanel iep;
  GiftPanel2 gp;
  InfoPanel ip;
  BlackMarketPanel2 bmp;
  MessagePanel2 mep;
  StatisticsPanel sp;
  ResourcesPanel rp;
  HighlightsPanel hp;
  BattleSimsPanel bsp;
  ResearchPanel researchPanel;
  DiplomacyPanel dp;
  GameGUIPanel aGameGUIPanel;
  private SRTabbedPane tabbedPanel;

  public NavBarPanel(Player p,GameGUIPanel gameGuiPanel,SpaceRazeApplet client) {
    this.gameGuiPanel = gameGuiPanel;
    setLayout(null);
    setBackground(StyleGuide.colorBackground);

   
    // create all panels
	
	dbp = new DataBankPanel(p, gameGuiPanel, "Databank", client);
	dbp.setName("Databank");
    panels.add(dbp);

    op = new OrdersPanel(p.getOrders(), "Orders", p);
    op.setName("Orders");
    panels.add(op);

    np = new NotesPanel(p, "Notes");
    np.setName("Notes");
    panels.add(np);

    iep = new IncomeExpensesPanel(p,"Inc & exp");
    iep.setName("Inc & exp");
    panels.add(iep);

    gp = new GiftPanel2(p,client,"Gifts & Taxes");
    gp.setName("Gifts & Taxes");
    panels.add(gp);

    ip = new InfoPanel(p.getLastPublicInfo(), p, "Turn info");
    ip.setName("Turn info");
    panels.add(ip);

    bmp = new BlackMarketPanel2(p,client,"Black Market",p.getGalaxy().getBlackMarket());
    bmp.setName("Black Market");
    panels.add(bmp);

    mep = new MessagePanel2(p, client, "Messages");
    mep.setName("Messages");
    panels.add(mep);

    sp = new StatisticsPanel(p,p.getGalaxy(),"Statistics");
    sp.setName("Statistics");
    panels.add(sp);
    
    rp = new ResourcesPanel(p, gameGuiPanel, "Resources");
    rp.setName("Resources");
    panels.add(rp);

    hp = new HighlightsPanel(p, "Highlights");
    hp.setName("Highlights");
    panels.add(hp);

    bsp = new BattleSimsPanel(p, gameGuiPanel,"Battle Sim");
    bsp.setName("Battle Sim");
    panels.add(bsp);
    
    researchPanel = new ResearchPanel(p, "Research",client);
    researchPanel.setName("Research");
    panels.add(researchPanel);
/*	Removed.  Add if tested and reconstucted.
    dp = new DiplomacyPanel(p, "Diplomacy");
    dp.setName("Diplomacy");
    panels.add(dp);
  */  

    tabbedPanel = new SRTabbedPane("noBottomBorder");
    SRTabbedPaneUI tpui = new SRTabbedPaneUI();
    tabbedPanel.setUI(tpui);
    tabbedPanel.setFont(StyleGuide.buttonFont);
    
    tabbedPanel.setBackground(StyleGuide.colorCurrent.darker().darker().darker().darker());
    tabbedPanel.setForeground(StyleGuide.colorCurrent);
    
    // create all panels
    
    int panelIndex = 0;
    
    
    tabbedPanel.addTab("Map", tempPanel("Map"));
    tabbedPanel.setToolTipTextAt(panelIndex, "Map");
    panelIndex++;
    
    tabbedPanel.addTab("Highlights", tempPanel("Highlights"));
    tabbedPanel.setToolTipTextAt(panelIndex, "Current turns highlights");
    panelIndex++;
    
    if (!(p.isDefeated() | p.getGalaxy().isGameOver())){
	    tabbedPanel.addTab("Turn info", tempPanel("turn info"));
	    tabbedPanel.setToolTipTextAt(panelIndex, "Current and old turns information");
	    panelIndex++;
    }
    
    tabbedPanel.addTab("Resources", tempPanel("Resources"));
    tabbedPanel.setToolTipTextAt(panelIndex, "Information about yours units and plantes");
    panelIndex++;
    
    if (!(p.isDefeated() | p.getGalaxy().isGameOver())){
	    tabbedPanel.addTab("Inc & exp", tempPanel("Inc & exp"));
	    tabbedPanel.setToolTipTextAt(panelIndex, "Incom & expences");
	    panelIndex++;
    }
    
    tabbedPanel.addTab("Statistics", tempPanel("Statistics"));
    tabbedPanel.setToolTipTextAt(panelIndex, "Game statistics");
    panelIndex++;
    
    tabbedPanel.addTab("Databank", tempPanel("databank"));
    tabbedPanel.setToolTipTextAt(panelIndex, "Game world details and yours units data");
    panelIndex++;
    
    if (!(p.isDefeated() | p.getGalaxy().isGameOver())){
	    tabbedPanel.addTab("Black Market", tempPanel("Black Market"));
	    tabbedPanel.setToolTipTextAt(panelIndex, "Bay stuff one the black market");
	    panelIndex++;
    }
    
    tabbedPanel.addTab("Messages", tempPanel("Messages"));
    tabbedPanel.setToolTipTextAt(panelIndex, "Send and recieve message to players in this game");
    panelIndex++;
    
    if (!(p.isDefeated() | p.getGalaxy().isGameOver())){
	    tabbedPanel.addTab("Gifts & Taxes", tempPanel("Gifts & Taxes"));
	    tabbedPanel.setToolTipTextAt(panelIndex, "Give money to your friends in the game");
	    panelIndex++;
    }
    
    tabbedPanel.addTab("Notes", tempPanel("Notes"));
    tabbedPanel.setToolTipTextAt(panelIndex, "Write notes to remember yours plans");
    panelIndex++;
    
    tabbedPanel.addTab("Battle Sim", tempPanel("Battle Sim"));
    tabbedPanel.setToolTipTextAt(panelIndex, "Simulate battles");
    panelIndex++;
    /*
    if (!p.getGalaxy().getDiplomacyGameType().equals(DiplomacyGameType.DEATHMATCH)){
    	tabbedPanel.addTab("Diplomacy", tempPanel("diplomacy"));
        tabbedPanel.setToolTipTextAt(panelIndex, "Ses and change current diplomecy level");
        panelIndex++;
    }
    */
    tabbedPanel.addTab("Orders", tempPanel("Orders"));
    tabbedPanel.setToolTipTextAt(panelIndex, "Ses yours current turn orders");
    panelIndex++;
    
    if (p.getGalaxy().getGameWorld().isResearchWorld() && !(p.isDefeated() | p.getGalaxy().isGameOver())){
    	tabbedPanel.addTab("Research", tempPanel("Research"));
        tabbedPanel.setToolTipTextAt(panelIndex, "Develop your faction");
    }
    
    //tabbedPanel.setBounds(0,0,865,670);
    tabbedPanel.setBounds(1,0,1197,28);
    //tabbedPanel.setBounds(1,0,1108,27);
    tabbedPanel.addChangeListener(this);
    add(tabbedPanel);
    
  }

  public String getNotes(){
	  return np.getNotes();
  }

  

  public void showPanel(String panelid){
	  bsp.stopBattleSim();
	  
	  for(int i= 0; i < tabbedPanel.getTabCount(); i++){
		  if(tabbedPanel.getTitleAt(i).equals(panelid)){
			  tabbedPanel.setSelectedIndex(i);
		  }
	  }
	  
	  
	  for (int i = 0; i < panels.size(); i++){
		  SRUpdateablePanel p = panels.get(i);
		  if (p.getId().equalsIgnoreCase(panelid)){
			  p.updateData();
			  
			  ((JPanel)p).setVisible(true);
			  gameGuiPanel.setCurrentPanel((JPanel)p);
		  }else{
			  ((JPanel)p).setVisible(false);
		  }
	  }
  }
  
  public void showUnreadMessage(boolean openPopup){
	  Logger.info("show messages");
	  showPanel("Messages");
	  if (openPopup){
		  mep.showFirstUnreadMessage();
	  }
  }

  public void showShiptypeDetails(String aShipType, String faction){
	  showPanel("Databank");
	  dbp.showShiptypeDetails(aShipType, faction);
  }

  public void showTroopTypeDetails(String aTroopType, String faction){
	  showPanel("Databank");
	  dbp.showTroopTypeDetails(aTroopType, faction);
  }

  public void showBattleSim(){
	  showPanel("Battle Sim");
	  bsp.showSpaceshipSim(true);
  }

  public void showLandBattleSim(){
	  showPanel("Battle Sim");
	  bsp.showSpaceshipSim(false);
  }

  public void showVipTypeDetails(String aVIPType, String faction){
	  showPanel("Databank");
	  dbp.showVIPTypeDetails(aVIPType, faction);
  }
  
  public void showBuildingTypeDetails(String aBuildingType, String faction){
	  showPanel("Databank");
	  dbp.showBuildingTypeDetails(aBuildingType, faction);
  }

  public void addToBattleSim(String shipsString, String side){
	  bsp.addToBattleSim(shipsString, side);
  }

  public void addToLandBattleSim(String troopsString, String side){
	  bsp.addToBattleSimLand(troopsString, side);
  }

  public void stateChanged(ChangeEvent e) {	  
	  JTabbedPane pane = (JTabbedPane)e.getSource();

	  if(tabbedPanel.getSelectedComponent() != null){
		  Logger.finest("stateChanged: " + tabbedPanel.getSelectedComponent().getName());
	  }
	  if (pane.getSelectedIndex() == 0){
		  gameGuiPanel.hidePanels();
		  gameGuiPanel.showMap();
	  }else{
		  if(tabbedPanel.getSelectedComponent() != null){
			  showPanel(tabbedPanel.getSelectedComponent().getName());
		  }
	  }
  }
  
  private SRBasePanel tempPanel(String name){
	  SRBasePanel tempPanel = new SRBasePanel();
	  tempPanel.setName(name);
	  return tempPanel;
  }
  
}