
package sr.world;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sr.enums.BlackMarketFrequency;
import sr.enums.SpaceshipRange;
import sr.enums.SpaceshipTargetingType;
import sr.general.Functions;

/**
 * Instances of this class represent one spaceship type, from which 
 * spaceships can be built.
 * 
 * @author wmpabod
 *
 */
public class SpaceshipType implements Serializable{
    static final long serialVersionUID = 1L;

    // size/tonnage constants
	public static final int SIZE_SMALL = 150;
	public static final int SIZE_MEDIUM = 450;
	public static final int SIZE_LARGE = 750;
	public static final int SIZE_HUGE = 1200;
	
    String name,shortName,advanteges,disadvanteges;
    int tonnage,nrProduced,shields,upkeep,buildCost,hits,bombardment,increaseInitiative,initDefence;
    private SpaceshipRange range;
    int psychWarfare;
    boolean noRetreat = false,initSupport = false;
    private int weaponsStrengthSmall,weaponsStrengthMedium,weaponsStrengthLarge,weaponsStrengthHuge;
    private int weaponsMaxSalvoesMedium,weaponsMaxSalvoesLarge,weaponsMaxSalvoesHuge; // if Integer.MAX then treat as infinite
    private int armorSmall,armorMedium,armorLarge,armorHuge;
    private int supply = 0; // 0 - 4 max size of ship that can be resupplied
    private boolean canAppearOnBlackMarket = true;
    private BlackMarketFrequency blackMarketFrequency = BlackMarketFrequency.COMMON;
    private int blackmarketFirstTurn = 0;
    private int bluePrintFirstTurn = 0;
    private BlackMarketFrequency bluePrintFrequency = BlackMarketFrequency.COMMON;
    
    private boolean planetarySurvey,canAttackScreenedShips;
    private int weaponsStrengthSquadron,squadronCapacity;
    private SpaceshipTargetingType targetingType = null;
    private boolean squadron = false;
    private String description,history;
    private String shortDescription,advantages,disadvantages;
    UniqueIdCounter uic;
    private boolean civilian = false;
    private boolean lookAsCivilian = false;
    private boolean canBlockPlanet = true;
    private boolean visibleOnMap = true;
    private boolean availableToBuild = true;
    private boolean alwaysRetreat;
	private int incEnemyClosedBonus,incNeutralClosedBonus,incFrendlyClosedBonus,incOwnClosedBonus;
    private int incEnemyOpenBonus,incNeutralOpenBonus,incFrendlyOpenBonus,incOwnOpenBonus;
    private int aimBonus;
    private int troopCapacity,troopLaunchCapacity;

    private boolean screened = false;
    
//    private int weaponsAirToGround;
//    private int nrAirToGoundAttacks = 3;
//  worldUnigue=  only one in the world, factionUnigue= only one at faction, playerUnique =  only one at player.
	private boolean worldUnique=  false, factionUnique = false, playerUnique =  false;
	/*
	public int getNrAirToGoundAttacks() {
		return nrAirToGoundAttacks;
	}

	public void setNrAirToGoundAttacks(int nrAirToGoundAttacks) {
		this.nrAirToGoundAttacks = nrAirToGoundAttacks;
	}

	public int getWeaponsAirToGround() {
		return weaponsAirToGround;
	}

	public void setWeaponsAirToGround(int weaponsAirToGround) {
		this.weaponsAirToGround = weaponsAirToGround;
	}*/

