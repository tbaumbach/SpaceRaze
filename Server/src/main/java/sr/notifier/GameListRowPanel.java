package sr.notifier;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import sr.client.GeneralConfirmPopupPanel;
import sr.client.GeneralMessagePopupPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.SRTextField;
import sr.general.logging.Logger;

@SuppressWarnings("serial")
public class GameListRowPanel extends SRBasePanel implements ActionListener{
	private GameData gameData;
	private NotifierFrame notifierFrame;
	private SRTextField passwordTF;
	private boolean wrongPassword;
	
	public GameListRowPanel(GameData gameData, int rowHeight, NotifierFrame notifierFrame){
		this.gameData = gameData;
		Logger.finer("gameId: " + gameData.getGameId());
		this.notifierFrame = notifierFrame;
		setLayout(null);
		
		String[] columnData = gameData.getColumnData();
		int[] columnWidhts = GameData.getColumnsWidths();

		int x = 0;

		if (notifierFrame.getReturnGames() == ReturnGames.OWN_AND_OPEN){
			if (gameData.getTurn() > 0){
				int index = findLoginName(notifierFrame.getUser());
				if (index > -1){
					String statusChar = gameData.getPlayers()[index][2];
					ImageIcon ii = null;
					if (statusChar.equals("n")){
						ii = new ImageIcon(notifierFrame.getGreen());
					}else
					if (statusChar.equals("s")){
						ii = new ImageIcon(notifierFrame.getYellow());
					}else
					if (statusChar.equals("x")){
						ii = new ImageIcon(notifierFrame.getRed());
					}
					JLabel statusLabel = new JLabel("",ii,JLabel.LEFT);
					statusLabel.setBounds(x,2,20,20);
					add(statusLabel);
				}
			}
			x += 30;
		}
		
		SRLabel tmpLbl = null;
		for (int i = 0; i < columnWidhts.length; i++) {
			tmpLbl = new SRLabel(columnData[i]);
			tmpLbl.setBounds(x, 0, columnWidhts[i], rowHeight);
			add(tmpLbl);			
			x += columnWidhts[i];
		}
		
		if (gameData.getStatus().equals("Starting")){
			String password = "";
			if (gameData.getPassword() != null){
				password = gameData.getPassword();
			}
			if ((notifierFrame.getReturnGames() == ReturnGames.OWN_AND_OPEN) & !password.equals("") & (findLoginName(notifierFrame.getUser()) == -1)){
				passwordTF = new SRTextField();
				passwordTF.setBounds(x, 2, 100, rowHeight-4);
				passwordTF.addActionListener(this);
				add(passwordTF);
				x += 110;
			}
			if ((notifierFrame.getReturnGames() == ReturnGames.ALL) | (findLoginName(notifierFrame.getUser()) == -1)){
				SRButton openGameBtn = new SRButton("Join");
				openGameBtn.setBounds(x, 2, 100, rowHeight-4);
				openGameBtn.addActionListener(this);
				add(openGameBtn);
				x += 110;
				if (notifierFrame.getReturnGames() == ReturnGames.ALL){
					SRButton deleteBtn = new SRButton("Delete");
					deleteBtn.setBounds(x, 2, 100, rowHeight-4);
					deleteBtn.addActionListener(this);
					add(deleteBtn);			
				}
			}else{
				tmpLbl = new SRLabel("Joined");
				tmpLbl.setBounds(x, 0, 100, rowHeight);
				add(tmpLbl);			
			}
		}else{
			if (notifierFrame.getReturnGames() == ReturnGames.OWN_AND_OPEN){
				SRButton openGameBtn = new SRButton("Play");
				openGameBtn.setBounds(x, 2, 100, rowHeight-4);
				openGameBtn.addActionListener(this);
				add(openGameBtn);
			}else{  // ReturnGames.ALL
				// skapa updateknapp
				SRButton updateBtn = new SRButton("+");
				updateBtn.setBounds(x, 2, 25, rowHeight-4);
				updateBtn.addActionListener(this);
				add(updateBtn);			
				x += 35;
				// skapa rollbackknapp
				SRButton rollbackBtn = new SRButton("-");
				rollbackBtn.setBounds(x, 2, 25, rowHeight-4);
//				rollbackBtn.setBackground(rollbackBtn.getBackground().brighter());
				rollbackBtn.addActionListener(this);
				add(rollbackBtn);			
				x += 35;
				// skapa deleteknapp
				SRButton deleteBtn = new SRButton("Del");
				deleteBtn.setBounds(x, 2, 25, rowHeight-4);
//				deleteBtn.setBackground(rollbackBtn.getBackground().brighter());
				deleteBtn.addActionListener(this);
				add(deleteBtn);			
				x += 40;
				// skapa knappar f�r alla spelare i partiet
				SRButton tmpBtn = null;
				String[][] players = gameData.getPlayers();
				int btnWidth = 60;
				if (players.length > 5){
					btnWidth = (310/players.length)-10;
				}
				for (int i = 0; i < players.length; i++) {
					tmpBtn = new SRButton(players[i][0]);
					tmpBtn.setBounds(x, 2, btnWidth, rowHeight-4);
					tmpBtn.addActionListener(this);
					Color btnColor = getColor(players[i][3]);
					tmpBtn.setForeground(btnColor);
					tmpBtn.setBorder(new LineBorder(btnColor));
					if (players[i][2].equals("n")){
						tmpBtn.setForeground(btnColor.darker().darker().darker());
						tmpBtn.setBorder(new LineBorder(btnColor.darker().darker().darker()));
					}
					add(tmpBtn);			
					x += (btnWidth + 10);
				}		
			}
		}
	}
	
