package sr.world;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sr.enums.BlackMarketFrequency;

/* 
 * Defines a type of VIP
 */
public class VIPType implements Serializable {
  static final long serialVersionUID = 1L;
  private UniqueIdCounter uic;

  // Required values
  private String name; // required, must be unique
  private String shortName; // required, must be unique

  private String advanteges;
  
  // Alignment
  private Alignment alignment = null; 	
  // determines who can have this VIP
  // if duellist is true - determines if this duellist will  
  // attack another duellist in the same system
  private BlackMarketFrequency frequency = BlackMarketFrequency.COMMON; // how often is this VIPType found. Default => common => 100
  
  // Abilities
  private int assassination; // chance to assassinate
  private int counterEspionage; // chance to catch other spies and assassins
  private int exterminator; // chance to catch other spies and assassins
  private boolean spying; // can spy on planets
  private boolean immuneToCounterEspionage; // can not be caught by counter espionage
  private int initBonus; // % bonus
  private int initSupportBonus; // % bonus (not used - yet?), should not be used together with squadrons
  private int initDefence; // % bonus, should not be used together with squadrons
  private int initFighterSquadronBonus; // % bonus
  private int antiAirBonus; // % bonus to AA damage, not used (yet?)
  private int psychWarfareBonus; // bonus to resistance decrease of besieged planet
//  private int siegeBonus; // bonus to resistance decrease of besieged planet
  private int resistanceBonus; // bonus to resistance on planet
  private int shipBuildBonus; // decreases build cost of ship
  private int troopBuildBonus; // decreases build cost of troops
//  private int vipBuildBonus; // decreases build cost of VIPs. (NOT IN USE)
  private int buildingBuildBonus; // decreases build cost of Bulidings
  private int techBonus; // increased shields & weapons for ships and troops built (%)
  private int openIncBonus; // increase income on open planet
  private int closedIncBonus; // increase income on closed planet
  private boolean canVisitEnemyPlanets = false;
  private boolean canVisitNeutralPlanets = false;
  private int duellist; // may duel with other duellists, value = skill
  private boolean hardToKill; // cannot be killed by ship destroyed or planet conquered/razed
  private boolean wellGuarded; // cannot be assassinated
  private boolean governor; // player looses if a vip with this ability dies
  private boolean FTLbonus; // enables a short range ship to travel long range - not implemented (yet?)
  private boolean diplomat; // can persuade neutral planets
  private boolean infestate; // can infestate neutral planets
  private boolean showOnOpenPlanet; // makes this vip visible to other players if on an open planet
  private String description,howToPlay; 
  private int aimBonus;
//  private int troopAttacksBonus;
  private int landBattleGroupAttackBonus; // bonus percentage to all own troops attack strength in a landbattle. Not cimulative.
  // tillagda av Paul 100511
  private boolean stealth; // makes a ship invisible on the map
  private int bombardmentBonus; // increase bombardment for a fleet (with a bombardment of at least 1)
  private boolean attackScreenedSquadron; // enables a squadron to attack screened enemy ships 
  private boolean attackScreenedCapital; // enables a capital ship to attack screened enemy ships
  private boolean planetarySurvey; // on a ship, act as spy on closed planets  

  public static final int DUELLIST_APPRENTICE = 30; 
  public static final int DUELLIST_VETERAN = 50; 
  public static final int DUELLIST_MASTER = 70; 
  
  
  
  private int buildCost = 0; 
  private int upkeep = 0;
  private boolean availableToBuild = true;
  // private boolean availableToBuild = false; skall vara false om forskning på VIPar skall tillåtas
  
  // worldUnigue=  only one in the world, factionUnigue= only one at faction, playerUnique =  only one at player.
  private boolean worldUnique = false, factionUnique = false, playerUnique = false;
	
  // Other fields
  //private boolean unique; // VIPType is unique - can only be found once NOT USED YET
  //private boolean uniqueHasBeenFound = false; // always start as false. Becomes true when a vip of this type has been found. NOT USED YET

  public VIPType(String name,String shortName,Alignment alignment, UniqueIdCounter uic){
    this.name = name;
    this.shortName = shortName;
    this.alignment = alignment;
    this.uic = uic;
  }
  
