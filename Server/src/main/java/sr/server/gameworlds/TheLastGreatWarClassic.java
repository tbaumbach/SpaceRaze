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
import sr.world.SpaceshipType;
import sr.world.UniqueIdCounter;
import sr.world.VIPType;

public class TheLastGreatWarClassic {
	
	public static GameWorld getGameWorld(){
		// XXX The Last Great War Classic
		GameWorld gw = new GameWorld();
		

        gw.setFileName("thelastgreatwarclassic");

        gw.setFullName("The Last Great War Classic version");
        gw.setShortDescription("The Last Great War between 7 faction from the razed earth");
        gw.setDescription("Play in the classic SpaceRaze style. No troops, research and planet buildings. Good to play for beginners and for fast playing game style");
        gw.setHistory("The year 2006 USA starts developing new rockets and space ship to explore and build up a base on the moon. One of the main causes were Chinas program to get to the moon and out to the space. At the same time some company’s started to develop space shuttles to do short trip out to the space. The idea was expansive enjoyments trips for rich people. Some years after that EU decide to develop new space ships that could fly between earth and space without any help from rockets or air planes. That was not an easy thing to do and it took long time. The first ship was flaying at 2041. At the same time Russian hade build the world first space ship and started to explore the sun system. The problem Russian hade was the trip from earth to the waiting space ship. The ship was a big susses so China started develop ships for space travels and for colonize the planets in the sun system.\nAt 2072 USA, China, Russian and EU hade smaller fleets. At the same time the first civilian ships started to transport rich people between hotels on planets or space station. At 2084 same countries from Africa founded the Alliance of Africa. The main course was to build up a space fleet to defending the interest and get for resources from the space.\n\nAt 2098 the first space ship in the history was founded drifting around and short after that China and USA broke against the non weapons agreement. After that even same civilian ships was equipped with weapons. Some really big company bought in some ship to make o police force (a first step against Trade Federation).\nAt AC 2104 EU develop the first hyper driver to do long jumps in the space. It took 8 years to the first hyper jump with a space ship that returned undamaged. On of the imported things in jumping is to have a clean and known way to travel.\n2116 was the year then the first jump to a planet in another sun system was made. Soon after 2121 all ships that wanted could have a hyper drive. At this time the space was unknown and hyper jump was dangerous outside our sun system. So they that were doing these trips were poor people that wanted food on the table and start a new live. No one knows how many of this ships that was lost in the deep space, but under almost 50 years this trips was ordinary.\n\nAt around the year 2145 many planets start take contact with the old world (as they called it). Travelling between planets and sun system was made common. Around 2171 a lot of trading ships was lost in the space and even same planets were attacked. No one knows who the attacker was.\nAt 2178 some big companies started the Trade Federation to build up an armed fleet to strike back and to escort trading fleets. The old countries from the earth didn’t liked the idea of heavy armed civilian battle ships, so they started to build up military fleets to control the space and the colony und the own flags.\nAround 2230 it was a lot of pirates attack and huge misunderstandings between countries, colonies, companies and free planets. At 2240 some colonies in the outer space started a new federation name to Federation of liberty to defend they self against military fleet that occupied planets as they accused for being raiding strong holds.\n\nThe 22 Mars in 2242 the first attacked against the earth was made. No one know who or there from the first nuclear bomb was sent but all countries defending system answered with sending nuclear bombs back to the attacker. In just 5 hours 97.5% of all inherits in earth were dead and the rest hurt for the rest of the lives. It took lees then one week to destroy the rest of the sun system.\nAfter 2242 the rest of the world and old country’s started to build up the military. The most of the planets is still under self rule but the 7 factions are still fighting and need the resources from the neutral planets. Some neutral planets have build up defence to protect the self from aggressions. Who started the war that will end it? The answers will be in the future");
        
        gw.setBattleSimDefaultShips1("");
        gw.setBattleSimDefaultShips2("");

        gw.setCreatedDate("2006-02-07");
		gw.setChangedDate("2007-08-21");
		gw.setCreatedByUser("Thobias Baumbach");

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
		Alignment ggood = alignments.findAlignment(gStr);
		
		usa.addCanHaveVip(neutral.getName());
		usa.addCanHaveVip(smuggler.getName());
		usa.addCanHaveVip(ggood.getName());
		
		china.addCanHaveVip(neutral.getName());
		china.addCanHaveVip(smuggler.getName());
		china.addCanHaveVip(ggood.getName());
		
		eu.addCanHaveVip(neutral.getName());
		eu.addCanHaveVip(smuggler.getName());
		eu.addCanHaveVip(ggood.getName());
		
		alliance.addCanHaveVip(neutral.getName());
		alliance.addCanHaveVip(smuggler.getName());
		alliance.addCanHaveVip(ggood.getName());
		
		russian.addCanHaveVip(neutral.getName());
		russian.addCanHaveVip(smuggler.getName());
		russian.addCanHaveVip(ggood.getName());
		
		trade.addCanHaveVip(neutral.getName());
		trade.addCanHaveVip(ggood.getName());
		
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

	VIPType tmpVipType = new VIPType("Spy","Spy",neutral,uniqueVIPIdCounter);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setSpying(true);
        tmpVipType.setCounterEspionage(50);
        tmpVipType.setDescription("This VIP can visit enemy’s planets and get the same information as on open planets. On the own planets this VIP can seeking up enemies assassin and try to kill them.");
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Merchant","Mer",ggood,uniqueVIPIdCounter);
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
        tmpVipType.setDescription("This designer is so skill full that all yours ships build on the same planets is 20% better in both shields and weapons. Beware of that a designer on open planets will be easy to spot for enemy’s assassins.");
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Economic Genius","Eco",neutral,uniqueVIPIdCounter);
        tmpVipType.setShipBuildBonus(20);
        tmpVipType.setShowOnOpenPlanet(true);
        tmpVipType.setDescription("Economic genius wills lower the cost to build ships on the same planets. Beware of that an economic genius on open planets will be easy to spot for enemy’s assassins.");
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("General","Gen",neutral,uniqueVIPIdCounter);
        //tmpVipType.setSiegeBonus(1);
        tmpVipType.setPsychWarfareBonus(1);
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setDescription("A General is skill full in ground combats and helping out yours troops in both attacking planets and defending them. To give bonus in attacks a general needs troops to lead in the same fleet.");
        gw.addVipType(tmpVipType);
        
        tmpVipType = new VIPType("Sky Marshal","Sky",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitBonus(10);
        tmpVipType.setFrequency(BlackMarketFrequency.RARE);
        tmpVipType.setDescription("The highest fleet commander that gives yours fleet an advantage in tactics that’s and gives the fleet more opportunity to give fire.");
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Admiral","Adm",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitBonus(6);
        tmpVipType.setDescription("The admiral gives your fleet an advantage in tactics, that’s gives the fleet more opportunity to give fire. A admiral is a leader and must be on a capital ship to lead and give bonus.");
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Top Ace","Ace",neutral,uniqueVIPIdCounter);
        tmpVipType.setInitFighterSquadronBonus(8);
        tmpVipType.setDescription("Thanks to the skill the top ace have, the fleet gives more opportunity to fire the weapons. A top ace is a squadron’s pilot and must be on a squadron to give bonus.");
        gw.addVipType(tmpVipType);

        tmpVipType = new VIPType("Assassin","Ass",neutral,uniqueVIPIdCounter);
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
        tmpVipType.setResistanceBonus(3);
        tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
        tmpVipType.setDescription("A diplomat can persuade a neutral planet to join yours side. It will take one turn to persuade one of the planets resistant.");
        gw.addVipType(tmpVipType);
        
//  ######## Neural ########
        
        // Neutral Defence platforms
        // -----------------

	// Medium Defender (Neutral)
        SpaceshipType tempsst = new SpaceshipType("Medium Defender","MDef",SpaceshipType.SIZE_MEDIUM,40,140,SpaceshipRange.NONE,2,10,uniqueShipIdCounter,17,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(13);
        tempsst.setWeaponsMaxSalvoesMedium(18);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setArmorSmall(35);
        tempsst.setArmorMedium(10);
        tempsst.setDescription("This medium planet defender ship is made for protection and support against hostile’s fleets. Very cheap in maintains because the lack of hyper engine. This ship is often used by neutral mediums sized planets and in number of 1 to 4. Good against the most of ship but not a match for huge sized ship.");
        gw.addShipType(tempsst);
        
        // Large Defender (Neutral)
        tempsst = new SpaceshipType("Large Defender","LDef",SpaceshipType.SIZE_LARGE,75,370,SpaceshipRange.NONE,3,36,uniqueShipIdCounter,10,10);
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
        tempsst = new SpaceshipType("Freedom Fighter","FreF",SpaceshipType.SIZE_SMALL,5,40,SpaceshipRange.NONE,1,8,uniqueShipIdCounter,12,12);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(1);
        tempsst.setDescription("A good all-round support fighter. This fighter is often used by small neutral planets in number of 1-4. Some time these ships have been convinced to join a faction fleet if the home planet is under rule of the same faction.");
        gw.addShipType(tempsst);
        
        // add shiptypes for neutral planets
        gw.setNeutralSize1(gw.getSpaceshipTypeByName("Freedom Fighter"));
        gw.setNeutralSize2(gw.getSpaceshipTypeByName("Medium Defender"));
        gw.setNeutralSize3(gw.getSpaceshipTypeByName("Large Defender"));
        
       
		Corruption tmpCorruption = new Corruption();
		tmpCorruption.addBreakpoint(70, 20);
		tmpCorruption.addBreakpoint(140, 40);
		tmpCorruption.addBreakpoint(210, 60);
		
        
//  ######## China (Red) ########
		// XXX China        
        Faction tempFaction = new Faction("China",Faction.getColorHexString(240,35,45),china);
        
        
        // Adding Buildings to the faction
        Buildings tempBuildings = new Buildings();
        BuildingType tempBuildingType;
        
        tempBuildingType= new BuildingType("Small Wharf", "W1", 2, uniqueBuildingIdCounter);
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
        
        tempBuildingType= new BuildingType("Large Wharf", "W3", 18, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(3);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge Wharf", "W5", 40, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Space station", "SS", 8, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("");
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
        
        // China Defence platforms
        // -----------------
        // China Defender
        tempsst = new SpaceshipType("China Defender","CDef",SpaceshipType.SIZE_MEDIUM,10,60,SpaceshipRange.NONE,1,8,uniqueShipIdCounter,8,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setArmorSmall(80);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This small planet defender ship is made for protection and support against fighters. Very cheap in maintains because the lack of hyper engine. Much better against fighters then the badly designed fighters in our navy.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("China Defender"));
        
        // China Home Base
        tempsst = new SpaceshipType("China Home Base","CHom",SpaceshipType.SIZE_LARGE,20,350,SpaceshipRange.NONE,0,200,uniqueShipIdCounter,10,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setArmorSmall(45);
        tempsst.setArmorMedium(20);
        tempsst.setIncOwnClosedBonus(7);
        tempsst.setIncOwnOpenBonus(9);
        tempsst.setIncreaseInitiative(6);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This large planet home base is made as a head office and offers good protections against Squadrons. The ship hasn’t any hyper engine. Gives an income of 7 on closed planet and 9 on open.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("China Home Base"));
        
        // Squadrons
        // ---------
        // China Fighter
        tempsst = new SpaceshipType("China Fighter","CFig",SpaceshipType.SIZE_SMALL,0,18,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,5,10);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("The only fighter in the navy and unfortunately not good enough. This ship was developed to protect the capital ships against bombers squadrons in big battles there Fighter Destroyer ships are easy targets for enemies’ capital ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("China Fighter"));
        /*
        // China Fighter II ****************************************************
        tempsst = new SpaceshipType("China Fighter II","CFig2",SpaceshipType.SIZE_SMALL,5,25,SpaceshipRange.NONE,1,4,uniqueShipIdCounter,6,15);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("The only fighter in the navy and unfortunately not good enough. This ship was developed to protect the capital ships against bombers squadrons in big battles there Fighter Destroyer ships are easy targets for enemies’ capital ships.");
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("China Fighter II"));
        */
        // China Bomber
        tempsst = new SpaceshipType("China Bomber","CBom",SpaceshipType.SIZE_SMALL,0,15,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,10,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(8);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A badly designed anti capital ship, only good in battle against enemies without fighters or anti fighter capacity.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("China Bomber"));
        /*
        // China Bomber II *******************************************************
        tempsst = new SpaceshipType("China Bomber II","CBom2",SpaceshipType.SIZE_SMALL,5,15,SpaceshipRange.NONE,1,4,uniqueShipIdCounter,12,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(8);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(12);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A badly designed anti capital ship, only good in battle against enemies without fighters or anti fighter capacity.");
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("China Bomber II"));
        
        // China Attacker *******************************************************
        tempsst = new SpaceshipType("China Attacker","CAtt",SpaceshipType.SIZE_SMALL,5,20,SpaceshipRange.SHORT,1,4,uniqueShipIdCounter,10,10);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(8);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("");
        tempsst.setAvailableToBuild(false);
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("China Attacker"));
        */
        
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
        
        // Troop Transporter
        tempsst = new SpaceshipType("Troop Transporter","Tra",SpaceshipType.SIZE_MEDIUM,5,30,SpaceshipRange.LONG,4,9,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(5);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setArmorSmall(15);
        tempsst.setPsychWarfare(1);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This is the main troop transport ship is armed with an E1 medium laser gun to hold hostility troop transports away. This ship was created to carry troops and not for combat action. The ship has an old engine that makes it expensive in both building and maintains.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Transporter"));
        
        // Fighter Destroyer
        tempsst = new SpaceshipType("Fighter Destroyer","FDes",SpaceshipType.SIZE_MEDIUM,20,70,SpaceshipRange.LONG,4,9,uniqueShipIdCounter,5,40);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setArmorSmall(70);
        tempsst.setArmorMedium(20);
        tempsst.setInitDefence(4);
        tempsst.setDescription("To protect our civil and army transport ships against pirate’s fighter this ship was build as an escort ship. The ship hade very good armour against fighters and the best anti fighter’s lasers in the fleet, but is vulnerable for fire from medium weapons. In the beginning of the war many ships was lost in combat against ships equipped with medium laser. The ship is so powerful that enemy hade to concentrate all the fire power against it in the beginning of a fight and then lost a lot of initiative doing that.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Destroyer"));
        
        // Destroyer
        tempsst = new SpaceshipType("Destroyer","Des",SpaceshipType.SIZE_MEDIUM,20,110,SpaceshipRange.LONG,5,12,uniqueShipIdCounter,15,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setArmorSmall(40);
        tempsst.setDescription("This medium class ship is the best long range battle ship in the fleet. Good against medium and small ship but don't heavy enough to be a threat against the big battle main ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer"));
        
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
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A heavy armed large capital ship that was made after that the first battle against enemy’s huge capital ships. Armed with torpedoes against huge class capital ship and also bombardment capacity. The weakness of this ship is the lack of protections against squadrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("WarDestroyer"));
        
        //WarBattleship
        tempsst = new SpaceshipType("WarBattleship","WarB",SpaceshipType.SIZE_LARGE,90,550,SpaceshipRange.SHORT,18,68,uniqueShipIdCounter,15,12);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(45);
        tempsst.setWeaponsMaxSalvoesLarge(18);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setBombardment(2);
        tempsst.setIncreaseInitiative(12);
        tempsst.setSquadronCapacity(6);
        tempsst.setPsychWarfare(2);
        //tempsst.setSiegeBonus(1);
        tempsst.setPlanetarySurvey(true);
        tempsst.setSupply(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("The biggest and best capital ship in the world then first one was made. It's hard to find any week point of this ship, excellent to conquer planets, survey capacity, big enough to supply the fleet, great against capital ship and good carrier capacity. It's just the newly huge capital ships that could be too much to meet in a combat.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("WarBattleship"));
        
//      Galaxy WarBattleship
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
        tempsst.setPsychWarfare(2);
        //tempsst.setSiegeBonus(1);
        tempsst.setNoRetreat(true);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This is the greatest ship in the world. It has capacity to destroy planets in one single shot and a big army to conquer them. Extremely powerful against large/huge capital ships and have carrier capacity for 10 squadrons. It has a system that prevents ships to start theirs hyper space engine and flee from the battlefield.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galaxy WarBattleship"));
        
        // ****** fraction unique property ******
        
        // Create orbital structures
     //   SpaceStation os = new SpaceStation();
      //  os.setSpaceport(true);
        //os.setOpenProdBonus(1);
      //  tempFaction.setOrbitalStructure(os);
        
        tmpVipType = new VIPType("Party Leader","Par",china,uniqueVIPIdCounter);
        tmpVipType.setGovernor(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setOpenIncBonus(1);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("The Party Leader is leader for China factions. He gives one extra income if he is on open planets. He can’t convince neutral planets to join the factions as the other leaders. It just too hard to convince free planets to lose the freedom.");
        gw.addVipType(tmpVipType);
        tempFaction.setGovernorVIPType(tmpVipType);
        
        tmpVipType = new VIPType("Military Navigator","MNav",china,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
      //  tmpVipType.setSiegeBonus(1);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setFTLbonus(true);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("A Military Navigator is a hero with skill for does long hyper jumps with large short ships. He is guarded by elite soldiers and therefore impossible to kill. He can also travel between all sorts of planets.");
        gw.addVipType(tmpVipType);
        tempFaction.addStartingVIPType(tmpVipType);
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("WarBattleship"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("China Home Base"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("China Bomber"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("China Bomber"));
        
        
        
        // *******************************************************************************
        // ************************* --- Test av forskning --- ***************************
        // *******************************************************************************
        /*
        Research tempResearch = new Research();
        ResearchAdvantage tempResearchAdvantage;
        
        tempResearchAdvantage = new ResearchAdvantage("China Attacker","Gives China Attacker");
        tempResearchAdvantage.setTimeToResearch(0);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("China Attacker"));
        //tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        tempResearch.addAdvantag(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("China Fighter II","Second generation fighter");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("China Fighter II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("China Fighter"));
        tempResearchAdvantage.addChild((ResearchAdvantage)tempResearch.getAdvantage("China Attacker"));
        tempResearchAdvantage.setCostToResearchOneTurn(12);
        tempResearch.addAdvantag(tempResearchAdvantage);
        
        tempResearchAdvantage = new ResearchAdvantage("China Bomber II","Second generation bomber");
        tempResearchAdvantage.setTimeToResearch(2);
        tempResearchAdvantage.addShip(gw.getSpaceshipTypeByName("China Bomber II"));
        tempResearchAdvantage.addReplaceShip(gw.getSpaceshipTypeByName("China Bomber"));
        tempResearchAdvantage.addChild((ResearchAdvantage)tempResearch.getAdvantage("China Attacker"));
        tempResearchAdvantage.setCostToResearchOneTurnInProcent(50);
        tempResearch.addAdvantag(tempResearchAdvantage);
        
        ResearchUpgradeShip researchUpgradeShip = new ResearchUpgradeShip("China Fighter");
        researchUpgradeShip.setWeaponsStrengthLarge(20);
        
        
        tempResearchAdvantage = new ResearchAdvantage("Squadrons","Gives new squadrons to develop");
        tempResearchAdvantage.setTimeToResearch(1);
        tempResearchAdvantage.addChild((ResearchAdvantage)tempResearch.getAdvantage("China Fighter II"));
        tempResearchAdvantage.addChild((ResearchAdvantage)tempResearch.getAdvantage("China Bomber II"));
        tempResearchAdvantage.addResearchUpgradeShip(researchUpgradeShip);
        tempResearch.addAdvantagAsRoot(tempResearchAdvantage);
        
        
        
        
        tempResearch.setNumberOfSimultaneouslyResearchAdvantages(2);
        
        tempFaction.setResearch(tempResearch);
        */
        // *******************************************************************************
        // *******************************************************************************
        // *******************************************************************************
        
        
        //tempFaction.setResistanceBonus();
     //   tempFaction.setStartingWharfSize(OrbitalWharf.MAX_SIZE_MEDIUM);
     //   tempFaction.setWharfBuildCost(2);
     //   tempFaction.setWharfUpgradeCostMedium(8);
     //   tempFaction.setWharfUpgradeCostLarge(18);
     //   tempFaction.setWharfUpgradeCostHuge(40);
        tempFaction.setAdvantages("Strong heavy capital ships");
        tempFaction.setDisadvantages("No small ships and bad squadrons");
        tempFaction.setHistory("2005 was the year then we send up our first man in to the space and started our glory history in space. Only 6 years later we send our first man to the moon. Short after that we began to build up a station on moon and also started the plans to explore Mars.\nTo avoid military conflict in space we decide to write under the non weapons agreement. At 20052 we started to develop and build space ship to explore and transports in the space. Around 2075 we hade more then 300 space ships and was the greatest nation in space.\n\n2098 the chocking new about that one civilian ship was founded drifting around in the space with heavy damage and no cargo left. Short after that our great leaders give orders to put weapons on our ships to protections against pirates. 2112 EU did the first succeeded hyper jump. We don’t know how EU did that before us but we know that some one was spying on us and downloaded information about our hyper drive developing. We decided to get more information about the hyper driver. But still we were in chock then EU did the first jump to a planet in another sun system. We hade the greatest space fleet but could not do any space jumps.\nThen the first hyper engines get out to market we started to build up a fleet for hyper jumping. That was really expensive to do in both economic and human lives. But when we found a safe way to a planet the profit some time was huge. At this time a lot of civilian ships did a lot of hyper jumps to start new colonies. So it’s was more and more common that the new planets we did a succeeded jump to was already under humans control. The most of the time the inhabitant was glad to se us but some time we were founding us under attack.\nAround 2170 we started to lose ships and even some planets after attacks from unknown attacker. AT 2178 some big companies stared to build big war ships and we hade to answer to that threat so we started to develop a large ship to dominate big areas and to give us a chance to destroy pirate’s bases and planets.\nThe name of this ship was War Battleship and was outstanding for many years.\nAt 2240 a lot of planets and ships in the outer space started a federation called Federation of liberty. Only 2 years after that the Great War started after nuclear attacks. It hard to tell who started we attacks but our defence system answered the attack with attacking EU on the earth. We can only guess way EU did that attacked on us. But the reason could be that they was afraid to loose the domination that they hade on long range jumps to us. In only one week we lost all connection with earth and the rest of the sun system.\nWe can only hope that some ships hade the time to flee from the sun system and started over in unknown system.\nWe all have to work hard to survive in the new world. You can’t trust any one outside our nation as we did before the war breakout. You have to have faith in our great nation and trust in our strong military. We have the biggest and strongest ships so don’t hesitant to use them, but beware of our week squadrons don’t match enemies squadrons.");
     //   tempFaction.setBuildOrbitalStructureCostBase(8);
     //   tempFaction.setBuildOrbitalStructureCostMulitplier(0);
     //   tempFaction.setStartWithSS(false);
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setAlignment(china);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);

        
//  ######## Alliance Of Africa (Grön) ########
//      XXX Allians Of Africa
        tempFaction = new Faction("Alliance Of Africa",Faction.getColorHexString(0,255,0),alliance);
        
        // Adding Buildings to the faction
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
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge Wharf", "W5", 30, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Space station", "SS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setOpenPlanetBonus(1);
        tempBuildingType.setClosedPlanetBonus(1);
        tempBuildingType.setDescription("");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Wharf"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
        // Defence platforms
        // -----------------
        // Allians Defender
        tempsst = new SpaceshipType("Allians Defender","ADef",SpaceshipType.SIZE_LARGE,75,370,SpaceshipRange.NONE,4,30,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(20);
        tempsst.setWeaponsMaxSalvoesMedium(10);
        tempsst.setWeaponsStrengthLarge(30);
        tempsst.setWeaponsMaxSalvoesLarge(12);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setIncreaseInitiative(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This large planet defender ship was developed for protection against large and medium capital ships. The ship is very cheap to have because the lack of hyper engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Allians Defender"));
        
        // Allians Home Base
        tempsst = new SpaceshipType("Allians Home Base","AHom",SpaceshipType.SIZE_LARGE,20,300,SpaceshipRange.NONE,0,200,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(45);
        tempsst.setArmorMedium(20);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setIncOwnClosedBonus(7);
        tempsst.setIncOwnOpenBonus(8);
        tempsst.setIncreaseInitiative(7);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This large planet home base is made as a head office and offers good protections against medium sized ships. The ship hasn’t any hyper engine. Gives an income of 6 on closed planet and 7 on open.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Allians Home Base"));
        
        // Squadrons
        // ---------
        // Allians Fighter
        tempsst = new SpaceshipType("Allians Fighter","A-F",SpaceshipType.SIZE_SMALL,7,20,SpaceshipRange.NONE,1,3,uniqueShipIdCounter,5,17);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This ship is probably the best small support dog fighters in the whole world. Cheap and lot of fire power for the money.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Allians Fighter"));
        
        // Allians Bomber
        tempsst = new SpaceshipType("Allians Bomber","A-B",SpaceshipType.SIZE_SMALL,7,15,SpaceshipRange.NONE,1,3,uniqueShipIdCounter,15,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(22);
        tempsst.setWeaponsMaxSalvoesMedium(1);
        tempsst.setWeaponsStrengthLarge(5);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("One of the best small anti capital fighters in the whole world. Cheap and lot of fire power for the money.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Allians Bomber"));
        
        //Allians Attacker
        tempsst = new SpaceshipType("Allians Attacker","A-A",SpaceshipType.SIZE_SMALL,7,30,SpaceshipRange.NONE,2,4,uniqueShipIdCounter,8,11);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(12);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A good all-round support fighter. This fighter is often used then the enemies’ fleets are unscouted.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Allians Attacker"));
        
        //Assault Fighter
        tempsst = new SpaceshipType("Assault Fighter","AFig",SpaceshipType.SIZE_SMALL,12,40,SpaceshipRange.SHORT,2,8,uniqueShipIdCounter,10,28);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setInitSupport(true);
        tempsst.setIncreaseInitiative(2);
        tempsst.setDescription("Extremely powerful dog fighter with capacity to do short range jumps. This ship needs a hangar or a friendly plant to survive");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Assault Fighter"));
        
        //Assault Bomber
        tempsst = new SpaceshipType("Assault Bomber","ABom",SpaceshipType.SIZE_SMALL,5,45,SpaceshipRange.SHORT,3,8,uniqueShipIdCounter,10,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(17);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(15);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setInitSupport(true);
        tempsst.setIncreaseInitiative(3);
        tempsst.setDescription("Powerful anti capital fighter with capacity to do short range jumps. This ship needs a hangar or a friendly plant to survive.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Assault Bomber"));
        
        //Assault Attacker
        tempsst = new SpaceshipType("Assault Attacker","AAtt",SpaceshipType.SIZE_MEDIUM,10,50,SpaceshipRange.LONG,3,14,uniqueShipIdCounter,12,24);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(16);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(7);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setInitSupport(true);
        tempsst.setIncreaseInitiative(6);
        tempsst.setDescription("The greatest fighter in the world with capacity to do long range jumps. Because the extremely threat this fighters is it gives the owner a good advantage. This ship needs a hangar or a friendly plant to survive.");
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
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Shuttle"));
        
        // Troop Ship
        tempsst = new SpaceshipType("Troop Ship","TShi",SpaceshipType.SIZE_MEDIUM,5,20,SpaceshipRange.LONG,2,7,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setPsychWarfare(1);
        tempsst.setCanAppearOnBlackMarket(false);
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
        tempsst.setDescription("A medium carrier and supply ship with enough fire power to hold smaller ships away. But the real fire power is in the squadrons. This is the greatest and most imported ship in the long range fleet.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Liberty Carrier"));
        
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
        tempsst.setDescription("A large short range capital ship with up to 8 squadrons in the hangar. Good against all types of ship without huge capital ships. It has bombardment capacity. The hangar in the ship takes so much space so it's no room for any ground troops.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Assault Ship"));
        
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
        tempsst.setPsychWarfare(2);
        tempsst.setIncreaseInitiative(10);
        tempsst.setInitDefence(2);
        tempsst.setSquadronCapacity(16);
        //tempsst.setSiegeBonus(1);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("The proud of the navy. With the powerful torpedoes against huge ship and capacity to carry 16 squadrons this was the answer against the threat from enemies’ huge capital ships. Great bombardment and a battalion of soldier make this to a great planet over taker");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galaxy WarCarrier"));
        
        // ****** fraction unique property ******
        
        
        
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
        
        tmpVipType = new VIPType("Top Pilot","TopP",alliance,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
        tmpVipType.setInitFighterSquadronBonus(10);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("A Top pilot is an execeptionally skilled Squadron pilot. when he is placed in a squdron he will increase the attack rating of all squadrons in one fleet. He is impossible to kill and he is able to move between planets.");
        gw.addVipType(tmpVipType);
        tempFaction.addStartingVIPType(tmpVipType);
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Troop Ship"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Liberty Carrier"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Assault Attacker"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Allians Home Base"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Allians Defender"));
        
        
        //tempFaction.setResistanceBonus();
        // tempFaction.setStartingWharfSize(OrbitalWharf.MAX_SIZE_SMALL);
        
        
        tempFaction.setAdvantages("Great squadrons");
        tempFaction.setHistory("In the middle of the 21 century the nation of Africa was started to talk about the idea to start a federation to get out in to the space. At 2084 same countries from Africa founded the Alliance of Africa. The main course was to build up a space fleet to defending the interest and get for resources from the space. As soon the first hyper drives were out on the market we started to rebuild old big ships with hyper drives to start new colonies to avoid the starving around out people. This type of trips was very risky but we didn’t have any choice. It took some years before the first ships went back with food and for pick up new volunteers.\nThen we started to lost ships and colonies around 2171 we started to build up a military fleet. To build up fire power as fast as possible we began to develop squadrons to put on our ships. That was a great decision and we shot down a lot o hostile ships. But the year 2230 the pirates started to attack with large ship and own squadrons. To respond to the new threat the started to build up a military fleet with big capital ships carrying squads. But the fire power is still in our good squadrons.\nAt 2240 some of our colonies in the out space went over to the pirate called Federation of liberty.  At 2242 USA attacked us with a massive nuclear attacked and destroyed and killed all humans in Africa on the earth. We don’t rely know way but USA has always accused us for supporting religious military fanatics. But we have to say that it was not just USA that attacked us that week. Who started this war and way? I guess we never are going to found that out that but one thing is sure, we can’t rely on any one. Hit hard before they hit you and build up the fleet to get military domination. We have to try to connect our colonies to secure a good life. Beware of hostiles factions");
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setAlignment(alliance);
        tempFaction.setCorruption(tmpCorruption);
		gw.addFaction(tempFaction);
        
        

        

//  ######## Trade Federation (Blå) ########
//      XXX Trade Federation        
        tempFaction = new Faction("Trade Federation",Faction.getColorHexString(24,66,255),trade);
        
        // Adding Buildings to the faction
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
        
        tempBuildingType= new BuildingType("Large Wharf", "W3", 18, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(3);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge Wharf", "W5", 25, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Space station", "SS", 12, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setOpenPlanetBonus(2);
        //tempBuildingType.setClosedPlanetBonus(1);
        tempBuildingType.setDescription("");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Wharf"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
        // Trade Federation Defence platforms
        // -----------------
        // Federation Defender
        tempsst = new SpaceshipType("Federation Defender","FDef",SpaceshipType.SIZE_LARGE,75,370,SpaceshipRange.NONE,4,36,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(15);
        tempsst.setWeaponsStrengthLarge(20);
        tempsst.setWeaponsMaxSalvoesLarge(10);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setIncreaseInitiative(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This large planet defender ship was developed for protection against large and medium capital ships. The ship is very cheap to have because the lack of hyper engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Federation Defender"));
        
        // Federation Home Base
        tempsst = new SpaceshipType("Federation Home Base","FHom",SpaceshipType.SIZE_HUGE,20,600,SpaceshipRange.NONE,0,200,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(45);
        tempsst.setArmorMedium(20);
        tempsst.setIncOwnClosedBonus(6);
        tempsst.setIncOwnOpenBonus(9);
        tempsst.setIncreaseInitiative(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This huge planet home base is made as a head office and offers protections against small sized ships. The ship hasn’t any hyper engine. Gives an income of 5 on closed planet and 8 on open.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Federation Home Base"));        
        
        // Squadrons
        // ---------
        // Federation Fighter
        tempsst = new SpaceshipType("Federation Fighter","F-F",SpaceshipType.SIZE_SMALL,0,22,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,5,13);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A good and cheap dog fighters that it made by AAD.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Federation Fighter"));
        
        // Federation Bomber
        tempsst = new SpaceshipType("Federation Bomber","F-B",SpaceshipType.SIZE_SMALL,0,15,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,10,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(5);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A good and cheap anti capital fighter that it made by AAD");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Federation Bomber"));
        
        // Federation Attacker
        tempsst = new SpaceshipType("Federation Attacker","F-A",SpaceshipType.SIZE_SMALL,0,30,SpaceshipRange.SHORT,2,4,uniqueShipIdCounter,5,8);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A good all-round support fighter with hyper engine for short range jumps. This fighter is often used to patrol and defending our planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Federation Attacker"));
        
        
        // Capital ships
        // -------------
        // Corvette
        tempsst = new SpaceshipType("Corvette","Crv",SpaceshipType.SIZE_SMALL,10,30,SpaceshipRange.LONG,1,3,uniqueShipIdCounter,8,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setCanBlockPlanet(false);
        tempsst.setDescription("Light small long range capital ship used for exploring and person transports. Build by the company BBZ.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette"));
        
        // Frigate
        tempsst = new SpaceshipType("Frigate","Frg",SpaceshipType.SIZE_SMALL,10,50,SpaceshipRange.LONG,3,7,uniqueShipIdCounter,17,8);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(5);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setDescription("This small long range ship is one of the newly ships in the fleet. Build too scare away enemies small sized ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Frigate"));
        
        // Troop Frigate
        tempsst = new SpaceshipType("Troop Frigate","TFri",SpaceshipType.SIZE_MEDIUM,5,30,SpaceshipRange.LONG,2,6,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setPsychWarfare(1);
        tempsst.setDescription("This is the main troop transport ship with very poor fire power but good manoeuvre capacity that's make this ship to long range class ship. The ship is a rebuild civilian personal transport ship.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Frigate"));
        
        // Carrier
        tempsst = new SpaceshipType("Carrier","Car",SpaceshipType.SIZE_MEDIUM,10,70,SpaceshipRange.LONG,3,12,uniqueShipIdCounter,2,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setArmorSmall(35);
        tempsst.setSquadronCapacity(6);
        tempsst.setDescription("A medium carrier with enough fire power to hold squadron’s away from the hangars ports. Can take up to 6 squadrons and that's make this ship to a great threat in the battlefield.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier"));
        
        // Supply Frigate
        tempsst = new SpaceshipType("Supply Frigate","SFri",SpaceshipType.SIZE_MEDIUM,5,30,SpaceshipRange.SHORT,1,10,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(25);
        tempsst.setSupply(4);
        tempsst.setPlanetarySurvey(true);
        tempsst.setDescription("Rebuild from a civilian ore miner ship that made it to an excellent supply ship with good survey capacity. But the medium hull and badly flight capacity make it to a short ranger and easy target.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Frigate"));
        
        // Medium Destroyer
        tempsst = new SpaceshipType("Medium Destroyer","MDes",SpaceshipType.SIZE_MEDIUM,20,110,SpaceshipRange.LONG,5,12,uniqueShipIdCounter,15,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setArmorSmall(40);
        tempsst.setDescription("This capital ship was designed as a heavy escort to our treading fleets against pirates. Big enough to scare away pirates and fast to hunt them down.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Medium Destroyer"));
                
        //Battleship
        tempsst = new SpaceshipType("Battleship","Bat",SpaceshipType.SIZE_LARGE,75,375,SpaceshipRange.SHORT,11,40,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(30);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(40);
        tempsst.setWeaponsMaxSalvoesLarge(16);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(35);
        tempsst.setIncreaseInitiative(5);
        tempsst.setSquadronCapacity(6);
        //tempsst.setSiegeBonus(1);
        tempsst.setPsychWarfare(1);
        tempsst.setDescription("Then the firsts civilian trading fleets that were escorted by Medium Destroyers was attacked or just disappear in the deep space some huge company’s founded the Trade Federation (AC 2478). The first action Trade Federation did was to build up a military fleet to protect the interest and the first ship that was ordered was this Battleship. The ship if so powerful that it's can combat against up to 3 medium capital ships by it self. The carrier capacity is up to 6 squadrons. The hull is made by the BBZ Company and modify by HAC.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship"));
        
//      Galaxy Battleship
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
        tempsst.setPsychWarfare(2);
        //tempsst.setSiegeBonus(1);
        tempsst.setInitDefence(2);
        tempsst.setPlanetarySurvey(true);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("Construct to secure independence in the war that start at AC 2542 and to give us capacity to overtake planets. The ship has survey capacity to fund hidden pirates raiding stronghold and a good ground troops support that make its troops fighting better. Can by it's self dominate a whole galaxy.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galaxy Battleship"));
        
        // ****** fraction unique property ******
        
        
        
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
        tempFaction.addStartingVIPType(tmpVipType);
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Frigate"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Troop Frigate"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Medium Destroyer"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Federation Home Base"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Federation Defender"));
        
        
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setAdvantages("Resistance bonus = 1");
        tempFaction.setHistory("At 2101 some big companies started a small police force to secure civilian transport in the space. That was the first step against the federation that become Trade Federation. At 2178 some big companies started the Trade Federation to build up an armed fleet to escort trading fleets and to strike back on pirates.\nThe first ship that was ordered was the big and heavy armed Battleship. But the old countries from the earth didn’t liked the idea of heavy armed civilian battle ships, so they started to build up own military fleets to control the space and the colony und the own flags. That wasn’t any god for us at all. Some free (no taxes) planets went under the nation’s flags. To trade with them we were forced to pay heavy taxes to support the nation military fleet that was supposed to give us peas to the world. So our profits went down more and more. Often then we were meeting military fleets on patrol they stopped us fore checking after stolen cargo. Even then our fleet was escorted by the heavy Battleship they tried to forced us to let them board us.\nBut after we lost some fleet after letting false flagged ship board us we declared us as a free nation and that we wasn’t going to let any nation board our ships. Around 2240 we lost lots of trading posts in the outer space. Some was destroyed or just stopped trading or communicate with us. When we were sending out military fleets to find out that happened we often found us under attacked or just destroyed out posts.\nThen the Great War started we fled away from the old world with all ships that we could and just left our bases and ships without hyper drivers behind. Sadly we also lost our great leaders at the main office on earth. We must now found new homes to our dear friends from earth and the rest of the sun system. But beware of the pirated scum that call they self for Federation of liberty. It’s hard to trust any at these times but you can be shore that you can’t do any deals with the pirates, they aren’t trustable at al, don’t forgot to look after outposts still loyal to us.");
        //tempFaction.setStartingWharfSize(OrbitalWharf.MAX_SIZE_SMALL);
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setAlignment(trade);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        
        

//      ######## USA (Gul) ########
//      XXX USA (Gul)        
        tempFaction = new Faction("USA",Faction.getColorHexString(255,197,19),usa);
        
        // Adding Buildings to the faction
        tempBuildings = new Buildings();
        
        tempBuildingType= new BuildingType("Small Wharf", "W1", 3, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(1);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Small Wharf what can build small ships");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Medium Wharf", "W2", 13, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(2);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Medium Wharf what can build small and medium ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Small Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Large Wharf", "W3", 20, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(3);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge Wharf", "W5", 25, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Space station", "SS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setOpenPlanetBonus(1);
        //tempBuildingType.setClosedPlanetBonus(1);
        tempBuildingType.setDescription("");
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Wharf"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
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
        tempsst.setDescription("This Medium planet defender capital ship was developed for protection against squadrons attacks. Common on ours most important planets. Should work together with anti capital ships or USA Bombers.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("USA Defender"));
        
        // USA Military Base
        tempsst = new SpaceshipType("USA Military Base","UMB",SpaceshipType.SIZE_LARGE,20,300,SpaceshipRange.NONE,0,200,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(45);
        tempsst.setArmorMedium(20);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setIncOwnOpenBonus(6);
        tempsst.setIncOwnClosedBonus(5);
        tempsst.setIncreaseInitiative(30);
        tempsst.setNoRetreat(true);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This medium planet home base is made as a military headquarter and offers good protections against medium sized ships. The ship hasn’t any hyper engine. Gives an income of 4 on closed planet and 5 on open. Because of the advanced military technology this base gives a lot of initiative in fleet combats.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("USA Military Base"));
        
        // Squadrons
        // ---------
        // USA Fighter
        tempsst = new SpaceshipType("USA Fighter","U-F",SpaceshipType.SIZE_SMALL,0,27,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,5,14);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A good and cheep dog fighters that it made by AAD and modified by USA armoury.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("USA Fighter"));
        
        // USA Bomber
        tempsst = new SpaceshipType("USA Bomber","U-B",SpaceshipType.SIZE_SMALL,0,18,SpaceshipRange.NONE,1,3,uniqueShipIdCounter,15,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(13);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(8);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A good anti capital fighter that it made by AAD and modified by USA armoury.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("USA Bomber"));
        
        // USA Attacker
        tempsst = new SpaceshipType("USA Attacker","U-A",SpaceshipType.SIZE_SMALL,7,30,SpaceshipRange.SHORT,2,5,uniqueShipIdCounter,8,8);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(12);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A good all-round support fighter with hyper engine for short range jumps. This fighter is often used to replace shot downed squadrons after a battle in fleet far away from our bases.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("USA Attacker"));
        
        
        // Capital ships
        // -------------
        // Corvette II
        tempsst = new SpaceshipType("Corvette II","Crv2",SpaceshipType.SIZE_SMALL,12,38,SpaceshipRange.LONG,2,5,uniqueShipIdCounter,8,6);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setCanBlockPlanet(false);
        tempsst.setArmorSmall(5);
        tempsst.setDescription("Light small long range capital ship used for exploring and person transports. Build by the company BBZ and modify by the military. Cost more build to but is one of the best ships in the class.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corvette II"));
        
        // Frigate II
        tempsst = new SpaceshipType("Frigate II","Frg2",SpaceshipType.SIZE_SMALL,15,55,SpaceshipRange.LONG,3,7,uniqueShipIdCounter,10,5);
        tempsst.setArmorSmall(5);
        tempsst.setWeaponsStrengthMedium(5);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(1);
        tempsst.setDescription("Based on the hull from a Frigate and modified with torpedo against large capital ships that’s made it to a good supporting ships in the big battles.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Frigate II"));
        
        // Troop Frigate II
        tempsst = new SpaceshipType("Troop Frigate II","TFr2",SpaceshipType.SIZE_MEDIUM,5,35,SpaceshipRange.LONG,2,8,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(25);
        tempsst.setArmorMedium(5);
        tempsst.setPsychWarfare(1);
        tempsst.setDescription("This is the main troop transport ship with very poor fire power but good manoeuvre capacity that's make this ship to long range class ship. The ship is a rebuild civilian personal transporter modified with lasers.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Frigate II"));
        
        // Carrier II
        tempsst = new SpaceshipType("Carrier II","Car2",SpaceshipType.SIZE_MEDIUM,15,80,SpaceshipRange.LONG,3,14,uniqueShipIdCounter,12,12);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(35);
        tempsst.setSquadronCapacity(6);
        tempsst.setDescription("A medium carrier with enough fire power to hold fighter ships away from the hangars ports. Can take up to 6 squadrons and that's make this ship to a great supporting ship on long range mission.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrier II"));
        
        // Supply Frigate II
        tempsst = new SpaceshipType("Supply Frigate II","SFr2",SpaceshipType.SIZE_MEDIUM,25,30,SpaceshipRange.SHORT,1,12,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(35);
        tempsst.setSupply(4);
        tempsst.setPlanetarySurvey(true);
        tempsst.setDescription("Civilian luxury cruisers rebuild to supply the fleet. The medium hull and badly flight capacity make it to a short ranger and easy target.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Frigate II"));
        
        // Destroyer II
        tempsst = new SpaceshipType("Destroyer II","Des2",SpaceshipType.SIZE_MEDIUM,20,130,SpaceshipRange.LONG,5,13,uniqueShipIdCounter,20,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(15);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setArmorSmall(40);
        tempsst.setDescription("A medium ship and the best long range battle ship in the fleet. Good against medium and small ship but don't heavy enough to go against the big battle main ships. Used to control the outer space.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Destroyer II"));
                
        //Battleship II
        tempsst = new SpaceshipType("Battleship II","Bat2",SpaceshipType.SIZE_LARGE,75,440,SpaceshipRange.SHORT,11,49,uniqueShipIdCounter,12,12);
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
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("Build on the same hull as the Battleship but modified to get bombardment by USA armoury. Strong and all-round to work alone deep in the space. Superior the most large ships but can’t do long jumps. The carrier capacity is up to 6 squadrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battleship II"));
        
//      Galaxy Battleship
        tempsst = new SpaceshipType("Galaxy Battleship II","GBa2",SpaceshipType.SIZE_HUGE,115,750,SpaceshipRange.SHORT,27,96,uniqueShipIdCounter,10,10);
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
        tempsst.setPsychWarfare(1);
        tempsst.setPlanetarySurvey(true);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("Construct to secure military domination in the galaxy. Good in both fleet combats and to overtake planets (excellent bombardment plus a battalion troops). Good survey capacity to get information about the planet resistance.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galaxy Battleship II"));
        
        // ****** fraction unique property ******
        
        
        
   /*     VIPType President2 = new VIPType("President","Pre",usa,uniqueVIPIdCounter);
        President2.setGovernor(true);
        President2.setWellGuarded(true);
        President2.setOpenIncBonus(1);
        President2.setCanVisitNeutralPlanets(true);
        President2.setDiplomat(true);
        President2.setFrequency(BlackMarketFrequency.NEVER);
        President2.setDescription("The President is the leader of the faction so protect him. He gives one extra income on open planets. He have also diplomatic skill to convince neutral planets to join he’s faction.");
        gw.addVipType(President2);*/
        tempFaction.setGovernorVIPType(President);
        
        tmpVipType = new VIPType("Coordinator","Coor",usa,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setShipBuildBonus(10);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("Thanks to the skill the coordinator have the ships build on the same planets is cheaper to build. He is guarded by elite soldiers and therefore impossible to kill. He can also travel between all sorts of planets.");
        gw.addVipType(tmpVipType);
        tempFaction.addStartingVIPType(tmpVipType);
        
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Corvette II"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Troop Frigate II"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("USA Fighter"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("USA Fighter"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("USA Bomber"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("USA Attacker"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Carrier II"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("USA Military Base"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("USA Defender"));
        
        
        
        tempFaction.setResistanceBonus(1);
        tempFaction.setAdvantages("Resistance bonus = 1, good ships");
        tempFaction.setDisadvantages("Expensive ships");
        tempFaction.setHistory("The year 2006 we started to develop new rockets and space ship to explore and to build up a base on the moon. One of the main causes was that China hade started o program to get to the moon and get out to the space. We could of course not let any nation get that military advanced on us. So we started to send in a lot of dollar in to the project to be first with a permanent moon base. At 2012 we did our first trip to moon for over 40 years (one year after China). It took longer time then expected but we hade also one base just one year after that. This developing went to be a huge deep hole to put money in and we were forced to take big loans to going on. To avoid military conflict in space we decide to write under the non weapons agreement. The biggest reason was that we were total broke and didn’t hade any resources to spend on advanced military space technology.\nThe in the following years we expand our space fleet and around AC 2072 we hade almost 250 heavy space ships. Just after China we were the greatest nation in the space. It was now all the money spent in the space program was began pay of. Our economy was going from nearly broken to great.\nThen the first space shipped was attacked we decide to armed our space fleet and also to build up a military space fleet. Then the first hyper driver was developed we were chocked and long after that technology. It took us some years to rebuild our fleet fore hyper jumping’s.  At 2178 some company started a federation and began to build to build Battle ships up to 15 times bigger then our military ships. Of course couldn’t we accept that so we decide to make a copy of the same hull as the Battleship was made on and armed it with our superior weapon systems. It wasn’t easy to get the blue prints on the hull and not easy to denied the access to them.\nAround AC 2230 we lost a lot civilian ship in the outer space and also some heavy armed military fleet. We don’t know who that did this attacks but only one non nation military fleet hade that military strange to attack our great military fleet. It hade to be the Trade federation but we don’t have the evidence to proof that. To answer against this type of threat we decide to build up our military fleet.\n22 Mars in 2242 the war began at earth and in the rest of sun system. We don’t have any report from on that happened and the first month after that. All ours ship what was on way to earth didn’t come back before we send away 5 great warships to checked out that was going on. One of them returned heavy damaged and not much to report. As fast they entered the space around the earth they were attacked by heavy fire from unknown ships. The only reason that the only one returned was a kamikaze attack against one of the hostile’s ship from one of our own battleship.\nWe sound to be the only nation that doesn’t know anything about that happened on earth. We must now trust on our military fleet to defend us ands it’s time to pay back. But first must we get in contact with our colonies and start to develop and build up our fleet.");
        //tempFaction.setStartingWharfSize(OrbitalWharf.MAX_SIZE_SMALL);
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setAlignment(usa);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        
        
		
        
//      ######## Russian (Rosa) ########
//      XXX Russian (Rosa)        
        tempFaction = new Faction("Russian",Faction.getColorHexString(255,0,255),russian);
        
        // Adding Buildings to the faction
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
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge Wharf", "W5", 22, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Space station", "SS", 15, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setTechBonus(10);
        //tempBuildingType.setOpenPlanetBonus(1);
        //tempBuildingType.setClosedPlanetBonus(1);
        tempBuildingType.setDescription("");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Wharf"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
        // Russian Defence platforms
        // -----------------
        // Russian Defender
        tempsst = new SpaceshipType("Russian Defender","RDef",SpaceshipType.SIZE_SMALL,0,70,SpaceshipRange.NONE,1,8,uniqueShipIdCounter,2,2);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthLarge(22);
        tempsst.setWeaponsMaxSalvoesLarge(8);
        tempsst.setIncreaseInitiative(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This small planet defender ship was developed for protection against large capital ships. It’s very cheap to have because the lack of hyper engine. Protect these ships with anti squadron’s ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Russian Defender"));
        
        // Russian Home Base
        tempsst = new SpaceshipType("Russian Home Base","RHom",SpaceshipType.SIZE_LARGE,20,300,SpaceshipRange.NONE,0,200,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(45);
        tempsst.setArmorMedium(20);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(30);
        tempsst.setWeaponsMaxSalvoesLarge(13);
        tempsst.setIncOwnClosedBonus(6);
        tempsst.setIncOwnOpenBonus(7);
        tempsst.setIncreaseInitiative(5);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This large planet home base is made as a head office and offers good protections against all type of ships and special large ships. The ship hasn’t any hyper engine. Gives an income of 5 on closed planet and 6 on open.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Russian Home Base"));        
        
        // Squadrons
        // ---------
        // Russian Fighter
        tempsst = new SpaceshipType("Russian Fighter","R-F",SpaceshipType.SIZE_SMALL,0,22,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,4,12);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A cheap dog fighter that was rebuild from a common earth fighter.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Russian Fighter"));
        
        // Russian Bomber
        tempsst = new SpaceshipType("Russian Bomber","R-B",SpaceshipType.SIZE_SMALL,0,15,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,10,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(5);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A cheap anti capital fighter with week hull.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Russian Bomber"));
        
        // Russian Heavy Bomber
        tempsst = new SpaceshipType("Russian Heavy Bomber","R-HB",SpaceshipType.SIZE_SMALL,6,30,SpaceshipRange.SHORT,2,6,uniqueShipIdCounter,10,2);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(17);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setIncreaseInitiative(1);
        tempsst.setDescription("Powerful anti capital fighter with capacity to do short range jumps. This ship needs a hangar or a friendly plant to survive. Often used in combination with a Star Destroyer to make a short history with hostiles medium or large capitals ships.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Russian Heavy Bomber"));
        
        
        // Capital ships
        // -------------
        // Russian Corvette
        tempsst = new SpaceshipType("Russian Corvette","RCrv",SpaceshipType.SIZE_SMALL,6,25,SpaceshipRange.LONG,2,3,uniqueShipIdCounter,6,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setCanBlockPlanet(false);
        tempsst.setDescription("Light small long range capital ship used for exploring and person transports. To small for blockading enemies planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Russian Corvette"));
        
        // Cruiser
        tempsst = new SpaceshipType("Cruiser","Cru",SpaceshipType.SIZE_SMALL,10,50,SpaceshipRange.SHORT,3,5,uniqueShipIdCounter,11,8);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(5);
        tempsst.setWeaponsMaxSalvoesMedium(2);
        tempsst.setDescription("A small short range ship with capacity to attack medium sized ship if they are in great numbers. Cheap to build but expensive to own.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Cruiser"));
        
        // Troop Cruiser
        tempsst = new SpaceshipType("Troop Cruiser","TCru",SpaceshipType.SIZE_MEDIUM,5,20,SpaceshipRange.LONG,2,6,uniqueShipIdCounter,8,8);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setPsychWarfare(1);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This is the main troop transport ship with very poor fire power but good manoeuvre capacity that's make this ship to long range class ship.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Cruiser"));
        
        // planet Carrier
        tempsst = new SpaceshipType("Planet Carrier","PCar",SpaceshipType.SIZE_MEDIUM,6,60,SpaceshipRange.LONG,3,9,uniqueShipIdCounter,8,8);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setSquadronCapacity(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("An Old class medium carrier that can take 4 squadrons. This ship should not take any active part in the combat action.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Planet Carrier"));
        
        // Supply Cruiser
        tempsst = new SpaceshipType("Supply Cruiser","SCru",SpaceshipType.SIZE_MEDIUM,5,20,SpaceshipRange.SHORT,1,9,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(20);
        tempsst.setSupply(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("Old troop transporters rebuild to supply the fleet. The medium hull and badly flight capacity make it to a short ranger and easy target.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Cruiser"));
        
        // Ship Destroyer
        tempsst = new SpaceshipType("Ship Destroyer","SDes",SpaceshipType.SIZE_MEDIUM,10,100,SpaceshipRange.LONG,5,9,uniqueShipIdCounter,12,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(12);
        tempsst.setWeaponsMaxSalvoesMedium(6);
        tempsst.setArmorSmall(30);
        tempsst.setDescription("A medium ship that is the best long range battle ship in the fleet. Good against medium and small ship but don't heavy enough to go against the big battle main ships. Used to control the outer space. The ship is the backbone in our fleet.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Ship Destroyer"));
                
        //Star Destroyer
        tempsst = new SpaceshipType("Star Destroyer","StaD",SpaceshipType.SIZE_LARGE,45,310,SpaceshipRange.SHORT,11,34,uniqueShipIdCounter,10,30);
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
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("Good against both capital ships and squadrons. This ship was build to fight by the own and have carrier capacity up to 6 squadrons. The weakness of this ship is the lack of troops and long range capacity. Often used as in the front to destroy enemies fleets and scouts out enemies planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Star Destroyer"));
        
//      Galaxy Destroyer
        tempsst = new SpaceshipType("Galaxy Destroyer","GDes",SpaceshipType.SIZE_HUGE,80,600,SpaceshipRange.SHORT,25,86,uniqueShipIdCounter,10,15);
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
        tempsst.setPsychWarfare(2);
        //tempsst.setSiegeBonus(1);
        tempsst.setBombardment(1);
        tempsst.setNoRetreat(true);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("The pride of the fleet and good planet conquer with bombing, troops and ground control central. This ship has capacity to carry 9 squadrons and a system that prevents ships to start theirs hyper space engines and flee from the battlefield. Not the strongest huge class ship in the space but gives lots for the money.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galaxy Destroyer"));
        
        // ****** fraction unique property ******
        
        
        
   /*     VIPType President3 = new VIPType("President","Pre",russian,uniqueVIPIdCounter);
        President3.setGovernor(true);
        President3.setWellGuarded(true);
        President3.setOpenIncBonus(1);
        President3.setCanVisitNeutralPlanets(true);
        President3.setDiplomat(true);
        President3.setFrequency(BlackMarketFrequency.NEVER);
        President3.setDescription("The President is the leader of the faction so protect him. He gives one extra income on open planets. He have also diplomatic skill to convince neutral planets to join he’s faction.");
        gw.addVipType(President3);*/
        tempFaction.setGovernorVIPType(President);
        
        tmpVipType = new VIPType("Engineer","Eng",russian,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setTechBonus(10);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("Thanks to the skill the engineer has the ships build on the same planets is better. He is guarded by elite soldiers and therefore impossible to kill. He can also travel between all sorts of planets.");
        gw.addVipType(tmpVipType);
        tempFaction.addStartingVIPType(tmpVipType);
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Russian Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Cruiser"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Troop Cruiser"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Ship Destroyer"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Russian Home Base"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Russian Defender"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Russian Defender"));
        
        
        tempFaction.setResistanceBonus(3);
        tempFaction.setOpenPlanetBonus(-1);
        tempFaction.setAdvantages("Resistance Bonus = 3, Tech bonus on Space Station");
        tempFaction.setDisadvantages("Open planet gives only 1 extra incom");
        tempFaction.setHistory("At 2042 Russian was the first nation to build a space ship for travelling around in the sun system. We weren’t the first nation on Mars but we started to build a great station that we easily supplied with help of our space ship. The big problem we hade was the trips down and up from the planets. But then the base on Mars went big and big we started to get more and more resources to our space station and wharfs. By to lack of money we hade to take ides from civilian ship and replace expansive solutions with cheaper.\nThen the first pirated attacks around 2100 started we were forced to build up a military fleet. To avoid big costs we decide to build the world first space squadron. That wasn’t easy but we could fast build new squadrons in any of our wharfs that we hade.\nAt 2121 we started to bay in hyper engines to develop own engines and hulls to them. At 2166 we hade our first heavy squadron equipped with a hyper engine. Short after that we killed a spy at our centre for developing of space squadron. We can only guess who he worked for, but just 4 years later Alliance of Africa developed where first heavy squadron.\nThe 22 Mars in 2242 the first attacked against the earth was made. We were taken by surprise and with our guard down. The ships that could escape did that but unfortunately not to the same place. It’s now time to start build up our military fleet and to reconnect our colonies. We don’t have the greatest fleet but don’t forget that we have the best ground defence in the world.");
        //tempFaction.setStartingWharfSize(OrbitalWharf.MAX_SIZE_SMALL);
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setAlignment(russian);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        
        
        
//      ######## EU (Turkos) ########
//      XXX EU (Turkos)       
        tempFaction = new Faction("EU",Faction.getColorHexString(128,255,255),eu);
        
        // Adding Buildings to the faction
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
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge Wharf", "W5", 8, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Space station", "SS", 10, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        //tempBuildingType.setTechBonus(10);
        tempBuildingType.setOpenPlanetBonus(1);
        //tempBuildingType.setClosedPlanetBonus(1);
        tempBuildingType.setDescription("");
        tempBuildings.addBuilding(tempBuildingType);
        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Medium Wharf"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
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
        tempsst.setDescription("This huge planet defender ship was developed for protection against large and huge capital ships. It’s very cheap to have because the lack of hyper engine");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("EU Defender"));
        
        // EU Home Base
        tempsst = new SpaceshipType("EU Home Base","EHom",SpaceshipType.SIZE_LARGE,120,20,SpaceshipRange.NONE,0,200,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(10);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(10);
        tempsst.setWeaponsStrengthLarge(10);
        tempsst.setWeaponsMaxSalvoesLarge(10);
        tempsst.setIncOwnClosedBonus(7);
        tempsst.setIncOwnOpenBonus(8);
        tempsst.setIncreaseInitiative(5);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This large planet home base is made as a head office and offers good protections against all type of ships. The ship hasn’t any hyper engine. Gives an income of 5 on closed planet and 6 on open.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("EU Home Base"));        
        
        // Squadrons
        // ---------
        // EU Fighter
        tempsst = new SpaceshipType("EU Fighter","EU-F",SpaceshipType.SIZE_SMALL,13,7,SpaceshipRange.NONE,1,3,uniqueShipIdCounter,5,19);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setDescription("A good and cheap dog fighter.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("EU Fighter"));
        
        // EU Bomber
        tempsst = new SpaceshipType("EU Bomber","EU-B",SpaceshipType.SIZE_SMALL,7,7,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,10,3);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(12);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(7);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setDescription("A good and cheap anti capital fighter.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("EU Bomber"));
        
        // EU Attacker
        tempsst = new SpaceshipType("EU Attacker","EU-A",SpaceshipType.SIZE_SMALL,15,10,SpaceshipRange.LONG,2,5,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setDescription("A good all-round support fighter with hyper engine for long range jumps. This fighter is often used to patrol and defending our planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("EU Attacker"));
        
        
        // Capital ships
        // -------------
        // EU Corvette
        tempsst = new SpaceshipType("EU Corvette","EUCr",SpaceshipType.SIZE_SMALL,20,10,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,11,7);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setCanBlockPlanet(false);
        tempsst.setDescription("Light small long range capital ship used for exploring and person transports. To small for blockading enemies planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("EU Corvette"));
        
        // Assault Frigate
        tempsst = new SpaceshipType("Assault Frigate","AFrg",SpaceshipType.SIZE_SMALL,20,15,SpaceshipRange.LONG,3,6,uniqueShipIdCounter,17,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(6);
        tempsst.setWeaponsMaxSalvoesMedium(1);
        tempsst.setDescription("A small short range ship with capacity to attack medium sized ship if they are in great numbers. Armed with good shields and weapons. This is the most advanced corvette in the whole world but have a smaller hull.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Assault Frigate"));
        
        // Troop Fighter
        tempsst = new SpaceshipType("Troop Fighter","TFig",SpaceshipType.SIZE_MEDIUM,20,15,SpaceshipRange.LONG,2,8,uniqueShipIdCounter,5,20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setPsychWarfare(1);
        tempsst.setDescription("This ship is a combination between a troop ship and anti squadron’s ship. The week shield and medium hull makes it vulnerable against capital ships. One of the most used ship in our fleet.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Fighter"));
        
        // Fighter Transporter
        tempsst = new SpaceshipType("Fighter Transporter","FTra",SpaceshipType.SIZE_MEDIUM,40,30,SpaceshipRange.LONG,4,13,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(5);
        tempsst.setWeaponsStrengthLarge(5);
        tempsst.setWeaponsMaxSalvoesLarge(3);
        tempsst.setSquadronCapacity(4);
        tempsst.setDescription("The fleet’s carrier much more armed then carriers are used to be. Have capacity to shoot against up to large ships. Can carrier up to 4 squadrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Transporter"));
        
        // Supply Transporter
        tempsst = new SpaceshipType("Supply Transporter","Stra",SpaceshipType.SIZE_MEDIUM,15,20,SpaceshipRange.LONG,1,11,uniqueShipIdCounter,5,5);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setArmorSmall(10);
        tempsst.setSupply(4);
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
        tempsst.setDescription("A medium ship that is all-round and good against large capital ships. Heavy armed to bombard planets and with excellent shields. Because the heavy weapons take a lot of space this ship don’t have any room for the big long ranges engine.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Planet Destroyer"));
                
        //Star Frigate
        tempsst = new SpaceshipType("Star Frigate","SFRi",SpaceshipType.SIZE_LARGE,160,250,SpaceshipRange.SHORT,11,41,uniqueShipIdCounter,20,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(25);
        tempsst.setWeaponsMaxSalvoesMedium(20);
        tempsst.setWeaponsStrengthLarge(47);
        tempsst.setWeaponsMaxSalvoesLarge(16);
        tempsst.setArmorSmall(15);
        tempsst.setArmorMedium(5);
        tempsst.setIncreaseInitiative(5);
        tempsst.setSquadronCapacity(4);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This ship was build to get more fire power to the fleet. The carrier capacity is up to 6 squadrons. The weakness of this ship is the lack of troops and long range capacity.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Star Frigate"));
        
//      Galaxy Frigate
        tempsst = new SpaceshipType("Galaxy Frigate","GFri",SpaceshipType.SIZE_LARGE,270,300,SpaceshipRange.LONG,20,66,uniqueShipIdCounter,10,25);
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
        tempsst.setPsychWarfare(1);
        tempsst.setPlanetarySurvey(true);
        tempsst.setInitDefence(2);
        tempsst.setBombardment(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("The king of long range ships. The only large long range ship in the world. Great bombardment capacity and troops to overtake planets. Good against all types of ships but missing carrier capacity. Good planet surveying. This is likely the most advanced ship in the world but make it to the most expansive to build and maintenance.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galaxy Frigate"));
        
        // ****** fraction unique property ******
        
        
        
    /*    VIPType President4 = new VIPType("President","Pre",eu,uniqueVIPIdCounter);
        President4.setGovernor(true);
        President4.setWellGuarded(true);
        President4.setOpenIncBonus(1);
        President4.setCanVisitNeutralPlanets(true);
        President4.setDiplomat(true);
        President4.setFrequency(BlackMarketFrequency.NEVER);
        President4.setDescription("The President is the leader of the faction so protect him. He gives one extra income on open planets. He have also diplomatic skill to convince neutral planets to join he’s faction.");
        gw.addVipType(President4);*/
        tempFaction.setGovernorVIPType(President);
        
        tmpVipType = new VIPType("War Genius ","WarG",eu,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setInitBonus(10);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("Thanks to the skill the war genius has, the fleet gives more opportunity to fire the weapons. A war genius is a leader and must be on a capital ship to lead and give bonus. He is guarded by elite soldiers and therefore impossible to kill. He can also travel between all sorts of planets.");
        gw.addVipType(tmpVipType);
        tempFaction.addStartingVIPType(tmpVipType);
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("EU Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Assault Frigate"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Troop Fighter"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Fighter Transporter"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("EU Home Base"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("EU Defender"));
        
        
        tempFaction.setAdvantages("Start with medium wharf, good shields");
        tempFaction.setDisadvantages("week hulls");
        tempFaction.setHistory("At the year 2041 we made our first trip with a new space ship type that could travel between earth and space without any helps from rockets. That’s gave us much more capacity to travel up to the space. Just after that we started a program to build ships to travel around in the space and colonise the sun system.\nIn 2098 the first pirate attacked in space chocked us all and we were not prepared on that at all. To defend us against the attacked we started to travel in fleets.\nAt 2104 we were the first nation that hade developed a hyper drive engine, but it took us more then 8 years before we could do the first jump between 2 planets. Then doing this type of jumps you need to found an absolute free ways. One single small stone could destroy a whole ship then it hits the ship in 232 times lighting speed. 4 years after we did our first jumped to a planet in another sun system. That started a new epoch in space history and soon after that we decide to sell hyper engines outside the nation fleet. The reason was that we found out that a lot of nation was trying to steal the technology from us. The only nation that we hade evidence on was Russian but we know that more nations or companies was trying. Then the hyper drive was free to bay a lot of pore people bought old ships and equipped it with the hyper drivers and did a jump to unknown sun system to start a new life. We know that many of these desperate trips ended in the hyper jump, some of the ships hull wasn’t strong enough or the jump way was unsafe.\nTo develop more safely jump we decide to develop shields to protects the ships against smaller stones. With ships equipped with shield we could began travel to all smaller colonies and start trading with them. Around 2190 the first heavy armed battle ships was made. That was of course a military threat so we decided to start developing new military ship to protect us. Lucky for us we hade a pretty good military fleet around 2230 then the bigger pirates attacks started. But we decided to start develop the strongest and most advanced large ship in the world. The first Galaxy Frigate was made and in duty deep in the space just before the war began.\nWe don’t know way and who that started this war but we hade to defend us. We have the most advanced shields to protect our ships so don’t be afraid. We must now build up our wharfs so we can secure independents with help of ours Galaxy Frigates.");
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setAlignment(eu);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
        

		
		
        
//      ######## Federation of liberty (Lila) ########
//      XXX Federation of liberty        
        tempFaction = new Faction("Federation Of Liberty",Faction.getColorHexString(120,50,180),federation);
        
        // Adding Buildings to the faction
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
        
        tempBuildingType= new BuildingType("Large Wharf", "W3", 18, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(3);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Large Wharf what can build small, medium and large ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Medium Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        tempBuildingType= new BuildingType("Huge Wharf", "W5", 25, uniqueBuildingIdCounter);
        tempBuildingType.setWharfSize(5);
        tempBuildingType.setInOrbit(true);
        tempBuildingType.setDescription("Huge Wharf what can build small, medium, large and huge ships");
        tempBuildingType.setParentBuildingType(tempBuildings.getBuildingType("Large Wharf"));
        tempBuildings.addBuilding(tempBuildingType);
        
        
        tempBuildingType= new BuildingType("Space station", "SS", 11, uniqueBuildingIdCounter);
        tempBuildingType.setSpaceport(true);
        tempBuildingType.setInOrbit(true);
        //tempBuildingType.setTechBonus(10);
        tempBuildingType.setOpenPlanetBonus(1);
        tempBuildingType.setClosedPlanetBonus(1);
        tempBuildingType.setDescription("");
        tempBuildings.addBuilding(tempBuildingType);
        

        
        tempFaction.addStartingBuildings(tempBuildings.getBuildingType("Small Wharf"));
        
        // Adding all buildings to the faction.
        tempFaction.setBuildings(tempBuildings);
        
        // Federation of liberty Defence platforms
        // -----------------
        // Liberty Defender
        tempsst = new SpaceshipType("Liberty Defender","LDef",SpaceshipType.SIZE_HUGE,0,540,SpaceshipRange.NONE,5,36,uniqueShipIdCounter,15,10);
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
        tempsst.setDescription("This huge planet defender ship was developed for protect important planets. It’s very cheap to have because the lack of hyper engine and also have a system that prevents ships to start theirs hyper space engines and flee from the battlefield.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Liberty Defender"));
        
        // Raid Station
        tempsst = new SpaceshipType("Raid Station","RSta",SpaceshipType.SIZE_MEDIUM,0,200,SpaceshipRange.NONE,0,120,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(65);
        tempsst.setArmorMedium(30);
        tempsst.setWeaponsStrengthMedium(10);
        tempsst.setWeaponsMaxSalvoesMedium(13);
        tempsst.setIncOwnClosedBonus(8);
        tempsst.setIncreaseInitiative(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This medium planet home base is made as raids headquarter and offers good protections against medium sized ships. The ship hasn’t any hyper engine. Gives an income of 6 on closed planet.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Raid Station"));
        
        
        // Squadrons
        // ---------
        // Scout
        tempsst = new SpaceshipType("Scout","Sco",SpaceshipType.SIZE_SMALL,0,10,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,1,9);
        tempsst.setSquadron(true);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setPlanetarySurvey(true);
        tempsst.setDescription("This squadron are used to scout out unknown planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Scout"));
        
        // Liberty Fighter
        tempsst = new SpaceshipType("Liberty Fighter","L-F",SpaceshipType.SIZE_SMALL,0,30,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,3,13);
        tempsst.setSquadron(true);
        tempsst.setArmorSmall(20);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A good and cheap dog fighter.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Liberty Fighter"));
        
        // Liberty Bomber
        tempsst = new SpaceshipType("Liberty Bomber","L-B",SpaceshipType.SIZE_SMALL,0,22,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,10,2);
        tempsst.setSquadron(true);
        tempsst.setArmorSmall(10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setWeaponsStrengthMedium(8);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setWeaponsStrengthLarge(4);
        tempsst.setWeaponsMaxSalvoesLarge(2);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("A good and cheap anti capital fighter.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Liberty Bomber"));
        
        // Liberty Attacker
        tempsst = new SpaceshipType("Liberty Heavy Attacker","L-HA",SpaceshipType.SIZE_SMALL,0,45,SpaceshipRange.SHORT,2,6,uniqueShipIdCounter,10,12);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setSquadron(true);
        tempsst.setArmorSmall(10);
        tempsst.setWeaponsStrengthMedium(14);
        tempsst.setWeaponsMaxSalvoesMedium(4);
        tempsst.setInitSupport(true);
        tempsst.setIncreaseInitiative(1);
        tempsst.setDescription("A good all-round support fighter with hyper engine for short range jumps. This fighter is often used to patrol and defending our planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Liberty Heavy Attacker"));
        
        
        // Capital ships
        // -------------
        // Liberty Corvette
        tempsst = new SpaceshipType("Liberty Corvette","LCrv",SpaceshipType.SIZE_SMALL,0,35,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,10,6);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setCanBlockPlanet(false);
        tempsst.setArmorSmall(20);
        tempsst.setDescription("Light small long range capital ship used for exploring and person transports. To small for blockading enemies planets.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Liberty Corvette"));
        
        // Fighter Corvette
        tempsst = new SpaceshipType("Fighter Corvette","FCrv",SpaceshipType.SIZE_SMALL,0,40,SpaceshipRange.LONG,3,6,uniqueShipIdCounter,5,15);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
        tempsst.setArmorSmall(50);
        tempsst.setDescription("This is a modified Liberty corvette with more fire power against squadrons.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fighter Corvette"));
        
        // Old troop Transporter
        tempsst = new SpaceshipType("Old Troop Transporter","OTra",SpaceshipType.SIZE_MEDIUM,0,15,SpaceshipRange.LONG,2,8,uniqueShipIdCounter,2,2);
        tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setPsychWarfare(1);
        tempsst.setDescription("This is the main troop transport ship with very poor fire power but good manoeuvre capacity that's make this ship to long range class ship");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Old Troop Transporter"));
        
        // Planet Raider
        tempsst = new SpaceshipType("Planet Raider","PRai",SpaceshipType.SIZE_MEDIUM,0,70,SpaceshipRange.LONG,5,9,uniqueShipIdCounter,17,5);
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
        tempsst.setDescription("This medium sized ship is one of the most advanced ships with cloaking skills that makes it invisible at long distance. One of the best ways to use the cloaking is to blocked enemies planets and raid civilian trading ships. That’s make this ship cheap to have at enemies planets. But it only works with one at the same planet.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Planet Raider"));
        
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
        tempsst.setPsychWarfare(1);
        tempsst.setDescription("Probably the strongest medium ship in the galaxy. Armed with a DZ800 laser for bombardment and construct so it even can fire against large capital ships. The DZ800 laser take so much place that this ship missing long range engines.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Troop Destroyer"));
        
        // Suport Carrier
        tempsst = new SpaceshipType("Suport Carrier","SCar",SpaceshipType.SIZE_MEDIUM,0,80,SpaceshipRange.LONG,3,11,uniqueShipIdCounter,10,10);
        tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
        tempsst.setArmorSmall(55);
        tempsst.setArmorMedium(40);
        tempsst.setSquadronCapacity(6);
        tempsst.setCanAppearOnBlackMarket(false);
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
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("The fleets best and larges anti capital ship. Good against medium and large class ship. Often used in great numbers in the really big battles. Takes up to 8 squadrons but is to big to go long range.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Warship"));
        
//      Galaxy Ship
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
        tempsst.setPsychWarfare(1);
        tempsst.setInitDefence(10);
        tempsst.setCanAppearOnBlackMarket(false);
        tempsst.setDescription("This huge anti squadrons’ ship is armed with 58 anti squadrons cannons. Often armed with up to 8 Liberty Heavy Attackers or Liberty Bombers to hold big capitals ship on safe distance. The Hull is from the biggest ore miner ship and armed to the maximum.");
        gw.addShipType(tempsst);
        tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galaxy Ship"));
        
        // ****** fraction unique property ******
        
        
        
  /*      VIPType President5 = new VIPType("President","Pre",federation,uniqueVIPIdCounter);
        President5.setGovernor(true);
        President5.setWellGuarded(true);
        President5.setOpenIncBonus(1);
        President5.setCanVisitNeutralPlanets(true);
        President5.setDiplomat(true);
        President5.setFrequency(BlackMarketFrequency.NEVER);
        President5.setDescription("The President is the leader of the faction so protect him. He gives one extra income on open planets. He have also diplomatic skill to convince neutral planets to join he’s faction.");
        gw.addVipType(President5);*/
        tempFaction.setGovernorVIPType(President);
        
        tmpVipType = new VIPType("Master Spy","MSpy",federation,uniqueVIPIdCounter);
        tmpVipType.setCanVisitNeutralPlanets(true);
        tmpVipType.setCanVisitEnemyPlanets(true);
        tmpVipType.setHardToKill(true);
        tmpVipType.setWellGuarded(true);
        tmpVipType.setSpying(true);
        tmpVipType.setImmuneToCounterEspionage(true);
        tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
        tmpVipType.setDescription("The master spy can visit enemy’s planets and get the same information as on open planets. He is guarded by elite soldiers and therefore impossible to kill.");
        gw.addVipType(tmpVipType);
        tempFaction.addStartingVIPType(tmpVipType);
        
        
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Fighter Corvette"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Suport Carrier"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Planet Raider"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Liberty Heavy Attacker"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Old Troop Transporter"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Raid Station"));
        tempFaction.addStartingShipType(tempFaction.getSpaceshipTypeByName("Liberty Defender"));
        
        
        tempFaction.setClosedPlanetBonus(1);
        tempFaction.setAdvantages("Closed planet bonus = 1, good armor");
        tempFaction.setDisadvantages("No shields");
        tempFaction.setHistory("In the century of 2230 the space was a chaotic place with a lot pirates attacks. The most attacks were against outer space colonies that didn’t get any protections from the old countries and only helped from Trade Federation if the colony was a good trading partner. The smaller colonies hade to protect them self and some time they was attacked by the big federations accuse to be pirates stronghold.\nSome colonies started to build up army fleets together and some started to pay money to pirate to stop the attacks against them.\nAt AC 2240 134 colonies started the Federation of liberty. The older federation didn’t like that at all. They saw our glory federation as a simple pack of pirates. So they told us to dissolve our federation or they were going to bombard all of us. The big problem was that the pirates started attacked to divide us. They understand that the Federation of liberty were going to pirate business much harder.\nThen the big war started in 2242 we were under heavy attacks from pirates, other federation and countries. Son after that the attacks from pirates just stopped and instead they started to attack our attacker from other federations and countries. The pirates contact us and offer us military protection towards food and fuel. We answer them that they could join our glory federation or die. That was a success and from that day Federation of liberty is a powerful nation to respect.");
        tempFaction.setCanReconstruct(true);
        tempFaction.setReconstructCostBase(10);
        tempFaction.setAlignment(federation);
        tempFaction.setCorruption(tmpCorruption);
        gw.addFaction(tempFaction);
    	
        
       return gw;
	}

}
