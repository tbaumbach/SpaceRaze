package sr.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.SRScrollPane;
import sr.client.components.SRTextArea;
import sr.client.components.scrollable.ListPanel;
import sr.client.interfaces.SRUpdateablePanel;
import sr.general.Functions;
import sr.world.Faction;
import sr.world.Player;
import sr.world.SpaceshipType;
import sr.world.comparator.FactionsComparator;
import sr.world.comparator.spaceshiptype.SpaceshipTypeComparator;
import sr.world.comparator.spaceshiptype.SpaceshipTypeNameComparator;

@SuppressWarnings("serial")
public class ShiptypePanel extends SRBasePanel implements ListSelectionListener, SRUpdateablePanel, ActionListener {
    private List<SpaceshipType> spaceshiptypes;
    private List<Faction> factions;
    private ListPanel shiptypelist = null;
    private ComboBoxPanel filterChoice;
    private SRLabel typenamelbl, typenamelbl2, typenamelbl3, typenamelbl4;
    private SRLabel shortTypenamelbl, shortTypenamelbl2, shortTypenamelbl3, shortTypenamelbl4;
    private SRLabel canbuildlbl, canbuildlbl2;
    private SRLabel shipTypeLbl, shipTypeLbl2, shipTypeLbl3, shipTypeLbl4;
    private SRLabel targetingTypeLbl, targetingTypeLbl2, targetingTypeLbl3, targetingTypeLbl4;
    //private SRLabel slotsLabel, slotsLabel2; //not in use. check the ship size.
    private SRLabel sizelbl, sizelbl2, sizelbl3, sizelbl4;
    private SRLabel rangelbl, rangelbl2, rangelbl3, rangelbl4;
    private SRLabel damageCapacitylbl, damageCapacitylbl2, damageCapacitylbl3, damageCapacitylbl4;
    private SRLabel shieldslbl, shieldslbl2, shieldslbl3, shieldslbl4;
    private SRLabel weaponsSquadronLbl, weaponsSquadronLbl2, weaponsSquadronLbl3, weaponsSquadronLbl4;
    private SRLabel weaponsLbl,weaponsLbl2, weaponsLbl3, weaponsLbl4;
    private SRLabel armorLbl,armorLbl2, armorLbl3, armorLbl4;
    private SRLabel psychlbl, psychlbl2, psychlbl3, psychlbl4;
    private SRLabel bombardmentlbl, bombardmentlbl2, bombardmentlbl3, bombardmentlbl4;
    private SRLabel squadronCapacityLbl, squadronCapacityLbl2, squadronCapacityLbl3, squadronCapacityLbl4;
    private SRLabel troopCapLbl,troopCapLbl2, troopCapLbl3, troopCapLbl4;
    private SRLabel uniqueGradeLbl, uniqueGradeLbl2, uniqueGradeLbl3, uniqueGradeLbl4;
    private SRLabel initiativeLabel, initiativeLabel2, initiativeLabel3, initiativeLabel4;
    private SRLabel initSupportBonusLbl, initSupportBonusLbl2, initSupportBonusLbl3, initSupportBonusLbl4;
    private SRLabel initDefenceBonusLbl, initDefenceBonusLbl2, initDefenceBonusLbl3, initDefenceBonusLbl4;
    private SRLabel retreatLabel, retreatLabel2, retreatLabel3, retreatLabel4;
    private SRLabel cloakingLbl,cloakingLbl2, cloakingLbl3, cloakingLbl4;
    private SRLabel supplyLabel, supplyLabel2, supplyLabel3, supplyLabel4;
    private SRLabel canBesiegeLbl,canBesiegeLbl2, canBesiegeLbl3, canBesiegeLbl4;
    private SRLabel surveyLbl, surveyLbl2, surveyLbl3, surveyLbl4;
    private SRLabel canAttackScreenLbl, canAttackScreenLbl2, canAttackScreenLbl3, canAttackScreenLbl4; 
    
    private SRLabel civilianLbl,civilianLbl2, civilianLbl3, civilianLbl4;
    private SRLabel lookLikeCivilianLbl,lookLikeCivilianLbl2, lookLikeCivilianLbl3, lookLikeCivilianLbl4;
    private SRLabel incomeClosedLbl,incomeClosedLbl2, incomeClosedLbl3, incomeClosedLbl4;
    private SRLabel incomeOpenLbl,incomeOpenLbl2, incomeOpenLbl3, incomeOpenLbl4;
    private SRLabel alwaysRetreatLbl1,alwaysRetreatLbl2;
    private SRLabel screenedLbl1 ,screenedLbl2;
    
    //private SRLabel blackMarketLbl, blackMarketLbl2;

    private SRLabel upkeepLabel, upkeepLabel2, upkeepLabel3, upkeepLabel4;
    private SRLabel buildCostLabel,buildCostLabel2, buildCostLabel3, buildCostLabel4;
    
    
    private SRLabel shiptypeInfoLabel;
    private String id;
    private Player p;
    private int column1X = 195;
    private int column2X = 340;
    private int column3X = 520;
    private int column4X = 655;
    // used for computing components location
    private int yPosition = 10;
    private final int yInterval = 17;
    @SuppressWarnings("unused")
	private boolean hasSquadrons; // not used (yet?)
    private SRTextArea shiptypeInfoTextArea;
    private JScrollPane scrollPane;
    private SRButton buttonAddShipToCompare;
    private SpaceshipType spaceShipType;
    
