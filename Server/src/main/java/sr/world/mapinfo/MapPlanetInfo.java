package sr.world.mapinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sr.general.logging.Logger;
import sr.world.Building;
import sr.world.Galaxy;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.VIP;
import sr.world.comparator.spaceship.SpaceshipSizeAndBuildCostComparator;

/**
 * Hanterar kart-informationen för en enskild planet för ett visst drag för en viss spelare
 * 
 * @author Paul Bodin
 *
 */
public class MapPlanetInfo implements Serializable {
	static final long serialVersionUID = 1L;
	public static final String NEUTRAL = "neutral";
	
	// värden som skall visas på kartan om en spelares nuvarande kunskap om planeten som denna instans gäller
	private String owner; // namnet på gov som äger planeten, "neutral" om neutral. Kan visas i lagpartier?
	private List<String> buildingsHidden,buildingsVisible; // alla buildings tillhör ägaren av planeten
	private VIPData ownVIPs,otherVIPs; // det kan aldrig vara vippar från mer än en annan spelare än sina egna vippar
	private List<String> shipsOwn; // gäller endast egna skepp, varje skeppssträng kan börja med en siffra för hur många skepp det är av den typen 
	private List<FleetData> fleetsOther; // max size för neutrala och andra flottor, inkl civila skepp
	private boolean razed,open,besieged;
	private String prod,res,lastKnownProd,lastKnownRes;
	
	// planet notes sätts via order men lagras här
	private String notes; 
	
	// info about last known info
	private String lastKnownOwner; // null = neutral. Påverkar planetens färg.
	private String lastKnownMaxShipSize; // visa bara en grå storlek även om det fanns flera flottor vid planeten. Inkluderar info om civila skepp, t.ex. "small+civ"
	private List<String> lastKnownBuildingsInOrbit,lastKnownBuildingsOnSurface;
	private boolean lastKnownRazed;
	private int lastInfoTurn = 0; // används även för att lagra nuvarande turn om det finns data om en planet

