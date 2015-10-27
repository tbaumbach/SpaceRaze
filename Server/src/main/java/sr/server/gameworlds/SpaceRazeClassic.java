package sr.server.gameworlds;

import sr.enums.BlackMarketFrequency;
import sr.enums.DiplomacyLevel;
import sr.enums.SpaceshipRange;
import sr.world.Alignment;
import sr.world.Alignments;
import sr.world.BuildingType;
import sr.world.Buildings;
import sr.world.Faction;
import sr.world.GameWorld;
import sr.world.SpaceshipType;
import sr.world.UniqueIdCounter;
import sr.world.VIPType;
import sr.world.diplomacy.DiplomacyRelation;
import sr.world.diplomacy.GameWorldDiplomacy;

public class SpaceRazeClassic{

	public static GameWorld getGameWorld(){
		GameWorld gw = new GameWorld();

        gw.setFileName("srclassic");

        gw.setFullName("SpaceRaze Classic");
        gw.setDescription("The classic gameworld used during the development of SpaceRaze");
        
        gw.setBattleSimDefaultShips1("[2]crv;nebb;stc(s)");
        gw.setBattleSimDefaultShips2("[3]gi");

		gw.setCreatedDate("2005-09-26");
		gw.setChangedDate("2005-11-08");
		gw.setCreatedByUser("pabod");

		gw.setAdjustScreenedStatus(false);

//		gw.setSquadronsSurviveOutsideCarriers(false);

		Alignments alignments = gw.getAlignments();
		Alignment neutral = alignments.findAlignment("neutral");
		Alignment good = alignments.findAlignment("good");
		Alignment evil = alignments.findAlignment("evil");

		// Spaceship types
        UniqueIdCounter uniqueShipIdCounter = new UniqueIdCounter();

        // Golan I
        SpaceshipType tempsst = new SpaceshipType("Golan I","GI",SpaceshipType.SIZE_SMALL,10,50,SpaceshipRange.NONE,1,3,uniqueShipIdCounter,10);
        gw.addShipType(tempsst);
        // Golan II
        tempsst = new SpaceshipType("Golan II","GII",SpaceshipType.SIZE_MEDIUM,40,90,SpaceshipRange.NONE,1,6,uniqueShipIdCounter,10);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(30);
        tempsst.setArmorSmall(0);
        gw.addShipType(tempsst);
        // Golan IIB
        tempsst = new SpaceshipType("Golan IIB","GIIB",SpaceshipType.SIZE_MEDIUM,50,120,SpaceshipRange.NONE,1,7,uniqueShipIdCounter,10);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(20);
        tempsst.setArmorSmall(0);
        gw.addShipType(tempsst);
        // Golan III
        tempsst = new SpaceshipType("Golan III","GIII",SpaceshipType.SIZE_LARGE,60,140,SpaceshipRange.NONE,2,9,uniqueShipIdCounter,10);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(30);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(30);
        tempsst.setWeaponsStrengthHuge(10);
        tempsst.setWeaponsMaxSalvoesHuge(30);
        tempsst.setIncreaseInitiative(10);
        tempsst.setInitSupport(true);
        tempsst.setArmorSmall(0);
        tempsst.setArmorMedium(0);
        gw.addShipType(tempsst);
        // Corvette
        tempsst = new SpaceshipType("Corvette","Crv",SpaceshipType.SIZE_SMALL,5,15,SpaceshipRange.LONG,1,3,uniqueShipIdCounter,5);
        gw.addShipType(tempsst);
        // StC
        tempsst = new SpaceshipType("Strike Cruiser","StC",SpaceshipType.SIZE_SMALL,5,20,SpaceshipRange.LONG,2,6,uniqueShipIdCounter,5);
        tempsst.setPsychWarfare(1);
        gw.addShipType(tempsst);
        // SfS
        tempsst = new SpaceshipType("Starfighter Squadron","SfS",SpaceshipType.SIZE_SMALL,0,30,SpaceshipRange.LONG,3,4,uniqueShipIdCounter,10);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setInitSupport(true);
        tempsst.setIncreaseInitiative(10);
        gw.addShipType(tempsst);
        // Nebulon A Frigate
        tempsst = new SpaceshipType("Nebulon A frigate","NebA",SpaceshipType.SIZE_SMALL,20,60,SpaceshipRange.LONG,3,7,uniqueShipIdCounter,15);
        gw.addShipType(tempsst);
        // Lancer
        tempsst = new SpaceshipType("Lancer frigate","Lan",SpaceshipType.SIZE_SMALL,20,50,SpaceshipRange.LONG,3,8,uniqueShipIdCounter,10);
        tempsst.setInitDefence(10);
        gw.addShipType(tempsst);
        // Supply Freighter
        tempsst = new SpaceshipType("Supply Freighter","SF",SpaceshipType.SIZE_MEDIUM,10,20,SpaceshipRange.SHORT,3,8,uniqueShipIdCounter,5);
    	tempsst.setSupply(4);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        // Nebulon B frigate
        tempsst = new SpaceshipType("Nebulon B frigate","NebB",SpaceshipType.SIZE_MEDIUM,40,80,SpaceshipRange.LONG,4,10,uniqueShipIdCounter,20);
        tempsst.setBombardment(1);
        tempsst.setArmorSmall(0);
        gw.addShipType(tempsst);
        // Escort Carrier
        tempsst = new SpaceshipType("Escort Carrier","EsC",SpaceshipType.SIZE_MEDIUM,30,80,SpaceshipRange.LONG,4,12,uniqueShipIdCounter,10);
        tempsst.setIncreaseInitiative(20);
        tempsst.setInitSupport(true);
        tempsst.setArmorSmall(0);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        // Interdictor
        tempsst = new SpaceshipType("Interdictor","Int",SpaceshipType.SIZE_MEDIUM,30,70,SpaceshipRange.LONG,4,14,uniqueShipIdCounter,10);
        tempsst.setNoRetreat(true);
        tempsst.setPsychWarfare(2);
        tempsst.setArmorSmall(0);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
       	// Dreadnaught
       	tempsst = new SpaceshipType("Dreadnaught","Drd",SpaceshipType.SIZE_MEDIUM,50,80,SpaceshipRange.LONG,4,14,uniqueShipIdCounter,20);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setWeaponsStrengthLarge(20);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setArmorSmall(0);
        gw.addShipType(tempsst);
        // Victory Star Destroyer
        tempsst = new SpaceshipType("Victory Star Destroyer","VSD",SpaceshipType.SIZE_LARGE,70,180,SpaceshipRange.SHORT,5,18,uniqueShipIdCounter,20);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(10);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(10);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(10);
        tempsst.setArmorSmall(0);
        tempsst.setArmorMedium(0);
        gw.addShipType(tempsst);
        // Mon Calamari Cruiser
        tempsst = new SpaceshipType("Mon Calamari Cruiser","MCC",SpaceshipType.SIZE_LARGE,80,200,SpaceshipRange.SHORT,6,20,uniqueShipIdCounter,20);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(15);
        tempsst.setWeaponsStrengthLarge(15);
        tempsst.setWeaponsMaxSalvoesLarge(15);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(20);
        tempsst.setArmorSmall(0);
        tempsst.setArmorMedium(0);
        gw.addShipType(tempsst);
        // Imperial Star Destroyer
        tempsst = new SpaceshipType("Imperial Star Destroyer","ISD",SpaceshipType.SIZE_LARGE,100,250,SpaceshipRange.SHORT,8,24,uniqueShipIdCounter,20);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(20);
        tempsst.setWeaponsMaxSalvoesLarge(20);
        tempsst.setBombardment(2);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(30);
        tempsst.setArmorSmall(0);
        tempsst.setArmorMedium(0);
        gw.addShipType(tempsst);
        // Super Star Destroyer
        tempsst = new SpaceshipType("Super Star Destroyer","SSD",SpaceshipType.SIZE_HUGE,120,350,SpaceshipRange.SHORT,12,40,uniqueShipIdCounter,25);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsMaxSalvoesMedium(25);
        tempsst.setWeaponsStrengthLarge(20);
        tempsst.setWeaponsMaxSalvoesLarge(25);
        tempsst.setBombardment(3);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(40);
        tempsst.setArmorSmall(0);
        tempsst.setArmorMedium(0);
        tempsst.setArmorLarge(0);
        gw.addShipType(tempsst);

        // add shiptypes for neutral planets
        gw.setNeutralSize1(gw.getSpaceshipTypeByName("Golan I"));
        gw.setNeutralSize2(gw.getSpaceshipTypeByName("Golan II"));
        gw.setNeutralSize3(gw.getSpaceshipTypeByName("Golan III"));

        // vip types
        UniqueIdCounter uniqueVIPIdCounter = new UniqueIdCounter();

        VIPType tmpVipType = new VIPType("Governor","Gov",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        
        VIPType govType = tmpVipType;

        tmpVipType = new VIPType("Dark Jedi","DJ",evil,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setInitBonus(10);
//        tmpVipType.setAlignment(Alignment.EVIL);
        tmpVipType.setHardToKill(true);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setAssassination(50);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setWellGuarded(true);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Light Jedi","LJ",good,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setInitBonus(10);
//        tmpVipType.setAlignment(Alignment.GOOD);
        tmpVipType.setHardToKill(true);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setSpying(true);
        tmpVipType.setCounterEspionage(50);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setWellGuarded(true);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Master Spy","Spy",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setSpying(true);
        tmpVipType.setCounterEspionage(50);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Economic Genius","Eco",neutral,uniqueVIPIdCounter);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setOpenIncBonus(3);
        tmpVipType.setClosedIncBonus(1);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("General","Gen",neutral,uniqueVIPIdCounter);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setResistanceBonus(3);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Admiral","Adm",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitBonus(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Top Ace","Ace",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitBonus(10);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Assassin","Ass",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setAssassination(50);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Expert Engineer","Eng",neutral,uniqueVIPIdCounter);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setTechBonus(10);
/*        tmpVipType.setSmallBuildBonus(1);
        tmpVipType.setMediumBuildBonus(2);
        tmpVipType.setLargeBuildBonus(3);
        tmpVipType.setHugeBuildBonus(10);
        tmpVipType.setWharfBuildBonus(2);
        tmpVipType.setWharfUpgradeBonus(2);*/
        tmpVipType.setShipBuildBonus(30);
        tmpVipType.setBuildingBuildBonus(30);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("AA Master Gunner","AA",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitDefence(10);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("FTL Master","FTL",neutral,uniqueVIPIdCounter);
        tmpVipType.setFTLbonus(true);
        gw.addVipType(tmpVipType);
        
        // Buildings
        // *********        
		UniqueIdCounter uBIC = new UniqueIdCounter();
      
        Buildings tempBuildings = new Buildings();
		BuildingType tmpBuildingType = null;
        
		// orbital wharfs
        tmpBuildingType = new BuildingType("Small Orbital Wharf", "W1", 5, uBIC);
        tmpBuildingType.setWharfSize(1);
        tmpBuildingType.setInOrbit(true);
        BuildingType parent = tmpBuildingType;
        tempBuildings.addBuilding(tmpBuildingType);
        
        tmpBuildingType = new BuildingType("Medium Orbital Wharf", "W2", 10, uBIC);
        tmpBuildingType.setWharfSize(2);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setParentBuildingType(parent);
        parent = tmpBuildingType;
        tempBuildings.addBuilding(tmpBuildingType);
        
        tmpBuildingType = new BuildingType("Large Orbital Wharf", "W3", 10, uBIC);
        tmpBuildingType.setWharfSize(3);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setParentBuildingType(parent);
        parent = tmpBuildingType;
        tempBuildings.addBuilding(tmpBuildingType);
        
        tmpBuildingType = new BuildingType("Huge Orbital Wharf", "W5", 10, uBIC);
        tmpBuildingType.setWharfSize(5);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setParentBuildingType(parent);
        tempBuildings.addBuilding(tmpBuildingType);
        
        tmpBuildingType = new BuildingType("Space Station", "SS", 15, uBIC);
        tmpBuildingType.setOpenPlanetBonus(2);
        tmpBuildingType.setClosedPlanetBonus(1);
        tmpBuildingType.setTechBonus(10);
        tmpBuildingType.setSpaceport(true);
        tempBuildings.addBuilding(tmpBuildingType);        

        // Factions
        // empire
        Faction tempFaction = new Faction("Empire",Faction.getColorHexString(240,35,45),evil);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan III"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Strike Cruiser"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Lancer frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Freighter"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Nebulon B frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interdictor"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Imperial Star Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Super Star Destroyer"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Nebulon B frigate"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Golan II"));
        tempFaction.setResistanceBonus(2);
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Orbital Wharf"));
        tempFaction.setBuildings(tempBuildings);
        tempFaction.setGovernorVIPType(govType);
        gw.addFaction(tempFaction);
        Faction empireFaction = tempFaction;

        // rebels
        tempFaction = new Faction("Rebel",Faction.getColorHexString(0,255,0),good);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan III"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Strike Cruiser"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Starfighter Squadron"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Freighter"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Nebulon B frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Dreadnaught"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Mon Calamari Cruiser"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Dreadnaught"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Golan II"));
        tempFaction.setClosedPlanetBonus(1);
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Orbital Wharf"));
        tempFaction.setBuildings(tempBuildings);
        tempFaction.setGovernorVIPType(govType);
        gw.addFaction(tempFaction);
        Faction rebelFaction = tempFaction;

        // league
        tempFaction = new Faction("League",Faction.getColorHexString(24,66,255),neutral);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan IIB"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan III"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Strike Cruiser"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Freighter"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Nebulon B frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Escort Carrier"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Victory Star Destroyer"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Nebulon B frigate"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Golan II"));
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Orbital Wharf"));
        tempFaction.setBuildings(tempBuildings);
