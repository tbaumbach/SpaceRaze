package sr.server.gameworlds;

import sr.enums.BlackMarketFrequency;
import sr.enums.SpaceshipRange;
import sr.enums.SpaceshipTargetingType;
import sr.world.Alignment;
import sr.world.Alignments;
import sr.world.BuildingType;
import sr.world.Buildings;
import sr.world.Corruption;
import sr.world.Faction;
import sr.world.GameWorld;
import sr.world.SpaceStation;
import sr.world.SpaceshipType;
import sr.world.UniqueIdCounter;
import sr.world.VIPType;

public class Universe3051{
	

	public static GameWorld getGameWorld(){
		// XXX Spaceraze Expanded
		GameWorld gw = new GameWorld();
		
//	    gw.setCumulativeBombardment(true);
//		gw.setSquadronsSurviveOutsideCarriers(false);

	    gw.setFileName("universe");

	    gw.setFullName("Universe Year 3051");
	    

	    gw.setBattleSimDefaultShips1("crv;crv;crv;sct");
	    gw.setBattleSimDefaultShips2("Crk;Crk;Rai;cou");
	    
	    gw.setShortDescription("An Advanced beginner to intermediate world based on that all factions shall have the same ship types. The World Still have many advanced features of the Spaceraze design.");
        gw.setHistory("Corporations has taken control over the known universe, in the Year 3051 an Universal war breaks out, smaller neutral systems tries to avoid to get involved. Instead they are just considered as pawns in a bigger game, there alliances and borders are decided by the larger richer corporations");
        
	    

	    gw.setCreatedDate("2005-10-26");
		gw.setChangedDate("2005-10-27");
		gw.setCreatedByUser("Niohl");
		
		gw.setRazedPlanetChance(5);
		gw.setClosedNeutralPlanetChance(30);

		Alignments alignments = gw.getAlignments();
		Alignment neutral = alignments.findAlignment("neutral");
		//Alignment good = alignments.findAlignment("good");
		//Alignment evil = alignments.findAlignment("evil");

	    UniqueIdCounter uniqueShipIdCounter = new UniqueIdCounter();
	    UniqueIdCounter uniqueBuildingIdCounter = new UniqueIdCounter();
	    UniqueIdCounter uniqueVIPIdCounter = new UniqueIdCounter();
        
	    
		Corruption tmpCorruption = new Corruption();
		tmpCorruption.addBreakpoint(25, 5);
		tmpCorruption.addBreakpoint(50, 10);
		tmpCorruption.addBreakpoint(75, 15);
		tmpCorruption.addBreakpoint(100, 20);
		tmpCorruption.addBreakpoint(125, 25);
		tmpCorruption.addBreakpoint(150, 50);
//###################  GOV  ##########################################
        

        VIPType tmpVipType = new VIPType("Governor","Gov",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        gw.addVipType(tmpVipType);

        VIPType govType = tmpVipType;
        
//    	###################  VIP  ##########################################	        
        
        

        tmpVipType = new VIPType("Master Spy","Spy",neutral,uniqueVIPIdCounter);
        tmpVipType.setBuildCost(20);
        tmpVipType.setUpkeep(3);
        tmpVipType.setCounterEspionage(50);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setSpying(true);
        tmpVipType.setCounterEspionage(50);
              
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Commando Unit","CU",neutral,uniqueVIPIdCounter);
        tmpVipType.setBuildCost(40);
        tmpVipType.setUpkeep(3);
        tmpVipType.setAssassination(60);
        tmpVipType.setPsychWarfareBonus(2);
        tmpVipType.setResistanceBonus(5);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);        
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Assassin","Ass",neutral,uniqueVIPIdCounter);
        tmpVipType.setBuildCost(25);
        tmpVipType.setUpkeep(2);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setAssassination(70);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);        
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("General","Gen",neutral,uniqueVIPIdCounter);
        tmpVipType.setCounterEspionage(40);
        tmpVipType.setBuildCost(30);
        tmpVipType.setUpkeep(3);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setFrequency(BlackMarketFrequency.SOMEWHAT_UNCOMMON);        
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Astrogator","Ast",neutral,uniqueVIPIdCounter);
        tmpVipType.setCounterEspionage(20);
        tmpVipType.setBuildCost(40);
        tmpVipType.setUpkeep(4);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        tmpVipType.setFTLbonus(true);
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Admiral","Adm",neutral,uniqueVIPIdCounter);
        tmpVipType.setCounterEspionage(30);
        tmpVipType.setBuildCost(40);
        tmpVipType.setUpkeep(4);
        tmpVipType.setInitBonus(10);
        tmpVipType.setFrequency(BlackMarketFrequency.SOMEWHAT_UNCOMMON);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Top Ace","Ace",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitFighterSquadronBonus(5);
        tmpVipType.setBuildCost(20);
        tmpVipType.setUpkeep(3);
        tmpVipType.setCounterEspionage(10);
        tmpVipType.setDescription("The Top Ace, gives the squadrons better inititive. A top ace is a squadron’s pilot and must be on a squadron to give bonus. He boosts all squadrons in a fleet.");
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Economic Genious","Eco",neutral,uniqueVIPIdCounter);
        tmpVipType.setCounterEspionage(10);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setFrequency(BlackMarketFrequency.SOMEWHAT_UNCOMMON);
        tmpVipType.setOpenIncBonus(4);
        tmpVipType.setClosedIncBonus(2);
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Spaceship Engineer","Eng",neutral,uniqueVIPIdCounter);
        tmpVipType.setCounterEspionage(20);
        tmpVipType.setBuildCost(30);
        tmpVipType.setUpkeep(4);
        tmpVipType.setFrequency(BlackMarketFrequency.SOMEWHAT_UNCOMMON);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setShipBuildBonus(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Scientist","Sci",neutral,uniqueVIPIdCounter);
        tmpVipType.setCounterEspionage(30);
        tmpVipType.setBuildCost(40);
        tmpVipType.setUpkeep(4);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setTechBonus(10);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Expert Scientist","ESci",neutral,uniqueVIPIdCounter);
        tmpVipType.setCounterEspionage(40);
        tmpVipType.setBuildCost(50);
        tmpVipType.setUpkeep(5);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setTechBonus(15);
        tmpVipType.setFrequency(BlackMarketFrequency.SOMEWHAT_UNCOMMON);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Diplomat","Dip",neutral,uniqueVIPIdCounter);
        tmpVipType.setCounterEspionage(40);
        tmpVipType.setBuildCost(40);
        tmpVipType.setUpkeep(4);
        tmpVipType.setFrequency(BlackMarketFrequency.SOMEWHAT_UNCOMMON);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        gw.addVipType(tmpVipType);	    
	    
//###################  Buildings  ##########################################
        Buildings tempBuildings = new Buildings();
        BuildingType tempBuildingType;
        
        tempBuildingType= new BuildingType("Small Wharf", "W1", 5, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(1);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Small Wharf what can build small ships");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Medium Wharf", "W2", 7, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(2);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Medium Wharf what can build small and medium ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Small Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large Wharf", "W3", 15, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(3);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge Wharf", "W5", 24, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempBuildings.addBuilding(tempBuildingType);

        tempBuildingType= new BuildingType("Military Spaceport", "MSP", 8, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Build this building on the planets and long range ways will be short range between planetes whit spaceports");
        tempBuildings.addBuilding(tempBuildingType);

        tempBuildingType= new BuildingType("Commercial Spaceport", "CSP",6, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setOpenPlanetBonus(2);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Military Spaceport"));
        tempBuildingType.setDescription("Extending the military Spaceport to become a commercial hub");
        tempBuildings.addBuilding(tempBuildingType);
        

        
        
//      ###################  New Building Types  ##########################################
       
        tempBuildingType= new BuildingType("Improved Infrastructure", "IIS", 7, uniqueBuildingIdCounter);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("Preparation to build, advanced buildings");
        tempBuildings.addBuilding(tempBuildingType);
     
        
        tempBuildingType= new BuildingType("Security Center", "SC", 20, uniqueBuildingIdCounter);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Assassin"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Master Spy"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Commando Unit"));
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("Security Center to train undercover VIPs");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Improved Infrastructure"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Military Academy", "MAC", 20, uniqueBuildingIdCounter);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Admiral"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Top Ace"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("General"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Astrogator"));
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("Academy to train military VIPs");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Improved Infrastructure"));
        tempBuildings.addBuilding(tempBuildingType);

        tempBuildingType= new BuildingType("Advanced University", "AU", 20, uniqueBuildingIdCounter);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Scientist"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Expert Scientist"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Economic Genious"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Spaceship Engineer"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Diplomat"));
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("Academy to train Civilian VIPs");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Improved Infrastructure"));
        tempBuildings.addBuilding(tempBuildingType);

        tempBuildingType= new BuildingType("City", "City", 4, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setOpenPlanetBonus(1);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("City, Increase Open Planet Income +1.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Metropolis", "Met", 8, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setOpenPlanetBonus(3);
        tempBuildingType.setResistanceBonus(1);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("Megatropolis, Increase Open Planet Income +3.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("City"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Capital", "Cap", 16, uniqueBuildingIdCounter);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setOpenPlanetBonus(5);
        tempBuildingType.setResistanceBonus(2);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("Player Capital gives 5 in income if the planet is open. Gives bonus to the defending ground force. Only one Universal Trade Center can be build each player.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Metropolis"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Faction Capital", "FCap", 32, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(8);
        tempBuildingType.setResistanceBonus(5);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setFactionUnique(true);
        tempBuildingType.setDescription("Capital that gives 8 in incom if the planet is open. Gives a big bonus to the defending ground force. Only one Capital can be build for each faction.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Capital"));
        tempBuildings.addBuilding(tempBuildingType);

        tempBuildingType= new BuildingType("Quadrant Capital", "QCap", 64, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(12);
        tempBuildingType.setResistanceBonus(5);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setWorldUnique(true);
        tempBuildingType.setDescription("Capital that gives 8 in incom if the planet is open. Gives a big bonus to the defending ground force. Only one Capital can be build for each faction.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Faction Capital"));
        tempBuildings.addBuilding(tempBuildingType);

        
      
        
        tempBuildingType= new BuildingType("Tech Center", "TC", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setTechBonus(10);        
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("Tech center give better technology to units.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Quadrant Tech Center", "ATC", 50, uniqueBuildingIdCounter);
        tempBuildingType.setTechBonus(25);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setWorldUnique(true);
        tempBuildingType.setDescription("Advanced tech center gives even better technology to units than tech center do.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Tech Center"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        
        tempBuildingType= new BuildingType("Basic Shield", "BS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A small shield to defend planets from bombardment.");
        tempBuildings.addBuilding(tempBuildingType);

        tempBuildingType= new BuildingType("Advanced Shield", "AS", 25, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(3);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDescription("Advanced Shield, Defends planets from most bombardments.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Basic Shield"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Star Shield", "TOS", 70, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(5);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setWorldUnique(true);
        tempBuildingType.setDescription("The One Sheild, Defends against all bombardment.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Advanced Shield"));
        tempBuildings.addBuilding(tempBuildingType);

        
        tempBuildingType= new BuildingType("Missile Silo", "MS", 15, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(100);
        tempBuildingType.setCannonHitChance(90);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A planet based missile system to shoot down ships what besiege the planet.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Missile Defence", "MD", 25, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(100);
        tempBuildingType.setCannonHitChance(95);
        tempBuildingType.setCannonRateOfFire(3);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A planet based missile system to shoot down ships in great numbers, that besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Missile Silo"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Star Missile Defence", "SMD", 70, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(1000);
        tempBuildingType.setCannonHitChance(95);
        tempBuildingType.setCannonRateOfFire(3);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setWorldUnique(true);
        tempBuildingType.setDescription("A planet based missile system to shoot down ships in great numbers, that besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Missile Defence"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Cannon", "Can", 10, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(100);
        tempBuildingType.setCannonHitChance(75);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A planet based Cannon to shoot down small ships what besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Heavy Cannon", "HC", 20, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(250);
        tempBuildingType.setCannonHitChance(75);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A planet based heavy Cannon to shoot down ships what besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Cannon"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Star Cannon", "SC", 50, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(2500);
        tempBuildingType.setCannonHitChance(75);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setWorldUnique(true);
        tempBuildingType.setDescription("A planet based Star Cannon to shoot down most ships that besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Heavy Cannon"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        
		
		
//###################  DEFENCE  ##########################################

		
		
        // Golan I
        SpaceshipType tempsst = new SpaceshipType("Defence Platform I","DPI",SpaceshipType.SIZE_SMALL,20,100,SpaceshipRange.NONE,1,7,uniqueShipIdCounter,15,25);
        tempsst.setDescription("Defensive Plattform I, great first defence vs enemy raiders, and to complement defence vs Squadrons");
        tempsst.setAdvanteges("Cheap, Initiative bonus(+2), Good vs Small, Very good vs Squadrons");
        tempsst.setDisAdvanteges("Cannot move, Poor vs Medium, Large and Huge capital ships");
        
        tempsst.setArmorSmall(40);
        tempsst.setWeaponsStrengthMedium(10);  
        
        tempsst.setIncreaseInitiative(2);
         
        tempsst.setCanAppearOnBlackMarket(false);
        gw.addShipType(tempsst);
        
        // Golan II
        tempsst = new SpaceshipType("Defence Platform II","DPII",SpaceshipType.SIZE_MEDIUM,50,400,SpaceshipRange.NONE,2,15,uniqueShipIdCounter,30,15);
        tempsst.setDescription("Defence Plattform II, Very usable vs small attacking fleets");
        tempsst.setAdvanteges("Cheap, Initiative bonus(+2), Very good vs Small and Medium");
        tempsst.setDisAdvanteges("Cannot move, Poor vs Large and Huge capital ships");
        
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setArmorSmall(70);
        tempsst.setArmorMedium(60);
        
        tempsst.setIncreaseInitiative(2);
        
        tempsst.setWeaponsStrengthMedium(30);    
        
        gw.addShipType(tempsst);
        
        // Golan III
        tempsst = new SpaceshipType("Defence Platform III","DPIII",SpaceshipType.SIZE_LARGE,100,800,SpaceshipRange.NONE,3,40,uniqueShipIdCounter,10,10);
        tempsst.setDescription("Defence Plattform III, Usable against Medium attacking fleets");
        tempsst.setAdvanteges("Cheap, Initiative bonus(+3), Very good vs Medium, Large");
        tempsst.setDisAdvanteges("Cannot move, Poor vs Medium, Large and Huge capital ships");
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setArmorSmall(90);
        tempsst.setArmorMedium(70);
        tempsst.setArmorLarge(20);
        
        tempsst.setIncreaseInitiative(3);
        
        tempsst.setWeaponsStrengthMedium(25);    
        tempsst.setWeaponsStrengthLarge(70);   
        tempsst.setWeaponsStrengthHuge(90);    
	
        gw.addShipType(tempsst);
	// Home Base
        tempsst = new SpaceshipType("Head Quarter","HQ",SpaceshipType.SIZE_LARGE,400,1600,SpaceshipRange.NONE,0,160,uniqueShipIdCounter,15,15);
        tempsst.setDescription("Defence Plattform III, Usable against Medium attacking fleets");
        tempsst.setAdvanteges("Initiative bonus(+4), Very good vs Medium, Large");
        tempsst.setDisAdvanteges("Cannot move, Poor vs Medium, Large and Huge capital ships");
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setArmorSmall(90);
        tempsst.setArmorMedium(70);
        tempsst.setArmorLarge(50);
        
        tempsst.setIncreaseInitiative(4);
        
    	tempsst.setIncOwnOpenBonus(3);
    	tempsst.setIncOwnClosedBonus(3);
    	
        tempsst.setWeaponsStrengthMedium(50);    
        tempsst.setWeaponsStrengthLarge(70);   
        tempsst.setWeaponsStrengthHuge(40);    
	
        gw.addShipType(tempsst);

//###################  SMALL  ##########################################
        
        // Scout
        tempsst = new SpaceshipType("Scout","Sct",SpaceshipType.SIZE_SMALL,10,50,SpaceshipRange.LONG,1,3,uniqueShipIdCounter,5,5);
        tempsst.setArmorSmall(20);
        tempsst.setDescription("Scout: Useful as a small transport and to scout enemy fleets");
        tempsst.setAdvanteges("Long range, Not visible on map, Cheap");
        tempsst.setDisAdvanteges("Very poor vs all other ships, cannot siege planets");
        
        tempsst.setVisibleOnMap(false);
        tempsst.setCanBlockPlanet(false);
        tempsst.setScreened(true);
       
        gw.addShipType(tempsst);
        
        // Corvette
        tempsst = new SpaceshipType("Corvette","Crv",SpaceshipType.SIZE_SMALL,20,100,SpaceshipRange.LONG,2,6,uniqueShipIdCounter,15,10);
        tempsst.setArmorSmall(40);
        tempsst.setDescription("Corvette: A small capital ship and VIP transport");
        tempsst.setAdvanteges("Long range, Good vs Medium ships, Cheap");
        tempsst.setDisAdvanteges("Poor vs most capital ships");
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        gw.addShipType(tempsst);
        
        // Frigate
        tempsst = new SpaceshipType("Frigate","Frg",SpaceshipType.SIZE_SMALL,20,150,SpaceshipRange.LONG,3,7,uniqueShipIdCounter,25,20);
        tempsst.setArmorSmall(40);
        tempsst.setDescription("An small Escort, used to defend against squadrons and other small ship ");
        gw.addShipType(tempsst);

//###################  MEDIUM  ##########################################
        
        // Troopship
        tempsst = new SpaceshipType("Troopship","Tpt",SpaceshipType.SIZE_MEDIUM,20,75,SpaceshipRange.LONG,2,8,uniqueShipIdCounter,10,10);
        tempsst.setDescription("Troops on troopships can lower a planets resistance by 1");
        tempsst.setArmorSmall(0);
        tempsst.setPsychWarfare(1);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);

        // Destroyer
        tempsst = new SpaceshipType("Destroyer","Des",SpaceshipType.SIZE_MEDIUM,50,250,SpaceshipRange.LONG,6,19,uniqueShipIdCounter,25,25);
        tempsst.setDescription("Useful for long range raiding attacks, and also boost larger task forces with Anti-Squadron coverage");
        tempsst.setArmorSmall(70);
        tempsst.setWeaponsStrengthMedium(40);    
        tempsst.setWeaponsStrengthLarge(20);   
        tempsst.setWeaponsStrengthHuge(50);    
		tempsst.setWeaponsMaxSalvoesLarge(1);
		tempsst.setWeaponsMaxSalvoesHuge(1);
        gw.addShipType(tempsst);

//      Submarine
        tempsst = new SpaceshipType("Submarine","Sub",SpaceshipType.SIZE_MEDIUM,25,150,SpaceshipRange.LONG,5,17,uniqueShipIdCounter,10,5);
        tempsst.setDescription("A Submarine is a Stealth Torpedo carrier");
        tempsst.setArmorSmall(70);
        tempsst.setWeaponsStrengthMedium(10);    
        tempsst.setWeaponsStrengthLarge(300);   
        tempsst.setWeaponsStrengthHuge(300);    
		tempsst.setWeaponsMaxSalvoesLarge(8);
		tempsst.setWeaponsMaxSalvoesHuge(8);
		tempsst.setInitDefence(20);
		tempsst.setVisibleOnMap(false);
		tempsst.setCanAttackScreenedShips(true);
        gw.addShipType(tempsst);

//      Interdictor
        tempsst = new SpaceshipType("Interdictor","Int",SpaceshipType.SIZE_MEDIUM,100,350,SpaceshipRange.LONG,6,20,uniqueShipIdCounter,10,10);
        tempsst.setDescription("Stop Fleeing ships");
        tempsst.setArmorSmall(50);
        tempsst.setSquadronCapacity(1);
        tempsst.setWeaponsStrengthMedium(15);    
        tempsst.setWeaponsStrengthLarge(20);   
        tempsst.setWeaponsStrengthHuge(20);    
		tempsst.setWeaponsMaxSalvoesLarge(1);
		tempsst.setWeaponsMaxSalvoesHuge(1);
		tempsst.setPsychWarfare(1);
        tempsst.setNoRetreat(true);
        gw.addShipType(tempsst);

        // Carrier
        tempsst = new SpaceshipType("Carrier","Car",SpaceshipType.SIZE_MEDIUM,40,300,SpaceshipRange.LONG,6,18,uniqueShipIdCounter,10,10);
        tempsst.setDescription("Backbone in every large fleet is the carrier and its large supply of Squadrons. Keep it Screened");
        tempsst.setArmorSmall(50);
        tempsst.setSquadronCapacity(8);
        tempsst.setScreened(true);

        gw.addShipType(tempsst);        
        
//###################  LARGE  ##########################################

        // Commandship
        tempsst = new SpaceshipType("Commandship","Com",SpaceshipType.SIZE_LARGE,50,400,SpaceshipRange.SHORT,7,27,uniqueShipIdCounter,15,10);
        tempsst.setDescription("The Natural Leader in any major task force, with its Iniative bonus it can help a smaller fleet beat an larger one");
        tempsst.setPsychWarfare(2);
        tempsst.setIncreaseInitiative(7);
        tempsst.setArmorSmall(50);
        tempsst.setSquadronCapacity(2); 
        tempsst.setPlanetarySurvey(true);        
        tempsst.setWeaponsStrengthMedium(25);    
        tempsst.setWeaponsStrengthLarge(30);   
        tempsst.setWeaponsStrengthHuge(25);
	    
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setWeaponsMaxSalvoesHuge(3);

        gw.addShipType(tempsst);
   
        // Cruiser
        tempsst = new SpaceshipType("Cruiser","Cru",SpaceshipType.SIZE_LARGE,150,600,SpaceshipRange.SHORT,9,30,uniqueShipIdCounter,20,15);
        tempsst.setDescription("Main battle unit, Great against most enemies");
        tempsst.setBombardment(1);
        tempsst.setArmorSmall(90);
        tempsst.setArmorMedium(75);
        
        tempsst.setWeaponsStrengthMedium(90);    
        tempsst.setWeaponsStrengthLarge(60);   
        tempsst.setWeaponsStrengthHuge(60);    
        tempsst.setWeaponsMaxSalvoesLarge(6);
        tempsst.setWeaponsMaxSalvoesHuge(6);

        
        tempsst.setArmorMedium(25);
        tempsst.setSquadronCapacity(2);
        gw.addShipType(tempsst);

//###################  HUGE  ##########################################
        
        // Battleship
        tempsst = new SpaceshipType("Battleship","Bat",SpaceshipType.SIZE_HUGE,300,2200,SpaceshipRange.SHORT,19,70,uniqueShipIdCounter,15,10);
        tempsst.setDescription("Useful as flagship in major task forces, Its bombardment is feared amongst its enemies");
        tempsst.setArmorSmall(75);
        tempsst.setArmorMedium(80);
        tempsst.setArmorLarge(60);
        tempsst.setBombardment(3);
        tempsst.setPsychWarfare(1);
       
        tempsst.setWeaponsStrengthMedium(50);    
        tempsst.setWeaponsStrengthLarge(100);   
        tempsst.setWeaponsStrengthHuge(100);    
	
	
        tempsst.setSquadronCapacity(2);
        gw.addShipType(tempsst);
        
        
        // Battlestar
        tempsst = new SpaceshipType("Battlestar","Star",SpaceshipType.SIZE_HUGE,800,4500,SpaceshipRange.SHORT,36,130,uniqueShipIdCounter,15,10);
        tempsst.setDescription("Useful as flagship in major task forces, Its bombardment is feared amongst its enemies");
        tempsst.setArmorSmall(75);
        tempsst.setArmorMedium(80);
        tempsst.setArmorLarge(60);
        tempsst.setBombardment(5);
        tempsst.setPsychWarfare(1);
        tempsst.setWeaponsStrengthMedium(40);    
        tempsst.setWeaponsStrengthLarge(200);   
        tempsst.setWeaponsStrengthHuge(200);    
	
	
        tempsst.setSquadronCapacity(2);
        gw.addShipType(tempsst);
        
        
     


//###################  SQUADRONS  ##########################################
             
        // F1 Fighter
        tempsst = new SpaceshipType("Fighter Sqd","Fgt",SpaceshipType.SIZE_SMALL,10,50,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,15,40);
        tempsst.setArmorSmall(70);
        tempsst.setDescription("Best way of winning Sqadron Superiority is to have the most Fighter Sqds");
        tempsst.setSquadron(true);
        tempsst.setCanAttackScreenedShips(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        gw.addShipType(tempsst);
        
        // B1 Bomber
        tempsst = new SpaceshipType("Bomber Sqd","Bom",SpaceshipType.SIZE_SMALL,10,50,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,20,10);
        tempsst.setArmorSmall(70);
        tempsst.setDescription("A Bomber Squadron can make a big difference against an Small & Medium taskforce");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setCanAttackScreenedShips(true);
        tempsst.setWeaponsStrengthMedium(40);
        tempsst.setWeaponsMaxSalvoesMedium(8);
        
        gw.addShipType(tempsst);
        
        // T1 Torpedo boat
        tempsst = new SpaceshipType("Torpedo Boat","Tor",SpaceshipType.SIZE_SMALL,10,50,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,5,5);
        tempsst.setArmorSmall(70);
        tempsst.setDescription("A Torpedo Boat Squadron can make a big difference against an Large & Huge taskforce");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setCanAttackScreenedShips(true);
        tempsst.setWeaponsStrengthMedium(10);    
        tempsst.setWeaponsStrengthLarge(60);   
        tempsst.setWeaponsStrengthHuge(60);
	    
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setWeaponsMaxSalvoesHuge(3);
        
        gw.addShipType(tempsst);
        
        

        
//###################  Corporation  ##########################################
        
//###################  SMALL  ##########################################
        
        // Courier
        tempsst = new SpaceshipType("Courier","Cou",SpaceshipType.SIZE_SMALL,10,50,SpaceshipRange.LONG,1,3,uniqueShipIdCounter,5,10);
        tempsst.setArmorSmall(20);
        tempsst.setDescription("Useful as scout ship, and can also be used to boost long range raiding forces and to transport VIPs");
        tempsst.setPlanetarySurvey(true);
        tempsst.setCanBlockPlanet(false);
        
        gw.addShipType(tempsst);
        
        // Raider
        tempsst = new SpaceshipType("Raider","Rai",SpaceshipType.SIZE_SMALL,10,130,SpaceshipRange.LONG,2,6,uniqueShipIdCounter,15,15);
        tempsst.setArmorSmall(40);
        tempsst.setDescription("A raider and small VIP transport");
        tempsst.setWeaponsStrengthMedium(40);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        gw.addShipType(tempsst);
        
        // Carrack
        tempsst = new SpaceshipType("Carrack","Crk",SpaceshipType.SIZE_SMALL,10,170,SpaceshipRange.LONG,3,7,uniqueShipIdCounter,20,20);
        tempsst.setArmorSmall(40);
        tempsst.setDescription("An small Escort, used to defend against squadrons and other small ship ");
        gw.addShipType(tempsst);

//###################  MEDIUM  ##########################################
        
        // Troop Galley
        tempsst = new SpaceshipType("Troop Galley","TrG",SpaceshipType.SIZE_MEDIUM,20,75,SpaceshipRange.LONG,2,8,uniqueShipIdCounter,10,10);
        tempsst.setDescription("Troops on troopships can lower a planets resistance by 1");
        tempsst.setArmorSmall(0);
        tempsst.setPsychWarfare(1);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);

        // Crusader
        tempsst = new SpaceshipType("Crusader","Csd",SpaceshipType.SIZE_MEDIUM,10,280,SpaceshipRange.LONG,5,17,uniqueShipIdCounter,20,40);
        tempsst.setDescription("Useful for long range raiding attacks, and also boost larger task forces with Anti-Squadron coverage");
        tempsst.setArmorSmall(70);
        tempsst.setWeaponsStrengthMedium(70);    
        gw.addShipType(tempsst);

//      Attack Submarine
        tempsst = new SpaceshipType("Attack Submarine","ASub",SpaceshipType.SIZE_MEDIUM,15,130,SpaceshipRange.LONG,6,20,uniqueShipIdCounter,10,5);
        tempsst.setDescription("A Submarine is a Stealth Torpedo carrier");
        tempsst.setArmorSmall(70);
        tempsst.setWeaponsStrengthMedium(10);    
        tempsst.setWeaponsStrengthLarge(300);   
        tempsst.setWeaponsStrengthHuge(250);    
        tempsst.setWeaponsMaxSalvoesLarge(16);
        tempsst.setWeaponsMaxSalvoesHuge(8);
        tempsst.setCanAttackScreenedShips(true);
        tempsst.setInitDefence(20);
		tempsst.setVisibleOnMap(false);
        gw.addShipType(tempsst);

//      Retaliator
        tempsst = new SpaceshipType("Retaliator","Ret",SpaceshipType.SIZE_MEDIUM,50,400,SpaceshipRange.LONG,7,25,uniqueShipIdCounter,10,10);
        tempsst.setDescription("Stop Fleeing ships");
        tempsst.setArmorSmall(50);
        tempsst.setSquadronCapacity(1);
        tempsst.setWeaponsStrengthMedium(15);    
        tempsst.setWeaponsStrengthLarge(50);   
        tempsst.setWeaponsStrengthHuge(50);    
		tempsst.setWeaponsMaxSalvoesLarge(1);
		tempsst.setWeaponsMaxSalvoesHuge(1);
		tempsst.setPsychWarfare(2);
		tempsst.setInitDefence(10);		
		tempsst.setNoRetreat(true);
        tempsst.setVisibleOnMap(false);
        gw.addShipType(tempsst);

        // Carrier
      //  tempsst = new SpaceshipType("Carrier2","Car2",SpaceshipType.SIZE_MEDIUM,40,300,SpaceshipRange.SHORT,4,25,uniqueShipIdCounter,10,10);
      //  tempsst.setDescription("Backbone in every large fleet is the carrier and its large supply of Squadrons. Keep it Screened");
      //  tempsst.setArmorSmall(50);
       // tempsst.setSquadronCapacity(8);

        //gw.addShipType(tempsst);        
        
//###################  LARGE  ##########################################

      
        
        // Fleet Galleon
        tempsst = new SpaceshipType("Fleet Galleon","FGa",SpaceshipType.SIZE_LARGE,25,300,SpaceshipRange.LONG,7,25,uniqueShipIdCounter,15,10);
        tempsst.setDescription("The Natural Leader in any major task force, with its Iniative bonus it can help a smaller fleet beat an larger one");
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(5);
        tempsst.setArmorSmall(50);
        tempsst.setSquadronCapacity(5); 
        tempsst.setPlanetarySurvey(true);        
        tempsst.setWeaponsStrengthMedium(15);    

        gw.addShipType(tempsst);
   
        // Dreadnaught
        tempsst = new SpaceshipType("Dreadnaught","Drd",SpaceshipType.SIZE_LARGE,75,900,SpaceshipRange.SHORT,11,36,uniqueShipIdCounter,20,15);
        tempsst.setDescription("Main battle unit, Great against most enemies");
        tempsst.setBombardment(2);
        tempsst.setArmorSmall(90);
        tempsst.setArmorMedium(75);
        
        tempsst.setWeaponsStrengthMedium(80);   
        
        tempsst.setWeaponsStrengthLarge(120);
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setWeaponsStrengthHuge(120);    
        tempsst.setWeaponsMaxSalvoesHuge(3);

        
        tempsst.setArmorMedium(25);
        tempsst.setSquadronCapacity(3);
        gw.addShipType(tempsst);

//###################  HUGE  ##########################################
        
        // Juggernaut
        tempsst = new SpaceshipType("Juggernaut","Jug",SpaceshipType.SIZE_HUGE,350,3000,SpaceshipRange.SHORT,26,90,uniqueShipIdCounter,15,10);
        tempsst.setDescription("Useful as flagship in major task forces, Its bombardment is feared amongst its enemies");
        tempsst.setArmorSmall(75);
        tempsst.setArmorMedium(80);
        tempsst.setArmorLarge(60);
        tempsst.setBombardment(4);
        tempsst.setPsychWarfare(1);
        tempsst.setPlayerUnique(true);
        tempsst.setWeaponsStrengthMedium(60);    
        tempsst.setWeaponsStrengthLarge(160);   
        tempsst.setWeaponsStrengthHuge(160);    
	
	
        tempsst.setSquadronCapacity(4);
        gw.addShipType(tempsst);
        
        
        // ManOWar
        tempsst = new SpaceshipType("ManOWar","MoW",SpaceshipType.SIZE_HUGE,100,1900,SpaceshipRange.SHORT,17,57,uniqueShipIdCounter,15,10);
        tempsst.setDescription("Useful as flagship in major task forces, Its bombardment is feared amongst its enemies");
        tempsst.setArmorSmall(75);
        tempsst.setArmorMedium(80);
        tempsst.setArmorLarge(60);
        tempsst.setBombardment(2);
        tempsst.setPsychWarfare(1);
        tempsst.setWeaponsStrengthMedium(80);    
        tempsst.setWeaponsStrengthLarge(240);   
        tempsst.setWeaponsStrengthHuge(90);    
	
	
        tempsst.setSquadronCapacity(3);
        gw.addShipType(tempsst);
        

//###################  SQUADRONS  ##########################################
             
        // Interceptor
        tempsst = new SpaceshipType("Interceptor","Intc",SpaceshipType.SIZE_SMALL,5,55,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,15,35);
        tempsst.setDescription("Best way of winning Sqadron Superiority is to have the most Fighter Sqds");
        tempsst.setArmorSmall(70);
        tempsst.setSquadron(true);
        tempsst.setCanAttackScreenedShips(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        gw.addShipType(tempsst);
        
        // Gunship
        tempsst = new SpaceshipType("Gunship","Gun",SpaceshipType.SIZE_SMALL,0,70,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,30,15);
        tempsst.setArmorSmall(70);
        tempsst.setDescription("A Bomber Squadron can make a big difference against an Small & Medium taskforce");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(20);
       
        
        gw.addShipType(tempsst);
        
        // Hawk Eye
        tempsst = new SpaceshipType("Hawk Eye","Hwk",SpaceshipType.SIZE_SMALL,5,60,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,5,5);
        tempsst.setArmorSmall(70);
        tempsst.setDescription("A Torpedo Boat Squadron can make a big difference against an Large & Huge taskforce");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(10);    
        tempsst.setWeaponsStrengthLarge(50);   
        tempsst.setWeaponsStrengthHuge(50);
        tempsst.setInitSupport(true);
        tempsst.setIncreaseInitiative(5);
        tempsst.setInitDefence(10);
	    
        tempsst.setWeaponsMaxSalvoesLarge(1);
        tempsst.setWeaponsMaxSalvoesHuge(1);
        
        gw.addShipType(tempsst);
        
        
        
        
//###################  CIVILIANS  ##########################################

        // Merchant
        tempsst = new SpaceshipType("Merchant Fleet","Mer",SpaceshipType.SIZE_LARGE,40,80,SpaceshipRange.SHORT,1,15,uniqueShipIdCounter,5,5);
        tempsst.setDescription("Earn Extra credits by having one of these on each planet");
        tempsst.setArmorSmall(50);
        tempsst.setCanBlockPlanet(false);
        
    	tempsst.setIncOwnOpenBonus(3);
    	tempsst.setIncOwnClosedBonus(2);
    	tempsst.setIncFrendlyOpenBonus(3);
    
    	tempsst.setIncNeutralOpenBonus(4);
    	tempsst.setIncNeutralClosedBonus(3);
    	tempsst.setIncEnemyClosedBonus(5);
    	tempsst.setIncEnemyOpenBonus(6);        
        tempsst.setScreened(true);
        gw.addShipType(tempsst);

	// Supply Ship
        tempsst = new SpaceshipType("Supply Ship","Sup",SpaceshipType.SIZE_MEDIUM,40,80,SpaceshipRange.SHORT,3,8,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(50);
        tempsst.setSupply(4);
        tempsst.setCanBlockPlanet(false);
        tempsst.setScreened(true);
       
        tempsst.setDescription("Supply ships with new weapons is a must, otherwise they will be sitting ducks");
        gw.addShipType(tempsst);
        
        
        
        // Repairship
        //tempsst = new SpaceshipType("Repair dock","Rep",SpaceshipType.SIZE_MEDIUM,40,80,SpaceshipRange.SHORT,1,10,uniqueShipIdCounter,30);
        //tempsst.setDescription("An moveble repair dock, have one in each Task force to repair damaged ships");
        //tempsst.setArmorSmall(50);
        //tempsst.setTroops(true);
        //gw.addShipType(tempsst);

        

        


//###################  SPACE STATIONS  ##########################################
        
        SpaceStation ss = new SpaceStation();
        ss.setOpenProdBonus(2);
        ss.setSpaceport(true);

//###################  FACTIONS  ##########################################

        // Faction 1
        Faction tempFaction = new Faction("House of Mongolia",Faction.getColorHexString(25,70,196),neutral);
        tempFaction.setDescription("Bla bla blaaah....");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battlestar"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Commandship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interdictor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Submarine"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Scout"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bomber Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Boat"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Merchant Fleet"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Head Quarter"));
        
        // Adding all buildings to the faction.
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));        
        tempFaction.setBuildings(tempBuildings);

        
        tempFaction.setCorruption(tmpCorruption);
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(4);

        
        
        gw.addFaction(tempFaction);

// Faction 1
        tempFaction = new Faction("Nippon Corporation",Faction.getColorHexString(210,210,210),neutral);
       
        tempFaction.setDescription("Bla bla blaaah....");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battlestar"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Commandship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interdictor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Submarine"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Scout"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bomber Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Boat"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Merchant Fleet"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Head Quarter"));
        // Adding all buildings to the faction.
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));        
        tempFaction.setBuildings(tempBuildings);

        
        tempFaction.setCorruption(tmpCorruption);
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        
        
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(4);
        
        
        
        gw.addFaction(tempFaction);
        
        // Faction 1
        tempFaction = new Faction("Scandinavian Tech",Faction.getColorHexString(231,255,32),neutral);
        tempFaction.setDescription("Bla bla blaaah....");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battlestar"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Commandship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interdictor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Submarine"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Scout"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bomber Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Boat"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Merchant Fleet"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Head Quarter"));
        
        // Adding all buildings to the faction.
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));        
        tempFaction.setBuildings(tempBuildings);
        
        tempFaction.setCorruption(tmpCorruption);
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        
        
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(4);
        
        
        
        gw.addFaction(tempFaction);
        
        // Faction 1
        tempFaction = new Faction("American Systems",Faction.getColorHexString(32,189,255),neutral);
        tempFaction.setDescription("Bla bla blaaah....");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battlestar"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Commandship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interdictor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Submarine"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Scout"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bomber Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Boat"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Merchant Fleet"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Head Quarter"));
        
        // Adding all buildings to the faction.
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));        
        tempFaction.setBuildings(tempBuildings);

        tempFaction.setCorruption(tmpCorruption);
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        
        
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(4);
        
        
        
        gw.addFaction(tempFaction);
        
        // Faction 1
        tempFaction = new Faction("Emirates Trading Co.",Faction.getColorHexString(255,150,0),neutral);
        tempFaction.setDescription("Bla bla blaaah....");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battlestar"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Commandship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interdictor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Submarine"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Scout"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bomber Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Boat"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Merchant Fleet"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Head Quarter"));
        
        // Adding all buildings to the faction.
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));        
        tempFaction.setBuildings(tempBuildings);
        
        tempFaction.setCorruption(tmpCorruption);
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        
        
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(4);
        
        
        
        gw.addFaction(tempFaction);
        
        // Faction 1
        tempFaction = new Faction("African Union",Faction.getColorHexString(53,202,67),neutral);
        tempFaction.setDescription("Bla bla blaaah....");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battlestar"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Commandship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interdictor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Submarine"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Scout"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bomber Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Boat"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Merchant Fleet"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Head Quarter"));
        
        // Adding all buildings to the faction.
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));        
        tempFaction.setBuildings(tempBuildings);
        
        tempFaction.setCorruption(tmpCorruption);
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        
        
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(4);
                
        gw.addFaction(tempFaction);
        
        // Faction 1
        tempFaction = new Faction("Thai Song Corp.",Faction.getColorHexString(242,130,56),neutral);
        tempFaction.setDescription("Bla bla blaaah....");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battlestar"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Commandship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interdictor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Submarine"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Scout"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bomber Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Boat"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Merchant Fleet"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Head Quarter"));
        
        // Adding all buildings to the faction.
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));        
        tempFaction.setBuildings(tempBuildings);
        
        tempFaction.setCorruption(tmpCorruption);
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        
        
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(4);
        
        
        
        gw.addFaction(tempFaction);
        
        // Faction 1
        tempFaction = new Faction("United Nations",Faction.getColorHexString(56,242,139),neutral);
        tempFaction.setDescription("Bla bla blaaah....");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battlestar"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Commandship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interdictor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Submarine"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Scout"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bomber Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Boat"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Merchant Fleet"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Head Quarter"));
        
        // Adding all buildings to the faction.
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));        
        tempFaction.setBuildings(tempBuildings);
        
        tempFaction.setCorruption(tmpCorruption);
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        
        
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(4);
        
        
        
        gw.addFaction(tempFaction);
        
        // Faction 1
        tempFaction = new Faction("Hindustan Infotech Ltd",Faction.getColorHexString(56,183,242),neutral);
        tempFaction.setDescription("Bla bla blaaah....");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battlestar"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Commandship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interdictor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Submarine"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Scout"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bomber Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Boat"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Merchant Fleet"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Head Quarter"));
        
        // Adding all buildings to the faction.
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));        
        tempFaction.setBuildings(tempBuildings);
        
        tempFaction.setCorruption(tmpCorruption);
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        
        
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(4);
        
        
        
        gw.addFaction(tempFaction);
        
        // Faction 1
        tempFaction = new Faction("Latin BioTech Co",Faction.getColorHexString(139,242,56),neutral);
        tempFaction.setDescription("Bla bla blaaah....");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battlestar"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Commandship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interdictor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Submarine"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Scout"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bomber Sqd"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Boat"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Merchant Fleet"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troopship"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Head Quarter"));
        
        // Adding all buildings to the faction.
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));        
        tempFaction.setBuildings(tempBuildings);
       
        tempFaction.setCorruption(tmpCorruption);
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        
        
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(4);
        
        
        gw.addFaction(tempFaction);
        
        // Faction 1
        tempFaction = new Faction("Galinka Empire",Faction.getColorHexString(215,58,32),neutral);
        tempFaction.setDescription("Bla bla blaaah....");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Juggernaut"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("ManOWar"));
        

        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Dreadnaught"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fleet Galleon"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Crusader"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Galley"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Retaliator"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Attack Submarine"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Courier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrack"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Raider"));
        
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interceptor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Gunship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Hawk Eye"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Merchant Fleet"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Courier"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troop Galley"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Raider"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Carrack"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Carrack"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Head Quarter"));
        
        // Adding all buildings to the faction.
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));        
        tempFaction.setBuildings(tempBuildings);
        

        tempFaction.setCorruption(tmpCorruption);
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(4);
         
        gw.addFaction(tempFaction);
        
        // Faction 1
        tempFaction = new Faction("Australian Empire",Faction.getColorHexString(142,142,142),neutral);
        tempFaction.setDescription("Bla bla blaaah....");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Juggernaut"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("ManOWar"));
        

        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Dreadnaught"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fleet Galleon"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Crusader"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Galley"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Retaliator"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Attack Submarine"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Courier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrack"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Raider"));
        
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interceptor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Gunship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Hawk Eye"));
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Merchant Fleet"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Courier"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troop Galley"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Raider"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Carrack"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Carrack"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Head Quarter"));
        
        // Adding all buildings to the faction.
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));        
        tempFaction.setBuildings(tempBuildings);

        tempFaction.setCorruption(tmpCorruption);
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        
        
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(4);
        
        
        
        gw.addFaction(tempFaction);

        // add shiptypes for neutral planets
        gw.setNeutralSize1(gw.getSpaceshipTypeByName("Defence Platform I"));
        gw.setNeutralSize2(gw.getSpaceshipTypeByName("Defence Platform II"));
        gw.setNeutralSize3(gw.getSpaceshipTypeByName("Defence Platform III"));


	return gw;
	}	
	
}