	private Color getColor(String colorValues){
		Color newColor = null;
		Logger.info("colorValues: " + colorValues);
		int red,green,blue;
		StringTokenizer st = new StringTokenizer(colorValues);
		red = Integer.valueOf(st.nextToken());
		green = Integer.valueOf(st.nextToken());
		blue = Integer.valueOf(st.nextToken());
		Logger.info(red + " " + green + " " + blue);
		newColor = new Color(red,green,blue);
		return newColor;
	}
	
	public void actionPerformed(ActionEvent ae){
		Logger.fine("Open game: " + gameData.getGameName() + ", id: " + gameData.getGameId());
		
		Logger.fine("ae.getSource() instanceof SRButton: " + (ae.getSource() instanceof SRButton));
		Logger.fine("ae.getActionCommand(): " + ae.getActionCommand());
		Logger.fine("ae.getSource() == passwordTF: " + (ae.getSource() == passwordTF));
		Logger.fine("notifierFrame.getReturnGames(): " + notifierFrame.getReturnGames());
		Logger.fine("((passwordTF != null) && passwordTF.getText().equals(gameData.getPassword())): " + ((passwordTF != null) && passwordTF.getText().equals(gameData.getPassword())));
		
		
		if ((ae.getSource() instanceof SRButton) & (ae.getActionCommand().equalsIgnoreCase("ok"))){ // anv. har tryckt ok i confirmpopup
			if (wrongPassword){
				wrongPassword = false;
			}else{
				// om ok fr�n anv�ndaren ta bort spelet
				notifierFrame.deleteGame(gameData.getGameName());
				// update games list
				notifierFrame.checkWithServer();
			}
		}else
		if (ae.getActionCommand().equals("Join") | (ae.getSource() == passwordTF)){
			if (notifierFrame.getReturnGames() == ReturnGames.ALL | notifierFrame.getReturnGames() == ReturnGames.OWN_AND_OPEN | ((passwordTF != null) && passwordTF.getText().equals(gameData.getPassword()))){
				if (notifierFrame.getReturnGames() == ReturnGames.OWN_AND_OPEN){
					notifierFrame.showApplet(gameData.getGameId(),notifierFrame.getUser(),notifierFrame.getPassword());
				}else{
					notifierFrame.showApplet(gameData.getGameId(),null,null);
				}
			}else{ // wrong password
				wrongPassword = true;
				GeneralMessagePopupPanel messagePopup = new GeneralMessagePopupPanel("Wrong password",this,"Wrong password for game " + gameData.getGameName());
				messagePopup.setPopupSize(300,110);
				messagePopup.open(this);
				passwordTF.setText("");
			}
		}else
		if (ae.getActionCommand().equals("+")){
			// update game +1 turn
			notifierFrame.updateGame(1, gameData.getGameName());
			// update games list
			notifierFrame.checkWithServer();
		}else
		if (ae.getActionCommand().equals("-")){
			// update game +1 turn
			notifierFrame.updateGame(-1, gameData.getGameName());
			// update games list
			notifierFrame.checkWithServer();
		}else
		if (ae.getActionCommand().equals("Del") | ae.getActionCommand().equals("Delete")){
			// fr�ga anv�ndaren om han verkligen vill ta bort spelet
			GeneralConfirmPopupPanel popup = new GeneralConfirmPopupPanel("Delete game?",this,"Do you really want to delete this game?");
			popup.setPopupSize(350,110);
			popup.open(this);
		}else
		if (ae.getActionCommand().equals("Play")){
			notifierFrame.showApplet(gameData.getGameId(),notifierFrame.getUser(),notifierFrame.getPassword());
		}else{
			int index = findLoginName(ae.getActionCommand());
			String user = gameData.getPlayers()[index][0];
			String password = gameData.getPlayers()[index][1]; 
			Logger.fine("Btn pressed: " + user + " " + password);
			notifierFrame.showApplet(gameData.getGameId(),user,password);
		}
	}
	
	public int findLoginName(String loginName){
		Logger.finer("findUser: " + loginName);
		int foundIndex = -1;
		int tmpIndex = 0;
		while ((foundIndex == -1) & (tmpIndex < gameData.getPlayers().length)){
			String aLoginName = gameData.getPlayers()[tmpIndex][0];
			Logger.finer("aLoginName: " + aLoginName);
			if (aLoginName.equals(loginName)){
				foundIndex = tmpIndex;
			}else{
				tmpIndex++;
			}
		}
		Logger.finer("foundIndex: " + foundIndex);
		return foundIndex;
	}

}
