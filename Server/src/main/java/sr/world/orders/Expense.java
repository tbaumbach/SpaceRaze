package sr.world.orders;

import java.io.Serializable;

import sr.enums.HighlightType;
import sr.general.logging.Logger;
import sr.world.BlackMarketBid;
import sr.world.Building;
import sr.world.BuildingType;
import sr.world.Galaxy;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.SpaceshipType;
import sr.world.Troop;
import sr.world.TroopType;
import sr.world.TurnInfo;
import sr.world.VIP;
import sr.world.VIPType;

// representerar ett utl�gg gjort av spelaren under sitt drag
public class Expense implements Serializable {
  static final long serialVersionUID = 1L;
  String type;
  //Planet planet;
  String planetName;
 // OrbitalWharf ow;
  //SpaceshipType sst;
  String spaceshipTypeName;
  //BuildingType buildingType;
  String buildingTypeName;
  int currentBuildingId = 0;//byggnaden som bygger enheten.
  //TroopType troopType;
  String troopTypeName;
  //VIPType vipType;
  String typeVIPName;
  //Galaxy g;
  //Player player;
  String playerName=""; // Du eller player som mot tar en g�va.
  BlackMarketBid aBid;
  ResearchOrder researchO;
  int sum;
  
  public Expense(){}

  // Research
  public Expense(String temptype, ResearchOrder ro, Player aPlayer){
	  this.type = temptype;
	  researchO = ro;
	  this.playerName = aPlayer.getName();
	}
 

  // bjuda p� ett erbjudande p� svarta marknaden, type = "blackmarketbid"
  // Fungear inte med Rest API(JSON). Då det siter ihop för mycket med serven.
  public Expense(String temptype, BlackMarketBid aBid, Player aPlayer){
    this.playerName = aPlayer.getName();
    this.aBid = aBid;
    aBid.addPlayer(aPlayer);
    this.type = temptype;
  }

  // bygga nytt skepp, type = "buildship"
  public Expense(String temptype, Building tempBuilding, SpaceshipType tempsst, String aPlayername){
    this.planetName = tempBuilding.getLocation().getName();
    //sst = tempsst;
    spaceshipTypeName = tempsst.getName();
    this.type = temptype;
    currentBuildingId = tempBuilding.getUniqueId();
    playerName =aPlayername;
  }
  
//bygga nytt troop, type = "buildtroop"
  public Expense(String temptype, Building tempBuilding, TroopType tempTroopType, String aPlayername){
    this.planetName = tempBuilding.getLocation().getName();
    //this.troopType = tempTroopType;
    this.troopTypeName = tempTroopType.getUniqueName();
    this.type = temptype;
    currentBuildingId = tempBuilding.getUniqueId();
    playerName =aPlayername;
  }
  
//bygga ny VIP, type = "buildVIP"
  public Expense(String temptype, Building tempBuilding, VIPType tempVIPTYPE, String aPlayername){
    this.planetName = tempBuilding.getLocation().getName();
    this.typeVIPName = tempVIPTYPE.getName();
    this.type = temptype;
    currentBuildingId = tempBuilding.getUniqueId();
    playerName =aPlayername;
  }
  
//bygga ny byggnad, type = "building"
  public Expense(String temptype, BuildingType tempbuildingType, String aPlayername,Planet planet, Building tempBuilding){
	  //this.buildingType = tempbuildingType;
	  this.buildingTypeName = tempbuildingType.getName();
	  this.type = temptype;
	  this.planetName = planet.getName();
	  if(tempBuilding!= null){
		  currentBuildingId = tempBuilding.getUniqueId();
	  }
	  playerName =aPlayername;
  }

  // ge pengar till en annan spelare, type = "transaction"
  public Expense(String temptype, Player aPlayer, int aSum){
    this.type = temptype;
    this.playerName = aPlayer.getName();
    this.sum = aSum;
  }

