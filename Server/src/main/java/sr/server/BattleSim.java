/*
 * Created on 2005-maj-05
 */
package sr.server;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import sr.general.logging.Logger;
import sr.world.Faction;
import sr.world.Galaxy;
import sr.world.GameWorld;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.SpaceshipType;
import sr.world.StatisticGameType;
import sr.world.TaskForce;
import sr.world.VIP;

/**
 * @author WMPABOD
 *
 * This class is used to simulate SpaceRaze battles.
 */
public class BattleSim {
	private double tf1wins;
	private double tf2wins;
	private String message;
	private static int totalCombatNr = 5000;
	private static int maximumNrShips = 1000;
	private static int sleep = 0;
	private static String gameWorldName;
	private int tf1CostSupply,tf2CostSupply;
	private int tf1CostBuy,tf2CostBuy;
	
	public BattleSim(double tf1wins, double tf2wins,String message,int tf1CostBuy,int tf1CostSupply,int tf2CostBuy,int tf2CostSupply) {
		this.tf1wins = tf1wins;
		this.tf2wins = tf2wins;
		this.message = message;
		this.tf1CostBuy = tf1CostBuy; 
		this.tf1CostSupply = tf1CostSupply;
		this.tf2CostBuy = tf2CostBuy;
		this.tf2CostSupply = tf2CostSupply;
	}

    private static String addAShip(String typeName,TaskForce tf, Galaxy g, Planet battlePlanet, boolean screened, int techBonus, List<String> vipNames){
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
   						int colonIndex = tempStr.indexOf(":");
   						if (tempStr.equalsIgnoreCase("s")){
   							// ship is screened
   							screened = true;
   						}else
   							if (colonIndex > -1){
   								// tech bonus for ship
   								String techBonusString = tempStr.substring(colonIndex+1);
   								techBonus = Integer.parseInt(techBonusString);
   							}else{
   								// is a VIP
// 								VIP tempVIP = g.getNewVIPshortName(tempStr);
// 								tempVIP.setBoss(tf.getPlayer());
// 								vips.add(tempVIP);
   								vipNames.add(tempStr);
   							}
   					}
   				}
   				for (int i = 0; i < nrShips; i++){
   					message = addAShip(aShip,tf,g,battlePlanet,screened,techBonus,vipNames);
   				}
   			}
   			catch(Exception e){
   				message = "Error when parsing token " + token;
   			}
   		}
   		return message;
    }

    public static BattleSim simulateBattles(String ships1, String ships2, int nrIterations, int maxNrShips, int sleepTime, String aGameWorldName){
    	totalCombatNr = nrIterations;
    	maximumNrShips = maxNrShips;
    	sleep = sleepTime;
    	gameWorldName = aGameWorldName;
    	return simulateBattles(ships1,ships2);
    }
    
    /**
     * 
     * @param faction1
     * @param faction2
     * @param ships1 semicolon-separated list of the ships in faction1:s fleet
     * @param ships2
     */
    public static BattleSim simulateBattles(String ships1, String ships2){
    	Logger.finer("Battlesim started");
    	Logger.finer("TotalNrBattles: " + totalCombatNr);
    	Logger.setDoOutput(false);
    	int countWinsPlayer1 = 0;
    	int countWinsPlayer2 = 0;
    	int tf1CostSupply = 0;
    	int tf2CostSupply = 0;
    	int tf1CostBuy = 0;
    	int tf2CostBuy = 0;
    	String message = null;
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
    		GameWorld gw = GameWorldHandler.getGameWorld(gameWorldName);
    		String faction1 = ((Faction)gw.getFactions().get(0)).getName(); 
    		String faction2 = ((Faction)gw.getFactions().get(1)).getName();
    		Galaxy g = gc.createGalaxy("battleSim","test",3,gw,0,0,0,0,StatisticGameType.ALL);
    		// create galaxyUpdater
    		GalaxyUpdater gu = new GalaxyUpdater(g); // testar utan galaxy-instans
    		// create planets
    		Planet battlePlanet = g.findPlanet("nova");
    		Planet homePlanet1 = g.findPlanet("spandrel");
    		Planet homePlanet2 = g.findPlanet("hyperion");
    		// create players
    		Player player1 = new Player("Player1","pass",g,"Gov1",faction1,homePlanet1);
    		Player player2 = new Player("Player2","pass",g,"Gov2",faction2,homePlanet2);
    		// create the 1st task force
    		TaskForce tf1 = new TaskForce(player1,g);
    		TaskForce tf2 = new TaskForce(player2,g);
    		// add spaceships to fleets
    		if ((ships1 == null) || (ships1.equals(""))){
    			message = "No ships in fleet A";
    			break;
    		}else
        		if ((ships2 == null) || (ships2.equals(""))){
        		message = "No ships in fleet B";
        		break;
        	}else{
        		message = addShips(tf1,g,battlePlanet,ships1);
    			if (message == null){
    				message = addShips(tf2,g,battlePlanet,ships2);
    				if (message != null){
    					break;
    				}
    			}else{
    				break;
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
    		if (message != null){
    			break;
    		}
    		// compute costs (once)
    		if (tf1CostSupply == 0){
    			tf1CostBuy = tf1.getTotalCostBuy();
    			tf2CostBuy = tf2.getTotalCostBuy();
    			tf1CostSupply = tf1.getTotalCostSupply();
    			tf2CostSupply = tf2.getTotalCostSupply();
    		}
    		// perform combat
    		gu.performCombat(tf1,tf2,battlePlanet);
    		if (tf1.getStatus().equalsIgnoreCase("fighting")){
    			countWinsPlayer1++;
    		}else{
    			countWinsPlayer2++;
    		}
    	}
    	double tf1wins = (countWinsPlayer1*1.0)/totalCombatNr;
    	double tf2wins = (countWinsPlayer2*1.0)/totalCombatNr;
    	BattleSim bs = new BattleSim(tf1wins,tf2wins,message,tf1CostBuy,tf1CostSupply,tf2CostBuy,tf2CostSupply);
    	if (ships1 != null){
    		Logger.finer("Fleet A: " + tf1wins + " " + ships1);
    		Logger.finer("Fleet B: " + tf2wins + " " + ships2);
    	}
    	Logger.setDoOutput(true);
    	Logger.finer("Battlesim finished");
        return bs;
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

	public int getTf1CostBuy() {
		return tf1CostBuy;
	}

	public int getTf1CostSupply() {
		return tf1CostSupply;
	}

	public int getTf2CostBuy() {
		return tf2CostBuy;
	}

	public int getTf2CostSupply() {
		return tf2CostSupply;
	}
	
}