//        tempFaction.setOrbitalStructure(os1);
//        tempFaction.setBuildOrbitalStructureCostBase(7);
//        tempFaction.setBuildOrbitalStructureCostMulitplier(3);
        gw.addFaction(tempFaction);
        Faction leagueFaction = tempFaction;

        // add custom diplomacy
        // ********************
        
        GameWorldDiplomacy diplomacy = gw.getDiplomacy();
        DiplomacyRelation tempDiplomacyRelation;
        
        // Empire-Empire relation
        tempDiplomacyRelation = diplomacy.getRelation(empireFaction, empireFaction);
        // highest = confederacy -> OK
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.WAR);
        // start = peace -> OK

        // Rebel-Rebel relation
        tempDiplomacyRelation = diplomacy.getRelation(rebelFaction, rebelFaction);
        // highest = confederacy -> OK
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.CONFEDERACY);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.CONFEDERACY);

        // League-League relation
        tempDiplomacyRelation = diplomacy.getRelation(leagueFaction, leagueFaction);
        // highest = confederacy -> OK
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.WAR);
        // start = peace -> OK

        // Rebel-Empire relation
        tempDiplomacyRelation = diplomacy.getRelation(rebelFaction, empireFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.CEASE_FIRE);
        // lowest = Eternal War -> OK
        // start = War -> OK

        // Rebel-League relation
        tempDiplomacyRelation = diplomacy.getRelation(rebelFaction, leagueFaction);
        // highest = confederacy -> OK
        // lowest = Eternal War -> OK
        // start = War -> OK

        // League-Empire relation
        tempDiplomacyRelation = diplomacy.getRelation(leagueFaction, empireFaction);
        // highest = confederacy -> OK
        // lowest = Eternal War -> OK
        // start = War -> OK

		return gw;
	}

}
