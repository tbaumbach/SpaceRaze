package sr.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import sr.enums.DiplomacyLevel;
import sr.enums.HighlightType;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.diplomacy.DiplomacyChange;
import sr.world.diplomacy.DiplomacyOffer;
import sr.world.landbattle.report.LandBattleReports;
import sr.world.mapinfo.MapInfos;
import sr.world.orders.Expense;
import sr.world.orders.Orders;
import sr.world.orders.VIPMovement;
import sr.world.spacebattle.ReportLevel;
import sr.world.spacebattle.SpaceBattleReports;

public class Player implements Serializable{
    static final long serialVersionUID = 1L;
    private List<SpaceshipType> spaceshipTypes,otherSpaceshipTypes; // ,recievedMessages
    private List<TroopType> troopTypes,otherTroopTypes;
    private List<VIPType> vipTypes;
    private String name,password,govenorName;
    private Orders orders;
    private Galaxy g;
    private TurnInfo ti;
    private String errormessage = null, notes = "";
    private boolean defeated = false;
    private boolean win = false;
	private boolean updatedThisTurn = false; // saved this turn
    private boolean finishedThisTurn = false; // ok for server to update
    private String nextupdate;  // flytta till PublicInfo?
    private int treasury,totalPop;
    private Faction faction;
    private String factionName; // needed for Droid client to recreate faction objects
    private PlanetInfos pi;
    private Planet homeplanet;
//    public boolean writeUpkeep = false;
    private boolean retreatingGovenor = false;
    private int turnDefeated;
    private int nrTurnsBroke;
    private Research research; 
    private Buildings buildings;
    private List<DiplomacyOffer> diplomacyOffers; // current offers for the current turn 
    
    // Copy from Faction
    private int openPlanetBonus = 0,closedPlanetBonus = 0,resistanceBonus = 0;
    private int techBonus = 0; // %
    private boolean canReconstruct = false;
    private int reconstructCostBase = 8;
    private Corruption corruption;
    private int latestMessageIdFromServer = 0;
    private int messageId;
    private ReportLevel reportLevel;
    private PlanetOrderStatuses planetOrderStatuses,originalPlanetOrderStatuses;
    private MapInfos mapPlanetInfos;
    
    private int income = -1, upkeepTroops = -1, upkeepVIPs = -1, upkeepShips = -1;
    
    public Player(String errormessage){
        this.errormessage = errormessage;
    }

    public Player(String errormessage, Galaxy ag){
    	this(errormessage);
    	g = ag;
    }

    public Player(String name, String password, Galaxy g, String govenorName, String factionName, Planet homeplanet){
    	Logger.fine("Name: " + name + " govenorName: " + govenorName +  " factionName: " + factionName);
        this.name = name;
        this.password = password;
        this.govenorName = govenorName.replace('#',' ');
        spaceshipTypes = new LinkedList<SpaceshipType>();
        otherSpaceshipTypes = new LinkedList<SpaceshipType>();
        troopTypes = new ArrayList<TroopType>();
        otherTroopTypes = new ArrayList<TroopType>();
        vipTypes = new ArrayList<VIPType>();
        orders = new Orders();
        this.g = g;
        ti = new TurnInfo();
        treasury = 0;
        faction = g.findFaction(factionName);
        this.homeplanet = homeplanet;
        research = faction.getResearch().clone();
        buildings = faction.getBuildings().clone();
        
        //      Copy from Faction
        openPlanetBonus = faction.getOpenPlanetBonus();
        closedPlanetBonus = faction.getClosedPlanetBonus();
        resistanceBonus = faction.getResistanceBonus();
       /* wharfBuildCost = faction.getWharfBuildCost();
        wharfUpgradeCostMedium = faction.getWharfUpgradeCostMedium(); // if maxint cannot upgrade
        wharfUpgradeCostLarge = faction.getWharfUpgradeCostLarge();
        wharfUpgradeCostHuge = faction.getWharfUpgradeCostHuge();*/
        techBonus = faction.getTechBonus(); // %
       // orbitalStructure = new SpaceStation(faction.getOrbitalStructure());
     /*   buildOrbitalStructureCostBase = faction.getBuildOrbitalStructureCostBase();
        buildOrbitalStructureCostMulitplier = faction.getBuildOrbitalStructureCostMulitplier();*/
        canReconstruct = faction.isCanReconstruct();
        reconstructCostBase = faction.getReconstructCostBase();
        // set values from faction
        Corruption tmpCorr = faction.getCorruption();
        if (tmpCorr != null){
        	corruption = tmpCorr.clone();
        }
        vipTypes = faction.getVIPTypes();
        reportLevel = ReportLevel.LONG;
        planetOrderStatuses = new PlanetOrderStatuses(g.getPlanets());
        mapPlanetInfos = new MapInfos();
    }
    
    public ReportLevel getReportLevel(){
    	return reportLevel;
    }

    public void setReportLevel(ReportLevel newReportLevel){
    	reportLevel = newReportLevel;
    }

    public void resetDiplomacyOffers(){
    	diplomacyOffers = new LinkedList<DiplomacyOffer>();
    }
    
    public void addDiplomacyOffer(DiplomacyOffer anOffer){
    	diplomacyOffers.add(anOffer);
    }
    
    public DiplomacyOffer findDiplomacyOffer(Player otherPlayer){
    	DiplomacyOffer foundOffer = null;
    	int counter = 0;
    	while ((foundOffer == null) & (counter < diplomacyOffers.size())){
    		DiplomacyOffer tmpOffer = diplomacyOffers.get(counter);
    		if (tmpOffer.isOtherPlayer(otherPlayer)){
    			foundOffer = tmpOffer;
    		}else{
    			counter++;
    		}
    	}
    	return foundOffer;
    }

