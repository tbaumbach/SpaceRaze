package sr.notifier;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.border.LineBorder;

import sr.client.GeneralMessagePopupPanel;
import sr.client.StyleGuide;
import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRLabel;
import sr.general.logging.Logger;
import sr.server.map.MapImageCreator;
import sr.world.Map;

@SuppressWarnings("serial")
public class MapInfoPanel extends SRBasePanel implements ItemListener, ActionListener {
	private SRLabel titleLbl,maxPlayersLbl,maxPlayersLbl2,nrStartPlanetsLbl,nrStartPlanetsLbl2;
	private ComboBoxPanel maxPlayersChoice,nrStartPlanetsChoice;
	// info fields
	private SRLabel  nrPlanetsLbl,averageNrConnLbl,nrPlanetsLbl2,averageNrConnLbl2;
	// map
	private sr.world.Map map;
	
	public MapInfoPanel(Map aMap, boolean showAdvanced){
		this.map = aMap;
		setLayout(null);
		setSize(520, 610);                         
		setBorder(new LineBorder(StyleGuide.colorNeutral));
		
		titleLbl = new SRLabel();
		titleLbl.setSize(150, 20);
		titleLbl.setForeground(titleLbl.getForeground().brighter());
		addUpperLeft(titleLbl);

		// left column
		
		nrPlanetsLbl = new SRLabel("# Planets:");
		nrPlanetsLbl.setSize(150, 20);
		addBelow(nrPlanetsLbl,titleLbl);
		nrPlanetsLbl2 = new SRLabel();
		nrPlanetsLbl2.setSize(70, 20);
		addRight(nrPlanetsLbl2,nrPlanetsLbl);

		averageNrConnLbl = new SRLabel("Average # Connections:");
		averageNrConnLbl.setSize(150, 20);
		addBelow(averageNrConnLbl,nrPlanetsLbl);
		averageNrConnLbl2 = new SRLabel();
		averageNrConnLbl2.setSize(70, 20);
		addRight(averageNrConnLbl2,averageNrConnLbl);

		// right column
		
		maxPlayersLbl = new SRLabel("# Players:");
		maxPlayersLbl.setSize(130, 20);
		addRight(maxPlayersLbl,nrPlanetsLbl2);
		maxPlayersLbl2 = new SRLabel();
		maxPlayersLbl2.setSize(130, 20);
		addRight(maxPlayersLbl2,maxPlayersLbl);
		maxPlayersChoice = new ComboBoxPanel();
		maxPlayersChoice.setSize(100,20);
		maxPlayersChoice.addItemListener(this);
		addRight(maxPlayersChoice,maxPlayersLbl);

		nrStartPlanetsLbl = new SRLabel("# Start planets:");
		nrStartPlanetsLbl.setSize(130, 20);
		addBelow(nrStartPlanetsLbl,maxPlayersLbl);
		nrStartPlanetsLbl2 = new SRLabel();
		nrStartPlanetsLbl2.setSize(130, 20);
		addRight(nrStartPlanetsLbl2,nrStartPlanetsLbl);
		nrStartPlanetsChoice = new ComboBoxPanel();
		nrStartPlanetsChoice.setSize(100,20);
		addRight(nrStartPlanetsChoice,nrStartPlanetsLbl);

		showMap(aMap,showAdvanced);
	}
	
	public void paintComponent(Graphics g){
		// draw background
		g.setColor(Color.BLACK);
		Dimension d = getSize();
		g.fillRect(0, 0, d.width, d.height);
		// draw map
		Image mapImage = new MapImageCreator().createImage(map);
		int width = mapImage.getWidth(this);
		int height = mapImage.getHeight(this);
//		Logger.fine("W/H: " + width + "/" + height);
		int x = 0;
		int y = 0;
		if (width < 500){
			x = (500-width)/2;
		}
		if (height < 500){
			y = (500-height)/2;
		}
		g.drawImage(mapImage, 10+x, 100+y, this);
		// border around map
		g.setColor(StyleGuide.colorNeutral);
		g.drawRect(10, 100, 500, 500);
	}

    public void showMap(Map aMap, boolean showAdvanced){
    	Logger.fine(aMap.getNameFull() + " " + showAdvanced);
    	this.map = aMap;
		titleLbl.setText(aMap.getNameFull());
		maxPlayersChoice.removeAllItems();
		for (int i = 2; i <= aMap.getNrPlanets(); i++) {
			maxPlayersChoice.addItem(String.valueOf(i));
		}
		maxPlayersChoice.setSelectedIndex(aMap.getMaxNrStartPlanets()-2);
		if (showAdvanced){
			maxPlayersChoice.setVisible(true);
			maxPlayersLbl2.setVisible(false);
		}else{
			maxPlayersChoice.setVisible(false);
			maxPlayersLbl2.setText(maxPlayersChoice.getSelectedItem());
			maxPlayersLbl2.setVisible(true);
		}
		nrStartPlanetsChoice.removeAllItems();
		int maxStartPlanets = aMap.getNrPlanets()/aMap.getMaxNrStartPlanets();
		for (int i = 1; i <= maxStartPlanets; i++) {
			nrStartPlanetsChoice.addItem(String.valueOf(i));
		}
		if (showAdvanced){
			nrStartPlanetsChoice.setVisible(true);
			nrStartPlanetsLbl2.setVisible(false);
		}else{
			nrStartPlanetsChoice.setVisible(false);
			nrStartPlanetsLbl2.setText("1");
			nrStartPlanetsLbl2.setVisible(true);
		}
		nrPlanetsLbl2.setText(String.valueOf(aMap.getNrPlanets()));
		averageNrConnLbl2.setText(aMap.getAverageNrConnectionsString());
    }

    public int getMaxPlayers(){
    	return maxPlayersChoice.getSelectedIndex() + 2;
    }
    
    public int getNrStartPlanets(){
    	return nrStartPlanetsChoice.getSelectedIndex() + 1;
    }

    public void itemStateChanged(ItemEvent ie){
    	if (ie.getStateChange() == ItemEvent.SELECTED){
    		if ((ComboBoxPanel)ie.getItemSelectable() == maxPlayersChoice){
    			Logger.info("changed");
    			int oldNrPlanetsIndex = nrStartPlanetsChoice.getSelectedIndex();
    			nrStartPlanetsChoice.setSelectedIndex(0);
    			nrStartPlanetsChoice.removeAllItems();
    			int maxStartPlanets = map.getNrPlanets()/(maxPlayersChoice.getSelectedIndex()+2);
    			for (int i = 1; i <= maxStartPlanets; i++) {
    				nrStartPlanetsChoice.addItem(String.valueOf(i));
    			}
    			if (oldNrPlanetsIndex > 0){
    				Logger.fine("popup");
    				GeneralMessagePopupPanel messagePopup = new GeneralMessagePopupPanel("# Start Planets reset",this,"The # Start Planets choice have been reset to '1'");
    				messagePopup.setPopupSize(300,110);
    				messagePopup.open(this);
    			}
    		}
    	}
    }

    public void actionPerformed(ActionEvent ae){
    	// not used
    }
    
}