  // reconstruct a razed planet, type = "reconstruct"
  //type = "pop" eller "res"
  public Expense(String temptype, Planet aPlanet, Player aPlayer){
    this.type = temptype;
    this.playerName = aPlayer.getName();
    this.planetName = aPlanet.getName();
  }
  

  public void performExpense(TurnInfo ti, Player p, Orders o){
	  
	  Logger.finer("Expense.performExpense(TurnInfo ti, Player p, Orders o) type: " +  type);
	  Galaxy g = p.getGalaxy();
	  Player playerToResive = g.getPlayer(playerName);
	  Planet planet = null;
	  if(planetName != null && !planetName.equalsIgnoreCase("")){
		  planet = g.getPlanet(planetName);
	  }
    if (type.equalsIgnoreCase("pop")){
    	g.getPlayer(playerName).removeFromTreasury(planet.getPopulation());
      planet.increasePopulation();
      ti.addToLatestExpenseReport(planet.getName() + " has increased its production from " + (planet.getPopulation() - 1) + " to " + planet.getPopulation() + ".");
      ti.addToLatestExpenseReport("Cost to increase production: " + planet.getPopulation() + ".");
    }else
    if (type.equalsIgnoreCase("res")){
      planet.increaseResistance();
      ti.addToLatestExpenseReport(planet.getName() + " has increased its resistance from " + (planet.getResistance() - 1) + " to " + planet.getResistance() + ".");
      planet.getPlayerInControl().removeFromTreasury(planet.getResistance());
      ti.addToLatestExpenseReport("Cost to increase resistance: " + planet.getResistance() + ".");
    }else
    if (type.equalsIgnoreCase("building")){
    	
    	BuildingType buildingType = g.getPlayer(playerName).findBuildingType(buildingTypeName);
    	
    	String uniqueBuildingString="";
    	boolean buildBuilding = true;
    	
    	VIP tempVIP = p.getGalaxy().findVIPBuildingBuildBonus(planet,p,o);
    	
    	if(buildingType.isWorldUnique()){
    		if(!buildingType.isWorldUniqueBuild(p.getGalaxy())){
    			uniqueBuildingString = "Congratulations you have build the world unique " + buildingType.getName() + ".";
    		}else{// The building can't be build.
    			buildBuilding =  false;
    			uniqueBuildingString = "You can not build the world unique " + buildingType.getName() + " building. Some other organisation was faster then you.";
    		}
    	}else
		if(buildingType.isFactionUnique()){
    		if(!buildingType.isFactionUniqueBuild(p)){
    			uniqueBuildingString = "Congratulations you have build the faction unique " + buildingType.getName() + ".";
    		}else{// The building can't be build.
    			buildBuilding =  false;
    			uniqueBuildingString = "You can not build the faction unique " + buildingType.getName() + " building. Some other leader was faster then you.";
    		}
    	}else
    	if(buildingType.isPlayerUnique()){
    		if(!buildingType.isPlayerUniqueBuild(p)){
    			uniqueBuildingString = "You have build the player unique " + buildingType.getName() + " and you can not build more of this type.";
    			
    		}else{// The building can't be build. Should never happend if the orders is checked then the select box is filled.
    			buildBuilding =  false;
    			uniqueBuildingString = "You can not build the player unique " + buildingType.getName() + " building, you have already the building.";
    		}
    	}
    	
    	if(buildBuilding){
			planet.getPlayerInControl().removeFromTreasury(buildingType.getBuildCost(tempVIP));
			
			Building tempBuilding = null;
			tempBuilding = buildingType.getBuilding(planet, g);
			// add the building to the planet.
			planet.addBuilding(tempBuilding);
			// if the building have any parent building this is a upgrade and the parent building should be removed
			if(tempBuilding.getBuildingType().getParentBuilding() != null){
				ti.addToLatestExpenseReport("You have upgraded a " + tempBuilding.getBuildingType().getParentBuilding().getName() + " to a " + tempBuilding.getBuildingType().getName() + " at the planet " + planet.getName() + ".");
				ti.addToLatestExpenseReport("Cost to upgrade " + tempBuilding.getBuildingType().getName() + ": " + buildingType.getBuildCost(tempVIP) + ".");
		        //planet.removeBuilding(tempBuilding.getBuildingType().getName());
		        planet.removeBuilding(currentBuildingId);
			}else{
				ti.addToLatestExpenseReport("You have built a new " + tempBuilding.getBuildingType().getName() + ") at the planet " + planet.getName() + ".");
				ti.addToLatestExpenseReport("Cost to build new " + tempBuilding.getBuildingType().getName() + ": " + buildingType.getBuildCost(tempVIP) + ".");
			}
    	}//else{// the building is unique and cant be build.
    	if(!uniqueBuildingString.equalsIgnoreCase("")){
    		ti.addToLatestExpenseReport(uniqueBuildingString);
    	}
    }else
    if (type.equalsIgnoreCase("buildship")){
    	
    	String uniqueBuildingString="";
    	boolean buildShip = true;
    	
    	SpaceshipType sst = g.getPlayer(playerName).findSpaceshipType(spaceshipTypeName);
    	
    	if(sst.isWorldUnique()){
    		if(!sst.isWorldUniqueBuild(p.getGalaxy())){
    			uniqueBuildingString = "Congratulations you have build the world unique " + sst.getName() + ".";
    		}else{// The building can't be build.
    			buildShip =  false;
    			uniqueBuildingString = "You can not build the world unique " + sst.getName() + " ship. Some other organisation was faster then you.";
    		}
    	}else
		if(sst.isFactionUnique()){
    		if(!sst.isFactionUniqueBuild(p)){
    			uniqueBuildingString = "Congratulations you have build the faction unique " + sst.getName() + ".";
    		}else{// The building can't be build.
    			buildShip =  false;
    			uniqueBuildingString = "You can not build the faction unique " + sst.getName() + " ship. Some other leader was faster then you.";
    		}
    	}else
    	if(sst.isPlayerUnique()){
    		if(!sst.isPlayerUniqueBuild(p)){
    			uniqueBuildingString = "You have build the player unique " + sst.getName() + " and you can not build more of this type.";
    			
    		}else{// The building can't be build. Should never happend if the orders is checked then the select box is filled.
    			buildShip =  false;
    			uniqueBuildingString = "You can not build the player unique " + sst.getName() + " ship, you have already the ship.";
    		}
    	}
    	
    	if(buildShip){
			Spaceship sstemp = null;
			VIP tempVIP = p.getGalaxy().findVIPShipBuildBonus(planet,p,o);
			VIP tempVIP2 = p.getGalaxy().findVIPTechBonus(planet,p,o);
			int factionTechBonus = p.getFaction().getTechBonus();
			
			sstemp = sst.getShip(tempVIP2,factionTechBonus,planet.getBuildingTechBonus());
			//sstemp = ow.buildShip(sst,tempVIP2,factionTechBonus);
			Logger.finest(" -buildship planet: " + sstemp.getTypeName());
			sstemp.setOwner(planet.getPlayerInControl());
			sstemp.setLocation(planet);
			g.addSpaceship(sstemp);
			ti.addToLatestExpenseReport("You have built a new " + sst.getName() + " (named " + sstemp.getName() + ") at " + planet.getName() + ".");
			// TODO (Tobbe) l�gg bonusen f�r buildings.  Skall bonus addas eller skall den som �r st�rst g�lla.
			planet.getPlayerInControl().removeFromTreasury(sst.getBuildCost(tempVIP));
			Logger.finest(" -buildship loc name: " + planet.getName());
			ti.addToLatestExpenseReport("Cost to build new " + sst.getName() + ": " + sst.getBuildCost(tempVIP) + ".");
			
    	} // the ship is unique and cant be build.
    	if(!uniqueBuildingString.equalsIgnoreCase("")){
    		ti.addToLatestExpenseReport(uniqueBuildingString);
    	}   	
      
    }else
    if (type.equalsIgnoreCase("buildtroop")){
    	
    	String uniqueBuildingString="";
    	boolean buildTroop = true;
    	
    	TroopType troopType = g.getPlayer(playerName).findTroopType(troopTypeName);
    	
    	if(troopType.isWorldUnique()){
    		if(!troopType.isWorldUniqueBuild(p.getGalaxy())){
    			uniqueBuildingString = "Congratulations you have build the world unique " + troopType.getUniqueName() + ".";
    		}else{// The building can't be build.
    			buildTroop =  false;
    			uniqueBuildingString = "You can not build the world unique " + troopType.getUniqueName() + " troop. Some other organisation was faster then you.";
    		}
    	}else
		if(troopType.isFactionUnique()){
    		if(!troopType.isFactionUniqueBuild(p)){
    			uniqueBuildingString = "Congratulations you have build the faction unique " + troopType.getUniqueName() + ".";
    		}else{// The building can't be build.
    			buildTroop =  false;
    			uniqueBuildingString = "You can not build the faction unique " + troopType.getUniqueName() + " troop. Some other leader was faster then you.";
    		}
    	}else
    	if(troopType.isPlayerUnique()){
    		if(!troopType.isPlayerUniqueBuild(p)){
    			uniqueBuildingString = "You have build the player unique " + troopType.getUniqueName() + " and you can not build more of this type.";
    			
    		}else{// The building can't be build. Should never happend if the orders is checked then the select box is filled.
    			buildTroop =  false;
    			uniqueBuildingString = "You can not build the player unique " + troopType.getUniqueName() + " troop, you have already the troop.";
    		}
    	}
    	
    	if(buildTroop){
			
    		Troop tempTroop = null;
            
    		VIP tempVIP = p.getGalaxy().findVIPTroopBuildBonus(planet,p,o);
            VIP tempVIP2 = p.getGalaxy().findVIPTechBonus(planet,p,o);
            int factionTechBonus = p.getFaction().getTechBonus();
            
            tempTroop = troopType.getTroop(tempVIP2,factionTechBonus,planet.getBuildingTechBonus());
            //sstemp = ow.buildShip(sst,tempVIP2,factionTechBonus);
      	  	Logger.finest(" -buildship planet: " + tempTroop.getUniqueName());
      	  	tempTroop.setOwner(planet.getPlayerInControl());
      	  	tempTroop.setPlanetLocation(planet);
      	  	g.addTroop(tempTroop);
      	  	ti.addToLatestExpenseReport("You have built a new " + troopType.getUniqueName() + " (named " + tempTroop.getUniqueName() + ") at " + planet.getName() + ".");
            // TODO (Tobbe) l�gg bonusen f�r buildings.  Skall bonus addas eller skall den som �r st�rst g�lla.
            planet.getPlayerInControl().removeFromTreasury(troopType.getCostBuild(tempVIP));
            Logger.finest(" -buildtroop loc name: " + planet.getName());
            ti.addToLatestExpenseReport("Cost to build new " + troopType.getUniqueName() + ": " + troopType.getCostBuild(tempVIP) + ".");
			
    	}//else{// the ship is unique and cant be build.
    	if(!uniqueBuildingString.equalsIgnoreCase("")){
    		ti.addToLatestExpenseReport(uniqueBuildingString);
    	}
    	
        
    }else
    if (type.equalsIgnoreCase("buildVIP")){
    	
    	String uniqueVIPString="";
    	boolean buildVIP = true;
    	
    	
    	// TODO (Tobbe) g�r om.  g�r inte att anv�nda VIPar fr�n bara player om den �r worldunique.
    	VIPType vipType = p.getGalaxy().findVIPType(typeVIPName);
    	
    	if(vipType.isWorldUnique()){
    		if(!p.getGalaxy().findVIPType(typeVIPName).isWorldUniqueBuild(p.getGalaxy())){
    			uniqueVIPString = "Congratulations you have build the world unique " + typeVIPName + ".";
    		}else{// The VIP can't be build.
    			buildVIP =  false;
    			uniqueVIPString = "You can not build the world unique " + typeVIPName + " VIP. Some other organisation was faster then you.";
    		}
    	}
    	
    	if(vipType.isWorldUnique()){ // anv�nds inte s� l�nge alignment finns kvar. eller alla VIPar ligger i GW
    		if(!vipType.isWorldUniqueBuild(p.getGalaxy())){
    			uniqueVIPString = "Congratulations you have build the world unique " + vipType.getName() + ".";
    		}else{// The VIP can't be build.
    			buildVIP =  false;
    			uniqueVIPString = "You can not build the world unique " + vipType.getName() + " VIP. Some other organisation was faster then you.";
    		}
    	}else
		if(vipType.isFactionUnique()){ // anv�nds inte s� l�nge alignment finns kvar. eller alla VIPar ligger i GW
    		if(!vipType.isFactionUniqueBuild(p)){
    			
    			uniqueVIPString = "Congratulations you have build the faction unique " + vipType.getName() + ".";
    		}else{// The VIP can't be build.
    			buildVIP =  false;
    			uniqueVIPString = "You can not build the faction unique " + vipType.getName() + " VIP. Some other leader was faster then you.";
    		}
    	}else
    	if(vipType.isPlayerUnique()){ // anv�nds inte s� l�nge alignment finns kvar. eller alla VIPar ligger i GW
    		if(!vipType.isPlayerUniqueBuild(p)){
    			uniqueVIPString = "You have build the player unique " + vipType.getName() + " and you can not build more of this type.";
    			
    		}else{// The VIP can't be build. Should never happend if the orders is checked then the select box is filled.
    			buildVIP =  false;
    			uniqueVIPString = "You can not build the player unique " + vipType.getName() + " VIP, you have already the VIP.";
    		}
    	}
    	
    	if(buildVIP){
			
            VIP vip = null;
            
            vip = p.getGalaxy().findVIPType(typeVIPName).createNewVIP(planet.getPlayerInControl(), planet, false);
            Logger.finest(" -buildVIP planet: " + vip.getTypeName());
      	  	g.getAllVIPs().add(vip);
            ti.addToLatestExpenseReport("You have built a new " + vip.getName() + " (named " + vip.getName() + ") at " + planet.getName() + ".");
            planet.getPlayerInControl().removeFromTreasury(vip.getBuildCost());
      	    ti.addToLatestExpenseReport("Cost to build new " + vip.getName() + ": " + vip.getBuildCost() + ".");
    	}//else{// the VIP is unique and cant be build.
    	if(!uniqueVIPString.equalsIgnoreCase("")){
    		ti.addToLatestExpenseReport(uniqueVIPString);
    	}
    	
    	
    	
      
    }else
    if (type.equalsIgnoreCase("transaction")){
    	
      p.addToTreasury(-sum);
    	playerToResive.addToTreasury(sum);
      p.addToGeneral("You have given " + sum + " money to Govenor " + playerToResive.getGovenorName() + ".");
      playerToResive.addToGeneral("You have recieved " + sum + " money from Govenor " + p.getGovenorName() + ".");
      playerToResive.addToHighlights(p.getGovenorName() + ";" + sum,HighlightType.TYPE_GIFT);
    }else
    if (type.equalsIgnoreCase("reconstruct")){
    	planet.setProd(1);
    	planet.setRes(1 + playerToResive.getFaction().getResistanceBonus());
    	planet.setPlayerInControl(playerToResive);
    	playerToResive.getPlanetInfos().setRazed(false,planet.getName());
    	int cost = playerToResive.getFaction().getReconstructCostBase();
    	p.removeFromTreasury(cost);
    	playerToResive.addToGeneral("You have reconstructed the planet " + planet.getName() + " and it is now under your control with a production of 1.");
    	playerToResive.addToHighlights(planet.getName(),HighlightType.TYPE_PLANET_RECONSTRUCTED);
    }else
    if(type.equalsIgnoreCase("research")){
    	p.addToTreasury(-researchO.getCost());
    	ti.addToLatestExpenseReport("You have spend " + researchO.getCost() + " in research on: " + researchO.getAdvantageName() + ".");
    	
    }else if(aBid != null){
//      aBid.addPlayer(player);
      g.addBlackMarketBid(aBid);
    }
  }

