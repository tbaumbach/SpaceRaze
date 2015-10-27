//Title:        SpaceRaze
//Author:       Paul Bodin
//Description:  Javabaserad version av Spaceraze.
//Bygger på Spaceraze Galaxy fast skall fungera mera som Wigges webbaserade variant.
//Detta Javaprojekt omfattar serversidan av spelet.

package sr.world;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import sr.enums.HighlightType;
import sr.general.logging.Logger;
import sr.world.incomeExpensesReports.IncomeReport;
import sr.world.incomeExpensesReports.IncomeType;
import sr.world.landbattle.report.LandBattleReport;
import sr.world.landbattle.report.LandBattleReports;
import sr.world.spacebattle.SpaceBattleReport;
import sr.world.spacebattle.SpaceBattleReports;

public class TurnInfo implements Serializable{
  static final long serialVersionUID = 1L;
  private List<Report> generalReports,expenseReports,VIPReports,blackMarketReports,civilianReports,researchReports,buildingReports,diplomacyReports;
  private List<EconomyReport> economyReports;
  private List<LandBattleReports> allLandBattleReports;
  private List<IncomeReport> incomeReports;
  private List<SpaceBattleReports> allSpaceBattleReports;

  public TurnInfo() {
    generalReports = new LinkedList<Report>();
    generalReports.add(new Report());
//    battleReports = new LinkedList<Report>();
//    battleReports.add(new Report());
    expenseReports = new LinkedList<Report>();
    expenseReports.add(new Report());
    VIPReports = new LinkedList<Report>();
    VIPReports.add(new Report());
    blackMarketReports = new LinkedList<Report>();
    blackMarketReports.add(new Report());
    economyReports = new LinkedList<EconomyReport>();
    economyReports.add(new EconomyReport());
    civilianReports = new LinkedList<Report>();
    civilianReports.add(new Report());
    researchReports = new LinkedList<Report>();
    researchReports.add(new Report());
    buildingReports = new LinkedList<Report>();
    buildingReports.add(new Report());
    diplomacyReports = new LinkedList<Report>();
    diplomacyReports.add(new Report());
    allLandBattleReports = new LinkedList<LandBattleReports>();
    allLandBattleReports.add(new LandBattleReports());
    allSpaceBattleReports = new LinkedList<SpaceBattleReports>();
    allSpaceBattleReports.add(new SpaceBattleReports());
    incomeReports = new LinkedList<IncomeReport>();
    incomeReports.add(new IncomeReport());
  }
  
  public void newTurn(){
    generalReports.add(new Report());
//    battleReports.add(new Report());
    expenseReports.add(new Report());
    VIPReports.add(new Report());
    blackMarketReports.add(new Report());
    economyReports.add(new EconomyReport());
    civilianReports.add(new Report());
    researchReports.add(new Report());
    buildingReports.add(new Report());
    diplomacyReports.add(new Report());
    allLandBattleReports.add(new LandBattleReports());
    allSpaceBattleReports.add(new SpaceBattleReports());
    incomeReports.add(new IncomeReport());
  }

  public void addToLatestSpaceBattleReports(SpaceBattleReport aSpaceBattleReport){
	  SpaceBattleReports lbr = allSpaceBattleReports.get(allSpaceBattleReports.size() - 1);
	  lbr.addNewSpaceBattleReport(aSpaceBattleReport);
  }

  public void addToLatestLandBattleReports(LandBattleReport aLandBattleReport){
	  LandBattleReports lbr = allLandBattleReports.get(allLandBattleReports.size() - 1);
	  lbr.addNewLandBattleReport(aLandBattleReport);
  }

  public void addToLatestIncomeReport(IncomeType type, String desc, String location, int value){
	  IncomeReport ir = incomeReports.get(incomeReports.size() - 1);
	  ir.addNewRow(type, desc, location, value);
  }

  public void addToLatestGeneralReport(String str){
	  Report r = generalReports.get(generalReports.size() - 1);
	  r.addReport(str);
  }

  public void addToLatestGeneralReportAt(String str, int index){
	  Report r = generalReports.get(generalReports.size() - 1);
	  r.addReportAt(str,index);
  }

  public void addToLatestDiplomacyReport(String str){
	  Report r = diplomacyReports.get(diplomacyReports.size() - 1);
	  r.addReport(str);
  }

  public void addToLatestHighlights(String str, HighlightType type){
      Report r = generalReports.get(generalReports.size() - 1);
      r.addHighlight(str,type);
  }

  public void addToLatestShipsLostInSpace(Spaceship ss){
    Report r = generalReports.get(generalReports.size() - 1);
    r.addShipLostInSpace(ss);
  }

  public void addToLatestTroopsLostInSpace(Troop aTroop){
	  Report r = generalReports.get(generalReports.size() - 1);
	  r.addTroopLostInSpace(aTroop);
  }

//  public void addToLatestBattleReport(String str){
//    Report r = battleReports.get(battleReports.size() - 1);
//    r.addReport(str);
//  }

  public void addToLatestExpenseReport(String str){
    Report r = expenseReports.get(expenseReports.size() - 1);
    r.addReport(str);
  }

