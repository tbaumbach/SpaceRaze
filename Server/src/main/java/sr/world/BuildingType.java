package sr.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sr.enums.TypeOfTroop;
import sr.general.Functions;
import sr.general.logging.Logger;

public class BuildingType implements Serializable, Cloneable{
	static final long serialVersionUID = 1L;
	
	private String name, description, shortName,advanteges;
	private boolean inOrbit = false;
	private boolean autoDestructWhenConquered;
	private boolean selfDestructable = true;
	private boolean developed= true;
	private int openPlanetBonus = 0,closedPlanetBonus = 0;
	private int techBonus = 0;
	private int wharfSize = 0; // if = 0 cannot build ships
	private int troopSize = 0; // if = 0 cannot build troops
	private List<VIPType> buildVIPTypes;
	// worldUnigue=  only one in the world, factionUnigue= only one at faction, playerUnique =  only one at player, planetUnigue = one at planet.
	private boolean worldUnique = false, factionUnique = false, playerUnique = false, planetUnique = false;
	private int buildCost = 0;
	private boolean spaceport;
	UniqueIdCounter uic;
	int nrProduced;
	//  kan nog vara en ide...  om byggnaden ligger i orbit så fungerar det som för skepp men om byggnaden är på planeten så syns den inte fören en fi har trupper på planeten.
	private boolean visibleOnMap = true;
	
	// war buildings. shieldCapacity= ???, CannonDamage =  damge against enemy ships(one shot), cononRateOfFire(number of shot/turn) 
	private int resistanceBonus = 0, shieldCapacity = 0, CannonDamage = 0, CannonRateOfFire = 0, CannonHitChance= 50;
	
	private List<TypeOfTroop> typeOfTroop = new ArrayList<TypeOfTroop>(); // infantry, armored or support
	
	//private List<BuildingType> nextBuildingSteps;
	private BuildingType parentBuildingType;

	// Vänta med dessa tills grunden är klar
	// -------------------------------------
//	private int shipTechBonus = 0; // %  on ships bild on planet
	private int siegeBonus = 0;
	
	private boolean alienkiller;
	private int counterEspionage = 0;
	private int exterminator = 0;
	
	// Paul  Kan du fixa dessa
	
	// GÖR OM skapa en klass o lägg dessa i den. 
	// så att troops får en egen klass + så att build troops klassen kan hämta troops bonus från denna klass
	// adding bonus to troops build on the planet
	private int troopHitBonus = 0; // % on troops bild on planet
	private int troopAttackBonus = 0; // % % on troops bild on planet
	private int troopDefanceBonus = 0; // % % on troops bild on planet
	private int troopAirBonus = 0; // % % on troops bild on planet
	private int troopSuportBonus = 0; // % % on troops bild on planet

	// adding bonus to troops in ground combat
	private int defenceBonus = 0; // %
	private int airDefanceBonus = 0; // %
	private int suportDefanceBonus = 0; // %
	
	
	//private int shipTechBonus = 0; // %  on ships bild on planet
	
	/*
	// (Tobbe) Dessa användes inte. Samma egenskaper som VIPar har och skall kanske användas i framtiden. Skall vara i % form.
	private int shipBuildBonus; // decreases build cost of ships
	private int troopBuildBonus; // decreases build cost of troops
	private int vipBuildBonus; // decreases build cost of VIPs
	private int buildingBuildBonus; // decreases build cost of buildings
	*/
	