	/**
	 * Skapa all info om planet som spelaren player känner till det just nu, för att beskriva hur
	 * player:s karta kommer att se ut då player gör sitt nästa drag.
	 * 
	 * MapPlanetInfos behövs för att kunna hitta lastKnownInfo-information
	 */
	public MapPlanetInfo(Planet planet, Player player, MapInfos mapPlanetInfos, int turn){
		Galaxy g = player.getGalaxy();
		Logger.finer("MapPlanetInfo creator, planet: " + planet.getName() + ", player: " + player.getGovenorName() + ", turn: " + turn);
		boolean spy = (g.findVIPSpy(planet,player) != null);
		boolean shipInSystem = (g.playerHasShipsInSystem(player,planet));
		boolean surveyShip = (g.findSurveyShip(planet,player) != null);
		boolean surveyVIP = (g.findSurveyVIPonShip(planet,player) != null);
		boolean survey = surveyShip | surveyVIP;
		boolean openPlanet = planet.isOpen();
		boolean neutralPlanet = (planet.getPlayerInControl() == null);
		boolean ownsPlanet = planet.getPlayerInControl() == player;
		MapPlanetInfo lastKnownOwnerMapPlanetInfo = mapPlanetInfos.getLastKnownOwnerInfo(planet);
		MapPlanetInfo lastKnownProdResMapPlanetInfo = mapPlanetInfos.getLastKnownProdResInfo(planet);
		MapPlanetInfo lastKnownRazedMapPlanetInfo = mapPlanetInfos.getLastKnownRazedInfo(planet);
		// owner, lastKnownOwner, lastInfoTurn, razed, lastKnownMaxShipSize
		if (ownsPlanet){
			owner = planet.getPlayerInControl().getName();
			razed = false;
			besieged = planet.isBesieged();
			lastInfoTurn = turn;
			lastKnownOwner = null;
			lastKnownMaxShipSize = null;
			lastKnownRazed = false;
		}else
		if (spy | shipInSystem | openPlanet){
//			Logger.info("shipInSystem: " + shipInSystem);
			if (neutralPlanet){
//				Logger.info("neutralPlanet: " + neutralPlanet);
				if (planet.isRazed()){
//					Logger.info("planet.isRazed(): " + planet.isRazed());
					owner = null;
					razed = true;
//					lastKnownRazed = true;
				}else{
					owner = NEUTRAL;
					razed = false;
//					lastKnownRazed = false;
				}
			}else{
				owner = planet.getPlayerInControl().getName();
				razed = false;
//				lastKnownRazed = false;
			}
			besieged = planet.isBesieged();
			lastInfoTurn = turn;
			lastKnownOwner = null;
			lastKnownMaxShipSize = null;
			lastKnownRazed = false;
		}else{ // ingen info detta drag
			Logger.finer("no current info about planet this turn");
			owner = null;
			razed = false;
			besieged = false;
			// there can both a lastKnownOwner and lastKnownRazed on the same planet, only use the one with the
			// latest turn nr.
			if ((lastKnownOwnerMapPlanetInfo != null) | (lastKnownRazedMapPlanetInfo != null)){
				int lastOwnerTurn = -1;
				int lastRazedTurn = -1;
				if (lastKnownOwnerMapPlanetInfo != null){
					lastOwnerTurn = lastKnownOwnerMapPlanetInfo.getLastInfoTurn();
				}
				if (lastKnownRazedMapPlanetInfo != null){
					lastRazedTurn = lastKnownRazedMapPlanetInfo.getLastInfoTurn();
				}
//				Logger.fine("lastOwnerTurn: " + lastOwnerTurn);
//				Logger.fine("lastRazedTurn: " + lastRazedTurn);
				if (lastOwnerTurn > lastRazedTurn){
					Logger.finer("lastKnownOwnerMapPlanetInfo = senaste infon");
					lastKnownOwner = lastKnownOwnerMapPlanetInfo.getOwner();
					lastInfoTurn = lastKnownOwnerMapPlanetInfo.getLastInfoTurn();
					lastKnownRazed = lastKnownOwnerMapPlanetInfo.isRazed();
					lastKnownMaxShipSize = getLastKnownLargetsSizeShipOnPlanet(lastKnownOwnerMapPlanetInfo);
				}else{
					Logger.finer("info om att den varit razead tidigare!");
					lastKnownOwner = "neutral";
					lastInfoTurn = lastKnownRazedMapPlanetInfo.getLastInfoTurn();
					lastKnownRazed = true;
					lastKnownMaxShipSize = null;
//					Logger.fine("lastKnownRazed: " + lastKnownRazed);
//					Logger.fine("lastInfoTurn: " + lastInfoTurn);
				}
			}else{
				Logger.finer("ingen info alls!!!");
				lastKnownOwner = "neutral";
				lastInfoTurn = 1;
				lastKnownRazed = false;
				lastKnownMaxShipSize = null;
			}
		}
		// open
		open = openPlanet;
		// shipsOwn
		if (shipInSystem){
			shipsOwn = getShipsList(planet, player, g);
		}
		// fleetsOther
		if (spy | shipInSystem | openPlanet | ownsPlanet){
			fleetsOther = getOtherFleets(planet, player, g);
		}
		// VIPs
		ownVIPs = getOwnVIPsData(player,planet,g);
		if (openPlanet | spy){
			otherVIPs = getOtherVIPsData(player,planet,g);
		}
		// buildingsOrbit, buildingsSurface, lastKnownBuildingsInOrbit, lastKnownBuildingsOnSurface
		if (openPlanet | shipInSystem | spy | ownsPlanet | survey){
//			buildingsOrbit = getBuildingsList(planet.getBuildingsInOrbit());
			buildingsVisible = getBuildingsList(planet.getBuildingsByVisibility(true));
			lastKnownBuildingsInOrbit = null;
//			if (openPlanet | spy | survey | ownsPlanet){
			if (spy | survey | ownsPlanet){
//				buildingsSurface = getBuildingsList(planet.getBuildingsOnSurface());
				buildingsHidden = getBuildingsList(planet.getBuildingsByVisibility(false));
				lastKnownBuildingsOnSurface = null;
			}
		}else{
			buildingsVisible = null;
			buildingsHidden = null;
			if (lastKnownOwnerMapPlanetInfo != null){
				lastKnownBuildingsInOrbit = lastKnownOwnerMapPlanetInfo.getBuildingsVisible();
				lastKnownBuildingsOnSurface = lastKnownOwnerMapPlanetInfo.getBuildingsHidden();
			}else{
				lastKnownBuildingsInOrbit = null;
				lastKnownBuildingsOnSurface = null;
			}
		}
		// prod, res
//		if (openPlanet | spy | survey | ownsPlanet){
//			prod = planet.getPopulation();
//			res = planet.getResistance();
//			lastKnownProd = -1;
//			lastKnownRes = -1;
//		}else{
//			if (lastKnownMapPlanetInfo != null){
//				prod = -1;
//				res = -1;
//				lastKnownProd = lastKnownMapPlanetInfo.getProd();
//				lastKnownRes = lastKnownMapPlanetInfo.getRes();
//			}else{
//				prod = -1;
//				res = -1;
//				lastKnownProd = -1;
//				lastKnownRes = -1;
//			}
//		}
		if (openPlanet | spy | survey | ownsPlanet){
        	if (planet.isRazed()){
    			prod = "-";
        		if (planet.isRazedAndUninfected()){
        			res = "-";
        		}else{
        			// alien player
        			res = String.valueOf(planet.getResistance());
        		}
        	}else{
    			prod = String.valueOf(planet.getPopulation());
    			res = String.valueOf(planet.getResistance());
        	}
			lastKnownProd = null;
			lastKnownRes = null;
		}else{
			prod = null;
			res = null;
			if (lastKnownProdResMapPlanetInfo != null){
				lastKnownProd = String.valueOf(lastKnownProdResMapPlanetInfo.getProd());
				lastKnownRes = String.valueOf(lastKnownProdResMapPlanetInfo.getRes());
			}else{
				lastKnownProd = null;
				lastKnownRes = null;
			}
		}
		// notes
		notes = player.getPlanetInfos().getNotes(planet.getName());
//		Logger.info("razed: " + razed);
//		Logger.info("lastKnownRazed: " + lastKnownRazed);
	}
	
