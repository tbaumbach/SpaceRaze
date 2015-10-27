package sr.server.gameworlds;

import sr.enums.BlackMarketFrequency;
import sr.enums.SpaceshipRange;
import sr.enums.SpaceshipTargetingType;
import sr.world.Alignment;
import sr.world.Alignments;
import sr.world.Faction;
import sr.world.GameWorld;
import sr.world.SpaceStation;
import sr.world.SpaceshipType;
import sr.world.UniqueIdCounter;
import sr.world.VIPType;

@SuppressWarnings("unused")
public class StarWars{

	public static GameWorld getGameWorld(){
		// XXX Star Wars
		GameWorld gw = new GameWorld();

//    gw.setCumulativeBombardment(false);

    gw.setFileName("sw");

    gw.setFullName("Starwars");
    gw.setDescription("Starwars Inspired Universe! Play either of four factions Empire, Alliance, Trade Federation or Galactic Republic");

    gw.setBattleSimDefaultShips1("");
    gw.setBattleSimDefaultShips2("");

    gw.setCreatedDate("2005-11-28");
	gw.setChangedDate("2006-03-29");
	gw.setCreatedByUser("niohl");

	
	Alignments alignments = new Alignments();
	String sEmpire	= "Empire";
	String sAlliance = "Alliance";
	String sTrade	= "Trade";
	String sOldRepublic = "Old Republic"; //Små skepp
//	String sAliens = "Aliens"; //look as Civilian
	
	//VIP
	String sNeutral	= "Neutral";
	String sEvil	= "Evil";
	String sGood	= "Good";
	String sJedi	= "Jedi";
	String sBountyHunter	= "BountyHunter";
	
	alignments.add(sEmpire);
	alignments.add(sAlliance);
	alignments.add(sTrade);
	alignments.add(sOldRepublic);
//	alignments.add(sAliens);
	
	//VIP
	alignments.add(sNeutral);
	alignments.add(sEvil);
	alignments.add(sGood);
	alignments.add(sJedi);
	
	Alignment aEmpire = alignments.findAlignment(sEmpire);
	Alignment aAlliance = alignments.findAlignment(sAlliance);
	Alignment aTrade = alignments.findAlignment(sTrade);
//	Alignment aAliens = alignments.findAlignment(sAliens);
	Alignment aOldRepublic = alignments.findAlignment(sOldRepublic);
	
//	VIP
	Alignment aNeutral 	= alignments.findAlignment(sNeutral);
	Alignment aEvil 	= alignments.findAlignment(sEvil);
	Alignment aGood		= alignments.findAlignment(sGood);
	Alignment aJedi	= alignments.findAlignment(sJedi);
	
	aEmpire.addCanHaveVip(aEvil.getName());
	aEmpire.addCanHaveVip(aJedi.getName());
	aEmpire.addCanHaveVip(aNeutral.getName());
	
	
	aTrade.addCanHaveVip(aNeutral.getName());
	aTrade.addCanHaveVip(aJedi.getName());
	
	aAlliance.addCanHaveVip(aGood.getName());
	aAlliance.addCanHaveVip(aJedi.getName());
	aAlliance.addCanHaveVip(aNeutral.getName());
	
	//aAliens.addCanHaveVip(aEvil);
	//aAliens.addCanHaveVip(aNeutral);
	
	aOldRepublic.addCanHaveVip(aGood.getName());
	aOldRepublic.addCanHaveVip(aJedi.getName());
	aOldRepublic.addCanHaveVip(aNeutral.getName());

	// Spaceship types
    UniqueIdCounter uniqueShipIdCounter = new UniqueIdCounter();

    SpaceshipType tempsst = null;
    
//  ######## ( ) ########
    /*                                                                                                                                          
Guargantuan 			Huge Battle Destroyer
Berserker Cruiser		Large Battlecruiser
Venator Class V			Carrier-Battleship Large 
Protector II			Hangar Medium improved AA
Protector I				Medium Cruiser
Meteor					Medium Missile Cruiser                                                                                                
Ion Frigate				Medium Lancer Expensive                                                                                                  
Reliant	Carrier			Medium carrier
Reliant Troopship		Medium Troops                                           
Sulanko Frigate			Poor Hull Small                                                                                                                                                
 */                                                                                                                                        
//Large                                                                                                                                               
//Berserker Cruiser  //                                                                                                                          
tempsst = new SpaceshipType("Berserker Cruiser","BCR",SpaceshipType.SIZE_LARGE,450,1000,SpaceshipRange.LONG,15,40,uniqueShipIdCounter,40,10);     
tempsst.setDescription("Galactic Republic, Multicapable battleship ");                                                                             
tempsst.setWeaponsStrengthMedium(20);                                                                                                             
tempsst.setArmorSmall(95);                                                                                                                        
tempsst.setArmorMedium(50);                                                                                                                       
tempsst.setWeaponsStrengthLarge(35);                                                                                                              
tempsst.setWeaponsStrengthHuge(30);                                                                                                               
tempsst.setBombardment(3);                                                                                                                        
tempsst.setPsychWarfare(1);                                                                                                                          
tempsst.setIncreaseInitiative(3);                                                                                                                 
tempsst.setSquadronCapacity(3);                                                                                                                   
tempsst.setPlanetarySurvey(true);                                                                                                                 
tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);                                                                                                  
gw.addShipType(tempsst);                                                                                                                          
                                                                                                                                         
//Venator Class V  //                                                                                                                          
tempsst = new SpaceshipType("Venator Class V","VCV",SpaceshipType.SIZE_LARGE,600,1200,SpaceshipRange.LONG,15,40,uniqueShipIdCounter,40,10);     
tempsst.setDescription("Galactic Republic, Multicapable battleship ");                                                                             
tempsst.setWeaponsStrengthMedium(20);                                                                                                             
tempsst.setArmorSmall(95);                                                                                                                        
tempsst.setArmorMedium(50);                                                                                                                       
tempsst.setWeaponsStrengthLarge(35);                                                                                                              
tempsst.setWeaponsStrengthHuge(30);       

tempsst.setWeaponsMaxSalvoesMedium(40);
tempsst.setWeaponsMaxSalvoesLarge(30);
tempsst.setWeaponsMaxSalvoesHuge(20);

tempsst.setBombardment(3);                                                                                                                                                         
tempsst.setPsychWarfare(1);
tempsst.setIncreaseInitiative(3);                                                                                                                 
tempsst.setSquadronCapacity(3);                                                                                                                   
tempsst.setPlanetarySurvey(true);                                                                                                                 
tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);                                                                                                  
gw.addShipType(tempsst);                                                                                                                                                
                                                                                                                                         
                                                                                                                                         
//HUGE                                                                                                                                                
//Guargantuan  //                                                                                                                                  
tempsst = new SpaceshipType("Guargantuan","Guar",SpaceshipType.SIZE_HUGE,450,2000,SpaceshipRange.SHORT,20,65,uniqueShipIdCounter,40,15);          
tempsst.setDescription("Galactic Republic, Huge Battleship");                                                  
tempsst.setWeaponsStrengthMedium(60);                                                                                                             
tempsst.setCanAppearOnBlackMarket(false);                                                                                                         
tempsst.setArmorSmall(95);                                                                                                                        
tempsst.setWeaponsStrengthLarge(120);                                                                                                             
tempsst.setWeaponsStrengthHuge(70);      
tempsst.setWeaponsMaxSalvoesMedium(40);
tempsst.setWeaponsMaxSalvoesLarge(30);
tempsst.setWeaponsMaxSalvoesHuge(20);

tempsst.setBombardment(3);                                                                                                                        
tempsst.setPsychWarfare(1);                                                                                                                          
tempsst.setNoRetreat(true);                                                                                                                       
tempsst.setIncreaseInitiative(5);                                                                                                                 
tempsst.setSquadronCapacity(10);                                                                                                                   
tempsst.setPlanetarySurvey(true);                                                                                                                 
tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);                                                                                                  
gw.addShipType(tempsst);                                                                                                                          

                                                                                                                                         
//Medium                                                                                                                                              
//Reliant Carrier  //                                                                                                                                
tempsst = new SpaceshipType("Reliant Carrier","ReC",SpaceshipType.SIZE_MEDIUM,10,180,SpaceshipRange.LONG,2,5,uniqueShipIdCounter,5,5);               
tempsst.setDescription("Galactic Republic, Merchant ship rebuilt to carrier");                                                                     
tempsst.setSquadronCapacity(4);                                                                                                                   
tempsst.setCanAppearOnBlackMarket(true);                                                                                                          
tempsst.setScreened(true);
tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);                                                                                                 
gw.addShipType(tempsst);                                                                                                                          
                                                                                                                                         
//Meteor  //                                                                                                                        
tempsst = new SpaceshipType("Meteor","Met",SpaceshipType.SIZE_MEDIUM,50,200,SpaceshipRange.LONG,5,10,uniqueShipIdCounter,10,10);   
tempsst.setDescription("Galactic Republic, Bombardment Cruiser. Against medium ships");                                                            
tempsst.setWeaponsStrengthLarge(120);
tempsst.setWeaponsMaxSalvoesLarge(3);
tempsst.setCanAppearOnBlackMarket(true);                                                                                                          
tempsst.setArmorSmall(50);                                                                                                                        
tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);                                                                                                  
gw.addShipType(tempsst);                                                                                                                          

//Ion Frigate  //                                                                                                                          
tempsst = new SpaceshipType("Ion Frigate","Ion",SpaceshipType.SIZE_MEDIUM,50,350,SpaceshipRange.LONG,6,12,uniqueShipIdCounter,40,40);     
tempsst.setDescription("Galactic Republic, Anti-air Defence cruiser");                                                                             
tempsst.setArmorSmall(50);                                                                                                                        
tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);                                                                                                  
gw.addShipType(tempsst);       

//Reliant Troopship  //                                                                                                                          
tempsst = new SpaceshipType("Reliant Troopship","Rel",SpaceshipType.SIZE_MEDIUM,100,500,SpaceshipRange.LONG,6,12,uniqueShipIdCounter,15,15);     
tempsst.setDescription("Galactic Republic, TroopShip");                                                                             
tempsst.setArmorSmall(50);
tempsst.setBombardment(1);
tempsst.setPsychWarfare(1);  
tempsst.setScreened(true);
tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);                                                                                                  
gw.addShipType(tempsst);                                                                                                                          
                                                                                                                                         