  public void addToLatestVIPReport(String str){
	Logger.finest("adding to general: " + str);
    Report r = VIPReports.get(VIPReports.size() - 1);
    r.addReport(str);
  }

  public void addToLatestBlackMarketReport(String str){
    Report r = blackMarketReports.get(blackMarketReports.size() - 1);
    r.addReport(str);
  }

  public void addToLatestCivilianReport(String str){
	  Report r = civilianReports.get(civilianReports.size() - 1);
	  r.addReport(str);
  }
  
  public void addToLatestResearchReport(String str){
	  Report r = researchReports.get(researchReports.size() - 1);
	  r.addReport(str);
  }
  
  public void addToLatestBuldingsReport(String str){
	  Report r = buildingReports.get(buildingReports.size() - 1);
	  r.addReport(str);
  }
  
  public LandBattleReports getLatestLandBattleReports(){
	  LandBattleReports lbr = allLandBattleReports.get(allLandBattleReports.size() - 1);
	  return lbr;
  }

  public SpaceBattleReports getLatestSpaceBattleReports(){
	  SpaceBattleReports lbr = allSpaceBattleReports.get(allSpaceBattleReports.size() - 1);
	  return lbr;
  }

  public Report getLatestGeneralReport(){
    Report r = generalReports.get(generalReports.size() - 1);
    return r;
  }

//  public Report getLatestBattleReport(){
//    Report r = battleReports.get(battleReports.size() - 1);
//    return r;
//  }

  public Report getLatestExpenseReport(){
    Report r = expenseReports.get(expenseReports.size() - 1);
    return r;
  }

  public Report getLatestVIPReports(){
    Report r = VIPReports.get(VIPReports.size() - 1);
    return r;
  }

  public Report getLatestBlackMarketReports(){
    Report r = blackMarketReports.get(blackMarketReports.size() - 1);
    return r;
  }

  public Report getLatestCivilianReports(){
	  Report r = civilianReports.get(civilianReports.size() - 1);
	  return r;
  }
  
  public Report getLatestResearchReports(){
	  Report r = researchReports.get(researchReports.size() - 1);
	  return r;
  }

  public IncomeReport getLatestIncomeReports(){
	  IncomeReport ir = incomeReports.get(incomeReports.size() - 1);
	  return ir;
  }

  public Report getLatestBuildingReports(){
	  Report r = buildingReports.get(buildingReports.size() - 1);
	  return r;
  }

  public Report getLatestDiplomacyReports(){
	  Report r = diplomacyReports.get(diplomacyReports.size() - 1);
	  return r;
  }

  public EconomyReport getLatestEconomyReport(){
    return getEconomyReport(economyReports.size() - 1);
  }

  public Report getGeneralReport(int index){
	  Report r = generalReports.get(index);
	  return r;
  }

//  public Report getBattleReport(int index){
//    Report r = (Report)battleReports.get(index);
//    return r;
//  }

  public LandBattleReports getLandBattleReports(int index){
	  LandBattleReports r = allLandBattleReports.get(index);
	  return r;
  }

  public SpaceBattleReports getSpaceBattleReports(int index){
	  SpaceBattleReports r = allSpaceBattleReports.get(index);
	  return r;
  }

  public Report getExpenseReport(int index){
    Report r = expenseReports.get(index);
    return r;
  }

  public Report getVIPReport(int index){
    Report r = VIPReports.get(index);
    return r;
  }

  public Report getBlackMarketReport(int index){
    Report r = blackMarketReports.get(index);
    return r;
  }

  public EconomyReport getEconomyReport(int index){
    EconomyReport er = economyReports.get(index);
    return er;
  }

  public Report getCivilianReport(int index){
	  Report cr = civilianReports.get(index);
	  return cr;
  }
  
  public Report getResearchReport(int index){
	    Report r = researchReports.get(index);
	    return r;
	  }
  
  public Report getBuildingsReport(int index){
	  Report r = buildingReports.get(index);
	  return r;
  }

  public Report getDiplomacyReport(int index){
	  Report r = diplomacyReports.get(index);
	  return r;
  }

  public int getGeneralSize(){
    Report r = generalReports.get(generalReports.size() - 1);
    return r.size();
  }

  public void pruneDroid(){
	  removeAllExceptLast(generalReports);
	  removeAllExceptLast(expenseReports);
	  removeAllExceptLast(VIPReports);
	  removeAllExceptLast(blackMarketReports);
	  removeAllExceptLast(economyReports);
	  removeAllExceptLast(civilianReports);
	  removeAllExceptLast(researchReports);
	  removeAllExceptLast(buildingReports);
	  removeAllExceptLast(diplomacyReports);
	  removeAllExceptLast(allLandBattleReports);
	  removeAllExceptLast(allSpaceBattleReports);
	  removeAllExceptLast(incomeReports);
  }
  
  @SuppressWarnings("unchecked")
  private void removeAllExceptLast(List list){
	  int size = list.size();
	  Object lastEntry = list.get(size-1);
	  list.clear();
	  for (int i = 0; i < (size-1); i++) {
		  list.add(null);
	  }
	  list.add(lastEntry);
  }

}