    public PlanetInfos getPlanetInfos(){
      return pi;
    }

    public void setPlanetInfos(PlanetInfos newpi){
      this.pi = newpi;
    }

    public void createPlanetInfos(){
      pi = new PlanetInfos(g.getPlanets(),this);
    }

    public TurnInfo getTurnInfo(){
      return ti;
    }

    public void setTotalPop(int newPop){
      totalPop = newPop;
    }

    public int getTotalPop(){
      return totalPop;
    }

    public void setDefeated(boolean newValue){
      defeated = newValue;
    }

    public void defeated(boolean govenorDead,int turnDefeated){
      defeated = true;
      this.turnDefeated = turnDefeated;
      if (govenorDead){
        ti.addToLatestGeneralReport("Your Govenor has been killed.\n");
//        addToHighlights("",Highlight.TYPE_GOVENOR_KILLED); // Not needed, highlight already created in other places (vip killed in ship or planet)
        g.govenorKilled(this);
      }else{
        ti.addToLatestGeneralReport("You have no planets or spaceships left.\n");
        addToHighlights("",HighlightType.TYPE_NO_SHIPS_NO_PLANETS);
        g.noPlanetsOrShips(this);
      }
      ti.addToLatestGeneralReport("You have been defeated.\n");
      addToHighlights("",HighlightType.TYPE_DEFEATED);
      g.playerDefeated(this);
    }

    public void abandonGame(int turnDefeated){
        defeated = true;
        this.turnDefeated = turnDefeated;
        ti.addToLatestGeneralReport("You have abandoned this game and your Govenor have left this quadrant.\n");
        ti.addToLatestGeneralReport("Game is over.\n");
        addToHighlights("",HighlightType.TYPE_GAME_ABANDONED);
        g.playerAbandonsGame(this);
    }

    public void brokeRemovedFromGame(int turnRemoved){
        defeated = true;
        this.turnDefeated = turnRemoved;
        ti.addToLatestGeneralReport("You have been broke for 5 turns in a row and government have broken down.\n");
        ti.addToLatestGeneralReport("Game is over.\n");
        addToHighlights("",HighlightType.TYPE_GAME_BROKE_REMOVED);
        g.playerRemovedBrokeGame(this);
    }

    public void brokeRemovedWarning(){
        ti.addToLatestGeneralReport("Warning: you have been broke for 4 turns in a row and will lose the game if you are broke one more turn.\n");
        addToHighlights("",HighlightType.TYPE_GAME_BROKE_WARNING);
    }

    public boolean isDefeated(){
      return defeated;
    }

    public void setNextUpdate(String newtime){
        nextupdate = newtime;
    }

