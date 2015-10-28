/*
 * Created on 2005-sep-27
 */
package sr.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sr.enums.InitiativeMethod;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.comparator.AlignmentNameComparator;
import sr.world.comparator.BuildingTypeNameComparator;
import sr.world.comparator.VIPTypeComparator;
import sr.world.comparator.spaceshiptype.SpaceshipTypeComparator;
import sr.world.diplomacy.GameWorldDiplomacy;

/**
 * @author wmpabod
 *
 * Contains all data for one distinct gameworld primarily used in individual games.
 */
public class GameWorld implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<SpaceshipType> shipTypes;
	private List<VIPType> vipTypes;
	private List<Faction> factions;
	private List<TroopType> troopTypes;
	private SpaceshipType neutralSize1,neutralSize2,neutralSize3;
	private TroopType neutralTroopType;
	private String description,history,shortDescription,howToPlay;
	private String fileName,fullName,createdByUser,createdDate,changedDate;
	private String battleSimDefaultShips1,battleSimDefaultShips2;
	private final boolean cumulativeBombardment = false; // cannot be changed anymore...
	private final boolean squadronsSurviveOutsideCarriers = false; // On Non-Friendly Planets, cannot be changed anymore...
	private InitiativeMethod initMethod = InitiativeMethod.WEIGHTED_1;
	private int closedNeutralPlanetChance = 60; // %
	private int razedPlanetChance = 0; // %
	private Alignments alignments = new Alignments(true);
	private String versionId = "1"; // should be increased every time a new version of a GameWorld is published
	private GameWorldDiplomacy diplomacy;
	private int baseBombardmentDamage = 1000; // default value (always kills the troop) 50% hit chance.
	private Buildings buildings; // used to list all buildings and to store neutral buildings.
	private boolean adjustScreenedStatus = true;
	
	public GameWorld(){
		shipTypes = new ArrayList<SpaceshipType>();
		factions = new ArrayList<Faction>();
		vipTypes = new ArrayList<VIPType>();
		troopTypes = new ArrayList<TroopType>();
		diplomacy = new GameWorldDiplomacy();
	}
	
	/**
	 * Always add a clone...
	 * @param sst
	 */
	public void addShipType(SpaceshipType sst){
		shipTypes.add(new SpaceshipType(sst));
	}
	
	public void addFaction(Faction f){
		factions.add(f);
		createDiplomacyRelations(f);
	}
	
	private void createDiplomacyRelations(Faction aFaction){
		for (Faction tmpFaction : factions) {
			diplomacy.addDefaultRelation(aFaction, tmpFaction);
		}
	}

	public void addVipType(VIPType vt){
		vipTypes.add(vt);
	}

	public void addTroopType(TroopType tt){
		troopTypes.add(tt.clone());
	}
	
	public List<TroopType> getTroopTypes(){
		return troopTypes;
	}
	
	public boolean isTroopGameWorld(){
		return (troopTypes.size() > 0);
	}

    public SpaceshipType getSpaceshipTypeByName(String sstname){
    	SpaceshipType foundsst = null;
    	int i = 0;
    	while ((i < shipTypes.size()) & (foundsst == null)){
    		SpaceshipType tempsst = (SpaceshipType)shipTypes.get(i);
    		if (tempsst.getName().equalsIgnoreCase(sstname)){
    			foundsst = tempsst;
    		}else{
    			i++;
    		}
    	}
    	if (foundsst != null){
        	Logger.finest("GameWorld.getSpaceshipTypeByName, sstname:" + sstname + " -> " + foundsst);
    	}else{ // om detta inträffar så finns det antagligen en felstavning av en skeppstyp i gameworlden
        	Logger.severe("GameWorld(" + fullName + ").getSpaceshipTypeByName, sstname:" + sstname + " -> " + foundsst);
    		Thread.dumpStack();
    	}
    	return foundsst;
    }

    public BuildingType getBuildingTypeByName(String btname){
    	BuildingType foundbt = buildings.getBuildingType(btname);
    	if (foundbt != null){
        	Logger.finest("GameWorld.getBuildingTypeByName, btname:" + btname + " -> " + foundbt);
    	}else{ // om detta inträffar så finns det antagligen en felstavning av en skeppstyp i gameworlden
        	Logger.severe("GameWorld(" + fullName + ").getBuildingTypeByName, btname:" + btname + " -> " + foundbt);
    		Thread.dumpStack();
    	}
    	return foundbt;
    }

    public TroopType getTroopTypeByName(String ttname){
    	return getTroopTypeByName(ttname,true);
    }

    public TroopType getTroopTypeByName(String ttname, boolean errorDump){
    	TroopType foundtt = null;
    	int i = 0;
    	while ((i < troopTypes.size()) & (foundtt == null)){
    		TroopType temptt = troopTypes.get(i);
    		if (temptt.getUniqueName().equalsIgnoreCase(ttname)){
    			foundtt = temptt;
    		}else{
    			i++;
    		}
    	}
    	if (foundtt != null){
    		Logger.finest("GameWorld.getTroopTypeByName, ttname:" + ttname + " -> " + foundtt);
    	}else{ // om detta inträffar så finns det antagligen en felstavning av en trupptyp i gameworlden
    		if (errorDump){
    			Logger.severe("GameWorld(" + fullName + ").getTroopTypeByName, ttname:" + ttname + " -> " + foundtt);
    			Thread.dumpStack();
    		}
    	}
    	return foundtt;
    }

    public VIPType getVIPTypeByName(String vtname){
    	VIPType foundvt = null;
    	int i = 0;
    	while ((i < vipTypes.size()) & (foundvt == null)){
    		VIPType tempvt = (VIPType)vipTypes.get(i);
    		if (tempvt.getName().equalsIgnoreCase(vtname)){
    			foundvt = tempvt;
    		}else{
    			i++;
    		}
    	}
    	if (foundvt != null){
    	   	Logger.finest("GameWorld.getVIPTypeByName, ttname:" + vtname + " -> " + foundvt);
    	}else{ // om detta inträffar så finns det antagligen en felstavning av en viptyp i gameworlden
    	   	Logger.severe("GameWorld(" + fullName + ").getVIPTypeByName, ttname:" + vtname + " -> " + foundvt);
    		Thread.dumpStack();
    	}
    	return foundvt;
    }

    public TroopType getTroopTypeByShortName(String ttshortname){
    	TroopType foundtt = null;
    	int i = 0;
    	while ((i < troopTypes.size()) & (foundtt == null)){
    		TroopType temptt = troopTypes.get(i);
    		if (temptt.getUniqueShortName().equalsIgnoreCase(ttshortname)){
    			foundtt = temptt;
    		}else{
    			i++;
    		}
    	}
    	Logger.finer("GameWorld.getTroopTypeShortByName, ttname:" + ttshortname + " -> " + foundtt);
    	return foundtt;
    }

    public SpaceshipType getSpaceshipTypeCopyByName(String sstname){
    	return getSpaceshipTypeByName(sstname).copy();
    }
    
	public String getChangedDate() {
		return changedDate;
	}
	
	public void setChangedDate(String changedDate) {
		this.changedDate = changedDate;
	}
	
	public String getCreatedByUser() {
		return createdByUser;
	}
	
	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	public boolean isCumulativeBombardment() {
		return cumulativeBombardment;
	}
