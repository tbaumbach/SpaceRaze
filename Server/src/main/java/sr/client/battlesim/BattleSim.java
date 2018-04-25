/*
 * Created on 2005-maj-05
 */
package sr.client.battlesim;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import sr.general.logging.Logger;
import sr.server.GalaxyCreator;
import sr.server.GalaxyUpdater;
import sr.world.Faction;
import sr.world.Galaxy;
import sr.world.GameWorld;
import sr.world.Map;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.SpaceshipType;
import sr.world.TaskForce;
import sr.world.VIP;

/**
 * @author WMPABOD
 *
 * This class is used to simulate SpaceRaze battles.
 * This is a client-side version of the battle sim. 
 * It cannot use any resources on the server.
 * 
 * This class can be used to:
 * -(static)check if a string containing ship data is correct
 * -create a BattleSim object (params: gameworld,simListener)
 * -start the simulations
 * -stop the simulations
 * 
 */
public class BattleSim extends Thread{
	private double tf1wins;
	private double tf2wins;
	private String message;
	private static int totalCombatNr = 5000;
	private static int maximumNrShips = 1000;
	private static int sleep = 0;
//	private String gameWorldName;
	private final String TF1HOMEPLANET_NAME = "tf1home";
	private final String TF2HOMEPLANET_NAME = "tf2home";
	private final String BATTLEPLANET_NAME = "battleplanet";
	private BattleSimListener battleSimListener;
	private GameWorld gameWorld;
	private Map battleSimMap;
	private String tf1ships,tf2ships;
	
	public BattleSim(BattleSimListener aBattleSimListener,GameWorld aGameWorld) {
		this.battleSimListener = aBattleSimListener;
		this.gameWorld = aGameWorld;
		battleSimMap = createMap();
	}
/*	
	public BattleSim(double tf1wins, double tf2wins,String message,int tf1CostBuy,int tf1CostSupply,int tf2CostBuy,int tf2CostSupply) {
		this.tf1wins = tf1wins;
		this.tf2wins = tf2wins;
		this.message = message;
		this.tf1CostBuy = tf1CostBuy; 
		this.tf1CostSupply = tf1CostSupply;
		this.tf2CostBuy = tf2CostBuy;
		this.tf2CostSupply = tf2CostSupply;
	}
*/
	
    private static String addAShip(String typeName,TaskForce tf, Galaxy g, Planet battlePlanet, boolean screened, int techBonus, int kills, int damaged, List<String> vipNames){
    	Logger.finer("BattleSim addAShip: " + typeName + " " + screened + " " + techBonus + " " + vipNames.size());
    	String message = null;
    	SpaceshipType sst = g.findSpaceshipType(typeName);
    	if (sst == null){
    		// try to find by short name
    		sst = g.findSpaceshipTypeShortName(typeName);
    	}
    	if (sst != null){
    		Spaceship ss = sst.getShip(null,techBonus,0);
    		ss.setLocation(battlePlanet);
    		ss.setOwner(tf.getPlayer());
//    		ss.setScreened(screened);
    		ss.setKills(kills);
    		if (damaged > 0){
    			ss.setDamage(damaged);
    		}
    		for (String aVipName : vipNames) {
    			Logger.finer("VIP: " + aVipName);
				VIP tempVIP = g.getNewVIPshortName(aVipName);
				tempVIP.setBoss(tf.getPlayer());
    			tempVIP.setLocation(ss);
			}
    		tf.addSpaceship(ss);
    		g.addSpaceship(ss);
    	}else{
    		message = "Cannot find shiptype with name: " + typeName;
    	}
    	return message;
    }
    
