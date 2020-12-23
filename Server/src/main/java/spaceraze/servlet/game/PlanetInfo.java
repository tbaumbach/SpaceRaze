package spaceraze.servlet.game;

import java.util.ArrayList;
import java.util.List;

import spaceraze.servlethelper.game.DiplomacyPureFunctions;
import spaceraze.servlethelper.game.planet.PlanetPureFunctions;
import spaceraze.servlethelper.game.spaceship.SpaceshipPureFunctions;
import spaceraze.servlethelper.game.troop.TroopPureFunctions;
import spaceraze.servlethelper.map.MapPureFunctions;
import spaceraze.world.Building;
import spaceraze.world.Galaxy;
import spaceraze.world.Planet;
import spaceraze.world.Player;
import spaceraze.world.Spaceship;
import spaceraze.world.Troop;
import spaceraze.world.VIP;
import spaceraze.world.enums.SpaceShipSize;

/*
 * Denna klass hanterar all information som en spelar ser om en planet under pågående drag.
 * Tar inte full hänsyn till att det vore grymt om det gick att se hur kartan såg ut några turns tillbaka.
 * Men det går att lösa genom att denna klass sparas som json som ändå kan vara en bra ide för att minska på svarstiden.
 * Alltså är det möjligt att denna info skapas vid uppdatering av en turn. 
 * Eller så får denna klass skapas varje turn och sparas under i en lista och sedan backar man i tiden för att hitta senaste informationen om planenten
 * 
 * Denna klass ska skapas till varje spelar vid uppdatering av turn och sparas i Player.
 * Vilket då gör att det går att se tillbaka hur kartan såg ut vid valfri turn.
 */
public class PlanetInfo {
	
	private String name;
	private String owner;// namnet på gov som äger planeten, "neutral" om neutral. null om okänd(closed planets the players haven't get any information from).
	// behövs faction? annars får det hämtas från spelarnas info.
	private boolean open, razed, besieged;
	private int basePopulation = -1;
	private int population, resistance;
	private int lastInfoTurn = 0; // Används för att tala om för spelarn vilken turn informationen kommer ifrån.
	private String notes;
	private List<BuildingInfo> buildings; // Här visas byggnader som tillhör ägaren. Alltså inte bara spelarens byggnader. Kan bara finnas en ägare av byggnader på en planet.t
	private List<VIPInfo> vips; // Innehåller alla spelarens, allierade och synliga fientliga VIPar. OBS bara VIPar på planeten, ej på skepp eller troops.
	
	
	// The owners ships
	private List<ShipInfo> ships;
	private List<TroopInfo> troops;
	// Other plyers/neutral fleets(size and civilians)
	private List<FleetInfo> fleets;
	// Other plyers/neutral armys(troops numbers)
	private List<ArmyInfo> armys;
	
	
	
	//TODO borde vi även lägga till battlereports för skepp o trupper? eller ska det ligga i turnInfo. 
	
	
	//TODO Det under denna rad är sådant som ännu inte är hanterat och bara inklippt från MapPlanetInfo. För att inte glömma något.
	// info about last known info. 
	// Försöker använda de riktiga värderna i stället och använder lastInfoTurn för att avgöra om det är historiska värden eller ej.
	//private String lastKnownOwner; // null = neutral. Påverkar planetens färg. 
	//private String lastKnownMaxShipSize; // visa bara en grå storlek även om det fanns flera flottor vid planeten. Inkluderar info om civila skepp, t.ex. "small+civ"
	//private List<String> lastKnownBuildingsInOrbit,lastKnownBuildingsOnSurface;
	//private boolean lastKnownRazed;
	