//Protector I  //                                                                                                                     
tempsst = new SpaceshipType("Protector I","PI",SpaceshipType.SIZE_MEDIUM,100,400,SpaceshipRange.LONG,5,11,uniqueShipIdCounter,10,15);
tempsst.setDescription("Galactic Republic, Against medium ships");                                                          
tempsst.setWeaponsStrengthMedium(50);                                                                                                             
tempsst.setWeaponsMaxSalvoesMedium(3);
tempsst.setArmorSmall(50);                                                                                                                        
tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);                                                                                                  
gw.addShipType(tempsst);                                                                                                                          
                                                                                                                                         
//Protector II  //                                                                                                                           
tempsst = new SpaceshipType("Protector II","PII",SpaceshipType.SIZE_MEDIUM,150,600,SpaceshipRange.LONG,7,13,uniqueShipIdCounter,25,15);      
tempsst.setDescription("Galactic Republic, Second Generation Cruiser");                                                                  
tempsst.setCanAppearOnBlackMarket(true);                                                                                                          
tempsst.setWeaponsStrengthMedium(25); 
tempsst.setWeaponsMaxSalvoesMedium(4);
tempsst.setWeaponsStrengthLarge(20);  
tempsst.setWeaponsMaxSalvoesLarge(4);
tempsst.setSquadronCapacity(1);                                                                                                                   
tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);                                                                                                  
gw.addShipType(tempsst);                                                                                                                          
                                                                                                                                         
                                                                                                                                         
//SMALL                                                                                                                                               

//Sulanko Frigate  //                                                                                                                           
tempsst = new SpaceshipType("Sulanko Frigate","Suk",SpaceshipType.SIZE_SMALL,50,50,SpaceshipRange.LONG,1,3,uniqueShipIdCounter,20,20);         
tempsst.setDescription("Galactic Republic, Small allround Frigate");                                                                               
tempsst.setWeaponsStrengthMedium(10);
tempsst.setWeaponsMaxSalvoesMedium(10);

tempsst.setCanAppearOnBlackMarket(true);
tempsst.setPlanetarySurvey(true); 
tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
gw.addShipType(tempsst);
    

//######## ( ) ########
//Figters
//VF  //
tempsst = new SpaceshipType("V-Fighter","VF",SpaceshipType.SIZE_SMALL,15,20,SpaceshipRange.NONE,1,1,uniqueShipIdCounter,10,12);
tempsst.setDescription("Galactic Republic, First generation fighter");
tempsst.setSquadron(true);
tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
gw.addShipType(tempsst);

//N1  //
tempsst = new SpaceshipType("Naboo Starfighter N-1","N1",SpaceshipType.SIZE_SMALL,20,35,SpaceshipRange.LONG,2,2,uniqueShipIdCounter,10,24);
tempsst.setDescription("Galactic Republic, Second genration fighter");
tempsst.setSquadron(true);
tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
gw.addShipType(tempsst);

//B5  //
tempsst = new SpaceshipType("B5-Bomber","B5",SpaceshipType.SIZE_MEDIUM,10,60,SpaceshipRange.LONG,3,4,uniqueShipIdCounter,25,10);
tempsst.setDescription("Galactic Republic, First Generation bomber");
tempsst.setWeaponsStrengthMedium(20);
tempsst.setSquadron(true);
tempsst.setWeaponsMaxSalvoesMedium(3);
tempsst.setWeaponsStrengthLarge(15);
tempsst.setWeaponsMaxSalvoesLarge(3);
tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
gw.addShipType(tempsst);

//B1  //
tempsst = new SpaceshipType("B1-Bomber","B1",SpaceshipType.SIZE_SMALL,10,40,SpaceshipRange.NONE,2,2,uniqueShipIdCounter,25,15);
tempsst.setDescription("Trade Federation Second genration bomber");
tempsst.setWeaponsStrengthMedium(15);
tempsst.setSquadron(true);
tempsst.setWeaponsMaxSalvoesMedium(3);
tempsst.setWeaponsStrengthLarge(15);
tempsst.setWeaponsMaxSalvoesLarge(3);
tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
gw.addShipType(tempsst);


