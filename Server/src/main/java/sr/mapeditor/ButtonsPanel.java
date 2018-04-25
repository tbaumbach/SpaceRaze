/*
 * Created on 2005-jun-16
 */
package sr.mapeditor;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;
import sr.client.components.SRButton;
import sr.world.Map;

/**
 * @author WMPABOD
 *
 * Panel where the map is shown
 */
@SuppressWarnings("serial")
public class ButtonsPanel extends JPanel implements ActionListener{
	private SRButton saveBtn,saveAsBtn,publishBtn,publishAsBtn,exitBtn;
	private MapEditorApplet applet;
	private boolean saveState,pubState;

	public ButtonsPanel(MapEditorApplet applet){
		this.applet = applet;
		setBorder(new LineBorder(StyleGuide.colorNeutral));
		setLayout(null);

		saveBtn = new SRButton("Save Draft");
		saveBtn.setBounds(10,10,105,22);
		saveBtn.addActionListener(this);
	    add(saveBtn);

		saveAsBtn = new SRButton("Save Draft As...");
		saveAsBtn.setBounds(10,40,105,22);
		saveAsBtn.addActionListener(this);
	    add(saveAsBtn);

		publishBtn = new SRButton("Publish");
		publishBtn.setBounds(10,70,105,22);
		publishBtn.addActionListener(this);
	    add(publishBtn);

		publishAsBtn = new SRButton("Publish As...");
		publishAsBtn.setBounds(10,100,105,22);
		publishAsBtn.addActionListener(this);
	    add(publishAsBtn);

		exitBtn = new SRButton("Exit");
		exitBtn.setBounds(10,130,105,22);
		exitBtn.addActionListener(this);
	    add(exitBtn);
	    
	    saveBtn.setEnabled(false);
	    publishBtn.setEnabled(false);
	}
	
	public void activateSaveBtns(){
	    saveBtn.setEnabled(true);
	    publishBtn.setEnabled(true);
	}

	public void paintComponent(Graphics g){
		g.setColor(StyleGuide.colorBackground);
		g.fillRect(0,0,getSize().width,getSize().height);
	}
	
	/**
	 * This method enables or disbles buttons while a map action is i progress.
	 * It also stores the state of all buttons.
	 * @param enable True = return to state before disabling, false = disable
	 */
	public void enableBtnsWhileMapAction(boolean enable){
		saveAsBtn.setEnabled(enable);
		publishAsBtn.setEnabled(enable);
		if (enable){
			saveBtn.setEnabled(saveState);
			publishBtn.setEnabled(pubState);
		}else{
			// save buttons state
			saveState = saveBtn.isEnabled();
			pubState = publishBtn.isEnabled();
			// disable buttons
			saveBtn.setEnabled(false);
			publishBtn.setEnabled(false);
		}
	}
	
	private boolean checkFileNameOk(String aFileName){
		boolean ok = true;
		String message = "The map can't be published due to the following error in the filename:\n";
		// check not empty
		if (!aFileName.equals("")){
			boolean foundBadCharacter = false;
			for (int i = 0; i < aFileName.length(); i++){
				boolean tmpOk = false;
				char c = aFileName.charAt(i);
				// letters
				if (Character.isLetter(c)){
					tmpOk = true;
				}else // numbers
				if (Character.isDigit(c)){
					tmpOk = true;
				}else // underscore
				if (c == '_'){
					tmpOk = true;
				}
				if (!tmpOk){
					foundBadCharacter = true;
				}
			}
			if (foundBadCharacter){
				ok = false;
				message = message + "- filename can only contain letters, numbers and underscore" + "\n";
			}
		}else{
			ok = false;
			message = message + "- filename cannot be empty" + "\n";
		}
		// open error dialog
		if (!ok){
			JOptionPane.showMessageDialog(applet,message,"Map filename error",JOptionPane.ERROR_MESSAGE);
		}
		return ok;
	}

	private boolean validateSaveOk(){
		boolean ok = true;
		String message = "The map can't be saved due to the following errors:\n";
		Map theMap = applet.getMap();
		boolean noEmptyNames = theMap.checkPlanetNamesNoEmpty();
		// check if any planet has an empty name  or a name consisting only of blanks
		if (!noEmptyNames){
			ok = false;
			message = message + "- 'Planet name' field can not be empty" + "\n";
		}
		if (!ok){
			JOptionPane.showMessageDialog(applet,message,"Map data error",JOptionPane.ERROR_MESSAGE);
		}
		return ok;
	}
	
	private boolean validatePublishOk(){
		boolean ok = true;
		Map theMap = applet.getMap();
		String message = "The map can't be published due to the following errors:\n";
		// check map name not empty
		if (theMap.getNameFull().equals("")){
			ok = false;
			message = message + "- 'Map name' field can not be empty" + "\n";
		}
		// check number in recNrPlayers
		if (theMap.getMaxNrStartPlanets() < 2){
			ok = false;
			message = message + "- 'Rec # players' field must contain a number (without decimals) greater than 1" + "\n";
		}else
		// check recNrPlayers aren't greater than the number of planets
		if (theMap.getMaxNrStartPlanets() > theMap.getPlanets().size()){
			ok = false;
			message = message + "- 'Rec # players' field must contain a number smaller or equal to the number of planets in the map" + "\n";
		}
		// check number of planets, cannot be smaller than 2
		if (theMap.getPlanets().size() < 2){
			ok = false;
			message = message + "- the number of planets in the map must be at least 2" + "\n";
		}else
		// check that all planets have at least 1 connection
		if (theMap.checkConnectionToPlanets() != null){	
			ok = false;
			message = message + "- all planets must have at least one connection to another planet" + "\n";
		}else
		if (!theMap.checkPlanetsAllConnected()){
			ok = false;
			message = message + "- it must be possible to travel from any planet to any other planet, the map may not be divided into regions with no connections in between." + "\n";
		}
		if (!theMap.checkPlanetNamesUnique()){
			ok = false;
			message = message + "- all planets must unique names\n";
		}
		if (!ok){
			JOptionPane.showMessageDialog(applet,message,"Map data error",JOptionPane.ERROR_MESSAGE);
		}
		return ok;
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == exitBtn){
			applet.exit();
		}else
		if (validateSaveOk()){
			if (ae.getSource() == saveBtn){
				applet.updateMapData();
				applet.saveMap(MapEditorApplet.SAVE_DRAFT,applet.getMapFileName());
			}else
			if (ae.getSource() == saveAsBtn){
				applet.updateMapData();
				String aFileName = (String)JOptionPane.showInputDialog(applet,"Enter filename: ","Save Draft As...",JOptionPane.QUESTION_MESSAGE,null,null,applet.getMapFileName());
				if (checkFileNameOk(aFileName)){
					applet.setMapFileName(aFileName);
					applet.saveMap(MapEditorApplet.SAVE_DRAFT,aFileName);
					activateSaveBtns();
				}
			}else
			if (validatePublishOk()){
				if (ae.getSource() == publishBtn){
					applet.updateMapData();
					applet.saveMap(MapEditorApplet.SAVE_PUB,applet.getMapFileName());
				}else
				if (ae.getSource() == publishAsBtn){
					applet.updateMapData();
					String aFileName = (String)JOptionPane.showInputDialog(applet,"Enter filename: ","Publish As...",JOptionPane.QUESTION_MESSAGE,null,null,applet.getMapFileName());
					if (checkFileNameOk(aFileName)){
						applet.setMapFileName(aFileName);
						applet.saveMap(MapEditorApplet.SAVE_PUB,aFileName);
						activateSaveBtns();
					}
				}
			}
		}
	}
}
