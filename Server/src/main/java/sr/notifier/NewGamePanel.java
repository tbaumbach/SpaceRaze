package sr.notifier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.border.LineBorder;

import sr.client.GeneralMessagePopupPanel;
import sr.client.StyleGuide;
import sr.client.components.CheckBoxPanel;
import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.SRTextField;
import sr.enums.DiplomacyGameType;
import sr.general.logging.Logger;
import sr.server.GameWorldHandler;
import sr.world.GameWorld;
import sr.world.Map;
import sr.world.StatisticGameType;

@SuppressWarnings("serial")
public class NewGamePanel extends SRBasePanel implements ItemListener, ActionListener{
	private SRLabel mapLbl;
	private ComboBoxPanel gwChoice,mapChoice;
	private SRButton gwPlusBtn,gwMinusBtn,mapPlusBtn,mapMinusBtn;
	private SRButton okBtn,cancelBtn,advancedBtn;
	private boolean showAllFields;
	private NotifierFrame notifierFrame;
	private final String SHOW_ADV_TEXT = "Show advanced options";
	// advanced fields
	private SRLabel diplomacyLbl,endTurnLbl,statisticGameTypeLbl,timeLbl,gamePasswordLbl,customGameNameLbl;
	private ComboBoxPanel diplomacyChoice,endTurnChoice,statisticGameTypeChoice,timeChoice;
	private SRTextField gamePasswordTF,customGameNameTF;
	private CheckBoxPanel groupFactionCB,randomFactionCB;
	
	// autoBalance tror jag alltid ska vara true
	// emailPlayers ska alltid ske förutom vid singelpartier och partier med lösenord
	// dessa hanteras på kartpanelen: maxNrPlayers, numberOfStartPlanet
	// denna hanteras på gameworldpanelen: selectableFactionNames