//######## ( ) ########
//  Figters
    //DF-x10  //
    tempsst = new SpaceshipType("DF-x10","F10",SpaceshipType.SIZE_SMALL,15,15,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,10,12);
    tempsst.setDescription("Trade Federation First generation fighter");
    tempsst.setSquadron(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

    //DF-x14  //
    tempsst = new SpaceshipType("DF-x14","F14",SpaceshipType.SIZE_MEDIUM,25,20,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,10,25);
    tempsst.setDescription("Trade Federation Second genration fighter");
    tempsst.setSquadron(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

    //DB-y44  //
    tempsst = new SpaceshipType("DB-y44","B44",SpaceshipType.SIZE_MEDIUM,15,60,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,25,10);
    tempsst.setDescription("Trade Federation First Generation bomber");
    tempsst.setWeaponsStrengthMedium(25);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(2);
    tempsst.setWeaponsStrengthLarge(10);
    tempsst.setWeaponsMaxSalvoesLarge(2);
    tempsst.setIncreaseInitiative(1);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //DB-y27  //
    tempsst = new SpaceshipType("DB-y27","B27",SpaceshipType.SIZE_SMALL,15,20,SpaceshipRange.NONE,2,3,uniqueShipIdCounter,25,10);
    tempsst.setDescription("Trade Federation Second genration bomber");
    tempsst.setWeaponsStrengthMedium(10);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(2);
    tempsst.setWeaponsStrengthLarge(10);
    tempsst.setWeaponsMaxSalvoesLarge(2);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    
    
//  ######## ( ) ########   
//Large
    //Droid control ship  //
    tempsst = new SpaceshipType("Droid control ship","DCS",SpaceshipType.SIZE_LARGE,900,800,SpaceshipRange.LONG,15,45,uniqueShipIdCounter,40,10);
    tempsst.setDescription("Trade Federation, Command ship increases initiative");
    tempsst.setWeaponsStrengthMedium(20);
    tempsst.setArmorSmall(55);
    tempsst.setArmorMedium(50);
    tempsst.setWeaponsStrengthLarge(25);
    tempsst.setWeaponsStrengthHuge(20);
    tempsst.setWeaponsMaxSalvoesMedium(40);
    tempsst.setWeaponsMaxSalvoesLarge(30);
    tempsst.setWeaponsMaxSalvoesHuge(20);

    tempsst.setBombardment(3);
    tempsst.setPsychWarfare(1);
    tempsst.setIncreaseInitiative(9);
    tempsst.setSquadronCapacity(3);
    tempsst.setPlanetarySurvey(true);        
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    tempsst = new SpaceshipType("Droid Battle ship","DBS",SpaceshipType.SIZE_LARGE,900,800,SpaceshipRange.LONG,15,45,uniqueShipIdCounter,40,10);
    tempsst.setDescription("Trade Federation, Multicapable battleship ");
    tempsst.setWeaponsStrengthMedium(20);
    tempsst.setArmorSmall(95);
    tempsst.setArmorMedium(50);
    tempsst.setWeaponsStrengthLarge(35);
    tempsst.setWeaponsStrengthHuge(30);
    tempsst.setWeaponsMaxSalvoesMedium(40);
    tempsst.setWeaponsMaxSalvoesLarge(30);
    tempsst.setWeaponsMaxSalvoesHuge(20);

    tempsst.setSquadronCapacity(3);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    
    
//  ######## ( ) ########
//HUGE
    //Battlestar  //
    tempsst = new SpaceshipType("Battlestar","BaS",SpaceshipType.SIZE_HUGE,900,3000,SpaceshipRange.SHORT,45,115,uniqueShipIdCounter,40,15);
    tempsst.setDescription("Trade Federation, First generation Death star. Superior to most ships");
    tempsst.setWeaponsStrengthMedium(60);
    tempsst.setCanAppearOnBlackMarket(false);
    tempsst.setArmorSmall(95);
    tempsst.setWeaponsStrengthLarge(120);
    tempsst.setWeaponsStrengthHuge(150);
    tempsst.setWeaponsMaxSalvoesMedium(40);
    tempsst.setWeaponsMaxSalvoesLarge(30);
    tempsst.setWeaponsMaxSalvoesHuge(20);

    tempsst.setBombardment(3);
    tempsst.setPsychWarfare(1);
    tempsst.setNoRetreat(true);
    tempsst.setIncreaseInitiative(5);
    tempsst.setSquadronCapacity(5);
    tempsst.setPlanetarySurvey(true);        
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);
//  ######## ( ) ########
//Medium
    //Fast Carrier  //
    tempsst = new SpaceshipType("Fast Carrier","FCa",SpaceshipType.SIZE_MEDIUM,10,300,SpaceshipRange.LONG,3,7,uniqueShipIdCounter,5,5);
    tempsst.setDescription("Trade Federation, Merchant ship rebuilt to carrier");
    tempsst.setSquadronCapacity(6);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setScreened(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Improved Dreadnought  //
    tempsst = new SpaceshipType("Improved Dreadnought","iDrd",SpaceshipType.SIZE_MEDIUM,50,400,SpaceshipRange.LONG,8,12,uniqueShipIdCounter,20,10);
    tempsst.setDescription("Trade Federation, Bombardment Cruiser. Against medium ships");
    tempsst.setWeaponsStrengthMedium(30);
    tempsst.setWeaponsStrengthLarge(10);
    tempsst.setWeaponsMaxSalvoesMedium(20);
    tempsst.setWeaponsMaxSalvoesLarge(20);

    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setArmorSmall(50);
    tempsst.setBombardment(1);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Acclamator cruiser  //
    tempsst = new SpaceshipType("Acclamator cruiser","Acc",SpaceshipType.SIZE_MEDIUM,100,400,SpaceshipRange.LONG,8,15,uniqueShipIdCounter,40,40);
    tempsst.setDescription("Trade Federation, Anti-air Defence cruiser");
    tempsst.setWeaponsStrengthMedium(10);
    tempsst.setWeaponsMaxSalvoesMedium(20);
    tempsst.setArmorSmall(50);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

    //Broadside-class cruiser  //
    tempsst = new SpaceshipType("Broadside Class cruiser","BCC",SpaceshipType.SIZE_MEDIUM,100,400,SpaceshipRange.LONG,9,17,uniqueShipIdCounter,15,15);
    tempsst.setDescription("Trade Federation, Troopship and Cruiser. Against larger ships");
    tempsst.setWeaponsStrengthMedium(10);
    tempsst.setWeaponsStrengthLarge(30);
    tempsst.setWeaponsMaxSalvoesMedium(20);
    tempsst.setWeaponsMaxSalvoesLarge(20);
    tempsst.setArmorSmall(50);
    tempsst.setPsychWarfare(1);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Sullustan cruiser  //
    tempsst = new SpaceshipType("Sullustan Cruiser","Sul",SpaceshipType.SIZE_MEDIUM,150,500,SpaceshipRange.LONG,10,19,uniqueShipIdCounter,15,15);
    tempsst.setDescription("Trade Federation, Allround cruiser with planet survey");
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setWeaponsStrengthMedium(15);
    tempsst.setWeaponsStrengthLarge(15);
    tempsst.setWeaponsMaxSalvoesMedium(20);
    tempsst.setWeaponsMaxSalvoesLarge(20);
    tempsst.setIncreaseInitiative(1);
    tempsst.setSquadronCapacity(2);
    tempsst.setPlanetarySurvey(true);        
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

//  ######## ( ) ########
//SMALL
    
    //Free Starrunner  //
    tempsst = new SpaceshipType("Free Starrunner","FrS",SpaceshipType.SIZE_SMALL,30,30,SpaceshipRange.LONG,1,10,uniqueShipIdCounter,10,10);
    tempsst.setDescription("Trade Federation, Smugler, Trader, pirate earns money on enemy planets");
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setIncEnemyOpenBonus(3);
    tempsst.setIncEnemyClosedBonus(2);
    gw.addShipType(tempsst);

    //Sullustan Frigate  //
    tempsst = new SpaceshipType("Sullustan Frigate","Suf",SpaceshipType.SIZE_SMALL,50,100,SpaceshipRange.LONG,3,9,uniqueShipIdCounter,20,15);
    tempsst.setDescription("Trade Federation, Small allround Frigate");
    tempsst.setWeaponsStrengthMedium(10);
    tempsst.setWeaponsMaxSalvoesMedium(20);
    tempsst.setSquadronCapacity(1);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);
    
//  ######## ( ) ########
//////////////////////////////////////Rebel Capital ships//////////////////////////////////////////////////////  
    
    //Millenium Falcon  //
    tempsst = new SpaceshipType("Millenium Falcon","Mil",SpaceshipType.SIZE_SMALL,30,30,SpaceshipRange.LONG,1,1,uniqueShipIdCounter,10,10);
    tempsst.setDescription("Rebel, Planet survey and smuggler ship earns money on enemy planets");
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setIncEnemyOpenBonus(1);
    tempsst.setIncEnemyClosedBonus(1);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    tempsst.setPlanetarySurvey(true);
    gw.addShipType(tempsst);

    //Corellian Gunship  //
    tempsst = new SpaceshipType("Corellian Gunship","CrG",SpaceshipType.SIZE_SMALL,50,120,SpaceshipRange.LONG,2,5,uniqueShipIdCounter,25,20);
    tempsst.setDescription("Rebel, Small Anti-air corvette");
    tempsst.setWeaponsStrengthMedium(5);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Dreadnaught  //
    tempsst = new SpaceshipType("Dreadnaught","Drd",SpaceshipType.SIZE_MEDIUM,50,300,SpaceshipRange.LONG,5,9,uniqueShipIdCounter,20,10);
    tempsst.setDescription("Rebel, Cruiser with bombardment capabilities. Medium - Large targets");
    tempsst.setWeaponsStrengthMedium(20);
    tempsst.setWeaponsStrengthLarge(15);
    tempsst.setBombardment(1);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setArmorSmall(50);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Nebulon-B Frigate  //
    tempsst = new SpaceshipType("Nebulon-B Frigate","NebB",SpaceshipType.SIZE_MEDIUM,100,400,SpaceshipRange.LONG,6,12,uniqueShipIdCounter,20,30);
    tempsst.setDescription("Rebel, Medium Anti-air Cruiser, with carrier capabilities");
    tempsst.setWeaponsStrengthMedium(10);
    tempsst.setWeaponsMaxSalvoesMedium(40);
    tempsst.setArmorSmall(50);
    tempsst.setSquadronCapacity(1);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

    //Assault frigate  //
    tempsst = new SpaceshipType("Assault frigate","AsF",SpaceshipType.SIZE_MEDIUM,100,400,SpaceshipRange.LONG,7,14,uniqueShipIdCounter,15,15);
    tempsst.setDescription("Rebel, Planet Besiege ship. Troops and specialised equipment takes down 2 resistance per turn");
    tempsst.setWeaponsStrengthMedium(10);
    tempsst.setWeaponsStrengthLarge(30);
    tempsst.setWeaponsMaxSalvoesMedium(20);
    tempsst.setWeaponsMaxSalvoesLarge(20);
    tempsst.setArmorSmall(50);
    tempsst.setPsychWarfare(2);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Liberator Cruiser  //
    tempsst = new SpaceshipType("Liberator Cruiser","Lib",SpaceshipType.SIZE_MEDIUM,150,500,SpaceshipRange.LONG,8,16,uniqueShipIdCounter,15,15);
    tempsst.setDescription("Rebel, Carrier specialised against medium ships an planet survey");
    tempsst.setWeaponsStrengthMedium(30);
    tempsst.setWeaponsMaxSalvoesMedium(20);
    
    tempsst.setArmorSmall(50);
    tempsst.setIncreaseInitiative(1);
    tempsst.setSquadronCapacity(3);
    tempsst.setPlanetarySurvey(true);        
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Mon Calamari Cruiser  //
    tempsst = new SpaceshipType("Mon Calamari Cruiser","MCC",SpaceshipType.SIZE_LARGE,300,1100,SpaceshipRange.SHORT,12,30,uniqueShipIdCounter,25,15);
    tempsst.setDescription("Rebel, The cornerstone against Medium - Large ships");
    tempsst.setWeaponsStrengthMedium(30);
    tempsst.setArmorSmall(85);
    tempsst.setArmorMedium(50);
    tempsst.setWeaponsStrengthLarge(30);
    tempsst.setWeaponsStrengthHuge(10);
    tempsst.setWeaponsMaxSalvoesMedium(20);
    tempsst.setWeaponsMaxSalvoesLarge(20);
    tempsst.setWeaponsMaxSalvoesHuge(10);
    
    tempsst.setBombardment(2);
    tempsst.setPsychWarfare(1);
    tempsst.setIncreaseInitiative(3);
    tempsst.setSquadronCapacity(2);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);
    
    //Dauntless Cruiser  //
    tempsst = new SpaceshipType("Dauntless Cruiser","DaC",SpaceshipType.SIZE_LARGE,600,2200,SpaceshipRange.SHORT,22,50,uniqueShipIdCounter,25,20);
    tempsst.setDescription("Rebel, Developed to meet the threat from ISD and SSD");
    tempsst.setCanAppearOnBlackMarket(false);
    tempsst.setWeaponsStrengthMedium(20);
    tempsst.setArmorSmall(85);
    tempsst.setArmorMedium(50);
    tempsst.setWeaponsStrengthLarge(30);
    tempsst.setWeaponsStrengthHuge(35);
    tempsst.setWeaponsMaxSalvoesMedium(30);
    tempsst.setWeaponsMaxSalvoesLarge(20);
    tempsst.setWeaponsMaxSalvoesHuge(10);
    tempsst.setBombardment(3);
    tempsst.setPsychWarfare(1);
    tempsst.setIncreaseInitiative(5);
    tempsst.setSquadronCapacity(4);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Bulwark Battlecruiser  //
    tempsst = new SpaceshipType("Bulwark Battlecruiser","BBC",SpaceshipType.SIZE_HUGE,900,3300,SpaceshipRange.SHORT,28,75,uniqueShipIdCounter,40,15);
    tempsst.setDescription("Especially Developed to meet SSD and Huge Ships");
    tempsst.setCanAppearOnBlackMarket(false);
    tempsst.setArmorSmall(95);
    tempsst.setArmorMedium(50);
    tempsst.setWeaponsStrengthMedium(30);
    tempsst.setWeaponsStrengthLarge(30);
    tempsst.setWeaponsStrengthHuge(50);
    tempsst.setWeaponsMaxSalvoesMedium(30);
    tempsst.setWeaponsMaxSalvoesLarge(20);
    tempsst.setWeaponsMaxSalvoesHuge(10);
    tempsst.setBombardment(4);
    tempsst.setPsychWarfare(1);
    tempsst.setIncreaseInitiative(10);
    tempsst.setSquadronCapacity(8);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

