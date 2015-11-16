package sr.server.gameworlds;

import java.util.List;

import sr.enums.BattleGroupPosition;
import sr.enums.BlackMarketFrequency;
import sr.enums.DiplomacyLevel;
import sr.enums.SpaceshipRange;
import sr.enums.SpaceshipTargetingType;
import sr.enums.TroopTargetingType;
import sr.enums.TypeOfTroop;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.Alignment;
import sr.world.Alignments;
import sr.world.BuildingType;
import sr.world.Buildings;
import sr.world.Corruption;
import sr.world.Faction;
import sr.world.GameWorld;
import sr.world.Research;
import sr.world.ResearchAdvantage;
import sr.world.SpaceshipType;
import sr.world.TroopType;
import sr.world.UniqueIdCounter;
import sr.world.VIPType;
import sr.world.diplomacy.DiplomacyRelation;
import sr.world.diplomacy.GameWorldDiplomacy;

public class Titanium{
	
	public static GameWorld getGameWorld(){
		GameWorld gw = new GameWorld();
		
        gw.setFileName("titanium");

        gw.setFullName("Titanium");
        gw.setDescription("An advanced gameworld containing five diverse factions. \n" +
        	"Titanium features many types of research, troop units and planetary buildings. \n" +
        	"The five factions are: \n" +
        	"Orb - dynamic and flexible, relying heavily on squadrons and using different types of ground units\n" + 
        	"Lancer - traders, mixing civilian ships and the largest battleships, but weak in ground combat\n" +
        	"Cyber - cybernetically enhanced, using powerful mechs to crush enemy ground forces\n" +
        	"Ghost - secretive and defensive, using stealth and guerilla tactics\n" +
        	"Templar - pround and haughty alien race, using brainwashing of subjugate populations and high tech");

        gw.setBattleSimDefaultShips1("");
        gw.setBattleSimDefaultShips2("[4]pdf");

        gw.setCreatedDate("2006-11-27");
		gw.setChangedDate("2006-11-27");
		gw.setCreatedByUser("pabod");

		gw.setAdjustScreenedStatus(false);

		Alignments alignments = new Alignments();
		String oStr= "Orb";
		String lStr = "Lancer";
		String cStr = "Cyber";
		String gStr = "Ghost";
		String tStr = "Templar";
		String mStr = "Mercenary";
		alignments.add(oStr);
		alignments.add(lStr);
		alignments.add(cStr);
		alignments.add(gStr);
		alignments.add(tStr);
		alignments.add(mStr);
		Alignment orb = alignments.findAlignment(oStr);
		Alignment lancer = alignments.findAlignment(lStr);
		Alignment cyber = alignments.findAlignment(cStr);
		Alignment ghost = alignments.findAlignment(gStr);
		Alignment templar = alignments.findAlignment(tStr);
		Alignment mercenary = alignments.findAlignment(mStr);
		orb.setDescription("Diversity and flexibility");
		lancer.setDescription("Profit and diplomacy");
		cyber.setDescription("Machine and man in symbiosis");
		ghost.setDescription("In shadows lie safety");
		templar.setDescription("No mercy, the stronger deserve to rule");
		mercenary.setDescription("Work for the one who pays best");
		orb.addCanHaveVip(mercenary.getName());
		lancer.addCanHaveVip(mercenary.getName());
		cyber.addCanHaveVip(mercenary.getName());
		ghost.addCanHaveVip(mercenary.getName());
		templar.addCanHaveVip(mercenary.getName());
		gw.setAlignments(alignments);

		Corruption tmpCorruption = createCorruption(7);
		
		// Bombardment have 50% chance to do 1000 damage (destroying the troop) 
		//gw.setBaseBombardmentDamage(60);

        // *********
		// VIP types
        // *********
		// XXX VIP Types

		// General VIP types
        // *****************
        
		UniqueIdCounter uniqueVIPIdCounter = new UniqueIdCounter();

        VIPType tmpVipType = null;

        tmpVipType = new VIPType("Spy","Spy",mercenary,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setSpying(true);
        tmpVipType.setBuildCost(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Security Chief","Sec",mercenary,uniqueVIPIdCounter);
        tmpVipType.setCounterEspionage(60);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setExterminator(60);
        tmpVipType.setBuildCost(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Economic Master","Eco",mercenary,uniqueVIPIdCounter);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setOpenIncBonus(3);
        tmpVipType.setClosedIncBonus(1);
        tmpVipType.setBuildCost(20);
        gw.addVipType(tmpVipType);

       	tmpVipType = new VIPType("Smuggler","Smu",mercenary,uniqueVIPIdCounter);
        tmpVipType.setClosedIncBonus(3);
        tmpVipType.setBuildCost(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Commander","Com",mercenary,uniqueVIPIdCounter);
        tmpVipType.setLandBattleGroupAttackBonus(30);
        tmpVipType.setBuildCost(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("General","Gen",mercenary,uniqueVIPIdCounter);
        tmpVipType.setLandBattleGroupAttackBonus(60);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setBuildCost(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Admiral","Adm",mercenary,uniqueVIPIdCounter);
        tmpVipType.setInitBonus(10);
        tmpVipType.setBuildCost(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Ace","Ace",mercenary,uniqueVIPIdCounter);
        tmpVipType.setInitFighterSquadronBonus(10);
        tmpVipType.setBuildCost(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Assassin","Ass",mercenary,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setAssassination(50);
        tmpVipType.setBuildCost(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Engineer","Eng",mercenary,uniqueVIPIdCounter);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setShipBuildBonus(20);
        tmpVipType.setBuildingBuildBonus(20);
        tmpVipType.setBuildCost(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Scientist","Sci",mercenary,uniqueVIPIdCounter);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setTechBonus(10);
        tmpVipType.setBuildCost(20);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Diplomat","Dip",mercenary,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setBuildCost(20);
        gw.addVipType(tmpVipType);

        List<VIPType> mercenaryVIPs = Functions.deepClone(gw.getVipTypes());
        
        int buildCost1 = 20;
        int buildCost2 = 30;
        int buildCost3 = 50;
        
        // Orb VIPs
        // ********
        
        tmpVipType = new VIPType("Field Marshal","FM",mercenary,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setLandBattleGroupAttackBonus(30);
		tmpVipType.setBuildCost(buildCost2);
        gw.addVipType(tmpVipType);
        VIPType orbStart1 = tmpVipType;

        tmpVipType = new VIPType("Orb Admiral","OA",orb,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setInitBonus(10);
		tmpVipType.setBuildCost(buildCost2);
        gw.addVipType(tmpVipType);
        VIPType orbStart2 = tmpVipType;

        tmpVipType = new VIPType("Orb Scientist","OS",orb,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setTechBonus(10);
		tmpVipType.setBuildCost(buildCost1);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Orb Expert Scientist","OES",orb,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setTechBonus(20);
		tmpVipType.setBuildCost(buildCost2);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Orb Hero","OHer",orb,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setCounterEspionage(50);
        tmpVipType.setExterminator(50);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setInitBonus(10);
		tmpVipType.setBuildCost(buildCost3);
        gw.addVipType(tmpVipType);

        // Lancer VIPs
        // ***********
        
        tmpVipType = new VIPType("Lancer Economic Master","LEM",lancer,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setOpenIncBonus(4);
        tmpVipType.setClosedIncBonus(2);
		tmpVipType.setBuildCost(buildCost1);
        gw.addVipType(tmpVipType);
        VIPType lancerStart1 = tmpVipType;

        tmpVipType = new VIPType("Lancer Economic Genious","LEG",lancer,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setOpenIncBonus(5);
        tmpVipType.setClosedIncBonus(3);
		tmpVipType.setBuildCost(buildCost2);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Lancer Engineer","LE",lancer,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setShipBuildBonus(20);
        tmpVipType.setBuildingBuildBonus(20);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
		tmpVipType.setBuildCost(buildCost1);
        gw.addVipType(tmpVipType);
        VIPType lancerStart2 = tmpVipType;

        tmpVipType = new VIPType("Lancer Master Engineer","LME",lancer,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setTechBonus(10);
        tmpVipType.setShipBuildBonus(40);
        tmpVipType.setBuildingBuildBonus(40);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
		tmpVipType.setBuildCost(buildCost2);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Lancer Champion","LCha",lancer,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
        tmpVipType.setInitFighterSquadronBonus(10);
        tmpVipType.setHardToKill(true);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setSpying(true);
        tmpVipType.setCounterEspionage(70);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setExterminator(80);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
		tmpVipType.setBuildCost(buildCost3);
        gw.addVipType(tmpVipType);

        // Cyber VIPs
        // **********
        
        tmpVipType = new VIPType("Cyber Elite Commander","CG",cyber,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setLandBattleGroupAttackBonus(30);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setWellGuarded(true);
		tmpVipType.setBuildCost(buildCost1);
        gw.addVipType(tmpVipType);
        VIPType cyberStart1 = tmpVipType;

        tmpVipType = new VIPType("Cyber Field Marchal","CFM",cyber,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setLandBattleGroupAttackBonus(60);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setWellGuarded(true);
		tmpVipType.setBuildCost(buildCost2);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Cyber Ace","CA",cyber,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setInitFighterSquadronBonus(15);
		tmpVipType.setBuildCost(buildCost1);
        gw.addVipType(tmpVipType);
        VIPType cyberStart2 = tmpVipType;

        tmpVipType = new VIPType("Cyber Top Ace","CTA",cyber,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setInitFighterSquadronBonus(20);
		tmpVipType.setBuildCost(buildCost2);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Cybernetic Warrior","CWar",cyber,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_MASTER);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setInitFighterSquadronBonus(10);
        tmpVipType.setHardToKill(true);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setAssassination(70);
        tmpVipType.setExterminator(40);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
		tmpVipType.setBuildCost(buildCost3);
        gw.addVipType(tmpVipType);

        // Ghost VIPs
        // **********
        
        tmpVipType = new VIPType("Ghost Assassin","GA",ghost,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setAssassination(60);
		tmpVipType.setBuildCost(buildCost1);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Ghost Spy","GS",ghost,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setSpying(true);
		tmpVipType.setBuildCost(buildCost2);
        gw.addVipType(tmpVipType);
        VIPType ghostStart1 = tmpVipType;

        tmpVipType = new VIPType("Ghost Security Chief","GSE",ghost,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setCounterEspionage(70);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setClosedIncBonus(1);
        tmpVipType.setExterminator(70);
		tmpVipType.setBuildCost(buildCost1);
        gw.addVipType(tmpVipType);

       	tmpVipType = new VIPType("Ghost Smuggler","GSM",ghost,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setClosedIncBonus(3);
		tmpVipType.setBuildCost(buildCost2);
        gw.addVipType(tmpVipType);
        VIPType ghostStart2 = tmpVipType;

        tmpVipType = new VIPType("Ghost Fighter","GFig",ghost,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
        tmpVipType.setResistanceBonus(4);
        tmpVipType.setCounterEspionage(60);
        tmpVipType.setExterminator(60);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setSpying(true);
		tmpVipType.setBuildCost(buildCost3);
        gw.addVipType(tmpVipType);

        // Templar VIPs
        // ************        
        
        tmpVipType = new VIPType("Templar Assassin","TA",templar,uniqueVIPIdCounter);
        tmpVipType.setDescription("The Templar assassins use a form of psionic soul-walking to leave their own bodies and invade the mind of a victim in their sleep and kill him without a trace.");
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setAssassination(70);
		tmpVipType.setBuildCost(buildCost1);
        gw.addVipType(tmpVipType);
        VIPType templarStart1 = tmpVipType;

        tmpVipType = new VIPType("Templar Star Navigator","TSN",templar,uniqueVIPIdCounter);
        tmpVipType.setDescription("The Templar Star Navigators use a more powerful version of psionic soul-walking than their assassin colleagues to enable a starship to jump further inte space tahn they normally can.");
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setFTLbonus(true);
		tmpVipType.setBuildCost(buildCost2);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Templar Counter-Spy","TCS",templar,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setCounterEspionage(70);
        tmpVipType.setResistanceBonus(2);
		tmpVipType.setBuildCost(buildCost1);
        gw.addVipType(tmpVipType);
        VIPType templarStart2 = tmpVipType;

        tmpVipType = new VIPType("Templar Infestator","TI",templar,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setInfestate(true);
        tmpVipType.setSpying(true);
		tmpVipType.setBuildCost(buildCost2);
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Templar Knight","TKni",templar,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setDuellistSkill(VIPType.DUELLIST_MASTER);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setAssassination(70);
        tmpVipType.setInfestate(true);
        tmpVipType.setFTLbonus(true);
		tmpVipType.setBuildCost(buildCost3);
        gw.addVipType(tmpVipType);
        
        // ******
        // Troops
        // ******
        // XXX TroopTypes

        // Basic troop units avaliable to Orb, Lancer & Ghost (and nuetral planets)
        // ***********************************************************************
        
        String typeName = null;

		UniqueIdCounter uTIC = new UniqueIdCounter();
        
        TroopType tt = new TroopType("Militia","Mil",100,2,2,uTIC,15,10);
        tt.setDescription("Cheap defensive infantry unit. Cannot travel in spaceships.");
        tt.setShortDescription("Cheap defensive infantry");
        tt.setAdvantages("Cheap");
        tt.setDisadvantages("Cannot travel in spaceships");
        tt.setSpaceshipTravel(false);
        gw.addTroopType(tt);
        
        gw.setNeutralTroopType(tt);
        
        tt = new TroopType("Militia Artillery","MilA",50,2,3,uTIC,5,5);
        tt.setDescription("Light cheap artillery unit.");
        tt.setShortDescription("Light cheap artillery");
        tt.setAdvantages("Artillery attack");
        tt.setDisadvantages("None");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(25);
        tt.setTargetingType(TroopTargetingType.ALLROUND);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        tt.setSpaceshipTravel(false);
        tt.setCanBuild(false);
        gw.addTroopType(tt);

        tt = new TroopType("Infantry","Inf",120,2,4,uTIC,20,10);
        tt.setDescription("Basic infantry unit.");
        tt.setShortDescription("Basic infantry");
        tt.setAdvantages("None");
        tt.setDisadvantages("None");
        gw.addTroopType(tt);

        typeName = "Scout Infantry";
        tt = new TroopType(typeName,"SInf",100,2,5,uTIC,25,10);
        tt.setTargetingType(TroopTargetingType.ALLROUND);
        tt.setDescription("Fast moving infantry flanking unit.");
        tt.setShortDescription("Flanking infantry");
        tt.setAdvantages("Can flank");
        tt.setDisadvantages("Weaker than normal infantry");
  //      tt.setAttackScreened(true);
        tt.setDefaultPosition(BattleGroupPosition.FLANKER);
        tt.setCanBuild(false);
        gw.addTroopType(tt);

        tt = new TroopType("Light Artillery","LArt",60,2,4,uTIC,5,15);
        tt.setDescription("Light artillery unit. Have a decent attack against armored opponents.");
        tt.setShortDescription("Light artillery");
        tt.setAdvantages("Can attack armored units.");
        tt.setDisadvantages("None");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(30);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        tt.setCanBuild(false);
        gw.addTroopType(tt);

        typeName = "Light Tanks";
        tt = new TroopType(typeName,"LT",120,2,6,uTIC,30,20);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        tt.setDescription("Light Tank unit.");
        tt.setShortDescription("Light Tanks");
        tt.setAdvantages("Good versus infantry units");
        tt.setDisadvantages("None");
        tt.setCanBuild(false);
        gw.addTroopType(tt);

        // Orb troop units
        // ***************
 /* TODO       
        tt = new TroopType("Orb Anti-Air Light Armor","OAA",100,2,6,uTIC,30,10);
        tt.setDescription("Heavy artillery unit.");
        tt.setShortDescription("Anti-Air armor");
        tt.setAdvantages("Heavy AA attack and good damage against infantry.");
        tt.setDisadvantages("Weak attack against armor.");
        tt.setAttackAntiAir(30);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
*/
        tt = new TroopType("Orb Heavy Artillery","OHA",40,2,8,uTIC,5,5);
        tt.setDescription("Heavy artillery unit.");
        tt.setShortDescription("Heavy artillery");
        tt.setAdvantages("Heavy artillery damage.");
        tt.setDisadvantages("Useless in close combat");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(50);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        
        typeName = "Orb Heavy Tanks";
        tt = new TroopType(typeName,"OHT",200,2,10,uTIC,30,40);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setDescription("Heavy Tank unit.");
        tt.setShortDescription("Heavy Tanks");
        tt.setAdvantages("Good vs infantry, very good vs Tanks");
        tt.setDisadvantages("None");
        tt.setCanBuild(false);
        gw.addTroopType(tt);

        // Cyber troop units
        // *****************
        
        typeName = "Cyber Mechs";
        tt = new TroopType(typeName,"CM",200,2,6,uTIC,30,30);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ALLROUND);
        tt.setDescription("Cyber mech unit, consisting of 30' tall mechs piloted by cybernetically integrated pilots.");
        tt.setShortDescription("Mech Lance");
        tt.setAdvantages("Good allround mech unit");
        tt.setDisadvantages("None");
        gw.addTroopType(tt);

        typeName = "Cyber Scout Mechs";
        tt = new TroopType(typeName,"CSM",200,2,8,uTIC,25,25);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ALLROUND);
        tt.setDescription("Fast-moving Cyber mech unit, consisting of 25' tall mechs piloted by cybernetically integrated pilots.");
        tt.setShortDescription("Scout Mech Lance");
        tt.setAdvantages("Fast allround mech unit");
        tt.setDisadvantages("None");
        tt.setCanBuild(false);
  //      tt.setAttackScreened(true);
        tt.setDefaultPosition(BattleGroupPosition.FLANKER);
        gw.addTroopType(tt);

        typeName = "Cyber Antitank Mechs";
        tt = new TroopType(typeName,"CAtM",250,2,8,uTIC,20,50);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setDescription("Cyber mech unit, consisting of 30' tall mechs piloted by cybernetically integrated pilots. Specialized in heavy antitank weapons.");
        tt.setShortDescription("Antitank mech Lance");
        tt.setAdvantages("Good antitank weapons");
        tt.setDisadvantages("None");
        tt.setCanBuild(false);
        gw.addTroopType(tt);
/*
        typeName = "Cyber Anti-air Mechs";
        tt = new TroopType(typeName,"CAaM",200,2,8,uTIC,50,15);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        tt.setDescription("Cyber mech unit, consisting of 30' tall mechs piloted by cybernetically integrated pilots. Carries heavy anti-air antitank weapons, which is also very effective against infantry.");
        tt.setShortDescription("Anti-air mech lance");
        tt.setAdvantages("Good vs air and infantry");
        tt.setDisadvantages("None");
        tt.setAttackAntiAir(40);
        tt.setCanBuild(false);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        gw.addTroopType(tt);
*/
        typeName = "Cyber Artillery Mechs";
        tt = new TroopType(typeName,"CAM",200,2,8,uTIC,10,10);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ALLROUND);
        tt.setDescription("Cyber mech artillery unit, consisting of 30' tall mechs piloted by cybernetically integrated pilots.");
        tt.setShortDescription("Artillery mech lance");
        tt.setAdvantages("Powerful artillery attack");
        tt.setDisadvantages("Weak close combat attacks");
        tt.setAttackArtillery(50);
        tt.setCanBuild(false);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        gw.addTroopType(tt);

        typeName = "Cyber Heavy Mechs";
        tt = new TroopType(typeName,"CHM",300,2,10,uTIC,40,40);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ALLROUND);
        tt.setDescription("Cyber heavy mech unit, consisting of 30' tall mechs piloted by cybernetically integrated pilots.");
        tt.setShortDescription("Heavy Mech Lance");
        tt.setAdvantages("Heavy allround mech unit");
        tt.setDisadvantages("None");
        tt.setCanBuild(false);
        gw.addTroopType(tt);

        typeName = "Cyber Advanced Mechs";
        tt = new TroopType(typeName,"CAdM",250,2,8,uTIC,30,30);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ALLROUND);
        tt.setDescription("Cyber mech unit, consisting of 30' tall mechs piloted by cybernetically integrated pilots.");
        tt.setShortDescription("Mech Lance");
        tt.setAdvantages("Good allround mech unit");
        tt.setDisadvantages("None");
  //      tt.setAttackScreened(true);
        
        //TODO Paul jag tog bort denna då vi nu kör med att alla har 3 turns
        //tt.setNrAttacks(4);
        tt.setDropPenalty(0);
        tt.setCanBuild(false);
        gw.addTroopType(tt);

        // Ghost troop units
        // *****************

        tt = new TroopType("Ghost Guerilla Infantry","GGI",180,1,6,uTIC,30,15);
        tt.setDescription("Ghost guerilla infantry unit, specialized in defensive combat.");
        tt.setShortDescription("Guerilla infantry");
        tt.setAdvantages("Defensive fighting");
        tt.setDisadvantages("Can't move from the planet they are build on");
        // TODO Paul jag tog bort dessa ur spellogiken. Plussade även på DamageCapacity med 40. De blir väl typ samma sak men lättar att förstå.
        //tt.setDefensiveBonus(40);
        //tt.setDefenceArtillery(20);
        tt.setSpaceshipTravel(false);
        tt.setCanBuild(false);
        gw.addTroopType(tt);

        typeName = "Ghost Mobile Infantry";
        tt = new TroopType(typeName,"GMI",160,2,8,uTIC,40,20);
        tt.setTargetingType(TroopTargetingType.ALLROUND);
        tt.setDescription("Fast moving elite infantry flanking unit, where all soldiers have a powered armored suit, which allows them to carry a wide assortment of weaponry.");
        tt.setShortDescription("Elite infantry");
        tt.setAdvantages("Can flank");
        tt.setDisadvantages("None");
        tt.setDropPenalty(0);
   //     tt.setAttackScreened(true);
        tt.setDefaultPosition(BattleGroupPosition.FLANKER);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        
        // Templar troop units
        // *******************

        typeName = "Templar Drone Infantry";
        tt = new TroopType(typeName,"TDI",80,1,1,uTIC,15,5);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        tt.setDescription("Templar drone (brainwashed slaves) infantry unit.");
        tt.setShortDescription("Cheap drone infantry");
        tt.setAdvantages("None");
        tt.setDisadvantages("Weak vs armored units");
        gw.addTroopType(tt);

        typeName = "Templar Drone Rocket Infantry";
        tt = new TroopType(typeName,"TDRI",80,1,2,uTIC,5,15);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setDescription("Templar drone (brainwashed slaves) anti-tank infantry unit.");
        tt.setShortDescription("Cheap drone anti-tank infantry");
        tt.setAdvantages("Good attack vs armored units");
        tt.setDisadvantages("Weak vs infantry units");
        tt.setCanBuild(false);
        gw.addTroopType(tt);
/* TODO
        typeName = "Templar Drone Missile Infantry";
        tt = new TroopType(typeName,"TDMI",80,1,2,uTIC,10,5);
        tt.setTargetingType(TroopTargetingType.ALLROUND);
        tt.setDescription("Templar drone (brainwashed slaves) anti-air infantry unit.");
        tt.setShortDescription("Cheap drone anti-air infantry");
        tt.setAdvantages("Good anti-air attack");
        tt.setDisadvantages("Weak in close combat");
        tt.setAttackAntiAir(15);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
*/
        typeName = "Templar Drone Heavy Infantry";
        tt = new TroopType(typeName,"TDHI",120,2,5,uTIC,25,15);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setDescription("Templar drone (brainwashed slaves) heavy infantry unit, in which the soldier use an powered exoskeleton allowing better protection and to carry haeavier weapons.");
        tt.setShortDescription("Heavy drone infantry");
        tt.setAdvantages("Good allround infantry");
        tt.setDisadvantages("None");
        tt.setCanBuild(false);
        gw.addTroopType(tt);

        typeName = "Templar Light Armor";
        tt = new TroopType(typeName,"TLA",150,2,10,uTIC,30,30);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ALLROUND);
        tt.setDescription("Templar elite light tank unit, manned by Templars.");
        tt.setShortDescription("Elite light tanks");
        tt.setAdvantages("Good allround arnament");
        tt.setDisadvantages("None");
    //    tt.setAttackScreened(true);
        tt.setDefaultPosition(BattleGroupPosition.FLANKER);
        tt.setCanBuild(false);
        gw.addTroopType(tt);

        typeName = "Templar Heavy Armor";
        tt = new TroopType(typeName,"THA",400,3,20,uTIC,50,50);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ALLROUND);
        tt.setDescription("Super-heavy nuclear-driven armoured dreadnaughts, manned by Templars.");
        tt.setShortDescription("Super-heavy tanks");
        tt.setAdvantages("Powerful allround weaponry");
        tt.setDisadvantages("None");
        tt.setCanBuild(false);
        gw.addTroopType(tt);

        // *******************
		// * Spaceship Types *
		// *******************
        // XXX Spaceship Types

		// Basic Spaceship types (available from start)
		// ************************************************
		UniqueIdCounter uSIC = new UniqueIdCounter();
		
		SpaceshipType tempsst = null;
		int sqdBaseSh = 10;
		int sqdBaseDC = 20;
//		int sqdBugBaseDC = 15;
		int sqdBaseSmAtt = 10;
		int sqdBaseFightSqdAtt = 20;
		int sqdBaseBombSqdAtt = 10;

		// Civian ships
		// ************
		
        // Small Merchant Freighter
        typeName = "Small Merchant Freighter";
        tempsst = new SpaceshipType(typeName,"SMF",SpaceshipType.SIZE_SMALL,SpaceshipRange.LONG,0,4,uSIC);
        tempsst.setDescription("Small-sized long range civilian merchant ship. Give income bonuses on frendly and neutral open planets.");
        tempsst.setCivilian(true);
        tempsst.setIncOwnOpenBonus(3);
        tempsst.setIncFrendlyOpenBonus(3);
        tempsst.setIncNeutralOpenBonus(4);
        gw.addShipType(tempsst);

        // VIP transport
        tempsst = new SpaceshipType("VIP Transport","VT",SpaceshipType.SIZE_SMALL,SpaceshipRange.LONG,2,6,uSIC);
        tempsst.setDescription("Small long range civilian VIP transport ship. Always retreat if it encounters enemy military ships without protection, unless enemy ships have stop retreats ability.");
        tempsst.setAlwaysRetreat(true);
        gw.addShipType(tempsst);

		
        // Planetary defence ships (also used by neutral planets)
		// ******************************************************

		// Planetary defence frigate
		tempsst = new SpaceshipType("PD Frigate","PDF",SpaceshipType.SIZE_SMALL,15,50,SpaceshipRange.NONE,1,4,uSIC,20,15);
        tempsst.setDescription("Small planetary defence (PB) ship. It can't move outside the starsystem it is built in. It is about as powerful as a normal Frigate, but cheaper.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        gw.addShipType(tempsst);

		// Planetary defence destroyer
        tempsst = new SpaceshipType("PD Destroyer","PDD",SpaceshipType.SIZE_MEDIUM,30,100,SpaceshipRange.NONE,1,7,uSIC,30,10);
        tempsst.setDescription("Medium planetary defence (PB) ship. It can't move outside the starsystem it is built in. It is about as powerful as a normal Destroyer, but cheaper.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        gw.addShipType(tempsst);

		// Home fleet destroyer
        tempsst = new SpaceshipType("HF Destroyer","HFD",SpaceshipType.SIZE_MEDIUM,30,100,SpaceshipRange.NONE,0,7,uSIC,30,10);
        tempsst.setDescription("Medium planetary defence (PB) ship. The home fleet destroyer is identical to the PD destroyer, except that it is financed by the industrial base on faction home planets. It can't move outside the starsystem it is built in. It is about as powerful as a normal Destroyer, but cheaper.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);

		// Planetary defence Cruiser
        tempsst = new SpaceshipType("PD Cruiser","PDC",SpaceshipType.SIZE_LARGE,80,300,SpaceshipRange.NONE,2,10,uSIC,50,10);
        tempsst.setDescription("Large planetary defence (PB) ship. It can't move outside the starsystem it is built in. It is about as powerful as a normal Cruiser, but cheaper.");
		tempsst.setArmor(60,30);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(120);
        tempsst.setWeaponsStrengthLarge(40);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);

		tempsst = new SpaceshipType("PD Battleship","PDB",SpaceshipType.SIZE_HUGE,200,700,SpaceshipRange.NONE,4,20,uSIC,50,15);
		tempsst.setDescription("Huge planetary defence (PB) ship. It can't move outside the starsystem it is built in. It is almost as powerful as a normal battleship, and much cheaper.");
		tempsst.setArmor(80,60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(120);
        tempsst.setWeaponsStrengthLarge(240);
        tempsst.setWeaponsStrengthHuge(80);
        tempsst.setIncreaseInitiative(5);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);

        gw.setNeutralSize1(gw.getSpaceshipTypeByName("PD Frigate"));
        gw.setNeutralSize2(gw.getSpaceshipTypeByName("PD Destroyer"));
        gw.setNeutralSize3(gw.getSpaceshipTypeByName("PD Cruiser"));        

        // Buildings
        // *********        
        // XXX BuildingTypes
		UniqueIdCounter uBIC = new UniqueIdCounter();
		BuildingType tmpBuildingType = null;
		Buildings buildingsGW = new Buildings();
		Buildings bOrb = new Buildings();
		Buildings bLancer = new Buildings();
		Buildings bCyber = new Buildings();
		Buildings bGhost = new Buildings();
		Buildings bTemplar = new Buildings();
        
		// build troops - infantry
        tmpBuildingType = new BuildingType("Infantry Training Base", "Inf", 5, uBIC);
        tmpBuildingType.setDescription("Can build one infantry troop unit every turn.");
        tmpBuildingType.setTroopSize(1);
        tmpBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bGhost,bTemplar);
//        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);

		// build troops - armor, Cyber excluded
        tmpBuildingType = new BuildingType("Armor Training Base", "Arm", 5, uBIC);
        tmpBuildingType.setDescription("Can build one armoured troop unit every turn.");
        tmpBuildingType.setTroopSize(1);
        tmpBuildingType.setDeveloped(false);
        tmpBuildingType.addTypeOfTroop(TypeOfTroop.ARMORED);
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bGhost,bTemplar);

		// build troops - armor, Cyber only
        tmpBuildingType = new BuildingType("Mech Factory", "Arm", 5, uBIC);
        tmpBuildingType.setDescription("Can build one mech unit every turn.");
        tmpBuildingType.setTroopSize(1);
        tmpBuildingType.addTypeOfTroop(TypeOfTroop.ARMORED);
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bCyber);

		// build troops - support
        tmpBuildingType = new BuildingType("Artillery Training Base", "Art", 5, uBIC);
        tmpBuildingType.setDescription("Can build one artillery or support troop unit every turn.");
        tmpBuildingType.setTroopSize(1);
        tmpBuildingType.setDeveloped(false);
        tmpBuildingType.addTypeOfTroop(TypeOfTroop.SUPPORT);
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost);

        // orbital wharfs
        tmpBuildingType = new BuildingType("Small Orbital Wharf", "W1", 5, uBIC);
        tmpBuildingType.setDescription("Can build one small ship every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        tmpBuildingType.setWharfSize(1);
        tmpBuildingType.setInOrbit(true);
        BuildingType parent = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);
        
        tmpBuildingType = new BuildingType("Medium Orbital Wharf", "W2", 10, uBIC);
        tmpBuildingType.setDescription("Can build one medium or two small ship every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        tmpBuildingType.setWharfSize(2);
        tmpBuildingType.setParentBuildingType(parent);
        tmpBuildingType.setInOrbit(true);
        parent = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);
        
        tmpBuildingType = new BuildingType("Large Orbital Wharf", "W3", 20, uBIC);
        tmpBuildingType.setDescription("Can build one large ship or some smaller ships every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        tmpBuildingType.setWharfSize(3);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setParentBuildingType(parent);
        tmpBuildingType.setDeveloped(false);
        parent = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);
        
        tmpBuildingType = new BuildingType("Huge Orbital Wharf", "W5", 50, uBIC);
        tmpBuildingType.setDescription("Can build one huge ship or several smaller ships every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        tmpBuildingType.setWharfSize(5);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setParentBuildingType(parent);
        tmpBuildingType.setDeveloped(false);
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);
        
        // wharf on planet surface - small
        tmpBuildingType = new BuildingType("Small Planetary Wharf", "P1", 5, uBIC);
        tmpBuildingType.setDescription("Can build one small ship every turn.");
        tmpBuildingType.setWharfSize(1);
        tmpBuildingType.setDeveloped(false);
        parent = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);
        
        // wharf on planet surface - upgrade to medium
        tmpBuildingType = new BuildingType("Medium Planetary Wharf", "P2", 10, uBIC);
        tmpBuildingType.setDescription("Can build one medium or two small ship every turn.");
        tmpBuildingType.setWharfSize(2);
        tmpBuildingType.setParentBuildingType(parent);
        tmpBuildingType.setDeveloped(false);
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);

        // planet shields
        tmpBuildingType = new BuildingType("Small Planetary Shield", "PS1", 5, uBIC);
        tmpBuildingType.setDescription("Give some protection against enemy bombardment.");
        tmpBuildingType.setShieldCapacity(1);
        tmpBuildingType.setPlanetUnique(true);
        parent = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);
        
        tmpBuildingType = new BuildingType("Medium Planetary Shield", "PS2", 10, uBIC);
        tmpBuildingType.setDescription("Give good protection against enemy bombardment.");
        tmpBuildingType.setShieldCapacity(2);
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setDeveloped(false);
        tmpBuildingType.setParentBuildingType(parent);
//        parent = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bTemplar);

        tmpBuildingType = new BuildingType("Ghost Medium Planetary Shield", "GPS2", 5, uBIC);
        tmpBuildingType.setDescription("Give good protection against enemy bombardment, and is cheaper than the ordinary medium planetary shield.");
        tmpBuildingType.setShieldCapacity(2);
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setDeveloped(false);
        tmpBuildingType.setParentBuildingType(parent); // small planetary shield is parent
        parent = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bGhost);
        
        tmpBuildingType = new BuildingType("Ghost Large Planetary Shield", "GPS3", 5, uBIC);
        tmpBuildingType.setDescription("Give very good protection against enemy bombardment.");
        tmpBuildingType.setShieldCapacity(3);
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setDeveloped(false);
        tmpBuildingType.setParentBuildingType(parent);
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bGhost);
        
        // Defence building
        tmpBuildingType = new BuildingType("Defensive Bunkers", "DB", 5, uBIC);
        tmpBuildingType.setDescription("A system of defensive bunkers that will raise the planets resistance (+3) and aid defending troops engaged in land battle.");
        tmpBuildingType.setResistanceBonus(3);
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setAutoDestructWhenConquered(true);
        tmpBuildingType.setDeveloped(false);
        parent = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);

        // Ghost defence building
        tmpBuildingType = new BuildingType("Ghost Advanced Bunkers", "GAB", 10, uBIC);
        tmpBuildingType.setDescription("A system of advanced and hidden defensive bunkers that will raise the planets resistance (+6) and aid defending troops engaged in land battle.");
        tmpBuildingType.setResistanceBonus(6);
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setAutoDestructWhenConquered(true);
        tmpBuildingType.setDeveloped(false);
        tmpBuildingType.setParentBuildingType(parent);
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bGhost);
        
        // space stations
        tmpBuildingType = new BuildingType("Spaceport Class 1", "S1", 5, uBIC);
        tmpBuildingType.setDescription("The smallest type of spaceport. It increases both open and closed incomes. Is vulnerable to enemy ships since it is in orbit around the planet.");
        tmpBuildingType.setOpenPlanetBonus(1);
        tmpBuildingType.setClosedPlanetBonus(1);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setPlanetUnique(true);
        parent = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);

        tmpBuildingType = new BuildingType("Spaceport Class 2", "S2", 10, uBIC);
        tmpBuildingType.setDescription("The second smallest type of spaceport. It increases open incomes compared to the class 1 spaceport, and also adds defensive weapons that can destroy small hostile ships. Is vulnerable to enemy ships since it is in orbit around the planet.");
        tmpBuildingType.setOpenPlanetBonus(2);
        tmpBuildingType.setClosedPlanetBonus(1);
        tmpBuildingType.setCannonDamage(100);
        tmpBuildingType.setCannonRateOfFire(2);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setParentBuildingType(parent);
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setDeveloped(false);
        parent = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);

        tmpBuildingType = new BuildingType("Spaceport Class 3", "S3", 15, uBIC);
        tmpBuildingType.setDescription("An average size type of spaceport. It increases both open and closed incomes compared to the class 2 spaceport. But most importantly is contains a stargate which enables short range ships to travel long range to other friendly planets that also have a stargate. It is vulnerable to enemy ships since it is in orbit around the planet.");
        tmpBuildingType.setOpenPlanetBonus(3);
        tmpBuildingType.setClosedPlanetBonus(2);
        tmpBuildingType.setCannonDamage(100);
        tmpBuildingType.setCannonRateOfFire(2);
        tmpBuildingType.setSpaceport(true);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setParentBuildingType(parent);
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setDeveloped(false);
        parent = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);

        tmpBuildingType = new BuildingType("Spaceport Class 4", "S4", 20, uBIC);
        tmpBuildingType.setDescription("The second largest type of spaceport. It increases open income and add a +10 tech bonus. Is vulnerable to enemy ships since it is in orbit around the planet.");
        tmpBuildingType.setOpenPlanetBonus(4);
        tmpBuildingType.setClosedPlanetBonus(2);
        tmpBuildingType.setCannonDamage(100);
        tmpBuildingType.setCannonRateOfFire(2);
        tmpBuildingType.setSpaceport(true);
        tmpBuildingType.setTechBonus(10);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setParentBuildingType(parent);
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setDeveloped(false);
        parent = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);

        tmpBuildingType = new BuildingType("Spaceport Class 5", "S5", 25, uBIC);
        tmpBuildingType.setDescription("The largest type of spaceport. It increases both open and closed incomes, adds +10% tech bonus and upgrades the defensive weapons so that it can defend itself against hostile small and medium ships. Is vulnerable to enemy ships since it is in orbit around the planet.");
        tmpBuildingType.setOpenPlanetBonus(5);
        tmpBuildingType.setClosedPlanetBonus(3);
        tmpBuildingType.setCannonDamage(200);
        tmpBuildingType.setCannonRateOfFire(5);
        tmpBuildingType.setSpaceport(true);
        tmpBuildingType.setTechBonus(10);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setParentBuildingType(parent);
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setDeveloped(false);
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);

        // Home Planet Industrial base. Ger hög inkomst och kan aldrig byggas.
        tmpBuildingType = new BuildingType("Orbital Industries", "OI", 0, uBIC);
        tmpBuildingType.setDescription("The orbital industrial base of a home planet is constructed over several decades through organic expansion into the orbit of a major planet. Therefore it cannot be rebuilt if it is destroyed.");
        tmpBuildingType.setAutoDestructWhenConquered(true);
        tmpBuildingType.setInOrbit(true);
        tmpBuildingType.setDeveloped(false);
        tmpBuildingType.setOpenPlanetBonus(10);
        tmpBuildingType.setClosedPlanetBonus(10);
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);
        
		// VIP buildings
        String[] longNames1 = {"Orb Academy","Orb High Academy","Orb University","Orb High University","Orb Elite School"}; 
        String[] shortNames1 = {"OA","OHA","OU","OHU","OES"};
        String[] vipNames1 = {"Field Marshal","Orb Admiral","Orb Scientist","Orb Expert Scientist","Orb Hero"};
        createVIPBuildings(gw,bOrb,buildingsGW,uBIC,longNames1,shortNames1,vipNames1);

        String[] longNames2 = {"Lancer Economic School","Lancer Advanced Economic School","Lancer University","Lancer High University","Lancer Elite School"}; 
        String[] shortNames2 = {"LES","LAES","LE","LME","LCha"};
        String[] vipNames2 = {"Lancer Economic Master","Lancer Economic Genious","Lancer Engineer","Lancer Master Engineer","Lancer Champion"};
        createVIPBuildings(gw,bLancer,buildingsGW,uBIC,longNames2,shortNames2,vipNames2);

        String[] longNames3 = {"Cyber Military Academy","Cyber Advanced Military Academy","Cyber Sky High University","Cyber Advanced Sky High University","Cyber Elite School"}; 
        String[] shortNames3 = {"CMA","CAMA","CSHU","CASHU","CES"};
        String[] vipNames3 = {"Cyber Elite Commander","Cyber Field Marchal","Cyber Ace","Cyber Top Ace","Cybernetic Warrior"};
        createVIPBuildings(gw,bCyber,buildingsGW,uBIC,longNames3,shortNames3,vipNames3);

        String[] longNames4 = {"Ghost Covert Ops School","Ghost Advanced Covert Ops School","Ghost Subversion University","Ghost Advanced Subversion University","Ghost Elite School"}; 
        String[] shortNames4 = {"GCOS","GACOS","GSU","GASU","GES"};
        String[] vipNames4 = {"Ghost Spy","Ghost Assassin","Ghost Security Chief","Ghost Smuggler","Ghost Fighter"};
        createVIPBuildings(gw,bGhost,buildingsGW,uBIC,longNames4,shortNames4,vipNames4);

        String[] longNames5 = {"Temple of Reaping","Templar of Reaping and Pathfinding","Temple of Harvest","Temple of Harvest and Mind","Templar Fortress of Power"}; 
        String[] shortNames5 = {"ToR","ToRP","ToH","ToHM","TFoP"};
        String[] vipNames5 = {"Templar Assassin","Templar Star Navigator","Templar Counter-Spy","Templar Infestator","Templar Knight"};
        createVIPBuildings(gw,bTemplar,buildingsGW,uBIC,longNames5,shortNames5,vipNames5);
        
        tmpBuildingType = new BuildingType("Mercenary Liason Office", "MLO", 100, uBIC);
        tmpBuildingType.setDescription("This unique building enables the owner to hire all the different types of mercenary VIPs.");
        for (VIPType mercenaryType : mercenaryVIPs) {
        	tmpBuildingType.addBuildVIPType(gw.getVIPTypeByName(mercenaryType.getName()));
		}
        tmpBuildingType.setInOrbit(false);
        tmpBuildingType.setWorldUnique(true);
        tmpBuildingType.setDeveloped(false);
        buildingsGW.addBuilding(tmpBuildingType);
        addBuildingToFactions(tmpBuildingType,bOrb,bLancer,bCyber,bGhost,bTemplar);
        //TODO buildings i GW används inte. Här borde buildingsGW tas bort helt.
        gw.setBuildings(buildingsGW);
  
        Research tempResearch = null;
        
        // Factions
        // ********
        
        // *******
        // * Orb *
        // *******
        // XXX Orb faction
        Faction tempFaction = new Faction("Orb",Faction.getColorHexString(255,255,63),orb);
        tempFaction.setDescription("The Orb faction is a human faction. They use flexible design and tactics in both their spaceships and troops.");

        // Troop types
        // ***********

        tempFaction.addTroopType(gw.getTroopTypeByName("Militia"));        
        tempFaction.addTroopType(gw.getTroopTypeByName("Militia Artillery"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Infantry"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Scout Infantry"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Light Artillery"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Light Tanks"));

        //TODO
        //tempFaction.addTroopType(gw.getTroopTypeByName("Orb Anti-Air Light Armor"));        
        tempFaction.addTroopType(gw.getTroopTypeByName("Orb Heavy Artillery"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Orb Heavy Tanks"));

        // Building Types
        // **************
        
        tempFaction.setBuildings(bOrb);
        
        // Spaceship types
        // ***************

        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Cruiser"));

        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Small Merchant Freighter"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("VIP Transport"));

        // Orb Fighter
        typeName = "Orb Fighter";
        tempsst = new SpaceshipType(typeName,"O-F",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC,SpaceshipRange.NONE,1,3,uSIC,sqdBaseSmAtt,sqdBaseFightSqdAtt);
		tempsst.setDescription("A fighter squadron specialized in attacking enemy squadrons. It can do very little damage to enemy capital ships. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        typeName = "Orb Fighter-Bomber";
        tempsst = new SpaceshipType(typeName,"O-FB",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC + 5,SpaceshipRange.NONE,1,5,uSIC,sqdBaseSmAtt,sqdBaseFightSqdAtt-5);
		tempsst.setDescription("An all-round pod squadron which can both do some damage against squadrons or capital ships, where its two-shot medium and large torpedo salvoes can hurt larger ships. It cannot move on it's own but has to be attached to a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setWeaponsStrengthLarge(25);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        // Orb Bomber
        typeName = "Orb Bomber";
        tempsst = new SpaceshipType(typeName,"O-B",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC + 5,SpaceshipRange.NONE,1,5,uSIC,sqdBaseSmAtt,sqdBaseBombSqdAtt);
		tempsst.setDescription("Squadron specialized in attacking capital ships, especially ships larger than small where its medium and large torpedo salvoes can can do significant damage. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(50);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Orb Troop carriers
        typeName = "Orb Troop Shuttles";
        tempsst = new SpaceshipType(typeName,"O-TS",SpaceshipType.SIZE_SMALL,0,sqdBaseDC - 5,SpaceshipRange.NONE,1,4,uSIC,5,5);
		tempsst.setDescription("A troop carrying pod squadron design. It is almost useless in combat. It cannot move on it's own but has to be attached to a pod carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setTroopCapacity(1);
        tempsst.setScreened(true);
 //       tempsst.setTroopLaunchCapacity(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        // Orb Marine Strike Force
        typeName = "Orb Marine Strike Force";
        tempsst = new SpaceshipType(typeName,"O-MS",SpaceshipType.SIZE_SMALL,sqdBaseSh-5,sqdBaseDC-5,SpaceshipRange.NONE,1,4,uSIC,1,5);
		tempsst.setDescription("Squadron carrying a small marine detachment who can besiege planets and lower their resistance. Virtually useless in combat and should probably be screened. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setPsychWarfare(1);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        /*
        // Orb Ground attack
        typeName = "Orb Ground Attack";
        tempsst = new SpaceshipType(typeName,"O-GA",SpaceshipType.SIZE_SMALL,sqdBaseSh-5,sqdBaseDC-5,SpaceshipRange.NONE,1,4,uSIC,1,5);
		tempsst.setDescription("Air-to-ground attack squadron which can attack enemt troops on planet surface. Virtually useless in combat and should probably be screened. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanPerformAirToGroundAttacks(true);
        tempsst.setWeaponsAirToGround(sqdBaseAirToGroundAtt);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
        */

        // Orb bombardment
        typeName = "Orb Planetary Bombardment";
        tempsst = new SpaceshipType(typeName,"O-PB",SpaceshipType.SIZE_SMALL,sqdBaseSh-5,sqdBaseDC-5,SpaceshipRange.NONE,1,6,uSIC,1,5);
		tempsst.setDescription("Air-to-ground attack squadron which can bombard planets. Virtually useless in combat and should probably be screened. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setBombardment(2);
        tempsst.setAvailableToBuild(false);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Orb Scout
        typeName = "Orb Scout";
        tempsst = new SpaceshipType(typeName,"O-S",SpaceshipType.SIZE_SMALL,sqdBaseSh-5,sqdBaseDC-10,SpaceshipRange.NONE,1,6,uSIC,1,5);
		tempsst.setDescription("Reconnaissance squadron which can survey planets. Virtually useless in combat and should probably be screened. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setPlanetarySurvey(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Orb Heavy Bomber
        typeName = "Orb Heavy Bomber";
        tempsst = new SpaceshipType(typeName,"O-HB",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC + 5,SpaceshipRange.NONE,1,7,uSIC,sqdBaseSmAtt-5,sqdBaseBombSqdAtt-5);
		tempsst.setDescription("Squadron specialized in attacking large and huge capital ships, where its large and huge torpedo salvoes can can do significant damage. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setWeaponsStrengthHuge(50);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Orb Advanced Fighter
        typeName = "Orb Advanced Fighter";
        tempsst = new SpaceshipType(typeName,"O-AF",SpaceshipType.SIZE_SMALL,sqdBaseSh+5,sqdBaseDC+5,SpaceshipRange.NONE,1,8,uSIC,sqdBaseSmAtt,sqdBaseFightSqdAtt+5);
		tempsst.setDescription("An advanced fighter squadron which can also do some damage against medium-sized capital ships. It cannot move on it's own but has to be attached to a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        typeName = "Orb Tripod";
		tempsst = new SpaceshipType(typeName,"O3P",SpaceshipType.SIZE_SMALL,0,40,SpaceshipRange.LONG,1,5,uSIC,5,5);
		tempsst.setDescription("Small-sized long range squadron-carrying ship. It can carry 3 squadrons. Weak in combat, it should probably be screened at all times.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(3);
        tempsst.setCanBlockPlanet(false);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        typeName = "Orb Hexapod";
		tempsst = new SpaceshipType(typeName,"O6P",SpaceshipType.SIZE_MEDIUM,0,100,SpaceshipRange.LONG,2,8,uSIC,5,5);
		tempsst.setDescription("Medium-sized long range squadron-carrying ship. It can carry 6 squadrons. Weak in combat, it should probably be screened at all times.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(6);
        tempsst.setCanBlockPlanet(false);
        tempsst.setAvailableToBuild(false);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        typeName = "Orb Pendecapod";
		tempsst = new SpaceshipType(typeName,"O15P",SpaceshipType.SIZE_LARGE,0,200,SpaceshipRange.LONG,3,15,uSIC,10,10);
		tempsst.setDescription("Large-sized long range squadron-carrying ship. It can carry 15 squadrons. Weak in combat, it should probably be screened at all times.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(15);
        tempsst.setCanBlockPlanet(false);
        tempsst.setAvailableToBuild(false);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        typeName = "Orb Centipod";
		tempsst = new SpaceshipType(typeName,"OCP",SpaceshipType.SIZE_HUGE,0,400,SpaceshipRange.SHORT,1,5,uSIC,15,15);
		tempsst.setDescription("Huge-sized centipede-like long range squadron-carrying ship. It can carry 100 squadrons. Weak in combat, it should probably be screened at all times.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(100);
        tempsst.setCanBlockPlanet(false);
        tempsst.setAvailableToBuild(false);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        // Research - Orb
        // **************
        
        tempResearch = new Research();
        ResearchAdvantage aRA = null;
        
        createTTresearch(gw,tempResearch,"Militia Artillery",null);
        aRA = createTTresearch(gw,tempResearch,"Scout Infantry",null);
        aRA = createTTresearch(gw,tempResearch,"Light Artillery",aRA);
        aRA = createTTresearch(gw,tempResearch,"Light Tanks",aRA);
        //TODO
        //aRA = createTTresearch(gw,tempResearch,"Orb Anti-Air Light Armor",aRA);
        aRA = createTTresearch(gw,tempResearch,"Orb Heavy Artillery",aRA);
        aRA = createTTresearch(gw,tempResearch,"Orb Heavy Tanks",aRA);
        
        // large and huge ship sizes
        ResearchAdvantage oLargeShips = createLargeShipsResearch(tempResearch);
        ResearchAdvantage oHugeShips = createHugeShipsResearch(tempResearch,oLargeShips);;

        createSTresearch(gw,tempResearch,"PD Cruiser",oLargeShips,"Large defence shiptype: ");

        ResearchAdvantage sqd = createSTresearch(gw,tempResearch,"Orb Fighter",null,"Squadron shiptype: ");
        sqd = createSTresearch(gw,tempResearch,"Orb Bomber",sqd,"Squadron shiptype: ");
        //TODO
        //sqd = createSTresearch(gw,tempResearch,"Orb Ground Attack",sqd,"Squadron shiptype: ");
        sqd = createSTresearch(gw,tempResearch,"Orb Planetary Bombardment",sqd,"Squadron shiptype: ");
        sqd = createSTresearch(gw,tempResearch,"Orb Scout",sqd,"Squadron shiptype: ");
        sqd = createSTresearch(gw,tempResearch,"Orb Heavy Bomber",sqd,"Squadron shiptype: ");
        createSTresearch(gw,tempResearch,"Orb Advanced Fighter",sqd,"Squadron shiptype: ");
        ResearchAdvantage car = createSTresearch(gw,tempResearch,"Orb Hexapod",null,"Carrier shiptype: ");
        car = createSTresearch(gw,tempResearch,"Orb Pendecapod",car,"Carrier shiptype: ");
        car.addParent(oLargeShips);
        car = createSTresearch(gw,tempResearch,"Orb Centipod",car,"Carrier shiptype: ");
        car.addParent(oHugeShips);

        // Buildings
        createBTresearch(buildingsGW,tempResearch,"Medium Planetary Shield",null,"Upgrade planet shield building to protect against bomberdment of 2");

        ResearchAdvantage olow = createBTresearch(buildingsGW,tempResearch,"Large Orbital Wharf",oLargeShips,"Can build one large ship or some smaller ships every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        ResearchAdvantage ohow = createBTresearch(buildingsGW,tempResearch,"Huge Orbital Wharf",olow,"Can build one huge ship or several smaller ships every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        ohow.addParent(oHugeShips);
        
        ResearchAdvantage oha = createBTresearch(buildingsGW,tempResearch,"Orb High Academy",null,"");
        ResearchAdvantage ohu = createBTresearch(buildingsGW,tempResearch,"Orb High University",oha,"");
        ResearchAdvantage oes = createBTresearch(buildingsGW,tempResearch,"Orb Elite School",ohu,"");
        createBTresearch(buildingsGW,tempResearch,"Mercenary Liason Office",oes,"");

        ResearchAdvantage oarm = createBTresearch(buildingsGW,tempResearch,"Armor Training Base",null,"Enables the building of training bases which can produce armored troop units.");
        createBTresearch(buildingsGW,tempResearch,"Artillery Training Base",oarm,"Enables the building of training bases which can produce artillery and support troop units.");

        createBTresearch(buildingsGW,tempResearch,"Defensive Bunkers",null,"Enables the building of planet surface bunkers which increases a planets resistance.");

        createSpaceportResearch(buildingsGW,tempResearch,oLargeShips,oHugeShips);
        
        // Corruption Research
        createCResearch(tempResearch);
        
        // Tech research
        createTechResearch(tempResearch,null,4);
        
        // Faction bonuses
        createFactionTresearch(tempResearch,"Orb open income bonus","Increase open income on all planets to +1.",null,1,0,0);

        tempFaction.setResearch(tempResearch);

        // Orb Governor
        // ************
        
        tmpVipType = new VIPType("Orb Governor","OGov",orb,uniqueVIPIdCounter);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setOpenIncBonus(1);
        tmpVipType.setResistanceBonus(3);
        gw.addVipType(tmpVipType);
        tempFaction.setGovernorVIPType(tmpVipType);

        // starting units
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Orb Tripod"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Orb Tripod"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Orb Fighter-Bomber"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Orb Fighter-Bomber"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Orb Fighter-Bomber"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Orb Troop Shuttles"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Orb Troop Shuttles"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Orb Marine Strike Force"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Militia"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Infantry"));

        // starting buildings
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Medium Orbital Wharf"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Infantry Training Base"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Orbital Industries"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Small Planetary Shield"));
        
        // other faction abilities
        tempFaction.setCanReconstruct(true);
        tempFaction.setNrStartingRandomVIPs(1);
        tempFaction.addStartingVIPType(orbStart1);
        tempFaction.addStartingVIPType(orbStart2);
        tempFaction.setCorruption(tmpCorruption);
        Faction orbFaction = tempFaction;
        gw.addFaction(tempFaction);

        // **********
        // * Lancer *
        // **********
        // XXX Lancer faction
        tempFaction = new Faction("Lancer",Faction.getColorHexString(140,140,255),lancer);
        tempFaction.setDescription("The Lancer faction is a human faction. They believe firmly in making profits from trade and the use of diplomacy is the best way to achieve power. Especially if it is backed by some very large and very armed spaceships.");

        // Troop types
        // ***********

        tempFaction.addTroopType(gw.getTroopTypeByName("Militia"));        
        tempFaction.addTroopType(gw.getTroopTypeByName("Militia Artillery"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Infantry"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Scout Infantry"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Light Artillery"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Light Tanks"));

        // Building Types
        // **************
        
        tempFaction.setBuildings(bLancer);
        
        // Lancer Spaceship Types
        // **********************

        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Cruiser"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Battleship"));        

        // Lancer Scout
        typeName = "Lancer Scout";
        tempsst = new SpaceshipType(typeName,"LSc",SpaceshipType.SIZE_SMALL,5,10,SpaceshipRange.LONG,1,2,uSIC,5,5);
        tempsst.setDescription("Small-sized long range military scout ship. Very small military ship useful for scouting missions and to carry VIPs. Also useful for commerce raiding. It is virtually useless in combat. Cannot besiege/block planets.");
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

		// Lancer Frigate
        typeName = "Lancer Frigate";
		tempsst = new SpaceshipType(typeName,"LFr",SpaceshipType.SIZE_SMALL,20,60,SpaceshipRange.LONG,3,6,uSIC,20,15);
        tempsst.setDescription("Small-sized long range military ship. Useful for blocking enemny planets, scouting missions, commerce raiding and to carry VIPs. It can be used in combat against small enemies and maybe support against medium enemy ships, but against larger ships it is virtually useless.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        // Lancer Destroyer
        typeName = "Lancer Destroyer";
		tempsst = new SpaceshipType(typeName,"LDe",SpaceshipType.SIZE_MEDIUM,50,150,SpaceshipRange.LONG,5,15,uSIC,35,10);
		tempsst.setDescription("Medium-sized long range military ship. It is most useful when attacking small or medium opponents or as support against less powerful large ships. It is too weak to do any significant damage against huge opponents.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(35);        
        tempsst.setWeaponsMaxSalvoesMedium(4);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        // Lancer Cruiser
        typeName = "Lancer Cruiser";
		tempsst = new SpaceshipType(typeName,"LCr",SpaceshipType.SIZE_LARGE,120,400,SpaceshipRange.SHORT,10,30,uSIC,40,10);
		tempsst.setDescription("Large-sized short-range capital ship. It is very good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It has a marine detachment, bombardment and can carry one squadron.");
		tempsst.setArmor(60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(130);
        tempsst.setWeaponsStrengthLarge(80);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        // Lancer Q-ship Cruiser
        typeName = "Lancer Q-ship Cruiser";
		tempsst = new SpaceshipType("Lancer Q-ship Cruiser","LQCr",SpaceshipType.SIZE_LARGE,100,350,SpaceshipRange.SHORT,10,35,uSIC,40,10);
		tempsst.setDescription("Large-sized short-range disguised capital ship. The Lancer Q-ship lokks like a civilian right until it enters battle with enemy military ships. It is good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It has a marine detachment onboard.");
		tempsst.setArmor(50,30);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(100);
        tempsst.setWeaponsStrengthLarge(60);
        tempsst.setWeaponsMaxSalvoesLarge(4);
//        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setLookAsCivilian(true);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        // Lancer Battleship
        typeName = "Lancer Battleship";
		tempsst = new SpaceshipType(typeName,"LBs",SpaceshipType.SIZE_HUGE,300,1000,SpaceshipRange.SHORT,25,80,uSIC,60,30);
		tempsst.setDescription("Huge-sized short range capital ship. Have a powerful array of weapons that make massive damage to ships of all sizes, and have very powerful armor. It has planetary bombardment, It can carry up to two troops, have a marine detachment, can carry up to four squadrons and have an advanced tactical center to increase combat efficiency.");
		tempsst.setArmor(90,70,50);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(120);
        tempsst.setWeaponsStrengthLarge(200);
        tempsst.setWeaponsStrengthHuge(200);
        tempsst.setWeaponsMaxSalvoesHuge(4);
//        tempsst.setSquadronCapacity(4);
        tempsst.setTroopCapacity(2);
  //      tempsst.setTroopLaunchCapacity(1);
        tempsst.setBombardment(2);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(5);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Lancer Leviathan
        typeName = "Lancer Leviathan";
		tempsst = new SpaceshipType(typeName,"LL",SpaceshipType.SIZE_HUGE,400,1500,SpaceshipRange.SHORT,40,150,uSIC,60,30);
		tempsst.setDescription("This huge-sized short range capital ship is the most powerful capital ship in known space. It has a powerful array of weapons that make massive damage to ships of all sizes, and have very powerful armor. It can carry up to four troops, have an elite marine detachment, have powerful bombardment, carry up to four squadrons and have an advanced tactical center to increase combat efficiency.");
		tempsst.setArmor(90,75,60);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(300);
        tempsst.setWeaponsStrengthLarge(300);
        tempsst.setWeaponsStrengthHuge(300);
        tempsst.setWeaponsMaxSalvoesHuge(10);
//        tempsst.setSquadronCapacity(4);'
        tempsst.setTroopCapacity(4);
 //       tempsst.setTroopLaunchCapacity(2);
        tempsst.setBombardment(3);
        tempsst.setPsychWarfare(2);
        tempsst.setIncreaseInitiative(10);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Civilian ships
        // **************
        
        typeName = "Lancer Troop Transport";
        tempsst = new SpaceshipType(typeName,"LTT",SpaceshipType.SIZE_MEDIUM,SpaceshipRange.LONG,1,5,uSIC);
        tempsst.setDescription("Medium-sized long range civilian troop ship. It carries troop landing craft and have place for three troop units. It also carries a small marine detachment which can lower a besieged planets resistance. Note that this ship cannot besiege planets on it's own, but when in company with at least one military ship which can besiege planets, it can use it's marine detachment and troops against planets.");
        tempsst.setCivilian(true);
        tempsst.setPsychWarfare(1);
        tempsst.setTroopCapacity(3);
  //      tempsst.setTroopLaunchCapacity(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Small Merchant Freighter"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("VIP Transport"));

        // Medium Merchant Freighter
        typeName = "Medium Merchant Freighter";
        tempsst = new SpaceshipType(typeName,"MMF",SpaceshipType.SIZE_MEDIUM,SpaceshipRange.LONG,0,8,uSIC);
        tempsst.setDescription("Medium-sized long range civilian merchant ship. Give income bonuses on frendly and neutral open planets.");
        tempsst.setCivilian(true);
        tempsst.setIncOwnOpenBonus(5);
        tempsst.setIncFrendlyOpenBonus(5);
        tempsst.setIncNeutralOpenBonus(6);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        // Large Merchant Freighter
        typeName = "Large Merchant Freighter";
        tempsst = new SpaceshipType(typeName,"LMF",SpaceshipType.SIZE_LARGE,SpaceshipRange.LONG,0,12,uSIC);
        tempsst.setDescription("Large-sized long range civilian merchant ship. Give income bonuses on frendly and neutral open planets.");
        tempsst.setCivilian(true);
        tempsst.setIncOwnOpenBonus(7);
        tempsst.setIncFrendlyOpenBonus(7);
        tempsst.setIncNeutralOpenBonus(9);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);

        // Huge Merchant Freighter
        typeName = "Huge Merchant Freighter";
        tempsst = new SpaceshipType(typeName,"HMF",SpaceshipType.SIZE_HUGE,SpaceshipRange.LONG,0,16,uSIC);
        tempsst.setDescription("Huge-sized short range civilian merchant ship. Give income bonuses on frendly and neutral open planets.");
        tempsst.setCivilian(true);
        tempsst.setIncOwnOpenBonus(9);
        tempsst.setIncFrendlyOpenBonus(9);
        tempsst.setIncNeutralOpenBonus(12);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);
        
        // Research - Lancer
        // *****************
        
        tempResearch = new Research();
        aRA = null;
        
        createTTresearch(gw,tempResearch,"Militia Artillery",null);
        aRA = createTTresearch(gw,tempResearch,"Scout Infantry",null);
        aRA = createTTresearch(gw,tempResearch,"Light Artillery",aRA);
        createTTresearch(gw,tempResearch,"Light Tanks",aRA);

        // large and huge ship sizes
        ResearchAdvantage lLargeShips = createLargeShipsResearch(tempResearch);
        ResearchAdvantage lHugeShips = createHugeShipsResearch(tempResearch,lLargeShips);;

        // def ships
        ResearchAdvantage pdc = createSTresearch(gw,tempResearch,"PD Cruiser",lLargeShips,"Large defence shiptype: ");
        ResearchAdvantage lbs = createSTresearch(gw,tempResearch,"PD Battleship",lHugeShips,"Huge defence shiptype: ");
        lbs.addParent(pdc);

        // capital ships
        ResearchAdvantage cap = createSTresearch(gw,tempResearch,"Lancer Cruiser",lLargeShips);
        cap = createSTresearch(gw,tempResearch,"Lancer Q-ship Cruiser",cap);
        cap = createSTresearch(gw,tempResearch,"Lancer Battleship",cap);
        cap.addParent(lHugeShips);
        createSTresearch(gw,tempResearch,"Lancer Leviathan",cap);
        
        // civ research
        ResearchAdvantage civ = createSTresearch(gw,tempResearch,"Large Merchant Freighter",lLargeShips,"Large civilian shiptype: ");
        civ = createSTresearch(gw,tempResearch,"Huge Merchant Freighter",civ,"Huge civilian shiptype: ");
        civ.addParent(lHugeShips);

        // Buildings
        createBTresearch(buildingsGW,tempResearch,"Medium Planetary Shield",null,"Upgrade planet shield building to protect against bomberdment of 2");

        ResearchAdvantage llow = createBTresearch(buildingsGW,tempResearch,"Large Orbital Wharf",lLargeShips,"Can build one large ship or some smaller ships every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        ResearchAdvantage lhow = createBTresearch(buildingsGW,tempResearch,"Huge Orbital Wharf",llow,"Can build one huge ship or several smaller ships every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        lhow.addParent(lHugeShips);

        ResearchAdvantage ls1 = createBTresearch(buildingsGW,tempResearch,"Lancer Advanced Economic School",null,"");
        ResearchAdvantage ls2 = createBTresearch(buildingsGW,tempResearch,"Lancer High University",ls1,"");
        ResearchAdvantage ls3 = createBTresearch(buildingsGW,tempResearch,"Lancer Elite School",ls2,"");
        createBTresearch(buildingsGW,tempResearch,"Mercenary Liason Office",ls3,"");

        ResearchAdvantage larm = createBTresearch(buildingsGW,tempResearch,"Armor Training Base",null,"Enables the building of training bases which can procude armored troop units.");
        createBTresearch(buildingsGW,tempResearch,"Artillery Training Base",larm,"Enables the building of training bases which can procude artillery and support troop units.");

        createBTresearch(buildingsGW,tempResearch,"Defensive Bunkers",null,"Enables the building of planet surface bunkers which increases a planets resistance.");

        createSpaceportResearch(buildingsGW,tempResearch,lLargeShips,lHugeShips);
        
        // Corruption Research
        createCResearch(tempResearch);

        // Tech research
        createTechResearch(tempResearch,null,4);

        // Faction bonuses
        ResearchAdvantage lob = createFactionTresearch(tempResearch,"Lancer open income bonus 1","Increase open income on all planets with +2.",null,2,0,0);
        lob = createFactionTresearch(tempResearch,"Lancer open income bonus 2","Increase open income on all planets with an additional +1.",lob,1,0,0);
        lob = createFactionTresearch(tempResearch,"Lancer open income bonus 3","Increase open income on all planets with an additional +1.",lob,1,0,0);
        createFactionTresearch(tempResearch,"Lancer open income bonus 4","Increase open income on all planets with an additional +1.",lob,1,0,0);
        
        tempFaction.setResearch(tempResearch);

        // Lancer Governor
        // ***************
        
        tmpVipType = new VIPType("Lancer Governor","LGov",lancer,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(2);
        tmpVipType.setResistanceBonus(2);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        tempFaction.setGovernorVIPType(tmpVipType);

        // starting units
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Lancer Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Lancer Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Lancer Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Medium Merchant Freighter"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Lancer Troop Transport"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Militia"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Infantry"));
        
        // starting buildings
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Medium Orbital Wharf"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Infantry Training Base"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Orbital Industries"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Small Planetary Shield"));
        
        // other faction abilities
        tempFaction.setCanReconstruct(true);
        tempFaction.setOpenPlanetBonus(1);
        tempFaction.setNrStartingRandomVIPs(2);
        tempFaction.addStartingVIPType(lancerStart1);
        tempFaction.addStartingVIPType(lancerStart2);
        tempFaction.setCorruption(tmpCorruption);
        Faction lancerFaction = tempFaction;
        gw.addFaction(tempFaction);

        // *********
        // * Cyber *
        // *********
        // XXX Cyber faction
        tempFaction = new Faction("Cyber",Faction.getColorHexString(0,255,255),cyber);

        // Troop types
        // ***********

        tempFaction.addTroopType(gw.getTroopTypeByName("Cyber Mechs"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Cyber Scout Mechs"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Cyber Antitank Mechs"));
        //TODO
        //tempFaction.addTroopType(gw.getTroopTypeByName("Cyber Anti-air Mechs"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Cyber Artillery Mechs"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Cyber Heavy Mechs"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Cyber Advanced Mechs"));

        // Building Types
        // **************
        
        tempFaction.setBuildings(bCyber);
        
        // Cyber Spaceship Types
        // *********************

        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Cruiser"));        

        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Small Merchant Freighter"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("VIP Transport"));

        // Cyber Fighter
        typeName = "Cyber Fighter";
        tempsst = new SpaceshipType(typeName,"C-F",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC,SpaceshipRange.NONE,1,5,uSIC,sqdBaseSmAtt,sqdBaseFightSqdAtt+10);
		tempsst.setDescription("A fighter squadron specialized in attacking enemy squadrons. It can do very little damage to enemy capital ships, but it's cybernetically integrated pilot is superior in dogfighting. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Cyber Light Bomber
        typeName = "Cyber Light Bomber";
        tempsst = new SpaceshipType(typeName,"C-LB",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC,SpaceshipRange.NONE,1,6,uSIC,sqdBaseSmAtt,sqdBaseFightSqdAtt);
		tempsst.setDescription("Squadron specialized in attacking capital ships, especially ships larger than small where its medium torpedo salvoes can can do significant damage, and it's cybernetically integrated pilot is good at dogfighting. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(50);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Cyber Bomber
        typeName = "Cyber Bomber";
        tempsst = new SpaceshipType(typeName,"C-B",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC,SpaceshipRange.NONE,1,6,uSIC,sqdBaseSmAtt,sqdBaseBombSqdAtt);
		tempsst.setDescription("Squadron specialized in attacking capital ships, especially ships larger than medium where its large and huge torpedo salvoes can can do significant damage. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setWeaponsStrengthHuge(50);
        tempsst.setWeaponsMaxSalvoesHuge(2);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
/*
        // Cyber Ground Attack Fighter
        typeName = "Cyber Ground Attack Fighter";
        tempsst = new SpaceshipType(typeName,"C-GA",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC,SpaceshipRange.NONE,1,6,uSIC,sqdBaseSmAtt-5,sqdBaseFightSqdAtt);
		tempsst.setDescription("A ground attack fighter squadron, which can both do some damage against enemy squadrons and enemy ground troops during a ground battle. It cannot move on it's own but has to be attached to a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsAirToGround(40);
        tempsst.setCanPerformAirToGroundAttacks(true);
        tempsst.setDefaultAirToGroundAttackStatus(true);
        tempsst.setCanBlockPlanet(false);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);
*/
        // Cyber Advanced Fighter
        typeName = "Cyber Advanced Fighter";
        tempsst = new SpaceshipType(typeName,"C-AF",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC+5,SpaceshipRange.NONE,1,8,uSIC,sqdBaseSmAtt+5,sqdBaseFightSqdAtt+15);
		tempsst.setDescription("A fighter squadron specialized in attacking enemy squadrons. It can do very little damage to enemy capital ships, but it's cybernetically integrated pilot is superior in dogfighting. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Cyber Scout
        typeName = "Cyber Scout";
        tempsst = new SpaceshipType(typeName,"CSc",SpaceshipType.SIZE_SMALL,5,10,SpaceshipRange.LONG,1,4,uSIC,5,5);
        tempsst.setDescription("Small-sized long range military scout ship. Very small military ship useful for scouting missions and surveying planets and to carry VIPs. Also useful for commerce raiding. It is virtually useless in combat. Cannot besiege/block planets.");
        tempsst.setCanBlockPlanet(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
        
		// Cyber Troop Dropship
        typeName = "Cyber Troop Dropship";
		tempsst = new SpaceshipType(typeName,"CTD",SpaceshipType.SIZE_SMALL,10,50,SpaceshipRange.LONG,3,6,uSIC,5,5);
        tempsst.setDescription("Small-sized long range military ship. Useful for blocking enemny planets, scouting missions, commerce raiding and to carry VIPs. It can carry one troop and one squadron. It is very weak in combat.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(1);
        tempsst.setTroopCapacity(1);
        tempsst.setScreened(true);
   //     tempsst.setTroopLaunchCapacity(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Cyber Troop Light Carrier
        typeName = "Cyber Troop Light Carrier";
		tempsst = new SpaceshipType(typeName,"CTLC",SpaceshipType.SIZE_MEDIUM,20,120,SpaceshipRange.LONG,4,12,uSIC,5,5);
		tempsst.setDescription("Medium-sized long range military troop transport ship. It can carry 3 troops and launch one troop each turn. It also carries a small marine detachment. It is too weak to do any significant damage against anything but small opponents.");
		tempsst.setArmor(20);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setPsychWarfare(1);
        tempsst.setSquadronCapacity(2);
        tempsst.setTroopCapacity(3);
        tempsst.setScreened(true);
    //    tempsst.setTroopLaunchCapacity(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Cyber Troop Advanced Carrier
        typeName = "Cyber Troop Advanced Carrier";
		tempsst = new SpaceshipType(typeName,"CTLC",SpaceshipType.SIZE_MEDIUM,10,100,SpaceshipRange.LONG,5,15,uSIC,5,5);
		tempsst.setDescription("Medium-sized long range military troop transport ship. It can carry 3 troops and launch 3 troop each turn. It also carries a small marine detachment. It is too weak to do any significant damage against anything but small opponents.");
		tempsst.setArmor(10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setPsychWarfare(1);
        tempsst.setSquadronCapacity(3);
        tempsst.setTroopCapacity(3);
        tempsst.setScreened(true);
  //      tempsst.setTroopLaunchCapacity(3);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Cyber Cruiser
        typeName = "Cyber Cruiser";
		tempsst = new SpaceshipType(typeName,"CCr",SpaceshipType.SIZE_LARGE,120,400,SpaceshipRange.SHORT,10,30,uSIC,40,10);
		tempsst.setDescription("Large-sized short-range capital ship. It is very good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It has a small marine detachment, bombardment and can carry two squadron.");
		tempsst.setArmor(60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(150);
        tempsst.setWeaponsStrengthLarge(100);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setBombardment(1); 
        tempsst.setPsychWarfare(1);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Cyber Troop Carrier
        typeName = "Cyber Troop Carrier";
		tempsst = new SpaceshipType(typeName,"CTC",SpaceshipType.SIZE_LARGE,50,300,SpaceshipRange.SHORT,8,25,uSIC,10,10);
		tempsst.setDescription("Large-sized long-range capital ship. It is good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It can carry up to 6 troops, has a small marine detachment, bombardment and can carry one squadron.");
		tempsst.setArmor(40,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(6);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setTroopCapacity(6);
        tempsst.setScreened(true);
   //     tempsst.setTroopLaunchCapacity(2);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Cyber Battleship
        typeName = "Cyber Battleship";
		tempsst = new SpaceshipType(typeName,"CBs",SpaceshipType.SIZE_HUGE,300,1000,SpaceshipRange.SHORT,25,80,uSIC,60,30);
		tempsst.setDescription("Huge-sized short range capital ship. Have a powerful array of weapons that make massive damage to ships of all sizes, and have very powerful armor. It has planetary bombardment, a small marine detachment, can carry up to four squadrons and have an advanced tactical center to increase combat efficiency.");
		tempsst.setArmor(90,70,50);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(120);
        tempsst.setWeaponsStrengthLarge(200);
        tempsst.setWeaponsStrengthHuge(200);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setSquadronCapacity(4);
        tempsst.setBombardment(3);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(5);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Research - Cyber
        // *****************
        
        tempResearch = new Research();
        aRA = null;
        
        aRA = createTTresearch(gw,tempResearch,"Cyber Scout Mechs",null);
        aRA = createTTresearch(gw,tempResearch,"Cyber Antitank Mechs",aRA);
        //TODO
        //aRA = createTTresearch(gw,tempResearch,"Cyber Anti-air Mechs",aRA);
        aRA = createTTresearch(gw,tempResearch,"Cyber Artillery Mechs",aRA);
        aRA = createTTresearch(gw,tempResearch,"Cyber Heavy Mechs",aRA);
        createTTresearch(gw,tempResearch,"Cyber Advanced Mechs",aRA);
        
        // large and huge ship sizes
        ResearchAdvantage cLargeShips = createLargeShipsResearch(tempResearch);
        ResearchAdvantage cHugeShips = createHugeShipsResearch(tempResearch,cLargeShips);;

        // def ship
        createSTresearch(gw,tempResearch,"PD Cruiser",cLargeShips,"Large defence shiptype: ");

        // squadrons
        ResearchAdvantage csqd = createSTresearch(gw,tempResearch,"Cyber Bomber",null,"Squadron shiptype: ");
        //TODO
        //csqd = createSTresearch(gw,tempResearch,"Cyber Ground Attack Fighter",csqd,"Squadron shiptype: ");
        createSTresearch(gw,tempResearch,"Cyber Advanced Fighter",csqd,"Squadron shiptype: ");

        // capital ships
        ResearchAdvantage ccr = createSTresearch(gw,tempResearch,"Cyber Cruiser",cLargeShips);
        ResearchAdvantage ctc = createSTresearch(gw,tempResearch,"Cyber Troop Carrier",ccr);
        createSTresearch(gw,tempResearch,"Cyber Troop Advanced Carrier",ctc);
        ResearchAdvantage cbs = createSTresearch(gw,tempResearch,"Cyber Battleship",ccr);
        cbs.addParent(cHugeShips);

        // Buildings
        createBTresearch(buildingsGW,tempResearch,"Medium Planetary Shield",null,"Upgrade planet shield building to protect against bomberdment of 2");

        ResearchAdvantage clow = createBTresearch(buildingsGW,tempResearch,"Large Orbital Wharf",cLargeShips,"Can build one large ship or some smaller ships every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        ResearchAdvantage chow = createBTresearch(buildingsGW,tempResearch,"Huge Orbital Wharf",clow,"Can build one huge ship or several smaller ships every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        chow.addParent(cHugeShips);

        ResearchAdvantage cs1 = createBTresearch(buildingsGW,tempResearch,"Cyber Advanced Military Academy",null,"");
        ResearchAdvantage cs2 = createBTresearch(buildingsGW,tempResearch,"Cyber Advanced Sky High University",cs1,"");
        ResearchAdvantage cs3 = createBTresearch(buildingsGW,tempResearch,"Cyber Elite School",cs2,"");
        createBTresearch(buildingsGW,tempResearch,"Mercenary Liason Office",cs3,"");

/* Cyber can build armored training camp from beginning, and can never build any support units        
        ResearchAdvantage carm = createBTresearch(gw,tempResearch,"Armor Training Base",null,"Enables the building of training bases which can procude armored troop units.");
        createBTresearch(gw,tempResearch,"Artillery Training Base",carm,"Enables the building of training bases which can procude artillery and support troop units.");
*/
        createBTresearch(buildingsGW,tempResearch,"Defensive Bunkers",null,"Enables the building of planet surface bunkers which increases a planets resistance.");

        createSpaceportResearch(buildingsGW,tempResearch,cLargeShips,cHugeShips);
        
        // Corruption Research
        createCResearch(tempResearch);

        // Tech research
        createTechResearch(tempResearch,null,4);

        // Faction bonuses
        createFactionTresearch(tempResearch,"Cyber open income bonus","Increase open income on all planets to +1.",null,1,0,0);

        tempFaction.setResearch(tempResearch);

        // Cyber Governor
        // **************
        
        tmpVipType = new VIPType("Cyber Governor","CGov",cyber,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        tempFaction.setGovernorVIPType(tmpVipType);

        // starting units
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Cyber Light Bomber"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Cyber Light Bomber"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Cyber Light Bomber"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Cyber Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Cyber Troop Dropship"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Cyber Troop Light Carrier"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Cyber Mechs"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Cyber Mechs"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Cyber Mechs"));
        
        // starting buildings
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Medium Orbital Wharf"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Mech Factory"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Orbital Industries"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Small Planetary Shield"));
        
        // other faction abilities
        tempFaction.setCanReconstruct(true);
        tempFaction.setTechBonus(10);
        tempFaction.setNrStartingRandomVIPs(1);
        tempFaction.addStartingVIPType(cyberStart1);
        tempFaction.addStartingVIPType(cyberStart2);
        tempFaction.setCorruption(tmpCorruption);
        Faction cyberFaction = tempFaction;
        gw.addFaction(tempFaction);

        // *********
        // * Ghost *
        // *********
        // XXX Ghost faction
        tempFaction = new Faction("Ghost",Faction.getColorHexString(122,255,122),ghost);

        // Ghost Troop types
        // *****************

        tempFaction.addTroopType(gw.getTroopTypeByName("Militia"));        
        tempFaction.addTroopType(gw.getTroopTypeByName("Militia Artillery"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Infantry"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Scout Infantry"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Light Artillery"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Light Tanks"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Ghost Guerilla Infantry"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Ghost Mobile Infantry"));

        // Building Types
        // **************
        
        tempFaction.setBuildings(bGhost);
        
        // Ghost Spaceship Types
        // *********************

        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Frigate"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Destroyer"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("PD Cruiser"));        

        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Small Merchant Freighter"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("VIP Transport"));

        typeName = "Ghost Fighter-Bomber";
        tempsst = new SpaceshipType(typeName,"G-FB",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC + 5,SpaceshipRange.NONE,1,4,uSIC,sqdBaseSmAtt,sqdBaseFightSqdAtt-5);
		tempsst.setDescription("An all-round squadron which can both do some damage against squadrons and capital ships, where its two-shot medium and large torpedo salvoes can hurt larger ships. It cannot move on it's own but has to be attached to a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setWeaponsStrengthLarge(25);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setVisibleOnMap(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);
/*
        typeName = "Ghost Advanced Attacker";
        tempsst = new SpaceshipType(typeName,"G-AA",SpaceshipType.SIZE_SMALL,sqdBaseSh,sqdBaseDC + 5,SpaceshipRange.NONE,1,6,uSIC,sqdBaseSmAtt,sqdBaseFightSqdAtt);
		tempsst.setDescription("An all-round squadron which can both do some damage against squadrons and capital ships, and also strafe ground troops, where its two-shot medium and large torpedo salvoes can hurt larger ships. It cannot move on it's own but has to be attached to a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(3);
        tempsst.setCanPerformAirToGroundAttacks(true);
        tempsst.setWeaponsAirToGround(30);
        tempsst.setVisibleOnMap(false);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(tempsst);
*/
        // Ghost Scout
        typeName = "Ghost Advanced Scout";
        tempsst = new SpaceshipType(typeName,"GASc",SpaceshipType.SIZE_SMALL,5,10,SpaceshipRange.LONG,1,4,uSIC,5,5);
        tempsst.setDescription("Small-sized long range military scout ship. Very small military ship useful for scouting missions and surveying planets and to carry VIPs. Also useful for commerce raiding. It is virtually useless in combat. Cannot besiege/block planets.");
        tempsst.setVisibleOnMap(false);
        tempsst.setCanBlockPlanet(false);
        tempsst.setPlanetarySurvey(true);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
        
		// Ghost Frigate
        typeName = "Ghost Frigate";
		tempsst = new SpaceshipType(typeName,"GFr",SpaceshipType.SIZE_SMALL,20,60,SpaceshipRange.LONG,3,6,uSIC,20,15);
        tempsst.setDescription("Small-sized long range military ship. Useful for blocking enemny planets, scouting missions, commerce raiding and to carry VIPs. It can be used in combat against small enemies and maybe support against medium enemy ships, but against larger ships it is virtually useless.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setVisibleOnMap(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Ghost Destroyer
        typeName = "Ghost Destroyer";
		tempsst = new SpaceshipType(typeName,"GDe",SpaceshipType.SIZE_MEDIUM,50,150,SpaceshipRange.LONG,5,15,uSIC,35,10);
		tempsst.setDescription("Medium-sized long range military ship. It is most useful when attacking small or medium opponents or as support against less powerful large ships. It is too weak to do any significant damage against huge opponents.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(35);        
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setVisibleOnMap(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Ghost Troop Transport
        typeName = "Ghost Troop Transport";
		tempsst = new SpaceshipType(typeName,"GTT",SpaceshipType.SIZE_MEDIUM,20,120,SpaceshipRange.LONG,3,10,uSIC,10,5);
		tempsst.setDescription("Medium-sized long range military troop transport ship. It can carry 3 troops and launch one troop each turn. It is too weak to do any significant damage against anything but small opponents.");
		tempsst.setArmor(20);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setTroopCapacity(3);
        tempsst.setScreened(true);
//        tempsst.setTroopLaunchCapacity(1);
        tempsst.setVisibleOnMap(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Ghost Cruiser
        typeName = "Ghost Cruiser";
		tempsst = new SpaceshipType(typeName,"GCr",SpaceshipType.SIZE_LARGE,120,400,SpaceshipRange.SHORT,10,30,uSIC,40,10);
		tempsst.setDescription("Large-sized short-range capital ship. It is very good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It has a small marine detachment, bombardment and can carry two squadron.");
		tempsst.setArmor(60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(130);
        tempsst.setWeaponsStrengthLarge(80);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setSquadronCapacity(2);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setVisibleOnMap(false);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Ghost Light Cruiser
        typeName = "Ghost Light Cruiser";
		tempsst = new SpaceshipType(typeName,"GLCr",SpaceshipType.SIZE_LARGE,80,300,SpaceshipRange.SHORT,8,25,uSIC,40,10);
		tempsst.setDescription("Large-sized long-range capital ship. It is good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It has a small marine detachment, bombardment and can carry one squadron.");
		tempsst.setArmor(50,30);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(100);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setSquadronCapacity(1);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setVisibleOnMap(false);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Ghost Battleship
        typeName = "Ghost Battleship";
		tempsst = new SpaceshipType(typeName,"GBs",SpaceshipType.SIZE_HUGE,300,1000,SpaceshipRange.SHORT,25,80,uSIC,60,30);
		tempsst.setDescription("Huge-sized short range capital ship. Have a powerful array of weapons that make massive damage to ships of all sizes, and have very powerful armor. It has planetary bombardment, a small marine detachment, can carry up to four squadrons and have an advanced tactical center to increase combat efficiency.");
		tempsst.setArmor(90,70,50);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(120);
        tempsst.setWeaponsStrengthLarge(200);
        tempsst.setWeaponsStrengthHuge(200);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setSquadronCapacity(4);
        tempsst.setBombardment(3);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(5);
        tempsst.setVisibleOnMap(false);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Research - Ghost
        // ****************
        
        tempResearch = new Research();
        aRA = null;
        
        createTTresearch(gw,tempResearch,"Militia Artillery",null);
        aRA = createTTresearch(gw,tempResearch,"Scout Infantry",null);
        aRA = createTTresearch(gw,tempResearch,"Light Artillery",aRA);
        aRA = createTTresearch(gw,tempResearch,"Light Tanks",aRA);
        aRA = createTTresearch(gw,tempResearch,"Ghost Guerilla Infantry",aRA);
        aRA = createTTresearch(gw,tempResearch,"Ghost Mobile Infantry",aRA);
        
        // large and huge ship sizes
        ResearchAdvantage gLargeShips = createLargeShipsResearch(tempResearch);
        ResearchAdvantage gHugeShips = createHugeShipsResearch(tempResearch,lLargeShips);;

        // def ship
        createSTresearch(gw,tempResearch,"PD Cruiser",lLargeShips,"Large defence shiptype: ");

        // capital ships
        //TODO
        //createSTresearch(gw,tempResearch,"Ghost Advanced Attacker",null,"Squadron shiptype: ");
        ResearchAdvantage gcap = createSTresearch(gw,tempResearch,"Ghost Cruiser",gLargeShips);
        gcap = createSTresearch(gw,tempResearch,"Ghost Battleship",gcap);
        gcap.addParent(gHugeShips);
        gcap = createSTresearch(gw,tempResearch,"Ghost Light Cruiser",gcap);
        
        // buildings
        ResearchAdvantage gms = createBTresearch(buildingsGW,tempResearch,"Ghost Medium Planetary Shield",null,"Upgrade planet shield building to protect against bomberdment of 2");
        createBTresearch(buildingsGW,tempResearch,"Ghost Large Planetary Shield",gms,"Upgrade planet shield building to protect against bomberdment of 3");

        ResearchAdvantage spw = createBTresearch(buildingsGW,tempResearch,"Small Planetary Wharf",null,"A spaceship wharf placed on the planet surface, it cannot be destrpyed by enemy besieging ships");
        createBTresearch(buildingsGW,tempResearch,"Medium Planetary Wharf",spw,"Upgrade wharf to be able to build medium ships");

        ResearchAdvantage glow = createBTresearch(buildingsGW,tempResearch,"Large Orbital Wharf",gLargeShips,"Can build one large ship or some smaller ships every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        ResearchAdvantage ghow = createBTresearch(buildingsGW,tempResearch,"Huge Orbital Wharf",glow,"Can build one huge ship or several smaller ships every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        ghow.addParent(gHugeShips);

        ResearchAdvantage gs1 = createBTresearch(buildingsGW,tempResearch,"Ghost Advanced Covert Ops School",null,"");
        ResearchAdvantage gs2 = createBTresearch(buildingsGW,tempResearch,"Ghost Advanced Subversion University",gs1,"");
        ResearchAdvantage gs3 = createBTresearch(buildingsGW,tempResearch,"Ghost Elite School",gs2,"");
        createBTresearch(buildingsGW,tempResearch,"Mercenary Liason Office",gs3,"");

        ResearchAdvantage garm = createBTresearch(buildingsGW,tempResearch,"Armor Training Base",null,"Enables the building of training bases which can procude armored troop units.");
        createBTresearch(buildingsGW,tempResearch,"Artillery Training Base",garm,"Enables the building of training bases which can procude artillery and support troop units.");

        ResearchAdvantage dbr = createBTresearch(buildingsGW,tempResearch,"Defensive Bunkers",null,"Enables the building of planet surface bunkers which increases a planets resistance.");
        createBTresearch(buildingsGW,tempResearch,"Ghost Advanced Bunkers",dbr,"Enables the building of advanced and hidden planet surface bunkers which dramatically increases a planets resistance.");
        
        createSpaceportResearch(buildingsGW,tempResearch,gLargeShips,gHugeShips);
        
        // Corruption Research
        createCResearch(tempResearch);

        // Tech research
        createTechResearch(tempResearch,null,4);

        // Faction bonuses
        ResearchAdvantage gcb = createFactionTresearch(tempResearch,"Ghost closed income bonus 1","Increase closed income on all planets to +2.",null,0,2,0);
        gcb = createFactionTresearch(tempResearch,"Ghost closed income bonus 2","Increase closed income on all planets with an additional +1.",gcb,0,1,0);

        ResearchAdvantage grb = createFactionTresearch(tempResearch,"Ghost resistance bonus 1","Increase resistance on all newly conquered planets to +3.",null,0,0,3);
        grb = createFactionTresearch(tempResearch,"Ghost resistance bonus 2","Increase resistance on all newly conquered planets with an additional +2.",grb,0,0,2);
        createFactionTresearch(tempResearch,"Ghost resistance bonus 3","Increase resistance on all newly conquered planets with an additional +2.",grb,0,0,2);
        
        tempFaction.setResearch(tempResearch);

        // Ghost Governor
        // **************
        
        tmpVipType = new VIPType("Ghost Governor","GGov",ghost,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setSpying(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setClosedIncBonus(1);
        tmpVipType.setResistanceBonus(4);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        tempFaction.setGovernorVIPType(tmpVipType);

        // starting units
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Ghost Advanced Scout"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Ghost Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Ghost Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Ghost Troop Transport"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("HF Destroyer"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Militia"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Infantry"));
        
        // starting buildings
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Medium Orbital Wharf"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Infantry Training Base"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Orbital Industries"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Small Planetary Shield"));
        
        // other faction abilities
        tempFaction.setCanReconstruct(true);
        tempFaction.setResistanceBonus(1);
        tempFaction.setClosedPlanetBonus(1);
        tempFaction.setNrStartingRandomVIPs(1);
        tempFaction.addStartingVIPType(ghostStart1);
        tempFaction.addStartingVIPType(ghostStart2);
        tempFaction.setCorruption(tmpCorruption);
        Faction ghostFaction = tempFaction;
        gw.addFaction(tempFaction);

        // ***********
        // * Templar *
        // ***********
        // XXX Templar faction
        tempFaction = new Faction("Templar",Faction.getColorHexString(255,100,100),templar);
        
        // Troop types
        // ***********

        tempFaction.addTroopType(gw.getTroopTypeByName("Templar Drone Infantry"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Templar Drone Rocket Infantry"));
        //TODO
        //tempFaction.addTroopType(gw.getTroopTypeByName("Templar Drone Missile Infantry"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Templar Drone Heavy Infantry"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Templar Light Armor"));
        tempFaction.addTroopType(gw.getTroopTypeByName("Templar Heavy Armor"));
        
        // Building Types
        // **************
        
        tempFaction.setBuildings(bTemplar);
        
        // Ship types
        // **********
        
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Small Merchant Freighter"));
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("VIP Transport"));

        // Templar Drone Fighter
        typeName = "Templar Drone Fighter";
        tempsst = new SpaceshipType(typeName,"T-F",SpaceshipType.SIZE_SMALL,sqdBaseSh+5,sqdBaseDC-5,SpaceshipRange.NONE,1,2,uSIC,sqdBaseSmAtt-5,sqdBaseFightSqdAtt-5);
		tempsst.setDescription("A fighter squadron specialized in attacking enemy squadrons. It can do very little damage to enemy capital ships. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Templar Drone Bomber
        typeName = "Templar Drone Bomber";
        tempsst = new SpaceshipType(typeName,"T-B",SpaceshipType.SIZE_SMALL,sqdBaseSh+5,sqdBaseDC-5,SpaceshipRange.NONE,1,3,uSIC,sqdBaseSmAtt-5,sqdBaseBombSqdAtt-5);
		tempsst.setDescription("Squadron specialized in attacking capital ships, especially ships larger than small where its medium and large torpedo salvoes can can do significant damage. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setWeaponsStrengthLarge(30);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
/*
        // Templar Drone Ground attack
        typeName = "Templar Drone Ground Attack";
        tempsst = new SpaceshipType(typeName,"T-GA",SpaceshipType.SIZE_SMALL,sqdBaseSh+5,sqdBaseDC-5,SpaceshipRange.NONE,1,3,uSIC,sqdBaseSmAtt-5,sqdBaseBombSqdAtt-5);
		tempsst.setDescription("Air-to-ground attack squadron which can attack enemt troops on planet surface. Virtually useless in combat and should probably be screened. It cannot move on it's own but has to be carried inside a carrier.");
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanPerformAirToGroundAttacks(true);
        tempsst.setWeaponsAirToGround(sqdBaseAirToGroundAtt-5);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
      */  
        // Templar Light Frigate
        typeName = "Templar Light Frigate";
        tempsst = new SpaceshipType(typeName,"TLF",SpaceshipType.SIZE_SMALL,20,20,SpaceshipRange.LONG,2,4,uSIC,15,10);
        tempsst.setDescription("Small-sized long range military scout ship. Very small military ship useful for scouting missions and to carry VIPs. Also useful for commerce raiding. It is virtually useless in combat. Cannot besiege/block planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));
        
		// Templar Troop Transport
        typeName = "Templar Troop Transport";
		tempsst = new SpaceshipType(typeName,"TTT",SpaceshipType.SIZE_SMALL,20,40,SpaceshipRange.LONG,2,5,uSIC,5,5);
        tempsst.setDescription("Small-sized long range military troop ship. This troop transport can single-handedly besiege enemy planets, and can run away from enemy ships if ordered.");
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    //    tempsst.setTroopLaunchCapacity(1);
        tempsst.setTroopCapacity(2);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Templar Destroyer
        typeName = "Templar Destroyer";
		tempsst = new SpaceshipType(typeName,"TDe",SpaceshipType.SIZE_MEDIUM,50,100,SpaceshipRange.LONG,5,12,uSIC,25,10);
		tempsst.setDescription("Medium-sized long range military ship. It is most useful when attacking small or medium opponents or as support against less powerful large ships. It is too weak to do any significant damage against huge opponents. It carries a small marine detachment which lowers the resistance of besieged planets.");
		tempsst.setArmor(30);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);        
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Templar Carrier
        typeName = "Templar Carrier";
		tempsst = new SpaceshipType(typeName,"Cru",SpaceshipType.SIZE_LARGE,100,250,SpaceshipRange.SHORT,8,25,uSIC,10,10);
		tempsst.setDescription("Large-sized short-range capital carrier ship. It can carry a large number of squadrons. It is too weak to to do very much damage to nay opponents except small ones.");
		tempsst.setArmor(40);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadronCapacity(12);
        tempsst.setAvailableToBuild(false);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Templar Light Cruiser
        typeName = "Templar Light Cruiser";
		tempsst = new SpaceshipType(typeName,"TLC",SpaceshipType.SIZE_LARGE,200,200,SpaceshipRange.LONG,8,25,uSIC,30,15);
		tempsst.setDescription("Large-sized long-range capital ship. It is very good against small or medium opponents and pretty good against large ones. It is too weak to to do very much damage to huge opponents. It has a small marine detachment, bombardment and can carry one squadron.");
		tempsst.setArmor(40,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(80);
        tempsst.setBombardment(1);
        tempsst.setPsychWarfare(1);
        tempsst.setVisibleOnMap(false);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Templar Battleship
        typeName = "Templar Battleship";
		tempsst = new SpaceshipType(typeName,"TBs",SpaceshipType.SIZE_HUGE,500,500,SpaceshipRange.SHORT,20,60,uSIC,40,20);
		tempsst.setDescription("Huge-sized short range capital ship. Have a powerful array of weapons that make massive damage to ships of all sizes, and have very powerful armor. It has planetary bombardment, a small marine detachment, can carry up to four squadrons and have an advanced tactical center to increase combat efficiency.");
		tempsst.setArmor(80,60,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(100);
        tempsst.setWeaponsStrengthLarge(150);
        tempsst.setWeaponsStrengthHuge(100);
        tempsst.setSquadronCapacity(3);
        tempsst.setBombardment(3);
        tempsst.setPsychWarfare(1);
        tempsst.setIncreaseInitiative(10);
        tempsst.setNoRetreat(true);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

		// Planetary defence destroyer
        typeName = "Templar PD Destroyer";
        tempsst = new SpaceshipType(typeName,"TPDD",SpaceshipType.SIZE_MEDIUM,70,70,SpaceshipRange.NONE,1,10,uSIC,20,15);
        tempsst.setDescription("Medium planetary defence (PB) ship. It can't move outside the starsystem it is built in. It is about as powerful as a normal Destroyer, but cheaper.");
		tempsst.setArmor(30);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setNoRetreat(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

		// Home fleet destroyer
        typeName = "Templar HF Destroyer";
        tempsst = new SpaceshipType(typeName,"THFD",SpaceshipType.SIZE_MEDIUM,70,70,SpaceshipRange.NONE,0,10,uSIC,20,15);
        tempsst.setDescription("Medium planetary defence (PB) ship. The home fleet destroyer is identical to the PD destroyer, except that it is financed by the industrial base on faction home planets. It can't move outside the starsystem it is built in. It is about as powerful as a normal Destroyer, but cheaper.");
		tempsst.setArmor(30);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setAvailableToBuild(false);
        tempsst.setNoRetreat(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName(typeName));

        // Research - Templar
        // ******************
        
        tempResearch = new Research();
        aRA = null;
        
        aRA = createTTresearch(gw,tempResearch,"Templar Drone Rocket Infantry",aRA);
        //TODO
        //aRA = createTTresearch(gw,tempResearch,"Templar Drone Missile Infantry",aRA);
        aRA = createTTresearch(gw,tempResearch,"Templar Drone Heavy Infantry",aRA);
        aRA = createTTresearch(gw,tempResearch,"Templar Light Armor",aRA);
        aRA = createTTresearch(gw,tempResearch,"Templar Heavy Armor",aRA);
        
        // large and huge ship sizes
        ResearchAdvantage tLargeShips = createLargeShipsResearch(tempResearch);
        ResearchAdvantage tHugeShips = createHugeShipsResearch(tempResearch,tLargeShips);;

        // capital ships
        createSTresearch(gw,tempResearch,"Templar Drone Bomber",null,"Squadron shiptype: ");
        //TODO
        //createSTresearch(gw,tempResearch,"Templar Drone Ground Attack",tsqd,"Squadron shiptype: ");
        ResearchAdvantage tcap = createSTresearch(gw,tempResearch,"Templar Carrier",tLargeShips);
        tcap = createSTresearch(gw,tempResearch,"Templar Light Cruiser",tLargeShips);
        tcap = createSTresearch(gw,tempResearch,"Templar Battleship",tcap);
        tcap.addParent(tHugeShips);

        // Buildings
        createBTresearch(buildingsGW,tempResearch,"Medium Planetary Shield",null,"Upgrade planet shield building to protect against bomberdment of 2");

        ResearchAdvantage tlow = createBTresearch(buildingsGW,tempResearch,"Large Orbital Wharf",tLargeShips,"Can build one large ship or some smaller ships every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        ResearchAdvantage thow = createBTresearch(buildingsGW,tempResearch,"Huge Orbital Wharf",tlow,"Can build one huge ship or several smaller ships every turn. Is vulnerable to enemy ships since it is in orbit around the planet.");
        thow.addParent(tHugeShips);

        ResearchAdvantage ts1 = createBTresearch(buildingsGW,tempResearch,"Templar of Reaping and Pathfinding",null,"");
        ResearchAdvantage ts2 = createBTresearch(buildingsGW,tempResearch,"Temple of Harvest and Mind",ts1,"");
        ResearchAdvantage ts3 = createBTresearch(buildingsGW,tempResearch,"Templar Fortress of Power",ts2,"");
        createBTresearch(buildingsGW,tempResearch,"Mercenary Liason Office",ts3,"");

        createBTresearch(buildingsGW,tempResearch,"Armor Training Base",null,"Enables the building of training bases which can procude armored troop units.");

        createBTresearch(buildingsGW,tempResearch,"Defensive Bunkers",null,"Enables the building of planet surface bunkers which increases a planets resistance.");

        createSpaceportResearch(buildingsGW,tempResearch,tLargeShips,tHugeShips);
        
        // Corruption Research
        createCResearch(tempResearch);

        // Tech research
        ResearchAdvantage ttr = createTechResearch(tempResearch,null,2);
        createTechResearch(tempResearch,ttr,4);

        // Faction bonuses
        createFactionTresearch(tempResearch,"Templar closed income bonus","Increase closed income on all planets to +1.",null,0,1,0);

        tempFaction.setResearch(tempResearch);

        // Templar Governor
        // ****************
        
        tmpVipType = new VIPType("Templar Governor","TGov",templar,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setInfestate(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setClosedIncBonus(1);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        tempFaction.setGovernorVIPType(tmpVipType);
        
        // starting units
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Templar Light Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Templar Light Frigate"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Templar Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Templar Troop Transport"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Templar Troop Transport"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Templar HF Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Templar HF Destroyer"));
        tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Templar HF Destroyer"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Templar Drone Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Templar Drone Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Templar Drone Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Templar Drone Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Templar Drone Infantry"));
        
        // starting buildings
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Medium Orbital Wharf"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Infantry Training Base"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Orbital Industries"));
		tempFaction.addStartingBuildings(gw.getBuildingTypeByName("Small Planetary Shield"));
        
        // other faction abilities
        tempFaction.setResistanceBonus(1);
        tempFaction.setNrStartingRandomVIPs(1);
        tempFaction.addStartingVIPType(templarStart1);
        tempFaction.addStartingVIPType(templarStart2);
        tempFaction.setCorruption(tmpCorruption);
        tempFaction.setAlien(true);
        Faction templarFaction = tempFaction;
        gw.addFaction(tempFaction);

        // ********************
        // add custom diplomacy
        // ********************
        // XXX Diplomacy
        
        GameWorldDiplomacy diplomacy = gw.getDiplomacy();
        DiplomacyRelation tempDiplomacyRelation;
        
        // Orb-Orb relation
        tempDiplomacyRelation = diplomacy.getRelation(orbFaction, orbFaction);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.CONFEDERACY);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.PEACE);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.WAR);
        
        // Orb-Lancer relation
        tempDiplomacyRelation = diplomacy.getRelation(orbFaction, lancerFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.CONFEDERACY);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.CEASE_FIRE);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.WAR);

        // Orb-Cyber relation
        tempDiplomacyRelation = diplomacy.getRelation(orbFaction, cyberFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ALLIANCE);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.WAR);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.WAR);

        // Orb-Ghost relation
        tempDiplomacyRelation = diplomacy.getRelation(orbFaction, ghostFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.PEACE);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.WAR);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.ETERNAL_WAR);

        // Orb-Templar relation
        tempDiplomacyRelation = diplomacy.getRelation(templarFaction, lancerFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.WAR);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.WAR);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.ETERNAL_WAR);

        // Lancer-Lancer relation
        tempDiplomacyRelation = diplomacy.getRelation(lancerFaction, lancerFaction);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.CONFEDERACY);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.PEACE);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.WAR);
        
        // Lancer-Cyber relation
        tempDiplomacyRelation = diplomacy.getRelation(cyberFaction, lancerFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.CONFEDERACY);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.CEASE_FIRE);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.WAR);

        // Lancer-Ghost relation
        tempDiplomacyRelation = diplomacy.getRelation(ghostFaction, lancerFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.CONFEDERACY);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.CEASE_FIRE);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.ETERNAL_WAR);

        // Lancer-Templar relation
        tempDiplomacyRelation = diplomacy.getRelation(templarFaction, lancerFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.CEASE_FIRE);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.WAR);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.ETERNAL_WAR);

        // Cyber-Cyber relation
        tempDiplomacyRelation = diplomacy.getRelation(cyberFaction, cyberFaction);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.CONFEDERACY);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.PEACE);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.WAR);

        // Cyber-Ghost relation
        tempDiplomacyRelation = diplomacy.getRelation(ghostFaction, cyberFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.ALLIANCE);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.WAR);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.WAR);

        // Cyber-Templar relation
        tempDiplomacyRelation = diplomacy.getRelation(templarFaction, cyberFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.PEACE);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.WAR);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.ETERNAL_WAR);

        // Ghost-Ghost relation
        tempDiplomacyRelation = diplomacy.getRelation(ghostFaction, ghostFaction);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.CONFEDERACY);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.PEACE);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.WAR);