	//PlanetInfo(){};
	
	
	PlanetInfo(Planet planet, Player player){
		buildings = new ArrayList<BuildingInfo>();
		vips = new ArrayList<VIPInfo>();
		ships = new ArrayList<ShipInfo>();
		troops = new ArrayList<TroopInfo>();
		fleets = new ArrayList<FleetInfo>();
		armys = new ArrayList<ArmyInfo>();
		
		name = planet.getName();
		razed = planet.isRazed();
		//TODO klasserna PlanerInfos och sr.world.PlanetInfo ska ersättas av denna klass enligt samma model som MapPLanetInfo.
		// Frågan är då om notes här kommer bli orginalet? Annars är all information här hämtad från andra källor.
		// Kanske enkelt att bara skapa en länkad list i player med planet name som nyckel. Vänta med att göra det tills det går att spela på siten.
		// Vill inte förstöra möjligheten att använda spel körde i swing klienten.
		notes = PlanetPureFunctions.findPlanetInfo(planet.getName(), player.getPlanetInformations()).getNotes();
		
		Galaxy galaxy = player.getGalaxy();
		boolean haveSpy = (galaxy.findVIPSpy(planet,player) != null);
		boolean alliedSpy = PlanetPureFunctions.isItAlliedSpyOnPlanet(player, planet, galaxy);
		boolean spy = haveSpy || alliedSpy;
		boolean surveyShip = SpaceshipPureFunctions.findSurveyShip(planet,player, galaxy.getSpaceships(), galaxy.getGameWorld()) != null;
		boolean alliedSurveyShip = PlanetPureFunctions.isItAlliesSurveyShipsOnPlanet(player, planet, galaxy);
		boolean surveyVIP = (galaxy.findSurveyVIPonShip(planet,player) != null);
		boolean alliedSurveyVIP = PlanetPureFunctions.isItAlliesSurveyVipOnPlanet(player, planet, galaxy);
		boolean survey = surveyShip || alliedSurveyShip || surveyVIP || alliedSurveyVIP;
		boolean shipInSystem = (galaxy.playerHasShipsInSystem(player,planet));
		boolean alliedShipsInSystem = PlanetPureFunctions.isItAlliedShipsInSystem(player, planet, galaxy);
		
		
		if(shipInSystem){
			addShips(player, planet, galaxy);
		}
		
		addTroops(player, planet, galaxy);
		
		boolean haveTroopsOnTheGround = (troops.size() > 0);
		
		
	//	if(!razed){ // a razed planet is a dead planet = nothing on it.
			open = planet.isOpen();
						
			boolean isOwner = planet.isPlanetOwner(player);
			
			
			//Checks if the planets owner is a allied = show all.
			boolean isAllied = !PlanetPureFunctions.isEnemyOrNeutralPlanet(player, planet, galaxy);
			
			if(open || shipInSystem || alliedShipsInSystem || isOwner || isAllied || spy || survey || haveTroopsOnTheGround){
				
				if(planet.isPlayerPlanet()){
					owner = planet.getPlayerInControl().getGovernorName();
				}else{
					owner = "neutral";
				}
				
				basePopulation = planet.getBasePopulation();
				besieged = planet.isBesieged();
				population = planet.getPopulation();
				resistance = planet.getResistance();
				lastInfoTurn = galaxy.getTurn();
				
				//TODO remove this after ew have made the history thing, checking older planetInfos.
				//lastKnownOwner = null;
				//lastKnownMaxShipSize = null;
				//lastKnownRazed = false;
				
				getOthersFleets(planet, player, galaxy);
								
				addVIPs(player, planet, galaxy, isAllied, spy, survey, haveTroopsOnTheGround);
				
				// Information from the ground.
				if(isOwner || isAllied || spy || survey || haveTroopsOnTheGround){
					getOthersArmys(planet, player, galaxy, true);
					addBuildings(planet.getBuildings());
				} else if(open || shipInSystem || alliedShipsInSystem){// Information from orbit, can't see cloaked units.
					getOthersArmys(planet, player, galaxy, false);
					addBuildings(planet.getBuildingsByVisibility(true)); // bara buildings som syns på kartan.
				}
				
				
			}else{
				//TODO Ingen ny information från denna turn. Hämta information från tidigare turns.
				// Buildings, VIPs  mm. När det gäller VIPar så ska aldrig allierads VIPar synas här då spelaren alltid har ny info om dem.
				
				// lastInfoTurn Glöm inte att fylla i denna här. lastInfoTurn är det värdet som avgör om det är historiska värden eller live info.
				// lastKnownProd,lastKnownRes hanteras genom att använda vanliga prod/res med kombination av lastInfoTurn. Gällar alla värden.
			}
			
			
			
	//	}else{ // TODO Planet is razed = allways closed, no one that can report from it. Only if the player or Allied have ships, spys.
			
	//	}
		
		
	}
	