  public int getCost(Orders o, Galaxy aGalaxy){
    int cost = 0;
    Planet planet = null;
    Logger.finer("Expense.getCost(Orders o, Galaxy aGalaxy) type: " +  type);
    if(planetName != null && !planetName.equalsIgnoreCase("")){
    	planet = aGalaxy.getPlanet(planetName);
    }
    
    if (type.equalsIgnoreCase("pop")){
      cost = planet.getPopulation();
    }else
    if (type.equalsIgnoreCase("res")){
      cost = planet.getResistance();
    }else
    if (type.equalsIgnoreCase("building")){
    	Logger.finer("planetName: " + planetName);
    	Logger.finer("planet: " + planet.getName());
    	Logger.finer("planet.getPlayerInControl(): " + planet.getPlayerInControl());
    	VIP tempVIP = aGalaxy.findVIPBuildingBuildBonus(planet,planet.getPlayerInControl(),o);
    	Player aPlayer = aGalaxy.getPlayer(playerName);
    	BuildingType aBuildingType = aPlayer.findBuildingType(buildingTypeName);
    	cost =  aBuildingType.getBuildCost(tempVIP);
    }else
    if (type.equalsIgnoreCase("buildship")){
    	// kollar f�rst om det finns en engineer vid planeten
    	VIP tempEngineer = aGalaxy.findVIPShipBuildBonus(planet,planet.getPlayerInControl(),o);
    	cost = aGalaxy.getPlayer(playerName).findSpaceshipType(spaceshipTypeName).getBuildCost(tempEngineer);
    }
    else
    if (type.equalsIgnoreCase("buildtroop")){
    	// first check if there is an engineer at the planet
    	VIP tempVIP = aGalaxy.findVIPTroopBuildBonus(planet,planet.getPlayerInControl(),o);
    	TroopType troopType = aGalaxy.getPlayer(playerName).findTroopType(troopTypeName);
    	cost = troopType.getCostBuild(tempVIP);
    	
    }else
    if (type.equalsIgnoreCase("buildVIP")){
    	VIPType tempVIPType = aGalaxy.getGameWorld().getVIPTypeByName(typeVIPName);
    	if(tempVIPType == null){
    		tempVIPType = aGalaxy.getPlayer(playerName).getVIPType(typeVIPName);
    	}
        cost = tempVIPType.getBuildCost();
    }else
    if (type.equalsIgnoreCase("transaction")){
      cost = sum;
    }else
    if (type.equalsIgnoreCase("blackmarketbid")){
      cost = aBid.getCost();
    }else
    if (type.equalsIgnoreCase("reconstruct")){
    	cost = aGalaxy.getPlayer(playerName).getFaction().getReconstructCost(planet);
    }else{
        if (type.equalsIgnoreCase("research")){
        	cost = researchO.getCost();
        }
    }
    return cost;
  }