    public ShiptypePanel(List<SpaceshipType> spaceshiptypes, Player p, String id, boolean hasSquadrons){
      this.spaceshiptypes = Functions.cloneList(spaceshiptypes);
	  Collections.sort(this.spaceshiptypes,new SpaceshipTypeComparator());
      this.id = id;
      this.p = p;
      this.hasSquadrons = hasSquadrons;
      this.setLayout(null);
      setBackground(StyleGuide.colorBackground);
      
      int cHeight = 18;

      filterChoice = new ComboBoxPanel();
      filterChoice.setBounds(10,10,170,20);
      filterChoice.addActionListener(this);
      fillFilterList();
      this.add(filterChoice);

      fillShiptypeList();

      typenamelbl = new SRLabel();
      typenamelbl.setSize(190,cHeight);
      add(typenamelbl);
      typenamelbl2 = new SRLabel();
      typenamelbl2.setSize(240,cHeight);
      add(typenamelbl2);
      
      typenamelbl3 = new SRLabel();
      typenamelbl3.setSize(190,cHeight);
      add(typenamelbl3);
      typenamelbl4 = new SRLabel();
      typenamelbl4.setSize(240,cHeight);
      add(typenamelbl4);
      
      shortTypenamelbl = new SRLabel();
      shortTypenamelbl.setSize(190,cHeight);
      add(shortTypenamelbl);
      shortTypenamelbl2 = new SRLabel();
      shortTypenamelbl2.setSize(240,cHeight);
      add(shortTypenamelbl2);
      
      shortTypenamelbl3 = new SRLabel();
      shortTypenamelbl3.setSize(190,cHeight);
      add(shortTypenamelbl3);
      shortTypenamelbl4 = new SRLabel();
      shortTypenamelbl4.setSize(240,cHeight);
      add(shortTypenamelbl4);

      shipTypeLbl = new SRLabel();
      shipTypeLbl.setSize(190,cHeight);
      add(shipTypeLbl);
      shipTypeLbl2 = new SRLabel();
      shipTypeLbl2.setSize(190,cHeight);
      add(shipTypeLbl2);
      shipTypeLbl.setToolTipText("Capital Ship / Fighter Squadron / Bomber Squadron / Multirole Squadron");
	  shipTypeLbl2.setToolTipText("Capital Ship / Fighter Squadron / Bomber Squadron / Multirole Squadron");
      
      shipTypeLbl3 = new SRLabel();
      shipTypeLbl3.setSize(190,cHeight);
      add(shipTypeLbl3);
      shipTypeLbl4 = new SRLabel();
      shipTypeLbl4.setSize(190,cHeight);
      add(shipTypeLbl4);
      shipTypeLbl3.setToolTipText("Capital Ship / Fighter Squadron / Bomber Squadron / Multirole Squadron");
	  shipTypeLbl4.setToolTipText("Capital Ship / Fighter Squadron / Bomber Squadron / Multirole Squadron");
	  
	  
	  targetingTypeLbl = new SRLabel();
      targetingTypeLbl.setSize(100,cHeight);
      add(targetingTypeLbl);
      targetingTypeLbl2 = new SRLabel();
      targetingTypeLbl2.setSize(100,cHeight);
      add(targetingTypeLbl2);
      targetingTypeLbl.setToolTipText("Anti Squadron / Balanced / Anti Capital");
	  targetingTypeLbl2.setToolTipText("Anti Squadron / Balanced / Anti Capital");
      
      targetingTypeLbl3 = new SRLabel();
      targetingTypeLbl3.setSize(100,cHeight);
      add(targetingTypeLbl3);
      targetingTypeLbl4 = new SRLabel();
      targetingTypeLbl4.setSize(100,cHeight);
      add(targetingTypeLbl4);
      targetingTypeLbl3.setToolTipText("Anti Squadron / Balanced / Anti Capital");
	  targetingTypeLbl4.setToolTipText("Anti Squadron / Balanced / Anti Capital");
      
	  canbuildlbl = new SRLabel();
      canbuildlbl.setSize(100,cHeight);
      add(canbuildlbl);
      canbuildlbl2 = new SRLabel();
      canbuildlbl2.setSize(100,cHeight);
      add(canbuildlbl2);

      sizelbl = new SRLabel();
      sizelbl.setSize(100,cHeight);
      sizelbl.setToolTipText("Small / Medium / Large / Huge");
      add(sizelbl);
      sizelbl2 = new SRLabel();
      sizelbl2.setSize(100,cHeight);
      sizelbl2.setToolTipText("Small / Medium / Large / Huge");
      add(sizelbl2);
      
      sizelbl3 = new SRLabel();
      sizelbl3.setSize(100,cHeight);
      sizelbl3.setToolTipText("Small / Medium / Large / Huge");
      add(sizelbl3);
      sizelbl4 = new SRLabel();
      sizelbl4.setSize(100,cHeight);
      sizelbl4.setToolTipText("Small / Medium / Large / Huge");
      add(sizelbl4);
      
      rangelbl = new SRLabel();
      rangelbl.setSize(100,cHeight);
      rangelbl.setToolTipText("Short / Long");
      add(rangelbl);
      rangelbl2 = new SRLabel();
      rangelbl2.setSize(100,cHeight);
      rangelbl2.setToolTipText("Short / Long");
      add(rangelbl2);

      rangelbl3 = new SRLabel();
      rangelbl3.setSize(100,cHeight);
      rangelbl3.setToolTipText("Short / Long");
      add(rangelbl3);
      rangelbl4 = new SRLabel();
      rangelbl4.setSize(100,cHeight);
      rangelbl4.setToolTipText("Short / Long");
      add(rangelbl4);

      weaponsSquadronLbl = new SRLabel();
      weaponsSquadronLbl.setSize(130,cHeight);
      weaponsSquadronLbl.setToolTipText("Attack against squdrons");
      add(weaponsSquadronLbl);
      weaponsSquadronLbl2 = new SRLabel();
      weaponsSquadronLbl2.setSize(100,cHeight);
      weaponsSquadronLbl2.setToolTipText("Attack against squdrons");
      add(weaponsSquadronLbl2);
      
      weaponsSquadronLbl3 = new SRLabel();
      weaponsSquadronLbl3.setSize(130,cHeight);
      weaponsSquadronLbl3.setToolTipText("Attack against squdrons");
      add(weaponsSquadronLbl3);
      weaponsSquadronLbl4 = new SRLabel();
      weaponsSquadronLbl4.setSize(100,cHeight);
      weaponsSquadronLbl4.setToolTipText("Attack against squdrons");
      add(weaponsSquadronLbl4);

      weaponsLbl = new SRLabel();
      weaponsLbl.setSize(130,cHeight);
      weaponsLbl.setToolTipText("(s)mall+ / (m)edium+ / (l)arge+ / (h)uge");
      add(weaponsLbl);
      weaponsLbl2 = new SRLabel();
      weaponsLbl2.setSize(300,cHeight);
      add(weaponsLbl2);
      
      weaponsLbl3 = new SRLabel();
      weaponsLbl3.setSize(130,cHeight);
      weaponsLbl3.setToolTipText("(s)mall+ / (m)edium+ / (l)arge+ / (h)uge");
      add(weaponsLbl3);
      weaponsLbl4 = new SRLabel();
      weaponsLbl4.setSize(300,cHeight);
      add(weaponsLbl4);

      armorLbl = new SRLabel();
      armorLbl.setSize(130,cHeight);
      armorLbl.setToolTipText("(s)mall / (m)edium / (l)arge / (h)uge");
      add(armorLbl);
      armorLbl2 = new SRLabel();
      armorLbl2.setSize(300,cHeight);
      add(armorLbl2);
      
      armorLbl3 = new SRLabel();
      armorLbl3.setSize(130,cHeight);
      armorLbl3.setToolTipText("(s)mall / (m)edium / (l)arge / (h)uge");
      add(armorLbl3);
      armorLbl4 = new SRLabel();
      armorLbl4.setSize(300,cHeight);
      add(armorLbl4);

      incomeOpenLbl = new SRLabel();
      incomeOpenLbl.setSize(130,cHeight);
      incomeOpenLbl.setToolTipText("(o)wn / (f)riendly / (n)eutral / (e)nemy");
      add(incomeOpenLbl);
      incomeOpenLbl2 = new SRLabel();
      incomeOpenLbl2.setSize(300,cHeight);
      add(incomeOpenLbl2);
      
      incomeOpenLbl3 = new SRLabel();
      incomeOpenLbl3.setSize(130,cHeight);
      incomeOpenLbl3.setToolTipText("(o)wn / (f)riendly / (n)eutral / (e)nemy");
      add(incomeOpenLbl3);
      incomeOpenLbl4 = new SRLabel();
      incomeOpenLbl4.setSize(300,cHeight);
      add(incomeOpenLbl4);

      incomeClosedLbl = new SRLabel();
      incomeClosedLbl.setSize(137,cHeight);
      incomeClosedLbl.setToolTipText("(o)wn / (f)riendly / (n)eutral / (e)nemy");
      add(incomeClosedLbl);
      incomeClosedLbl2 = new SRLabel();
      incomeClosedLbl2.setSize(300,cHeight);
      add(incomeClosedLbl2);
      
      incomeClosedLbl3 = new SRLabel();
      incomeClosedLbl3.setSize(137,cHeight);
      incomeClosedLbl3.setToolTipText("(o)wn / (f)riendly / (n)eutral / (e)nemy");
      add(incomeClosedLbl3);
      incomeClosedLbl4 = new SRLabel();
      incomeClosedLbl4.setSize(300,cHeight);
      add(incomeClosedLbl4);

      shieldslbl = new SRLabel();
      shieldslbl.setSize(100,cHeight);
      shieldslbl.setToolTipText("The shields reloads every turn");
      add(shieldslbl);
      shieldslbl2 = new SRLabel();
      shieldslbl2.setSize(100,cHeight);
      shieldslbl2.setToolTipText("The shields reloads every turn");
      add(shieldslbl2);
      
      shieldslbl3 = new SRLabel();
      shieldslbl3.setSize(100,cHeight);
      shieldslbl3.setToolTipText("The shields reloads every turn");
      add(shieldslbl3);
      shieldslbl4 = new SRLabel();
      shieldslbl4.setSize(100,cHeight);
      shieldslbl4.setToolTipText("The shields reloads every turn");
      add(shieldslbl4);
 
      damageCapacitylbl = new SRLabel();
      damageCapacitylbl.setSize(150,cHeight);
      damageCapacitylbl.setToolTipText("Repair damage ships on planets with a wharf (larger or same size as the ship)");
      add(damageCapacitylbl);
      damageCapacitylbl2 = new SRLabel();
      damageCapacitylbl2.setSize(150,cHeight);
      damageCapacitylbl2.setToolTipText("Repair damage ships on planets with a wharf (larger or same size as the ship)");
      add(damageCapacitylbl2);
      
      damageCapacitylbl3 = new SRLabel();
      damageCapacitylbl3.setSize(150,cHeight);
      damageCapacitylbl3.setToolTipText("Repair damage ships on planets with a wharf (larger or same size as the ship)");
      add(damageCapacitylbl3);
      damageCapacitylbl4 = new SRLabel();
      damageCapacitylbl4.setSize(150,cHeight);
      damageCapacitylbl4.setToolTipText("Repair damage ships on planets with a wharf (larger or same size as the ship)");
      add(damageCapacitylbl4);

      psychlbl = new SRLabel();
      psychlbl.setSize(100,cHeight);
      psychlbl.setToolTipText("Used to lower the planet resistance to conquer them");
      add(psychlbl);
      psychlbl2 = new SRLabel();
      psychlbl2.setSize(100,cHeight);
      psychlbl2.setToolTipText("Used to lower the planet resistance to conquer them");
      add(psychlbl2);
      
      psychlbl3 = new SRLabel();
      psychlbl3.setSize(100,cHeight);
      psychlbl3.setToolTipText("Used to lower the planet resistance to conquer them");
      add(psychlbl3);
      psychlbl4 = new SRLabel();
      psychlbl4.setSize(100,cHeight);
      psychlbl4.setToolTipText("Used to lower the planet resistance to conquer them");
      add(psychlbl4);

      bombardmentlbl = new SRLabel();
      bombardmentlbl.setSize(100,cHeight);
      bombardmentlbl.setToolTipText("Lower the planet resistance, population and destroyes troops");
      add(bombardmentlbl);
      bombardmentlbl2 = new SRLabel();
      bombardmentlbl2.setSize(100,cHeight);
      bombardmentlbl2.setToolTipText("Lower the planet resistance, population and destroyes troops");
      add(bombardmentlbl2);
      
      bombardmentlbl3 = new SRLabel();
      bombardmentlbl3.setSize(100,cHeight);
      bombardmentlbl3.setToolTipText("Lower the planet resistance, population and destroyes troops");
      add(bombardmentlbl3);
      bombardmentlbl4 = new SRLabel();
      bombardmentlbl4.setSize(100,cHeight);
      bombardmentlbl4.setToolTipText("Lower the planet resistance, population and destroyes troops");
      add(bombardmentlbl4);
      
      retreatLabel = new SRLabel();
      retreatLabel.setSize(130,cHeight);
      retreatLabel.setToolTipText("Stops ships from retreat from the battleground");
      add(retreatLabel);
      retreatLabel2 = new SRLabel();
      retreatLabel2.setSize(130,cHeight);
      retreatLabel2.setToolTipText("Stops ships from retreat from the battleground");
      add(retreatLabel2);
      
      retreatLabel3 = new SRLabel();
      retreatLabel3.setSize(130,cHeight);
      retreatLabel3.setToolTipText("Stops ships from retreat from the battleground");
      add(retreatLabel3);
      retreatLabel4 = new SRLabel();
      retreatLabel4.setSize(130,cHeight);
      retreatLabel4.setToolTipText("Stops ships from retreat from the battleground");
      add(retreatLabel4);

      initiativeLabel = new SRLabel();
      initiativeLabel.setSize(150,cHeight);
      initiativeLabel.setToolTipText("Give the fleet initiative advantage");
      add(initiativeLabel);
      initiativeLabel2 = new SRLabel();
      initiativeLabel2.setSize(150,cHeight);
      initiativeLabel2.setToolTipText("Give the fleet initiative advantage");
      add(initiativeLabel2);
      
      initiativeLabel3 = new SRLabel();
      initiativeLabel3.setSize(150,cHeight);
      initiativeLabel3.setToolTipText("Give the fleet initiative advantage (only on capital ships)");
      add(initiativeLabel3);
      initiativeLabel4 = new SRLabel();
      initiativeLabel4.setSize(150,cHeight);
      initiativeLabel4.setToolTipText("Give the fleet initiative advantage");
      add(initiativeLabel4);

      initSupportBonusLbl = new SRLabel();
      initSupportBonusLbl.setSize(110,cHeight);
      initSupportBonusLbl.setToolTipText("Powerful squadron giving fleet initiative (is good to combine with capital ships initiative bonus)");
      add(initSupportBonusLbl);
      initSupportBonusLbl2 = new SRLabel();
      initSupportBonusLbl2.setSize(100,cHeight);
      initSupportBonusLbl2.setToolTipText("Powerful squadron giving fleet initiative (is good to combine with capital ships initiative bonus)");
      add(initSupportBonusLbl2);
      
      initSupportBonusLbl3 = new SRLabel();
      initSupportBonusLbl3.setSize(110,cHeight);
      initSupportBonusLbl3.setToolTipText("Powerful squadron giving fleet initiative (is good to combine with capital ships initiative bonus)");
      add(initSupportBonusLbl3);
      initSupportBonusLbl4 = new SRLabel();
      initSupportBonusLbl4.setSize(100,cHeight);
      initSupportBonusLbl4.setToolTipText("Powerful squadron giving fleet initiative (is good to combine with capital ships initiative bonus)");
      add(initSupportBonusLbl4);

      initDefenceBonusLbl = new SRLabel();
      initDefenceBonusLbl.setSize(110,cHeight);
      initDefenceBonusLbl.setToolTipText("Lower enemys initiative if they have any ship with 'initiative bonus' or 'initiative support bonus'");
      add(initDefenceBonusLbl);
      initDefenceBonusLbl2 = new SRLabel();
      initDefenceBonusLbl2.setSize(100,cHeight);
      initDefenceBonusLbl2.setToolTipText("Lower enemys initiative if they have any ship with 'initiative bonus' or 'initiative support bonus'");
      add(initDefenceBonusLbl2);
      
      initDefenceBonusLbl3 = new SRLabel();
      initDefenceBonusLbl3.setSize(110,cHeight);
      initDefenceBonusLbl3.setToolTipText("Lower enemys initiative if they have any ship with 'initiative bonus' or 'initiative support bonus'");
      add(initDefenceBonusLbl3);
      initDefenceBonusLbl4 = new SRLabel();
      initDefenceBonusLbl4.setSize(100,cHeight);
      initDefenceBonusLbl4.setToolTipText("Lower enemys initiative if they have any ship with 'initiative bonus' or 'initiative support bonus'");
      add(initDefenceBonusLbl4);

      cloakingLbl = new SRLabel();
      cloakingLbl.setSize(110,cHeight);
      cloakingLbl.setToolTipText("This ship is unvisible on enemys map");
      add(cloakingLbl);
      cloakingLbl2 = new SRLabel();
      cloakingLbl2.setSize(100,cHeight);
      cloakingLbl2.setToolTipText("This ship is unvisible on enemys map");
      add(cloakingLbl2);
      
      cloakingLbl3 = new SRLabel();
      cloakingLbl3.setSize(110,cHeight);
      cloakingLbl3.setToolTipText("This ship is unvisible on enemys map");
      add(cloakingLbl3);
      cloakingLbl4 = new SRLabel();
      cloakingLbl4.setSize(100,cHeight);
      cloakingLbl4.setToolTipText("This ship is unvisible on enemys map");
      add(cloakingLbl4);

      buildCostLabel = new SRLabel();
      buildCostLabel.setSize(100,cHeight);
      buildCostLabel.setToolTipText("Cost to build this ship");
      add(buildCostLabel);
      buildCostLabel2 = new SRLabel();
      buildCostLabel2.setSize(100,cHeight);
      buildCostLabel2.setToolTipText("Cost to build this ship");
      add(buildCostLabel2);
      
      buildCostLabel3 = new SRLabel();
      buildCostLabel3.setSize(100,cHeight);
      buildCostLabel3.setToolTipText("Cost to build this ship");
      add(buildCostLabel3);
      buildCostLabel4 = new SRLabel();
      buildCostLabel4.setSize(100,cHeight);
      buildCostLabel4.setToolTipText("Cost to build this ship");
      add(buildCostLabel4);

      upkeepLabel = new SRLabel();
      upkeepLabel.setSize(100,cHeight);
      upkeepLabel.setToolTipText("Cost to maintenance this ship");
      add(upkeepLabel);
      upkeepLabel2 = new SRLabel();
      upkeepLabel2.setSize(100,cHeight);
      upkeepLabel2.setToolTipText("Cost to maintenance this ship");
      add(upkeepLabel2);
      
      upkeepLabel3 = new SRLabel();
      upkeepLabel3.setSize(100,cHeight);
      upkeepLabel3.setToolTipText("Cost to maintenance this ship");
      add(upkeepLabel3);
      upkeepLabel4 = new SRLabel();
      upkeepLabel4.setSize(100,cHeight);
      upkeepLabel4.setToolTipText("Cost to maintenance this ship");
      add(upkeepLabel4);
      
      supplyLabel = new SRLabel();
      supplyLabel.setSize(150,cHeight);
      supplyLabel.setToolTipText("This ship can reloade ammo at ships in the same fleet");
      add(supplyLabel);
      supplyLabel2 = new SRLabel();
      supplyLabel2.setSize(150,cHeight);
      supplyLabel2.setToolTipText("Possible ship size to reloade weaponse ammo");
      add(supplyLabel2);
      
      supplyLabel3 = new SRLabel();
      supplyLabel3.setSize(150,cHeight);
      supplyLabel3.setToolTipText("This ship can reloade ammo at ships in the same fleet");
      add(supplyLabel3);
      supplyLabel4 = new SRLabel();
      supplyLabel4.setSize(150,cHeight);
      supplyLabel4.setToolTipText("Possible ship size to reloade weaponse ammo");
      add(supplyLabel4);

      civilianLbl = new SRLabel();
      civilianLbl.setSize(100,cHeight);
      civilianLbl.setToolTipText("A civilian ship without any weapons");
      add(civilianLbl);
      civilianLbl2 = new SRLabel();
      civilianLbl2.setSize(100,cHeight);
      civilianLbl2.setToolTipText("A civilian ship without any weapons");
      add(civilianLbl2);
      
      civilianLbl3 = new SRLabel();
      civilianLbl3.setSize(100,cHeight);
      civilianLbl3.setToolTipText("A civilian ship without any weapons");
      add(civilianLbl3);
      civilianLbl4 = new SRLabel();
      civilianLbl4.setSize(100,cHeight);
      civilianLbl4.setToolTipText("A civilian ship without any weapons");
      add(civilianLbl4);

      alwaysRetreatLbl1 = new SRLabel();
      alwaysRetreatLbl1.setSize(100,cHeight);
      alwaysRetreatLbl1.setToolTipText("A civilian ship that can retreat from military ships");
      add(alwaysRetreatLbl1);
      alwaysRetreatLbl2 = new SRLabel();
      alwaysRetreatLbl2.setSize(100,cHeight);
      alwaysRetreatLbl2.setToolTipText("A civilian ship that can retreat from military ships");
      add(alwaysRetreatLbl2);
      
      screenedLbl1 = new SRLabel();
      screenedLbl1.setSize(100,cHeight);
      screenedLbl1.setToolTipText("Screened ship will hide behind other ship in the fleet if it is any.");
      add(screenedLbl1);
      screenedLbl2 = new SRLabel();
      screenedLbl2.setSize(100,cHeight);
      screenedLbl2.setToolTipText("Screened ship will hide behind other ship in the fleet if it is any.");
      add(screenedLbl2);
      
 
      lookLikeCivilianLbl = new SRLabel();
      lookLikeCivilianLbl.setSize(100,cHeight);
      lookLikeCivilianLbl.setToolTipText("A military ship disguised as a civilian");
      add(lookLikeCivilianLbl);
      lookLikeCivilianLbl2 = new SRLabel();
      lookLikeCivilianLbl2.setSize(100,cHeight);
      lookLikeCivilianLbl2.setToolTipText("A military ship disguised as a civilian");
      add(lookLikeCivilianLbl2);
      
      lookLikeCivilianLbl3 = new SRLabel();
      lookLikeCivilianLbl3.setSize(100,cHeight);
      lookLikeCivilianLbl3.setToolTipText("A military ship disguised as a civilian");
      add(lookLikeCivilianLbl3);
      lookLikeCivilianLbl4 = new SRLabel();
      lookLikeCivilianLbl4.setSize(100,cHeight);
      lookLikeCivilianLbl4.setToolTipText("A military ship disguised as a civilian");
      add(lookLikeCivilianLbl4);

      canBesiegeLbl = new SRLabel();
      canBesiegeLbl.setSize(100,cHeight);
      canBesiegeLbl.setToolTipText("Decide if the ship can besiege planets or not");
      add(canBesiegeLbl);
      canBesiegeLbl2 = new SRLabel();
      canBesiegeLbl2.setSize(100,cHeight);
      canBesiegeLbl2.setToolTipText("Decide if the ship can besiege planets or not");
      add(canBesiegeLbl2);
      
      canBesiegeLbl3 = new SRLabel();
      canBesiegeLbl3.setSize(100,cHeight);
      canBesiegeLbl3.setToolTipText("Decide if the ship can besiege planets or not");
      add(canBesiegeLbl3);
      canBesiegeLbl4 = new SRLabel();
      canBesiegeLbl4.setSize(100,cHeight);
      canBesiegeLbl4.setToolTipText("Decide if the ship can besiege planets or not");
      add(canBesiegeLbl4);

      surveyLbl = new SRLabel();
      surveyLbl.setSize(100,cHeight);
      surveyLbl.setToolTipText("Can survey the planet to get more info");
      add(surveyLbl);
      surveyLbl2 = new SRLabel();
      surveyLbl2.setSize(100,cHeight);
      surveyLbl2.setToolTipText("Can survey the planet to get more info");
      add(surveyLbl2);
      
      surveyLbl3 = new SRLabel();
      surveyLbl3.setSize(100,cHeight);
      surveyLbl3.setToolTipText("Can survey the planet to get more info");
      add(surveyLbl3);
      surveyLbl4 = new SRLabel();
      surveyLbl4.setSize(100,cHeight);
      surveyLbl4.setToolTipText("Can survey the planet to get more info");
      add(surveyLbl4);

      squadronCapacityLbl = new SRLabel();
      squadronCapacityLbl.setSize(100,cHeight);
      squadronCapacityLbl.setToolTipText("Numbers of squdrons the hangar can support");
      add(squadronCapacityLbl);
      squadronCapacityLbl2 = new SRLabel();
      squadronCapacityLbl2.setSize(100,cHeight);
      squadronCapacityLbl2.setToolTipText("Numbers of squdrons the hangar can support");
      add(squadronCapacityLbl2);
      
      squadronCapacityLbl3 = new SRLabel();
      squadronCapacityLbl3.setSize(100,cHeight);
      squadronCapacityLbl3.setToolTipText("Numbers of squdrons the hangar can support");
      add(squadronCapacityLbl3);
      squadronCapacityLbl4 = new SRLabel();
      squadronCapacityLbl4.setSize(100,cHeight);
      squadronCapacityLbl4.setToolTipText("Numbers of squdrons the hangar can support");
      add(squadronCapacityLbl4);

      troopCapLbl = new SRLabel();
      troopCapLbl.setSize(100,cHeight);
      troopCapLbl.setToolTipText("Numbers of troops the ship can carry");
      add(troopCapLbl);
      troopCapLbl2 = new SRLabel();
      troopCapLbl2.setSize(100,cHeight);
      troopCapLbl2.setToolTipText("Numbers of troops the ship can carry");
      add(troopCapLbl2);
      
      troopCapLbl3 = new SRLabel();
      troopCapLbl3.setSize(100,cHeight);
      troopCapLbl3.setToolTipText("Numbers of troops the ship can carry");
      add(troopCapLbl3);
      troopCapLbl4 = new SRLabel();
      troopCapLbl4.setSize(100,cHeight);
      troopCapLbl4.setToolTipText("Numbers of troops the ship can carry");
      add(troopCapLbl4);
      
      uniqueGradeLbl = new SRLabel();
      uniqueGradeLbl.setSize(150,cHeight);
      uniqueGradeLbl.setToolTipText("Only one of this ship can be build at a level of World, Faction or Player");
      add(uniqueGradeLbl);
      uniqueGradeLbl2 = new SRLabel();
      uniqueGradeLbl2.setToolTipText("Only one of this ship can be build at a level of World, Faction or Player");
      uniqueGradeLbl2.setSize(200,cHeight);
      add(uniqueGradeLbl2);
      uniqueGradeLbl3 = new SRLabel();
      uniqueGradeLbl3.setSize(150,cHeight);
      uniqueGradeLbl3.setToolTipText("Only one of this ship can be build at a level of World, Faction or Player");
      uniqueGradeLbl3.setVisible(false);
      add(uniqueGradeLbl3);
      uniqueGradeLbl4 = new SRLabel();
      uniqueGradeLbl4.setSize(200,cHeight);
      uniqueGradeLbl4.setToolTipText("Only one of this ship can be build at a level of World, Faction or Player");
      uniqueGradeLbl4.setVisible(false);
      add(uniqueGradeLbl4);
      
      canAttackScreenLbl = new SRLabel();
      canAttackScreenLbl.setSize(150,cHeight);
      canAttackScreenLbl.setToolTipText("Possible to attack screened ships");
      add(canAttackScreenLbl);
      canAttackScreenLbl2 = new SRLabel();
      canAttackScreenLbl2.setToolTipText("Possible to attack screened ships");
      canAttackScreenLbl2.setSize(200,cHeight);
      add(canAttackScreenLbl2);
      canAttackScreenLbl3 = new SRLabel();
      canAttackScreenLbl3.setSize(150,cHeight);
      canAttackScreenLbl3.setToolTipText("Possible to attack screened ships");
      canAttackScreenLbl3.setVisible(false);
      add(canAttackScreenLbl3);
      canAttackScreenLbl4 = new SRLabel();
      canAttackScreenLbl4.setSize(200,cHeight);
      canAttackScreenLbl4.setToolTipText("Possible to attack screened ships");
      canAttackScreenLbl4.setVisible(false);
      add(canAttackScreenLbl4);
      
      buttonAddShipToCompare = new SRButton("Compare this ship");
      buttonAddShipToCompare.setToolTipText("Hit the button to get this ship values in a column at the right, to compare with other ships");
      buttonAddShipToCompare.setBounds(column1X, 430, 180, 20);
      buttonAddShipToCompare.addActionListener(this);
      buttonAddShipToCompare.setVisible(false);
      add(buttonAddShipToCompare);

      // Shiptype info textarea
      shiptypeInfoLabel = new SRLabel();
      shiptypeInfoLabel.setBounds(column1X,460,120,cHeight);
      add(shiptypeInfoLabel);

      shiptypeInfoTextArea = new SRTextArea();
      
      shiptypeInfoTextArea.setEditable(false);
 
      scrollPane = new SRScrollPane(shiptypeInfoTextArea);
      scrollPane.setBounds(column1X,480,650,110);
      scrollPane.setVisible(false);
      add(scrollPane);
    }
    