  @JsonIgnore
  public String getHTMLTableContent(){
	  StringBuffer sb = new StringBuffer();
	  sb.append("<table border=\"0\" cellspacing=\"4\" cellpadding=\"0\" class=\"sr\">\n");
//	  sb.append("<tr><td>&nbsp;</td></tr>\n");
	  sb.append("<tr>");
	  sb.append("<td>Name</td>");
	  sb.append("<td>Short Name</td>");
	  sb.append("<td>Alignment</td>");
	  sb.append("<td>Frequency</td>");
	  sb.append("</tr>\n");
	  sb.append("<tr>");
	  sb.append("<td>" + name + "</td>");
	  sb.append("<td>" + shortName + "</td>");
	  sb.append("<td>" + alignment.toString() + "</td>");
	  if (governor){
		  sb.append("<td>-</td>");
	  }else{
		  sb.append("<td>" + getFrequencyString() + "</td>");
	  }
	  sb.append("</tr>\n");
	  if (description != null){
		  sb.append("<tr>");
		  sb.append("<td>Description:</td>");
		  sb.append("<td colspan=\"4\">" + description + "</td>");
		  sb.append("</tr>\n");
	  }
	  List<String> abilities = getAbilitiesStrings();
	  for (String aStr : abilities) {
		  sb.append("<tr>");
		  sb.append("<td>");
		  sb.append(aStr + "<br>");
		  sb.append("</td>");
		  sb.append("</tr>\n");
	  }
	  sb.append("</table>");
	  return sb.toString();
  }
  
  @JsonIgnore
  public String getHTMLTableContentNO(){
	  StringBuffer sb = new StringBuffer();
	  String RowName= shortName;
	  sb.append("<tr style='display:inline' class='ListTextRow' id='" + RowName + "A' onMouseOver='TranparentRow(\"" + RowName + "\",5,1);' onMouseOut='TranparentRow(\"" + RowName + "\",5,0);' onclick='ShowLayer(\"" + shortName +  "\");ShowLayer(\"" + shortName +  "A\");ShowLayer(\"" + shortName +  "B\");'>");
	  sb.append("<td class='ListText' id='" + RowName + "1' WIDTH='10'></td>");
	  sb.append("<td class='ListText' id='" + RowName + "2'><div class='SolidText'>" + name + "</div></td>");
	  sb.append("<td class='ListText' id='" + RowName + "3'><div class='SolidText'>" + shortName + "</div></td>");
	  sb.append("<td class='ListText' id='" + RowName + "4'><div class='SolidText'>" + alignment.toString() + "</div></td>");
	  if (governor){
		  sb.append("<td class='ListText' id='" + RowName + "5'><div class='SolidText'>Governor</div></td>");
	  }else{
		  sb.append("<td class='ListText' id='" + RowName + "5'><div class='SolidText'>" + getFrequencyString() + "</div></td>");
	  }
	  sb.append("</tr>\n");

	  sb.append("<tr  onclick='ShowLayer(\"" + shortName +  "\");ShowLayer(\"" + shortName +  "A\");ShowLayer(\"" + shortName +  "B\");'  id='" + RowName + "B' class='ListTextRow' style='display:none'>");
	  	sb.append("<td class='ListTextDark' style='border-top: #000 1px solid;' WIDTH='10'></td>");
	  	sb.append("<td class='ListTextDark' style='border-top: #000 1px solid;'><div class='SolidText'>" + name + "</div></td>");
	  	sb.append("<td class='ListTextDark' style='border-top: #000 1px solid;'><div class='SolidText'>" + shortName + "</div></td>");
	  	sb.append("<td class='ListTextDark' style='border-top: #000 1px solid;'><div class='SolidText'>" + alignment.toString() + "</div></td>");

	  if (governor){
		  sb.append("<td class='ListTextDark' style='border-top: #000 1px solid;' id='" + RowName + "5'><div class='SolidText'>Governor</div></td>");
	  }else{
		  sb.append("<td class='ListTextDark' style='border-top: #000 1px solid;' id='" + RowName + "5'><div class='SolidText'>" + getFrequencyString() + "</div></td>");
	  }
	  sb.append("</tr>\n");

	  
	  
	  
	  sb.append("<tr onclick='ShowLayer(\"" + shortName +  "\");ShowLayer(\"" + shortName +  "A\");ShowLayer(\"" + shortName +  "B\");' class='ListTextRow' style=' display:none'id=" + RowName + "><td style='border-bottom: #000 1px solid;border-top: #000 1px solid;' class='ListTextLight' WIDTH='10'></td><td style='border-bottom: #000 1px solid;border-top: #000 1px solid;' class='ListTextLight' colspan='4'><div class='SolidText'>");
	  
	  if (description != null){
		  
		  sb.append("<b>Description:</b><br>");
		  sb.append(description);
		  sb.append("<br><br>");
	  }
	  List<String> abilities = getAbilitiesStrings();
	  
	  sb.append("<b>Bonus:</b><br>");
	  for (String aStr : abilities) {
		  sb.append(aStr + "<br>");
	  }
	  sb.append("<br>\n");
	  sb.append("</div></td></tr>");
	  return sb.toString();
  }

