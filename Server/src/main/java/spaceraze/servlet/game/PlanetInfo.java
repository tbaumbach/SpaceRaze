package spaceraze.servlet.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import sr.general.logging.Logger;
import sr.world.Building;
import sr.world.Galaxy;
import sr.world.Planet;
import sr.world.Player;
import sr.world.Spaceship;
import sr.world.VIP;
import sr.world.mapinfo.FleetData;

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
	private List<BuildingInfo> buildings; // Här visas byggnader som tillhör ägaren. Alltså inte bara spelarens byggnader.
	private List<VIPInfo> vips; // Innehåller alla spelarens, allierade och synliga fientliga VIPar.
	//TODO Vore snyggt om vi även visar fientliga VIPar som har dödat egna VIPar. Tyvärr vet vi ju inte typen utan bara egenskapen(assasin). Detta är överkurs.
	
	// The owners ships
	private List<ShipInfo> ships;
	// Other plyers/neutral fleets(size and civilians)
	private List<FleetInfo> fleets;
	
	//TODO ######### fixa för trupper. ##########
	//private List<TroopInfo> troops;
	
	//TODO borde vi även lägga till battlereports för skepp o trupper? eller ska det ligga i turnInfo. 
	
	
	//TODO Det under denna rad är sådant som ännu inte är hanterat och bara inklippt från MapPlanetInfo. För att inte glömma något.
	// info about last known info. 
	// Försöker använda de riktiga värderna i stället och använder lastInfoTurn för att avgöra om det är historiska värden eller ej.
	//private String lastKnownOwner; // null = neutral. P�verkar planetens f�rg. 
	//private String lastKnownMaxShipSize; // visa bara en gr� storlek �ven om det fanns flera flottor vid planeten. Inkluderar info om civila skepp, t.ex. "small+civ"
	//private List<String> lastKnownBuildingsInOrbit,lastKnownBuildingsOnSurface;
	//private boolean lastKnownRazed;
	
	
	
	PlanetInfo(Planet planet, Player player){
		buildings = new ArrayList<BuildingInfo>();
		vips = new ArrayList<VIPInfo>();
		ships = new ArrayList<ShipInfo>();
		fleets = new ArrayList<FleetInfo>();
		name = planet.getName();
		razed = planet.isRazed();
		//TODO klasserna PlanerInfos och sr.world.PlanetInfo ska ersättas av denna klass enligt samma model som MapPLanetInfo.
		// Frågan är då om notes här kommer bli orginalet? Annars är all information här hämtad från andra källor.
		// Kanske enkelt att bara skapa en länkad list i player med planet name som nyckel. Vänta med att göra det tills det går att spela på siten.
		// Vill inte förstöra möjligheten att använda spel körde i appleten.
		notes = player.getPlanetInfos().getNotes(planet.getName());
		
		Galaxy  galaxy = player.getGalaxy();
		// TODO här ska även allierdas VIPar/skepp/survey kollas = spelarn ser allt som dina allierade.
		boolean haveSpy = (galaxy.findVIPSpy(planet,player) != null);
		boolean surveyShip = (galaxy.findSurveyShip(planet,player) != null);
		boolean surveyVIP = (galaxy.findSurveyVIPonShip(planet,player) != null);
		boolean survey = surveyShip | surveyVIP;
		boolean shipInSystem = (galaxy.playerHasShipsInSystem(player,planet));
		
		
		//TODO när shipInSystem även betyder allierade skepp så ska deta bytas ut mot galaxy.playerHasShipsInSystem(player,planet)
		if(shipInSystem){
			addShips(player, planet, galaxy);
		}
		
		
	//	if(!razed){ // a razed planet is a dead planet = nothing on it.
			open = planet.isOpen();
			
			besieged = planet.isBesieged();
			
			boolean isOwner = planet.isPlanetOwner(player);
			
			
			//Checks if the planets owner is a allied = show all.
			boolean isAllied = planet.isEnemyOrNeutralPlanet(player);
			if(open || shipInSystem || isOwner || isAllied || haveSpy || survey){
				owner = planet.getPlayerInControl().getGovenorName();
				basePopulation = planet.getBasePop();
				besieged = planet.isBesieged();
				population = planet.getPopulation();
				resistance = planet.getResistance();
				lastInfoTurn = galaxy.getTurn();
				
				//TODO remove this after ew have made the history thing, checking older planetInfos.
				//lastKnownOwner = null;
				//lastKnownMaxShipSize = null;
				//lastKnownRazed = false;
				
				addVIPs(player, planet, galaxy, isOwner, isAllied, haveSpy, survey);
				
				if(isOwner || isAllied || haveSpy || survey){
					
					addBuildings(planet.getBuildings());
				} else if(open || shipInSystem){
					addBuildings(planet.getBuildingsByVisibility(true)); // bara buildings som syns på kartan.
				}
				
				
			}else{
				//TODO Ingen ny information från denna turn. Hämta information från tidigare turns.
				// Buildings, VIPs  mm. När det gäller VIPar så ska aldrig allierads VIPar synas här då spelaren alltid har ny info om dem.
				
				// lastInfoTurn Glöm inte att fylla i denna här. lastInfoTurn är det värdet som avgör om det är historiska värden eller live info.
				// lastKnownProd,lastKnownRes hanteras genom att använda vanliga prod/res med kombination av lastInfoTurn. Gällar alla värden.
			}
			
			if (haveSpy | shipInSystem | open | isOwner | isAllied){
				getOtherFleets(planet, player, galaxy);
			}
			
			
	//	}else{ // TODO Planet is razed = allways closed, no one that can report from it. Only if the player or Allied have ships, spys.
			
	//	}
		
		
	}
	
	private void getOtherFleets(Planet planet, Player player, Galaxy g){
        // loopa igenom alla spelare och kolla efter flottor
        for (Player tempPlayer : g.getPlayers()) {
        	if (tempPlayer != player){
        		int shipSize = g.getLargestLookAsMilitaryShipSizeOnPlanet(planet,tempPlayer);
        		boolean civilianExists = !player.getGalaxy().getLargestShipSizeOnPlanet(planet,tempPlayer,true).equals("");
        		if ((shipSize > -1) | civilianExists){
        			FleetInfo fleet = new FleetInfo(tempPlayer.getGovenorName(),shipSize,civilianExists);
        			fleets.add(fleet);
        		}
        	}
        }
        // kolla efter neutrala skepp
        int shipSize = player.getGalaxy().getLargestLookAsMilitaryShipSizeOnPlanet(planet,null);
        if (shipSize > -1){
        	FleetInfo fleet = new FleetInfo(null,shipSize,false);
    		fleets.add(fleet);
        }
        
    }
	
	//VIPS on the planet, not VIPs on troops or ships.
	private void addVIPs(Player player, Planet planet, Galaxy galaxy, boolean isOwner, boolean isAllied, boolean haveSpy, boolean survey){
		
		for (VIP aVIP : galaxy.getAllVIPs()) {
			if (aVIP.getPlanetLocation() == planet){
				if(aVIP.getBoss() == player || player.getGalaxy().getDiplomacy().checkAllianceWithAllInConfederacy(player, aVIP.getBoss())){
					vips.add(new VIPInfo(aVIP, player));
				}else if(open || haveSpy || survey){ // VIPar som  tillhör fiender. Alltså VIPar som inte finns på spelarens eller dess allierades planeter. 
					if (aVIP.getShowOnOpenPlanet()){
						vips.add(new VIPInfo(aVIP, player));
					}
				}
			}
		}
    }

	private void addBuildings(List<Building> planetBuildings) {
		for (Building building : planetBuildings) {
			buildings.add(new BuildingInfo(building));
		}
	}
	
	//The owners ships.
	private void addShips(Player player, Planet planet, Galaxy galaxy){
		List<Spaceship> spaceShips = galaxy.getPlayersSpaceshipsOnPlanet(player, planet);
		for (Spaceship spaceship : spaceShips) {
			ships.add(new ShipInfo(spaceship));
		}
		
	}

	public String getName() {
		return name;
	}

	public boolean isOpen() {
		return open;
	}

	public String isOwner() {
		return owner;
	}

	public int getBasePopulation() {
		return basePopulation;
	}
	
	class BuildingInfo{
		
		private String name, type;
		private int id;
		
		BuildingInfo(Building building){
			name = building.getBuildingType().getName();
			id = building.getUniqueId();
			name = building.getUniqueName();
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		public int getId() {
			return id;
		}
	}
	
	class VIPInfo{
		
		private String type, owner;
		private int id = -1, kills = -1;
		
		VIPInfo(VIP aVip, Player player){
			type = aVip.getTypeName();
						
			if(aVip.getBoss() != null){
				
				if(player.getName().equals(aVip.getBoss().getName())){
					owner = aVip.getBoss().getGovenorName();
					id = aVip.getId();
					kills = aVip.getKills();
				}else if(player.getGalaxy().getDiplomacy().checkAllianceWithAllInConfederacy(player, aVip.getBoss())){
					// Ägaren till VIPen är en allierad vilket betyder att spelarn får veta vem som äger VIPen.
					owner = aVip.getBoss().getGovenorName();
				}
			}
			
			
		}

		public String getType() {
			return type;
		}

		public String getOwner() {
			return owner;
		}

		public int getId() {
			return id;
		}

		public int getKills() {
			return kills;
		}
	}
	
	class ShipInfo{
		
		//TODO har inte tagit med värden som handlar om flykt. Vet inte om det behövs eller inte, framtiden får visa vägen.
		
		private String type, name;
		private int kills, currentHP, currentShield, techWhenBuilt, shields;
		private int weaponsSmall, weaponsMedium, weaponsLarge, weaponsHuge, weaponsSquadron;
		private int weaponsSalvoesMedium, weaponsSalvoesLarge, weaponsSalvoesHuge;
		private double armorSmall, armorMedium, armorLarge, armorHuge;
		private boolean retreating, screeened;
		
		ShipInfo(Spaceship aShip){
			
			type = aShip.getTypeName();
			name = aShip.getUniqueName();
			kills = aShip.getKills();
			currentHP = aShip.getCurrentDc();
			retreating = aShip.isRetreating();
			screeened = aShip.getScreened();
			currentShield = aShip.getCurrentShields();
			shields = aShip.getShields();
			techWhenBuilt = aShip.getTechWhenBuilt();
			weaponsSmall = aShip.getWeaponsStrengthSmall();
			weaponsMedium = aShip.getWeaponsStrengthMedium();
			weaponsLarge = aShip.getWeaponsStrengthLarge();
			weaponsHuge = aShip.getWeaponsStrengthHuge();
			weaponsSquadron = aShip.getWeaponsStrengthSquadron();
			weaponsSalvoesMedium = aShip.getWeaponsSalvoesMedium();
			weaponsSalvoesLarge = aShip.getWeaponsSalvoesLarge();
			weaponsSalvoesHuge = aShip.getWeaponsSalvoesHuge();
			armorSmall = aShip.getArmorSmall();
			armorMedium = aShip.getArmorMedium();
			armorLarge = aShip.getArmorLarge();
			armorHuge = aShip.getArmorHuge();
			
		}

		public String getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		public int getKills() {
			return kills;
		}

		public int getCurrentHP() {
			return currentHP;
		}

		public int getCurrentShield() {
			return currentShield;
		}

		public int getTechWhenBuilt() {
			return techWhenBuilt;
		}

		public int getShields() {
			return shields;
		}

		public int getWeaponsSmall() {
			return weaponsSmall;
		}

		public int getWeaponsMedium() {
			return weaponsMedium;
		}

		public int getWeaponsLarge() {
			return weaponsLarge;
		}

		public int getWeaponsHuge() {
			return weaponsHuge;
		}

		public int getWeaponsSquadron() {
			return weaponsSquadron;
		}

		public int getWeaponsSalvoesMedium() {
			return weaponsSalvoesMedium;
		}

		public int getWeaponsSalvoesLarge() {
			return weaponsSalvoesLarge;
		}

		public int getWeaponsSalvoesHuge() {
			return weaponsSalvoesHuge;
		}

		public double getArmorSmall() {
			return armorSmall;
		}

		public double getArmorMedium() {
			return armorMedium;
		}

		public double getArmorLarge() {
			return armorLarge;
		}

		public double getArmorHuge() {
			return armorHuge;
		}

		public boolean isRetreating() {
			return retreating;
		}

		public boolean isScreeened() {
			return screeened;
		}
	}
	
	class FleetInfo {
		
		private String owner;
		private int shipSize;
		private boolean civ;
		
		FleetInfo(String owner, int size, boolean isCivilan){
			this.owner = owner;
			this.shipSize = size;
			this.civ = isCivilan;
		}

		public String getOwner() {
			return owner;
		}

		public int getShipSize() {
			return shipSize;
		}

		public boolean isCiv() {
			return civ;
		}
	}

}