        // Ghost-Templar relation
        tempDiplomacyRelation = diplomacy.getRelation(ghostFaction, templarFaction);
        tempDiplomacyRelation.setHighestRelation(DiplomacyLevel.CONFEDERACY);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.CEASE_FIRE);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.ETERNAL_WAR);

        // Templar-Templar relation
        tempDiplomacyRelation = diplomacy.getRelation(templarFaction, templarFaction);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.CONFEDERACY);
        tempDiplomacyRelation.setStartRelation(DiplomacyLevel.PEACE);
        tempDiplomacyRelation.setLowestRelation(DiplomacyLevel.WAR);

		return gw;
	}

	private static ResearchAdvantage createTTresearch(GameWorld gw, Research aResearch, String aTTName, ResearchAdvantage parent){
		ResearchAdvantage tempResearchAdvantage = new ResearchAdvantage(aTTName,"Troop type: " + aTTName);
		tempResearchAdvantage.setTimeToResearch(2);
		tempResearchAdvantage.addTroopType(gw.getTroopTypeByName(aTTName));
		if (parent != null){
			tempResearchAdvantage.addParent(parent);
			aResearch.addAdvantage(tempResearchAdvantage);
		}else{
			aResearch.addAdvantagAsRoot(tempResearchAdvantage);
		}
		return tempResearchAdvantage;
	}

	private static ResearchAdvantage createSTresearch(GameWorld gw, Research aResearch, String aSTName, ResearchAdvantage parent){
		return createSTresearch(gw,aResearch,aSTName,parent,"Ship type: ");
	}

	private static ResearchAdvantage createSTresearch(GameWorld gw, Research aResearch, String aSTName, ResearchAdvantage parent, String textPrefix){
		ResearchAdvantage tempResearchAdvantage = new ResearchAdvantage(aSTName,textPrefix + aSTName);
		tempResearchAdvantage.setTimeToResearch(2);
		tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName(aSTName));
		if (parent != null){
			tempResearchAdvantage.addParent(parent);
			aResearch.addAdvantage(tempResearchAdvantage);
		}else{
			aResearch.addAdvantagAsRoot(tempResearchAdvantage);
		}
		return tempResearchAdvantage;
	}
	
	private static ResearchAdvantage createLargeShipsResearch(Research tempResearch){
		ResearchAdvantage tempResearchAdvantage = new ResearchAdvantage("Large ships","Allows building of large ships");
		tempResearchAdvantage.setTimeToResearch(2);
		tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
		return tempResearchAdvantage;
	}

	private static ResearchAdvantage createHugeShipsResearch(Research tempResearch, ResearchAdvantage lLargeShips){
		ResearchAdvantage tempResearchAdvantage = new ResearchAdvantage("Huge ships","Allows building of huge ships");
		tempResearchAdvantage.setTimeToResearch(2);
	    tempResearch.addAdvantage(tempResearchAdvantage);
	    tempResearchAdvantage.addParent(lLargeShips);
		return tempResearchAdvantage;
	}

	private static ResearchAdvantage createBTresearch(Buildings buildingsGW, Research aResearch, String aBTName, ResearchAdvantage parent, String textSuffix){
		ResearchAdvantage tempResearchAdvantage = new ResearchAdvantage(aBTName,"Building type: " + textSuffix);
		tempResearchAdvantage.setTimeToResearch(2);
		tempResearchAdvantage.addBuildingType(getBuildingTypeByName(buildingsGW, aBTName));
		if (parent != null){
			tempResearchAdvantage.addParent(parent);
			aResearch.addAdvantage(tempResearchAdvantage);
		}else{
			aResearch.addAdvantagAsRoot(tempResearchAdvantage);
		}
		return tempResearchAdvantage;
	}
	
    private static void createCResearch(Research tempResearch){
    	ResearchAdvantage tempResearchAdvantage = null;
    	ResearchAdvantage parent = null;
    	for (int i = 1; i <= 7; i++){
    		tempResearchAdvantage = new ResearchAdvantage("Effective Government " + i,"Lowers corruption.");
    		tempResearchAdvantage.setTimeToResearch(4);
    		Corruption tmpCorruption = createCorruption(7-i);
    		tempResearchAdvantage.setCorruption(tmpCorruption);
    		if (parent == null){
    			tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
    		}else{
    			tempResearchAdvantage.addParent(parent);
    			tempResearch.addAdvantage(tempResearchAdvantage);
    		}
			parent = tempResearchAdvantage;
    	}
    }
	
	private static void addBuildingToFactions(BuildingType aBuildingType, Buildings... buildings){
		for (Buildings aBuildings : buildings) {
			aBuildings.addBuilding(aBuildingType);
		}
	}

	/**
	 * If 7 if recieved as multiplier, max corrution will be 7*3*4=84%
	 * @param multiplier
	 * @return
	 */
	private static Corruption createCorruption(int multiplier){
		Corruption tmpCorruption = new Corruption();
		int step = 3;
		if (multiplier > 0){
			tmpCorruption.addBreakpoint(50, multiplier*step*1);
			tmpCorruption.addBreakpoint(100, multiplier*step*2);
			tmpCorruption.addBreakpoint(150, multiplier*step*3);
			tmpCorruption.addBreakpoint(200, multiplier*step*4);
		}else{
			tmpCorruption.addBreakpoint(50, 0);
		}
		return tmpCorruption;
	}

	private static ResearchAdvantage createTechResearch(Research tempResearch, ResearchAdvantage rootParent, int turns){
    	ResearchAdvantage tempResearchAdvantage = null;
    	ResearchAdvantage parent = rootParent;
    	for (int i = 1; i <= 5; i++){
    		tempResearchAdvantage = new ResearchAdvantage("Advanced technology " + i,"More advanced technology.");
    		tempResearchAdvantage.setTimeToResearch(turns);
    		int techBonus = 10*i;
    		if (rootParent != null){
    			techBonus += 50;
    		}
    		tempResearchAdvantage.setTechBonus(techBonus);
    		if (parent == null){
    			tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
    		}else{
    			tempResearchAdvantage.addParent(parent);
    			tempResearch.addAdvantage(tempResearchAdvantage);
    		}
			parent = tempResearchAdvantage;
    	}
    	return tempResearchAdvantage;
	}
	
	private static ResearchAdvantage createFactionTresearch(Research tempResearch,String rName, String rDesc, ResearchAdvantage parent, int openBonus, int closedBonus, int resBonus){
    	ResearchAdvantage tempResearchAdvantage = null;
    	tempResearchAdvantage = new ResearchAdvantage(rName,rDesc);
    	tempResearchAdvantage.setTimeToResearch(2);
    	if (openBonus > 0){
    		tempResearchAdvantage.setOpenPlanetBonus(openBonus);
    	}
    	if (closedBonus > 0){
    		tempResearchAdvantage.setClosedPlanetBonus(openBonus);
    	}
    	if (resBonus > 0){
    		tempResearchAdvantage.setResistanceBonus(resBonus);
    	}
    	if (parent == null){
    		tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
    	}else{
    		tempResearchAdvantage.addParent(parent);
    		tempResearch.addAdvantage(tempResearchAdvantage);
    	}
    	return tempResearchAdvantage;
	}

	private static void createVIPBuildings(GameWorld gw, Buildings factionBuildings, Buildings buildingsGW, UniqueIdCounter uBIC, String[] longNames, String[] shortNames, String[] vipNames){
        BuildingType tmpBuildingType = new BuildingType(longNames[0], shortNames[0], 20, uBIC);
        tmpBuildingType.setDescription("Can train " + vipNames[0] + " VIPs.");
        tmpBuildingType.addBuildVIPType(gw.getVIPTypeByName(vipNames[0]));
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setAutoDestructWhenConquered(true);
        BuildingType ob1 = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        factionBuildings.addBuilding(tmpBuildingType);
        
        tmpBuildingType = new BuildingType(longNames[1], shortNames[1], 50, uBIC);
        tmpBuildingType.addBuildVIPType(gw.getVIPTypeByName(vipNames[0]));
        tmpBuildingType.addBuildVIPType(gw.getVIPTypeByName(vipNames[1]));
        tmpBuildingType.setDescription("Can train " + vipNames[0] + " and " + vipNames[1] + " VIPs.");
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setAutoDestructWhenConquered(true);
        tmpBuildingType.setDeveloped(false);
        tmpBuildingType.setParentBuildingType(ob1);
        buildingsGW.addBuilding(tmpBuildingType);
        factionBuildings.addBuilding(tmpBuildingType);

        tmpBuildingType = new BuildingType(longNames[2], shortNames[2], 20, uBIC);
        tmpBuildingType.setDescription("Can train " + vipNames[2] + " VIPs.");
        tmpBuildingType.addBuildVIPType(gw.getVIPTypeByName(vipNames[2]));
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setAutoDestructWhenConquered(true);
        BuildingType ob3 = tmpBuildingType;
        buildingsGW.addBuilding(tmpBuildingType);
        factionBuildings.addBuilding(tmpBuildingType);

        tmpBuildingType = new BuildingType(longNames[3], shortNames[3], 50, uBIC);
    	tmpBuildingType.addBuildVIPType(gw.getVIPTypeByName(vipNames[2]));
    	tmpBuildingType.addBuildVIPType(gw.getVIPTypeByName(vipNames[3]));
        tmpBuildingType.setDescription("Can train " + vipNames[2] + " and " + vipNames[3] + " VIPs.");
        tmpBuildingType.setPlanetUnique(true);
        tmpBuildingType.setAutoDestructWhenConquered(true);
        tmpBuildingType.setDeveloped(false);
        tmpBuildingType.setParentBuildingType(ob3);
        buildingsGW.addBuilding(tmpBuildingType);
        factionBuildings.addBuilding(tmpBuildingType);

        tmpBuildingType = new BuildingType(longNames[4], shortNames[4], 100, uBIC);
		tmpBuildingType.addBuildVIPType(gw.getVIPTypeByName(vipNames[4]));
        tmpBuildingType.setDescription("Can train " + vipNames[4] + " VIPs.");
        tmpBuildingType.setPlayerUnique(true);
        tmpBuildingType.setAutoDestructWhenConquered(true);
        tmpBuildingType.setDeveloped(false);
        buildingsGW.addBuilding(tmpBuildingType);
        factionBuildings.addBuilding(tmpBuildingType);
	}
	
	private static void createSpaceportResearch(Buildings buildingsGW, Research tempResearch,ResearchAdvantage largeShips,ResearchAdvantage hugeShips){
        ResearchAdvantage s2 = createBTresearch(buildingsGW,tempResearch,"Spaceport Class 2",null,"Enables the building of Spaceport Class 2");
        ResearchAdvantage s3 = createBTresearch(buildingsGW,tempResearch,"Spaceport Class 3",s2,"Enables the building of Spaceport Class 3");
        s3.addParent(largeShips);
        ResearchAdvantage s4 = createBTresearch(buildingsGW,tempResearch,"Spaceport Class 4",s3,"Enables the building of Spaceport Class 4");
        ResearchAdvantage s5 = createBTresearch(buildingsGW,tempResearch,"Spaceport Class 5",s4,"Enables the building of Spaceport Class 5");
        s5.addParent(hugeShips);
	}
	
	private static BuildingType getBuildingTypeByName(Buildings buildingsGW, String name){
		BuildingType foundbt = buildingsGW.getBuildingType(name);
    	if (foundbt != null){
    	}else{ // om detta inträffar så finns det antagligen en felstavning av en skeppstyp i gameworlden
        	Logger.severe("Titanium.getBuildingTypeByName, btname:" + name + " -> " + foundbt);
    		Thread.dumpStack();
    	}
    	return foundbt;
	}
	
}