  @JsonIgnore
  public List<String> getAbilitiesStrings(){  // add info about how easily/hard the VIP dies?   And info about where this VIP may travel?
    List<String> allStrings = new LinkedList<String>();
    if (assassination > 0){
        allStrings.add("Assassin: " + assassination + "%");
    }
    if (counterEspionage > 0){
        allStrings.add("Counter-espionage: " + counterEspionage + "%");
    }
    if (spying){
        allStrings.add("Spy");
    }
    if (initBonus > 0){
        allStrings.add("Initiative bonus: " + initBonus + "%");
    }
    if (initSupportBonus > 0){
        allStrings.add("Initiative support bonus: " + initSupportBonus + "%");
    }
    if (initDefence > 0){
        allStrings.add("Initiative defence: " + initDefence + "%");
    }
    if (psychWarfareBonus > 0){
        allStrings.add("Psych warfare bonus: " + psychWarfareBonus);
    }
    
    if (shipBuildBonus > 0){
        allStrings.add("Ships build bonus: " + shipBuildBonus + "%");
    }
    if (troopBuildBonus > 0){
        allStrings.add("Troops build bonus: " + troopBuildBonus + "%");
    }
    if (buildingBuildBonus > 0){
        allStrings.add("Buidings build bonus: " + buildingBuildBonus + "%");
    }
    if (techBonus > 0){
        allStrings.add("Tech bonus: " + techBonus + "%");
    }
    if (openIncBonus > 0){
        allStrings.add("Open planet income bonus: " + openIncBonus);
    }
    if (closedIncBonus > 0){
        allStrings.add("Closed planet income bonus: " + closedIncBonus);
    }
    if (canVisitEnemyPlanets){
        allStrings.add("Can visit enemy planets");
    }
    if (canVisitNeutralPlanets){
        allStrings.add("Can visit neutral planets");
    }
    if (duellist > 0){
        allStrings.add("Duellist, skill: " + getDuellistSkillString());
    }
    if (hardToKill){
        allStrings.add("Hard to kill: not killed by destroyed ships or lost planets");
    }
    if (wellGuarded){
        allStrings.add("Cannot be assassinated");
    }
    if (governor){
        allStrings.add("Governor");
    }
    if (FTLbonus){
        allStrings.add("Boosts range of ships");
    }
    if (diplomat){
        allStrings.add("Diplomat: can persuade neutral planets to join you (only non-alien factions)");
    }
    if (infestate){
        allStrings.add("Infestate: can infestate planets to join you");
    }
    if (showOnOpenPlanet){
        allStrings.add("Visible on open planets");
    }
    if (immuneToCounterEspionage){
        allStrings.add("Immune to enemy counter-espionage");
    }
    if (initFighterSquadronBonus > 0){
    	allStrings.add("Squadron initiative bonus: " + initFighterSquadronBonus + "%");
    }
    if (resistanceBonus > 0){
    	allStrings.add("Resistance bonus: " + resistanceBonus);
    }
    if (exterminator > 0){
    	allStrings.add("Exterminator: " + exterminator + "%");
    }
//    if (troopAttacksBonus > 0){
//    	allStrings.add("Troop number attacks bonus: +" + troopAttacksBonus);
//    }
    if (landBattleGroupAttackBonus > 0){
    	allStrings.add("Troops attack bonus: +" + landBattleGroupAttackBonus);
    }
    if (stealth){
    	allStrings.add("Makes a ship invisible on the map");
    }
    if (bombardmentBonus > 0){
    	allStrings.add("Increase bombardment with +" + bombardmentBonus + " for a fleet (with a bombardment of at least 1)");
    }
    if (attackScreenedSquadron){
    	allStrings.add("Enables a squadron to attack screened enemy ships");
    }
    if (attackScreenedCapital){
    	allStrings.add("Enables a capital ship to attack screened enemy ships");
    }
    if (planetarySurvey){
    	allStrings.add("On a ship, act as spy on closed planets");
    }
    if (worldUnique){
        allStrings.add("Is World Unique");
    }
    if (factionUnique){
        allStrings.add("Is Faction Unique");
    }
    if (playerUnique){
        allStrings.add("Is Player Unique");
    }
    
    

    return allStrings;
  }
  