	public NewGamePanel(NotifierFrame notifierFrame){
		this.notifierFrame = notifierFrame;
		setLayout(null);
//		setSize(1190, 600);                         
		setSize(360, 180);                         
		setBorder(new LineBorder(StyleGuide.colorNeutral));
		
		SRLabel titleLbl = new SRLabel("Start new game");
		titleLbl.setSize(150, 20);
		titleLbl.setForeground(titleLbl.getForeground().brighter());
		addUpperLeft(titleLbl);
		
		SRLabel gwLbl = new SRLabel("Gameworld:");
		gwLbl.setSize(130, 20);
		addBelow(gwLbl,titleLbl);

		gwChoice = new ComboBoxPanel();
		gwChoice.setSize(170,20);
		List<GameWorld> gws = GameWorldHandler.getGameWorldTypes();
		gwChoice.addItem("None");
		for (GameWorld gw : gws) {
			gwChoice.addItem(gw.getFullName());
		}
		gwChoice.addItemListener(this);
		addRight(gwChoice,gwLbl);

		gwMinusBtn = new SRButton("-");
		gwMinusBtn.setSize(20,10);
		gwMinusBtn.addActionListener(this);	
		addRight(gwMinusBtn,gwChoice,5);
		gwPlusBtn = new SRButton("+");
		gwPlusBtn.setSize(20,10);
		gwPlusBtn.addActionListener(this);
		addBelow(gwPlusBtn,gwMinusBtn,0);

		mapLbl = new SRLabel("Map:");
		mapLbl.setSize(130, 20);
		addBelow(mapLbl,gwLbl);

		mapChoice = new ComboBoxPanel();
		mapChoice.setSize(170,20);
		List<Map> maps = notifierFrame.getMaps();
		mapChoice.addItem("None");
		for (Map map : maps) {
			mapChoice.addItem(map.getNameFull());
		}
		mapChoice.addItemListener(this);
		addRight(mapChoice,mapLbl);

		mapMinusBtn = new SRButton("-");
		mapMinusBtn.setSize(20,10);
		mapMinusBtn.addActionListener(this);	
		addRight(mapMinusBtn,mapChoice,5);
		mapPlusBtn = new SRButton("+");
		mapPlusBtn.setSize(20,10);
		mapPlusBtn.addActionListener(this);
		addBelow(mapPlusBtn,mapMinusBtn,0);

		advancedBtn = new SRButton(SHOW_ADV_TEXT);
		advancedBtn.setSize(230,20);
		advancedBtn.addActionListener(this);	
		addBelow(advancedBtn,mapLbl,20);

		diplomacyLbl = new SRLabel("Diplomacy:");
		diplomacyLbl.setSize(150, 20);
		diplomacyLbl.setVisible(false);
		addBelow(diplomacyLbl,advancedBtn,20);

		diplomacyChoice = new ComboBoxPanel();
		diplomacyChoice.setSize(170,20);
		DiplomacyGameType[] diplomacyGameTypes = DiplomacyGameType.values();
		for (DiplomacyGameType aDiplomacyGameType : diplomacyGameTypes) {
			diplomacyChoice.addItem(aDiplomacyGameType.getLongText());
		}
		diplomacyChoice.setSelectedIndex(2);
//		diplomacyChoice.setSelectedIndex(0); // inte glömma att ändra i changeFields(boolean) nedan  också
		diplomacyChoice.addItemListener(this);
		diplomacyChoice.setVisible(false);
		addRight(diplomacyChoice,diplomacyLbl);

		endTurnLbl = new SRLabel("Max # turns:");
		endTurnLbl.setSize(150, 20);
		endTurnLbl.setVisible(false);
		addBelow(endTurnLbl,diplomacyLbl);

		endTurnChoice = new ComboBoxPanel();
		endTurnChoice.setSize(170,20);
		endTurnChoice.addItem("Unlimited");
		for (int i = 10; i <= 100; i +=10){			
			endTurnChoice.addItem(String.valueOf(i));
		}
		endTurnChoice.setSelectedIndex(10);
		endTurnChoice.addItemListener(this);
		endTurnChoice.setVisible(false);
		addRight(endTurnChoice,endTurnLbl);

		statisticGameTypeLbl = new SRLabel("Statistics:");
		statisticGameTypeLbl.setSize(150, 20);
		statisticGameTypeLbl.setVisible(false);
		addBelow(statisticGameTypeLbl,endTurnLbl);

		statisticGameTypeChoice = new ComboBoxPanel();
		statisticGameTypeChoice.setSize(170,20);
		StatisticGameType[] statisticGameTypes = StatisticGameType.values();
		for (StatisticGameType aStatisticGameType : statisticGameTypes) {
			statisticGameTypeChoice.addItem(aStatisticGameType.getText());
		}
		statisticGameTypeChoice.setSelectedIndex(2);
		statisticGameTypeChoice.addItemListener(this);
		statisticGameTypeChoice.setVisible(false);
		addRight(statisticGameTypeChoice,statisticGameTypeLbl);

		timeLbl = new SRLabel("# updates/week:");
		timeLbl.setSize(150, 20);
		timeLbl.setVisible(false);
		addBelow(timeLbl,statisticGameTypeLbl);

		timeChoice = new ComboBoxPanel();
		timeChoice.setSize(170,20);
		timeChoice.addItem("0");
		timeChoice.addItem("1");
		timeChoice.addItem("2");
		timeChoice.addItem("3");
		timeChoice.addItem("5");
		timeChoice.addItem("7");
		timeChoice.setSelectedIndex(4);
		timeChoice.addItemListener(this);
		timeChoice.setVisible(false);
		addRight(timeChoice,timeLbl);

		customGameNameLbl = new SRLabel("Custom game name:");
		customGameNameLbl.setSize(150, 20);
		customGameNameLbl.setVisible(false);
		addBelow(customGameNameLbl,timeLbl);

		customGameNameTF = new SRTextField();
		customGameNameTF.setSize(150, 20);
		customGameNameTF.setVisible(false);
		addRight(customGameNameTF,customGameNameLbl);

		gamePasswordLbl = new SRLabel("Game password:");
		gamePasswordLbl.setSize(150, 20);
		gamePasswordLbl.setVisible(false);
		addBelow(gamePasswordLbl,customGameNameLbl);

		gamePasswordTF = new SRTextField();
		gamePasswordTF.setSize(150, 20);
		gamePasswordTF.setVisible(false);
		addRight(gamePasswordTF,gamePasswordLbl);
		
		groupFactionCB = new CheckBoxPanel("Group Faction");
		groupFactionCB.setSize(150, 20);
		groupFactionCB.setVisible(false);
		groupFactionCB.setSelected(true);
		addBelow(groupFactionCB,gamePasswordTF);

		randomFactionCB = new CheckBoxPanel("Random Faction");
		randomFactionCB.setSize(150, 20);
		randomFactionCB.setVisible(false);
		addBelow(randomFactionCB,groupFactionCB);

		okBtn = new SRButton("Ok");
		okBtn.setSize(110,20);
		okBtn.addActionListener(this);		
		addLowerRight(okBtn);

		cancelBtn = new SRButton("Cancel");
		cancelBtn.setSize(110,20);
		cancelBtn.addActionListener(this);		
		addLeft(cancelBtn,okBtn);

	}