    private void fillFilterList(){
    	filterChoice.addItem("All (sort by name)");
    	filterChoice.addItem("All (sort by class & size)");
    	filterChoice.addItem("Yours");
    	factions = Functions.cloneList(p.getGalaxy().getGameWorld().getFactions());
    	Collections.sort(factions,new FactionsComparator());
    	for (Faction aFaction : factions) {
			filterChoice.addItem(aFaction.getName());
		}
    }
    
    private void fillShiptypeList(){
    	if(shiptypelist != null){
    		remove(shiptypelist);
    	}
    	shiptypelist = new ListPanel();
        shiptypelist.setBounds(10,40,170,560);
        shiptypelist.setListSelectionListener(this);
        shiptypelist.setForeground(StyleGuide.colorCurrent);
        shiptypelist.setBackground(StyleGuide.colorBackground);
        shiptypelist.setBorder(new LineBorder(StyleGuide.colorCurrent));
        
    	
        DefaultListModel dlm = (DefaultListModel)shiptypelist.getModel();
        dlm.removeAllElements();
    	List<SpaceshipType> tempSstList = null;
    	List<String> tempSstListName = new ArrayList<String>();
    	if (filterChoice.getSelectedIndex() > 2){
    		// get faction to show ships from
    		Faction showOnlyFaction = factions.get(filterChoice.getSelectedIndex() - 3);
    		tempSstList = showOnlyFaction.getSpaceshipTypes();
    	}else if(filterChoice.getSelectedIndex() == 2){
    		tempSstList = p.getSpaceshipTypes();
    	}
    	else{
    		tempSstList = spaceshiptypes;
    	}
    	
    	if (filterChoice.getSelectedIndex() == 0){
    		
    		for(int i=0;i < tempSstList.size();i++){
        		String tempName ="";
       			tempName = tempSstList.get(i).getName();
        		tempSstListName.add(tempName);
        	}
    		
    		Collections.sort(tempSstListName,new SpaceshipTypeNameComparator());
    	}else{
    		Collections.sort(tempSstList,new SpaceshipTypeComparator());
    		
    		List<String> types = new ArrayList<String>();
    		types.add("defense");
    		types.add("civilan");
    		types.add("squadron");
    		types.add("small");
    		types.add("medium");
    		types.add("large");
    		types.add("huge");
    		
    		for(int i=0;i < tempSstList.size();i++){
    			String shipTypename = checkIfNewShipType(tempSstList.get(i), types);
    			if(shipTypename != null){
    				tempSstListName.add("------------" + shipTypename + "--------------------");
    			}
        		String tempName ="";
        		if (filterChoice.getSelectedIndex() == 1){
        			tempName = tempSstList.get(i).getName();
        		}else{
        			if(tempSstList.get(i).isAvailableToBuild()){
        				tempName = tempSstList.get(i).getName();
        			}else{
        				tempName = "*" + tempSstList.get(i).getName();
        			}
        		}
        		
        		tempSstListName.add(tempName);
        	}
    	}
        for(int i = 0; i < tempSstListName.size(); i++){
        	dlm.addElement(tempSstListName.get(i));
        }
        
        shiptypelist.updateScrollList();
        add(shiptypelist);
    }
    
