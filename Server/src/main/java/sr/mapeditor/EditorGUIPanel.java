/*
 * Created on 2005-jun-18
 */
package sr.mapeditor;

import java.awt.Color;

import javax.swing.JPanel;

import sr.client.StyleGuide;
import sr.world.Map;
import sr.world.Planet;

/**
 * @author WMPABOD
 *
 * This panel contains all visible components of the map editor applet, and the 
 * logic concerning those components.
 * This panel covers the complete area of the applet.
 * 
 */
@SuppressWarnings("serial")
public class EditorGUIPanel extends JPanel {
	private MapPanel mp;
	private EmblemPanel ep;
	private MapControlsPanel mcp;
	private ButtonsPanel bp;
	private MapDataPanel mdp;
	private PlanetDataPanel pdp;
	private MapActionPanel map;
	private Map theMap;
	/**
	 * Set to true when the user has pressed the create new planet button which 
	 * expect user interaction on the map.
	 */
	private boolean createNewPlanet;
	/**
	 * Contains the planet to move
	 */
	private Planet movePlanet =  null;
	private boolean createShortConnection,createLongConnection;

	public EditorGUIPanel(MapEditorApplet mea, Map aMap){
		this.theMap = aMap;
		
	    // create panels
		setLayout(null);
		setBackground(Color.black);
		StyleGuide.reset();
		
		mp = new MapPanel(theMap,this);
	    int mapx = 128;
	    int mapy = 1;
	    int mapw = 558;
	    int maph = 558;
	    mp.setBounds(mapx,mapy,mapw,maph);
		add(mp);
		
		ep = new EmblemPanel();
	    ep.setBounds(1,1,126,126);
	    add(ep);

		mcp = new MapControlsPanel(mp);
	    mcp.setBounds(1,128,126,181);
	    add(mcp);
	    
	    map = new MapActionPanel(this);
	    map.setBounds(1,310,126,89);
	    add(map);

		bp = new ButtonsPanel(mea);
	    bp.setBounds(1,400,126,159);
	    if (mea.getMapFileName() != null){
	    	bp.activateSaveBtns();
	    }
	    add(bp);

		mdp = new MapDataPanel(this,theMap);
	    mdp.setBounds(688,1,241,184);
	    add(mdp);
	    
	    pdp = new PlanetDataPanel(this,theMap);
	    pdp.setBounds(688,186,241,373);
	    add(pdp);
	}
	
	public void updateMapData(){
		mdp.updateMapData();
	}

	public void updateVersion(){
		mdp.updateVersion();
	}

	public boolean getCreateNewPlanet(){
		return createNewPlanet;
	}
	
	public boolean getMovePlanet(){
		return movePlanet != null;
	}
	
	public void createNewPlanet(int x, int y){
		Planet newPlanet = theMap.createNewPlanet(x,y);
		enableBtnsWhileMapAction(true);
		pdp.showPlanet(newPlanet);
		mp.setChosenPlanet(newPlanet);
		mp.repaint();
		createNewPlanet = false;
		map.setMapActionText(null);
		pdp.setFocusOnName();
	}
	
	public void movePlanet(int x, int y){
		movePlanet.setX(x);
		movePlanet.setY(y);
		pdp.showPlanet(movePlanet);
		mp.repaint();
		movePlanet = null;
		map.setMapActionText(null);
		enableBtnsWhileMapAction(true);
	}
	
	public void selectPlanet(Planet selectedPlanet){
//		System.out.println("selectPlanet: " + selectedPlanet.getName());
		pdp.showPlanet(selectedPlanet);
	}
	
	public void setMovePlanet(Planet aPlanet){
		movePlanet = aPlanet;
		enableBtnsWhileMapAction(false);
		map.setMapActionText("Select;New position");
	}
	
	public void setCreateNewPlanet(boolean newValue){
		createNewPlanet = newValue;
		enableBtnsWhileMapAction(false);
		map.setMapActionText("Place;New planet");
	}

	public void addShortConnection(Planet selectedPlanet){
		createShortConnection = false;
		map.setMapActionText(null);
		enableBtnsWhileMapAction(true);
		pdp.showPlanet(selectedPlanet);
	}

	public void addLongConnection(Planet selectedPlanet){
		createLongConnection = false;
		map.setMapActionText(null);
		enableBtnsWhileMapAction(true);
		pdp.showPlanet(selectedPlanet);
	}

	public void deletePlanet(Planet aPlanet){
		mp.setChosenPlanet(null);
		theMap.removePlanet(aPlanet);
		mp.repaint();
	}
	
	public void updateMap(){
		mp.repaint();
	}
	
	public void setShortRangeConnection(){
		createShortConnection = true;
		enableBtnsWhileMapAction(false);
		map.setMapActionText("Select;Destination");
	}
	
	public void setLongRangeConnection(){
		createLongConnection = true;
		enableBtnsWhileMapAction(false);
		map.setMapActionText("Select;Destination");
	}
	
	public boolean getCreateShortConnection(){
		return createShortConnection;
	}
	
	public boolean getCreateLongConnection(){
		return createLongConnection;
	}
	
	public void clearMapActions(){
		createShortConnection = false;
		createLongConnection = false;
		createNewPlanet = false;
		movePlanet = null;
		enableBtnsWhileMapAction(true);
	}
	
	private void enableBtnsWhileMapAction(boolean enable){
		bp.enableBtnsWhileMapAction(enable);
		pdp.enableBtnsWhileMapAction(enable);
		mdp.enableBtnsWhileMapAction(enable);
	}
	
/*
	public String getMapFileName(){
		return mea.getMapFileName();
	}
*/	
	public void setMapFileName(String newFileName){
		mdp.setMapName(newFileName);
	}
}