    public void itemStateChanged(ItemEvent ie){
    	if (ie.getStateChange() == ItemEvent.SELECTED){
    		try{
    			if ((ComboBoxPanel)ie.getItemSelectable() == gwChoice){
    				String gwName = gwChoice.getSelectedItem();
    				if (gwName.equalsIgnoreCase("None")){
    					notifierFrame.showGameWorldInfo(null,false);
    				}else{
    					notifierFrame.showGameWorldInfo(gwName,showAllFields);
    				}
//    				updateFields();
    			}else
    			if ((ComboBoxPanel)ie.getItemSelectable() == mapChoice){
    				String mapName = mapChoice.getSelectedItem();
    				if (mapName.equalsIgnoreCase("None")){
    					notifierFrame.showMapInfo(null,false);
    				}else{
    					notifierFrame.showMapInfo(mapName,showAllFields);
    				}
//    				updateFields();
    			}
    		}
    		catch(NumberFormatException nfe){
    		}
    	}
    }
    
    private void changeFields(boolean showAdvancedFields){
    	this.showAllFields = showAdvancedFields;
    	// show/hide fields
		diplomacyLbl.setVisible(showAdvancedFields);
		diplomacyChoice.setVisible(showAdvancedFields);
		endTurnLbl.setVisible(showAdvancedFields);
		endTurnChoice.setVisible(showAdvancedFields);
		statisticGameTypeLbl.setVisible(showAdvancedFields);
		statisticGameTypeChoice.setVisible(showAdvancedFields);
		timeLbl.setVisible(showAdvancedFields);
		timeChoice.setVisible(showAdvancedFields);
		customGameNameLbl.setVisible(showAdvancedFields);
		customGameNameTF.setVisible(showAdvancedFields);
		gamePasswordLbl.setVisible(showAdvancedFields);
		gamePasswordTF.setVisible(showAdvancedFields);
		groupFactionCB.setVisible(showAdvancedFields);
		randomFactionCB.setVisible(showAdvancedFields);

		// if hiding, set default values
		if (!showAdvancedFields){
			diplomacyChoice.setSelectedIndex(2);
			endTurnChoice.setSelectedIndex(10);
			statisticGameTypeChoice.setSelectedIndex(2);
			timeChoice.setSelectedIndex(4);
			customGameNameTF.setText("");
			gamePasswordTF.setText("");
			groupFactionCB.setSelected(true);
			randomFactionCB.setSelected(false);
		}
		
		// change height
		int height = 180;
		if (showAdvancedFields){
			height = 440;
		}
		setSize(360, height);
		
		// move adv btn and ok/cancel btns
		remove(okBtn);
		remove(cancelBtn);
		addLowerRight(okBtn);
		addLeft(cancelBtn,okBtn);
		if (showAdvancedFields){
			advancedBtn.setText("Hide advanced options");
		}else{
			advancedBtn.setText(SHOW_ADV_TEXT);
		}

    }
    