  public String getType(){
	  return type;
  }

  public String getText(Galaxy aGalaxy,Orders orders){
    String returnString = "";
    if (type.equalsIgnoreCase("pop")){
      returnString = "Increase production on " + planetName + " with +1.";
    }else
    if (type.equalsIgnoreCase("res")){
      returnString = "Increase resistance on " + planetName + " with +1.";
    }else
    if (type.equalsIgnoreCase("building")){
    	BuildingType buildingType = aGalaxy.getPlayer(playerName).findBuildingType(buildingTypeName);
    	if(buildingType.getParentBuilding() == null){
    		returnString = "Build new " + buildingType.getName() + " at " + planetName + ".";
    	}else{
    		returnString = "Upgrade " + buildingType.getParentBuilding().getName() + " to " + buildingType.getName() + " at " + planetName + ".";
    	}
    }else
    if (type.equalsIgnoreCase("buildship")){
    	SpaceshipType sst = aGalaxy.getPlayer(playerName).findSpaceshipType(spaceshipTypeName);
    	returnString = "Build new " + sst.getName() + " at " + planetName + ".";
    }else
    if (type.equalsIgnoreCase("buildtroop")){
    	TroopType troopType = aGalaxy.getPlayer(playerName).findTroopType(troopTypeName);
        returnString = "Build new " + troopType.getUniqueName() + " at " + planetName + ".";
    }else
    if (type.equalsIgnoreCase("buildVIP")){
        returnString = "Build new " + typeVIPName + " at " + planetName + ".";
    }else
    if (type.equalsIgnoreCase("transaction")){
      returnString = "Transfer " + sum + " money to Govenor " + aGalaxy.getPlayer(playerName).getGovenorName();
    }else
    if(type.equalsIgnoreCase("blackmarketbid")){
      returnString = aBid.getText();
    }else
    if(type.equalsIgnoreCase("reconstruct")){
      returnString = "Reconstruct the planet " + planetName;
    }else
    if(type.equalsIgnoreCase("research")){
    	returnString = "Research on " + researchO.getAdvantageName();
    }
    int cost = getCost(orders,aGalaxy);
    returnString += " (cost: " + cost + ")";
    return returnString;
  }