    public String getNextUpdate(){
        return nextupdate;
    }

    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }

    public List<SpaceshipType> getSpaceshipTypes(){
      return spaceshipTypes;
    }
    
    public Vector<VIPType> getAvailableVIPTypes(){
    	Vector<VIPType> tempVIPTypes = new Vector<VIPType>(); 
    	
    	for(int i=0; i < vipTypes.size();i++){
    		//LoggingHandler.fine("(vipTypes.get(i)).isAvailableToBuild() " + (vipTypes.get(i)).isAvailableToBuild());
    		if((vipTypes.get(i)).isConstructible(this)){
    			tempVIPTypes.add(vipTypes.get(i));
    		//	LoggingHandler.fine("tempVIPTypes.add()= " + (vipTypes.get(i)).getName());
    		}
    	}
    	return tempVIPTypes;
    }
    
    public Vector<SpaceshipType> getAvailableSpaceshipTypes(){
    	Vector<SpaceshipType> tempShipTypes = new Vector<SpaceshipType>(); 
    	
    	for(int i=0; i < spaceshipTypes.size();i++){
    		if((spaceshipTypes.get(i)).isConstructible(this)){
    			tempShipTypes.add((SpaceshipType)spaceshipTypes.get(i));
    		//	LoggingHandler.fine("tempShipTypes.add()= " + ((SpaceshipType)spaceshipTypes.get(i)).getName());
    		}
    	}
    	return tempShipTypes;
    }
    
    public Vector<TroopType> getAvailableTroopTypes(){
    	Vector<TroopType> tempTroopTypes = new Vector<TroopType>(); 
    	
    	for(int i=0; i < troopTypes.size();i++){
    	//	LoggingHandler.fine("(troopTypes.get(i)).isAvailableToBuild() " + (troopTypes.get(i)).isCanBuild());
    		if(((TroopType)troopTypes.get(i)).isConstructible(this)){
    			tempTroopTypes.add(troopTypes.get(i));
    	//		LoggingHandler.fine("tempTroopTypes.add()= " + (troopTypes.get(i)).getUniqueName());
    		}
    	}
    	return tempTroopTypes;
    }
    
    public Vector<BuildingType> getAvailableNewBuildings(Planet aPlanet){
    	Vector<BuildingType> tempBuildingTypes = buildings.getAvailableNewBuildings(aPlanet); 
    	
    	return tempBuildingTypes;
    	
    }

    public boolean isPlayer(String name, String password){
      return name.equalsIgnoreCase(this.name) & password.equalsIgnoreCase(this.password);
    }

    // enklare variant av isPlayer som ej används vid inloggning
    public boolean isPlayer(String name){
      return name.equalsIgnoreCase(this.name);
    }
    
    public boolean isPlayer(Player anotherPlayer){
    	return name.equalsIgnoreCase(anotherPlayer.getName());
    }

    public boolean isPlayerByGovenorName(String aGovenorName){
      return aGovenorName.equalsIgnoreCase(this.govenorName);
    }

    public String getErrorMessage(){
        return errormessage;
    }

    public void removeSpaceshipType(SpaceshipType sst){
    	spaceshipTypes.remove(sst);
    }
    
    public void addSpaceshipType(SpaceshipType sst){
        spaceshipTypes.add(new SpaceshipType(sst));
    }

    public void removeOtherSpaceshipType(SpaceshipType sst){
    	SpaceshipType foundSST = findOtherSpaceshipType(sst.getName());
    	otherSpaceshipTypes.remove(foundSST);
    }

    public void setOrders(Orders newOrders){
      orders = newOrders;
    }

    public Orders getOrders(){
      return orders;
    }

    public Galaxy getGalaxy(){
      return g;
    }

    public void addToGeneral(String str){
      ti.addToLatestGeneralReport(str);
    }

    public void addToGeneralAt(String str, int index){
    	ti.addToLatestGeneralReportAt(str,index);
    }

    public void addToHighlights(String str, HighlightType type){
        ti.addToLatestHighlights(str,type);
      }

    public void addToShipsLostInSpace(Spaceship ss){
        ti.addToLatestShipsLostInSpace(ss);
    }

    public void addToTroopsLostInSpace(Troop aTroop){
        ti.addToLatestTroopsLostInSpace(aTroop);
    }

    public void addToLatestBlackMarketMessages(String aMessage){
      ti.addToLatestBlackMarketReport(aMessage);
    }

    public void addToVIPReport(String str){
      ti.addToLatestVIPReport(str);
    }

    public void performOrders(){
      orders.performOrders(ti,this);
    }

    public void performDiplomacyOrders(){
    	orders.performDiplomacyOrders(this);
    }

    public void updateTurnInfo(){
      ti.newTurn();
    }

    public void addToTreasury(int money){
      treasury = treasury + money;
    }

    public void setTreasury(int money){
      treasury = money;
    }

    public void removeFromTreasury(int money){
      treasury = treasury - money;
      if (treasury < 0){
        treasury = 0;
      }
    }

    public int getTreasury(){
      return treasury;
    }

    public boolean isBroke(){
    	return (g.getPlayerUpkeepShips(this) + g.getPlayerUpkeepTroops(this)) + g.getPlayerUpkeepVIPs(this)> (treasury + g.getPlayerIncome(this,false));
    }
    // used in client to just count first time
    public boolean isBrokeClient(){
    	return (upkeepShips() + upkeepTroops() + upkeepVIPs())> (treasury + income());
    }
    // used in client to just count first time
    public int upkeepShips(){
    	if(upkeepShips == -1){
    		upkeepShips = g.getPlayerUpkeepShips(this);
    	}
    	return upkeepShips;
    }
    // used in client to just count first time
    public int upkeepTroops(){
    	if(upkeepTroops < 0){
    		upkeepTroops = g.getPlayerUpkeepTroops(this);
    	}
    	return upkeepTroops;
    }
    // used in client to just count first time
    public int upkeepVIPs(){
    	if(upkeepVIPs < 0){
    		upkeepVIPs = g.getPlayerUpkeepVIPs(this);
    	}
    	return upkeepVIPs;
    }
    // used in client to just count first time
    public int income(){
    	if(income < 0){
    		income = g.getPlayerIncome(this,false);
    	}
    	return income;
    }

    public int getSum(){
    	Logger.finer("upkeepShips();" + upkeepShips());
    	Logger.finer("upkeepTroops();" + upkeepTroops());
    	Logger.finer("upkeepVIPs();" + upkeepVIPs());
    	Logger.finer("income();" + income());
    	Logger.finer("orders.getExpensesCost();" + orders.getExpensesCost(g));
    	Logger.finer("treasury;" + treasury);
    	int tmpIncome = treasury - upkeepShips() - upkeepTroops() - upkeepVIPs() + income();
//    	if (tmpIncome > 0){ // check taxes
//    		DiplomacyState lordState = g.getDiplomacy().isVassal(this);
//    		if (lordState != null){
//    			tmpIncome -= lordState.getTax();
//    			if (tmpIncome < 0){
//    				tmpIncome = 0;
//    			}
//    		}
//    	}
    	tmpIncome -= orders.getExpensesCost(g);
    	return tmpIncome;
    }

    public int getLeftToSpend(){
    	int tmpIncome = treasury - g.getPlayerUpkeepShips(this) - g.getPlayerUpkeepTroops(this) - g.getPlayerUpkeepVIPs(this) + g.getPlayerIncome(this,false);
    	tmpIncome -= orders.getExpensesCost(g);
    	return tmpIncome;
    }

    /*
    public int getstartSum(){
    	return treasury - g.getPlayerUpkeepShips(this) - g.getPlayerUpkeepTroops(this) - g.getPlayerUpkeepVIPs(this) + g.getPlayerIncome(this);
    }*/

    public void addOrRemovePlanetVisib(Planet p){
      orders.addOrRemovePlanetVisib(p);
    }

    public void addOrRemoveAbandonPlanet(Planet p){
      orders.addOrRemoveAbandonPlanet(p);
    }

    public void addNewBuilding(Planet aPlanet, BuildingType bt){
      orders.addNewBuilding(aPlanet, getName(), bt, g);
    }
    
  /*  public void removeNewBuilding(Planet aPlanet, BuildingType bt){
        orders.removeNewBuilding(aPlanet, bt);
      }*/
    public void removeNewBuilding(Planet aPlanet){
        orders.removeNewBuilding(aPlanet, this.getGalaxy());
      }
    /*
    public void removeNewWharf(Planet aPlanet, BuildingType bt){
      orders.removeNewBuilding(aPlanet, bt);
    }
    
    public void addNewWharf(Planet aPlanet, Galaxy g){
        orders.addNewWharf(aPlanet,g,this);
      }

      public void removeNewWharf(Planet aPlanet){
        orders.removeNewWharf(aPlanet);
      }
	
    public void addNewSpaceStation(Planet aPlanet, Galaxy g){
    	orders.addNewSpaceStation(aPlanet,g,this);
    }

    public void removeNewSpaceStation(Planet aPlanet){
    	orders.removeNewSpaceStation(aPlanet);
    }
	*/
    public void addReconstruct(Planet aPlanet){
    	orders.addReconstruct(aPlanet,this);
    }

    public void removeReconstruct(Planet aPlanet){
    	orders.removeReconstruct(aPlanet, this.getGalaxy());
    }

    public void addIncPop(Planet aPlanet){
      orders.addIncPop(aPlanet,this);
    }

    public void removeIncPop(Planet aPlanet){
      orders.removeIncPop(aPlanet, this.getGalaxy());
    }

    public void addIncRes(Planet aPlanet){
      orders.addIncRes(aPlanet,this);
    }

    public void removeIncRes(Planet aPlanet){
      orders.removeIncRes(aPlanet, this.getGalaxy());
    }

    public void addShipMove(Spaceship ss, Planet destination){
      orders.addNewShipMove(ss,destination);
    }

    public void addTroopToPlanetMove(Troop aTroop, Planet destination){
    	orders.addNewTroopToPlanetMove(aTroop,destination,g.getTurn());
    }

    public void addTroopToCarrierMove(Troop aTroop, Spaceship destinationCarrier){
    	orders.addNewTroopToCarrierMove(aTroop,destinationCarrier);
    }

    public void addShipToCarrierMove(Spaceship ss, Spaceship destinationCarrier){
    	orders.addNewShipToCarrierMove(ss,destinationCarrier);
    }

    public void addVIPMove(VIP aVIP,Object destination){
      orders.addNewVIPMove(aVIP,destination);
    }

    public String getShipDestinationName(Spaceship ss){
      return orders.getDestinationName(ss, g);
    }

    public String getTroopDestinationPlanetName(Troop aTroop){
    	return orders.getDestinationPlanetName(aTroop, g);
    }

    public String getShipDestinationCarrierName(Spaceship ss){
        return orders.getDestinationCarrierName(ss, g);
    }

    public String getShipDestinationCarrierShortName(Spaceship ss){
        return orders.getDestinationCarrierShortName(ss, g);
    }

    public String getTroopDestinationCarrierName(Troop aTroop){
        return orders.getTroopDestinationCarrierName(aTroop, g);
    }

    public String getTroopDestinationCarrierShortName(Troop aTroop){
        return orders.getTroopDestinationCarrierShortName(aTroop, g);
    }

    public String getVIPDestinationName(VIP aVIP,boolean longName){
      return orders.getDestinationName(aVIP, g, longName);
    }

    public List<String> getAllDestinations(Planet location, boolean longRange){
    	return getAllDestinations(location,longRange,false);
    }

    public List<String> getAllDestinations(Planet location, boolean longRange, boolean ownPlanetsOnly){
        return g.getAllDestinationsStrings(location,longRange,this,ownPlanetsOnly);
      }

    public void addBuildShip(Building building, SpaceshipType sst){
      orders.addBuildShip(building, sst, this);
    }

    public void removeUpgradeBuilding(Building building){
      orders.removeUpgradeBuilding(building, this.getGalaxy());
    }

    public void addUppgradeBuilding(Building currentBuilding, BuildingType newBuilding){
      orders.addUppgradeBuilding(currentBuilding, newBuilding, this);
    }

    public void setAbandonGame(boolean abandonGame){
        orders.setAbandonGame(abandonGame);
    }
    
    public boolean isAbandonGame(){
    	return orders.isAbandonGame();
    }

    public PublicInfo getLastPublicInfo(){
      return g.getLastPublicInfo();
    }

    public SpaceshipType findOwnSpaceshipType(String findname){
    	Logger.finer("findname: " + findname);
    	SpaceshipType sst = null;
    	int i = 0;
    	while ((sst == null) & (i<spaceshipTypes.size())){
    		SpaceshipType temp = spaceshipTypes.get(i);
    		Logger.finest("in while: " + temp.getName());
    		if (temp.getName().equalsIgnoreCase(findname)){
    			sst = temp;
    		}else{
    			i++;
    		}
    	}
    	return sst;
    }

    public SpaceshipType findSpaceshipType(String findname){
      SpaceshipType sst = null;
      sst = findOwnSpaceshipType(findname);
      if (sst == null){
    	  sst = findOtherSpaceshipType(findname);
      }
      return sst;
    }

    public TroopType findTroopType(String findname){
        TroopType tt = null;
        int i = 0;
        while ((tt == null) & (i < troopTypes.size())){
          TroopType temp = troopTypes.get(i);
          if (temp.getUniqueName().equalsIgnoreCase(findname)){
            tt = temp;
          }else{
            i++;
          }
        }
        return tt;
      }
    
    public VIPType findVIPType(String findname){
        Logger.fine("findname: " + findname + " vipTypes.size(): " + vipTypes.size());
        VIPType tt = null;
        int i = 0;
        while ((tt == null) & (i < vipTypes.size())){
        	VIPType temp = vipTypes.get(i);
        	Logger.fine("temp.getName(): " + temp.getName());
        	if (temp.getName().equalsIgnoreCase(findname)){
        		Logger.fine("found!");
        		tt = temp;
        	}else{
        		i++;
        	}
        }
        return tt;
    }
    
    public BuildingType findBuildingType(String findname){   	
    	return buildings.getBuildingType(findname);
    }

    private SpaceshipType findOtherSpaceshipType(String findname){
        SpaceshipType sst = null;
        int i = 0;
        while ((sst == null) & (i<otherSpaceshipTypes.size())){
          SpaceshipType temp = otherSpaceshipTypes.get(i);
          if (temp.getName().equalsIgnoreCase(findname)){
            sst = temp;
          }else{
            i++;
          }
        }
        return sst;
      }

    public String getGovenorName(){
      return govenorName;
    }

    public Faction getFaction(){
      return faction;
    }

    public void addShipSelfDestruct(Spaceship currentss){
      orders.addShipSelfDestruct(currentss);
    }

    public void removeShipSelfDestruct(Spaceship currentss){
      orders.removeShipSelfDestruct(currentss);
    }

    public void addTroopSelfDestruct(Troop aTroop){
    	orders.addTroopSelfDestruct(aTroop);
    }

    public void removeTroopSelfDestruct(Troop aTroop){
    	orders.removeTroopSelfDestruct(aTroop);
    }

    public void addBuildingSelfDestruct(Building building){
      orders.addBuildingSelfDestruct(building);
    }

    public void removeBuildingSelfDestruct(Building building){
      orders.removeBuildingSelfDestruct(building);
    }

    public boolean getShipSelfDestruct(Spaceship ss){
      return orders.getShipSelfDestruct(ss);
    }

    public boolean getTroopSelfDestruct(Troop aTroop){
        return orders.getTroopSelfDestruct(aTroop);
    }

    public boolean getBuildingSelfDestruct(Building building){
      return orders.getBuildingSelfDestruct(building);
    }

    public Planet getHomeplanet(){
      return homeplanet;
    }

  public boolean checkMove(Spaceship ss){
	  return checkShipToCarrierMove(ss) | checkShipMove(ss);
  }
  
  public boolean checkShipMove(Spaceship ss){
    return orders.checkShipMove(ss);
  }

  public boolean checkVIPMove(VIP vip){
	  return orders.checkVIPMove(vip);
  }

  public boolean checkTroopToPlanetMove(Troop aTroop){
	  return orders.checkTroopToPlanetMove(aTroop);
  }

  public boolean checkTroopToCarrierMove(Troop aTroop){
	  return orders.checkTroopToCarrierMove(aTroop);
  }

  public boolean checkTroopToCarrierMove(Troop aTroop, Spaceship aCarrier){
	  return orders.checkTroopToCarrierMove(aTroop,aCarrier);
  }

  public boolean checkTroopMove(Troop aTroop){
	  boolean moving = false;
	  if (checkTroopToCarrierMove(aTroop)){
		  moving = true;
	  }else
	  if (checkTroopToPlanetMove(aTroop)){
		  moving = true;
	  }
	  return moving;
  }

  public boolean checkShipToCarrierMove(Spaceship ss){
	  return orders.checkShipToCarrierMove(ss);
  }

  public boolean checkShipToCarrierMove(Spaceship aSqd, Spaceship aCarrier){
	  return orders.checkShipToCarrierMove(aSqd,aCarrier);
  }

  public int countShipToCarrierMoves(Spaceship aCarrier){
	  return orders.countShipToCarrierMoves(aCarrier);
  }

  public int countTroopToCarrierMoves(Spaceship aCarrier){
	  return orders.countTroopToCarrierMoves(aCarrier);
  }

  public BlackMarketBid getBidToOffer(BlackMarketOffer tempOffer){
    BlackMarketBid tempBid = null;
    Expense anExpense = orders.getBidToOffer(tempOffer);
    if (anExpense != null){
      tempBid = anExpense.getBlackMarketBid();
    }
    return tempBid;
  }

  public void setUpdatedThisTurn(boolean newValue){
    updatedThisTurn = newValue;
  }

  public boolean getUpdatedThisTurn(){
    return updatedThisTurn;
  }

  public String getNotes(){
    return notes;
  }

  public void setNotes(String newNotes){
    notes = newNotes;
  }
  
  public void setPassword(String newPassword){
  	password = newPassword;
  }
  
  public String getTurnInfoText(int turn, ReportLevel reportLevel){
  	  StringBuffer text = new StringBuffer();
	  Report generalReport = getTurnInfo().getGeneralReport(turn);
 	  for (int i = 0; i < generalReport.size(); i++){
        text.append(generalReport.getInfoAt(i) + "\n");
      }
      List<String> infoStrings = generalReport.getAllReports();
      if ((infoStrings.get(infoStrings.size()-1)).equalsIgnoreCase("---------------")){
//      int generalReportIndex = generalReport.findInfoIndex("General Report");
  //    if ((generalReportIndex + 2) == generalReport.size()){
      	text.append("You had no general reports this turn.\n");
      	text.append("\n");
      }
      // Expences reports
//      if ((turn > 1) & (!getGalaxy().gameEnded)){
      if (turn > 1){
      	text.append("\n\nEconomy Report\n");
      	text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    EconomyReport er = getTurnInfo().getEconomyReport(turn);
      	text.append(" Saved from turn " + (turn-1) + ": " + er.getSavedLastTurn() + "\n");
      	text.append(" Income this turn: " + er.getIncomeLastTurn() + "\n");
      	text.append(" Income corruption this turn: " + er.getCorruptionIncomeLastTurn() + "\n");
      	text.append(" Upkeep for spaceships this turn: " + er.getSupportShipsLastTurn() + "\n");
      	text.append(" Upkeep corruption for spaceships this turn: " + er.getCorruptionUpkeepShipsLastTurn() + "\n");
      	text.append(" Upkeep for troops this turn: " + er.getSupportTroopsLastTurn() + "\n");
      	// TODO (Tobbe) Fixa så att kostnader för VIPar lägg till i turn Economy report.
      	text.append("Expences this turn: " + er.getExpensesLastTurn() + "\n");
        text.append("  (see expenses details below under \"Expenses Reports\")\n");
        text.append("\n");
      	text.append(" Saved to next turn: " + er.getSavedNextTurn() + "\n");
      	text.append(" Income next turn: " + er.getIncomeNextTurn() + "\n");
      	text.append(" Income corruption next turn: " + er.getCorruptionIncomeNextTurn() + "\n");
      	text.append(" Upkeep for spaceships next turn: " + er.getSupportShipsNextTurn() + "\n");
      	text.append(" Upkeep corruption for spaceships next turn: " + er.getCorruptionUpkeepShipsNextTurn() + "\n");
      	text.append(" Upkeep for troops next turn: " + er.getSupportTroopsNextTurn() + "\n");
//      TODO (Tobbe) Fixa så att kostnader för VIPar lägg till i turn Economy report.
      	if (er.getBrokeNextTurn()){
      		text.append("WARNING: Upkeep exceeds income. You are broke!\n");
      		text.append("Until upkeep gets below income you cannot move any ship or VIP, or have any expenses.\n");      		
      	}
        text.append("\n");

      	text.append("\nExpences Reports\n");
      	text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report expenceReport = getTurnInfo().getExpenseReport(turn);
        if (expenceReport.size() == 0){
        	text.append("You had no expences this turn.\n");
        }else{
  	      for (int j = 0; j < expenceReport.size(); j++){
  	      	text.append(expenceReport.getInfoAt(j) + "\n");
	      }
        }
        text.append("\n");

      	text.append("\nCivilian ships Reports\n");
      	text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report civilianReport = getTurnInfo().getCivilianReport(turn);
        if (civilianReport.size() == 0){
        	text.append("You had no civilian reports this turn.\n");
        }else{
  	      for (int j = 0; j < civilianReport.size(); j++){
  	      	text.append(civilianReport.getInfoAt(j) + "\n");
	      }
        }
        text.append("\n");

        // VIP reports
        text.append("\nVIP Reports\n");
        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report VIPReport = getTurnInfo().getVIPReport(turn);
        if (VIPReport.size() == 0){
        	text.append("Nothing to report this turn.\n");
        }else{
  	      for (int j = 0; j < VIPReport.size(); j++){
  	      	text.append(VIPReport.getInfoAt(j) + "\n");
	      }
        }
        text.append("\n");

        // Diplomacy reports
        text.append("\nDiplomacy Reports\n");
        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report diplomacyReport = getTurnInfo().getDiplomacyReport(turn);
	    if (diplomacyReport.size() == 0){
	    	text.append("Nothing to report this turn.\n");
	    }else{
	    	for (int k = 0; k < diplomacyReport.size(); k++){
	    		text.append(diplomacyReport.getInfoAt(k) + "\n");
	    	}
	    }
        text.append("\n");

	    // Black Market reports
        text.append("\nBlack Market Reports\n");
        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report blackMarketReport = getTurnInfo().getBlackMarketReport(turn);
        if (blackMarketReport.size() == 0){
        	text.append("Nothing to report this turn.\n");
        }else{
  	      for (int k = 0; k < blackMarketReport.size(); k++){
  	      	text.append(blackMarketReport.getInfoAt(k) + "\n");
	      }
        }
        text.append("\n");
        
        //      Research reports
        text.append("\nResearch Reports\n");
        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report researchReport = getTurnInfo().getResearchReport(turn);
        if (researchReport.size() == 0){
        	text.append("Nothing to report this turn.\n");
        }else{
  	      for (int k = 0; k < researchReport.size(); k++){
  	      	text.append(researchReport.getInfoAt(k) + "\n");
	      }
        }
        text.append("\n");
        
        // buildings reports
        text.append("\nBuilding Reports\n");
        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    Report buildingsReport = getTurnInfo().getBuildingsReport(turn);
        if (buildingsReport.size() == 0){
        	text.append("Nothing to report this turn.\n");
        }else{
  	      for (int k = 0; k < buildingsReport.size(); k++){
  	      	text.append(buildingsReport.getInfoAt(k) + "\n");
	      }
        }
        text.append("\n");

        // Battle reports
	    SpaceBattleReports spaceBattleReports = getTurnInfo().getSpaceBattleReports(turn);
	    if (spaceBattleReports.battleExist()){
	        text.append("\nSpace battle Reports\n");
	        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	        text.append("\n");
	    	text.append(spaceBattleReports.getAsString(this,reportLevel) + "\n");
	    }

        // Land battle reports
	    LandBattleReports landBattleReports = getTurnInfo().getLandBattleReports(turn);
	    if (landBattleReports.battleExist()){
	        text.append("\nLand battle Reports\n");
	        text.append("--------------------------------------------------------------------------------------------------------------------------------\n");
	    	text.append(landBattleReports.getAsString(reportLevel) + "\n");
	    }

      }
      return text.toString();
  }

	public boolean isRetreatingGovenor() {
		return retreatingGovenor;
	}
	
	public void setRetreatingGovenor(boolean retreatingGovenor) {
		this.retreatingGovenor = retreatingGovenor;
	}
	
	public void addOtherShipTypes(List<SpaceshipType> allShipTypes){
		for (SpaceshipType sst : allShipTypes) {
			SpaceshipType found = findSpaceshipType(sst.getName());
			if (found == null){
				// not in players own list
				// add to other ships instead
				otherSpaceshipTypes.add(sst.copy());
			}
		}
	}
	
	@Override
	public String toString(){
		String tmpStr = "";
		tmpStr += "Player: " + name + " (";
		tmpStr += govenorName + ") - ";
		if (faction != null){
			tmpStr += faction.getName();
		}else{ // sometimes droid clint player objects are missing faction but have factionName instead
			tmpStr += factionName;
		}
		return tmpStr;
	}

	public int getTurnDefeated(){
		return turnDefeated;
	}

	public boolean isAlien(){
		return faction.isAlien();
	}
	
	public boolean isFinishedThisTurn() {
		return finishedThisTurn;
	}

	public void setFinishedThisTurn(boolean finishedThisTurn) {
		this.finishedThisTurn = finishedThisTurn;
	}

	public int getNrTurnsBroke() {
		return nrTurnsBroke;
	}

	public void setNrTurnsBroke(int nrTurnsBroke) {
		this.nrTurnsBroke = nrTurnsBroke;
	}

	public void incNrTurnsBroke() {
		nrTurnsBroke++;
	}
	
	public Buildings getBuildings() {
		return buildings;
	}

	public void setBuildings(Buildings buildings) {
		this.buildings = buildings;
	}
	
	

	public Research getResearch() {
		return research;
	}

	public void setResearch(Research research) {
		this.research = research;
	}
