/*
 * Created on 2005-jun-16
 */
package sr.mapeditor;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.StyleGuide;
import sr.client.components.CheckBoxPanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.SRTextField;
import sr.client.components.scrollable.ListPanel;
import sr.world.Map;
import sr.world.Planet;
import sr.world.PlanetConnection;

/**
 * @author WMPABOD
 *
 * Panel where the map is shown
 */
@SuppressWarnings("serial")
public class PlanetDataPanel extends JPanel implements ActionListener, ListSelectionListener, KeyListener {
	private SRLabel planetNameLbl,xLbl,yLbl,zLbl,connectionLbl,noPlanetLbl;
	private SRButton updateBtn,addLongBtn,addShortBtn,removeConnBtn,removePlanetBtn,moveBtn;
	private SRTextField planetNameTf,xTf,yTf,zTf;
	private ListPanel connectionsList;
	private Planet selectedPlanet;
	private EditorGUIPanel guiPanel;
	private Map theMap;
	private boolean removeConnState;
	private CheckBoxPanel startPlanet;

	public PlanetDataPanel(EditorGUIPanel guiPanel, Map aMap){
		this.guiPanel = guiPanel;
		this.theMap = aMap;
		
		setBorder(new LineBorder(StyleGuide.colorNeutral));
		setLayout(null);
		
		planetNameLbl = new SRLabel("Planet name:");
		planetNameLbl.setBounds(10,10,80,22);
	    add(planetNameLbl);

		planetNameTf = new SRTextField("");
		planetNameTf.setBounds(90,10,140,22);
		planetNameTf.addActionListener(this);
		planetNameTf.addKeyListener(this);
	    add(planetNameTf);
	    
	    startPlanet = new CheckBoxPanel("Start planet");
	    startPlanet.setBounds(10,40,200,22);
	    startPlanet.setSelected(false);
	    startPlanet.addActionListener(this);
	    add(startPlanet);
	    startPlanet.setVisible(true);

	    // coordinates
		xLbl = new SRLabel("x:");
		xLbl.setBounds(10,70,10,22);
	    add(xLbl);

		xTf = new SRTextField("");
		xTf.setBounds(23,70,30,22);
		xTf.addActionListener(this);
	    add(xTf);

		yLbl = new SRLabel("y:");
		yLbl.setBounds(63,70,10,22);
	    add(yLbl);

		yTf = new SRTextField("");
		yTf.setBounds(76,70,30,22);
		yTf.addActionListener(this);
	    add(yTf);

		zLbl = new SRLabel("z:");
		zLbl.setBounds(116,70,25,22);
	    add(zLbl);

		zTf = new SRTextField("");
		zTf.setBounds(129,70,25,22);
		zTf.addActionListener(this);
	    add(zTf);

	    updateBtn = new SRButton("Update");
	    updateBtn.setBounds(170,70,60,22);
	    updateBtn.addActionListener(this);
		add(updateBtn);
		
		// List
		connectionLbl = new SRLabel("Connections:");
		connectionLbl.setBounds(10,100,220,22);
	    add(connectionLbl);

	    connectionsList = new ListPanel();
		connectionsList.setBounds(10,130,220,84);
		connectionsList.setListSelectionListener(this);
		add(connectionsList);

	    // buttons
		addShortBtn = new SRButton("Add short range");
		addShortBtn.setBounds(10,223,220,22);
		addShortBtn.addActionListener(this);
		add(addShortBtn);
		
	    addLongBtn = new SRButton("Add long range");
	    addLongBtn.setBounds(10,253,220,22);
	    addLongBtn.addActionListener(this);
		add(addLongBtn);
	    
		removeConnBtn = new SRButton("Remove conection");
		removeConnBtn.setBounds(10,283,220,22);
		removeConnBtn.addActionListener(this);
		add(removeConnBtn);
		
		moveBtn = new SRButton("Move planet");
		moveBtn.setBounds(10,313,220,22);
		moveBtn.addActionListener(this);
		add(moveBtn);

		removePlanetBtn = new SRButton("Delete planet");
		removePlanetBtn.setBounds(10,343,220,22);
		removePlanetBtn.addActionListener(this);
		add(removePlanetBtn);
		
		// if no planet is selected show this label
		noPlanetLbl = new SRLabel("NO PLANET SELECTED");
		noPlanetLbl.setBounds(55,170,140,22);
		add(noPlanetLbl);
		
		showPlanet(null);
	}
	
	public void setFocusOnName(){
		planetNameTf.selectAll();
		planetNameTf.requestFocus();
	}
	