  public String getPlanetName(){
    return planetName;
  }
/*
  public int getOrbitalWharfId(){
    return ow.getId();
  }
*/
  public String getSpaceshipTypeName(){
    return spaceshipTypeName;
  }
  
  
  public String getVIPType(){
	  return this.typeVIPName;
  }

  public BlackMarketBid getBlackMarketBid(){
    return aBid;
  }
  
  public boolean isBuildBuildingAt(Planet aPlanet, BuildingType aBuildingType){
	    boolean returnValue = false;
	    if ((type.equalsIgnoreCase("building")) & (aPlanet.getName().equalsIgnoreCase(planetName)) & (buildingTypeName.equalsIgnoreCase(aBuildingType.getName()))){
	      returnValue = true;
	    }
	    return returnValue;
	}
  
  public boolean isBuildBuildingAt(Planet aPlanet){
	    boolean returnValue = false;
	    if ((type.equalsIgnoreCase("building")) & (aPlanet.getName().equalsIgnoreCase(planetName)) & (currentBuildingId == 0)){
	      returnValue = true;
	    }
	    return returnValue;
	}
  
  public boolean isUpgradeBuilding(Building aBuilding){
	    boolean returnValue = false;
	    if ((type.equalsIgnoreCase("building")) && (currentBuildingId > 0) && (currentBuildingId == aBuilding.getUniqueId()) && (buildingTypeName != null)){
	      returnValue = true;
	    }
	    return returnValue;
	}
/*
  public boolean isBuildWharfAt(Planet aPlanet){
    boolean returnValue = false;
    if ((type.equalsIgnoreCase("newwharf")) & (aPlanet == planet)){
      returnValue = true;
    }
    return returnValue;
  }

  public boolean isBuildSpaceStationAt(Planet aPlanet){
	  boolean returnValue = false;
	  if ((type.equalsIgnoreCase("newspacestation")) & (aPlanet == planet)){
		  returnValue = true;
	  }
	  return returnValue;
  }
*/
  public boolean isIncPopAt(Planet aPlanet){
    boolean returnValue = false;
    if ((type.equalsIgnoreCase("pop")) & (aPlanet.getName().equalsIgnoreCase(planetName))){
      returnValue = true;
    }
    return returnValue;
  }

