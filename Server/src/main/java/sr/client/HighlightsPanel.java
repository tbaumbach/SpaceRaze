package sr.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sr.client.color.ColorConverter;
import sr.client.components.HighlightPanel;
import sr.client.components.HighlightScrollButton;
import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.enums.HighlightType;
import sr.general.logging.Logger;
import sr.world.CanBeLostInSpace;
import sr.world.Faction;
import sr.world.Highlight;
import sr.world.Player;
import sr.world.Report;
import sr.world.ResearchAdvantage;

/**
 * 
 * @author WMPABOD
 *
 * This class shows a panel with all highlight items from last turn.
 * It is shown as default at startup after login to client.
 */
@SuppressWarnings("serial")
public class HighlightsPanel extends SRBasePanel implements SRUpdateablePanel{
  String id;
  private List<HighlightPanel> allHighlightPanels = new LinkedList<HighlightPanel>();
  private SRLabel highlightTitle, lostInSpaceTitle, highlightTitle2;
  private HighlightScrollButton hlsbUp,hlsbDown;
  private int offsetNr = 0;
//  private int hlpWidth = 350;
  private int hlpWidth = 500;
  private Player curPlayer;
  private Report lastReport;
  private int lastturn;
  private List<SRLabel> researchLabels =  new ArrayList<SRLabel>();

  public HighlightsPanel(Player currentPlayer, String id) {
  	this.curPlayer = currentPlayer;
    this.id = id;
    setLayout(null);
       
//    lastturn = currentPlayer.getGalaxy().getTurn() - 1;
    lastturn = currentPlayer.getGalaxy().getTurn();
    String titleStr = "Highlights";
	int xoffset = 0;
    if (lastturn > 1){
    	titleStr = titleStr + " turn " + lastturn;
    }else{ // turn = 0
    	titleStr = titleStr + " New Game";
    }

    if (((lastturn) >= 10) & ((lastturn) < 100)){
    	xoffset = 8;
    }else
   	if ((lastturn) >= 100){
   		xoffset = 17;
   	}else
   	if (lastturn == 1){
   		xoffset = 37;
   	}
    highlightTitle2 = new SRLabel("(View Turn Info for details)");
    highlightTitle2.setBounds(145 + xoffset,10,200,20);
    highlightTitle2.setFont(new Font("SansSerif",0,17));
    add(highlightTitle2);
    highlightTitle = new SRLabel(titleStr);
//    highlightTitle.setBounds(10,10,hlpWidth,20);
    highlightTitle.setBounds(10,10,135 + xoffset,20);
    highlightTitle.setFont(new Font("SansSerif",1,17));
    add(highlightTitle);

  	if (lastturn > 0){
  		lostInSpaceTitle = new SRLabel("Lost in Spaze");
  		lostInSpaceTitle.setBounds(hlpWidth + 55,10,200,20);
  		lostInSpaceTitle.setFont(new Font("SansSerif",1,17));
  		add(lostInSpaceTitle);
  	}
    
    hlsbUp = new HighlightScrollButton(true,this);
    hlsbUp.setBounds(10,42,hlpWidth,18);
	hlsbUp.setEnabled(false);
    add(hlsbUp);
    
	hlsbDown = new HighlightScrollButton(false,this);
    hlsbDown.setBounds(10,594,hlpWidth,18);
	hlsbDown.setEnabled(false);
    add(hlsbDown);

    lastReport = currentPlayer.getTurnInfo().getLatestGeneralReport();
    List<Highlight> highlightsList = lastReport.getHighlights();
    int tmpy = 65;
    if (highlightsList.size() == 0){
    	// add highlight that there are no special highlights this turn
    	highlightsList.add(new Highlight(null,HighlightType.TYPE_NOTHING_TO_REPORT));
    }

	int countVisible = 0;
    for (Highlight hlTmp : highlightsList) {
    	countVisible++;
		HighlightPanel hlp = new HighlightPanel(hlTmp);
		hlp.setBounds(10,tmpy,hlpWidth,30);
		allHighlightPanels.add(hlp);
		if (countVisible > 15){
			hlp.setVisible(false);
		}
		add(hlp);
		tmpy = tmpy + 35;
	}

    if (highlightsList.size() > 15){
    	hlsbDown.setEnabled(true);
    }
    
    updateData();
}
  
