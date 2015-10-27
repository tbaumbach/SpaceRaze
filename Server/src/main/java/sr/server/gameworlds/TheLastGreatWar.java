package sr.server.gameworlds;

import sr.enums.BattleGroupPosition;
import sr.enums.BlackMarketFrequency;
import sr.enums.SpaceshipRange;
import sr.enums.SpaceshipTargetingType;
import sr.enums.TroopTargetingType;
import sr.enums.TypeOfTroop;
import sr.world.Alignment;
import sr.world.Alignments;
import sr.world.BuildingType;
import sr.world.Buildings;
import sr.world.Corruption;
import sr.world.Faction;
import sr.world.GameWorld;
import sr.world.Research;
import sr.world.ResearchAdvantage;
import sr.world.ResearchUpgradeBuilding;
import sr.world.ResearchUpgradeShip;
import sr.world.SpaceshipType;
import sr.world.TroopType;
import sr.world.UniqueIdCounter;
import sr.world.VIPType;

public class TheLastGreatWar{
	
	public static GameWorld getGameWorld(){
		// XXX The Last Great War
		GameWorld gw = new GameWorld();
		

        gw.setFileName("thelastgreatwar");

        gw.setFullName("The Last Great War");
        gw.setShortDescription("The Last Great War between 7 faction from the razed earth.");
        gw.setDescription("The Last Great War between 7 faction from the razed earth. Advanced game world with troops, research and lots of different ships and buildings. This world is best playing on big maps and under long time.");
        gw.setHistory("The year 2006 USA starts developing new rockets and space ship to explore and build up a base on the moon. One of the main causes were Chinas program to get to the moon and out to the space. At the same time some company’s started to develop space shuttles to do short trip out to the space. The idea was expansive enjoyments trips for rich people. Some years after that EU decide to develop new space ships that could fly between earth and space without any help from rockets or air planes. That was not an easy thing to do and it took long time. The first ship was flaying at 2041. At the same time Russian hade build the world first space ship and started to explore the sun system. The problem Russian hade was the trip from earth to the waiting space ship. The ship was a big susses so China started develop ships for space travels and for colonize the planets in the sun system.\nAt 2072 USA, China, Russian and EU hade smaller fleets. At the same time the first civilian ships started to transport rich people between hotels on planets or space station. At 2084 same countries from Africa founded the Alliance of Africa. The main course was to build up a space fleet to defending the interest and get for resources from the space.\n\nAt 2098 the first space ship in the history was founded drifting around and short after that China and USA broke against the non weapons agreement. After that even same civilian ships was equipped with weapons. Some really big company bought in some ship to make o police force (a first step against Trade Federation).\nAt AC 2104 EU develop the first hyper driver to do long jumps in the space. It took 8 years to the first hyper jump with a space ship that returned undamaged. On of the imported things in jumping is to have a clean and known way to travel.\n2116 was the year then the first jump to a planet in another sun system was made. Soon after 2121 all ships that wanted could have a hyper drive. At this time the space was unknown and hyper jump was dangerous outside our sun system. So they that were doing these trips were poor people that wanted food on the table and start a new live. No one knows how many of this ships that was lost in the deep space, but under almost 50 years this trips was ordinary.\n\nAt around the year 2145 many planets start take contact with the old world (as they called it). Travelling between planets and sun system was made common. Around 2171 a lot of trading ships was lost in the space and even same planets were attacked. No one knows who the attacker was.\nAt 2178 some big companies started the Trade Federation to build up an armed fleet to strike back and to escort trading fleets. The old countries from the earth didn’t liked the idea of heavy armed civilian battle ships, so they started to build up military fleets to control the space and the colony und the own flags.\nAround 2230 it was a lot of pirates attack and huge misunderstandings between countries, colonies, companies and free planets. At 2240 some colonies in the outer space started a new federation name to Federation of liberty to defend they self against military fleet that occupied planets as they accused for being raiding strong holds.\n\nThe 22 Mars in 2242 the first attacked against the earth was made. No one know who or there from the first nuclear bomb was sent but all countries defending system answered with sending nuclear bombs back to the attacker. In just 5 hours 97.5% of all inherits in earth were dead and the rest hurt for the rest of the lives. It took lees then one week to destroy the rest of the sun system.\nAfter 2242 the rest of the world and old country’s started to build up the military. The most of the planets is still under self rule but the 7 factions are still fighting and need the resources from the neutral planets. Some neutral planets have build up defence to protect the self from aggressions. Who started the war that will end it? The answers will be in the future");
        
        gw.setBattleSimDefaultShips1("");
        gw.setBattleSimDefaultShips2("");

        gw.setCreatedDate("2007-05-01");
		gw.setChangedDate("2010-12-18");
		gw.setCreatedByUser("Thobias Baumbach");
		gw.setVersionId("0.10");

		Alignments alignments = new Alignments();
		String uStr= "USA";
		String cStr = "China";
		String eStr= "EU";
		String aStr= "Alliance of Africa";
		String tStr= "Trade federation";
		String rStr= "Russian";
		String fStr= "Federation of liberty";
		
		//VIP
		String nStr= "Neutral";
		String sStr= "Smuggler";
		String gStr= "Trade";
		
		alignments.add(uStr);
		alignments.add(cStr);
		alignments.add(eStr);
		alignments.add(aStr);
		alignments.add(tStr);
		alignments.add(rStr);
		alignments.add(fStr);
		
		//VIP
		alignments.add(nStr);
		alignments.add(sStr);
		alignments.add(gStr);
		
		Alignment usa = alignments.findAlignment(uStr);
		Alignment china = alignments.findAlignment(cStr);
		Alignment eu = alignments.findAlignment(eStr);
		Alignment alliance = alignments.findAlignment(aStr);
		Alignment trade = alignments.findAlignment(tStr);
		Alignment russian = alignments.findAlignment(rStr);
		Alignment federation = alignments.findAlignment(fStr);
		
		
	//	VIP
		Alignment neutral = alignments.findAlignment(nStr);
		Alignment smuggler = alignments.findAlignment(sStr);
		Alignment good = alignments.findAlignment(gStr);
		
		usa.addCanHaveVip(neutral.getName());
		usa.addCanHaveVip(smuggler.getName());
		usa.addCanHaveVip(good.getName());
		
		china.addCanHaveVip(neutral.getName());
		china.addCanHaveVip(smuggler.getName());
		china.addCanHaveVip(good.getName());
		
		eu.addCanHaveVip(neutral.getName());
		eu.addCanHaveVip(smuggler.getName());
		eu.addCanHaveVip(good.getName());
		
		alliance.addCanHaveVip(neutral.getName());
		alliance.addCanHaveVip(smuggler.getName());
		alliance.addCanHaveVip(good.getName());
		
		russian.addCanHaveVip(neutral.getName());
		russian.addCanHaveVip(smuggler.getName());
		russian.addCanHaveVip(good.getName());
		
		trade.addCanHaveVip(neutral.getName());
		trade.addCanHaveVip(good.getName());
		
		federation.addCanHaveVip(neutral.getName());
		federation.addCanHaveVip(smuggler.getName());
		
		gw.setAlignments(alignments);
		
		// Spaceship types
        UniqueIdCounter uniqueShipIdCounter = new UniqueIdCounter();
        UniqueIdCounter uniqueVIPIdCounter = new UniqueIdCounter();
        UniqueIdCounter uniqueBuildingIdCounter = new UniqueIdCounter();

//¤¤¤¤¤¤ vip types ¤¤¤¤¤¤
   /*     VIPType tmpVipType = new VIPType("Governor","Gov",uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        gw.addVipType(tmpVipType);
     */   

	VIPType tmpVipType = new VIPType("Fanatic Spy","FSpy",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setSpying(true);
        tmpVipType.setCounterEspionage(50);
        tmpVipType.setDescription("This VIP can visit enemy’s planets and get the same information as on open planets. On the own planets this VIP can seeking up enemies assassin and try to kill them.");
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Merchant","Mer",good,uniqueVIPIdCounter);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setOpenIncBonus(4);
        tmpVipType.setClosedIncBonus(1);
        tmpVipType.setDescription("A merchant will give you 4 extra incomes on open planets and one extra on closed. Beware of that merchant on open planets will be an easy target to spot for enemies assassins.");
        gw.addVipType(tmpVipType);

       	tmpVipType = new VIPType("Smuggler","Smu",smuggler,uniqueVIPIdCounter);
        tmpVipType.setClosedIncBonus(3);
        tmpVipType.setDescription("A smuggler gives 3 extra incomes on closed planets. Open planets don’t give smuggler a chance to work.");
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Master Designer","MDes",neutral,uniqueVIPIdCounter);
        tmpVipType.setTechBonus(20);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setDescription("This designer is so skill full that all yours ships and troops build on the same planets is 20% better in both shields and weapons. Beware of that a master designer on open planets will be easy to spot for enemy’s assassins.");
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Economic Genius","Eco",neutral,uniqueVIPIdCounter);
        tmpVipType.setShipBuildBonus(20);
        tmpVipType.setTroopBuildBonus(20);
        tmpVipType.setBuildingBuildBonus(20);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setDescription("Economic genius wills lower the cost to build ships, troops and buildings on the same planets. Beware of that an economic genius on open planets will be easy to spot for enemy’s assassins.");
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Fanatic General","FGen",neutral,uniqueVIPIdCounter);
        tmpVipType.setLandBattleGroupAttackBonus(20);
        //tmpVipType.setSiegeBonus(1);
        //tmpVipType.setResistanceBonus(3);
        tmpVipType.setDescription("A General is skill full in ground combats and helping out yours troops in both attacking and defending planets. To give bonus in attacks a general needs to be on a troop.");
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Sky Marshal","Sky",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitBonus(10);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        tmpVipType.setDescription("The highest fleet commander that gives yours fleet an advantage in tactics that’s and gives the fleet more opportunity to give fire.");
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Admiral","Adm",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitBonus(7);
        tmpVipType.setDescription("The admiral gives your fleet an advantage in tactics, that’s gives the fleet more opportunity to give fire.");
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Top Ace","TAce",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitFighterSquadronBonus(8);
        tmpVipType.setDescription("Thanks to the skill the top ace have, the fleet gives more opportunity to fire the weapons. A top ace is a squadron’s pilot and must be on a squadron to give bonus.");
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Fanatic Assassin","FAss",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setAssassination(50);
        tmpVipType.setDescription("Used to kill hostiles VIPs. Can travel between planets by him self.");
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Master Captain","MCap",neutral,uniqueVIPIdCounter);
        tmpVipType.setFTLbonus(true);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        tmpVipType.setDescription("Boost the range for short range ship to work as a long range ship.");
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Diplomat","Dip",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
        tmpVipType.setDescription("A diplomat can persuade a neutral planet to join yours side. It will take one turn to persuade one of the planets resistant.");
        gw.addVipType(tmpVipType);
        
//      *******************************************************************************
        // ****************** --- VIP som byggs i buildings --- *******************
        // *******************************************************************************
        
        // XXX VIP:ar att köpa via buildings.
        
        // Just for Trade federation
        tmpVipType = new VIPType("Negotiator","Neg",trade,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setBuildCost(20);
        tmpVipType.setPlayerUnique(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("The Negotiator is a hero with skill in diplomatic skill to convince neutral planets to join he’s faction.");
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("General","Gen",neutral,uniqueVIPIdCounter);
        tmpVipType.setLandBattleGroupAttackBonus(20);
        tmpVipType.setPlayerUnique(true);
        tmpVipType.setBuildCost(12);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("A General is skill full in ground combats and helping out yours troops in both attacking planets and defending them. To give bonus in attacks a general needs to be on a troop.");
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Assassin","Ass",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setAssassination(30);
        tmpVipType.setBuildCost(20);
        tmpVipType.setPlayerUnique(true);
        tmpVipType.setDescription("Used to kill hostiles VIPs. Can travel between planets by him self.");
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Spy","Spy",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setSpying(true);
        tmpVipType.setCounterEspionage(20);
        tmpVipType.setBuildCost(15);
        tmpVipType.setPlayerUnique(true);
        tmpVipType.setDescription("This VIP can visit enemy’s planets and get the same information as on open planets. On the own planets this VIP can seeking up enemies assassin and try to kill them.");
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Ace","Ace",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitFighterSquadronBonus(6);
        tmpVipType.setBuildCost(15);
        tmpVipType.setPlayerUnique(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("Thanks to the skill the ace have, the fleet gives more opportunity to fire the weapons. A ace is a squadron’s pilot and must be on a squadron to give bonus.");
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Designer","Des",neutral,uniqueVIPIdCounter);
        tmpVipType.setTechBonus(10);
        tmpVipType.setBuildCost(20);
        tmpVipType.setPlayerUnique(true);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("This designer is so skill full that all yours ships and troops build on the same planets is 10% better in both shields and weapons. Beware of that a designer on open planets will be easy to spot for enemy’s assassins.");
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Captain","Capt",neutral,uniqueVIPIdCounter);
        tmpVipType.setFTLbonus(true);
        tmpVipType.setBuildCost(100);
        tmpVipType.setPlayerUnique(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("Boost the range for short range ship to work as a long range ship.");
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Commander","Com",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitBonus(5);
        tmpVipType.setBuildCost(15);
        tmpVipType.setPlayerUnique(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("The commander gives your fleet an advantage in tactics, that’s gives the fleet more opportunity to give fire.");
        gw.addVipType(tmpVipType);
        
        
        
//      ******
        // Troops
        // ******
        // XXX TroopTypes

        // ***********************************************************************
        
        
		UniqueIdCounter uTIC = new UniqueIdCounter();
		
		TroopType freedomForce = new TroopType("Freedom Force","FF",110,2,7,uTIC,12,10);
		freedomForce.setDescription("Free planet defenders that fight hard to defend the freedom.");
		freedomForce.setShortDescription("Cheap defensive infantry");
		freedomForce.setAdvantages("Cheap to have");
		freedomForce.setDisadvantages("Expensive to recruit");
		freedomForce.setBlackMarketFrequency(BlackMarketFrequency.VERY_COMMON);
		freedomForce.setCanAppearOnBlackMarket(true);
		gw.addTroopType(freedomForce);
        gw.setNeutralTroopType(freedomForce);
        
        
        
       
//  ######## Neural ########
        
        // Neutral Defence platforms
        // -----------------
      
	// Medium Defender (Neutral)
        SpaceshipType tempsst = new SpaceshipType("Medium Defender","MDef",SpaceshipType.SIZE_MEDIUM,20,120,SpaceshipRange.NONE,2,10,uniqueShipIdCounter,15,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(11);
        tempsst.setWeaponsMaxSalvoesMedium(18);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setArmorSmall(35);
        tempsst.setArmorMedium(10);
        tempsst.setDescription("This medium planet defender ship is made for protection and support against hostile’s fleets. Very cheap in maintains because the lack of hyper engine. This ship is often used by neutral mediums sized planets and in number of 1 to 4. Good against the most of ship but not a match for huge sized ship.");
        gw.addShipType(tempsst);
        
        // Large Defender (Neutral)
        tempsst = new SpaceshipType("Large Defender","LDef",SpaceshipType.SIZE_LARGE,75,290,SpaceshipRange.NONE,3,36,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsMaxSalvoesMedium(15);
        tempsst.setWeaponsStrengthLarge(30);
        tempsst.setWeaponsMaxSalvoesLarge(10);
        tempsst.setArmorSmall(50);
        tempsst.setArmorMedium(25);
        tempsst.setDescription("This large planet defender ship is made for protection and support against hostile’s fleets. Very cheap in maintains because the lack of hyper engine. This ship is often used by neutral large sized planets and in number of 1 to 3. Good against medium and large ships.");
        tempsst.setIncreaseInitiative(2);
        gw.addShipType(tempsst);
        
        
        // Neutral Squadrons
        // ---------
        // Freedom Fighter
        tempsst = new SpaceshipType("Freedom Fighter","FreF",SpaceshipType.SIZE_SMALL,5,40,SpaceshipRange.NONE,1,12,uniqueShipIdCounter,12,12);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(1);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.VERY_COMMON);
        tempsst.setBluePrintFirstTurn(60);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.RARE);
        tempsst.setDescription("A good all-round support fighter. This fighter is often used by small neutral planets in number of 1-4. Some time these ships have been convinced to join a faction fleet if the home planet is under rule of the same faction.");
        gw.addShipType(tempsst);
        
        // add shiptypes for neutral planets
        gw.setNeutralSize1(gw.getSpaceshipTypeByName("Freedom Fighter"));
        gw.setNeutralSize2(gw.getSpaceshipTypeByName("Medium Defender"));
        gw.setNeutralSize3(gw.getSpaceshipTypeByName("Large Defender"));
        
		Corruption tmpCorruption = new Corruption();
		tmpCorruption.addBreakpoint(60, 30); // kan sänkas till 10%
		tmpCorruption.addBreakpoint(120, 60); // kan sänkas till 40%
		tmpCorruption.addBreakpoint(180, 80); // kan sänkas till 50%
		tmpCorruption.addBreakpoint(250, 90); // kan sänkas till 60%
		
		
		// 1 forsknings steget. Kostar 20% i 3 turn.
		Corruption tmpCorruption1 = new Corruption();
		tmpCorruption1.addBreakpoint(60, 10); 
		tmpCorruption1.addBreakpoint(120, 60); 
		tmpCorruption1.addBreakpoint(180, 80); 
		tmpCorruption1.addBreakpoint(250, 90); 
		
		// 2 forsknings steget. Kostar 20% i 3 turn.
		Corruption tmpCorruption2 = new Corruption();
		tmpCorruption2.addBreakpoint(60, 10); 
		tmpCorruption2.addBreakpoint(120, 40); 
		tmpCorruption2.addBreakpoint(180, 80); 
		tmpCorruption2.addBreakpoint(250, 90);
		
		// 3 forsknings steget. Kostar 20% i 3 turn.
		Corruption tmpCorruption3 = new Corruption();
		tmpCorruption3.addBreakpoint(60, 10); 
		tmpCorruption3.addBreakpoint(120, 40); 
		tmpCorruption3.addBreakpoint(180, 60); 
		tmpCorruption3.addBreakpoint(250, 90);
		
		//4 forsknings steget. Kostar 20% i 3 turn.
		Corruption tmpCorruption4 = new Corruption();
		tmpCorruption4.addBreakpoint(60, 10); 
		tmpCorruption4.addBreakpoint(120, 40); 
		tmpCorruption4.addBreakpoint(180, 50); 
		tmpCorruption4.addBreakpoint(250, 90);
		
		// 5 forsknings steget. Kostar 10% i 5 turn.
		Corruption tmpCorruption5 = new Corruption();
		tmpCorruption5.addBreakpoint(60, 10); 
		tmpCorruption5.addBreakpoint(120, 40); 
		tmpCorruption5.addBreakpoint(180, 50); 
		tmpCorruption5.addBreakpoint(250, 70);
		
		// 6 forsknings steget. Kostar 10% i 5 turn.
		Corruption tmpCorruption6 = new Corruption();
		tmpCorruption6.addBreakpoint(60, 10); 
		tmpCorruption6.addBreakpoint(120, 40); 
		tmpCorruption6.addBreakpoint(180, 50); 
		tmpCorruption6.addBreakpoint(250, 60);
		
		// ************************* --- Forskning Corrruption --- *************************
        
		ResearchAdvantage researchCorruption1 = new ResearchAdvantage("Corruption level 6","Lower corruption");
		researchCorruption1.setCorruption(tmpCorruption6);
		researchCorruption1.setTimeToResearch(5);
		researchCorruption1.setCostToResearchOneTurnInProcent(10);
        
        ResearchAdvantage researchCorruption2 = new ResearchAdvantage("Corruption level 5","Lower corruption");
        researchCorruption2.setCorruption(tmpCorruption5);
        researchCorruption2.setTimeToResearch(5);
        researchCorruption2.setCostToResearchOneTurnInProcent(10);
        researchCorruption2.addChild(researchCorruption1);
        
        ResearchAdvantage researchCorruption3 = new ResearchAdvantage("Corruption level 4","Lower corruption");
        researchCorruption3.setCorruption(tmpCorruption4);
        researchCorruption3.setTimeToResearch(3);
        researchCorruption3.setCostToResearchOneTurnInProcent(20);
        researchCorruption3.addChild(researchCorruption2);
        
        ResearchAdvantage researchCorruption4 = new ResearchAdvantage("Corruption level 3","Lower corruption");
        researchCorruption4.setCorruption(tmpCorruption3);
        researchCorruption4.setTimeToResearch(3);
        researchCorruption4.setCostToResearchOneTurnInProcent(20);
        researchCorruption4.addChild(researchCorruption3);
        
        ResearchAdvantage researchCorruption5 = new ResearchAdvantage("Corruption level 2","Lower corruption");
        researchCorruption5.setCorruption(tmpCorruption2);
        researchCorruption5.setTimeToResearch(3);
        researchCorruption5.setCostToResearchOneTurnInProcent(20);
        researchCorruption5.addChild(researchCorruption4);
        
        ResearchAdvantage researchCorruption6 = new ResearchAdvantage("Corruption level 1","Lower corruption");
        researchCorruption6.setCorruption(tmpCorruption1);
        researchCorruption6.setTimeToResearch(3);
        researchCorruption6.setCostToResearchOneTurnInProcent(20);
        researchCorruption6.addChild(researchCorruption5);
        
        
//  ######## China (Red) ########
		// XXX China        
        Faction tempFaction = new Faction("China",Faction.getColorHexString(240,35,45),china);
        
        // Adding Buildings to the faction
        Buildings tempBuildings = new Buildings();
        BuildingType tempBuildingType;
        
        tempBuildingType= new BuildingType("Small Wharf", "W1", 2, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(1);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Small China Wharf what can build small ships");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Medium Wharf", "W2", 8, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(2);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Medium Wharf what can build small and medium ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Small Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large China Wharf", "W3", 18, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(3);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge China Wharf", "W5", 40, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large China Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Spaceport", "SP", 8, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setOpenPlanetBonus(1);
        tempBuildingType.setClosedPlanetBonus(1);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Build this building on the planets and long range ways will be short range between planetes whit spaceports and give 1 in incom.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("China Home Base", "CHom", 20, uniqueBuildingIdCounter);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setOpenPlanetBonus(7);
        tempBuildingType.setClosedPlanetBonus(7);
        tempBuildingType.setDescription("The home base is made as a head office. Gives an income of 7.");
        tempBuildingType.setPlayerUnique(true);
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("China Base", "CB", 2, uniqueBuildingIdCounter);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("A small base and a ground for bigger buildings");
        tempBuildingType.setAutoDestructWhenConquered(true);
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Agent Center", "AC", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Assassin"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Spy"));
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Agent Center to train undercover VIPs");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("China Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("China Military Academy", "CMA", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Commander"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Ace"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("General"));
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Academy to train military VIPs");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("China Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("City", "Ci", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setOpenPlanetBonus(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("City that gives 2 in trade incom if the planet is open.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("China Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Province Capital", "PC", 20, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(5);
        tempBuildingType.setResistanceBonus(2);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Province Capital that gives 5 in incom if the planet is open. Gives bonus to the defending ground force. Only one Province Capital can be build each player.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("City"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Capital", "Ca", 30, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(7);
        tempBuildingType.setResistanceBonus(5);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setFactionUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Capital that gives 7 in incom if the planet is open. Gives a big bonus to the defending ground force. Only one Capital can be build each faction.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Barracks", "Bar", 10, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Barracks to train infantary");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large Barracks", "LB", 10, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(3);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Barracks to train up to 3 infantarys");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Barracks"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("China Military Center", "CMC", 10, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(5);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDescription("Military center to train up to 5 infantarys");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Barracks"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Tech Center", "TC", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setTechBonus(6);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("Tech center give better technology to units.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Advanced Tech Center", "ATC", 12, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setTechBonus(12);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Advanced tech center gives even better technology to units than tech center do.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Tech Center"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Cannon Factory", "CF", 10, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.SUPPORT);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("In this buiding support ground units can be build.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Tank Factory", "TF", 10, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.ARMORED);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("In this buiding armored ground units can be build.");
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Basic Shield", "BS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A small shield to defende planets from bombardment.");
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Missile Silo", "MS", 5, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(100);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A planet based missile system to shoot down ships what besiege the planet.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Missile Defence", "MD", 15, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(100);
        tempBuildingType.setCannonRateOfFire(3);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A planet based missile system to shoot down ships in great numbers, what besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Missile Silo"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
       tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));
       tempFaction.addStartingBuildings(tempBuildings.getBuildingType("China Base"));
       tempFaction.addStartingBuildings(tempBuildings.getBuildingType("SpacePort"));
       tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Barracks"));
       tempFaction.addStartingBuildings(tempBuildings.getBuildingType("China Home Base"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
        
        // ###### TROOPS ######
        
        TroopType tt = new TroopType("China Light Infantry","CLI",80,1,3,uTIC,12,5);
        tt.setDescription("Cheap light infantry unit often used to defend planetes.");
        tt.setShortDescription("Cheap light infantry unit.");
        tt.setAdvantages("Cheap");
        tt.setDisadvantages("Weak against armor.");
        tt.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("China Light Infantry"));
        
        tt = new TroopType("China Infantry","CI",100,1,4,uTIC,12,12);
        tt.setDescription("Cheap infantry unit often used to defend planetes.");
        tt.setShortDescription("Cheap infantry unit.");
        tt.setAdvantages("Cheap");
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("China Infantry"));
        
        tt = new TroopType("China Heavy Infantry","CHI",120,2,6,uTIC,20,17);
        tt.setDescription("Heavy infantry unit.");
        tt.setShortDescription("Heavy infantry unit.");
        tt.setAdvantages("");
        tt.setDisadvantages("");
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("China Heavy Infantry"));
        
        tt = new TroopType("China Heavy Infantry II","CHI2",120,2,7,uTIC,30,27);
        tt.setDescription("Heavy infantry unit.");
        tt.setShortDescription("Heavy infantry unit.");
        tt.setAdvantages("");
        tt.setDisadvantages("");
        tt.setCanBuild(false);
        tt.setBlackmarketFirstTurn(10);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("China Heavy Infantry II"));
        
        tt = new TroopType("China Elite Command","CEC",120,4,15,uTIC,50,44);
        tt.setDescription("Heavy infantry unit that is the elite of the elite.");
        tt.setShortDescription("Heavy infantry unit that is the elit of the elit.");
        tt.setAdvantages("The strongest infantary in China military.");
        tt.setDisadvantages("Can only have one of them.");
        tt.setPlayerUnique(true);
        tt.setCanBuild(false);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("China Elite Command"));
        
        tt = new TroopType("Light China Artillery","LCA",30,1,3,uTIC,5,2);
        tt.setDescription("Light China artillery unit.");
        tt.setShortDescription("Light China artillery");
        tt.setAdvantages("Artillery attack");
        tt.setDisadvantages("Almost defenseless");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(25);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Light China Artillery"));
        
        tt = new TroopType("China Artillery","CA",30,1,5,uTIC,7,7);
        tt.setDescription("China artillery unit.");
        tt.setShortDescription("China artillery");
        tt.setAdvantages("Artillery attack");
        tt.setDisadvantages("Almost defenseless");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(35);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        tt.setBlackmarketFirstTurn(8);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("China Artillery"));
        
        tt = new TroopType("China Heavy Artillery","CHA",40,1,10,uTIC,10,14);
        tt.setDescription("China artillery unit.");
        tt.setShortDescription("China artillery");
        tt.setAdvantages("Artillery attack");
        tt.setDisadvantages("Almost defenseless");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(45);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        tt.setBlackmarketFirstTurn(12);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("China Heavy Artillery"));
        
        tt = new TroopType("China Light Tanks","CLT",80,2,6,uTIC,20,5);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        tt.setDescription("Light tank unit.");
        tt.setShortDescription("Light Tanks");
        tt.setAdvantages("Good versus infantry units and can attack support units (Flank attacks");
        tt.setDisadvantages("Weak against tanks");
        tt.setBlackmarketFirstTurn(10);
        tt.setDefaultPosition(BattleGroupPosition.FLANKER);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("China Light Tanks"));
        
        tt = new TroopType("China Antitank Tanks","CAT",80,2,6,uTIC,10,40);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setDescription("Light tank unit amrmed with a anti tanks cannon.");
        tt.setShortDescription("Light antitank tanks");
        tt.setAdvantages("Good versus tanks units.");
        tt.setDisadvantages("Weak against infantry");
        tt.setBlackmarketFirstTurn(10);
        tt.setDefaultPosition(BattleGroupPosition.FIRST_LINE);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("China Antitank Tanks"));
        
        tempFaction.addStartingTroop(gw.getTroopTypeByName("China Light Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("China Light Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("China Heavy Infantry")); 
        

        // China Defence platforms
        // -----------------
        // China Defender
        tempsst = new SpaceshipType("C Defender","CDef",SpaceshipType.SIZE_MEDIUM,10,60,SpaceshipRange.NONE,1,8,uniqueShipIdCounter,8,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setArmorSmall(80);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This small planet defender ship is made for protection and support against fighters. Very cheap in maintains because the lack of hyper engine. Much better against fighters then the badly designed fighters in our navy.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("C Defender"));
        
        tempsst = new SpaceshipType("C Heavy Defender","CHDef",SpaceshipType.SIZE_LARGE,60,400,SpaceshipRange.NONE,4,28,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(10);
        tempsst.setWeaponsStrengthLarge(40);
        tempsst.setWeaponsMaxSalvoesLarge(12);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("This large planet defender ship was developed for protection against large and medium capital ships. The ship is very cheap to have because the lack of hyper engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("C Heavy Defender"));
        
                
        // China Home Defender
        tempsst = new SpaceshipType("C Home Defender","CHD",SpaceshipType.SIZE_LARGE,20,350,SpaceshipRange.NONE,0,50,uniqueShipIdCounter,10,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setArmorSmall(45);
        tempsst.setArmorMedium(20);
        tempsst.setIncreaseInitiative(6);
        tempsst.setAvailableToBuild(true);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This large planet home defender is made to offers good protections against Squadrons. The ship hasn’t any hyper engine.");
        tempsst.setPlayerUnique(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("C Home Defender"));
        
        // Squadrons
        // ---------
        // China Fighter
        tempsst = new SpaceshipType("C Fighter","CFig",SpaceshipType.SIZE_SMALL,0,20,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,5,10);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("The only fighter in the navy and unfortunately not good enough. This ship was developed to protect the capital ships against bombers squadrons in big battles there Fighter Destroyer ships are easy targets for enemies’ capital ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("C Fighter"));
        
        // China Fighter II ****************************************************
        tempsst = new SpaceshipType("C Fighter II","CFig2",SpaceshipType.SIZE_SMALL,5,27,SpaceshipRange.NONE,1,4,uniqueShipIdCounter,6,15);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This ship is the next generation of fighters better but mor expencive to buy. This ship was developed to protect the capital ships against bombers squadrons in big battles there Fighter Destroyer ships are easy targets for enemies’ capital ships.");
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("C Fighter II"));
        
        // China Bomber
        tempsst = new SpaceshipType("C Bomber","CBom",SpaceshipType.SIZE_SMALL,0,17,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,10,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(8);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A badly designed anti capital ship, only good in battle against enemies without fighters or anti fighter capacity.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("C Bomber"));
        
        // China Bomber II *******************************************************
        tempsst = new SpaceshipType("C Bomber II","CBom2",SpaceshipType.SIZE_SMALL,5,17,SpaceshipRange.NONE,1,4,uniqueShipIdCounter,12,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(8);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(12);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This ship is a upgraded C Bomber with a small shield and armed with large weapons. A badly designed anti capital ship, only good in battle against enemies without fighters or anti fighter capacity.");
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("C Bomber II"));
        
        // China Attacker *******************************************************
        tempsst = new SpaceshipType("C Attacker","CAtt",SpaceshipType.SIZE_SMALL,5,22,SpaceshipRange.SHORT,2,6,uniqueShipIdCounter,10,10);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(8);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This ship is both a bomber and a fighter in one ship. Bigger and more expansive but have short range capasity.");
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("C Attacker"));
        
        
        // Capital ships
        // -------------
        // Planet corvette
        tempsst = new SpaceshipType("Planet corvette","PCrv",SpaceshipType.SIZE_MEDIUM,10,60,SpaceshipRange.LONG,2,5,uniqueShipIdCounter,8,6);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(15);
        tempsst.setCanBlockPlanet(false);
        tempsst.setWeaponsStrengthMedium(50);
        tempsst.setWeaponsMaxSalvoesMedium(1);
        tempsst.setDescription("One of the first ship models in the space navy force. This ship was build to explore the space and to protect us against smaller enemy ships. After the war break out the ship was equipped with medium E4 type laser to shut down smaller troop ships. Unfortunately the big old hull makes this ship to an easy target for medium size ship.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Planet corvette"));
        
        // Planet corvette II
        tempsst = new SpaceshipType("Planet corvette II","PCrv2",SpaceshipType.SIZE_SMALL,10,30,SpaceshipRange.LONG,1,5,uniqueShipIdCounter,12,6);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setCanBlockPlanet(false);
        tempsst.setDescription("Light small long range capital ship used for exploring and person transports. Developed to get better control in the outerspace.");
        tempsst.setAvailableToBuild(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Planet corvette II"));
        
        // Troop Transporter
        tempsst = new SpaceshipType("Troop Transporter","Tra",SpaceshipType.SIZE_MEDIUM,5,30,SpaceshipRange.LONG,4,9,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(5);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setArmorSmall(15);
        tempsst.setTroopCapacity(3);
        tempsst.setScreened(true);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This is the main troop transport ship is armed with an E1 medium laser gun to hold hostility troop transports away. This ship was created to carry troops and not for combat action. The ship has an old engine that makes it expensive in both building and maintains.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Transporter"));
        
        // Troop Transporter II
        tempsst = new SpaceshipType("Troop Transporter II","Tra2",SpaceshipType.SIZE_MEDIUM,5,30,SpaceshipRange.LONG,2,12,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(5);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setArmorSmall(15);
        tempsst.setTroopCapacity(3);
        tempsst.setScreened(true);
        tempsst.setDescription("This is the main troop transport ship is armed with an E1 medium laser gun to hold hostility troop transports away. This ship was created to carry troops and not for combat action. The ship has a new type of engine that reduce the cost in both building and maintains compared with the precursor.");
        tempsst.setAvailableToBuild(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Transporter II"));
        
        // Fighter Destroyer
        tempsst = new SpaceshipType("Fighter Destroyer","FDes",SpaceshipType.SIZE_MEDIUM,20,70,SpaceshipRange.LONG,4,9,uniqueShipIdCounter,5,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setArmorSmall(70);
        tempsst.setArmorMedium(20);
        tempsst.setInitDefence(4);
        tempsst.setDescription("To protect our civil and army transport ships against pirate’s fighter this ship was build as an escort ship. The ship hade very good armor against fighters and the best anti fighter’s lasers in the fleet, but is vulnerable for fire from medium weapons. In the beginning of the war many ships was lost in combat against ships equipped with medium laser. The ship is so powerful that enemy hade to concentrate all the fire power against it in the beginning of a fight and then lost a lot of initiative doing that.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Destroyer"));
        
        // Destroyer
        tempsst = new SpaceshipType("Destroyer","Des",SpaceshipType.SIZE_MEDIUM,20,110,SpaceshipRange.LONG,5,12,uniqueShipIdCounter,15,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setArmorSmall(40);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This medium class ship is one of the best long range battle ship in the fleet. Good against medium and small ship but don't heavy enough to be a threat against the big battle main ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        
        // Destroyer Carrier
        tempsst = new SpaceshipType("Destroyer Carrier","DesC",SpaceshipType.SIZE_MEDIUM,20,110,SpaceshipRange.LONG,6,15,uniqueShipIdCounter,15,2);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setArmorSmall(40);
        tempsst.setSquadronCapacity(2);
        tempsst.setBlackmarketFirstTurn(10);
        tempsst.setBluePrintFirstTurn(30);
        tempsst.setDescription("This medium class ship is one of the best long range battle ship in the fleet. This ship have capacity to carrier 2 squdrons to defend against incoming squadrons attacks.");
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer Carrier"));
        
        // Destroyer L
        tempsst = new SpaceshipType("Destroyer L","DesL",SpaceshipType.SIZE_MEDIUM,20,110,SpaceshipRange.LONG,6,15,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setWeaponsStrengthLarge(20);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setArmorSmall(40);
        tempsst.setBlackmarketFirstTurn(10);
        tempsst.setBluePrintFirstTurn(30);
        tempsst.setDescription("This medium class ship is the best long range battle ship in the fleet against bigger capital ships. Good against medium and large ships but weak against squdrons and small ships.");
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer L"));
        
        // Destroyer B
        tempsst = new SpaceshipType("Destroyer B","DesB",SpaceshipType.SIZE_MEDIUM,20,110,SpaceshipRange.LONG,6,15,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setBombardment(1);
        tempsst.setArmorSmall(40);
        tempsst.setBlackmarketFirstTurn(10);
        tempsst.setBluePrintFirstTurn(30);
        tempsst.setDescription("This medium class ship is the only long range battle ship that have bombardment capacity in the fleet.");
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer B"));
        
                
        // WarDestroyer
        tempsst = new SpaceshipType("WarDestroyer","WarD",SpaceshipType.SIZE_LARGE,75,480,SpaceshipRange.SHORT,12,51,uniqueShipIdCounter,15,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(28);
        tempsst.setWeaponsMaxSalvoesMedium(22);
        tempsst.setWeaponsStrengthLarge(45);
        tempsst.setWeaponsMaxSalvoesLarge(15);
        tempsst.setWeaponsStrengthHuge(15);
        tempsst.setWeaponsMaxSalvoesHuge(6);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setBombardment(1);
        tempsst.setIncreaseInitiative(6);
        tempsst.setSquadronCapacity(2);
        tempsst.setBlackmarketFirstTurn(30);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(60);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("A heavy armed large capital ship that was made after that the first battle against enemy’s huge capital ships. Armed with torpedoes against huge class capital ship and also bombardment capacity. The weakness of this ship is the lack of protections against squadrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("WarDestroyer"));
        
        // WarDestroyer II
        tempsst = new SpaceshipType("WarDestroyer II","WarD2",SpaceshipType.SIZE_LARGE,100,480,SpaceshipRange.SHORT,12,55,uniqueShipIdCounter,15,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(28);
        tempsst.setWeaponsMaxSalvoesMedium(22);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(15);
        tempsst.setWeaponsStrengthHuge(30);
        tempsst.setWeaponsMaxSalvoesHuge(6);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setBombardment(3);
        tempsst.setIncreaseInitiative(8);
        tempsst.setSquadronCapacity(2);
        tempsst.setBlackmarketFirstTurn(40);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.RARE);
        tempsst.setBluePrintFirstTurn(80);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.RARE);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("A heavy armed large capital ship that was made after that the first battle against enemy’s huge capital ships. Armed with torpedoes against huge class capital ship and also great bombardment capacity. The weakness of this ship is the lack of protections against squadrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("WarDestroyer II"));
        
        // WarDestroyer T
        tempsst = new SpaceshipType("WarDestroyer T","WarDT",SpaceshipType.SIZE_LARGE,75,480,SpaceshipRange.SHORT,12,55,uniqueShipIdCounter,15,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(28);
        tempsst.setWeaponsMaxSalvoesMedium(22);
        tempsst.setWeaponsStrengthLarge(45);
        tempsst.setWeaponsMaxSalvoesLarge(15);
        tempsst.setWeaponsStrengthHuge(15);
        tempsst.setWeaponsMaxSalvoesHuge(6);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setTroopCapacity(5);
        tempsst.setIncreaseInitiative(6);
        tempsst.setSquadronCapacity(2);
        tempsst.setBlackmarketFirstTurn(40);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.RARE);
        tempsst.setBluePrintFirstTurn(80);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.RARE);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("A heavy armed large capital ship that was made after that the first battle against enemy’s huge capital ships. Armed with torpedoes against huge class capital ship and capacity to hold 5 company of troops. The weakness of this ship is the lack of protections against squadrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("WarDestroyer T"));
        
        // WarDestroyer C
        tempsst = new SpaceshipType("WarDestroyer C","WarDC",SpaceshipType.SIZE_LARGE,75,480,SpaceshipRange.SHORT,12,55,uniqueShipIdCounter,15,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(28);
        tempsst.setWeaponsMaxSalvoesMedium(22);
        tempsst.setWeaponsStrengthLarge(45);
        tempsst.setWeaponsMaxSalvoesLarge(15);
        tempsst.setWeaponsStrengthHuge(15);
        tempsst.setWeaponsMaxSalvoesHuge(6);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setBombardment(1);
        tempsst.setIncreaseInitiative(6);
        tempsst.setSquadronCapacity(6);
        tempsst.setBlackmarketFirstTurn(40);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.RARE);
        tempsst.setBluePrintFirstTurn(80);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.RARE);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("A heavy armed large capital ship that was made after that the first battle against enemy’s huge capital ships. Armed with torpedoes against huge class capital ship and also bombardment capacity. Can carrier up to 6 squdrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("WarDestroyer C"));
        
        //WarBattleship
        tempsst = new SpaceshipType("WarBattleship","WarB",SpaceshipType.SIZE_LARGE,90,500,SpaceshipRange.LONG,15,42,uniqueShipIdCounter,12,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(27);
        tempsst.setWeaponsMaxSalvoesLarge(18);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setBombardment(2);
        tempsst.setIncreaseInitiative(12);
        tempsst.setSquadronCapacity(6);
        tempsst.setTroopCapacity(3);
        tempsst.setPlanetarySurvey(true);
        tempsst.setSupply(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setPlayerUnique(true);
        tempsst.setDescription("The biggest and best capital ship in the world then first one was made. It's hard to find any week point of this ship, excellent to conquer planets, survey capacity, big enough to supply the fleet, great against capital ship and good carrier capacity. It's just the newly huge capital ships that could be too much to meet in a combat. This ship is a leader ship so, for that reason only one can be build at the same time");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("WarBattleship"));
        
        // Galaxy WarBattleship
        tempsst = new SpaceshipType("Galaxy WarBattleship","GWBa",SpaceshipType.SIZE_HUGE,150,1050,SpaceshipRange.SHORT,33,126,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(40);
        tempsst.setWeaponsMaxSalvoesMedium(12);
        tempsst.setWeaponsStrengthLarge(70);
        tempsst.setWeaponsMaxSalvoesLarge(15);
        tempsst.setWeaponsStrengthHuge(70);
        tempsst.setWeaponsMaxSalvoesHuge(10);
        tempsst.setArmorSmall(85);
        tempsst.setArmorMedium(60);
        tempsst.setArmorLarge(25);
        tempsst.setArmorHuge(10);
        tempsst.setBombardment(4);
        tempsst.setIncreaseInitiative(17);
        tempsst.setSquadronCapacity(10);
        tempsst.setTroopCapacity(6);
        tempsst.setNoRetreat(true);
        tempsst.setBlackmarketFirstTurn(80);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.VERY_RARE);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("This is the greatest ship in the world. It has capacity to destroy planets in one single shot and a big army to conquer them. Extremely powerful against large/huge capital ships and have carrier capacity for 10 squadrons. It has a system that prevents ships to start theirs hyper space engine and flee from the battlefield.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galaxy WarBattleship"));
        
        
        // Galaxy WarBattleship II
        tempsst = new SpaceshipType("Galaxy WarBattleship II","GWBa2",SpaceshipType.SIZE_HUGE,150,1050,SpaceshipRange.SHORT,33,140,uniqueShipIdCounter,10,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(40);
        tempsst.setWeaponsMaxSalvoesMedium(12);
        tempsst.setWeaponsStrengthLarge(80);
        tempsst.setWeaponsMaxSalvoesLarge(15);
        tempsst.setWeaponsStrengthHuge(80);
        tempsst.setWeaponsMaxSalvoesHuge(10);
        tempsst.setArmorSmall(85);
        tempsst.setArmorMedium(60);
        tempsst.setArmorLarge(25);
        tempsst.setArmorHuge(10);
        tempsst.setBombardment(6);
        tempsst.setIncreaseInitiative(19);
        tempsst.setSquadronCapacity(8);
        tempsst.setTroopCapacity(6);
        tempsst.setNoRetreat(true);
        tempsst.setBlackmarketFirstTurn(100);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.VERY_RARE);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("This is the next generation of the greatest ship in the world. It has capacity to destroy planets in one single shot even if the planet is defended by a shield, A big army to conquer planets. Extremely powerful against large/huge capital ships and have carrier capacity for 8 squadrons. It has a system that prevents ships to start theirs hyper space engine and flee from the battlefield.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galaxy WarBattleship II"));
        
        
        
        // ****** fraction unique property ******
        
        tmpVipType = new VIPType("President of china","PoC",china,uniqueVIPIdCounter);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("The president is leader for China factions. He gives one extra income if he is on open planets. He can’t convince neutral planets to join the factions as the other leaders. It just too hard to convince free planets to lose the freedom.");
        gw.addVipType(tmpVipType);
        tempFaction.setGovernorVIPType(tmpVipType);
        
        tempFaction.addStartingVIPType(gw.getVIPTypeByName("General"));
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("WarBattleship"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("C Home Defender"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("C Bomber"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("C Bomber"));
        
        
        
        // *******************************************************************************
        // ****************************** --- Forskning --- ******************************
        // *******************************************************************************
        
        Research tempResearch = new Research();
        ResearchAdvantage tempResearchAdvantage;
        
        
        
        // ************************* --- Forskning Economic --- *************************
        
        tempResearch.addAdvantage(researchCorruption1);
        tempResearch.addAdvantage(researchCorruption2);
        tempResearch.addAdvantage(researchCorruption3);
        tempResearch.addAdvantage(researchCorruption4);
        tempResearch.addAdvantage(researchCorruption5);
        tempResearch.addAdvantage(researchCorruption6);
        
        
        
        // Trading III
        tempResearchAdvantage = new ResearchAdvantage("Trading III","All open planets gives one more incom");
        tempResearchAdvantage.setOpenPlanetBonus(1);
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Trading II
        tempResearchAdvantage = new ResearchAdvantage("Trading II","Gives more income to the Province Capital and the Capital");
        
        ResearchUpgradeBuilding aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Province Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Trading III"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Trading I
        tempResearchAdvantage = new ResearchAdvantage("Trading I","Gives one more income to all new SpacePort");
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("SpacePort");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Trading II"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        
        tempResearchAdvantage = new ResearchAdvantage("Economic","Develop Economic");
        tempResearchAdvantage.addChild(researchCorruption6);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Trading I"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // ************************* --- Forskning Weapons --- *************************
        
        tempResearchAdvantage = new ResearchAdvantage("China Heavy Artillery","Gives China Heavy Artillery");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("China Heavy Artillery"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("China Artillery","Gives China Artillery");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("China Artillery"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Heavy Artillery"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("China Light Tanks","Gives China Light Tanks");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("China Light Tanks"));
        tempResearch.addAdvantage(tempResearchAdvantage);
                
        tempResearchAdvantage = new ResearchAdvantage("China AntiTank Tanks","Gives China AntiTank Tanks");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("China AntiTank Tanks"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Tanks","Gives the a platfrom to develop tanks on");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Light Tanks"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China AntiTank Tanks"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("China Elite Command","Gives China Elite Command");
        tempResearchAdvantage.setTimeToResearch(0);
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("China Elite Command"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("China Infantry","Gives China Infantry");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("China Infantry"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("China Light Infantry"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Elite Command"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("China Heavy Infantry II","Gives China Heavy Infantry II");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("China Heavy Infantry II"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("China Heavy Infantry"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Elite Command"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Infantary","Infantary center to give better soldier");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Heavy Infantry II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Infantry"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        
        tempResearchAdvantage = new ResearchAdvantage("Troops","Develop Troops");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infantary"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Tanks"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Artillery"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        
        
        tempResearchAdvantage = new ResearchAdvantage("China Attacker","Gives China Attacker");
        tempResearchAdvantage.setTimeToResearch(0);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("C Attacker"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("China Fighter II","Second generation fighter");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("C Fighter II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("C Fighter"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Attacker"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("China Bomber II","Second generation bomber");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("C Bomber II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("C Bomber"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Attacker"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Squadrons","Gives shields to squadrons");
        ResearchUpgradeShip researchUpgradeShip = new ResearchUpgradeShip("C Fighter");
        researchUpgradeShip.setShields(5);
        tempResearchAdvantage.addResearchUpgradeShip(researchUpgradeShip);
        researchUpgradeShip = new ResearchUpgradeShip("C Bomber");
        researchUpgradeShip.setShields(5);
        tempResearchAdvantage.addResearchUpgradeShip(researchUpgradeShip);
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Fighter II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Bomber II"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Galaxy WarBattleship II","Gives Galaxy WarBattleship II");
        tempResearchAdvantage.setTimeToResearch(4);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Galaxy WarBattleship II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Galaxy WarBattleship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Huge Ships","Gives Galaxy WarBattleship");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Galaxy WarBattleship II"));
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Galaxy WarBattleship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("China Heavy Defender","Gives China Heavy Defender");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("C Heavy Defender"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("WarDestroyer II","Gives WarDestroyer II");
        tempResearchAdvantage.setTimeToResearch(0);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("WarDestroyer II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("WarDestroyer"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("WarDestroyer C","Gives WarDestroyer C");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("WarDestroyer C"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("WarDestroyer II"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("WarDestroyer T","Gives WarDestroyer T");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("WarDestroyer T"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("WarDestroyer II"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Large Ships","Gives WarDestroyer");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("WarDestroyer T"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("WarDestroyer C"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Huge Ships"));
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("WarDestroyer"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Destroyer Carrier","Gives Destroyer Carrier");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Destroyer Carrier"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Destroyer Large","Gives Destroyer L");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Destroyer L"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Destroyer Bombardment","Gives Destroyer B");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Destroyer B"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Troop Capacity","Gives greater capacity");
        tempResearchAdvantage.setTimeToResearch(5);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        researchUpgradeShip = new ResearchUpgradeShip("Troop Transporter II");
        researchUpgradeShip.setTroopCarrier(1);
        tempResearchAdvantage.addResearchUpgradeShip(researchUpgradeShip);
        researchUpgradeShip = new ResearchUpgradeShip("WarBattleship");
        researchUpgradeShip.setTroopCarrier(1);
        tempResearchAdvantage.addResearchUpgradeShip(researchUpgradeShip);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Troop Transporter II","Gives Troop Transporter II");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Troop Transporter II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Troop Transporter"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Medium Ships","Gives medium ships");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Destroyer Carrier"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Destroyer Large"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Destroyer Bombardment"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Troop Transporter II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Large Ships"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Small Ships","Start develop capital ships");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Planet corvette II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Planet corvette"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Medium Ships"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Spaceships","Develop Spaceships");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Heavy Defender"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Small Ships"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Squadrons"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // ************************* --- Forskning Buildgins --- *************************
        
        // Huge Wharf
        tempResearchAdvantage = new ResearchAdvantage("Huge Wharf","Gives Huge Wharf to build Huge ships");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Huge China Wharf"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Huge Ships"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Infrastructure
        tempResearchAdvantage = new ResearchAdvantage("Infrastructure","Gives more income to towns");
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("City");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Province Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Capital
        tempResearchAdvantage = new ResearchAdvantage("Capital","Gives Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Capital"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Province Capital
        tempResearchAdvantage = new ResearchAdvantage("Province Capital","Gives Province Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Capital"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Missile Defence
        tempResearchAdvantage = new ResearchAdvantage("Missile Defence","Gives Missile Defence");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Missile Defence"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Planet Defence
        tempResearchAdvantage = new ResearchAdvantage("Missile Silo","Gives Missile Silo");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Missile Silo"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Missile Defence"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Basic Shield
        tempResearchAdvantage = new ResearchAdvantage("Basic Shield","Gives Basic Shield");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Basic Shield"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Advanced Tech Center
        tempResearchAdvantage = new ResearchAdvantage("Advanced Tech Center","Gives Advanced Tech Center");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Advanced Tech Center"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        
        // Tank Factory
        tempResearchAdvantage = new ResearchAdvantage("Tank Factory","Gives Tank Factory");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Tank Factory"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // China Military Academy
        tempResearchAdvantage = new ResearchAdvantage("China Military Academy","Gives China Military Academy");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("China Military Academy"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Agent Center
        tempResearchAdvantage = new ResearchAdvantage("Agent Center","Gives Agent Center");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Agent Center"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Buildings","Develop Bildings");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Huge Wharf"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infrastructure"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Province Capital"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Missile Silo"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Basic Shield"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Advanced Tech Center"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Tank Factory"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("China Military Academy"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Agent Center"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
                
        tempResearch.setNumberOfSimultaneouslyResearchAdvantages(2);
        
        tempFaction.setResearch(tempResearch);
        
        // *******************************************************************************
        // *******************************************************************************
        // *******************************************************************************
        
        
        //tempFaction.setResistanceBonus();
     //   tempFaction.setStartingWharfSize(OrbitalWharf.MAX_SIZE_MEDIUM);
     /*   tempFaction.setWharfBuildCost(2);
        tempFaction.setWharfUpgradeCostMedium(8);
        tempFaction.setWharfUpgradeCostLarge(18);
        tempFaction.setWharfUpgradeCostHuge(40);*/
        tempFaction.setAdvantages("Strong heavy capital ships");
        tempFaction.setDisadvantages("No small ships and bad squadrons");
        tempFaction.setHistory("2005 was the year then we send up our first man in to the space and started our glory history in space. Only 6 years later we send our first man to the moon. Short after that we began to build up a station on moon and also started the plans to explore Mars.\nTo avoid military conflict in space we decide to write under the non weapons agreement. At 20052 we started to develop and build space ship to explore and transports in the space. Around 2075 we hade more then 300 space ships and was the greatest nation in space.\n\n2098 the chocking new about that one civilian ship was founded drifting around in the space with heavy damage and no cargo left. Short after that our great leaders give orders to put weapons on our ships to protections against pirates. 2112 EU did the first succeeded hyper jump. We don’t know how EU did that before us but we know that some one was spying on us and downloaded information about our hyper drive developing. We decided to get more information about the hyper driver. But still we were in chock then EU did the first jump to a planet in another sun system. We hade the greatest space fleet but could not do any space jumps.\nThen the first hyper engines get out to market we started to build up a fleet for hyper jumping. That was really expensive to do in both economic and human lives. But when we found a safe way to a planet the profit some time was huge. At this time a lot of civilian ships did a lot of hyper jumps to start new colonies. So it’s was more and more common that the new planets we did a succeeded jump to was already under humans control. The most of the time the inhabitant was glad to se us but some time we were founding us under attack.\nAround 2170 we started to lose ships and even some planets after attacks from unknown attacker. AT 2178 some big companies stared to build big war ships and we hade to answer to that threat so we started to develop a large ship to dominate big areas and to give us a chance to destroy pirate’s bases and planets.\nThe name of this ship was War Battleship and was outstanding for many years.\nAt 2240 a lot of planets and ships in the outer space started a federation called Federation of liberty. Only 2 years after that the Great War started after nuclear attacks. It hard to tell who started we attacks but our defence system answered the attack with attacking EU on the earth. We can only guess way EU did that attacked on us. But the reason could be that they was afraid to loose the domination that they hade on long range jumps to us. In only one week we lost all connection with earth and the rest of the sun system.\nWe can only hope that some ships hade the time to flee from the sun system and started over in unknown system.\nWe all have to work hard to survive in the new world. You can’t trust any one outside our nation as we did before the war breakout. You have to have faith in our great nation and trust in our strong military. We have the biggest and strongest ships so don’t hesitant to use them, but beware of our week squadrons don’t match enemies squadrons.");
      /*  tempFaction.setBuildOrbitalStructureCostBase(8);
        tempFaction.setBuildOrbitalStructureCostMulitplier(0);
        tempFaction.setStartWithSS(false);*/
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        //tempFaction.setAlignment(china);
        tempFaction.setCorruption(tmpCorruption);
        
        gw.addFaction(tempFaction);

        
//  ######## Alliance Of Africa (Grön) ########
//      XXX Alliance Of Africa
        tempFaction = new Faction("Alliance Of Africa",Faction.getColorHexString(0,255,0),alliance);
        
//      Adding Buildings to the faction
        tempBuildings = new Buildings();
        
        tempBuildingType= new BuildingType("Small Wharf", "W1", 4, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(1);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Small Wharf what can build small ships");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Medium Wharf", "W2", 8, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(2);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Medium Wharf what can build small and medium ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Small Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large Wharf", "W3", 16, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(3);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Wharf"));
        tempBuildingType.setDeveloped(false);
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge Africa Wharf", "W5", 30, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Alliance Dock", "AS", 3, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(1);
        tempBuildingType.setClosedPlanetBonus(1);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A dock that gives +1 incom on open and closed planets");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Space Harbour", "SH", 6, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setClosedPlanetBonus(1);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Alliance Dock"));
        tempBuildingType.setDescription("Build this building on the planets and long range ways will be short range between planetes whit spaceports + giving 1 incom on closed planet");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Outpost Of Alliance", "OOA", 2, uniqueBuildingIdCounter);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("A small base that is the first step to colonise the planet.");
        tempBuildingType.setAutoDestructWhenConquered(true);
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Alliance Home Base", "AHom", 20, uniqueBuildingIdCounter);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setOpenPlanetBonus(8);
        tempBuildingType.setClosedPlanetBonus(8);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("The home base is made as a head office. Gives an income of 8.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("City", "Ci", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setOpenPlanetBonus(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("City that gives 2 in trade incom if the planet is open.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Outpost Of Alliance"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Province Capital", "PC", 20, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(5);
        tempBuildingType.setResistanceBonus(2);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Province Capital that gives 5 in incom if the planet is open. Gives bonus to the defending ground force. Only one Province Capital can be build each player.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("City"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Capital", "Ca", 30, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(7);
        tempBuildingType.setResistanceBonus(5);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setFactionUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Capital that gives 7 in incom if the planet is open. Gives a big bonus to the defending ground force. Only one Capital can be build each faction.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Barracks", "Bar", 7, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Barracks to train infantary");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Outpost Of Alliance"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Training Center", "TC", 10, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(3);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Military base to train up to 3 infantarys");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Barracks"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Cannon", "Can", 5, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(50);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A planet based Cannon to shoot down small ships what besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Heavy Cannon", "HC", 15, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(250);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A planet based heavy Cannon to shoot down ships what besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Cannon"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Planet Defender Cannon", "PDC", 20, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(500);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A planet based huge sized Cannon to shoot down ships what besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Heavy Cannon"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Basic Shield", "BS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A small shield to defende planets from bombardment.");
        tempBuildings.addBuilding(tempBuildingType);
        
        // ersätter Basic Shield
        tempBuildingType= new BuildingType("Planet Shield", "PS", 15, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A shield to defende planets from bombardment.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Basic Shield"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Factory", "Fa", 12, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setTechBonus(10);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("A factory give better parts to build units with.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Tank Factory", "TF", 10, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.ARMORED);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("In this buiding armored ground units can be build.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("University", "Un", 20, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Assassin"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Spy"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Designer"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Ace"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Captain"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Commander"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("General"));
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A University to train VIPs");
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Alliance Home Base"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Wharf"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Barracks"));
        
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
// ###### TROOPS ######
        
        
        tt = new TroopType("Africa Light Infantry","ALI",100,1,3,uTIC,9,5);
        tt.setDescription("Cheap light infantry unit often used to defend planetes.");
        tt.setShortDescription("Cheap light infantry unit.");
        tt.setAdvantages("Cheap");
        tt.setDisadvantages("Weak against armor and air.");
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Africa Light Infantry"));
        
        tt = new TroopType("Africa Infantry","AI",120,1,4,uTIC,10,9);
        tt.setDescription("Cheap infantry unit often used to defend planetes.");
        tt.setShortDescription("Cheap infantry unit.");
        tt.setAdvantages("Cheap");
        tt.setDisadvantages("Weak");
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Africa Infantry"));
        
        tt = new TroopType("Africa Mobil Infantry","AMI",80,1,5,uTIC,10,5);
        tt.setDescription("Infantry unit often used to flank.");
        tt.setShortDescription("Cheap infantry unit.");
        tt.setAdvantages("Flanker");
        tt.setDisadvantages("Weak against tanks and can't take heavy hits.");
        tt.setCanBuild(false);
        tt.setDefaultPosition(BattleGroupPosition.FLANKER);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Africa Mobil Infantry"));
        
        tt = new TroopType("Africa Mobil Infantry II","AMI2",110,1,7,uTIC,18,8);
        tt.setDescription("Infantry unit often used to flank.");
        tt.setShortDescription("Infantry unit often used to flank.");
        tt.setAdvantages("Flanker");
        tt.setDisadvantages("Weak against tanks.");
        tt.setCanBuild(false);
        tt.setDefaultPosition(BattleGroupPosition.FLANKER);
        tt.setBlackmarketFirstTurn(14);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Africa Mobil Infantry II"));
        
        tt = new TroopType("Africa Antitank Infantry","AAI",100,1,6,uTIC,8,20);
        tt.setDescription("Infantry unit used againt armor.");
        tt.setShortDescription("Cheap infantry unit.");
        tt.setAdvantages("Good against tanks");
        tt.setDisadvantages("The tank cannos cost to buy.");
        tt.setCanBuild(false);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Africa Antitank Infantry"));
        
        tt = new TroopType("Africa Heavy Infantry","AHI",150,2,6,uTIC,17,17);
        tt.setDescription("Heavy infantry unit.");
        tt.setShortDescription("Heavy infantry unit.");
        tt.setAdvantages("");
        tt.setDisadvantages("");
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Africa Heavy Infantry"));
        
        tt = new TroopType("Africa Heavy Infantry II","AHI2",160,2,7,uTIC,30,20);
        tt.setDescription("Heavy infantry unit.");
        tt.setShortDescription("Heavy infantry unit.");
        tt.setAdvantages("");
        tt.setDisadvantages("");
        tt.setCanBuild(false);
        tt.setBlackmarketFirstTurn(14);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Africa Heavy Infantry II"));
        
        tt = new TroopType("Africa Fanatics","AF",50,2,7,uTIC,75,60);
        tt.setDescription("A group of fanatics that will give theirs life for the their leader.");
        tt.setShortDescription("A small unit that can cause great damage.");
        tt.setAdvantages("Make great damage.");
        tt.setDisadvantages("Can't take mutch damage.");
        tt.setPlayerUnique(true);
        tt.setCanBuild(false);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Africa Fanatics"));
        
        
        tt = new TroopType("Africa Heavy Antitank Tank","AHAtT",260,2,15,uTIC,20,40);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setDescription("One extreme heavy and big tank equipped with a great antitank cannon. To heavy to travel in spaceships.");
        tt.setShortDescription("One heavy antitank tank, planet stationed.");
        tt.setAdvantages("Good versus tanks units.");
        tt.setDisadvantages("Planet stationed");
        tt.setDefaultPosition(BattleGroupPosition.FIRST_LINE);
        tt.setSpaceshipTravel(false);
        tt.setCanBuild(false);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Africa Heavy Antitank Tank"));
        
        tt = new TroopType("Africa Tanks","AT",130,3,12,uTIC,20,40);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setDescription("Allraound tank unit.");
        tt.setShortDescription("Allraound tank unit");
        tt.setAdvantages("Good against armored units");
        tt.setDisadvantages("");
        tt.setCanBuild(false);
        tt.setBlackmarketFirstTurn(6);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Africa Tanks"));
        
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Africa Light Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Africa Light Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Africa Heavy Infantry"));
        
        
        // Defence platforms
        // -----------------
        // Alliance Defender
        tempsst = new SpaceshipType("Alliance Defender","ADef",SpaceshipType.SIZE_LARGE,75,370,SpaceshipRange.NONE,4,30,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsMaxSalvoesMedium(10);
        tempsst.setWeaponsStrengthLarge(30);
        tempsst.setWeaponsMaxSalvoesLarge(12);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setIncreaseInitiative(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This large planet defender ship was developed for protection against large and medium capital ships. The ship is very cheap to have because the lack of hyper engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Alliance Defender"));
        
        // Alliance Home Defender
        tempsst = new SpaceshipType("A Home Defender","AHD",SpaceshipType.SIZE_LARGE,20,270,SpaceshipRange.NONE,0,50,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(45);
        tempsst.setArmorMedium(20);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(250);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setIncreaseInitiative(7);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setPlayerUnique(true);
        tempsst.setDescription("This large planet home defender offers good protections against medium sized ships. The ship hasn’t any hyper engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("A Home Defender"));
        
        // Squadrons
        // ---------
        // Alliance Fighter
        tempsst = new SpaceshipType("Alliance Fighter","A-F",SpaceshipType.SIZE_SMALL,7,20,SpaceshipRange.NONE,1,3,uniqueShipIdCounter,5,17);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setBluePrintFirstTurn(15);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setDescription("This ship is probably the best small support dog fighters in the whole world. Cheap and lot of fire power for the money.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Alliance Fighter"));
        
        // Alliance Bomber
        tempsst = new SpaceshipType("Alliance Bomber","A-B",SpaceshipType.SIZE_SMALL,7,15,SpaceshipRange.NONE,1,3,uniqueShipIdCounter,15,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(22);
        tempsst.setWeaponsMaxSalvoesMedium(1);
        tempsst.setWeaponsStrengthLarge(5);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setBluePrintFirstTurn(15);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setDescription("One of the best small anti capital fighters in the whole world. Cheap and lot of fire power for the money.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Alliance Bomber"));
        
        //Alliance Attacker
        tempsst = new SpaceshipType("Alliance Attacker","A-A",SpaceshipType.SIZE_SMALL,7,30,SpaceshipRange.NONE,2,4,uniqueShipIdCounter,8,11);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(12);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setBluePrintFirstTurn(20);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setDescription("A good all-round support fighter. This fighter is often used then the enemies’ fleets are unscouted.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Alliance Attacker"));
        
        //Assault Fighter
        tempsst = new SpaceshipType("Assault Fighter","AFig",SpaceshipType.SIZE_SMALL,12,40,SpaceshipRange.SHORT,2,8,uniqueShipIdCounter,10,28);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setInitSupport(true);
        tempsst.setIncreaseInitiative(2);
        tempsst.setAvailableToBuild(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Extremely powerful dog fighter with capacity to do short range jumps. This ship needs a hangar or a friendly plant to survive");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Assault Fighter"));
        
        //Assault Bomber
        tempsst = new SpaceshipType("Assault Bomber","ABom",SpaceshipType.SIZE_SMALL,5,45,SpaceshipRange.SHORT,3,8,uniqueShipIdCounter,10,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(16);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(15);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setWeaponsStrengthHuge(20);
        tempsst.setWeaponsMaxSalvoesHuge(1);
        tempsst.setInitSupport(true);
        tempsst.setIncreaseInitiative(3);
        tempsst.setAvailableToBuild(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Powerful anti capital fighter with capacity to do short range jumps. This ship needs a hangar or a friendly plant to survive.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Assault Bomber"));
        
        //Ace Attacker
        tempsst = new SpaceshipType("Ace Attacker","AceA",SpaceshipType.SIZE_SMALL,10,55,SpaceshipRange.LONG,1,14,uniqueShipIdCounter,14,27);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(18);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(8);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setInitSupport(true);
        tempsst.setIncreaseInitiative(7);
        tempsst.setPlayerUnique(true);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("The leader ship of the fleet. The greatest fighter in the world with capacity to do long range jumps. Because the extremely threat this fighters is it gives the owner a good advantage. This ship needs a hangar or a friendly plant to survive.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Ace Attacker"));
        
        //Assault Attacker
        tempsst = new SpaceshipType("Assault Attacker","AAtt",SpaceshipType.SIZE_SMALL,10,50,SpaceshipRange.LONG,3,14,uniqueShipIdCounter,12,24);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(16);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(7);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setInitSupport(true);
        tempsst.setIncreaseInitiative(5);
        tempsst.setAvailableToBuild(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Inspired of Ace Attacker this ship developed. This ship needs a hangar or a friendly plant to survive.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Assault Attacker"));
        
        // Capital ships
        // -------------
        // Shuttle
        tempsst = new SpaceshipType("Shuttle","Shu",SpaceshipType.SIZE_SMALL,0,10,SpaceshipRange.SHORT,2,5,uniqueShipIdCounter,2,2);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setPlanetarySurvey(true);
        tempsst.setCanBlockPlanet(false);
        tempsst.setDescription("A small civilian person transport shuttle that is rebuild to a scout/spy ship. This ship is just for scouting and not at all for fights. It’s good to have in the fleet to scout out the planets defence.");
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Shuttle"));
        
        // Troop Ship
        tempsst = new SpaceshipType("Troop Ship","TShi",SpaceshipType.SIZE_MEDIUM,5,20,SpaceshipRange.LONG,2,7,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setTroopCapacity(3);
        tempsst.setScreened(true);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This is the main trop transport ship with very poor fire power, but good manoeuvre capacity make this ship to long range class ship.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Ship"));
        
        // Liberty Carrier
        tempsst = new SpaceshipType("Liberty Carrier","LCar",SpaceshipType.SIZE_MEDIUM,20,90,SpaceshipRange.LONG,5,14,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(5);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setArmorSmall(40);
        tempsst.setSquadronCapacity(3);
        tempsst.setSupply(4);
        tempsst.setDescription("A medium carrier and supply ship with enough fire power to hold smaller ships away. But the real fire power is in the squadrons. This is one of the greatest and most imported ship in the long range fleet.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Liberty Carrier"));
        
        // AOA Carrier
        tempsst = new SpaceshipType("AOA Carrier","ACar",SpaceshipType.SIZE_MEDIUM,10,50,SpaceshipRange.LONG,6,16,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(25);
        tempsst.setSquadronCapacity(5);
        tempsst.setScreened(true);
        tempsst.setDescription("A low armed medium carrier. Can take 5 squadrons.");
        tempsst.setAvailableToBuild(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("AOA Carrier"));
        
        // Assault Ship
        tempsst = new SpaceshipType("Assault Ship","AShi",SpaceshipType.SIZE_LARGE,75,375,SpaceshipRange.SHORT,11,37,uniqueShipIdCounter,13,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(35);
        tempsst.setWeaponsMaxSalvoesLarge(16);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setBombardment(1);
        tempsst.setSquadronCapacity(8);
        tempsst.setIncreaseInitiative(2);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(30);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(60);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setDescription("A large short range capital ship with up to 8 squadrons in the hangar. Good against all types of ship without huge capital ships. It has bombardment capacity. The hangar in the ship takes so much space so it's no room for any ground troops.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Assault Ship"));
        
        // Conqueror
        tempsst = new SpaceshipType("Conqueror","conq",SpaceshipType.SIZE_LARGE,0,300,SpaceshipRange.SHORT,10,40,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setBombardment(3);
        tempsst.setTroopCapacity(5);
        tempsst.setScreened(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(50);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("The Conqueror is construct to ower take planets. Powerful bombardment capacity and room for 5 companys of troops. Should not act in space battles.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Conqueror"));
        
        // Galaxy WarCarrier
        tempsst = new SpaceshipType("Galaxy WarCarrier","GWCa",SpaceshipType.SIZE_HUGE,105,675,SpaceshipRange.SHORT,24,96,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(38);
        tempsst.setWeaponsMaxSalvoesLarge(20);
        tempsst.setWeaponsStrengthHuge(200);
        tempsst.setWeaponsMaxSalvoesHuge(1);
        tempsst.setArmorSmall(85);
        tempsst.setArmorMedium(60);
        tempsst.setArmorLarge(25);
        tempsst.setArmorHuge(10);
        tempsst.setBombardment(2);
        tempsst.setTroopCapacity(3);
        tempsst.setIncreaseInitiative(10);
        tempsst.setInitDefence(2);
        tempsst.setSquadronCapacity(16);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(100);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.RARE);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("The proud of the navy. With the powerful torpedoes against huge ship and capacity to carry 16 squadrons this was the answer against the threat from enemies’ huge capital ships. Great bombardment and a battalion of soldier make this to a great planet over taker");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galaxy WarCarrier"));
        
        // ****** fraction unique property ******
        
        // Create orbital structures
     /*   os = new SpaceStation();
        os.setSpaceport(true);
        os.setOpenProdBonus(1);
        os.setClosedProdBonus(1);
        tempFaction.setOrbitalStructure(os);*/
        
        VIPType President = new VIPType("President","Pre",neutral,uniqueVIPIdCounter);
        President.setGovernor(true);
        President.setWellGuarded(true);
        President.setOpenIncBonus(1);
        President.setCanVisitNeutralPlanets(true);
        President.setDiplomat(true);
        President.setFrequency(BlackMarketFrequency.NEVER);
        President.setDescription("The President is the leader of the faction so protect him. He gives one extra income on open planets. He have also diplomatic skill to convince neutral planets to join he’s faction.");
        gw.addVipType(President);
        tempFaction.setGovernorVIPType(President);
        
        tempFaction.addStartingVIPType(gw.getVIPTypeByName("Ace"));
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Troop Ship"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Liberty Carrier"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Ace Attacker"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("A Home Defender"));
        
        // *******************************************************************************
        // ****************************** --- Forskning --- ******************************
        // *******************************************************************************
        
        tempResearch = new Research();
        
        // ************************* --- Forskning Economic --- *************************
        
        researchCorruption1 = new ResearchAdvantage("Corruption level 6","Lower corruption");
		researchCorruption1.setCorruption(tmpCorruption6);
		researchCorruption1.setTimeToResearch(5);
		researchCorruption1.setCostToResearchOneTurnInProcent(10);
        
        researchCorruption2 = new ResearchAdvantage("Corruption level 5","Lower corruption");
        researchCorruption2.setCorruption(tmpCorruption5);
        researchCorruption2.setTimeToResearch(5);
        researchCorruption2.setCostToResearchOneTurnInProcent(10);
        researchCorruption2.addChild(researchCorruption1);
        
        researchCorruption3 = new ResearchAdvantage("Corruption level 4","Lower corruption");
        researchCorruption3.setCorruption(tmpCorruption4);
        researchCorruption3.setTimeToResearch(3);
        researchCorruption3.setCostToResearchOneTurnInProcent(20);
        researchCorruption3.addChild(researchCorruption2);
        
        researchCorruption4 = new ResearchAdvantage("Corruption level 3","Lower corruption");
        researchCorruption4.setCorruption(tmpCorruption3);
        researchCorruption4.setTimeToResearch(3);
        researchCorruption4.setCostToResearchOneTurnInProcent(20);
        researchCorruption4.addChild(researchCorruption3);
        
        researchCorruption5 = new ResearchAdvantage("Corruption level 2","Lower corruption");
        researchCorruption5.setCorruption(tmpCorruption2);
        researchCorruption5.setTimeToResearch(3);
        researchCorruption5.setCostToResearchOneTurnInProcent(20);
        researchCorruption5.addChild(researchCorruption4);
        
        researchCorruption6 = new ResearchAdvantage("Corruption level 1","Lower corruption");
        researchCorruption6.setCorruption(tmpCorruption1);
        researchCorruption6.setTimeToResearch(3);
        researchCorruption6.setCostToResearchOneTurnInProcent(20);
        researchCorruption6.addChild(researchCorruption5);
        
        tempResearch.addAdvantage(researchCorruption1);
        tempResearch.addAdvantage(researchCorruption2);
        tempResearch.addAdvantage(researchCorruption3);
        tempResearch.addAdvantage(researchCorruption4);
        tempResearch.addAdvantage(researchCorruption5);
        tempResearch.addAdvantage(researchCorruption6);
        
        tempResearchAdvantage = new ResearchAdvantage("Economic","Develop Economic");
        tempResearchAdvantage.addChild(researchCorruption6);
        //tempResearchAdvantage.addChild(tempResearch.getAdvantage("Trading I"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // Capital
        tempResearchAdvantage = new ResearchAdvantage("Capital","Gives Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Capital"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Province Capital
        tempResearchAdvantage = new ResearchAdvantage("Province Capital","Gives Province Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Capital"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Space Harbour
        tempResearchAdvantage = new ResearchAdvantage("Space Harbour","Gives Space Harbour");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Space Harbour"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // University
        tempResearchAdvantage = new ResearchAdvantage("University","Gives University");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("University"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        
        // Infrastructure 3
        tempResearchAdvantage = new ResearchAdvantage("Infrastructure III","Shields will give one in incom on open planets.");
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Basic Shield");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Planet Shield");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Infrastructure 2
        tempResearchAdvantage = new ResearchAdvantage("Infrastructure II","Cannon will give one in incom on open planets.");
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Cannon");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Heavy Cannon");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Planet Defender Cannon");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infrastructure III"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Infrastructure
        tempResearchAdvantage = new ResearchAdvantage("Infrastructure", "Better tax control.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("City");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Province Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Province Capital"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infrastructure II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Space Harbour"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("University"));
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // Planet Defender Cannon
        tempResearchAdvantage = new ResearchAdvantage("Planet Defender Cannon","Gives Planet Defender Cannon");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Planet Defender Cannon"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Heavy Cannon
        tempResearchAdvantage = new ResearchAdvantage("Heavy Cannon","Gives Heavy Cannon");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Heavy Cannon"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Planet Defender Cannon"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Cannon
        tempResearchAdvantage = new ResearchAdvantage("Cannon","Gives Cannon");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Cannon"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Heavy Cannon"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Planet Shield
        tempResearchAdvantage = new ResearchAdvantage("Planet Shield","Gives Planet Shield");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Planet Shield"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Basic Shield
        tempResearchAdvantage = new ResearchAdvantage("Basic Shield","Gives Basic Shield");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Basic Shield"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Planet Shield"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Planet defence
        tempResearchAdvantage = new ResearchAdvantage("Planet defence", "Platfrom to begin develop on planet defence.");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Basic Shield"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Cannon"));
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        // Africa Fanatics
        tempResearchAdvantage = new ResearchAdvantage("Africa Fanatics","Gives Africa Fanatics");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Africa Fanatics"));
        tempResearchAdvantage.setTimeToResearch(0);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Africa Mobil Infantry II
        tempResearchAdvantage = new ResearchAdvantage("Africa Mobil Infantry II","Gives Africa Mobil Infantry II");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Africa Mobil Infantry II"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Africa Mobil Infantry"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Africa Mobil Infantry
        tempResearchAdvantage = new ResearchAdvantage("Africa Mobil Infantry","Gives Africa Mobil Infantry");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Africa Mobil Infantry"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Africa Mobil Infantry II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Africa Fanatics"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Africa Heavy Infantry II
        tempResearchAdvantage = new ResearchAdvantage("Africa Heavy Infantry II","Gives Africa Heavy Infantry II");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Africa Heavy Infantry II"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Africa Heavy Infantry"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Africa Infantry
        tempResearchAdvantage = new ResearchAdvantage("Africa Infantry","Gives Africa Infantry");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Africa Infantry"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Africa Light Infantry"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Africa Heavy Infantry II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Africa Fanatics"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Africa Antitank Infantry
        tempResearchAdvantage = new ResearchAdvantage("Africa Antitank Infantry","Gives Africa Antitank Infantry");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Africa Antitank Infantry"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Infantry
        tempResearchAdvantage = new ResearchAdvantage("Infantry","Gives Training Center");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Training Center"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Africa Antitank Infantry"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Africa Infantry"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Africa Mobil Infantry"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Africa Heavy Antitank Tank
        tempResearchAdvantage = new ResearchAdvantage("Africa Heavy Antitank Tank","Gives Africa Heavy Antitank Tank");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Africa Heavy Antitank Tank"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Tanks
        tempResearchAdvantage = new ResearchAdvantage("Tanks","Gives Tank Factory and Africa Tanks");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Tank Factory"));
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Africa Tanks"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Africa Heavy Antitank Tank"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Army
        tempResearchAdvantage = new ResearchAdvantage("Army", "Platfrom to begin develop on ground units.");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infantry"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Tanks"));
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        tempResearchAdvantage = new ResearchAdvantage("Better squadrons","Gives greater squadrons");
        tempResearchAdvantage.setTimeToResearch(4);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        researchUpgradeShip = new ResearchUpgradeShip("Alliance Fighter");
        researchUpgradeShip.setShields(3);
        tempResearchAdvantage.addResearchUpgradeShip(researchUpgradeShip);
        researchUpgradeShip = new ResearchUpgradeShip("Alliance Bomber");
        researchUpgradeShip.setShields(3);
        tempResearchAdvantage.addResearchUpgradeShip(researchUpgradeShip);
        researchUpgradeShip = new ResearchUpgradeShip("Alliance Attacker");
        researchUpgradeShip.setShields(3);
        tempResearchAdvantage.addResearchUpgradeShip(researchUpgradeShip);
        researchUpgradeShip = new ResearchUpgradeShip("Assault Fighter");
        researchUpgradeShip.setShields(3);
        tempResearchAdvantage.addResearchUpgradeShip(researchUpgradeShip);
        researchUpgradeShip = new ResearchUpgradeShip("Assault Bomber");
        researchUpgradeShip.setShields(3);
        tempResearchAdvantage.addResearchUpgradeShip(researchUpgradeShip);
        researchUpgradeShip = new ResearchUpgradeShip("Assault Attacker");
        researchUpgradeShip.setShields(3);
        tempResearchAdvantage.addResearchUpgradeShip(researchUpgradeShip);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // Alliance Attacker
        tempResearchAdvantage = new ResearchAdvantage("Alliance Attacker","Gives Alliance Attacker");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Alliance Attacker"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Better squadrons"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Assault Attacker
        tempResearchAdvantage = new ResearchAdvantage("Assault Attacker","Gives Assault Attacker");
        tempResearchAdvantage.setTimeToResearch(0);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Assault Attacker"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Assault Fighter
        tempResearchAdvantage = new ResearchAdvantage("Assault Fighter","Gives Assault Fighter");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Assault Fighter"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Assault Attacker"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Assault Bomber
        tempResearchAdvantage = new ResearchAdvantage("Assault Bomber","Gives Assault Bomber");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Assault Bomber"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Assault Attacker"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Squadrons
        tempResearchAdvantage = new ResearchAdvantage("Squadrons", "Platfrom to begin develop on Squadrons.");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Assault Bomber"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Assault Fighter"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Alliance Attacker"));
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        // Conqueror
        tempResearchAdvantage = new ResearchAdvantage("Conqueror","Gives Conqueror");
        tempResearchAdvantage.setTimeToResearch(4);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Conqueror"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Assault Ship
        tempResearchAdvantage = new ResearchAdvantage("Assault Ship","Gives Assault Ship");
        tempResearchAdvantage.setTimeToResearch(4);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Assault Ship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Galaxy WarCarrier
        tempResearchAdvantage = new ResearchAdvantage("Galaxy WarCarrier","Gives Galaxy WarCarrier");
        tempResearchAdvantage.setTimeToResearch(5);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Galaxy WarCarrier"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Huge Wharf
        tempResearchAdvantage = new ResearchAdvantage("Huge Wharf","Gives Huge Africa Wharf");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Huge Africa Wharf"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Galaxy WarCarrier"));
        tempResearchAdvantage.setTimeToResearch(5);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Large Wharf
        tempResearchAdvantage = new ResearchAdvantage("Large Wharf","Gives Large Wharf");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Assault Ship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Conqueror"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Huge Wharf"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // AOA Carrier
        tempResearchAdvantage = new ResearchAdvantage("AOA Carrier","Gives AOA Carrier");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("AOA Carrier"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Fleet
        tempResearchAdvantage = new ResearchAdvantage("Fleet", "Platfrom to begin develop on the fleet.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("AOA Carrier"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Large Wharf"));
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        tempResearch.setNumberOfSimultaneouslyResearchAdvantages(2);
        
        tempFaction.setResearch(tempResearch);
        
        // *******************************************************************************
        // *******************************************************************************
        // *******************************************************************************
        
        
        //tempFaction.setResistanceBonus();
        // tempFaction.setStartingWharfSize(OrbitalWharf.MAX_SIZE_SMALL);
        
        tempFaction.setAdvantages("Great squadrons");
        tempFaction.setDisadvantages("Lack of artillery");
        tempFaction.setHistory("In the middle of the 21 century the nation of Africa was started to talk about the idea to start a federation to get out in to the space. At 2084 same countries from Africa founded the Alliance of Africa. The main course was to build up a space fleet to defending the interest and get for resources from the space. As soon the first hyper drives were out on the market we started to rebuild old big ships with hyper drives to start new colonies to avoid the starving around out people. This type of trips was very risky but we didn’t have any choice. It took some years before the first ships went back with food and for pick up new volunteers.\nThen we started to lost ships and colonies around 2171 we started to build up a military fleet. To build up fire power as fast as possible we began to develop squadrons to put on our ships. That was a great decision and we shot down a lot o hostile ships. But the year 2230 the pirates started to attack with large ship and own squadrons. To respond to the new threat the started to build up a military fleet with big capital ships carrying squads. But the fire power is still in our good squadrons.\nAt 2240 some of our colonies in the out space went over to the pirate called Federation of liberty.  At 2242 USA attacked us with a massive nuclear attacked and destroyed and killed all humans in Africa on the earth. We don’t rely know way but USA has always accused us for supporting religious military fanatics. But we have to say that it was not just USA that attacked us that week. Who started this war and way? I guess we never are going to found that out that but one thing is sure, we can’t rely on any one. Hit hard before they hit you and build up the fleet to get military domination. We have to try to connect our colonies to secure a good life. Beware of hostiles factions");
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setCorruption(tmpCorruption);
       // tempFaction.setAlignment(alliance);
        gw.addFaction(tempFaction);

        

//  ######## Trade Federation (Blå) ########
//      XXX Trade Federation        
        tempFaction = new Faction("Trade Federation",Faction.getColorHexString(24,66,255),trade);
        
//      Adding Buildings to the faction
        tempBuildings = new Buildings();
        
                
        tempBuildingType= new BuildingType("Dock", "TD", 3, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(1);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A small ship dock that gives +1 incom on open planets");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Spaceport", "SP", 8, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Build this building on the planets and long range ways will be short range between planetes whit spaceports");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Dock"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Small Wharf", "W1", 2, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(1);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Small Wharf what can build small ships");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Medium Wharf", "W2", 12, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(2);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Medium Wharf what can build small and medium ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Small Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large Wharf", "W3", 18, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(3);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge Trade Wharf", "W5", 25, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Civilian Outpost", "CO", 2, uniqueBuildingIdCounter);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("A small base with civilians.");
        tempBuildingType.setAutoDestructWhenConquered(true);
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Mercenary Camp", "MT", 10, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Base to hire mercenarys");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Civilian Outpost"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Mercenary Tavern", "MB", 10, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(3);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Base to hire mercenarys");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Mercenary Camp"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("City", "Ci", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setOpenPlanetBonus(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("City that gives 2 in trade incom if the planet is open.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Civilian Outpost"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Province Capital", "PC", 20, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(5);
        tempBuildingType.setResistanceBonus(2);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Province Capital that gives 5 in incom if the planet is open. Gives bonus to the defending ground force. Only one Province Capital can be build each player.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("City"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Capital", "Ca", 30, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(7);
        tempBuildingType.setResistanceBonus(5);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setFactionUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Capital that gives 7 in incom if the planet is open. Gives a big bonus to the defending ground force. Only one Capital can be build each faction.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("School", "Sc", 15, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        //tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Assassin"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Spy"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Designer"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Ace"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Captain"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Commander"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("General"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Negotiator"));
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A school to train VIPs");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Civilian Outpost"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Trade Home Base", "FHom", 20, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(9);
        tempBuildingType.setClosedPlanetBonus(9);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDescription("The home base is made as a head office. Gives an income of 9.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Company Base", "CB", 2, uniqueBuildingIdCounter);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("A small base used as a ground to build more advanced buildigns.");
        tempBuildingType.setAutoDestructWhenConquered(true);
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Factory", "Fa", 12, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setTechBonus(10);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("A factory give better parts to build units with.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Mech Construction Yard", "MCY", 4, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.ARMORED);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("In this buiding armored ground units can be build.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Mech Factory", "MF", 4, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(2);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.ARMORED);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.SUPPORT);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Armored and support troops can be build in big number at this building");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Mech Construction Yard"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Bunker", "Bu", 5, uniqueBuildingIdCounter);
        tempBuildingType.setResistanceBonus(3);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A Bunker to boost the defending troops defending skill.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Basic Shield", "BS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A small shield to defende planets from bombardment.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        // ersätter Basic Shield
        tempBuildingType= new BuildingType("Planet Shield", "PS", 15, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A shield to defende planets from bombardment.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Cannon", "Can", 5, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(50);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A planet based Cannon to shoot down small ships.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Puls Cannon", "PC", 15, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(50);
        tempBuildingType.setCannonRateOfFire(3);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A planet based Cannon used to shoot down small ships in great numbers.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Cannon"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Heavy Cannon", "HC", 15, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(250);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A planet based heavy Cannon to shoot down ships what besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Cannon"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Trade Home Base"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Wharf"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Civilian Outpost"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Company Base"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Mech Construction Yard"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
        
        
// ###### TROOPS ######
        
        
        tt = new TroopType("Mechs","Mec",110,1,5,uTIC,15,5);
        tt.setDescription("Light mashingun mechs unit used to defend planetes and is fast so they can flank the enemy. Controled by a super computer and control center in a bunker on the planet that makes this to a stationed planet troop");
        tt.setShortDescription("Cheap armor unit that can flank.");
        tt.setAdvantages("Cheap and flankers.");
        tt.setDisadvantages("Weak against armor. Can't do spacetravel.");
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        tt.setDefaultPosition(BattleGroupPosition.FLANKER);
        tt.setSpaceshipTravel(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Mechs"));
        
        tt = new TroopType("Miniguns Mechs","MM",110,1,10,uTIC,25,5);
        tt.setDescription("Miniguns mechs unit used to defend planetes. Controled by a super computer and control center in a bunker on the planet that makes this to a stationed planet troop");
        tt.setShortDescription("Good armor unit against infantry and air.");
        tt.setAdvantages("Make heavy infantry.");
        tt.setDisadvantages("Weak against armor. Can't do spacetravel.");
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        tt.setSpaceshipTravel(false);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Miniguns Mechs"));
        
        tt = new TroopType("Cannon Mechs","CM",120,1,10,uTIC,10,25);
        tt.setDescription("Cannon mechs unit used to defend planetes against armor. Controled by a super computer and control center in a bunker on the planet that makes this to a stationed planet troop");
        tt.setShortDescription("Good armor unit against armor.");
        tt.setAdvantages("Make heavy armor damge.");
        tt.setDisadvantages("Can't do spacetravel.");
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        tt.setSpaceshipTravel(false);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Cannon Mechs"));
        
        tt = new TroopType("Artillery Mechs","AM",70,1,8,uTIC,2,8);
        tt.setDescription("Artillery mechs unit used to support first line troops in defending the planet. Controled by a super computer and control center in a bunker on the planet that makes this to a stationed planet troop");
        tt.setShortDescription("Artillery.");
        tt.setAdvantages("Artillery.");
        tt.setDisadvantages("Weak in close combat. Can't do spacetravel.");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(30);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        tt.setSpaceshipTravel(false);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Artillery Mechs"));
        
        // Kanske skall en robot enhet läggas till som kan göra spacetravel och som skall komma i slutet av forskningen. Så att trade får tanks att anfalla med så att det inte blir tok lätt att försvara sig mot dem. (möjligt att en support enhet också skall läggas till)
        
        
        
        tt = new TroopType("Mercenary Infantry","MI",100,2,8,uTIC,16,12);
        tt.setDescription("Mercenary infantry unit.");
        tt.setShortDescription("Mercenary infantry unit.");
        tt.setAdvantages("");
        tt.setDisadvantages("Expensive.");
        tt.setCanBuild(true);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Mercenary Infantry"));
        
        tt = new TroopType("Mercenary HI","MHI",140,3,10,uTIC,26,20);
        tt.setDescription("Heavy infantry unit.");
        tt.setShortDescription("Heavy infantry unit.");
        tt.setAdvantages("Good all-round unit");
        tt.setDisadvantages("Expensive.");
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Mercenary HI"));
        
        tt = new TroopType("Mercenary HAI","MHAI",130,3,10,uTIC,10,40);
        tt.setDescription("Heavy Anittank infantry.");
        tt.setShortDescription("Heavy Anittank infantry.");
        tt.setAdvantages("Good against armor");
        tt.setDisadvantages("Expensive.");
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Mercenary HAI"));
        
        tt = new TroopType("Mercenary Elite","MeE",130,5,20,uTIC,55,50);
        tt.setDescription("Heavy infantry unit that is the elite of the elite.");
        tt.setShortDescription("Heavy infantry unit that is the elit of the elit.");
        tt.setAdvantages("The strongest mercenary infantary.");
        tt.setDisadvantages("Can only be one of them.");
        tt.setWorldUnique(true);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Mercenary Elite"));
        
        
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Mechs"));     
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Mechs")); 
        
        // Trade Federation Defence platforms
        // -----------------
        // F Defender
        tempsst = new SpaceshipType("F Defender","FDef",SpaceshipType.SIZE_LARGE,75,370,SpaceshipRange.NONE,4,36,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(15);
        tempsst.setWeaponsStrengthLarge(20);
        tempsst.setWeaponsMaxSalvoesLarge(10);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setIncreaseInitiative(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This large planet defender ship was developed for protection against large and medium capital ships. The ship is very cheap to have because the lack of hyper engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("F Defender"));
        
        // F Home Defender
        tempsst = new SpaceshipType("F Home Defender","FHD",SpaceshipType.SIZE_HUGE,20,540,SpaceshipRange.NONE,0,50,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(45);
        tempsst.setArmorMedium(20);
        tempsst.setWeaponsStrengthLarge(250);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setIncreaseInitiative(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setPlayerUnique(true);
        tempsst.setDescription("This huge planet defender base offers protections against small sized ships. The ship hasn’t any hyper engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("F Home Defender"));
        
        // Squadrons
        // ---------
        // Interceptor
        tempsst = new SpaceshipType("Interceptor","Int",SpaceshipType.SIZE_SMALL,0,22,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,5,13);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setDescription("A good and cheap dog fighters, made by AAD.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interceptor"));
        
        // Interceptor II
        tempsst = new SpaceshipType("Interceptor II","Int2",SpaceshipType.SIZE_SMALL,5,25,SpaceshipRange.NONE,1,4,uniqueShipIdCounter,7,20);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setDescription("The next generation dog fighter, made by AAD.");
        tempsst.setBlackmarketFirstTurn(6);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interceptor II"));
        
        // B-12
        tempsst = new SpaceshipType("B-12","B12",SpaceshipType.SIZE_SMALL,0,15,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,10,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(5);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setDescription("A good and cheap anti capital fighter, made by AAD");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("B-12"));
        
        // B-21
        tempsst = new SpaceshipType("B-21","B21",SpaceshipType.SIZE_SMALL,0,25,SpaceshipRange.NONE,1,4,uniqueShipIdCounter,10,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(15);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setBlackmarketFirstTurn(6);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("The next generation anti capital fighter, made by AAD");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("B-21"));
        
        // B-55
        tempsst = new SpaceshipType("B-55","B55",SpaceshipType.SIZE_SMALL,0,20,SpaceshipRange.NONE,1,4,uniqueShipIdCounter,5,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthLarge(15);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setWeaponsStrengthHuge(30);
        tempsst.setWeaponsMaxSalvoesHuge(2);
        tempsst.setBlackmarketFirstTurn(20);
        tempsst.setBluePrintFirstTurn(20);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("The next generation anti capital fighter. Construct to take down huge and large capital ships. All armd against medium capital ships was removed to get room for the big torpedo.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("B-55"));
        
        // A-2
        tempsst = new SpaceshipType("A-2","A2",SpaceshipType.SIZE_SMALL,0,30,SpaceshipRange.SHORT,2,4,uniqueShipIdCounter,5,8);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setDescription("A good all-round support fighter with hyper engine for short range jumps. This fighter is often used to patrol and defending our planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("A-2"));
        
        //A-8
        tempsst = new SpaceshipType("A-8","A8",SpaceshipType.SIZE_SMALL,10,30,SpaceshipRange.SHORT,2,6,uniqueShipIdCounter,10,13);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setDescription("The next generation attackers.");
        tempsst.setBlackmarketFirstTurn(6);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("A-8"));
        
        
        // Capital ships
        // -------------
        // Corvette
        tempsst = new SpaceshipType("Corvette","Crv",SpaceshipType.SIZE_SMALL,10,30,SpaceshipRange.LONG,1,3,uniqueShipIdCounter,8,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setCanBlockPlanet(false);
        tempsst.setDescription("Light small long range capital ship used for exploring and person transports. Build by the company BBZ.");
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        
        // Falcon Frigate
        tempsst = new SpaceshipType("Falcon Frigate","Frg",SpaceshipType.SIZE_SMALL,10,50,SpaceshipRange.LONG,3,7,uniqueShipIdCounter,17,8);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(5);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This small long range ship is one of the newly ships in the fleet. Build too scare away enemies small sized ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Falcon Frigate"));
        
        // Hawk Frigate
        tempsst = new SpaceshipType("Hawk Frigate","HFrg",SpaceshipType.SIZE_SMALL,10,60,SpaceshipRange.LONG,3,7,uniqueShipIdCounter,20,10);
        tempsst.setArmorSmall(5);
        tempsst.setWeaponsStrengthMedium(8);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setAvailableToBuild(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A new version of the Falcon Frigate. Better hull and heavier armd.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Hawk Frigate"));
        
        // Troop Frigate
        tempsst = new SpaceshipType("Troop Frigate","TFri",SpaceshipType.SIZE_MEDIUM,5,30,SpaceshipRange.LONG,2,6,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setTroopCapacity(3);
        tempsst.setScreened(true);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This is the main troop transport ship with very poor fire power but good manoeuvre capacity that's make this ship to long range class ship. The ship is a rebuild civilian personal transport ship.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Frigate"));
        
        // Troop Frigate II
        tempsst = new SpaceshipType("Troop Frigate II","TFr2",SpaceshipType.SIZE_MEDIUM,5,35,SpaceshipRange.LONG,2,8,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(25);
        tempsst.setWeaponsStrengthMedium(5);
        tempsst.setTroopCapacity(4);
        tempsst.setScreened(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setDescription("This is the main military troop transport ship with poor fire power but good manoeuvre capacity that's make this ship to long range class ship. The ship was developed to take more troops and get sheaper transports. Will replace the 'Troop Frigate'");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Frigate II"));
        
        // Carrier
        tempsst = new SpaceshipType("Carrier","Car",SpaceshipType.SIZE_MEDIUM,10,70,SpaceshipRange.LONG,3,12,uniqueShipIdCounter,2,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setArmorSmall(35);
        tempsst.setSquadronCapacity(6);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A medium carrier with enough fire power to hold squadron’s away from the hangars ports. Can take up to 6 squadrons and that's make this ship to a great threat in the battlefield.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier"));
        
        // Supply Frigate
        tempsst = new SpaceshipType("Supply Frigate","SuF",SpaceshipType.SIZE_MEDIUM,5,30,SpaceshipRange.SHORT,1,10,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(25);
        tempsst.setSupply(4);
        tempsst.setPlanetarySurvey(true);
        tempsst.setScreened(true);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Rebuild from a civilian ore miner ship that made it to an excellent supply ship with good survey capacity. But the medium hull and badly flight capacity make it to a short ranger and easy target.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Frigate"));
        
        // Swallow
        tempsst = new SpaceshipType("Swallow","Swa",SpaceshipType.SIZE_MEDIUM,20,110,SpaceshipRange.LONG,5,12,uniqueShipIdCounter,15,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setArmorSmall(40);
        tempsst.setDescription("This destroyer class ship was designed as a heavy escort to our treading fleets against pirates. Big enough to scare away pirates and fast to hunt them down.");
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Swallow"));
        
        // Blackbird
        tempsst = new SpaceshipType("Blackbird","Bla",SpaceshipType.SIZE_MEDIUM,25,130,SpaceshipRange.LONG,5,13,uniqueShipIdCounter,15,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setArmorSmall(40);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(10);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setDescription("A new version of the Swallow. Better hull and heavier armd against medium ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Blackbird"));
                
        //Battleship Independent
        tempsst = new SpaceshipType("Battleship Independent","BaI",SpaceshipType.SIZE_LARGE,75,375,SpaceshipRange.SHORT,11,40,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(40);
        tempsst.setWeaponsMaxSalvoesLarge(16);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setIncreaseInitiative(5);
        tempsst.setSquadronCapacity(6);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(30);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(60);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setDescription("Then the firsts civilian trading fleets that were escorted by Medium Destroyers was attacked or just disappear in the deep space some huge company’s founded the Trade Federation (AC 2478). The first action Trade Federation did was to build up a military fleet to protect their interests and the first ship that was ordered was this Battleship. The ship if so powerful that it's can combat against up to 3 medium capital ships by it self. The carrier capacity is up to 6 squadrons. The hull is made by the BBZ Company and modify by HAC.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship Independent"));
        
        //Battleship Immunity
        tempsst = new SpaceshipType("Battleship Immunity","BatI",SpaceshipType.SIZE_LARGE,90,380,SpaceshipRange.SHORT,11,43,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(40);
        tempsst.setWeaponsMaxSalvoesLarge(16);
        tempsst.setWeaponsStrengthHuge(20);
        tempsst.setWeaponsMaxSalvoesHuge(7);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setIncreaseInitiative(7);
        tempsst.setSquadronCapacity(7);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(40);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(70);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setDescription("A new version of the Battleship. Better shields, armd against huge ships and better squdrons capacity.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship Immunity"));
        
        //Battleship Retaliation
        tempsst = new SpaceshipType("Battleship Retaliation","BatR",SpaceshipType.SIZE_LARGE,90,380,SpaceshipRange.SHORT,11,45,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(40);
        tempsst.setWeaponsMaxSalvoesLarge(16);
        tempsst.setWeaponsStrengthHuge(20);
        tempsst.setWeaponsMaxSalvoesHuge(7);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setIncreaseInitiative(7);
        tempsst.setSquadronCapacity(7);
        tempsst.setTroopCapacity(3);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(40);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(70);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setDescription("A new version of the Battleship Immunity with added troop capacity.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship Retaliation"));
        
        //Galaxy Battleship
        tempsst = new SpaceshipType("Galaxy Battleship","GBat",SpaceshipType.SIZE_HUGE,105,675,SpaceshipRange.SHORT,27,96,uniqueShipIdCounter,10,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(60);
        tempsst.setWeaponsMaxSalvoesLarge(20);
        tempsst.setWeaponsStrengthHuge(60);
        tempsst.setWeaponsMaxSalvoesHuge(14);
        tempsst.setArmorSmall(85);
        tempsst.setArmorMedium(60);
        tempsst.setArmorLarge(25);
        tempsst.setArmorHuge(10);
        tempsst.setIncreaseInitiative(12);
        tempsst.setSquadronCapacity(10);
        tempsst.setTroopCapacity(3);
        tempsst.setInitDefence(2);
        tempsst.setPlanetarySurvey(true);
        tempsst.setBlackmarketFirstTurn(60);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.RARE);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("Construct to secure independence in the war that start at AC 2542 and to give us capacity to overtake planets. The ship has survey capacity to find hidden pirates raiding stronghold. Can by it's self dominate a whole galaxy.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galaxy Battleship"));
        
        //The one
        tempsst = new SpaceshipType("The one","one",SpaceshipType.SIZE_HUGE,170,800,SpaceshipRange.SHORT,20,96,uniqueShipIdCounter,10,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(120);
        tempsst.setWeaponsMaxSalvoesLarge(10);
        tempsst.setWeaponsStrengthHuge(130);
        tempsst.setWeaponsMaxSalvoesHuge(6);
        tempsst.setArmorSmall(85);
        tempsst.setArmorMedium(60);
        tempsst.setArmorLarge(25);
        tempsst.setArmorHuge(10);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setPlayerUnique(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("The presidents flag ship of the fleet and o good blockade breaker. Construct go give maximum power to the big canons. This ship can take down big ships but needs supporting ship to hold squdrons on safe distance.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("The one"));
        
        //    Small trade ship
        tempsst = new SpaceshipType("Small Tradeship","STs",SpaceshipType.SIZE_SMALL,SpaceshipRange.LONG,0,5,uniqueShipIdCounter);
        tempsst.setCivilian(true);
        tempsst.setIncOwnClosedBonus(1);
        tempsst.setIncOwnOpenBonus(2);
        tempsst.setIncFrendlyClosedBonus(2);
        tempsst.setIncFrendlyOpenBonus(4);
        tempsst.setIncNeutralClosedBonus(4);
        tempsst.setIncNeutralOpenBonus(5);
        tempsst.setPlayerUnique(true);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Long range trade ship to get incom to pay the war.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Small Tradeship"));
        
        //  Medium trade ship
        tempsst = new SpaceshipType("Medium Tradeship","MTs",SpaceshipType.SIZE_MEDIUM,SpaceshipRange.LONG,0,8,uniqueShipIdCounter);
        tempsst.setCivilian(true);
        tempsst.setIncOwnClosedBonus(1);
        tempsst.setIncOwnOpenBonus(5);
        tempsst.setIncFrendlyClosedBonus(4);
        tempsst.setIncFrendlyOpenBonus(6);
        tempsst.setIncNeutralClosedBonus(5);
        tempsst.setIncNeutralOpenBonus(7);
        tempsst.setAvailableToBuild(false);
        tempsst.setPlayerUnique(true);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Long range trade ship to get incom to pay the war.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Medium Tradeship"));
        
        //  Large trade ship
        tempsst = new SpaceshipType("Large Tradeship","LTs",SpaceshipType.SIZE_LARGE,SpaceshipRange.SHORT,0,14,uniqueShipIdCounter);
        tempsst.setCivilian(true);
        tempsst.setIncOwnClosedBonus(2);
        tempsst.setIncOwnOpenBonus(7);
        tempsst.setIncFrendlyClosedBonus(6);
        tempsst.setIncFrendlyOpenBonus(7);
        tempsst.setIncNeutralClosedBonus(7);
        tempsst.setIncNeutralOpenBonus(10);
        tempsst.setPlayerUnique(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Short range trade ship to get incom to pay the war.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Large Tradeship"));
        
        //  Huge trade ship
        tempsst = new SpaceshipType("Huge Tradeship","HTs",SpaceshipType.SIZE_HUGE,SpaceshipRange.SHORT,0,19,uniqueShipIdCounter);
        tempsst.setCivilian(true);
        tempsst.setIncOwnClosedBonus(2);
        tempsst.setIncOwnOpenBonus(8);
        tempsst.setIncFrendlyClosedBonus(7);
        tempsst.setIncFrendlyOpenBonus(9);
        tempsst.setIncNeutralClosedBonus(9);
        tempsst.setIncNeutralOpenBonus(12);
        tempsst.setFactionUnigue(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Short range trade ship to get incom to pay the war.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Huge Tradeship"));
        
        
        
        // ****** fraction unique property ******
        
        // Create orbital structures
      /*  os = new SpaceStation();
        os.setSpaceport(true);
        os.setOpenProdBonus(2);
        tempFaction.setOrbitalStructure(os);
        tempFaction.setBuildOrbitalStructureCostBase(12);
        tempFaction.setBuildOrbitalStructureCostMulitplier(0);
        */
        
        tmpVipType = new VIPType("Governor","Gov",trade,uniqueVIPIdCounter);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setDiplomat(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("The Governor is the leader of the faction so protect him. He gives one extra income on open planets. He have also diplomatic skill to convince neutral planets to join he’s faction.");
        gw.addVipType(tmpVipType);
        tempFaction.setGovernorVIPType(tmpVipType);
        
        /* Skapa en sådan VIP som går att köpa
        tmpVipType = new VIPType("Resistance Leader","ResL",trade,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setCounterEspionage(10);
        tmpVipType.setOpenIncBonus(1);
        tmpVipType.setClosedIncBonus(1);
        tmpVipType.setResistanceBonus(5);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setDescription("The Resistance Leader is a hero with skill for protecting planets. He gives one extra income on open and closed planets. He has also a small chance to kill assassins on planets he protects.");
        gw.addVipType(tmpVipType);
        tempFaction.addStartingVIPType(tmpVipType);*/
        
        tempFaction.addStartingVIPType(gw.getVIPTypeByName("Negotiator"));
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Falcon Frigate"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Troop Frigate"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Swallow"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("F Home Defender"));
        
        // *******************************************************************************
        // ****************************** --- Forskning --- ******************************
        // *******************************************************************************
        
        tempResearch = new Research();
        
        
        // A-8
        tempResearchAdvantage = new ResearchAdvantage("A-8", "Gives possibility to build A-8 attacker squadron class.");
        tempResearchAdvantage.setTimeToResearch(0);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("A-8"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("A-2"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        
        
        // Troop Frigate II
        tempResearchAdvantage = new ResearchAdvantage("Troop Frigate II", "Gives possibility to build Troop Frigate II class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Troop Frigate II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Troop Frigate"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Mercenary Elite
        tempResearchAdvantage = new ResearchAdvantage("Mercenary Elite", "Gives possibility to build Mercenary Elite.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Mercenary Elite"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Mercenary Tavern
        tempResearchAdvantage = new ResearchAdvantage("Mercenary Tavern", "Gives possibility to build Mercenary Base.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Mercenary Tavern"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Mercenary Elite"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Mercenary HAI
        tempResearchAdvantage = new ResearchAdvantage("Mercenary HAI", "Gives possibility to build Mercenary HAI.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Mercenary HAI"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Mercenary Tavern"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Mercenary HI
        tempResearchAdvantage = new ResearchAdvantage("Mercenary HI", "Gives possibility to build Mercenary HI.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Mercenary HI"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Mercenary Infantry"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Mercenary HAI"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Hawk Frigate
        tempResearchAdvantage = new ResearchAdvantage("Hawk Frigate", "Gives possibility to build Hawk Frigate class.");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Hawk Frigate"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Falcon Frigate"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // B-55
        tempResearchAdvantage = new ResearchAdvantage("B-55", "Gives possibility to build B-55 heavy bomber squadron class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("B-55"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // B-21
        tempResearchAdvantage = new ResearchAdvantage("B-21", "Gives possibility to build B-21 bomber squadron class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("B-21"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("B-12"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("A-8"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("B-55"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Battleship Retaliation
        tempResearchAdvantage = new ResearchAdvantage("Battleship Retaliation", "Gives possibility to build Battleship Retaliation ship class.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Battleship Immunity"));
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Battleship Retaliation"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Battleship Immunity
        tempResearchAdvantage = new ResearchAdvantage("Battleship Immunity", "Gives possibility to build Battleship Immunity ship class.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Battleship Immunity"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Battleship Independent"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Battleship Retaliation"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Battleship Independent
        tempResearchAdvantage = new ResearchAdvantage("Battleship Independent", "Gives possibility to build Battleship Independent ship class.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Battleship Independent"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Battleship Immunity"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // The one
        tempResearchAdvantage = new ResearchAdvantage("The one", "Gives possibility to build The one ship class.");
        tempResearchAdvantage.setTimeToResearch(4);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("The one"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Galaxy Battleship
        tempResearchAdvantage = new ResearchAdvantage("Galaxy Battleship", "Gives possibility to build Galaxy Battleship ship class.");
        tempResearchAdvantage.setTimeToResearch(4);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Galaxy Battleship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Battleship Immunity"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("The one"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        
        // Customs
        tempResearchAdvantage = new ResearchAdvantage("Customs", "Added a customs check to citys.");
        tempResearchAdvantage.setTimeToResearch(0);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("City");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Province Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        //Advanced Trading
        tempResearchAdvantage = new ResearchAdvantage("Advanced Trading", "Gives possibility to build Huge Tradeship.");
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Huge Tradeship"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Customs"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Trading
        tempResearchAdvantage = new ResearchAdvantage("Trading", "Gives possibility to build Large Tradeship.");
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Large Tradeship"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Advanced Trading"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Simple Trading
        tempResearchAdvantage = new ResearchAdvantage("Simple Trading", "Gives possibility to build Medium Tradeship.");
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Medium Tradeship"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Trading"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Infrastructure 2
        tempResearchAdvantage = new ResearchAdvantage("Infrastructure 2","Gives more income to open planets.");
        tempResearchAdvantage.setOpenPlanetBonus(1);
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Customs"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Infrastructure 1
        tempResearchAdvantage = new ResearchAdvantage("Infrastructure 1","Factory will give one in incom.");
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Factory");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infrastructure 2"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Huge Wharfs 
        tempResearchAdvantage = new ResearchAdvantage("Huge Wharfs", "Gives possibility to build Huge wharfs.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Huge Trade Wharf"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Advanced Trading"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Galaxy Battleship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Large Wharfs 
        tempResearchAdvantage = new ResearchAdvantage("Large Wharfs", "Gives possibility to build Large wharfs.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Huge Wharfs"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Trading"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Battleship Independent"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Artillery Mechs
        tempResearchAdvantage = new ResearchAdvantage("Artillery Mechs","Gives Artillery Mechs");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Artillery Mechs"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Cannon Mechs
        tempResearchAdvantage = new ResearchAdvantage("Cannon Mechs","Gives Cannon Mechs");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Cannon Mechs"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Miniguns Mechs
        tempResearchAdvantage = new ResearchAdvantage("Miniguns Mechs","Gives Miniguns Mechs");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Miniguns Mechs"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Mechs"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Defencive Troops
        tempResearchAdvantage = new ResearchAdvantage("Defencive Troops","Gives Bunker");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Bunker"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Artillery Mechs"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Miniguns Mechs"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Cannon Mechs"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // School
        tempResearchAdvantage = new ResearchAdvantage("School","Gives School");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("School"));
        tempResearchAdvantage.setTimeToResearch(4);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Capital
        tempResearchAdvantage = new ResearchAdvantage("Capital","Gives Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Capital"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("The one"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Province Capital
        tempResearchAdvantage = new ResearchAdvantage("Province Capital","Gives Province Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Capital"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        
        // Planet Shield
        tempResearchAdvantage = new ResearchAdvantage("Planet Shield","Gives Planet Shield and replace the old Basic shield model.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Planet Shield"));
        tempResearchAdvantage.addReplaceType(tempBuildings.getBuildingType("Basic Shield"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Heavy Cannon
        tempResearchAdvantage = new ResearchAdvantage("Heavy Cannon","Gives Heavy Cannon");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Heavy Cannon"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Puls Cannon
        tempResearchAdvantage = new ResearchAdvantage("Puls Cannon","Gives Puls Cannon");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Puls Cannon"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Air defence
        tempResearchAdvantage = new ResearchAdvantage("Air defence","Establish a platform for researching on planets air defence.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Planet Shield"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Puls Cannon"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Heavy Cannon"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Space Port
        tempResearchAdvantage = new ResearchAdvantage("Space Port","Gives Space Port");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Spaceport"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Blackbird
        tempResearchAdvantage = new ResearchAdvantage("Blackbird", "Gives possibility to build Blackbird ship class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Swallow"));
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Blackbird"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Interceptor II
        tempResearchAdvantage = new ResearchAdvantage("Interceptor II", "Gives possibility to build Interceptor II ship class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Interceptor"));
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Interceptor II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("A-8"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Fleet
        tempResearchAdvantage = new ResearchAdvantage("Fleet","Establish a platform for researching on offensice ships.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Blackbird"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Interceptor II"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Space
        tempResearchAdvantage = new ResearchAdvantage("Space","Establish a platform for researching on defensive in planets surrounded space.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Fleet"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Space Port"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Air defence"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Planet
        tempResearchAdvantage = new ResearchAdvantage("Planet","Establish a platform for researching on planets defensive.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Province Capital"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("School"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Defencive Troops"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Troops
        tempResearchAdvantage = new ResearchAdvantage("Troops", "Establish a platform for researching better troops.");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Galaxy Battleship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Battleship Retaliation"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Mercenary HI"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Troop Frigate II"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Ships
        tempResearchAdvantage = new ResearchAdvantage("Ships","Establish a platform for researching better ships.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Large Wharfs"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Hawk Frigate"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("B-21"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Troop Frigate II"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Defensive
        tempResearchAdvantage = new ResearchAdvantage("Defensive","Establish a platform for researching for defensive warfare.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Space"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Planet"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        //Offensive
        tempResearchAdvantage = new ResearchAdvantage("Offensive","Establish a platform for researching for offensive warfare.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Ships"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Troops"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        
        // ************************* --- Forskning Economic --- *************************
        researchCorruption1 = new ResearchAdvantage("Corruption level 6","Lower corruption");
		researchCorruption1.setCorruption(tmpCorruption6);
		researchCorruption1.setTimeToResearch(5);
		researchCorruption1.setCostToResearchOneTurnInProcent(10);
        
        researchCorruption2 = new ResearchAdvantage("Corruption level 5","Lower corruption");
        researchCorruption2.setCorruption(tmpCorruption5);
        researchCorruption2.setTimeToResearch(5);
        researchCorruption2.setCostToResearchOneTurnInProcent(10);
        researchCorruption2.addChild(researchCorruption1);
        
        researchCorruption3 = new ResearchAdvantage("Corruption level 4","Lower corruption");
        researchCorruption3.setCorruption(tmpCorruption4);
        researchCorruption3.setTimeToResearch(3);
        researchCorruption3.setCostToResearchOneTurnInProcent(20);
        researchCorruption3.addChild(researchCorruption2);
        
        researchCorruption4 = new ResearchAdvantage("Corruption level 3","Lower corruption");
        researchCorruption4.setCorruption(tmpCorruption3);
        researchCorruption4.setTimeToResearch(3);
        researchCorruption4.setCostToResearchOneTurnInProcent(20);
        researchCorruption4.addChild(researchCorruption3);
        
        researchCorruption5 = new ResearchAdvantage("Corruption level 2","Lower corruption");
        researchCorruption5.setCorruption(tmpCorruption2);
        researchCorruption5.setTimeToResearch(3);
        researchCorruption5.setCostToResearchOneTurnInProcent(20);
        researchCorruption5.addChild(researchCorruption4);
        
        researchCorruption6 = new ResearchAdvantage("Corruption level 1","Lower corruption");
        researchCorruption6.setCorruption(tmpCorruption1);
        researchCorruption6.setTimeToResearch(3);
        researchCorruption6.setCostToResearchOneTurnInProcent(20);
        researchCorruption6.addChild(researchCorruption5);
        
        tempResearch.addAdvantage(researchCorruption1);
        tempResearch.addAdvantage(researchCorruption2);
        tempResearch.addAdvantage(researchCorruption3);
        tempResearch.addAdvantage(researchCorruption4);
        tempResearch.addAdvantage(researchCorruption5);
        tempResearch.addAdvantage(researchCorruption6);
        
        tempResearchAdvantage = new ResearchAdvantage("Economic","Develop Economic");
        tempResearchAdvantage.addChild(researchCorruption6);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Simple Trading"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infrastructure 1"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        
        
        
        
        tempResearch.setNumberOfSimultaneouslyResearchAdvantages(2);
        
        tempFaction.setResearch(tempResearch);
        
        // *******************************************************************************
        // *******************************************************************************
        // *******************************************************************************
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setAdvantages("Resistance bonus = 1, starts with a diplomat");
        tempFaction.setHistory("At 2101 some big companies started a small police force to secure civilian transport in the space. That was the first step against the federation that become Trade Federation. At 2178 some big companies started the Trade Federation to build up an armed fleet to escort trading fleets and to strike back on pirates.\nThe first ship that was ordered was the big and heavy armed Battleship. But the old countries from the earth didn’t liked the idea of heavy armed civilian battle ships, so they started to build up own military fleets to control the space and the colony und the own flags. That wasn’t any god for us at all. Some free (no taxes) planets went under the nation’s flags. To trade with them we were forced to pay heavy taxes to support the nation military fleet that was supposed to give us peas to the world. So our profits went down more and more. Often then we were meeting military fleets on patrol they stopped us fore checking after stolen cargo. Even then our fleet was escorted by the heavy Battleship they tried to forced us to let them board us.\nBut after we lost some fleet after letting false flagged ship board us we declared us as a free nation and that we wasn’t going to let any nation board our ships. Around 2240 we lost lots of trading posts in the outer space. Some was destroyed or just stopped trading or communicate with us. When we were sending out military fleets to find out that happened we often found us under attacked or just destroyed out posts.\nThen the Great War started we fled away from the old world with all ships that we could and just left our bases and ships without hyper drivers behind. Sadly we also lost our great leaders at the main office on earth. We must now found new homes to our dear friends from earth and the rest of the sun system. But beware of the pirated scum that call they self for Federation of liberty. It’s hard to trust any at these times but you can be shore that you can’t do any deals with the pirates, they aren’t trustable at al, don’t forgot to look after outposts still loyal to us.");
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setCorruption(tmpCorruption);
        //tempFaction.setAlignment(trade);
        gw.addFaction(tempFaction);

//      ######## USA (Gul) ########
//      XXX USA (Gul)        
        tempFaction = new Faction("USA",Faction.getColorHexString(255,197,19),usa);
        
//      Adding Buildings to the faction
        tempBuildings = new Buildings();
        
        tempBuildingType= new BuildingType("Small USA Wharf", "W1", 3, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(1);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Small Wharf what can build small ships");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Medium USA Wharf", "W2", 11, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(2);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Medium Wharf what can build small and medium ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Small USA Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large USA Wharf", "W3", 18, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(3);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium USA Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge USA Wharf", "W5", 25, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large USA Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Space station", "SS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setOpenPlanetBonus(1);
        tempBuildingType.setDescription("Build this building on the planets and long range ways will be short range between planetes whit Space station. Give 1 incom on open planets");
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("USA Home Base", "UHB", 20, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(6);
        tempBuildingType.setClosedPlanetBonus(6);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDescription("This home base gives an income of 6.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Civilian Outpost", "CO", 2, uniqueBuildingIdCounter);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("A small base with civilians.");
        tempBuildingType.setAutoDestructWhenConquered(true);
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("City", "Ci", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setOpenPlanetBonus(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("City that gives 2 in trade incom if the planet is open.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Civilian Outpost"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Province Capital", "PC", 20, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(5);
        tempBuildingType.setResistanceBonus(2);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Province Capital that gives 5 in incom if the planet is open. Gives bonus to the defending ground force. Only one Province Capital can be build each player.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("City"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Capital", "Ca", 30, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(7);
        tempBuildingType.setResistanceBonus(5);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setFactionUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Capital that gives 7 in incom if the planet is open. Gives a big bonus to the defending ground force. Only one Capital can be build each faction.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Drone Base", "DB", 20, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(3);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("This building are taking humens eggs from the civilan in the old city and make drone soldier of them.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("City"));
        tempBuildingType.setAutoDestructWhenConquered(true);
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Agent Center", "AC", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Assassin"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Spy"));
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Agent Center to train undercover VIPs");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Military Academy", "MA", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Commander"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Ace"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Captain"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("General"));
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Academy to train military VIPs");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Civilian Outpost"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Factory", "Fa", 12, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setTechBonus(10);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A factory give better parts to build units with.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Tank Factory", "TF", 14, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.ARMORED);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("In this buiding armored ground units can be build.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Armor Factory", "AF", 16, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(3);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.ARMORED);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.SUPPORT);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("In this building, armored and support ground units can be build in big number.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Tank Factory"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Basic Shield", "BS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A small shield to defende planets from bombardment.");
        tempBuildings.addBuilding(tempBuildingType);
        
        // ersätter Basic Shield
        tempBuildingType= new BuildingType("Planet Shield", "PS", 15, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A shield to defende planets from bombardment.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Basic Shield"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Missile Silo", "MS", 5, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(100);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A planet based missile system to shoot down ships what besiege the planet.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Atomic Missile", "AD", 15, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(2000);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setFactionUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A atomic planet based missile system to shoot down ships in all sized.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Missile Silo"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Cannon", "Can", 5, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(50);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A planet based Cannon to shoot down small ships what besiege the planet.");
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("USA Home Base"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small USA Wharf"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Civilian Outpost"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Tank Factory"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
        
        
// ###### TROOPS ######
        
        tt = new TroopType("Light Drones","LDr",80,1,8,uTIC,20,5);
        tt.setDescription("Light drone infantry unit often used to defend planetes.");
        tt.setShortDescription("Light drone infantry unit.");
        tt.setAdvantages("Cheap upkeep cost");
        tt.setDisadvantages("Expencive to buy, Weak against armor.");
        //tt.setSpaceshipTravel(false);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Light Drones"));
        
        tt = new TroopType("Drones","Dro",100,1,9,uTIC,25,12);
        tt.setDescription("Drone infantry unit often used to defend planetes.");
        tt.setShortDescription("Drone infantry unit.");
        tt.setAdvantages("Cheap upkeep cost");
        tt.setDisadvantages("Expencive to buy.");
        tt.setCanBuild(false);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Drones"));
        
        tt = new TroopType("Heavy Drones","HDr",120,2,12,uTIC,40,30);
        tt.setDescription("Heavy infantry unit.");
        tt.setShortDescription("Heavy infantry unit.");
        tt.setAdvantages("");
        tt.setDisadvantages("Expencive to buy.");
        tt.setCanBuild(false);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Heavy Drones"));
        
        tt = new TroopType("Drone Scouts","DrS",50,1,8,uTIC,20,5);
        tt.setDescription("Drone scout infantry unit often used to do flank attacks.");
        tt.setShortDescription("Drone scout infantry unit used to flank.");
        tt.setAdvantages("Flankerst");
        tt.setDisadvantages("Expencive to buy, Weak against armor.");
  //      tt.setAttackScreened(true);
        tt.setCanBuild(false);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        tt.setDefaultPosition(BattleGroupPosition.FLANKER);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Drone Scouts"));
        
        tt = new TroopType("USA SEAL","SEAL",100,4,20,uTIC,60,45);
        tt.setDescription("Heavy infantry unit that is the elite of the elite.");
        tt.setShortDescription("Heavy infantry unit that is the elit of the elit.");
        tt.setAdvantages("The strongest infantary in USA military, Flankers");
        tt.setDisadvantages("Can only have one of them.");
        tt.setPlayerUnique(true);
        tt.setCanBuild(false);
    //    tt.setAttackScreened(true);
        //tt.setSpaceshipTravel(false);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        tt.setDefaultPosition(BattleGroupPosition.FLANKER);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("USA SEAL"));
        
        
        tt = new TroopType("Dragon Fire","DrF",100,2,6,uTIC,8,5);
        tt.setDescription("Light USA tank artillery.");
        tt.setShortDescription("Light USA tank artillery");
        tt.setAdvantages("Artillery attack");
        tt.setDisadvantages("Expensive");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(30);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Dragon Fire"));
        
        
        tt = new TroopType("USA Howitzer","UHo",100,2,10,uTIC,20,14);
        tt.setDescription("USA artillery.");
        tt.setShortDescription("USA artillery.");
        tt.setAdvantages("Artillery attack");
        tt.setDisadvantages("Expensive");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(45);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("USA Howitzer"));
        
        tt = new TroopType("Sherman","She",150,3,15,uTIC,40,30);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setDescription("Tank unit all-raound.");
        tt.setShortDescription("Tank unit all-raound.");
        tt.setAdvantages("");
        tt.setDisadvantages("Expensive");
        tt.setDefaultPosition(BattleGroupPosition.FIRST_LINE);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Sherman"));
        
        tt = new TroopType("M803 Abrams","Abr",250,4,26,uTIC,30,50);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setDescription("Heavy tank unit.");
        tt.setShortDescription("Heavy Tank unit.");
        tt.setAdvantages("Can take heavy damage");
        tt.setDisadvantages("");
        tt.setDefaultPosition(BattleGroupPosition.FIRST_LINE);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("M803 Abrams"));
        
        tt = new TroopType("M48 Hellcat","Hell",150,3,15,uTIC,15,50);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setDescription("Tank unit amrmed with a anti tanks cannon.");
        tt.setShortDescription("Antitank tanks.");
        tt.setAdvantages("Good versus tanks units.");
        tt.setDisadvantages("Expensive, Weak against infantry.");
        tt.setDefaultPosition(BattleGroupPosition.FIRST_LINE);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("M48 Hellcat"));
       
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Light Drones"));     
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Sherman"));
        
        
        // Trade Federation Defence platforms
        // -----------------
        // USA Defender
        tempsst = new SpaceshipType("USA Defender","UDef",SpaceshipType.SIZE_MEDIUM,30,130,SpaceshipRange.NONE,2,10,uniqueShipIdCounter,15,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(18);
        tempsst.setArmorSmall(55);
        tempsst.setArmorMedium(10);
        tempsst.setInitDefence(1);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This Medium planet defender capital ship was developed for protection against squadrons attacks. Common on ours most important planets. Should work together with anti capital ships or USA Bombers.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("USA Defender"));
        
        // USA Military Base
        tempsst = new SpaceshipType("USA Military Base","UMB",SpaceshipType.SIZE_LARGE,20,270,SpaceshipRange.NONE,0,50,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(45);
        tempsst.setArmorMedium(20);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(250);
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setIncreaseInitiative(30);
        tempsst.setNoRetreat(true);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setPlayerUnique(true);
        tempsst.setDescription("This medium planet home base is made as a military headquarter and offers good protections against medium sized ships. The ship hasn’t any hyper engine. Because of the advanced military technology this base gives a lot of initiative in fleet combats.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("USA Military Base"));
        
        // Squadrons
        // ---------
        // Mustang
        tempsst = new SpaceshipType("Mustang","Mus",SpaceshipType.SIZE_SMALL,0,27,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,5,14);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A good and cheep dog fighters that it made by AAD and modified by USA armory.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Mustang"));
        
        // Raptor
        tempsst = new SpaceshipType("Raptor","Rap",SpaceshipType.SIZE_SMALL,5,30,SpaceshipRange.NONE,1,5,uniqueShipIdCounter,7,22);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("Next generation of fighter.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Raptor"));
        
        // Lancer
        tempsst = new SpaceshipType("Lancer","Lan",SpaceshipType.SIZE_SMALL,0,18,SpaceshipRange.NONE,1,3,uniqueShipIdCounter,15,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(13);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(8);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A good anti capital fighter that it made by AAD and modified by USA armory.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Lancer"));
        
        // Spirit
        tempsst = new SpaceshipType("Spirit","Spi",SpaceshipType.SIZE_SMALL,0,23,SpaceshipRange.NONE,1,6,uniqueShipIdCounter,17,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(17);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(17);
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("Next generation of anti capital fighter.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Spirit"));
        
        // Thunderbolt
        tempsst = new SpaceshipType("Thunderbolt","Thu",SpaceshipType.SIZE_SMALL,0,18,SpaceshipRange.NONE,1,6,uniqueShipIdCounter,10,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(8);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setWeaponsStrengthHuge(30);
        tempsst.setWeaponsMaxSalvoesHuge(2);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setDescription("USA Heavy Bomber is a good anti capital fighter that it made by AAD and modified by USA armory. Armd with weaponse against huge ships but weak aginst smaller capital ships");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Thunderbolt"));
        
        // Phantom
        tempsst = new SpaceshipType("Phantom","Pha",SpaceshipType.SIZE_SMALL,7,30,SpaceshipRange.SHORT,2,5,uniqueShipIdCounter,8,8);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(12);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A good all-round support fighter with hyper engine for short range jumps. This fighter is often used to replace shot downed squadrons after a battle in fleet far away from our bases.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Phantom"));
        
        // Hornet
        tempsst = new SpaceshipType("Hornet","Hor",SpaceshipType.SIZE_SMALL,10,32,SpaceshipRange.SHORT,2,8,uniqueShipIdCounter,10,12);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("Next generation of all-round support fighter with hyper engine for short range jumps. This fighter is often used to replace shot downed squadrons after a battle in fleet far away from our bases.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Hornet"));
        
        
        // Capital ships
        // -------------
        // Corvette II
        tempsst = new SpaceshipType("Corvette II","Crv2",SpaceshipType.SIZE_SMALL,12,38,SpaceshipRange.LONG,2,5,uniqueShipIdCounter,8,6);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setCanBlockPlanet(false);
        tempsst.setArmorSmall(5);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Light small long range capital ship used for exploring and person transports. Build by the company BBZ and modify by the military. Cost more build to but is one of the best ships in the class.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette II"));
        
        // Knox Frigate
        tempsst = new SpaceshipType("Knox Frigate","KFr",SpaceshipType.SIZE_SMALL,15,55,SpaceshipRange.LONG,3,7,uniqueShipIdCounter,10,5);
        tempsst.setArmorSmall(5);
        tempsst.setWeaponsStrengthMedium(5);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(1);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Based on the hull from a Falcon Frigate and modified with torpedo against large capital ships that’s made it to a good supporting ships in the big battles.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Knox Frigate"));
        
        // Gun Ship
        tempsst = new SpaceshipType("Gun Ship","GSh",SpaceshipType.SIZE_SMALL,15,40,SpaceshipRange.LONG,3,7,uniqueShipIdCounter,6,25);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setArmorSmall(5);
        tempsst.setDescription("Based on the hull from a Falcon Frigate and modified with lot of anti squdrons guns.");
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Gun Ship"));
        
        // Tarawa
        tempsst = new SpaceshipType("Tarawa","Tar",SpaceshipType.SIZE_MEDIUM,5,35,SpaceshipRange.LONG,2,8,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(25);
        tempsst.setArmorMedium(5);
        tempsst.setTroopCapacity(3);
        tempsst.setScreened(true);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This is the main troop transport ship with very poor fire power but good manoeuvre capacity that's make this ship to long range class ship. The ship is a rebuild civilian personal transporter modified with lasers.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Tarawa"));
        
        // Wasp
        tempsst = new SpaceshipType("Wasp","Wasp",SpaceshipType.SIZE_MEDIUM,10,40,SpaceshipRange.LONG,2,10,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(25);
        tempsst.setArmorMedium(5);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setTroopCapacity(4);
        tempsst.setScreened(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setDescription("This is a modified version of the Tarawa.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Wasp"));
        
        // Nimitz
        tempsst = new SpaceshipType("Nimitz","Nim",SpaceshipType.SIZE_MEDIUM,15,80,SpaceshipRange.LONG,3,14,uniqueShipIdCounter,12,12);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(35);
        tempsst.setSquadronCapacity(6);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A medium carrier with enough fire power to hold fighter ships away from the hangars ports. Can take up to 6 squadrons and that's make this ship to a great supporting ship on long range mission.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Nimitz"));
        
        // Supply Frigate II
        tempsst = new SpaceshipType("Supply Frigate II","SFr2",SpaceshipType.SIZE_MEDIUM,25,30,SpaceshipRange.SHORT,1,12,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(35);
        tempsst.setSupply(4);
        tempsst.setPlanetarySurvey(true);
        tempsst.setScreened(true);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Civilian luxury cruisers rebuild to supply the fleet. The medium hull and badly flight capacity make it to a short ranger and easy target.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Frigate II"));
        
        // Gleaves Destroyer
        tempsst = new SpaceshipType("Gleaves Destroyer","GD",SpaceshipType.SIZE_MEDIUM,20,130,SpaceshipRange.LONG,5,13,uniqueShipIdCounter,20,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setArmorSmall(40);
        tempsst.setDescription("A medium ship and one of the best long range battle ship in the fleet. Good against medium and small ship but don't heavy enough to go against the big battle main ships. Used to control the outer space.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Gleaves Destroyer"));
        
        // Gearing Destroyer
        tempsst = new SpaceshipType("Gearing Destroyer","GeD",SpaceshipType.SIZE_MEDIUM,25,140,SpaceshipRange.LONG,5,16,uniqueShipIdCounter,20,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setAvailableToBuild(false);
        tempsst.setArmorSmall(40);
        tempsst.setBlackmarketFirstTurn(10);
        tempsst.setBluePrintFirstTurn(14);
        tempsst.setDescription("The best long range ship in the fleet. Learning of experience from the earlier models of destroyers.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Gearing Destroyer"));
        
        // Kidd Destroyer
        tempsst = new SpaceshipType("Kidd Destroyer","Kidd",SpaceshipType.SIZE_MEDIUM,20,130,SpaceshipRange.LONG,5,13,uniqueShipIdCounter,15,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setArmorSmall(40);
        tempsst.setAvailableToBuild(false);
        tempsst.setBombardment(1);
        tempsst.setBlackmarketFirstTurn(10);
        tempsst.setBluePrintFirstTurn(20);
        tempsst.setDescription("A modified Gleaves Destoyer with bombardment to destroy small bases.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Kidd Destroyer"));
        
        //Nevada Battleship
        tempsst = new SpaceshipType("Nevada Battleship","NevB",SpaceshipType.SIZE_LARGE,75,440,SpaceshipRange.SHORT,11,49,uniqueShipIdCounter,12,12);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(45);
        tempsst.setWeaponsMaxSalvoesLarge(15);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setIncreaseInitiative(5);
        tempsst.setSquadronCapacity(6);
        tempsst.setBombardment(1);
        tempsst.setBlackmarketFirstTurn(35);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(60);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("Build on the same hull as the Battleship Independent but modified to get bombardment by USA armory. Strong and all-round to work alone deep in the space. Superior the most large ships but can’t do long jumps. The carrier capacity is up to 6 squadrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Nevada Battleship"));
        
        //Tennessee Battleship
        tempsst = new SpaceshipType("Tennessee Battleship","TenB",SpaceshipType.SIZE_LARGE,75,470,SpaceshipRange.SHORT,11,51,uniqueShipIdCounter,12,12);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(15);
        tempsst.setWeaponsStrengthHuge(25);
        tempsst.setWeaponsMaxSalvoesHuge(6);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setIncreaseInitiative(6);
        tempsst.setSquadronCapacity(6);
        tempsst.setBombardment(2);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(43);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(80);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setDescription("Build on the same hull as the Nevada Battleship but modified to get armd against huge ships. Strong and all-round to work alone deep in the space. Superior the most large ships but can’t do long jumps.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Tennessee Battleship"));
        
        //Colorado Battleship
        tempsst = new SpaceshipType("Colorado Battleship","ColB",SpaceshipType.SIZE_LARGE,75,470,SpaceshipRange.SHORT,11,51,uniqueShipIdCounter,15,12);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(35);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(15);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setIncreaseInitiative(7);
        tempsst.setSquadronCapacity(6);
        tempsst.setTroopCapacity(4);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(43);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(80);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setDescription("Build on the same hull as the Nevada Battleship but modified to get troop capacity and stronger weapons against smaller capital ships. To get room to the troop the possible to bombard is lost. Strong and all-round to work alone deep in the space.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Colorado Battleship"));
        
        //Dakota Battleship
        tempsst = new SpaceshipType("Dakota Battleship","DaB",SpaceshipType.SIZE_HUGE,115,750,SpaceshipRange.SHORT,27,96,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(35);
        tempsst.setWeaponsMaxSalvoesMedium(22);
        tempsst.setWeaponsStrengthLarge(60);
        tempsst.setWeaponsMaxSalvoesLarge(15);
        tempsst.setWeaponsStrengthHuge(65);
        tempsst.setWeaponsMaxSalvoesHuge(10);
        tempsst.setArmorSmall(85);
        tempsst.setArmorMedium(60);
        tempsst.setArmorLarge(25);
        tempsst.setArmorHuge(10);
        tempsst.setIncreaseInitiative(13);
        tempsst.setSquadronCapacity(8);
        tempsst.setBombardment(2);
        tempsst.setTroopCapacity(3);
        tempsst.setPlanetarySurvey(true);
        tempsst.setBlackmarketFirstTurn(80);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("Construct to secure military domination in the galaxy. Good in both fleet combats and to overtake planets (excellent bombardment plus a battalion troops). Good survey capacity to get information about the planet resistance.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Dakota Battleship"));
        
        //Iowa Battleship
        tempsst = new SpaceshipType("Iowa Battleship","Iowa",SpaceshipType.SIZE_HUGE,115,850,SpaceshipRange.SHORT,28,100,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(35);
        tempsst.setWeaponsMaxSalvoesMedium(22);
        tempsst.setWeaponsStrengthLarge(60);
        tempsst.setWeaponsMaxSalvoesLarge(15);
        tempsst.setWeaponsStrengthHuge(65);
        tempsst.setWeaponsMaxSalvoesHuge(10);
        tempsst.setArmorSmall(85);
        tempsst.setArmorMedium(60);
        tempsst.setArmorLarge(25);
        tempsst.setArmorHuge(10);
        tempsst.setIncreaseInitiative(14);
        tempsst.setSquadronCapacity(8);
        tempsst.setBombardment(3);
        tempsst.setTroopCapacity(4);
        tempsst.setPlanetarySurvey(true);
        tempsst.setBlackmarketFirstTurn(100);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("Based on a Dakota Battleship hull. Better hull, bombardment and troop capacity");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Iowa Battleship"));
        
        // ****** fraction unique property ******
        
        
        tempFaction.setGovernorVIPType(President);
        
        tempFaction.addStartingVIPType(gw.getVIPTypeByName("Assassin"));
        
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette II"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Tarawa"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Mustang"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Mustang"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Lancer"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Phantom"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Nimitz"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("USA Military Base"));
        
        // *******************************************************************************
        // ****************************** --- Forskning --- ******************************
        // *******************************************************************************
        
        tempResearch = new Research();
        
     // Thunderbolt
        tempResearchAdvantage = new ResearchAdvantage("Thunderbolt", "Gives possibility to build Thunderbolt ship class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Thunderbolt"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Hornet
        tempResearchAdvantage = new ResearchAdvantage("Hornet", "Gives possibility to build Hornet ship class. Replace the Phantom class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Hornet"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Phantom"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Spirit
        tempResearchAdvantage = new ResearchAdvantage("Spirit", "Gives possibility to build Spirit ship class. Replace the Lancer class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Spirit"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Lancer"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Raptor
        tempResearchAdvantage = new ResearchAdvantage("Raptor", "Gives possibility to build Raptor ship class. Replace the Mustang class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Raptor"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Mustang"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Squadrons","Develop better squadrons.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Raptor"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Spirit"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Thunderbolt"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Hornet"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // SEAL
        tempResearchAdvantage = new ResearchAdvantage("SEAL","Gives USA SEAL");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("USA SEAL"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Heavy Drones
        tempResearchAdvantage = new ResearchAdvantage("Heavy Drones","Gives Heavy Drones.");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Heavy Drones"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Scouts
        tempResearchAdvantage = new ResearchAdvantage("Scouts","Gives Drone Scouts.");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Drone Scouts"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Drones
        tempResearchAdvantage = new ResearchAdvantage("Drones","Gives Drones. Replace Light Drones");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Drones"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Light Drones"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Heavy Drones"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Scouts"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Howitzer
        tempResearchAdvantage = new ResearchAdvantage("Howitzer","Gives USA Howitzer. Replace Dragon Fire.");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("USA Howitzer"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Dragon Fire"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Abrams
        tempResearchAdvantage = new ResearchAdvantage("Abrams","Gives M803 Abrams");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("M803 Abrams"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Hellcat
        tempResearchAdvantage = new ResearchAdvantage("Hellcat","Gives M48 Hellcat");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("M48 Hellcat"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Abrams"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        
        tempResearchAdvantage = new ResearchAdvantage("Troop","Develop better troops.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Hellcat"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Howitzer"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Drones"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("SEAL"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // Iowa Battleship
        tempResearchAdvantage = new ResearchAdvantage("Iowa Battleship", "Gives possibility to build Iowa Battleship ship class. Replace the Dakota class.");
        tempResearchAdvantage.setTimeToResearch(6);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Iowa Battleship"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Dakota Battleship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Dakota Battleship
        tempResearchAdvantage = new ResearchAdvantage("Dakota Battleship", "Gives possibility to build Dakota Battleship ship class.");
        tempResearchAdvantage.setTimeToResearch(5);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Dakota Battleship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Iowa Battleship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Colorado Battleship
        tempResearchAdvantage = new ResearchAdvantage("Colorado Battleship", "Gives possibility to build Colorado Battleship ship class.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Colorado Battleship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Tennessee Battleship
        tempResearchAdvantage = new ResearchAdvantage("Tennessee Battleship", "Gives possibility to build Tennessee Battleship ship class. Replace the Nevada class.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Tennessee Battleship"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Nevada Battleship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Nevada Battleship
        tempResearchAdvantage = new ResearchAdvantage("Nevada Battleship", "Gives possibility to build Nevada Battleship ship class.");
        tempResearchAdvantage.setTimeToResearch(4);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Nevada Battleship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Colorado Battleship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Tennessee Battleship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Dakota Battleship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Kidd Destroyer
        tempResearchAdvantage = new ResearchAdvantage("Kidd Destroyer", "Gives possibility to build Kidd Destroyer ship class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Kidd Destroyer"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Gearing Destroyer
        tempResearchAdvantage = new ResearchAdvantage("Gearing Destroyer", "Gives possibility to build Gearing Destroyer ship class. Replace the Gleaves class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Gearing Destroyer"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Gleaves Destroyer"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Gun Ship
        tempResearchAdvantage = new ResearchAdvantage("Gun Ship", "Gives possibility to build Gun Ship class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Gun Ship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Wasp
        tempResearchAdvantage = new ResearchAdvantage("Wasp", "Gives possibility to build Wasp ship class. Replace the Tarawa class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Wasp"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Tarawa"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Colorado Battleship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Iowa Battleship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("SEAL"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Capital Ships","Develop better capital ships.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Wasp"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Gun Ship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Gearing Destroyer"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Kidd Destroyer"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Nevada Battleship"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // Atomic Missile
        tempResearchAdvantage = new ResearchAdvantage("Atomic Missile", "Gives Atomic Missile.");
        tempResearchAdvantage.setTimeToResearch(4);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Atomic Missile"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Mobile
        tempResearchAdvantage = new ResearchAdvantage("Mobile Missile", "Gives better heavy weaponse on ships.");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Tennessee Battleship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Thunderbolt"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Fixed
        tempResearchAdvantage = new ResearchAdvantage("Fixed", "Gives Missile Silo.");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Missile Silo"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Atomic Missile"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Missile
        tempResearchAdvantage = new ResearchAdvantage("Missile", "Start develop new missiles.");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Fixed"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Mobile Missile"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Bombardment
        tempResearchAdvantage = new ResearchAdvantage("Bombardment", "Gives ships with better bombardment.");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Kidd Destroyer"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Iowa Battleship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Atomic Missile"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("Heavy Weaponse","Develop better weaponse.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Missile"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Bombardment"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("SEAL"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // Huge Wharf
        tempResearchAdvantage = new ResearchAdvantage("Huge Wharf","Gives Huge USA Wharf");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Huge USA Wharf"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Dakota Battleship"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Large Wharf
        tempResearchAdvantage = new ResearchAdvantage("Large Wharf","Gives Large USA Wharf");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Large USA Wharf"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Huge Wharf"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Nevada Battleship"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Planet Shield
        tempResearchAdvantage = new ResearchAdvantage("Planet Shield","Gives Planet Shield");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Planet Shield"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Basic Shield
        tempResearchAdvantage = new ResearchAdvantage("Basic Shield","Gives Basic Shield");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Basic Shield"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Planet Shield"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Armor Factory
        tempResearchAdvantage = new ResearchAdvantage("Armor Factory","Gives Armor Factory");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Armor Factory"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Factory
        tempResearchAdvantage = new ResearchAdvantage("Factory","Gives Factory");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Factory"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Armor Factory"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Military Academy
        tempResearchAdvantage = new ResearchAdvantage("Military Academy","Gives Military Academy");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Military Academy"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("SEAL"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Agent Center
        tempResearchAdvantage = new ResearchAdvantage("Agent Center","Gives Agent Center");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Agent Center"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // VIPs
        tempResearchAdvantage = new ResearchAdvantage("VIPs","Gives possibility to research on VIP buildings.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Agent Center"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Military Academy"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Capital
        tempResearchAdvantage = new ResearchAdvantage("Capital","Gives Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Capital"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Province Capital
        tempResearchAdvantage = new ResearchAdvantage("Province Capital","Gives Province Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Capital"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Buildings
        tempResearchAdvantage = new ResearchAdvantage("Buildings", "Gives a platfrom to develop ground structures.");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Large Wharf"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Basic Shield"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Factory"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("VIPs"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Province Capital"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        
        // ************************* --- Forskning Economic --- *************************
        
        // Trade 2
        tempResearchAdvantage = new ResearchAdvantage("Trade 2","Gives more income to open planets.");
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Large USA Wharf");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Huge USA Wharf");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Trade
        tempResearchAdvantage = new ResearchAdvantage("Trade","Better tax control.");
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("City");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Province Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Trade 2"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        researchCorruption1 = new ResearchAdvantage("Corruption level 6","Lower corruption");
		researchCorruption1.setCorruption(tmpCorruption6);
		researchCorruption1.setTimeToResearch(5);
		researchCorruption1.setCostToResearchOneTurnInProcent(10);
        
        researchCorruption2 = new ResearchAdvantage("Corruption level 5","Lower corruption");
        researchCorruption2.setCorruption(tmpCorruption5);
        researchCorruption2.setTimeToResearch(5);
        researchCorruption2.setCostToResearchOneTurnInProcent(10);
        researchCorruption2.addChild(researchCorruption1);
        
        researchCorruption3 = new ResearchAdvantage("Corruption level 4","Lower corruption");
        researchCorruption3.setCorruption(tmpCorruption4);
        researchCorruption3.setTimeToResearch(3);
        researchCorruption3.setCostToResearchOneTurnInProcent(20);
        researchCorruption3.addChild(researchCorruption2);
        
        researchCorruption4 = new ResearchAdvantage("Corruption level 3","Lower corruption");
        researchCorruption4.setCorruption(tmpCorruption3);
        researchCorruption4.setTimeToResearch(3);
        researchCorruption4.setCostToResearchOneTurnInProcent(20);
        researchCorruption4.addChild(researchCorruption3);
        
        researchCorruption5 = new ResearchAdvantage("Corruption level 2","Lower corruption");
        researchCorruption5.setCorruption(tmpCorruption2);
        researchCorruption5.setTimeToResearch(3);
        researchCorruption5.setCostToResearchOneTurnInProcent(20);
        researchCorruption5.addChild(researchCorruption4);
        
        researchCorruption6 = new ResearchAdvantage("Corruption level 1","Lower corruption");
        researchCorruption6.setCorruption(tmpCorruption1);
        researchCorruption6.setTimeToResearch(3);
        researchCorruption6.setCostToResearchOneTurnInProcent(20);
        researchCorruption6.addChild(researchCorruption5);
        
        tempResearch.addAdvantage(researchCorruption1);
        tempResearch.addAdvantage(researchCorruption2);
        tempResearch.addAdvantage(researchCorruption3);
        tempResearch.addAdvantage(researchCorruption4);
        tempResearch.addAdvantage(researchCorruption5);
        tempResearch.addAdvantage(researchCorruption6);
        
        tempResearchAdvantage = new ResearchAdvantage("Economic","Develop Economic");
        tempResearchAdvantage.addChild(researchCorruption6);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Trade"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        
        tempResearch.setNumberOfSimultaneouslyResearchAdvantages(2);
        
        tempFaction.setResearch(tempResearch);
        
        // *******************************************************************************
        // *******************************************************************************
        // *******************************************************************************
        
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setAdvantages("Resistance bonus = 1, good ships");
        tempFaction.setDisadvantages("Expensive ships");
        tempFaction.setHistory("The year 2006 we started to develop new rockets and space ship to explore and to build up a base on the moon. One of the main causes was that China hade started o program to get to the moon and get out to the space. We could of course not let any nation get that military advanced on us. So we started to send in a lot of dollar in to the project to be first with a permanent moon base. At 2012 we did our first trip to moon for over 40 years (one year after China). It took longer time then expected but we hade also one base just one year after that. This developing went to be a huge deep hole to put money in and we were forced to take big loans to going on. To avoid military conflict in space we decide to write under the non weapons agreement. The biggest reason was that we were total broke and didn’t hade any resources to spend on advanced military space technology.\nThe in the following years we expand our space fleet and around AC 2072 we hade almost 250 heavy space ships. Just after China we were the greatest nation in the space. It was now all the money spent in the space program was began pay of. Our economy was going from nearly broken to great.\nThen the first space shipped was attacked we decide to armed our space fleet and also to build up a military space fleet. Then the first hyper driver was developed we were chocked and long after that technology. It took us some years to rebuild our fleet fore hyper jumping’s.  At 2178 some company started a federation and began to build to build Battle ships up to 15 times bigger then our military ships. Of course couldn’t we accept that so we decide to make a copy of the same hull as the Battleship was made on and armed it with our superior weapon systems. It wasn’t easy to get the blue prints on the hull and not easy to denied the access to them.\nAround AC 2230 we lost a lot civilian ship in the outer space and also some heavy armed military fleet. We don’t know who that did this attacks but only one non nation military fleet hade that military strange to attack our great military fleet. It hade to be the Trade federation but we don’t have the evidence to proof that. To answer against this type of threat we decide to build up our military fleet.\n22 Mars in 2242 the war began at earth and in the rest of sun system. We don’t have any report from on that happened and the first month after that. All ours ship what was on way to earth didn’t come back before we send away 5 great warships to checked out that was going on. One of them returned heavy damaged and not much to report. As fast they entered the space around the earth they were attacked by heavy fire from unknown ships. The only reason that the only one returned was a kamikaze attack against one of the hostile’s ship from one of our own battleship.\nWe sound to be the only nation that doesn’t know anything about that happened on earth. We must now trust on our military fleet to defend us ands it’s time to pay back. But first must we get in contact with our colonies and start to develop and build up our fleet.");
        //tempFaction.setStartingWharfSize(OrbitalWharf.MAX_SIZE_SMALL);
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setCorruption(tmpCorruption);
        //tempFaction.setAlignment(usa);
        gw.addFaction(tempFaction);
        
//      ######## Russian (Rosa) ########
//      XXX Russian (Rosa)        
        tempFaction = new Faction("Russian",Faction.getColorHexString(255,0,255),russian);
        
//      Adding Buildings to the faction
        tempBuildings = new Buildings();
        
        tempBuildingType= new BuildingType("Small Wharf", "W1", 3, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(1);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Small Wharf what can build small ships");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Medium Wharf", "W2", 12, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(2);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Medium Wharf what can build small and medium ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Small Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large Wharf", "W3", 16, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(3);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge Wharf", "W5", 22, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Spaceport", "SP", 8, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Build this building on the planets and long range ways will be short range between planetes whit spaceports");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Russian Home Base", "RHom", 20, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(8);
        tempBuildingType.setClosedPlanetBonus(8);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("The home base is made as a head office. Gives an income of 8.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Civilian Outpost", "CO", 2, uniqueBuildingIdCounter);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("A small base with civilians.");
        tempBuildingType.setAutoDestructWhenConquered(true);
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("City", "Ci", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setOpenPlanetBonus(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("City that gives 2 in trade incom if the planet is open.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Civilian Outpost"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Province Capital", "PC", 20, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(5);
        tempBuildingType.setResistanceBonus(2);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Province Capital that gives 5 in incom if the planet is open. Gives bonus to the defending ground force. Only one Province Capital can be build each player.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("City"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Capital", "Ca", 30, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(7);
        tempBuildingType.setResistanceBonus(5);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setFactionUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Capital that gives 7 in incom if the planet is open. Gives a big bonus to the defending ground force. Only one Capital can be build each faction.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("University", "Un", 20, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Assassin"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Spy"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Designer"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Ace"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Captain"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Commander"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("General"));
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("A University to train VIPs");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Civilian Outpost"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Mining Base", "MiB", 8, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setOpenPlanetBonus(2);
        tempBuildingType.setClosedPlanetBonus(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A mining base that gives 2 in incom on both open and closed planets.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Civilian Outpost"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Factory", "Fa", 12, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setTechBonus(10);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A factory give better parts to build units with.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Armor Construction Yard", "Ac", 8, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.ARMORED);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.SUPPORT);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("In this buiding armored and support ground units can be build.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Russian Armor Factory", "RAF", 12, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(3);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.ARMORED);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.SUPPORT);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("In this buiding armored and support ground units can be build in big numbers.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Armor Construction Yard"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Basic Shield", "BS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A small shield to defende planets from bombardment.");
        tempBuildings.addBuilding(tempBuildingType);
        
        // ersätter Basic Shield
        tempBuildingType= new BuildingType("Planet Shield", "PS", 15, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A shield to defende planets from bombardment.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Basic Shield"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Cannon", "Can", 5, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(50);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A planet based Cannon to shoot down small ships what besiege the planet.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Heavy Cannon", "HC", 15, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(250);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A planet based heavy Cannon to shoot down ships what besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Cannon"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Puls Cannon", "PC", 15, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(50);
        tempBuildingType.setCannonRateOfFire(3);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A planet based Cannon that shoot down small ships in great numbers, what besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Cannon"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Heavy Puls Cannon", "HC", 15, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(70);
        tempBuildingType.setCannonRateOfFire(5);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDescription("A planet based Cannon that shoot down ships in great numbers, what besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Puls Cannon"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Barracks", "Bar", 5, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Barracks to train infantary");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large Barracks", "LB", 12, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(3);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Barracks to train up to 3 infantarys");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Barracks"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Russian Home Base"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Wharf"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Civilian Outpost"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Barracks"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
        
        
// ###### TROOPS ######
        
        tt = new TroopType("Narodnoe Opolcheniye","NaO",70,1,2,uTIC,10,5);
        tt.setDescription("Russian light militia unit used to defend planetes.");
        tt.setShortDescription("Russian light infantry unit.");
        tt.setAdvantages("Cheap");
        tt.setDisadvantages("Planet stationed");
        tt.setSpaceshipTravel(false);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Narodnoe Opolcheniye"));
        
        tt = new TroopType("Narodnoe Opolcheniye II","NO2",110,1,3,uTIC,12,6);
        tt.setDescription("Cheap militia infantry unit used to defend planetes.");
        tt.setShortDescription("Cheap infantry unit.");
        tt.setAdvantages("Cheap");
        tt.setDisadvantages("Planet stationed");
        tt.setCanBuild(false);
        tt.setSpaceshipTravel(false);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Narodnoe Opolcheniye II"));
        
        tt = new TroopType("Russian Light Infantry","RLI",80,1,5,uTIC,10,5);
        tt.setDescription("Cheap light infantry unit.");
        tt.setShortDescription("Cheap light infantry unit.");
        tt.setAdvantages("Cheap");
        tt.setDisadvantages("Weak against armor.");
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Russian Light Infantry"));
        
        tt = new TroopType("Russian Infantry","RI",100,1,6,uTIC,12,6);
        tt.setDescription("Cheap infantry unit.");
        tt.setShortDescription("Cheap infantry unit.");
        tt.setAdvantages("Cheap");
        tt.setCanBuild(false);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Russian Infantry"));
        
        tt = new TroopType("Spetsnaz","Spe",120,2,5,uTIC,16,16);
        tt.setDescription("Heavy infantry unit.");
        tt.setShortDescription("Heavy infantry unit.");
        tt.setAdvantages("");
        tt.setDisadvantages("");
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Spetsnaz"));
        
        tt = new TroopType("Spetsnaz II","Sp2",120,2,6,uTIC,27,27);
        tt.setDescription("Heavy infantry unit.");
        tt.setShortDescription("Heavy infantry unit.");
        tt.setAdvantages("");
        tt.setDisadvantages("");
        tt.setCanBuild(false);
        tt.setBlackmarketFirstTurn(8);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Spetsnaz II"));
        
        
        tt = new TroopType("Light Russian Artillery","LRA",30,1,3,uTIC,5,2);
        tt.setDescription("Light Russian artillery unit.");
        tt.setShortDescription("Light artillery");
        tt.setAdvantages("Artillery attack");
        tt.setDisadvantages("Almost defenseless");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(25);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Light Russian Artillery"));
        
        tt = new TroopType("Russian Artillery","RA",30,1,5,uTIC,7,7);
        tt.setDescription("Artillery unit.");
        tt.setShortDescription("Russian artillery");
        tt.setAdvantages("Artillery attack");
        tt.setDisadvantages("Almost defenseless");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(35);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        tt.setCanBuild(false);
        tt.setBlackmarketFirstTurn(8);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Russian Artillery"));
        
        tt = new TroopType("Stalin Organ","Organ",40,3,13,uTIC,15,8);
        tt.setDescription("Orgen the best artillery unit in the world.");
        tt.setShortDescription("Russian Heavy artillery");
        tt.setAdvantages("Heavy Artillery attack");
        tt.setDisadvantages("Almost defenseless");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(75);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        tt.setCanBuild(false);
        tt.setPlayerUnique(true);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Stalin Organ"));
        
        tt = new TroopType("BMP","BMP",70,2,5,uTIC,20,5);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        tt.setDescription("Light tank unit.");
        tt.setShortDescription("Light Tanks");
        tt.setAdvantages("Good versus infantry units and can attack support units (Flank attacks)");
        tt.setDisadvantages("Weak against tanks");
        tt.setDefaultPosition(BattleGroupPosition.FLANKER);
        tt.setCanBuild(false);
        tt.setBlackmarketFirstTurn(6);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("BMP"));
        
        tt = new TroopType("T-34","T34",70,3,10,uTIC,25,20);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setDescription("Light unit.");
        tt.setShortDescription("Tanks");
        tt.setAdvantages("");
        tt.setDisadvantages("");
        tt.setCanBuild(false);
        tt.setBlackmarketFirstTurn(6);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("T-34"));
        
        tt = new TroopType("SU-85","SU85",60,2,5,uTIC,10,40);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setDescription("Light tank unit amrmed with a anti tanks cannon.");
        tt.setShortDescription("Light antitank tanks");
        tt.setAdvantages("Good versus tanks units.");
        tt.setDisadvantages("Weak against infantry");
        tt.setDefaultPosition(BattleGroupPosition.FIRST_LINE);
        tt.setCanBuild(false);
        tt.setBlackmarketFirstTurn(6);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("SU-85"));
        
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Narodnoe Opolcheniye"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Light Russian Artillery"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Light Russian Artillery"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Russian Light Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Russian Light Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Russian Light Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Russian Light Infantry"));
        
        
        
        // Russian Defence platforms
        // -----------------
        // Russian Defender
        tempsst = new SpaceshipType("Russian Defender","RDef",SpaceshipType.SIZE_SMALL,0,70,SpaceshipRange.NONE,1,8,uniqueShipIdCounter,2,2);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthLarge(22);
        tempsst.setWeaponsMaxSalvoesLarge(8);
        tempsst.setIncreaseInitiative(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This small planet defender ship was developed for protection against large capital ships. It’s very cheap to have because the lack of hyper engine. Protect these ships with anti squadron’s ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Russian Defender"));
        
        
        //Russian Large Defender
        tempsst = new SpaceshipType("Russian L Defender","FDef",SpaceshipType.SIZE_LARGE,75,170,SpaceshipRange.NONE,3,25,uniqueShipIdCounter,6,25);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setArmorSmall(45);
        tempsst.setArmorMedium(20);
        tempsst.setIncreaseInitiative(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("This large planet defender ship was developed for protection against Squdrons. The ship is very cheap to have because the lack of hyper engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Russian L Defender"));
        
        // Russian Home Defender
        tempsst = new SpaceshipType("Russian Home Defender","RHD",SpaceshipType.SIZE_LARGE,20,370,SpaceshipRange.NONE,0,50,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(45);
        tempsst.setArmorMedium(20);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(250);
        tempsst.setWeaponsMaxSalvoesLarge(8);
        tempsst.setIncreaseInitiative(5);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setPlayerUnique(true);
        tempsst.setDescription("This large planet home defender offers good protections against all type of ships and special large ships. The ship hasn’t any hyper engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Russian Home Defender"));     
        
        // Squadrons
        // ---------
        // MiG-21
        tempsst = new SpaceshipType("MiG-21","M21",SpaceshipType.SIZE_SMALL,0,22,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,4,12);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A cheap dog fighter that was rebuild from a common earth fighter.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("MiG-21"));
        
        // MiG-29
        tempsst = new SpaceshipType("MiG-29","M29",SpaceshipType.SIZE_SMALL,5,22,SpaceshipRange.NONE,1,3,uniqueShipIdCounter,4,15);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Next generation of fighters.");
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("MiG-29"));
        
        // Su-24
        tempsst = new SpaceshipType("Su-24","S24",SpaceshipType.SIZE_SMALL,0,15,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,10,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(5);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A cheap anti capital fighter with week hull.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Su-24"));
        
        // Su-27
        tempsst = new SpaceshipType("Su-27","S27",SpaceshipType.SIZE_SMALL,0,25,SpaceshipRange.SHORT,2,5,uniqueShipIdCounter,8,12);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(7);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("A good all-round support fighter with hyper engine for short range jumps. This fighter is often used to patrol and defending our planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Su-27"));
        
        
        // Su-34
        tempsst = new SpaceshipType("Su-34","S34",SpaceshipType.SIZE_SMALL,6,30,SpaceshipRange.SHORT,2,6,uniqueShipIdCounter,10,2);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(17);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setIncreaseInitiative(1);
        tempsst.setBluePrintFirstTurn(20);
        tempsst.setDescription("Powerful anti capital fighter with capacity to do short range jumps. This ship needs a hangar or a friendly plant to survive. Often used in combination with a Star Destroyer to make a short history with hostiles medium or large capitals ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Su-34"));
        
        // Su-35
        tempsst = new SpaceshipType("Su-35","S35",SpaceshipType.SIZE_SMALL,7,35,SpaceshipRange.SHORT,2,7,uniqueShipIdCounter,4,2);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setWeaponsStrengthHuge(25);
        tempsst.setWeaponsMaxSalvoesHuge(3);
        tempsst.setIncreaseInitiative(1);
        tempsst.setBlackmarketFirstTurn(10);
        tempsst.setBluePrintFirstTurn(35);
        tempsst.setDescription("Powerful anti capital fighter with capacity to do short range jumps. This ship needs a hangar or a friendly plant to survive. Often used in combination with a Star Destroyer to make a short history with hostiles large or huge capitals ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Su-35"));
        
        
        // Capital ships
        // -------------
        // Steregushchy Corvette
        tempsst = new SpaceshipType("Steregushchy Corvette","SCrv",SpaceshipType.SIZE_SMALL,6,25,SpaceshipRange.LONG,2,3,uniqueShipIdCounter,6,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setCanBlockPlanet(false);
        tempsst.setDescription("Light small long range capital ship used for exploring and person transports. To small for blockading enemies planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Steregushchy Corvette"));
        
        // Sergey Gorshkov
        tempsst = new SpaceshipType("Sergey Gorshkov","SeG",SpaceshipType.SIZE_SMALL,10,50,SpaceshipRange.SHORT,3,5,uniqueShipIdCounter,15,8);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(5);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A small short range ship with capacity to attack medium sized ship if they are in great numbers. Cheap to build but expensive to own.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Sergey Gorshkov"));
        
        // Troop Cruiser
        tempsst = new SpaceshipType("Troop Cruiser","TCru",SpaceshipType.SIZE_MEDIUM,5,20,SpaceshipRange.LONG,2,5,uniqueShipIdCounter,8,8);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setTroopCapacity(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setScreened(true);
        tempsst.setDescription("This is the main troop transport ship with very poor fire power but good manoeuvre capacity that's make this ship to long range class ship.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Cruiser"));
        
        // The Army 
        tempsst = new SpaceshipType("The Army","Army",SpaceshipType.SIZE_MEDIUM,0,25,SpaceshipRange.LONG,2,10,uniqueShipIdCounter,8,8);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setTroopCapacity(6);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setScreened(true);
        tempsst.setDescription("This is the main army transport ship with very poor fire power but good manoeuvre capacity that's make this ship to long range class ship. Used to owertake planets and is tha comander ship of the army. The army have only one of this ship type.");
        tempsst.setPlayerUnique(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("The Army"));
        
        // The Army II
        tempsst = new SpaceshipType("The Army II","Arm2",SpaceshipType.SIZE_LARGE,0,150,SpaceshipRange.SHORT,3,22,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setTroopCapacity(10);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("After the war went bigger and the needs of more troops in offensive action was obvious we started the development of this ship. Used to owertake planets and is tha comander ship of the army. The army have only one of this ship type.");
        tempsst.setPlayerUnique(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setScreened(true);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("The Army II"));
        
        // Kuznetsov
        tempsst = new SpaceshipType("Kuznetsov","Kuz",SpaceshipType.SIZE_MEDIUM,6,60,SpaceshipRange.LONG,3,9,uniqueShipIdCounter,8,8);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setSquadronCapacity(4);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setScreened(true);
        tempsst.setDescription("An Old class medium carrier that can take 4 squadrons. This ship should not take any active part in the combat action.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Kuznetsov"));
        
        // Kuznetsov II
        tempsst = new SpaceshipType("Kuznetsov II","Kuz2",SpaceshipType.SIZE_MEDIUM,20,80,SpaceshipRange.LONG,4,13,uniqueShipIdCounter,12,12);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setSquadronCapacity(6);
        tempsst.setBlackmarketFirstTurn(10);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setAvailableToBuild(false);
        tempsst.setScreened(true);
        tempsst.setDescription("The next generation of carriers. Can take 6 squadrons. This ship should not take any active part in the combat action.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Kuznetsov II"));
        
        // Supply Cruiser
        tempsst = new SpaceshipType("Supply Cruiser","SCru",SpaceshipType.SIZE_MEDIUM,5,20,SpaceshipRange.SHORT,1,9,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setSupply(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setScreened(true);
        tempsst.setDescription("Old troop transporters rebuild to supply the fleet. The medium hull and badly flight capacity make it to a short ranger and easy target.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Cruiser"));
        
        // Kashin
        tempsst = new SpaceshipType("Kashin","Kas",SpaceshipType.SIZE_MEDIUM,10,100,SpaceshipRange.LONG,5,9,uniqueShipIdCounter,12,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(12);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setArmorSmall(30);
        tempsst.setDescription("A medium ship that was the best long range battle ship in the fleet at the beginning of the war. Good against medium and small ship but don't heavy enough to go against the big battle main ships. Used to control the outer space. The ship is the backbone in our fleet.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Kashin"));
        
        // Udaloy
        tempsst = new SpaceshipType("Udaloy","Uda",SpaceshipType.SIZE_MEDIUM,10,100,SpaceshipRange.LONG,5,14,uniqueShipIdCounter,12,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(12);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setAvailableToBuild(false);
        tempsst.setSquadronCapacity(1);
        tempsst.setArmorSmall(30);
        tempsst.setBlackmarketFirstTurn(15);
        tempsst.setBluePrintFirstTurn(20);
        tempsst.setDescription("To get more used of the great 'Russian Heavy Bomber' the 'Kashin' was rebuild to support an squdron. Used to control the outer space.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Udaloy"));
        
        // Sovremenny
        tempsst = new SpaceshipType("Sovremenny","Sov",SpaceshipType.SIZE_MEDIUM,10,120,SpaceshipRange.LONG,5,10,uniqueShipIdCounter,15,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(18);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setAvailableToBuild(false);
        tempsst.setArmorSmall(30);
        tempsst.setBlackmarketFirstTurn(15);
        tempsst.setBluePrintFirstTurn(20);
        tempsst.setDescription("The next generation of 'Kashin'. Good against medium and small ship but don't heavy enough to go against the big battle main ships. Used to control the outer space. The ship is the backbone in our fleet.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Sovremenny"));
                
        //Potemkin
        tempsst = new SpaceshipType("Potemkin","Pot",SpaceshipType.SIZE_LARGE,45,310,SpaceshipRange.SHORT,11,34,uniqueShipIdCounter,10,30);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(21);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(36);
        tempsst.setWeaponsMaxSalvoesLarge(18);
        tempsst.setArmorSmall(55);
        tempsst.setArmorMedium(30);
        tempsst.setInitDefence(5);
        tempsst.setSquadronCapacity(6);
        tempsst.setBombardment(1);
        tempsst.setPlanetarySurvey(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(30);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(60);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setDescription("Good against both capital ships and squadrons. This ship was build to fight by the own and have carrier capacity up to 6 squadrons. The weakness of this ship is the lack of troops and long range capacity. Often used as in the front to destroy enemies fleets and scouts out enemies planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Potemkin"));
        
        //Potemkin II
        tempsst = new SpaceshipType("Potemkin II","Pot2",SpaceshipType.SIZE_LARGE,50,330,SpaceshipRange.SHORT,11,37,uniqueShipIdCounter,10,35);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(21);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(40);
        tempsst.setWeaponsMaxSalvoesLarge(18);
        tempsst.setWeaponsStrengthHuge(20);
        tempsst.setWeaponsMaxSalvoesHuge(2);
        tempsst.setArmorSmall(55);
        tempsst.setArmorMedium(30);
        tempsst.setInitDefence(5);
        tempsst.setSquadronCapacity(6);
        tempsst.setBombardment(2);
        tempsst.setPlanetarySurvey(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(40);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(70);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setDescription("Next generation of large ships with better bombardment and armd against huge ships. Good against both capital ships and squadrons. This ship was build to fight by the own and have carrier capacity up to 6 squadrons. The weakness of this ship is the lack of troops and long range capacity. Often used as in the front to destroy enemies fleets and scouts out enemies planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Potemkin II"));
        
        //Kirov
        tempsst = new SpaceshipType("Kirov","Kir",SpaceshipType.SIZE_HUGE,80,600,SpaceshipRange.SHORT,25,86,uniqueShipIdCounter,10,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(16);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(20);
        tempsst.setWeaponsStrengthHuge(55);
        tempsst.setWeaponsMaxSalvoesHuge(12);
        tempsst.setArmorSmall(80);
        tempsst.setArmorMedium(60);
        tempsst.setArmorLarge(20);
        tempsst.setArmorHuge(5);
        tempsst.setIncreaseInitiative(10);
        tempsst.setSquadronCapacity(9);
        tempsst.setTroopCapacity(3);
        tempsst.setBombardment(1);
        tempsst.setNoRetreat(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(70);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.RARE);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("The pride of the fleet and good planet conquer with bombing, troops and ground control central. This ship has capacity to carry 9 squadrons and a system that prevents ships to start theirs hyper space engines and flee from the battlefield. Not the strongest huge class ship in the space but gives lots for the money.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Kirov"));
        
        //Kirov II
        tempsst = new SpaceshipType("Kirov II","Kir2",SpaceshipType.SIZE_HUGE,80,700,SpaceshipRange.SHORT,26,90,uniqueShipIdCounter,10,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(16);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(20);
        tempsst.setWeaponsStrengthHuge(55);
        tempsst.setWeaponsMaxSalvoesHuge(12);
        tempsst.setArmorSmall(80);
        tempsst.setArmorMedium(60);
        tempsst.setArmorLarge(20);
        tempsst.setArmorHuge(5);
        tempsst.setIncreaseInitiative(11);
        tempsst.setSquadronCapacity(9);
        tempsst.setTroopCapacity(4);
        tempsst.setBombardment(3);
        tempsst.setNoRetreat(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(100);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.RARE);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("The next generation of Kirov, better hull, bombardment and troop capacity. The pride of the fleet and good planet conquer with bombing, troops and ground control central. This ship has capacity to carry 9 squadrons and a system that prevents ships to start theirs hyper space engines and flee from the battlefield. Not the strongest huge class ship in the space but gives lots for the money.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Kirov II"));
        
        // ****** fraction unique property ******
        
        tempFaction.setGovernorVIPType(President);
        
        tempFaction.addStartingVIPType(gw.getVIPTypeByName("Designer"));
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Steregushchy Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Sergey Gorshkov"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("The Army"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Kashin"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Russian Home Defender"));
        
        // *******************************************************************************
        // ****************************** --- Forskning --- ******************************
        // *******************************************************************************
        
        tempResearch = new Research();
        
        
        // Kirov II
        tempResearchAdvantage = new ResearchAdvantage("Kirov II", "Gives possibility to build Kirov II.");
        tempResearchAdvantage.setTimeToResearch(5);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Kirov II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Kirov"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // The Army II
        tempResearchAdvantage = new ResearchAdvantage("The Army II", "Gives possibility to build The Army II.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("The Army II"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Ships
        tempResearchAdvantage = new ResearchAdvantage("Ships","Develop better troop carring ships.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("The Army II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Kirov II"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // BMP
        tempResearchAdvantage = new ResearchAdvantage("BMP","Gives BMP (light flanker tank)");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("BMP"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // T-34
        tempResearchAdvantage = new ResearchAdvantage("T-34","Gives T-34 (tank)");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("T-34"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // SU-85
        tempResearchAdvantage = new ResearchAdvantage("SU-85","Gives SU-85 (antitank tank)");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("SU-85"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Tanks
        tempResearchAdvantage = new ResearchAdvantage("Tanks","Develop better infantry.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("BMP"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("T-34"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("SU-85"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Stalin Organ
        tempResearchAdvantage = new ResearchAdvantage("Spetsnaz II","Gives Spetsnaz II");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Spetsnaz II"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Spetsnaz"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Large Barracks 
        tempResearchAdvantage = new ResearchAdvantage("Large Barracks", "Gives possibility to build Large Barracks.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Large Barracks"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Infantry
        tempResearchAdvantage = new ResearchAdvantage("Infantry","Develop better infantry.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Spetsnaz II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Large Barracks"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Russian Armor Factory 
        tempResearchAdvantage = new ResearchAdvantage("Russian Armor Factory", "Gives possibility to build Russian Armor Factory.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Russian Armor Factory"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Stalin Organ
        tempResearchAdvantage = new ResearchAdvantage("Stalin Organ","Gives Stalin Organ");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Stalin Organ"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Invader Force
        tempResearchAdvantage = new ResearchAdvantage("Invader force","Develop better units to take over planets.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Ships"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Tanks"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infantry"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Russian Armor Factory"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Stalin Organ"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        //Planet Shield 
        tempResearchAdvantage = new ResearchAdvantage("Planet Shield", "Gives Planet Shield.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Planet Shield"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Basic Shield 
        tempResearchAdvantage = new ResearchAdvantage("Basic Shield", "Gives Basic Shield.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Basic Shield"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Planet Shield"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Russian L Defender
        tempResearchAdvantage = new ResearchAdvantage("Russian L Defender", "Gives possibility to build Russian L Defender.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Russian L Defender"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Heavy Puls Cannon 
        tempResearchAdvantage = new ResearchAdvantage("Heavy Puls Cannon", "Gives Heavy Puls Cannon.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Heavy Puls Cannon"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Puls Cannon 
        tempResearchAdvantage = new ResearchAdvantage("Puls Cannon", "Gives Puls Cannon.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Puls Cannon"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Heavy Puls Cannon"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Heavy Cannon 
        tempResearchAdvantage = new ResearchAdvantage("Heavy Cannon", "Gives Heavy Cannon.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Heavy Cannon"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Cannons
        tempResearchAdvantage = new ResearchAdvantage("Cannons","Develop better cannons.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Heavy Cannon"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Puls Cannon"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Russian Infantry
        tempResearchAdvantage = new ResearchAdvantage("Russian Infantry","Gives Russian Infantry");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Russian Infantry"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Russian Light Infantry"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Russian Artillery
        tempResearchAdvantage = new ResearchAdvantage("Russian Artillery","Gives Russian Artillery");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Russian Artillery"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Light Russian Artillery"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Stalin Organ"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Narodnoe Opolcheniye II
        tempResearchAdvantage = new ResearchAdvantage("Narodnoe Opolcheniye II","Gives Narodnoe Opolcheniye II (Militia)");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Narodnoe Opolcheniye II"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Narodnoe Opolcheniye"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Troops
        tempResearchAdvantage = new ResearchAdvantage("Troops","Develop better troops.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Russian Infantry"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Russian Artillery"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Narodnoe Opolcheniye II"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Planet Defense
        tempResearchAdvantage = new ResearchAdvantage("Planet Defense","Develop planet defense.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Troops"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Cannons"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Basic Shield"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Russian L Defender"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // Udaloy
        tempResearchAdvantage = new ResearchAdvantage("Udaloy", "Gives possibility to build Udaloy(medium warship).");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Udaloy"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Sovremenny
        tempResearchAdvantage = new ResearchAdvantage("Sovremenny", "Gives possibility to build Sovremenny(medium warship).");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Sovremenny"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Kashin"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Kuznetsov II
        tempResearchAdvantage = new ResearchAdvantage("Kuznetsov II", "Gives possibility to build Kuznetsov II(carrier).");
        tempResearchAdvantage.setTimeToResearch(4);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Kuznetsov II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Kuznetsov"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Long range
        tempResearchAdvantage = new ResearchAdvantage("Long range","Develop better longe range .");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Kuznetsov II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Udaloy"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Sovremenny"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Kirov
        tempResearchAdvantage = new ResearchAdvantage("Kirov", "Gives possibility to build Kirov(Huge capital warship).");
        tempResearchAdvantage.setTimeToResearch(4);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Kirov"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Kirov II"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Potemkin II
        tempResearchAdvantage = new ResearchAdvantage("Potemkin II", "Gives possibility to build Potemkin II(Large capital warship).");
        tempResearchAdvantage.setTimeToResearch(5);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Potemkin II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Potemkin"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Potemkin
        tempResearchAdvantage = new ResearchAdvantage("Potemkin", "Gives possibility to build Potemkin (Large capital warship).");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Potemkin"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Potemkin II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Kirov"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // MiG-29
        tempResearchAdvantage = new ResearchAdvantage("MiG-29", "Gives possibility to build MiG-29 (fighter).");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("MiG-29"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("MiG-21"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Su-27
        tempResearchAdvantage = new ResearchAdvantage("Su-27", "Gives possibility to build Su-27 (Attacker).");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Su-27"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Su-35
        tempResearchAdvantage = new ResearchAdvantage("Su-35", "Gives possibility to build Su-35 (heavy bomber).");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Su-35"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Squadrons
        tempResearchAdvantage = new ResearchAdvantage("Squadrons","Develop better squadrons.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("MiG-29"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Su-27"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Su-35"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Independent Force
        tempResearchAdvantage = new ResearchAdvantage("Independent Force","Develop out fleet to control the outer space.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Squadrons"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Long range"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Potemkin"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // Factory
        tempResearchAdvantage = new ResearchAdvantage("Factory","Gives Factory");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Factory"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // University
        tempResearchAdvantage = new ResearchAdvantage("University","Gives University");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("University"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Mining Base
        tempResearchAdvantage = new ResearchAdvantage("Mining Base","Gives Mining Base");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Mining Base"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Capital
        tempResearchAdvantage = new ResearchAdvantage("Capital","Gives Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Capital"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Province Capital
        tempResearchAdvantage = new ResearchAdvantage("Province Capital","Gives Province Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Capital"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Spaceport
        tempResearchAdvantage = new ResearchAdvantage("Spaceport","Gives Spaceport");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Spaceport"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(15);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Huge Wharf
        tempResearchAdvantage = new ResearchAdvantage("Huge Wharf","Gives Huge Wharf");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Huge Wharf"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Kirov"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Large wharf
        tempResearchAdvantage = new ResearchAdvantage("Large Wharf","Gives Large Wharf");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Spaceport"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Huge Wharf"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("The Army II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Russian L Defender"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Potemkin"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Buildings
        tempResearchAdvantage = new ResearchAdvantage("Buildings","Develop new buildings.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Large Wharf"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Province Capital"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("University"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Mining Base"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Factory"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // Infrastructure 2
        tempResearchAdvantage = new ResearchAdvantage("Infrastructure 2","Gives more income to open planets.");
        tempResearchAdvantage.setOpenPlanetBonus(1);
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Infrastructure 1
        tempResearchAdvantage = new ResearchAdvantage("Infrastructure 1","Better tax control.");
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("City");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Province Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infrastructure 2"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // ************************* --- Forskning Economic --- *************************
        
        researchCorruption1 = new ResearchAdvantage("Corruption level 6","Lower corruption");
		researchCorruption1.setCorruption(tmpCorruption6);
		researchCorruption1.setTimeToResearch(5);
		researchCorruption1.setCostToResearchOneTurnInProcent(10);
        
        researchCorruption2 = new ResearchAdvantage("Corruption level 5","Lower corruption");
        researchCorruption2.setCorruption(tmpCorruption5);
        researchCorruption2.setTimeToResearch(5);
        researchCorruption2.setCostToResearchOneTurnInProcent(10);
        researchCorruption2.addChild(researchCorruption1);
        
        researchCorruption3 = new ResearchAdvantage("Corruption level 4","Lower corruption");
        researchCorruption3.setCorruption(tmpCorruption4);
        researchCorruption3.setTimeToResearch(3);
        researchCorruption3.setCostToResearchOneTurnInProcent(20);
        researchCorruption3.addChild(researchCorruption2);
        
        researchCorruption4 = new ResearchAdvantage("Corruption level 3","Lower corruption");
        researchCorruption4.setCorruption(tmpCorruption3);
        researchCorruption4.setTimeToResearch(3);
        researchCorruption4.setCostToResearchOneTurnInProcent(20);
        researchCorruption4.addChild(researchCorruption3);
        
        researchCorruption5 = new ResearchAdvantage("Corruption level 2","Lower corruption");
        researchCorruption5.setCorruption(tmpCorruption2);
        researchCorruption5.setTimeToResearch(3);
        researchCorruption5.setCostToResearchOneTurnInProcent(20);
        researchCorruption5.addChild(researchCorruption4);
        
        researchCorruption6 = new ResearchAdvantage("Corruption level 1","Lower corruption");
        researchCorruption6.setCorruption(tmpCorruption1);
        researchCorruption6.setTimeToResearch(3);
        researchCorruption6.setCostToResearchOneTurnInProcent(20);
        researchCorruption6.addChild(researchCorruption5);
        
        tempResearch.addAdvantage(researchCorruption1);
        tempResearch.addAdvantage(researchCorruption2);
        tempResearch.addAdvantage(researchCorruption3);
        tempResearch.addAdvantage(researchCorruption4);
        tempResearch.addAdvantage(researchCorruption5);
        tempResearch.addAdvantage(researchCorruption6);
        
        tempResearchAdvantage = new ResearchAdvantage("Economic","Develop Economic");
        tempResearchAdvantage.addChild(researchCorruption6);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infrastructure 1"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        
        tempResearch.setNumberOfSimultaneouslyResearchAdvantages(2);
        
        tempFaction.setResearch(tempResearch);
        
        // *******************************************************************************
        // *******************************************************************************
        // *******************************************************************************
        
        
        tempFaction.setResistanceBonus(3);
        tempFaction.setOpenPlanetBonus(-1);
        tempFaction.setAdvantages("Resistance Bonus = 3, Tech bonus on SS");
        tempFaction.setDisadvantages("Open planet gives only 1 extra incom");
        tempFaction.setHistory("At 2042 Russian was the first nation to build a space ship for travelling around in the sun system. We weren’t the first nation on Mars but we started to build a great station that we easily supplied with help of our space ship. The big problem we hade was the trips down and up from the planets. But then the base on Mars went big and big we started to get more and more resources to our space station and wharfs. By to lack of money we hade to take ides from civilian ship and replace expansive solutions with cheaper.\nThen the first pirated attacks around 2100 started we were forced to build up a military fleet. To avoid big costs we decide to build the world first space squadron. That wasn’t easy but we could fast build new squadrons in any of our wharfs that we hade.\nAt 2121 we started to bay in hyper engines to develop own engines and hulls to them. At 2166 we hade our first heavy squadron equipped with a hyper engine. Short after that we killed a spy at our centre for developing of space squadron. We can only guess who he worked for, but just 4 years later Alliance of Africa developed where first heavy squadron.\nThe 22 Mars in 2242 the first attacked against the earth was made. We were taken by surprise and with our guard down. The ships that could escape did that but unfortunately not to the same place. It’s now time to start build up our military fleet and to reconnect our colonies. We don’t have the greatest fleet but don’t forget that we have the best ground defence in the world.");
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setCorruption(tmpCorruption);
        //tempFaction.setAlignment(russian);
        gw.addFaction(tempFaction);
        
//      ######## EU (Turkos) ########
//      XXX EU (Turkos)       
        tempFaction = new Faction("EU",Faction.getColorHexString(128,255,255),eu);
        
//      Adding Buildings to the faction
        tempBuildings = new Buildings();
        
        tempBuildingType= new BuildingType("Small Wharf", "W1", 3, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(1);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Small Wharf what can build small ships");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Medium Wharf", "W2", 12, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(2);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Medium Wharf what can build small and medium ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Small Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large Wharf", "W3", 20, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(3);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge EU Wharf", "W5", 8, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setFactionUnique(true);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Spaceport", "SP", 8, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Build this building on the planets and long range ways will be short range between planetes whit spaceports");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Small Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Civilian Outpost", "CO", 2, uniqueBuildingIdCounter);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("A small base with civilians.");
        tempBuildingType.setAutoDestructWhenConquered(true);
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("EU Home Base", "EHom", 20, uniqueBuildingIdCounter);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setOpenPlanetBonus(8);
        tempBuildingType.setClosedPlanetBonus(8);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("The home base is made as a head offices. Gives an income of 8.");
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Mercenary Mech Center", "MMC", 10, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.ARMORED);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.SUPPORT);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Base to hire mercenarys mechs");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Civilian Outpost"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("City", "Ci", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setOpenPlanetBonus(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("City that gives 2 in trade incom if the planet is open.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Civilian Outpost"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Province Capital", "PC", 20, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(5);
        tempBuildingType.setResistanceBonus(2);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Province Capital that gives 5 in incom if the planet is open. Gives bonus to the defending ground force. Only one Province Capital can be build each player.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("City"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Capital", "Ca", 30, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(7);
        tempBuildingType.setResistanceBonus(5);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setFactionUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Capital that gives 7 in incom if the planet is open. Gives a big bonus to the defending ground force. Only one Capital can be build each faction.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("University", "Un", 20, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Assassin"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Spy"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Designer"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Ace"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Captain"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Commander"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("General"));
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A University to train VIPs");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Civilian Outpost"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Company Base", "CB", 2, uniqueBuildingIdCounter);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("Often used as a building ground by companys to build more advanced buildigns.");
        tempBuildingType.setAutoDestructWhenConquered(true);
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Factory", "Fa", 12, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setTechBonus(10);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A factory give better parts to build units with.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Planet Shield", "PS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setOpenPlanetBonus(1);
        tempBuildingType.setDescription("A shield to defende planets from bombardment. Gives 1 in incom on both open and closed planets. ");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large Planet Shield", "LPS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setShieldCapacity(3);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setOpenPlanetBonus(1);
        tempBuildingType.setDescription("A shield to defende planets from bombardment. Gives 1 in incom on both open and closed planets. ");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Planet Shield"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Cannon", "Can", 5, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(50);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A planet based Cannon to shoot down small ships what besiege the planet.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Company Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Barracks", "Bar", 10, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Barracks to train infantary");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large Barracks", "LB", 14, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(3);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Barracks to train up to 3 infantarys");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Barracks"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("EU Home Base"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Wharf"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Civilian Outpost"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Company Base"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Barracks"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
        
        
        
// ###### TROOPS ######
        
        tt = new TroopType("Robot Light Infantry","RoLI",60,1,4,uTIC,20,5);
        tt.setDescription("Robot light infantry unit often used to defend planetes.");
        tt.setShortDescription("Cheap light infantry unit.");
        tt.setAdvantages("Cheap");
        tt.setDisadvantages("Weak against armor.");
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Robot Light Infantry"));
        
        tt = new TroopType("Hovercraft Robot Infantry","HRoI",55,2,4,uTIC,20,5);
        tt.setDescription("Light Robotic infantry unit often used to flank enemy.");
        tt.setShortDescription("Light infantry unit to flank enemy.");
        tt.setAdvantages("Flankers");
        tt.setDisadvantages("Weak.");
        tt.setCanBuild(false);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        tt.setDefaultPosition(BattleGroupPosition.FLANKER);
        tt.setBlackmarketFirstTurn(8);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Hovercraft Robot Infantry"));
        
        tt = new TroopType("Robot Infantry","RoI",100,2,8,uTIC,25,20);
        tt.setDescription("Robot infantry unit.");
        tt.setShortDescription("Infantry unit.");
        tt.setAdvantages("Big firepower");
        tt.setDisadvantages("");
        tt.setCanBuild(false);
        tt.setBlackmarketFirstTurn(8);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Robot Infantry"));
        
        tt = new TroopType("Robot Heavy Infantry","RoHI",140,3,12,uTIC,30,30);
        tt.setDescription("Heavy infantry unit.");
        tt.setShortDescription("Heavy infantry unit.");
        tt.setAdvantages("");
        tt.setDisadvantages("");
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Robot Heavy Infantry"));
        
        tt = new TroopType("Robot Heavy Infantry II","RoHI2",140,3,14,uTIC,35,30);
        tt.setDescription("Heavy infantry unit.");
        tt.setShortDescription("Heavy infantry unit.");
        tt.setAdvantages("Good against all type of units");
        tt.setDisadvantages("Expensive");
        tt.setCanBuild(false);
        tt.setBlackmarketFirstTurn(8);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Robot Heavy Infantry II"));
        
        tt = new TroopType("Robot Artillery","RoA",50,1,5,uTIC,7,7);
        tt.setDescription("Robot artillery unit.");
        tt.setShortDescription("Artillery");
        tt.setAdvantages("Artillery attack");
        tt.setDisadvantages("Almost defenseless");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(35);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        tt.setBlackmarketFirstTurn(8);
        tt.setCanBuild(false);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Robot Artillery"));
        
        tt = new TroopType("Panther Mech","PM",50,1,6,uTIC,40,4);
        tt.setDescription("One heavy mech with a skilful mercenary pilot, armed with two miniguns.");
        tt.setShortDescription("One heavy Mech armed with two miniguns.");
        tt.setAdvantages("Good against infantry");
        tt.setDisadvantages("Only one mech");
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setPlayerUnique(true);
        tt.setCanBuild(false);
        tt.setTargetingType(TroopTargetingType.ANTIINFANTRY);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Panther Mech"));
        
        tt = new TroopType("Hummel Mech","HM",20,1,6,uTIC,20,20);
        tt.setDescription("One heavy mech with a skilful mercenary pilot, the mech have a heavy cannon on the back to use as a artillery.");
        tt.setShortDescription("One heavy Mech with a artillery cannon.");
        tt.setAdvantages("Artillery attack");
        tt.setDisadvantages("Only one mech");
        tt.setTypeOfTroop(TypeOfTroop.SUPPORT);
        tt.setAttackArtillery(50);
        tt.setDefaultPosition(BattleGroupPosition.SUPPORT);
        tt.setPlayerUnique(true);
        tt.setCanBuild(false);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Hummel Mech"));
        
        
        tt = new TroopType("Tiger Mech","TM",50,1,6,uTIC,20,45);
        tt.setTypeOfTroop(TypeOfTroop.ARMORED);
        tt.setTargetingType(TroopTargetingType.ANTITANK);
        tt.setDescription("One heavy mech with a skilful mercenary pilot, armed with antitank cannon.");
        tt.setShortDescription("One heavy Mech with a antitank cannon.");
        tt.setAdvantages("Good versus tanks units.");
        tt.setDisadvantages("Only one mech");
        tt.setDefaultPosition(BattleGroupPosition.FIRST_LINE);
        tt.setCanBuild(false);
        tt.setPlayerUnique(true);
        tt.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        gw.addTroopType(tt);
        tempFaction.addTroopType(gw.getTroopTypeByName("Tiger Mech"));
        
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Robot Light Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Robot Light Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Robot Heavy Infantry"));
        
        
        
        
        // EU Defence platforms
        // -----------------
        // EU Defender
        tempsst = new SpaceshipType("EU Defender","EDef",SpaceshipType.SIZE_HUGE,240,270,SpaceshipRange.NONE,5,35,uniqueShipIdCounter,10,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(30);
        tempsst.setWeaponsMaxSalvoesLarge(10);
        tempsst.setWeaponsStrengthHuge(40);
        tempsst.setWeaponsMaxSalvoesHuge(10);
        tempsst.setArmorSmall(30);
        tempsst.setArmorMedium(15);
        tempsst.setArmorLarge(10);
        tempsst.setInitDefence(5);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This huge planet defender ship was developed for protection against large and huge capital ships. It’s very cheap to have because the lack of hyper engine");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("EU Defender"));
        
        // EU Home Defender
        tempsst = new SpaceshipType("EU Home Defender","EHD",SpaceshipType.SIZE_LARGE,105,20,SpaceshipRange.NONE,0,50,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(10);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(10);
        tempsst.setWeaponsStrengthLarge(250);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setIncreaseInitiative(5);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setPlayerUnique(true);
        tempsst.setDescription("This large planet home defender offers good protections against all type of ships. The ship hasn’t any hyper engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("EU Home Defender"));        
        
        // Squadrons
        // ---------
        // Rafale
        tempsst = new SpaceshipType("Rafale","Raf",SpaceshipType.SIZE_SMALL,13,7,SpaceshipRange.NONE,1,3,uniqueShipIdCounter,5,19);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setDescription("A good and cheap dog fighter.");
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Rafale"));
        
        // Typhoon
        tempsst = new SpaceshipType("Typhoon","Typ",SpaceshipType.SIZE_SMALL,20,8,SpaceshipRange.NONE,1,5,uniqueShipIdCounter,10,24);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setDescription("Next generation of fighters.");
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(12);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Typhoon"));
        
        // Vulcan
        tempsst = new SpaceshipType("Vulcan","Vul",SpaceshipType.SIZE_SMALL,7,7,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,10,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(12);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(7);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setDescription("A good and cheap anti capital fighter.");
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Vulcan"));
        
        // Marauder
        tempsst = new SpaceshipType("Marauder","Mar",SpaceshipType.SIZE_SMALL,15,8,SpaceshipRange.NONE,2,5,uniqueShipIdCounter,5,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthLarge(20);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setWeaponsStrengthHuge(50);
        tempsst.setWeaponsMaxSalvoesHuge(1);
        tempsst.setAvailableToBuild(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A good and cheap anti capital fighter.  Used to take down huge ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Marauder"));
        
        // Viggen
        tempsst = new SpaceshipType("Viggen","Vig",SpaceshipType.SIZE_SMALL,15,10,SpaceshipRange.LONG,2,6,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setDescription("A good all-round support fighter with hyper engine for long range jumps. This fighter is often used to patrol and defending our planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Viggen"));
        
        // Gripen
        tempsst = new SpaceshipType("Gripen","JAS",SpaceshipType.SIZE_SMALL,23,10,SpaceshipRange.LONG,2,9,uniqueShipIdCounter,15,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(12);
        tempsst.setDescription("The next generation av attacker. A good all-round support fighter with hyper engine for long range jumps. This fighter is often used to patrol and defending our planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Gripen"));
        
        
        // Capital ships
        // -------------
        // EU Corvette
        tempsst = new SpaceshipType("EU Corvette","EUCr",SpaceshipType.SIZE_SMALL,20,10,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,11,7);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setCanBlockPlanet(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Light small long range capital ship used for exploring and person transports. To small for blockading enemies planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("EU Corvette"));
        
        // Assault Frigate
        tempsst = new SpaceshipType("Assault Frigate","AFrg",SpaceshipType.SIZE_SMALL,20,15,SpaceshipRange.LONG,3,6,uniqueShipIdCounter,17,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(6);
        tempsst.setWeaponsMaxSalvoesMedium(1);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A small ship with capacity to attack medium sized ship if they are in great numbers. Armed with good shields and weapons. One of the most advanced frigate in the whole world.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Assault Frigate"));
        
        // HMS Victory
        tempsst = new SpaceshipType("HMS Victory","HVic",SpaceshipType.SIZE_SMALL,35,15,SpaceshipRange.LONG,2,10,uniqueShipIdCounter,14,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setPlayerUnique(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.NEVER);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A small ship with capacity to attack medium sized ship if they are in great numbers. Armed with good shields and weapons. This is the most advanced frigate in the whole world.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("HMS Victory"));
        
        // Troop Fighter
        tempsst = new SpaceshipType("Troop Fighter","TFig",SpaceshipType.SIZE_MEDIUM,20,15,SpaceshipRange.LONG,2,8,uniqueShipIdCounter,5,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setTroopCapacity(3);
        tempsst.setDescription("This ship is a combination between a troop ship and anti squadron’s ship. The week shield and medium hull makes it vulnerable against capital ships. One of the most used ship in our fleet.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Fighter"));
        
        // Troop Fighter II
        tempsst = new SpaceshipType("Troop Fighter II","TFi2",SpaceshipType.SIZE_MEDIUM,30,15,SpaceshipRange.LONG,2,10,uniqueShipIdCounter,5,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setTroopCapacity(4);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(12);
        tempsst.setDescription("The next generation of troop transporter with bigger shields and troop capacity. This ship is a combination between a troop ship and anti squadron’s ship. The week shield and medium hull makes it vulnerable against capital ships. One of the most used ship in our fleet.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Fighter II"));
        
        // Fighter Transporter
        tempsst = new SpaceshipType("Fighter Transporter","FTra",SpaceshipType.SIZE_MEDIUM,40,30,SpaceshipRange.LONG,4,13,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setWeaponsStrengthLarge(5);
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setSquadronCapacity(4);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("The fleet’s carrier much more armed then carriers are used to be. Have capacity to shoot against up to large ships. Can carrier up to 4 squadrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Transporter"));
        
        // Fighter Transporter II
        tempsst = new SpaceshipType("Fighter Transporter II","FTr2",SpaceshipType.SIZE_MEDIUM,50,35,SpaceshipRange.LONG,4,15,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setWeaponsStrengthLarge(15);
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setSquadronCapacity(4);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("The next generation of carriers. The fleet’s carrier much more armed then carriers are used to be. Have capacity to shoot against up to large ships. Can carrier up to 4 squadrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Transporter II"));
        
        // Supply Transporter
        tempsst = new SpaceshipType("Supply Transporter","Stra",SpaceshipType.SIZE_MEDIUM,15,20,SpaceshipRange.LONG,1,11,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(10);
        tempsst.setSupply(4);
        tempsst.setScreened(true);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This supply ship is not suitable for combat and should not be active in fights. The best with the ship is the capacity to do long range trips.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Transporter"));
        
        // Planet Destroyer
        tempsst = new SpaceshipType("Planet Destroyer","PDes",SpaceshipType.SIZE_MEDIUM,90,60,SpaceshipRange.SHORT,5,12,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(8);
        tempsst.setWeaponsStrengthLarge(15);
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setBombardment(1);
        tempsst.setArmorSmall(10);
        tempsst.setBluePrintFirstTurn(20);
        tempsst.setDescription("A medium ship that is all-round and good against large capital ships. Heavy armed to bombard planets and with excellent shields. Because the heavy weapons take a lot of space this ship don’t have any room for the big long ranges engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Planet Destroyer"));
                
        //Vittorio Veneto 
        tempsst = new SpaceshipType("Vittorio Veneto","Vit",SpaceshipType.SIZE_LARGE,160,250,SpaceshipRange.SHORT,11,41,uniqueShipIdCounter,20,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(47);
        tempsst.setWeaponsMaxSalvoesLarge(16);
        tempsst.setArmorSmall(15);
        tempsst.setArmorMedium(5);
        tempsst.setIncreaseInitiative(5);
        tempsst.setSquadronCapacity(4);
        tempsst.setBlackmarketFirstTurn(30);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(60);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("This ship was build to get more fire power to the fleet. The carrier capacity is up to 4 squadrons. The weakness of this ship is the lack of troops and long range capacity.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Vittorio Veneto"));
        
        //King George
        tempsst = new SpaceshipType("King George","King",SpaceshipType.SIZE_LARGE,180,270,SpaceshipRange.SHORT,11,45,uniqueShipIdCounter,20,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(16);
        tempsst.setWeaponsStrengthHuge(20);
        tempsst.setWeaponsMaxSalvoesHuge(2);
        tempsst.setArmorSmall(15);
        tempsst.setArmorMedium(5);
        tempsst.setIncreaseInitiative(6);
        tempsst.setSquadronCapacity(5);
        tempsst.setBlackmarketFirstTurn(40);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(70);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("Next generation of Vittorio Veneto. This ship was build to get more fire power to the fleet. The carrier capacity is up to 5 squadrons. The weakness of this ship is the lack of troops and long range capacity.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("King George"));
        
        //Battlecruiser
        tempsst = new SpaceshipType("Battlecruiser","BatC",SpaceshipType.SIZE_LARGE,270,300,SpaceshipRange.LONG,20,66,uniqueShipIdCounter,10,25);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsMaxSalvoesMedium(12);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(8);
        tempsst.setWeaponsStrengthHuge(30);
        tempsst.setWeaponsMaxSalvoesHuge(5);
        tempsst.setArmorSmall(10);
        tempsst.setArmorMedium(5);
        tempsst.setIncreaseInitiative(10);
        tempsst.setTroopCapacity(3);
        tempsst.setPlanetarySurvey(true);
        tempsst.setInitDefence(2);
        tempsst.setBombardment(2);
        tempsst.setBlackmarketFirstTurn(70);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.RARE);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("The only large long range ship type in the world. Great bombardment capacity and troops to overtake planets. Good against all types of ships but missing carrier capacity. Good planet surveying. This is likely the most advanced ship in the world but make it expansive to build and maintenance.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battlecruiser"));
        
        //Bismarck
        tempsst = new SpaceshipType("Bismarck","Bism",SpaceshipType.SIZE_LARGE,300,300,SpaceshipRange.LONG,20,80,uniqueShipIdCounter,10,25);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsMaxSalvoesMedium(12);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(8);
        tempsst.setWeaponsStrengthHuge(50);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setArmorSmall(10);
        tempsst.setArmorMedium(5);
        tempsst.setIncreaseInitiative(12);
        tempsst.setTroopCapacity(3);
        tempsst.setPlanetarySurvey(true);
        tempsst.setInitDefence(2);
        tempsst.setBombardment(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setAvailableToBuild(false);
        tempsst.setPlayerUnique(true);
        tempsst.setDescription("The king of long range ships. The only large long range ship in the world. Great bombardment capacity and troops to overtake planets. Good against all types of ships but missing carrier capacity. Good planet surveying. This is likely the most advanced ship in the world but make it expansive to build and maintenance. ");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bismarck"));
        
        // ****** fraction unique property ******
        
        tempFaction.setGovernorVIPType(President);
        
        tempFaction.addStartingVIPType(gw.getVIPTypeByName("Commander"));
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("EU Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Assault Frigate"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Troop Fighter"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Fighter Transporter"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("EU Home Defender"));
        
        // *******************************************************************************
        // ****************************** --- Forskning --- ******************************
        // *******************************************************************************
        
        tempResearch = new Research();
        
        
        // Bismarck
        tempResearchAdvantage = new ResearchAdvantage("Bismarck", "Gives possibility to build Bismark.");
        tempResearchAdvantage.setTimeToResearch(6);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Bismarck"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // HMS Victory
        tempResearchAdvantage = new ResearchAdvantage("HMS Victory", "Gives possibility to build HMS Victory.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("HMS Victory"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Fleet
        tempResearchAdvantage = new ResearchAdvantage("Fleet","Develop fleet leader units.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("HMS Victory"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Bismarck"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Hummel Mechs
        tempResearchAdvantage = new ResearchAdvantage("Hummel Mechs","Gives Hummel Mechs");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Hummel Mech"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Tiger Mechs
        tempResearchAdvantage = new ResearchAdvantage("Tiger Mechs","Gives Tiger Mechs");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Tiger Mech"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Panther Mechs
        tempResearchAdvantage = new ResearchAdvantage("Panther Mechs","Gives Panther Mechs");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Panther Mech"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Army
        tempResearchAdvantage = new ResearchAdvantage("Army","Develop army leader units.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Panther Mechs"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Hummel Mechs"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Tiger Mechs"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(20);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Leader of the herd
        tempResearchAdvantage = new ResearchAdvantage("Leader of the herd","Develop our leader units.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Army"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Fleet"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // King George
        tempResearchAdvantage = new ResearchAdvantage("King George", "Gives possibility to build King George class.");
        tempResearchAdvantage.setTimeToResearch(4);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("King George"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Vittorio Veneto"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Marauder
        tempResearchAdvantage = new ResearchAdvantage("Marauder", "Gives possibility to build Marauder class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Marauder"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Huge weaponse
        tempResearchAdvantage = new ResearchAdvantage("Huge weaponse", "Gives possibility to develop better weaponse against huge ships.");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Marauder"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("King George"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Battlecruiser
        tempResearchAdvantage = new ResearchAdvantage("Battlecruiser", "Gives possibility to build Battlecruiser class.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Battlecruiser"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Bismarck"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Vittorio Veneto
        tempResearchAdvantage = new ResearchAdvantage("Vittorio Veneto", "Gives possibility to build Vittorio Veneto Battleship class.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Vittorio Veneto"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Battlecruiser"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Huge weaponse"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Huge EU Wharf
        tempResearchAdvantage = new ResearchAdvantage("Huge EU Wharf","Gives Huge EU Wharf");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Huge EU Wharf"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Large wharf
        tempResearchAdvantage = new ResearchAdvantage("Large Wharf","Gives Large Wharf");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Vittorio Veneto"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Huge EU Wharf"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Heavy Fleet
        tempResearchAdvantage = new ResearchAdvantage("Heavy Fleet","Gives possibility to develop our heavy fleet.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Large Wharf"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        // Hovercraft Robot Infantry
        tempResearchAdvantage = new ResearchAdvantage("Hovercraft Robot Infantry","Gives Hovercraft Robot Infantry");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Hovercraft Robot Infantry"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Robot Heavy Infantry II
        tempResearchAdvantage = new ResearchAdvantage("Robot Heavy Infantry II","Gives Robot Heavy Infantry II");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Robot Heavy Infantry II"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Robot Heavy Infantry"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(15);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Infantry
        tempResearchAdvantage = new ResearchAdvantage("Infantry","Gives possibility to develop better infantry units.");
        tempResearchAdvantage.setTimeToResearch(0);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Hovercraft Robot Infantry"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Robot Heavy Infantry II"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Robot Artillery
        tempResearchAdvantage = new ResearchAdvantage("Robot Artillery","Gives Robot Artillery");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Robot Artillery"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Mercenary Mech Center
        tempResearchAdvantage = new ResearchAdvantage("Mercenary Mech Center","Gives Mercenary Mech Center");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Mercenary Mech Center"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Robot Artillery"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Tiger Mechs"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Hummel Mechs"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Troop Fighter II
        tempResearchAdvantage = new ResearchAdvantage("Troop Fighter II", "Gives possibility to build Troop Fighter II class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Troop Fighter II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Troop Fighter"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infantry"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Large Barracks
        tempResearchAdvantage = new ResearchAdvantage("Large Barracks","Gives Large Barracks");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Large Barracks"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infantry"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Offensive Army
        tempResearchAdvantage = new ResearchAdvantage("Offensive Army","Gives possibility to develop our offensive army.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Large Barracks"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Troop Fighter II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Mercenary Mech Center"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // Cannon
        tempResearchAdvantage = new ResearchAdvantage("Cannon","Gives Cannon");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Cannon"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Large Planet Shield
        tempResearchAdvantage = new ResearchAdvantage("Large Planet Shield","Gives Large Planet Shield");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Large Planet Shield"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Planet Shield
        tempResearchAdvantage = new ResearchAdvantage("Planet Shield","Gives Planet Shield");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Planet Shield"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Large Planet Shield"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Robot Infantry
        tempResearchAdvantage = new ResearchAdvantage("Robot Infantry","Gives Robot Infantry");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Robot Infantry"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Robot Heavy Infantry II"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Planet Defense
        tempResearchAdvantage = new ResearchAdvantage("Planet Defense","Gives possibility to develop better defense of planets.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Robot Infantry"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Planet Shield"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Cannon"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // Typhoon
        tempResearchAdvantage = new ResearchAdvantage("Typhoon", "Gives possibility to build Typhoon class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Typhoon"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Rafale"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Gripen
        tempResearchAdvantage = new ResearchAdvantage("Gripen", "Gives possibility to build Gripen class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Gripen"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Viggen"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Fighter Transporter II
        tempResearchAdvantage = new ResearchAdvantage("Fighter Transporter II", "Gives possibility to build Fighter Transporter II class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Fighter Transporter II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Fighter Transporter"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Light Fleet
        tempResearchAdvantage = new ResearchAdvantage("Light Fleet","Gives possibility to develop the light fleet.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Fighter Transporter II"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Gripen"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Typhoon"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        // Factory
        tempResearchAdvantage = new ResearchAdvantage("Factory","Gives Factory");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Factory"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // University
        tempResearchAdvantage = new ResearchAdvantage("University","Gives University");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("University"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Capital
        tempResearchAdvantage = new ResearchAdvantage("Capital","Gives Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Capital"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Province Capital
        tempResearchAdvantage = new ResearchAdvantage("Province Capital","Gives Province Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Capital"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Improvments III
        tempResearchAdvantage = new ResearchAdvantage("Improvments III","Cannon will give one in incom on open planets.");
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Cannon");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        aResearchUpgradeBuilding.setClosedPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Improvments II
        tempResearchAdvantage = new ResearchAdvantage("Improvments II","University will give one in incom on open planets.");
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("University");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Improvments III"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Improvments I
        tempResearchAdvantage = new ResearchAdvantage("Improvments I", "Better tax control.");
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("City");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Province Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Improvments II"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        
        //Planet Improvments
        tempResearchAdvantage = new ResearchAdvantage("Planet Improvments","Gives possibility to develop new buildings.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Factory"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("University"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Improvments I"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Province Capital"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // ************************* --- Forskning Economic --- *************************
        
        researchCorruption1 = new ResearchAdvantage("Corruption level 6","Lower corruption");
		researchCorruption1.setCorruption(tmpCorruption6);
		researchCorruption1.setTimeToResearch(5);
		researchCorruption1.setCostToResearchOneTurnInProcent(10);
        
        researchCorruption2 = new ResearchAdvantage("Corruption level 5","Lower corruption");
        researchCorruption2.setCorruption(tmpCorruption5);
        researchCorruption2.setTimeToResearch(5);
        researchCorruption2.setCostToResearchOneTurnInProcent(10);
        researchCorruption2.addChild(researchCorruption1);
        
        researchCorruption3 = new ResearchAdvantage("Corruption level 4","Lower corruption");
        researchCorruption3.setCorruption(tmpCorruption4);
        researchCorruption3.setTimeToResearch(3);
        researchCorruption3.setCostToResearchOneTurnInProcent(20);
        researchCorruption3.addChild(researchCorruption2);
        
        researchCorruption4 = new ResearchAdvantage("Corruption level 3","Lower corruption");
        researchCorruption4.setCorruption(tmpCorruption3);
        researchCorruption4.setTimeToResearch(3);
        researchCorruption4.setCostToResearchOneTurnInProcent(20);
        researchCorruption4.addChild(researchCorruption3);
        
        researchCorruption5 = new ResearchAdvantage("Corruption level 2","Lower corruption");
        researchCorruption5.setCorruption(tmpCorruption2);
        researchCorruption5.setTimeToResearch(3);
        researchCorruption5.setCostToResearchOneTurnInProcent(20);
        researchCorruption5.addChild(researchCorruption4);
        
        researchCorruption6 = new ResearchAdvantage("Corruption level 1","Lower corruption");
        researchCorruption6.setCorruption(tmpCorruption1);
        researchCorruption6.setTimeToResearch(3);
        researchCorruption6.setCostToResearchOneTurnInProcent(20);
        researchCorruption6.addChild(researchCorruption5);
        
        tempResearch.addAdvantage(researchCorruption1);
        tempResearch.addAdvantage(researchCorruption2);
        tempResearch.addAdvantage(researchCorruption3);
        tempResearch.addAdvantage(researchCorruption4);
        tempResearch.addAdvantage(researchCorruption5);
        tempResearch.addAdvantage(researchCorruption6);
        
        tempResearchAdvantage = new ResearchAdvantage("Economic","Develop Economic");
        tempResearchAdvantage.addChild(researchCorruption6);
        //tempResearchAdvantage.addChild(tempResearch.getAdvantage("Trading I"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        
        tempResearch.setNumberOfSimultaneouslyResearchAdvantages(2);
        
        tempFaction.setResearch(tempResearch);
        
        // *******************************************************************************
        // *******************************************************************************
        // *******************************************************************************
        
        
        tempFaction.setAdvantages("Good shields");
        tempFaction.setDisadvantages("week hulls");
        tempFaction.setHistory("At the year 2041 we made our first trip with a new space ship type that could travel between earth and space without any helps from rockets. That’s gave us much more capacity to travel up to the space. Just after that we started a program to build ships to travel around in the space and colonise the sun system.\nIn 2098 the first pirate attacked in space chocked us all and we were not prepared on that at all. To defend us against the attacked we started to travel in fleets.\nAt 2104 we were the first nation that hade developed a hyper drive engine, but it took us more then 8 years before we could do the first jump between 2 planets. Then doing this type of jumps you need to found an absolute free ways. One single small stone could destroy a whole ship then it hits the ship in 232 times lighting speed. 4 years after we did our first jumped to a planet in another sun system. That started a new epoch in space history and soon after that we decide to sell hyper engines outside the nation fleet. The reason was that we found out that a lot of nation was trying to steal the technology from us. The only nation that we hade evidence on was Russian but we know that more nations or companies was trying. Then the hyper drive was free to bay a lot of pore people bought old ships and equipped it with the hyper drivers and did a jump to unknown sun system to start a new life. We know that many of these desperate trips ended in the hyper jump, some of the ships hull wasn’t strong enough or the jump way was unsafe.\nTo develop more safely jump we decide to develop shields to protects the ships against smaller stones. With ships equipped with shield we could began travel to all smaller colonies and start trading with them. Around 2190 the first heavy armed battle ships was made. That was of course a military threat so we decided to start developing new military ship to protect us. Lucky for us we hade a pretty good military fleet around 2230 then the bigger pirates attacks started. But we decided to start develop the strongest and most advanced large ship in the world. The first Galaxy Frigate was made and in duty deep in the space just before the war began.\nWe don’t know way and who that started this war but we hade to defend us. We have the most advanced shields to protect our ships so don’t be afraid. We must now build up our wharfs so we can secure independents with help of ours Galaxy Frigates.");
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setCorruption(tmpCorruption);
        //tempFaction.setAlignment(eu);
        gw.addFaction(tempFaction);
        
//      ######## Federation of liberty (Lila) ########
//      XXX Federation of liberty        
        tempFaction = new Faction("Federation Of Liberty",Faction.getColorHexString(120,50,180),federation);
        
//      Adding Buildings to the faction
        tempBuildings = new Buildings();
        
        tempBuildingType= new BuildingType("Small Hidden Wharf", "W1", 3, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setVisibleOnMap(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Small Wharf what can build small ships. This building is a hidden base under the surface. That makes it to the only building that can build ship then the planet is besieged.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Medium Liberty Wharf", "W2", 12, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(2);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Medium Wharf what can build small and medium ships");
        //tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Small Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large Liberty Wharf", "W3", 16, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(3);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Liberty Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge Liberty Wharf", "W5", 22, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Liberty Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Space station", "SS", 11, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setOpenPlanetBonus(1);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Build this building on the planets and long range ways will be short range between planetes whit Space station. Give 1 incom on open planets");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Federation Home Base", "FHB", 20, uniqueBuildingIdCounter);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setClosedPlanetBonus(8);
        tempBuildingType.setOpenPlanetBonus(8);
        tempBuildingType.setDescription("The home base is made as raids headquarter. Gives an income of 8.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Planet Base", "PB", 2, uniqueBuildingIdCounter);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("A small base.");
        tempBuildingType.setAutoDestructWhenConquered(true);
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("City", "Ci", 10, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setOpenPlanetBonus(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("City that gives 2 in trade incom if the planet is open.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Planet Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Province Capital", "PC", 20, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(5);
        tempBuildingType.setResistanceBonus(2);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Province Capital that gives 5 in incom if the planet is open. Gives bonus to the defending ground force. Only one Province Capital can be build each player.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("City"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Capital", "Ca", 30, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(7);
        tempBuildingType.setResistanceBonus(5);
        tempBuildingType.isAutoDestructWhenConquered();
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setFactionUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Capital that gives 7 in incom if the planet is open. Gives a big bonus to the defending ground force. Only one Capital can be build each faction.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Mercenary Base", "MB", 8, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDescription("Base to hire mercenarys.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Planet Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Mercenary Stronghold", "MS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(3);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.INFANTRY);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("Base to hire mercenarys in great numbers.");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Mercenary Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("University", "Un", 20, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Assassin"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Spy"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Designer"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Ace"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Captain"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("Commander"));
        tempBuildingType.addBuildVIPType(gw.getVIPTypeByName("General"));
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A University to train VIPs");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Liberty Mining Base", "LMB", 8, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(1);
        tempBuildingType.setClosedPlanetBonus(1);
        tempBuildingType.setCannonDamage(50);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A mining base that gives 1 in incom on both open and closed planet. This building is protected by a cannon");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Planet Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Trading Base", "TrB", 8, uniqueBuildingIdCounter);
        tempBuildingType.setOpenPlanetBonus(6);
        tempBuildingType.setCannonDamage(50);
        tempBuildingType.setCannonRateOfFire(2);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlayerUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A trading base that gives 6 in incom on open planet. This building is protected by 2 cannons");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Liberty Mining Base"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Factory", "Fa", 12, uniqueBuildingIdCounter);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setTechBonus(10);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setDescription("A factory give better parts to build units with.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Armor Construction Yard", "Ac", 7, uniqueBuildingIdCounter);
        tempBuildingType.setTroopSize(1);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.ARMORED);
        tempBuildingType.addTypeOfTroop(TypeOfTroop.SUPPORT);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("In this buiding armored and support ground units can be build.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Heavy Cannon", "HC", 12, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(250);
        tempBuildingType.setCannonRateOfFire(1);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A planet based heavy Cannon to shoot down ships what besiege the planet.");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Puls Cannon", "PC", 12, uniqueBuildingIdCounter);
        tempBuildingType.setCannonDamage(50);
        tempBuildingType.setCannonRateOfFire(3);
        tempBuildingType.setInOrbit(false);
        tempBuildingType.setPlanetUnique(true);
        tempBuildingType.setDeveloped(false);
        tempBuildingType.setDescription("A planet based Cannon that shoot down small ships in great numbers, what besiege the planet.");
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Federation Home Base"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Hidden Wharf"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Planet Base"));
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Mercenary Base"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
        
        
// ###### TROOPS ######
        
        // the free peopels army
        
        //tempFaction.addTroopType(freedomForce);
        
        tempFaction.addTroopType(gw.getTroopTypeByName("Mercenary Infantry"));
        
        tempFaction.addTroopType(gw.getTroopTypeByName("Mercenary HI"));
        
        tempFaction.addTroopType(gw.getTroopTypeByName("Mercenary HAI"));
        
        tempFaction.addTroopType(gw.getTroopTypeByName("Panther Mech"));
        
        tempFaction.addTroopType(gw.getTroopTypeByName("Hummel Mech"));
        
        tempFaction.addTroopType(gw.getTroopTypeByName("Tiger Mech"));
        
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Mercenary Infantry"));
        tempFaction.addStartingTroop(gw.getTroopTypeByName("Mercenary Infantry"));
        
        
        
        
        
        // Federation of liberty Defence platforms
        // -----------------
        // Turtle Defender
        tempsst = new SpaceshipType("Turtle Defender","TDef",SpaceshipType.SIZE_HUGE,0,540,SpaceshipRange.NONE,5,36,uniqueShipIdCounter,15,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(25);
        tempsst.setWeaponsMaxSalvoesLarge(10);
        tempsst.setWeaponsStrengthHuge(30);
        tempsst.setWeaponsMaxSalvoesHuge(8);
        tempsst.setArmorSmall(70);
        tempsst.setArmorMedium(35);
        tempsst.setInitDefence(4);
        tempsst.setNoRetreat(true);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This huge planet defender ship was developed for protect important planets. It’s very cheap to have because the lack of hyper engine and also have a system that prevents ships to start theirs hyper space engines and flee from the battlefield.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Turtle Defender"));
        
        // Raid Station
        tempsst = new SpaceshipType("Raid Station","RSta",SpaceshipType.SIZE_MEDIUM,0,50,SpaceshipRange.NONE,0,20,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(30);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(13);
        tempsst.setWeaponsStrengthLarge(250);
        tempsst.setWeaponsMaxSalvoesLarge(4);
        tempsst.setIncreaseInitiative(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setPlayerUnique(true);
        tempsst.setDescription("This medium raid station offers good protections against large sized ships. The ship hasn’t any hyper engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Raid Station"));
        
        
        // Squadrons
        // ---------
        // Observer
        tempsst = new SpaceshipType("Observer","Obs",SpaceshipType.SIZE_SMALL,0,10,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,1,9);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setPlanetarySurvey(true);
        tempsst.setDescription("This squadron are used to scout out unknown planets.");
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Observer"));
        
        // Piranha Fighter
        tempsst = new SpaceshipType("Piranha Fighter","P-F",SpaceshipType.SIZE_SMALL,0,30,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,3,13);
        tempsst.setSquadron(true);
        tempsst.setArmorSmall(20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setDescription("A good and cheap dog fighter.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Piranha Fighter"));
        
        // Bass Fighter
        tempsst = new SpaceshipType("Bass Fighter","Bas",SpaceshipType.SIZE_SMALL,0,40,SpaceshipRange.NONE,1,4,uniqueShipIdCounter,6,20);
        tempsst.setSquadron(true);
        tempsst.setArmorSmall(20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setDescription("Next generation of fighters.");
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bass Fighter"));
        
        // Liberty Bomber
        tempsst = new SpaceshipType("Shark Bomber","S-B",SpaceshipType.SIZE_SMALL,0,22,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,10,2);
        tempsst.setSquadron(true);
        tempsst.setArmorSmall(10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(8);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(4);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A good and cheap anti capital fighter.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Shark Bomber"));
        
        // Whale Bomber
        tempsst = new SpaceshipType("Whale Bomber","WB",SpaceshipType.SIZE_SMALL,0,30,SpaceshipRange.NONE,1,4,uniqueShipIdCounter,7,2);
        tempsst.setSquadron(true);
        tempsst.setArmorSmall(10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(8);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(4);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setWeaponsStrengthHuge(30);
        tempsst.setWeaponsMaxSalvoesHuge(3);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(10);
        tempsst.setBluePrintFirstTurn(15);
        tempsst.setDescription("Next generation of anti capital fighter. Armd with weaponse against huge ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Whale Bomber"));
        
        // Tiger Attacker
        tempsst = new SpaceshipType("Tiger Attacker","T-A",SpaceshipType.SIZE_SMALL,0,45,SpaceshipRange.SHORT,2,6,uniqueShipIdCounter,10,12);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setArmorSmall(10);
        tempsst.setWeaponsStrengthMedium(14);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setInitSupport(true);
        tempsst.setIncreaseInitiative(1);
        tempsst.setDescription("A good all-round support fighter with hyper engine for short range jumps. This fighter is often used to patrol and defending our planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Tiger Attacker"));
        
        // Puma Attacker
        tempsst = new SpaceshipType("Puma Attacker","P-A",SpaceshipType.SIZE_SMALL,0,55,SpaceshipRange.SHORT,3,7,uniqueShipIdCounter,15,18);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setArmorSmall(10);
        tempsst.setWeaponsStrengthMedium(22);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setInitSupport(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setIncreaseInitiative(2);
        tempsst.setDescription("Next generation of heavy attacker. A good all-round support fighter with hyper engine for short range jumps. This fighter is often used to patrol and defending our planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Puma Attacker"));
        
        
        // Capital ships
        // -------------
        // Liberty Corvette
        tempsst = new SpaceshipType("Liberty Corvette","LCrv",SpaceshipType.SIZE_SMALL,0,35,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,10,6);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setCanBlockPlanet(false);
        tempsst.setArmorSmall(20);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Light small long range capital ship used for exploring and person transports. To small for blockading enemies planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Liberty Corvette"));
        
        // Fighter Corvette
        tempsst = new SpaceshipType("Fighter Corvette","FCrv",SpaceshipType.SIZE_SMALL,0,40,SpaceshipRange.LONG,3,6,uniqueShipIdCounter,5,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setArmorSmall(50);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFirstTurn(10);
        tempsst.setDescription("This is a modified Liberty corvette with more fire power against squadrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Corvette"));
        
        // Old troop Transporter
        tempsst = new SpaceshipType("Old Troop Transporter","OTra",SpaceshipType.SIZE_MEDIUM,0,15,SpaceshipRange.LONG,2,8,uniqueShipIdCounter,2,2);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setTroopCapacity(3);
        tempsst.setScreened(true);
        tempsst.setDescription("This is the main troop transport ship with very poor fire power but good manoeuvre capacity that's make this ship to long range class ship");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Old Troop Transporter"));
        
        // Shadow
        tempsst = new SpaceshipType("Shadow","Shdw",SpaceshipType.SIZE_MEDIUM,0,25,SpaceshipRange.LONG,3,10,uniqueShipIdCounter,8,6);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setAvailableToBuild(false);
        tempsst.setTroopCapacity(3);
        tempsst.setVisibleOnMap(false);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(8);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This is the new main troop transport ship with cloaking capacity. I bit more expensive than the old one but the cloaking capacity will help us to transport troop in a safe way.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Shadow"));
        
        // Raider
        tempsst = new SpaceshipType("Raider","Rai",SpaceshipType.SIZE_MEDIUM,0,70,SpaceshipRange.LONG,5,9,uniqueShipIdCounter,17,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(60);
        tempsst.setArmorMedium(20);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setIncEnemyClosedBonus(3);
        tempsst.setIncEnemyOpenBonus(3);
        tempsst.setIncNeutralClosedBonus(2);
        tempsst.setIncNeutralOpenBonus(2);
        tempsst.setCanBlockPlanet(false);
        tempsst.setVisibleOnMap(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This medium sized ship is one of the most advanced ships with cloaking skills that makes it invisible at long distance. One of the best ways to use the cloaking is to blocked enemies planets and raid civilian trading ships. That’s make this ship cheap to have at enemies planets. But it only works with one at the same planet.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Raider"));
        
        // troop Destroyer
        tempsst = new SpaceshipType("Troop Destroyer","TDes",SpaceshipType.SIZE_MEDIUM,0,120,SpaceshipRange.SHORT,6,16,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(70);
        tempsst.setArmorMedium(40);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setWeaponsStrengthLarge(80);
        tempsst.setWeaponsMaxSalvoesLarge(1);
        tempsst.setBombardment(1);
        tempsst.setTroopCapacity(3);
        tempsst.setBluePrintFirstTurn(15);
        tempsst.setDescription("Probably the strongest medium ship in the galaxy. Armed with a DZ800 laser for bombardment and construct so it even can fire against large capital ships. The DZ800 laser take so much place that this ship missing long range engines.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Destroyer"));
        
        // Sweeper
        tempsst = new SpaceshipType("Sweeper","Swe",SpaceshipType.SIZE_MEDIUM,0,130,SpaceshipRange.SHORT,6,20,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(70);
        tempsst.setArmorMedium(40);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setWeaponsStrengthLarge(80);
        tempsst.setWeaponsMaxSalvoesLarge(1);
        tempsst.setBombardment(2);
        tempsst.setTroopCapacity(4);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(12);
        tempsst.setBluePrintFirstTurn(35);
        tempsst.setDescription("Next generation of troop destoryers. Armd with better bombardment and bigger troop capacity.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Sweeper"));
        
        // Wiper
        tempsst = new SpaceshipType("Wiper","Wip",SpaceshipType.SIZE_MEDIUM,0,120,SpaceshipRange.LONG,6,16,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(70);
        tempsst.setArmorMedium(40);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setBombardment(1);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(15);
        tempsst.setBluePrintFirstTurn(30);
        tempsst.setDescription("A long range capital ship with bombardment capacity. Developed to destroy planets with just long range ways.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Wiper"));
        
        // Carrier Destroyer
        tempsst = new SpaceshipType("Carrier Destroyer","CDes",SpaceshipType.SIZE_MEDIUM,0,120,SpaceshipRange.LONG,6,16,uniqueShipIdCounter,15,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(70);
        tempsst.setArmorMedium(40);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setSquadronCapacity(1);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(15);
        tempsst.setBluePrintFirstTurn(30);
        tempsst.setDescription("A long range capital ship with capacity carrier a squadron. Developed to give more fire power in the longe range fleet.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier Destroyer"));
        
        // Suport Carrier
        tempsst = new SpaceshipType("Suport Carrier","SCar",SpaceshipType.SIZE_MEDIUM,0,80,SpaceshipRange.LONG,3,11,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(55);
        tempsst.setArmorMedium(40);
        tempsst.setSquadronCapacity(6);
        tempsst.setScreened(true);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("A medium carrier with enough fire power to hold small ships away. Can take up to 6 squadrons and that's make this ship to a great threat in the battlefield.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Suport Carrier"));
        
        // Supply Ship
        tempsst = new SpaceshipType("Supply Ship","SShi",SpaceshipType.SIZE_MEDIUM,0,40,SpaceshipRange.SHORT,1,10,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(55);
        tempsst.setArmorMedium(20);
        tempsst.setSupply(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("Rebuild from captured ships to supply the fleet. The medium hull and badly flight capacity make it to a short ranger and easy target. Used to reload ships missiles and torpedoes.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Ship"));
                
        //Warship
        tempsst = new SpaceshipType("Warship","War",SpaceshipType.SIZE_LARGE,0,475,SpaceshipRange.SHORT,11,40,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(40);
        tempsst.setWeaponsMaxSalvoesLarge(18);
        tempsst.setArmorSmall(80);
        tempsst.setArmorMedium(45);
        tempsst.setArmorLarge(10);
        tempsst.setSquadronCapacity(8);
        tempsst.setBlackmarketFirstTurn(30);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(60);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("The fleets best and larges anti capital ship. Good against medium and large class ship. Often used in great numbers in the really big battles. Takes up to 8 squadrons. Is to big to go long range.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Warship"));
        
        //Warship II
        tempsst = new SpaceshipType("Warship II","War2",SpaceshipType.SIZE_LARGE,0,500,SpaceshipRange.SHORT,11,44,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(50);
        tempsst.setWeaponsMaxSalvoesLarge(18);
        tempsst.setWeaponsStrengthHuge(15);
        tempsst.setWeaponsMaxSalvoesHuge(4);
        tempsst.setArmorSmall(80);
        tempsst.setArmorMedium(45);
        tempsst.setArmorLarge(10);
        tempsst.setSquadronCapacity(8);
        tempsst.setBlackmarketFirstTurn(40);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setBluePrintFirstTurn(70);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.UNCOMMON);
        tempsst.setAvailableToBuild(false);
        tempsst.setDescription("Next generation o war ships. Armd with more fire power against large and huge capital ships. The fleets best and larges anti capital ship. Good against medium and large class ship. Often used in great numbers in the really big battles. Takes up to 8 squadrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Warship II"));
        
        //Galaxy Ship
        tempsst = new SpaceshipType("Galaxy Ship","GShi",SpaceshipType.SIZE_HUGE,0,800,SpaceshipRange.SHORT,27,87,uniqueShipIdCounter,20,50);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setWeaponsStrengthMedium(35);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(35);
        tempsst.setWeaponsMaxSalvoesLarge(20);
        tempsst.setWeaponsStrengthHuge(30);
        tempsst.setWeaponsMaxSalvoesHuge(15);
        tempsst.setArmorSmall(90);
        tempsst.setArmorMedium(70);
        tempsst.setArmorLarge(35);
        tempsst.setArmorHuge(20);
        tempsst.setIncreaseInitiative(5);
        tempsst.setSquadronCapacity(8);
        tempsst.setTroopCapacity(3);
        tempsst.setInitDefence(10);
        tempsst.setAvailableToBuild(false);
        tempsst.setBlackmarketFirstTurn(80);
        tempsst.setBlackMarketFrequency(BlackMarketFrequency.RARE);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("This huge anti squadrons’ ship is armed with 58 anti squadrons cannons. Often armed with up to 8 Liberty Heavy Attackers or Liberty Bombers to hold big capitals ship on safe distance. The Hull is from the biggest ore miner ship and armed to the maximum.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galaxy Ship"));
        
        //Dominator
        tempsst = new SpaceshipType("Dominator","Dom",SpaceshipType.SIZE_HUGE,0,750,SpaceshipRange.SHORT,30,95,uniqueShipIdCounter,20,50);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setWeaponsStrengthMedium(35);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(35);
        tempsst.setWeaponsMaxSalvoesLarge(20);
        tempsst.setWeaponsStrengthHuge(30);
        tempsst.setWeaponsMaxSalvoesHuge(15);
        tempsst.setArmorSmall(90);
        tempsst.setArmorMedium(70);
        tempsst.setArmorLarge(35);
        tempsst.setArmorHuge(20);
        tempsst.setIncreaseInitiative(4);
        tempsst.setInitDefence(5);
        tempsst.setVisibleOnMap(false);
        tempsst.setPlayerUnique(true);
        tempsst.setAvailableToBuild(false);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setBluePrintFrequency(BlackMarketFrequency.NEVER);
        tempsst.setDescription("The ruling class of ship. Is a unique ship and construct to surprise small fleets or planets with poor defend. This ship is based on the 'Galaxy ship'. The carrier and troop transport module is removed and replaced by the biggest cloaking device in the world. This huge anti squadrons’ ship is armed with 58 anti squadrons cannons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Dominator"));
        
        // ****** fraction unique property ******
        
        tempFaction.setGovernorVIPType(President);
        
        tempFaction.addStartingVIPType(gw.getVIPTypeByName("Spy"));
        
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Liberty Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Suport Carrier"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Raider"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Piranha Fighter"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Piranha Fighter"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Tiger Attacker"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Old Troop Transporter"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Raid Station"));
        
        // *******************************************************************************
        // ****************************** --- Forskning --- ******************************
        // *******************************************************************************
        
        tempResearch = new Research();
        
        // Capital
        tempResearchAdvantage = new ResearchAdvantage("Capital","Gives Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Capital"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Province Capital
        tempResearchAdvantage = new ResearchAdvantage("Province Capital","Gives Province Capital");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Province Capital"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Capital"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Mining
        tempResearchAdvantage = new ResearchAdvantage("Mining", "More advanced mining site");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Liberty Mining Base");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        aResearchUpgradeBuilding.setClosedPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Trading
        tempResearchAdvantage = new ResearchAdvantage("Trading", "More advanced trading");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("City");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Mining"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Infrastructure
        tempResearchAdvantage = new ResearchAdvantage("Infrastructure", "Better tax control.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("City");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Province Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        aResearchUpgradeBuilding = new ResearchUpgradeBuilding();
        aResearchUpgradeBuilding.setName("Capital");
        aResearchUpgradeBuilding.setOpenPlanetBonus(1);
        tempResearchAdvantage.addResearchUpgradeBuilding(aResearchUpgradeBuilding);
        
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Trading"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        
        // Dominator
        tempResearchAdvantage = new ResearchAdvantage("Dominator", "Gives possibility to build the Dominator.");
        tempResearchAdvantage.setTimeToResearch(5);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Dominator"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Galaxy ship
        tempResearchAdvantage = new ResearchAdvantage("Galaxy Ship", "Gives possibility to build Galaxy ship class.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Galaxy Ship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Dominator"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Warship II
        tempResearchAdvantage = new ResearchAdvantage("Warship II", "Gives possibility to build Warship II ship class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Warship II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Warship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Warship
        tempResearchAdvantage = new ResearchAdvantage("Warship", "Gives possibility to build Warship ship class.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Warship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Warship II"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Sweeper
        tempResearchAdvantage = new ResearchAdvantage("Sweeper", "Gives possibility to build Sweeper ship class.");
        tempResearchAdvantage.setTimeToResearch(0);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Sweeper"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Troop Destroyer"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Huge Wharfs 
        tempResearchAdvantage = new ResearchAdvantage("Huge ships", "Gives possibility to build Huge liberty wharfs.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Huge Liberty Wharf"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Galaxy Ship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Large Ships 
        tempResearchAdvantage = new ResearchAdvantage("Large ships", "Gives possibility to build Large liberty wharfs.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Large Liberty Wharf"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Huge ships"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Warship"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Short range fleet
        tempResearchAdvantage = new ResearchAdvantage("Short range fleet","Establish a platform for researching for short range fleet.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Large ships"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Sweeper"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        // Wiper
        tempResearchAdvantage = new ResearchAdvantage("Wiper", "Gives possibility to build Wiper ship class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Wiper"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Fighter Corvette
        tempResearchAdvantage = new ResearchAdvantage("Fighter Corvette", "Gives possibility to build Fighter Corvette ship class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Fighter Corvette"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Carrier Destroyer
        tempResearchAdvantage = new ResearchAdvantage("Carrier Destroyer", "Gives possibility to build Carrier Destroyer ship class.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Carrier Destroyer"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Shadow
        tempResearchAdvantage = new ResearchAdvantage("Shadow", "Gives possibility to build Shadow ship class.");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Shadow"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Old Troop Transporter"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Space station 
        tempResearchAdvantage = new ResearchAdvantage("Space station", "Gives possibility to build Space stations.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Space station"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Long range fleet
        tempResearchAdvantage = new ResearchAdvantage("Longe range fleet","Establish a platform for researching for long range fleet.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Space station"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Carrier Destroyer"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Fighter Corvette"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Wiper"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Shadow"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        
        // Puma Attacker
        tempResearchAdvantage = new ResearchAdvantage("Puma Attacker", "Gives possibility to build Puma Attacker class.");
        tempResearchAdvantage.setTimeToResearch(0);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Puma Attacker"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Whale Bomber
        tempResearchAdvantage = new ResearchAdvantage("Whale Bomber", "Gives possibility to build Whale Bomber class.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Whale Bomber"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Puma Attacker"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Bass Fighter
        tempResearchAdvantage = new ResearchAdvantage("Bass Fighter", "Gives possibility to build Bass Fighter class.");
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Bass Fighter"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("Piranha Fighter"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Puma Attacker"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Observer
        tempResearchAdvantage = new ResearchAdvantage("Observer", "Gives possibility to build Observer class.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(5);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Observer"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Squadrons
        tempResearchAdvantage = new ResearchAdvantage("Squadrons","Establish a platform for researching on squadrons.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Observer"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Bass Fighter"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Whale Bomber"));
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("Tiger Attacker"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        // Hummel Mech
        tempResearchAdvantage = new ResearchAdvantage("Hummel Mech","Gives Hummel Mech");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Hummel Mech"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Tiger Mech
        tempResearchAdvantage = new ResearchAdvantage("Tiger Mech","Gives Tiger Mech");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Tiger Mech"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Panther Mech
        tempResearchAdvantage = new ResearchAdvantage("Panther Mech","Gives Panther Mech");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Panther Mech"));
        tempResearchAdvantage.setTimeToResearch(0);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Hummel Mech"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Tiger Mech"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Mercenary HAI
        tempResearchAdvantage = new ResearchAdvantage("Mercenary HAI","Gives Mercenary HAI");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Mercenary HAI"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Mercenary HI
        tempResearchAdvantage = new ResearchAdvantage("Mercenary HI","Gives Mercenary HI");
        tempResearchAdvantage.addTroopType(gw.getTroopTypeByName("Mercenary HI"));
        tempResearchAdvantage.addReplaceTroopTypes(gw.getTroopTypeByName("Mercenary Infantry"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        // Troop transport
        tempResearchAdvantage = new ResearchAdvantage("Troop transport", "Gives possibility to develop better troop transporters.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Shadow"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Sweeper"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Army
        tempResearchAdvantage = new ResearchAdvantage("Army","Establish a platform for researching on the army.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Troop transport"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Mercenary HI"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Mercenary HAI"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Panther Mech"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        //Heavy Cannon 
        tempResearchAdvantage = new ResearchAdvantage("Heavy Cannon", "Gives possibility to build Heavy Cannon.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Heavy Cannon"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Puls Cannon 
        tempResearchAdvantage = new ResearchAdvantage("Puls Cannon", "Gives possibility to build Puls Cannon.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Puls Cannon"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Trading Base 
        tempResearchAdvantage = new ResearchAdvantage("Trading Base", "Gives possibility to build Trading Base.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Trading Base"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Mining Base 
        tempResearchAdvantage = new ResearchAdvantage("Mining Base", "Gives possibility to build Liberty Mining Base.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Liberty Mining Base"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Trading Base"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //University 
        tempResearchAdvantage = new ResearchAdvantage("University", "Gives possibility to build University.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("University"));
        tempResearchAdvantage.setTimeToResearch(3);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Mercenary Stronghold 
        tempResearchAdvantage = new ResearchAdvantage("Mercenary Stronghold", "Gives possibility to build Mercenary Strongholds.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Mercenary Stronghold"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Armor Construction Yard 
        tempResearchAdvantage = new ResearchAdvantage("Armor Construction Yard", "Gives possibility to build Armor Construction Yards.");
        tempResearchAdvantage.addBuildingType(tempBuildings.getBuildingType("Armor Construction Yard"));
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Panther Mech"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Ground Constructions
        tempResearchAdvantage = new ResearchAdvantage("Ground Constructions","Establish a platform for researching on the ground constructions.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Mercenary Stronghold"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Armor Construction Yard"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("University"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Mining Base"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Puls Cannon"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Heavy Cannon"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        //Fixed Cannons 
        tempResearchAdvantage = new ResearchAdvantage("Fixed Cannons", "Gives heavy cannon placed on plantes to prevent blokades.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Heavy Cannon"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Ship Cannons 
        tempResearchAdvantage = new ResearchAdvantage("Ship Cannons", "Gives better ship cannons.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Galaxy Ship"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Warship II"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Army Cannons 
        tempResearchAdvantage = new ResearchAdvantage("Army Cannons", "Gives better cannons to the army.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Tiger Mech"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Hummel Mech"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Bombardment 
        tempResearchAdvantage = new ResearchAdvantage("Bombardment", "Gives better bombardment.");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(10);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Sweeper"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Wiper"));
        tempResearch.addAdvantage(tempResearchAdvantage);
        
        //Heavy Weaponse
        tempResearchAdvantage = new ResearchAdvantage("Heavy Weaponse","Establish a platform for researching on heavy weaponse.");
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Bombardment"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Army Cannons"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Ship Cannons"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Fixed Cannons"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        // ************************* --- Forskning Economic --- *************************
        
        researchCorruption1 = new ResearchAdvantage("Corruption level 6","Lower corruption");
		researchCorruption1.setCorruption(tmpCorruption6);
		researchCorruption1.setTimeToResearch(5);
		researchCorruption1.setCostToResearchOneTurnInProcent(10);
        
        researchCorruption2 = new ResearchAdvantage("Corruption level 5","Lower corruption");
        researchCorruption2.setCorruption(tmpCorruption5);
        researchCorruption2.setTimeToResearch(5);
        researchCorruption2.setCostToResearchOneTurnInProcent(10);
        researchCorruption2.addChild(researchCorruption1);
        
        researchCorruption3 = new ResearchAdvantage("Corruption level 4","Lower corruption");
        researchCorruption3.setCorruption(tmpCorruption4);
        researchCorruption3.setTimeToResearch(3);
        researchCorruption3.setCostToResearchOneTurnInProcent(20);
        researchCorruption3.addChild(researchCorruption2);
        
        researchCorruption4 = new ResearchAdvantage("Corruption level 3","Lower corruption");
        researchCorruption4.setCorruption(tmpCorruption3);
        researchCorruption4.setTimeToResearch(3);
        researchCorruption4.setCostToResearchOneTurnInProcent(20);
        researchCorruption4.addChild(researchCorruption3);
        
        researchCorruption5 = new ResearchAdvantage("Corruption level 2","Lower corruption");
        researchCorruption5.setCorruption(tmpCorruption2);
        researchCorruption5.setTimeToResearch(3);
        researchCorruption5.setCostToResearchOneTurnInProcent(20);
        researchCorruption5.addChild(researchCorruption4);
        
        researchCorruption6 = new ResearchAdvantage("Corruption level 1","Lower corruption");
        researchCorruption6.setCorruption(tmpCorruption1);
        researchCorruption6.setTimeToResearch(3);
        researchCorruption6.setCostToResearchOneTurnInProcent(20);
        researchCorruption6.addChild(researchCorruption5);
        
        tempResearch.addAdvantage(researchCorruption1);
        tempResearch.addAdvantage(researchCorruption2);
        tempResearch.addAdvantage(researchCorruption3);
        tempResearch.addAdvantage(researchCorruption4);
        tempResearch.addAdvantage(researchCorruption5);
        tempResearch.addAdvantage(researchCorruption6);
        
        tempResearchAdvantage = new ResearchAdvantage("Economic","Develop Economic");
        tempResearchAdvantage.addChild(researchCorruption6);
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Infrastructure"));
        tempResearchAdvantage.addChild(tempResearch.getAdvantage("Province Capital"));
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        
        tempResearch.setNumberOfSimultaneouslyResearchAdvantages(2);
        
        tempFaction.setResearch(tempResearch);
        
        // *******************************************************************************
        // *******************************************************************************
        // *******************************************************************************
        
        tempFaction.setClosedPlanetBonus(1);
        tempFaction.setAdvantages("Closed planet bonus = 1, good armor and cloaking ships");
        tempFaction.setDisadvantages("No shields");
        tempFaction.setHistory("In the century of 2230 the space was a chaotic place with a lot pirates attacks. The most attacks were against outer space colonies that didn’t get any protections from the old countries and only helped from Trade Federation if the colony was a good trading partner. The smaller colonies hade to protect them self and some time they was attacked by the big federations accuse to be pirates stronghold.\nSome colonies started to build up army fleets together and some started to pay money to pirate to stop the attacks against them.\nAt AC 2240 134 colonies started the Federation of liberty. The older federation didn’t like that at all. They saw our glory federation as a simple pack of pirates. So they told us to dissolve our federation or they were going to bombard all of us. The big problem was that the pirates started attacked to divide us. They understand that the Federation of liberty were going to pirate business much harder.\nThen the big war started in 2242 we were under heavy attacks from pirates, other federation and countries. Son after that the attacks from pirates just stopped and instead they started to attack our attacker from other federations and countries. The pirates contact us and offer us military protection towards food and fuel. We answer them that they could join our glory federation or die. That was a success and from that day Federation of liberty is a powerful nation to respect.");
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setCorruption(tmpCorruption);
      //  tempFaction.setAlignment(federation);
        gw.addFaction(tempFaction);
        
       return gw;
	}

}