	public SpaceshipType(String name, String shortName, int tonnage, int shields, int hits, SpaceshipRange range, int upkeep, int buildCost, UniqueIdCounter uic, int weaponsStrengthSmall){
    	this(name,shortName,tonnage,shields,hits,range,upkeep,buildCost,uic,weaponsStrengthSmall,0);
    }
    /*
    public static String getHTMLHeaderRow(){
  	  StringBuffer sb = new StringBuffer();
  	  sb.append("<tr>");
  	  sb.append("<td>Name</td>");
  	  sb.append("<td>Short<br>Name</td>");
  	  sb.append("<td>Size</td>");
  	  sb.append("<td>Shiptype</td>");
  	  sb.append("<td>Targeting<br>Type</td>");
  	  sb.append("<td>range</td>");
  	  sb.append("<td>Shields</td>");
  	  sb.append("<td>Hits</td>");
  	  sb.append("<td>Weapons<br>Strength<br>Squadron</td>");
  	  sb.append("<td>Weapons<br>Strength<br>Small</td>");
  	  sb.append("<td>Weapons<br>Strength<br>Medium</td>");
  	  sb.append("<td>Weapons<br>Strength<br>Large</td>");
  	  sb.append("<td>Weapons<br>Strength<br>Huge</td>");
  	  sb.append("<td>Armor<br>Small</td>");
  	  sb.append("<td>Armor<br>Medium</td>");
  	  sb.append("<td>Armor<br>Large</td>");
  	  sb.append("<td>Armor<br>Huge</td>");
  	  sb.append("<td>Income Open<br>o/f/n/e</td>");  	  
  	  sb.append("<td>Income Closed<br>o/f/n/e</td>");  	  
  	  sb.append("<td>Init<br>Bonus</td>");
  	  sb.append("<td>Init<br>Support<br>Bonus</td>");
  	  sb.append("<td>Init<br>Defence</td>");
  	  sb.append("<td>Bombardment</td>");
  	  sb.append("<td></td>");
  	  sb.append("<td>PsychWarfare</td>");
  	  sb.append("<td>Squadron<br>Capacity</td>");
  	  sb.append("<td>Stops<br>Retreat</td>");
  	  sb.append("<td>Supply</td>");
  	  sb.append("<td>Planetary<br>Survey</td>");
  	  sb.append("<td>Can<br>Appear<br>On<br>Black<br>Market</td>");
  	  sb.append("<td>Can<br>attack<br>Screened<br>Ships</td>");
  	  sb.append("<td>Visible<br>on map</td>");
  	//TODO sb.append("<td>Screened</td>");
  	  sb.append("<td>Build<br>Cost</td>");
  	  sb.append("<td>Upkeep</td>");
  	  sb.append("</tr>\n");
  	  return sb.toString();
    }
    */
    /*
    public static String getHTMLHeaderRowNO(){
  	  StringBuffer sb = new StringBuffer();
  	  sb.append("<tr height=1 class='ListheaderRow'><td colspan='38'></td></tr>");
  	  sb.append("<tr class='ListheaderRow' height=16>");
  	  sb.append("<td class='ListHeader' colspan='2'>Ship</td>");
  	  sb.append("<td class='ListHeader' width=1></td>");
  	  sb.append("<td class='ListHeader' colspan='6'>Details</td>");
  	  sb.append("<td class='ListHeader' width=1></td>");
  	  sb.append("<td class='ListHeader' colspan='5'>Weapons Strength</td>");
  	  sb.append("<td class='ListHeader' width=1></td>");
  	  sb.append("<td class='ListHeader' colspan='4'>Armor</td>");
  	  sb.append("<td class='ListHeader' width=1></td>");
  	  sb.append("<td class='ListHeader' colspan='3'>Init</td>");
  	  sb.append("<td class='ListHeader' width=1></td>");
  	  sb.append("<td class='ListHeader' colspan='10'>Additional</td>");
  	  sb.append("<td class='ListHeader' width=1></td>");
  	  sb.append("<td class='ListHeader' colspan='2'>Cost</td>");
  	  sb.append("</tr>\n");

  	  sb.append("<tr class='ListheaderRow' height=16>");
  	  sb.append("<td class='ListMainLeft'>Name</td>");
  	  sb.append("<td class='ListMain'>ID</td>");
  	  sb.append("<td class='ListLine' width=1></td>");
  	  sb.append("<td class='ListMainLeft'>Size</td>");
  	  sb.append("<td class='ListMain'>Type</td>");
  	  sb.append("<td class='ListMain'>Target</td>");
  	  sb.append("<td class='ListMain'>Range</td>");
  	  sb.append("<td class='ListMain'>Shields</td>");
  	  sb.append("<td class='ListMain'>Hits</td>");
  	  sb.append("<td class='ListLine' width=1></td>");
  	  sb.append("<td class='ListMainLeft'>Sqd</td>");
  	  sb.append("<td class='ListMain'>S</td>");
  	  sb.append("<td class='ListMain'>M</td>");
  	  sb.append("<td class='ListMain'>L</td>");
  	  sb.append("<td class='ListMain'>H</td>");
  	  sb.append("<td class='ListLine' width=1></td>");
  	  sb.append("<td class='ListMainLeft'>S</td>");
  	  sb.append("<td class='ListMain'>M</td>");
  	  sb.append("<td class='ListMain'>L</td>");
  	  sb.append("<td class='ListMain'>H</td>");
  	  sb.append("<td class='ListLine' width=1></td>");
  	  sb.append("<td class='ListMainLeft'>Bonus</td>");
  	  sb.append("<td class='ListMain'>Support</td>");
  	  sb.append("<td class='ListMain'>Defence</td>");
  	  sb.append("<td class='ListLine' width='1'></td>");
  	  sb.append("<td class='ListMainLeft'>Bombs</td>");
  	  sb.append("<td class='ListMain'></td>");
  	  sb.append("<td class='ListMain'>Troops</td>");
  	  sb.append("<td class='ListMain'>Squadrons</td>");
  	  sb.append("<td class='ListMain'>Stop Retreat</td>");
  	  sb.append("<td class='ListMain'>Supply</td>");
  	  sb.append("<td class='ListMain'>Survey</td>");
  	  sb.append("<td class='ListMain'>BM</td>");
  	  sb.append("<td class='ListMain'>Attack Screened</td>");
  	  sb.append("<td class='ListMain'>Visible</td>");
  	//TODO sb.append("<td class='ListMain'>Screened</td>");
  	  sb.append("<td class='ListLine' width=1></td>");
  	  sb.append("<td class='ListMainLeft'>Build</td>");
  	  sb.append("<td class='ListMain'>Upkeep</td>");
  	  sb.append("</tr>\n");
  	  return sb.toString();
    }
    */
    /*
    public String getHTMLTableRow(){
  	  StringBuffer sb = new StringBuffer();
  	  sb.append("<tr>");
  	  sb.append("<td>" + name + "</td>");
  	  sb.append("<td>" + shortName + "</td>");
  	  sb.append("<td>" + getSizeString() + "</td>");
  	  if (squadron){
  		  sb.append("<td>Squadron</td>");
  	  }else{
  		  sb.append("<td>Capital ship</td>");
  	  }
  	  sb.append("<td>" + targetingType.toString() + "</td>");
  	  sb.append("<td>" + range + "</td>");
  	  sb.append("<td>" + shields + "</td>");
  	  sb.append("<td>" + hits + "</td>");
  	  sb.append("<td>" + weaponsStrengthSquadron + "</td>");
  	  sb.append("<td>" + weaponsStrengthSmall + "</td>");
  	  sb.append("<td>" + weaponsStrengthMedium + " (" + getWeaponsMaxSalvoesMediumString() + ")</td>");
  	  sb.append("<td>" + weaponsStrengthLarge + " (" + getWeaponsMaxSalvoesLargeString() + ")</td>");
  	  sb.append("<td>" + weaponsStrengthHuge + " (" + getWeaponsMaxSalvoesHugeString() + ")</td>");
  	  sb.append("<td>" + armorSmall + "</td>");
  	  sb.append("<td>" + armorMedium + "</td>");
  	  sb.append("<td>" + armorLarge + "</td>");
  	  sb.append("<td>" + armorHuge + "</td>");
  	  sb.append("<td><nobr>" + getIncomeOpenString() + "</nobr></td>");
  	  sb.append("<td><nobr>" + getIncomeClosedString() + "</nobr></td>");
  	  if (!initSupport){
  		  sb.append("<td>" + increaseInitiative + "</td>");
  	  }else{
  		  sb.append("<td>0</td>");
  	  }
  	  if (initSupport){
  		  sb.append("<td>" + increaseInitiative + "</td>");
  	  }else{
  		  sb.append("<td>0</td>");
  	  }
  	  sb.append("<td>" + initDefence + "</td>");
  	  sb.append("<td>" + bombardment + "</td>");
//  	  sb.append("<td>" + siegeBonus + "</td>");
//  	  sb.append("<td>" + troops + "</td>");
  	  sb.append("<td></td>");
  	  sb.append("<td>" + psychWarfare + "</td>");
  	  sb.append("<td>" + squadronCapacity + "</td>");
  	  sb.append("<td>" + noRetreat + "</td>");
  	  sb.append("<td>" + supply + "</td>");
  	  sb.append("<td>" + planetarySurvey + "</td>");
  	  sb.append("<td>" + canAppearOnBlackMarket + "</td>");
  	  sb.append("<td>" + canAttackScreenedShips + "</td>");
  	  sb.append("<td>" + visibleOnMap + "</td>");
  	//TODO sb.append("<td>" + screened + "</td>");
  	  sb.append("<td>" + buildCost + "</td>");
  	  sb.append("<td>" + upkeep + "</td>");
  	  sb.append("</tr>\n");
  	  return sb.toString();
    }
    */
    /*
    public String getHTMLTableRowNO(){
  	  StringBuffer sb = new StringBuffer();
  	  sb.append("<tr class='TRMain' onMouseOver='OnMouseOver(this)'; onMouseOut='OnMouseOut(this)'; >");
  	  sb.append("<td class='ListMainLeft'>" + name + "</td>");
  	  sb.append("<td class='ListMain'>" + shortName + "</td>");
  	sb.append("<td class='ListLine' width=1></td>");
  	  sb.append("<td class='ListMainLeft'>" + getSizeString() + "</td>");
  	  if (squadron){
  		  sb.append("<td class='ListMain'>Squadron</td>");
  	  }else{
  		  sb.append("<td class='ListMain'>Capital</td>");
  	  }
  	  sb.append("<td class='ListMain'>" + targetingType.toString() + "</td>");
  	  sb.append("<td class='ListMain'>" + range + "</td>");
  	  sb.append("<td class='ListMain'>" + shields + "</td>");
  	  sb.append("<td class='ListMain'>" + hits + "</td>");
  	sb.append("<td class='ListLine' width=1></td>");
  	  sb.append("<td class='ListMainLeft'>" + weaponsStrengthSquadron + "</td>");
  	  sb.append("<td class='ListMain'>" + weaponsStrengthSmall + "</td>");
  	  sb.append("<td class='ListMain'>" + weaponsStrengthMedium + " (" + getWeaponsMaxSalvoesMediumString() + ")</td>");
  	  sb.append("<td class='ListMain'>" + weaponsStrengthLarge + " (" + getWeaponsMaxSalvoesLargeString() + ")</td>");
  	  sb.append("<td class='ListMain'>" + weaponsStrengthHuge + " (" + getWeaponsMaxSalvoesHugeString() + ")</td>");
  	sb.append("<td class='ListLine' width=1></td>");
  	  sb.append("<td class='ListMainLeft'>" + armorSmall + "</td>");
  	  sb.append("<td class='ListMain'>" + armorMedium + "</td>");
  	  sb.append("<td class='ListMain'>" + armorLarge + "</td>");
  	  sb.append("<td class='ListMain'>" + armorHuge + "</td>");
  	sb.append("<td class='ListLine' width=1></td>");
  	  if (!initSupport){
  		  sb.append("<td class='ListMainLeft'>" + increaseInitiative + "</td>");
  	  }else{
  		  sb.append("<td class='ListMainLeft'>0</td>");
  	  }
  	  if (initSupport){
  		  sb.append("<td class='ListMain'>" + increaseInitiative + "</td>");
  	  }else{
  		  sb.append("<td class='ListMain'>0</td>");
  	  }
  	  sb.append("<td class='ListMain'>" + initDefence + "</td>");
  	sb.append("<td class='ListLine' width=1></td>");
  	  sb.append("<td class='ListMainLeft'>" + bombardment + "</td>");
//  	  sb.append("<td class='ListMain'>" + siegeBonus + "</td>");
//  	  sb.append("<td class='ListMain'>" + troops + "</td>");
  	  sb.append("<td class='ListMain'></td>");
  	  sb.append("<td class='ListMain'>" + psychWarfare + "</td>");
  	  sb.append("<td class='ListMain'>" + squadronCapacity + "</td>");
  	  sb.append("<td class='ListMain'>" + noRetreat + "</td>");
  	  sb.append("<td class='ListMain'>" + supply + "</td>");
  	  sb.append("<td class='ListMain'>" + planetarySurvey + "</td>");
  	  sb.append("<td class='ListMain'>" + canAppearOnBlackMarket + "</td>");
  	  sb.append("<td class='ListMain'>" + canAttackScreenedShips + "</td>");
  	  sb.append("<td class='ListMain'>" + visibleOnMap + "</td>");
  	//TODO sb.append("<td class='ListMain'>" + screened + "</td>");
  	sb.append("<td class='ListLine' width=1></td>");
  	  sb.append("<td class='ListMainLeft'>" + buildCost + "</td>");
  	  sb.append("<td class='ListMain'>" + upkeep + "</td>");
  	  sb.append("</tr>\n");
  	  return sb.toString();
    }
    */
	