  public void move(boolean up){
  	if (up){
  		offsetNr = offsetNr + 1;
  	}else{
  		offsetNr = offsetNr - 1;
  	}
	int tmpy = 65;
  	for (int i = 1; i < allHighlightPanels.size() + 1; i++){
  		HighlightPanel hlp = (HighlightPanel)allHighlightPanels.get(i - 1);
  		int pos = i + offsetNr;
  		if ((pos >= 1) & (pos <= 15)){
  			hlp.setBounds(10,tmpy,hlpWidth,30);
  			hlp.setVisible(true);
  			tmpy = tmpy + 35;
  		}else{
  			hlp.setVisible(false);
  		}
  	}
  	if (allHighlightPanels.size() + offsetNr == 15){
  		hlsbDown.setEnabled(false);
  	}else{
  		hlsbDown.setEnabled(true);
  	}
  	if (offsetNr == 0){
  		hlsbUp.setEnabled(false);
  	}else{
  		hlsbUp.setEnabled(true);
  	}
	paintChildren(getGraphics());
  }
  
  public void paintComponent(Graphics g){
  	g.setColor(StyleGuide.colorBackground);
  	g.fillRect(0,0,getSize().width,getSize().height);
  	if (lastturn > 0){
  		int curY = 70;
  		int intervalY = 20;
//  		int centerX = 470;
  		int centerX = hlpWidth + 170;
//  		int maxShipsPerRow = 3;
  		List<CanBeLostInSpace> allLostInSpace = lastReport.getLostInSpace();
  		boolean lisExist = false;
  		// print players own losses
//  		String playerFactionName = curPlayer.getFaction().getName();
  		List<CanBeLostInSpace> lisList = getLostInSpace(allLostInSpace,null,curPlayer);
  		if (lisList.size() > 0){
  			curY = drawFactionLis(centerX,curY,g,intervalY,lisList,"Own ships lost",StyleGuide.colorCurrent,StyleGuide.colorCurrent);
  			lisExist = true;
  		}
  		// print neutral ships destroyed
  		lisList = getLostInSpace(allLostInSpace,null,null);
  		if (lisList.size() > 0){
  			curY = drawFactionLis(centerX,curY,g,intervalY,lisList,"Neutral ships destroyed",StyleGuide.colorCurrent,StyleGuide.colorNeutral);
  			lisExist = true;
  		}
  		
//  		List allFactions = curPlayer.getGalaxy().getActiveFactions(curPlayer.getFaction());
  		List<Faction> allFactions = curPlayer.getGalaxy().getFactions();

  		for (Faction aFaction : allFactions) {
  			lisList = getLostInSpace(allLostInSpace,aFaction.getName(),curPlayer);
  	  		if (lisList.size() > 0){
  	  			curY = drawFactionLis(centerX,curY,g,intervalY,lisList,aFaction.getName() + " ships destroyed",StyleGuide.colorCurrent,ColorConverter.getColorFromHexString(aFaction.getPlanetHexColor()));
  	  			lisExist = true;
  	  		}
		}
  		
  		// if no ships is lost in space, print "None"
  		if (!lisExist){
  		  	g.setColor(StyleGuide.colorCurrent);
  		  	drawLisString("None",centerX,curY,g);
  		}
  	}
  }
  