    private String checkIfNewShipType(SpaceshipType spaceshipType, List<String> types){
    	String type=null;
    	if((spaceshipType.isDefenceShip() && types.contains("defense")) ||
    			(spaceshipType.isCivilian() && types.contains("civilan")) ||
    			(spaceshipType.isSquadron() && types.contains("squadron")) ||
    			(!spaceshipType.isDefenceShip() && !spaceshipType.isSquadron() && !spaceshipType.isCivilian() 
    					&& types.contains(spaceshipType.getSizeString())) ){
    		if(spaceshipType.isDefenceShip()){
    			types.remove("defense");
    			type = "defense";
    		}else if(spaceshipType.isCivilian()){
    			types.remove("civilan");
    			type = "civilan";
    		}else if(spaceshipType.isSquadron()){
    			types.remove("squadron");
    			type = "squadron";
    		}else{
    			types.remove(spaceshipType.getSizeString());
    			type = spaceshipType.getSizeString();
    		}
    	}
    	return type;
    }
    
    private int newLine(){
    	yPosition = yPosition + yInterval;
    	return yPosition;
    }

    public void valueChanged(ListSelectionEvent lse){
      if (lse.getSource() instanceof ListPanel){
        showSpaceshipType(shiptypelist.getSelectedItem(), null);
      }
    }