/*
	public int getBuildOrbitalStructureCostBase() {
		return buildOrbitalStructureCostBase;
	}

	public void setBuildOrbitalStructureCostBase(int buildOrbitalStructureCostBase) {
		this.buildOrbitalStructureCostBase = buildOrbitalStructureCostBase;
	}

	public int getBuildOrbitalStructureCostMulitplier() {
		return buildOrbitalStructureCostMulitplier;
	}

	public void setBuildOrbitalStructureCostMulitplier(
			int buildOrbitalStructureCostMulitplier) {
		this.buildOrbitalStructureCostMulitplier = buildOrbitalStructureCostMulitplier;
	}
*/
	public boolean isCanReconstruct() {
		return canReconstruct;
	}

	public void setCanReconstruct(boolean canReconstruct) {
		this.canReconstruct = canReconstruct;
	}

	public int getClosedPlanetBonus() {
		return closedPlanetBonus;
	}

	public void setClosedPlanetBonus(int closedPlanetBonus) {
		this.closedPlanetBonus = closedPlanetBonus;
	}

	public int getOpenPlanetBonus() {
		return openPlanetBonus;
	}

	public void setOpenPlanetBonus(int openPlanetBonus) {
		this.openPlanetBonus = openPlanetBonus;
	}
/*
	public SpaceStation getOrbitalStructure() {
		return orbitalStructure;
	}

	public void setOrbitalStructure(SpaceStation orbitalStructure) {
		this.orbitalStructure = orbitalStructure;
	}
*/
	public int getReconstructCostBase() {
		return reconstructCostBase;
	}

	public void setReconstructCostBase(int reconstructCostBase) {
		this.reconstructCostBase = reconstructCostBase;
	}
	
	public int getResistanceBonus() {
		return resistanceBonus;
	}

	public void setResistanceBonus(int resistanceBonus) {
		this.resistanceBonus = resistanceBonus;
	}

	public int getTechBonus() {
		return techBonus;
	}

	public void setTechBonus(int techBonus) {
		this.techBonus = techBonus;
	}

	public int getReconstructCost(Planet aPlanet){
		return reconstructCostBase + aPlanet.getBasePop();
	}

	public int getIncomeAfterCorruption(int anIncome){
		int tmpIncome = anIncome;
		if (corruption != null){
			tmpIncome = corruption.getIncomeAfterCorruption(anIncome);
		}
		return tmpIncome;
	}

	public int getLostToCorruption(int anIncome){
		return anIncome - getIncomeAfterCorruption(anIncome);
	}

	/**
	 * This method calls the getIncomeAfterCorruption method.
	 * Income and upkeep are affected the same way by corruption.
	 * @param anIncome
	 * @return
	 */
	public int getUpkeepAfterCorruption(int anIncome){
		return getIncomeAfterCorruption(anIncome);
	}

	/**
	 * Duplicate of method in faction
	 * @return
	 */
	public String getCorruptionDescription(){
		String corrDesc = "None";
		if (corruption != null){
			corrDesc = corruption.getCorruptionDescription();
		}
		return corrDesc;
	}
	
	public void setCorruption(Corruption corruption) {
		this.corruption = corruption;
	}

	public void addTroopType(TroopType aTroopType){
		troopTypes.add(aTroopType);
	}
	
	public void addVIPType(VIPType aVIPType){
		vipTypes.add(aVIPType);
	}
	
	public void addOtherTroopType(TroopType aTroopType){
		otherTroopTypes.add(aTroopType);
	}
	
	public List<TroopType> getTroopTypes(){
		return troopTypes;
	}
	
	public List<VIPType> getVIPTypes(){
		return vipTypes;
	}
	
	public void addBuildTroop(Building building, TroopType tt){
	      orders.addBuildTroop(building, tt, this);
	    }
	
	public void addBuildVIP(Building building, VIPType vipTypes){
	      orders.addBuildVIP(building, vipTypes, this);
	    }
	
	public SpaceshipType getSpaceShipType(String shipName){
		for(int i=0; i < spaceshipTypes.size();i++){
			if(((SpaceshipType)spaceshipTypes.get(i)).getName().equalsIgnoreCase(shipName)){
				return ((SpaceshipType)spaceshipTypes.get(i));
			}
		}
		return null;
	}
	
	public TroopType getTroopType(String troopName){
		for(int i=0; i < troopTypes.size();i++){
			if(troopTypes.get(i).getUniqueName().equalsIgnoreCase(troopName)){
				return troopTypes.get(i);
			}
		}
		return null;
	}
	
	public VIPType getVIPType(String vipName){
		for(int i=0; i < vipTypes.size();i++){
			if(vipTypes.get(i).getName().equalsIgnoreCase(vipName)){
				return vipTypes.get(i);
			}
		}
		return null;
	}