/*	Bombardment will always be non-cumulative
	public void setCumulativeBombardment(boolean cumulativeBombardment) {
		this.cumulativeBombardment = cumulativeBombardment;
	}
*/	
	
	public String getTotalDescription(){
		String totalDescription = "";
    	
    	if(shortDescription != null && !shortDescription.equals("")){
    		totalDescription += "Short Description\n";
        	totalDescription += shortDescription + "\n\n";
    	}
    	if(description != null && !description.equals("")){
    		totalDescription +="Description\n";
        	totalDescription += description + "\n\n";
    	}
    	if(howToPlay != null && !howToPlay.equals("")){
    		totalDescription +="Description\n";
        	totalDescription += howToPlay + "\n\n";
    	}
    	if(history != null && !history.equals("")){
    		totalDescription +="History\n";
        	totalDescription += history + "\n\n";
    	}
    	totalDescription +="Created by: " + createdByUser+ "\n";
    	totalDescription +="Created date: " + createdDate + "\n";
    	totalDescription +="Changed date: " + changedDate + "\n";
    	totalDescription +="Version: " + versionId + "\n";
    	
    	return totalDescription;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	public String getHistory() {
		return history;
	}
	
	public void setHistory(String history) {
		this.history = history;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public List<Faction> getFactions() {
		return factions;
	}
	
	public List<SpaceshipType> getShipTypes() {
		return shipTypes;
	}
	
	public List<VIPType> getVipTypes() {
		return vipTypes;
	}

	public InitiativeMethod getInitMethod() {
		return initMethod;
	}
/* cannot be changed any more...
	public void setInitMethod(InitiativeMethod initMethod) {
		this.initMethod = initMethod;
	}
*/
	public SpaceshipType getNeutralSize1() {
		return neutralSize1;
	}

	public void setNeutralSize1(SpaceshipType neutralSize1) {
		this.neutralSize1 = neutralSize1;
	}

	public SpaceshipType getNeutralSize2() {
		return neutralSize2;
	}

	public void setNeutralSize2(SpaceshipType neutralSize2) {
		this.neutralSize2 = neutralSize2;
	}

	public SpaceshipType getNeutralSize3() {
		return neutralSize3;
	}

	public void setNeutralSize3(SpaceshipType neutralSize3) {
		this.neutralSize3 = neutralSize3;
	}

	public String getBattleSimDefaultShips1() {
		return battleSimDefaultShips1;
	}

	public void setBattleSimDefaultShips1(String battleSimDefaultShips1) {
		this.battleSimDefaultShips1 = battleSimDefaultShips1;
	}

	public String getBattleSimDefaultShips2() {
		return battleSimDefaultShips2;
	}

	public void setBattleSimDefaultShips2(String battleSimDefaultShips2) {
		this.battleSimDefaultShips2 = battleSimDefaultShips2;
	}

	/*
	 * Only used by the battlesim page? Civilian ships are filtered out!
	 */
	@JsonIgnore
	public String getSpaceshipTypeOptionsHTML(){
		StringBuffer retHTML = new StringBuffer();
		List<SpaceshipType> militaryShipTypes = removeCivilianShips(shipTypes);
		Collections.sort(militaryShipTypes,new SpaceshipTypeComparator());
		for (SpaceshipType tmpsst : militaryShipTypes) {
			retHTML.append("<option value=\"");
			retHTML.append(tmpsst.getShortName());
			retHTML.append("\">");
			retHTML.append(tmpsst.getName());
			retHTML.append("</option>\n");
		}
		return retHTML.toString();
	}
	
	private List<SpaceshipType> removeCivilianShips(List<SpaceshipType> allShipTypes){
		List<SpaceshipType> newList = new LinkedList<SpaceshipType>();
		for (SpaceshipType shipType : allShipTypes) {
			if (!shipType.isCivilian()){
				newList.add(shipType);
			}
		}
		return newList;
	}

	public List<VIPType> getBattleVIPtypes(){
		Logger.finer("getBattleVIPtypes()");
		List<VIPType> battleVips = new ArrayList<VIPType>();
		for (Object o : vipTypes) {
			VIPType aVIPtype = (VIPType)o;
//			LoggingHandler.finest("a vip: " + aVIPtype.getName());
			if (aVIPtype.isBattleVip()){
//				LoggingHandler.finest("  adding: " + aVIPtype.getName());
				battleVips.add(aVIPtype);
			}
		}
		return battleVips;
	}

	public List<VIPType> getLandBattleVIPtypes(){
		Logger.finer("getLandBattleVIPtypes()");
		List<VIPType> battleVips = new ArrayList<VIPType>();
		for (Object o : vipTypes) {
			VIPType aVIPtype = (VIPType)o;
			if (aVIPtype.isLandBattleVip()){
				battleVips.add(aVIPtype);
			}
		}
		return battleVips;
	}

	public boolean hasSquadrons(){
		boolean hasSquadrons = false;
		int i = 0;
		while ((i < shipTypes.size()) && (!hasSquadrons)){
			SpaceshipType sst = (SpaceshipType)shipTypes.get(i);
			if (sst.isSquadron()){
				hasSquadrons = true;
			}
			i++;
		}
		return hasSquadrons;
	}
	
	public boolean hasTroops(){
		return troopTypes.size() > 0;
	}

	@JsonIgnore
	public String getFactionsTableContentHTML(){
		StringBuffer retHTML = new StringBuffer();
		retHTML.append(Faction.getHTMLHeaderRow());
		for (Faction tmpf : factions) {
			retHTML.append(tmpf.getHTMLTableRow(fileName));
		}
		return retHTML.toString();
	}
	
	@JsonIgnore
	public String getFactionsTableContentHTMLNO(){
		StringBuffer retHTML = new StringBuffer();
		retHTML.append(Faction.getHTMLHeaderRowNO());
		int i = 0;
		for (Faction tmpf : factions) {
			i = i + 1;
			String RowName = i + "GameWorldListRow";
			retHTML.append(tmpf.getHTMLTableRowNO(fileName,RowName,i));
		}
		return retHTML.toString();
	}
	
	@JsonIgnore
	public String getFactionsCheckboxesHTML(){
		StringBuffer retHTML = new StringBuffer();
		for (Faction tmpf : factions) {
			retHTML.append(tmpf.getHTMLCheckbox());
		}
		return retHTML.toString();
	}

	public List<String> getFactionsNames(){
		List<String> factionNames = new LinkedList<String>();
		for (Faction aFaction : factions) {
			factionNames.add(aFaction.getName());
		}
		return factionNames;
	}

	@JsonIgnore
	public static String getHTMLHeaderRow(){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		sb.append("<td>Name</td>");
		sb.append("<td>Filename</td>");
		sb.append("<td>#Factions</td>");
		sb.append("<td>#Shiptypes</td>");
		sb.append("<td>#VipTypes</td>");
		sb.append("<td>Created By</td>");
		sb.append("<td>Created Date</td>");
		sb.append("<td>Last Changed</td>");
		sb.append("</tr>\n");
		return sb.toString();
	}
	  
	@JsonIgnore
	public String getHTMLTableRow(){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		sb.append("<td><a href=\"gameworld.jsp?gameworldfilename=" + fileName + "\">" + fullName + "</a></td>");
		sb.append("<td>" + fileName + "</td>");
		sb.append("<td>" + factions.size() + "</td>");
		sb.append("<td>" + shipTypes.size() + "</td>");
		sb.append("<td>" + vipTypes.size() + "</td>");
		sb.append("<td>" + createdByUser + "</td>");
		sb.append("<td>" + createdDate + "</td>");
		sb.append("<td>" + changedDate + "</td>");
		sb.append("</tr>\n");
		return sb.toString();
	}

	@JsonIgnore
	public static String getHTMLHeaderRowNO(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("<tr class='ListheaderRow'>");
		sb.append("<td class='ListHeader'></td>");
		sb.append("<td class='ListHeader'><div class='SolidText' style='padding-left:5px;'>Name</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>Filename</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>#Factions</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>#Ship</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>#Vip</div></td>");
		//sb.append("<td class='ListHeader'><div class='SolidText'>Todo</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>Created By</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>Created Date</div></td>");
		sb.append("<td class='ListHeader'><div class='SolidText'>Last Changed</div></td>");
		sb.append("<td class='ListHeader'></td>");
		sb.append("</tr>\n");
		
			return sb.toString();
	}
	  
	@JsonIgnore			
	public String getHTMLTableRowNO_BattleSim(int i, String RowName){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr class='ListTextRow' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',10,1);\" onMouseOut=\"TranparentRow('" + RowName + "',10,0);\">");
		sb.append("<td id='" + RowName + "1' width='3' class='ListText'></td>");
		sb.append("<td id='" + RowName + "2' class='ListText'><div class='SolidText' style='padding-left:5px;'><a href=\"Master.jsp?action=battle_sim&gameworld=" + fileName + "\">" + fullName + "</a></div></td>");
		sb.append("<td id='" + RowName + "3' class='ListText'><div class='SolidText'>" + fileName + "</div></td>");
		sb.append("<td id='" + RowName + "4' class='ListText'><div class='SolidText'>" + factions.size() + "</div></td>");
		sb.append("<td id='" + RowName + "5' class='ListText'><div class='SolidText'>" + shipTypes.size() + "</div></td>");
		sb.append("<td id='" + RowName + "6' class='ListText'><div class='SolidText'>" + vipTypes.size() + "</div></td>");
		//sb.append("<td id='" + RowName + "7' class='ListText'><div class='SolidText'> Todo </div></td>");
		sb.append("<td id='" + RowName + "7' class='ListText'><div class='SolidText'>" + createdByUser + "</div></td>");
		sb.append("<td id='" + RowName + "8' class='ListText'><div class='SolidText'>" + createdDate + "</div></td>");
		sb.append("<td id='" + RowName + "9' class='ListText'><div class='SolidText'>" + changedDate + "</div></td>");
		sb.append("<td id='" + RowName + "10' width='3' class='ListText'></td>");
		sb.append("</tr>\n");
		return sb.toString();
	}
	
	@JsonIgnore
	public String getHTMLTableRowNO(int i, String RowName){
		StringBuffer sb = new StringBuffer();
		sb.append("<tr class='ListTextRow' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',10,1);\" onMouseOut=\"TranparentRow('" + RowName + "',10,0);\">");
		sb.append("<td id='" + RowName + "1' width='3' class='ListText'></td>");
		sb.append("<td id='" + RowName + "2' class='ListText'><div class='SolidText' style='padding-left:5px;'><a href=\"Master.jsp?action=gameworld&gameworldfilename=" + fileName + "\">" + fullName + "</a></div></td>");
		sb.append("<td id='" + RowName + "3' class='ListText'><div class='SolidText'>" + fileName + "</div></td>");
		sb.append("<td id='" + RowName + "4' class='ListText'><div class='SolidText'>" + factions.size() + "</div></td>");
		sb.append("<td id='" + RowName + "5' class='ListText'><div class='SolidText'>" + shipTypes.size() + "</div></td>");
		sb.append("<td id='" + RowName + "6' class='ListText'><div class='SolidText'>" + vipTypes.size() + "</div></td>");
		//sb.append("<td id='" + RowName + "7' class='ListText'><div class='SolidText'> Todo </div></td>");
		sb.append("<td id='" + RowName + "7' class='ListText'><div class='SolidText'>" + createdByUser + "</div></td>");
		sb.append("<td id='" + RowName + "8' class='ListText'><div class='SolidText'>" + createdDate + "</div></td>");
		sb.append("<td id='" + RowName + "9' class='ListText'><div class='SolidText'>" + changedDate + "</div></td>");
		sb.append("<td id='" + RowName + "10' width='3' class='ListText'></td>");
		sb.append("</tr>\n");
		return sb.toString();
	}
	
	public Faction findFaction(String aFactionName){
		Faction found = null;
		for (Faction tmpf : factions) {
			if (tmpf.getName().equalsIgnoreCase(aFactionName)){
				found = tmpf;
			}
		}
		assert found != null : "Faction with name " + aFactionName + " does not exist";
		return found;
	}
	/*
	@JsonIgnore
	public String getSpaceshipTypesTableContentHTML(){
		StringBuffer retHTML = new StringBuffer();
		retHTML.append(SpaceshipType.getHTMLHeaderRow());
		Collections.sort(shipTypes,new SpaceshipTypeComparator());
		for (SpaceshipType sst : shipTypes) {
			retHTML.append(sst.getHTMLTableRow());
		}
		return retHTML.toString();
	}

	@JsonIgnore
	public String getSpaceshipTypesTableContentHTMLNO(){
		StringBuffer retHTML = new StringBuffer();
		retHTML.append(SpaceshipType.getHTMLHeaderRowNO());
		Collections.sort(shipTypes,new SpaceshipTypeComparator());
		for (SpaceshipType sst : shipTypes) {
			retHTML.append(sst.getHTMLTableRowNO());
		}
		return retHTML.toString();
	}

	@JsonIgnore
	public String getVIPTypesTableContentHTML(){
		StringBuffer retHTML = new StringBuffer();
		for (VIPType vt : vipTypes) {
			retHTML.append(vt.getHTMLTableContent());
			retHTML.append("<p><p>");
		}
		return retHTML.toString();
	}

	@JsonIgnore
	public String getVIPTypesTableContentHTMLNO(){
		StringBuffer retHTML = new StringBuffer();
		for (VIPType vt : vipTypes) {
			retHTML.append(vt.getHTMLTableContentNO());
		}
		return retHTML.toString();
	}
	
	@JsonIgnore
	public String getBuildingsTypesTableContentHTMLNO(){
		StringBuffer retHTML = new StringBuffer();
		retHTML.append("");
		Logger.finer("buildings.getBuildingsVectorOrderByParent().size()");
		
		//for (Iterator iter = buildings.getBuildingsVectorOrderByParent().iterator(); iter.hasNext();) {
		//	BuildingType bt = (BuildingType) iter.next();
		//	retHTML.append(bt.getHTMLTableContentNO());
		//}
		return retHTML.toString();
	}
	
	
	@JsonIgnore
	public String getAlignmentsTableContentHTML(){
		StringBuffer retHTML = new StringBuffer();
		List<Alignment> allAlignments = Functions.cloneList(alignments.getAllAlignments());
		Collections.sort(allAlignments,new AlignmentNameComparator<Alignment>());
		for (Alignment alignment : allAlignments) {
			retHTML.append(alignment.getHTMLTableContent());
			retHTML.append("<p><p>");
		}
		return retHTML.toString();
	}

	*/
	public boolean isSquadronsSurviveOutsideCarriers() {
		return squadronsSurviveOutsideCarriers;
	}
/*
	public void setSquadronsSurviveOutsideCarriers(boolean squadronsSurviveOutsideCarriers) {
		this.squadronsSurviveOutsideCarriers = squadronsSurviveOutsideCarriers;
	}
*/
	public int getClosedNeutralPlanetChance() {
		return closedNeutralPlanetChance;
	}

	public void setClosedNeutralPlanetChance(int closedNeutralPlanetChance) {
		this.closedNeutralPlanetChance = closedNeutralPlanetChance;
	}

	public int getRazedPlanetChance() {
		return razedPlanetChance;
	}

	public void setRazedPlanetChance(int razedPlanetChance) {
		this.razedPlanetChance = razedPlanetChance;
	}

	public Alignments getAlignments() {
		return alignments;
	}

	public void setAlignments(Alignments alignments) {
		this.alignments = alignments;
	}
	
	public String toString(){
		return "gameWorld: " + fullName + " (" + fileName + ")";
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}


	public TroopType getNeutralTroopType() {
		return neutralTroopType;
	}

	public void setNeutralTroopType(TroopType neutralTroopType) {
		this.neutralTroopType = neutralTroopType;
	}

	public GameWorldDiplomacy getDiplomacy() {
		return diplomacy;
	}

	public int getBaseBombardmentDamage() {
		return baseBombardmentDamage;
	}

	public void setBaseBombardmentDamage(int baseBombardmentDamage) {
		this.baseBombardmentDamage = baseBombardmentDamage;
	}
	
	public Buildings getBuildings() {
		return buildings;
	}

	public void setBuildings(Buildings buildings) {
		this.buildings = buildings;
	}

	public String getHowToPlay() {
		return howToPlay;
	}

	public void setHowToPlay(String howtoPlay) {
		this.howToPlay = howtoPlay;
	}

	public boolean isResearchWorld(){
		for (Faction faction : factions) {
			if(faction.getResearch().getAdvantages().size() != 0){
				return true;
			}
		}
		return false;
	}
	
	  public boolean isAdjustScreenedStatus() {
		return adjustScreenedStatus;
	}

	public void setAdjustScreenedStatus(boolean adjustScreenedStatus) {
		this.adjustScreenedStatus = adjustScreenedStatus;
	}
/*
	public String getAllFactionsDroid(){
		  StringBuffer sb = new StringBuffer();
		  for (Faction aFaction : factions) {
			  sb.append(aFaction.getFactionLinkDroid());
			  sb.append("&nbsp;<br>");
		  }
		  return sb.toString();
	  }

	  public String getReconstructCostsDroid(){
		  StringBuffer sb = new StringBuffer();
		  for (Faction aFaction : factions) {
			  if (!aFaction.isAlien()){
				  sb.append(aFaction.getName());
				  sb.append(": ");
				  sb.append(aFaction.getReconstructCostBase());
				  sb.append("&nbsp;<br>");
			  }
		  }
		  return sb.toString();
	  }

	  public String getAllShiptypesDroid(){
		  StringBuffer sb = new StringBuffer();
		  for (SpaceshipType aSpaceshipType : shipTypes) {
			  sb.append(aSpaceshipType.getName());
			  sb.append("&nbsp;<br>");
		  }
		  return sb.toString();
	  }

	  public String getAllVIPTypesDroid(){
		  StringBuffer sb = new StringBuffer();
		  List<VIPType> aVIPTypeList = new ArrayList<VIPType>(vipTypes);
		  Collections.sort(aVIPTypeList, new VIPTypeComparator());
		  for (VIPType aVIPType : aVIPTypeList) {
			  sb.append(aVIPType.getName());
			  sb.append("&nbsp;<br>");
		  }
		  return sb.toString();
	  }

	  public String getVIPTypesDroid(String alignmentName){
		  StringBuffer sb = new StringBuffer();
		  for (VIPType aVIPType : vipTypes) {
			  if (aVIPType.getAlignment().getName().equalsIgnoreCase(alignmentName)){
				  sb.append(aVIPType.getName());
				  sb.append("&nbsp;<br>");
			  }
		  }
		  return sb.toString();
	  }

	  public String getFactionsDroid(String alignmentName){
		  StringBuffer sb = new StringBuffer();
		  for (Faction aFaction : factions) {
			  if (aFaction.getAlignment().getName().equalsIgnoreCase(alignmentName)){
				  sb.append(aFaction.getName());
				  sb.append("&nbsp;<br>");
			  }
		  }
		  return sb.toString();
	  }

	  public String getAllBuildingTypesDroid(){
		  StringBuffer sb = new StringBuffer();
		  List<BuildingType> buildingsListCopy = new ArrayList<BuildingType>(buildings.getBuildings());
		  Collections.sort(buildingsListCopy,new BuildingTypeNameComparator());
		  for (BuildingType aBuildingType : buildingsListCopy) {
			  sb.append(aBuildingType.getName());
			  sb.append("&nbsp;<br>");
		  }
		  return sb.toString();
	  }

	  public String getAllAlignmentsDroid(){
		  StringBuffer sb = new StringBuffer();
		  for (Alignment aAlignment : alignments.getAllAlignments()) {
			  sb.append(aAlignment.getName());
			  sb.append(" alignment&nbsp;<br>");
		  }
		  return sb.toString();
	  }

	  public String getFactionInfoDroid(String factionName){
		  Faction foundFaction = findFaction(factionName);
		  return foundFaction.getFactionInfoDroid();
	  }

	  public String getShiptypeInfoDroid(String shiptypeName){
		  SpaceshipType foundShiptype = getSpaceshipTypeByName(shiptypeName);
		  return foundShiptype.getShiptypeInfoDroid();
	  }

	  public String getAlignmentInfoDroid(String alignmentName){
		  Alignment foundAlignment = getAlignments().findAlignment(alignmentName);
		  return foundAlignment.getAlignmentInfoDroid();
	  }

	  public String getBuildingInfoDroid(String buildingName){
		  BuildingType foundBuilding = getBuildingTypeByName(buildingName);
		  return foundBuilding.getBuildingInfoDroid(buildings);
	  }

	  public String getVipInfoDroid(String vipName){
		  VIPType foundVip = getVIPTypeByName(vipName);
		  return foundVip.getVipInfoDroid();
	  }

	  public String getVipsWithAbilityDroid(int abilityNr){
		  List<String> vipList = new ArrayList<String>();
		  StringBuffer sbTitle = new StringBuffer();
		  sbTitle.append("All VIPs with this ability");
		  switch (abilityNr){
		  	case 6: case 7: case 8:
		  	case 9: case 10: case 11:
		  	case 12: case 13: case 14:
		  	case 24: case 26:
		  		sbTitle.append(" (bonus)");
			  break;
		  	case 2: case 3: case 4: case 17:
		  		sbTitle.append(" (level)");
		  }
		  sbTitle.append(":<br>");
		  for (VIPType aVIPType : vipTypes) {
			  StringBuffer sb = new StringBuffer();
			  if (aVIPType.getHasAbilityDroid(abilityNr)){
				  sb.append(aVIPType.getName());
				  if (abilityNr == 2){
					  sb.append(" (");
					  sb.append(aVIPType.getAssassination());
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 3){
					  sb.append(" (");
					  sb.append(aVIPType.getCounterEspionage());
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 4){
					  sb.append(" (");
					  sb.append(aVIPType.getExterminator());
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 6){
					  sb.append(" (");
					  sb.append(aVIPType.getInitBonus());
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 7){
					  sb.append(" (");
					  sb.append(aVIPType.getInitFighterSquadronBonus());
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 8){
					  sb.append(" (");
					  sb.append(aVIPType.getInitDefence());
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 9){
					  sb.append(" (");
					  sb.append(aVIPType.getPsychWarfareBonus());
					  sb.append(")");
				  }else	    
				  if (abilityNr == 10){
					  sb.append(" (");
					  sb.append(aVIPType.getShipBuildBonus());
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 11){
					  sb.append(" (");
					  sb.append(aVIPType.getBuildingBuildBonus());
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 12){
					  sb.append(" (");
					  sb.append(aVIPType.getTechBonus());
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 13){
					  sb.append(" (");
					  sb.append(aVIPType.getOpenIncBonus());
					  sb.append(")");
				  }else
				  if (abilityNr == 14){
					  sb.append(" (");
					  sb.append(aVIPType.getClosedIncBonus());
					  sb.append(")");
				  }else
				  if (abilityNr == 17){
					  sb.append(" (");
					  sb.append(aVIPType.getDuellistSkillString());
					  sb.append(")");
				  }else
				  if (abilityNr == 24){
					  sb.append(" (");
					  sb.append(aVIPType.getResistanceBonus());
					  sb.append(")");
				  }else
				  if (abilityNr == 26){
					  sb.append(" (");
					  sb.append(aVIPType.getBombardmentBonus());
					  sb.append(")");
				  }
				  sb.append("&nbsp;<br>");
				  vipList.add(sb.toString());
			  }
		  }
		  Collections.sort(vipList);
		  StringBuffer sbAll = new StringBuffer();
		  sbAll.append(sbTitle.toString());
		  for(String aShipString : vipList){
			  sbAll.append(aShipString);
		  }
		  return sbAll.toString();
	  }

	  public String getShipsWithAbilityDroid(int abilityNr){
		  List<String> shipList = new ArrayList<String>();
		  for (SpaceshipType aShipType : shipTypes) {
			  if (aShipType.getHasAbilityDroid(abilityNr)){
				  StringBuffer sb = new StringBuffer();
				  sb.append(aShipType.getName());
				  if (abilityNr == 6){
					  sb.append(" (");
					  sb.append(Integer.toString(aShipType.getSquadronCapacity()));
					  sb.append(")");
				  }else
				  if (abilityNr == 10){
					  sb.append(" (");
					  sb.append(Integer.toString(aShipType.getBombardment()));
					  sb.append(")");
				  }else
				  if (abilityNr == 11){
					  sb.append(" (");
					  sb.append(Integer.toString(aShipType.getInitSupportBonus()));
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 12){
					  sb.append(" (");
					  sb.append(Integer.toString(aShipType.getInitiativeBonus()));
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 13){
					  sb.append(" (");
					  sb.append(Integer.toString(aShipType.getInitDefence()));
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 14){
					  sb.append(" (");
					  sb.append(Integer.toString(aShipType.getPsychWarfare()));
					  sb.append(")");
				  }
				  sb.append("&nbsp;<br>");
				  shipList.add(sb.toString());
			  }
		  }
		  Collections.sort(shipList);
		  StringBuffer sbAll = new StringBuffer();
		  for(String aShipString : shipList){
			  sbAll.append(aShipString);
		  }
		  return sbAll.toString();
	  }

	  public String getBuildingsWithAbilityDroid(int abilityNr){
		  List<String> buildingList = new ArrayList<String>();
		  for (BuildingType aBuildingType : buildings.getBuildings()) {
			  if (aBuildingType.getHasAbilityDroid(abilityNr)){
				  StringBuffer sb = new StringBuffer();
				  sb.append(aBuildingType.getName());
				  if (abilityNr == 2){
					  sb.append(" (");
					  sb.append(aBuildingType.getOpenPlanetBonus());
					  sb.append(")");
				  }else
				  if (abilityNr == 3){
					  sb.append(" (");
					  sb.append(aBuildingType.getClosedPlanetBonus());
					  sb.append(")");
				  }else
				  if (abilityNr == 4){
					  sb.append(" (");
					  sb.append(aBuildingType.getTechBonus());
					  sb.append("%");
					  sb.append(")");
				  }else
				  if (abilityNr == 5){
					  sb.append(" (");
					  sb.append(aBuildingType.getResistanceBonus());
					  sb.append(")");
				  }else
				  if (abilityNr == 6){
					  sb.append(" (");
					  sb.append(aBuildingType.getShieldCapacity());
					  sb.append(")");
				  }else
				  if (abilityNr == 8){
					  sb.append(" (");
					  sb.append(aBuildingType.getWharfSize());
					  sb.append(")");
				  }
				  sb.append("&nbsp;<br>");
				  buildingList.add(sb.toString());
			  }
		  }
		  Collections.sort(buildingList);
		  StringBuffer sbAll = new StringBuffer();
		  for(String aShipString : buildingList){
			  sbAll.append(aShipString);
		  }
		  return sbAll.toString();
	  }

	  public String getFactionsWithAbilityDroid(int abilityNr){
		  List<String> factionList = new ArrayList<String>();
//		  sb.append("All factions with this ability:<br>");
		  for (Faction aFaction : factions) {
			  if (aFaction.getHasAbilityDroid(abilityNr)){
				  StringBuffer sb = new StringBuffer();
				  sb.append(aFaction.getName());
				  if (abilityNr == 2){
					  sb.append(" (");
					  sb.append(aFaction.getResistanceBonus());
					  sb.append(")");
				  }else
				  if (abilityNr == 3){
					  sb.append(" (");
					  sb.append(aFaction.getOpenPlanetBonus());
					  sb.append(")");
				  }else
				  if (abilityNr == 4){
					  sb.append(" (");
					  sb.append(aFaction.getClosedPlanetBonus());
					  sb.append(")");
				  }else
				  if (abilityNr == 5){
					  sb.append(" (");
					  sb.append(aFaction.getTechBonus());
					  sb.append("%");
					  sb.append(")");
				  }
				  sb.append("&nbsp;<br>");
				  factionList.add(sb.toString());
			  }
		  }
		  Collections.sort(factionList);
		  StringBuffer sbAll = new StringBuffer();
		  for(String aShipString : factionList){
			  sbAll.append(aShipString);
		  }
		  return sbAll.toString();
	  }
*/
}