    private SpaceshipType findSpaceshiptype(String findname){
      SpaceshipType sst = null;
      int i = 0;
      if(filterChoice.getSelectedIndex() == 2){
    	  sst = p.findSpaceshipType(findname);
      }else
      if (filterChoice.getSelectedIndex() > 2){
  		Faction aFaction = factions.get(filterChoice.getSelectedIndex() - 3);
  		sst = aFaction.getSpaceshipTypeByName(findname);
      }else{
	      while ((sst == null) & (i<spaceshiptypes.size())){
	        SpaceshipType temp = (SpaceshipType)spaceshiptypes.get(i);
	        if (temp.getName().equalsIgnoreCase(findname)){
	          sst = temp;
	        }
	        i++;
	      }
      }
      return sst;
    }

    public void showSpaceshipType(String name, String faction){
    	
    	yPosition = 10;
    	
    	if(faction != null){
    		filterChoice.setSelectedItem(faction);
    		fillShiptypeList();
    	}
    	SpaceshipType sst = null;  
    	if(!name.contains("---")){
	    	if(name.contains("*")){
	    		name = name.substring(1);
	    	}
	    	sst = findSpaceshiptype(name);
	    	spaceShipType = sst;
	    	
	    	Enumeration<?> elements = shiptypelist.getModel().elements();
	    	int selectIndex = -1; 
	    	int index = 0;
	    	
	    	if(shiptypelist.getModel().contains("*" + name)){
	    		name = "*" +name;
	    	}
	    	
	    	while(elements.hasMoreElements()){
	    		String nextElement = (String)elements.nextElement();
	    		
	    		if(nextElement.equalsIgnoreCase(name)){
	    			selectIndex = index;
	    		}
	    		index++;
	    	}
	    	if(index > 0){
	    		shiptypelist.setSelected(selectIndex);
	    	}
    	}else{
    		shiptypelist.clearSelected();
    	}
      
    	if (sst != null){
    		typenamelbl.setText("Name: ");
    		typenamelbl.setLocation(column1X,yPosition);
    		typenamelbl2.setLocation(column2X,yPosition);
    		typenamelbl2.setText(sst.getName());
    	  
    		shortTypenamelbl.setText("Short name: ");
    		shortTypenamelbl.setLocation(column1X,newLine());
    		shortTypenamelbl2.setLocation(column2X,yPosition);
    		shortTypenamelbl2.setText(sst.getShortName());
    	  
    		shipTypeLbl.setText("Type: ");
    		shipTypeLbl.setLocation(column1X,newLine());
    		shipTypeLbl2.setText(sst.getShipType());
    		shipTypeLbl2.setLocation(column2X,yPosition);
    	  
    	  
    	  targetingTypeLbl.setText("Targeting Type:");
    	  targetingTypeLbl.setLocation(column1X,newLine());
    	  targetingTypeLbl2.setText(sst.getTargetingType().toString());
    	  targetingTypeLbl2.setLocation(column2X,yPosition);
    	  
    	  sizelbl.setText("Size: ");
    	  sizelbl2.setText(sst.getSizeString());
    	  sizelbl.setLocation(column1X,newLine());
    	  sizelbl2.setLocation(column2X,yPosition);
    	  
    	  rangelbl.setText("Range: ");
    	  rangelbl2.setText(sst.getRangeString());
    	  rangelbl.setLocation(column1X,newLine());
    	  rangelbl2.setLocation(column2X,yPosition);
    	  
    	  if(!sst.isCivilian()){
    		  
    		  damageCapacitylbl.setText("Damage capacity: ");
	    	  damageCapacitylbl2.setText(String.valueOf(sst.getHits()));
	    	  damageCapacitylbl.setLocation(column1X,newLine());
	    	  damageCapacitylbl2.setLocation(column2X,yPosition);
	    	  
	    	  shieldslbl.setText("Shields: ");
	    	  shieldslbl2.setText(String.valueOf(sst.getShields()));
	    	  shieldslbl.setLocation(column1X,newLine());
	    	  shieldslbl2.setLocation(column2X,yPosition);
	    	  
	    	  weaponsSquadronLbl.setText("Weapons (squadron): ");
	    	  weaponsSquadronLbl2.setText(String.valueOf(sst.getWeaponsStrengthSquadron()));
	    	  weaponsSquadronLbl.setLocation(column1X,newLine());
	    	  weaponsSquadronLbl2.setLocation(column2X,yPosition);
	    	  
	    	  weaponsLbl.setText("Weapons (s/m/l/h): ");
	    	  weaponsLbl2.setText(getWeaponsString(sst));
	    	  weaponsLbl.setLocation(column1X,newLine());
	    	  weaponsLbl2.setLocation(column2X,yPosition);
	    	  
	    	  armorLbl.setText("Armor (s/m/l/h):");
	    	  armorLbl2.setText(getArmorString(sst));
	    	  armorLbl.setLocation(column1X,newLine());
	    	  armorLbl2.setLocation(column2X,yPosition);
	    	  
	    	  if(sst.getPsychWarfare() > 0){
	    		  psychlbl.setText("PsychWarfare: ");
		    	  psychlbl2.setText(String.valueOf(sst.getPsychWarfare()));
		    	  psychlbl.setLocation(column1X,newLine());
		    	  psychlbl2.setLocation(column2X,yPosition); 
		    	  psychlbl.setVisible(true);
	    		  psychlbl2.setVisible(true);
	    	  }else{
	    		  psychlbl.setVisible(false);
	    		  psychlbl2.setVisible(false);
	    	  }
	    	  
	    	  if(sst.getBombardment() > 0){
	    		  bombardmentlbl.setText("Bombardment: ");
		    	  bombardmentlbl2.setText(String.valueOf(sst.getBombardment()));
		    	  bombardmentlbl.setLocation(column1X,newLine());
		    	  bombardmentlbl2.setLocation(column2X,yPosition);
		    	  bombardmentlbl.setVisible(true);
		    	  bombardmentlbl2.setVisible(true);
	    	  }else{
	    		  bombardmentlbl.setVisible(false);
		    	  bombardmentlbl2.setVisible(false);
	    	  }
	    	  
	    	  
	    	  damageCapacitylbl.setVisible(true);
    		  damageCapacitylbl2.setVisible(true);
    		  shieldslbl.setVisible(true);
    		  shieldslbl2.setVisible(true);
    		  weaponsLbl.setVisible(true);
    		  weaponsLbl2.setVisible(true);
    		  weaponsSquadronLbl.setVisible(true);
    		  weaponsSquadronLbl2.setVisible(true);
    		  armorLbl.setVisible(true);
    		  armorLbl2.setVisible(true);
    		  
    	  
    	  }else{
    		  damageCapacitylbl.setVisible(false);
    		  damageCapacitylbl2.setVisible(false);
    		  shieldslbl.setVisible(false);
    		  shieldslbl2.setVisible(false);
    		  weaponsLbl.setVisible(false);
    		  weaponsLbl2.setVisible(false);
    		  weaponsSquadronLbl.setVisible(false);
    		  weaponsSquadronLbl2.setVisible(false);
    		  armorLbl.setVisible(false);
    		  armorLbl2.setVisible(false);
    		  psychlbl.setVisible(false);
    		  psychlbl2.setVisible(false);
    		  bombardmentlbl.setVisible(false);
    		  bombardmentlbl2.setVisible(false);
    	  }
    	  
    	  if(sst.getSquadronCapacity() > 0){
    		  squadronCapacityLbl.setText("Sqd capacity:");
        	  squadronCapacityLbl2.setText(String.valueOf(sst.getSquadronCapacity()));
        	  squadronCapacityLbl.setLocation(column1X,newLine());
        	  squadronCapacityLbl2.setLocation(column2X,yPosition);
        	  squadronCapacityLbl.setVisible(true);
        	  squadronCapacityLbl2.setVisible(true);
    	  }else{
    		  squadronCapacityLbl.setVisible(false);
    		  squadronCapacityLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.getTroopCapacity() > 0){
    		  troopCapLbl.setText("Troop capacity:");
    		  troopCapLbl2.setText(sst.getTroopCapacity());
    		  troopCapLbl.setLocation(column1X,newLine());
    		  troopCapLbl2.setLocation(column2X,yPosition);
    		  troopCapLbl.setVisible(true);
    		  troopCapLbl2.setVisible(true);
    	  }else{
    		  troopCapLbl.setVisible(false);
    		  troopCapLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.getInitiativeBonus() > 0){
    		  initiativeLabel.setText("Initiative bonus:");
    		  initiativeLabel2.setText(sst.getInitiativeBonus() + "%");
    		  initiativeLabel.setLocation(column1X,newLine());
    		  initiativeLabel2.setLocation(column2X,yPosition);
    		  initiativeLabel.setVisible(true);
    		  initiativeLabel2.setVisible(true);
    	  }else{
    		  initiativeLabel.setVisible(false);
    		  initiativeLabel2.setVisible(false);
    	  }
    	  
    	  if(sst.getInitSupportBonus() > 0){
    		  initSupportBonusLbl.setText("Init support bonus:");
    		  initSupportBonusLbl2.setText(sst.getInitSupportBonus() + "%");
    		  initSupportBonusLbl.setLocation(column1X,newLine());
    		  initSupportBonusLbl2.setLocation(column2X,yPosition);
    		  initSupportBonusLbl.setVisible(true);
    		  initSupportBonusLbl2.setVisible(true);
    	  }else{
    		  initSupportBonusLbl.setVisible(false);
    		  initSupportBonusLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.getInitDefence() > 0){
    		  initDefenceBonusLbl.setText("Init defence:");
    		  initDefenceBonusLbl2.setText(sst.getInitDefence() + "%");
    		  initDefenceBonusLbl.setLocation(column1X,newLine());
    		  initDefenceBonusLbl2.setLocation(column2X,yPosition);
    		  initDefenceBonusLbl.setVisible(true);
    		  initDefenceBonusLbl2.setVisible(true);
    	  }else{
    		  initDefenceBonusLbl.setVisible(false);
    		  initDefenceBonusLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.getNoRetreat()){
    		  retreatLabel.setText("Stops retreat: ");
    		  retreatLabel2.setText(sst.getNoRetreatString());
    		  retreatLabel.setLocation(column1X,newLine());
    		  retreatLabel2.setLocation(column2X,yPosition);
    		  retreatLabel.setVisible(true);
    		  retreatLabel2.setVisible(true);
    	  }else{
    		  retreatLabel.setVisible(false);
    		  retreatLabel2.setVisible(false);
    	  }
    	  
    	  if(!sst.isVisibleOnMap()){
    		  cloakingLbl.setText("Cloaking:");
    		  cloakingLbl2.setText(Functions.getYesNo(!sst.isVisibleOnMap()));
    		  cloakingLbl.setLocation(column1X,newLine());
    		  cloakingLbl2.setLocation(column2X,yPosition);
    		  cloakingLbl.setVisible(true);
    		  cloakingLbl2.setVisible(true);
    	  }else{
    		  cloakingLbl.setVisible(false);
    		  cloakingLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.getMaxResupply() > 0){
    		  supplyLabel.setText("Supply level:");
    		  supplyLabel2.setText(sst.getMaxResupplyString());
    		  supplyLabel.setLocation(column1X,newLine());
    		  supplyLabel2.setLocation(column2X,yPosition);
    		  supplyLabel.setVisible(true);
    		  supplyLabel2.setVisible(true);
    	  }else{
    		  supplyLabel.setVisible(false);
    		  supplyLabel2.setVisible(false);
    	  }
    	  
    	  canBesiegeLbl.setText("Can besiege:");
    	  canBesiegeLbl2.setText(Functions.getYesNo(sst.isCanBlockPlanet()));
    	  canBesiegeLbl.setLocation(column1X,newLine());
    	  canBesiegeLbl2.setLocation(column2X,yPosition);
    	  
    	  if(sst.isPlanetarySurvey()){
    		  surveyLbl.setText("Planetary survey:");
    		  surveyLbl2.setText(Functions.getYesNo(sst.isPlanetarySurvey()));
    		  surveyLbl.setLocation(column1X,newLine());
    		  surveyLbl2.setLocation(column2X,yPosition);
    		  surveyLbl.setVisible(true);
    		  surveyLbl2.setVisible(true);
    	  }else{
    		  surveyLbl.setVisible(false);
    		  surveyLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.isCanAttackScreenedShips()){
    		  canAttackScreenLbl.setText("Can attack screened:");
    		  canAttackScreenLbl2.setText(Functions.getYesNo(sst.isCanAttackScreenedShips()));
    		  canAttackScreenLbl.setLocation(column1X,newLine());
    		  canAttackScreenLbl2.setLocation(column2X,yPosition);
    		  canAttackScreenLbl.setVisible(true);
    		  canAttackScreenLbl2.setVisible(true);
    	  }else{
    		  canAttackScreenLbl.setVisible(false);
    		  canAttackScreenLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.isCivilian()){
    		  civilianLbl.setText("Civilian:");
    		  civilianLbl2.setText(Functions.getYesNo(sst.isCivilian()));
    		  civilianLbl.setLocation(column1X,newLine());
    		  civilianLbl2.setLocation(column2X,yPosition);
    		  civilianLbl.setVisible(true);
    		  civilianLbl2.setVisible(true);
    	  }else{
    		  civilianLbl.setVisible(false);
    		  civilianLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.isLookAsCivilian()){
    		  lookLikeCivilianLbl.setText("Look like civilian:");
    		  lookLikeCivilianLbl2.setText(Functions.getYesNo(sst.isLookAsCivilian()));
    		  lookLikeCivilianLbl.setLocation(column1X,newLine());
    		  lookLikeCivilianLbl2.setLocation(column2X,yPosition);
    		  lookLikeCivilianLbl.setVisible(true);
    		  lookLikeCivilianLbl2.setVisible(true);
    	  }else{
    		  lookLikeCivilianLbl.setVisible(false);
    		  lookLikeCivilianLbl2.setVisible(false);
    	  }

    	  if(sst.isAlwaysRetreat()){
    		  alwaysRetreatLbl1.setText("Always retreat:");
    		  alwaysRetreatLbl2.setText(Functions.getYesNo(sst.isAlwaysRetreat()));
    		  alwaysRetreatLbl1.setLocation(column1X,newLine());
    		  alwaysRetreatLbl2.setLocation(column2X,yPosition);
    		  alwaysRetreatLbl1.setVisible(true);
    		  alwaysRetreatLbl2.setVisible(true);
    	  }else{
    		  alwaysRetreatLbl1.setVisible(false);
    		  alwaysRetreatLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.isScreened()){
    		  screenedLbl1.setText("Default screened:");
    		  screenedLbl2.setText(Functions.getYesNo(sst.isScreened()));
    		  screenedLbl1.setLocation(column1X,newLine());
    		  screenedLbl2.setLocation(column2X,yPosition);
    		  screenedLbl1.setVisible(true);
    		  screenedLbl2.setVisible(true);
    	  }else{
    		  screenedLbl1.setVisible(false);
    		  screenedLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.getIncEnemyOpenBonus() > 0 || sst.getIncFrendlyOpenBonus() > 0 || sst.getIncNeutralOpenBonus() > 0 || sst.getIncOwnOpenBonus() > 0){
    		  incomeOpenLbl.setText("Income Open (o/f/n/e):");
    		  incomeOpenLbl2.setText(sst.getIncomeOpenString());
    		  incomeOpenLbl.setLocation(column1X,newLine());
    		  incomeOpenLbl2.setLocation(column2X,yPosition);
    		  incomeOpenLbl.setVisible(true);
    		  incomeOpenLbl2.setVisible(true);
    	  }else{
    		  incomeOpenLbl.setVisible(false);
    		  incomeOpenLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.getIncEnemyClosedBonus() > 0 || sst.getIncFrendlyClosedBonus() > 0 || sst.getIncNeutralClosedBonus() > 0 || sst.getIncOwnClosedBonus() > 0){
    		  incomeClosedLbl.setText("Income Closed (o/f/n/e):");
    		  incomeClosedLbl2.setText(sst.getIncomeClosedString());
    		  incomeClosedLbl.setLocation(column1X,newLine());
    		  incomeClosedLbl2.setLocation(column2X,yPosition);
    		  incomeClosedLbl.setVisible(true);
    		  incomeClosedLbl2.setVisible(true);
    	  }else{
    		  incomeClosedLbl.setVisible(false);
    		  incomeClosedLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.isWorldUnique() || sst.isFactionUnique() || sst.isPlayerUnique()){
	        	uniqueGradeLbl.setText("Unique grade: ");
	        	uniqueGradeLbl.setLocation(column1X, newLine());
	        	uniqueGradeLbl.setVisible(true);
		        if(sst.isWorldUnique()){
		        	uniqueGradeLbl2.setText("World");
	        		
	        	}else if(sst.isFactionUnique()){
	        		uniqueGradeLbl2.setText("Faction");
	        		
	        	}else if(sst.isPlayerUnique()){
	        		uniqueGradeLbl2.setText("Player");
	        		
	        	}
		        uniqueGradeLbl2.setLocation(column2X, yPosition);
		        uniqueGradeLbl2.setVisible(true);
		    }else{
	        	uniqueGradeLbl.setVisible(false);
	        	uniqueGradeLbl2.setVisible(false);
	        }
    	  
    	  upkeepLabel.setText("Upkeep: ");
    	  upkeepLabel2.setText(String.valueOf(sst.getUpkeep()));
    	  upkeepLabel.setLocation(column1X,newLine());
    	  upkeepLabel2.setLocation(column2X,yPosition);
    	  
    	  buildCostLabel.setText("Build cost: ");
    	  buildCostLabel2.setText(String.valueOf(sst.getBuildCost(null)));
    	  buildCostLabel.setLocation(column1X,newLine());
    	  buildCostLabel2.setLocation(column2X,yPosition);
    	  
    	  if (filterChoice.getSelectedIndex() == 2){
        	  canbuildlbl.setText("Can build: ");
        	  canbuildlbl2.setText(Functions.getYesNo(sst.isAvailableToBuild()));
        	  canbuildlbl.setLocation(column1X,newLine());
        	  canbuildlbl2.setLocation(column2X,yPosition);
        	  canbuildlbl.setVisible(true);
        	  canbuildlbl2.setVisible(true);
          }else
          if (filterChoice.getSelectedIndex() > 2){
        	  canbuildlbl.setText("Build from start: ");
        	  canbuildlbl2.setText(Functions.getYesNo(sst.isAvailableToBuild()));
        	  canbuildlbl.setLocation(column1X,newLine());
        	  canbuildlbl2.setLocation(column2X,yPosition);
        	  canbuildlbl.setVisible(true);
        	  canbuildlbl2.setVisible(true);
          }else{
        	  canbuildlbl.setVisible(false);
        	  canbuildlbl2.setVisible(false);
          }
      
    	  shiptypeInfoLabel.setText("Description:");
      
    	  //slotsLabel.setText("Slots: ");
		  //slotsLabel2.setText(String.valueOf(sst.getSlots()));
		  
    	  //blackMarketLbl.setText("Black Market:");
		  /*
	      if (sst.isCanAppearOnBlackMarket()){
	          blackMarketLbl2.setText("Yes");
	      }else{
	          blackMarketLbl2.setText("No");
	      }
	      */

    	  // description textarea
    	  shiptypeInfoTextArea.setText(sst.getTotalDescription());
    	  shiptypeInfoTextArea.setVisible(true);
    	  scrollPane.setVisible(true);
    	  
    	  buttonAddShipToCompare.setVisible(true);
      }
      
      
      
    }
    
