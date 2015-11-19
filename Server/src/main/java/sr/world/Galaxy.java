package sr.world;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import sr.campaign.ai.enums.FindPlanetCriterium;
import sr.enums.DiplomacyGameType;
import sr.enums.DiplomacyLevel;
import sr.enums.HighlightType;
import sr.enums.SpaceshipRange;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.server.GalaxyCreator;
import sr.server.GalaxyUpdater;
import sr.world.comparator.PlanetProdComparator;
import sr.world.comparator.PlanetRangeComparator;
import sr.world.diplomacy.Diplomacy;
import sr.world.diplomacy.DiplomacyState;
import sr.world.incomeExpensesReports.IncomeType;
import sr.world.mapinfo.MapPlanetInfo;
import sr.world.orders.Orders;

public class Galaxy implements Serializable{
    static final long serialVersionUID = 1L;
    public int turn, maxNrStartPlanets;
    private int steps; // used to determine how close homeplanets for new players can be
    public boolean gameEnded = false;
    private String mapFileName,gameName,password,mapFullName;
    public List<PublicInfo> publicInfos;
    public List<PlanetConnection> planetConnections;
    public List<Faction> factions;
    public List<VIPType> vipTypes;
    public List<VIP> allVIPs;
    public List<Player> players;
    public List<Planet> planets;
    private List<SpaceshipType> spaceshipTypes;
    private List<Spaceship> spaceships;
    private List<Troop> troops;
    private List<TroopType> troopTypes;
    public UniqueIdCounter uniqueShipIdCounter,uniqueVIPIdCounter,uniqueBlackMarketCounter, uniqueBuildingIdCounter;
    private BlackMarket blackMarket;
    private Date lastUpdated;
    private boolean autoBalance,hasAutoUpdated;
    private long time; // used to determine if and how often a game should update itself
    private boolean lastUpdateComplete = true;
    private StringBuffer lastLog;
    private String startedByPlayer; // contains the login of the player who started this game
    private GameWorld gw;
    private boolean groupSameFaction = false;
    private List<String> selectableFactionNames;
    private boolean randomFaction = false;
    private boolean singlePlayer = false;
    private boolean ranked = false;
    private int singleVictory= 60, factionVictory=65; 
    private int endTurn = 0;
    private int numberOfStartPlanet=1;
    private StatisticsHandler statisticsHandler;
    private Diplomacy diplomacy; // handles all in-game diplomacy
    private List<DiplomacyState> postConfList;
    
    public Galaxy(Map aMap, String gameName, int steps, GameWorld aGameWorld, StatisticGameType statisticGameType){
    	this.mapFileName = aMap.getFileName();
    	this.mapFullName = aMap.getNameFull();
    	this.gameName = gameName;
    	this.steps = steps;
    	this.gw = aGameWorld;
        planets = new LinkedList<Planet>();
        players = new LinkedList<Player>();
        spaceshipTypes = gw.getShipTypes();
        troopTypes = gw.getTroopTypes();
        spaceships = new ArrayList<Spaceship>();
        publicInfos = new LinkedList<PublicInfo>();
        publicInfos.add(new PublicInfo());
        //orbitalWharfs = new Vector();
        planetConnections = new LinkedList<PlanetConnection>();
        vipTypes = gw.getVipTypes();
        allVIPs = new LinkedList<VIP>();
        troops = new ArrayList<Troop>();
        turn = 0;
        uniqueShipIdCounter = new UniqueIdCounter();
        //uniqueWharfIdCounter = new UniqueIdCounter();
        uniqueVIPIdCounter = new UniqueIdCounter();
        uniqueBlackMarketCounter = new UniqueIdCounter();
        uniqueBuildingIdCounter = new UniqueIdCounter();
        diplomacy = new Diplomacy(this);
        factions = gw.getFactions();
        blackMarket = new BlackMarket(uniqueBlackMarketCounter);

        // create map data
        for (Planet aPlanet : aMap.getPlanets()) {
			planets.add(aPlanet.clonePlanet());
		}
        for (PlanetConnection aConnection : aMap.getConnections()) {
			Planet p1 = findPlanet(aConnection.getPlanet1().getName());
			Planet p2 = findPlanet(aConnection.getPlanet2().getName());
			planetConnections.add(new PlanetConnection(p1,p2,aConnection.isLongRange()));
		}
        maxNrStartPlanets = aMap.getMaxNrStartPlanets();
        
        Logger.fine("statisticGameType: " + statisticGameType.toString());
        statisticsHandler = new StatisticsHandler(this,statisticGameType);
    }
    
	public void setranked(boolean branked) {
		this.ranked = branked;
	}
	
	public void setsinglePlayer(boolean bsinglePlayer) {
		this.singlePlayer = bsinglePlayer;
	}
	public boolean getranked() {
		Logger.finer("RANKED GAME: " + ranked);
		return this.ranked;
	}
	
	public boolean getsinglePlayer() {
		return this.singlePlayer;
	}
    
    public String getMapFileName(){
    	return mapFileName;
    }
    
    public void setLastUpdated(Date newDate){
    	lastUpdated = newDate;
    }
    
    
    public void setTime(long newTime){
    	this.time = newTime;
    }
    
    public long getTime(){
    	return time;
    }
    
    public String getLastUpdatedString(){
    	String retStr = "Game has not been updated yet";
    	SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd HH:mm:ss");
    	Date tmpDate = getLastUpdated();
    	if (tmpDate != null){
    		retStr = sdf.format(tmpDate);
    	}
    	return retStr;
    }
    
    public Date getLastUpdated(){
    	return lastUpdated;
    }

    public void setMaxNrStartPlanets(int newValue){
      maxNrStartPlanets = newValue;
    }

    public int getVIPResBonus(Planet aPlanet,Player aPlayer){
      int resBonus = findHighestVIPResistanceBonus(aPlanet,aPlayer);      
      return resBonus;
    }

    public VIP findVIPGovenor(Planet aPlanet, Player aPlayer){
      VIP foundVIP = null;
      int i = 0;
      while((foundVIP == null) & (i < allVIPs.size())){
        VIP tempVIP = (VIP)allVIPs.get(i);
        if ((tempVIP.isGovernor()) & (tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)){
          foundVIP = tempVIP;
        }else{
          i++;
        }
      }
      return foundVIP;
    }

    public VIP findVIPGovenor(Player aPlayer){
      VIP foundVIP = null;
      int i = 0;
      while((foundVIP == null) & (i < allVIPs.size())){
        VIP tempVIP = (VIP)allVIPs.get(i);
        if ((tempVIP.isGovernor()) & (tempVIP.getBoss() == aPlayer)){
          foundVIP = tempVIP;
        }else{
          i++;
        }
      }
      return foundVIP;
    }

    public int findHighestVIPResistanceBonus(Planet aPlanet, Player aPlayer){
      int highestResistanceBonus = 0;
      for (int i = 0; i < allVIPs.size();i++){
        VIP tempVIP = (VIP)allVIPs.get(i);
        if ((tempVIP.hasResistanceBonus()) & (tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)){
        	if (tempVIP.getResistanceBonus() > highestResistanceBonus){
        		highestResistanceBonus = tempVIP.getResistanceBonus();
        	}
        }
      }
      return highestResistanceBonus;
    }

    public VIP findHighestVIPPsychWarfareBonus(Spaceship aShip, Player aPlayer){
      VIP foundVIP = null;
      int highestPsychWarfareBonus = 0;
      for (int i = 0; i < allVIPs.size();i++){
        VIP tempVIP = (VIP)allVIPs.get(i);
        if ((tempVIP.hasPsychWarfareBonus()) & (tempVIP.getBoss() == aPlayer) & (tempVIP.getShipLocation() == aShip)){
        	if (tempVIP.getPsychWarfareBonus() > highestPsychWarfareBonus){
        		highestPsychWarfareBonus = tempVIP.getPsychWarfareBonus();
        		foundVIP = tempVIP;
        	}
        }
      }
      return foundVIP;
    }

    public VIP findHighestVIPAimBonus(Spaceship aShip, Player aPlayer){
        VIP foundVIP = null;
        int highestAimBonus = 0;
        for (int i = 0; i < allVIPs.size();i++){
          VIP tempVIP = (VIP)allVIPs.get(i);
          if ((tempVIP.hasAimBonus()) & (tempVIP.getBoss() == aPlayer) & (tempVIP.getShipLocation() == aShip)){
          	if (tempVIP.getAimBonus() > highestAimBonus){
          		highestAimBonus = tempVIP.getAimBonus();
          		foundVIP = tempVIP;
          	}
          }
        }
        return foundVIP;
    }