  public boolean isIncResAt(Planet aPlanet){
    boolean returnValue = false;
    if ((type.equalsIgnoreCase("res")) & (aPlanet.getName().equalsIgnoreCase(planetName))){
      returnValue = true;
    }
    return returnValue;
  }

  public boolean isBuildingBuildingShip(Building aBuilding){
	  Logger.finer("aBuilding.getUniqueName(): " + aBuilding.getUniqueName());
	  Logger.finer("type currentBuildingId aBuilding.getUniqueId()" + type + " " + currentBuildingId + " " + aBuilding.getUniqueId());
	    boolean isBilding = false;
	    if (type.equalsIgnoreCase("buildship")){
	      if (currentBuildingId == aBuilding.getUniqueId()){
	    	  isBilding = true;
	      }
	    }
	    return isBilding;
	  }
  
  public boolean isBuildingBuildingTroop(Building aBuilding){
	    boolean isBilding = false;
	    if (type.equalsIgnoreCase("buildtroop")){
	      if (currentBuildingId == aBuilding.getUniqueId()){
	    	  isBilding = true;
	      }
	    }
	    return isBilding;
	  }
  
  public boolean isBuildingBuildingVIP(Building aBuilding){
	    boolean isBilding = false;
	    if (type.equalsIgnoreCase("buildVIP")){
	      if (currentBuildingId == aBuilding.getUniqueId()){
	    	  isBilding = true;
	      }
	    }
	    return isBilding;
	  }
  