	private void getOthersFleets(Planet planet, Player player, Galaxy g){
        // loopa igenom alla spelare och kolla efter flottor
        for (Player tempPlayer : g.getPlayers()) {
        	if (tempPlayer != player){
        		int shipSize = MapPureFunctions.getLargestLookAsMilitaryShipSizeOnPlanet(planet,tempPlayer, g);
        		boolean civilianExists = !MapPureFunctions.getLargestShipSizeOnPlanet(planet,tempPlayer,true, player.getGalaxy()).equals("");
        		if ((shipSize > -1) | civilianExists){
        			FleetInfo fleet = new FleetInfo(tempPlayer.getGovernorName(),shipSize,civilianExists);
        			fleets.add(fleet);
        		}
        	}
        }
        // kolla efter neutrala skepp
        int shipSize = MapPureFunctions.getLargestLookAsMilitaryShipSizeOnPlanet(planet,null, g);
        if (shipSize > -1){
        	FleetInfo fleet = new FleetInfo(null,shipSize,false);
    		fleets.add(fleet);
        }
        
    }
	
	private void getOthersArmys(Planet planet, Player player, Galaxy g, boolean showUnVisible){
        // loopa igenom alla spelare och kolla efter troops
        for (Player tempPlayer : g.getPlayers()) {
        	if (tempPlayer != player){
        		List<Troop> troopsOnPlanet = TroopPureFunctions.getTroopsOnPlanet(planet, tempPlayer, showUnVisible, g.getTroops());
        		if(troopsOnPlanet.size()> 0){
        			armys.add(new ArmyInfo(tempPlayer.getGovernorName(),troopsOnPlanet.size()));
        		}
        	}
        }
        List<Troop> troopsOnPlanet = TroopPureFunctions.getTroopsOnPlanet(planet, null, showUnVisible, g.getTroops());
        if(troopsOnPlanet.size()> 0){
			armys.add(new ArmyInfo("Neutral",troopsOnPlanet.size()));
		}
    }
	
	private void addVIPs(Player player, Planet planet, Galaxy galaxy, boolean isAllied, boolean haveSpy, boolean survey, boolean haveTroopsOnTheGround){
		
		for (VIP aVIP : galaxy.getAllVIPs()) {
			if (aVIP.getPlanetLocation() == planet){
				if(aVIP.getBoss() == player || DiplomacyPureFunctions.checkAllianceWithAllInConfederacy(player, aVIP.getBoss(), galaxy)){
					vips.add(new VIPInfo(aVIP, player));
				}else if(open || isAllied ||haveSpy || survey || haveTroopsOnTheGround){ // VIPar som  tillhör fiender. Alltså VIPar som inte finns på spelarens eller dess allierades planeter. 
					if (aVIP.getShowOnOpenPlanet()){
						vips.add(new VIPInfo(aVIP, player));
					}
				}
			}//Check if VIP are on a ship 
			else if(aVIP.getShipLocation() != null && aVIP.getBoss() == player){
				ShipInfo aShip = findShip(aVIP.getShipLocation().getUniqueName());
				if(aShip != null){
					aShip.addVIP(new VIPInfo(aVIP, player));
				}
			}//Check if VIP are on a troop
			else if(aVIP.getTroopLocation() != null && aVIP.getBoss() == player){
				TroopInfo troop = findTroop(aVIP.getTroopLocation().getName());
				if(troop != null){
					troop.addVIP(new VIPInfo(aVIP, player));
				}
			}
		}
    }
	
