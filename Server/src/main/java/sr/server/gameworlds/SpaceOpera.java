package sr.server.gameworlds;

import sr.enums.BlackMarketFrequency;
import sr.enums.DiplomacyLevel;
import sr.enums.SpaceshipRange;
import sr.enums.SpaceshipTargetingType;
import sr.world.Alignment;
import sr.world.Alignments;
import sr.world.BuildingType;
import sr.world.Buildings;
import sr.world.Corruption;
import sr.world.Faction;
import sr.world.GameWorld;
import sr.world.SpaceshipType;
import sr.world.UniqueIdCounter;
import sr.world.VIPType;
import sr.world.diplomacy.DiplomacyRelation;
import sr.world.diplomacy.GameWorldDiplomacy;

public class SpaceOpera{

	public static GameWorld getGameWorld(){
		GameWorld gw = new GameWorld();
//		gw.setCumulativeBombardment(false);
//		gw.setSquadronsSurviveOutsideCarriers(false);
//		gw.setInitMethod(InitiativeMethod.WEIGHTED_2);
		
		gw.setFileName("spaceopera");
		
		gw.setFullName("Space Opera");
		gw.setDescription("Based on the role-playing game Space Opera. Contains a number of different factions/races, each with their own more or less unique abilities and shiptypes.");
		
		gw.setVersionId("1");
		
		gw.setRazedPlanetChance(10);
//		gw.setRazedPlanetChance(100); // testar aliens
		gw.setClosedNeutralPlanetChance(35);
		gw.setAdjustScreenedStatus(false);
		
		gw.setBattleSimDefaultShips1("[3]ippb");
		gw.setBattleSimDefaultShips2("[1]crv;[1]pat");
//		gw.setBattleSimDefaultShips1("[0]pb;[0]crv;[0]udes;[0]ulcr;[0]ucr;[0]ubcr;[0]vbss;[0]cbs;[0]u-f;[0]u-b");
//		gw.setBattleSimDefaultShips2("[0]pb;[0]crv;[0]udes;[0]ulcr;[0]ucr;[0]ubcr;[0]vbss;[0]cbs;[0]u-f;[0]u-b");
		
		gw.setCreatedDate("2006-02-01");
		gw.setChangedDate("2006-04-11");
		gw.setCreatedByUser("pabod");		

		Alignments alignments = gw.getAlignments();
		String aStr= "Arachnid";
		String kStr = "Klackon";
		alignments.add(aStr);
		alignments.add(kStr);
		Alignment neutral = alignments.findAlignment("neutral");
		Alignment good = alignments.findAlignment("good");
		Alignment evil = alignments.findAlignment("evil");
		Alignment arachnid = alignments.findAlignment(aStr);
		Alignment klackon = alignments.findAlignment(kStr);
		neutral.setDescription("Balanced view of the universe");
		good.setDescription("Respectful of life and prefers law and order");
		evil.setDescription("Unscroupulous and seek power at any cost");
		arachnid.setDescription("Alien hive-mind mentality");
		klackon.setDescription("Cold and harch alien intellect");
		klackon.addCanHaveVip(evil.getName());
		klackon.addCanHaveVip(arachnid.getName());
		arachnid.addCanHaveVip(evil.getName());
		arachnid.addCanHaveVip(klackon.getName());		
		evil.addCanHaveVip(klackon.getName());
		evil.addCanHaveVip(arachnid.getName());
		neutral.addCanHaveVip(klackon.getName());

		Corruption tmpCorruption = new Corruption();
		tmpCorruption.addBreakpoint(50, 20);
		tmpCorruption.addBreakpoint(100, 40);
		tmpCorruption.addBreakpoint(150, 50);
		tmpCorruption.addBreakpoint(200, 60);
		tmpCorruption.addBreakpoint(250, 70);
		
		// General VIP types
        // *****************
        
        UniqueIdCounter uniqueVIPIdCounter = new UniqueIdCounter();
        VIPType tmpVipType = null;
        VIPType govType = tmpVipType;

        tmpVipType = new VIPType("BRINT Agent","BrtA",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setSpying(true);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("BRINT Captain","BrtC",neutral,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setSpying(true);
        tmpVipType.setCounterEspionage(50);
        tmpVipType.setExterminator(40);
        tmpVipType.setAssassination(40);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("BRINT General","BrtG",neutral,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setSpying(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setCounterEspionage(70);
        tmpVipType.setExterminator(60);
        tmpVipType.setAssassination(60);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("BOSS Agent","BosA",neutral,uniqueVIPIdCounter);
        tmpVipType.setCounterEspionage(50);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setExterminator(60);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("BOSS Captain","BosC",neutral,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
        tmpVipType.setCounterEspionage(70);
        tmpVipType.setResistanceBonus(4);
        tmpVipType.setExterminator(80);
        tmpVipType.setClosedIncBonus(1);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("BOSS General","BosG",neutral,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        tmpVipType.setCounterEspionage(90);
        tmpVipType.setResistanceBonus(5);
        tmpVipType.setExterminator(90);
        tmpVipType.setClosedIncBonus(2);
        tmpVipType.setOpenIncBonus(1);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Economic Genius","Eco",neutral,uniqueVIPIdCounter);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setOpenIncBonus(3);
        tmpVipType.setClosedIncBonus(1);
        gw.addVipType(tmpVipType);

       	tmpVipType = new VIPType("Smuggler","Smu",neutral,uniqueVIPIdCounter);
        tmpVipType.setClosedIncBonus(3);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("General","Gen",neutral,uniqueVIPIdCounter);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setResistanceBonus(3);
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Field Marshal","Fie",neutral,uniqueVIPIdCounter);
        tmpVipType.setPsychWarfareBonus(2);
        tmpVipType.setResistanceBonus(5);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Admiral","Adm",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitBonus(10);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Sky Marshal","Sky",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitBonus(15);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Ace","Ace",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitFighterSquadronBonus(10);
        tmpVipType.setAttackScreenedSquadron(true);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Top Ace","TAce",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitFighterSquadronBonus(20);
        tmpVipType.setAttackScreenedSquadron(true);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Assassin","Ass",evil,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setAssassination(50);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Master Assassin","MAss",evil,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setAssassination(80);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Master Engineer","Tech",neutral,uniqueVIPIdCounter);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setTechBonus(10);
        tmpVipType.setShipBuildBonus(40);
        tmpVipType.setBuildingBuildBonus(40);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Spaceship Engineer","Eng",neutral,uniqueVIPIdCounter);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setShipBuildBonus(20);
        tmpVipType.setBuildingBuildBonus(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Scientist","Sci",neutral,uniqueVIPIdCounter);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setTechBonus(10);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Expert Scientist","ESci",neutral,uniqueVIPIdCounter);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setTechBonus(20);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Astrogator","Ast",neutral,uniqueVIPIdCounter);
        tmpVipType.setFTLbonus(true);
        tmpVipType.setPlanetarySurvey(true);
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Diplomat","Dip",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Master Diplomat","Dip",neutral,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setSpying(true);
        gw.addVipType(tmpVipType);
      
        tmpVipType = new VIPType("Bounty Hunter","BH",evil,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setAssassination(50);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
        tmpVipType.setSpying(true);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
        tmpVipType.setExterminator(50);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("White Psi Apprentice","WPA",good,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
        tmpVipType.setHardToKill(true);
        tmpVipType.setSpying(true);
        tmpVipType.setCounterEspionage(30);
        tmpVipType.setInitFighterSquadronBonus(5);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
        tmpVipType.setWellGuarded(true);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("White Psi Master","WPM",good,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setInitFighterSquadronBonus(10);
        tmpVipType.setHardToKill(true);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setSpying(true);
        tmpVipType.setPlanetarySurvey(true);
        tmpVipType.setCounterEspionage(70);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setExterminator(80);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Gray Psi Apprentice","GPA",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setCounterEspionage(30);
        tmpVipType.setInitBonus(5);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Gray Psi Master","GPM",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setCounterEspionage(50);
        tmpVipType.setExterminator(60);
        tmpVipType.setInitBonus(10);
        tmpVipType.setFTLbonus(true);
        tmpVipType.setStealth(true);
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Black Psi Apprentice","BPA",evil,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
        tmpVipType.setInitFighterSquadronBonus(5);
        tmpVipType.setHardToKill(true);
        tmpVipType.setResistanceBonus(1);
        tmpVipType.setAssassination(30);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Black Psi Master","BPM",evil,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setInitFighterSquadronBonus(10);
        tmpVipType.setHardToKill(true);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setAssassination(50);
        tmpVipType.setExterminator(40);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
        gw.addVipType(tmpVipType);

        // rainbow psi master
        tmpVipType = new VIPType("Psi Grand Master","PGM",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_MASTER);
        tmpVipType.setResistanceBonus(4);
        tmpVipType.setCounterEspionage(80);
        tmpVipType.setExterminator(80);
        tmpVipType.setInitBonus(15);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setFTLbonus(true);
        tmpVipType.setInitFighterSquadronBonus(15);
        tmpVipType.setSpying(true);
        tmpVipType.setPlanetarySurvey(true);
        tmpVipType.setStealth(true);
        gw.addVipType(tmpVipType);

        // Klackon vips
        tmpVipType = new VIPType("Klackon Breeder","KBre",klackon,uniqueVIPIdCounter);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setShipBuildBonus(30);
        tmpVipType.setBuildingBuildBonus(30);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Klackon Admiral","KAdm",klackon,uniqueVIPIdCounter);
        tmpVipType.setInitBonus(10);
        tmpVipType.setBombardmentBonus(1);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Klackon Assassin","KAss",klackon,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setAssassination(60);
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Klackon Infestator","KInf",klackon,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setInfestate(true);
        gw.addVipType(tmpVipType);
                
        // Bug vips
        tmpVipType = new VIPType("Brain Bug","BGen",arachnid,uniqueVIPIdCounter);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setCounterEspionage(50);
        tmpVipType.setClosedIncBonus(2);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Psi Bug","BPsi",arachnid,uniqueVIPIdCounter);
        tmpVipType.setInitBonus(10);
        tmpVipType.setFTLbonus(true);
        tmpVipType.setPlanetarySurvey(true);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Bug Ace","BAce",arachnid,uniqueVIPIdCounter);
        tmpVipType.setInitFighterSquadronBonus(10);
        tmpVipType.setSpying(true);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Bug Princess","BPri",arachnid,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setInfestate(true);
        tmpVipType.setSpying(true);
        gw.addVipType(tmpVipType);

		// *******************
		// * Spaceship types *
		// *******************
		UniqueIdCounter uSIC= new UniqueIdCounter();
		
		SpaceshipType tempsst = null;
		int sqdBaseSh = 10;
		int sqdBaseDC = 20;
		int sqdBugBaseDC = 15;
		int sqdBaseSmAtt = 10;
		int sqdBaseFightSqdAtt = 20;
		int sqdBaseBombSqdAtt = 10;
		
        // Interplanetary defence ships (also used by neutral planets)
		// -----------------------------------------------------------

		// Interplanetary Patrol Boat
		tempsst = new SpaceshipType("IP Patrol Boat","IPPB",SpaceshipType.SIZE_SMALL,10,30,SpaceshipRange.NONE,1,3,uSIC,15,10);
        tempsst.setDescription("Interplanetary (i.e. can not move outside the starsystem it is built) version of a Patrol Boat. It is about as powerful as a normal Patrol Boat, but is cheaper.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        gw.addShipType(tempsst);

		// Interplanetary Corvette
        tempsst = new SpaceshipType("IP Corvette","IPCo",SpaceshipType.SIZE_MEDIUM,30,100,SpaceshipRange.NONE,1,6,uSIC,30,10);
        tempsst.setDescription("Interplanetary (i.e. can not move outside the starsystem it is built) version of a Corvette. It is about as powerful as a normal Corvette, but is much cheaper.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        gw.addShipType(tempsst);

		// Interplanetary Cruiser
        tempsst = new SpaceshipType("IP Cruiser","IPCr",SpaceshipType.SIZE_LARGE,80,300,SpaceshipRange.NONE,2,9,uSIC,50,10);
        tempsst.setDescription("Interplanetary (i.e. can not move outside the starsystem it was built) version of a Cruiser. It is about as powerful as a normal Cruiser, but is much cheaper.");
		tempsst.setArmor(60,30);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(120);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(6);
        gw.addShipType(tempsst);

        gw.setNeutralSize1(gw.getSpaceshipTypeByName("IP Patrol Boat"));
        gw.setNeutralSize2(gw.getSpaceshipTypeByName("IP Corvette"));
        gw.setNeutralSize3(gw.getSpaceshipTypeByName("IP Cruiser"));
        
        // Civilian ships
        // **************
        
        // Blockade Runner
        tempsst = new SpaceshipType("Blockade Runner","Run",SpaceshipType.SIZE_SMALL,SpaceshipRange.LONG,1,5,uSIC);
        tempsst.setDescription("Small-sized long range civilian merchant ship. Specialized in dealing with closed systems, but can also give some income on open planets.");
        tempsst.setIncOwnClosedBonus(2);
        tempsst.setIncFrendlyClosedBonus(3);
        tempsst.setIncNeutralClosedBonus(4);
        tempsst.setIncEnemyClosedBonus(5);
        tempsst.setIncOwnOpenBonus(1);
        tempsst.setIncFrendlyOpenBonus(2);
        tempsst.setIncNeutralOpenBonus(3);
        tempsst.setIncEnemyOpenBonus(4); 
        tempsst.setVisibleOnMap(false);
        gw.addShipType(tempsst);

        // Small Merchant Freighter
        tempsst = new SpaceshipType("Small Merchant Freighter","SMF",SpaceshipType.SIZE_SMALL,SpaceshipRange.LONG,1,4,uSIC);
        tempsst.setDescription("Small-sized long range civilian merchant ship. Give income bonuses on frendly and neutral open planets.");
        tempsst.setIncOwnOpenBonus(2);
        tempsst.setIncFrendlyOpenBonus(3);
        tempsst.setIncNeutralOpenBonus(4);
        gw.addShipType(tempsst);
        
        // Troop Passenger Liner
        /*
        tempsst = new SpaceshipType("Troop Passenger Liner","TrPL",SpaceshipType.SIZE_SMALL,SpaceshipRange.LONG,1,5,uSIC);
        tempsst.setDescription("Small-sized long range civilian troop ship. Modified version of a civilian passenger liner, adapted to carry troops and landing crafts. Note that this ship cannot besiege planets on it's own, but when in company with at least one military ship which can besiege planets, it can use it's troops against planets.");
        tempsst.setPsychWarfare(1);
        gw.addShipType(tempsst);
        */

        // Survey ship
        tempsst = new SpaceshipType("Outreach Survey Ship","Out",SpaceshipType.SIZE_SMALL,SpaceshipRange.LONG,1,8,uSIC);
        tempsst.setDescription("Small-sized long range civilian survey ship. Outreach is a scientific vessel, primarily used to scout new solar systems to search for planets suitable for colonization, and to effectively analyse any inhabited worlds it encounters.");
        tempsst.setPlanetarySurvey(true);
        gw.addShipType(tempsst);

        // Medium Merchant Freighter
        tempsst = new SpaceshipType("Medium Merchant Freighter","MMF",SpaceshipType.SIZE_MEDIUM,SpaceshipRange.LONG,1,8,uSIC);
        tempsst.setDescription("Medium-sized long range civilian merchant ship. Give income bonuses on frendly and neutral open planets.");
        tempsst.setIncOwnOpenBonus(3);
        tempsst.setIncFrendlyOpenBonus(4);
        tempsst.setIncNeutralOpenBonus(5);
        gw.addShipType(tempsst);

        // Supply Freighter
        tempsst = new SpaceshipType("Supply Ship","Sup",SpaceshipType.SIZE_MEDIUM,SpaceshipRange.LONG,1,8,uSIC);
        tempsst.setDescription("Medium-sized long range civilian supply ship. Can only resupply small and large ships.");
        tempsst.setSupply(2);
        gw.addShipType(tempsst);

        // Large Merchant Freighter
        tempsst = new SpaceshipType("Large Merchant Freighter","LMF",SpaceshipType.SIZE_LARGE,SpaceshipRange.SHORT,1,12,uSIC);
        tempsst.setDescription("Large-sized short range civilian merchant ship. Give income bonuses on frendly and neutral open planets.");
        tempsst.setIncOwnOpenBonus(4);
        tempsst.setIncFrendlyOpenBonus(5);
        tempsst.setIncNeutralOpenBonus(6);
        gw.addShipType(tempsst);

        // Huge Supply Freighter
        tempsst = new SpaceshipType("Mobile Resupply Base","MRB",SpaceshipType.SIZE_HUGE,SpaceshipRange.SHORT,3,15,uSIC);
        tempsst.setDescription("Huge-sized short range civilian resupply ship. Can resupply all types of ships, even battlestars.");
        tempsst.setSupply(4);
        gw.addShipType(tempsst);

        // VIP transport
        tempsst = new SpaceshipType("VIP Transport","VT",SpaceshipType.SIZE_SMALL,SpaceshipRange.LONG,1,3,uSIC);
        tempsst.setDescription("Small long range civilian VIP transport ship. Always retreat if it encounters enemy military ships without protection, unless enemy ships have stop retreats ability.");
        tempsst.setAlwaysRetreat(true);
//        tempsst.setPlayerUnique(true); // ev. lägga tillbaka detta då buggen i minishipplanet är fixad
        gw.addShipType(tempsst);

        // Military capital ships
        // **********************
        
        // Scout
        tempsst = new SpaceshipType("Nike FTL Scout","Sct",SpaceshipType.SIZE_SMALL,5,10,SpaceshipRange.LONG,1,2,uSIC,5,5);
        tempsst.setDescription("Small-sized long range military scout ship. Very small military ship useful for scouting missions and to carry VIPs. Also useful for commerce raiding. It is virtually useless in combat. Cannot besiege/block planets.");
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        
		// Patrol Boat
		tempsst = new SpaceshipType("Patrol Boat","Pat",SpaceshipType.SIZE_SMALL,10,40,SpaceshipRange.LONG,2,5,uSIC,10,10);
        //tempsst.setDescription("Small-sized long range military ship. Useful for blocking enemny planets, scouting missions, commerce raiding and to carry VIPs. This is the smallest ship that can besiege planets together with a Troop Passenger Liner (TrPL). It can be used in combat against small enemies and maybe support against medium enemy ships, but against larger ships it is virtually useless.");
        tempsst.setDescription("Small-sized long range military ship. Useful for blocking enemny planets, scouting missions, commerce raiding and to carry VIPs. It can be used in combat against small enemies and maybe support against medium enemy ships, but against larger ships it is virtually useless.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        gw.addShipType(tempsst);

        // Patrol Corvette
		tempsst = new SpaceshipType("Patrol Corvette","PCrv",SpaceshipType.SIZE_SMALL,20,60,SpaceshipRange.LONG,3,8,uSIC,25,15);
		tempsst.setDescription("Small-sized long range military ship. It is most useful when attacking small opponents or as support against medium ships. Against large and huge opponents it is virtually useless.");
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        gw.addShipType(tempsst);

        // Troop Armed Transport
		tempsst = new SpaceshipType("Troop Armed Transport","TrAT",SpaceshipType.SIZE_MEDIUM,10,30,SpaceshipRange.LONG,2,10,uSIC,5,5);
        tempsst.setDescription("Medium-sized long range military troop ship. This military troop transport can single-handedly besiege enemy planets.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setPsychWarfare(1);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);

        // Corvette
		tempsst = new SpaceshipType("Corvette","Crv",SpaceshipType.SIZE_MEDIUM,30,100,SpaceshipRange.LONG,4,10,uSIC,40,15);
		tempsst.setDescription("Medium-sized long range military ship. It is most useful when attacking small opponents or as support against more powerful medium or less powerful large ships. Against huge opponents it is virtually useless.");
		tempsst.setArmor(30);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        gw.addShipType(tempsst);

        // Factions
        // ********
        
		UniqueIdCounter uBIC = new UniqueIdCounter();
		Buildings tempBuildings = new Buildings();
		BuildingType tmpBuildingType = null;

        // *******
        // * UTF *
        // *******

        Faction tempFaction = new Faction("UTF",Faction.getColorHexString(0,255,0),good);
        String typeName = null;
        
        // UTF Fighter
        typeName = "UTF Fighter";
        tempsst = new SpaceshipType(typeName,"U-F",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC,SpaceshipRange.NONE,1,4,uSIC,sqdBaseSmAtt,sqdBaseFightSqdAtt);
		tempsst.setDescription("A fighter squadron specialized in attacking enemy squadrons. It can also do some minor damage to enemy capital ships and has one torpedo salvoe against medium or larger opponents. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(1);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // UTF Bomber
        typeName = "UTF Bomber";
        tempsst = new SpaceshipType(typeName,"U-B",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC + 10,SpaceshipRange.NONE,1,6,uSIC,sqdBaseSmAtt,sqdBaseBombSqdAtt);
		tempsst.setDescription("Squadron specialized in attacking capital ships, especially ships larger than small where its medium and large torpedo salvoes can can do significant damage. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(50);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // UTF Destroyer
        typeName = "UTF Destroyer";
		tempsst = new SpaceshipType(typeName,"UDes",SpaceshipType.SIZE_MEDIUM,50,160,SpaceshipRange.LONG,5,14,uSIC,35,10);
		tempsst.setDescription("Medium-sized long range military ship. It is most useful when attacking small or medium opponents or as support against less powerful large ships. It is too weak to do any significant damage against huge opponents.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(40);        
        tempsst.setWeaponsMaxSalvoesMedium(4);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // UTF Light Carrier
        typeName = "UTF Light Carrier";
		tempsst = new SpaceshipType(typeName,"ULCa",SpaceshipType.SIZE_MEDIUM,60,220,SpaceshipRange.LONG,6,18,uSIC,10,15);
		tempsst.setDescription("Medium-sized long range squadron carrier. Can carry up to 4 squadrons.");
		tempsst.setArmor(20);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(4);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // UTF Light Cruiser
        typeName = "UTF Light Cruiser";
		tempsst = new SpaceshipType(typeName,"ULCr",SpaceshipType.SIZE_LARGE,80,320,SpaceshipRange.SHORT,8,26,uSIC,50,15);
		tempsst.setDescription("Light large-sized short-range capital ship. It is very good against small or medium opponents and gives good support against large ones. It is too weak to threaten huge opponents.");
		tempsst.setArmor(50,35);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(120);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // UTF Cruiser
        typeName = "UTF Cruiser";
		tempsst = new SpaceshipType(typeName,"UCr",SpaceshipType.SIZE_LARGE,140,420,SpaceshipRange.SHORT,10,32,uSIC,40,10);
		tempsst.setDescription("Large-sized short-range capital ship. It is very good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It has troops, bombardment and can carry one squadron.");
		tempsst.setArmor(60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(170);
        tempsst.setSquadronCapacity(1);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // UTF Carrier
        typeName = "UTF Carrier";
		tempsst = new SpaceshipType(typeName,"UCa",SpaceshipType.SIZE_LARGE,160,470,SpaceshipRange.SHORT,8,28,uSIC,20,20);
		tempsst.setDescription("Large-sized short range squadron carrier. Can carry up to 8 squadrons.");
		tempsst.setArmor(40,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(8);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // UTF Battle Cruiser
        typeName = "UTF Battlecruiser";
		tempsst = new SpaceshipType(typeName,"UBCr",SpaceshipType.SIZE_LARGE,150,550,SpaceshipRange.SHORT,12,40,uSIC,40,10);
		tempsst.setDescription("Heavy large-sized short range capital ship. Has a powerful array of weapons that can even make significant damage to huge ships. It has troops, bombardment and can carry two squadrons.");
		tempsst.setArmor(70,45);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(170);
        tempsst.setWeaponsStrengthLarge(100);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setSquadronCapacity(2);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // UTF Battle Starship
        typeName = "UTF Battle Starship";
		tempsst = new SpaceshipType(typeName,"UBSs",SpaceshipType.SIZE_HUGE,250,850,SpaceshipRange.SHORT,23,70,uSIC,50,15);
		tempsst.setDescription("Huge-sized short range capital ship. Has a powerful array of weapons that make significant damage to ships of all sizes, and have powerful armor. It has troops, bombardment and can carry three squadrons. It also have a tactical center to increase combat effectiveness.");
		tempsst.setArmor(80,60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(150);
        tempsst.setWeaponsStrengthLarge(280);
        tempsst.setWeaponsStrengthHuge(140);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setSquadronCapacity(3);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(5);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // UTF Battle Star
        typeName = "UTF Battle Star";
		tempsst = new SpaceshipType(typeName,"UBS",SpaceshipType.SIZE_HUGE,400,1300,SpaceshipRange.SHORT,30,100,uSIC,60,20);
		tempsst.setDescription("Huge-sized short range capital ship. The UTF Concordat class battle star are the most powerful unit in known space. Have a powerful array of weapons that make massive damage to ships of all sizes, and have very powerful armor. It carries elite troops, have powerful bombardment, carry up to four squadrons and have an advanced tactical center to increase combat efficiency.");
		tempsst.setArmor(90,70,50);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(180);
        tempsst.setWeaponsStrengthLarge(150);
        tempsst.setWeaponsStrengthHuge(300);
        tempsst.setWeaponsMaxSalvoesHuge(6);
        tempsst.setSquadronCapacity(4);
        tempsst.setBombardment(2);
        tempsst.setPsychWarfare(2);
        tempsst.setIncreaseInitiative(10);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // ---------------------------------------------------------
        
        // Governor
        tmpVipType = new VIPType("UTF Governor","Gov",good,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        govType = tmpVipType;

        // Buildings
        // *********
        tempBuildings = new Buildings();
        
//        tmpBuildingType = new BuildingType("UTF Space Station", "USS", 10, uBIC); // cost 5 + 1
//        tmpBuildingType.setOpenPlanetBonus(3);
//        tmpBuildingType.setClosedPlanetBonus(1);
//        tmpBuildingType.setTechBonus(10);
//        tmpBuildingType.setSpaceport(true);
//        tmpBuildingType.setPlanetUnique(true);
//        tmpBuildingType.setAutoDestructWhenConquered(true);
//        tempBuildings.addBuilding(tmpBuildingType);        
        
        setWharfs(tempBuildings,uBIC,"UTF","U",5,10,15,25);
        setSpaceStations(tempBuildings,uBIC,"UTF","U",0,0,3);

        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("UTF Small Orbital Wharf"));
        
        tempFaction.setBuildings(tempBuildings);

        tempFaction.setDescription("The United Terran Federation (UTF) is above average tech level, and it's space fleet is very powerful, with many varied ship types. UTF are one of the three human factions.");
        tempFaction.setShortDescription("Human well-balanced faction, with the largest huge ships.");
        tempFaction.setAdvantages("Open planet income, largest capital ship");
        tempFaction.setDisadvantages("None");
        tempFaction.setHistory("UTF if the oldest and most powerful of the human factions, centered around old Earth, and contains many of the oldest and most powerful human worlds. UTF have reasonably good relations with the trade-oriented IRSOL faction, but the creation of the Empire of Azuriach have led to numerous wars between UTF and Azuriach. Relations with the two non-insectoid non-human races who cooperate with Azuriach is frosty at best.");
        tempFaction.setHowToPlay("Try to use a good mix of capital ships and squadrons, and use your powerful battle stars to full effect.");
        addGeneralShipTypesAndStartingTypes(tempFaction,gw,true);
        tempFaction.setGovernorVIPType(govType);
        tempFaction.setTechBonus(5);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setCanReconstruct(true);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        Faction utfFaction = tempFaction;

        // ************
        // * Azuriach *
        // ************

        tempFaction = new Faction("Azuriach",Faction.getColorHexString(255,30,30),evil);

        // Azuriach Fighter
        typeName = "Azuriach Fighter";
        tempsst = new SpaceshipType(typeName,"A-F",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC,SpaceshipRange.NONE,1,3,uSIC,sqdBaseSmAtt,sqdBaseFightSqdAtt+5);
		tempsst.setDescription("A fighter squadron specialized in attacking enemy squadrons. It can also do some minor damage to enemy capital ships. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Azuriach Bomber
        typeName = "Azuriach Bomber";
        tempsst = new SpaceshipType(typeName,"A-B",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC + 5,SpaceshipRange.NONE,1,5,uSIC,sqdBaseSmAtt,sqdBaseBombSqdAtt);
		tempsst.setDescription("Squadron specialized in attacking capital ships, especially ships larger than small where its medium and large torpedo salvoes can can do significant damage. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(40);
        tempsst.setWeaponsMaxSalvoesMedium(3);
        tempsst.setWeaponsStrengthLarge(40);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Azuriach Destroyer
        typeName = "Azuriach Destroyer";
		tempsst = new SpaceshipType(typeName,"ADes",SpaceshipType.SIZE_MEDIUM,50,150,SpaceshipRange.LONG,5,15,uSIC,30,10);
		tempsst.setDescription("Medium-sized long range military ship. It is most useful when attacking small or medium opponents or as support against less powerful large ships. It is too weak to do any significant damage against huge opponents.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(40);        
        tempsst.setWeaponsMaxSalvoesMedium(4);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Azuriach Light Carrier
        typeName = "Azuriach Light Carrier";
		tempsst = new SpaceshipType(typeName,"ALCa",SpaceshipType.SIZE_MEDIUM,60,200,SpaceshipRange.LONG,6,18,uSIC,10,15);
		tempsst.setDescription("Medium-sized long range squadron carrier. Can carry up to 4 squadrons.");
		tempsst.setArmor(20);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(4);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Azuriach Light Cruiser
        typeName = "Azuriach Light Cruiser";
		tempsst = new SpaceshipType(typeName,"ALCr",SpaceshipType.SIZE_LARGE,80,300,SpaceshipRange.SHORT,8,28,uSIC,50,15);
		tempsst.setDescription("Light large-sized short range capital ship. It is very good against small or medium opponents and gives good support against large ones. It is too weak to threaten huge opponents. Have bombardment capacity.");
		tempsst.setArmor(50,35);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(100);
        tempsst.setBombardment(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Azuriach Cruiser
        typeName = "Azuriach Cruiser";
		tempsst = new SpaceshipType(typeName,"ACr",SpaceshipType.SIZE_LARGE,130,400,SpaceshipRange.SHORT,10,30,uSIC,40,10);
		tempsst.setDescription("Large-sized short-range capital ship. It is very good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It has troops, bombardment and can carry one squadron.");
		tempsst.setArmor(60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(150);
        tempsst.setSquadronCapacity(1);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Azuriach Carrier
        typeName = "Azuriach Carrier";
		tempsst = new SpaceshipType(typeName,"ACa",SpaceshipType.SIZE_LARGE,140,450,SpaceshipRange.SHORT,8,26,uSIC,15,20);
		tempsst.setDescription("Large-sized short range squadron carrier. Can carry up to 8 squadrons.");
		tempsst.setArmor(40,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(8);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Azuriach Battle Cruiser
        typeName = "Azuriach Battlecruiser";
		tempsst = new SpaceshipType(typeName,"ABCr",SpaceshipType.SIZE_LARGE,140,500,SpaceshipRange.SHORT,12,36,uSIC,40,10);
		tempsst.setDescription("Heavy large-sized short range capital ship. Has a powerful array of weapons that can even make significant damage to huge ships. It has troops, powerful bombardment and can carry one squadrons.");
		tempsst.setArmor(70,45);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(150);
        tempsst.setWeaponsStrengthLarge(80);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setSquadronCapacity(1);
        tempsst.setBombardment(2);
        tempsst.setPsychWarfare(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Azuriach Battle Starship
        typeName = "Azuriach Battle Starship";
		tempsst = new SpaceshipType(typeName,"ABSs",SpaceshipType.SIZE_HUGE,220,800,SpaceshipRange.SHORT,22,65,uSIC,50,15);
		tempsst.setDescription("Huge-sized short range capital ship. Has a powerful array of weapons that make significant damage to ships of all sizes, and have powerful armor. It has troops, powerful bombardment and can carry two squadrons. It also have a tactical center to increase combat effectiveness.");
		tempsst.setArmor(80,60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(140);
        tempsst.setWeaponsStrengthLarge(260);
        tempsst.setWeaponsStrengthHuge(140);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setSquadronCapacity(2);
        tempsst.setBombardment(3);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(4);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Azuriach Battle Star
        typeName = "Azuriach Battle Star";
		tempsst = new SpaceshipType(typeName,"ABS",SpaceshipType.SIZE_HUGE,350,1150,SpaceshipRange.SHORT,28,90,uSIC,60,20);
		tempsst.setDescription("Huge-sized short range capital ship. The Azuriach Warlord class battle star are probably the second most powerful unit in known space, after the Concordat Battle Star of the UTF. Have a powerful array of weapons that make massive damage to ships of all sizes, and have very powerful armor. It has the second most powerful planetary bombardment in existance, elite troops, can carry up to three squadrons and have an tactical center to increase combat efficiency.");
		tempsst.setArmor(90,70,50);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(160);
        tempsst.setWeaponsStrengthLarge(140);
        tempsst.setWeaponsStrengthHuge(250);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setSquadronCapacity(3);
        tempsst.setBombardment(5);
        tempsst.setPsychWarfare(2);
        tempsst.setIncreaseInitiative(8);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        tmpVipType = new VIPType("Azuriach Governor","Gov",evil,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setClosedIncBonus(1);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        govType = tmpVipType;

        // Buildings
        // *********
        tempBuildings = new Buildings();
        
//        tmpBuildingType = new BuildingType("Azuriach Space Station", "ASS", 10, uBIC); // cost 5 + 1
//        tmpBuildingType.setOpenPlanetBonus(2);
//        tmpBuildingType.setClosedPlanetBonus(2);
//        tmpBuildingType.setTechBonus(10);
//        tmpBuildingType.setSpaceport(true);
//        tmpBuildingType.setPlanetUnique(true);
//        tmpBuildingType.setAutoDestructWhenConquered(true);
//        tempBuildings.addBuilding(tmpBuildingType);        
        
        setWharfs(tempBuildings,uBIC,"Azuriach","A",5,10,15,25);
        setSpaceStations(tempBuildings,uBIC,"Azuriach","A",-1,1,3);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Azuriach Small Orbital Wharf"));
        
        tempFaction.setBuildings(tempBuildings);
      
        tempFaction.setDescription("The Azuriach Empire are ruthless in it's search for power and it's powerful navy have the most powerful planetary bombardment weapons known. Azuriach are one of the three human factions.");
        tempFaction.setShortDescription("Human well-balanced faction, with powerful bombardment ships.");
        tempFaction.setAdvantages("Closed planet income, powerful bombardment");
        tempFaction.setDisadvantages("None");
        tempFaction.setHistory("Azuriach was formed in a bloody military rebellion on one of the most powerful frontier planets of the UTF. It quickly grew to become the second largest human faction until UTF managed to suspend its growth with vastly increased military efforts. Azuriach have reasonably good relations with most of the other non-insectoid factions, except with MekPurrs and Blarad Kingdom, who they deem cooperate to closely with UTF.");
        tempFaction.setHowToPlay("Try to use a good mix of capital ships and squadrons, and use your powerful battle stars to full effect. Also use your superior bombardment to quickly conquer planets or raze enemy planets if needed.");
        addGeneralShipTypesAndStartingTypes(tempFaction,gw,true);
        tempFaction.setClosedPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        tempFaction.setCanReconstruct(true);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        Faction azuriachFaction = tempFaction;

        // *********
        // * IRSOL *
        // *********
        tempFaction = new Faction("IRSOL",Faction.getColorHexString(0,0,255),neutral);

        // Huge Merchant Freighter
        typeName = "Huge Merchant Freighter";
        tempsst = new SpaceshipType(typeName,"HMF",SpaceshipType.SIZE_HUGE,SpaceshipRange.SHORT,1,16,uSIC);
        tempsst.setDescription("Huge-sized short range civilian merchant ship. Give income bonuses on frendly and neutral open planets.");
        tempsst.setIncOwnOpenBonus(5);
        tempsst.setIncFrendlyOpenBonus(6);
        tempsst.setIncNeutralOpenBonus(7);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Small Q-ship 
        typeName = "Small Q-ship";
		tempsst = new SpaceshipType(typeName,"SQS",SpaceshipType.SIZE_SMALL,20,70,SpaceshipRange.LONG,3,10,uSIC,30,5);
		tempsst.setDescription("A Q-ship is a military ship built to look like a civilian merchant freighter. It is primarily used to lure and destroy enemy merchant raiders. It actually has a small cargo hold with which it can earn some income to lessen the cost of the ship.");
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setIncOwnOpenBonus(1);
        tempsst.setIncFrendlyOpenBonus(1);
        tempsst.setIncNeutralOpenBonus(1);
        tempsst.setLookAsCivilian(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Medium Q-ship 
        typeName = "Medium Q-ship";
		tempsst = new SpaceshipType(typeName,"MQS",SpaceshipType.SIZE_MEDIUM,30,130,SpaceshipRange.LONG,5,15,uSIC,20,5);
		tempsst.setDescription("A medium Q-ship is a military ship built to look like a civilian merchant freighter. It is primarily used to lure and destroy enemy merchant raiders or to backstab formerly allies. It actually has a cargo hold with which it can earn some income to lessen the cost of the ship.");
		tempsst.setArmor(20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setIncOwnOpenBonus(1);
        tempsst.setIncFrendlyOpenBonus(1);
        tempsst.setIncNeutralOpenBonus(1);
        tempsst.setPsychWarfare(1);
        tempsst.setLookAsCivilian(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // IRSOL Fighter
        typeName = "IRSOL Fighter";
        tempsst = new SpaceshipType(typeName,"I-F",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC,SpaceshipRange.NONE,1,4,uSIC,sqdBaseSmAtt+5,sqdBaseFightSqdAtt);
		tempsst.setDescription("A fighter squadron specialized in attacking enemy squadrons. It can also do some damage to enemy capital ships. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // IRSOL Bomber
        typeName = "IRSOL Bomber";
        tempsst = new SpaceshipType(typeName,"I-B",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC + 5,SpaceshipRange.NONE,1,5,uSIC,sqdBaseSmAtt,sqdBaseBombSqdAtt);
		tempsst.setDescription("Squadron specialized in attacking capital ships, especially medium and large where its medium torpedo salvoes can can do significant damage. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(50);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // IRSOL Destroyer
        typeName = "IRSOL Destroyer";
		tempsst = new SpaceshipType(typeName,"IDes",SpaceshipType.SIZE_MEDIUM,50,150,SpaceshipRange.LONG,5,15,uSIC,35,10);
		tempsst.setDescription("Medium-sized long range military ship. It is most useful when attacking small or medium opponents or as support against less powerful large ships. It is too weak to do any significant damage against huge opponents.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(35);        
        tempsst.setWeaponsMaxSalvoesMedium(4);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Azuriach Light Carrier
        typeName = "IRSOL Light Carrier";
		tempsst = new SpaceshipType(typeName,"ILCa",SpaceshipType.SIZE_MEDIUM,50,180,SpaceshipRange.LONG,6,18,uSIC,10,15);
		tempsst.setDescription("Medium-sized long range squadron carrier. Can carry up to 4 squadrons.");
		tempsst.setArmor(10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(4);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // IRSOL Cruiser
        typeName = "IRSOL Cruiser";
		tempsst = new SpaceshipType(typeName,"ICr",SpaceshipType.SIZE_LARGE,120,380,SpaceshipRange.SHORT,9,28,uSIC,40,10);
		tempsst.setDescription("Large-sized short-range capital ship. It is very good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It has troops, bombardment and can carry one squadron.");
		tempsst.setArmor(60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(130);
        tempsst.setSquadronCapacity(1);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // IRSOL Carrier
        typeName = "IRSOL Carrier";
		tempsst = new SpaceshipType(typeName,"ICa",SpaceshipType.SIZE_LARGE,100,350,SpaceshipRange.SHORT,7,22,uSIC,10,20);
		tempsst.setDescription("Large-sized short range squadron carrier. Can carry up to 6 squadrons.");
		tempsst.setArmor(40,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(6);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // IRSOL Battle Starship
        typeName = "IRSOL Battle Starship";
		tempsst = new SpaceshipType(typeName,"IBSs",SpaceshipType.SIZE_HUGE,200,650,SpaceshipRange.SHORT,18,55,uSIC,40,15);
		tempsst.setDescription("Huge-sized short range capital ship. Has a powerful array of weapons that make significant damage to ships of all sizes, and have powerful armor. It has troops, bombardment and can carry three squadrons. It also have a tactical center to increase combat effectiveness.");
		tempsst.setArmor(80,60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(100);
        tempsst.setWeaponsStrengthLarge(200);
        tempsst.setWeaponsStrengthHuge(100);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setSquadronCapacity(3);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(4);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // IRSOL Battle Star
        typeName = "IRSOL Battle Star";
		tempsst = new SpaceshipType(typeName,"IBS",SpaceshipType.SIZE_HUGE,280,950,SpaceshipRange.SHORT,22,70,uSIC,60,20);
		tempsst.setDescription("Huge-sized short range capital ship. Have a powerful array of weapons that make massive damage to ships of all sizes, and have very powerful armor. It has planetary bombardment, elite troops, can carry up to four squadrons and have an advanced tactical center to increase combat efficiency.");
		tempsst.setArmor(90,70,50);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(120);
        tempsst.setWeaponsStrengthLarge(200);
        tempsst.setWeaponsStrengthHuge(200);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setSquadronCapacity(4);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(2);
        tempsst.setIncreaseInitiative(8);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        tmpVipType = new VIPType("IRSOL Governor","Gov",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(2);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        govType = tmpVipType;

        // Buildings
        // *********
        tempBuildings = new Buildings();
        
//        tmpBuildingType = new BuildingType("IRSOL Space Station", "ISS", 8, uBIC); // cost 3 + 1
//        tmpBuildingType.setOpenPlanetBonus(4);
//        tmpBuildingType.setTechBonus(10);
//        tmpBuildingType.setSpaceport(true);
//        tmpBuildingType.setPlanetUnique(true);
//        tmpBuildingType.setAutoDestructWhenConquered(true);
//        tempBuildings.addBuilding(tmpBuildingType);        
        
        setWharfs(tempBuildings,uBIC,"IRSOL","I",5,5,10,15);
        setSpaceStations(tempBuildings,uBIC,"IRSOL","I",1,-2,3);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("IRSOL Small Orbital Wharf"));
        
        tempFaction.setBuildings(tempBuildings);
               
        tempFaction.setDescription("IRSOL are a human faction but its citizens have adopted perfectly to a life in space, and they are experts at building orbital structures and also have the best merchant fleet in known space.");
        tempFaction.setShortDescription("Human trade-oriented faction.");
        tempFaction.setAdvantages("Open planet income, huge trader");
        tempFaction.setDisadvantages("None");
        tempFaction.setHistory("The growth of interstellar trade cartels grew in the early human expansion until they litterally formed their own state. It is the smallest of the three human factions, but very rich, and its trade ships traverse most of known space. IRSOL have good relations (which off course is good for business) with all of the other non-insectoid factions except the Ranan Worlds, who they accuse of piracy and raiding.");
        tempFaction.setHowToPlay("Try to use a good mix of capital ships and squadrons. Open your planets if possible to get the open income bonus. Also use traders and space stations to maximize income.");
        addGeneralShipTypesAndStartingTypes(tempFaction,gw,true);
        tempFaction.setOpenPlanetBonus(2);
        tempFaction.setGovernorVIPType(govType);
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(9);
        //tempFaction.setReconstructCostMultiplier(3);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        Faction irsolFaction = tempFaction;

        // ***************
        // * Transhumans *
        // ***************
        tempFaction = new Faction("Transhumans",Faction.getColorHexString(140,140,255),neutral);

        // Transhuman Fighter-Bomber
        typeName = "Transhuman Fighter-Bomber";
        tempsst = new SpaceshipType(typeName,"T-FB",SpaceshipType.SIZE_SMALL,sqdBaseSh+10,sqdBaseDC,SpaceshipRange.SHORT,2,10,uSIC,sqdBaseSmAtt+10,sqdBaseFightSqdAtt+5);
		tempsst.setDescription("An all-round squadron good at attacking squadrons or small and medium capital ships, and can even hurt larger ships. It is equipped with a short-range hyperdrive which allows it to move between starsystems on its own, but can also be carried in a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Transhuman Patrol Corvette
        typeName = "Transhuman Patrol Corvette";
		tempsst = new SpaceshipType(typeName,"TPCo",SpaceshipType.SIZE_SMALL,60,60,SpaceshipRange.LONG,3,10,uSIC,40,15);
		tempsst.setDescription("Powerful small sized long range military ship. It is most useful when attacking small or even medium opponents. It is too weak to do much damage against large opponents, and is virtually useless against huge ships.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Transhuman Destroyer
        typeName = "Transhuman Destroyer";
		tempsst = new SpaceshipType(typeName,"TDes",SpaceshipType.SIZE_MEDIUM,180,180,SpaceshipRange.LONG,5,20,uSIC,50,20);
		tempsst.setDescription("Powerful medium sized long range military ship. It is most useful when attacking small or medium opponents or as support against large ships. It is too weak to do much damage against huge opponents.");
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(50);        
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Transhuman Command ship
        typeName = "Transhuman Command Ship";
		tempsst = new SpaceshipType(typeName,"TCom",SpaceshipType.SIZE_MEDIUM,10,180,SpaceshipRange.LONG,5,20,uSIC,5,5);
		tempsst.setDescription("Medium-sized long range command ship. Weakly armed, but have a very advanced tactical center to increase combat efficiency, have a planetary survey capacity and stops enemies from retreating.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setIncreaseInitiative(10);
        tempsst.setInitDefence(10);
        tempsst.setPlanetarySurvey(true);        
        tempsst.setNoRetreat(true);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Transhuman Siege ship
        typeName = "Transhuman Siege Ship";
		tempsst = new SpaceshipType(typeName,"TSie",SpaceshipType.SIZE_MEDIUM,10,180,SpaceshipRange.LONG,5,20,uSIC,5,5);
		tempsst.setDescription("Medium-sized long range siege ship. Weakly armed, but have elite troops and planetary bombardment capacity.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setPsychWarfare(2);
        tempsst.setBombardment(1);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Transhuman Carrier
        typeName = "Transhuman Carrier";
		tempsst = new SpaceshipType(typeName,"TCa",SpaceshipType.SIZE_MEDIUM,10,180,SpaceshipRange.LONG,5,16,uSIC,5,5);
		tempsst.setDescription("Medium-sized long range squadron carrier. Can carry up to 6 squadrons.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(6);        
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Transhuman Fleet Cruiser
        typeName = "Transhuman Fleet Cruiser";
		tempsst = new SpaceshipType(typeName,"TFCr",SpaceshipType.SIZE_LARGE,550,550,SpaceshipRange.SHORT,12,50,uSIC,50,15);
		tempsst.setDescription("Heavy large-sized short range capital ship. Has a powerful array of weapons that can even make significant damage to huge ships. It has weak armor, but have very strong shields and never need to reload it's main arnament.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(180);
        tempsst.setWeaponsStrengthLarge(140);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        tmpVipType = new VIPType("Transhuman Governor","Gov",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        govType = tmpVipType;

        // Buildings
        // *********
        tempBuildings = new Buildings();
        
//        tmpBuildingType = new BuildingType("Transhuman Space Station", "TSS", 10, uBIC); // cost 5 + 1
//        tmpBuildingType.setOpenPlanetBonus(3);
//        tmpBuildingType.setClosedPlanetBonus(1);
//        tmpBuildingType.setTechBonus(10);
//        tmpBuildingType.setSpaceport(true);
//        tmpBuildingType.setPlanetUnique(true);
//        tmpBuildingType.setAutoDestructWhenConquered(true);
//        tempBuildings.addBuilding(tmpBuildingType);        
        
        setWharfs(tempBuildings,uBIC,"Transhuman","T",5,10,15,25);
        setSpaceStations(tempBuildings,uBIC,"Transhuman","T",0,0,3);

        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Transhuman Small Orbital Wharf"));
        
        tempFaction.setBuildings(tempBuildings);

        tempFaction.setDescription("The hyper-intelligent transhumans are no longer considered human, and their high-tech knowledge are unmatched. The transhumans are a minor race and doesn't have many different ship designs but those that they have are very powerful and highly specialized.");
        tempFaction.setShortDescription("High-Tech faction with specialized ships");
        tempFaction.setAdvantages("High tech bonus, powerful shields");
        tempFaction.setDisadvantages("Very expensive reconstruct");
        tempFaction.setHistory("The Transhuman race have developed from early human colonists, and have developed inte a technocrat society of hyper-intelligent humanoids. Their high technology have enabled them to spread their rule to many planets.");
        tempFaction.setHowToPlay("Transhumans have very powerful but specialized ships, and it is vital to playing as at transhuman to use a good mix of the right ship types");
        addGeneralShipTypesAndStartingTypes(tempFaction,gw,true,false,false);
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Transhuman Patrol Corvette"));
        tempFaction.setGovernorVIPType(govType);
        tempFaction.setTechBonus(15);
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(12);
        //tempFaction.setReconstructCostMultiplier(4);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        Faction transhumanFaction = tempFaction;

        // ***********
        // * MekPurr *
        // ***********
        tempFaction = new Faction("MekPurr",Faction.getColorHexString(255,61,255),good);

        // MekPurr Destroyer
        typeName = "MekPurr Destroyer";
		tempsst = new SpaceshipType(typeName,"MDes",SpaceshipType.SIZE_MEDIUM,75,150,SpaceshipRange.LONG,5,18,uSIC,40,40);
		tempsst.setDescription("Medium sized long range military ship. It is most useful when attacking small or medium opponents or as support against less powerful large ships. It is also very effective against squadrons. It is too weak to do any significant damage against huge opponents.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(40);        
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // MekPurr Cruiser
        typeName = "MekPurr Cruiser";
		tempsst = new SpaceshipType(typeName,"MCr",SpaceshipType.SIZE_LARGE,150,300,SpaceshipRange.LONG,9,28,uSIC,40,30);
		tempsst.setDescription("Large-sized long-range capital ship. It is very good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It has troops and bombardment.");
		tempsst.setArmor(60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(130);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(5);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // MekPurr Battle Starship
        typeName = "MekPurr Battle Starship";
		tempsst = new SpaceshipType(typeName,"MBSs",SpaceshipType.SIZE_HUGE,300,600,SpaceshipRange.LONG,20,60,uSIC,40,15);
		tempsst.setDescription("Huge-sized long range capital ship. The MekPuss Battle starship is the smallest of its class but is the biggest ship in known space that have long range capacity. Has a powerful array of weapons that make significant damage to ships of all sizes, even if its arnament is the weakest of all battle starships, and have powerful armor. It has troops, bombardment and have an advanced tactical center to increase combat effectiveness.");
		tempsst.setArmor(80,60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(100);
        tempsst.setWeaponsStrengthLarge(150);
        tempsst.setWeaponsStrengthHuge(100);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(10);
        tempsst.setPlanetarySurvey(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        tmpVipType = new VIPType("MekPurr Governor","Gov",good,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        tmpVipType.setResistanceBonus(1);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        govType = tmpVipType;

        // Buildings
        // *********
        tempBuildings = new Buildings();
        
//        tmpBuildingType = new BuildingType("MekPurr Space Station", "MSS", 9, uBIC); // cost 4 + 1
//        tmpBuildingType.setOpenPlanetBonus(3);
//        tmpBuildingType.setClosedPlanetBonus(1);
//        tmpBuildingType.setTechBonus(10);
//        tmpBuildingType.setSpaceport(true);
//        tmpBuildingType.setPlanetUnique(true);
//        tmpBuildingType.setAutoDestructWhenConquered(true);
//        tempBuildings.addBuilding(tmpBuildingType);        
        
        setWharfs(tempBuildings,uBIC,"MekPurr","M",5,10,15,25);
        setSpaceStations(tempBuildings,uBIC,"MekPurr","M",0,0,3);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("MekPurr Small Orbital Wharf"));
        
        tempFaction.setBuildings(tempBuildings);

        tempFaction.setDescription("The MekPurrs are a feline (cat-people) race. They are short in stature and have retractable claws. They have a very high tech (only surpassed by the transhumans). Their capital warships all have long range (ever their battlestarship) and they don't use any squadrons.");
        tempFaction.setShortDescription("High-Tech cat-people faction with long-range capital ships.");
        tempFaction.setAdvantages("All capital ships are long range, tech bonus");
        tempFaction.setDisadvantages("No squadrons");
        tempFaction.setHistory("The MekPurrs history is of a long conflict with their brethren, the larger and highly aggressive Avatar feline species, and the MekPurrs only way to overcome their nemesis was to develop higher technoöogy weapons. In the end the MekPurrs won and relying on their technological knowledge and a high degree of robotic servants to perform heavy tasks, they are set to never let any other species threaten their existence again.");
        tempFaction.setHowToPlay("To combat enemy squadrons the MekPurr Destroyer is vital. Also use the long range Cruiser and Battle Starships to full effect.");
        addGeneralShipTypesAndStartingTypes(tempFaction,gw,true);
        tempFaction.setGovernorVIPType(govType);
        tempFaction.setTechBonus(10);
        tempFaction.setCanReconstruct(true);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        Faction mekpurrFaction = tempFaction;


        // ******************
        // * Blarad Kingdom *
        // ******************
        tempFaction = new Faction("Blarad Kingdom",Faction.getColorHexString(122,255,122),good);

        // Blarad Bomber
        typeName = "Blarad Bomber";
        tempsst = new SpaceshipType(typeName,"B-B",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC + 20,SpaceshipRange.NONE,1,8,uSIC,sqdBaseSmAtt+5,sqdBaseBombSqdAtt+5);
		tempsst.setDescription("The Blarad bomber squadron is the most powerful bomber type of all starnations, and its torpedoe attacks are matched by none. It can also take more damage than any other squadron type and can even pack some punch against enemy squadrons. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(60);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(60);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Blarad Troop Transport
        typeName = "Blarad Troop Transport";
		tempsst = new SpaceshipType(typeName,"BTrT",SpaceshipType.SIZE_MEDIUM,10,40,SpaceshipRange.LONG,3,12,uSIC,10,5);
		tempsst.setDescription("Medium long range military troop ship. This military troop transport carry Blarad elite troops and can single-handedly besiege enemy planets, and can run away from enemy ships if ordered.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setPsychWarfare(2);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Blarad Destroyer
        typeName = "Blarad Destroyer";
		tempsst = new SpaceshipType(typeName,"BDes",SpaceshipType.SIZE_MEDIUM,80,250,SpaceshipRange.LONG,5,20,uSIC,40,15);
		tempsst.setDescription("Very heavy medium sized long range military ship, and is almost as big as a light cruiser. It is most useful when attacking small opponents and especially effective when attacking medium sized medium opponents with its heavy duty torpedo salvoes. It is also useful as support against large ships. It is too weak to do much damage against huge opponents.");
		tempsst.setArmor(50);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(80);        
        tempsst.setWeaponsMaxSalvoesMedium(4);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Blarad Light Carrier
        typeName = "Blarad Carrier";
		tempsst = new SpaceshipType(typeName,"BCa",SpaceshipType.SIZE_MEDIUM,70,280,SpaceshipRange.LONG,6,20,uSIC,10,15);
		tempsst.setDescription("Medium long range squadron carrier. Can carry up to 4 squadrons.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(4);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Blarad Battle Cruiser
        typeName = "Blarad Cruiser";
		tempsst = new SpaceshipType(typeName,"BCr",SpaceshipType.SIZE_LARGE,150,550,SpaceshipRange.SHORT,12,36,uSIC,40,10);
		tempsst.setDescription("Large-sized short range capital ship. The Blarad Cruiser is the same size as a Battlecruiser in other navies, and at least as powerful. It is a large-sized short-range capital ship and is very good against small, medium or large opponents and can do serious damage to huge opponents. It has troops, bombardment and can carry one squadron. It's only disadvantage is that is have a very limited number of salvoes available and run dry quickle if it meets multiple opponents, and it needs to resupply between combats.");
		tempsst.setArmor(75,50);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(200);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setWeaponsStrengthLarge(200);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setSquadronCapacity(1);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Blarad Battle Starship
        typeName = "Blarad Battle Starship";
		tempsst = new SpaceshipType(typeName,"BBSs",SpaceshipType.SIZE_HUGE,280,900,SpaceshipRange.SHORT,22,70,uSIC,50,15);
		tempsst.setDescription("Huge short range capital ship. The Blarad Harulta class battle starships are so big that any other navy would label them a battlestar. Has a powerful array of weapons that make massive damage to ships of all sizes, and have powerful armor. It has troops, bombardment and can carry three squadrons. It also have a tactical center to increase combat effectiveness. It's only disadvantage is that is have a very limited number of salvoes available and run dry quickle if it meets multiple opponents, and it needs to resupply between combats.");
		tempsst.setArmor(85,65,45);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(150);
        tempsst.setWeaponsStrengthLarge(300);
        tempsst.setWeaponsMaxSalvoesLarge(6);
        tempsst.setWeaponsStrengthHuge(250);
        tempsst.setWeaponsMaxSalvoesHuge(2);
        tempsst.setSquadronCapacity(3);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(5);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        tmpVipType = new VIPType("Blarad Governor","Gov",good,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        govType = tmpVipType;

        // Buildings
        // *********
        tempBuildings = new Buildings();
        
//        tmpBuildingType = new BuildingType("Blarad Space Station", "USS", 10, uBIC); // cost 5 + 1
//        tmpBuildingType.setOpenPlanetBonus(3);
//        tmpBuildingType.setClosedPlanetBonus(1);
//        tmpBuildingType.setTechBonus(10);
//        tmpBuildingType.setSpaceport(true);
//        tmpBuildingType.setPlanetUnique(true);
//        tmpBuildingType.setAutoDestructWhenConquered(true);
//        tempBuildings.addBuilding(tmpBuildingType);        
        
        setWharfs(tempBuildings,uBIC,"Blarad","B",5,10,15,20);
        setSpaceStations(tempBuildings,uBIC,"Blarad","B",0,0,3);

        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Blarad Small Orbital Wharf"));
        
        tempFaction.setBuildings(tempBuildings);
               
        tempFaction.setDescription("The Blarad Kingdom are populated by ursoids (bear-people). They are huge in stature, often 2.5m high and unmatched in unarmed combat. Their starship designs reflect their stature - they tend to do things big.");
        tempFaction.setShortDescription("Bear-people faction with elite troops and best bomber squadrons");
        tempFaction.setAdvantages("Open planet income, resistance bonus, elite troops, bomber squadrons");
        tempFaction.setDisadvantages("No fighter squadrons, finite salvoes, expensive reconstruct");
        tempFaction.setHistory("The Ursoids are a proud and (mostly) honorable people, and their feudal leaders expect reverance not only from their own people but of all other species as well, as they see as inferior. The only species they have respect for are the savage Avatar felines, who are almost their match in unarmed combat.");
        tempFaction.setHowToPlay("Use the \"Blarad Troop Transport\" with its elite troops and the powerful \"Blarad Bomber\" to full effect. Remember to use Mobile Resupply Base to reload capital ships after combat if not at home base.");
        addGeneralShipTypesAndStartingTypes(tempFaction,gw,false,true,true);
        tempFaction.setGovernorVIPType(govType);
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(9);
        //tempFaction.setReconstructCostMultiplier(3);
        tempFaction.setResistanceBonus(1);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        Faction blaradFaction = tempFaction;

        // ************
        // * Hiss'ist *
        // ************
        tempFaction = new Faction("Hiss'ist",Faction.getColorHexString(0,255,255),evil);

        // Hiss'ist Fighter
        typeName = "Hiss'ist Fighter";
        tempsst = new SpaceshipType(typeName,"H-F",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC,SpaceshipRange.NONE,1,3,uSIC,sqdBaseSmAtt,sqdBaseFightSqdAtt+5);
		tempsst.setDescription("A fighter squadron specialized in attacking enemy squadrons. It can also do some minor damage to enemy capital ships. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Hiss'ist Fighter-Bomber
        typeName = "Hiss'ist Fighter-Bomber";
        tempsst = new SpaceshipType(typeName,"H-FB",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC,SpaceshipRange.NONE,1,3,uSIC,sqdBaseSmAtt,sqdBaseFightSqdAtt-5);
		tempsst.setDescription("An all-round squadron which can both do some damage against squadrons or capital ships, where its medium torpedo salvoes can hurt larger ships, especially medium sized ones. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Hiss'ist Bomber
        typeName = "Hiss'ist Bomber";
        tempsst = new SpaceshipType(typeName,"H-B",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC + 5,SpaceshipRange.NONE,1,3,uSIC,sqdBaseSmAtt,sqdBaseBombSqdAtt);
		tempsst.setDescription("Squadron specialized in attacking capital ships, especially ships larger than small where its medium and large torpedo salvoes can can do significant damage. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(50);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Hiss'ist Troop Landing Crafts
        typeName = "Hiss'ist Drop Ships";
        tempsst = new SpaceshipType(typeName,"H-DS",SpaceshipType.SIZE_SMALL,sqdBaseSh-5,sqdBaseDC-5,SpaceshipRange.NONE,1,3,uSIC,1,5);
		tempsst.setDescription("Squadron carrying troops which can attack planets. Virtually useless in combat and should probably be screened. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setPsychWarfare(1);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Hiss'ist Ground attack
        typeName = "Hiss'ist Ground Attack";
        tempsst = new SpaceshipType(typeName,"H-GA",SpaceshipType.SIZE_SMALL,sqdBaseSh-5,sqdBaseDC-5,SpaceshipRange.NONE,1,3,uSIC,1,5);
		tempsst.setDescription("Air-to-ground attack shuttles squadron which specializes in attacking (bombarding) planets. Virtually useless in combat and should probably be screened. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setBombardment(2);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Hiss'ist Raider
        typeName = "Hiss'ist Raider";
        tempsst = new SpaceshipType(typeName,"H-FB",SpaceshipType.SIZE_SMALL,sqdBaseSh+5,sqdBaseDC,SpaceshipRange.NONE,1,8,uSIC,sqdBaseSmAtt+10,sqdBaseFightSqdAtt);
		tempsst.setDescription("An advanced all-round squadron which can both do some damage against squadrons and capital ships, where its medium torpedo salvoes can hurt larger ships, especially medium sized ones. It's unique design and speed enables it to quickly break through enemy lines and attack screened ships. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(60);
        tempsst.setWeaponsMaxSalvoesMedium(1);
        tempsst.setCanAttackScreenedShips(true);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Hiss'ist Scout
        typeName = "Hiss'ist Squadron Leader";
        tempsst = new SpaceshipType(typeName,"H-S",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC-10,SpaceshipRange.NONE,1,8,uSIC,5,5);
		tempsst.setDescription("Commumnication, command and reconnaissance squadron. Give initiative bonus and can survey planets. Virtually useless in combat and should probably be screened. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setPlanetarySurvey(true);
        tempsst.setIncreaseInitiative(10);
        tempsst.setCanBlockPlanet(false);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Hiss'ist Destroyer
        typeName = "Hiss'ist Destroyer";
		tempsst = new SpaceshipType(typeName,"HDes",SpaceshipType.SIZE_MEDIUM,50,150,SpaceshipRange.LONG,5,15,uSIC,30,10);
		tempsst.setDescription("Medium sized long range military ship. It is most useful when attacking small or medium opponents or as support against less powerful large ships. It is too weak to do any significant damage against huge opponents.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(40);        
        tempsst.setWeaponsMaxSalvoesMedium(4);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Hiss'ist Light Carrier
        typeName = "Hiss'ist Light Carrier";
		tempsst = new SpaceshipType(typeName,"HLCa",SpaceshipType.SIZE_MEDIUM,70,250,SpaceshipRange.LONG,5,20,uSIC,10,15);
		tempsst.setDescription("Medium-sized long range squadron carrier. Can carry up to 6 squadrons.");
		tempsst.setArmor(20);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(6);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Hiss'ist Light Cruiser
        typeName = "Hiss'ist Light Cruiser";
		tempsst = new SpaceshipType(typeName,"HLCr",SpaceshipType.SIZE_LARGE,80,300,SpaceshipRange.SHORT,8,24,uSIC,40,15);
		tempsst.setDescription("Large-sized short-range capital ship. It is very good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It has troops, bombardment and can carry one squadron.");
		tempsst.setArmor(45,30);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(100);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setSquadronCapacity(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Hiss'ist Carrier
        typeName = "Hiss'ist Carrier";
		tempsst = new SpaceshipType(typeName,"HCa",SpaceshipType.SIZE_LARGE,150,500,SpaceshipRange.SHORT,7,26,uSIC,10,20);
		tempsst.setDescription("Large-sized short range squadron carrier. Can carry up to 12 squadrons.");
		tempsst.setArmor(30,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(12);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Hiss'ist Battle Starship
        typeName = "Hiss'ist Battle Starship";
		tempsst = new SpaceshipType(typeName,"HBSs",SpaceshipType.SIZE_HUGE,200,700,SpaceshipRange.SHORT,20,60,uSIC,50,15);
		tempsst.setDescription("Huge-sized short range capital ship. Has a powerful array of weapons that make significant damage to ships of all sizes, and have powerful armor. It has troops, bombardment and can carry three squadrons. It also have a tactical center to increase combat effectiveness.");
		tempsst.setArmor(80,60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(120);
        tempsst.setWeaponsStrengthLarge(200);
        tempsst.setWeaponsStrengthHuge(150);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setSquadronCapacity(4);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(4);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        tmpVipType = new VIPType("Hiss'ist Leader","Gov",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        govType = tmpVipType;

        // Buildings
        // *********
        tempBuildings = new Buildings();
        
//        tmpBuildingType = new BuildingType("Hiss'ist Space Station", "HSS", 10, uBIC); // cost 5 + 1
//        tmpBuildingType.setOpenPlanetBonus(2);
//        tmpBuildingType.setClosedPlanetBonus(2);
//        tmpBuildingType.setTechBonus(10);
//        tmpBuildingType.setSpaceport(true);
//        tmpBuildingType.setPlanetUnique(true);
//        tmpBuildingType.setAutoDestructWhenConquered(true);
//        tempBuildings.addBuilding(tmpBuildingType);        
        
        setWharfs(tempBuildings,uBIC,"Hiss'ist","H",5,10,15,25);
        setSpaceStations(tempBuildings,uBIC,"Hiss'ist","H",0,1,3);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Hiss'ist Small Orbital Wharf"));
        
        tempFaction.setBuildings(tempBuildings);
        
        tempFaction.setDescription("The Hiss'ist people are lizard-men, and are a remorseless people. They specialize in starfighter squadrons and rely heavily on them in interstellar combat. Their willingness to sacrifice troops in defence of their planets make them difficult to conquer.");
        tempFaction.setShortDescription("Lizard-men faction specialized in squadron combat");
        tempFaction.setAdvantages("Closed planet income, resistance bonus, versatile squadrons, cheap reconstruct");
        tempFaction.setDisadvantages("Somewhat weak capital ships");
        tempFaction.setHistory("The Hiss'ist have always had a problem with overpopulation, and their history is fraught with wars between different factions of the Hiss'ist people. After they developed starflight they have had an agressive policy of expansion to solve this problem.");
        tempFaction.setHowToPlay("Make heavy use the Hiss'ist squadrons including the \"Squadron Leader\", \"Ground Attack\" and \"Drop ships\" squadron types.");
        addGeneralShipTypesAndStartingTypes(tempFaction,gw,true);
        tempFaction.setClosedPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        tempFaction.setResistanceBonus(2);
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(6);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        Faction hissistFaction = tempFaction;

        // ***************
        // * Ranan Horde *
        // ***************
        tempFaction = new Faction("Ranan Horde",Faction.getColorHexString(255,255,63),evil);

        // Ranan Fighter-Bomber
        typeName = "Ranan Fighter-Bomber";
        tempsst = new SpaceshipType(typeName,"R-FB",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC + 5,SpaceshipRange.NONE,1,5,uSIC,sqdBaseSmAtt,sqdBaseFightSqdAtt-5);
		tempsst.setDescription("An all-round squadron which can both do some damage against squadrons or capital ships, where its two-shot medium and large torpedo salvoes can hurt larger ships. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setWeaponsStrengthLarge(25);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Ranan Raider Shuttles
        typeName = "Ranan Raider Shuttles";
        tempsst = new SpaceshipType(typeName,"R-RS",SpaceshipType.SIZE_SMALL,0,sqdBaseDC - 5,SpaceshipRange.NONE,1,6,uSIC,5,5);
		tempsst.setDescription("A squadron of troop carrying shuttles which also can raid planets. This can bring a good profit on enemy and neutral planets. It is almost useless in combat. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setIncEnemyClosedBonus(5);
        tempsst.setIncEnemyOpenBonus(5);
        tempsst.setIncNeutralClosedBonus(5);
        tempsst.setIncNeutralOpenBonus(5);
        tempsst.setPsychWarfare(1);
        tempsst.setBombardment(1);
        tempsst.setCanBlockPlanet(true);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Ranan Corvette
        typeName = "Ranan Corvette";
		tempsst = new SpaceshipType(typeName,"RCrv",SpaceshipType.SIZE_MEDIUM,30,130,SpaceshipRange.LONG,5,10,uSIC,30,15);
		tempsst.setDescription("Medium sized long range military ship. It is most useful when attacking small opponents or as support against more powerful medium. Against large and huge opponents it has little to offer. The Ranan Corvette is less powerful than the standard Corvette but can carry one squadron attached on the outside of it's hull.");
		tempsst.setArmor(20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setSquadronCapacity(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Ranan Destroyer
        typeName = "Ranan Destroyer";
		tempsst = new SpaceshipType(typeName,"RDes",SpaceshipType.SIZE_MEDIUM,50,200,SpaceshipRange.LONG,6,14,uSIC,30,10);
		tempsst.setDescription("Heavy medium sized long range military ship. It is most useful when attacking small or medium opponents or as support against less powerful large ships. It is too weak to do any significant damage against huge opponents. The Ranan Destroyer is somewhat weaker than other destroyers, but has bombardment and can carry one squadron attached outside its hull.");
		tempsst.setArmor(30);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setBombardment(1);
        tempsst.setSquadronCapacity(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Ranan Troop Carrier
        typeName = "Ranan Siege Carrier";
		tempsst = new SpaceshipType(typeName,"RSC",SpaceshipType.SIZE_MEDIUM,40,200,SpaceshipRange.LONG,6,14,uSIC,10,10);
		tempsst.setDescription("Medium-sized long range ship. This ship can give siege support to troops and have room for three squadrons. It does not carry troops itself and can not besiege/block planets on its own.");
		tempsst.setArmor(20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setPsychWarfare(2);
        tempsst.setSquadronCapacity(3);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Ranan Light Cruiser
        typeName = "Ranan Light Cruiser";
		tempsst = new SpaceshipType(typeName,"RLCr",SpaceshipType.SIZE_LARGE,70,280,SpaceshipRange.LONG,9,22,uSIC,40,10);
		tempsst.setDescription("Light large-sized long-range capital ship. It is very good against small or medium opponents and gives good support against large ones. It is too weak to threaten huge opponents. The Ranan Light Cruiser is somewhat weaker than other light cruisers but is long range, has place for one squadron and also has significant bombardment ability.");
		tempsst.setArmor(40,25);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(80);
        tempsst.setWeaponsMaxSalvoesMedium(8);
        tempsst.setSquadronCapacity(1);
        tempsst.setBombardment(2);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Ranan Battlecruiser
        typeName = "Ranan Battlecruiser";
		tempsst = new SpaceshipType(typeName,"RBCr",SpaceshipType.SIZE_LARGE,120,450,SpaceshipRange.SHORT,12,28,uSIC,30,10);
		tempsst.setDescription("Heavy large-sized short range capital ship. Has a powerful array of weapons that can even make some damage to huge ships. It has bombardment capacity and can carry three squadrons.");
		tempsst.setArmor(45,30);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(120);
        tempsst.setWeaponsMaxSalvoesMedium(8);
        tempsst.setWeaponsStrengthLarge(60);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setWeaponsStrengthHuge(60);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setSquadronCapacity(2);
        tempsst.setBombardment(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        tmpVipType = new VIPType("Ranan Leader","Gov",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setClosedIncBonus(1);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        govType = tmpVipType;

        // Buildings
        // *********
        tempBuildings = new Buildings();
        
//        tmpBuildingType = new BuildingType("Ranan Space Station", "RSS", 14, uBIC); // cost 5 + 2
//        tmpBuildingType.setOpenPlanetBonus(1);
//        tmpBuildingType.setClosedPlanetBonus(3);
//        tmpBuildingType.setTechBonus(10);
//        tmpBuildingType.setSpaceport(true);
//        tmpBuildingType.setPlanetUnique(true);
//        tmpBuildingType.setAutoDestructWhenConquered(true);
//        tempBuildings.addBuilding(tmpBuildingType);        
        
        setWharfs(tempBuildings,uBIC,"Ranan","R",5,10,15);
        setSpaceStations(tempBuildings,uBIC,"Ranan","R",-2,2,3);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Ranan Small Orbital Wharf"));
        
        tempFaction.setBuildings(tempBuildings);
        
        tempFaction.setDescription("The Ranan Horde are canines (dog-people, or maybe rather wolf-men), and their pack mentality influense their strategy in interstellar combat, relying on large numbers of cheaper units. Their excellent guerilla tactics make them difficult to conquer.");
        tempFaction.setShortDescription("Wolf-men faction specialized in raider warfare");
        tempFaction.setAdvantages("Closed planet income, resistance bonus, raider squadrons, cheap reconstruct, long range ships");
        tempFaction.setDisadvantages("No huge capital ships");
        tempFaction.setHistory("The aggressive and independant Ranan clans have warred and raided each others since time unknown. The development of spacetravel have lead to many conflicts when they applied the same attitude towards other space-faring species. Only when these conflicts threatened to annihilate all Ranan clans did they unite and became the Ranan nation. Since they lack the technological skills to build huge spaceships, they still use raider tactics, but on a larger scale.");
        tempFaction.setHowToPlay("Ranans lack of huge capital ships make them weak in up-front confrontation, but use their long range raider capacity (\"Light Cruiser\" and \"Siege carrier\", or \"Corvette/Destroyer\" and \"Raider Shuttles\" combinations) to full effect.");
        addGeneralShipTypesAndStartingTypes(tempFaction,gw,true,false,false);
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Ranan Corvette"));
        tempFaction.setClosedPlanetBonus(1);
        tempFaction.setGovernorVIPType(govType);
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(5);
        tempFaction.setResistanceBonus(1);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        Faction rananFaction = tempFaction;
        
        // ************
        // * Klackons *
        // ************
        tempFaction = new Faction("Klackons",Faction.getColorHexString(182,0,182),klackon);

        int kLDC = 50;
        int kHDC = 150;
        int kLBuy = 5;
        int kHBuy = 8;
        
        // Klackon Ground attack ship
        typeName = "Klackon Gound Attack Ship";
		tempsst = new SpaceshipType(typeName,"KGas",SpaceshipType.SIZE_SMALL,0,kLDC,SpaceshipRange.LONG,2,kLBuy+2,uSIC,2,2);
		tempsst.setDescription("Small long range ship. Used to razed planets and ready them for colonization.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setBombardment(2);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Klackon colony ship
        typeName = "Klackon Colony Ship";
		tempsst = new SpaceshipType(typeName,"KCol",SpaceshipType.SIZE_SMALL,0,kLDC,SpaceshipRange.LONG,2,kLBuy+4,uSIC,2,2);
		tempsst.setDescription("Small long range ship. Used to colonize razed planets.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setPsychWarfare(1);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Klackon Supply Ship
        typeName = "Klackon Supply Ship";
		tempsst = new SpaceshipType(typeName,"KSup",SpaceshipType.SIZE_SMALL,0,kLDC,SpaceshipRange.LONG,2,kLBuy+4,uSIC,2,2);
		tempsst.setDescription("Small long range ship. Used to resupply other Klackon ships.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSupply(1);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Klackon Squadron Hunter
        typeName = "Klackon Squadron Hunter";
		tempsst = new SpaceshipType(typeName,"KSH",SpaceshipType.SIZE_SMALL,10,kLDC,SpaceshipRange.LONG,2,kLBuy,uSIC,15,30);
		tempsst.setDescription("Small long range ship. Anti-squadron ship");
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Klackon laser corvette
        typeName = "Klackon Laser Boat";
		tempsst = new SpaceshipType(typeName,"KLB",SpaceshipType.SIZE_SMALL,10,kLDC,SpaceshipRange.LONG,2,kLBuy,uSIC,40,10);
		tempsst.setDescription("Small long range ship. Specialized in attacking small capital ships");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Klackon Light Missile Boat
        typeName = "Klackon Light Missile Boat";
		tempsst = new SpaceshipType(typeName,"KLMB",SpaceshipType.SIZE_SMALL,10,kLDC,SpaceshipRange.LONG,2,kLBuy,uSIC,5,5);
		tempsst.setDescription("Small long range ship. Specialized in attacking medium capital ships");
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(100);
        tempsst.setWeaponsMaxSalvoesMedium(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Klackon Light Torpedo Boat
        typeName = "Klackon Light Torpedo Boat";
		tempsst = new SpaceshipType(typeName,"KLTB",SpaceshipType.SIZE_SMALL,10,kLDC,SpaceshipRange.LONG,2,kLBuy,uSIC,5,5);
		tempsst.setDescription("Small long range ship. Specialized in attacking large capital ships");
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthLarge(125);
        tempsst.setWeaponsMaxSalvoesLarge(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Klackon Light Starbuster
        typeName = "Klackon Light Starbuster";
		tempsst = new SpaceshipType(typeName,"KLSB",SpaceshipType.SIZE_SMALL,10,kLDC,SpaceshipRange.LONG,2,kLBuy,uSIC,5,5);
		tempsst.setDescription("Small long range ship. Specialized in attacking huge capital ships");
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthHuge(150);
        tempsst.setWeaponsMaxSalvoesHuge(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Klackon Heavy Missile Boat
        typeName = "Klackon Heavy Missile Boat";
		tempsst = new SpaceshipType(typeName,"KHMB",SpaceshipType.SIZE_MEDIUM,20,kHDC,SpaceshipRange.SHORT,3,kHBuy,uSIC,40,10);
		tempsst.setDescription("Medium-sized short range ship. Specialized in attacking medium capital ships");
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(200);
        tempsst.setWeaponsMaxSalvoesMedium(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Klackon Heavy Torpedo Boat
        typeName = "Klackon Heavy Torpedo Boat";
		tempsst = new SpaceshipType(typeName,"KHTB",SpaceshipType.SIZE_MEDIUM,20,kHDC,SpaceshipRange.SHORT,3,kHBuy,uSIC,40,10);
		tempsst.setDescription("Medium-sized short range ship. Specialized in attacking large capital ships");
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(50);
        tempsst.setWeaponsMaxSalvoesMedium(1);
        tempsst.setWeaponsStrengthLarge(200);
        tempsst.setWeaponsMaxSalvoesLarge(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Klackon Heavy Starbuster
        typeName = "Klackon Heavy Starbuster";
		tempsst = new SpaceshipType(typeName,"KHSB",SpaceshipType.SIZE_MEDIUM,20,kHDC,SpaceshipRange.SHORT,3,kHBuy,uSIC,40,10);
		tempsst.setDescription("Medium-sized short range ship. Specialized in attacking huge capital ships");
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(50);
        tempsst.setWeaponsMaxSalvoesMedium(1);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(1);
        tempsst.setWeaponsStrengthHuge(200);
        tempsst.setWeaponsMaxSalvoesHuge(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Klackon Mothership
        typeName = "Klackon Mothership";
		tempsst = new SpaceshipType(typeName,"KMS",SpaceshipType.SIZE_MEDIUM,0,kHDC,SpaceshipRange.SHORT,5,20,uSIC,5,5);
		tempsst.setDescription("Medium-sized short range ship. Has considerable siege ability and can resupply all other type of Klackon combat ships. Also have a tactical center to increase combat efficiency.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setBombardment(3);
        tempsst.setPsychWarfare(2);
        tempsst.setSupply(2);
        tempsst.setIncreaseInitiative(5);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        tmpVipType = new VIPType("Klackon Leader","KL",klackon,uniqueVIPIdCounter);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setInfestate(true);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        govType = tmpVipType;

        // Buildings
        // *********
        tempBuildings = new Buildings();
        
//        tmpBuildingType = new BuildingType("Klackon Space Station", "KSS", 10, uBIC); // cost 10 + 0
//        tmpBuildingType.setClosedPlanetBonus(2);
//        tmpBuildingType.setTechBonus(20);
//        tmpBuildingType.setPlanetUnique(true);
//        tmpBuildingType.setAutoDestructWhenConquered(true);
//        tempBuildings.addBuilding(tmpBuildingType);        
        
        setWharfs(tempBuildings,uBIC,"Klackon","K",5,10);
        setSpaceStations(tempBuildings,uBIC,"Klackon","K",-2,2,1);
        setKlackonMissileDefence(tempBuildings,uBIC);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Klackon Small Orbital Wharf"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Klackon Missile Defence Mk 3"));
        
        tempFaction.setBuildings(tempBuildings);
        
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Klackon Laser Boat"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Klackon Laser Boat"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Klackon Gound Attack Ship"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Klackon Colony Ship"));

        tempFaction.setDescription("The Klackons are aliens, much resembling to huge cockroaches. Their primitive technology stops them from building larger ships than of medium size, but instead have very specialized and cheap units which can be combined into powerful fleets.");
        tempFaction.setShortDescription("Alien faction using swarms of specialized ships in combat");
        tempFaction.setAdvantages("Resistance bonus, powerful specialized small and medium ships, planetary missile defence building");
        tempFaction.setDisadvantages("No large of huge ships, no defence ships, no squadrons, no traders");
        tempFaction.setHistory("Originally the Klackons were a multitude of different tribes, warring for their territories on their homeplanet. By the time their technological development allowed travel to other stars and contact with other spacefaring species, the tribes started to expand to other stars and thereby focusing most of their energy on defeating the resistance they met from the other species.");
        tempFaction.setHowToPlay("Klackons use a variety of specialized small and medium ships, combined into larger swarms. Be careful to use the right kind of ships against different opponents, for instance against an opponent who use a lot of squadrons, always have a number of \"Squadron Hunter\" in your fleets. Dont forget to include a screened \"Supply Ship\" or \"Mothership\" to enable the ships to reload their torpedoes after combat. Use the upgradeable Klackon Missile Defence buildings to make it impossible for enemies to raid your planets with small raider ships.");
        tempFaction.setAlien(true);
        tempFaction.setResistanceBonus(2);
        tempFaction.setGovernorVIPType(govType);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        Faction klackonFaction = tempFaction;

        // ********
        // * Bugs *
        // ********
        tempFaction = new Faction("Bugs",Faction.getColorHexString(255,191,0),arachnid);

        // Bug Fighter
        typeName = "Bug Fighter";
        tempsst = new SpaceshipType(typeName,"B-F",SpaceshipType.SIZE_SMALL,0,sqdBugBaseDC,SpaceshipRange.NONE,1,1,uSIC,10,25);
        tempsst.setDescription("Cheap and basic fighter squadron. It cannot move on it's own but has to be carried inside a carrier/bug meteorite.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setVisibleOnMap(false);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Bug Small attacker
        typeName = "Bug Attacker";
        tempsst = new SpaceshipType(typeName,"B-A",SpaceshipType.SIZE_SMALL,0,sqdBugBaseDC,SpaceshipRange.NONE,1,1,uSIC,30,10);
        tempsst.setDescription("Squadron specialized at attacking small capital ships. It cannot move on it's own but has to be carried inside a carrier/bug meteorite.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setVisibleOnMap(false);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Bug Light Bomber
        typeName = "Bug Light Bomber";
        tempsst = new SpaceshipType(typeName,"B-LB",SpaceshipType.SIZE_SMALL,0,sqdBugBaseDC,SpaceshipRange.NONE,1,1,uSIC,5,5);
        tempsst.setDescription("Bomber squadron specialized aganist medium targets, and to lesser degree large and huge targets, and pretty useless against squadrons or small targets. It cannot move on it's own but has to be carried inside a carrier/bug meteorite.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(100);
        tempsst.setWeaponsMaxSalvoesMedium(1);
        tempsst.setVisibleOnMap(false);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Bug Heavy Bomber
        typeName = "Bug Heavy Bomber";
        tempsst = new SpaceshipType(typeName,"B-HB",SpaceshipType.SIZE_SMALL,0,sqdBugBaseDC,SpaceshipRange.NONE,1,1,uSIC,5,5);
        tempsst.setDescription("Bomber squadron specialized aganist large and huge targets, and pretty useless against squadrons or small or megium targets. It cannot move on it's own but has to be carried inside a carrier/bug meteorite.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthLarge(100);
        tempsst.setWeaponsMaxSalvoesLarge(1);
        tempsst.setWeaponsStrengthHuge(50);
        tempsst.setWeaponsMaxSalvoesHuge(1);
        tempsst.setVisibleOnMap(false);
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Bug Defence Meteorite
        typeName = "Bug Defence Meteorite";
		tempsst = new SpaceshipType(typeName,"BDM",SpaceshipType.SIZE_SMALL,0,200,SpaceshipRange.NONE,1,6,uSIC,10,10);
        tempsst.setDescription("Defence meteorite which can guard against enemy raiders, and in greater numbers they can even protect against enemy medium-sized task forces. Are invisible for enemies unless in the same system.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(50);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setVisibleOnMap(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Small Bug Meteorite
        typeName = "Bug Small Meteorite";
		tempsst = new SpaceshipType(typeName,"BSM",SpaceshipType.SIZE_SMALL,0,500,SpaceshipRange.LONG,5,10,uSIC,10,10);
        tempsst.setDescription("A meteorite retrofitted for interstellar movement and excaved to house three squadrons. It has very weak weapons in itself and need squadrons to make any significant damage to enemy ships. It has both planetary bombardment capacity and troops. Are invisible for enemies unless in the same system.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(3);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setVisibleOnMap(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
        
        // Medium Bug Meteorite
        typeName = "Bug Medium Meteorite";
		tempsst = new SpaceshipType(typeName,"BMM",SpaceshipType.SIZE_MEDIUM,0,1000,SpaceshipRange.LONG,10,20,uSIC,15,15);
        tempsst.setDescription("A meteorite retrofitted for interstellar movement and excaved to house ten squadrons. It has very weak weapons in itself and need squadrons to make any significant damage to enemy ships. It has both planetary bombardment capacity and troops. Are invisible for enemies unless in the same system.");
		tempsst.setArmor(30);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(10);
        tempsst.setBombardment(2);
        tempsst.setPsychWarfare(1);
        tempsst.setVisibleOnMap(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
        
        // Large Bug Meteorite
        typeName = "Bug Large Meteorite";
		tempsst = new SpaceshipType(typeName,"BLM",SpaceshipType.SIZE_LARGE,0,2000,SpaceshipRange.SHORT,14,40,uSIC,15,15);
        tempsst.setDescription("A meteorite retrofitted for interstellar movement and excaved to house twenty squadrons. It has very weak weapons in itself and need squadrons to make any significant damage to enemy ships. It has both heavy planetary bombardment capacity and troops. Are invisible for enemies unless in the same system.");
		tempsst.setArmor(40,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(20);
        tempsst.setBombardment(4);
        tempsst.setPsychWarfare(1);
        tempsst.setVisibleOnMap(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Huge Bug Meteorite
        typeName = "Bug Huge Meteorite";
		tempsst = new SpaceshipType(typeName,"BHM",SpaceshipType.SIZE_HUGE,0,5000,SpaceshipRange.SHORT,20,80,uSIC,15,15);
        tempsst.setDescription("A meteorite retrofitted for interstellar movement and excaved to house fifty squadrons. It has very weak weapons in itself and need squadrons to make any significant damage to enemy ships. It has both extremely heavy planetary bombardment capacity and troops. Are invisible for enemies unless in the same system.");
		tempsst.setArmor(50,30);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(50);
        tempsst.setBombardment(8);
        tempsst.setPsychWarfare(1);
        tempsst.setVisibleOnMap(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        tmpVipType = new VIPType("Bug Queen","BQ",arachnid,uniqueVIPIdCounter);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setFTLbonus(true);
        tmpVipType.setSpying(true);
        tmpVipType.setCounterEspionage(50);
        tmpVipType.setTechBonus(10);
        tmpVipType.setInfestate(true);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        govType = tmpVipType;
        
        // Buildings
        // *********
        tempBuildings = new Buildings();
        
//        tmpBuildingType = new BuildingType("Bug Space Station", "BSS", 10, uBIC); // cost 10 + 0
//        tmpBuildingType.setClosedPlanetBonus(2);
//        tmpBuildingType.setTechBonus(10);
//        tmpBuildingType.setSpaceport(true);
//        tmpBuildingType.setPlanetUnique(true);
//        tmpBuildingType.setAutoDestructWhenConquered(true);
//        tempBuildings.addBuilding(tmpBuildingType);        

        tmpBuildingType = new BuildingType("Bug Deep Caves", "BDC", 10, uBIC); // cost 10
        tmpBuildingType.setResistanceBonus(2);
        tmpBuildingType.setShieldCapacity(1);
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setAutoDestructWhenConquered(true);
        tmpBuildingType.setVisibleOnMap(false);
        tmpBuildingType.setSelfDestructable(false);
        tempBuildings.addBuilding(tmpBuildingType);        

        setWharfs(tempBuildings,uBIC,"Bug","B",false,5,10,15,25);
        setSpaceStations(tempBuildings,uBIC,"Bug","B",-2,2,1);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Bug Small Orbital Wharf"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Bug Deep Caves"));
        
        tempFaction.setBuildings(tempBuildings);

        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Bug Small Meteorite"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Bug Light Bomber"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Bug Attacker"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Bug Attacker"));

        tempFaction.setDescription("The bugs are aliens, or insectoids, resembling somewhat to man-sized ants or termites. They are governed by a hive mind and have several specialized breeds, included some that have psionic powers. The bugs have a very primitive tech level and cannot build conventional spaceships except squadrons, but instead rely on retrofitting meteorites as larger vessels for interstellar travel and war.");
        tempFaction.setShortDescription("Alien faction using retrofitted meteorites carrying swarms of squadrons");
        tempFaction.setAdvantages("Resistance bonus, specialized squadrons, bombardment defence, powerful bombardment, hidden units");
        tempFaction.setDisadvantages("No combat-oriented capital ships, weak defence ships, no traders");
        tempFaction.setHistory("The bugs lived unknown to all other species for many years, until a scientific scout spaceship landed on their homeplanet. The crew was soon slaughtered by the nearest bug hive and their weapons and ship were thorouly examined, and soon replicated. The bugs did not have the necessary industrial knowledge to build spaceships of their own, but could fit engines to meteroids and use them for travel, and could build fighter squadrons for the meteroids to carry. Soon the knowledge spread to the other bug hives on the planet and they started to send their young queens to the nearby stars, and thereby started the Bug Wars, which have left no other starfaring species untouched.");
        tempFaction.setHowToPlay("Bugs use retrofitted meteorites as squadron carriers and siege ships. Their squadrons are very specialized. Be careful to use the right kind of ships against different opponents, for instance against an opponent who use huge ships and a lot of squadrons, use alot of \"Bug Heavy Bomber\" and \"Bug Fighter\" in your fleet. Use the Bug Defence Meteorite (which like all Bug ships are invisible on the map for all other players) to stop enemies from raiding your planets with small raider ships.");
        tempFaction.setResistanceBonus(1);
        tempFaction.setAlien(true);
        tempFaction.setGovernorVIPType(govType);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        Faction bugsFaction = tempFaction;
        
        // add custom diplomacy
        // ********************
        
        GameWorldDiplomacy diplomacy = gw.getDiplomacy();
        DiplomacyRelation tempDiplomacyRelation;
        
        // Bugs relation
        tempDiplomacyRelation = diplomacy.getRelation(bugsFaction, bugsFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(bugsFaction, utfFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(bugsFaction, azuriachFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(bugsFaction, irsolFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(bugsFaction, transhumanFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(bugsFaction, blaradFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(bugsFaction, mekpurrFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

//        tempDiplomacyRelation = diplomacy.getRelation(bugsFaction, avatarFaction);
//        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
//        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(bugsFaction, hissistFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(bugsFaction, rananFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(bugsFaction, klackonFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        // Klackon relations
        
        tempDiplomacyRelation = diplomacy.getRelation(klackonFaction, utfFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(klackonFaction, azuriachFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(klackonFaction, irsolFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(klackonFaction, transhumanFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(klackonFaction, blaradFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(klackonFaction, mekpurrFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

//        tempDiplomacyRelation = diplomacy.getRelation(klackonFaction, avatarFaction);
//        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
//        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(klackonFaction, hissistFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);

        tempDiplomacyRelation = diplomacy.getRelation(klackonFaction, rananFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ETERNAL_WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.ETERNAL_WAR);
        
        // Utf relations
        
        tempDiplomacyRelation = diplomacy.getRelation(utfFaction, azuriachFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.CEASE_FIRE);

        tempDiplomacyRelation = diplomacy.getRelation(utfFaction, rananFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.PEACE);

        tempDiplomacyRelation = diplomacy.getRelation(utfFaction, hissistFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.PEACE);

        // Blarad relations
        
        tempDiplomacyRelation = diplomacy.getRelation(blaradFaction, azuriachFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.PEACE);

        tempDiplomacyRelation = diplomacy.getRelation(blaradFaction, rananFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.PEACE);

        tempDiplomacyRelation = diplomacy.getRelation(blaradFaction, hissistFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.PEACE);

        // Mekpurr relations
        
        tempDiplomacyRelation = diplomacy.getRelation(mekpurrFaction, azuriachFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.PEACE);

        tempDiplomacyRelation = diplomacy.getRelation(mekpurrFaction, rananFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.CEASE_FIRE);

        tempDiplomacyRelation = diplomacy.getRelation(mekpurrFaction, hissistFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.PEACE);

//        tempDiplomacyRelation = diplomacy.getRelation(mekpurrFaction, avatarFaction);
//        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.PEACE);

        // Other relations
        
//        tempDiplomacyRelation = diplomacy.getRelation(avatarFaction, rananFaction);
//        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.CEASE_FIRE);

        tempDiplomacyRelation = diplomacy.getRelation(irsolFaction, rananFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.PEACE);

		return gw;
	}	
	
	/**
	 * Always get the crv and pcrv
	 * @param tempFaction
	 * @param gw
	 * @param milTroops
	 */
	private static void addGeneralShipTypesAndStartingTypes(Faction tempFaction, GameWorld gw, boolean milTroops){
		addGeneralShipTypesAndStartingTypes(tempFaction,gw,milTroops,true,true);
	}
	
	private static void addGeneralShipTypesAndStartingTypes(Faction tempFaction, GameWorld gw, boolean milTroops, boolean hasCrv, boolean hasPCrv){
		// add shiptypes
		// -------------
		// defence ships
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("IP Patrol Boat"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("IP Corvette"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("IP Cruiser"));
        // civilian ships
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Blockade Runner"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Small Merchant Freighter"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Medium Merchant Freighter"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Large Merchant Freighter"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Outreach Survey Ship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Mobile Resupply Base"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("VIP Transport"));
        // military ships
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Nike FTL Scout"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Patrol Boat"));
        if (hasCrv){
        	tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Patrol Corvette"));
        }
        if (hasCrv){
        	tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        }
        if (milTroops){
            tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Armed Transport"));
        }
        // add starting ships
        // ------------------
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("IP Corvette"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Small Merchant Freighter"));
    	tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troop Armed Transport"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Patrol Boat"));
        if (hasCrv){
        	tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Corvette"));
        }
/*        if (milTroops){
        	tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Troop Passenger Liner"));
        }*/
//        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Blockade Runner"));
	}

	
	private static void setWharfs(Buildings aBuildings, UniqueIdCounter uBIC, String longPrefix, String shortPrefix, int... costs){
		setWharfs(aBuildings, uBIC, longPrefix, shortPrefix, true, costs);
	}
	
	private static void setWharfs(Buildings aBuildings, UniqueIdCounter uBIC, String longPrefix, String shortPrefix, boolean visibleOnMap, int... costs){
		BuildingType tmpBuildingType = null;
		BuildingType parent = null;
		
		if (costs.length > 0){
			tmpBuildingType = new BuildingType(longPrefix + " Small Orbital Wharf", shortPrefix + "W1", costs[0], uBIC);
			tmpBuildingType.setWharfSize(1);
			tmpBuildingType.setInOrbit(true);
			tmpBuildingType.setVisibleOnMap(visibleOnMap);
			parent = tmpBuildingType;
			aBuildings.addBuilding(tmpBuildingType);
		}

		if (costs.length > 1){
			tmpBuildingType = new BuildingType(longPrefix + " Medium Orbital Wharf", shortPrefix + "W2", costs[1], uBIC);
			tmpBuildingType.setWharfSize(2);
			tmpBuildingType.setInOrbit(true);
			tmpBuildingType.setVisibleOnMap(visibleOnMap);
	        tmpBuildingType.setParentBuildingType(parent);
			parent = tmpBuildingType;
			aBuildings.addBuilding(tmpBuildingType);
		}

		if (costs.length > 2){
			tmpBuildingType = new BuildingType(longPrefix + " Large Orbital Wharf", shortPrefix + "W3", costs[2], uBIC);
			tmpBuildingType.setWharfSize(3);
			tmpBuildingType.setInOrbit(true);
			tmpBuildingType.setVisibleOnMap(visibleOnMap);
	        tmpBuildingType.setParentBuildingType(parent);
			parent = tmpBuildingType;
			aBuildings.addBuilding(tmpBuildingType);
		}

		if (costs.length > 3){
			tmpBuildingType = new BuildingType(longPrefix + " Huge Orbital Wharf", shortPrefix + "W5", costs[3], uBIC);
			tmpBuildingType.setWharfSize(5);
			tmpBuildingType.setInOrbit(true);
			tmpBuildingType.setVisibleOnMap(visibleOnMap);
	        tmpBuildingType.setParentBuildingType(parent);
			aBuildings.addBuilding(tmpBuildingType);
		}
	}

	/*
	 * Spaceport (SP-C etc)
	 * ---------
	 * in orbit
	 * may not selfdestruct
	 * visible on open planets
	 * förstörs när den erövras
	 * kostar 10/10/10
	 * A +2 open spaceport
	 * AA +3 open +1 closed +10 tech
	 * AAA +4 open +2 closed +20 tech
	 * 
	 * UTF:			som ovan
	 * Azuriach:	-1 open, +1 closed
	 * IRSOL:		+1 open, -1 closed
	 * Transhumans:	som ovan
	 * MekPurr:		som ovan
	 * Blarad:		som ovan
	 * Hiss'ist:	+1 closed
	 * Ranan:		-2 open, +2 closed
	 * Klackon:		-2 open, +2 closed, endast klass A
	 * Bugs:		-2 open, +2 closed, endast klass A
	 * 
	 * Bugs kan dessutom bygga Bug Deep Caves (BDC, sh:1, cost:10) nere på planeten 
	 * Klackons kan bygga Missile Silos Mk I-V nere på planeten istället enligt nedan
	 * 
	 */
	private static void setSpaceStations(Buildings aBuildings, UniqueIdCounter uBIC, String longPrefix, String shortPrefix, int openMod, int closedMod, int maxSize){
		BuildingType tmpBuildingType = null;
		BuildingType parent = null;
		
        tmpBuildingType = new BuildingType(longPrefix + " Spaceport Class A", shortPrefix + "SP-A", 10, uBIC);
        if (2+openMod > 0) tmpBuildingType.setOpenPlanetBonus(2 + openMod);
        if (closedMod > 0) tmpBuildingType.setClosedPlanetBonus(closedMod);
        tmpBuildingType.setSpaceport(true);
		tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setAutoDestructWhenConquered(true);
		parent = tmpBuildingType;
        aBuildings.addBuilding(tmpBuildingType);        

		if (maxSize > 1){
	        tmpBuildingType = new BuildingType(longPrefix + " Spaceport Class AA", shortPrefix + "SP-AA", 10, uBIC);
	        if (3+openMod > 0) tmpBuildingType.setOpenPlanetBonus(3 + openMod);
	        if (1+closedMod > 0) tmpBuildingType.setClosedPlanetBonus(1 + closedMod);
	        tmpBuildingType.setTechBonus(10);
	        tmpBuildingType.setSpaceport(true);
			tmpBuildingType.setInOrbit(true);
	        tmpBuildingType.setPlanetUnique(true);
	        tmpBuildingType.setAutoDestructWhenConquered(true);
	        tmpBuildingType.setParentBuildingType(parent);
			parent = tmpBuildingType;
	        aBuildings.addBuilding(tmpBuildingType);        
		}

		if (maxSize > 2){
	        tmpBuildingType = new BuildingType(longPrefix + " Spaceport Class AAA", shortPrefix + "SP-AAA", 10, uBIC);
	        if (4+openMod > 0) tmpBuildingType.setOpenPlanetBonus(4 + openMod);
	        if (2+closedMod > 0) tmpBuildingType.setClosedPlanetBonus(2 + closedMod);
	        tmpBuildingType.setTechBonus(20);
	        tmpBuildingType.setSpaceport(true);
			tmpBuildingType.setInOrbit(true);
	        tmpBuildingType.setPlanetUnique(true);
	        tmpBuildingType.setAutoDestructWhenConquered(true);
	        tmpBuildingType.setParentBuildingType(parent);
//			parent = tmpBuildingType;
	        aBuildings.addBuilding(tmpBuildingType);        
		}
	}

	/**
	 * Klackon Missile Defence (KMD1 etc)
	 * ----------
	 * may not selfdestruct
	 * not visible on open planets
	 * förstörs när den erövras
	 * kostar 5/5/5/5/5
	 * Mk I 	missile 1/50%/50
	 * Mk II 	missile 2/55%/55
	 * Mk III 	missile 3/60%/60
	 * Mk IV 	missile 4/65%/65
	 * Mk V 	missile 5/70%/70
	 */
	private static void setKlackonMissileDefence(Buildings aBuildings, UniqueIdCounter uBIC){
		BuildingType tmpBuildingType = null;
		BuildingType parent = null;		
		for (int i = 1; i < 6; i++){		
	        tmpBuildingType = new BuildingType("Klackon Missile Defence Mk " + i, "KMD-" + i, 5, uBIC);
	        tmpBuildingType.setCannonRateOfFire(i);
	        tmpBuildingType.setCannonHitChance(45 + (i*5));
	        tmpBuildingType.setCannonDamage(45 + (i*5));
			tmpBuildingType.setInOrbit(false);
	        tmpBuildingType.setPlanetUnique(true);
	        tmpBuildingType.setAutoDestructWhenConquered(true);
	        tmpBuildingType.setVisibleOnMap(false);
	        if (parent != null) tmpBuildingType.setParentBuildingType(parent);
			parent = tmpBuildingType;
	        aBuildings.addBuilding(tmpBuildingType);        
		}
	}

    // ***********
    // * Avatars *
    // ***********
//    tempFaction = new Faction("Avatars",new Color(255,100,100),neutral);
//
//    // Avatar Fighter
//    typeName = "Avatar Fighter";
//    tempsst = new SpaceshipType(typeName,"V-F",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC,SpaceshipRange.NONE,1,5,uSIC,sqdBaseSmAtt+5,sqdBaseFightSqdAtt+10);
//	tempsst.setDescription("A fighter squadron specialized and very good at attacking enemy squadrons. It can also do some minor damage to enemy capital ships. It cannot move on it's own but has to be carried inside a carrier.");
//    tempsst.setSquadron(true);
//    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
//    tempsst.setCanBlockPlanet(false);
//    gw.addShipType(tempsst);
//    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
//
//    // Avatar Troop Transport
//    typeName = "Avatar Troop Transport";
//	tempsst = new SpaceshipType(typeName,"ATrT",SpaceshipType.SIZE_SMALL,5,20,SpaceshipRange.LONG,2,8,uSIC,5,5);
//	tempsst.setDescription("Small long range military troop ship. This military troop transport can single-handedly besiege enemy planets, and can run away from enemy ships if ordered.");
//    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
//    tempsst.setPsychWarfare(1);
//    gw.addShipType(tempsst);
//    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
//
//    // Avatar Destroyer
//    typeName = "Avatar Destroyer";
//	tempsst = new SpaceshipType(typeName,"VDes",SpaceshipType.SIZE_MEDIUM,70,200,SpaceshipRange.LONG,5,18,uSIC,40,15);
//	tempsst.setDescription("Heavy medium sized long range military ship. It is most useful when attacking small or medium opponents or as support against less powerful large ships. It is too weak to do any significant damage against huge opponents.");
//	tempsst.setArmor(40);
//    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
//    tempsst.setWeaponsStrengthMedium(40);        
//    tempsst.setWeaponsMaxSalvoesMedium(4);
//    gw.addShipType(tempsst);
//    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
//
//    // Avatar Light Carrier
//    typeName = "Avatar Light Carrier";
//	tempsst = new SpaceshipType(typeName,"VLCa",SpaceshipType.SIZE_MEDIUM,50,150,SpaceshipRange.LONG,5,14,uSIC,10,15);
//	tempsst.setDescription("Medium-sized long range squadron carrier. Can carry up to 6 squadrons.");
//	tempsst.setArmor(20);
//    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
//    tempsst.setSquadronCapacity(6);
//    gw.addShipType(tempsst);
//    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
//    
//    // Avatar Cruiser
//    typeName = "Avatar Cruiser";
//	tempsst = new SpaceshipType(typeName,"VCr",SpaceshipType.SIZE_LARGE,130,400,SpaceshipRange.SHORT,10,30,uSIC,40,10);
//	tempsst.setDescription("Large-sized short-range capital ship. It is very good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It has troops, bombardment and can carry one squadron.");
//	tempsst.setArmor(60,40);
//    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
//    tempsst.setWeaponsStrengthMedium(170);
//    tempsst.setSquadronCapacity(1);
//    tempsst.setBombardment(1);
//    tempsst.setPsychWarfare(1);
//    gw.addShipType(tempsst);
//    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
//
//    // Avatar Battle Starship
//    typeName = "Avatar Battle Starship";
//	tempsst = new SpaceshipType(typeName,"VBSs",SpaceshipType.SIZE_HUGE,200,700,SpaceshipRange.SHORT,20,60,uSIC,50,15);
//	tempsst.setDescription("Huge-sized short range capital ship. Has a powerful array of weapons that make significant damage to ships of all sizes, and have powerful armor. It has troops, bombardment and can carry two squadrons. It also have a tactical center to increase combat effectiveness.");
//	tempsst.setArmor(80,60,40);
//    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
//    tempsst.setWeaponsStrengthMedium(120);
//    tempsst.setWeaponsStrengthLarge(240);
//    tempsst.setWeaponsStrengthHuge(120);
//    tempsst.setWeaponsMaxSalvoesHuge(4);
//    tempsst.setSquadronCapacity(2);
//    tempsst.setBombardment(1);
//    tempsst.setPsychWarfare(1);
//    tempsst.setIncreaseInitiative(5);
//    gw.addShipType(tempsst);
//    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
//
//    // Avatar Battle Star
//    typeName = "Avatar Battle Star";
//	tempsst = new SpaceshipType(typeName,"VBS",SpaceshipType.SIZE_HUGE,300,1000,SpaceshipRange.SHORT,25,75,uSIC,60,20);
//	tempsst.setDescription("Huge short range capital ship. Have a powerful array of weapons that make massive damage to ships of all sizes, and have very powerful armor. It has planetary bombardment, troops (all Avatar troops can be considered elite), can carry up to three squadrons and have an advanced tactical center to increase combat efficiency.");
//	tempsst.setArmor(90,70,50);
//    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
//    tempsst.setWeaponsStrengthMedium(160);
//    tempsst.setWeaponsStrengthLarge(200);
//    tempsst.setWeaponsStrengthHuge(200);
//    tempsst.setWeaponsMaxSalvoesHuge(6);
//    tempsst.setSquadronCapacity(3);
//    tempsst.setBombardment(1);
//    tempsst.setPsychWarfare(1);
//    tempsst.setIncreaseInitiative(10);
//    gw.addShipType(tempsst);
//    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
//
//    tmpVipType = new VIPType("Avatar Governor","Gov",neutral,uniqueVIPIdCounter);
//    tmpVipType.setCanVisitNeutralPlanets(true);
//    tmpVipType.setDiplomat(true);
//    tmpVipType.setGovernor(true);
//    tmpVipType.setWellGuarded(true);
//    tmpVipType.setOpenIncBonus(1);
//    tmpVipType.setResistanceBonus(2);
//    tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
//    gw.addVipType(tmpVipType);
//    govType = tmpVipType;
//
//    // Buildings
//    // *********
//    tempBuildings = new Buildings();
//    
//    tmpBuildingType = new BuildingType("Avatar Space Station", "VSS", 10, uBIC); // cost 5 + 1
//    tmpBuildingType.setOpenPlanetBonus(3);
//    tmpBuildingType.setClosedPlanetBonus(1);
//    tmpBuildingType.setTechBonus(10);
//    tmpBuildingType.setSpaceport(true);
//    tmpBuildingType.setPlanetUnique(true);
//    tmpBuildingType.setAutoDestructWhenConquered(true);
//    tempBuildings.addBuilding(tmpBuildingType);        
//    
//    setWharfs(tempBuildings,uBIC,"Avatar","V",5,10,15,25);
//    
//    tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Avatar Small Orbital Wharf"));
//    
//    tempFaction.setBuildings(tempBuildings);
//
//    tempFaction.setDescription("The Avatars are one of the two feline (cat-people) races. They are very aggressive, are powerfully built and have non-retractable claws. In man to man combat they are only beaten by the Ursoids (bear-people). They defend their planets with fierce determination.");
//    addGeneralShipTypesAndStartingTypes(tempFaction,gw,false,true,true);
//    tempFaction.setPsychWarfareBonus(1);
//    tempFaction.setResistanceBonus(1);
//    tempFaction.setGovernorVIPType(govType);
//    tempFaction.setCanReconstruct(true);
//    tempFaction.setCorruption(tmpCorruption);
//    gw.addFaction(tempFaction);
//    Faction avatarFaction = tempFaction;

}