    public VIP findVIPEconomicBonus(Planet aPlanet, Player aPlayer){
      VIP foundVIP = null;
      int bonus = 0;      
      for(int i = 0; i < allVIPs.size();i++){
        VIP tempVIP = (VIP)allVIPs.get(i);
        if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)){
        	if (aPlanet.isOpen()){
            	if (tempVIP.getOpenIncBonus() > bonus){
            		foundVIP = tempVIP;
            		bonus = tempVIP.getOpenIncBonus();
            	}        		
        	}else{
            	if (tempVIP.getClosedIncBonus() > bonus){
            		foundVIP = tempVIP;
            		bonus = tempVIP.getClosedIncBonus();
            	}        		
        	}
        }
      }
      return foundVIP;
    }

    public VIP findVIPResistanceBonus(Planet aPlanet, Player aPlayer){
        VIP foundVIP = null;
        int bonus = 0;      
        for(int i = 0; i < allVIPs.size();i++){
        	VIP tempVIP = (VIP)allVIPs.get(i);
        	if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)){
        		if (tempVIP.getResistanceBonus() > bonus){
        			bonus = tempVIP.getResistanceBonus();
        			foundVIP = tempVIP;
        		}
        	}
        }
        return foundVIP;
      }

    public VIP findVIPTechBonus(Planet aPlanet, Player aPlayer, Orders orders){
      VIP foundVIP = null;
      int bonus = 0;
      for(int i = 0; i < allVIPs.size();i++){
        VIP tempVIP = (VIP)allVIPs.get(i);
        if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)){
        	if (tempVIP.getTechBonus() > bonus){
		        if (orders.VIPWillStay(tempVIP)){
		        	foundVIP = tempVIP;
		        	bonus = tempVIP.getTechBonus();
		        }
        	}
        }
      }
      return foundVIP;
    }

    public VIP findVIPShipBuildBonus(Planet aPlanet, Player aPlayer, Orders orders){
        VIP foundVIP = null;
        int bonus = 0;
        for(int i = 0; i < allVIPs.size();i++){
          VIP tempVIP = (VIP)allVIPs.get(i);
          if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)){
          	if (tempVIP.getShipBuildBonus() > bonus){
		        if (orders.VIPWillStay(tempVIP)){
		        	foundVIP = tempVIP;
		        	bonus = tempVIP.getShipBuildBonus();
		        }
          	}
          }
        }
        return foundVIP;
    }
    
    public VIP findVIPTroopBuildBonus(Planet aPlanet, Player aPlayer, Orders orders){
        VIP foundVIP = null;
        int bonus = 0;
        for(int i = 0; i < allVIPs.size();i++){
          VIP tempVIP = (VIP)allVIPs.get(i);
          if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)){
          	if (tempVIP.getTroopBuildBonus() > bonus){
		        if (orders.VIPWillStay(tempVIP)){
		        	foundVIP = tempVIP;
		        	bonus = tempVIP.getTroopBuildBonus();
		        }
          	}
          }
        }
        return foundVIP;
    }
    
    public VIP findVIPBuildingBuildBonus(Planet aPlanet, Player aPlayer, Orders orders){
    	VIP foundVIP = null;
    	int bonus = 0;
    	for(int i = 0; i < allVIPs.size();i++){
    		VIP tempVIP = (VIP)allVIPs.get(i);
    		if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)){
    			if (tempVIP.getBuildingBuildBonus() > bonus){
    		        if (orders.VIPWillStay(tempVIP)){
    		        	foundVIP = tempVIP;
    		        	bonus = tempVIP.getBuildingBuildBonus();
    		        }
    			}
    		}
    	}
    	return foundVIP;
    }
    
    /**
     * 
     * @param aShip a capital ship
     * @param aPlayer
     * @return
     */
    public int findVIPhighestInitBonusCapitalShip(Spaceship aShip, Player aPlayer){
      int initBonus = 0;
      for (int i = 0; i < allVIPs.size(); i++){
        VIP tempVIP = (VIP)allVIPs.get(i);
        if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getShipLocation() == aShip)){
          	if (tempVIP.getInitBonus() > initBonus){
          		initBonus = tempVIP.getInitBonus();
          	}
        }
      }
      return initBonus;
    }

    /**
     * 
     * @param aShip a squadron
     * @param aPlayer
     * @param firstLineOnly
     * @return
     */
    public int findVIPhighestInitBonusSquadron(Spaceship aShip, Player aPlayer){
    	int initSquadronBonus = 0;
    	for (int i = 0; i < allVIPs.size(); i++){
    		VIP tempVIP = (VIP)allVIPs.get(i);
    		if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getShipLocation() == aShip)){
    			if (tempVIP.getInitSquadronBonus() > initSquadronBonus){
    				initSquadronBonus = tempVIP.getInitSquadronBonus();
    			}
    		}
    	}
    	Logger.finer("initSquadronBonus: " + initSquadronBonus);
    	return initSquadronBonus;
    }

    public boolean findVIPcanAttackScreened(Spaceship firingShip){
    	boolean found = false;
    	int index = 0;
    	while ((!found) & index < allVIPs.size()){
    		VIP aVIP = allVIPs.get(index);
    		if (aVIP.getShipLocation() == firingShip){
    			if (firingShip.isSquadron()){
    				if (aVIP.isAttackScreenedSquadron()){
    					found = true;
    				}
    			}else{ // capital ship
    				if (aVIP.isAttackScreenedCapital()){
    					found = true;
    				}
    			}
    		}
    		if (!found){
    			index++;
    		}
    	}
    	return found;
    }

    public int findVIPhighestInitDefence(Spaceship aShip, Player aPlayer){
        int initDefence = 0;
        for (int i = 0; i < allVIPs.size(); i++){
          VIP tempVIP = (VIP)allVIPs.get(i);
          if ((tempVIP.getInitDefence() > 0) & (tempVIP.getBoss() == aPlayer) & (tempVIP.getShipLocation() == aShip)){
          	if (tempVIP.getInitDefence() > initDefence){
          		initDefence = tempVIP.getInitDefence();
          	}
          }
        }
        return initDefence;
      }

    public Spaceship findSurveyShip(Planet aPlanet, Player aPlayer){
    	Spaceship foundShip = null;
    	int i = 0;
    	while((foundShip == null) & (i < spaceships.size())){
    		Spaceship tempShip = spaceships.get(i);
    		if ((tempShip.isPlanetarySurvey()) & (tempShip.getOwner() == aPlayer) & (tempShip.getLocation() == aPlanet)){
    			foundShip = tempShip;
    		}else{
    			i++;
    		}
    	}
    	return foundShip;
    }

    public VIP findSurveyVIPonShip(Planet aPlanet, Player aPlayer){
    	VIP foundVIP = null;
    	int i = 0;
    	while((foundVIP == null) & (i < allVIPs.size())){
            VIP tempVIP = (VIP)allVIPs.get(i);
            if (tempVIP.isPlanetarySurvey() & (tempVIP.getBoss() == aPlayer)){
            	if (tempVIP.getShipLocation() != null){ // VIP is on a ship
            		if (tempVIP.getShipLocation().getLocation() == aPlanet){ // ship is in orbit around aPlanet
            			foundVIP = tempVIP;
            		}
            	}
            }
    		if (foundVIP == null){
    			i++;
    		}
    	}
    	return foundVIP;
    }

    /**
     * Find all other players that have troops on planet aPlanet
     * @param aPlayer Find any other player than aPlayer
     * @param aPlanet the planet 
     * @return a set containing all other players
     */
    public Set<Player> findOtherTroopsPlayersOnRazedPlanet(Player aPlayer,Planet aPlanet){
    	Set<Player> otherPlayers = new HashSet<Player>();
    	// find all troops on aPlanet
    	List<Troop> troopsOnPlanet = findAllTroopsOnPlanet(aPlanet);
    	for (Troop aTroop : troopsOnPlanet) {
    		// if not aPlayer or null
    		Player owner = aTroop.getOwner();
    		if ((owner != aPlayer) & (owner != null)){
    			// add the troops owner to set
    			otherPlayers.add(owner);
    		}
		}
    	return otherPlayers;
    }

    public boolean findDropShip(Planet aPlanet, Player aPlayer){
    	boolean found = false;
    	int i = 0;
    	while(!found & (i < spaceships.size())){
    		Spaceship tempShip = spaceships.get(i);
    		if ((tempShip.getTroopLaunchCapacity() > 0) & (tempShip.getOwner() == aPlayer) & (tempShip.getLocation() == aPlanet)){
    			found = true;
    		}else{
    			i++;
    		}
    	}
    	return found;
    }

    public VIP findVIPSpy(Planet aPlanet, Player aPlayer){
      VIP foundVIP = null;
      int i = 0;
      while((foundVIP == null) & (i < allVIPs.size())){
        VIP tempVIP = (VIP)allVIPs.get(i);
        if ((tempVIP.isSpy()) & (tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)){
          foundVIP = tempVIP;
        }else{
          i++;
        }
      }
      return foundVIP;
    }

    public VIP findVIPAssassin(Planet aPlanet, Player aPlayer){
      VIP foundVIP = null;
      int i = 0;
      while((foundVIP == null) & (i < allVIPs.size())){
        VIP tempVIP = (VIP)allVIPs.get(i);
        if ((tempVIP.isAssassin()) & (tempVIP.getBoss() == aPlayer) & (tempVIP.getPlanetLocation() == aPlanet)){
          foundVIP = tempVIP;
        }else{
          i++;
        }
      }
      return foundVIP;
    }

    public List<VIP> findAllVIPsOnPlanetOrShipsOrTroops(Planet aPlanet){
      List<VIP> tempAllVIPs = new LinkedList<VIP>();
      for (int i = 0; i < allVIPs.size(); i++){
        VIP tempVIP = (VIP)allVIPs.get(i);
        boolean atPlanet = tempVIP.getPlanetLocation() == aPlanet; // finns vipen vid planeten?
        boolean inShipAtPlanet = false;
        boolean inTroopAtPlanet = false;
        if (!atPlanet & (tempVIP.getPlanetLocation() == null)){ //  om inte på planeten...
          Spaceship tempss = tempVIP.getShipLocation();
          if (tempss != null){
        	  if (tempss.getLocation() == aPlanet){  // finns vipen i ett skepp vid planeten?
        		  inShipAtPlanet = true;
        	  }
          }else{ // vip is on troop
        	  if (tempVIP.getTroopLocation().isAtPlanet(aPlanet)){
        		  inTroopAtPlanet = true;
        	  }
          }
        }
        if (atPlanet | inShipAtPlanet | inTroopAtPlanet){
          tempAllVIPs.add(tempVIP);
        }
      }
      return tempAllVIPs;
    }

    public List<VIP> findPlayersVIPsOnPlanetOrShipsOrTroops(Planet aPlanet, Player aPlayer){
    	Logger.info("aPlanet: " + aPlanet);
    	Logger.info("aPlayer: " + aPlayer);
    	Logger.info("aPlayer: " + aPlayer.getName());
    	Logger.finer("findPlayersVIPsOnPlanetOrShipsOrTroops called: " + aPlanet.getName() + " " + aPlayer.getName());
    	List<VIP> tempAllVIPs = findAllVIPsOnPlanetOrShipsOrTroops(aPlanet);
    	List<VIP> vipsAtPlanet = new LinkedList<VIP>();
    	Logger.finer("tempAllVIPs: " + tempAllVIPs);
    	for (VIP vip : tempAllVIPs) {
			if (vip.getBoss() == aPlayer){
				vipsAtPlanet.add(vip);
			}
		}
    	return vipsAtPlanet;
    }

    public List<VIP> findPlayersVIPsOnPlanet(Planet aPlanet, Player aPlayer){
    	Logger.finer("findPlayersVIPsOnPlanet called: " + aPlanet.getName() + " " + aPlayer.getName());
    	List<VIP> tempAllVIPs = findAllVIPsOnPlanetOrShipsOrTroops(aPlanet);
    	List<VIP> vipsAtPlanet = new LinkedList<VIP>();
    	Logger.finer("tempAllVIPs: " + tempAllVIPs);
    	for (VIP vip : tempAllVIPs) {
			if ((vip.getBoss() == aPlayer) & (vip.getPlanetLocation() == aPlanet)){
				vipsAtPlanet.add(vip);
			}
		}
    	return vipsAtPlanet;
    }

    public List<VIP> findAllVIPsOnPlanet(Planet aPlanet){
      Vector<VIP> tempAllVIPs = new Vector<VIP>();
      for (int i = 0; i < allVIPs.size(); i++){
        VIP tempVIP = (VIP)allVIPs.get(i);
        boolean atPlanet = tempVIP.getPlanetLocation() == aPlanet; // finns vipen vid planeten?
        if (atPlanet){
          tempAllVIPs.addElement(tempVIP);
        }
      }
      return tempAllVIPs;
    }
    
    public List<VIP> getAllGovsFromFactionOnPlanet(Planet aPlanet,Faction aFaction){
    	Logger.finer("called for planet: " + aPlanet.getName() + " and faction: " + aFaction.getName());
    	List<VIP> allGovs = new LinkedList<VIP>();
    	List<VIP> allVIPsonPlanet = findAllVIPsOnPlanet(aPlanet);
    	Logger.finest("VIPs found on planet: " + allVIPsonPlanet.size());
        for (int i = 0; i < allVIPsonPlanet.size(); i++){
            VIP tempVIP = (VIP)allVIPsonPlanet.get(i);
        	Logger.finest("VIP found: " + tempVIP.getName());
            if (tempVIP.isGovernor()){
            	Logger.finest("VIP is governor!");
            	Logger.finest(tempVIP.getBoss().getFaction().getName() + " equals " + aFaction.getName() + " ?");
            	if (tempVIP.getBoss().getFaction().equals(aFaction)){
                	Logger.finest("Remove VIP!");
            		allGovs.add(tempVIP);
            	}
            }
          }
    	return allGovs;
    }
    
    public int getNrTroops(Planet aPlanet){
    	int nrTroopsOnPlanet = 0;
    	for (Troop aTroop : troops) {
			if (aTroop.getPlanetLocation() != null){
				if (aTroop.getPlanetLocation() == aPlanet){
					if (aPlanet.getPlayerInControl() == aTroop.getOwner()){
						if (aTroop.getTroopType().isVisible()){
							nrTroopsOnPlanet++;
						}
					}
				}
			}
		}
    	return nrTroopsOnPlanet;
    }

    public List<Troop> findAllTroopsOnShip(Spaceship aShip){
    	List<Troop> troopsOnShip = new LinkedList<Troop>();
    	for (Troop troop : troops) {
			if (troop.getShipLocation() == aShip){
				troopsOnShip.add(troop);
			}
		}
    	return troopsOnShip;
    }

    public List<Troop> findAllTroopsOnPlanet(Planet aPlanet){
    	List<Troop> troopsOnPlanet = new LinkedList<Troop>();
    	for (Troop troop : troops) {
			if (troop.getPlanetLocation() == aPlanet){
				troopsOnPlanet.add(troop);
			}
		}
    	return troopsOnPlanet;
    }

    public List<Troop> findAllDefendingTroopsOnPlanet(Planet aPlanet){
    	Player planetOwner = aPlanet.getPlayerInControl();
    	return findTroopsOnPlanet(aPlanet, planetOwner);
    }

    public List<Troop> findTroopsOnPlanet(Planet aPlanet, Player aPlayer){
    	List<Troop> troopsOnPlanet = new LinkedList<Troop>();
    	for (Troop troop : troops) {
			if (troop.getPlanetLocation() == aPlanet){
				if (troop.getOwner() == aPlayer){
					troopsOnPlanet.add(troop);
				}
			}
		}
    	return troopsOnPlanet;
    }

    /**
     * Find if there are at least one ship at the planet aPlanet that belongs to aPlayer.
     * @param aPlanet
     * @param aPlayer
     * @return if there are at least one dropship at aPlanet belonging to aPlayer
     */
    /*
    public boolean dropShipInOrbit(Planet aPlanet, Player aPlayer){
    	boolean dropShipExist = false;
    	int i = 0;
    	while ((i < spaceships.size()) & !dropShipExist){
    		Spaceship aShip = (Spaceship)spaceships.get(i);
    		if (aShip.getOwner() == aPlayer){
    			if (aShip.getTroopLaunchCapacity() > 0){
    				dropShipExist = true;
    			}else{
    				i++;
    			}
    		}else{
    			i++;
    		}
    	}
    	return dropShipExist;
    }
    */

    public List<VIP> findAllVIPsOnShip(Spaceship aShip){
    	List<VIP> tempAllVIPs = new LinkedList<VIP>();
    	for (VIP tempVIP : allVIPs){
    		if(tempVIP.getShipLocation() != null){
    			Spaceship tempss = tempVIP.getShipLocation();
    			if (tempss == aShip){
    				tempAllVIPs.add(tempVIP);
    			}
    		}
    	}
    	return tempAllVIPs;
    }

    public List<VIP> findAllVIPsOnTroop(Troop aTroop){
        List<VIP> tempAllVIPs = new LinkedList<VIP>();
        for (int i = 0; i < allVIPs.size(); i++){
          VIP tempVIP = (VIP)allVIPs.get(i);
          if(tempVIP.getTroopLocation() != null){
            Troop tempTroop = tempVIP.getTroopLocation();
            if (tempTroop == aTroop){
              tempAllVIPs.add(tempVIP);
            }
          }
        }
        return tempAllVIPs;
      }

    public String getAllBattleSimVipsOnShip(Spaceship aShip){
    	StringBuffer sb = new StringBuffer();
    	List<VIP> vipsOnShip = findAllVIPsOnShip(aShip);
    	List<VIP> battleVips = new LinkedList<VIP>();
    	for (VIP aVIP : vipsOnShip) {
			if (aVIP.isBattleVip()){
				battleVips.add(aVIP);
			}
		}
    	for (VIP aVIP : battleVips) {
			if (sb.length() > 0){
				sb.append(",");
			}
			sb.append(aVIP.getShortName());
		}
    	return sb.toString();
    }

    public String getAllBattleSimVipsOnTroop(Troop aTroop){
    	StringBuffer sb = new StringBuffer();
    	List<VIP> vipsOnTroop = findAllVIPsOnTroop(aTroop);
    	List<VIP> battleVips = new LinkedList<VIP>();
    	for (VIP aVIP : vipsOnTroop) {
			if (aVIP.isLandBattleVIP()){
				battleVips.add(aVIP);
			}
		}
    	for (VIP aVIP : battleVips) {
			if (sb.length() > 0){
				sb.append(",");
			}
			sb.append(aVIP.getShortName());
		}
    	return sb.toString();
    }

    // remove all VIPs belonging to a player. Used primarily (only?) to remove defeated players VIPs
    public void removeVIPs(Player aPlayer){
    	List<VIP> removeVIPs = new LinkedList<VIP>();
    	for (int i = allVIPs.size() - 1; i >= 0; i--){
    		VIP tempVIP = allVIPs.get(i);
    		if(tempVIP.getBoss() == aPlayer){
    			removeVIPs.add(tempVIP);
    		}
    	}
    	for (VIP aVIP : removeVIPs) {
			removeVIP(aVIP);
		}
    }
    
    public void removeVIP(VIP aVIP){
    	allVIPs.remove(aVIP);
    }

    public void checkVIPsInScuttledShips(Spaceship aShip, Player aPlayer){
      List<VIP> allVIPsOnShip = findAllVIPsOnShip(aShip);
      for (VIP tempVIP : allVIPsOnShip){
        allVIPs.remove(tempVIP);
        aPlayer.addToVIPReport("Your " + tempVIP.getName() + " travelling in " + aShip.getName() + " have abandoned your cause when the ship was scuttled.");
        aPlayer.addToHighlights(tempVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
      }
    }

    public void checkVIPsInSelfDestroyedShips(Spaceship aShip, Player aPlayer){
      List<VIP> allVIPsOnShip = findAllVIPsOnShip(aShip);
      for (VIP tempVIP : allVIPsOnShip){
        if (aShip.getLocation() == null){
        	// ship is retreating, vip is killed
            allVIPs.remove(tempVIP);
            aPlayer.addToVIPReport("Your " + tempVIP.getName() + " has been killed when your retreating ship " + aShip.getName() + " was selfdestructed.");
            aPlayer.addToHighlights(tempVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
        }else
        if (aShip.getLocation().getPlayerInControl() == aPlayer){
            // om skeppet är vid en egen planet så flyttar VIPen dit
        	tempVIP.setLocation(aShip.getLocation());
        	aPlayer.addToVIPReport("Your " + tempVIP.getName() + " travelling in " + aShip.getName() + " have moved to the planet " + aShip.getLocation().getName() + " when the ship was selfdestructed.");
        }else{
          // annars om VIPen kan vara på fientliga planeter så flyttar den dit
          if (tempVIP.canVisitEnemyPlanets()){
            tempVIP.setLocation(aShip.getLocation());
            aPlayer.addToVIPReport("Your " + tempVIP.getName() + " travelling in " + aShip.getName() + " have moved to the planet " + aShip.getLocation().getName() + " when the ship was selfdestructed.");
          }else // annars om det är en neutral planet och VIP en är en guvenör flyttar han dit
          if ((tempVIP.canVisitNeutralPlanets()) & (aShip.getLocation().getPlayerInControl() == null)){
            tempVIP.setLocation(aShip.getLocation());
            aPlayer.addToVIPReport("Your " + tempVIP.getName() + " travelling in " + aShip.getName() + " have moved to the planet " + aShip.getLocation().getName() + " when the ship was selfdestructed.");
          }else{  // annars dör VIPen
            allVIPs.remove(tempVIP);
            aPlayer.addToVIPReport("Your " + tempVIP.getName() + " has been killed when your ship " + aShip.getName() + " was selfdestructed at " + aShip.getLocation().getName() + ".");
            aPlayer.addToHighlights(tempVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
          }
        }
      }
    }

    public void checkVIPsInSelfDestroyedTroops(Troop aTroop, Player aPlayer){
        List<VIP> allVIPsOnTroop = findAllVIPsOnTroop(aTroop);
        for (VIP aVip : allVIPsOnTroop) {
        	TurnInfo ti = aVip.getBoss().getTurnInfo();
        	// troop is aboard ship -> move VIP to ship
        	if (aTroop.getShipLocation() != null){
        		ti.addToLatestGeneralReport(aVip.getName() + " has been forced to move when " + aTroop.getUniqueName() + " was selfdestructed.");
        		aVip.moveVIP(aTroop.getShipLocation(), ti);
        	}else{ // troop is on planet
        		Planet thePlanet = aTroop.getPlanetLocation();
        		// own planet -> move VIP to planet
        		if (thePlanet.getPlayerInControl() == aVip.getBoss()){
            		ti.addToLatestGeneralReport(aVip.getName() + " has been forced to move when " + aTroop.getUniqueName() + " was selfdestructed.");
            		aVip.moveVIP(thePlanet, ti);
        		}else
        		if (thePlanet.getPlayerInControl() == null){
        			// neutral planet
        			if (aVip.canVisitNeutralPlanets()){
        				// VIP can visit neutral planets -> move VIP to planet
                		ti.addToLatestGeneralReport(aVip.getName() + " has moved from " + aTroop.getUniqueName() + " to " + thePlanet.getName()) ;
                		ti.addToLatestVIPReport(aVip.getName() + " has been forced to move to the planet " + thePlanet.getName() + " when your troop " + aTroop.getUniqueName() + " was selfdestructed.");
        				aVip.setLocation(thePlanet);
        			}else{
        				// otherwise VIP is killed
        				allVIPs.remove(aVip);
        				aPlayer.addToVIPReport("Your " + aVip.getName() + " has been killed when your troop " + aTroop.getUniqueName() + " was selfdestructed at " + thePlanet.getName() + ".");
        				aPlayer.addToHighlights(aVip.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
        			}
        		}else{
        			// enemy planet 
        			if (aVip.canVisitEnemyPlanets()){
        				// VIP can visit enemy planets -> move VIP to planet
                		ti.addToLatestGeneralReport(aVip.getName() + " has moved from " + aTroop.getUniqueName() + " to " + thePlanet.getName()) ;
                		ti.addToLatestVIPReport(aVip.getName() + " has been forced to move to the planet " + thePlanet.getName() + " when your troop " + aTroop.getUniqueName() + " was selfdestructed.");
        				aVip.setLocation(thePlanet);
        			}else{
        				// otherwise VIP is killed
        				allVIPs.remove(aVip);
        				aPlayer.addToVIPReport("Your " + aVip.getName() + " has been killed when your troop " + aTroop.getUniqueName() + " was selfdestructed at " + thePlanet.getName() + ".");
        				aPlayer.addToHighlights(aVip.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
        			}
        		}
        	}
        }
    }

    public void checkVIPsInTroopsInSelfDestroyedShips(Troop aTroop, Player aPlayer){
        List<VIP> allVIPsOnTroop = findAllVIPsOnTroop(aTroop);
        for (VIP aVip : allVIPsOnTroop) {
        	TurnInfo ti = aVip.getBoss().getTurnInfo();
        	Spaceship carrier = aTroop.getShipLocation();
        	// troop is aboard retreating ship -> destroy VIP
        	if (carrier.isRetreating()){
        		ti.addToLatestGeneralReport(aVip.getName() + " has been lost when " + aTroop.getUniqueName() + " was destructed in a selfdestructing ship (" + carrier.getName() + ").");
				allVIPs.remove(aVip);
				aPlayer.addToHighlights(aVip.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
        	}else{ // troops carrier is at a planet
        		Planet thePlanet = carrier.getLocation();
        		// own planet -> move VIP to planet
        		if (thePlanet.getPlayerInControl() == aVip.getBoss()){
            		ti.addToLatestGeneralReport(aVip.getName() + " has been forced to move when " + aTroop.getUniqueName() + " was destructed in a selfdestructing ship (" + carrier.getName() + ").");
            		aVip.moveVIP(thePlanet, ti);
        		}else
        		if (thePlanet.getPlayerInControl() == null){
        			// neutral planet
        			if (aVip.canVisitNeutralPlanets()){
        				// VIP can visit neutral planets -> move VIP to planet
                		ti.addToLatestGeneralReport(aVip.getName() + " has moved from " + aTroop.getUniqueName() + " to " + thePlanet.getName()) ;
                		ti.addToLatestVIPReport(aVip.getName() + " has been forced to move to the planet " + thePlanet.getName() + " when your troop " + aTroop.getUniqueName() + " was destructed in a selfdestructing ship (" + carrier.getName() + ").");
        				aVip.setLocation(thePlanet);
        			}else{
        				// otherwise VIP is killed
        				allVIPs.remove(aVip);
        				aPlayer.addToVIPReport("Your " + aVip.getName() + " has been killed when your troop " + aTroop.getUniqueName() + " was destructed in a selfdestructing ship (" + carrier.getName() + ").");
        				aPlayer.addToHighlights(aVip.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
        			}
        		}else{
        			// enemy planet 
        			if (aVip.canVisitEnemyPlanets()){
        				// VIP can visit enemy planets -> move VIP to planet
                		ti.addToLatestGeneralReport(aVip.getName() + " has moved from " + aTroop.getUniqueName() + " to " + thePlanet.getName()) ;
                		ti.addToLatestVIPReport(aVip.getName() + " has been forced to move to the planet " + thePlanet.getName() + " when your troop " + aTroop.getUniqueName() + " was selfdestructed.");
        				aVip.setLocation(thePlanet);
        			}else{
        				// otherwise VIP is killed
        				allVIPs.remove(aVip);
        				aPlayer.addToVIPReport("Your " + aVip.getName() + " has been killed when your troop " + aTroop.getUniqueName() + " was destructed in a selfdestructing ship (" + carrier.getName() + ").");
        				aPlayer.addToHighlights(aVip.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
        			}
        		}
        	}
        }
    }

    public void checkVIPsInDestroyedShips(Spaceship aShip, Player aPlayer){
      List<VIP> allVIPsOnShip = findAllVIPsOnShip(aShip);
      for (int i = 0; i < allVIPsOnShip.size(); i++){
        VIP tempVIP = allVIPsOnShip.get(i);
		// if VIP is hard to kill he moves to the nearby planet
        if (tempVIP.isHardToKill()){
          tempVIP.setLocation(aShip.getLocation());
          aPlayer.addToVIPReport("Your " + tempVIP.getName() + " travelling in " + aShip.getName() + " have moved to the planet " + aShip.getLocation().getName() + " when the ship was destroyed.");
        }else{  // annars dör VIPen
          allVIPs.remove(tempVIP);
          aPlayer.addToVIPReport("Your " + tempVIP.getName() + " has been killed when your ship " + aShip.getName() + " was destroyed at " + aShip.getLocation().getName() + ".");
          aPlayer.addToHighlights(tempVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
        }
      }
    }
    
    public void checkTroopsInDestroyedShips(Spaceship aShip, Player aPlayer){
    	List<Troop> troopList = getTroopsOnShip(aShip);
    	for (Troop aTroop : troopList) {
    		aPlayer.addToTroopsLostInSpace(aTroop);
    		aTroop.getOwner().addToGeneral("Your troop " + aTroop.getUniqueName() + " has been killed when your ship " + aShip.getName() + " was destroyed at " + aShip.getLocation().getName() + ".");
			aTroop.getOwner().addToHighlights(aTroop.getUniqueName(), HighlightType.TYPE_OWN_TROOP_DESTROYED);
    		removeTroop(aTroop);
    	}
    }

    public void checkVIPsInDestroyedTroop(Troop aTroop){
    	List<VIP> allVIPsOnTroop = findAllVIPsOnTroop(aTroop);
    	Player aPlayer = aTroop.getOwner();
    	for (VIP vip : allVIPsOnTroop) {
    		// if VIP is hard to kill he moves to the nearby planet
    		if (vip.isHardToKill()){
    			if (aTroop.getPlanetLocation() != null){
    				vip.setLocation(aTroop.getPlanetLocation());
        			aPlayer.addToVIPReport("Your " + vip.getName() + " travelling in " + aTroop.getUniqueName() + " have moved to the planet " + aTroop.getPlanetLocation().getName() + " when the ship was destroyed.");
    			}else{ // VIP is on troop on a ship
    				vip.setLocation(aTroop.getShipLocation().getLocation());
        			aPlayer.addToVIPReport("Your " + vip.getName() + " travelling in " + aTroop.getUniqueName() + " have moved to the planet " + aTroop.getShipLocation().getLocation().getName() + " when the ship carrying the troop was destroyed.");
    			}
    		}else{  // annars dör VIPen
    			allVIPs.remove(vip);
    			if (aTroop.getPlanetLocation() != null){
    				aPlayer.addToVIPReport("Your " + vip.getName() + " has been killed when your troop " + aTroop.getUniqueName() + " was destroyed at " + aTroop.getPlanetLocation().getName() + ".");
    			}else{
    				aPlayer.addToVIPReport("Your " + vip.getName() + " has been killed when your troop " + aTroop.getUniqueName() + " was destroyed at " + aTroop.getShipLocation().getLocation().getName() + " when the ship carrying the troop was destroyed.");
    			}
				aPlayer.addToHighlights(vip.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
    		}
    	}
    }

    public void checkVIPsOnAbandonedPlanet(Planet aPlanet, Player aPlayer){
      List<VIP> allVIPsOnPlanet = findAllVIPsOnPlanet(aPlanet);
      for (int i = 0; i < allVIPsOnPlanet.size(); i++){
        VIP tempVIP = allVIPsOnPlanet.get(i);
        if (tempVIP.getBoss() == aPlayer){
          if (!tempVIP.canVisitNeutralPlanets()){
            allVIPs.remove(tempVIP);
            aPlayer.addToVIPReport("Your " + tempVIP.getName() + " has abandoned your cause when your planet " + aPlanet.getName() + " was abandoned.");
            aPlayer.addToHighlights(tempVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
          }
        }
      }
    }

    public void checkVIPsOnRazedPlanet(Planet aPlanet, Player aPlayer){
      List<VIP> allVIPsOnPlanet = findAllVIPsOnPlanet(aPlanet);
      for (int i = 0; i < allVIPsOnPlanet.size(); i++){
        VIP tempVIP = allVIPsOnPlanet.get(i);
        if (tempVIP.getBoss() == aPlayer){
          if (!tempVIP.canVisitEnemyPlanets()){
            allVIPs.remove(tempVIP);
            aPlayer.addToVIPReport("Your " + tempVIP.getName() + " has been killed when the planet " + aPlanet.getName() + " was RAZED.");
            aPlayer.addToHighlights(tempVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
          }
        }
      }
    }

    public void checkGovenorsOnRazedPlanet(Planet aPlanet){
        List<VIP> allVIPsOnPlanet = findAllVIPsOnPlanet(aPlanet);
        for (int i = 0; i < allVIPsOnPlanet.size(); i++){
          VIP tempVIP = allVIPsOnPlanet.get(i);
          if (tempVIP.isGovernor()){
          	allVIPs.remove(tempVIP);
          	tempVIP.getBoss().addToVIPReport("Your " + tempVIP.getName() + " has been killed when the planet " + aPlanet.getName() + " was RAZED.");
//          	aPlayer.addToHighlights(tempVIP.getName(),Highlight.TYPE_OWN_VIP_KILLED);
          }
        }
      }

    public void checkVIPsOnConqueredPlanet(Planet aPlanet, Player aPlayer){
        List<VIP> allVIPsOnPlanet = findAllVIPsOnPlanet(aPlanet);
      for (int i = 0; i < allVIPsOnPlanet.size(); i++){
        VIP tempVIP = allVIPsOnPlanet.get(i);
        if (tempVIP.getBoss() != aPlayer){
          if (!tempVIP.canVisitEnemyPlanets()){
            allVIPs.remove(tempVIP);
            tempVIP.getBoss().addToVIPReport("Your " + tempVIP.getName() + " have been killed when the planet " + aPlanet.getName() + " was conquered.");
            tempVIP.getBoss().addToHighlights(tempVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
            aPlayer.addToVIPReport("An enemy " + tempVIP.getTypeName() + " have been killed when you conquered the planet " + aPlanet.getName() + ".");
            aPlayer.addToHighlights(tempVIP.getName(),HighlightType.TYPE_ENEMY_VIP_KILLED);
          }
        }
      }
    }

    public void checkTroopsOnInfestedPlanet(Planet aPlanet, Player aPlayer){
    	List<Troop> allTroopsOnPlanet = findAllTroopsOnPlanet(aPlanet);
    	for (Troop aTroop : allTroopsOnPlanet) {
    		if (aTroop.getOwner() == aPlanet.getPlayerInControl()){ // if troop belongs to the same player that controls the planet (or is neutral)
    			troops.remove(aTroop);
    			if (aTroop.getOwner() != null){
    				aTroop.getOwner().addToGeneral("Your " + aTroop.getUniqueName() + " have been destroyed when the planet " + aPlanet.getName() + " was infested.");
    			}
//    			aTroop.getOwner().addToHighlights(tempVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
    			aPlayer.addToGeneral("An enemy " + aTroop.getTroopType().getUniqueName() + " have been killed when you infested the planet " + aPlanet.getName() + ".");
//    			aPlayer.addToHighlights(tempVIP.getName(),Highlight.TYPE_ENEMY_VIP_KILLED);
    		}
    	}
    }

    /**
     * Check if there are any abandoned troops on aPlanet that should be destroyed.
     * A troop is considered abandoned if there are no ships belonging to the same 
     * player with drop capacity in orbit around aPlanet.
     * @param aPlanet
     */
    /*public void checkAbandonedTroops(Planet aPlanet){
    	List<Troop> troopsOnPlanet = findAllTroopsOnPlanet(aPlanet);
    	for (Troop aTroop : troopsOnPlanet) {
    		Logger.finer( "aTroop.getOwner() - aPlanet.getPlayerInControl(): " + aTroop.getOwner() + " - " + aPlanet.getPlayerInControl());
    		if (aTroop.getOwner() != aPlanet.getPlayerInControl()){
        		Logger.finer( "aTroop.getOwner() != aPlanet.getPlayerInControl() is true" + aTroop.getUniqueName());
    			boolean dropShipExist = findDropShip(aPlanet, aTroop.getOwner());
    			if (!dropShipExist){
    				removeTroop(aTroop);
    				if (aTroop.getOwner() != null){ // not neutral owner of troop
    					aTroop.getOwner().addToGeneral("Your troop at the planet " + aTroop.getPlanetLocation().getName() + " have been destroyed since it had no support from ships with launch capacity.");
    					aTroop.getOwner().addToHighlights(aTroop.getUniqueName(), HighlightType.TYPE_OWN_TROOP_DESTROYED);
    				}
    				// messsage to planet owner
    				Player planetOwner = aPlanet.getPlayerInControl();
    				if (planetOwner != null){
    					planetOwner.addToGeneral("An enemy troop at the planet " + aTroop.getPlanetLocation().getName() + " have been destroyed since it had no support from ships with launch capacity.");
    					planetOwner.addToHighlights(aTroop.getUniqueName(), HighlightType.TYPE_ENEMY_TROOP_DESTROYED);
    				}
    				// find if there are any troops from other players on planet
    				Set<Player> otherPlayersOnPlanet = findOtherTroopsPlayersOnRazedPlanet(aTroop.getOwner(),aPlanet);
    				for (Player aPlayer : otherPlayersOnPlanet) {
    					if (aPlayer != planetOwner){
    						aPlayer.addToGeneral("An enemy troop at the planet " + aTroop.getPlanetLocation().getName() + " have been destroyed since it had no support from ships with launch capacity.");
    						aPlayer.addToHighlights(aTroop.getUniqueName(), HighlightType.TYPE_ENEMY_TROOP_DESTROYED);
    					}
    				}
    			}
    		}
		}
    }*/

    // check conflicts between good and evil duellists on the same faction or player
    public void checkDuels(){
      for (int i = 0; i < planets.size(); i++){
        Planet aPlanet = (Planet)planets.get(i);
        List<VIP> vipsAtPlanet = findAllVIPsOnPlanet(aPlanet);
        if (vipsAtPlanet.size() > 1){
    		Logger.finest("Check vips at planet " + aPlanet.getName());
        	checkDuelAtPlanet(aPlanet,vipsAtPlanet,0,vipsAtPlanet.size() - 1);
        }
      }
    }

    // check conflicts between good and evil duellists on the same faction or player, at a specific planet
    private void checkDuelAtPlanet(Planet aPlanet, List<VIP> vipsAtPlanet,int lowVIPindex, int highVIPindex){
	  Logger.finer( "checkDuelAtPlanet: (l/h) " + lowVIPindex + " " + highVIPindex);
      VIP lowVIP = vipsAtPlanet.get(lowVIPindex);
      VIP highVIP = vipsAtPlanet.get(highVIPindex);
      // check if the current VIP will fight
      if (isDuellistConflict(aPlanet,lowVIP,highVIP)){ // Fight!
    	// compute who wins
    	int lowChanceToWin = 50;
    	lowChanceToWin = lowChanceToWin + lowVIP.getDuellistSkill();
    	lowChanceToWin = lowChanceToWin - highVIP.getDuellistSkill();
    	if (lowChanceToWin > 95){
    		lowChanceToWin = 95;
    	}else
        if (lowChanceToWin < 5){
        	lowChanceToWin = 5;
        }
    	boolean lowWon = Functions.getD100(lowChanceToWin);
        int loserIndex = -1, winnerIndex = -1;
        if (lowWon){ // low wins
          loserIndex = highVIPindex;
          winnerIndex = lowVIPindex;
        }else{ // high wins
          loserIndex = lowVIPindex;
          winnerIndex = highVIPindex;
        }
        VIP losingVIP = vipsAtPlanet.get(loserIndex);
        VIP winningVIP = vipsAtPlanet.get(winnerIndex);
        winningVIP.incKills();
        allVIPs.remove(losingVIP);
        if (losingVIP.getBoss() == winningVIP.getBoss()){
        	losingVIP.getBoss().addToHighlights(losingVIP.getName(),HighlightType.TYPE_ACCIDENTAL_DUEL);
            losingVIP.getBoss().addToVIPReport("Your " + losingVIP.getName() + " has been killed by your own " + winningVIP.getName() + ".");
            winningVIP.getBoss().addToVIPReport("Your " + winningVIP.getName() + " has killed your own " + losingVIP.getName() + ".");
        }else
        if (losingVIP.getBoss().getFaction() == winningVIP.getBoss().getFaction()){
        	losingVIP.getBoss().addToHighlights(losingVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
        	winningVIP.getBoss().addToHighlights(losingVIP.getName(),HighlightType.TYPE_FRIENDLY_VIP_KILLED);
            losingVIP.getBoss().addToVIPReport("Your " + losingVIP.getName() + " has been killed by a friendly " + winningVIP.getName() + " belonging to Governor " + winningVIP.getBoss().getGovenorName() + ".");
            winningVIP.getBoss().addToVIPReport("Your " + winningVIP.getName() + " has killed a friendly " + losingVIP.getName() + " belonging to Governor " + losingVIP.getBoss().getGovenorName() + ".");
        }else{ // different factions => enemies
        	losingVIP.getBoss().addToHighlights(losingVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
        	winningVIP.getBoss().addToHighlights(losingVIP.getName(),HighlightType.TYPE_ENEMY_VIP_KILLED);
            losingVIP.getBoss().addToVIPReport("Your " + losingVIP.getName() + " has been killed by an enemy " + winningVIP.getName() + ".");
            winningVIP.getBoss().addToVIPReport("Your " + winningVIP.getName() + " has killed an enemy " + losingVIP.getName() + ".");
        }
        if (loserIndex == highVIPindex){
          lowVIPindex = 0;
        }
        highVIPindex = highVIPindex - 1;
        if (lowVIPindex < highVIPindex){
        	checkDuelAtPlanet(aPlanet,vipsAtPlanet,lowVIPindex,highVIPindex);
        }
      }else{ // no fight...
        lowVIPindex = lowVIPindex + 1;
        if (lowVIPindex == highVIPindex){
          lowVIPindex = 0;
          highVIPindex = highVIPindex - 1;
        }
		Logger.finest(lowVIPindex + " " + highVIPindex);
        if (highVIPindex > 0){
        	checkDuelAtPlanet(aPlanet,vipsAtPlanet,lowVIPindex,highVIPindex);
        }
      }
    }

    private boolean isDuellistConflict(Planet aPlanet, VIP VIP1, VIP VIP2){
      boolean fight = false;
      if ((VIP1.getLocation() == aPlanet) & (VIP2.getLocation() == aPlanet)){
          if (VIP1.isDuellist() & VIP2.isDuellist()){
        	  if (VIP1.hatesDuellist(VIP2)){
        		  fight = true;
        	  }else{
        		  // only fight if on opposing sides
//        		  if (VIP1.getBoss().getFaction() != VIP2.getBoss().getFaction()){
           		  if (diplomacy.hostileDuelists(VIP1.getBoss(), VIP2.getBoss(), aPlanet)){ // use diplomacy to determine if they fight
        			  if (VIP1.getAlignment().equals(VIP2.getAlignment())){
        				  if (VIP1.getAlignment().isDuelOwnAlignment()){
        					  fight = true;
        				  }
        			  }else{
    					  fight = true;
        			  }
        		  }
        	  }
          }
      }
      return fight;
    }
/*
    // check conflicts between Dark Jedis from different Factions
    public void checkDarkDarkJediFights(){
      for (int i = 0; i < planets.size(); i++){
        Planet aPlanet = (Planet)planets.elementAt(i);
        checkDarkDarkJediFight(aPlanet,0,allVIPs.size() - 1);
      }
    }

    // check conflicts between Dark Jedis from different Factions
    private void checkDarkDarkJediFight(Planet aPlanet,int lowVIP, int highVIP){
      // check if the current VIP will fight
      if (isDarkDarkJediConflict(aPlanet,(VIP)allVIPs.elementAt(lowVIP),(VIP)allVIPs.elementAt(highVIP))){ // Fight!
        int whoWins = Functions.getRandomInt(1,2);
        int loserIndex = -1, winnerIndex = -1;
        if (whoWins == 1){ // low wins
          loserIndex = highVIP;
          winnerIndex = lowVIP;
        }else{ // high wins
          loserIndex = lowVIP;
          winnerIndex = highVIP;
        }
        VIP losingVIP = (VIP)allVIPs.elementAt(loserIndex);
        VIP winningVIP = (VIP)allVIPs.elementAt(winnerIndex);
        winningVIP.incKills();
        allVIPs.removeElementAt(loserIndex);
        losingVIP.getBoss().addToVIPReport("Your " + losingVIP.getName() + " has been killed by an enemy Dark Jedi.");
        winningVIP.getBoss().addToVIPReport("Your " + winningVIP.getName() + " has killed an enemy Dark Jedi.");
        losingVIP.getBoss().addToHighlights(losingVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
        winningVIP.getBoss().addToHighlights(losingVIP.getName(),Highlight.TYPE_ENEMY_VIP_KILLED);
        if (loserIndex == highVIP){
          lowVIP = 0;
        }
        highVIP = highVIP - 1;
        if (lowVIP < highVIP){
          checkDarkDarkJediFight(aPlanet,lowVIP,highVIP);
        }
      }else{ // no fight...
        lowVIP = lowVIP + 1;
        if (lowVIP == highVIP){
          lowVIP = 0;
          highVIP = highVIP - 1;
        }
        if (highVIP > 0){
          checkDarkDarkJediFight(aPlanet,lowVIP,highVIP);
        }
      }
    }

    private boolean isDarkDarkJediConflict(Planet aPlanet, VIP VIP1, VIP VIP2){
      boolean fight = false;
      if ((VIP1.isDarkJedi()) & (VIP2.isDarkJedi()) & (VIP1.getLocation() == aPlanet) & (VIP2.getLocation() == aPlanet)){
        if (VIP1.getBoss().getFaction() != VIP2.getBoss().getFaction()){
          fight = true;
        }
      }
      return fight;
    }

    // check if any Light Jedis can be detected by enemy protecting Light Jedis
    public void checkLightJediLightJediSpies(){
      for (int i = 0; i < planets.size(); i++){
        Planet aPlanet = (Planet)planets.elementAt(i);
        checkLightJediLightJediSpies(aPlanet,0,allVIPs.size() - 1);
      }
    }

    // check conflicts between Light and Dark Jedis
    private void checkLightJediLightJediSpies(Planet aPlanet,int lowVIP, int highVIP){
      // check if the current VIP will fight
      if (isLightJediSpiesConflict(aPlanet,(VIP)allVIPs.elementAt(lowVIP),(VIP)allVIPs.elementAt(highVIP))){ // Conflict!
        // kolla vilken av Jedina som är på en egen planet
        boolean highIsHome = false;
        VIP aHighVIP = (VIP)allVIPs.elementAt(highVIP);
        Planet highPlanetLocation = aHighVIP.getPlanetLocation();
        if (highPlanetLocation != null){
          if (highPlanetLocation.getPlayerInControl() == aHighVIP.getBoss()){
            highIsHome = true;
          }
        }
        // slumpa om den andra blir upptäckt
        int discovered = Functions.getRandomInt(1,2);
        int loserIndex = -1, winnerIndex = -1;
        if (discovered == 1){ // the other Light Jedi spy is discovered
          if (highIsHome){
            winnerIndex = highVIP;
            loserIndex = lowVIP;
          }else{
            loserIndex = highVIP;
            winnerIndex = lowVIP;
          }
          VIP losingVIP = (VIP)allVIPs.elementAt(loserIndex);
          VIP winningVIP = (VIP)allVIPs.elementAt(winnerIndex);
          allVIPs.removeElementAt(loserIndex);
          losingVIP.getBoss().addToVIPReport("Your " + losingVIP.getName() + " has been discovered by an enemy Light Jedi at " + aPlanet.getName() + " and forced him to abandon his cause and leave this quadrant.");
          winningVIP.getBoss().addToVIPReport("Your " + winningVIP.getName() + " has discovered an enemy Light Jedi at " + aPlanet.getName() + " and forced him to abandon his cause and leave this quadrant.");
          losingVIP.getBoss().addToHighlights(winningVIP.getBoss().getGovenorName(),Highlight.TYPE_OWN_JEDI_LEAVES);
          winningVIP.getBoss().addToHighlights(losingVIP.getBoss().getGovenorName(),Highlight.TYPE_ENEMY_JEDI_LEAVES);
          if (loserIndex == highVIP){
            lowVIP = 0;
          }
          highVIP = highVIP - 1;
          if (lowVIP < highVIP){
            checkLightJediLightJediSpies(aPlanet,lowVIP,highVIP);
          }
        }else{ // update counters as in no fight
          lowVIP = lowVIP + 1;
          if (lowVIP == highVIP){
            lowVIP = 0;
            highVIP = highVIP - 1;
          }
          if (highVIP > 0){
            checkLightJediLightJediSpies(aPlanet,lowVIP,highVIP);
          }
        }
      }else{ // no fight...
        lowVIP = lowVIP + 1;
        if (lowVIP == highVIP){
          lowVIP = 0;
          highVIP = highVIP - 1;
        }
        if (highVIP > 0){
          checkLightJediLightJediSpies(aPlanet,lowVIP,highVIP);
        }
      }
    }

    private boolean isLightJediSpiesConflict(Planet aPlanet, VIP VIP1, VIP VIP2){
      boolean fight = false;
      if ((VIP1.isLightJedi()) & (VIP2.isLightJedi()) & (VIP1.getPlanetLocation() == aPlanet) & (VIP2.getPlanetLocation() == aPlanet) & (VIP1.getBoss().getFaction() != VIP2.getBoss().getFaction())){
        Planet planetLocation1 = VIP1.getPlanetLocation();
        Planet planetLocation2 = VIP2.getPlanetLocation();
        if (planetLocation1 != null){
          if (planetLocation1.getPlayerInControl() == VIP1.getBoss()){
            fight = true;
          }
        }
        if (planetLocation2 != null){
          if (planetLocation2.getPlayerInControl() == VIP2.getBoss()){
            fight = true;
          }
        }
      }
      return fight;
    }
*/
    // check if spies catch any (normal) spies or assassins
    public void checkCounterEspionage(){
      for (int i = 0; i < planets.size(); i++){
        Planet aPlanet = (Planet)planets.get(i);
        List<VIP> vipsAtPlanet = findAllVIPsOnPlanetOrShipsOrTroops(aPlanet);
        if (vipsAtPlanet.size() > 1){
        	checkCounterEspionageAtPlanet(aPlanet,vipsAtPlanet,0,vipsAtPlanet.size() - 1);
        }
      }
    }

    // check if exterminatorsdestroy any alien infestators
    public void checkExtermination(){
		List<VIP> exterminators = null;
		List<VIP> infestators = null;
    	for (int i = 0; i < planets.size(); i++){
    		Planet aPlanet = (Planet)planets.get(i);
    		exterminators = getExterminators(aPlanet);
    		infestators = getInfestators(aPlanet);
    		if ((exterminators.size() > 0) & (infestators.size() > 0)){
    			checkExterminationAtPlanet(aPlanet,infestators,exterminators);
    		}
    	}
    }
    
    private List<VIP> getExterminators(Planet aPlanet){
    	List<VIP> exterminators = new LinkedList<VIP>();
    	for (VIP aVIP : allVIPs) {
			if (aVIP.isExterminator() & (aVIP.getLocation() == aPlanet)){
				if (aPlanet.getPlayerInControl() == aVIP.getBoss()){ // exterminators can only work on own planets
					exterminators.add(aVIP);
				}
			}
		}
    	return exterminators;
    }

    private List<VIP> getInfestators(Planet aPlanet){
    	List<VIP> infestators = new LinkedList<VIP>();
    	for (VIP aVIP : allVIPs) {
			if (aVIP.isInfestator() & (aVIP.getLocation() == aPlanet)){
				infestators.add(aVIP);
			}
		}
    	return infestators;
    }
    
    private void checkExterminationAtPlanet(Planet aPlanet,List<VIP> infestators,List<VIP> exterminators){
    	List<VIP> enemyInfestators;
    	Collections.shuffle(infestators);
    	for (VIP anExt : exterminators) {
    		enemyInfestators = new LinkedList<VIP>();
    		// copy all enemy infestators to enemy list
    		for (VIP anInf : infestators) {
//				if (anInf.getBoss().getFaction() != anExt.getBoss().getFaction()){
				if (diplomacy.hostileExterminator(anInf.getBoss(),anExt.getBoss())){
					enemyInfestators.add(anInf);
				}
			}
			if (enemyInfestators.size() > 0){ // all infs may already be killed or are friendly
				int randomNr = Functions.getRandomInt(1,100);
				if (randomNr <= anExt.getExterminatorSkill()){ // the inf is killed
					int randomIndex = Functions.getRandomInt(0,enemyInfestators.size()-1);
					VIP anInf = enemyInfestators.get(randomIndex);
					anInf.getBoss().addToVIPReport("Your " + anInf.getName() + " has been discovered by an enemy exterminator at " + aPlanet.getName() + " and has been killed.");
					anExt.getBoss().addToVIPReport("Your " + anExt.getName() + " has discovered an enemy " + anInf.getName() + " at " + aPlanet.getName() + " and has killed him.");
					anInf.getBoss().addToHighlights(anInf.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
					anExt.getBoss().addToHighlights(anInf.getName(),HighlightType.TYPE_ENEMY_VIP_KILLED);
					allVIPs.remove(anInf);
					infestators.remove(randomIndex);
				}
			}
		}
    }

    // check if spies catch enemy VIPs on their planets
    private void checkCounterEspionageAtPlanet(Planet aPlanet, List<VIP> vipsAtPlanet, int lowVIP, int highVIP){
      // check if the current VIP will fight
      if (isSpiesConflict(aPlanet,vipsAtPlanet.get(lowVIP),vipsAtPlanet.get(highVIP))){ // Conflict!
        // kolla vilken av Spionerna som är på en egen planet
        VIP aHighVIP = vipsAtPlanet.get(highVIP);
        VIP aLowVIP = vipsAtPlanet.get(lowVIP);
        boolean highIsHome = false;
        if (aHighVIP.isCounterSpy()){
          Planet planetLocation = aHighVIP.getPlanetLocation();
          if (planetLocation != null){
            if (planetLocation.getPlayerInControl() == aHighVIP.getBoss()){
              if (!aLowVIP.isImmuneToCounterEspionage()){
                highIsHome = true;
              }
            }
          }
        }
        // slumpa om den andra blir upptäckt
        int counterEspionageSkill = 0;
        if (highIsHome){
        	counterEspionageSkill = aHighVIP.getCounterEspionage();
          }else{
          	counterEspionageSkill = aLowVIP.getCounterEspionage();
          }
    	boolean discovered = Functions.getD100(counterEspionageSkill);
        int loserIndex = -1, winnerIndex = -1;
        if (discovered){ // the other VIP is discovered
          if (highIsHome){
            winnerIndex = highVIP;
            loserIndex = lowVIP;
          }else{
            loserIndex = highVIP;
            winnerIndex = lowVIP;
          }
          VIP losingVIP = vipsAtPlanet.get(loserIndex);
          VIP winningVIP = vipsAtPlanet.get(winnerIndex);
          allVIPs.remove(losingVIP);
          losingVIP.getBoss().addToVIPReport("Your " + losingVIP.getName() + " has been discovered by an enemy counter-spy at " + aPlanet.getName() + " and has been killed.");
          winningVIP.getBoss().addToVIPReport("Your " + winningVIP.getName() + " has discovered an enemy " + losingVIP.getName() + " at " + aPlanet.getName() + " and has captured and killed him.");
          losingVIP.getBoss().addToHighlights(losingVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
          winningVIP.getBoss().addToHighlights(losingVIP.getName(),HighlightType.TYPE_ENEMY_VIP_KILLED);
          // update counters etc
          if (loserIndex == highVIP){
            lowVIP = 0;
          }
          highVIP = highVIP - 1;
          if (lowVIP < highVIP){
        	  checkCounterEspionageAtPlanet(aPlanet,vipsAtPlanet,lowVIP,highVIP);
          }
        }else{ // update counters as in no fight
          lowVIP = lowVIP + 1;
          if (lowVIP == highVIP){
            lowVIP = 0;
            highVIP = highVIP - 1;
          }
          if (highVIP > 0){
        	  checkCounterEspionageAtPlanet(aPlanet,vipsAtPlanet,lowVIP,highVIP);
          }
        }
      }else{ // no fight...
        lowVIP = lowVIP + 1;
        if (lowVIP == highVIP){
          lowVIP = 0;
          highVIP = highVIP - 1;
        }
        if (highVIP > 0){
        	checkCounterEspionageAtPlanet(aPlanet,vipsAtPlanet,lowVIP,highVIP);
        }
      }
    }

    private boolean isSpiesConflict(Planet aPlanet, VIP VIP1, VIP VIP2){
      boolean isOnOwnPlanet = false;
      if ((VIP1.getPlanetLocation() == aPlanet) & (VIP2.getPlanetLocation() == aPlanet) & diplomacy.hostileCounterSpies(VIP1.getBoss(),VIP2.getBoss())){
        if (VIP1.isCounterSpy()){
          Planet planetLocation = VIP1.getPlanetLocation();
          if (planetLocation != null){
            if (planetLocation.getPlayerInControl() == VIP1.getBoss()){
              if (!VIP2.isImmuneToCounterEspionage()){
                isOnOwnPlanet = true;
              }
            }
          }
        }
        if (VIP2.isCounterSpy()){
          Planet planetLocation = VIP2.getPlanetLocation();
          if (planetLocation != null){
            if (planetLocation.getPlayerInControl() == VIP2.getBoss()){
              if (!VIP1.isImmuneToCounterEspionage()){
                isOnOwnPlanet = true;
              }
            }
          }
        }
      }
      return isOnOwnPlanet;
    }

    // check if any Assassins kill other VIPs that isn't well guarded
    public void checkAssassins(){
    	for (int i = 0; i < planets.size(); i++){
    		Planet aPlanet = (Planet)planets.get(i);
    		List<VIP> vipsAtPlanet = findAllVIPsOnPlanetOrShipsOrTroops(aPlanet);
    		List<VIP> v = Functions.cloneList(vipsAtPlanet);
    		Collections.shuffle(v);
    		if (v.size() > 1){
    			checkAssassins(aPlanet,0,v.size() - 1,v);
    		}
    	}
    	// remove hasKilled from all assassins
    	for (int i = 0; i < allVIPs.size(); i++){
    		VIP aVIP = (VIP)allVIPs.get(i);
    		if (aVIP.isAssassin()){
    			aVIP.clearHasKilled();
    		}
    	}
    }

    // check if assassins kill any enemy VIPs
    private void checkAssassins(Planet aPlanet,int lowVIP, int highVIP, List<VIP> allVIPsOnPlanetRandomized){
      // check if the current VIP will fight
      if (isPossibleAssassinationConflict(aPlanet,(VIP)allVIPsOnPlanetRandomized.get(lowVIP),(VIP)allVIPsOnPlanetRandomized.get(highVIP))){ // Conflict!
        VIP aHighVIP = allVIPsOnPlanetRandomized.get(highVIP);
        VIP aLowVIP = allVIPsOnPlanetRandomized.get(lowVIP);
        boolean highIsAssassin = false;
        if ((aHighVIP.isAssassin()) & (aHighVIP.getLocation() == aPlanet) & (aLowVIP.getLocation() == aPlanet) & (!aLowVIP.isWellGuarded())){
        	highIsAssassin = true;
        }
        // slumpa om den andra blir mördad
        int assassinationSkill = 0;
        if (highIsAssassin){
        	assassinationSkill = aHighVIP.getAssassinationSkill();
        }else{
        	assassinationSkill = aLowVIP.getAssassinationSkill();
        }
        if (assassinationSkill > 95){
        	assassinationSkill = 95;
        }
    	boolean discovered = Functions.getD100(assassinationSkill);
        int loserIndex = -1, winnerIndex = -1;
        if (discovered){ // the other is murdered
          if (highIsAssassin){
            winnerIndex = highVIP;
            loserIndex = lowVIP;
          }else{
            loserIndex = highVIP;
            winnerIndex = lowVIP;
          }
          VIP losingVIP = (VIP)allVIPsOnPlanetRandomized.get(loserIndex);
          VIP winningVIP = (VIP)allVIPsOnPlanetRandomized.get(winnerIndex);
          winningVIP.incKills();
          allVIPs.remove(losingVIP);
          allVIPsOnPlanetRandomized.remove(loserIndex);
          winningVIP.setHasKilled();
          losingVIP.getBoss().addToVIPReport("Your " + losingVIP.getName() + " has been assassinated by an enemy assassin at " + aPlanet.getName() + ".");
          winningVIP.getBoss().addToVIPReport("Your " + winningVIP.getName() + " has assassinated an enemy " + losingVIP.getName() + " at " + aPlanet.getName() + ".");
          losingVIP.getBoss().addToHighlights(losingVIP.getName(),HighlightType.TYPE_OWN_VIP_KILLED);
          winningVIP.getBoss().addToHighlights(losingVIP.getName(),HighlightType.TYPE_ENEMY_VIP_KILLED);
          // update counters etc
          if (loserIndex == highVIP){
            lowVIP = 0;
          }
          highVIP = highVIP - 1;
          if (lowVIP < highVIP){
            checkAssassins(aPlanet,lowVIP,highVIP,allVIPsOnPlanetRandomized);
          }
        }else{ // update counters as in no fight
          lowVIP = lowVIP + 1;
          if (lowVIP == highVIP){
            lowVIP = 0;
            highVIP = highVIP - 1;
          }
          if (highVIP > 0){
            checkAssassins(aPlanet,lowVIP,highVIP,allVIPsOnPlanetRandomized);
          }
        }
      }else{ // no fight...
        lowVIP = lowVIP + 1;
        if (lowVIP == highVIP){
          lowVIP = 0;
          highVIP = highVIP - 1;
        }
        if (highVIP > 0){
          checkAssassins(aPlanet,lowVIP,highVIP,allVIPsOnPlanetRandomized);
        }
      }
    }

    private boolean isPossibleAssassinationConflict(Planet aPlanet,VIP VIP1,VIP VIP2){
      boolean possibleAssassination = false;
      
      if (diplomacy.hostileAssassin(VIP1.getBoss(), VIP2.getBoss())){
        if ((VIP1.isAssassin()) & (!VIP1.getHasKilled()) & (VIP1.getLocation() == aPlanet) & (VIP2.getLocation() == aPlanet) & (!VIP2.isWellGuarded())){
        	if (diplomacy.hostileAssassin(VIP1.getBoss(), VIP2.getBoss())){
        		possibleAssassination = true;
        	}
        }
        if ((VIP2.isAssassin()) & (!VIP2.getHasKilled()) & (VIP2.getLocation() == aPlanet) & (VIP1.getLocation() == aPlanet) & (!VIP1.isWellGuarded())){
        	if (diplomacy.hostileAssassin(VIP1.getBoss(), VIP2.getBoss())){
        		possibleAssassination = true;
        	}
        }
      }
      return possibleAssassination;
    }

    public VIP createRandomVIP(){
//      int randomVIPindex = Functions.getRandomInt(1,vipTypes.size()-1);  // never get index to a govenor vip (index 0)
//      VIPType tempviptype = (VIPType)vipTypes.elementAt(randomVIPindex);
    	VIPType tempviptype = getRandomVIPType();
    	return tempviptype.createNewVIP(true);
    }

    public VIP maybeAddVIP(Player aPlayer){
      VIP aVIP = null;
      int aRandom = Functions.getRandomInt(1,2);
      if (aRandom == 1){
          aVIP = createPlayerVIP(aPlayer);
      }
      return aVIP;
    }
    
    /**
     * Create a VIP for a certain player
     * @param aPlayer the player for whom this VIP will belong
     * @return a VIP compatible with the players faction
     */
    private VIP createPlayerVIP(Player aPlayer){
        Logger.finer("createPlayerVIP: " + aPlayer.getName() + ", alignment=" + aPlayer.getFaction().getAlignment());
        VIP aVIP = null;
        boolean ok = false;
        while (!ok){ // loopa tills det blir en vip som spelaren kan ha
          ok = true;
          aVIP = createRandomVIP();
          Logger.finer(aVIP.getName() + ", alignment=" + aVIP.getAlignment() + ", canHaveVip=" + aPlayer.getFaction().getAlignment().canHaveVip(aVIP.getAlignment().getName()));
          if (!aPlayer.getFaction().getAlignment().canHaveVip(aVIP.getAlignment().getName())){
            ok = false;
          }
        }
        aVIP.setBoss(aPlayer);
        allVIPs.add(aVIP);
        return aVIP;
    }

    @SuppressWarnings("unused")
    private SpaceshipType getSpaceshipTypeByName(String sstname){
      SpaceshipType foundsst = null;
      int i = 0;
      while ((i < spaceshipTypes.size()) & (foundsst == null)){
        SpaceshipType tempsst = (SpaceshipType)spaceshipTypes.get(i);
        if (tempsst.getName().equalsIgnoreCase(sstname)){
          foundsst = tempsst;
        }else{
          i++;
        }
      }
      return foundsst;
    }

    public Spaceship findSpaceship(String ssname, Player owner){
        Spaceship foundss = null;
        int i = 0;
        while ((i < spaceships.size()) & (foundss == null)){
          Spaceship tempss = (Spaceship)spaceships.get(i);
          if (tempss.getName().equalsIgnoreCase(ssname) && tempss.getOwner() == owner){
            foundss = tempss;
          }else{
            i++;
          }
        }
        return foundss;
      }

/*    private SpaceshipType cloneSpaceshipTypeByName(String sstname){
      SpaceshipType foundsst = null;
      int i = 0;
      while ((i < spaceshipTypes.size()) & (foundsst == null)){
        SpaceshipType tempsst = (SpaceshipType)spaceshipTypes.elementAt(i);
        if (tempsst.getName().equalsIgnoreCase(sstname)){
          foundsst = tempsst;
        }else{
          i++;
        }
      }
      return new SpaceshipType(foundsst);
    }
*/
    public List<SpaceshipType> getSpaceshipTypes(){
      return spaceshipTypes;
    }

    public List<Planet> getPlanets(){
      return planets;
    }

    // enklare variant av getPlayer som ej används vid inloggning
    public Faction getFaction(String name){
      int i = 0;
      Faction found = null;
      Faction temp = null;
      while ((i<factions.size()) & (found == null)){
        temp = (Faction)factions.get(i);
        if (temp.isFaction(name)){
          found = temp;
        }else{
          i++;
        }
      }
      return found;
    }

    public Player getPlayer(String name, String password){
        int i = 0;
        Player p = null;
        Logger.finer("getPlayer: " + players.size());
        Player temp = null;
        while ((i<players.size()) & (p == null)){
            temp = (Player)players.get(i);
            if (temp.isPlayer(name,password)){
                p = temp;
            }
            i++;
        }
        if (p == null){
            p = new Player("Couldn't find player");
        }
        return p;
    }

    // enklare variant av getPlayer som ej används vid inloggning
    public Player getPlayer(String name){
    	Logger.finer("name: " + name);
    	int i = 0;
    	Player found = null;
    	Player temp = null;
    	while ((i<players.size()) & (found == null)){
    		temp = (Player)players.get(i);
        	Logger.finer("temp: " + temp.getName());
    		if (temp.isPlayer(name)){
    			found = temp;
    		}else{
    			i++;
    		}
    	}
    	Logger.finer("found: " + found);
    	return found;
    }

    // enklare variant av getPlayer som ej används vid inloggning
    public Player getPlayerByGovenorName(String govenorName){
      int i = 0;
      Player found = null;
      Player temp = null;
      while ((i<players.size()) & (found == null)){
        temp = (Player)players.get(i);
        if (temp.isPlayerByGovenorName(govenorName)){
          found = temp;
        }else{
          i++;
        }
      }
      return found;
    }

    public Player getNewPlayer(String name, String password, String govenorName, String factionName){
    	Logger.finer("getNewPlayer: " + name + " " + password + " " + govenorName);
        Player p;
        if (getNrStartPlanets() == players.size()){
            p = new Player("All starting planets already taken");
        }else{
        	Faction playerFaction = findFaction(factionName);
            Planet homeplanet = getStartPlanet(steps,playerFaction);
            p = createPlayer(name,password,homeplanet,govenorName,factionName);
            Logger.finer("Galaxy.getNewPlayer");
            homeplanet.setPlayerInControl(p);
            Logger.finer("Galaxy.getNewPlayer2");
            removeNeutralShips(homeplanet);
            removeNeutralTroops(homeplanet);
            Logger.finer("Galaxy.getNewPlayer3");
            homeplanet.setHomeplanet(p.getFaction());
            Logger.finer("Galaxy.getNewPlayer4");
// under testning
//homeplanet.reverseVisibility();
            addPlayer(p);
            Logger.finer("Galaxy.getNewPlayer5");
        }
        return p;
    }

    private void removeNeutralShips(Planet homeplanet){
      for (int i = spaceships.size() - 1; i >= 0 ; i--){
        Spaceship tempShip = spaceships.get(i);
        if ((tempShip.getLocation() == homeplanet) & (tempShip.getOwner() == null)){
          spaceships.remove(tempShip);
        }
      }
    }

    private void removeNeutralTroops(Planet homeplanet){
    	List<Troop> troopsToRemove = new LinkedList<Troop>();
    	for (Troop aTroop : troops) {
    		if ((aTroop.getPlanetLocation() == homeplanet) & (aTroop.getOwner() == null)){
    			troopsToRemove.add(aTroop);
    		}
		}
    	for (Troop troop : troopsToRemove) {
			troops.remove(troop);
		}
    }

    public int getNrStartPlanets(){
      return maxNrStartPlanets;
    }
    
    /**
     * Creates a new VIP from a named vipType
     * @param vipTypeName
     * @return
     */
    public VIP getNewVIP(String vipTypeName){
    	VIPType vipType = findVIPType(vipTypeName);
    	VIP tempVIP = vipType.createNewVIP(true);
        allVIPs.add(tempVIP);
    	return tempVIP;
    }

    public VIP getNewVIPshortName(String vipTypeShortName){
    	Logger.finer("getNewVIPshortName: " + vipTypeShortName);
    	VIPType vipType = findVIPTypeShortName(vipTypeShortName);
    	VIP tempVIP = vipType.createNewVIP(true);
        allVIPs.add(tempVIP);
    	return tempVIP;
    }
//  gissar att TurnInfo texten inte visas någon stan?  den är tok fel i alla fall.
    private Player createPlayer(String name, String password, Planet homeplanet, String govenorName, String factionName){
        Player p = new Player(name,password,this, govenorName, factionName, homeplanet);
        p.getTurnInfo().addToLatestGeneralReport("Welcome to this SpaceRaze Game.");
        p.getTurnInfo().addToLatestGeneralReport("You have 1 planet under your control - the planet " + homeplanet.getName() + ".");
        p.getTurnInfo().addToLatestGeneralReport("");
        p.getTurnInfo().addToLatestGeneralReport("This is turn 0, which is while all players join the game.");
        p.getTurnInfo().addToLatestGeneralReport("You will have to wait until it becomes turn 1 before you can play.");
        p.getTurnInfo().addToLatestGeneralReport("");
        
        p.getTurnInfo().addToLatestGeneralReport("You start with 2 Corvettes, 1 Nebulon B Frigate, 1 medium orbital fort and 1 orbital wharf.");
        p.getTurnInfo().addToLatestGeneralReport("");
        // add res bonus to homeplanet
        homeplanet.setRes(homeplanet.getResistance() + p.getFaction().getResistanceBonus());
        
        // clone all buildings type to the player obj
        addBuildingType(p);
//      create all starting buildings for the new player
        List<BuildingType> startBuildingTypes = p.getFaction().getStartingBuildings();
        for (Iterator<BuildingType> iter = startBuildingTypes.iterator(); iter.hasNext();) {
        	BuildingType buildingTemp1 = (BuildingType) iter.next();
        	Logger.info("buildingTemp1.getName(): " + buildingTemp1.getName());
        	BuildingType buildingTemp2 = p.findBuildingType(buildingTemp1.getName());
        	Building buildingTemp = buildingTemp2.getBuilding(homeplanet, this);
        	//buildingTemp.setLocation(homeplanet);
        	//buildingTemp.setOwner(p);
        	Logger.finer("Building added: " + p.getName() + " " + buildingTemp.getBuildingType().getName());
        	homeplanet.addBuilding(buildingTemp);
        	//spaceships.add(buildingTemp);
        }
        
        
        //orbitalWharfs.add(new OrbitalWharf(p.getFaction().getStartingWharfSize(),homeplanet,this));
        //LoggingHandler.finer("Wharf added: " + p.getName() + " " + orbitalWharfs.size());
        // create all spaceshiptypes
        addSpaceshipTypes(p);
        p.addOtherShipTypes(spaceshipTypes);
        // create all starting spaceships for the new player
        List<SpaceshipType> startTypes = p.getFaction().getStartingShipTypes();
        for (SpaceshipType sstTemp1 : startTypes) {
        	SpaceshipType sstTemp2 = p.findSpaceshipType(sstTemp1.getName());
        	Spaceship ssTemp = sstTemp2.getShip(null,p.getFaction().getTechBonus(),0);
        	ssTemp.setLocation(homeplanet);
        	ssTemp.setOwner(p);
        	spaceships.add(ssTemp);
        }
        // clone trooptypes in faction and add to new player
        addTroopTypes(p);
        addOtherTroopTypes(p);
        // create all starting troops for this player
        List<TroopType> startTroopTypes = p.getFaction().getStartingTroops();
        for (TroopType aTroopType : startTroopTypes) {
        	// first get trooptype from player
        	TroopType playerTroopType = p.findTroopType(aTroopType.getUniqueName());
        	// then create new troop
        	Troop aTroop = playerTroopType.getTroop(null, p.getFaction().getTechBonus(), 0);
        	aTroop.setPlanetLocation(homeplanet);
        	aTroop.setOwner(p);
        	troops.add(aTroop);
		}
        // create Govenor VIP
        //VIP tempVip = new VIP(p,homeplanet,(VIPType)vipTypes.elementAt(0));
        Logger.finer("create gov");
//        VIP tempVip = ((VIPType)vipTypes.elementAt(0)).createNewVIP(p,homeplanet);
//        allVIPs.addElement(tempVip);
        allVIPs.add(p.getFaction().getGovernorVIPType().createNewVIP(p, homeplanet, true));
        // create 1 random VIP
//        tempVip = this.createRandomVIP();
//        tempVip.setBoss(p);
        Logger.finer("create player vip");
        for (int i = 0; i < p.getFaction().getNrStartingRandomVIPs(); i++) {
            VIP tempVip = createPlayerVIP(p);
            tempVip.setLocation(homeplanet);
		}
        List<VIPType> playerStartVips = p.getFaction().getStartingVIPTypes();
        for (VIPType aVipType : playerStartVips) {
            VIP tempVip = aVipType.createNewVIP(true);
	        tempVip.setBoss(p);
            tempVip.setLocation(homeplanet);
	        allVIPs.add(tempVip);
		}        
        // create new diplomacy states to all other players (that have already joined this game)
        diplomacy.createInitialDiplomaticRelations(p, gw);
//        GameWorldDiplomacy diplomacy = gw.getDiplomacy();
//        for (Iterator iter = players.iterator(); iter.hasNext();) {
//			Player aPlayer = (Player) iter.next();
//			DiplomacyRelation tmpRelation = diplomacy.getRelation(aPlayer.getFaction(), p.getFaction());
//			DiplomacyState tmpState = new DiplomacyState(tmpRelation.clone(),aPlayer,p);
//			diplomacyStates.add(tmpState);
//		}
        return p;
    }
    
	public SpaceshipType getShipType(String typename){
	  SpaceshipType st = null;
	  List<SpaceshipType> allshiptypes = spaceshipTypes;
	  int i = 0;
	  while (st == null){
	    SpaceshipType temp = (SpaceshipType)allshiptypes.get(i);
	    if (temp.getName().equalsIgnoreCase(typename)){
	      st = temp;
	    }else{
	      i++;
      }
	  }
	  return st;
	}

	public TroopType getTroopType(String typename){
		return getTroopType(typename,true);
	}

	public TroopType getTroopType(String typename, boolean errorDump){
		return gw.getTroopTypeByName(typename,errorDump);
	}

	public TroopType getTroopTypeByShortName(String typeShortName){
		return gw.getTroopTypeByShortName(typeShortName);
	}

	/*
// används under testning
private void createAllVIPs(Player p, Planet homeplanet){
  for (int i = 1; i < vipTypes.size(); i++){
    VIP tempVip = ((VIPType)vipTypes.get(i)).createNewVIP(p,homeplanet);
    allVIPs.add(tempVip);
  }
}
*/
    private void addSpaceshipTypes(Player p){
      List<SpaceshipType> factionSpaceshipTypes = p.getFaction().getSpaceshipTypes();
      //Logger.finer("addSpaceshipTypes: " + p.getName());
      for (int i = 0; i < factionSpaceshipTypes.size(); i++){
    	  Logger.finer("i: " + i + " " + factionSpaceshipTypes.get(i));
    	  p.addSpaceshipType(new SpaceshipType(factionSpaceshipTypes.get(i)));
      }
    }
    
    private void addBuildingType(Player p){
    	p.setBuildings(Functions.deepClone(p.getFaction().getBuildings()));
    }
    

    private void addTroopTypes(Player p){
        List<TroopType> factionTroopTypes = p.getFaction().getTroopTypes();
        Logger.finer("addTroopTypes: " + p.getName());
        for (TroopType aTroopType : factionTroopTypes) {
        	  //Logger.finer("factionTroopTypes: " + aTroopType);
        	  TroopType tmpTT = aTroopType.clone();
          	  p.addTroopType(tmpTT);
		}
    }

    private void addOtherTroopTypes(Player p){
        // add all other trooptypes that the player cannot build
        for (TroopType galaxyTroopType : troopTypes) {
			TroopType tmpPlayerTt = p.findTroopType(galaxyTroopType.getUniqueName());
			if (tmpPlayerTt == null){ // player does not have trooptype already
	        	  TroopType tmpTT = galaxyTroopType.clone();
	          	  p.addOtherTroopType(tmpTT);
			}
		}
    }

//  @SuppressWarnings("unchecked")
  private Planet getStartPlanet(int steps, Faction playerFaction){
	Logger.finer("getStartPlanet steps: " + steps);
    Planet foundPlanet = null;
//    Random r = Functions.getRandom();
    // klona planetlistan
//    List<Planet> tempplanets = (List<Planet>)((LinkedList<Planet>)planets).clone();
   // List<Planet> tempplanets = new LinkedList<Planet>(planets); // byter ut raden ovanför mot att bara skapa en ny lista, tror inte vi behöver klona listan
    List<Planet> tempplanets = getStarPlanets();
    // slumpa om den
    Collections.shuffle(tempplanets);
//    Functions.randomize(tempplanets);
    // if group players, sort the planets with planets close to planets with 
    // players from the same faction first
    if (groupSameFaction){
    	setPlanetRangeToClosestFriendly(tempplanets,playerFaction);
    }
    // loopa igenom planeterna tills listan är slut eller en lämplig hemplanet hittats
    int i = 0;
    while ((i < tempplanets.size()) & (foundPlanet == null)){
      Planet tempPlanet = tempplanets.get(i);
      // kolla om planeten inte redan är en hemplanet
      if (tempPlanet.getPlayerInControl() == null){
        // kolla om en planet har andra hemplaneter inom steps steg
        if (!checkStartplanetsWithinRange(steps,tempPlanet,false) & !checkStartplanetsWithinRange(steps,tempPlanet,true)){
          // om nej -> returnera denna planet
          foundPlanet = tempPlanet;
        }else{ // om ja, gå vidare till nästa
          i++;
        }
      }else{
        i++;
      }
    }
    if ((foundPlanet == null) & (steps > 0)){
      // om inte, returnera resultatet av nytt anrop till getStartPlanet med steps = steps - 1
      foundPlanet = getStartPlanet(steps - 1,playerFaction);
    }
    return foundPlanet;
  }
  
  private List<Planet> getStarPlanets() {
	  LinkedList<Planet>  tempList = new LinkedList<Planet>();
	  for (Planet aPlanet : planets) {
		  if(aPlanet.isPosssibleStartplanet()){
			  Logger.finer("Possible startplanet " + aPlanet.getName());
			  tempList.add(aPlanet);
		  }
	  }  
	// TODO Auto-generated method stub
	return tempList;
}

/**
   * Sort the planet list so that the planets closest to startplanets for
   * players from the same faction comes first.
   * If there are no players already from the same faction nothing is performed.
   * This method should only be called in games where the "groupSameFaction"
   * is set to true.
   * @param allPlanets all planets in the game
   * @param playersFaction the faction of the new player
   */
  private void setPlanetRangeToClosestFriendly(List<Planet> allPlanets, Faction playersFaction){
	  Logger.finer("setPlanetRangeToClosestFriendly faction: " + playersFaction.getName());
	  // count the number of players from the same faction as the player
	  int nrSameFaction = 0;
	  for (Planet planet : allPlanets) {
		  if (planet.getPlayerInControl() != null){
			  if (planet.getPlayerInControl().getFaction() == playersFaction){
				  nrSameFaction++;
			  }
		  }
	  }
	  Logger.finer("nrSameFaction: " + nrSameFaction);
	  // if more than zero players from the same faction
	  if (nrSameFaction > 0){
		  // loop through all planets
		  for (Planet planet : allPlanets) {
			  if (planet.getPlayerInControl() != null){
				  // if planet already is a startplanet, set range to maxint
				  planet.setRangeToClosestFriendly(Integer.MAX_VALUE);
				  Logger.finer("planet max value: " + planet.getName());
			  }else{
				  // sök i grafen tills man hittar en planet som är samma faction, spara hur många 
				  //   steg dit det är i rangeToClosestFriendly
				  int steps = getStepsToClosestFriendly(allPlanets,planet,playersFaction);
				  planet.setRangeToClosestFriendly(steps);
				  Logger.finest("planet: " + planet.getName() + " value: " + planet.getRangeToClosestFriendly());
			  }
		  }
		  // sortera om listan m.ha. rangeToClosestFriendly, lägst först
		  Collections.sort(allPlanets,new PlanetRangeComparator());
		  // trace...
		  Logger.finest("planets sorted");
		  for (Planet planet : allPlanets) {
			  Logger.finest("planet: " + planet.getName() + " value: " + planet.getRangeToClosestFriendly());
		  }
	  }
  }

  private int getStepsToClosestFriendly(List<Planet> planets, Planet aPlanet, Faction playerFaction){
	    boolean planetFound = false;
	    // skapa tom vektor över hittade planeter
	    List<Planet> edgePlanets = new ArrayList<Planet>(); // de planeter som är på gränsen till det genomsökta området
	    edgePlanets.add(aPlanet);
	    List<Planet> newEdgePlanets = new ArrayList<Planet>(); // de planeter som är på gränsen till det genomsökta området
	    List<Planet> searchedPlanets = new ArrayList<Planet>();  // lägg in alla som genomsökts + startplaneten
	    searchedPlanets.add(aPlanet);
	    List<Planet> allNeighbours;
	    int tempSteps = 0;
	    // loopa tills alla planeter har letats igenom, eller tempSteps = steps eller minst 1 startplanet har hittats
	    while ((searchedPlanets.size() < planets.size()) & (!planetFound)){
	      tempSteps++;
	      // Gå igenom alla edgePlanets
	      for (int i = 0; i < edgePlanets.size(); i++){
	        Planet tempPlanet = edgePlanets.get(i);
	        // Hämta alla grannar till tempPlanet, både short & long range
	        allNeighbours = getAllDestinations(tempPlanet,false);
	        // Gå igenom alla allNeighbours  (lägg i newEdgePlanets)
	        for (int j = 0; j < allNeighbours.size(); j++){
	          Planet tempNeighbourPlanet = allNeighbours.get(j);
	          // kolla att tempNeighbourPlanet inte redan finns i searchedPlanets
	          if ((!searchedPlanets.contains(tempNeighbourPlanet)) & (!newEdgePlanets.contains(tempNeighbourPlanet))){
	            // lägg i newEdgePlanets
	            newEdgePlanets.add(tempNeighbourPlanet);
	          }
	        }
	        allNeighbours = getAllDestinations(tempPlanet,true);
	        // Gå igenom alla allNeighbours  (lägg i newEdgePlanets)
	        for (int j = 0; j < allNeighbours.size(); j++){
	          Planet tempNeighbourPlanet = allNeighbours.get(j);
	          // kolla att tempNeighbourPlanet inte redan finns i searchedPlanets
	          if ((!searchedPlanets.contains(tempNeighbourPlanet)) & (!newEdgePlanets.contains(tempNeighbourPlanet))){
	            // lägg i newEdgePlanets
	            newEdgePlanets.add(tempNeighbourPlanet);
	          }
	        }
	      }
	      // Gå igenom newEdgePlanets och kolla om någon av dem är en hemplanet till en spelare från samma faction
	      for (int k = 0; k < newEdgePlanets.size(); k++){
	        Planet tempPlanet = newEdgePlanets.get(k);
	        // kolla om planeten är en startplanet
	        if (tempPlanet.getPlayerInControl() != null){
	        	if (tempPlanet.getPlayerInControl().getFaction() == playerFaction){
	        		// planet hittad! Sätt till true!
	        		planetFound = true;
	        	}
	        }
	      }
	      // töm edgePlanets
	      edgePlanets.clear();
	      // kopiera över newEdgePlanets till edgePlanets
	      for (int l = 0; l < newEdgePlanets.size(); l++){
	        edgePlanets.add((Planet)newEdgePlanets.get(l));
	      }
	      // kopiera över newEdgePlanets till searchedPlanets
	      for (int m = 0; m < newEdgePlanets.size(); m++){
	        searchedPlanets.add((Planet)newEdgePlanets.get(m));
	      }
	      // töm newEdgePlanets
	      newEdgePlanets.clear();
	    }
	    return tempSteps;
	  }

  private boolean checkStartplanetsWithinRange(int steps, Planet aLocation, boolean longRange){
    boolean planetFound = false;
    // skapa tom vektor över hittade planeter
    List<Planet> edgePlanets = new ArrayList<Planet>(); // de planeter som är på gränsen till det genomsökta området
    edgePlanets.add(aLocation);
    List<Planet> newEdgePlanets = new ArrayList<Planet>(); // de planeter som är på gränsen till det genomsökta området
    List<Planet> searchedPlanets = new ArrayList<Planet>();  // lägg in alla som genomsökts + startplaneten
    searchedPlanets.add(aLocation);
    List<Planet> allNeighbours;
    int tempSteps = 0;
    // loopa tills alla planeter har letats igenom, eller tempSteps = steps eller minst 1 startplanet har hittats
    while ((searchedPlanets.size() < planets.size()) & (tempSteps < steps) & (!planetFound)){
      tempSteps++;
      // Gå igenom alla edgePlanets
      for (int i = 0; i < edgePlanets.size(); i++){
        Planet tempPlanet = edgePlanets.get(i);
        // Hämta alla grannar till tempPlanet
        allNeighbours = getAllDestinations(tempPlanet,longRange);
        // Gå igenom alla allNeighbours  (lägg i newEdgePlanets)
        for (int j = 0; j < allNeighbours.size(); j++){
          Planet tempNeighbourPlanet = allNeighbours.get(j);
          // kolla att tempNeighbourPlanet inte redan finns i searchedPlanets
          if ((!searchedPlanets.contains(tempNeighbourPlanet)) & (!newEdgePlanets.contains(tempNeighbourPlanet))){
            // lägg i newEdgePlanets
            newEdgePlanets.add(tempNeighbourPlanet);
          }
        }
      }
      // Gå igenom newEdgePlanets och (och ej belägrade??? kan bara gälla egna planeter)
      for (int k = 0; k < newEdgePlanets.size(); k++){
        Planet tempPlanet = newEdgePlanets.get(k);
        // kolla om planeten är en startplanet
        if (tempPlanet.getPlayerInControl() != null){
          // planet hittad! Returnera true!
          planetFound = true;
        }
      }
      // töm edgePlanets
      edgePlanets.clear();
      // kopiera över newEdgePlanets till edgePlanets
      for (int l = 0; l < newEdgePlanets.size(); l++){
        edgePlanets.add((Planet)newEdgePlanets.get(l));
      }
      // kopiera över newEdgePlanets till searchedPlanets
      for (int m = 0; m < newEdgePlanets.size(); m++){
        searchedPlanets.add((Planet)newEdgePlanets.get(m));
      }
      // töm newEdgePlanets
      newEdgePlanets.clear();
    }
    return planetFound;
  }

/*
    private Planet getStartPlanet(){
        Planet p = null;
        Random r = new Random();
        int dividenr = Math.abs(r.nextInt()%planets.size());
        int i = dividenr;
        while ((i<planets.size()) & (p == null)){
            Planet temp = (Planet)planets.elementAt(i);
            if (temp.isStartPlanet() & (temp.getPlayerInControl() == null)){
                p = temp;
            }else{
              i++;
            }
        }
        if (p == null){
          i = 0;
          while ((i<dividenr) & (p == null)){
            Planet temp = (Planet)planets.elementAt(i);
            if (temp.isStartPlanet() & (temp.getPlayerInControl() == null)){
              p = temp;
            }else{
              i++;
            }
          }
        }
        return p;
    }
  */
    public void addPlayer(Player p){
      players.add(p);
    }

    public int getTurn(){
        return turn;
    }

    // extract Order from Player and insert it into the original Player instance
    // and do the same for PlanetInfos
    public String replacePlayer(Player p){
      Logger.finer("replaceplayer(galaxy): " + p.getName());  // spårutskrift till consolen
      if (p.getErrorMessage() != null){
        return "Insert failed: " + p.getErrorMessage();
      }else{
        Player temp = getPlayer(p.getName(), p.getPassword());
        temp.setOrders(p.getOrders());
        temp.setPlanetInfos(p.getPlanetInfos());
        temp.setNotes(p.getNotes());
        if (turn > 0){
          temp.setUpdatedThisTurn(true);
          temp.setFinishedThisTurn(p.isFinishedThisTurn());
  		Logger.finer("p.isFinishedThisTurn(): " + p.isFinishedThisTurn());
        }
        return "Player data inserted successfully.";
      }
    }

    public Planet findPlanet(String findname){
//    	Logger.fine("findname: " + findname);
    	Planet p = null;
    	int i = 0;
    	while ((p == null) & (i<planets.size())){
    		Planet temp = (Planet)planets.get(i);
//        	Logger.fine("temp.getName(): " + temp.getName());
    		if (temp.getName().equalsIgnoreCase(findname)){
    			p = temp;
    		}else{
    			i++;
    		}
    	}
//    	Logger.fine("return: " + p);
    	return p;
    }

    public Spaceship findSpaceship(String spaceshipName){
    	Spaceship ss = null;
    	int i = 0;
    	while ((ss == null) & (i<spaceships.size())){
    		Spaceship temp = spaceships.get(i);
    		if (temp.getName().equalsIgnoreCase(spaceshipName)){
    			ss = temp;
    		}else{
    			i++;
    		}
    	}
    	return ss;
    }

    public Spaceship findSpaceship(int findid){
      Spaceship ss = null;
      int i = 0;
      while ((ss == null) & (i<spaceships.size())){
        Spaceship temp = spaceships.get(i);
        if (temp.getId() == findid){
          ss = temp;
        }else{
          i++;
        }
      }
      return ss;
    }

    public Troop findTroop(int findid){
        Troop foundTroop = null;
        int i = 0;
        while ((foundTroop == null) & (i<troops.size())){
          Troop temp = troops.get(i);
          if (temp.getId() == findid){
            foundTroop = temp;
          }else{
            i++;
          }
        }
        return foundTroop;
      }

    /* gamla ta bort?
    public Spaceship findSpaceship(String findname){
      Spaceship ss = null;
      int i = 0;
      while ((ss == null) & (i<spaceships.size())){
        Spaceship temp = (Spaceship)spaceships.elementAt(i);
        if (temp.getUniqueName().equalsIgnoreCase(findname)){
          ss = temp;
        }
        i++;
      }
      return ss;
    }
*/
    public SpaceshipType findSpaceshipType(String findname){
      SpaceshipType sst = null;
      int i = 0;
      while ((sst == null) & (i<spaceshipTypes.size())){
        SpaceshipType temp = (SpaceshipType)spaceshipTypes.get(i);
        if (temp.getName().equalsIgnoreCase(findname)){
          sst = temp;
        }
        i++;
      }
      return sst;
    }
    
    public TroopType findTroopType(String ttname){
    	TroopType tt = null;
        int i = 0;
        while ((tt == null) & (i < troopTypes.size())){
        	TroopType aTT = troopTypes.get(i);
			if (aTT.getUniqueName().equals(ttname)){
				tt = aTT;
			}else{
				i++;
			}
		}
    	return tt;
    }

    public SpaceshipType findSpaceshipTypeShortName(String shortfindname){
        SpaceshipType sst = null;
        int i = 0;
        while ((sst == null) & (i<spaceshipTypes.size())){
          SpaceshipType temp = (SpaceshipType)spaceshipTypes.get(i);
          if (temp.getShortName().equalsIgnoreCase(shortfindname)){
            sst = temp;
          }
          i++;
        }
        return sst;
      }

    public List<Player> getSoloConfederacyWinner(){
    	List<Player> confPlayers = new LinkedList<Player>();
    	for (Player aPlayer : players) {
    		if (!aPlayer.isDefeated()){
    			confPlayers.add(aPlayer);
    		}
    	}
    	return confPlayers;
    }

    public boolean checkSoloConfederacyWinner(){
    	boolean singleConfederacyFound = true;
    	for (Player aPlayer1 : players) {
    		if (!aPlayer1.isDefeated()){
    			for (Player aPlayer2 : players) {
    	    		if (!aPlayer2.isDefeated()){
    	    			if (aPlayer1 != aPlayer2){
    	    				if (getDiplomacyState(aPlayer1,aPlayer2).getCurrentLevel() != DiplomacyLevel.CONFEDERACY){
    	    					singleConfederacyFound = false;
    	    				}
    	    			}
    	    		}
    			}
    		}
		}
    	return singleConfederacyFound;
    }

    public boolean playersLeft(){
    	boolean playersExist = false;
    	for (Player aPlayer : players) {
			if (!aPlayer.isDefeated()){
				playersExist = true;
			}
		}
    	return playersExist;
    }
    
    // check if there is only 1 player left in the game and that he has at least 1 planet
    public Player checkSoloPlayerWinner(){
      Player winner = null;
      Player tempPlayer2 = null;
      int tempnr = 0;
      for (int i = 0; i < players.size();i++){
        Player tempPlayer = (Player)players.get(i);
        if (!tempPlayer.isDefeated()){
          tempnr = tempnr + 1;
          tempPlayer2 = tempPlayer;
        }
      }
      if (tempnr == 1){
        // om det bara finns en spelare kvar måste han ha minst 1 planet för att vinna
        boolean atLeastOnePlanet = false;
        for (int j = 0; j < planets.size();j++){
          Planet tempPlanet = (Planet)planets.get(j);
          if (tempPlanet.getPlayerInControl() == tempPlayer2){
            atLeastOnePlanet = true;
          }
        }
        if (atLeastOnePlanet){
          winner = tempPlayer2;
        }else{
          winner = null;
        }
      }
      return winner;
    }
    
    private List<List<Player>> getAllConfederacies(){
    	List<List<Player>> confList = new LinkedList<List<Player>>();
    	List<Player> allFoundConfPlayers = new LinkedList<Player>(); // all players in conf
    	for (Player aPlayer1 : players) {
    		if (!aPlayer1.isDefeated() & !allFoundConfPlayers.contains(aPlayer1)){
    			// find all other players with whom this player is in a confederacy
    			List<Player> tempConfPlayers = new LinkedList<Player>(); // all players in conf with player1
    			for (Player aPlayer2 : players) {
    				if (aPlayer1 != aPlayer2){
    					if (getDiplomacyState(aPlayer1,aPlayer2).getCurrentLevel() == DiplomacyLevel.CONFEDERACY){
    						if (!aPlayer2.isDefeated()){
    							tempConfPlayers.add(aPlayer2);
    						}
    					}
    				}
    			}
    			if (tempConfPlayers.size() > 0){
    				tempConfPlayers.add(aPlayer1);
    				allFoundConfPlayers.addAll(tempConfPlayers);
    				confList.add(tempConfPlayers);
    			}
    		}
		}
    	return confList;
    }

    private List<List<Player>> getAllLordships(){
    	List<List<Player>> lordList = new LinkedList<List<Player>>();
    	List<Player> allFoundLordPlayers = new LinkedList<Player>(); // all players in lord/vassal relations
    	for (Player aPlayer1 : players) {
    		if (!allFoundLordPlayers.contains(aPlayer1)){
    			// find all other vassal players with whom this player is in a Lord
    			List<Player> tempLordPlayers = diplomacy.getVassalPlayers(aPlayer1);
//    			List<Player> tempLordPlayers = new LinkedList<Player>(); // all players in conf with player1
//    			for (Player aPlayer2 : players) {
//    				if (aPlayer1 != aPlayer2){
//    					DiplomacyState aState = getDiplomacyState(aPlayer1,aPlayer2);
//    					if ((aState.getCurrentLevel() == DiplomacyLevel.LORD) && (aState.getLord() == aPlayer1)){
//    						tempLordPlayers.add(aPlayer2);
//    					}
//    				}
//    			}
    			if (tempLordPlayers.size() > 0){
    				tempLordPlayers.add(0,aPlayer1);
    				allFoundLordPlayers.addAll(tempLordPlayers);
    				lordList.add(tempLordPlayers);
    			}
    		}
		}
    	return lordList;
    }

    private int findPlayerConfederacy(Player aPlayer, List<List<Player>> allConfederacies){
    	int foundIndex = -1;
    	int tempIndex = 0;
    	while ((foundIndex == -1) & (tempIndex < allConfederacies.size())){
    		List<Player> aConf = allConfederacies.get(tempIndex);
    		if (aConf.contains(aPlayer)){
    			foundIndex = tempIndex;
    		}else{
    			tempIndex++;
    		}
    	}
    	return foundIndex;
    }

    private int findPlayerLordship(Player aPlayer, List<List<Player>> allLordships){
    	int foundIndex = -1;
    	int tempIndex = 0;
    	while ((foundIndex == -1) & (tempIndex < allLordships.size())){
    		List<Player> aLord = allLordships.get(tempIndex);
    		if (aLord.contains(aPlayer)){
    			foundIndex = tempIndex;
    		}else{
    			tempIndex++;
    		}
    	}
    	return foundIndex;
    }

    // check if one confederacy has at least factionVictory XX% of the total pop of all planets in the game
    public List<Player> checkWinningConfederacy(){
    	List<Player> winner = null;
    	List<List<Player>> allConfederacies = getAllConfederacies();
    	if (allConfederacies.size() > 0){
    		// array for total prod for all conf
    		int[] confProdTotal = new int[allConfederacies.size()]; 
    		int otherProd = 0;  // räkna popen på alla neutrala planeter
    		// räkna popen på alla factioner
    		for (Planet aPlanet : planets){
    			if (aPlanet.getPlayerInControl() != null){
    				int confIndex = findPlayerConfederacy(aPlanet.getPlayerInControl(), allConfederacies);
    				if (confIndex > -1){
    					if (aPlanet.getPlayerInControl().isAlien()){
    						confProdTotal[confIndex] += aPlanet.getResistance();
    					}else{
    						confProdTotal[confIndex] += aPlanet.getPopulation();
    					}
    				}else{
    					otherProd += aPlanet.getPopulation();
    				}
    			}else{
    				otherProd += aPlanet.getPopulation();
    			}
    		}
    		// summera all pop i sectorn
    		int totalProd = otherProd;
    		for (int aConfProd : confProdTotal){
    			totalProd += aConfProd;
    		}
    		// check for a winner
    		int index = 0;
    		while ((winner == null) & (index < confProdTotal.length)){
				if (((confProdTotal[index]*1.0)/totalProd*1.0) > (factionVictory/100.0)){
					winner = allConfederacies.get(index);
				}else{
					index++;
    			}
    		}
    	}
    	return winner;
    }

    // check if one confederacy has at least factionVictory XX% of the total pop of all planets in the game
    public List<Player> checkWinningLord(){
    	List<Player> winner = null;
    	List<List<Player>> allLordships = getAllLordships();
    	if (allLordships.size() > 0){
    		// array for total prod for all conf
    		int[] lordProdTotal = new int[allLordships.size()]; 
    		int otherProd = 0;  // räkna popen på alla neutrala planeter
    		// räkna popen på alla factioner
    		for (Planet aPlanet : planets){
    			if (aPlanet.getPlayerInControl() != null){
    				int lordIndex = findPlayerLordship(aPlanet.getPlayerInControl(), allLordships);
    				if (lordIndex > -1){
    					if (aPlanet.getPlayerInControl().isAlien()){
    						lordProdTotal[lordIndex] += aPlanet.getResistance();
    					}else{
    						lordProdTotal[lordIndex] += aPlanet.getPopulation();
    					}
    				}else{
        				otherProd += aPlanet.getPopulation();
    				}
    			}else{
    				otherProd += aPlanet.getPopulation();
    			}
    		}
    		// summera all pop i sectorn
    		int totalProd = otherProd;
    		for (int aConfProd : lordProdTotal){
    			totalProd += aConfProd;
    		}
    		// check for a winner
    		int index = 0;
    		while ((winner == null) & (index < lordProdTotal.length)){
				if (((lordProdTotal[index]*1.0)/totalProd*1.0) > (factionVictory/100.0)){
					winner = allLordships.get(index);
				}else{
					index++;
    			}
    		}
    	}
    	return winner;
    }

    public Faction checkWinningFaction(){
    	return checkWinningFaction(factionVictory);
    }

    // check if 1 faction has at least factionVictory(65) % of the total pop of all planets in the game
    public Faction checkWinningFaction(int factionVictoryLimit){
      Faction winner = null;
      // nollsätt totalpop för alla factioner
      for (int i = 0; i < factions.size(); i++){
        ((Faction)factions.get(i)).setTotalPop(0);
      }
      int neutralPop = 0;  // räkna popen på alla neutrala planeter
      // räkna popen på alla factioner
      for (int j = 0; j < planets.size();j++){
        Planet tempPlanet = (Planet)planets.get(j);
        if (tempPlanet.getPlayerInControl() != null){
        	if (tempPlanet.getPlayerInControl().isAlien()){
        		tempPlanet.getPlayerInControl().getFaction().setTotalPop(tempPlanet.getPlayerInControl().getFaction().getTotalPop() + tempPlanet.getResistance());
        	}else{
        		tempPlanet.getPlayerInControl().getFaction().setTotalPop(tempPlanet.getPlayerInControl().getFaction().getTotalPop() + tempPlanet.getPopulation());
        	}
        }else{
          neutralPop = neutralPop + tempPlanet.getPopulation();
        }
      }
      // summera all pop i sectorn
      int totalPop = neutralPop;
      for (int k = 0; k < factions.size(); k++){
        totalPop = totalPop + ((Faction)factions.get(k)).getTotalPop();
      }
      for (int l = 0; l < factions.size(); l++){
        if (winner == null){
          Faction tempFaction = (Faction)factions.get(l);
          if ((((tempFaction).getTotalPop()*1.0)/totalPop*1.0) > (factionVictoryLimit/100.0)){
            winner = tempFaction;
          }
        }
      }
      return winner;
    }

    // check if 1 faction has at least total production >= factionVictoryLimit
    public Faction checkWinningFactionLimit(int factionVictoryLimit){
    	Faction winner = null;
    	// nollsätt totalpop för alla factioner
    	for (Faction aFaction : factions){
    		aFaction.setTotalPop(0);
    	}
    	int neutralPop = 0;  // räkna popen på alla neutrala planeter
    	// räkna popen på alla factioner
    	for (Planet aPlanet : planets){
    		if (aPlanet.getPlayerInControl() != null){
    			Faction tempFaction = aPlanet.getPlayerInControl().getFaction();
    			int oldFactionProd = tempFaction.getTotalPop();
    			if (aPlanet.getPlayerInControl().isAlien()){
    				tempFaction.setTotalPop(oldFactionProd + aPlanet.getResistance());
    			}else{
    				tempFaction.setTotalPop(oldFactionProd + aPlanet.getPopulation());
    			}
    		}else{
    			neutralPop = neutralPop + aPlanet.getPopulation();
    		}
    	}
    	// summera all pop i sectorn
    	int totalPop = neutralPop;
    	for (Faction aFaction : factions){
    		totalPop = totalPop + aFaction.getTotalPop();
    	}
    	// kolla om det finns en vinnare
    	for (Faction aFaction : factions){
    		if (winner == null){
    			if (aFaction.getTotalPop() > factionVictoryLimit){
    				winner = aFaction;
    			}
    		}
    	}
      return winner;
    }

    public Player checkWinningPlayer(){
    	return checkWinningPlayer(singleVictory);
    }

    // check if 1 player has at least singleVictory (60) % of all pop in the game
    public Player checkWinningPlayer(int singleVictoryLimit){
      Player winner = null;
      // nollsätt totalpop för alla factioner
      for (int i = 0; i < players.size(); i++){
        ((Player)players.get(i)).setTotalPop(0);
      }
      int neutralPop = 0;   // räkna popen på alla neutrala planeter
      // räkna popen för alla spelare
      for (int j = 0; j < planets.size();j++){
        Planet tempPlanet = (Planet)planets.get(j);
        if (tempPlanet.getPlayerInControl() != null){
        	if (tempPlanet.getPlayerInControl().isAlien()){
        		tempPlanet.getPlayerInControl().setTotalPop(tempPlanet.getPlayerInControl().getTotalPop() + tempPlanet.getResistance());
        	}else{
        		tempPlanet.getPlayerInControl().setTotalPop(tempPlanet.getPlayerInControl().getTotalPop() + tempPlanet.getPopulation());
        	}
        }else{
          neutralPop = neutralPop + tempPlanet.getPopulation();
        }
      }
      // summera all pop i sectorn
      int totalPop = neutralPop;
      for (int k = 0; k < players.size(); k++){
        totalPop = totalPop + ((Player)players.get(k)).getTotalPop();
      }
      for (int l = 0; l < players.size(); l++){
        if (winner == null){
          Player tempPlayer = (Player)players.get(l);
          if ((((tempPlayer).getTotalPop()*1.0)/totalPop*1.0) > (singleVictoryLimit/100.0)){
            winner = tempPlayer;
          }
        }
      }
      return winner;
    }


    public void performStatistics(){
		setStaticsicsIncome();
		setStaticsicsProduction();
		setStatisticsVIPs();
		setStatisticsShipSize();
		setStatisticsShipNumber();
		setStatisticsTroopUnits();
		setStatisticsPlanetsCount();
		setStatisticsShipsKilled();
		setStatisticsShipKills();
    }

    /**
     * aFactionName
     * @param allLostInSpace
     * @param aFactionName
     * @param lostShips if false, return ships destroyed
     * @return
     */
    private List<CanBeLostInSpace> getShipsLostInSpace(List<CanBeLostInSpace> allLostInSpace, String aFactionName, boolean lostShips){
      	List<CanBeLostInSpace> lisList = new LinkedList<CanBeLostInSpace>();
      	for (Iterator<CanBeLostInSpace> iter = allLostInSpace.iterator(); iter.hasNext();) {
    		CanBeLostInSpace aLis = iter.next();
    		if (aLis instanceof Spaceship){
				if (!lostShips){ // ta alla skepp som ej är från aFaction
					if (aLis.getOwner() != null){
						if (!aLis.getOwner().getFaction().getName().equalsIgnoreCase(aFactionName)){
							lisList.add(aLis);
						}
					}else{ // neutralt = lägg till
						lisList.add(aLis);
					}
				}else{ // ta endast skepp från aFaction
					if (aLis.getOwner() != null){
						if (aLis.getOwner().getFaction().getName().equalsIgnoreCase(aFactionName)){
							lisList.add(aLis);
						}
					}
    			}
    		}
    	}
      	return lisList;
    }
    
    private void setStatisticsShipsKilled(){
		for (Player aPlayer : players) {
			Report lastReport = aPlayer.getTurnInfo().getLatestGeneralReport();
			String factionName = aPlayer.getFaction().getName();
	  		List<CanBeLostInSpace> allLostInSpace = lastReport.getLostInSpace();
	  		List<CanBeLostInSpace> lisOwn = getShipsLostInSpace(allLostInSpace,factionName,true); // egna förlorade skepp
	        addStatistics(StatisticType.SHIPS_LOST, aPlayer, lisOwn.size(), true);
	  		List<CanBeLostInSpace> lisOther = getShipsLostInSpace(allLostInSpace,factionName,false);
	        addStatistics(StatisticType.SHIPS_KILLED, aPlayer, lisOther.size(), true);
		}
    }
    
    private void setStatisticsTroopUnits(){
		java.util.Map<String,Integer> dataNumber = new HashMap<String,Integer>();
		for (Player aPlayer : players) {
			dataNumber.put(aPlayer.getName(), 0);
		}
		for (Troop aTroop : troops) {
			if (aTroop.getOwner() != null){
				String troopOwner = aTroop.getOwner().getName();
				// number
				Integer valueNumber = dataNumber.get(troopOwner);
				dataNumber.put(troopOwner, valueNumber + 1);
			}
		}
		for (Player aPlayer : players) {
			Integer valueNumber = dataNumber.get(aPlayer.getName());
	        addStatistics(StatisticType.TROOPS_NUMBER, aPlayer, valueNumber);
		}
    }

    private void setStatisticsPlanetsCount(){
		java.util.Map<String,Integer> dataNumber = new HashMap<String,Integer>();
		for (Player aPlayer : players) {
			dataNumber.put(aPlayer.getName(), 0);
		}
		for (Planet aPlanet : planets) {
			if (aPlanet.getPlayerInControl() != null){
				String planetOwner = aPlanet.getPlayerInControl().getName();
				// number
				Integer valueNumber = dataNumber.get(planetOwner);
				dataNumber.put(planetOwner, valueNumber + 1);
			}
		}
		for (Player aPlayer : players) {
			Integer valueNumber = dataNumber.get(aPlayer.getName());
	        addStatistics(StatisticType.PLANETS, aPlayer, valueNumber);
		}
    }

    private void setStaticsicsIncome(){
        int tempIncome;
        for (Player aPlayer : players) {
            if (!aPlayer.isDefeated()){
                tempIncome = getPlayerIncome(aPlayer,false);
                tempIncome -= getPlayerUpkeepShips(aPlayer);
                tempIncome -= getPlayerUpkeepTroops(aPlayer);
                tempIncome -= getPlayerUpkeepVIPs(aPlayer);
                if (tempIncome < 0){ // if broke set net income to 0
                	tempIncome = 0;
                }
                addStatistics(StatisticType.NET_INCOME, aPlayer, tempIncome);
            }else{
            	addStatistics(StatisticType.NET_INCOME, aPlayer, 0);
            }
		}
    }

    private void setStaticsicsProduction(){
      // skapa en map för factionernas totala pop
  	  java.util.Map<String,Integer> factionProductions = new HashMap<String,Integer>(); // String = faction name
      // nollsätt totalpop för alla factioner
      for (Player aPlayer : players) {
    	  aPlayer.setTotalPop(0);
    	  if (factionProductions.get(aPlayer.getFaction().getName()) == null){
    		  factionProductions.put(aPlayer.getFaction().getName(),0);
    	  }
      }
      int neutralPop = 0;   // räkna popen på alla neutrala planeter
      // lägg till factionerna
//      for (Faction aFaction : gw.getFactions()) {
//    	  Logger.fine(aFaction.getName());
//    	  factionProductions.put(aFaction.getName(), 0);
//      }
      // räkna popen för alla spelare
      for (int j = 0; j < planets.size();j++){
    	  Planet tempPlanet = (Planet)planets.get(j);
    	  if (tempPlanet.getPlayerInControl() != null){
    		  Faction planetFaction = tempPlanet.getPlayerInControl().getFaction();
    		  if (tempPlanet.getPlayerInControl().isAlien()){
    			  tempPlanet.getPlayerInControl().setTotalPop(tempPlanet.getPlayerInControl().getTotalPop() + tempPlanet.getResistance());
        		  factionProductions.put(planetFaction.getName(), factionProductions.get(planetFaction.getName()) + tempPlanet.getResistance());
    		  }else{
    			  tempPlanet.getPlayerInControl().setTotalPop(tempPlanet.getPlayerInControl().getTotalPop() + tempPlanet.getPopulation());
        		  factionProductions.put(planetFaction.getName(), factionProductions.get(planetFaction.getName()) + tempPlanet.getPopulation());
        		  Logger.fine(planetFaction.getName() + " " + factionProductions.get(planetFaction.getName()) + tempPlanet.getPopulation());
    		  }
    	  }else{
    		  neutralPop = neutralPop + tempPlanet.getPopulation();
    	  }
      }
      // summera all pop i sectorn
      int totalPop = neutralPop;
      for (int k = 0; k < players.size(); k++){
    	  totalPop = totalPop + ((Player)players.get(k)).getTotalPop();
      }
      // uppdatera player statistiken
      addStatistics(StatisticType.PRODUCTION_PLAYER, "Neutral", neutralPop);
      addStatistics(StatisticType.PRODUCTION_FACTION, "Neutral", neutralPop);
      for (int l = 0; l < players.size(); l++){
          Player tempPlayer = (Player)players.get(l);
          addStatistics(StatisticType.PRODUCTION_PLAYER, tempPlayer, tempPlayer.getTotalPop());
      }
      // uppdatera faction statistiken
      Set<String> keys = factionProductions.keySet();
      for (Object aFactionName : keys.toArray()) {
    	  Faction aFaction = gw.findFaction((String)aFactionName);
    	  Logger.fine(aFaction.getName());
    	  addStatistics(StatisticType.PRODUCTION_FACTION, aFaction,factionProductions.get(aFactionName));
      }
    }
    
    private void setStatisticsVIPs(){
		java.util.Map<String,Integer> data = new HashMap<String,Integer>();
		for (Player aPlayer : players) {
			data.put(aPlayer.getName(), 0);
		}
		for (VIP aVIP : allVIPs) {
			String vipOwner = aVIP.getBoss().getName();
			Integer value = data.get(vipOwner);
			data.put(vipOwner, value + 1);
		}
		for (Player aPlayer : players) {
			Integer value = data.get(aPlayer.getName());
	        addStatistics(StatisticType.VIPS, aPlayer, value);
		}
	}

    private void setStatisticsShipSize(){
		java.util.Map<String,Integer> dataSize = new HashMap<String,Integer>();
		for (Player aPlayer : players) {
			dataSize.put(aPlayer.getName(), 0);
		}
		for (Spaceship aSpaceship : spaceships) {
			if (aSpaceship.getOwner() != null){
				String shipOwner = aSpaceship.getOwner().getName();
				// size
				Integer valueSize = dataSize.get(shipOwner);
				dataSize.put(shipOwner, valueSize + aSpaceship.getSize());
			}
		}
		for (Player aPlayer : players) {
			Integer valueSize = dataSize.get(aPlayer.getName());
	        addStatistics(StatisticType.SHIP_SIZE, aPlayer, valueSize);
		}
	}

    private void setStatisticsShipNumber(){
		java.util.Map<String,Integer> dataNumber = new HashMap<String,Integer>();
		for (Player aPlayer : players) {
			dataNumber.put(aPlayer.getName(), 0);
		}
		for (Spaceship aSpaceship : spaceships) {
			if (aSpaceship.getOwner() != null){
				String shipOwner = aSpaceship.getOwner().getName();
				// number
				Integer valueNumber = dataNumber.get(shipOwner);
				dataNumber.put(shipOwner, valueNumber + 1);
			}
		}
		for (Player aPlayer : players) {
			Integer valueNumber = dataNumber.get(aPlayer.getName());
	        addStatistics(StatisticType.SHIP_NUMBER, aPlayer, valueNumber);
		}
	}

    private void setStatisticsShipKills(){
		java.util.Map<String,Integer> dataNumber = new HashMap<String,Integer>();
		for (Player aPlayer : players) {
			dataNumber.put(aPlayer.getName(), 0);
		}
		for (Spaceship aSpaceship : spaceships) {
			if (aSpaceship.getOwner() != null){
				String shipOwner = aSpaceship.getOwner().getName();
				// number
				Integer valueNumber = dataNumber.get(shipOwner);
				if (aSpaceship.getKills() > valueNumber){
					dataNumber.put(shipOwner, aSpaceship.getKills());
				}
			}
		}
		for (Player aPlayer : players) {
			Integer valueNumber = dataNumber.get(aPlayer.getName());
	        addStatistics(StatisticType.SHIPS_MOST_KILLS, aPlayer, valueNumber);
		}
	}

    public TaskForce getTaskForce(Player aPlayer, Planet aPlanet, boolean includeCivilians){
      TaskForce tf = new TaskForce(aPlayer,this);
      for (int i = 0; i < spaceships.size(); i++){
        Spaceship sstemp = spaceships.get(i);
        if ((sstemp.getOwner() == aPlayer) & (sstemp.getLocation() == aPlanet)){
        	if (!sstemp.isCivilian() | includeCivilians){
        		tf.addSpaceship(sstemp);
        	}
        }
      }
      if (tf.getTotalNrShips() == 0){ // om inga skepp returnera null = finns ingen taskforce
        tf = null;
      }
      return tf;
    }
	
	public List<TaskForce> getTaskForces(Planet aPlanet, boolean includeCivilians){
		List<TaskForce> taskforces = new LinkedList<TaskForce>();
        // get all neutral ships at aPlanet
        TaskForce neutraltf = getTaskForce(null,aPlanet,includeCivilians);
        if (neutraltf != null){
          taskforces.add(neutraltf);
        }
        // get all player taskforces at this planet
        for (int j = 0; j < players.size(); j++){
          Player tempplayer = (Player)players.get(j);
//          LoggingHandler.fine(this,this,"getTaskforces","Player loop: " + tempplayer.getName());
          TaskForce temptf = getTaskForce(tempplayer,aPlanet,includeCivilians);
          if (temptf != null){
//          	LoggingHandler.finer(this,this,"getTaskforces","TaskForce added: " + temptf.getTotalNrShips(true));
            taskforces.add(temptf);
          }
        }
		return taskforces;
	}

    public int getPlayerIncomeWithoutCorruption(Player aPlayer, boolean addToIncomeReport){
    	int income = 0;
    	if (aPlayer.isAlien()){
    		income = getPlayerIncomeAlien(aPlayer,addToIncomeReport);
    	}else{
    		income = getPlayerIncomeNonAlien(aPlayer,addToIncomeReport);
    	}
    	return income;
    }

    public int getPlayerIncome(Player aPlayer, boolean addToIncomeReport){
    	int income = getPlayerIncomeWithoutCorruption(aPlayer,addToIncomeReport);
//    	System.out.println("getPlayerIncome, income1: " + income);
    	income = aPlayer.getIncomeAfterCorruption(income);
//    	System.out.println("getPlayerIncome, income2: " + income);
    	// handle taxes
    	if (income > 0){ // only if not broke
    		income += getPlayerTaxes(aPlayer,income);
    	}
    	return income;
    }
    
    public int getPlayerTaxes(Player aPlayer, int income){
    	int taxes = 0;
		DiplomacyState lordState = diplomacy.isVassal(aPlayer);
		if (lordState != null){ // is vassal
			taxes -= lordState.getTax();
			if ((income + taxes) < 0){
				taxes = -income;
			}
		}else{ // maybe lord
    		List<DiplomacyState> vassals = diplomacy.getVassalStates(aPlayer);
    		if ((vassals != null) && (vassals.size() > 0)){ // is lord
    			int totalTax = 0;
    			for (DiplomacyState state : vassals) {
        			Player vassalPlayer = state.getOtherPlayer(aPlayer);
        	    	int vassalIncome = getPlayerIncomeWithoutCorruption(vassalPlayer,false);
        	    	vassalIncome = vassalPlayer.getIncomeAfterCorruption(vassalIncome);
        	    	int tax = 0;
					if (vassalIncome > state.getTax()){
						tax = state.getTax();
					}else{
						if (vassalIncome > 0){
							tax = vassalIncome;
						}
					}
					totalTax += tax;
				}
    			taxes += totalTax;
    		}
		}
		return taxes;
    }

    public int getPlayerIncomeAlien(Player aPlayer, boolean addToIncomeReport){
        int totIncome = 0;
        TurnInfo playerTurnInfo = null;
        if (addToIncomeReport){
      	  playerTurnInfo = aPlayer.getTurnInfo();
        }
        for (int j = 0; j < planets.size();j++){
            Planet tempPlanet = (Planet)planets.get(j);
            List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer,tempPlanet);
            if (tempPlanet.getPlayerInControl() == aPlayer){
              int tmpInc = tempPlanet.getIncomeAlien(aPlayer.getFaction().getOpenPlanetBonus(),aPlayer.getFaction().getClosedPlanetBonus(),playerTurnInfo) + getVIPIncomeBonus(aPlayer,tempPlanet,playerTurnInfo);
              Logger.finer("alien base income: " + tmpInc);
              totIncome += tmpInc;
              // add income bonus for buildings.
              
              totIncome += getPlanetBuildingsBonus(tempPlanet,playerTurnInfo);
              Logger.finest("alien base income: " + tmpInc);
              
//              totIncome += getShipIncomeOwn(shipsAtPlanet,tempPlanet.isOpen());
            }
            totIncome += getShipIncome(shipsAtPlanet,tempPlanet,playerTurnInfo);

//            else
//            if (tempPlanet.getPlayerInControl() == null){
//            	if (tempPlanet.getPopulation() > 0){ // razed planets can not give any income
//            		totIncome += getShipIncomeNeutral(shipsAtPlanet,tempPlanet.isOpen());
//            	}
//            }else
//            if (tempPlanet.getPlayerInControl().getFaction() == aPlayer.getFaction()){
//                totIncome += getShipIncomeFriendly(shipsAtPlanet,tempPlanet.isOpen());
//            }else{ // enemy planet
//                totIncome += getShipIncomeEnemy(shipsAtPlanet,tempPlanet.isOpen());
//            }
        }
        return totIncome;
    }

    public int getPlayerIncomeNonAlien(Player aPlayer, boolean addToIncomeReport){
      int totIncome = 0;
      TurnInfo playerTurnInfo = null;
      if (addToIncomeReport){
    	  playerTurnInfo = aPlayer.getTurnInfo();
      }
      for (int j = 0; j < planets.size();j++){
        Planet tempPlanet = (Planet)planets.get(j);
        List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer,tempPlanet);
       // Logger.finest("shipsAtPlanet " + shipsAtPlanet.size());
        if (tempPlanet.getPlayerInControl() == aPlayer){
        	Logger.finest("tempPlanet " + tempPlanet.getName());        	
        	int tmpInc = tempPlanet.getIncome(aPlayer.getFaction().getOpenPlanetBonus(),aPlayer.getFaction().getClosedPlanetBonus(),playerTurnInfo) + getVIPIncomeBonus(aPlayer,tempPlanet,playerTurnInfo);
        	Logger.finest("tmpInc " + tmpInc);
        	totIncome = totIncome + tmpInc;
        	Logger.finest("totIncome1 " + totIncome);
        	// add income bonus for Buildings
        	Logger.finest("getPlanetBuildingsBonus(tempPlanet) före");
        	totIncome = totIncome + getPlanetBuildingsBonus(tempPlanet,playerTurnInfo);
        	Logger.finest("totIncome2 " + totIncome);
        	Logger.finest("getPlanetBuildingsBonus(tempPlanet) efter");
        	Logger.finest("totIncome3 " + totIncome);
        }
        totIncome += getShipIncome(shipsAtPlanet,tempPlanet,playerTurnInfo);
      }
      Logger.finest("getPlayerIncomeNonAlien totIncome return " + totIncome);
      return totIncome;
    }
    
    private int getPlanetBuildingsBonus(Planet tempPlanet, TurnInfo playerTurnInfo){
    	int tempIncom = 0;
    	List<Building> tempBuildigns = tempPlanet.getBuildings();    	
    	for (Building building : tempBuildigns){
    		if (tempPlanet.isOpen()){
    		//	if (building.getBuildingType().getOpenPlanetBonus() > tempIncom){
    				if(!building.getBuildingType().isInOrbit() || !tempPlanet.isBesieged()){
    					int openInc = building.getBuildingType().getOpenPlanetBonus(); 
    					tempIncom += openInc;
    					if (openInc > 0){
    						if (playerTurnInfo != null){
    							playerTurnInfo.addToLatestIncomeReport(IncomeType.BUILDING, building.getUniqueName() + " open planet bonus", tempPlanet.getName(), openInc);
    						}
    					}
    				}
    		//	}
    			// removed posility to have minus incom on buildings...
    			/*else if(building.getBuildingType().getOpenPlanetBonus() != 0 && tempIncom == 0){// minus inkomst.
    				if(!building.getBuildingType().isInOrbit() || !tempPlanet.isBesieged()){
    					tempIncom = building.getBuildingType().getOpenPlanetBonus();
    				}
    			}*/
    			
			}else{
//	        	LoggingHandler.finer("planet closed",this);
			//	if (building.getBuildingType().getClosedPlanetBonus() > tempIncom){
					if(!building.getBuildingType().isInOrbit() || !tempPlanet.isBesieged()){
						int closedInc = building.getBuildingType().getClosedPlanetBonus();
						tempIncom += closedInc;
						if (closedInc > 0){
							if (playerTurnInfo != null){
								playerTurnInfo.addToLatestIncomeReport(IncomeType.BUILDING, building.getUniqueName() + " closed planet bonus", tempPlanet.getName(), closedInc);
							}
						}
				//	}
					// removed posility to have minus incom on buildings...
	    			/*else if(building.getBuildingType().getClosedPlanetBonus() != 0 && tempIncom == 0){// minus inkomst.
	    				if(!building.getBuildingType().isInOrbit() || !tempPlanet.isBesieged()){
	    					tempIncom = building.getBuildingType().getClosedPlanetBonus();
	    				}
	    			}*/
					
//		        	LoggingHandler.finer("tmpIncome: " + tmpIncome,this);
				}
			}
    	}
    	return tempIncom;
    }

    private int getShipIncome(List<Spaceship> shipsAtPlanet, Planet aPlanet, TurnInfo playerTurnInfo){
    	int tmpIncome = 0;
		Spaceship aShip = null;
    	for (Spaceship spaceship : shipsAtPlanet) {
			int shipIncome = getShipIncome(spaceship,aPlanet);
			if (shipIncome > tmpIncome){
				tmpIncome = shipIncome;
				aShip = spaceship;
			}
		}
    	if (tmpIncome > 0){
    		if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.SHIP, aShip.getName(), aPlanet.getName(), tmpIncome);
    	}
    	return tmpIncome;
    }
    
    private int getShipIncome(Spaceship spaceship,Planet aPlanet){
    	int income = 0;
    	Player planetPlayer = aPlanet.getPlayerInControl();
    	Player shipPlayer = spaceship.getOwner(); // should never be null
    	if (aPlanet.isOpen()){
    		if (planetPlayer == null){ // neutral planet
    			income = spaceship.getIncNeutralOpenBonus();
    		}else{ // planet not neutral
    			if (planetPlayer == shipPlayer){
					spaceship.getIncOwnOpenBonus();
    			}else{
    				DiplomacyState state = diplomacy.getDiplomacyState(shipPlayer, planetPlayer);
    				DiplomacyLevel level = state.getCurrentLevel();
    				if ((level == DiplomacyLevel.CONFEDERACY) | (level == DiplomacyLevel.ALLIANCE) | (level == DiplomacyLevel.LORD)){
    					spaceship.getIncFrendlyOpenBonus();
    				}else
    				if ((level == DiplomacyLevel.CEASE_FIRE) | (level == DiplomacyLevel.PEACE)){
    					spaceship.getIncNeutralOpenBonus();
    				}else
    				if ((level == DiplomacyLevel.WAR) | (level == DiplomacyLevel.ETERNAL_WAR)){
    					spaceship.getIncEnemyOpenBonus();
    				}
    			}
    		}
    	}else{ // closed planet
    		if (planetPlayer == null){ // neutral planet
    			income = spaceship.getIncNeutralClosedBonus();
    		}else{ // planet not neutral
    			if (planetPlayer == shipPlayer){
					spaceship.getIncOwnClosedBonus();
    			}else{
    				DiplomacyState state = diplomacy.getDiplomacyState(shipPlayer, planetPlayer);
    				DiplomacyLevel level = state.getCurrentLevel();
    				if ((level == DiplomacyLevel.CONFEDERACY) | (level == DiplomacyLevel.ALLIANCE) | (level == DiplomacyLevel.LORD)){
    					spaceship.getIncFrendlyClosedBonus();
    				}else
    				if ((level == DiplomacyLevel.CEASE_FIRE) | (level == DiplomacyLevel.PEACE)){
    					spaceship.getIncNeutralClosedBonus();
    				}else
    				if ((level == DiplomacyLevel.WAR) | (level == DiplomacyLevel.ETERNAL_WAR)){
    					spaceship.getIncEnemyClosedBonus();
    				}
    			}
    		}
    	}
    	return income;
    }

    private int getVIPIncomeBonus(Player aPlayer, Planet aPlanet, TurnInfo playerTurnInfo){
      int incomeBonus = 0;
      if (!aPlanet.isBesieged()){
    	  VIP tempVIP = findVIPEconomicBonus(aPlanet, aPlayer);
    	  if (tempVIP != null){
    		  if (aPlanet.isOpen()){
    			  incomeBonus = tempVIP.getOpenIncBonus();
    			  if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.VIP, tempVIP.getName() + " open planet bonus", aPlanet.getName(), incomeBonus);
    		  }else{
    			  incomeBonus = tempVIP.getClosedIncBonus();
    			  if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.VIP, tempVIP.getName() + " closed planet bonus", aPlanet.getName(), incomeBonus);
    		  }
    	  }
      }
      return incomeBonus;
    }

    public int getPlayerFreeUpkeep(Player aPlayer){
      int totUpkeepIncome = getPlayerFreeUpkeepWithoutCorruption(aPlayer);
      totUpkeepIncome = aPlayer.getUpkeepAfterCorruption(totUpkeepIncome);
      return totUpkeepIncome;
    }

    public int getPlayerFreeUpkeepWithoutCorruption(Player aPlayer){
        int totUpkeepIncome = 0;
        for (int i = 0; i < planets.size();i++){
          Planet tempPlanet = (Planet)planets.get(i);
          if (tempPlanet.getPlayerInControl() == aPlayer){
            totUpkeepIncome = totUpkeepIncome + tempPlanet.getUpkeep();
          }
        }
        return totUpkeepIncome;
      }

    public int getPlayerUpkeepCost(Player aPlayer){
      int totUpkeepCost = 0;
      for (int j = 0; j < spaceships.size();j++){
        Spaceship tempss = spaceships.get(j);
        if (tempss.getOwner() == aPlayer){
          totUpkeepCost = totUpkeepCost + tempss.getUpkeep();
        }
      }
      return totUpkeepCost;
    }

    public int getPlayerUpkeepShips(Player aPlayer){
      int totUpkeepCost = 0;
      int totUpkeepIncome = 0;
      int totUpkeep;
      totUpkeepIncome = getPlayerFreeUpkeep(aPlayer);
      totUpkeepCost = getPlayerUpkeepCost(aPlayer);
      totUpkeep = totUpkeepCost - totUpkeepIncome;
      if (totUpkeep < 0){
        totUpkeep = 0;
      }
      return totUpkeep;
    }
    
    
    public int getPlayerUpkeepVIPs(Player aPlayer){
        int totUpkeepCost = 0;
        
        for (int j = 0; j < allVIPs.size();j++){
          VIP tempVIP = (VIP)allVIPs.get(j);
          if (tempVIP.getBoss() == aPlayer){
            totUpkeepCost = totUpkeepCost + tempVIP.getUpkeep();
          }
        }
        
        return totUpkeepCost;
      }

    public int getPlayerUpkeepTroops(Player aPlayer){
        int totUpkeepCost = 0;
        for (Planet aPlanet : planets) {        	
			int totTroopsCost = getTroopsUpKeepPlanet(aPlayer, aPlanet);
        	if (totTroopsCost > 0){
	        	totUpkeepCost = totUpkeepCost + totTroopsCost;
	        }
		}		
        return totUpkeepCost;
    }
    
    public int getTroopsUpKeepPlanet(Player aPlayer, Planet aPlanet){
    	// get all players troops on this planet and compute support costs
		int upkeepOnPlanet = getTroopsCostPlanet(aPlayer, aPlanet);
		// get free upkeep for this planet (if any)
        int freeTroopUpkeep = 0;
		if (aPlanet.getPlayerInControl() == aPlayer){
			freeTroopUpkeep = aPlanet.getResistance();
		}
		// tot cost = cost - upkeep
		int totTroopsCost = upkeepOnPlanet - freeTroopUpkeep;
		return totTroopsCost;
    }
    
    public int getTroopsCostPlanet(Player aPlayer, Planet aPlanet){
    	List<Troop> troopsAtPlanet = getPlayersTroopsOnPlanet(aPlayer, aPlanet);
    	int upkeep = 0;
		for (Troop aTroop : troopsAtPlanet) {
			upkeep = upkeep + aTroop.getUpkeep();			
		}
		return upkeep;
    }

    public void removeShip(Spaceship ss){
      boolean ok;
      ss.setDestroyed();
      if (ss.isCarrier()){
    	  removeSquadronsFromCarrier(ss);
      }
      ok = spaceships.remove(ss);
      if (!ok){Logger.severe("Couldn't find spaceship to delete!!!");} // spårutskrift
    }

    public void removeTroop(Troop aTroop){
        boolean ok;
        aTroop.setDestroyed();
        ok = troops.remove(aTroop);
        if (aTroop.getOwner() != null){ // only players can have vips on troops
        	checkVIPsInDestroyedTroop(aTroop);
        }
        if (!ok){Logger.finer("Couldn't find troop to delete!!!");} // spårutskrift
    }

    private void removeSquadronsFromCarrier(Spaceship aCarrier){
    	for (Iterator<Spaceship> iter = spaceships.iterator(); iter.hasNext();) {
			Spaceship aShip = iter.next();
			if (aShip.isSquadron()){
				if (aShip.getCarrierLocation() == aCarrier){
					aShip.setCarrierLocation(null);
				}
			}
		}
    }

    public List<PlanetConnection> getPlanetConnections(){
      return planetConnections;
    }

    public void addPlanetConnections(PlanetConnection pc){
      planetConnections.add(pc);
    }

    public int getNrPlayers(){
      return players.size();
    }
    
    public int getLongestGovenorName(){
    	int longest = 0;
        for (int i = 0; i < players.size(); i++){
          Player temp = (Player)players.get(i);
          if (temp.getGovenorName().length() > longest){
              longest = temp.getGovenorName().length();
          }
        }
        return longest;
    }

    public int getNrActivePlayers(){
      return getActivePlayers().size();
    }

    public List<Player> getActivePlayers(){
    	List<Player> tmpPlayers = new LinkedList<Player>();
    	for (Player aPlayer : players){
    		if (!aPlayer.isDefeated()){
    			tmpPlayers.add(aPlayer);
    		}
    	}
    	return tmpPlayers;
    }

    public int getNrFinishedPlayers(){
      int nrTemp = 0;
      for (int i = 0; i < players.size(); i++){
        Player temp = (Player)players.get(i);
        if (!temp.isDefeated() & temp.isFinishedThisTurn()){
          nrTemp = nrTemp + 1;
        }
      }
      return nrTemp;
    }

    public boolean isPlayerShipAtPlanet(Player aPlayer, Planet aPlanet){
      boolean shipAtPlanet = false;
      for (int j = 0; j < spaceships.size(); j++){
        Spaceship tempss = spaceships.get(j);
        if ((tempss.getOwner() == aPlayer) & (tempss.getLocation() == aPlanet)){
          shipAtPlanet = true;
        }
      }
      return shipAtPlanet;
    }

    public boolean isPlayerTroopsAtPlanet(Player aPlayer, Planet aPlanet){
        boolean troopAtPlanet = false;
        for (int j = 0; j < troops.size(); j++){
        	Troop tempt = (Troop)troops.get(j);
        	if (tempt.getOwner() == aPlayer){
        		if (tempt.getPlanetLocation() == aPlanet){
        			troopAtPlanet = true;
        		}else{
        			if (tempt.getShipLocation() != null){
        				if (tempt.getShipLocation().getLocation() == aPlanet){
        					troopAtPlanet = true;
        				}
        			}
        		}
        	}
        }
        return troopAtPlanet;
    }

    public boolean isPlayerVIPAtPlanet(Player aPlayer, Planet aPlanet){
      boolean VIPAtPlanet = false;
      for (int j = 0; j < allVIPs.size(); j++){
        VIP tempVIP = (VIP)allVIPs.get(j);
        if ((tempVIP.getBoss() == aPlayer) & (tempVIP.getLocation() == aPlanet)){
          VIPAtPlanet = true;
        }
      }
      return VIPAtPlanet;
    }
    
    private VIP findStealthVIPonShip(Planet aPlanet, Spaceship aShip){
    	VIP foundVIP = null;
    	int index = 0;
    	while ((foundVIP == null) &  (index < allVIPs.size())) {
    		VIP aVIP = allVIPs.get(index);
			if (aVIP.getShipLocation() == aShip){
				if (aVIP.isStealth()){
					foundVIP = aVIP;
				}
			}
			if (foundVIP == null){
				index++;
			}
		}
    	return foundVIP;
    }
    
    public String getLargestShipSizeOnPlanet(Planet aPlanet, Player aPlayer, boolean civilian){
      String shipSize= "";
      int maxTonnage = 0;
      for (Spaceship aShip : spaceships) {
        if ((aShip.getOwner() == aPlayer) & (aShip.getLocation() == aPlanet)){
        	if (aShip.isLookAsCivilian() == civilian){
        		VIP stealthVIP = findStealthVIPonShip(aPlanet, aShip);
        		if (aShip.isVisibleOnMap() & (stealthVIP == null)){
        			if (aShip.getTonnage() > maxTonnage){
        				maxTonnage = aShip.getTonnage();
        			}
        		}
        	}
        }
      }
      if ((maxTonnage > 0) & (maxTonnage <= 300)){
        shipSize= "small";
      }else
      if ((maxTonnage > 300) & (maxTonnage <= 600)){
        shipSize= "medium";
      }else
      if ((maxTonnage > 600) & (maxTonnage <= 900)){
        shipSize= "large";
      }else
      if ((maxTonnage > 900) & (maxTonnage <= 1500)){
        shipSize= "huge";
      }
      return shipSize;
    }

    public int getLargestLookAsMilitaryShipSizeOnPlanet(Planet aPlanet, Player aPlayer){
        int shipSize= -1;
        int maxTonnage = 0;
        for (Spaceship aShip : spaceships) {
          if ((aShip.getOwner() == aPlayer) & (aShip.getLocation() == aPlanet)){
          	if (!aShip.isLookAsCivilian()){
          		VIP stealthVIP = findStealthVIPonShip(aPlanet, aShip);
          		if (aShip.isVisibleOnMap() & (stealthVIP == null)){
          			if (aShip.getTonnage() > maxTonnage){
//          				Logger.info("aShip name" + aShip.getName());
//          				Logger.info("aShip location" + aShip.getLocation());
          				maxTonnage = aShip.getTonnage();
          			}
          		}
          	}
          }
        }
        if ((maxTonnage > 0) & (maxTonnage <= 300)){
          shipSize= 1;
        }else
        if ((maxTonnage > 300) & (maxTonnage <= 600)){
          shipSize= 2;
        }else
        if ((maxTonnage > 600) & (maxTonnage <= 900)){
          shipSize= 3;
        }else
        if ((maxTonnage > 900) & (maxTonnage <= 1500)){
          shipSize= 5;
        }
        return shipSize;
      }

    /**
     * Find the max ship size string to be shown on the map, ex:
     * "small+civ"
     * @param aPlanet
     * @param aPlayer find largest size of ships belonging to other players/neutral
     * @return
     */
    public String getLargestShipSizeOnPlanet(Planet aPlanet, Player aPlayer){
        String shipSize= "";
        int maxTonnage = 0;
        boolean civ = false;
        for (int i = 0; i < spaceships.size(); i++){
        	Spaceship tempss = spaceships.get(i);
        	if ((tempss.getOwner() != aPlayer) & (tempss.getLocation() == aPlanet)){
        		if (tempss.isLookAsCivilian()){
        			civ = true;
        		}else
       			if (tempss.isVisibleOnMap()){
       				if (tempss.getTonnage() > maxTonnage){
       					maxTonnage = tempss.getTonnage();
       				}
       			}
        	}
        }
        if ((maxTonnage > 0) & (maxTonnage <= 300)){
          shipSize= "small";
        }else
        if ((maxTonnage > 300) & (maxTonnage <= 600)){
          shipSize= "medium";
        }else
        if ((maxTonnage > 600) & (maxTonnage <= 900)){
          shipSize= "large";
        }else
        if ((maxTonnage > 900) & (maxTonnage <= 1500)){
          shipSize= "huge";
        }
        if (civ){
        	shipSize += "+civ";
        }
        return shipSize;
      }

    public int getMaxResupplyFromShip(Planet aPlanet, Player aPlayer){
    	int maxSize = 0;
    	for (Iterator<Spaceship> ss = spaceships.iterator(); ss.hasNext();) {
			Spaceship aShip = ss.next();
			if (aShip.getLocation() == aPlanet){
				if (aShip.getOwner() == aPlayer){
					if (aShip.getMaxResupply() > maxSize){
						maxSize = aShip.getMaxResupply();
					}
				}
			}
		}
    	return maxSize;
    }

    public List<Player> getPlayers(){
      return players;
    }

    public List<String> getAllDestinationsStrings(Planet location, boolean longRange, Player aPlayer){
    	return getAllDestinationsStrings(location, longRange, aPlayer, false);
    }

    public List<String> getAllDestinationsStrings(Planet location, boolean longRange, Player aPlayer, boolean ownPlanetsOnly){
    	Logger.finer("getAllDestinationsStrings, location: " + location + " longRange: " + longRange);
    	// first find if there is a spaceport on the planet
    	boolean hasSpacePort = false;
    	hasSpacePort = location.hasSpacePort();
    	List<String> alldest = new LinkedList<String>();
    	for (int i = 0; i < planetConnections.size(); i++){
    		PlanetConnection temppc = (PlanetConnection)planetConnections.get(i);
    		Logger.finer("PC: " + temppc);
    		// first check if this connection includes location at all
    		Planet planetOtherEnd = temppc.getOtherEnd(location,true);
    		if (planetOtherEnd != null){
    			Logger.finer("PlanetOtherEnd: " + planetOtherEnd.getName());
    			boolean bothHaveValidSpacePorts = false;
    			if (hasSpacePort){
    				Logger.finer(" hasSpacePort");
    				// check if the other planet belongs to the same faction
    				boolean friendlyPlayer = false;
    				Player tmpPlayer = planetOtherEnd.getPlayerInControl();
    				if (tmpPlayer != null){ // planet is not neutral
    					Logger.finer("  planet is not neutral");
    					if (diplomacy.friendlySpaceports(aPlayer,tmpPlayer)){
    						Logger.finer("  friendly player");
    						friendlyPlayer = true;
    					}
//    					Faction tmpFaction = tmpPlayer.getFaction();
//    					if (tmpFaction.equals(aPlayer.getFaction())){
//    						Logger.finer("  same faction");
//    						sameFaction = true;
//    					}
    				}
    				// then check if there is a spaceport on the other planet
    				if (friendlyPlayer){
    					if (planetOtherEnd.hasSpacePort()){
							Logger.finer("ss2 is spaceport");
							bothHaveValidSpacePorts = true;
						}
    				}
    			}
    			boolean add = false;
    			if (bothHaveValidSpacePorts){
    				Logger.finer("bothHaveValidSpacePorts!");
    				add = true;
    			}else{
    				if (longRange){  // ship is long range
    					Logger.finer("ship is long range!");
        				add = true;
    				}else{
    					if (!temppc.isLongRange()){ // ship is short range but so is connection range
    						Logger.finer("ship is short range but so is connection range");
    	    				add = true;
    					}
    				}
    			}
    			if (add & ownPlanetsOnly){
    				if (planetOtherEnd.getPlayerInControl() != aPlayer){
    					add = false;
    				}
    			}
    			if (add){
    				alldest.add(planetOtherEnd.getName());
    			}
    		}
    	}
    	return alldest;
    }

    public List<Planet> getAllDestinations(Planet location, boolean longRange){
      List<Planet> alldest = new ArrayList<Planet>();
      Planet tempPlanet = null;
      for (int i = 0; i < planetConnections.size(); i++){
        PlanetConnection temppc = (PlanetConnection)planetConnections.get(i);
        tempPlanet = temppc.getOtherEnd(location,longRange);
        if (tempPlanet != null){  // there is a connection within range
          alldest.add(tempPlanet);
        }
      }
      return alldest;
    }

    /**
     * Returns 0 if there is no connection between the two planets
     * @param planet1
     * @param planet2
     * @return
     */
    public SpaceshipRange getDistance(Planet planet1, Planet planet2){
    	SpaceshipRange dist = SpaceshipRange.NONE;
        for (int i = 0; i < planetConnections.size(); i++){
          PlanetConnection temppc = (PlanetConnection)planetConnections.get(i);
          if (temppc.isConnection(planet1, planet2)){  // there is a connection within range
        	  if (temppc.isLongRange()){
        		  dist = SpaceshipRange.LONG;
        	  }else{
        		  dist = SpaceshipRange.SHORT;
        	  }
          }
        }
        return dist;
      }

    public boolean playerHasShipsInSystem(Player aPlayer, Planet aPlanet){
      boolean hasShipsInSystem = false;
      int i = 0;
      List<Spaceship> playerShips = getPlayersSpaceships(aPlayer);
      while ((i < playerShips.size()) & !hasShipsInSystem){
        Spaceship tempss = playerShips.get(i);
        if (tempss.getLocation() == aPlanet){
          hasShipsInSystem = true;
        }else{
          i++;
        }
      }
      return hasShipsInSystem;
    }

    public List<Spaceship> getPlayersSpaceshipsOnPlanet(Player aPlayer, Planet aPlanet){
      List<Spaceship> playersss = new ArrayList<Spaceship>();
      for (int i = 0; i < spaceships.size(); i++){
        Spaceship tempss = spaceships.get(i);
        if ((tempss.getOwner() == aPlayer) & (tempss.getLocation() == aPlanet)){
          playersss.add(tempss);
        }
      }
      return playersss;
    }

    public List<Spaceship> findPlayersSpaceshipsOnPlanet(Player aPlayer, Planet aPlanet, SpaceshipRange range){
    	List<Spaceship> retShips = new LinkedList<Spaceship>();
        List<Spaceship> playersss = getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet);
        for (Spaceship aSpaceship : playersss) {
			if (aSpaceship.getRange() == range){
				retShips.add(aSpaceship);
			}
		}
        return retShips;
      }

    public List<Troop> getPlayersTroopsOnPlanet(Player aPlayer, Planet aPlanet){
        List<Troop> playerst = new LinkedList<Troop>();
        Logger.finest("galaxy getPlayersTroopsOnPlanet: aPlanet: " + aPlanet.getName());
        for (Troop aTroop : troops) {
        	if(aTroop.getOwner() != null){
        	//	Logger.finest("aTroop.getOwner() + aPlayer: " + aTroop.getOwner().getName() + " " + aPlayer.getName());
        	
        		if (aTroop.getOwner() == aPlayer){
            		//Logger.finest("aTroop.getOwner() == aPlayer:  True" );
            		
            		if (aTroop.getPlanetLocation() != null && aTroop.getPlanetLocation() == aPlanet){
            		//	Logger.finest("aTroop.getPlanetLocation() + aPlanet: true");
            			playerst.add(aTroop);
            		}else{
            			if (aTroop.getShipLocation() != null){
            				if(aTroop.getShipLocation().getLocation() != null){
            					Logger.finest("aTroop.getShipLocation().getLocation() + aPlanet: " + aTroop.getShipLocation().getLocation().getName() + " " + aPlanet.getName());
            					if (aTroop.getShipLocation().getLocation() == aPlanet){
                					Logger.finest("aTroop.getShipLocation().getLocation() + aPlanet: true");
                					playerst.add(aTroop); // this should also cover troops in retreating ships
                				}
//            				}else{
//            					Logger.finest("aTroop.getShipLocation().getLocation() = null. + aPlanet: " + aPlanet.getName());
            				}
            			}
            		}
            	}
        	}
        }
        return playerst;
    }

    public int getMaxBombardment(Planet aPlanet, Player aPlayer){
    	List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer,aPlanet);
    	int maxBombardment = 0;
    	for (Spaceship ss : shipsAtPlanet) {
            if (ss.getBombardment() > maxBombardment){
            	maxBombardment = ss.getBombardment();
            }
		}
    	return maxBombardment;
    }

    public boolean isPsychWarfare(Planet aPlanet, Player aPlayer){
    	List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer,aPlanet);
    	boolean tempPW = false;
    	for (Spaceship ss : shipsAtPlanet) {
            if (ss.getPsychWarfare() > 0){
                tempPW = true;
            }
    	}
    	return tempPW;
    }

    public int getMaxPsychWarfare(Planet aPlanet, Player aPlayer){
    	List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer,aPlanet);
    	int maxPW = 0;
    	for (Spaceship ss : shipsAtPlanet) {
            if (ss.getPsychWarfare() > maxPW){
                maxPW = ss.getPsychWarfare();
            }
    	}
    	return maxPW;
    }

    /*
    public boolean getTroops(Planet aPlanet, Player aPlayer){
    	List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer,aPlanet);
    	boolean tempTroops = false;
    	for (Spaceship ss : shipsAtPlanet) {
            if (ss.getTroops()){
                tempTroops = true;
            }
    	}
    	return tempTroops;
    }
*/
    
    /**
     * Get the number of troops on a planet, from other players / neutral
     */
    public int getTroopsNrOnPlanet(Planet aPlanet, Player aPlayer){
    	int nrTroops = 0;
    	for (Troop aTroop : troops) {
            if (aTroop.getPlanetLocation() == aPlanet){
            	if (aTroop.getOwner() != aPlayer){
            		nrTroops++;
            	}
            }
    	}
    	return nrTroops;
    }

    public List<Troop> getTroopsOnPlanet(Planet aPlanet, Player aPlayer){
    	List<Troop> troopsAtPlanet = new LinkedList<Troop>();
    	for (Troop aTroop : troops) {
            if (aTroop.getPlanetLocation() == aPlanet){
            	if (aTroop.getOwner() == aPlayer){
            		troopsAtPlanet.add(aTroop);
            	}
            }
    	}
    	return troopsAtPlanet;
    }
    
    public List<Player> getAttackingPlayersWithTroopsOnPlanet(Planet aPlanet){
    	List<Player> players = new ArrayList<Player>();
    	for (Troop aTroop : troops) {
    		if(aTroop.getOwner() != null &&	(aPlanet.getPlayerInControl() == null || ( aTroop.getOwner() != aPlanet.getPlayerInControl() && diplomacy.hostileBesiege(aTroop.getOwner(), aPlanet.getPlayerInControl()))) &&
    				aTroop.getPlanetLocation() == aPlanet){
	    		if(!players.contains(aTroop.getOwner())){
	    			players.add(aTroop.getOwner());
	    		}
    		}
        }
    	return players;
    }
    
    public List<Troop> getTroopsOnShip(Spaceship sShip){
    	List<Troop> troopsAtPlanet = new LinkedList<Troop>();
    	for (Troop aTroop : troops) {
            if (aTroop.getShipLocation() == sShip){
            	troopsAtPlanet.add(aTroop);
            }
    	}
    	return troopsAtPlanet;
    }

    public List<Troop> getTroops(){
    	return troops;
    }

    public VIP getPsychWarfareBonusVIPs(Planet aPlanet, Player aPlayer){
    	List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer,aPlanet);
    	VIP highestPsychWarfareVIP = null;
        for (Spaceship ss: shipsAtPlanet){
          	VIP aVIP = findHighestVIPPsychWarfareBonus(ss,aPlayer);
          	if (aVIP != null){
          		if (highestPsychWarfareVIP == null){
          			highestPsychWarfareVIP = aVIP;
          		}else
          		if (aVIP.getPsychWarfareBonus() > highestPsychWarfareVIP.getPsychWarfareBonus()){
          			highestPsychWarfareVIP = aVIP;
          		}
          	}
        }
    	return highestPsychWarfareVIP;
    }

    public List<Spaceship> getPlayersSpaceships(Player aPlayer){
    	List<Spaceship> playersss = new ArrayList<Spaceship>();
    	for (int i = 0; i < spaceships.size(); i++){
    		Spaceship tempss = spaceships.get(i);
    		if (tempss.getOwner() == aPlayer){
    			playersss.add(tempss);
    		}
    	}
    	return playersss;
    }
    
    public List<Spaceship> getRetreatingShips(Player aPlayer){
    	List<Spaceship> allShips = getPlayersSpaceships(aPlayer);
    	List<Spaceship> rShips = new ArrayList<Spaceship>();
    	for (Iterator<Spaceship> iter = allShips.iterator(); iter.hasNext();) {
			Spaceship tmpss = iter.next();
//			if (tmpss.getRetreatingTo() != null){
			if (tmpss.isRetreating()){
				rShips.add(tmpss);
			}
		}
    	return rShips;
    }

    public List<VIP> getPlayersVips(Player aPlayer){
    	List<VIP> playersv = new LinkedList<VIP>();
        for (VIP tempv : allVIPs){
          if (tempv.getBoss() == aPlayer){
            playersv.add(tempv);
          }
        }
        return playersv;
    }

    public List<Troop> getPlayersTroops(Player aPlayer){
        List<Troop> playersTroops = new LinkedList<Troop>();
        for (int i = 0; i < troops.size(); i++){
          Troop tempTroop = (Troop)troops.get(i);
          if (tempTroop.getOwner() == aPlayer){
            playersTroops.add(tempTroop);
          }
        }
        return playersTroops;
    }

    public List<Planet> getPlayersPlanets(Player aPlayer){
      List<Planet> playersPlanets = new ArrayList<Planet>();
      for (int i = 0; i < planets.size(); i++){
        Planet tempPlanet = (Planet)planets.get(i);
        if (tempPlanet.getPlayerInControl() == aPlayer){
          playersPlanets.add(tempPlanet);
        }
      }
      return playersPlanets;
    }
    
    public int getNumberUnbesiegedPlayerPlanets(Player player){
    	List<Planet> playerPlanets = getPlayersPlanets(player);
    	int nr = 0;
    	for(Planet aPlanet : playerPlanets){
    		if (!aPlanet.isBesieged()){
    			nr++;
    		}
    	}
    	return nr;
    }

    public PublicInfo getLastPublicInfo(){
      return (PublicInfo)publicInfos.get(publicInfos.size() - 1);
    }

    public List<Spaceship> getSpaceships(){
      return spaceships;
    }
    
    /**
     * Return a list with all spaceships at aPlanet. If civilian is false,
     * only military ships are returned.
     * @param aPlanet a planet
     * @param civilan if only civilian ships should be returned
     * @return a list of civilian or military ships at the aPlanet
     */
    public List<Spaceship> getShips(Planet aPlanet, boolean civilian){
    	List<Spaceship> ships = new LinkedList<Spaceship>();
    	for (Spaceship aShip : spaceships) {
			if ((aShip.getLocation() != null) && (aShip.getLocation() == aPlanet)){
				if (aShip.isCivilian() == civilian){
					ships.add(aShip);
				}
			}
		}
    	return ships;
    }

    /**
     * Return a list with all spaceships belonging to the planets player (can be neutral) at aPlanet. 
     * @param aPlanet a planet
     * @return a list of ships at the aPlanet
     */
    public List<Spaceship> getShips(Planet aPlanet){
    	List<Spaceship> ships = new LinkedList<Spaceship>();
    	for (Iterator<Spaceship> iter = spaceships.iterator(); iter.hasNext();) {
			Spaceship aShip = iter.next();
			if ((aShip.getLocation() != null) && (aShip.getLocation() == aPlanet)){
				if (aShip.getOwner() == aPlanet.getPlayerInControl()){
					ships.add(aShip);
				}
			}
		}
    	return ships;
    }

    public void addSpaceship(Spaceship tempss){
      spaceships.add(tempss);
    }

    public Faction findFaction(String uniqueFactionName){
      int index = 0;
      Faction found = null;
      while ((index < factions.size()) & (found == null)){
        Faction tempFaction = (Faction)factions.get(index);
        if (tempFaction.getName().equalsIgnoreCase(uniqueFactionName)){
          found = tempFaction;
        }else{
          index++;
        }
      }
      return found;
    }

    public Planet getRunToPlanet(Spaceship aSpaceship){
      Logger.finer("getRunToPlanet aSpaceship: " + aSpaceship.getName());
      Planet foundPlanet = null;
      Planet firstDestination = null;
      // kolla efter egna planeter
      foundPlanet = findClosestOwnPlanetFromShip(aSpaceship.getLocation(),aSpaceship.getOwner(),aSpaceship);  
      // om en destinationsplanet har hittats skall den 1:a planeten på väg dit hämtas
      if (foundPlanet != null){
        Logger.finer("foundPlanet: " + foundPlanet.getName());
        firstDestination = findFirstJumpTowardsPlanet(aSpaceship.getLocation(),foundPlanet,aSpaceship);
        Logger.finer("firstDestination: " + firstDestination.getName());
      }else{
        Logger.finer("no planet found");
      }
      return firstDestination;
    }

    
    
    public Planet findClosestOwnPlanetFromShip(Planet aLocation, Player aPlayer, Spaceship aSpaceship){
    	return findClosestPlanet(aLocation,aPlayer,aSpaceship.getRange(),FindPlanetCriterium.OWN_PLANET_NOT_BESIEGED,null);
    }

    
    // aPlayer kan vara null för att leta efter neutrala planeter
    private Planet findClosestPlanet(Planet aLocation, Player aPlayer, SpaceshipRange aSpaceshipRange, FindPlanetCriterium aCriterium, List<String> visitedPlanets){
      Logger.finer("findClosestOwnPlanetFromShip: " + aLocation.getName());
      Planet foundPlanet = null;
      // skapa tom vektor över hittade planeter
      List<Planet> foundPlanets = new ArrayList<Planet>();
      List<Planet> edgePlanets = new ArrayList<Planet>(); // de planeter som var på gränsen till det genomsökta området
      edgePlanets.add(aLocation);
      List<Planet> newEdgePlanets = new ArrayList<Planet>(); // de planeter som är på gränsen till det genomsökta området
      List<Planet> searchedPlanets = new ArrayList<Planet>();  // lägg in alla som genomsökts + startplaneten
      searchedPlanets.add(aLocation);
/*
      // a spaceship cannot retreat back to the planet it retreated from
      if (aLocation != aSpaceship.getRetreatingFrom()){
      	searchedPlanets.addElement(aSpaceship.getRetreatingFrom());
      	LoggingHandler.fine( this, this, "","adding: " + aSpaceship.getRetreatingFrom());
      }
*/
      List<Planet> allNeighbours;
      // loopa tills alla planeter har letats igenom eller minst 1 lämplig planet har hittats
      while ((searchedPlanets.size() < planets.size()) & (foundPlanets.size() == 0) & (edgePlanets.size() > 0)){
      	Logger.finer("in while");
      	// Gå igenom alla edgePlanets
        for (int i = 0; i < edgePlanets.size(); i++){
       	  Logger.finest("loop edgeplanets");
          Planet tempPlanet = edgePlanets.get(i);
          Logger.finest("temp edgeplanet: " + tempPlanet.getName());
          // Hämta alla grannar till tempPlanet
          allNeighbours = getAllDestinations(tempPlanet,aSpaceshipRange == SpaceshipRange.LONG);
          // Gå igenom alla allNeighbours  (lägg i newEdgePlanets)
          for (int j = 0; j < allNeighbours.size(); j++){
          	Logger.finest("loop neighbours");
            Planet tempNeighbourPlanet = allNeighbours.get(j);
            Logger.finest("temp neighbours: " + tempNeighbourPlanet.getName());
            // kolla att tempNeighbourPlanet inte redan finns i searchedPlanets
            if ((!searchedPlanets.contains(tempNeighbourPlanet)) & (!newEdgePlanets.contains(tempNeighbourPlanet))){
              // lägg i newEdgePlanets
              newEdgePlanets.add(tempNeighbourPlanet);
              Logger.finest("adding to searched");
            }
          }
        }
        Logger.finer("loop edge finished");
        // Gå igenom newEdgePlanets och (och ej belägrade??? kan bara gälla egna planeter)
        for (int k = 0; k < newEdgePlanets.size(); k++){
          Logger.finest("loop new edge");
          Planet tempPlanet = newEdgePlanets.get(k);
          Logger.finest("temp new edgeplanet: " + tempPlanet.getName());
          boolean alreadyVisited = false;
    	  if ((visitedPlanets != null) && (visitedPlanets.contains(tempPlanet.getName()))){
    		  alreadyVisited = true;
    	  }
          if (!alreadyVisited){
	          if (aCriterium == FindPlanetCriterium.OWN_PLANET_NOT_BESIEGED){
		          // kolla om planeten tillhör eftersökt spelare
		          if (tempPlanet.getPlayerInControl() == aPlayer){// om planeter tillhör eftersökt spelare
		            // om den dessutom ej är belägrad, sätt in den i foundPlanets
		          	if (!tempPlanet.isBesieged()){
		          		foundPlanets.add(tempPlanet);
		          		Logger.finest("adding to found: " + tempPlanet.getName());
		          	}
		          }
	          }else
	          if (aCriterium == FindPlanetCriterium.CLOSED){ // only planets not belonging to the palyer
	        	  if ((tempPlanet.getPlayerInControl() != aPlayer) & (!tempPlanet.isOpen())){
	        		  foundPlanets.add(tempPlanet);
	        	  }
	          }else
	          if (aCriterium == FindPlanetCriterium.HOSTILE_ASSASSIN_OPEN){
	        	  if (tempPlanet.isOpen() & tempPlanet.getPlayerInControl() != null){
	        		  if (diplomacy.hostileAssassin(tempPlanet.getPlayerInControl(), aPlayer)){	  
	            		  foundPlanets.add(tempPlanet);
	        		  }
	        	  }
	          }else
	          if (aCriterium == FindPlanetCriterium.NEUTRAL_UNTOUCHED){
	        	  if (tempPlanet.isOpen() & tempPlanet.getPlayerInControl() == null){ // open neutral
	        		  foundPlanets.add(tempPlanet);
	        	  }else{ // if closed since the beginning, and assumed neutral
	        		  MapPlanetInfo mapPlanetInfo = aPlayer.getMapInfos().getLastKnownOwnerInfo(tempPlanet); // should return null if no info about owner
	        		  if ((tempPlanet.getPlayerInControl() != aPlayer) & !tempPlanet.isOpen() & (mapPlanetInfo == null)){
	            		  foundPlanets.add(tempPlanet);
	        		  }
	        	  }
	          }else
	          if (aCriterium == FindPlanetCriterium.EMPTY_VIP_TRANSPORT_WITHOUT_ORDERS){ // very specific criterium used by Droid GW
//	        	  List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer, tempPlanet);
//	        	  for (Spaceship aShip : shipsAtPlanet){
//	        		  if (aShip.getName().equals("VIP Transport")){
//	        			  if (!aPlayer.getOrders().checkShipMove(aShip)){ // if there are no order already for this ship
//	        				  if (findAllVIPsOnShip(aShip).size() == 0){ // check that transport is empty
//	        					  if (!foundPlanets.contains(tempPlanet)){
//	        						  foundPlanets.add(tempPlanet);
//	        					  }
//	        				  }
//	        			  }
//	        		  }
//	        	  }
	        	  Spaceship foundShip = findEmptyShipWithoutOrders(aPlayer,tempPlanet,"VIP Transport");
	        	  if (foundShip != null){
					  if (!foundPlanets.contains(tempPlanet)){
						  foundPlanets.add(tempPlanet);
					  }
	        	  }
	          }
          }
        }
        Logger.finest("loop new edge finished");
        // töm edgePlanets
        edgePlanets.clear();
        // kopiera över newEdgePlanets till edgePlanets
        for (int l = 0; l < newEdgePlanets.size(); l++){
          edgePlanets.add(newEdgePlanets.get(l));
        }
        // kopiera över newEdgePlanets till searchedPlanets
        for (int m = 0; m < newEdgePlanets.size(); m++){
          searchedPlanets.add(newEdgePlanets.get(m));
        }
        // töm newEdgePlanets
        newEdgePlanets.clear();
        // log if no more planets can be searched
        if (edgePlanets.size() == 0){
        	Logger.finest("egdePlanets is empty, while loop exited");
        }
      }
      Logger.finest("while finished");
      // om vektorn.size() > 0, dvs minst 1st lämplig planet har hittats
      if (foundPlanets.size() > 0){
        Logger.finest("foundPlanets.size() > 0");
        // välj slumpartat en av de planeterna
        if (foundPlanets.size() > 1){
//          Functions.randomize(foundPlanets);
          Collections.shuffle(foundPlanets);
        }
        // sätt foundPlanet till den utslumpade planeten
        foundPlanet = foundPlanets.get(0);
      }else{
        Logger.finest("foundPlanets.size() == 0");
      }
      return foundPlanet;
    }


    public Spaceship findEmptyShipWithoutOrders(Player aPlayer, Planet aPlanet, String shipTypeName){
    	Spaceship foundShip = null;
    	List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet);
    	for (Spaceship aShip : shipsAtPlanet){
    		if (aShip.getName().equals(shipTypeName)){
    			if (!aPlayer.getOrders().checkShipMove(aShip)){ // if there are no order already for this ship
    				if (findAllVIPsOnShip(aShip).size() == 0){ // check that ship is empty (no carried VIPs)
    					foundShip = aShip;
    				}
    			}
    		}
    	}
    	return foundShip;	
    }
    
    /**
     * Used for VIP who should move toward a planet
     */
    public Planet findFirstMoveTowardsPlanet(Planet aLocation, Planet aDestination){
    	return findFirstJumpTowardsPlanet(aLocation, aDestination, SpaceshipRange.LONG);
    }

    /**
     * Used for VIP who should move toward a planet
     */
    public Planet findFirstJumpTowardsPlanet(Planet aLocation, Planet aDestination, Spaceship aSpaceship){
    	return findFirstJumpTowardsPlanet(aLocation, aDestination, aSpaceship.getRange());
    }

    public Planet findFirstJumpTowardsPlanet(Planet aLocation, Planet aDestination, SpaceshipRange aSpaceshipRange){
      Logger.finer("findFirstJumpTowardsPlanet aDestination: " + aDestination.getName());
      Planet firstStopPlanet = null;
      boolean found = false;
      // sätt reachFrom på startplaneten så den blir rotnod
      aLocation.setReachFrom(null);
      List<Planet> edgePlanets = new LinkedList<Planet>(); // de planeter som är på gränsen till det genomsökta området
      edgePlanets.add(aLocation);
      List<Planet> newEdgePlanets = new LinkedList<Planet>(); // de planeter som är på gränsen till det genomsökta området
      List<Planet> searchedPlanets = new LinkedList<Planet>();  // lägg in alla som genomsökts + startplaneten
      searchedPlanets.add(aLocation);
      List<Planet> allNeighbours;
      // loopa tills alla planeter har letats igenom eller minst 1 lämplig planet har hittats
      while (!found){
      	Logger.finest("while (!found) found: " + found);
        // Gå igenom alla edgePlanets
        for (int i = 0; i < edgePlanets.size(); i++){
          Planet tempPlanet = (Planet)edgePlanets.get(i);
          Logger.finest("tempPlanet: " + tempPlanet.getName());
          // Hämta alla grannar till tempPlanet
          allNeighbours = getAllDestinations(tempPlanet,aSpaceshipRange == SpaceshipRange.LONG);
          // Gå igenom alla allNeighbours  (lägg i newEdgePlanets)
          for (int j = 0; j < allNeighbours.size(); j++){
            Planet tempNeighbourPlanet = allNeighbours.get(j);
            Logger.finest("tempNeighbourPlanet: " + tempNeighbourPlanet.getName());
            // kolla att tempNeighbourPlanet inte redan finns i searchedPlanets
            if ((!Functions.containsPlanet(searchedPlanets,tempNeighbourPlanet)) & (!Functions.containsPlanet(newEdgePlanets,tempNeighbourPlanet))){
              Logger.finest("containsPlanet: " + !Functions.containsPlanet(searchedPlanets,tempNeighbourPlanet));
              Logger.finest("containsPlanet: " + !Functions.containsPlanet(newEdgePlanets,tempNeighbourPlanet));
              Logger.finest("inside if: ");
              // sätt reachFrom så det går att hitta pathen senare
              tempNeighbourPlanet.setReachFrom(tempPlanet);
              // lägg i newEdgePlanets
              newEdgePlanets.add(tempNeighbourPlanet);
              // kolla om det är den eftersökta planeten
              if(tempNeighbourPlanet == aDestination){
              	Logger.finest("found = true ");
                found = true;
              }
            }
          }
        }
        // töm edgePlanets
        edgePlanets.clear();
        for (int l = 0; l < newEdgePlanets.size(); l++){
          // kopiera över newEdgePlanets till edgePlanets
          edgePlanets.add((Planet)newEdgePlanets.get(l));
          // kopiera över newEdgePlanets till searchedPlanets
          searchedPlanets.add((Planet)newEdgePlanets.get(l));
        }
        // töm newEdgePlanets
        newEdgePlanets.clear();
      }
      Planet lastStop = aDestination;
      // loopa tills reachFrom är null
      Logger.finest("before while, lastStop: " + lastStop.getName());
      while (lastStop.getReachFrom().getReachFrom() != null){
      	Logger.finest("");
      	Logger.finest("inside if: " + lastStop.getReachFrom().getName());
      	Logger.finest("inside if: " + lastStop.getReachFrom().getReachFrom().getName());
        lastStop = lastStop.getReachFrom();
      }
      firstStopPlanet = lastStop;
      return firstStopPlanet;
  }
/*
  public void sendMessageToAllPlayers(Message aMessage){
    for (int i = 0; i < players.size(); i++){
      Player aPlayer = (Player)players.get(i);
      if ((aPlayer != aMessage.getSender(this)) & (!aPlayer.isDefeated())){
        aPlayer.addToLatestRecievedMessages(aMessage);
        aPlayer.addToHighlights(aMessage.getSender(this).getGovenorName(),Highlight.TYPE_MESSAGE_PUBLIC);
      }
    }
  }

  public void sendMessageToFactionPlayers(Message aMessage){
    for (int i = 0; i < players.size(); i++){
      Player aPlayer = (Player)players.get(i);
      if ((aPlayer != aMessage.getSender(this)) & (!aPlayer.isDefeated())){
        if (aPlayer.getFaction().getName().equalsIgnoreCase(aMessage.getRecipientFaction())){
          aPlayer.addToLatestRecievedMessages(aMessage);
          aPlayer.addToHighlights(aMessage.getSender(this).getGovenorName() + ";" + aMessage.getRecipientFaction(),Highlight.TYPE_MESSAGE_FACTION);
        }
      }
    }
  }
  */
    
  /**
   * Get numbers of player in a faction
   */  
  public int getFactionMemberNr(Faction aFaction){
  	int nr = 0;
    for (int i = 0; i < players.size(); i++){
        Player aPlayer = (Player)players.get(i);
        if (aPlayer.getFaction() == aFaction){
        	nr++;
        }
    }
  	return nr;
  }
  
  /**
   * Get numbers of player in a faction still alive.
   * @param aFaction
   * @return
   */
  public int getFactionLivingMemberNr(Faction aFaction){
	  	int nr = 0;
	    for (int i = 0; i < players.size(); i++){
	        Player aPlayer = (Player)players.get(i);
	        if (aPlayer.getFaction() == aFaction && !aPlayer.isDefeated()){
	        	nr++;
	        }
	    }
	  	return nr;
	  }
  
  
  
  public List<Player> getFactionMember(Faction aFaction){
	  	List<Player> factionPlayers = new ArrayList<Player>();
	    for (int i = 0; i < players.size(); i++){
	        Player aPlayer = (Player)players.get(i);
	        if (aPlayer.getFaction() == aFaction){
	        	factionPlayers.add(aPlayer);
	        }
	    }
	  	return factionPlayers;
	  }

  public int getUndefeatedFactionMemberNr(Faction aFaction){
  	int nr = 0;
    for (int i = 0; i < players.size(); i++){
        Player aPlayer = (Player)players.get(i);
        if (aPlayer.getFaction() == aFaction){
        	if (!aPlayer.isDefeated()){
        		nr++;
        	}
        }
    }
  	return nr;
  }

  public List<Faction> getFactions(){
    return factions;
  }

  public void setFactions(List<Faction> factions){
	  this.factions = factions;
  }

  public List<VIP> getAllVIPs(){
    return allVIPs;
  }

  public VIP findVIP(int aUniqueId){
    int index = 0;
    VIP found = null;
    while ((index < allVIPs.size()) & (found == null)){
      VIP tempVIP = (VIP)allVIPs.get(index);
      if (tempVIP.getId() == aUniqueId){
        found = tempVIP;
      }else{
        index++;
      }
    }
    return found;
  }

  public VIPType findVIPType(String vipTypeName){
	  int index = 0;
	  VIPType found = null;
	  while ((index < vipTypes.size()) & (found == null)){
		  VIPType tempVIPType = (VIPType)vipTypes.get(index);
//		  System.out.println(tempVIPType.getName() + " = " + vipTypeName);
		  if (tempVIPType.getName().equalsIgnoreCase(vipTypeName)){
			  found = tempVIPType;
		  }else{
			  index++;
		  }
	  }
	  return found;
  }

  public VIPType findVIPTypeShortName(String vipTypeShortName){
	  int index = 0;
	  VIPType found = null;
	  while ((index < vipTypes.size()) & (found == null)){
		  VIPType tempVIPType = (VIPType)vipTypes.get(index);
		  if (tempVIPType.getShortName().equalsIgnoreCase(vipTypeShortName)){
			  found = tempVIPType;
		  }else{
			  index++;
		  }
	  }
	  return found;
  }

  public SpaceshipType getRandomCommonShiptype(){
    SpaceshipType returnType = null;
    SpaceshipType tempShipType = null;
    List<SpaceshipType> allAvailableTypes = getShiptypesToBlackMarket();
    int totalFrequencypoint = 0;
    for (SpaceshipType spaceshipType : allAvailableTypes) {
    	totalFrequencypoint += spaceshipType.getBlackMarketFrequency().getFrequency();
	}
    
    int freqValue = Functions.getRandomInt(0,totalFrequencypoint-1);
    int counter = 0;
    int tmpFreqSum = 0;  
    while (returnType == null){
    	tempShipType = allAvailableTypes.get(counter);
		tmpFreqSum = tmpFreqSum + tempShipType.getBlackMarketFrequency().getFrequency();
		if (tmpFreqSum > freqValue){
			returnType = tempShipType;
		}
		counter++;
    }
    return returnType;
  }

  public TroopType getRandomCommonTroopType(){
	  TroopType aTroopType = null;
	  TroopType tempTroopType = null;
	  List<TroopType> allAvailableTroopTypes = getTroopTypesToBlackMarket();
	  int totalFrequencypoint = 0;
	  for (TroopType troopType : allAvailableTroopTypes) {
		  totalFrequencypoint += troopType.getBlackMarketFrequency().getFrequency();
	  }
	  
	  int freqValue = Functions.getRandomInt(0,totalFrequencypoint-1);
	    int counter = 0;
	    int tmpFreqSum = 0;  
	    while (aTroopType == null){
	    	tempTroopType = allAvailableTroopTypes.get(counter);
			tmpFreqSum = tmpFreqSum + tempTroopType.getBlackMarketFrequency().getFrequency();
			if (tmpFreqSum > freqValue){
				aTroopType = tempTroopType;
			}
			counter++;
	    }
	  
	  return aTroopType;
  }

  /**
   * Searches through all sst lists for all factions, limited by
   * the turn number (no medium+ on turn 1 etc)
   * @return
   */
  private List<SpaceshipType> getShiptypesToBlackMarket(){
	  Logger.fine("getShiptypesToBlackMarket() called");
	  List<SpaceshipType> ssTypes = new LinkedList<SpaceshipType>();
	  //LoggingHandler.fine("Faction: " + aFaction.getName(),this);
	  List<SpaceshipType> tmpSsTypes = getSpaceshipTypes();
	  //LoggingHandler.fine("Ships nr: " + tmpSsTypes.size(),this);
	  for (SpaceshipType aSST : tmpSsTypes) {
		  if(aSST.isReadyToUseInBlackMarket(this)){
			  ssTypes.add(aSST);
		  }
	  }
	  return ssTypes;
  }

  /**
   * Searches through all trooptype lists for all factions
   * @return list containing trooptypes. If a trooptype can be build by several factions it will appear several times in the list
   */
  private List<TroopType> getTroopTypesToBlackMarket(){
	  Logger.fine("getTroopTypesToBlackMarket() called");
	  List<TroopType> troopTypes = new LinkedList<TroopType>();
	  for (Faction aFaction : factions) {
		  Logger.finer("Faction: " + aFaction.getName());
		  List<TroopType> factionTroopTypes = aFaction.getTroopTypes();
		  Logger.finer("TroopTypes #: " + factionTroopTypes.size());
		  for (TroopType aTroopType : factionTroopTypes) {
			  Logger.finer("TT: " + aTroopType.getUniqueName());
			  
			  if (aTroopType.isReadyToUseInBlackMarket(this)){
				  if(!troopTypes.contains(aTroopType)){
					  troopTypes.add(aTroopType);
				  }
			  }
		  }
	  }
	  return troopTypes;
  }

  public VIPType getRandomVIPType(){
    VIPType returnType = null;
    Logger.finer("getRandomVIPType()");
    int totFrequencySum = getTotalVIPFrequencySum();
    Logger.finer(String.valueOf(totFrequencySum));
    int freqValue = Functions.getRandomInt(0,totFrequencySum-1);
    Logger.finer(String.valueOf(freqValue));
    returnType = getVipFromFrequency(freqValue);
    Logger.finer(returnType.getName());
    /*
    while (returnType == null){
      int index = Functions.getRandomInt(0,vipTypes.size()-1);
      returnType = (VIPType)vipTypes.elementAt(index);
      if (returnType.isGovernor()){
        returnType = null;
      }
    }
    */
    return returnType;
  }
  
  private int getTotalVIPFrequencySum(){
	  int total = 0;
	  for (VIPType aVipType : vipTypes) {
		  if (aVipType.isReadyToUseInBlackMarket(this)){
			  total = total + aVipType.getFrequency();
		  }
	  }	
	  return total;
  }
  
  private VIPType getVipFromFrequency(int freqValue){
	  VIPType aVipType = null;
	  int tmpFreqSum = 0;
	  VIPType tmpVipType = null;
	  int counter = 0;
	  Logger.finer(String.valueOf(freqValue));
	  while (aVipType == null){
		  tmpVipType = (VIPType)vipTypes.get(counter);
		  if (tmpVipType.isReadyToUseInBlackMarket(this)){
			  tmpFreqSum = tmpFreqSum + tmpVipType.getFrequency();
			  Logger.finest("tmpFreqSum: " + tmpFreqSum);
			  if (tmpFreqSum > freqValue){
				  aVipType = tmpVipType;
			  }
		  }
		  counter++;
	  }
	  return aVipType;
  }

  public void blackMarketNewTurn(){
    if (turn > 0){
      blackMarket.newTurn(this);
    }
  }

  public BlackMarketOffer findBlackMarketOffer(int aUniqueId){
    return blackMarket.findBlackMarketOffer(aUniqueId);
  }

  public void addBlackMarketBid(BlackMarketBid aBid){
    blackMarket.addBlackMarketBid(aBid);
  }

  public void performBlackMarket(){
    blackMarket.performBlackMarket(this);
  }

  // add Black Market message to all except aPlayer
  public void addBlackMarketMessages(Player aPlayer, String aMessage){
    for (int i = 0; i < players.size(); i++){
      Player tempPlayer = (Player)players.get(i);
      if ((aPlayer != tempPlayer) & (!tempPlayer.isDefeated())){
        tempPlayer.addToLatestBlackMarketMessages(aMessage);
      }
    }
  }

  public BlackMarket getBlackMarket(){
    return blackMarket;
  }
  
  public String getStatus(){
  	String retStr = "Active";
  	if (!lastUpdateComplete){
  		retStr = "Error";
  	}else
  	if (turn == 0){
  		retStr = "Starting";
  	}else
  	if (gameEnded){
  		retStr = "Game Over";
  	}
  	return retStr;
  }

  public Faction findPlayerFaction(String playerName){
    Faction foundFaction = null;
    int i = 0;
    while ((i < players.size()) & (foundFaction == null)){
      Player tempPlayer = (Player)players.get(i);
      if (tempPlayer.getName().equalsIgnoreCase(playerName)){
        foundFaction = tempPlayer.getFaction();
      }else{
        i++;
      }
    }
    return foundFaction;
  }
  
  public String getGameName(){
    return gameName;
  }

  public void setAutoBalance(boolean newAutoBalance){
	autoBalance = newAutoBalance;
  }
  
  public boolean getAutoBalance(){
	return autoBalance;
  }

  public void setHasAutoUpdated(boolean newValue){
	hasAutoUpdated = newValue;
  }

  public boolean hasAutoUpdated(){
	return hasAutoUpdated;
  }

  public void newPlayerPassword(String login, String newPassword){
  	for (Player aPlayer : players) {
		if (aPlayer.isPlayer(login)){
			Logger.fine("New password for player: " + login);
			aPlayer.setPassword(newPassword);
		}
	}
  }
  
  /**
   * Create highlights and general info to all other players
   * @param defeatedPlayer the player who was defeated, skip him
   */
  public void playerDefeated(Player defeatedPlayer){
  	for (Player aPlayer : players) {
		if (!aPlayer.isPlayer(defeatedPlayer)){
			Logger.finest("Create messages about defeated player");
			aPlayer.addToHighlights(defeatedPlayer.getGovenorName(),HighlightType.TYPE_DEFEATED_OTHER_PLAYER);
			aPlayer.addToGeneral("Governor " + defeatedPlayer.getGovenorName() + " has been defeated.");
			aPlayer.addToGeneral("");
		}
	}
  }
  
  /**
   * Create highlights (and general info?) to all other players
   * @param defeatedPlayer the player who was defeated, skip him
   */
  public void govenorKilled(Player defeatedPlayer){
  	for (Player aPlayer : players) {
		if (!aPlayer.isPlayer(defeatedPlayer)){
			Logger.finest("Create messages about killed governor");
//			aPlayer.addToHighlights(defeatedPlayer.getGovenorName(),Highlight.TYPE_GOVENOR_KILLED_OTHER_PLAYER);
			aPlayer.addToGeneral("Governor " + defeatedPlayer.getGovenorName() + " has been killed.");
			aPlayer.addToGeneral("");
		}
	}
  }
  
  /**
   * Create general info to all other players
   * @param defeatedPlayer the player who was defeated, skip him
   */
  public void noPlanetsOrShips(Player defeatedPlayer){
  	for (Player aPlayer : players) {
		if (!aPlayer.isPlayer(defeatedPlayer)){
			Logger.finest("Create messages about a player who has no planets or ships");
//			aPlayer.addToHighlights(defeatedPlayer.getGovenorName(),Highlight.TYPE_NO_SHIPS_NO_PLANETS_OTHER_PLAYER);
			aPlayer.addToGeneral("Governor " + defeatedPlayer.getGovenorName() + " has no planets or ships left.");
			aPlayer.addToGeneral("");
		}
	}
  }

  /**
   * Create general info to all other players
   * @param defeatedPlayer the player who was defeated, skip him
   */
  public void playerAbandonsGame(Player defeatedPlayer){
	  Logger.finest("Create messages about a player who has abandoned this game");
	  for (Player aPlayer : players) {
		  if (!aPlayer.isPlayer(defeatedPlayer)){
			  aPlayer.addToHighlights(defeatedPlayer.getGovenorName(),HighlightType.TYPE_GAME_ABANDONED_OTHER_PLAYER);
			  aPlayer.addToGeneral("Governor " + defeatedPlayer.getGovenorName() + " has left this quadrant (player " + defeatedPlayer.getName() + " has abandoned this game).");
			  aPlayer.addToGeneral("");
		  }
	  }
  }

  /**
   * Create general info to all other players
   * @param removedPlayer the player who was defeated, skip him
   */
  public void playerRemovedBrokeGame(Player removedPlayer){
	  Logger.finest("Create messages about a player who has been removed from this game due to being broke for 5 turns");
	  for (Player aPlayer : players) {
		  if (!aPlayer.isPlayer(removedPlayer)){
			  aPlayer.addToHighlights(removedPlayer.getGovenorName(),HighlightType.TYPE_GAME_BROKE_REMOVED_OTHER_PLAYER);
			  aPlayer.addToGeneral("Governor " + removedPlayer.getGovenorName() + " (player " + removedPlayer.getName() + ") has been broke for 5 turns and have lost the game.");
			  aPlayer.addToGeneral("");
		  }
	  }
  }

	public void setLastUpdateComplete(boolean lastUpdateComplete) {
		this.lastUpdateComplete = lastUpdateComplete;
	}

	public boolean getLastUpdateComplete() {
		return lastUpdateComplete;
	}
	
	public void addToLastLog(String addString){
		if (lastLog == null){
			lastLog = new StringBuffer(addString);
		}else{
			lastLog.append(addString);
		}
	}

	public void clearLastLog(){
		if (lastLog != null){
			lastLog = null;
		}
	}
	
	public String getLastLog(){
		String retVal = "Log is empty";
		if (lastLog != null){
			retVal = lastLog.toString();
		}
		return retVal;
	}
	
	public List<VIP> getAllDiplomatsOnNeutralPlanets(){
		List<VIP> allDiplomats = new LinkedList<VIP>();
	    for (int i = 0; i < allVIPs.size(); i++){
	        VIP tempVIP = (VIP)allVIPs.get(i);
	        if (tempVIP.isDiplomat() & !tempVIP.getBoss().isAlien()){ // aliens can not use diplomacy
	        	Planet tempLocation = tempVIP.getPlanetLocation();
	        	if (tempLocation != null){ // tempVIP är vid en planet
	        		if ((tempLocation.getPlayerInControl() == null) & !tempLocation.isRazed()){  // planeten är neutral
	        			allDiplomats.add(tempVIP);
	        		}
	        	}
	        }
	    }
		return allDiplomats;
	}
	
	public List<VIP> getPlayerDiplomatsOnPlanet(Planet aPlanet, Player aPlayer){
		List<VIP> diplomatsOnPlanet = new ArrayList<VIP>();
	    for (int i = 0; i < allVIPs.size(); i++){
	        VIP tempVIP = (VIP)allVIPs.get(i);
	        if (tempVIP.isDiplomat() & !tempVIP.getBoss().isAlien()){ // aliens can not use diplomacy
	        	Planet tempLocation = tempVIP.getPlanetLocation();
	        	if (tempLocation == aPlanet){ // tempVIP is on aPlanet
	        		if (tempVIP.getBoss() == aPlayer){ // tempVIP belongs to aPlayer
	        			diplomatsOnPlanet.add(tempVIP);
	        		}
	        	}
	        }
	    }
		return diplomatsOnPlanet;
	}

	public List<VIP> getAllInfestatorsOnPlanet(Planet aPlanet, Player aPlayer){
		Logger.finer("getAllInfestatorsOnPlanet() called");
		List<VIP> allInfestatorOnPlanet = new LinkedList<VIP>();
	    for (int i = 0; i < allVIPs.size(); i++){
	        VIP tempVIP = (VIP)allVIPs.get(i);
	        if (tempVIP.isInfestator() & tempVIP.getBoss().isAlien()){
	          Planet tempLocation = tempVIP.getPlanetLocation();
	          if (tempLocation == aPlanet){
	        	  if (tempVIP.getBoss() == aPlayer){
	        		  allInfestatorOnPlanet.add(tempVIP);
	        	  }
	          }
	        }
	    }
		return allInfestatorOnPlanet;
	}

	public List<VIP> getAllInfestatorsOnPlanets(){
		Logger.finer("getAllInfestatorsOnPlanets() called");
		List<VIP> allInfestators = new LinkedList<VIP>();
		Logger.finest("allVIPs.size(): " + allVIPs.size());
	    for (int i = 0; i < allVIPs.size(); i++){
	        VIP tempVIP = (VIP)allVIPs.get(i);
			Logger.finest("tempVIP: " + tempVIP.getName());
			Logger.finest("tempVIP.isInfestator(): " + tempVIP.isInfestator());
			Logger.finest("tempVIP.getBoss().isAlien(): " + tempVIP.getBoss().isAlien());
			Logger.finest("tempVIP.getAlignment(): " + tempVIP.getAlignment());
			Logger.finest("tempVIP.getBoss().getFaction().getAlignment(): " + tempVIP.getBoss().getFaction().getAlignment());
	        if (tempVIP.isInfestator() & tempVIP.getBoss().isAlien() & tempVIP.getAlignment().equals(tempVIP.getBoss().getFaction().getAlignment())){ // only infestators with the same alignment as a player can use infestation
	          Planet tempLocation = tempVIP.getPlanetLocation();
	          if (tempLocation != null){ // tempVIP är vid en planet
		          Logger.finest("tempLocation != null ");
		          Logger.finest("tempLocation: " + tempLocation.getName());
	        	  Player owner = tempLocation.getPlayerInControl();
	        	  if ((owner == null) || ((owner != tempVIP.getBoss()) & !owner.isAlien() & !(owner.getFaction().equals(tempVIP.getBoss().getFaction())))){  // planet is neutral or belongs to another non-alien player from another faction
			          Logger.finest("can infest");
	        		  if (tempLocation.getPopulation() > 0){ // can not infestate razed planets, troops are needed for that
	    		          Logger.finest("tempLocation.getPopulation() > 0");
	        			  allInfestators.add(tempVIP);
	        		  }
	        	  }
	          }
	        }
	    }
		return allInfestators;
	}

	public List<VIP> getAllGovsOnNeutralPlanets(){
		List<VIP> allGovs = new LinkedList<VIP>();
	    for (VIP tempVIP : allVIPs){
	        if (tempVIP.isGovernor()){
	          Planet tempLocation = tempVIP.getPlanetLocation();
	          if (tempLocation != null){ // Gov är vid en planet
	            if (tempLocation.getPlayerInControl() == null){  // planeten är neutral
	            	allGovs.add(tempVIP);
	            }
	          }
	        }
	    }
		return allGovs;
	}

	public List<VIP> getAllDiplomatsNotOnNeutralPlanets(){
		List<VIP> allDips = new LinkedList<VIP>();
	    for (int i = 0; i < allVIPs.size(); i++){
	        VIP tempVIP = (VIP)allVIPs.get(i);
	        if (tempVIP.isDiplomat()){
	          Planet tempLocation = tempVIP.getPlanetLocation();
	          if (tempLocation != null){ // Gov är vid en planet
	            if (tempLocation.getPlayerInControl() != null){  // planeten är inte neutral
	            	allDips.add(tempVIP);
	            }
	          }else{
	          	allDips.add(tempVIP);
	          }
	        }
	    }
		return allDips;
	}

	public List<VIP> getAllInfestatorsNotOnActionPlanets(){
		List<VIP> allInfs = new LinkedList<VIP>();
	    for (int i = 0; i < allVIPs.size(); i++){
	        VIP tempVIP = (VIP)allVIPs.get(i);
	        if (tempVIP.isInfestator()){
	        	Planet tempLocation = tempVIP.getPlanetLocation();
	        	if (tempLocation == null){ // inf is not at a planet
	        		allInfs.add(tempVIP);
	        	}else
	        	if (tempLocation.getPopulation() == 0){ // razed cannot be infed 
	        		allInfs.add(tempVIP);
	        	}else{
	        		Player owner = tempLocation.getPlayerInControl();
	        		if (owner == null){ // can inf neutral planet!
	        			// do nothing, ok to infestate!
	        		}else
	        		if (owner == tempVIP.getBoss()){ // cannot inf own planet
	        			allInfs.add(tempVIP);
	        		}else
	        		if (owner.isAlien()){ // cannot inf alien planets
	        			allInfs.add(tempVIP);
	        		}else
	        		if (owner.getFaction().equals(tempVIP.getBoss().getFaction())){ // cannot inf same factions planets
	        			allInfs.add(tempVIP);
	        		}
	        	}
	        }
	    }
		return allInfs;
	}

	public String getStartedByPlayer() {
		return startedByPlayer;
	}
	
	public void setStartedByPlayer(String startedByPlayer) {
		this.startedByPlayer = startedByPlayer;
	}
	
	public List<Spaceship> getHostileShipsAtPlanet(Planet aPlanet){
		List<Spaceship> shipsAtPlanet = new ArrayList<Spaceship>();
		for (Iterator<Spaceship> iter = spaceships.iterator(); iter.hasNext();) {
			Spaceship aShip = iter.next();
			if (aShip.getLocation() == aPlanet){
				if (aShip.getOwner() != aPlanet.getPlayerInControl()){
					shipsAtPlanet.add(aShip);
				}
			}
		}
		return shipsAtPlanet;
	}
	
	public GameWorld getGameWorld(){
		return gw;
	}

	public void setGameWorld(GameWorld aGameWorld){
		gw = aGameWorld;
	}

	public boolean isFTLMasterOnShip(Spaceship aShip){
		boolean found = false;
		for (int i = 0; i < allVIPs.size(); i++){
			VIP tempVIP = (VIP)allVIPs.get(i);
			if (tempVIP.getShipLocation() == aShip){
				if (tempVIP.isFTLbonus()){
					Orders orders = aShip.getOwner().getOrders();
			        if (orders.VIPWillStay(tempVIP)){
			        	found = true;
					}
				}
			}
		}
		return found;
	}
	
	public List<Faction> getActiveFactions(Faction exceptionFaction){
		List<Faction> allFactions = new ArrayList<Faction>();
		for (Faction aFaction : factions) {
			if (!aFaction.getName().equalsIgnoreCase(exceptionFaction.getName())){
				allFactions.add(aFaction);
			}
		}
		return allFactions;
	}
	
	public boolean isGameOver(){
		return gameEnded;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isGroupSameFaction() {
		return groupSameFaction;
	}

	public void setGroupSameFaction(boolean groupSameFaction) {
		this.groupSameFaction = groupSameFaction;
	}
	
	public String getMapNameFull(){
		return mapFullName;
	}
	
    public int getNoSquadronsAssignedToCarrier(Spaceship aCarrier){
    	int count = 0;
    	Player aPlayer = aCarrier.getOwner();
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer,aCarrier.getLocation());
    	for (Spaceship aSpaceship : shipsAtPlanet) {
			if (aSpaceship.getCarrierLocation() == aCarrier){
				// check if sstemp has a move order
				if (aPlayer != null){
					boolean moveToPlanetOrder = aPlayer.checkShipMove(aSpaceship);
					boolean moveToCarrierOrder = aPlayer.checkShipToCarrierMove(aSpaceship);
					// if not, inc counter
					if (!(moveToCarrierOrder | moveToPlanetOrder)){
						count++;
					}
				}else{
					count++;
				}
			}
		}
    	return count;
    }

    public int getNoTroopsAssignedToCarrier(Spaceship aCarrier){
    	int count = 0;
    	Player aPlayer = aCarrier.getOwner();
		List<Troop> troopsAtPlanet = getPlayersTroopsOnPlanet(aPlayer,aCarrier.getLocation());
    	for (Troop aTroop : troopsAtPlanet) {
			if (aTroop.getShipLocation() == aCarrier){
				// check if sstemp has a move order
				if (aPlayer != null){
					boolean moveOrder = aPlayer.checkTroopMove(aTroop);
					// if not, inc counter
					if (!moveOrder){
						count++;
					}
				}
			}
		}
    	return count;
    }

    public int getNoSquadronsMovingToCarrier(Spaceship aCarrier){
    	int count = 0;
    	Player aPlayer = aCarrier.getOwner();
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer,aCarrier.getLocation());
    	for (Spaceship aSpaceship : shipsAtPlanet) {
			if (aSpaceship.isSquadron()){
				// check if sstemp has a move order to the carrier
				if (aPlayer != null){
					boolean moveToCarrierOrder = aPlayer.checkShipToCarrierMove(aSpaceship,aCarrier);
					if (moveToCarrierOrder){
						count++;
					}
				}
			}
		}
    	return count;
    }

    public int getNrTroopsMovingToCarrier(Spaceship aCarrier){
    	int count = 0;
    	Player aPlayer = aCarrier.getOwner();
		List<Troop> troopsAtPlanet = getPlayersTroopsOnPlanet(aPlayer,aCarrier.getLocation());
    	for (Troop aTroop : troopsAtPlanet) {
    		// check if a has a move order to the carrier
    		if (aPlayer != null){
    			boolean moveToCarrierOrder = aPlayer.checkTroopToCarrierMove(aTroop,aCarrier);
    			if (moveToCarrierOrder){
    				count++;
    			}
    		}
		}
    	return count;
    }

	public List<Spaceship> getCarriersWithFreeSlotsInSystem(Planet aLocation,Player aPlayer){
		return getOtherCarriersWithFreeSlotsInSystem(aLocation, aPlayer, null); // call with null will return all carriers
	}

	/**
	 * 
	 * @param aLocation
	 * @param aPlayer
	 * @param aCarrier use null if all carriers should be returned
	 * @return
	 */
	public List<Spaceship> getOtherCarriersWithFreeSlotsInSystem(Planet aLocation,Player aPlayer, Spaceship aCarrier){
		List<Spaceship> carriersWithFreeSlots = new ArrayList<Spaceship>();
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer,aLocation);
		for (Spaceship spaceship : shipsAtPlanet) {
			if (spaceship.isCarrier() & (spaceship != aCarrier)){
				int maxSlots = spaceship.getSquadronCapacity();
				int slotsFull = getNoSquadronsAssignedToCarrier(spaceship);
				int sqdMovingToCarrier = getNoSquadronsMovingToCarrier(spaceship);
				if ((slotsFull + sqdMovingToCarrier) < maxSlots){
					carriersWithFreeSlots.add(spaceship);
				}
			}
		}
		return carriersWithFreeSlots;
	}

	/**
	 * Return all carriers with at least minFreeSlots free slots for troops
	 * @param aLocation
	 * @param aPlayer
	 * @param minFreeSlots
	 * @return
	 */
	public List<Spaceship> getCarriersWithFreeTroopSlotsInSystem(Planet aLocation,Player aPlayer,int minFreeSlots){
		List<Spaceship> carriersWithFreeSlots = new ArrayList<Spaceship>();
		List<Spaceship> shipsAtPlanet = getPlayersSpaceshipsOnPlanet(aPlayer,aLocation);
		for (Spaceship spaceship : shipsAtPlanet) {
			if (spaceship.isTroopCarrier()){
				int maxSlots = spaceship.getTroopCapacity();
				int slotsFull = getNoTroopsAssignedToCarrier(spaceship);
				int troopsMovingToCarrier = getNrTroopsMovingToCarrier(spaceship);
				if ((slotsFull + troopsMovingToCarrier + minFreeSlots) <= maxSlots){
					carriersWithFreeSlots.add(spaceship);
				}
			}
		}
		return carriersWithFreeSlots;
	}

	public List<String> getSelectableFactionNames() {
		return selectableFactionNames;
	}

	public void setSelectableFactionNames(List<String> selectableFactionNames) {
		this.selectableFactionNames = selectableFactionNames;
		Logger.finest( "this.selectableFactionNames: " + this.selectableFactionNames);
	}

	public void setAllFactionsSelectable(){
		selectableFactionNames = new LinkedList<String>();
		for (Faction aFaction : factions) {
			selectableFactionNames.add(aFaction.getName());
		}
	}
	
	public boolean isFactionSelectable(Faction aFaction){
		boolean found = false;
		int i = 0;
		while (!found & (i < selectableFactionNames.size())){
			if (aFaction.isFaction(selectableFactionNames.get(i))){
				found = true;
			}else{
				i++;
			}
		}
		return found;
	}

	public boolean isRandomFaction() {
		return randomFaction;
	}

	public void setRandomFaction(boolean randomFaction) {
		this.randomFaction = randomFaction;
	}
	
	public String getFactionListString(){
		StringBuffer sb = new StringBuffer();
		if (selectableFactionNames.size() == gw.getFactions().size()){
			sb.append("All");
		}else{
			for (String selFactionName : selectableFactionNames) {
				if (sb.length() > 0){
					sb.append(", ");
				}
				sb.append(selFactionName);
			}
		}
		return sb.toString();
	}
	
	public void checkDestroyBuildings(Planet aPlanet,Player aPlayer,boolean autoDestroy){
		if (autoDestroy | aPlayer.getPlanetOrderStatuses().isDestroyOrbitalBuildings(aPlanet.getName())){
			List<Building> removeBuildings = new ArrayList<Building>();
			List<Building> buildings = aPlanet.getBuildings();
			for (Building building : buildings){
				if (building.getBuildingType().isInOrbit()){
					removeBuildings.add(building);
				}
			}
			for (Building building : removeBuildings){
				// skriva meddelanden...
				aPlayer.getTurnInfo().addToLatestGeneralReport("You have destroyed a " + building.getBuildingType().getName() + " on " + aPlanet.getName() + ".");
				if (aPlanet.getPlayerInControl() != null){
					aPlanet.getPlayerInControl().getTurnInfo().addToLatestGeneralReport("Your " + building.getBuildingType().getName() + " on the " + aPlanet.getName() + " has been destroyed.");
				}
				aPlanet.removeBuilding(building.getUniqueId());
			}
		}
	}

    public void removeBuildingsDefeatedAlienPlayer(Player defeatedPlayer){
    	List<Planet> playerPlanets = getPlayersPlanets(defeatedPlayer);
       // PlanetInfos pi = defeatedPlayer.getPlanetInfos();
    	for (Planet aPlanet : playerPlanets) {
			aPlanet.setBuildings(new ArrayList<Building>());
		}
    }

    public void removeBuildingsOnPlanet(Planet aPlanet){
    	aPlanet.setBuildings(new ArrayList<Building>());
    }

    public int getSteps(){
    	return steps;
    }

    public List<Spaceship> getSquadronsNotInCarrier(Player aPlayer, Planet aPlanet){
    	List<Spaceship> playerSquadrons = new ArrayList<Spaceship>();
    	List<Spaceship> playerShips = getPlayersSpaceshipsOnPlanet(aPlayer, aPlanet);
    	for (Spaceship aShip : playerShips) {
    		if (aShip.isSquadron()){
    			if (aShip.getCarrierLocation() == null){
    				playerSquadrons.add(aShip);
    			}
    		}
    	}
    	return playerSquadrons;
    }
    
    public void checkAbandonedSquadrons(Player aPlayer){
  	  Logger.finest("checkAbandonedSquadron called, player: " + aPlayer.getName());
  	  boolean sqdSurvive = getGameWorld().isSquadronsSurviveOutsideCarriers();
  	  List<Spaceship> playerSpaceships = Functions.cloneList(getPlayersSpaceships(aPlayer));
  	  Collections.shuffle(playerSpaceships);
  	  List<Spaceship> removeShips = new LinkedList<Spaceship>();
  	  boolean addSpace = false;
  	  for (Spaceship aShip : playerSpaceships) {
  		if (aShip.isSquadron()){
  			if (aShip.getCarrierLocation() == null){ // sqd is not in a carrier
  				if (aShip.getLocation() == null){
  					// not at a planet, do nothing (attached to carrier?)
  				}else
  				if (aShip.getLocation().getPlayerInControl() == null){
  					if (!sqdSurvive){
  						// handle sqds auto moves to carriers
  						List<Spaceship> carriersWithFreeSlots = getCarriersWithFreeSlotsInSystem(aShip.getLocation(),aPlayer);
  						if (carriersWithFreeSlots.size() > 0){
  							Collections.shuffle(carriersWithFreeSlots);
  							Spaceship aCarrier = carriersWithFreeSlots.get(0);
  							aShip.setCarrierLocation(aCarrier);
  							aShip.setOldLocation(aShip.getLocation());
  							String oldLocString = aShip.getLocation().getName();
  							Logger.finer("CarrierLocation: " + aShip.getCarrierLocation());
  							if (aShip.getOwner() != null){
  								aPlayer.addToGeneral("Your sguadron " + aShip.getName() + " has been attached to a nearby carrier (" + aCarrier.getName() + ") in the system " + oldLocString + ".");
  							}
  						}else{
  							removeShips.add(aShip);
  						}
  						addSpace = true;
  					}else
  					// planet is neutral
  					if (!playerHasCarrierAtPlanet(aShip.getOwner(),aShip.getLocation())){
  						// add ship to remove list
  						removeShips.add(aShip);
  						addSpace = true;
  					}
  				}else // planet is not neutral, check if it belongs to another faction
  				if(aShip.getOwner().getFaction() != aShip.getLocation().getPlayerInControl().getFaction()){
  					if (!sqdSurvive){
  						// handle sqds auto moves to carriers
  						List<Spaceship> carriersWithFreeSlots = getCarriersWithFreeSlotsInSystem(aShip.getLocation(),aPlayer);
  						if (carriersWithFreeSlots.size() > 0){
  							Collections.shuffle(carriersWithFreeSlots);
  							Spaceship aCarrier = carriersWithFreeSlots.get(0);
  							aShip.setCarrierLocation(aCarrier);
  							aShip.setOldLocation(aShip.getLocation());
  							String oldLocString = aShip.getLocation().getName();
  							Logger.finer("Carrier Location: " + aCarrier);
  							if (aShip.getOwner() != null){
  								aPlayer.addToGeneral("Your sguadron " + aShip.getName() + " has been attached to a nearby carrier (" + aCarrier.getName() + ") in the system " + oldLocString + ".");
  							}
  							addSpace = true;
  						}else{
  							removeShips.add(aShip);
  							addSpace = true;
  						}
  					}else
  					if (!playerHasCarrierAtPlanet(aShip.getOwner(),aShip.getLocation())){
  						// add ship to remove list
  						removeShips.add(aShip);
  						addSpace = true;
  					}
  				}
  			}
  		}
  	  }
  	  for (Spaceship aShip : removeShips) {
  			// destroy ship
  			if (aShip.getOwner() != null){
  				aPlayer.addToGeneral("Your sguadron " + aShip.getName() + " has been scuttled by it's crew because they had no supporting carrier in the system " + aShip.getLocation().getName() + ".");
  			}
  			Player owner = aShip.getOwner();
  			if (owner != null) {
  				checkVIPsInDestroyedShips(aShip, owner);
  			}
  	        removeShip(aShip);
  	  }
  	  if (addSpace){
  		  aPlayer.addToGeneral("");
  	  }
    }

    public boolean playerHasCarrierAtPlanet(Player aPlayer, Planet aPlanet){
  	  boolean found = false;
  	  for (Iterator<Spaceship> iter = getSpaceships().iterator(); iter.hasNext();) {
  		Spaceship aShip = iter.next();
  		if (aShip.getOwner() == aPlayer){
  			if (aShip.getLocation() == aPlanet){
  				if (aShip.isCarrier()){
  					found = true;
  				}
  			}
  		}
  	  }
  	  return found;
    }

    public void addTroop(Troop aTroop){
    	troops.add(aTroop);
    }
    
    public List<TroopType> getTroopTypes(){
    	return troopTypes;
    }
    
    public boolean hasTroops(){
    	return gw.hasTroops();
    }
    
    public List<VIP> findLandBattleVIPs(Troop aTroop, boolean groupBonus){
		List<VIP> VIPs = new LinkedList<VIP>();
		for (int i = 0; i < allVIPs.size(); i++){
			VIP aVIP = (VIP)allVIPs.get(i);
			
			if (aVIP.isLandBattleVIP() & ( aVIP.getTroopLocation() != null && aVIP.getTroopLocation().getUniqueName().equalsIgnoreCase(aTroop.getUniqueName()))){
//				LoggingHandler.finest("findLandBattleVIPs; " + aVIP.getShortName() + " " + aVIP.getTroopAttacksBonus());
				VIPs.add(aVIP);
			}
		}
		return VIPs;
    }
    
    public List<VIPType> getVIPType(Alignment findAlignment){
    	List<VIPType> vipTypes = new LinkedList<VIPType>();
    	for (VIPType aVipType : gw.getVipTypes()) {
			if (aVipType.getAlignment() == findAlignment){
				vipTypes.add(aVipType);
			}
		}
    	return vipTypes;
    }

	public DiplomacyGameType getDiplomacyGameType() {
		return diplomacy.getDiplomacyGameType();
	}
	
	public void setPlayerDiplomacy(){
		diplomacy.setPlayerDiplomacy();
	}
	
	public void setDiplomacyGameType(DiplomacyGameType diplomacyGameType) {
		diplomacy.setDiplomacyGameType(diplomacyGameType);
	}

	public List<DiplomacyState> getDiplomacyStates() {
		return diplomacy.getDiplomacyStates();
	}
	
	public DiplomacyState getDiplomacyState(Player player1, Player player2){
		return diplomacy.getDiplomacyState(player1, player2);
	}
	
	public Planet getPlanet(String planetName){
		Logger.finer("getPlanet(String planetName) planetName : " + planetName);
		Planet aPlanet=null;
		for (Planet tempPlanet : planets) {
			if(tempPlanet.getName().equalsIgnoreCase(planetName)){
				aPlanet = tempPlanet;
				break;
			}
		}
		return aPlanet;
	}
	
	public Building findBuilding(int buildingId, Player aPlayer){
		List<Planet> planetList = getPlayersPlanets(aPlayer);
		
		for (Planet aPlanet : planetList) {
			Building aBuilding = aPlanet.getBuilding(buildingId);
			if(aBuilding != null){
				return aBuilding;
			}
		}
		return null;
	}
	
	public boolean isOngoingGroundBattle(Planet aPlanet, Player aPlayer){
		boolean playerHavetroop = false, nonPlayerTroop = false;
		for (Troop aTroop : troops) {
			if(aTroop.getPlanetLocation() == aPlanet){
				if(aTroop.getOwner() == aPlayer){
					playerHavetroop = true;
				}else{
					nonPlayerTroop = true;
				}
			}
		}
		return (playerHavetroop && nonPlayerTroop);
		
	}
	
	public boolean spaceshipTypeExist(SpaceshipType aSpaceshipType, Faction aFaction, Player aPlayer){
		List<Spaceship> spaceshipsToCheck;
		if(aPlayer != null){// playerUnique
			Logger.fine("spaceshipTypeExist: Player check");
			spaceshipsToCheck = getPlayersSpaceships(aPlayer);
		}else if(aFaction != null){// factionUnique
			spaceshipsToCheck = new ArrayList<Spaceship>();
			for (Player tempPlayer : players) {
				if(tempPlayer.getFaction().getName().equals(aFaction.getName())){
					spaceshipsToCheck.addAll(getPlayersSpaceships(tempPlayer));
				}
			}
		}else{// worldUnique
			spaceshipsToCheck = spaceships;
		}
		
		for (Spaceship spaceship : spaceshipsToCheck) {
			if(spaceship.getTypeName().equals(aSpaceshipType.getName())){
				Logger.fine("Shio exist: " + aSpaceshipType.getName());
				return true;
			}
		}
		
		return false;
	}
	
	public boolean troopTypeExist(TroopType aTroopType, Faction aFaction, Player aPlayer){
		boolean exist = false;
		List<Troop> troopsToCheck;
		if(aPlayer != null){// playerUnique
			troopsToCheck = getPlayersTroops(aPlayer);
		}else if(aFaction != null){// factionUnique
			troopsToCheck = new ArrayList<Troop>();
			for (Player tempPlayer : players) {
				if(tempPlayer.getFaction().getName().equals(aFaction.getName())){
					troopsToCheck.addAll(getPlayersTroops(tempPlayer));
				}
			}
		}else{// worldUnique
			troopsToCheck = troops;
		}
		
		for (Troop tempTroop : troopsToCheck) {
			if(tempTroop.getTroopType().getUniqueName().equals(aTroopType.getUniqueName())){
				exist = true;
			}
		}
		
		return exist;
	}
	
	public boolean vipTypeExist(VIPType aVIPType, Faction aFaction, Player aPlayer){
		boolean exist = false;
		List<VIP> vipsToCheck;
		if(aPlayer != null){// playerUnique
			vipsToCheck = getPlayersVips(aPlayer);
		}else if(aFaction != null){// factionUnique
			vipsToCheck = new ArrayList<VIP>();
			for (Player tempPlayer : players) {
				if(tempPlayer.getFaction().getName().equals(aFaction.getName())){
					vipsToCheck.addAll(getPlayersVips(tempPlayer));
				}
			}
		}else{// worldUnique
			vipsToCheck = allVIPs;
		}
		
		for (VIP tempVIP : vipsToCheck) {
			if(tempVIP.getTypeName().equals(aVIPType.getName())){
				exist = true;
			}
		}
		
		return exist;
	}
	
	public boolean buildingTypeExist(BuildingType aBuildingType, Faction aFaction, Player aPlayer){
		boolean exist = false;
		List<Planet> planetsToCheck;
		if(aPlayer != null){// playerUnique
			planetsToCheck = getPlayersPlanets(aPlayer);
		}else if(aFaction != null){// factionUnique
			planetsToCheck = new ArrayList<Planet>();
			for (Player tempPlayer : players) {
				if(tempPlayer.getFaction().getName().equals(aFaction.getName())){
					planetsToCheck.addAll(getPlayersPlanets(tempPlayer));
				}
			}
		}else{// worldUnique
			planetsToCheck = planets;	
		}
		for (Planet tempPlanet : planetsToCheck) {
			for (Building tempBuilding : tempPlanet.getBuildings()) {
				if(tempBuilding.getBuildingType().getName().equals(aBuildingType.getName())){
					exist = true;
				}
			}
		}		
		return exist;
	}

	public int getSingleVictory() {
		return singleVictory;
	}

	public void setSingleVictory(int singleVictory) {
		this.singleVictory = singleVictory;
	}

	public int getFactionVictory() {
		return factionVictory;
	}

	public void setFactionVictory(int factionVictory) {
		this.factionVictory = factionVictory;
	}

	public int getEndTurn() {
		return endTurn;
	}

	public void setEndTurn(int endTurn) {
		this.endTurn = endTurn;
	}

	public List<Faction> getLargestFactions(){
		List<Faction> largestFactions = new ArrayList<Faction>();
		int largestProd = 0;
		int tempLargest = 0;
		 
		for (Player aPlayer : players) {
			tempLargest = aPlayer.getFaction().getTotalPop();
			if (largestProd < tempLargest)
			{  
				largestProd = tempLargest;
			}
		}
		for (Player aPlayer : players) {
			if (largestProd == aPlayer.getFaction().getTotalPop())
			{  
				Faction tempFaction = aPlayer.getFaction();
				if(!largestFactions.contains(tempFaction)) {
					largestFactions.add(tempFaction);
				}
			}
		}
		
		return largestFactions;
	}

	public int getNumberOfStartPlanet() {
		return numberOfStartPlanet;
	}

	public void setNumberOfStartPlanet(int numberOfStartPlanet) {
		this.numberOfStartPlanet = numberOfStartPlanet;
	}
	
	public void setStartPlanets(GalaxyUpdater aGalaxyUpdater){
		if(numberOfStartPlanet > 1){
			int tempNumberOfStartPlanet = numberOfStartPlanet-1;
			List<Planet> neutralPlanets = new ArrayList<Planet>();
			List<Planet> neutralPlanetsRandom = new ArrayList<Planet>();
			neutralPlanets.addAll(getPlayersPlanets(null));
			if(neutralPlanets.size() < (players.size() * tempNumberOfStartPlanet)){
				tempNumberOfStartPlanet = neutralPlanets.size()/players.size();
			}
			
			int numbersOfNeutralPlanets = neutralPlanets.size() - (players.size() * tempNumberOfStartPlanet);
			for(int i=0; i < numbersOfNeutralPlanets; i++){// removing all planet that should be neutral.
				int shouldBeNeutral = Functions.getRandomInt(0, neutralPlanets.size()-1);
				neutralPlanets.remove(shouldBeNeutral);
			}
			
			List<Planet> razedPlanets = new ArrayList<Planet>();
			for (Planet planet : neutralPlanets) {
				if(planet.isRazed()){
					razedPlanets.add(planet);
				}
			}
			if(razedPlanets.size() > 0){
				GalaxyCreator.randomizeNeutralPlanets(razedPlanets, this, false);
			}
			
			while(neutralPlanets.size() > 0){
	    		int random = Functions.getRandomInt(0, neutralPlanets.size()-1);
	    		neutralPlanetsRandom.add(neutralPlanets.get(random));
	    		neutralPlanets.remove(random);
	    	}
			Collections.sort(neutralPlanetsRandom,new PlanetProdComparator());
			
			
			List<Player> tempPlayers = new ArrayList<Player>();
			List<Player> randomPlayers = new ArrayList<Player>();
	    	tempPlayers.addAll(players);
	    	
	    	while(tempPlayers.size() > 0){
	    		int random = Functions.getRandomInt(0, tempPlayers.size()-1);
	    		randomPlayers.add(tempPlayers.get(random));
	    		tempPlayers.remove(random);
	    	}
	    	
	    	int playerNr=0;
	    	for (Planet planet : neutralPlanetsRandom) {
	    		
	    		Player randomPlayer = randomPlayers.get(playerNr);
	    		if (randomPlayer.isAlien()){
	    			removeNeutralShips(planet);
	    			checkTroopsOnInfestedPlanet(planet, randomPlayer);
	    			planet.setProd(0);
	    			planet.setRes(1 + randomPlayer.getFaction().getResistanceBonus());
	    			planet.setHasNeverSurrendered(false);
	    			planet.setPlayerInControl(randomPlayer);
	    		}else{
	    			VIP guvenor = findVIPGovenor(randomPlayer);
	    			planet.joinsVisitingDiplomat(guvenor, false);
	    			aGalaxyUpdater.shipsJoinGovenor(planet,guvenor);
	    			aGalaxyUpdater.troopsJoinGovenor(planet, guvenor);
	    		}
	    		
	    		if(playerNr == randomPlayers.size()-1){
	    			playerNr = 0;
	    		}else{
	    			playerNr++;
	    		}
			}
			
			// slumpa planetern (så att det blir helt slumpade). Sortera planeterna efter prod.
			// slumpa spelarna. lopa sedan igenom planetlistan o dela ut planeterna.
			
		}
	}
	
	/**
	 * Adds a statistics value.
	 * Turnnumber will be handled automatically.
	 */
	public void addStatistics(StatisticType aStatisticType, Faction aFaction, int value){
		addStatistics(aStatisticType,aFaction.getName(),value);
	}

	/**
	 * Adds a statistics value.
	 * Turnnumber will be handled automatically.
	 */
	public void addStatistics(StatisticType aStatisticType, Player aPlayer, int value){
		addStatistics(aStatisticType,aPlayer.getName(),value);
	}

	public void addStatistics(StatisticType aStatisticType, Player aPlayer, int value, boolean cumulative){
		addStatistics(aStatisticType,aPlayer.getName(),value, cumulative);
	}

	public void addStatistics(StatisticType aStatisticType, String aPlayerName, int value){
		addStatistics(aStatisticType, aPlayerName, value, false);
	}

	public void addStatistics(StatisticType aStatisticType, String aPlayerName, int value, boolean cumulative){
		statisticsHandler.addStatistics(aStatisticType,aPlayerName,value,cumulative);
	}

	public StatisticsHandler getStatisticsHandler() {
		return statisticsHandler;
	}
	
	public boolean getFactionGame(){
		boolean factionGame = false; // if any faction have more than one player
		List<Faction> foundFactions = new LinkedList<Faction>();
		for (Player aPlayer : players) {
			if (foundFactions.contains(aPlayer.getFaction())){
				factionGame = true;
			}else{
				foundFactions.add(aPlayer.getFaction());
			}
		}
		return factionGame;
	}
	
	public Diplomacy getDiplomacy(){
		return diplomacy;
	}
	
	public boolean checkAllInConfederacyOrder(Player thePlayer, List<Player> confPlayers){
		int confCounter = 0;
		Logger.fine("thePlayer: " + thePlayer);
		for (Player confPlayer : confPlayers) {
			Logger.fine("confPlayer: " + confPlayer);
			if (confPlayer.haveConfOrder(thePlayer)){
				Logger.fine("confCounter: " + confCounter);
				confCounter++;
			}
		}
		return confCounter == confPlayers.size();
	}

	public boolean checkAllInConfederacyOffer(Player thePlayer, List<Player> confPlayers){
		int confCounter = 0;
		Logger.finer("thePlayer: " + thePlayer);
		for (Player confPlayer : confPlayers) {
			Logger.finer("confPlayer: " + confPlayer);
			if (confPlayer.haveConfOffer(thePlayer)){
				Logger.finer("confCounter: " + confCounter);
				confCounter++;
			}
		}
		return confCounter == confPlayers.size();
	}

	public boolean checkConfederacyOfferExist(Player thePlayer, List<Player> confPlayers){
		int confCounter = 0;
		Logger.finer("thePlayer: " + thePlayer);
		for (Player confPlayer : confPlayers) {
			Logger.finer("confPlayer: " + confPlayer);
			if (confPlayer.haveConfOffer(thePlayer)){
				Logger.finer("confCounter: " + confCounter);
				confCounter++;
			}
		}
		return confCounter > 0;
	}

	public List<DiplomacyState> getPostConfList() {
		return postConfList;
	}

	public void setPostConfList(List<DiplomacyState> postConfList) {
		this.postConfList = postConfList;
	}

	public void addPostConfList(DiplomacyState addToList) {
		postConfList.add(addToList);
	}

	/**
	 * find the VIP with the highest bombardmentbonus on any of the sips in the list
	 * @param allss a list of ships
	 */
	public VIP findHighestVIPbombardmentBonus(List<Spaceship> ships) {
		VIP foundVIP = null;
		int bombardmentBonus = 0;
		for (VIP aVIP : allVIPs) {
			if (aVIP.getBombardmentBonus() > 0){
				if (aVIP.getShipLocation() != null){
					Spaceship tempShip = aVIP.getShipLocation();
					if (ships.contains(tempShip)){
						int tmpBonus = aVIP.getBombardmentBonus();
						if (tmpBonus > bombardmentBonus){
							foundVIP = aVIP;
						}		
					}
				}
			}
		}
		return foundVIP;
	}

	public boolean vipCanBeUsed(VIPType aVIPType) {
		boolean found = false;
		int index = 0;
		List<Player> activePlayers = getActivePlayers();
		while (!found & (index < activePlayers.size())){
			Player aPlayer = activePlayers.get(index);
			if (aPlayer.getFaction().getAlignment().canHaveVip(aVIPType.getAlignment().getName())){
				found = true;
			}else{
				index++;
			}
		}
		return found;
	}

	public SpaceshipType getRandomShipBlueprint() {
		List<SpaceshipType> possibleShiptypes = new LinkedList<SpaceshipType>();
		for (SpaceshipType aSpaceshipType : spaceshipTypes) {
			boolean allhaveType = true;
			for (Player aPlayer : getActivePlayers()) {
				if (aPlayer.findOwnSpaceshipType(aSpaceshipType.getName()) == null){
					allhaveType = false;
				}
			}
			if (!allhaveType){
				if(aSpaceshipType.isBluePrintReadyToUseInBlackMarket(this)){
					possibleShiptypes.add(aSpaceshipType);
				}
			}
		}
		
		SpaceshipType randomShiptype = null;
		SpaceshipType tempShipType = null;
		
		if (possibleShiptypes.size() > 0){
			int totalFrequencypoint = 0;
		    for (SpaceshipType spaceshipType : possibleShiptypes) {
		    	totalFrequencypoint += spaceshipType.getBluePrintFrequency().getFrequency();
			}
			
		    int freqValue = Functions.getRandomInt(0,totalFrequencypoint-1);
		    int counter = 0;
		    int tmpFreqSum = 0;  
		    while (randomShiptype == null){
		    	tempShipType = possibleShiptypes.get(counter);
				tmpFreqSum = tmpFreqSum + tempShipType.getBluePrintFrequency().getFrequency();
				if (tmpFreqSum > freqValue){
					randomShiptype = tempShipType;
				}
				counter++;
		    }
		}
		return randomShiptype;
	}

	// Used to remove factions for Droid client
	public void removeFactions() {
		factions = null;
	}

    public void pruneDroid(Player thePlayer){
    	// clear ships from other players
    	List<Spaceship> playersShips = new ArrayList<Spaceship>();
    	for (Spaceship aSpaceship : spaceships) {
			if (aSpaceship.getOwner() == thePlayer){
				playersShips.add(aSpaceship);
			}
		}
    	spaceships = playersShips;
    	// clear buildings from other players
    	for (Planet aPlanet : planets) {
			if (aPlanet.getPlayerInControl() != thePlayer){
				aPlanet.pruneDroid();
			}
		}
    	// clear VIPs from other players
    	List<VIP> playersVIPs = new ArrayList<VIP>();
    	for (VIP aVIP : allVIPs) {
			if (aVIP.getBoss() == thePlayer){
				playersVIPs.add(aVIP);
			}
		}
    	allVIPs = playersVIPs;
    	// prune all other players
        Logger.fine("prune all other players, thePlayer: " + thePlayer.getName());
    	for (Player aPlayer : players) {
            Logger.fine("aPlayer: " + aPlayer.getName());
			if (aPlayer != thePlayer){
		        Logger.fine("aPlayer != thePlayer -> PRUNING!");
				aPlayer.pruneOtherPlayerDroid();
			}
		}
    	// clear other stuff
    	lastLog = null;
    	selectableFactionNames = null;
    	// kan connections tas bort?
    }

    
	public boolean checkNoPlanet(Player aPlayer){
		boolean noPlanet = true;
		for (int i = 0; i < planets.size();i++){
			Planet p = (Planet)planets.get(i);
			if (p.getPlayerInControl() == aPlayer){
				noPlanet = false;
			}
		}
		return noPlanet;
	}
	
}