	  public String getHTMLTableContentNO(){
		  StringBuffer sb = new StringBuffer();
		  String RowName= shortName;
		  sb.append("<tr style='display:inline' class='ListTextRow' id='" + RowName + "A' onMouseOver='TranparentRow(\"" + RowName + "\",5,1);' onMouseOut='TranparentRow(\"" + RowName + "\",5,0);' onclick='ShowLayer(\"" + shortName +  "\");ShowLayer(\"" + shortName +  "A\");ShowLayer(\"" + shortName +  "B\");'>");
		  sb.append("<td class='ListText' id='" + RowName + "1' WIDTH='10'></td>");
		  sb.append("<td class='ListText' id='" + RowName + "2'><div class='SolidText'>" + name + "</div></td>");
		  sb.append("<td class='ListText' id='" + RowName + "3'><div class='SolidText'>" + shortName + "</div></td>");
		  sb.append("<td class='ListText' id='" + RowName + "4'><div class='SolidText'>" + buildCost + "</div></td>");
////		  if (governor){
//			  sb.append("<td class='ListText' id='" + RowName + "5'><div class='SolidText'>Governor</div></td>");
//		  }else{
//			  sb.append("<td class='ListText' id='" + RowName + "5'><div class='SolidText'>" + getFrequencyString() + "</div></td>");
//		  }
		  sb.append("</tr>\n");

		  sb.append("<tr  onclick='ShowLayer(\"" + shortName +  "\");ShowLayer(\"" + shortName +  "A\");ShowLayer(\"" + shortName +  "B\");'  id='" + RowName + "B' class='ListTextRow' style='display:none'>");
		  	sb.append("<td class='ListTextDark' style='border-top: #000 1px solid;' WIDTH='10'></td>");
		  	sb.append("<td class='ListTextDark' style='border-top: #000 1px solid;'><div class='SolidText'>" + name + "</div></td>");
		  	sb.append("<td class='ListTextDark' style='border-top: #000 1px solid;'><div class='SolidText'>" + shortName + "</div></td>");
		  	sb.append("<td class='ListTextDark' style='border-top: #000 1px solid;'><div class='SolidText'>" + buildCost + "</div></td>");

//		  if (governor){
//			  sb.append("<td class='ListTextDark' style='border-top: #000 1px solid;' id='" + RowName + "5'><div class='SolidText'>Governor</div></td>");
//		  }else{
//			  sb.append("<td class='ListTextDark' style='border-top: #000 1px solid;' id='" + RowName + "5'><div class='SolidText'>" + getFrequencyString() + "</div></td>");
//		  }
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

	
	public BuildingType(String name, String shortName, int buildCost, UniqueIdCounter uic){
		setName(name);
		setShortName(shortName);
		setBuildCost(buildCost);
		this.uic = uic;
		nrProduced = 0;
		//nextBuildingSteps = new ArrayList<BuildingType>();	
		buildVIPTypes = new ArrayList<VIPType>();
	}
	
	// returns buildingsType info (description)
	public String getBuildingText(){
    	String text;
    	
    	text= "";
    	
    	return text;
	}
	
	 public Building getBuilding(Planet planet, Galaxy g){
		 nrProduced++;
		 return new Building(this,null,nrProduced,g, planet);
	 }
	
	public BuildingType getNextBuildingType(String key, Player aPlayer){
		Iterator<BuildingType> it = aPlayer.getBuildings().getNextBuildingSteps(this).iterator();
		while(it.hasNext()){
			BuildingType temp = it.next();
			if(temp.getName().equals(key)){
				return temp;
			}
		}
		return null;
	}
	
	public void setParentBuildingType(BuildingType parentBuildingType){
		this.parentBuildingType = parentBuildingType;
	}
	
	public int getAirDefanceBonus() {
		return airDefanceBonus;
	}

	public void setAirDefanceBonus(int airDefanceBonus) {
		this.airDefanceBonus = airDefanceBonus;
	}

	public int getClosedPlanetBonus() {
		return closedPlanetBonus;
	}

	public void setClosedPlanetBonus(int closedPlanetBonus) {
		this.closedPlanetBonus = closedPlanetBonus;
	}

	public int getDefenceBonus() {
		return defenceBonus;
	}

	public void setDefenceBonus(int defenceBonus) {
		this.defenceBonus = defenceBonus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAdvanteges() {
		return advanteges;
	}

	public void setAdvanteges(String advanteges) {
		this.advanteges = advanteges;
	}
	
	public boolean isDeveloped() {
		return developed;
	}

	public void setDeveloped(boolean developed) {
		this.developed = developed;
	}

	public boolean isInOrbit() {
		return inOrbit;
	}

	public void setInOrbit(boolean inOrbit) {
		this.inOrbit = inOrbit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String name) {
		this.shortName = name;
	}
/*
	public String getNextBuildingStep() {
		return nextBuildingStep;
	}

	public void setNextBuildingStep(String nextBuildingStep) {
		this.nextBuildingStep = nextBuildingStep;
	}
*/
	public int getOpenPlanetBonus() {
		return openPlanetBonus;
	}

	public void setOpenPlanetBonus(int openPlanetBonus) {
		this.openPlanetBonus = openPlanetBonus;
	}

	public BuildingType getParentBuilding() {
		return parentBuildingType;
	}

	/*public void setParentBuilding(BuildingType parentBuildingType) {
		this.parentBuildingType = parentBuildingType;
		//parentBuildingType.addNextBuildingType(this);
	}*/

	public int getResistanceBonus() {
		return resistanceBonus;
	}

	public void setResistanceBonus(int resistanceBonus) {
		this.resistanceBonus = resistanceBonus;
	}
/*
	public int getShipTechBonus() {
		return shipTechBonus;
	}

	public void setShipTechBonus(int shipTechBonus) {
		this.shipTechBonus = shipTechBonus;
	}
*/
	public int getSiegeBonus() {
		return siegeBonus;
	}

	public void setSiegeBonus(int siegeBonus) {
		this.siegeBonus = siegeBonus;
	}

	public int getSuportDefanceBonus() {
		return suportDefanceBonus;
	}

	public void setSuportDefanceBonus(int suportDefanceBonus) {
		this.suportDefanceBonus = suportDefanceBonus;
	}
	
	
	// set all troop bonus on same time
	public void setTroopBonus(int troopBonus){
		setTroopAirBonus(troopBonus);
		setTroopAttackBonus(troopBonus);
		setTroopDefanceBonus(troopBonus);
		setTroopHitBonus(troopBonus);
		setTroopSuportBonus(troopBonus);
		
	}

	public int getTroopAirBonus() {
		return troopAirBonus;
	}

	public void setTroopAirBonus(int troopAirBonus) {
		this.troopAirBonus = troopAirBonus;
	}

	public int getTroopAttackBonus() {
		return troopAttackBonus;
	}

	public void setTroopAttackBonus(int troopAttackBonus) {
		this.troopAttackBonus = troopAttackBonus;
	}

	public int getTroopDefanceBonus() {
		return troopDefanceBonus;
	}

	public void setTroopDefanceBonus(int troopDefanceBonus) {
		this.troopDefanceBonus = troopDefanceBonus;
	}

	public int getTroopHitBonus() {
		return troopHitBonus;
	}

	public void setTroopHitBonus(int troopHitBonus) {
		this.troopHitBonus = troopHitBonus;
	}

	public int getTroopSuportBonus() {
		return troopSuportBonus;
	}

	public void setTroopSuportBonus(int troopSuportBonus) {
		this.troopSuportBonus = troopSuportBonus;
	}
/*
	public boolean isReplaceParentBuilding() {
		return replaceParentBuilding;
	}

	public void setReplaceParentBuilding(boolean replaceParentBuilding) {
		this.replaceParentBuilding = replaceParentBuilding;
	}
	*/
	public UniqueIdCounter getUniqueIdCounter(){
	      return uic;
	    }

	public int getBuildCost(VIP vipWithBonus){
      int tempBuildCost = buildCost;
      if (vipWithBonus != null){
    	  int vipBuildbonus = 100 - vipWithBonus.getBuildingBuildBonus();
    	  double tempBuildBonus = vipBuildbonus / 100.0;
    	  tempBuildCost = (int) Math.round(tempBuildCost * tempBuildBonus);
    	  if (tempBuildCost < 1){
    		  tempBuildCost = 1;
    	  }
      }
      return tempBuildCost;
    }

	public void setBuildCost(int buildCost) {
		this.buildCost = buildCost;
	}

	public int getTechBonus() {
		return techBonus;
	}

	public void setTechBonus(int techBonus) {
		this.techBonus = techBonus;
	}
	
	// Wharfs logic
	
	public static String getSizeString(int slots){		
		String size = "small";
		if (slots == 2){
			size = "medium";
		}else
		if (slots == 3){
			size = "large";
		}else
		if (slots == 5){
			size = "huge";
		}
		return size;
	}

	public String getSizeString(){
		
	      String size = "small";
	      if (wharfSize ==2){
	        size = "medium";
	      }else
	      if (wharfSize == 3){
	        size = "large";
	      }else
	      if (wharfSize == 5){
	        size = "huge";
	      }
	      return size;
	    }

//	    public static String getSizeString(int aTonnage){
//	        String size = "Small";
//	        if ((aTonnage >300) & (aTonnage <=600)){
//	          size = "Medium";
//	        }else
//	        if ((aTonnage >600) & (aTonnage <=900)){
//	          size = "Large";
//	        }else
//	        if (aTonnage >900){
//	          size = "Huge";
//	        }
//	        return size;
//	      }

		public int getWharfSize() {
			return wharfSize;
		}

		public void setWharfSize(int wharfSize) {
			this.wharfSize = wharfSize;
		}

		public boolean isSpaceport() {
			return spaceport;
		}

		public void setSpaceport(boolean spaceport) {
			this.spaceport = spaceport;
		}

	public boolean isAutoDestructWhenConquered() {
		return autoDestructWhenConquered;
	}

	public void setAutoDestructWhenConquered(boolean autoDestructWhenConquered) {
		this.autoDestructWhenConquered = autoDestructWhenConquered;
	}

	public List<VIPType> getBuildVIPTypes() {
		return buildVIPTypes;
	}

	public void setBuildVIPTypes(List<VIPType> buildVIPTypes) {
		this.buildVIPTypes = buildVIPTypes;
	}
	
	public void addBuildVIPType(VIPType buildVIPType) {
		this.buildVIPTypes.add(buildVIPType);
	}

	public boolean isFactionUnique() {
		return factionUnique;
	}

	
	public boolean isPlanetUnique() {
		return planetUnique;
	}

	public void setPlanetUnique(boolean planetUnique) {
		this.planetUnique = planetUnique;
	}

	public boolean isPlayerUnique() {
		return playerUnique;
	}

	
	public boolean isVisibleOnMap() {
		return visibleOnMap;
	}

	public void setVisibleOnMap(boolean visibleOnMap) {
		this.visibleOnMap = visibleOnMap;
	}

	public boolean isWorldUnique() {
		return worldUnique;
	}

	public void setWorldUnique(boolean worldUnique) {
		this.worldUnique = worldUnique;
	}
	
	public String getUniqueString(){
		String uniqueString = "";
  
		if(planetUnique){
			uniqueString = "Planet unique";
		}else
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

	public int getShieldCapacity() {
		return shieldCapacity;
	}

	public void setShieldCapacity(int shieldCapacity) {
		this.shieldCapacity = shieldCapacity;
	}

	public int getTroopSize() {
		return troopSize;
	}

	public void setTroopSize(int troopSize) {
		this.troopSize = troopSize;
	}
	public boolean isVIPBuilder(){
		if(buildVIPTypes != null && buildVIPTypes.size() > 0){
			return true;
		}
		return false;
	}
	
	public boolean isShipBuilder(){
		if(wharfSize > 0){
			return true;
		}
		return false;
	}
	
	public boolean isTroopBuilder(){
		if(troopSize > 0){
			return true;
		}
		return false;
	}
	
	public List<BuildingType> getUpgradebleBuildingTypes(Building aBuilding){
		List<BuildingType> upgradebleBuildingTypes = aBuilding.getLocation().getPlayerInControl().getBuildings().getNextBuildingSteps(this);
		List<BuildingType> playerUpgradebleBuildingTypes = new ArrayList<BuildingType>();
		
		for(int i=0; i < upgradebleBuildingTypes.size();i++){
			if(upgradebleBuildingTypes.get(i).isConstructible(aBuilding.getLocation(), aBuilding.getUniqueId())){
				playerUpgradebleBuildingTypes.add(upgradebleBuildingTypes.get(i));
			}
		}
		return playerUpgradebleBuildingTypes;
	}

	public List<TypeOfTroop> getTypeOfTroop() {
		return typeOfTroop;
	}

	public void addTypeOfTroop(TypeOfTroop typeOfTroop) {
		this.typeOfTroop.add(typeOfTroop);
	}
	
	public boolean canBuildTypeOfTroop(TypeOfTroop typeOfTroop){
		
		for(int i=0; i < this.typeOfTroop.size();i++){
			if(this.typeOfTroop.get(i) == typeOfTroop){
				return true;
			}
		}
		return false;
	}

	public boolean isFactionUniqueBuild(Player aPlayer) {
		return aPlayer.getGalaxy().buildingTypeExist(this, aPlayer.getFaction(), null);
	}

	public boolean isPlayerUniqueBuild(Player aPlayer) {
		return aPlayer.getGalaxy().buildingTypeExist(this, null, aPlayer);
	}

	public boolean isWorldUniqueBuild(Galaxy aGalaxy) {
		return aGalaxy.buildingTypeExist(this, null, null);
	}

	public void setFactionUnique(boolean factionUnique) {
		this.factionUnique = factionUnique;
	}

	public void setPlayerUnique(boolean playerUnique) {
		this.playerUnique = playerUnique;
	}
	
	public boolean isConstructible(Planet aPlanet, int buildingId){
		Logger.finer("isConstructible, aPlanet: " + aPlanet.getName());
		Logger.finer("isConstructible, BuildingType: " + this.getName());
		Player aPlayer = aPlanet.getPlayerInControl();
		boolean constructible =  true;
		if(!isDeveloped()){
			constructible = false;
		}else if((isWorldUnique() && isWorldUniqueBuild(aPlayer.getGalaxy())) || (isFactionUnique() && isFactionUniqueBuild(aPlayer)) || (isPlayerUnique() && isPlayerUniqueBuild(aPlayer))){
			constructible = false;
		}else if(isPlanetUnique() && aPlanet.hasBuilding(this.getName())){
			constructible = false;
		}else if(isWorldUnique()|| isFactionUnique()|| isPlayerUnique() || isPlanetUnique()){ // kollar om en unik byggnad redan har en child byggnad byggd. Om så är fallet så är den också unik och då skall det inte gå att bygga denna byggnad.
			if(isWorldUnique() || isFactionUnique() || isPlayerUnique()){
				// check if a build order already exist
				if(aPlayer.getOrders().haveBuildingTypeBuildOrder(this, buildingId)){
					constructible = false;
				}
			}
			if(constructible){
				for(int i=0; i < aPlanet.getBuildings().size();i++){
					BuildingType aBuildingType = aPlanet.getBuildings().get(i).getBuildingType();
					Logger.finer("aBuildingType: " + aBuildingType.getName());
					if(checkIfAUniqueChildBuildingIsAlreadyBuild(aBuildingType, aPlanet.getPlayerInControl(), name)){
						constructible = false;
					}
				}
			}
		}
		
		return constructible;
	}
	
	private boolean checkIfAUniqueChildBuildingIsAlreadyBuild(BuildingType aBuildingType, Player aPlayer, String buildingName){
		boolean childAlreadyBuild = false;
		Logger.finer("aBuildingType.getName(): " + aBuildingType.getName());
		if (aBuildingType.getParentBuilding() != null){
			Logger.finer("aBuildingType.getParentBuilding().getName(): " + aBuildingType.getParentBuilding().getName());
			if(aBuildingType.getParentBuilding().getName().equalsIgnoreCase(buildingName)){
				childAlreadyBuild = true;// det finns en child byggnad som är byggd och eftersom denna byggnad är unik så måste den också vara det och då stoppa bygge av denna byggnad.
			}else{
				BuildingType tempBuildingType = aPlayer.getBuildings().getBuildingType(aBuildingType.getParentBuilding().getName());
				if(tempBuildingType != null){
					Logger.finer("tempBuildingType.getName(): " + tempBuildingType.getName());
					childAlreadyBuild = aBuildingType.checkIfAUniqueChildBuildingIsAlreadyBuild(tempBuildingType, aPlayer, buildingName);
				}
			}
		}
		
		return childAlreadyBuild;
	}
		
	
	public List<String> getAbilitiesStrings(){
	    List<String> allStrings = new LinkedList<String>();
	    
	    
	    if (worldUnique){
	        allStrings.add("Is World Unique");
	    }
	    if (factionUnique){
	        allStrings.add("Is Faction Unique");
	    }
	    if (playerUnique){
	        allStrings.add("Is Player Unique");
	    }
	    if (planetUnique){
	        allStrings.add("Is Planet Unique");
	    }
	    if (spaceport){
	        allStrings.add("Spaceport");
	    }
	    if (openPlanetBonus > 0){
	        allStrings.add("Open Planet Bonus: " + openPlanetBonus);
	    }
	    if (closedPlanetBonus > 0){
	        allStrings.add("Closed Planet Bonus: " + closedPlanetBonus);
	    }
	    if (techBonus > 0){
	        allStrings.add("Tech Bonus: " + techBonus + "%");
	    }
	    if (wharfSize > 0){
	        allStrings.add("Wharf Size: " + wharfSize);
	    }
	    if (troopSize > 0){
	    	allStrings.add("Build Troop Capacity: " + troopSize);
	    }
	    if (alienkiller){
	        allStrings.add("Alien Killer: prevent infestator to infestate the planet");
	    }
	    if (counterEspionage > 0){
	        allStrings.add("Counter-espionage: " + counterEspionage + "%");
	    }
	    if (exterminator > 0){
	    	allStrings.add("Exterminator: " + exterminator + "%");
	    }
	    if (resistanceBonus > 0){
	    	allStrings.add("Resistance bonus: " + resistanceBonus);
	    }
	    if (shieldCapacity > 0){
	    	allStrings.add("Shield Capacity : " + shieldCapacity);
	    }
	    if (CannonDamage > 0){
	    	allStrings.add("Cannon Damage: " + CannonDamage);
	    	allStrings.add("Cannon Rate Of Fire: " + CannonRateOfFire);
	    	allStrings.add("Cannon hit chance: " + CannonHitChance);
	    }
	    
	    if (troopSize > 0){
	    	String tmp = "Troop building:";
	    	boolean addComma = false;
	    	for (TypeOfTroop type : typeOfTroop) {
	    		if (addComma){
	    			tmp += ",";
	    		}
	    		tmp += " " + type;
	    		addComma = true;
			}
	    	allStrings.add(tmp);
	    }

	    if (buildVIPTypes.size() > 0){
	    	String tmp = "VIP building:";
	    	boolean addComma = false;
	    	for (VIPType vipType : buildVIPTypes) {
	    		if (addComma){
	    			tmp += ",";
	    		}
	    		tmp += " " + vipType.getName();
	    		addComma = true;
			}
	    	allStrings.add(tmp);
	    }

	    if(!visibleOnMap){
	    	allStrings.add("Visible On Map: " + Functions.getYesNo(visibleOnMap));
    	}
	
	    if (autoDestructWhenConquered){
	    	allStrings.add("Will Auto Destruct When Conquered");
	    }
	    
//	    allStrings.add("Description: " + description);
	    //private boolean inOrbit = false;
		//private boolean visibleOnMap = true;
		
		
//		private int shipTechBonus = 0; // %  on ships bild on planet
		
		/*
				
		// adding bonus to troops in ground combat
		private int defenceBonus = 0; // %
		private int airDefanceBonus = 0; // %
		private int suportDefanceBonus = 0; // %
		
	    */
	    
	    if (inOrbit){
	    	allStrings.add("In orbit: can be destroyed by enemy ships in orbit if undefended");
	    }else{
	    	allStrings.add("Placed on planets surface");
	    }

	    return allStrings;
	}

	public boolean isAlienkiller() {
		return alienkiller;
	}

	public void setAlienkiller(boolean ailienkiller) {
		this.alienkiller = ailienkiller;
	}

	public int getExterminator() {
		return exterminator;
	}

	public void setExterminator(int exterminator) {
		this.exterminator = exterminator;
	}

	public int getCannonHitChance() {
		return CannonHitChance;
	}
	public void setCannonHitChance(int iCannonHitChance) {
		this.CannonHitChance = iCannonHitChance;
	}
	
	public int getCannonDamage() {
		return CannonDamage;
	}

	public void setCannonDamage(int CannonDamage) {
		this.CannonDamage = CannonDamage;
	}

	public int getCannonRateOfFire() {
		return CannonRateOfFire;
	}

	public void setCannonRateOfFire(int CannonRateOfFire) {
		this.CannonRateOfFire = CannonRateOfFire;
	}

	public boolean isSelfDestructable() {
		return selfDestructable;
	}

	public void setSelfDestructable(boolean selfDestructable) {
		this.selfDestructable = selfDestructable;
	}
	
	public boolean getHasAbilityDroid(int abilityNr){
		boolean hasAbility = false;
	    if ((abilityNr == 1) & spaceport){
			hasAbility = true;
	    }else
	    if ((abilityNr == 2) & (openPlanetBonus > 0)){
			hasAbility = true;
	    }else
	    if ((abilityNr == 3) & (closedPlanetBonus > 0)){
			hasAbility = true;
	    }else
	    if ((abilityNr == 4) & (techBonus > 0)){
			hasAbility = true;
	    }else
	    if ((abilityNr == 5) & (resistanceBonus > 0)){
			hasAbility = true;
	    }else
	    if ((abilityNr == 6) & (shieldCapacity > 0)){
			hasAbility = true;
	    }else
	    if ((abilityNr == 7) & (CannonDamage > 0)){
			hasAbility = true;
	    }else
	    if ((abilityNr == 8) & (wharfSize > 0)){
			hasAbility = true;
	    }else
	    if ((abilityNr == 9) & (buildVIPTypes.size() > 0)){
			hasAbility = true;
	    }else
	    if ((abilityNr == 10) & autoDestructWhenConquered){
			hasAbility = true;
	    }else    
	    if ((abilityNr == 11) & inOrbit){
			hasAbility = true;
	    }else
	    if ((abilityNr == 12) & !inOrbit){
			hasAbility = true;
	    }else
	    if ((abilityNr == 13) & planetUnique){
			hasAbility = true;
	    }else
		if ((abilityNr == 14) & !visibleOnMap){
			hasAbility = true;
		}
		return hasAbility;
	}

	public String getBuildingInfoDroid(Buildings buildingsGW) {
		  StringBuffer sb = new StringBuffer();
		  sb.append("<h4>Building: ");
		  sb.append(name + " (" + shortName + ")");
		  sb.append("</h4>");
		  sb.append(description);
		  sb.append("<p>");
		  sb.append("<b>Abilities: </b>");
		  sb.append("<br>");
		  List<String> abilities = getAbilitiesStringsDroid();
		  for (String ability : abilities) {
			  sb.append(ability);
			  sb.append("<br>");
		  }
		  sb.append("<br>");
		  boolean addBreak = false;
		  if (parentBuildingType != null){
			  sb.append("<b>Upgrade from: </b>");
			  sb.append(parentBuildingType.getName());
			  sb.append("<br>");
			  addBreak = true;
		  }
		  List<BuildingType> upgradebleBuildingTypes = buildingsGW.getNextBuildingSteps(this);
		  if (upgradebleBuildingTypes.size() > 0){
			  sb.append("<b>Upgrade to: </b>");
			  sb.append(upgradebleBuildingTypes.get(0).getName()); // XXX only works with DroidGW where there are only one child upgrade 
			  sb.append("<br>");
			  addBreak = true;
		  }
		  if (addBreak){
			  sb.append("<br>");
		  }
		  sb.append("<b>Build cost: </b>");
		  sb.append(getBuildCost(null));
		  return sb.toString();
	}

	public static String getAllAbilitiesDroid(){
	    List<String> allStrings = new LinkedList<String>();	    
    	allStrings.add("Spaceport");
    	allStrings.add("Open Income Bonus");
    	allStrings.add("Closed Income Bonus");
    	allStrings.add("Tech Bonus");
    	allStrings.add("Resistance bonus");
    	allStrings.add("Bombardment defence");
    	allStrings.add("Missile battery");
    	allStrings.add("Spaceship construction");
    	allStrings.add("VIP recruitment");
    	allStrings.add("Autodestruct when planet is conquered");
    	allStrings.add("Planetside/orbital building");
    	allStrings.add("Not visible on map");
    	allStrings.add("Planet Unique");
		StringBuffer sb = new StringBuffer();
		for (String string : allStrings) {
			sb.append(string);
	        sb.append("&nbsp;<br>");
		}
	    return sb.toString();
	}

	public List<String> getAbilitiesStringsDroid(){
	    List<String> allStrings = new LinkedList<String>();	    
	    if (spaceport){
	        allStrings.add("Spaceport");
	    }
	    if (openPlanetBonus > 0){
	        allStrings.add("Open Income Bonus: " + openPlanetBonus);
	    }
	    if (closedPlanetBonus > 0){
	        allStrings.add("Closed Income Bonus: " + closedPlanetBonus);
	    }
	    if (techBonus > 0){
	        allStrings.add("Tech Bonus: " + techBonus + "%");
	    }
	    if (resistanceBonus > 0){
	    	allStrings.add("Resistance bonus: " + resistanceBonus);
	    }
	    if (shieldCapacity > 0){
	    	allStrings.add("Bombardment defence : " + shieldCapacity);
	    }
	    if (CannonDamage > 0){
	    	allStrings.add("Missile battery:");
	    	allStrings.add("- Damage: " + CannonDamage);
	    	allStrings.add("- Rate Of Fire: " + CannonRateOfFire);
	    	allStrings.add("- Hit chance: " + CannonHitChance);
	    }
	    if (wharfSize > 0){
	        allStrings.add("Spaceship construction, max size " + wharfSize);
	    }
	    if (buildVIPTypes.size() > 0){
	    	allStrings.add("VIP recruitment:"); 
	    	for (VIPType vipType : buildVIPTypes) {
	    		allStrings.add("&nbsp&nbsp-" + vipType.getName() + " (cost: " + vipType.getBuildCost() + ")");
			}
	    }
	    if (autoDestructWhenConquered){
	    	allStrings.add("Autodestruct when planet is conquered");
	    }	    
	    if (inOrbit){
	    	allStrings.add("Orbital building");
	    }else{
	    	allStrings.add("Planetside building");
	    }
	    if(!visibleOnMap){
	    	allStrings.add("Not visible On Map");
    	}
	    if (planetUnique){
	        allStrings.add("Planet Unique");
	    }
//	    if (playerUnique){
//	        allStrings.add("Player Unique");
//	    }
	    return allStrings;
	}

}
