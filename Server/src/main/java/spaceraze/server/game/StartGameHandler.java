package spaceraze.server.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import spaceraze.world.Building;
import spaceraze.world.BuildingType;
import spaceraze.world.Faction;
import spaceraze.world.Galaxy;
import spaceraze.world.Planet;
import spaceraze.server.world.comparator.PlanetRangeComparator;
import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.world.Player;
import spaceraze.world.Spaceship;
import spaceraze.world.SpaceshipType;
import spaceraze.world.Troop;
import spaceraze.world.TroopType;
import spaceraze.world.VIP;
import spaceraze.world.VIPType;
import sr.server.GalaxyCreator;
import sr.server.GalaxyUpdater;

public class StartGameHandler {
	
	
	public void setStartPlanets(GalaxyUpdater galaxyUpdater){
		int numberOfStartPlanet = galaxyUpdater.getGalaxy().getNumberOfStartPlanet();
		Galaxy galaxy = galaxyUpdater.getGalaxy();
		if(numberOfStartPlanet > 1){
			int tempNumberOfStartPlanet = numberOfStartPlanet-1;
			List<Planet> neutralPlanets = new ArrayList<Planet>();
			List<Planet> neutralPlanetsRandom = new ArrayList<Planet>();
			neutralPlanets.addAll(galaxy.getPlayersPlanets(null));
			if(neutralPlanets.size() < (galaxy.getPlayers().size() * tempNumberOfStartPlanet)){
				tempNumberOfStartPlanet = neutralPlanets.size()/galaxy.getPlayers().size();
			}
			
			int numbersOfNeutralPlanets = neutralPlanets.size() - (galaxy.getPlayers().size() * tempNumberOfStartPlanet);
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
				GalaxyCreator.randomizeNeutralPlanets(razedPlanets, galaxy, false);
			}
			
			while(neutralPlanets.size() > 0){
	    		int random = Functions.getRandomInt(0, neutralPlanets.size()-1);
	    		neutralPlanetsRandom.add(neutralPlanets.get(random));
	    		neutralPlanets.remove(random);
	    	}
			Collections.sort(neutralPlanetsRandom,new PlanetProdComparator());
			
			
			List<Player> tempPlayers = new ArrayList<Player>();
			List<Player> randomPlayers = new ArrayList<Player>();
	    	tempPlayers.addAll(galaxy.getPlayers());
	    	
	    	while(tempPlayers.size() > 0){
	    		int random = Functions.getRandomInt(0, tempPlayers.size()-1);
	    		randomPlayers.add(tempPlayers.get(random));
	    		tempPlayers.remove(random);
	    	}
	    	
	    	int playerNr=0;
	    	for (Planet planet : neutralPlanetsRandom) {
	    		
	    		Player randomPlayer = randomPlayers.get(playerNr);
	    		if (randomPlayer.isAlien()){
	    			galaxyUpdater.removeNeutralShips(planet);
	    			galaxy.checkTroopsOnInfestedPlanet(planet, randomPlayer);
	    			planet.setProd(0);
	    			planet.setRes(1 + randomPlayer.getFaction().getResistanceBonus());
	    			planet.setHasNeverSurrendered(false);
	    			planet.setPlayerInControl(randomPlayer);
	    		}else{
	    			VIP guvenor = galaxy.findVIPGovenor(randomPlayer);
	    			planet.joinsVisitingDiplomat(guvenor, false);
	    			galaxyUpdater.shipsJoinGovenor(planet,guvenor);
	    			galaxyUpdater.troopsJoinGovenor(planet, guvenor);
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
	
	public Player getNewPlayer(String name, String password, String govenorName, String factionName, Galaxy galaxy){
    	Logger.finer("getNewPlayer: " + name + " " + password + " " + govenorName);
        Player p;
        if (galaxy.getNrStartPlanets() == galaxy.getPlayers().size()){
            p = new Player("All starting planets already taken");
        }else{
        	Faction playerFaction = galaxy.findFaction(factionName);
            Planet homeplanet = getStartPlanet(galaxy.getSteps(),playerFaction, galaxy);
            p = createPlayer(name,password,homeplanet,govenorName,factionName, galaxy);
            Logger.finer("Galaxy.getNewPlayer");
            homeplanet.setPlayerInControl(p);
            Logger.finer("Galaxy.getNewPlayer2");
            galaxy.removeNeutralShips(homeplanet);
            galaxy.removeNeutralTroops(homeplanet);
            Logger.finer("Galaxy.getNewPlayer3");
            homeplanet.setHomeplanet(p.getFaction());
            Logger.finer("Galaxy.getNewPlayer4");
            galaxy.addPlayer(p);
            Logger.finer("Galaxy.getNewPlayer5");
        }
        return p;
    }
	
//  @SuppressWarnings("unchecked")
  private Planet getStartPlanet(int steps, Faction playerFaction, Galaxy galaxy){
	Logger.finer("getStartPlanet steps: " + steps);
    Planet foundPlanet = null;
    List<Planet> tempplanets = getStarPlanets(galaxy);
    // slumpa om den
    Collections.shuffle(tempplanets);
//    Functions.randomize(tempplanets);
    // if group players, sort the planets with planets close to planets with 
    // players from the same faction first
    if (galaxy.isGroupSameFaction()){
    	setPlanetRangeToClosestFriendly(tempplanets,playerFaction, galaxy);
    }
    // loopa igenom planeterna tills listan är slut eller en lämplig hemplanet hittats
    int i = 0;
    while ((i < tempplanets.size()) & (foundPlanet == null)){
      Planet tempPlanet = tempplanets.get(i);
      // kolla om planeten inte redan är en hemplanet
      if (tempPlanet.getPlayerInControl() == null){
        // kolla om en planet har andra hemplaneter inom steps steg
        if (!checkStartplanetsWithinRange(steps,tempPlanet,false, galaxy) & !checkStartplanetsWithinRange(steps,tempPlanet,true, galaxy)){
          // om nej -> returnera denna planet
          foundPlanet = tempPlanet;
        }else{ // om ja, gå vidare till n�sta
          i++;
        }
      }else{
        i++;
      }
    }
    if ((foundPlanet == null) & (steps > 0)){
      // om inte, returnera resultatet av nytt anrop till getStartPlanet med steps = steps - 1
      foundPlanet = getStartPlanet(steps - 1,playerFaction, galaxy);
    }
    return foundPlanet;
  }
  
  private List<Planet> getStarPlanets(Galaxy galaxy) {
	  LinkedList<Planet>  tempList = new LinkedList<Planet>();
	  for (Planet aPlanet : galaxy.getPlanets()) {
		  if(aPlanet.isPosssibleStartplanet()){
			  Logger.finer("Possible startplanet " + aPlanet.getName());
			  tempList.add(aPlanet);
		  }
	  }  
	// TODO Auto-generated method stub
	return tempList;
}
	
//  gissar att TurnInfo texten inte visas någon stan?  den är tok fel i alla fall.
    private Player createPlayer(String name, String password, Planet homeplanet, String govenorName, String factionName, Galaxy galaxy){
        Player p = new Player(name,password,galaxy, govenorName, factionName, homeplanet);
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
        p.setBuildings(Functions.deepClone(p.getFaction().getBuildings()));
//      create all starting buildings for the new player
        List<BuildingType> startBuildingTypes = p.getFaction().getStartingBuildings();
        for (Iterator<BuildingType> iter = startBuildingTypes.iterator(); iter.hasNext();) {
        	BuildingType buildingTemp1 = (BuildingType) iter.next();
        	Logger.info("buildingTemp1.getName(): " + buildingTemp1.getName());
        	BuildingType buildingTemp2 = p.findBuildingType(buildingTemp1.getName());
        	Building buildingTemp = buildingTemp2.getBuilding(homeplanet, galaxy);
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
        p.addOtherShipTypes(galaxy.getGameWorld().getShipTypes());
        // create all starting spaceships for the new player
        List<SpaceshipType> startTypes = p.getFaction().getStartingShipTypes();
        for (SpaceshipType sstTemp1 : startTypes) {
        	SpaceshipType sstTemp2 = p.findSpaceshipType(sstTemp1.getName());
        	Spaceship ssTemp = sstTemp2.getShip(null,p.getFaction().getTechBonus(),0);
        	ssTemp.setLocation(homeplanet);
        	ssTemp.setOwner(p);
        	galaxy.addSpaceship(ssTemp);
        }
        // clone trooptypes in faction and add to new player
        addTroopTypes(p);
        addOtherTroopTypes(p, galaxy);
        // create all starting troops for this player
        List<TroopType> startTroopTypes = p.getFaction().getStartingTroops();
        for (TroopType aTroopType : startTroopTypes) {
        	// first get trooptype from player
        	TroopType playerTroopType = p.findTroopType(aTroopType.getUniqueName());
        	// then create new troop
        	Troop aTroop = playerTroopType.getTroop(null, p.getFaction().getTechBonus(), 0);
        	aTroop.setPlanetLocation(homeplanet);
        	aTroop.setOwner(p);
        	galaxy.addTroop(aTroop);
		}
        // create Govenor VIP
        //VIP tempVip = new VIP(p,homeplanet,(VIPType)vipTypes.elementAt(0));
        Logger.finer("create gov");
//        VIP tempVip = ((VIPType)vipTypes.elementAt(0)).createNewVIP(p,homeplanet);
//        allVIPs.addElement(tempVip);
        galaxy.getAllVIPs().add(p.getFaction().getGovernorVIPType().createNewVIP(p, homeplanet, true));
        // create 1 random VIP
//        tempVip = this.createRandomVIP();
//        tempVip.setBoss(p);
        Logger.finer("create player vip");
        for (int i = 0; i < p.getFaction().getNrStartingRandomVIPs(); i++) {
        	//TODO createPlayerVIP(p) should be moved to server side.
            VIP tempVip = galaxy.createPlayerVIP(p);
            tempVip.setLocation(homeplanet);
		}
        List<VIPType> playerStartVips = p.getFaction().getStartingVIPTypes();
        for (VIPType aVipType : playerStartVips) {
            VIP tempVip = aVipType.createNewVIP(true);
	        tempVip.setBoss(p);
            tempVip.setLocation(homeplanet);
	        galaxy.getAllVIPs().add(tempVip);
		}        
        // create new diplomacy states to all other players (that have already joined this game)
        galaxy.getDiplomacy().createInitialDiplomaticRelations(p, galaxy.getGameWorld());
//        GameWorldDiplomacy diplomacy = gw.getDiplomacy();
//        for (Iterator iter = players.iterator(); iter.hasNext();) {
//			Player aPlayer = (Player) iter.next();
//			DiplomacyRelation tmpRelation = diplomacy.getRelation(aPlayer.getFaction(), p.getFaction());
//			DiplomacyState tmpState = new DiplomacyState(tmpRelation.clone(),aPlayer,p);
//			diplomacyStates.add(tmpState);
//		}
        return p;
    }
    
    private void addSpaceshipTypes(Player p){
    	List<SpaceshipType> factionSpaceshipTypes = p.getFaction().getSpaceshipTypes();
        //Logger.finer("addSpaceshipTypes: " + p.getName());
        for (int i = 0; i < factionSpaceshipTypes.size(); i++){
        	Logger.finer("i: " + i + " " + factionSpaceshipTypes.get(i));
        	p.addSpaceshipType(new SpaceshipType(factionSpaceshipTypes.get(i)));
        }
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
    
    private void addOtherTroopTypes(Player p, Galaxy galaxy){
        // add all other trooptypes that the player cannot build
        for (TroopType galaxyTroopType : galaxy.getTroopTypes()) {
			TroopType tmpPlayerTt = p.findTroopType(galaxyTroopType.getUniqueName());
			if (tmpPlayerTt == null){ // player does not have trooptype already
	        	  TroopType tmpTT = galaxyTroopType.clone();
	          	  p.addOtherTroopType(tmpTT);
			}
		}
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
    private void setPlanetRangeToClosestFriendly(List<Planet> allPlanets, Faction playersFaction, Galaxy galaxy){
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
  				  int steps = getStepsToClosestFriendly(allPlanets,planet,playersFaction, galaxy);
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
    
    private int getStepsToClosestFriendly(List<Planet> planets, Planet aPlanet, Faction playerFaction, Galaxy galaxy){
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
	        allNeighbours = galaxy.getAllDestinations(tempPlanet,false);
	        // Gå igenom alla allNeighbours  (lägg i newEdgePlanets)
	        for (int j = 0; j < allNeighbours.size(); j++){
	          Planet tempNeighbourPlanet = allNeighbours.get(j);
	          // kolla att tempNeighbourPlanet inte redan finns i searchedPlanets
	          if ((!searchedPlanets.contains(tempNeighbourPlanet)) & (!newEdgePlanets.contains(tempNeighbourPlanet))){
	            // lägg i newEdgePlanets
	            newEdgePlanets.add(tempNeighbourPlanet);
	          }
	        }
	        allNeighbours = galaxy.getAllDestinations(tempPlanet,true);
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
	      // täm newEdgePlanets
	      newEdgePlanets.clear();
	    }
	    return tempSteps;
	  }
    
    private boolean checkStartplanetsWithinRange(int steps, Planet aLocation, boolean longRange, Galaxy galaxy){
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
        while ((searchedPlanets.size() < galaxy.getPlanets().size()) & (tempSteps < steps) & (!planetFound)){
          tempSteps++;
          // Gå igenom alla edgePlanets
          for (int i = 0; i < edgePlanets.size(); i++){
            Planet tempPlanet = edgePlanets.get(i);
            // Hämta alla grannar till tempPlanet
            allNeighbours = galaxy.getAllDestinations(tempPlanet,longRange);
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
    

}