    private void showSpaceshipTypeToCompare(SpaceshipType sst){
    	
    	yPosition = 10;
    	
    	if (sst != null){
    		typenamelbl3.setText("Name: ");
    		typenamelbl3.setLocation(column3X,yPosition);
    		typenamelbl4.setLocation(column4X,yPosition);
    		typenamelbl4.setText(sst.getName());
    	  
    		shortTypenamelbl3.setText("Short name: ");
    		shortTypenamelbl3.setLocation(column3X,newLine());
    		shortTypenamelbl4.setLocation(column4X,yPosition);
    		shortTypenamelbl4.setText(sst.getShortName());
    	  
    	  shipTypeLbl3.setText("Type: ");
		  shipTypeLbl4.setText(sst.getShipType());
		  shipTypeLbl3.setLocation(column3X,newLine());
		  shipTypeLbl4.setLocation(column4X,yPosition);
		  
		  targetingTypeLbl3.setText("Targeting Type:");
		  targetingTypeLbl4.setText(sst.getTargetingType().toString());
		  targetingTypeLbl3.setLocation(column3X,newLine());
		  targetingTypeLbl4.setLocation(column4X,yPosition);
		  
		  sizelbl3.setText("Size: ");
		  sizelbl4.setText(sst.getSizeString());
		  sizelbl3.setLocation(column3X,newLine());
		  sizelbl4.setLocation(column4X,yPosition);
		  
		  rangelbl3.setText("Range: ");
		  rangelbl4.setText(sst.getRangeString());
		  rangelbl3.setLocation(column3X,newLine());
		  rangelbl4.setLocation(column4X,yPosition);
    	  
    	  if(!sst.isCivilian()){
    		  
    		  damageCapacitylbl3.setText("Damage capacity: ");
	    	  damageCapacitylbl4.setText(String.valueOf(sst.getHits()));
	    	  damageCapacitylbl3.setLocation(column3X,newLine());
	    	  damageCapacitylbl4.setLocation(column4X,yPosition);
	    	  
	    	  shieldslbl3.setText("Shields: ");
	    	  shieldslbl4.setText(String.valueOf(sst.getShields()));
	    	  shieldslbl3.setLocation(column3X,newLine());
	    	  shieldslbl4.setLocation(column4X,yPosition);
	    	  
	    	  weaponsSquadronLbl3.setText("Weapons (squadron): ");
	    	  weaponsSquadronLbl4.setText(String.valueOf(sst.getWeaponsStrengthSquadron()));
	    	  weaponsSquadronLbl3.setLocation(column3X,newLine());
	    	  weaponsSquadronLbl4.setLocation(column4X,yPosition);
	    	  
	    	  weaponsLbl3.setText("Weapons (s/m/l/h): ");
	    	  weaponsLbl4.setText(getWeaponsString(sst));
	    	  weaponsLbl3.setLocation(column3X,newLine());
	    	  weaponsLbl4.setLocation(column4X,yPosition);
	    	  
	    	  armorLbl3.setText("Armor (s/m/l/h):");
	    	  armorLbl4.setText(getArmorString(sst));
	    	  armorLbl3.setLocation(column3X,newLine());
	    	  armorLbl4.setLocation(column4X,yPosition);
	    	  
	    	  if(sst.getPsychWarfare() > 0){
	    		  psychlbl3.setText("PsychWarfare: ");
		    	  psychlbl4.setText(String.valueOf(sst.getPsychWarfare()));
		    	  psychlbl3.setLocation(column3X,newLine());
		    	  psychlbl4.setLocation(column4X,yPosition); 
		    	  psychlbl3.setVisible(true);
	    		  psychlbl4.setVisible(true);
	    	  }else{
	    		  psychlbl3.setVisible(false);
	    		  psychlbl4.setVisible(false);
	    	  }
	    	  
	    	  if(sst.getBombardment() > 0){
	    		  bombardmentlbl3.setText("Bombardment: ");
		    	  bombardmentlbl4.setText(String.valueOf(sst.getBombardment()));
		    	  bombardmentlbl3.setLocation(column3X,newLine());
		    	  bombardmentlbl4.setLocation(column4X,yPosition);
		    	  bombardmentlbl3.setVisible(true);
		    	  bombardmentlbl4.setVisible(true);
	    	  }else{
	    		  bombardmentlbl3.setVisible(false);
		    	  bombardmentlbl4.setVisible(false);
	    	  }
	    	  
	    	  
	    	  damageCapacitylbl3.setVisible(true);
    		  damageCapacitylbl4.setVisible(true);
    		  shieldslbl3.setVisible(true);
    		  shieldslbl4.setVisible(true);
    		  weaponsLbl3.setVisible(true);
    		  weaponsLbl4.setVisible(true);
    		  weaponsSquadronLbl3.setVisible(true);
    		  weaponsSquadronLbl4.setVisible(true);
    		  armorLbl3.setVisible(true);
    		  armorLbl4.setVisible(true);
    		  
    	  
    	  }else{
    		  damageCapacitylbl3.setVisible(false);
    		  damageCapacitylbl4.setVisible(false);
    		  shieldslbl3.setVisible(false);
    		  shieldslbl4.setVisible(false);
    		  weaponsLbl3.setVisible(false);
    		  weaponsLbl4.setVisible(false);
    		  weaponsSquadronLbl3.setVisible(false);
    		  weaponsSquadronLbl4.setVisible(false);
    		  armorLbl3.setVisible(false);
    		  armorLbl4.setVisible(false);
    		  psychlbl3.setVisible(false);
    		  psychlbl4.setVisible(false);
    		  bombardmentlbl3.setVisible(false);
    		  bombardmentlbl4.setVisible(false);
    	  }
    	  
    	  if(sst.getSquadronCapacity() > 0){
    		  squadronCapacityLbl3.setText("Sqd capacity:");
        	  squadronCapacityLbl4.setText(String.valueOf(sst.getSquadronCapacity()));
        	  squadronCapacityLbl3.setLocation(column3X,newLine());
        	  squadronCapacityLbl4.setLocation(column4X,yPosition);
        	  squadronCapacityLbl3.setVisible(true);
        	  squadronCapacityLbl4.setVisible(true);
    	  }else{
    		  squadronCapacityLbl3.setVisible(false);
    		  squadronCapacityLbl4.setVisible(false);
    	  }
    	  
    	  if(sst.getTroopCapacity() > 0){
    		  troopCapLbl3.setText("Troop capacity:");
    		  troopCapLbl4.setText(sst.getTroopCapacity());
    		  troopCapLbl3.setLocation(column3X,newLine());
    		  troopCapLbl4.setLocation(column4X,yPosition);
    		  troopCapLbl3.setVisible(true);
    		  troopCapLbl4.setVisible(true);
    	  }else{
    		  troopCapLbl3.setVisible(false);
    		  troopCapLbl4.setVisible(false);
    	  }
    	  
    	  if(sst.getInitiativeBonus() > 0){
    		  initiativeLabel3.setText("Initiative bonus:");
    		  initiativeLabel4.setText(sst.getInitiativeBonus() + "%");
    		  initiativeLabel3.setLocation(column3X,newLine());
    		  initiativeLabel4.setLocation(column4X,yPosition);
    		  initiativeLabel3.setVisible(true);
    		  initiativeLabel4.setVisible(true);
    	  }else{
    		  initiativeLabel3.setVisible(false);
    		  initiativeLabel4.setVisible(false);
    	  }
    	  
    	  if(sst.getInitSupportBonus() > 0){
    		  initSupportBonusLbl3.setText("Init support bonus:");
    		  initSupportBonusLbl4.setText(sst.getInitSupportBonus() + "%");
    		  initSupportBonusLbl3.setLocation(column3X,newLine());
    		  initSupportBonusLbl4.setLocation(column4X,yPosition);
    		  initSupportBonusLbl3.setVisible(true);
    		  initSupportBonusLbl4.setVisible(true);
    	  }else{
    		  initSupportBonusLbl3.setVisible(false);
    		  initSupportBonusLbl4.setVisible(false);
    	  }
    	  
    	  if(sst.getInitDefence() > 0){
    		  initDefenceBonusLbl3.setText("Init defence:");
    		  initDefenceBonusLbl4.setText(sst.getInitDefence() + "%");
    		  initDefenceBonusLbl3.setLocation(column3X,newLine());
    		  initDefenceBonusLbl4.setLocation(column4X,yPosition);
    		  initDefenceBonusLbl3.setVisible(true);
    		  initDefenceBonusLbl4.setVisible(true);
    	  }else{
    		  initDefenceBonusLbl3.setVisible(false);
    		  initDefenceBonusLbl4.setVisible(false);
    	  }
    	  
    	  if(sst.getNoRetreat()){
    		  retreatLabel3.setText("Stops retreat: ");
    		  retreatLabel4.setText(sst.getNoRetreatString());
    		  retreatLabel3.setLocation(column3X,newLine());
    		  retreatLabel4.setLocation(column4X,yPosition);
    		  retreatLabel3.setVisible(true);
    		  retreatLabel4.setVisible(true);
    	  }else{
    		  retreatLabel3.setVisible(false);
    		  retreatLabel4.setVisible(false);
    	  }
    	  
    	  if(!sst.isVisibleOnMap()){
    		  cloakingLbl3.setText("Cloaking:");
    		  cloakingLbl4.setText(Functions.getYesNo(!sst.isVisibleOnMap()));
    		  cloakingLbl3.setLocation(column3X,newLine());
    		  cloakingLbl4.setLocation(column4X,yPosition);
    		  cloakingLbl3.setVisible(true);
    		  cloakingLbl4.setVisible(true);
    	  }else{
    		  cloakingLbl3.setVisible(false);
    		  cloakingLbl4.setVisible(false);
    	  }
    	  
    	  if(sst.getMaxResupply() > 0){
    		  supplyLabel3.setText("Supply level:");
    		  supplyLabel4.setText(sst.getMaxResupplyString());
    		  supplyLabel3.setLocation(column3X,newLine());
    		  supplyLabel4.setLocation(column4X,yPosition);
    		  supplyLabel3.setVisible(true);
    		  supplyLabel4.setVisible(true);
    	  }else{
    		  supplyLabel3.setVisible(false);
    		  supplyLabel4.setVisible(false);
    	  }
    	  
    	  canBesiegeLbl3.setText("Can besiege:");
    	  canBesiegeLbl4.setText(Functions.getYesNo(sst.isCanBlockPlanet()));
    	  canBesiegeLbl3.setLocation(column3X,newLine());
    	  canBesiegeLbl4.setLocation(column4X,yPosition);
    	  
    	  if(sst.isPlanetarySurvey()){
    		  surveyLbl3.setText("Planetary survey:");
    		  surveyLbl4.setText(Functions.getYesNo(sst.isPlanetarySurvey()));
    		  surveyLbl3.setLocation(column3X,newLine());
    		  surveyLbl4.setLocation(column4X,yPosition);
    		  surveyLbl3.setVisible(true);
    		  surveyLbl4.setVisible(true);
    	  }else{
    		  surveyLbl3.setVisible(false);
    		  surveyLbl4.setVisible(false);
    	  }
    	  
    	  if(sst.isCanAttackScreenedShips()){
    		  canAttackScreenLbl.setText("Can attack screened:");
    		  canAttackScreenLbl2.setText(Functions.getYesNo(sst.isCanAttackScreenedShips()));
    		  canAttackScreenLbl.setLocation(column1X,newLine());
    		  canAttackScreenLbl2.setLocation(column2X,yPosition);
    		  canAttackScreenLbl.setVisible(true);
    		  canAttackScreenLbl2.setVisible(true);
    	  }else{
    		  canAttackScreenLbl.setVisible(false);
    		  canAttackScreenLbl2.setVisible(false);
    	  }
    	  
    	  if(sst.isCivilian()){
    		  civilianLbl3.setText("Civilian:");
    		  civilianLbl4.setText(Functions.getYesNo(sst.isCivilian()));
    		  civilianLbl3.setLocation(column3X,newLine());
    		  civilianLbl4.setLocation(column4X,yPosition);
    		  civilianLbl3.setVisible(true);
    		  civilianLbl4.setVisible(true);
    	  }else{
    		  civilianLbl3.setVisible(false);
    		  civilianLbl4.setVisible(false);
    	  }
    	  
    	  if(sst.isLookAsCivilian()){
    		  lookLikeCivilianLbl3.setText("Look like civilian:");
    		  lookLikeCivilianLbl4.setText(Functions.getYesNo(sst.isLookAsCivilian()));
    		  lookLikeCivilianLbl3.setLocation(column3X,newLine());
    		  lookLikeCivilianLbl4.setLocation(column4X,yPosition);
    		  lookLikeCivilianLbl3.setVisible(true);
    		  lookLikeCivilianLbl4.setVisible(true);
    	  }else{
    		  lookLikeCivilianLbl3.setVisible(false);
    		  lookLikeCivilianLbl4.setVisible(false);
    	  }
    	  
    	  if(sst.getIncEnemyOpenBonus() > 0 || sst.getIncFrendlyOpenBonus() > 0 || sst.getIncNeutralOpenBonus() > 0 || sst.getIncOwnOpenBonus() > 0){
    		  incomeOpenLbl3.setText("Income Open (o/f/n/e):");
    		  incomeOpenLbl4.setText(sst.getIncomeOpenString());
    		  incomeOpenLbl3.setLocation(column3X,newLine());
    		  incomeOpenLbl4.setLocation(column4X,yPosition);
    		  incomeOpenLbl3.setVisible(true);
    		  incomeOpenLbl4.setVisible(true);
    	  }else{
    		  incomeOpenLbl3.setVisible(false);
    		  incomeOpenLbl4.setVisible(false);
    	  }
    	  
    	  if(sst.getIncEnemyClosedBonus() > 0 || sst.getIncFrendlyClosedBonus() > 0 || sst.getIncNeutralClosedBonus() > 0 || sst.getIncOwnClosedBonus() > 0){
    		  incomeClosedLbl3.setText("Income Closed (o/f/n/e):");
    		  incomeClosedLbl4.setText(sst.getIncomeClosedString());
    		  incomeClosedLbl3.setLocation(column3X,newLine());
    		  incomeClosedLbl4.setLocation(column4X,yPosition);
    		  incomeClosedLbl3.setVisible(true);
    		  incomeClosedLbl4.setVisible(true);
    	  }else{
    		  incomeClosedLbl3.setVisible(false);
    		  incomeClosedLbl4.setVisible(false);
    	  }
    	  
    	  if(sst.isWorldUnique() || sst.isFactionUnique() || sst.isPlayerUnique()){
	        	uniqueGradeLbl3.setText("Unique grade: ");
	        	uniqueGradeLbl3.setLocation(column3X, newLine());
	        	uniqueGradeLbl3.setVisible(true);
		        if(sst.isWorldUnique()){
		        	uniqueGradeLbl4.setText("World");
	        		
	        	}else if(sst.isFactionUnique()){
	        		uniqueGradeLbl4.setText("Faction");
	        		
	        	}else if(sst.isPlayerUnique()){
	        		uniqueGradeLbl4.setText("Player");
	        		
	        	}
		        uniqueGradeLbl4.setLocation(column4X, yPosition);
		        uniqueGradeLbl4.setVisible(true);
		    }else{
	        	uniqueGradeLbl3.setVisible(false);
	        	uniqueGradeLbl4.setVisible(false);
	        }
    	  
    	  upkeepLabel3.setText("Supply cost: ");
    	  upkeepLabel4.setText(String.valueOf(sst.getUpkeep()));
    	  upkeepLabel3.setLocation(column3X,newLine());
    	  upkeepLabel4.setLocation(column4X,yPosition);
    	  
    	  buildCostLabel3.setText("Build cost: ");
    	  buildCostLabel4.setText(String.valueOf(sst.getBuildCost(null)));
    	  buildCostLabel3.setLocation(column3X,newLine());
    	  buildCostLabel4.setLocation(column4X,yPosition);
    
      }
      
      
      
    }