//  ######## ( Imperial Capital ships) ########
        
    //Lambda Shuttle  //
    tempsst = new SpaceshipType("Lambda Shuttle","Shu",SpaceshipType.SIZE_SMALL,30,30,SpaceshipRange.LONG,1,1,uniqueShipIdCounter,5,5);
    tempsst.setDescription("Imperial, Small transport ship");
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    tempsst.setScreened(true);
    gw.addShipType(tempsst);

    //Lancer Frigate  //
    tempsst = new SpaceshipType("Lancer Frigate","Lan",SpaceshipType.SIZE_SMALL,90,150,SpaceshipRange.LONG,3,6,uniqueShipIdCounter,15,40);
    tempsst.setDescription("Imperial, Anti-air Frigate");
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

    //Carrack Light Cruiser  //
    tempsst = new SpaceshipType("Carrack Light Cruiser","Car",SpaceshipType.SIZE_SMALL,90,100,SpaceshipRange.LONG,2,6,uniqueShipIdCounter,20,10);
    tempsst.setDescription("Imperial, Small allround ship that can bombardment");
    tempsst.setWeaponsStrengthMedium(10);
    tempsst.setWeaponsStrengthLarge(10);
    tempsst.setWeaponsMaxSalvoesMedium(10);
    tempsst.setWeaponsMaxSalvoesLarge(10);    
    tempsst.setBombardment(1);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Nebulon-A Frigate  //
    tempsst = new SpaceshipType("Nebulon-A Frigate","NebA",SpaceshipType.SIZE_MEDIUM,100,400,SpaceshipRange.LONG,5,8,uniqueShipIdCounter,15,20);
    tempsst.setDescription("Imperial, Deigned to meet medium ship. Small Squadron capacity");
    tempsst.setWeaponsStrengthMedium(20);
    tempsst.setArmorSmall(50);
    tempsst.setWeaponsStrengthLarge(10);
    tempsst.setWeaponsStrengthHuge(10);
    tempsst.setWeaponsMaxSalvoesMedium(20);
    tempsst.setWeaponsMaxSalvoesLarge(20);
    tempsst.setWeaponsMaxSalvoesHuge(10);
    tempsst.setSquadronCapacity(1);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Interdictor Cruiser  //
    tempsst = new SpaceshipType("Interdictor Cruiser","Int",SpaceshipType.SIZE_MEDIUM,150,400,SpaceshipRange.LONG,5,12,uniqueShipIdCounter,10,10);
    tempsst.setDescription("Imperial, With giant tractor beams it stops other ships to hyperjump and Improved besiege of planets");
    tempsst.setWeaponsStrengthMedium(20);
    tempsst.setArmorSmall(50);
    tempsst.setWeaponsStrengthLarge(10);
    tempsst.setPsychWarfare(1);
    tempsst.setWeaponsMaxSalvoesMedium(20);
    tempsst.setWeaponsMaxSalvoesLarge(20);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setNoRetreat(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Victory Destroyer  //
    tempsst = new SpaceshipType("Victory Destroyer","VSD",SpaceshipType.SIZE_LARGE,200,900,SpaceshipRange.SHORT,9,20,uniqueShipIdCounter,15,10);
    tempsst.setDescription("Imperial, Once the largest ship in the universe designed to attack Medium ships");
    tempsst.setWeaponsStrengthMedium(30);
    tempsst.setArmorSmall(85);
    tempsst.setArmorMedium(50);
    tempsst.setWeaponsStrengthLarge(15);
    tempsst.setWeaponsMaxSalvoesMedium(30);
    tempsst.setWeaponsMaxSalvoesLarge(20);
    tempsst.setBombardment(1);
    tempsst.setPsychWarfare(1);
    tempsst.setIncreaseInitiative(1);
    tempsst.setSquadronCapacity(2);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);
 
    //Victory II Star Destroyer  //
    tempsst = new SpaceshipType("Victory II Star Destroyer","VSD2",SpaceshipType.SIZE_LARGE,225,950,SpaceshipRange.SHORT,10,25,uniqueShipIdCounter,30,15);
    tempsst.setDescription("Imperial, Improved verison with better capabilities agianst large ships");
    tempsst.setWeaponsStrengthMedium(25);
    tempsst.setArmorSmall(85);
    tempsst.setArmorMedium(50);
    tempsst.setWeaponsStrengthLarge(35);
    tempsst.setWeaponsStrengthHuge(10);
    tempsst.setWeaponsMaxSalvoesMedium(30);
    tempsst.setWeaponsMaxSalvoesLarge(20);
    tempsst.setWeaponsMaxSalvoesHuge(10);
    
    tempsst.setBombardment(2);
    tempsst.setPsychWarfare(1);
    tempsst.setCanAppearOnBlackMarket(false);
    tempsst.setIncreaseInitiative(2);
    tempsst.setSquadronCapacity(2);
    tempsst.setPlanetarySurvey(true);        
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Imperial Star Destroyer  //
    tempsst = new SpaceshipType("Imperial Star Destroyer","ISD",SpaceshipType.SIZE_LARGE,400,1300,SpaceshipRange.SHORT,14,40,uniqueShipIdCounter,40,10);
    tempsst.setDescription("Imperial, Excellent Against Large and huge ships");
    tempsst.setWeaponsStrengthMedium(20);
    tempsst.setArmorSmall(85);
    tempsst.setArmorMedium(50);
    tempsst.setWeaponsStrengthLarge(35);
    tempsst.setWeaponsStrengthHuge(40);
    tempsst.setWeaponsMaxSalvoesMedium(40);
    tempsst.setWeaponsMaxSalvoesLarge(30);
    tempsst.setWeaponsMaxSalvoesHuge(20);
    tempsst.setCanAppearOnBlackMarket(false);
    tempsst.setBombardment(3);
    tempsst.setPsychWarfare(1);
    tempsst.setIncreaseInitiative(3);
    tempsst.setSquadronCapacity(3);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Imperial Star Destroyer II  //
    tempsst = new SpaceshipType("Imperial Star Destroyer II","ISD2",SpaceshipType.SIZE_LARGE,450,1500,SpaceshipRange.SHORT,16,50,uniqueShipIdCounter,25,15);
    tempsst.setDescription("Imperial, Best against Large and huge ships");
    tempsst.setWeaponsStrengthMedium(20);
    tempsst.setCanAppearOnBlackMarket(false);
    tempsst.setArmorSmall(95);
    tempsst.setArmorMedium(50);
    tempsst.setWeaponsStrengthLarge(50);
    tempsst.setWeaponsStrengthHuge(70);
    tempsst.setWeaponsMaxSalvoesMedium(40);
    tempsst.setWeaponsMaxSalvoesLarge(30);
    tempsst.setWeaponsMaxSalvoesHuge(20);
    tempsst.setBombardment(3);
    tempsst.setPsychWarfare(1);
    tempsst.setIncreaseInitiative(5);
    tempsst.setSquadronCapacity(3);
    tempsst.setPlanetarySurvey(true);        
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Super Star Destroyer  //
    tempsst = new SpaceshipType("Super Star Destroyer","SSD",SpaceshipType.SIZE_HUGE,700,4500,SpaceshipRange.SHORT,30,100,uniqueShipIdCounter,15,10);
    tempsst.setDescription("Imperial, The best ship against Huge ships");
    tempsst.setWeaponsStrengthMedium(20);
    tempsst.setArmorSmall(95);
    tempsst.setArmorMedium(50);
    tempsst.setWeaponsStrengthLarge(100);
    tempsst.setCanAppearOnBlackMarket(false);
    tempsst.setWeaponsStrengthHuge(90);
    tempsst.setWeaponsMaxSalvoesMedium(40);
    tempsst.setWeaponsMaxSalvoesLarge(30);
    tempsst.setWeaponsMaxSalvoesHuge(20);
    tempsst.setBombardment(4);
    tempsst.setPsychWarfare(1);
    tempsst.setIncreaseInitiative(15);
    tempsst.setSquadronCapacity(10);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Death Star  //
    tempsst = new SpaceshipType("Death Star","DS",SpaceshipType.SIZE_HUGE,5000,10000,SpaceshipRange.SHORT,80,500,uniqueShipIdCounter,20,10);
    tempsst.setDescription("Imperial, DEATH STAR the name says it all!");
    tempsst.setWeaponsStrengthMedium(50);
    tempsst.setWeaponsStrengthLarge(5000);
    tempsst.setWeaponsStrengthHuge(5000);
    tempsst.setWeaponsMaxSalvoesMedium(100);
    tempsst.setWeaponsMaxSalvoesLarge(20);
    tempsst.setWeaponsMaxSalvoesHuge(20);
    tempsst.setBombardment(30);
    tempsst.setArmorSmall(0);
    tempsst.setPsychWarfare(1);
    tempsst.setNoRetreat(true);    
    tempsst.setIncreaseInitiative(20);
    tempsst.setSquadronCapacity(24);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

