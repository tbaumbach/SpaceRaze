/*
 * Created on 2005-feb-25
 */
package sr.client;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import sr.client.components.BasicPopupPanel;
import sr.client.components.SRLabel;
import sr.client.components.SRTextArea;
import sr.world.Player;

/**
 * @author wmpabod
 *
 * Popuppanel used for creation and edits of messages
 */
@SuppressWarnings("serial")
public class MessagePopupPanel extends BasicPopupPanel implements ShowMapPlanet, KeyListener {
    private SRLabel messageTextLabel, messageRecipientLabel, messageRecipientNameLabel, mapLabel;
    private SRTextArea contentArea;
    private MapCanvas mapCanvas;
    private Player p;
    private boolean mapClickedLast = false;

	public MessagePopupPanel(String title, ActionListener listener, String recipient, Player player){
		super(title,listener);
		this.p = player;
	    messageRecipientLabel = new SRLabel("Recipient:"); // change to a better label text?
	    messageRecipientLabel.setBounds(10,40,100,20);
	    add(messageRecipientLabel);

	    messageRecipientNameLabel = new SRLabel(recipient);
	    messageRecipientNameLabel.setBounds(130,40,400,20);
	    add(messageRecipientNameLabel);

	    messageTextLabel = new SRLabel("Message text:");
	    messageTextLabel.setBounds(10,70,100,20);
	    add(messageTextLabel);

	    contentArea = new SRTextArea();
//	    contentArea.setBounds(130,70,500,500);
	    contentArea.setBounds(130,70,500,200);
	    contentArea.setCaretColor(StyleGuide.colorCurrent);
	    contentArea.addKeyListener(this);
	    add(contentArea);

    	mapLabel = new SRLabel("Map (Lclick to add name to message, planets will be comma separated. Rclick ta add \"&\" before planet name):");
    	mapLabel.setBounds(10,290,680,20);
    	add(mapLabel);

    	mapCanvas = new MapCanvas(p,this,true);
        mapCanvas.setPlanets(p.getGalaxy().getPlanets(),p);
        mapCanvas.setSpaceships(p.getGalaxy().getPlayersSpaceships(p));
        mapCanvas.setOwnVips(p.getGalaxy().getPlayersVips(p));
        mapCanvas.setOwnTroops(p.getGalaxy().getPlayersTroops(p));
        mapCanvas.setOthersVips(p.getGalaxy().getAllVIPs());
        mapCanvas.setConnections(p.getGalaxy().getPlanetConnections());
        mapCanvas.computeNewOrigo();
	    mapCanvas.setBounds(10, 310, 680, 300);
	    mapCanvas.setInitialZoom(-20);
	    mapCanvas.setInitialSetCenter(player.getHomeplanet().getName());
		mapCanvas.setChosenCoors("");
	    add(mapCanvas);
	}
	  
	public void setSumfieldFocus(){
//		contentArea.selectAll();
		contentArea.requestFocus();
	}
	
	public String getMessageText(){
		return contentArea.getText();
	}
	
	public void showPlanet(String aPlanetName, boolean addAnd) {
		String text = contentArea.getText();
		// ska det finnas ett blanksteg
		if (!addAnd & mapClickedLast){
			text += ",";
		}
		if (text.length() > 0){
			if (text.lastIndexOf(" ") != (text.length()-1)){
				text += " ";
			}
		}
		if (addAnd){
			text += "& ";
		}
		text += aPlanetName;
		contentArea.setText(text);
		mapCanvas.update(mapCanvas.getGraphics());
		mapClickedLast = true;
	}

	public void showPlanet(String aPlanetName) {
		showPlanet(aPlanetName,false);
	}

	public void showPlanetShips(String aPlanetName) {
		showPlanet(aPlanetName,true);
	}

	public void keyPressed(KeyEvent e) {
		System.out.println(e.toString());
		mapClickedLast = false;
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

}
