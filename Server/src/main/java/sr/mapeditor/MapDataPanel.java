/*
 * Created on 2005-jun-16
 */
package sr.mapeditor;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.SRTextArea;
import sr.client.components.SRTextField;
import sr.world.Map;

/**
 * @author WMPABOD
 *
 * Panel where the map is shown
 */
public class MapDataPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private SRLabel fileNameLbl,mapNameLbl,descLbl,recNrPlayersLbl,fileName2Lbl,versionLbl,version2Lbl;
	private SRTextField mapNameTf,recNrPlayersTf;
	private SRTextArea descTa;
	private JScrollPane scrollPane;
	private SRButton newPlanetBtn;
	private EditorGUIPanel guiPanel;
	private Map theMap;

	public MapDataPanel(EditorGUIPanel guiPanel, Map aMap){
		this.guiPanel = guiPanel;
		this.theMap = aMap;
		
		setBorder(new LineBorder(StyleGuide.colorNeutral));
		setLayout(null);

		fileNameLbl = new SRLabel("File name:");
		fileNameLbl.setBounds(10,5,60,18);
	    add(fileNameLbl);

		fileName2Lbl = new SRLabel();
		if (theMap.getFileName() == null){
			fileName2Lbl.setText("?");
		}else{
			fileName2Lbl.setText(theMap.getFileName());
		}
		fileName2Lbl.setBounds(90,5,140,18);
	    add(fileName2Lbl);

		mapNameLbl = new SRLabel("Map name:");
		mapNameLbl.setBounds(10,25,80,20);
	    add(mapNameLbl);

		mapNameTf = new SRTextField();
	    if (theMap.getNameFull() != null){
	    	mapNameTf.setText(theMap.getNameFull());
		}else{
			mapNameTf.setText("Unnamed");
		}
		mapNameTf.setBounds(90,25,140,20);
	    add(mapNameTf);

	    versionLbl = new SRLabel("Version#:");
	    versionLbl.setBounds(10,45,60,18);
	    add(versionLbl);

	    version2Lbl = new SRLabel(String.valueOf(aMap.getVersionId()));
	    version2Lbl.setBounds(90,45,60,18);
	    add(version2Lbl);

	    descLbl = new SRLabel("Description:");
	    descLbl.setBounds(10,60,90,22);
	    add(descLbl);

	    descTa = new SRTextArea();
	    descTa.setText(theMap.getDescription());
	    descTa.setBounds(10,80,220,42);

	    scrollPane = new JScrollPane(descTa);
	    scrollPane.setBounds(10,80,220,42);
	    scrollPane.setAutoscrolls(true);
	    scrollPane.setBorder(new LineBorder(StyleGuide.colorNeutral));
	    add(scrollPane);

	    recNrPlayersLbl = new SRLabel("Rec # players:");
	    recNrPlayersLbl.setBounds(10,130,90,22);
	    add(recNrPlayersLbl);

	    recNrPlayersTf = new SRTextField("?");
	    if (theMap.getMaxNrStartPlanets() > 0){
	    	recNrPlayersTf.setText(String.valueOf(theMap.getMaxNrStartPlanets()));
	    }
	    recNrPlayersTf.setBounds(140,130,90,22);
	    add(recNrPlayersTf);

		newPlanetBtn = new SRButton("Create new planet");
		newPlanetBtn.setBounds(10,155,220,22);
		newPlanetBtn.addActionListener(this);
	    add(newPlanetBtn);
	}
	
	public void updateMapData(){
		theMap.setDescription(descTa.getText());
		theMap.setName(mapNameTf.getText());
		int maxNrPlayers = 0;
		try{
			maxNrPlayers = Integer.parseInt(recNrPlayersTf.getText());
		}
		catch (NumberFormatException nfe){
			// do nothing
		}
		theMap.setMaxNrStartPlanets(maxNrPlayers);
	}
	
	public void enableBtnsWhileMapAction(boolean enable){
		newPlanetBtn.setEnabled(enable);
	}

	public void updateVersion(){
		version2Lbl.setText(String.valueOf(theMap.getVersionId()));
	}

	public void paintComponent(Graphics g){
		g.setColor(StyleGuide.colorBackground);
		g.fillRect(0,0,getSize().width,getSize().height);
	}

	public void actionPerformed(ActionEvent arg0) {
		guiPanel.setCreateNewPlanet(true);
	}
	
	public void setMapName(String newMapName){
		fileName2Lbl.setText(newMapName);
	}
}