//  ######## ( Rebel Fighter ) ########
    
    //Z-95  //
    tempsst = new SpaceshipType("Z-95","Z95",SpaceshipType.SIZE_SMALL,0,40,SpaceshipRange.NONE,1,1,uniqueShipIdCounter,10,15);
    tempsst.setDescription("Cheap Efficent Fighter");
    tempsst.setWeaponsStrengthMedium(5);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(1);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

    //X-Wing  //
    tempsst = new SpaceshipType("X-Wing","X-w",SpaceshipType.SIZE_SMALL,20,40,SpaceshipRange.LONG,2,3,uniqueShipIdCounter,15,25);
    tempsst.setDescription("Rebels, Second generation fighter");
    tempsst.setWeaponsStrengthMedium(5);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(2);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

    //E-Wing  //
    tempsst = new SpaceshipType("E-Wing","E-w",SpaceshipType.SIZE_MEDIUM,20,50,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,15,30);
    tempsst.setDescription("Rebels, Third generation fighter");
    tempsst.setWeaponsStrengthMedium(5);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(4);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

    //A-Wing  //
    tempsst = new SpaceshipType("A-Wing","A-w",SpaceshipType.SIZE_SMALL,15,25,SpaceshipRange.LONG,1,2,uniqueShipIdCounter,10,20);
    tempsst.setDescription("Rebels, Cheap fighter with an Hyperdrive");
    tempsst.setSquadron(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

    //B-Wing  //
    tempsst = new SpaceshipType("B-Wing","B-w",SpaceshipType.SIZE_MEDIUM,15,50,SpaceshipRange.LONG,3,5,uniqueShipIdCounter,25,15);
    tempsst.setDescription("Rebels, Heavy Fighter-Bomber, Best allround Sqaudron");
    tempsst.setWeaponsStrengthMedium(15);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(8);
    tempsst.setWeaponsStrengthLarge(10);
    tempsst.setWeaponsMaxSalvoesLarge(3);
    tempsst.setIncreaseInitiative(1);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Y-Wing  //
    tempsst = new SpaceshipType("Y-Wing","Y-w",SpaceshipType.SIZE_SMALL,15,20,SpaceshipRange.LONG,2,3,uniqueShipIdCounter,20,7);
    tempsst.setDescription("Rebels, First Generation Bomber");
    tempsst.setWeaponsStrengthMedium(15);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(4);
    tempsst.setWeaponsStrengthLarge(10);
    tempsst.setWeaponsMaxSalvoesLarge(2);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

//  ######## ( Imperial Fighter ) ########
    
    //TIE-Fighter  //
    tempsst = new SpaceshipType("TIE-Fighter","TieF",SpaceshipType.SIZE_SMALL,0,22,SpaceshipRange.NONE,1,1,uniqueShipIdCounter,10,17);
    tempsst.setDescription("Empire, Cheap Fighter");
    tempsst.setSquadron(true);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

    //TIE-Interceptor  //
    tempsst = new SpaceshipType("TIE-Interceptor","TieI",SpaceshipType.SIZE_SMALL,0,25,SpaceshipRange.NONE,1,2,uniqueShipIdCounter,15,25);
    tempsst.setDescription("Empire, Second generation Tie-fighter");
    tempsst.setSquadron(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

    //TIE-Bomber  //
    tempsst = new SpaceshipType("TIE-Bomber","TieB",SpaceshipType.SIZE_SMALL,0,40,SpaceshipRange.NONE,2,3,uniqueShipIdCounter,25,7);
    tempsst.setDescription("Empire, Heavy Bomber");
    tempsst.setWeaponsStrengthMedium(15);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(8);
    tempsst.setWeaponsStrengthLarge(10);
    tempsst.setWeaponsMaxSalvoesLarge(4);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //TIE-Advanced  //
    tempsst = new SpaceshipType("TIE-Advanced","TieA",SpaceshipType.SIZE_SMALL,15,30,SpaceshipRange.NONE,2,3,uniqueShipIdCounter,15,30);
    tempsst.setDescription("Empire, Third generation Tie-fighter");
    tempsst.setWeaponsStrengthMedium(5);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(2);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

    //TIE-Defender  //
    tempsst = new SpaceshipType("TIE-Defender","TieD",SpaceshipType.SIZE_MEDIUM,20,35,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,20,35);
    tempsst.setDescription("Empire, Only Empire Fighter/bomber with an hyperdrive.");
    tempsst.setWeaponsStrengthMedium(10);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(3);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

//  ######## ( Starting ships ) ########
    
    //Old Rebublic Cruiser  //
    tempsst = new SpaceshipType("Old Rebublic Cruiser","RCr",SpaceshipType.SIZE_MEDIUM,0,300,SpaceshipRange.LONG,1,10,uniqueShipIdCounter,20,5);
    tempsst.setDescription("Starting ship");
    tempsst.setWeaponsStrengthMedium(10);
    tempsst.setWeaponsMaxSalvoesMedium(10);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Old Republic Carrier //
    tempsst = new SpaceshipType("Old Republic Carrier","RCa",SpaceshipType.SIZE_SMALL,0,150,SpaceshipRange.LONG,1,10,uniqueShipIdCounter,5,0);
    tempsst.setDescription("Starting Carrier");
    tempsst.setSquadronCapacity(2);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);
    
    
     //Old Rebublic Fighter  //
    tempsst = new SpaceshipType("Old Rebublic Fighter","ORF",SpaceshipType.SIZE_SMALL,0,30,SpaceshipRange.NONE,1,10,uniqueShipIdCounter,10,15);
    tempsst.setDescription("Starting Bomber");
    tempsst.setWeaponsStrengthMedium(10);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(1);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);
    
     //Old Rebublic Bomber  //
    tempsst = new SpaceshipType("Old Rebublic Bomber","ORB",SpaceshipType.SIZE_SMALL,0,40,SpaceshipRange.NONE,1,10,uniqueShipIdCounter,15,5);
    tempsst.setDescription("Starting Fighter");
    tempsst.setWeaponsStrengthMedium(10);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(7);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIAIR);
    gw.addShipType(tempsst);

//  ######## ( Public ships ) ########    

    //Corellian Corvettte  //
    tempsst = new SpaceshipType("Corellian Corvettte","CrV",SpaceshipType.SIZE_SMALL,70,70,SpaceshipRange.LONG,1,2,uniqueShipIdCounter,10,5);
    tempsst.setDescription("Cheap allround trasnport ship");
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Galleon  //
    tempsst = new SpaceshipType("Galleon","Gal",SpaceshipType.SIZE_SMALL,50,150,SpaceshipRange.LONG,2,2,uniqueShipIdCounter,5,5);
    tempsst.setDescription("Small Carrier");
    tempsst.setSquadronCapacity(2);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Escort Carrier  //
    tempsst = new SpaceshipType("Escort Carrier","Esc",SpaceshipType.SIZE_MEDIUM,100,300,SpaceshipRange.LONG,3,8,uniqueShipIdCounter,5,5);
    tempsst.setDescription("Medium Carrier");
    tempsst.setSquadronCapacity(10);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Star Galleon  //
    tempsst = new SpaceshipType("Star Galleon","SGal",SpaceshipType.SIZE_LARGE,200,600,SpaceshipRange.SHORT,6,15,uniqueShipIdCounter,5,5);
    tempsst.setDescription("Heavy Carrier");
    tempsst.setPsychWarfare(1);    
    tempsst.setSquadronCapacity(16);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    tempsst.setScreened(true);
    gw.addShipType(tempsst);
    
    // Long range freighter
    tempsst = new SpaceshipType("Long Range Freighter","Lrf",SpaceshipType.SIZE_MEDIUM,10,20,SpaceshipRange.LONG,3,8,uniqueShipIdCounter,5,5);
    tempsst.setDescription("Supplies Medium ships with material");
    tempsst.setSupply(2);
    tempsst.setScreened(true);
    gw.addShipType(tempsst);
    
    // Supply Freighter
    tempsst = new SpaceshipType("Supply Freighter","SF",SpaceshipType.SIZE_MEDIUM,10,20,SpaceshipRange.SHORT,5,10,uniqueShipIdCounter,5,5);
    tempsst.setDescription("Supplies Huge ships with material");
	tempsst.setSupply(4);
    tempsst.setScreened(true);
    gw.addShipType(tempsst);

    //Strike Cruiser  //
    tempsst = new SpaceshipType("Strike Cruiser","Stc",SpaceshipType.SIZE_SMALL,50,100,SpaceshipRange.LONG,2,4,uniqueShipIdCounter,20,5);
    tempsst.setDescription("Allround cruiser with Troops");
    tempsst.setPsychWarfare(1);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    
    //Torpedo Ship  //
    tempsst = new SpaceshipType("Torpedo Ship","Tor",SpaceshipType.SIZE_MEDIUM,10,60,SpaceshipRange.NONE,3,4,uniqueShipIdCounter,5,5);
    tempsst.setDescription("Weak against most, but great against Large and Huge");
    tempsst.setWeaponsStrengthMedium(20);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(5);
    tempsst.setWeaponsStrengthLarge(20);
    tempsst.setWeaponsMaxSalvoesLarge(10);
    tempsst.setWeaponsStrengthHuge(20);
    tempsst.setWeaponsMaxSalvoesHuge(5);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

    //Missile Boat  //
    tempsst = new SpaceshipType("Missile Boat","Mis",SpaceshipType.SIZE_MEDIUM,10,60,SpaceshipRange.NONE,3,4,uniqueShipIdCounter,20,5);
    tempsst.setDescription("Weak against most, but great against Medium and Small");
    tempsst.setWeaponsStrengthMedium(25);
    tempsst.setSquadron(true);
    tempsst.setWeaponsMaxSalvoesMedium(10);
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ANTIMBU);
    gw.addShipType(tempsst);