	private ShipInfo findShip(String name){
		for (ShipInfo ship: ships) {
			if(ship.getName().equals(name)){
				return ship;
			}else{
				for (ShipInfo squdron : ship.getSqudrons()) {
					if(squdron.getName().equals(name)){
						return squdron;
					}	
				}
			}
		}
		return null;
	}
	
	private TroopInfo findTroop(String name){
		for (TroopInfo troop: troops) {
			if(troop.getName().equals(name)){
				return troop;
			}
		}
		
		for (ShipInfo ship: ships) {
			for (TroopInfo troop : ship.getTroops()) {
				if(troop.getName().equals(name)){
					return troop;
				}
			}
			
			for (ShipInfo squdron : ship.getSqudrons()) {
				for (TroopInfo troop : squdron.getTroops()) {
					if(troop.getName().equals(name)){
						return troop;
					}
				}	
			}
		}
		return null;
	}

	private void addBuildings(List<Building> planetBuildings) {
		for (Building building : planetBuildings) {
			buildings.add(new BuildingInfo(building));
		}
	}
	
	//The owners ships.
	private void addShips(Player player, Planet planet, Galaxy galaxy){
		//TODO kolla upp att även squadroner som befinner sig i en carrier följer med i listan.
		List<Spaceship> spaceShips = SpaceshipPureFunctions.getPlayersSpaceshipsOnPlanet(player, planet, galaxy.getSpaceships());
		List<Spaceship> squdronsOnShip = new ArrayList<Spaceship>();
		
		//Add all ships to the planets
		for (Spaceship spaceship : spaceShips) {
			if(spaceship.getSize() == SpaceShipSize.SQUADRON && spaceship.getCarrierLocation() != null){
				squdronsOnShip.add(spaceship);
			}else{
				ships.add(new ShipInfo(spaceship, galaxy.getGameWorld()));
			}
		}
		
		//Add ships(squdrons) to carriers on the planet.
		for (Spaceship aSqudron : squdronsOnShip) {
			for (ShipInfo aShip : ships) {
				if(aShip.getName().equals(aSqudron.getCarrierLocation().getUniqueName())){
					aShip.addSqudron(aShip);
				}
			}
		}
		
	}
	
	
	//The owners Troops.
	private void addTroops(Player player, Planet planet, Galaxy galaxy){
		List<Troop> troopsOnPlanet = TroopPureFunctions.getPlayersTroopsOnPlanet(player, planet, galaxy.getTroops());
		
		for (Troop troop : troopsOnPlanet) {
			if(troop.getShipLocation() != null){
				ShipInfo ship = findShip(troop.getShipLocation().getUniqueName());
				ship.addTroop(new TroopInfo(troop, galaxy.getGameWorld()));
			}else{
				troops.add(new TroopInfo(troop, galaxy.getGameWorld()));
			}
		}
	}

	public String getName() {
		return name;
	}

	public boolean isOpen() {
		return open;
	}

	public String getOwner() {
		return owner;
	}

	public int getBasePopulation() {
		return basePopulation;
	}


	public boolean isRazed() {
		return razed;
	}


	public boolean isBesieged() {
		return besieged;
	}


	public int getPopulation() {
		return population;
	}


	public int getResistance() {
		return resistance;
	}


	public int getLastInfoTurn() {
		return lastInfoTurn;
	}


	public String getNotes() {
		return notes;
	}


	public List<BuildingInfo> getBuildings() {
		return buildings;
	}


	public List<VIPInfo> getVips() {
		return vips;
	}


	public List<ShipInfo> getShips() {
		return ships;
	}


	public List<FleetInfo> getFleets() {
		return fleets;
	}

	public List<TroopInfo> getTroops() {
		return troops;
	}

	public List<ArmyInfo> getArmys() {
		return armys;
	}

}