  public String getDuellistSkillString(){
  	String skillStr = "None";
  	if (duellist == DUELLIST_APPRENTICE){
  		skillStr = "Apprentice";
  	}else
  	if (duellist == DUELLIST_VETERAN){
  		skillStr = "Average";
  	}else
	if (duellist == DUELLIST_MASTER){
		skillStr = "Master";
	}
	return skillStr;
  }
  
  public String getFrequencyString(){
	  return frequency.toString();
  }

  public String getAlignmentString(){
	return alignment.toString();
  }

  public VIP createNewVIP(boolean isFanatic){
    return new VIP(this,uic.getUniqueId(),isFanatic);
  }

  public VIP createNewVIP(Player aBoss,Planet planetLocation, boolean isFanatic){
    VIP tempVIP = createNewVIP(isFanatic);
    tempVIP.setBoss(aBoss);
    tempVIP.setLocation(planetLocation);
    return tempVIP;
  }

  public String getTypeName(){
    return getName();
  }

  public String getName(){
    return name;
  }

  public String getShortName(){
    return shortName;
  }

  public int getInitDefence(){
  	return initDefence;
  }

  public boolean getCanVisitEnemyPlanets(){
    return canVisitEnemyPlanets;
  }

  public boolean getCanVisitNeutralPlanets(){
    return canVisitNeutralPlanets;
  }

  public boolean getShowOnOpenPlanet() {
	return showOnOpenPlanet;
  }

  public Alignment getAlignment() {
  	return alignment;
  }
  public void setAlignment(Alignment alignment) {
  	this.alignment = alignment;
  }
  public int getAssassination() {
  	return assassination;
  }
  public boolean isAssassin() {
  	return assassination > 0;
  }
  public void setAssassination(int assassination) {
  	this.assassination = assassination;
  }
  public int getClosedIncBonus() {
  	return closedIncBonus;
  }
  public void setClosedIncBonus(int closedIncBonus) {
  	this.closedIncBonus = closedIncBonus;
  }
  public int getCounterEspionage() {
  	return counterEspionage;
  }
  public void setCounterEspionage(int counterEspionage) {
  	this.counterEspionage = counterEspionage;
  }
  public boolean isDiplomat() {
  	return diplomat;
  }
  public void setDiplomat(boolean diplomat) {
  	this.diplomat = diplomat;
  }
  public int getDuellistSkill() {
  	return duellist;
  }
  public boolean isDuellist() {
  	return duellist > 0;
  }
  public void setDuellistSkill(int duellist) {
  	this.duellist = duellist;
  }
  public boolean isFTLbonus() {
  	return FTLbonus;
  }
  public void setFTLbonus(boolean lbonus) {
  	FTLbonus = lbonus;
  }
  public boolean isGovernor() {
  	return governor;
  }
  public void setGovernor(boolean governor) {
  	this.governor = governor;
  }
  public boolean isHardToKill() {
  	return hardToKill;
  }
  public void setHardToKill(boolean hardToKill) {
  	this.hardToKill = hardToKill;
  }
  public int getInitBonus() {
  	return initBonus;
  }
  public void setInitBonus(int initBonus) {
  	this.initBonus = initBonus;
  }
  public int getInitSupportBonus() {
  	return initSupportBonus;
  }
  public void setInitSupportBonus(int initSupportBonus) {
  	this.initSupportBonus = initSupportBonus;
  }
  public int getOpenIncBonus() {
  	return openIncBonus;
  }
  public void setOpenIncBonus(int openIncBonus) {
  	this.openIncBonus = openIncBonus;
  }
  public int getPsychWarfareBonus() {
  	return psychWarfareBonus;
  }
  public void setPsychWarfareBonus(int psychWarfareBonus) {
  	this.psychWarfareBonus = psychWarfareBonus;
  }
  public boolean isSpying() {
  	return spying;
  }
  public void setSpying(boolean spying) {
  	this.spying = spying;
  }
  public int getTechBonus() {
  	return techBonus;
  }
  public void setTechBonus(int techBonus) {
  	this.techBonus = techBonus;
  }
  public boolean isWellGuarded() {
  	return wellGuarded;
  }
  public void setWellGuarded(boolean wellGuarded) {
  	this.wellGuarded = wellGuarded;
  }
  public void setCanVisitEnemyPlanets(boolean canVisitEnemyPlanets) {
  	this.canVisitEnemyPlanets = canVisitEnemyPlanets;
  }
  public void setCanVisitNeutralPlanets(boolean canVisitNeutralPlanets) {
  	this.canVisitNeutralPlanets = canVisitNeutralPlanets;
  }
  public void setInitDefence(int initDefence) {
  	this.initDefence = initDefence;
  }
  public void setShowOnOpenPlanet(boolean showOnOpenPlanet) {
  	this.showOnOpenPlanet = showOnOpenPlanet;
  }
  