  private int drawFactionLis(int centerX, int aCurY, Graphics g, int intervalY, List<CanBeLostInSpace> lisList, String title, Color curColor, Color facColor){
  	int tmpCurY = aCurY;
  	g.setColor(curColor);
  	drawLisString(title,centerX,tmpCurY,g);
  	g.setColor(facColor);
  	tmpCurY = tmpCurY + intervalY;
  	if (lisList.size() > 0){
  		String tmpShipsString = "";
//  		int rowCounter = 0;
  		for (Iterator<CanBeLostInSpace> iter = lisList.iterator(); iter.hasNext();) {
			CanBeLostInSpace tmpLis = iter.next();
			if (!tmpShipsString.equals("")){
				tmpShipsString = tmpShipsString + " ";
			}
//			rowCounter++;
		  	FontMetrics fm = g.getFontMetrics();
//			if (rowCounter == 5){
			if (fm.stringWidth(tmpShipsString + tmpLis.getLostInSpaceString()) > 300){
				// draw row
				drawLisString(tmpShipsString,centerX,tmpCurY,g);
				tmpCurY = tmpCurY + intervalY;
//			  	rowCounter = 0;
			  	tmpShipsString = tmpLis.getLostInSpaceString();
			}else{
				tmpShipsString = tmpShipsString + tmpLis.getLostInSpaceString();
			}
		}
//  		if (rowCounter > 0){
  		if (!tmpShipsString.equals("")){
			// draw last row
			drawLisString(tmpShipsString,centerX,tmpCurY,g);
			tmpCurY = tmpCurY + intervalY;
  		}
  	}else{
  	  	drawLisString("None",centerX,tmpCurY,g);
  	  	tmpCurY = tmpCurY + intervalY;
  	}
	tmpCurY = tmpCurY + intervalY;
  	return tmpCurY;
  }
  
  private void drawLisString(String text, int centerX, int curY, Graphics g){
  	FontMetrics fm = g.getFontMetrics();
  	g.drawString(text,centerX - ((fm.stringWidth(text)/2)),curY);
  }
  
  /**
   * Returns a list with all LiS from a certain faction.
   * Same as in MailHandler.
   * @param allShips
   * @param aFaction
   * @return
   */
  private List<CanBeLostInSpace> getLostInSpace(List<CanBeLostInSpace> allLostInSpace, String aFactionName, Player aPlayer){
	List<CanBeLostInSpace> lisList = new LinkedList<CanBeLostInSpace>();
  	for (Iterator<CanBeLostInSpace> iter = allLostInSpace.iterator(); iter.hasNext();) {
		CanBeLostInSpace aLis = iter.next();
		if (aLis.getOwner() != null){
			Logger.finer("aLis.getOwner().getName(): " + aLis.getOwner().getName());
			Logger.finer("aLis.getOwner().getFaction().getName(): " + aLis.getOwner().getFaction().getName());
			Logger.finer("aFactionName: " + aFactionName);
			if ((aFactionName == null) & (aPlayer != null)){ // only the players own LIS
				if (aLis.getOwner() == aPlayer){
					lisList.add(aLis);
				}
			}else
			if (aLis.getOwner().getFaction().getName().equalsIgnoreCase(aFactionName) & (aLis.getOwner() != aPlayer)){
				lisList.add(aLis);
			}
		}else
		if ((aFactionName == null) & (aPlayer == null)){ // endast neutrala LIS
			lisList.add(aLis);
		}
	}
  	return lisList;
  }
  
  private void showOnGoingResearch(){
	  for (SRLabel aResearchLabel : researchLabels) {
		remove(aResearchLabel);
	  }
	  researchLabels.clear();
	  List<ResearchAdvantage> tmpAdvantages = null;
	  boolean firstOnGoingResearch=true;
	  tmpAdvantages = curPlayer.getResearch().getAllAdvantagesThatIsReadyToBeResearchOn();
	  for(int i = 0; i < tmpAdvantages.size(); i++){
		  if(curPlayer.getOrders().checkResearchOrder(tmpAdvantages.get(i).getName())){
			  if(firstOnGoingResearch){
				  SRLabel tempResearchPanel = new SRLabel("Present research");
				  researchLabels.add(tempResearchPanel);
				  tempResearchPanel.setBounds(555, 400, 250, 20);
				  tempResearchPanel.setFont(new Font("SansSerif",1,17));
				  firstOnGoingResearch= false;
			  }
			  SRLabel tempResearchPanel = new SRLabel(tmpAdvantages.get(i).getName());
			  researchLabels.add(tempResearchPanel);
			  int y = (i * 20) + 430;
			  tempResearchPanel.setBounds(555, y, 250, 20);
		}
	  }
  }

  public String getId(){
    return id;
  }

  public void updateData(){
	  showOnGoingResearch();
  }

}