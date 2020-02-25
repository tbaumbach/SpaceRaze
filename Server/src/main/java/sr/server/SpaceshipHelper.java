package sr.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import spaceraze.util.general.Logger;
import spaceraze.world.Galaxy;
import spaceraze.world.Planet;
import spaceraze.world.Player;
import spaceraze.world.Spaceship;
import spaceraze.world.enums.SpaceshipRange;
import spaceraze.world.mapinfo.MapPlanetInfo;
import sr.campaign.ai.enums.FindPlanetCriterium;

public class SpaceshipHelper {

	//public SpaceshipHelper() {
		
	//}

	public Planet getEscapePlanet(Spaceship spaceship, Galaxy galaxy, List<Planet> allPlanetsInGalaxy) {
		Logger.finer("getRunToPlanet aSpaceship: " + spaceship.getName());
		Planet foundPlanet = null;
		Planet firstDestination = null;
		// kolla efter egna planeter
		foundPlanet = findClosestOwnPlanetFromShip(spaceship.getLocation(), spaceship.getOwner(), spaceship, galaxy);  
		// om en destinationsplanet har hittats skall den 1:a planeten på väg dit hämtas
		if (foundPlanet != null){
			Logger.finer("foundPlanet: " + foundPlanet.getName());
			firstDestination = findFirstJumpTowardsPlanet(spaceship.getLocation(), foundPlanet, spaceship.getRange(), galaxy);
			Logger.finer("firstDestination: " + firstDestination.getName());
		}else{
			Logger.finer("no planet found");
		}
      return firstDestination;
		
	}
	
	public Planet findClosestOwnPlanetFromShip(Planet location, Player player, Spaceship spaceship, Galaxy galaxy){
    	return findClosestPlanet(location, player, spaceship.getRange(), FindPlanetCriterium.OWN_PLANET_NOT_BESIEGED, null, galaxy);
    }
	
	// player kan vara null för att leta efter neutrala planeter
    private Planet findClosestPlanet(Planet location, Player player, SpaceshipRange spaceshipRange, FindPlanetCriterium criterium, List<String> visitedPlanets, Galaxy galaxy){
      Logger.finer("findClosestOwnPlanetFromShip: " + location.getName());
      Planet foundPlanet = null;
      // skapa tom vektor över hittade planeter
      List<Planet> foundPlanets = new ArrayList<Planet>();
      List<Planet> edgePlanets = new ArrayList<Planet>(); // de planeter som var på gränsen till det genomsökta området
      edgePlanets.add(location);
      List<Planet> newEdgePlanets = new ArrayList<Planet>(); // de planeter som  är på gränsen till det genomsökta området
      List<Planet> searchedPlanets = new ArrayList<Planet>();  // lägg in alla som genomsökts + startplaneten
      searchedPlanets.add(location);

      List<Planet> allNeighbours;
      // loopa tills alla planeter har letats igenom eller minst 1 lämplig planet har hittats
      while ((searchedPlanets.size() < galaxy.getPlanets().size()) & (foundPlanets.size() == 0) & (edgePlanets.size() > 0)){
      	Logger.finer("in while");
      	// Gå igenom alla edgePlanets
        for (int i = 0; i < edgePlanets.size(); i++){
       	  Logger.finest("loop edgeplanets");
          Planet tempPlanet = edgePlanets.get(i);
          Logger.finest("temp edgeplanet: " + tempPlanet.getName());
          // Hämta alla grannar till tempPlanet
          allNeighbours = galaxy.getAllDestinations(tempPlanet,spaceshipRange == SpaceshipRange.LONG);
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
	          if (criterium == FindPlanetCriterium.OWN_PLANET_NOT_BESIEGED){
		          // kolla om planeten tillhör eftersökt spelare
		          if (tempPlanet.getPlayerInControl() == player){// om planeter tillhör eftersökt spelare
		            // om den dessutom ej är belägrad, sätt in den i foundPlanets
		          	if (!tempPlanet.isBesieged()){
		          		foundPlanets.add(tempPlanet);
		          		Logger.finest("adding to found: " + tempPlanet.getName());
		          	}
		          }
	          }else
	          if (criterium == FindPlanetCriterium.CLOSED){ // only planets not belonging to the palyer
	        	  if ((tempPlanet.getPlayerInControl() != player) & (!tempPlanet.isOpen())){
	        		  foundPlanets.add(tempPlanet);
	        	  }
	          }else
	          if (criterium == FindPlanetCriterium.HOSTILE_ASSASSIN_OPEN){
	        	  if (tempPlanet.isOpen() & tempPlanet.getPlayerInControl() != null){
	        		  if (galaxy.getDiplomacy().hostileAssassin(tempPlanet.getPlayerInControl(), player)){	  
	            		  foundPlanets.add(tempPlanet);
	        		  }
	        	  }
	          }else
	          if (criterium == FindPlanetCriterium.NEUTRAL_UNTOUCHED){
	        	  if (tempPlanet.isOpen() & tempPlanet.getPlayerInControl() == null){ // open neutral
	        		  foundPlanets.add(tempPlanet);
	        	  }else{ // if closed since the beginning, and assumed neutral
	        		  MapPlanetInfo mapPlanetInfo = player.getMapInfos().getLastKnownOwnerInfo(tempPlanet); // should return null if no info about owner
	        		  if ((tempPlanet.getPlayerInControl() != player) & !tempPlanet.isOpen() & (mapPlanetInfo == null)){
	            		  foundPlanets.add(tempPlanet);
	        		  }
	        	  }
	          }else
	          if (criterium == FindPlanetCriterium.EMPTY_VIP_TRANSPORT_WITHOUT_ORDERS){ // very specific criterium used by Droid GW
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
	        	  Spaceship foundShip = galaxy.findEmptyShipWithoutOrders(player,tempPlanet,"VIP Transport");
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
    
    
    public Planet findFirstJumpTowardsPlanet(Planet aLocation, Planet aDestination, SpaceshipRange aSpaceshipRange, Galaxy galaxy){
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
            allNeighbours = galaxy.getAllDestinations(tempPlanet,aSpaceshipRange == SpaceshipRange.LONG);
            // Gå igenom alla allNeighbours  (lägg i newEdgePlanets)
            for (int j = 0; j < allNeighbours.size(); j++){
              Planet tempNeighbourPlanet = allNeighbours.get(j);
              Logger.finest("tempNeighbourPlanet: " + tempNeighbourPlanet.getName());
              // kolla att tempNeighbourPlanet inte redan finns i searchedPlanets
              if ((!Galaxy.containsPlanet(searchedPlanets,tempNeighbourPlanet)) & (!Galaxy.containsPlanet(newEdgePlanets,tempNeighbourPlanet))){
                Logger.finest("containsPlanet: " + !Galaxy.containsPlanet(searchedPlanets,tempNeighbourPlanet));
                Logger.finest("containsPlanet: " + !Galaxy.containsPlanet(newEdgePlanets,tempNeighbourPlanet));
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
        // loopa tills reachFrom �r null
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
}