  /*public boolean isWharfBuilding(OrbitalWharf aWharf){
    boolean isWharf = false;
    if (type.equalsIgnoreCase("buildship")){
      if (ow == aWharf){
        isWharf = true;
      }
    }
    return isWharf;
  }*/

  public boolean isReconstructAt(Planet aPlanet){
	  boolean returnValue = false;
	  if ((type.equalsIgnoreCase("reconstruct")) & (aPlanet.getName().equalsIgnoreCase(planetName))){
		  returnValue = true;
	  }
	  return returnValue;
  }

  public boolean isBlackMarketBid(){
    return type.equalsIgnoreCase("blackmarketbid");
  }

  public boolean isTransaction(){
    return type.equalsIgnoreCase("transaction");
  }

  public boolean isBuilding(Building aBuilding){
	  Logger.finer("############");
	  Logger.finer("aBuilding.getUniqueName(): " + aBuilding.getUniqueName());
	  Logger.finer("type.equalsIgnoreCase(building): " + type.equalsIgnoreCase("building"));
	  Logger.finer("currentBuildingId > 0: " + (currentBuildingId > 0));
	  Logger.finer("aBuilding.getUniqueId() == currentBuildingId: " + (aBuilding.getUniqueId() == currentBuildingId));
	  Logger.finer("type currentBuildingId aBuilding.getUniqueId()" + type + " " + currentBuildingId + " " + aBuilding.getUniqueId());
	  return ((type.equalsIgnoreCase("building")) && currentBuildingId > 0 && (aBuilding.getUniqueId() == currentBuildingId));
  }
  
  public boolean isBuilding(Planet aPlanet){
	  return ((type.equalsIgnoreCase("building")) && aPlanet.getName().equalsIgnoreCase(planetName) && currentBuildingId == 0);
  }
  
  public boolean isResearchOrder(String researchName){
	  if(type.equalsIgnoreCase("research")){
	    return (researchO.getAdvantageName().equalsIgnoreCase(researchName));
	  }
  return false;
  }

  public String getPlayerName(){
    return playerName;
  }

  public int getSum(){
    return sum;
  }

  public String getBuildingTypeName() {
	  return buildingTypeName;
  }

  public void setBuildingType(BuildingType buildingType) {
	  this.buildingTypeName = buildingType.getName();
  }

public String getTroopTypeName() {
	return troopTypeName;
}

public String getVipTypeName() {
	return typeVIPName;
}

public int getCurrentBuildingId(){
	return currentBuildingId;
}



}