	public void showPlanet(Planet aPlanet){
		selectedPlanet = aPlanet;
		if (aPlanet == null){
			planetNameLbl.setVisible(false);
			xLbl.setVisible(false);
			yLbl.setVisible(false);
			zLbl.setVisible(false);
			connectionLbl.setVisible(false);
			updateBtn.setVisible(false);
			addLongBtn.setVisible(false);
			addShortBtn.setVisible(false);
			removeConnBtn.setVisible(false);
			removePlanetBtn.setVisible(false);
			moveBtn.setVisible(false);
			planetNameTf.setVisible(false);
			startPlanet.setVisible(false);
			xTf.setVisible(false);
			yTf.setVisible(false);
			zTf.setVisible(false);
			connectionsList.setVisible(false);
			noPlanetLbl.setVisible(true);
		}else{
			removeConnBtn.setEnabled(false);
			planetNameLbl.setVisible(true);
			xLbl.setVisible(true);
			yLbl.setVisible(true);
			zLbl.setVisible(true);
			connectionLbl.setVisible(true);
			updateBtn.setVisible(true);
			addLongBtn.setVisible(true);
			addShortBtn.setVisible(true);
			removeConnBtn.setVisible(true);
			removePlanetBtn.setVisible(true);
			moveBtn.setVisible(true);
			planetNameTf.setVisible(true);
			startPlanet.setVisible(true);
			xTf.setVisible(true);
			yTf.setVisible(true);
			zTf.setVisible(true);
			connectionsList.setVisible(true);
			noPlanetLbl.setVisible(false);
			// set data
			planetNameTf.setText(selectedPlanet.getName());
			startPlanet.setSelected(selectedPlanet.isPosssibleStartplanet());
			String tmpX = String.valueOf(Math.round(selectedPlanet.getXcoor()));
			String tmpY = String.valueOf(Math.round(selectedPlanet.getYcoor()));
			String tmpZ = String.valueOf(Math.round(selectedPlanet.getZcoor()));
			xTf.setText(tmpX);
			yTf.setText(tmpY);
			zTf.setText(tmpZ);
			// set all connections
			remove(connectionsList);
		    connectionsList = new ListPanel();
			connectionsList.setBounds(10,130,220,84);
			connectionsList.setListSelectionListener(this);
			add(connectionsList);
			// fill list with data
			DefaultListModel dlm = (DefaultListModel)connectionsList.getModel();
		    dlm.clear();
		    List<PlanetConnection> allConnections = theMap.getConnections();
			for (PlanetConnection aConnection : allConnections) {
				Planet p1 = aConnection.getPlanet1();
				Planet p2 = aConnection.getPlanet2();
				String rangeStr = "short";
				if (aConnection.isLongRange()){
					rangeStr = "long";
				}
				if (p1 == selectedPlanet){
					dlm.addElement(p2.getName() + " (" + rangeStr + ")");
				}else
				if (p2 == selectedPlanet){
					dlm.addElement(p1.getName() + " (" + rangeStr + ")");
				}
			}
	        connectionsList.updateScrollList();
			repaint();
		}
	}
	
	public void enableBtnsWhileMapAction(boolean enable){
		addLongBtn.setEnabled(enable);
		addShortBtn.setEnabled(enable);
		removePlanetBtn.setEnabled(enable);
		moveBtn.setEnabled(enable);
		if (enable){
			removeConnBtn.setEnabled(removeConnState);
		}else{
			// save buttons state
			removeConnState = removeConnBtn.isEnabled();
			// disable buttons
			removeConnBtn.setEnabled(false);
		}
	}

	public void paintComponent(Graphics g){
		g.setColor(StyleGuide.colorBackground);
		g.fillRect(0,0,getSize().width,getSize().height);
		if (selectedPlanet != null){
			g.setColor(StyleGuide.colorNeutral.darker().darker());
			g.drawRect(5,5,getSize().width-11,93);
		}
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == moveBtn){
			guiPanel.setMovePlanet(selectedPlanet);
		}else
		if ((ae.getSource() == updateBtn) | (ae.getSource() == planetNameTf) | (ae.getSource() == xTf) | (ae.getSource() == yTf) | (ae.getSource() == zTf)){
			selectedPlanet.setName(planetNameTf.getText());
			selectedPlanet.setX(Double.parseDouble(xTf.getText()));
			selectedPlanet.setY(Double.parseDouble(yTf.getText()));
			selectedPlanet.setZ(Double.parseDouble(zTf.getText()));
			guiPanel.updateMap();
		}else
		if (ae.getSource() == removePlanetBtn){
			guiPanel.deletePlanet(selectedPlanet);
			showPlanet(null);
			repaint();
		}else
		if (ae.getSource() == addShortBtn){
			guiPanel.setShortRangeConnection();
		}else
		if (ae.getSource() == addLongBtn){
			guiPanel.setLongRangeConnection();
		}else
		if (ae.getSource() == removeConnBtn){
			String selStr = (String)connectionsList.getSelectedItem();
			int index = selStr.indexOf(" (");
			selStr = selStr.substring(0,index);
			System.out.println("selStr: " + selStr);
			Planet aPlanet = theMap.findPlanet(selStr);
			theMap.deleteConnection(selectedPlanet,aPlanet);
			guiPanel.updateMap();
			showPlanet(selectedPlanet);
			removeConnBtn.setEnabled(false);
			repaint();
		}else
		if(ae.getSource() == startPlanet){
			selectedPlanet.setPosssibleStartplanet(startPlanet.isSelected());
		}
	}

	public void valueChanged(ListSelectionEvent lse) {
		removeConnBtn.setEnabled(true);
	}

	public void keyPressed(KeyEvent arg0) {}

	public void keyReleased(KeyEvent arg0) {
		selectedPlanet.setName(planetNameTf.getText());
		guiPanel.updateMap();
	}

	public void keyTyped(KeyEvent arg0) {}
	
}
