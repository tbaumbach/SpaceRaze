package sr.client;

import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.world.GameWorld;
import sr.world.Player;

@SuppressWarnings("serial")
public class GamePanel extends SRBasePanel implements SRUpdateablePanel{
	private SRLabel turnLbl,govNameLbl,mapNameLbl,factionNameLbl,gameNameLbl,maxTurnsLbl,factionWinLbl,soloWinLbl,gameWorldLbl;
    private SRLabel diplomacyTypeLbl,statisticsTypeLbl;
	private SRLabel turnLbl2,govNameLbl2,mapNameLbl2,factionNameLbl2,gameNameLbl2,maxTurnsLbl2,factionWinLbl2,soloWinLbl2,gameWorldLbl2;
    private SRLabel diplomacyTypeLbl2,statisticsTypeLbl2;
    private String id;
    private Player p;
    private GameWorld gw;
    
    public GamePanel(Player p, String id){
      this.id = id;
      this.p = p;
      this.gw = p.getGalaxy().getGameWorld();

      this.setLayout(null);
      setBackground(StyleGuide.colorBackground);
//      StyleGuide.colorCurrent = p.getFaction().getPlanetColor();
      
      int cHeight = 18;
      int cWidth1 = 190;
      int cWidth2 = 240;

      turnLbl = new SRLabel("Turn:",cWidth1,cHeight);
      turnLbl.setToolTipText("Current turn.");
      addUpperLeft(turnLbl);
      turnLbl2 = new SRLabel(cWidth2,cHeight);
      addRight(turnLbl2, turnLbl);

      gameWorldLbl = new SRLabel("Gameworld name:",cWidth1,cHeight);
      addBelow(gameWorldLbl,turnLbl);
      gameWorldLbl2 = new SRLabel(cWidth2,cHeight);
      addRight(gameWorldLbl2, gameWorldLbl);
 
      govNameLbl = new SRLabel("Govenor name:",cWidth1,cHeight);
      addBelow(govNameLbl,gameWorldLbl);
      govNameLbl2 = new SRLabel(cWidth2,cHeight);
      addRight(govNameLbl2, govNameLbl);
      
      mapNameLbl = new SRLabel("Map name:",cWidth1,cHeight);
      addBelow(mapNameLbl,govNameLbl);
      mapNameLbl2 = new SRLabel(cWidth2,cHeight);
      addRight(mapNameLbl2, mapNameLbl);

      factionNameLbl = new SRLabel("Faction name:",cWidth1,cHeight);
      addBelow(factionNameLbl,mapNameLbl);
      factionNameLbl2 = new SRLabel(cWidth2,cHeight);
      addRight(factionNameLbl2, factionNameLbl);

      gameNameLbl = new SRLabel("Game name:",cWidth1,cHeight);
      addBelow(gameNameLbl,factionNameLbl);
      gameNameLbl2 = new SRLabel(cWidth2,cHeight);
      addRight(gameNameLbl2, gameNameLbl);

      maxTurnsLbl = new SRLabel("Max turns limit:",cWidth1,cHeight);
      addBelow(maxTurnsLbl,gameNameLbl);
      maxTurnsLbl2 = new SRLabel(cWidth2,cHeight);
      addRight(maxTurnsLbl2, maxTurnsLbl);

      factionWinLbl = new SRLabel("Faction win limit:",cWidth1,cHeight);
      addBelow(factionWinLbl,maxTurnsLbl);
      factionWinLbl2 = new SRLabel(cWidth2,cHeight);
      addRight(factionWinLbl2, factionWinLbl);

      soloWinLbl = new SRLabel("Solo win limit:",cWidth1,cHeight);
      addBelow(soloWinLbl,factionWinLbl);
      soloWinLbl2 = new SRLabel(cWidth2,cHeight);
      addRight(soloWinLbl2, soloWinLbl);

      diplomacyTypeLbl = new SRLabel("Diplomacy:",cWidth1,cHeight);
      addBelow(diplomacyTypeLbl,soloWinLbl);
      diplomacyTypeLbl2 = new SRLabel(cWidth2,cHeight);
      addRight(diplomacyTypeLbl2, diplomacyTypeLbl);

      statisticsTypeLbl = new SRLabel("Statistics:",cWidth1,cHeight);
      addBelow(statisticsTypeLbl,diplomacyTypeLbl);
      statisticsTypeLbl2 = new SRLabel(cWidth2,cHeight);
      addRight(statisticsTypeLbl2, statisticsTypeLbl);

      showGame();
    }
    
    public void showGame(){
    	String maxTurns = "Unlimited";
    	String turnText =  "" + p.getGalaxy().getTurn();
    	if (p.getGalaxy().getEndTurn() > 0){
    		maxTurns = String.valueOf(p.getGalaxy().getEndTurn());
    		turnText += " of " + maxTurns; 
    	}
    	
    	turnLbl2.setText(turnText);
    	
    	govNameLbl2.setText(p.getGovenorName());
    	gameWorldLbl2.setText(gw.getFullName());
    	mapNameLbl2.setText(p.getGalaxy().getMapNameFull());
    	factionNameLbl2.setText(p.getFaction().getName());
    	gameNameLbl2.setText(p.getGalaxy().getGameName());
    	maxTurnsLbl2.setText(maxTurns);
    	factionWinLbl2.setText(p.getGalaxy().getFactionVictory() + "%");
    	soloWinLbl2.setText(p.getGalaxy().getSingleVictory() + "%");
        diplomacyTypeLbl2.setText(p.getGalaxy().getDiplomacyGameType().getLongText());
        statisticsTypeLbl2.setText(p.getGalaxy().getStatisticsHandler().getStatisticGameType().getText());
    }

    public String getId(){
        return id;
    }

    public void updateData(){
    }
}