	/**
	 * 
	 * @param player om null leta efter vippar från andra spelare
	 * @return
	 */
	private VIPData getOwnVIPsData(Player player, Planet planet, Galaxy g){
		VIPData vipsData = new VIPData();
		vipsData.setPlayerName(player.getName());
		for (VIP aVIP : g.getAllVIPs()) {
			if (aVIP.getBoss() == player){ // leta efter vippar som tillhör spelaren
				if (aVIP.getPlanetLocation() == planet){
					vipsData.addVipShortName(aVIP.getShortName());
				}
			}
		}
		if (vipsData.getVipShortNames().size() == 0){
			vipsData = null;
		}
        return vipsData;
    }

	private VIPData getOtherVIPsData(Player player, Planet planet, Galaxy g){
		VIPData vipsData = new VIPData();
		for (VIP aVIP : g.getAllVIPs()) {
			if (aVIP.getBoss() != player){ // leta efter vippar som inte tillhör spelaren
				if (aVIP.getPlanetLocation() == planet){
					if (aVIP.getShowOnOpenPlanet()){
						vipsData.addVipShortName(aVIP.getShortName());
						vipsData.setPlayerName(aVIP.getBoss().getName()); // förutsätter att alla andra vippar tillhör samma spelare
					}
				}
			}
		}
		if (vipsData.getVipShortNames().size() == 0){
			vipsData = null;
		}
        return vipsData;
    }

    private List<FleetData> getOtherFleets(Planet planet, Player player, Galaxy g){
    	List<FleetData> fleets = new LinkedList<FleetData>();
        // loopa igenom alla spelare och kolla efter flottor
        for (Player tempPlayer : g.getPlayers()) {
        	if (tempPlayer != player){
        		int shipSize = g.getLargestLookAsMilitaryShipSizeOnPlanet(planet,tempPlayer);
        		boolean civilianExists = !player.getGalaxy().getLargestShipSizeOnPlanet(planet,tempPlayer,true).equals("");
        		if ((shipSize > -1) | civilianExists){
        			FleetData fleetData = new FleetData(tempPlayer.getGovenorName(),shipSize,civilianExists);
        			fleets.add(fleetData);
        		}
        	}
        }
        // kolla efter neutrala skepp
        int shipSize = player.getGalaxy().getLargestLookAsMilitaryShipSizeOnPlanet(planet,null);
        if (shipSize > -1){
    		FleetData fleetData = new FleetData(null,shipSize,false);
    		fleets.add(fleetData);
        }
        return fleets;
    }

