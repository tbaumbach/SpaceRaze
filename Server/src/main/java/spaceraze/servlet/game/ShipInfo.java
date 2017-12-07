package spaceraze.servlet.game;

import java.util.ArrayList;
import java.util.List;

import sr.world.Spaceship;

public class ShipInfo {

	// TODO har inte tagit med värden som handlar om flykt. Vet inte om det
	// behövs eller inte, framtiden får visa vägen.

	private String type, name, shortName, carrierLocation;
	private int kills, currentHP, currentShield, techWhenBuilt, shields, size, id;
	private int weaponsSmall, weaponsMedium, weaponsLarge, weaponsHuge, weaponsSquadron;
	private int weaponsSalvoesMedium, weaponsSalvoesLarge, weaponsSalvoesHuge;
	private double armorSmall, armorMedium, armorLarge, armorHuge;
	private boolean retreating, screened;
	private List<ShipInfo> squdrons;
	private List<VIPInfo> vips;
	private List<TroopInfo> troops;

	ShipInfo(Spaceship aShip) {

		id = aShip.getId();
		type = aShip.getTypeName();
		name = aShip.getUniqueName();
		shortName = aShip.getShortName();
		size = aShip.getSize();
		kills = aShip.getKills();
		currentHP = aShip.getCurrentDc();
		retreating = aShip.isRetreating();
		screened = aShip.getScreened();
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
		squdrons = new ArrayList<ShipInfo>();
		vips =  new ArrayList<VIPInfo>();
		troops = new ArrayList<TroopInfo>();
		
	}
	
	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public int getSize() {
		return size;
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

	public boolean isScreened() {
		return screened;
	}
	
	public String getCarrierLocation(){
		return carrierLocation;
	}
	
	public List<ShipInfo> getSqudrons(){
		return squdrons;
	}
	
	public List<VIPInfo> getVIPs(){
		return vips;
	}
	
	public List<TroopInfo> getTroops(){
		return troops;
	}
	
	public void addSqudron(ShipInfo aShip){
		squdrons.add(aShip);
	}

	public void addVIP(VIPInfo aVIP) {
		vips.add(aVIP);
	}
	
	public void addTroop(TroopInfo aTroop){
		troops.add(aTroop);
	}
}