	@JsonIgnore
    public String getIncomeOpenString(){
    	StringBuffer sb = new StringBuffer();
        if (getIncOwnOpenBonus() > 0){
            sb.append(getIncOwnOpenBonus());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (getIncFrendlyOpenBonus() > 0){
            sb.append(getIncFrendlyOpenBonus());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (getIncNeutralOpenBonus() > 0){
            sb.append(getIncNeutralOpenBonus());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (getIncEnemyOpenBonus() > 0){
            sb.append(getIncEnemyOpenBonus());
        }else{
        	sb.append("-");
        }
    	return sb.toString();
    }
	@JsonIgnore
    public String getIncomeClosedString(){
    	StringBuffer sb = new StringBuffer();
        if (getIncOwnClosedBonus() > 0){
            sb.append(getIncOwnClosedBonus());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (getIncFrendlyClosedBonus() > 0){
            sb.append(getIncFrendlyClosedBonus());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (getIncNeutralClosedBonus() > 0){
            sb.append(getIncNeutralClosedBonus());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (getIncEnemyClosedBonus() > 0){
            sb.append(getIncEnemyClosedBonus());
        }else{
        	sb.append("-");
        }
    	return sb.toString();
    }
    
    /**
     * Constructor for civilian ships
     * @param name
     * @param shortName
     * @param tonnage
     * @param range
     * @param upkeep
     * @param buildCost
     * @param uic
     */
    public SpaceshipType(String name, String shortName, int tonnage, SpaceshipRange range, int upkeep, int buildCost, UniqueIdCounter uic){
    	this(name,shortName,tonnage,0,1,range,upkeep,buildCost,uic,0,0);
    	setCivilian(true);
    	setLookAsCivilian(true);
    	setCanBlockPlanet(false);
        setArmor(0,0,0,0);
    }
	
    public SpaceshipType(String name, String shortName, int tonnage, int shields, int hits, SpaceshipRange range, int upkeep, int buildCost, UniqueIdCounter uic, int weaponsStrengthSmall, int weaponsStrengthSquadron){
        this.name = name;
        this.shortName = shortName;
        this.tonnage = tonnage;
        this.range = range;
        this.shields = shields;
        this.upkeep = upkeep;
        this.buildCost = buildCost;
        this.hits = hits;
        this.weaponsStrengthSmall = weaponsStrengthSmall;
        this.weaponsStrengthSquadron = weaponsStrengthSquadron;
        // init
        nrProduced = 0;
        this.uic = uic;
        // default armor
        setStandardArmorLevels();
        // Default (almost) infinite number of salvoes
        this.weaponsMaxSalvoesMedium = Integer.MAX_VALUE;
        this.weaponsMaxSalvoesLarge = Integer.MAX_VALUE;
        this.weaponsMaxSalvoesHuge = Integer.MAX_VALUE;
        // default targeting type
        targetingType = SpaceshipTargetingType.ANTIMBU;
        
        if(tonnage == SIZE_SMALL){
        	blackmarketFirstTurn = 1;
        	bluePrintFirstTurn =  1;
        }else if(tonnage == SIZE_MEDIUM){
        	blackmarketFirstTurn = 6;
        	bluePrintFirstTurn =  6;
        }else if(tonnage == SIZE_LARGE){
        	blackmarketFirstTurn = 9;
        	bluePrintFirstTurn =  12;
        }else{//SIZE_HUGE
        	blackmarketFirstTurn = 15;
        	bluePrintFirstTurn =  20;
        }
        
        
    }    	

    public SpaceshipType(SpaceshipType oldsst){
        this.name = oldsst.getName();
        this.shortName = oldsst.getShortName();
        this.tonnage = oldsst.getTonnage();
        this.range = oldsst.getRange();
        this.shields = oldsst.getShields();
        this.upkeep = oldsst.getUpkeep();
        this.buildCost = oldsst.getBuildCost(null);
        this.bombardment = oldsst.getBombardment();
        this.noRetreat = oldsst.getNoRetreat();
        this.hits = oldsst.getHits();
        this.setInitSupport(oldsst.getInitSupport());
        if (oldsst.getInitSupport()){
        	this.increaseInitiative = oldsst.getInitSupportBonus();
        }else{
        	this.increaseInitiative = oldsst.getInitiativeBonus();
        }
        this.initDefence = oldsst.getInitDefence();
        this.weaponsStrengthSmall = oldsst.getWeaponsStrengthSmall(); 
        this.weaponsStrengthMedium = oldsst.getWeaponsStrengthMedium();
        this.weaponsStrengthLarge = oldsst.getWeaponsStrengthLarge(); 
        this.weaponsStrengthHuge = oldsst.getWeaponsStrengthHuge();
        this.weaponsMaxSalvoesMedium = oldsst.getWeaponsMaxSalvoesMedium();
        this.weaponsMaxSalvoesLarge = oldsst.getWeaponsMaxSalvoesLarge();
        this.weaponsMaxSalvoesHuge = oldsst.getWeaponsMaxSalvoesHuge();
        this.supply = oldsst.getMaxResupply();
        nrProduced = 0;
        this.uic = oldsst.getUniqueIdCounter();
        this.armorSmall = oldsst.getArmorSmall();
        this.armorMedium = oldsst.getArmorMedium();
        this.armorLarge = oldsst.getArmorLarge();
        this.armorHuge = oldsst.getArmorHuge();
        this.planetarySurvey = oldsst.isPlanetarySurvey();
//        this.siegeBonus = oldsst.getSiegeBonus();
//        this.troops = oldsst.getTroops();
        this.psychWarfare = oldsst.getPsychWarfare();
        this.targetingType = oldsst.getTargetingType();
        this.squadronCapacity = oldsst.getSquadronCapacity();
        this.weaponsStrengthSquadron = oldsst.getWeaponsStrengthSquadron();
        this.squadron = oldsst.isSquadron();
        this.description = oldsst.getDescription();
        this.history = oldsst.getHistory();
        this.incEnemyClosedBonus = oldsst.incEnemyClosedBonus;
        this.incEnemyOpenBonus = oldsst.incEnemyOpenBonus;
        this.incFrendlyClosedBonus = oldsst.incFrendlyClosedBonus;
        this.incFrendlyOpenBonus = oldsst.incFrendlyOpenBonus;
        this.incNeutralClosedBonus = oldsst.incNeutralClosedBonus;
        this.incNeutralOpenBonus = oldsst.incNeutralOpenBonus;
        this.incOwnClosedBonus = oldsst.incOwnClosedBonus;
        this.incOwnOpenBonus = oldsst.incOwnOpenBonus;
        this.canAttackScreenedShips = oldsst.canAttackScreenedShips;
        this.civilian = oldsst.civilian;
        this.lookAsCivilian = oldsst.lookAsCivilian;
        this.canBlockPlanet = oldsst.canBlockPlanet;
        this.visibleOnMap = oldsst.isVisibleOnMap();
      //TODO this.screened = oldsst.isScreened();
        this.availableToBuild = oldsst.isAvailableToBuild();
        this.troopCapacity = oldsst.getTroopCapacity();
        this.troopLaunchCapacity = oldsst.getTroopLaunchCapacity();
     //   this.weaponsAirToGround = oldsst.getWeaponsAirToGround();
     //   this.nrAirToGoundAttacks = oldsst.getNrAirToGoundAttacks();
        this.worldUnique = oldsst.isWorldUnique();
        this.factionUnique = oldsst.isFactionUnique();
        this.playerUnique = oldsst.isPlayerUnique();
        this.alwaysRetreat = oldsst.isAlwaysRetreat();
        this.screened = oldsst.isScreened();
        
        this.advanteges = oldsst.getAdvanteges();
        this.disadvanteges = oldsst.getDisAdvanteges();
        this.canAppearOnBlackMarket = oldsst.isCanAppearOnBlackMarket();
        this.blackMarketFrequency = oldsst.getBlackMarketFrequency();
        this.blackmarketFirstTurn = oldsst.getBlackmarketFirstTurn();
        this.bluePrintFirstTurn = oldsst.getBluePrintFirstTurn();
        this.bluePrintFrequency = oldsst.getBluePrintFrequency();
    }

    public int getSlots(){
      int slots = 1;
      if ((tonnage >300) & (tonnage <=600)){
        slots = 2;
      }else
      if ((tonnage >600) & (tonnage <=900)){
        slots = 3;
      }else
      if (tonnage >900){
        slots = 5;
      }
      return slots;
    }

    public String getSizeString(){
      String sizeString = "small";
      if ((tonnage > 300) & (tonnage <= 600)){
        sizeString = "medium";
      }else
      if ((tonnage > 600) & (tonnage <= 900)){
        sizeString = "large";
      }else
      if (tonnage > 900){
        sizeString = "huge";
      }
      return sizeString;
    }

    public int getBombardment(){
      return bombardment;
    }

    public int getPsychWarfare(){
      return psychWarfare;
    }

    public int getUpkeep(){
      return upkeep;
    }
    
    public boolean getInitSupport(){
    	return initSupport;
    }

    public void setBuildCost(int buildCost){
    	this.buildCost = buildCost;
    }
    
    @JsonIgnore
    public int getBuildCost(VIP vipWithBonus){
      int tempBuildCost = buildCost;
      if (vipWithBonus != null){
    	  int vipBuildbonus = 100 - vipWithBonus.getShipBuildBonus();
    	  double tempBuildBonus = vipBuildbonus / 100.0;
    	  tempBuildCost = (int) Math.round(tempBuildCost * tempBuildBonus);
    	  if (tempBuildCost < 1){
    		  tempBuildCost = 1;
    	  }
      }
      return tempBuildCost;
    }
    
    @JsonIgnore
    public UniqueIdCounter getUniqueIdCounter(){
      return uic;
    }

    @JsonIgnore
    public Spaceship getShip(VIP vipWithBonus, int factionTechBonus, int buildingBonus){
      nrProduced++;
      return new Spaceship(Functions.deepClone(this),null,nrProduced,uic.getUniqueId(),vipWithBonus,factionTechBonus,buildingBonus);
    }
/*
 * public Spaceship getVirtualShip(){ return new
 * Spaceship(this,null,nrProduced,0,false); }
 */
    public int getTonnage(){
      return tonnage;
    }

    public SpaceshipRange getRange(){
      return range;
    }

    public int getHits(){
      return hits;
    }

    public int getShields(){
      return shields;
    }
/*
 * public int getRealWeapons(){ return getVirtualShip().getWeapons(); }
 * 
 * public int getRealShields(){ return getVirtualShip().getShields(); }
 * 
 * public int getRealDamageCapacity(){ return
 * getVirtualShip().getDamageCapacity(); }
 */
    public String getName(){
      return name;
    }

    public String getShortName(){
      return shortName;
    }

    public int getNrProduced(){
      return nrProduced;
    }
/*
 * public int getDamageCapacity(){ return damagecapacity; }
 */
    public int getInitiativeBonus(){
    	int tmpInitBonus = 0;
    	if (!initSupport){
    		tmpInitBonus = increaseInitiative;
    	}
    	return tmpInitBonus;
    }

    public int getInitSupportBonus(){
    	int tmpInitSupportBonus = 0;
    	if (initSupport){
    		tmpInitSupportBonus = increaseInitiative;
    	}
    	return tmpInitSupportBonus;
    }

    public int getInitDefence(){
        return initDefence;
      }

    @JsonIgnore
    public String getRangeString(){
/*
 * String returnValue = "short"; if (range == LONG){ returnValue = "long"; }else
 * if (range == NONE){ returnValue = "none"; } return returnValue;
 */
    	return range.toLowercaseString();
    }

    public boolean getNoRetreat(){
      return noRetreat;
    }

    public void setNoRetreat(boolean newValue){
      noRetreat = newValue;
    }

    @JsonIgnore
    public String getNoRetreatString(){
      String returnString = "No";
      if (noRetreat){
        returnString = "Yes";
      }
      return returnString;
    }

	public int getWeaponsMaxSalvoesHuge() {
		return weaponsMaxSalvoesHuge;
	}
	
	public int getWeaponsMaxSalvoesLarge() {
		return weaponsMaxSalvoesLarge;
	}
	
	public int getWeaponsMaxSalvoesMedium() {
		return weaponsMaxSalvoesMedium;
	}

	@JsonIgnore
	public String getWeaponsMaxSalvoesMediumString() {
		String retStr = "0";
		if ((weaponsMaxSalvoesMedium > 0) & (weaponsMaxSalvoesMedium < Integer.MAX_VALUE)){
			retStr = String.valueOf(weaponsMaxSalvoesMedium);
		}
		return retStr;
	}

	@JsonIgnore
	public String getWeaponsMaxSalvoesLargeString() {
		String retStr = "0";
		if ((weaponsMaxSalvoesLarge > 0) & (weaponsMaxSalvoesLarge < Integer.MAX_VALUE)){
			retStr = String.valueOf(weaponsMaxSalvoesLarge);
		}
		return retStr;
	}

	@JsonIgnore
	public String getWeaponsMaxSalvoesHugeString() {
		String retStr = "0";
		if ((weaponsMaxSalvoesHuge > 0) & (weaponsMaxSalvoesHuge < Integer.MAX_VALUE)){
			retStr = String.valueOf(weaponsMaxSalvoesHuge);
		}
		return retStr;
	}

	public int getWeaponsStrengthHuge() {
		return weaponsStrengthHuge;
	}
	
	public int getWeaponsStrengthLarge() {
		return weaponsStrengthLarge;
	}
	
	public int getWeaponsStrengthMedium() {
		return weaponsStrengthMedium;
	}
	
	public int getWeaponsStrengthSmall() {
		return weaponsStrengthSmall;
	}
	
	public int getMaxResupply(){
		return supply;
	}
	
	public String getMaxResupplyString(){
		String retStr = "none";
		switch (supply){
			case 1: retStr = "small";
					break;
			case 2: retStr = "medium";
					break;
			case 3: retStr = "large";
					break;
			case 4: retStr = "huge";
					break;
		}
		return retStr;
	}

	public boolean isSquadron() {
		return squadron;
	}
	
	public void setSquadron(boolean newValue){
		squadron = newValue;
	}

	public boolean isCanAppearOnBlackMarket() {
		return canAppearOnBlackMarket;
	}

	public void setCanAppearOnBlackMarket(boolean canAppearOnBlackMarket) {
		this.canAppearOnBlackMarket = canAppearOnBlackMarket;
	}

	public int getArmorHuge() {
		return armorHuge;
	}

	public void setArmorHuge(int armorHuge) {
		this.armorHuge = armorHuge;
	}

	public int getArmorLarge() {
		return armorLarge;
	}

	public void setArmorLarge(int armorLarge) {
		this.armorLarge = armorLarge;
	}

	public int getArmorMedium() {
		return armorMedium;
	}

	public void setArmorMedium(int armorMedium) {
		this.armorMedium = armorMedium;
	}

	public int getArmorSmall() {
		return armorSmall;
	}

	public void setArmorSmall(int armorSmall) {
		this.armorSmall = armorSmall;
	}
	
	public void setStandardArmorLevels(){
		if (tonnage == SIZE_MEDIUM){
			armorSmall = 25;
		}else
		if (tonnage == SIZE_LARGE){
			armorSmall = 50;
			armorMedium = 25;
		}else
		if (tonnage == SIZE_HUGE){
			armorSmall = 75;
			armorMedium = 50;
			armorLarge = 25;
		}
	}

	public boolean isPlanetarySurvey() {
		return planetarySurvey;
	}

	public void setPlanetarySurvey(boolean planetarySurvey) {
		this.planetarySurvey = planetarySurvey;
	}
/*
	public int getSiegeBonus() {
		return siegeBonus;
	}

	public void setSiegeBonus(int siegeBonus) {
		this.siegeBonus = siegeBonus;
	}
*/
	public int getSupply() {
		return supply;
	}

	public void setSupply(int supply) {
		this.supply = supply;
	}

	public void setBombardment(int bombardment) {
		this.bombardment = bombardment;
	}

	public void setIncreaseInitiative(int increaseInitiative) {
		this.increaseInitiative = increaseInitiative;
	}
	
	public int getIncreaseInitiative() {
		return this.increaseInitiative;
	}

	public void setInitDefence(int initDefence) {
		this.initDefence = initDefence;
	}

	public void setInitSupport(boolean initSupport) {
		this.initSupport = initSupport;
	}

	public void setTroops(int newPsychWarfare) {
		this.psychWarfare = newPsychWarfare;
	}

	public void setWeaponsMaxSalvoesHuge(int weaponsMaxSalvoesHuge) {
		this.weaponsMaxSalvoesHuge = weaponsMaxSalvoesHuge;
	}

	public void setWeaponsMaxSalvoesLarge(int weaponsMaxSalvoesLarge) {
		this.weaponsMaxSalvoesLarge = weaponsMaxSalvoesLarge;
	}

	public void setWeaponsMaxSalvoesMedium(int weaponsMaxSalvoesMedium) {
		this.weaponsMaxSalvoesMedium = weaponsMaxSalvoesMedium;
	}

	public void setWeaponsStrengthHuge(int weaponsStrengthHuge) {
		this.weaponsStrengthHuge = weaponsStrengthHuge;
	}

	public void setWeaponsStrengthLarge(int weaponsStrengthLarge) {
		this.weaponsStrengthLarge = weaponsStrengthLarge;
	}

	public void setWeaponsStrengthMedium(int weaponsStrengthMedium) {
		this.weaponsStrengthMedium = weaponsStrengthMedium;
	}
	
	public void setWeaponsStrengthSmall(int weaponsStrengthSmall) {
		this.weaponsStrengthSmall = weaponsStrengthSmall;
	}

	public int getSquadronCapacity() {
		return squadronCapacity;
	}

	public void setSquadronCapacity(int squadronCapacity) {
		this.squadronCapacity = squadronCapacity;
	}

	public int getWeaponsStrengthSquadron() {
		return weaponsStrengthSquadron;
	}

	public void setWeaponsStrengthSquadron(int weaponsStrengthSquadron) {
		this.weaponsStrengthSquadron = weaponsStrengthSquadron;
	}
	
	public void setTargetingType(SpaceshipTargetingType aTargetingType){
		targetingType = aTargetingType;
	}
	
	@JsonIgnore
	public SpaceshipTargetingType getTargetingType(){
		return targetingType;
	}
		
	public String getShipType(){
		String type = "Capital Ship";
		if (isSquadron()){
			if (targetingType == SpaceshipTargetingType.ANTIAIR){
				type = "Fighter Squadron";
			}else
			if (targetingType == SpaceshipTargetingType.ANTIMBU){
				type = "Bomber Squadron";
			}else{ // ALLROUND
				type = "Multirole Squadron";
			}
		}
		return type;
	}
	
	public SpaceshipType copy(){
		return new SpaceshipType(this);
	}

	public boolean isDefenceShip(){
		return !isCivilian() & !squadron & (range == SpaceshipRange.NONE); 
	}
	
	@JsonIgnore
	public String getTotalDescription(){
		String totalDescription = "";
    	
		if(advantages != null && !advantages.equals("")){
    		totalDescription += "Advantages: " + advantages + "\n\n";
        }
    	if(disadvantages != null && !disadvantages.equals("")){
    		totalDescription += "Disadvantages: " + disadvantages + "\n\n";
        }
    	
    	if(shortDescription != null && !shortDescription.equals("")){
    		totalDescription += "Short Description\n";
        	totalDescription += shortDescription + "\n\n";
    	}
    	
    	if(description != null && !description.equals("")){
    		totalDescription +="Description\n";
        	totalDescription += description + "\n\n";
    	}
    	if(history != null && !history.equals("")){
    		totalDescription +="History\n";
        	totalDescription += history;
    	}
    	
    	return totalDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public int getIncEnemyClosedBonus() {
		return incEnemyClosedBonus;
	}

	public void setIncEnemyClosedBonus(int incEnemyClosedBonus) {
		this.incEnemyClosedBonus = incEnemyClosedBonus;
	}

	public int getIncEnemyOpenBonus() {
		return incEnemyOpenBonus;
	}

	public void setIncEnemyOpenBonus(int incEnemyOpenBonus) {
		this.incEnemyOpenBonus = incEnemyOpenBonus;
	}

	public int getIncFrendlyClosedBonus() {
		return incFrendlyClosedBonus;
	}

	public void setIncFrendlyClosedBonus(int incFrendlyClosedBonus) {
		this.incFrendlyClosedBonus = incFrendlyClosedBonus;
	}

	public int getIncFrendlyOpenBonus() {
		return incFrendlyOpenBonus;
	}

	public void setIncFrendlyOpenBonus(int incFrendlyOpenBonus) {
		this.incFrendlyOpenBonus = incFrendlyOpenBonus;
	}

	public int getIncNeutralClosedBonus() {
		return incNeutralClosedBonus;
	}

	public void setIncNeutralClosedBonus(int incNeutralClosedBonus) {
		this.incNeutralClosedBonus = incNeutralClosedBonus;
	}

	public int getIncNeutralOpenBonus() {
		return incNeutralOpenBonus;
	}

	public void setIncNeutralOpenBonus(int incNeutralOpenBonus) {
		this.incNeutralOpenBonus = incNeutralOpenBonus;
	}

	public int getIncOwnClosedBonus() {
		return incOwnClosedBonus;
	}

	public void setIncOwnClosedBonus(int incOwnClosedBonus) {
		this.incOwnClosedBonus = incOwnClosedBonus;
	}

	public int getIncOwnOpenBonus() {
		return incOwnOpenBonus;
	}

	public void setIncOwnOpenBonus(int incOwnOpenBonus) {
		this.incOwnOpenBonus = incOwnOpenBonus;
	}

	public boolean isCanAttackScreenedShips() {
		return canAttackScreenedShips;
	}

	public void setCanAttackScreenedShips(boolean canAttackScreenedShips) {
		this.canAttackScreenedShips = canAttackScreenedShips;
	}

	public boolean isCanBlockPlanet() {
		return canBlockPlanet;
	}

	public void setCanBlockPlanet(boolean canBlockPlanet) {
		this.canBlockPlanet = canBlockPlanet;
	}

	public boolean isCivilian() {
		return civilian;
	}

	public void setCivilian(boolean civilian) {
		this.civilian = civilian;
	}

	public boolean isLookAsCivilian() {
		return lookAsCivilian;
	}

	public void setLookAsCivilian(boolean lookAsCivilian) {
		this.lookAsCivilian = lookAsCivilian;
	}
	
	public void setArmor(int... armorValues){
		armorSmall = armorValues[0];
		if (armorValues.length > 1){
			armorMedium = armorValues[1];
			if (armorValues.length > 2){
				armorLarge = armorValues[2];
				if (armorValues.length > 3){
					armorHuge = armorValues[3];
				}
			}
		}
	}

	public boolean isVisibleOnMap() {
		return visibleOnMap;
	}

	public void setVisibleOnMap(boolean visibleOnMap) {
		this.visibleOnMap = visibleOnMap;
	}

	public boolean isAvailableToBuild() {
		return availableToBuild;
	}

	public void setAvailableToBuild(boolean availableToBuild) {
		this.availableToBuild = availableToBuild;
	}
	public void setShields(int inShield){
		shields = inShield;
	}

	public void setUpkeep(int upkeep) {
		this.upkeep = upkeep;
	}
	
	public void setSpaceshipRange(SpaceshipRange range) {
		this.range = range;
	}
	
	public SpaceshipRange getSpaceshipRange() {
		return range;
	}

	public int getAimBonus() {
		return aimBonus;
	}

	public void setAimBonus(int aimBonus) {
		this.aimBonus = aimBonus;
	}

	public int getTroopCapacity() {
		return troopCapacity;
	}

	public void setTroopCapacity(int troopCapacity) {
		this.troopCapacity = troopCapacity;
		this.troopLaunchCapacity = troopCapacity;
	}

	public int getTroopLaunchCapacity() {
		return troopLaunchCapacity;
	}
/*
	public void setTroopLaunchCapacity(int troopLaunchCapacity) {
		this.troopLaunchCapacity = troopLaunchCapacity;
	}*/

	
	public boolean isWorldUnique() {
		return worldUnique;
	}

	public void setWorldUnique(boolean worldUnique) {
		this.worldUnique = worldUnique;
	}
	
	public boolean isFactionUnique() {
		return factionUnique;
	}

	public void setFactionUnigue(boolean factionUnique) {
		this.factionUnique = factionUnique;
	}
	
	public boolean isPlayerUnique() {
		return playerUnique;
	}
	
	public String getUniqueString(){
		String uniqueString = "";
  
		if(playerUnique){
			uniqueString = "Player unique";
		}else
		if(factionUnique){
			uniqueString = "Faction unique";
		}else
		if(worldUnique){
			uniqueString = "World unique";
		}
  
		return uniqueString;
	}

	public boolean isPlayerUniqueBuild(Player aPlayer) {
		return aPlayer.getGalaxy().spaceshipTypeExist(this, null, aPlayer);
	}

	public boolean isWorldUniqueBuild(Galaxy aGalaxy) {
		return aGalaxy.spaceshipTypeExist(this, null, null);
	}

	public boolean isFactionUniqueBuild(Player aPlayer) {
		return aPlayer.getGalaxy().spaceshipTypeExist(this, aPlayer.getFaction(), null);
	}

	public void setFactionUnique(boolean factionUnique) {
		this.factionUnique = factionUnique;
	}

	public void setPlayerUnique(boolean playerUnique) {
		this.playerUnique = playerUnique;
	}
	
	public boolean isConstructible(Player aPlayer){
	//	LoggingHandler.fine("isConstructible aPlayer: " + aPlayer.getName() + " SpaceType namn: " + name);
	//	LoggingHandler.fine("isWorldUnique isFactionUnique isPlayerUnique : " + isWorldUnique() + " " + isFactionUnique() + " " +isPlayerUnique());
		
		boolean constructible =  true;
		if(!isAvailableToBuild()){
			constructible = false;
		}else if((isWorldUnique() && isWorldUniqueBuild(aPlayer.getGalaxy())) || (isFactionUnique() && isFactionUniqueBuild(aPlayer)) || (isPlayerUnique() && isPlayerUniqueBuild(aPlayer))){
			constructible = false;
		}else if(isWorldUnique() || isFactionUnique() || isPlayerUnique()){
		//	LoggingHandler.fine("isWorldUnique/isFactionUnique/isPlayerUnique check orders ");
			// check if a build order already exist
			if(aPlayer.getOrders().haveSpaceshipTypeBuildOrder(this)){
				constructible = false;
			}
			for (BlackMarketOffer aBlackMarketOffer : aPlayer.getGalaxy().getBlackMarket().getCurrentOffers()) {
				if(aBlackMarketOffer.isShip() && aBlackMarketOffer.getOfferedShiptype().getName().equals(name)){
					constructible = false;
				}
			}
		}
		return constructible;
	}
	
	@JsonIgnore
	public boolean isReadyToUseInBlackMarket(Galaxy aGalaxy){
		boolean constructible =  false;
		
		if (aGalaxy.getTurn() >= getBlackmarketFirstTurn()){
			if (getRange().canMove() | isSquadron()){
				if (isCanAppearOnBlackMarket()){
					if(!isPlayerUnique() && !isFactionUnique()){
						if(isWorldUnique() && !isWorldUniqueBuild(aGalaxy)){
							boolean isAlreadyAoffer = false;
							for (BlackMarketOffer aBlackMarketOffer : aGalaxy.getBlackMarket().getCurrentOffers()) {
								if(aBlackMarketOffer.isShip() && aBlackMarketOffer.getOfferedShiptype().getName().equals(name)){
									isAlreadyAoffer = true;
								}
							}
							if(!isAlreadyAoffer){
								boolean haveBuildingOrder = false;
								for (Player tempPlayer : aGalaxy.getPlayers()) {
									if(tempPlayer.getOrders().haveSpaceshipTypeBuildOrder(this)){
										haveBuildingOrder = true;
									}
								}
								if(!haveBuildingOrder){
									constructible =  true;
								}
							}
						}else{
							constructible = true;
						}
					}
				}
			}
		}
		return constructible;
	}
	
	@JsonIgnore
	public boolean isBluePrintReadyToUseInBlackMarket(Galaxy aGalaxy){
		boolean constructible =  false;
		
		if (aGalaxy.getTurn() >= getBluePrintFirstTurn()){
			if (bluePrintFrequency != BlackMarketFrequency.NEVER){
				if(!isPlayerUnique() && !isFactionUnique() && !isWorldUnique()){
					constructible = true;
				}
			}
		}
		
		return constructible;
	}
	
	public void setPsychWarfare(int newPsychWarfare) {
		psychWarfare = newPsychWarfare;
	}

	public int getBlackmarketFirstTurn() {
		return blackmarketFirstTurn;
	}

	public void setBlackmarketFirstTurn(int blackmarketFirstTurn) {
		this.blackmarketFirstTurn = blackmarketFirstTurn;
	}

	public int getBluePrintFirstTurn() {
		return bluePrintFirstTurn;
	}

	public void setBluePrintFirstTurn(int bluePrintFirstTurn) {
		this.bluePrintFirstTurn = bluePrintFirstTurn;
	}

	public BlackMarketFrequency getBluePrintFrequency() {
		return bluePrintFrequency;
	}

	public void setBluePrintFrequency(BlackMarketFrequency bluePrintFrequency) {
		this.bluePrintFrequency = bluePrintFrequency;
	}

	public String getAdvanteges() {
		return advanteges;
	}

	public void setAdvanteges(String advanteges) {
		this.advanteges = advanteges;
	}
	public String getDisAdvanteges() {
		return disadvanteges;
	}

	public void setDisAdvanteges(String disadvanteges) {
		this.disadvanteges = disadvanteges;
	}
	
	public BlackMarketFrequency getBlackMarketFrequency() {
		return blackMarketFrequency;
	}

	public void setBlackMarketFrequency(BlackMarketFrequency blackMarketFrequency) {
		this.blackMarketFrequency = blackMarketFrequency;
	}

    public boolean isAlwaysRetreat() {
		return alwaysRetreat;
	}

	public void setAlwaysRetreat(boolean alwaysRetreat) {
		this.alwaysRetreat = alwaysRetreat;
	}

	public boolean isScreened() {
		return screened;
	}

	public void setScreened(boolean screened) {
		this.screened = screened;
	}
	
	public boolean isCapitalShip(){
		return !isCivilian() & !isSquadron() & (getRange() != SpaceshipRange.NONE);
	}
	
	public boolean isCarrier() {
		return getSquadronCapacity() > 0;
	}

	/**
	 * VIP transport is defined by this method as a small civilian ship who have no special abilities
	 */
	public boolean isVIPTransport() {
		return isCivilian() & !isTrader() & !isSmuggler() & !isPlanetarySurvey() & (supply == 0);
	}

	/**
	 * Trader ships is defined by this method as a civilian ship who have bonus in at least one open planet income bonus
	 */
	public boolean isTrader() {
		return isCivilian() & (incEnemyOpenBonus > 0) | (incNeutralOpenBonus > 0) | (incFrendlyOpenBonus > 0) | (incOwnOpenBonus > 0);
	}

	/**
	 * Smuggler ships is defined by this method as a civilian ship who have bonus in at least one closed planet income bonus
	 */
	public boolean isSmuggler() {
		return isCivilian() & ((incEnemyClosedBonus > 0) | (incNeutralClosedBonus > 0) | (incFrendlyClosedBonus > 0) | (incOwnClosedBonus > 0));
	}
	
	/**
	 * Any ship that have psych warfare > 0
	 * @return true if psych warfare > 0
	 */
	/*public boolean isPsychWarfare(){
		return psychWarfare > 0;
	}*/

    /**
     * @return true if small capital psych warfare shiptype
     */
    public boolean isSmallPsychWarfareShiptype(){
    	return isCapitalShip() & psychWarfare > 0 & (getSlots() == 1) & !isCarrier(); // !isCarrier() is added to exclude Bug small meteorite 
    }

}