  public int getFrequency() {
  	return frequency.getFrequency();
  }
  
  public void setFrequency(BlackMarketFrequency newFrequency) {
  	this.frequency = newFrequency;
  }
  public boolean isAlignment(Alignment anAlignment){
  	return this.alignment == anAlignment;
  }
  public boolean isCounterSpy(){
    return counterEspionage > 0;
  }
  public int getAntiAirBonus() {
  	return antiAirBonus;
  }
  public void setAntiAirBonus(int antiAirBonus) {
  	this.antiAirBonus = antiAirBonus;
  }
  public int getInitFighterSquadronBonus() {
  	return initFighterSquadronBonus;
  }
  public void setInitFighterSquadronBonus(int initFighterSquadronBonus) {
  	this.initFighterSquadronBonus = initFighterSquadronBonus;
  }
  public int getResistanceBonus() {
  	return resistanceBonus;
  }
  public void setResistanceBonus(int resistanceBonus) {
  	this.resistanceBonus = resistanceBonus;
  }
  public boolean isImmuneToCounterEspionage() {
	  return immuneToCounterEspionage;
  }
  public void setImmuneToCounterEspionage(boolean immuneToCounterEspionage) {
	  this.immuneToCounterEspionage = immuneToCounterEspionage;
  }
  
  public boolean isBattleVip(){
//	  LoggingHandler.finer("isBattleVip(): " + name);
	  boolean battleVIP = false;
	  if (initBonus > 0){
		  battleVIP = true;
	  }else
	  if (initSupportBonus > 0){
		  battleVIP = true;
	  }else
	  if (initDefence > 0){
		  battleVIP = true;
	  }else
	  if (initFighterSquadronBonus > 0){
		  battleVIP = true;
	  }else
	  if (antiAirBonus > 0){
		  battleVIP = true;
	  }else
      if (aimBonus > 0){
    	  battleVIP = true;
      }
      return battleVIP;
  }

  public boolean isLandBattleVip(){
	  boolean battleVIP = false;
		  if (landBattleGroupAttackBonus > 0){
			  battleVIP = true;
		  }
	  return battleVIP;
  }

    public String getDescription(){
    	return description;
    }

    public void setDescription(String newDesc){
    	description = newDesc;
    }

    public boolean isInfestate() {
    	return infestate;
    }

    public void setInfestate(boolean infestate) {
    	this.infestate = infestate;
    }

    public int getExterminator() {
    	return exterminator;
    }

    public void setExterminator(int exterminator) {
    	this.exterminator = exterminator;
    }

    public int getAimBonus() {
    	return aimBonus;
    }

    public void setAimBonus(int aimBonus) {
    	this.aimBonus = aimBonus;
    }

    public int getLandBattleGroupAttackBonus() {
    	return landBattleGroupAttackBonus;
    }

    public void setLandBattleGroupAttackBonus(int landBattleGroupAttackBonus) {
    	this.landBattleGroupAttackBonus = landBattleGroupAttackBonus;
    }

	public int getBuildCost() {
		return buildCost;
	}
	
	public void setBuildCost(int buildcost) {
		this.buildCost = buildcost;
	}
	
	public int getUpkeep() {
		return upkeep;
	}
	
	public void setUpkeep(int upkeep) {
		this.upkeep = upkeep;
	}
	
	public boolean isAvailableToBuild() {
		return availableToBuild;
	}
	
	public void setAvailableToBuild(boolean availableToBuild) {
		this.availableToBuild = availableToBuild;
	}
	
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
	
	public void setPlayerUnique(boolean playerUnique) {
		this.playerUnique = playerUnique;
	}
	
	public boolean isFactionUniqueBuild(Player aPlayer) {
		return aPlayer.getGalaxy().vipTypeExist(this, aPlayer.getFaction(), null);
	}
	
	public boolean isPlayerUniqueBuild(Player aPlayer) {
		return aPlayer.getGalaxy().vipTypeExist(this, null, aPlayer);
	}
	