//  ######## ( Defence plattforms ) ########    
    
    //Golan I  //
    tempsst = new SpaceshipType("Golan I","GI",SpaceshipType.SIZE_SMALL,40,80,SpaceshipRange.NONE,1,5,uniqueShipIdCounter,25,10);
    tempsst.setDescription("Defence plattforms Level 1");
    tempsst.setSquadronCapacity(2);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Golan II  //
    tempsst = new SpaceshipType("Golan II","GII",SpaceshipType.SIZE_MEDIUM,150,300,SpaceshipRange.NONE,3,10,uniqueShipIdCounter,15,10);
    tempsst.setDescription("Defence plattforms Level 2");
    tempsst.setWeaponsStrengthMedium(25);
    tempsst.setSquadronCapacity(4);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Golan III  //
    tempsst = new SpaceshipType("Golan III","GIII",SpaceshipType.SIZE_LARGE,500,1000,SpaceshipRange.NONE,6,20,uniqueShipIdCounter,15,10);
    tempsst.setDescription("Defence plattforms Level 3");
    tempsst.setWeaponsStrengthMedium(15);
    tempsst.setWeaponsStrengthLarge(25);
    tempsst.setSquadronCapacity(8);
    tempsst.setNoRetreat(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Golan IV  //
    tempsst = new SpaceshipType("Golan IV","GIV",SpaceshipType.SIZE_HUGE,1500,3000,SpaceshipRange.NONE,15,65,uniqueShipIdCounter,15,10);
    tempsst.setDescription("Defence plattforms Level 4");
    tempsst.setWeaponsStrengthMedium(15);
    tempsst.setWeaponsStrengthLarge(15);
    tempsst.setWeaponsStrengthHuge(25);
    tempsst.setSquadronCapacity(16);
    tempsst.setNoRetreat(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

//  ######## ( Head Quarters ) ########    
    
    //Head Quarter  //
    tempsst = new SpaceshipType("Rebel Head Quarter","RHQ",SpaceshipType.SIZE_MEDIUM,700,900,SpaceshipRange.NONE,1,1000,uniqueShipIdCounter,15,10);
    tempsst.setDescription("Head Quarters, Gives a bonus to closed Home planets");
    
    tempsst.setIncOwnClosedBonus(10);
    tempsst.setWeaponsStrengthMedium(25);
    tempsst.setSquadronCapacity(4);
    tempsst.setNoRetreat(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Head Quarter  //
    tempsst = new SpaceshipType("Trade Federation Head Quarter","THQ",SpaceshipType.SIZE_MEDIUM,700,900,SpaceshipRange.NONE,1,1000,uniqueShipIdCounter,15,10);
    tempsst.setDescription("Head Quarters, If placed on open planets gives a great +7 income boost");
    tempsst.setIncOwnOpenBonus(2);
    tempsst.setIncOwnClosedBonus(2);
    tempsst.setWeaponsStrengthMedium(25);
    tempsst.setSquadronCapacity(4);
    tempsst.setNoRetreat(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Head Quarter  //
    tempsst = new SpaceshipType("Imperial Head Quarter","IHQ",SpaceshipType.SIZE_MEDIUM,700,900,SpaceshipRange.NONE,1,1000,uniqueShipIdCounter,15,10);
    tempsst.setDescription("Imperial Head Quarters, Gives greater income boost to open home planet");
    tempsst.setIncOwnOpenBonus(10);
    tempsst.setIncOwnClosedBonus(5);
    tempsst.setWeaponsStrengthMedium(25);
    tempsst.setSquadronCapacity(4);
    tempsst.setNoRetreat(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Head Quarter  //
    tempsst = new SpaceshipType("Galactic Republic HQ","GHQ",SpaceshipType.SIZE_MEDIUM,700,900,SpaceshipRange.NONE,1,1000,uniqueShipIdCounter,15,10);
    tempsst.setDescription("Galactic Republic Head Quarters, Gives greater income boost to open home planet");
    tempsst.setIncOwnOpenBonus(15);
    tempsst.setIncOwnClosedBonus(5);
    tempsst.setWeaponsStrengthMedium(25);
    tempsst.setSquadronCapacity(4);
    tempsst.setNoRetreat(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);
    
//  ######## ( NEUTRALA SKEPP ) ########

    //Space Fortress I  //
    tempsst = new SpaceshipType("Space Fortress I","SFI",SpaceshipType.SIZE_SMALL,40,80,SpaceshipRange.NONE,1,50,uniqueShipIdCounter,25,10);
    tempsst.setDescription("Neutral planet Defence plattforms Level 1");
    tempsst.setSquadronCapacity(2);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Space Fortress II  //
    tempsst = new SpaceshipType("Space Fortress II","SFII",SpaceshipType.SIZE_MEDIUM,150,300,SpaceshipRange.NONE,1,100,uniqueShipIdCounter,15,10);
    tempsst.setDescription("Neutral planet Defence plattforms Level 2");
    tempsst.setWeaponsStrengthMedium(25);
    tempsst.setSquadronCapacity(4);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);

    //Space Fortress III  //
    tempsst = new SpaceshipType("Space Fortress III","SFIII",SpaceshipType.SIZE_LARGE,500,1000,SpaceshipRange.NONE,2,200,uniqueShipIdCounter,15,10);
    tempsst.setDescription("Neutral planet Defence plattforms Level 3");
    tempsst.setWeaponsStrengthMedium(15);
    tempsst.setWeaponsStrengthLarge(25);
    tempsst.setSquadronCapacity(8);
    tempsst.setNoRetreat(true);
    tempsst.setTargetingType(SpaceshipTargetingType.ALLROUND);
    gw.addShipType(tempsst);



    
//  ######## ( Civilian ships ) ########
    // Civilian ships
    // **************
    
    // Blockade Runner
    tempsst = new SpaceshipType("Blockade Runner","Run",SpaceshipType.SIZE_SMALL,SpaceshipRange.LONG,1,8,uniqueShipIdCounter);
    tempsst.setDescription("A small merchant ship specialized in smuggling in closed systems");
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setIncOwnClosedBonus(1);
    tempsst.setIncNeutralOpenBonus(2);
    tempsst.setIncNeutralClosedBonus(1);
    tempsst.setIncEnemyClosedBonus(2);
    tempsst.setIncEnemyOpenBonus(4);
    tempsst.setScreened(true);
    gw.addShipType(tempsst);

    // Small Merchant Freighter
    tempsst = new SpaceshipType("Small Merchant Freighter","SMF",SpaceshipType.SIZE_SMALL,SpaceshipRange.LONG,1,8,uniqueShipIdCounter);
    tempsst.setDescription("Small sized merchant ship");
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setIncOwnOpenBonus(1);
    tempsst.setIncNeutralOpenBonus(3);
    tempsst.setIncFrendlyOpenBonus(2);
    tempsst.setScreened(true);
    gw.addShipType(tempsst);
    
    // Medium Merchant Freighter
    tempsst = new SpaceshipType("Medium Merchant Freighter","MMF",SpaceshipType.SIZE_MEDIUM,SpaceshipRange.LONG,1,12,uniqueShipIdCounter);
    tempsst.setDescription("Medium sized merchant ship");
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setIncOwnOpenBonus(2);
    tempsst.setIncNeutralOpenBonus(4);
    tempsst.setIncFrendlyOpenBonus(3);
    tempsst.setScreened(true);
    gw.addShipType(tempsst);
    
    
    // Supply Freighter
    tempsst = new SpaceshipType("Repair Ship","RS",SpaceshipType.SIZE_MEDIUM,SpaceshipRange.LONG,1,8,uniqueShipIdCounter);
    tempsst.setDescription("Medium sized repair ship");
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setSupply(2);
    tempsst.setScreened(true);
    gw.addShipType(tempsst);

    // Large Merchant Freighter
    tempsst = new SpaceshipType("Large Merchant Freighter","LMF",SpaceshipType.SIZE_LARGE,SpaceshipRange.SHORT,1,20,uniqueShipIdCounter);
    tempsst.setDescription("Large sized merchant ship");
    tempsst.setCanAppearOnBlackMarket(true);
    tempsst.setIncOwnOpenBonus(3);
    tempsst.setIncNeutralOpenBonus(8);
    tempsst.setIncFrendlyOpenBonus(6);
    tempsst.setScreened(true);
    gw.addShipType(tempsst);
    

    // add shiptypes for neutral planets
    gw.setNeutralSize1(gw.getSpaceshipTypeByName("Space Fortress I"));
    gw.setNeutralSize2(gw.getSpaceshipTypeByName("Space Fortress II"));
    gw.setNeutralSize3(gw.getSpaceshipTypeByName("Space Fortress III"));

//  ######## ( VIPS) ########
    //////////////////////////////////////////////////////////VIP/////////////////////////////////////////
    // vip types
    UniqueIdCounter uniqueVIPIdCounter = new UniqueIdCounter();

    VIPType tmpVipType = new VIPType("Emperor Palpatine","Pal",aEvil,uniqueVIPIdCounter);
    tmpVipType.setCanVisitNeutralPlanets(true);
    tmpVipType.setDiplomat(true);
    tmpVipType.setGovernor(true);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setOpenIncBonus(5);
    tmpVipType.setClosedIncBonus(5);
    gw.addVipType(tmpVipType);
    VIPType govType = tmpVipType;

    tmpVipType = new VIPType("Chancellor Valorum","ChV",aOldRepublic,uniqueVIPIdCounter);
    tmpVipType.setCanVisitNeutralPlanets(true);
    tmpVipType.setDiplomat(true);
    tmpVipType.setGovernor(true);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setOpenIncBonus(10);
    tmpVipType.setClosedIncBonus(10);
    gw.addVipType(tmpVipType);
    VIPType govType4 = tmpVipType;
    
    tmpVipType = new VIPType("Commander Mon Mothma","Mon",aAlliance,uniqueVIPIdCounter);
    tmpVipType.setCanVisitNeutralPlanets(true);        
    tmpVipType.setDiplomat(true);
    tmpVipType.setGovernor(true);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setOpenIncBonus(1);
    gw.addVipType(tmpVipType);
    VIPType govType2 = tmpVipType;

    tmpVipType = new VIPType("Viceroy Nute Gunray","Nute",aTrade,uniqueVIPIdCounter);
    tmpVipType.setCanVisitNeutralPlanets(true);        
    tmpVipType.setDiplomat(true);
    tmpVipType.setGovernor(true);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setOpenIncBonus(1);
    gw.addVipType(tmpVipType);
    VIPType govType1 = tmpVipType;

    tmpVipType = new VIPType("Imperial Probe Droid","ISpy",aNeutral,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setSpying(true);
    tmpVipType.setCounterEspionage(50);
    gw.addVipType(tmpVipType);

    VIPType ImpSpy = tmpVipType;

    
    tmpVipType = new VIPType("Bothan Spy","BSpy",aNeutral,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setSpying(true);
    tmpVipType.setCounterEspionage(50);
    gw.addVipType(tmpVipType);
//??    
    VIPType RebelSpy = tmpVipType;

    tmpVipType = new VIPType("Trade Company","Eco",aNeutral,uniqueVIPIdCounter);
    tmpVipType.setOpenIncBonus(4);
    tmpVipType.setClosedIncBonus(2);
    gw.addVipType(tmpVipType);

    tmpVipType = new VIPType("General","Gen",aNeutral,uniqueVIPIdCounter);
    tmpVipType.setPsychWarfareBonus(1);
    tmpVipType.setResistanceBonus(3);
    gw.addVipType(tmpVipType);

    VIPType ImpGen = tmpVipType;
    
    tmpVipType = new VIPType("Commando Unit","CoU",aNeutral,uniqueVIPIdCounter);
    tmpVipType.setPsychWarfareBonus(2);
    tmpVipType.setResistanceBonus(5);
    tmpVipType.setFrequency(BlackMarketFrequency.RARE);
    gw.addVipType(tmpVipType);

    VIPType RebelCoU = tmpVipType;
    
    tmpVipType = new VIPType("Admiral","Adm",aNeutral,uniqueVIPIdCounter);
    tmpVipType.setInitBonus(5);
    gw.addVipType(tmpVipType);
    
    VIPType ImpAdm = tmpVipType;
    
    tmpVipType = new VIPType("Top Ace","Ace",aNeutral,uniqueVIPIdCounter);
    tmpVipType.setInitFighterSquadronBonus(5);
    gw.addVipType(tmpVipType);

    VIPType RebelTop = tmpVipType;
    
    tmpVipType = new VIPType("Scientist","Sci",aNeutral,uniqueVIPIdCounter);
    tmpVipType.setTechBonus(20);
    gw.addVipType(tmpVipType);

    VIPType TradeSci = tmpVipType;
    
    tmpVipType = new VIPType("Spaceship Engineer","Eng",aNeutral,uniqueVIPIdCounter);
    gw.addVipType(tmpVipType);
    
    VIPType TradeEng = tmpVipType;

    tmpVipType = new VIPType("FTL Master","FTL",aNeutral,uniqueVIPIdCounter);
    tmpVipType.setFTLbonus(true);
    gw.addVipType(tmpVipType);
    
    tmpVipType = new VIPType("Diplomat","Dip",aNeutral,uniqueVIPIdCounter);
    tmpVipType.setImmuneToCounterEspionage(true);
    tmpVipType.setSpying(true);
    tmpVipType.setCanVisitNeutralPlanets(true);
    tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
    tmpVipType.setDiplomat(true);
    gw.addVipType(tmpVipType);
    
    VIPType RebelDip = tmpVipType;
    
    tmpVipType = new VIPType("Negotiator","Neg",aNeutral,uniqueVIPIdCounter);
    tmpVipType.setImmuneToCounterEspionage(true);
    tmpVipType.setCanVisitNeutralPlanets(true);
    tmpVipType.setDiplomat(true);
    gw.addVipType(tmpVipType);
    
    VIPType TradeDip = tmpVipType;
    
    tmpVipType = new VIPType("Assassin","Ass",aEvil,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setAssassination(50);
    gw.addVipType(tmpVipType);

    tmpVipType = new VIPType("Bounty Hunter","BoH",aNeutral,uniqueVIPIdCounter);
    tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setAssassination(80);
    gw.addVipType(tmpVipType);
    
    VIPType TradeAss = tmpVipType;
    
    tmpVipType = new VIPType("Jedi Lvl 1","Jed1",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
    tmpVipType.setPsychWarfareBonus(1);
    tmpVipType.setInitFighterSquadronBonus(2);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(2);
    tmpVipType.setAssassination(40);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
    gw.addVipType(tmpVipType);

    tmpVipType = new VIPType("Jedi Lvl 2","Jed2",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
    tmpVipType.setPsychWarfareBonus(1);
    tmpVipType.setInitFighterSquadronBonus(5);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(3);
    tmpVipType.setSpying(true);
    tmpVipType.setCounterEspionage(50);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
    gw.addVipType(tmpVipType);
    
    VIPType Jed1 = tmpVipType;
    
    tmpVipType = new VIPType("Padawan","Pad",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    tmpVipType.setHardToKill(true);
    tmpVipType.setSpying(true);
    tmpVipType.setCounterEspionage(30);
    tmpVipType.setFrequency(BlackMarketFrequency.UNCOMMON);
    tmpVipType.setWellGuarded(true);
    gw.addVipType(tmpVipType);
    
    VIPType Jed2 = tmpVipType;
    
    tmpVipType = new VIPType("Jedi Master","MJed",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_MASTER);
    tmpVipType.setPsychWarfareBonus(1);
    tmpVipType.setInitFighterSquadronBonus(10);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(3);
    tmpVipType.setSpying(true);
    tmpVipType.setCounterEspionage(80);
    tmpVipType.setImmuneToCounterEspionage(true);
    tmpVipType.setFrequency(BlackMarketFrequency.RARE);
    tmpVipType.setWellGuarded(true);
    gw.addVipType(tmpVipType);
    
    VIPType Jed3 = tmpVipType;
    
    //  ######## Alliance (Special VIPS) ########
    
    tmpVipType = new VIPType("Jedi Yoda","Yoda",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_MASTER);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(4);
    tmpVipType.setSpying(true);
    tmpVipType.setDiplomat(true);
    tmpVipType.setCounterEspionage(80);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Yoda = tmpVipType;

    tmpVipType = new VIPType("Obi-Wan Kenobi","Obi",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(1);
    tmpVipType.setCounterEspionage(80);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Kenobi = tmpVipType;

    tmpVipType = new VIPType("Luke Skywalker","Luke",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_MASTER);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(2);
    tmpVipType.setInitFighterSquadronBonus(15);
    tmpVipType.setCounterEspionage(80);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Luke = tmpVipType;
	
	tmpVipType = new VIPType("Leia Organa","Leia",aJedi,uniqueVIPIdCounter);
    tmpVipType.setImmuneToCounterEspionage(true);
    tmpVipType.setCounterEspionage(80);
    tmpVipType.setSpying(true);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
    tmpVipType.setDiplomat(true);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setHardToKill(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    gw.addVipType(tmpVipType);
    
    VIPType Leia = tmpVipType;
    
	tmpVipType = new VIPType("R2D2 & C3P0","R2D2",aJedi,uniqueVIPIdCounter);
    tmpVipType.setImmuneToCounterEspionage(true);
    tmpVipType.setSpying(true);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setHardToKill(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    gw.addVipType(tmpVipType);
    
    VIPType R2D2 = tmpVipType;
    
    tmpVipType = new VIPType("Admiral Ackbar","Ack",aJedi,uniqueVIPIdCounter);
    tmpVipType.setInitBonus(15);
    tmpVipType.setFTLbonus(true);
    tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setHardToKill(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    gw.addVipType(tmpVipType);
    
    VIPType Ackbar = tmpVipType;
    
    tmpVipType = new VIPType("Wedge Antilles","Wed",aJedi,uniqueVIPIdCounter);
    tmpVipType.setInitFighterSquadronBonus(15);
    tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setHardToKill(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    gw.addVipType(tmpVipType);

    VIPType Wedge = tmpVipType;
    
       
    //  ######## Empire (Special VIPS) ########
    
    tmpVipType = new VIPType("General Weers","Wee",aJedi,uniqueVIPIdCounter);
    tmpVipType.setPsychWarfareBonus(1);
    tmpVipType.setResistanceBonus(5);
    tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setHardToKill(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    gw.addVipType(tmpVipType);

    VIPType Weers = tmpVipType;
    
    tmpVipType = new VIPType("Admiral Thrawn","Thr",aJedi,uniqueVIPIdCounter);
    tmpVipType.setInitBonus(10);
    tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setHardToKill(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    gw.addVipType(tmpVipType);
    
    VIPType Thrawn = tmpVipType;
    
    tmpVipType = new VIPType("Admiral Piett","Pie",aJedi,uniqueVIPIdCounter);
    tmpVipType.setInitBonus(5);
    tmpVipType.setFTLbonus(true);
    tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setHardToKill(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    gw.addVipType(tmpVipType);
    
    VIPType Piett = tmpVipType;
    
    tmpVipType = new VIPType("Dark Vader","Dark",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_MASTER);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(5);
    tmpVipType.setInitFighterSquadronBonus(10);
    tmpVipType.setDiplomat(true);
    tmpVipType.setCounterEspionage(80);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Vader = tmpVipType;

    tmpVipType = new VIPType("Darth Maul","Maul",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(2);
    tmpVipType.setFTLbonus(true);
    tmpVipType.setSpying(true);
    tmpVipType.setCounterEspionage(40);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Maul = tmpVipType;

    tmpVipType = new VIPType("Mara Jade","Mara",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(1);
    tmpVipType.setSpying(true);
    tmpVipType.setCounterEspionage(60);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Mara = tmpVipType;
    
    //  ######## Trade (Special VIPS) ########
	
 
	
	tmpVipType = new VIPType("Haako Rune","Rune",aJedi,uniqueVIPIdCounter);
	tmpVipType.setOpenIncBonus(5);
	tmpVipType.setClosedIncBonus(1);
    tmpVipType.setCounterEspionage(60);
    tmpVipType.setSpying(true);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);
	
	VIPType Haako = tmpVipType;

    tmpVipType = new VIPType("Jango Fett","Fett",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(2);
    tmpVipType.setFTLbonus(true);
    tmpVipType.setSpying(true);
    tmpVipType.setCounterEspionage(40);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Jango = tmpVipType;
	
	tmpVipType = new VIPType("Count Dooko","Dook",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_MASTER);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(2);
    tmpVipType.setFTLbonus(true);
    tmpVipType.setSpying(true);
    tmpVipType.setCounterEspionage(40);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Dooko = tmpVipType;
	
    tmpVipType = new VIPType("Zam Wesell","Zam",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(2);
    tmpVipType.setFTLbonus(true);
    tmpVipType.setSpying(true);
    tmpVipType.setCounterEspionage(40);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Wesell = tmpVipType;
    
	
    tmpVipType = new VIPType("Nemodian Scientist","NSci",aJedi,uniqueVIPIdCounter);
    tmpVipType.setTechBonus(10);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
    gw.addVipType(tmpVipType);

    VIPType NSci = tmpVipType;
    
    tmpVipType = new VIPType("Nemodian Spaceship Engineer","NEng",aJedi,uniqueVIPIdCounter);
    tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
    gw.addVipType(tmpVipType);
    
    VIPType NEng = tmpVipType;

    //  ######## Galactic Republic (Special VIPS) ########
/*    Old Republic	
   - Supreme Chancelor	
    -Princess Amidala	
    -Mace Windu		
    -Qui chi-gon		
    Gungan General
    Jar Jar Binks	
*/
    tmpVipType = new VIPType("Gungan General","GuG",aJedi,uniqueVIPIdCounter);
    tmpVipType.setPsychWarfareBonus(1);
    tmpVipType.setResistanceBonus(5);
    tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setHardToKill(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    gw.addVipType(tmpVipType);

    VIPType Gungan = tmpVipType;
    
    tmpVipType = new VIPType("Mace Windu","Mace",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(4);
    tmpVipType.setSpying(true);
    tmpVipType.setDiplomat(true);
    tmpVipType.setCounterEspionage(80);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Mace = tmpVipType;

    tmpVipType = new VIPType("Qui chi-gon","Qui",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(1);
    tmpVipType.setDiplomat(true);
    tmpVipType.setCounterEspionage(80);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Qui = tmpVipType;

	tmpVipType = new VIPType("Jar Jar Binks","Jar",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    tmpVipType.setHardToKill(true);
    tmpVipType.setResistanceBonus(3);
    tmpVipType.setDiplomat(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Jar = tmpVipType;

    tmpVipType = new VIPType("Anakin Skywalker","Ana",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_MASTER);
    tmpVipType.setHardToKill(true);
    tmpVipType.setInitFighterSquadronBonus(15);
    tmpVipType.setCounterEspionage(80);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);

	VIPType Anakin = tmpVipType;
	
	tmpVipType = new VIPType("Queen Amidala","Ami",aJedi,uniqueVIPIdCounter);
    tmpVipType.setImmuneToCounterEspionage(true);
    tmpVipType.setCounterEspionage(80);
    tmpVipType.setSpying(true);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setFrequency(BlackMarketFrequency.NEVER);
    tmpVipType.setDiplomat(true);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setHardToKill(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    gw.addVipType(tmpVipType);
    
    VIPType Amidala = tmpVipType;
    
    
    
//  ######## Blackmarket (Special VIPS) ########
    
    tmpVipType = new VIPType("Boba Fett","Boba",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_MASTER);
    tmpVipType.setHardToKill(true);
    tmpVipType.setFTLbonus(true);
    tmpVipType.setInitFighterSquadronBonus(5);
    tmpVipType.setSpying(true);
    tmpVipType.setCounterEspionage(60);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.RARE);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);
    
    tmpVipType = new VIPType("Han Solo","Solo",aJedi,uniqueVIPIdCounter);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_VETERAN);
    tmpVipType.setHardToKill(true);
    tmpVipType.setFTLbonus(true);
    tmpVipType.setSpying(true);
    tmpVipType.setCounterEspionage(60);
    tmpVipType.setInitFighterSquadronBonus(5);
	tmpVipType.setImmuneToCounterEspionage(true);
	tmpVipType.setFrequency(BlackMarketFrequency.RARE);
	tmpVipType.setWellGuarded(true);
	gw.addVipType(tmpVipType);
	
	tmpVipType = new VIPType("Lando Calrissian","Land",aJedi,uniqueVIPIdCounter);
    tmpVipType.setImmuneToCounterEspionage(true);
    tmpVipType.setFTLbonus(true);
    tmpVipType.setInitFighterSquadronBonus(5);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setFrequency(BlackMarketFrequency.RARE);
    tmpVipType.setDiplomat(true);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setHardToKill(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    gw.addVipType(tmpVipType);
	
    tmpVipType = new VIPType("Chewbacca","Chew",aJedi,uniqueVIPIdCounter);
    tmpVipType.setImmuneToCounterEspionage(true);
    tmpVipType.setFTLbonus(true);
    tmpVipType.setInitFighterSquadronBonus(5);
    tmpVipType.setCanVisitEnemyPlanets(true);
    tmpVipType.setFrequency(BlackMarketFrequency.RARE);
    tmpVipType.setDiplomat(true);
    tmpVipType.setWellGuarded(true);
    tmpVipType.setHardToKill(true);
    tmpVipType.setDuellistSkill(VIPType.DUELLIST_APPRENTICE);
    gw.addVipType(tmpVipType);
    
    // Create orbital structures
    // empire
    SpaceStation os1 = new SpaceStation();
    os1.setSpaceport(true);
    os1.setOpenProdBonus(2);
    
    // league
    SpaceStation os3 = new SpaceStation();
    os3.setSpaceport(true);
    os3.setOpenProdBonus(4);

    // rebels
    SpaceStation os2 = new SpaceStation();
    os2.setSpaceport(true);
    os2.setClosedProdBonus(1);
    os2.setTechBonus(20);
    
    //Galactic Republic
    SpaceStation os4 = new SpaceStation();
    os4.setSpaceport(true);
    os4.setOpenProdBonus(5);
    os4.setTechBonus(20);
    
//  Trade Federation
    Faction tempFaction = new Faction("Trade Federation",Faction.getColorHexString(24,66,255),aNeutral);
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Sullustan Frigate"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Free Starrunner"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Acclamator cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Broadside class cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Sullustan cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Improved Dreadnought"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Droid control ship"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Battlestar"));
    
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("DF-x10"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("DF-x14"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("DB-y44"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("DB-y27"));
    
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corellian Corvettte"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galleon"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fast Carrier"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Escort Carrier"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Star Galleon"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Strike Cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Ship"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Missile Boat"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan I"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan II"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan III"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan IV"));
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Rebublic Cruiser"));   
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Republic Carrier"));    
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Rebublic Fighter"));    
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Rebublic Bomber"));
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Trade Federation Head Quarter"));	
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Medium Merchant Freighter"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Large Merchant Freighter"));	
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Small Merchant Freighter"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Blockade Runner"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Repair Ship"));
    
    tempFaction.addStartingVIPType(Haako);
    tempFaction.addStartingVIPType(Jango);
    tempFaction.addStartingVIPType(Dooko);
    tempFaction.addStartingVIPType(NSci);
    tempFaction.addStartingVIPType(NEng);
    tempFaction.addStartingVIPType(Wesell);

    
    tempFaction.setOpenPlanetBonus(3);
    
    tempFaction.setGovernorVIPType(govType1);
    
    gw.addFaction(tempFaction);
    
    
//  Galactic Republic
    
    tempFaction = new Faction("Galactic Republic",Faction.getColorHexString(200,200,255),aNeutral);
    
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Sulanko Frigate"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Reliant Troopship"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Reliant Carrier"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Ion Frigate"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Meteor"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Protector I"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Protector II"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Venator Class V"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Berserker Cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Guargantuan"));
    
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("DF-x10"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("DF-x14"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("DB-y44"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("DB-y27"));
    
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corellian Corvettte"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galleon"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Fast Carrier"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Escort Carrier"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Star Galleon"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Strike Cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Ship"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Missile Boat"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan I"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan II"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan III"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan IV"));
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Rebublic Cruiser"));   
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Republic Carrier"));    
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Rebublic Fighter"));    
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Rebublic Bomber"));
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Trade Federation Head Quarter"));	
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Medium Merchant Freighter"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Large Merchant Freighter"));	
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Small Merchant Freighter"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Blockade Runner"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Repair Ship"));
    
    tempFaction.setNrStartingRandomVIPs(1);
    tempFaction.addStartingVIPType(Amidala);
    tempFaction.addStartingVIPType(Jar);
    tempFaction.addStartingVIPType(Mace);
    tempFaction.addStartingVIPType(Gungan);
    tempFaction.addStartingVIPType(Qui);
    tempFaction.addStartingVIPType(Anakin);


    
    tempFaction.setOpenPlanetBonus(2);
    
    tempFaction.setGovernorVIPType(govType4);
    
// kommenterar ut Galactic Republic tills Nicklas hinner jobba klar den... /Paul
//    gw.addFaction(tempFaction);
    
    
    // empire
    tempFaction = new Faction("Empire",Faction.getColorHexString(240,35,45),aEmpire);
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Lambda Shuttle"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Lancer Frigate"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Carrack Light Cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Nebulon-A Frigate"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Interdictor Cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Victory Destroyer"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Victory II Star Destroyer"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Imperial Star Destroyer"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Imperial Star Destroyer II"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Super Star Destroyer"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Death Star"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corellian Corvettte"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galleon"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Escort Carrier"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Star Galleon"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Strike Cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("TIE-Fighter"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("TIE-Interceptor"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("TIE-Bomber"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("TIE-Advanced"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("TIE-Defender"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Torpedo Ship"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Missile Boat"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan I"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan II"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan III"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan IV"));
    
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Large Merchant Freighter"));	
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Medium Merchant Freighter"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Small Merchant Freighter"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Blockade Runner"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Repair Ship"));
    
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Rebublic Cruiser"));   
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Republic Carrier"));    
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Rebublic Fighter"));    
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Rebublic Bomber"));
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Imperial Head Quarter"));	
    
    tempFaction.addStartingVIPType(Weers);
    tempFaction.addStartingVIPType(Thrawn);
    tempFaction.addStartingVIPType(Piett);
    tempFaction.addStartingVIPType(Vader);
    tempFaction.addStartingVIPType(Maul);
    tempFaction.addStartingVIPType(Mara);
    tempFaction.addStartingVIPType(ImpSpy);
    tempFaction.addStartingVIPType(ImpSpy);

    tempFaction.setResistanceBonus(2);
    tempFaction.setGovernorVIPType(govType);
    gw.addFaction(tempFaction);

    // rebels
    tempFaction = new Faction("Rebel",Faction.getColorHexString(0,255,0),aAlliance);
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Z-95"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("X-Wing"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("E-Wing"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("A-Wing"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("B-Wing"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Y-Wing"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Supply Freighter"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Long Range Freighter"));   
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Millenium Falcon"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corellian Gunship"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Dreadnaught"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Nebulon-B Frigate"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Assault frigate"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Liberator Cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Mon Calamari Cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Dauntless Cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Bulwark Battlecruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Corellian Corvettte"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Galleon"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Escort Carrier"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Star Galleon"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Strike Cruiser"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan I"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan II"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan III"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Golan IV"));
    
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Large Merchant Freighter"));	
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Medium Merchant Freighter"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Small Merchant Freighter"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Blockade Runner"));
    tempFaction.addSpaceshipType(gw.getSpaceshipTypeByName("Repair Ship"));
    
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Rebublic Cruiser"));   
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Republic Carrier"));    
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Rebublic Fighter"));    
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Old Rebublic Bomber"));
    tempFaction.addStartingShipType(gw.getSpaceshipTypeByName("Rebel Head Quarter"));
    
    tempFaction.addStartingVIPType(RebelSpy);
    tempFaction.addStartingVIPType(Yoda);
    tempFaction.addStartingVIPType(Kenobi);
    tempFaction.addStartingVIPType(Luke);
    tempFaction.addStartingVIPType(Leia);
    tempFaction.addStartingVIPType(R2D2);
    tempFaction.addStartingVIPType(Ackbar);
    tempFaction.addStartingVIPType(Wedge);
	
    
    tempFaction.setClosedPlanetBonus(1);
    tempFaction.setGovernorVIPType(govType2);
     tempFaction.setResistanceBonus(1);
    gw.addFaction(tempFaction);

	return gw;
}	

	
}