    private static String addShips(TaskForce tf, Galaxy g, Planet battlePlanet, String ships){
   		StringTokenizer st = new StringTokenizer(ships,";");
   		String message = null;
   		while(st.hasMoreTokens() & (message == null)){
   			String token = null;
   			try{
   				String aShip = st.nextToken();
   				token = aShip;
   				// set # of ships
   				int multipleShipsEnd = aShip.indexOf("]");
   				int nrShips = 1;
   				if (multipleShipsEnd > -1){
   					// multiple instances of ships should be created
   					String nrString = aShip.substring(1,multipleShipsEnd);
   					aShip = aShip.substring(multipleShipsEnd+1,aShip.length());
   					Logger.finer("nrString: " + nrString);
   					nrShips = Integer.parseInt(nrString);
   				}
   				// set VIPS/techbonus/screened
   				int otherAbilitiesStart = aShip.indexOf("(");
   				boolean screened = false;
   				int techBonus = 0;
   				int kills = 0;
   				int damaged = 0;
   				List<String> vipNames = new ArrayList<String>();
   				if (otherAbilitiesStart > -1){
   					// vips/tech exist
   					String oaTemp = aShip.substring(otherAbilitiesStart+1,aShip.length()-1);
   					aShip = aShip.substring(0,otherAbilitiesStart);
   					Logger.finer("oaTemp:  " + oaTemp);
   					StringTokenizer st2 = new StringTokenizer(oaTemp,",");
   					while (st2.hasMoreElements()) {
   						String tempStr = st2.nextToken();
   						Logger.finer(tempStr);
   						if (tempStr.equalsIgnoreCase("s")){
   							// ship is screened
   							screened = true;
   						}else{
   	   						int colonIndex = tempStr.indexOf(":");
   							if (colonIndex > -1){
   								String prefix = tempStr.substring(0,colonIndex);
   								String suffix = tempStr.substring(colonIndex+1);
   								if (prefix.equalsIgnoreCase("t")){
   									// tech bonus for ship
   									techBonus = Integer.parseInt(suffix);
   								}else
   								if (prefix.equalsIgnoreCase("k")){
   									// nr kills for ship
   									kills = Integer.parseInt(suffix);
   								}else
   								if (prefix.equalsIgnoreCase("d")){
   									// ship damage in percent
   									damaged = Integer.parseInt(suffix);
   								}
   							}else{
   								// is a VIP
// 								VIP tempVIP = g.getNewVIPshortName(tempStr);
// 								tempVIP.setBoss(tf.getPlayer());
// 								vips.add(tempVIP);
   								vipNames.add(tempStr);
   							}
   						}
   					}
   				}
   				for (int i = 0; i < nrShips; i++){
   					message = addAShip(aShip,tf,g,battlePlanet,screened,techBonus,kills,damaged,vipNames);
   					
   				}
   			}
   			catch(Exception e){
   				message = "Error when parsing token " + token;
   			}
   		}
   		return message;
    }

    public void simulateBattles(String ships1, String ships2, int nrIterations, int maxNrShips, int sleepTime){
    	this.tf1ships = ships1;
    	this.tf2ships = ships2;
    	totalCombatNr = nrIterations;
    	maximumNrShips = maxNrShips;
    	sleep = sleepTime;
    	start();
    }
    
    public void run(){
    	simulateBattles();
    	battleSimListener.battleSimFinished();
    }
    
    /**
     * 
     * @param faction1
     * @param faction2
     * @param ships1 semicolon-separated list of the ships in faction1:s fleet
     * @param ships2
     */
    public void simulateBattles(){
    	Logger.finer("Battlesim started");
    	Logger.finer("TotalNrBattles: " + totalCombatNr);
    	Logger.setDoOutput(false);
    	int countWinsPlayer1 = 0;
    	int countWinsPlayer2 = 0;
    	for (int i = 1; i <= totalCombatNr; i++){
    		try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
    		if ((i%100) == 0){ // progress count
    			Logger.finest("# " + i + "/" + totalCombatNr);
    		}
    		// create galaxy
    		GalaxyCreator gc = new GalaxyCreator();
    		String faction1 = ((Faction)gameWorld.getFactions().get(0)).getName(); 
    		String faction2 = ((Faction)gameWorld.getFactions().get(1)).getName();
    		Galaxy g = gc.createGalaxy("battleSimGame",battleSimMap,3,gameWorld);
    		// create galaxyUpdater
    		GalaxyUpdater gu = new GalaxyUpdater(g); // testar utan galaxy-instans
    		// create planets
    		Planet battlePlanet = g.findPlanet(BATTLEPLANET_NAME);
    		Planet homePlanet1 = g.findPlanet(TF1HOMEPLANET_NAME);
    		Planet homePlanet2 = g.findPlanet(TF2HOMEPLANET_NAME);
    		// create players
    		Player player1 = new Player("Player1","pass",g,"Gov1",faction1,homePlanet1);
    		Player player2 = new Player("Player2","pass",g,"Gov2",faction2,homePlanet2);
    		// create the task forces
    		TaskForce tf1 = new TaskForce(player1,g);
    		TaskForce tf2 = new TaskForce(player2,g);
    		// add spaceships to task forces
    		message = addShips(tf1,g,battlePlanet,tf1ships);
    		message = addShips(tf2,g,battlePlanet,tf2ships);
    		// perform combat
    		gu.performCombat(tf1,tf2,battlePlanet);
    		if (tf1.getStatus().equalsIgnoreCase("fighting")){
    			countWinsPlayer1++;
    		}else{
    			countWinsPlayer2++;
    		}
        	double tf1wins = (countWinsPlayer1*1.0)/i;
        	double tf2wins = (countWinsPlayer2*1.0)/i;
        	BattleSimResult bsr = new BattleSimResult(tf1wins,tf2wins);
        	bsr.setIterations(i);
        	if (battleSimListener != null){
        		battleSimListener.battleSimPerformed(bsr);
        	}
    	}
    	Logger.finer("Fleet A: " + tf1wins);
    	Logger.finer("Fleet B: " + tf2wins);
    	Logger.setDoOutput(true);
    	Logger.finer("Battlesim finished");
    }

