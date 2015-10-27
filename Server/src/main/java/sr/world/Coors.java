package sr.world;

import java.awt.Color;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Coors implements Serializable{
    static final long serialVersionUID = 1L;
    double x,y,z,x2,y2,z2;
    String name;
    Color c;
    boolean besieged = false, origo = false, connection = false, longRange = false, open = false;   // shipOrFleet behövs ej?
    boolean razed = false, razedAndUninfected = false;
    List<Spaceship> ships;
    List<VIP> vips;
    List<Troop> troops;
    List<Building> buildings;
    private int production = -1,resistance = -1;
    private int lastKnownProd = -1,lastKnownRes = -1,lastKnownTroopsNr = -1;
    //private boolean spaceStation;
    private int nrTroops;
    private String lastKnownBuildingsString, lastKnownMaxShipSize;

    public int getNrTroops() {
		return nrTroops;
	}
    
    public List<Troop> getTroops(){
    	return troops;
    }

	public void setNrTroops(int nrTroops) {
		this.nrTroops = nrTroops;
	}

	public Coors(double x, double y, double z){
      ships = new LinkedList<Spaceship>();
      vips = new LinkedList<VIP>();
      troops = new LinkedList<Troop>();
      buildings = new LinkedList<Building>();
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public Coors(double x, double y, double z,double x2, double y2, double z2, boolean longRange){
      this.x = x;
      this.y = y;
      this.z = z;
      this.x2 = x2;
      this.y2 = y2;
      this.z2 = z2;
      this.longRange = longRange;
      connection = true;
    }

    public void setOrigo(boolean newvalue){
        origo = newvalue;
    }

    public boolean isOrigo(){
        return origo;
    }

    // används denna?
    public void setConnection(boolean newvalue){
        connection = newvalue;
    }

    public boolean isConnection(){
        return connection;
    }
    

    public void setProduction(int newProduction){
    	this.production = newProduction;
    }

    public void setResistance(int newResistance){
    	this.resistance = newResistance;
    }

    public int getProduction(){
    	return production;
    }

    public int getResistance(){
    	return resistance;
    }
    
    public boolean showValues(){
    	return production != -1;
    }

    public boolean showLastKnownValues(){
    	return lastKnownProd != -1;
    }

    public void setRazed(boolean newvalue){
        razed = newvalue;
    }

    public boolean isRazed(){
        return razed;
    }

    public void setRazedAndUninfected(boolean newvalue){
        razedAndUninfected = newvalue;
    }

    public boolean isRazedAndUninfected(){
        return razedAndUninfected;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getZ(){
        return z;
    }

    public void setX(double newvalue){
        x = newvalue;
    }

    public void setY(double newvalue){
        y = newvalue;
    }

    public void setZ(double newvalue){
        z = newvalue;
    }

    public double getX2(){
        return x2;
    }

    public double getY2(){
        return y2;
    }

    public double getZ2(){
        return z2;
    }

    public void setX2(double newvalue){
        x2 = newvalue;
    }

    public void setY2(double newvalue){
        y2 = newvalue;
    }

    public void setZ2(double newvalue){
        z2 = newvalue;
    }

    public void setName(String newname){
        name = newname;
    }

    public String getName(){
        return name;
    }

    public void setColor(Color newc){
        c = newc;
    }

    public Color getColor(){
        return c;
    }

    public void setBesieged(){
        besieged = true;
    }

    public boolean isBesieged(){
        return besieged;
    }

    public void addShip(Spaceship ss){
        ships.add(ss);
    }

    public void addVip(VIP v){
        vips.add(v);
    }

    public void addTroop(Troop aTroop){
        troops.add(aTroop);
    }
    
    public void addBuilding(Building aBuilding){
        buildings.add(aBuilding);
    }
/*
    public void setWharf(Vector ow){
        wharfs = ow;
    }
*/
    public void setBuilding(List<Building> inBuildings){
        buildings = inBuildings;
    }
    
    public List<Spaceship> getShortNameablesShips(){
        return ships;
    }

    public List<VIP> getShortNameablesVIPs(){
        return vips;
    }

    /**
     * Return a string with all Buildings shortName:
     * @return
     */
    public String getBuildingsString(boolean spy,boolean shipInSystem, boolean troopInSystem, Planet aPlanet, Player aPlayer){
    	String retStr = "";
    	if (buildings.size() > 0){
    		//buildings.
    	//	Vector li =  new Vector(buildings); TODO (Tobbe)  fixa sortering
    	//	Collections.sort(li);
    		for (Iterator<Building> iter = buildings.iterator(); iter.hasNext();) {
				Building building = iter.next();
				boolean alwaysShow = building.getBuildingType().isVisibleOnMap();
				boolean ownPlanet = aPlanet.getPlayerInControl() == aPlayer;
		//		Logger.finest(aPlanet + ", shipInSystem: " + shipInSystem);
				if (ownPlanet | alwaysShow | spy | shipInSystem | troopInSystem){
					retStr = retStr + building.getBuildingType().getShortName();
					if(iter.hasNext()){
						retStr = retStr + ", ";
					}
				}
			}
    	}
    	return retStr;
    }

    public void setOpen(boolean newValue){
        open = newValue;
    }

    public boolean isOpen(){
        return open;
    }
/*
	public boolean hasSpaceStation() {
		return spaceStation;
	}

	public void setSpaceStation(boolean spaceStation) {
		this.spaceStation = spaceStation;
	}
*/
	public int getLastKnownProd() {
		return lastKnownProd;
	}

	public void setLastKnownProd(int lastKnownProd) {
		this.lastKnownProd = lastKnownProd;
	}

	public int getLastKnownRes() {
		return lastKnownRes;
	}

	public void setLastKnownRes(int lastKnownRes) {
		this.lastKnownRes = lastKnownRes;
	}

	@Override
	public String toString(){
		return "Coor: " + name;
	}

	public String getLastKnownBuildingsString() {
		return lastKnownBuildingsString;
	}

	public void setLastKnownBuildingsString(String lastKnownBuildingsString) {
		this.lastKnownBuildingsString = lastKnownBuildingsString;
	}

	public String getLastKnownMaxShipSize() {
		return lastKnownMaxShipSize;
	}

	public void setLastKnownMaxShipSize(String lastKnownMaxShipSize) {
		this.lastKnownMaxShipSize = lastKnownMaxShipSize;
	}

	public int getLastKnownTroopsNr() {
		return lastKnownTroopsNr;
	}

	public void setLastKnownTroopsNr(int lastKnownTroopsNr) {
		this.lastKnownTroopsNr = lastKnownTroopsNr;
	}
}