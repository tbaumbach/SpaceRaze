package sr.server.gameworlds;

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

public class CorporateSpace {

	public static GameWorld getGameWorld(){
		GameWorld gw = new GameWorld();
//		gw.setInitMethod(InitiativeMethod.FIFTY_FIFTY);

        gw.setFileName("corporatespace");

        gw.setFullName("Corporate Space");
        gw.setDescription("A very basic gameworld for new players to learn the basics of the game");
        
        gw.setBattleSimDefaultShips1("[2]Crv;Des");
        gw.setBattleSimDefaultShips2("[4]DPI");

//        gw.setCumulativeBombardment(false);
        
		gw.setCreatedDate("2006-01-15");
		gw.setChangedDate("2006-01-15");
		gw.setCreatedByUser("pabod");

		Alignments alignments = gw.getAlignments();
		Alignment neutral = alignments.findAlignment("neutral");

		gw.setAdjustScreenedStatus(false);
		
		// Spaceship types
        UniqueIdCounter uniqueShipIdCounter = new UniqueIdCounter();

        // Golan I
        SpaceshipType tempsst = new SpaceshipType("Defence Platform I","DPI",SpaceshipType.SIZE_SMALL,10,30,SpaceshipRange.NONE,1,3,uniqueShipIdCounter,10);
        tempsst.setDescription("Small non-mobile defence unit");
        gw.addShipType(tempsst);
        // Golan II
        tempsst = new SpaceshipType("Defence Platform II","DPII",SpaceshipType.SIZE_MEDIUM,30,80,SpaceshipRange.NONE,1,6,uniqueShipIdCounter,30);
        tempsst.setDescription("Medium non-mobile defence unit");
        tempsst.setArmorSmall(0);
        gw.addShipType(tempsst);
        // Golan III
        tempsst = new SpaceshipType("Defence Platform III","DPIII",SpaceshipType.SIZE_LARGE,50,140,SpaceshipRange.NONE,1,9,uniqueShipIdCounter,50);
        tempsst.setDescription("Large non-mobile defence unit");
        tempsst.setArmorSmall(0);
        tempsst.setArmorMedium(0);
        gw.addShipType(tempsst);
        // Corvette
        tempsst = new SpaceshipType("Corvette","Crv",SpaceshipType.SIZE_SMALL,10,20,SpaceshipRange.LONG,1,3,uniqueShipIdCounter,10);
        tempsst.setDescription("Useful as scout ship, and can also be used to boost long range raiding forces and to transport VIPs");
        gw.addShipType(tempsst);
        // Destroyer
        tempsst = new SpaceshipType("Destroyer","Des",SpaceshipType.SIZE_MEDIUM,40,80,SpaceshipRange.LONG,3,10,uniqueShipIdCounter,30);
        tempsst.setDescription("Useful for long range raiding attacks, and also boost larger task forces");
        tempsst.setArmorSmall(0);
        tempsst.setPsychWarfare(1);
        gw.addShipType(tempsst);
        // Cruiser
        tempsst = new SpaceshipType("Cruiser","Cru",SpaceshipType.SIZE_LARGE,80,300,SpaceshipRange.SHORT,5,16,uniqueShipIdCounter,60);
        tempsst.setDescription("Main battle unit");
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setArmorSmall(0);
        tempsst.setArmorMedium(0);
        gw.addShipType(tempsst);
        // Battleship
        tempsst = new SpaceshipType("Battleship","Bat",SpaceshipType.SIZE_HUGE,120,600,SpaceshipRange.SHORT,10,30,uniqueShipIdCounter,120);
        tempsst.setDescription("Useful as flagship in major task forces");
        tempsst.setBombardment(2);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(10);
        tempsst.setArmorSmall(0);
        tempsst.setArmorMedium(0);
        tempsst.setArmorLarge(0);
        gw.addShipType(tempsst);

        // add shiptypes for neutral planets
        gw.setNeutralSize1(gw.getSpaceshipTypeByName("Defence Platform I"));
        gw.setNeutralSize2(gw.getSpaceshipTypeByName("Defence Platform II"));
        gw.setNeutralSize3(gw.getSpaceshipTypeByName("Defence Platform III"));

        // vip types
        UniqueIdCounter uniqueVIPIdCounter = new UniqueIdCounter();

        VIPType tmpVipType = new VIPType("Governor","Gov",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        gw.addVipType(tmpVipType);
        
        VIPType govType = tmpVipType;

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
        tmpVipType.setInitBonus(10);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Assassin","Ass",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setAssassination(50);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Expert Engineer","Eng",neutral,uniqueVIPIdCounter);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setTechBonus(10);
/*
        tmpVipType.setSmallBuildBonus(1);
        tmpVipType.setMediumBuildBonus(2);
        tmpVipType.setLargeBuildBonus(3);
        tmpVipType.setHugeBuildBonus(10);
        tmpVipType.setWharfBuildBonus(2);
        tmpVipType.setWharfUpgradeBonus(2);
*/        
        tmpVipType.setShipBuildBonus(30);
        tmpVipType.setBuildingBuildBonus(30);
        tmpVipType.setTroopBuildBonus(30);

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
        
        tmpBuildingType = new BuildingType("Medium Orbital Wharf", "W2", 5, uBIC);
        tmpBuildingType.setWharfSize(2);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setParentBuildingType(parent);
        parent = tmpBuildingType;
        tempBuildings.addBuilding(tmpBuildingType);
        
        tmpBuildingType = new BuildingType("Large Orbital Wharf", "W3", 5, uBIC);
        tmpBuildingType.setWharfSize(3);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setParentBuildingType(parent);
        parent = tmpBuildingType;
        tempBuildings.addBuilding(tmpBuildingType);
        
        tmpBuildingType = new BuildingType("Huge Orbital Wharf", "W5", 5, uBIC);
        tmpBuildingType.setWharfSize(5);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setParentBuildingType(parent);
        tempBuildings.addBuilding(tmpBuildingType);

        // Factions
        // Markaad
        Faction tempFaction = new Faction("Markaad",Faction.getColorHexString(255,150,0),neutral);
        tempFaction.setDescription("Maarkaad is a wealthy and ruthless interstellar cooperation rapidly expanding it's assets in the outer regions of inhabited space, often in direct conflict with other cooperations.");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Orbital Wharf"));
        tempFaction.setBuildings(tempBuildings);
        gw.addFaction(tempFaction);

        // Drakken
        tempFaction = new Faction("Drakken",Faction.getColorHexString(0,255,255),neutral);
        tempFaction.setDescription("Drakken is a wealthy and ruthless interstellar cooperation rapidly expanding it's assets in the outer regions of inhabited space, often in direct conflict with other cooperations.");
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform I"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Defence Platform III"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Destroyer"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Defence Platform II"));
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Orbital Wharf"));
        tempFaction.setBuildings(tempBuildings);
        gw.addFaction(tempFaction);

		return gw;
	}

}
