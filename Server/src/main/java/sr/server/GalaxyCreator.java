package sr.server;

import java.util.Collections;
import java.util.List;

import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
import spaceraze.servlethelper.game.troop.TroopMutator;
import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.world.Galaxy;
import spaceraze.world.GameWorld;
import spaceraze.world.Map;
import spaceraze.world.Planet;
import spaceraze.world.Spaceship;
import spaceraze.world.SpaceshipType;
import spaceraze.world.StatisticGameType;
import spaceraze.world.Troop;
import spaceraze.world.TroopType;
import sr.server.map.MapHandler;

public class GalaxyCreator{
	
	public Galaxy createGalaxy(String nameOfGame, String nameOfMap, int steps, GameWorld aGameWorld, int singleVictory, int factionVictory, int endTurn, int numberOfStartPlanet, StatisticGameType statisticGameType){
    	Logger.fine("createGalaxy called: " + nameOfGame + " " + nameOfMap);
    	Map theMap = MapHandler.getMap(nameOfMap);
    	Galaxy g = createGalaxy(nameOfGame,theMap, steps, aGameWorld, statisticGameType);
    	if(singleVictory > 0){
    		g.setSingleVictory(singleVictory);
    	}
    	if(factionVictory > 0){
    		g.setFactionVictory(factionVictory);
    	}
    	if(endTurn > 0){
    		g.setEndTurn(endTurn);
    	}
    	if(numberOfStartPlanet > 0){
    		g.setNumberOfStartPlanet(numberOfStartPlanet);
    	}
    	return g;
    }

    public Galaxy createGalaxy(String nameOfGame, Map aMap, int steps, GameWorld aGameWorld){
    	return createGalaxy(nameOfGame,aMap,steps,aGameWorld,StatisticGameType.ALL);
    }
    	
    public Galaxy createGalaxy(String nameOfGame, Map aMap, int steps, GameWorld aGameWorld, StatisticGameType statisticGameType){
    	Logger.fine("createGalaxy #2 called: " + nameOfGame + " " + aMap.getFileName());
//    	Map theMap = MapHandler.getMap(nameOfMap);
    	Galaxy g = new Galaxy(aMap,nameOfGame,steps,aGameWorld,statisticGameType);
    	// randomize planets order
    	Collections.shuffle(g.getPlanets());
    	// randomize planet data
        randomizeNeutralPlanets(g.getPlanets(),g, true);
        Logger.fine("Galaxy created.");
        return g;
    }

    static public void randomizeNeutralPlanets(List<Planet> allPlanets, Galaxy g, boolean couldBeRazed){
      for (int i = 0; i < allPlanets.size(); i++){
        Planet tempp = (Planet)allPlanets.get(i);
        if (!tempp.isStartPlanet()){
          int tmpProd = Functions.getRandomInt(1,3) + Functions.getRandomInt(1,4) - 1;
          tempp.setProd(tmpProd,tmpProd);
          if (tempp.getPopulation() > 3){
            tempp.setRes(Functions.getRandomInt(1,3) + Functions.getRandomInt(1,3));
          }else{
            tempp.setRes(Functions.getRandomInt(1,4));
          }
          int temp = Functions.getRandomInt(1,100);
          if (temp <= g.getGameWorld().getClosedNeutralPlanetChance()){  // �ndrar s� att �ppna blir st�ngda
            tempp.reverseVisibility();
          }
          SpaceshipType sst1 = g.getGameWorld().getNeutralSize1();
          SpaceshipType sst2 = g.getGameWorld().getNeutralSize2();
          SpaceshipType sst3 = g.getGameWorld().getNeutralSize3();
          TroopType tt = g.getGameWorld().getNeutralTroopType();
          if (couldBeRazed && Functions.getRandomInt(1,100) <= g.getGameWorld().getRazedPlanetChance()){ 
        	  tempp.setRazed();
          }else 
          if (tempp.getPopulation() < 3){ // pop 1-2
            temp = Functions.getRandomInt(1,3) - 1;
            createNeutralShips(temp,sst1,tempp,g);
            if (tt != null){
            	temp = Functions.getRandomInt(1,2) - 1;
            	createNeutralTroops(temp, tt, tempp, g);
            }
          }else
          if ((tempp.getPopulation() >= 3) & (tempp.getPopulation() < 5)){ // pop 3-4
            temp = Functions.getRandomInt(1,3);
            if (temp < 3){ // build some small ships
              temp = Functions.getRandomInt(2,4);
              createNeutralShips(temp,sst1,tempp,g);
              if (tt != null){
              	temp = Functions.getRandomInt(1,3) - 1;
              	createNeutralTroops(temp, tt, tempp, g);
              }
            }else{ // build some medium ships
              temp = Functions.getRandomInt(1,2);
              createNeutralShips(temp,sst2,tempp,g);
              if (tt != null){
              	temp = Functions.getRandomInt(1,2);
              	createNeutralTroops(temp, tt, tempp, g);
              }
            }
          }else{  // pop 5+
            temp = Functions.getRandomInt(1,3);
            if (temp > 1){ // build some medium ships
              temp = Functions.getRandomInt(2,4);
              createNeutralShips(temp,sst2,tempp,g);
              if (tt != null){
              	temp = Functions.getRandomInt(1,3);
              	createNeutralTroops(temp, tt, tempp, g);
              }
            }else{ // build some large ships
              temp = Functions.getRandomInt(1,3);
              createNeutralShips(temp,sst3,tempp,g);
              if (tt != null){
              	temp = Functions.getRandomInt(2,4);
              	createNeutralTroops(temp, tt, tempp, g);
              }
            }
          }
        }
      }
    }

    static private void createNeutralShips(int nr, SpaceshipType sst, Planet aPlanet, Galaxy g){
      for (int i = 0; i < nr; i++){
        addNeutralShip(aPlanet,sst,g);
      }
    }

    static private void addNeutralShip(Planet aPlanet,SpaceshipType sstTemp, Galaxy g){
      Spaceship ssTemp = SpaceshipMutator.createSpaceShip(sstTemp, g);
      ssTemp.setLocation(aPlanet);
      g.getSpaceships().add(ssTemp);
    }

    static private void createNeutralTroops(int nr, TroopType tt, Planet aPlanet, Galaxy g){
        for (int i = 0; i < nr; i++){
          addNeutralTroop(aPlanet,tt,g);
        }
    }
    
    static private void addNeutralTroop(Planet aPlanet,TroopType ttTemp, Galaxy g){
        Troop tTemp = TroopMutator.createTroop(ttTemp, g);
        tTemp.setPlanetLocation(aPlanet);
        g.addTroop(tTemp);
    }
    
}