	public boolean isWorldUniqueBuild(Galaxy aGalaxy) {
		return aGalaxy.vipTypeExist(this, null, null);
	}
	
	public void setFactionUnique(boolean factionUnique) {
		this.factionUnique = factionUnique;
	}
	
	public String getUniqueString(){
		String uniqueString = "";
  
		if(playerUnique){
			uniqueString = "Player unique";
		}else
		if(factionUnique){
			uniqueString = " Faction unique";
		}else
		if(worldUnique){
			uniqueString = "World unique";
		}
  
		return uniqueString;
	}
	
	
	
	public boolean isConstructible(Player aPlayer){
		boolean constructible =  true;
		
		/*if(!isAvailableToBuild()){
			constructible = false;
		}else*/ if((isWorldUnique() && isWorldUniqueBuild(aPlayer.getGalaxy())) || (isFactionUnique() && isFactionUniqueBuild(aPlayer)) || (isPlayerUnique() && isPlayerUniqueBuild(aPlayer))){
			constructible = false;
		}else if(!aPlayer.getFaction().getAlignment().canHaveVip(alignment.getName())){
			constructible = false;
		}else if(isWorldUnique() || isFactionUnique() || isPlayerUnique()){
			// check if a build order already exist
			if(aPlayer.getOrders().haveVIPTypeBuildOrder(this)){
				constructible = false;
			}
			for (BlackMarketOffer aBlackMarketOffer : aPlayer.getGalaxy().getBlackMarket().getCurrentOffers()) {
				if(aBlackMarketOffer.isVIP() && aBlackMarketOffer.getOfferedVIPType().getName().equals(name)){
					constructible = false;
				}
			}
		}
		
		return constructible;
	}
	
	
	public boolean isReadyToUseInBlackMarket(Galaxy aGalaxy){
		boolean constructible =  false;
		if (!isGovernor()){
			if(!isPlayerUnique() && !isFactionUnique()){
				if(isWorldUnique() && !isWorldUniqueBuild(aGalaxy)){
					boolean isAlreadyAoffer = false;
					for (BlackMarketOffer aBlackMarketOffer : aGalaxy.getBlackMarket().getCurrentOffers()) {
						if(aBlackMarketOffer.isVIP() && aBlackMarketOffer.getOfferedVIPType().getName().equals(name)){
							isAlreadyAoffer = true;
						}
					}
					
					if(!isAlreadyAoffer){
						boolean haveBuildingOrder = false;
						for (Player tempPlayer : aGalaxy.getPlayers()) {
							if(tempPlayer.getOrders().haveVIPTypeBuildOrder(this)){
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
		return constructible;
	}
	
	
	
	/*
	public boolean isConstructible(){
		boolean constructible =  true;
		if(!isAvailableToBuild()){
			constructible = false;
		}else if((isWorldUnique() && isWorldUniqueBuild()) || (isFactionUnique() && isFactionUniqueBuild()) || (isPlayerUnique() && isPlayerUniqueBuild())){
			constructible = false;
		}
		return constructible;
	}
	*/
	
	public int getBuildingBuildBonus() {
		return buildingBuildBonus;
	}
	
	public void setBuildingBuildBonus(int buildingBuildBonus) {
		this.buildingBuildBonus = buildingBuildBonus;
	}
	
	public int getShipBuildBonus() {
		return shipBuildBonus;
	}
	
	public void setShipBuildBonus(int shipBuildBonus) {
		this.shipBuildBonus = shipBuildBonus;
	}
	
	public int getTroopBuildBonus() {
		return troopBuildBonus;
	}
	
	public void setTroopBuildBonus(int troopBuildBonus) {
		this.troopBuildBonus = troopBuildBonus;
	}
	
	public boolean isTroopVIP(){
		boolean isTroopVIP = false;
	//	if(troopAttacksBonus > 0){
	//		isTroopVIP = true;
	//	}else 
		if(landBattleGroupAttackBonus > 0){
			isTroopVIP = true;
		}
		return isTroopVIP;
	}
	
	public String getAdvanteges() {
		return advanteges;
	}
	
	public void setAdvanteges(String advanteges) {
		this.advanteges = advanteges;
	}
	
	public boolean isStealth() {
		return stealth;
	}
	
	public void setStealth(boolean stealth) {
		this.stealth = stealth;
	}
	
	public int getBombardmentBonus() {
		return bombardmentBonus;
	}
	
	public void setBombardmentBonus(int bombardmentBonus) {
		this.bombardmentBonus = bombardmentBonus;
	}
	
	public boolean isAttackScreenedSquadron() {
		return attackScreenedSquadron;
	}
	
	public void setAttackScreenedSquadron(boolean attackScreenedSquadron) {
		this.attackScreenedSquadron = attackScreenedSquadron;
	}
	
	public boolean isAttackScreenedCapital() {
		return attackScreenedCapital;
	}
	
	public void setAttackScreenedCapital(boolean attackScreenedCapital) {
		this.attackScreenedCapital = attackScreenedCapital;
	}
	
	public boolean isPlanetarySurvey() {
		return planetarySurvey;
	}
	
	public void setPlanetarySurvey(boolean planetarySurvey) {
		this.planetarySurvey = planetarySurvey;
	}

	public String getHowToPlay() {
		return howToPlay;
	}

	public void setHowToPlay(String howToPlay) {
		this.howToPlay = howToPlay;
	}

	@JsonIgnore
	public static String getAllAbilitiesDroid(){
		List<String> allStrings = new LinkedList<String>();
		allStrings.add("Governor");
		allStrings.add("Assassination");
		allStrings.add("Counter-espionage");
		allStrings.add("Exterminator");
		allStrings.add("Spying");
		allStrings.add("Initiative bonus");
		allStrings.add("Squadron initiative bonus");
		allStrings.add("Initiative defence bonus");
		allStrings.add("Siege bonus");
		allStrings.add("Ships build bonus");
		allStrings.add("Buildings build bonus");
		allStrings.add("Tech bonus");
		allStrings.add("Open planet income bonus");
		allStrings.add("Closed planet income bonus");
		allStrings.add("Can visit enemy planets");
		allStrings.add("Can visit neutral planets");
		allStrings.add("Hero combat");
		allStrings.add("Hard to kill");
		allStrings.add("Well guarded (immune to assassinations)");
		allStrings.add("Boosts range of ships");
		allStrings.add("Diplomacy (can persuade neutral planets)");
		allStrings.add("Infestate (can infest planets)");
		allStrings.add("Immune to counter-espionage");
		allStrings.add("Resistance bonus");
		allStrings.add("Stealth (makes ship invisible)");
		allStrings.add("Bombardment bonus");
		allStrings.add("Squadron attack screened");
		allStrings.add("Planetary survey (view closed planets as open)");
		allStrings.add("Visible on open planets");
		allStrings.add("Player unique");
		StringBuffer sb = new StringBuffer();
		for (String string : allStrings) {
			sb.append(string);
	        sb.append("&nbsp;<br>");
		}
	    return sb.toString();
	}

	@JsonIgnore
	public List<String> getAbilitiesStringsDroid(){
		List<String> allStrings = new LinkedList<String>();
		if (governor){
			allStrings.add("Governor");
		}
		if (assassination > 0){
			allStrings.add("Assassination: " + assassination + "%");
		}
		if (counterEspionage > 0){
			allStrings.add("Counter-espionage: " + counterEspionage + "%");
		}
		if (exterminator > 0){
			allStrings.add("Exterminator: " + exterminator + "%");
		}
		if (spying){
			allStrings.add("Spying");
		}
		if (initBonus > 0){
			allStrings.add("Initiative bonus: " + initBonus + "%");
		}
		if (initFighterSquadronBonus > 0){
			allStrings.add("Squadron initiative bonus: " + initFighterSquadronBonus + "%");
		}
		if (initDefence > 0){
			allStrings.add("Initiative defence bonus: " + initDefence + "%");
		}
		if (psychWarfareBonus > 0){
			allStrings.add("Siege bonus: " + psychWarfareBonus);
		}		    
		if (shipBuildBonus > 0){
			allStrings.add("Ships build bonus: " + shipBuildBonus + "%");
		}
		if (buildingBuildBonus > 0){
			allStrings.add("Buildings build bonus: " + buildingBuildBonus + "%");
		}
		if (techBonus > 0){
			allStrings.add("Tech bonus: " + techBonus + "%");
		}
		if (openIncBonus > 0){
			allStrings.add("Open planet income bonus: " + openIncBonus);
		}
		if (closedIncBonus > 0){
			allStrings.add("Closed planet income bonus: " + closedIncBonus);
		}
		if (canVisitEnemyPlanets){
			allStrings.add("Can visit enemy planets");
		}
		if (canVisitNeutralPlanets){
			allStrings.add("Can visit neutral planets");
		}
		if (duellist > 0){
			allStrings.add("Hero combat, skill: " + getDuellistSkillString());
		}
		if (hardToKill){
			allStrings.add("Hard to kill");
		}
		if (wellGuarded){
			allStrings.add("Well guarded (immune to assassinations)");
		}
		if (FTLbonus){
			allStrings.add("Boosts range of ships");
		}
		if (diplomat){
			allStrings.add("Diplomacy (can persuade neutral planets)");
		}
		if (infestate){
			allStrings.add("Infestate (can infest planets)");
		}
		if (immuneToCounterEspionage){
			allStrings.add("Immune to counter-espionage");
		}
		if (resistanceBonus > 0){
			allStrings.add("Resistance bonus: " + resistanceBonus);
		}
		if (stealth){
			allStrings.add("Stealth (makes ship invisible)");
		}
		if (bombardmentBonus > 0){
			allStrings.add("Bombardment bonus: " + bombardmentBonus);
		}
		if (attackScreenedSquadron){
			allStrings.add("Squadron attack screened");
		}
		if (planetarySurvey){
			allStrings.add("Planetary survey (view closed planets as open)");
		}
		if (showOnOpenPlanet){
			allStrings.add("Visible on open planets");
		}
		if (playerUnique){
			allStrings.add("Player unique");
		}
		return allStrings;
	}

	@JsonIgnore
	public boolean getHasAbilityDroid(int abilityNr){
		boolean hasAbility = false;
		if ((abilityNr == 1) & governor){
			hasAbility = true;
		}else
		if ((abilityNr == 2) & (assassination > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 3) & (counterEspionage > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 4) & (exterminator > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 5) & spying){
			hasAbility = true;
		}else
		if ((abilityNr == 6) & (initBonus > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 7) & (initFighterSquadronBonus > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 8) & (initDefence > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 9) & (psychWarfareBonus > 0)){
			hasAbility = true;
		}else	    
		if ((abilityNr == 10) & (shipBuildBonus > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 11) & (buildingBuildBonus > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 12) & (techBonus > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 13) & (openIncBonus > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 14) & (closedIncBonus > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 15) & canVisitEnemyPlanets){
			hasAbility = true;
		}else
		if ((abilityNr == 16) & canVisitNeutralPlanets){
			hasAbility = true;
		}else
		if ((abilityNr == 17) & (duellist > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 18) & hardToKill){
			hasAbility = true;
		}else
		if ((abilityNr == 19) & wellGuarded){
			hasAbility = true;
		}else
		if ((abilityNr == 20) & FTLbonus){
			hasAbility = true;
		}else
		if ((abilityNr == 21) & diplomat){
			hasAbility = true;
		}else
		if ((abilityNr == 22) & infestate){
			hasAbility = true;
		}else
		if ((abilityNr == 23) & immuneToCounterEspionage){
			hasAbility = true;
		}else
		if ((abilityNr == 24) & (resistanceBonus > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 25) & stealth){
			hasAbility = true;
		}else
		if ((abilityNr == 26) & (bombardmentBonus > 0)){
			hasAbility = true;
		}else
		if ((abilityNr == 27) & attackScreenedSquadron){
			hasAbility = true;
		}else
		if ((abilityNr == 28) & planetarySurvey){
			hasAbility = true;
		}else
		if ((abilityNr == 29) & showOnOpenPlanet){
			hasAbility = true;
		}else
		if ((abilityNr == 30) & playerUnique){
			hasAbility = true;
		}
		return hasAbility;
	}

	@JsonIgnore
	public String getVipInfoDroid() {
		  StringBuffer sb = new StringBuffer();
		  sb.append("<h4>VIP type: ");
		  sb.append(name + " (" + shortName + ")");
		  sb.append("</h4>");
		  sb.append(description);
		  sb.append("<p>");
		  sb.append("<b>Alignment: </b>");
		  sb.append(alignment.getName() + " alignment");
		  sb.append("<br>");
		  sb.append("<b>Frequency: </b>");
		  sb.append(frequency.toString());
		  sb.append("<p>");
		  sb.append("<b>Abilities: </b>");
		  sb.append("<br>");
		  List<String> abilitiesList = getAbilitiesStringsDroid();
		  for (String ability : abilitiesList) {
			  sb.append(ability);
			  sb.append("&nbsp;<br>");
		  }
		  sb.append("<br>");
		  if (buildCost > 0){
			  sb.append("<b>Build Cost: </b>");
			  sb.append(buildCost);
			  sb.append("<p>");
		  }
		  sb.append("<b>How to play: </b>");
		  sb.append(howToPlay);
		  return sb.toString();
	}

}