    /**
     * Compute costs of both TFs
     * @param ships1 semicolon-separated list of the ships in TF1:s fleet
     * @param ships2 semicolon-separated list of the ships in TF2:s fleet
     */
    public BattleSimTfCosts getTfCosts(String ships1, String ships2){
    	Logger.finer("getTfCosts called");
    	Logger.finer("ships1: " + ships1);
    	Logger.finer("ships2: " + ships2);
    	Logger.setDoOutput(false);
    	String message = null;
    	// create galaxy
    	GalaxyCreator gc = new GalaxyCreator();
    	String faction1 = ((Faction)gameWorld.getFactions().get(0)).getName(); 
    	String faction2 = ((Faction)gameWorld.getFactions().get(1)).getName();
    	Galaxy g = gc.createGalaxy("battleSimGame",battleSimMap,3,gameWorld);
    	// create planets
    	Planet battlePlanet = g.findPlanet(BATTLEPLANET_NAME);
    	Planet homePlanet1 = g.findPlanet(TF1HOMEPLANET_NAME);
    	Planet homePlanet2 = g.findPlanet(TF2HOMEPLANET_NAME);
    	// create players
    	Player player1 = new Player("Player1","pass",g,"Gov1",faction1,homePlanet1);
    	Player player2 = new Player("Player2","pass",g,"Gov2",faction2,homePlanet2);
    	// create the 1st task force
    	TaskForce tf1 = new TaskForce(player1,g);
    	TaskForce tf2 = new TaskForce(player2,g);
    	// add spaceships to fleets
    	if ((ships1 == null) || (ships1.equals(""))){
    		message = "No ships in fleet A";
    	}else
   		if ((ships2 == null) || (ships2.equals(""))){
   			message = "No ships in fleet B";
   		}else{
   			message = addShips(tf1,g,battlePlanet,ships1);
   			if (message == null){
   				message = addShips(tf2,g,battlePlanet,ships2);
   			}
   		}
    	// count # participating ships
    	int totalShipCount = tf1.getTotalNrShips();
    	totalShipCount = totalShipCount + tf2.getTotalNrShips();
    	Logger.finer("totalShipCount: " + totalShipCount);
    	Logger.finer("maximumNrShips: " + maximumNrShips);
    	if (totalShipCount > maximumNrShips){
    		message = "Total ship count exceeds maximum (" + totalShipCount + " > " + maximumNrShips + ")";
    	}
    	// compute costs (once)
    	BattleSimTfCosts costs = new BattleSimTfCosts();
    	if (message == null){
    		costs.setTf1CostBuy(tf1.getTotalCostBuy());
    		costs.setTf2CostBuy(tf2.getTotalCostBuy());
    		costs.setTf1CostSupply(tf1.getTotalCostSupply());
    		costs.setTf2CostSupply(tf2.getTotalCostSupply());
    	}else{
    		costs.setMessage(message);
    	}
    	Logger.setDoOutput(true);
    	Logger.finer("getTfCosts finished");
    	Logger.fine("Message: " + message);
        return costs;
    }

    private Map createMap(){
    	Map newMap = new Map();
    	Planet tf1Homeplanet = new Planet(-10,0,0,TF1HOMEPLANET_NAME,7,3,true, true);
    	Planet tf2Homeplanet = new Planet(+10,0,0,TF2HOMEPLANET_NAME,7,3,true, true);
    	Planet battlePlanet = new Planet(0,0,0,BATTLEPLANET_NAME,5,5,false, true);
    	newMap.addNewPlanet(tf1Homeplanet);
    	newMap.addNewPlanet(tf2Homeplanet);
    	newMap.addNewPlanet(battlePlanet);
    	newMap.addNewConnection(tf1Homeplanet, battlePlanet, false);
    	newMap.addNewConnection(tf2Homeplanet, battlePlanet, false);
    	return newMap;
    }
    
	public String getTf1wins() {
		return String.valueOf(Math.round(tf1wins*100));
	}
	
	public String getTf2wins() {
		return String.valueOf(Math.round(tf2wins*100));
	}
	
	public String getMessage() {
		return message;
	}

}