    private String getWeaponsString(SpaceshipType sst){
    	StringBuffer sb = new StringBuffer();
        sb.append(sst.getWeaponsStrengthSmall());
        sb.append(" / ");
        if (sst.getWeaponsStrengthMedium() > 0){
        	sb.append(sst.getWeaponsStrengthMedium());
        	if ((sst.getWeaponsMaxSalvoesMedium() > 0) & (sst.getWeaponsMaxSalvoesMedium() < Integer.MAX_VALUE)){
        		sb.append(" (");
        		sb.append(sst.getWeaponsMaxSalvoesMedium());
        		sb.append(")");
        	}
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (sst.getWeaponsStrengthLarge() > 0){
        	sb.append(sst.getWeaponsStrengthLarge());
        	if ((sst.getWeaponsMaxSalvoesLarge() > 0) & (sst.getWeaponsMaxSalvoesLarge() < Integer.MAX_VALUE)){
        		sb.append(" (");
        		sb.append(sst.getWeaponsMaxSalvoesLarge());
        		sb.append(")");
        	}
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (sst.getWeaponsStrengthHuge() > 0){
        	sb.append(sst.getWeaponsStrengthHuge());
        	if ((sst.getWeaponsMaxSalvoesHuge() > 0) & (sst.getWeaponsMaxSalvoesHuge() < Integer.MAX_VALUE)){
        		sb.append(" (");
        		sb.append(sst.getWeaponsMaxSalvoesHuge());
        		sb.append(")");
        	}
        }else{
        	sb.append("-");
        }
    	return sb.toString();
    }

    private String getArmorString(SpaceshipType sst){
    	StringBuffer sb = new StringBuffer();
        if (sst.getArmorSmall() > 0){
            sb.append(sst.getArmorSmall());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (sst.getArmorMedium() > 0){
            sb.append(sst.getArmorMedium());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (sst.getArmorLarge() > 0){
            sb.append(sst.getArmorLarge());
        }else{
        	sb.append("-");
        }
        sb.append(" / ");
        if (sst.getArmorHuge() > 0){
            sb.append(sst.getArmorHuge());
        }else{
        	sb.append("-");
        }
    	return sb.toString();
    }

    public String getId(){
        return id;
    }

    public void updateData(){
    }

	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(buttonAddShipToCompare)){
			showSpaceshipTypeToCompare(spaceShipType);
		}else{
			fillShiptypeList();
		}
		
	}
}