    public void actionPerformed(ActionEvent ae){
    	Logger.info("Btn pressed: " + ae.getActionCommand());
    	if (ae.getSource() == advancedBtn){
    		changeFields(ae.getActionCommand().equals(SHOW_ADV_TEXT));
			String mapName = mapChoice.getSelectedItem();
			if (!mapName.equalsIgnoreCase("None")){
				notifierFrame.showMapInfo(mapName,showAllFields);
			}
			String gwName = gwChoice.getSelectedItem();
			if (!gwName.equalsIgnoreCase("None")){
				notifierFrame.showGameWorldInfo(gwName,showAllFields);
			}
    	}else
    	if (ae.getSource() == cancelBtn){
    		notifierFrame.removeNewGamePanel();
    		notifierFrame.checkWithServer();
    	}else
    	if (ae.getSource() == okBtn){
    		if (validateInput()){
    			Logger.fine("Ok, create new game, showAllFields: " + showAllFields);
    			CreateNewGameData createNewGameData = new CreateNewGameData();
    			GameWorld theGameWorld = GameWorldHandler.findGameWorld(gwChoice.getSelectedItem());
    			createNewGameData.setGameWorldFileName(theGameWorld.getFileName());
    			Map theMap = notifierFrame.findMap(mapChoice.getSelectedItem());
    			createNewGameData.setMapName(theMap.getFileName());
				createNewGameData.setAutoBalanceString("yes");
				if (customGameNameTF.getText().equals("")){
    				createNewGameData.setGameName("?");
				}else{
    				createNewGameData.setGameName(customGameNameTF.getText());
				}
				createNewGameData.setGamePassword(gamePasswordTF.getText());
				createNewGameData.setEndTurn(endTurnChoice.getSelectedIndex()*10);
				createNewGameData.setEmailPlayers(gamePasswordTF.getText().equals("") ? "yes" : "no");
				createNewGameData.setRandomFactionString(randomFactionCB.isSelected() ? "yes" : "no");
				createNewGameData.setGroupFaction(groupFactionCB.isSelected() ? "yes" : "no");
				DiplomacyGameType diplomacyGameType = DiplomacyGameType.get(diplomacyChoice.getSelectedIndex());
				createNewGameData.setDiplomacy(diplomacyGameType.getShortText());
				createNewGameData.setUserLogin(notifierFrame.getUser());
				createNewGameData.setTimeString(timeChoice.getSelectedItem());
				createNewGameData.setStatisticGameType(StatisticGameType.values()[statisticGameTypeChoice.getSelectedIndex()]);
				// sätts genom kartpanelen
				createNewGameData.setMaxNrPlayers(String.valueOf(notifierFrame.getMaxPlayers()));
				createNewGameData.setNumberOfStartPlanet(notifierFrame.getNrStartPlanets());
				// sätts genom factionspanelen
				createNewGameData.setSelectableFactionNames(notifierFrame.getFactionNames());
				// try to create new game
    			String status = notifierFrame.createNewGame(createNewGameData);
    			// handle feedback message
    			if ((status.length() >= 12 ) && (status.substring(0, 12).equals("Game started"))){
    				String message = "New game started";
    				if (createNewGameData.getGameName().equals("?")){
    					message += ": " + status.substring(13);
    				}
    				GeneralMessagePopupPanel messagePopup = new GeneralMessagePopupPanel("New game started",this,message);
    				messagePopup.setPopupSize(300,110);
    				messagePopup.open(this);
            		notifierFrame.removeNewGamePanel();
            		notifierFrame.checkWithServer();
    			}else{
    				String[] messageRows = new String[3];
    				messageRows[0] = "New game not started";
    				messageRows[1] = "Message from server:";
    				messageRows[2] = "    " + status;
    				GeneralMessagePopupPanel messagePopup = new GeneralMessagePopupPanel("New game not started",this,550,messageRows);
    				messagePopup.open(this);
    			}
    		}
    	}else
        if (ae.getSource() == gwPlusBtn){
        	List<GameWorld> gws = GameWorldHandler.getGameWorldTypes();
        	int oldIndex = gwChoice.getSelectedIndex();
        	if (oldIndex < (gws.size())){
        		gwChoice.setSelectedIndex(oldIndex+1);
        		String gwName = gwChoice.getSelectedItem();
        		notifierFrame.showGameWorldInfo(gwName,showAllFields);
        	}
        }else
        if (ae.getSource() == gwMinusBtn){
        	int oldIndex = gwChoice.getSelectedIndex();
        	if (oldIndex > 0){
        		gwChoice.setSelectedIndex(oldIndex-1);
        		String gwName = gwChoice.getSelectedItem();
        		if (gwName.equalsIgnoreCase("None")){
        			notifierFrame.showGameWorldInfo(null,false);
        		}else{
        			notifierFrame.showGameWorldInfo(gwName,showAllFields);
        		}
        	}
    	}else
        if (ae.getSource() == mapPlusBtn){
    		List<Map> maps = notifierFrame.getMaps();
        	int oldIndex = mapChoice.getSelectedIndex();
        	Logger.fine("oldIndex: " + oldIndex);
        	if (oldIndex < (maps.size())){
            	Logger.fine("mapChoice.getSelectedIndex(): " + mapChoice.getSelectedIndex());
        		mapChoice.setSelectedIndex(oldIndex+1);
            	Logger.fine("mapChoice.getSelectedIndex(): " + mapChoice.getSelectedIndex());
        		String mapName = mapChoice.getSelectedItem();
        		notifierFrame.showMapInfo(mapName,showAllFields);
        	}
        	Logger.fine("mapChoice.getSelectedIndex(): " + mapChoice.getSelectedIndex());
        }else
        if (ae.getSource() == mapMinusBtn){
        	int oldIndex = mapChoice.getSelectedIndex();
        	if (oldIndex > 0){
        		mapChoice.setSelectedIndex(oldIndex-1);
        		String mapName = mapChoice.getSelectedItem();
        		if (mapName.equalsIgnoreCase("None")){
        			notifierFrame.showMapInfo(null,false);
        		}else{
        			notifierFrame.showMapInfo(mapName,showAllFields);
        		}
        	}
        }
    }
    
    private boolean validateInput(){
    	boolean ok = true;
    	List<String> errorMessages = new LinkedList<String>();
    	if (gwChoice.getSelectedItem().equals("None")){
    		errorMessages.add("GameWorld not chosen");
    	}
    	if (mapChoice.getSelectedItem().equals("None")){
    		errorMessages.add("Map not chosen");
    	}
    	if (customGameNameTF.getText().equals("?")){
    		errorMessages.add("'?' not allowed as game name");
    	}
    	if (errorMessages.size() > 0){
    		ok = false;
			String[] messageRows = new String[errorMessages.size()+1];
			String s = "s";
			if (errorMessages.size() == 1){
				s = "";
			}
			messageRows[0] = "The following error" + s + " were found:";
			int counter = 1;
			for (String string : errorMessages) {
				messageRows[counter] = " - " + string;
				counter++;
			}
    		
    		GeneralMessagePopupPanel messagePopup = new GeneralMessagePopupPanel("Can not start game",this,550,messageRows);
    		messagePopup.open(this);
    	}
    	return ok;
    }

}