	private String getLastKnownLargetsSizeShipOnPlanet(MapPlanetInfo lastKnownMapPlanetInfo){
		String maxSizeIncCiv = "";
		if (lastKnownMapPlanetInfo.getFleetsOther() != null){
			int maxSize = 0;
			boolean civ = false;
			for (FleetData fleetData : lastKnownMapPlanetInfo.getFleetsOther()) {
				if (fleetData.getMaxSize() > maxSize){
					maxSize = fleetData.getMaxSize();
				}
				if (fleetData.isCiv()){
					civ = true;
				}
			}
			if (maxSize > 0){
				maxSizeIncCiv = getSizeString(maxSize);
			}
			if (civ){
				if (maxSize > 0){
					maxSizeIncCiv += "+";
				}
				maxSizeIncCiv += "civ";
			}
		}else{
			maxSizeIncCiv = null;
		}
		return maxSizeIncCiv;
	}
	
	private String getSizeString(int size){
	    String sizeString = "small";
	    if (size == 2){
	        sizeString = "medium";
	    }else
	    if (size == 3){
	        sizeString = "large";
	    }else
	    if (size == 5){
	        sizeString = "huge";
	    }
	    return sizeString;
	}
	
	private List<String> getShipsList(Planet planet, Player player, Galaxy g){
		// list to return
		List<String> shipStrings = new LinkedList<String>();
        // get spaceships on this planet, both civ and military
        List<Spaceship> shipsOnPlanet = g.getShips(planet,false);
        shipsOnPlanet.addAll(g.getShips(planet,true));
        List<Spaceship> shipsPlayer = new LinkedList<Spaceship>();
        for (Spaceship spaceship : shipsOnPlanet) {
			if (spaceship.getOwner() == player){
				shipsPlayer.add(spaceship);
			}
		}
        // sort the ships
        Collections.sort(shipsPlayer,new SpaceshipSizeAndBuildCostComparator());
        int shipCount = 0;
        int i = 0;
        boolean addShip = false; // anv. för att avgöra när alla skepp av en typ har hittats (så att räknaren blir rätt)
        List<VIP> tmpVips = new ArrayList<VIP>();
        for (Spaceship spaceship : shipsPlayer) {
        	shipCount++;
        	tmpVips = g.findAllVIPsOnShip(spaceship);
        	if (i == (shipsPlayer.size() - 1)){
        		addShip = true;
        	}else{
        		Spaceship spaceshipNext = shipsPlayer.get(i+1);
        		if (!spaceshipNext.getSpaceshipType().getName().equals(spaceship.getSpaceshipType().getName())){
        			addShip = true;
        		}
        	}
        	if (addShip){
	          	String tmpShipStr = spaceship.getSpaceshipType().getShortName();
	          	if (shipCount > 1){
	          		tmpShipStr = shipCount + " " + tmpShipStr;
	          	}
	          	if (tmpVips.size() > 0){
	          		tmpShipStr = tmpShipStr + " (";
	          	}
	          	if (tmpVips.size() > 0){
	          		for (Iterator<VIP> iter = tmpVips.iterator(); iter.hasNext();) {
						VIP aVIP = iter.next();
						tmpShipStr = tmpShipStr + aVIP.getShortName();
						if (iter.hasNext()){
							tmpShipStr = tmpShipStr + ",";
						}
					}
	          	}
	          	if (tmpVips.size() > 0){
	          		tmpShipStr = tmpShipStr + ")";
	          	}
	          	shipStrings.add(tmpShipStr);
	            shipCount = 0;
	            tmpVips = new ArrayList<VIP>();
          }
          i++;
        }
        return shipStrings;
	}

	private List<String> getBuildingsList(List<Building> buildings){
		List<String> buildingsList = null;
		if (buildings.size() > 0){
			buildingsList = new LinkedList<String>();
			for (Building building : buildings) {
				buildingsList.add(building.getBuildingType().getShortName());
			}
		}
		return buildingsList;
	}

	private String createBuildingString(List<String> buildings){
		StringBuffer sb = new StringBuffer();
		if (buildings != null){
			for (String buildingName : buildings) {
				if (sb.length() > 0){
					sb.append(", ");
				}
				sb.append(buildingName);
			}
		}
		return sb.toString();
	}

	public int getLastInfoTurn(){
		return lastInfoTurn;
	}