/*
	public DiplomacyRelation getDiplomacyRelation() {
		return diplomacyRelation;
	}

	public void setDiplomacyRelation(DiplomacyRelation diplomacyRelation) {
		this.diplomacyRelation = diplomacyRelation;
	}
*/	
	public boolean orderExist(Player otherPlayer, DiplomacyLevel aLevel){
		return orders.checkDiplomacy(otherPlayer,aLevel);
	}
	
/*
	public List<TroopType> getTroopTypesCanBuild(){
		List<TroopType> canBuildTroopTypes = new ArrayList<TroopType>();
		for (TroopType aTroopType : troopTypes) {
			if (aTroopType.isCanBuild()){
				canBuildTroopTypes.add(aTroopType.clone());
			}
		}
		return canBuildTroopTypes;
	}
*/
	public DiplomacyOffer getDiplomacyOffer(Player otherPlayer){
		return orders.findDiplomacyOffer(otherPlayer);
	}

	public DiplomacyChange getDiplomacyChange(Player otherPlayer){
		return orders.findDiplomacyChange(otherPlayer);
	}

	public int getLatestMessageIdFromServer() {
		return latestMessageIdFromServer;
	}

	public void setLatestMessageIdFromServer(int latestMessageIdFromServer) {
		this.latestMessageIdFromServer = latestMessageIdFromServer;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	
	public boolean haveConfOrder(Player anotherPlayer){
		return orders.haveConfOrder(anotherPlayer);
	}

	public boolean haveConfOffer(Player anotherPlayer){
		return orders.haveConfOffer(anotherPlayer);
	}

    public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	public PlanetOrderStatuses getPlanetOrderStatuses() {
		return planetOrderStatuses;
	}

	public void setPlanetOrderStatuses(PlanetOrderStatuses planetOrderStatuses) {
		this.planetOrderStatuses = planetOrderStatuses;
	}
	
	public PlanetOrderStatuses getOriginalPlanetOrderStatuses() {
		return originalPlanetOrderStatuses;
	}

	/**
	 * Set originalPlanetOrderStatuses to be a clone of the current planetOrderStatuses;
	 * Later in the Android app this can be used to print what orders have been given in the current turn.
	 */
	public void initOriginalPlanetOrderStatuses() {
		this.originalPlanetOrderStatuses = Functions.deepClone(planetOrderStatuses);
	}

	public void updateMapInfo() {
		mapPlanetInfos.createNextTurnMapInfo(this);		
	}

	public MapInfos getMapInfos() {
		return mapPlanetInfos;		
	}

	public String getFactionName() {
		return factionName;
	}

	public void setFactionName(String factionName) {
		this.factionName = factionName;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

    public void pruneOtherPlayerDroid(){
    	ti = null;
    	pi = null;
    	research = null;
    	mapPlanetInfos = null;
    	planetOrderStatuses = null;
    	reportLevel = null;
    	corruption = null;
    	diplomacyOffers = null;
    	buildings = null;
    	// XXX can faction reference be removed?
    	nextupdate = null;
    	notes = null;
    	orders = null;
    	password = null;
    	spaceshipTypes = null;
    	otherSpaceshipTypes = null; 
        troopTypes = null;
        otherTroopTypes = null;
        vipTypes = null;
    }

	public int getOrdersCount(){
		int nr = 0;
		Orders orders = getOrders();
		nr += orders.getOrdersCount();
		// count planet order statuses
		PlanetOrderStatuses currentPlanetOrderStatuses = getPlanetOrderStatuses();
		PlanetOrderStatuses originalPlanetOrderStatuses = getOriginalPlanetOrderStatuses();
		for(Planet aPlanet : g.getPlanets()){
			String planetName = aPlanet.getName();
			PlanetOrderStatus currentPlanetOrderStatus = currentPlanetOrderStatuses.getPlanetOrderStatus(planetName);
			PlanetOrderStatus originalPlanetOrderStatus = originalPlanetOrderStatuses.getPlanetOrderStatus(planetName);
			if (currentPlanetOrderStatus.isAttackIfNeutral() != originalPlanetOrderStatus.isAttackIfNeutral()){
				nr++;
			}
			if (currentPlanetOrderStatus.isDestroyOrbitalBuildings() != originalPlanetOrderStatus.isDestroyOrbitalBuildings()){
				nr++;
			}
			if (currentPlanetOrderStatus.isDoNotBesiege() != originalPlanetOrderStatus.isDoNotBesiege()){
				nr++;
			}
			if (currentPlanetOrderStatus.getMaxBombardment() != originalPlanetOrderStatus.getMaxBombardment()){
				nr++;
			}
		}	
		Logger.info("getOrdersCount() returns: " + nr);
		return nr;
	}

}