	public String getLastKnownOwner(){
		return lastKnownOwner;
	}

	public String getProd() {
		return prod;
	}

	public String getRes() {
		return res;
	}

	public boolean isRazed() {
		return razed;
	}

	public boolean isBesieged() {
		return besieged;
	}

	public String getNotes() {
		return notes;
	}

	public String getLastKnownMaxShipSize() {
		return lastKnownMaxShipSize;
	}

	public String getOwner() {
		return owner;
	}

	public List<String> getBuildingsHidden() {
		return buildingsHidden;
	}

	public String getBuildingsHiddenString() {
		return createBuildingString(buildingsHidden);
	}

	public List<String> getBuildingsVisible() {
		return buildingsVisible;
	}

	public String getBuildingsVisibleString() {
		return createBuildingString(buildingsVisible);
	}

	public VIPData getOwnVIPs() {
		return ownVIPs;
	}

	public VIPData getOtherVIPs() {
		return otherVIPs;
	}

	public List<String> getShipsOwn() {
		return shipsOwn;
	}

	public List<FleetData> getFleetsOther() {
		return fleetsOther;
	}

	public boolean isOpen() {
		return open;
	}

	public List<String> getLastKnownBuildingsInOrbit() {
		return lastKnownBuildingsInOrbit;
	}

	public String getLastKnownBuildingsOrbitString() {
		return createBuildingString(lastKnownBuildingsInOrbit);
	}

	public List<String> getLastKnownBuildingsOnSurface() {
		return lastKnownBuildingsOnSurface;
	}

	public String getLastKnownBuildingsSurfaceString() {
		return createBuildingString(lastKnownBuildingsOnSurface);
	}

	public String getLastKnownRes() {
		return lastKnownRes;
	}

	public String getLastKnownProd() {
		return lastKnownProd;
	}

	public void setLastKnownRazed(boolean lastKnownRazed) {
		this.lastKnownRazed = lastKnownRazed;
	}

	public boolean isLastKnownRazed() {
		return lastKnownRazed;
	}

	/**
	 * Används endast för testning!!!
	 */
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("owner: " + owner);
		sb.append("\n");
		sb.append("razed: " + razed);
		sb.append("\n");
		sb.append("besiege: " + besieged);
		sb.append("\n");
		sb.append("open: " + open);
		sb.append("\n");
		sb.append("notes: " + notes);
		sb.append("\n");
		sb.append("lastKnownOwner: " + lastKnownOwner);
		sb.append("\n");
		sb.append("lastKnownMaxShipSize: " + lastKnownMaxShipSize);
		sb.append("\n");
		sb.append("lastInfoTurn: " + lastInfoTurn);
		sb.append("\n");
		if (shipsOwn != null){
			for (String str : shipsOwn) {
				sb.append("shipsOwn: " + str);
				sb.append("\n");
			}
		}
		if (buildingsHidden != null){
			for (String str : buildingsHidden) {
				sb.append("buildingsSurface: " + str);
				sb.append("\n");
			}
		}
		if (buildingsVisible != null){
			for (String str : buildingsVisible) {
				sb.append("buildingsOrbit: " + str);
				sb.append("\n");
			}
		}
		if (lastKnownBuildingsInOrbit != null){
			for (String str : lastKnownBuildingsInOrbit) {
				sb.append("lastKnownBuildingsInOrbit: " + str);
				sb.append("\n");
			}
		}
		if (lastKnownBuildingsOnSurface != null){
			for (String str : lastKnownBuildingsOnSurface) {
				sb.append("lastKnownBuildingsOnSurface: " + str);
				sb.append("\n");
			}
		}
		if (fleetsOther != null){
			for (FleetData fd : fleetsOther) {
				sb.append(fd.toString());
			}
		}
		sb.append("prod: " + prod);
		sb.append("\n");
		sb.append("res: " + res);
		sb.append("\n");
		sb.append("lastKnownProd: " + lastKnownProd);
		sb.append("\n");
		sb.append("lastKnownRes: " + lastKnownRes);
		sb.append("\n");
		if (ownVIPs != null){
			sb.append(ownVIPs.toString());
		}
		if (otherVIPs != null){
			sb.append(otherVIPs.toString());
		}
		sb.append("\n");
		return sb.toString();
	}
	
}
