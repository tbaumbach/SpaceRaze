package sr.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import sr.enums.DiplomacyLevel;
import sr.enums.HighlightType;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.diplomacy.DiplomacyState;
import sr.world.incomeExpensesReports.IncomeType;

//TODO remove @JsonTypeName("Planet") 
public class Planet implements Serializable{
    static final long serialVersionUID = 1L;
    private double x,y,z;
    private String name;
    private int pop, res, basePop;
    private boolean startPlanet,open,besieged = false, posssibleStartplanet = true;
    private Player playerInControl = null, lastKnownPlayerInControl = null;
    private Planet reachFrom;
    private boolean hasNeverSurrendered = true;
    private int rangeToClosestFriendly; // used in Galaxy when computing startplanet location
    private List<Building> buildings = new ArrayList<Building>(); 

    /**
     * Used when reading map data from file
     * @param dataString containing a tab separated list of data: name\tx\ty\tz
     */
    public Planet(String dataString){
    	StringTokenizer st = new StringTokenizer(dataString,"\t");
    	name = st.nextToken();
    	x = Double.parseDouble(st.nextToken());
    	y = Double.parseDouble(st.nextToken());
    	z = Double.parseDouble(st.nextToken());
    	if(st.hasMoreTokens()){
    		posssibleStartplanet = Boolean.valueOf(st.nextToken());
    	}
    	
        pop = 0;
        basePop = 0;
        res = 0;
        startPlanet = false;
        open = false;
    }
    
    public Planet(double x, double y, double z, String name, int pop, int res, boolean startPlanet, boolean posssibleStartplanet){
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        this.pop = pop;
        basePop = pop;
        this.res = res;
        this.startPlanet = startPlanet;
        open = true;
        if (startPlanet){ // spelares planeter startar closed
          open = false;
        }
        this.posssibleStartplanet = posssibleStartplanet;
    }
    
    public Planet clonePlanet(){
    	return new Planet(x,y,z,name,pop,res,startPlanet, posssibleStartplanet);
    }
    
    /**
     * Transform x & y coors for use on map web pages
     */
    public void changeScale(double scaleMod){
    	x = (int)Math.round(x * scaleMod);
    	y = (int)Math.round(y * scaleMod);
    }

    /**
     * Transform x & y coors for use on map web pages
     */
    public void changeScaleDroid(double scaleModX, double scaleModY){
    	x = (int)Math.round(x * scaleModX);
    	y = (int)Math.round(y * scaleModY);
    }

    public void setProd(int newProd){
        pop = newProd;
    }
/*
    public void setBaseProd(int newBaseProd){
        basePop = newBaseProd;
    }
*/
    public void setProd(int newProd,int newBaseProd){
        pop = newProd;
        basePop = newBaseProd;
    }

    public void setHomeplanet(Faction playersFaction){
		open = false;
		hasNeverSurrendered = false;
    	if (playersFaction.isAlien()){
    		setProd(0,7);
    		setRes(7 + playersFaction.getResistanceBonus());
    	}else {
    		setProd(7,7);
    		setRes(3 + playersFaction.getResistanceBonus());
    	}
    }

    public void setRes(int newRes){
      res = newRes;
    }

    public int getMaxPopulation(){
      return basePop*2;
    }

    public Player getPlayerInControl(){
      return playerInControl;
    }

    public void setPlayerInControl(Player p){
      playerInControl = p;
    }

    public boolean isStartPlanet(){
      return startPlanet;
    }

    public void increasePopulation(){
      pop = pop + 1;
    }

    public void increaseResistance(){
      res = res + 1;
    }

    public double getXcoor(){
        return x;
    }

    public double getYcoor(){
        return y;
    }

    public double getZcoor(){
        return z;
    }

    public String getName(){
        return name;
    }

    public int getPopulation(){
        return pop;
    }

    public int getIncome(int openbonus, int closedbonus, TurnInfo playerTurnInfo){
//      LoggingHandler.finer("getIncome " + name + ": " + openbonus + " " + closedbonus);
      int tempIncome = 0;
      if (!besieged){
        if (pop > 0){
          tempIncome = pop;
          if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.PLANET, "Planet production", name, pop);
          if (open){
            tempIncome = tempIncome + 2 + openbonus;
            if ((openbonus + 2) > 0){
            	if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.OPEN_BONUS, "Planet open bonus", name, 2 + openbonus);
            }
          }else{
            tempIncome = tempIncome + closedbonus;
            if (closedbonus > 0){
            	if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.CLOSED_BONUS, "Planet closed bonus", name, closedbonus);
            }
          }
        }
      }
      return tempIncome;
    }

    public int getIncomeAlien(int openbonus, int closedbonus, TurnInfo playerTurnInfo){ 
//    	LoggingHandler.finer("getIncomeAlien " + name + ": " + closedbonus);
    	int tempIncome = 0;
    	if (!besieged){
    		if (res > 0){
    			tempIncome = res;
    			if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.PLANET, "Planet resistance", name, pop);
    			if (open){
    				tempIncome = tempIncome + 2 + openbonus;
    				if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.OPEN_BONUS, "Planet open bonus", name, 2 + openbonus);
    			}else{
    				tempIncome = tempIncome + closedbonus;
    				if (playerTurnInfo != null) playerTurnInfo.addToLatestIncomeReport(IncomeType.CLOSED_BONUS, "Planet closed bonus", name, closedbonus);
    			}
    		}
    	}
    	return tempIncome;
    }

    public int getUpkeep(){
      int tempUpkeep = 0;
      if (playerInControl != null && playerInControl.isAlien()){
    	  tempUpkeep = res;
      }else{
        if (pop > 0){
          tempUpkeep = pop;
        }
      }
      return tempUpkeep;
    }

    public boolean isPlayerPlanet(){
      return playerInControl != null;
    }

    public boolean isFactionPlanet(Faction aFaction){
    	boolean sameFaction = false;
    	if (playerInControl != null){
    		if (playerInControl.getFaction() == aFaction){
    			sameFaction = true;
    		}
    	}
    	return sameFaction;
    }

    
    public boolean isEnemyOrNeutralPlanet(Player player){
    	
    	boolean enemyOrNeutralPlanet = true;
    	
    	if(playerInControl == null){
    		enemyOrNeutralPlanet = true;
    	}else if(player == playerInControl){
    		enemyOrNeutralPlanet = false;
    	}else{
    		DiplomacyState diplomacyState = player.getGalaxy().getDiplomacyState(player, playerInControl);
			if(diplomacyState.getCurrentLevel().isHigher(DiplomacyLevel.PEACE)){
				enemyOrNeutralPlanet = false;
			}
    	}
    	return enemyOrNeutralPlanet;
    }

    
    /*
    public String getPlayerName(){    // anv�nds denna?
        return playerInControl.getName();
    }
	*/
    
    public void setBesieged(boolean value){
    	Logger.finer("setBesieged: " + value);
        besieged = value;
    }

    public boolean isBesieged(){
        return besieged;
    }

    public boolean checkSurrender(Galaxy g){
        return (res + g.getVIPResBonus(this,playerInControl)) < 1;
    }

    // blockad �r som en bel�gring men utan att planeten tar skada eller kan ge sig. Den bara blockeras.
    public void underBlockade(List<TaskForce> alltf){
      setBesieged(true);
      open = false;
      if (canBesiege(alltf)){
    	  if (playerInControl != null){
    		  playerInControl.addToGeneral("The planet " + name + " are being blockad by hostile forces.");
    		  for (int i = 0; i < alltf.size(); i++){
    			  TaskForce temptf = alltf.get(i);
    			  // kolla vilka som vill bel�gra?
    			  temptf.getPlayer().addToGeneral("The planet " + name + ", are being blocked by you and ships from other factions.");
    		  }
    	  }else{
    		  for (int i = 0; i < alltf.size(); i++){
    			  TaskForce temptf = alltf.get(i);
    			  // kolla vilka som vill bel�gra?
    			  temptf.getPlayer().addToGeneral("The neutral planet " + name + " are being blocked by you and ships from other factions.");
    		  }
    	  }
      }
    }
    
    private boolean canBesiege(List<TaskForce> alltf){
    	boolean canBesiege = false;
    	int counter = 0;
    	while ((counter < alltf.size()) & (!canBesiege)){
    		TaskForce tf = alltf.get(counter);
    		if (tf.canBesiege()){
    			canBesiege = true;
    		}else{
    			counter++;
    		}
    	}
    	return canBesiege;
    }

    public void conqueredByTroops(Player conqueringPlayer){
		Logger.finer("conqueredByTroops called");
		res = 1 + conqueringPlayer.getFaction().getResistanceBonus(); // olika typer av spelare f�r olika res p� nyer�vrade planeter
        if (playerInControl != null){
        	playerInControl.getPlanetInfos().setLastKnownOwner(name,conqueringPlayer.getName(),playerInControl.getGalaxy().turn + 1);
        	playerInControl.getPlanetInfos().setLastKnownProdRes(name,pop,res);
        	playerInControl.addToGeneral("The planet " + name + " have no troops and can make no resistance to Governor " + conqueringPlayer.getGovenorName() + " troops.");
        	playerInControl.addToGeneral("The planet " + name + " has surrendered to Governor " + conqueringPlayer.getGovenorName() + ".");
        	playerInControl.addToHighlights(name,HighlightType.TYPE_PLANET_LOST);
        	conqueringPlayer.addToGeneral("The planet " + name + " have no troops and can make no resistance to your troops.");
        	conqueringPlayer.addToGeneral("The planet " + name + ", formerly belonging to Governor " + playerInControl.getGovenorName() + ", has surrendered to you.");
        	conqueringPlayer.addToHighlights(name,HighlightType.TYPE_PLANET_CONQUERED);
        	
        	hasNeverSurrendered = false; // should not be needed?
        	
        	destroyBuildingsThatCanNotBeOverTaked(conqueringPlayer);
      
        }else{
        	conqueringPlayer.addToGeneral("The neutral planet " + name + " have no troops and can make no resistance to your troops.");
        	conqueringPlayer.addToGeneral("The neutral planet " + name + " has surrendered to your troops.");
        	conqueringPlayer.addToHighlights(name,HighlightType.TYPE_PLANET_CONQUERED);
        	if (hasNeverSurrendered){
        		hasNeverSurrendered = false;
        		// l�gg till en slumpvis VIP till denna spelare
        		VIP aVIP = conqueringPlayer.getGalaxy().maybeAddVIP(conqueringPlayer);
        		if (aVIP != null){
        			aVIP.setLocation(this);
        			conqueringPlayer.addToVIPReport("When you conquered " + name + " you have found a " + aVIP.getName() + " who has joined your service.");
        			conqueringPlayer.addToHighlights(aVIP.getName(),HighlightType.TYPE_VIP_JOINS);
        		}
        	}
        }
        
        conqueringPlayer.getGalaxy().checkVIPsOnConqueredPlanet(this,conqueringPlayer);
        
        Logger.finer("Planet, adding space to general");
        if (playerInControl != null){ // det fanns en spelare som kontrollerade planeten (dvs ej neutral)
        	playerInControl.addToGeneral("");
        }
        conqueringPlayer.addToGeneral("");
        // change owner of planet
        playerInControl = conqueringPlayer;
    }
    
    public int getShield(){
    	int biggestShield=0;
    	for(int i=0; i < buildings.size(); i++){
    		if(buildings.get(i).getBuildingType().getShieldCapacity() > biggestShield){
    			biggestShield = buildings.get(i).getBuildingType().getShieldCapacity();
    		}
    	}
    	return biggestShield;
    }

    public int underBombardment(TaskForce bombardingTaskForce){
    	Logger.fine("(Planet.java)  underBombardment  ");
    	int bombardment = bombardingTaskForce.getBombardment();
        int maxBombardment = Integer.MAX_VALUE;
        if (!bombardingTaskForce.getPlayer().isAlien()){
        	maxBombardment = bombardingTaskForce.getPlayer().getPlanetOrderStatuses().getMaxBombardment(name);
        }
        if (bombardment > maxBombardment){
        	bombardment = maxBombardment;
        }
        if (bombardment > 0){
        	Logger.fine("bombardment  " + bombardment);
        	int shield = getShield();
        	if(shield > 0){
        		Logger.fine("shield  " + shield);
        		if(shield >= bombardment){
        			bombardment=0;
        			if (playerInControl != null){
        				playerInControl.addToGeneral("While besieging your planet " + name + " Governor " + bombardingTaskForce.getPlayer().getGovenorName() + " attampt to bombardment your planet but your planet shields stopped his attampt.");
        			}
        			bombardingTaskForce.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your bombardment was stopped by planet defence shields.");
        		}else{
        			bombardment-= shield;
        			if (playerInControl != null){
        				playerInControl.addToGeneral("While besieging your planet " + name + " Governor " + bombardingTaskForce.getPlayer().getGovenorName() + " bombardment your planet, your planet shields reduced the bombardment with " + shield + ".");
        			}
        			bombardingTaskForce.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your bombardment was reduced  with " + shield + " by planet defence shields.");
        		}
        	}
        }
        if (bombardment > 0){
        	Logger.fine("bombardment left after shield  " + bombardment);
        	if (!getInfectedByAlien()){
        		pop = pop - bombardment;
        	}
        	res = res - bombardment;
        	if (playerInControl != null){
        		playerInControl.addToGeneral("While besieging your planet " + name + " Governor " + bombardingTaskForce.getPlayer().getGovenorName() + "'s bombardment have lowered " + name + "'s resistance and population by " + bombardment + ".");
        		bombardingTaskForce.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your bombardment have lowered its resistance and population by " + bombardment + ".");
        	
        		// 10% chans att bomba s�nder en byggnad/bombv�rde.
        		for(int bombardmentIndex = 0; bombardmentIndex < bombardment; bombardmentIndex++){
	            	if(Functions.getD100(10)){// 10% to hit a ground building.
	    				
	    				List<Building> groundBuildings = new ArrayList<Building>();
	    				for(int i = 0; i < buildings.size(); i++){
	    					if(!buildings.get(i).getBuildingType().isInOrbit()){// ground buiding
	    						groundBuildings.add(buildings.get(i));
	    					}
	    				}
	    				if(groundBuildings.size() > 0){
	    					int randomIndex = Functions.getRandomInt(0, groundBuildings.size()-1);
	    					Building destroyedBuilding = groundBuildings.get(randomIndex);
	    					playerInControl.addToGeneral("While besieging your planet " + name + " Governor " + bombardingTaskForce.getPlayer().getGovenorName() + "'s bombardment have destoyed the building " + destroyedBuilding.getBuildingType().getName() + ".");
	    					
	    					
	    					if(bombardingTaskForce.getPlayer().getGalaxy().getTroopsOnPlanet(this, bombardingTaskForce.getPlayer()).size() > 0){
	    						// The attacking player have troops on the planet that can report which typ of building that was destroeyd.
	    						bombardingTaskForce.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your bombardment have destroyed a " + destroyedBuilding.getBuildingType().getName() + " building.");
	    					}else{
	    						// No troops and no report about the destoeyd buiding, just the explosion that tells about a destroyed building.
	    						bombardingTaskForce.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your bombardment have destroyed a building.");
	    					}
	    					removeBuilding(destroyedBuilding.getUniqueId());
	    				}
	    				
	    			}
        		}
        	}else{
        		bombardingTaskForce.getPlayer().addToGeneral("While besieging the neutral planet " + name + " your bombardment have lowered its resistance and population by " + bombardment + ".");
        	}
        	
        	
				
        	
        }
        return bombardment; // return the number of points that were bombarded
    }

    public boolean getInfectedByAlien(){
        boolean infectedByAlien = false;
        if (playerInControl != null){
        	if (playerInControl.isAlien()){
        		infectedByAlien = true;
        	}
        }
        return infectedByAlien;
    }

    public int besieged(TaskForce tf){
    	Galaxy g = tf.getPlayer().getGalaxy();
//    	Player oldPlayerInControl = null;
//        boolean hasFallen = false;
        open = false;
        if (playerInControl != null){
          playerInControl.addToGeneral("The planet " + name + " is under siege by the forces of Governor " + tf.getPlayer().getGovenorName() + ".");
          tf.getPlayer().addToGeneral("You are laying siege to the planet " + name + ", belonging to Governor " + playerInControl.getGovenorName() + ".");
        }else{
          tf.getPlayer().addToGeneral("You are laying siege to the neutral planet " + name + ".");
        }
        int oldRes = res;
//        boolean infectedByAlien = getInfectedByAlien();
        int psychWarfare = g.getMaxPsychWarfare(this,tf.getPlayer());
        if (psychWarfare > 0){
        	// Detta var nog en bugg:  res -= psychWarfare; stog tv� g�nger.
        //  res -= psychWarfare;
          // TaskForce psychWarfare
          Logger.finer("psychWarfare: " + psychWarfare);
          res -= psychWarfare;
          if (playerInControl != null){
        	  playerInControl.addToGeneral("While besieging your planet " + name + " the psych warfare bonus in Governor " + tf.getPlayer().getGovenorName() + " (" + tf.getPlayer().getFaction().getName() + ") fleet have lowered " + name + "'s resistance by " + psychWarfare + ".");
        	  tf.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") the psych warfare bonus of your fleets ships have lowered its resistance by " + psychWarfare + ".");
          }else{
        	  tf.getPlayer().addToGeneral("While besieging the neutral planet " + name + " the psych warfare bonus of the ships in your fleet have lowered its resistance by " + psychWarfare + ".");
          }
          // VIP psychWarfare bonus
          VIP psychWarfareBonusVIP = tf.getPlayer().getGalaxy().getPsychWarfareBonusVIPs(this,tf.getPlayer());
          if (psychWarfareBonusVIP != null){
            res -= psychWarfareBonusVIP.getPsychWarfareBonus();
            if (playerInControl != null){
              playerInControl.addToGeneral("While besieging your planet " + name + " the precence of a " + psychWarfareBonusVIP.getName() + " in Governor " + tf.getPlayer().getGovenorName() + " (" + tf.getPlayer().getFaction().getName() + ") fleet have lowered " + name + "'s resistance by " + psychWarfareBonusVIP.getPsychWarfareBonus() + ".");
              tf.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your " + psychWarfareBonusVIP.getName() + " have lowered its resistance by " + psychWarfareBonusVIP.getPsychWarfareBonus() + ".");
            }else{
              tf.getPlayer().addToGeneral("While besieging the neutral planet " + name + " your " + psychWarfareBonusVIP.getName() + " have lowered its resistance by " + psychWarfareBonusVIP.getPsychWarfareBonus() + ".");
            }
          }
        }
        return oldRes - res;
    }

/*
    public int besieged(TaskForce tf){
    	Galaxy g = tf.getPlayer().getGalaxy();
//    	Player oldPlayerInControl = null;
//        boolean hasFallen = false;
        open = false;
        if (playerInControl != null){
          playerInControl.addToGeneral("The planet " + name + " is under siege by the forces of Governor " + tf.getPlayer().getGovenorName() + ".");
          tf.getPlayer().addToGeneral("You are laying siege to the planet " + name + ", belonging to Governor " + playerInControl.getGovenorName() + ".");
        }else{
          tf.getPlayer().addToGeneral("You are laying siege to the neutral planet " + name + ".");
        }
        int oldRes = res;
//        boolean infectedByAlien = getInfectedByAlien();
    	boolean troops = g.getTroops(this,tf.getPlayer());
        if (troops){
          res = res - 1;
          if (playerInControl != null){
            playerInControl.addToGeneral("While besieging your planet " + name + " " + tf.getPlayer().getFaction().getName() + " Governor " + tf.getPlayer().getGovenorName() + "'s troops have lowered " + name + "'s resistance by one.");
            tf.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your troops have lowered its resistance by one.");
          }else{
            tf.getPlayer().addToGeneral("While besieging the neutral planet " + name + " your troops have lowered its resistance by one.");
          }
          // faction siege bonus
          int siegeBonusFaction = tf.getPlayer().getFaction().getSiegeBonus();
          if (siegeBonusFaction > 0){
            res = res - siegeBonusFaction;
            if (playerInControl != null){
              playerInControl.addToGeneral("While besieging your planet " + name + " Governor " + tf.getPlayer().getGovenorName() + " (" + tf.getPlayer().getFaction().getName() + ") fleet have lowered " + name + "'s resistance by " + siegeBonusFaction + " due to his faction bonus.");
              tf.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your faction bonus have lowered its resistance by " + siegeBonusFaction + ".");
            }else{
              tf.getPlayer().addToGeneral("While besieging the neutral planet " + name + " your faction bonus have lowered its resistance by " + siegeBonusFaction + ".");
            }
          }
          // TaskForce siege bonus
//          int siegeBonusTF = tf.getMaxSiegeBonus();
          int siegeBonusTF = g.getMaxSiegeBonus(this,tf.getPlayer());
          LoggingHandler.finer("siege bonus: " + siegeBonusTF);
          if (siegeBonusTF > 0){
            res = res - siegeBonusTF;
            if (playerInControl != null){
              playerInControl.addToGeneral("While besieging your planet " + name + " the siege bonus in Governor " + tf.getPlayer().getGovenorName() + " (" + tf.getPlayer().getFaction().getName() + ") fleet have lowered " + name + "'s resistance by " + siegeBonusTF + ".");
              tf.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") the siege bonus of your fleets ships have lowered its resistance by " + siegeBonusTF + ".");
            }else{
              tf.getPlayer().addToGeneral("While besieging the neutral planet " + name + " the siege bonus of the ships in your fleet have lowered its resistance by " + siegeBonusTF + ".");
            }
          }
          // VIP siege bonus
//          VIP siegeBonusVIP = tf.getSiegeBonusVIP();
          VIP siegeBonusVIP = tf.getPlayer().getGalaxy().getSiegeBonusVIPs(this,tf.getPlayer());
          if (siegeBonusVIP != null){
            res = res - siegeBonusVIP.getSiegeBonus();
            if (playerInControl != null){
              playerInControl.addToGeneral("While besieging your planet " + name + " the precence of a " + siegeBonusVIP.getName() + " in Governor " + tf.getPlayer().getGovenorName() + " (" + tf.getPlayer().getFaction().getName() + ") fleet have lowered " + name + "'s resistance by " + siegeBonusVIP.getSiegeBonus() + ".");
              tf.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your " + siegeBonusVIP.getName() + " have lowered its resistance by " + siegeBonusVIP.getSiegeBonus() + ".");
            }else{
              tf.getPlayer().addToGeneral("While besieging the neutral planet " + name + " your " + siegeBonusVIP.getName() + " have lowered its resistance by " + siegeBonusVIP.getSiegeBonus() + ".");
            }
          }
        }
        return oldRes - res;
    }
*/    
    
    public void resistanceNotLowered(TaskForce tf){
      	// resistance has not been lowered, write feedback...
        tf.getPlayer().addToGeneral("Your forces has not lowered the resistance of the planet " + name + " in any way.");
        if (playerInControl != null){
            playerInControl.addToGeneral("The emeny forces has not lowered the planets resistance in any way.");
        }
    }
    
/*
    public void underSiege(TaskForce tf){
       
      int resLowered = underBombardment(tf);

      resLowered = resLowered + besieged(tf);

      if (resLowered > 0){
    	  resistanceNotLowered(tf);
      }
      
      boolean infectedByAlien = getInfectedByAlien();
      
      if (((pop < 1) & !infectedByAlien) | ((res < 1) & infectedByAlien)){ // planet razed
    	  razed(tf);
      }else{
        if ((res + tf.getPlayer().getGalaxy().getVIPResBonus(this,playerInControl)) < 1){ // planet surrenders, can never happen to an alien planet
          res = 1 + tf.getPlayer().getFaction().getResistanceBonus(); // olika typer av spelare f�r olika res p� nyer�vrade planeter
          hasFallen = true;
          if (playerInControl != null){
            playerInControl.getPlanetInfos().setLastKnownOwner(name,tf.getPlayer().getName(),playerInControl.getGalaxy().turn + 1);
            playerInControl.getPlanetInfos().setLastKnownProdRes(name,pop,res);
            playerInControl.addToGeneral("The planet " + name + " has surrendered to Governor " + tf.getPlayer().getGovenorName() + ".");
            playerInControl.addToHighlights(name,Highlight.TYPE_PLANET_LOST);
            tf.getPlayer().addToGeneral("The planet " + name + ", formerly belonging to Governor " + playerInControl.getGovenorName() + ", has surrendered to you.");
            tf.getPlayer().addToHighlights(name,Highlight.TYPE_PLANET_CONQUERED);
//            playerInControl.getGalaxy().checkVIPsOnConqueredPlanet(this,playerInControl);
            playerInControl.getGalaxy().checkVIPsOnConqueredPlanet(this,tf.getPlayer());
            hasNeverSurrendered = false;
            if (hasSpaceStation()){
            	tf.getPlayer().addToGeneral("The space station orbiting the planet " + name + " has been destroyed.");
            	playerInControl.addToGeneral("Your space station orbiting the planet " + name + " has been destroyed.");
            	spaceStation = null;
            }
          }else{
            tf.getPlayer().addToGeneral("The neutral planet " + name + " has surrendered to your forces.");
            tf.getPlayer().addToHighlights(name,Highlight.TYPE_PLANET_CONQUERED);
            tf.getPlayer().getGalaxy().checkVIPsOnConqueredPlanet(this,tf.getPlayer());
            if (playerInControl != null){
            	playerInControl.getGalaxy().checkVIPsOnConqueredPlanet(this,tf.getPlayer());
            }
            if (hasNeverSurrendered){
              hasNeverSurrendered = false;
              // l�gg till en slumpvis VIP till denna spelare
              VIP aVIP = tf.getPlayer().getGalaxy().maybeAddVIP(tf.getPlayer());
              if (aVIP != null){
                aVIP.setLocation(this);
                tf.getPlayer().addToVIPReport("When you conquered " + name + " you have found a " + aVIP.getName() + " who has joined your service.");
                tf.getPlayer().addToHighlights(aVIP.getName(),Highlight.TYPE_VIP_JOINS);
              }
            }
          }
          oldPlayerInControl = playerInControl;
          playerInControl = tf.getPlayer();
        if (checkSurrender(tf.getPlayer().getGalaxy())){ // planet surrenders, can never happen to an alien planet
        	planetSurrenders(tf);
        }else{ // planeten bel�grad men har ej gett upp �n
          holding(tf);
        }
      }
*/
      /*
      if (hasFallen | isRazedAndUninfected()){ // planeten har bytt �gare eller �r razad
        if (oldPlayerInControl != null){ // det fanns en spelare som kontrollerade planeten (dvs ej neutral)
          oldPlayerInControl.addToGeneral("");
        }
      }else{ // planeten har ej bytt �gare
        if (playerInControl != null){
          playerInControl.addToGeneral("");
        }
      }
      *//*
      LoggingHandler.finer("Planet, adding space to general");
      tf.getPlayer().addToGeneral("");
    }
*/
    
    public void holding(TaskForce tf){
    	Logger.fine("holding");
        setBesieged(true);
        if (playerInControl != null){
          tf.getPlayer().addToGeneral(name + " have not surrendered yet.");
          playerInControl.addToGeneral(name + " is holding out and has " + (res + playerInControl.getGalaxy().getVIPResBonus(this,playerInControl)) + " left in resistance.");
        }else{
          tf.getPlayer().addToGeneral(name + " have not surrenderad yet.");
        }
    }

    public void unchallengedDefendingTroopsBesieged(TaskForce tf){
    	Logger.fine("unchallengedDefendingTroopsBesieged");
        setBesieged(true);
        tf.getPlayer().addToGeneral("You are besieging " + name + " but it will never surrender as long as it have defending troops left.");
        if (playerInControl != null){
          playerInControl.addToGeneral(name + " is holding out and has " + (res + playerInControl.getGalaxy().getVIPResBonus(this,playerInControl)) + " left in resistance.");
          playerInControl.addToGeneral("Since there are no attacking troops " + name + " will never surrender as long as it have defending troops left.");
        }
    }

    public void besiegedAfterInconclusiveLandbattle(){
    	Logger.fine("besiegedAfterInconclusiveLandbattle");
        setBesieged(true);
    }

    public void planetSurrenders(TaskForce attackingTaskForce){          
    	res = 1 + attackingTaskForce.getPlayer().getFaction().getResistanceBonus(); // olika typer av spelare f�r olika res p� nyer�vrade planeter
    	if (playerInControl != null){
    		playerInControl.getPlanetInfos().setLastKnownOwner(name,attackingTaskForce.getPlayer().getName(),playerInControl.getGalaxy().turn + 1);
    		playerInControl.getPlanetInfos().setLastKnownProdRes(name,pop,res);
    		playerInControl.addToGeneral("The planet " + name + " has surrendered to Governor " + attackingTaskForce.getPlayer().getGovenorName() + ".");
    		playerInControl.addToHighlights(name,HighlightType.TYPE_PLANET_LOST);
    		attackingTaskForce.getPlayer().addToGeneral("The planet " + name + ", formerly belonging to Governor " + playerInControl.getGovenorName() + ", has surrendered to you.");
    		attackingTaskForce.getPlayer().addToHighlights(name,HighlightType.TYPE_PLANET_CONQUERED);
//  		playerInControl.getGalaxy().checkVIPsOnConqueredPlanet(this,playerInControl);
    		playerInControl.getGalaxy().checkVIPsOnConqueredPlanet(this,attackingTaskForce.getPlayer());
    		hasNeverSurrendered = false;
    		
    		destroyBuildingsThatCanNotBeOverTaked(attackingTaskForce.getPlayer());
    	/*	if (hasSpaceStation()){
    			attackingTaskForce.getPlayer().addToGeneral("The space station orbiting the planet " + name + " has been destroyed.");
    			playerInControl.addToGeneral("Your space station orbiting the planet " + name + " has been destroyed.");
    			spaceStation = null;
    		}*/
        }else{
        	attackingTaskForce.getPlayer().addToGeneral("The neutral planet " + name + " has surrendered to your forces.");
        	attackingTaskForce.getPlayer().addToHighlights(name,HighlightType.TYPE_PLANET_CONQUERED);
        	attackingTaskForce.getPlayer().getGalaxy().checkVIPsOnConqueredPlanet(this,attackingTaskForce.getPlayer());
        	
        	
        	if (hasNeverSurrendered){
        		hasNeverSurrendered = false;
        		// l�gg till en slumpvis VIP till denna spelare
        		VIP aVIP = attackingTaskForce.getPlayer().getGalaxy().maybeAddVIP(attackingTaskForce.getPlayer());
        		if (aVIP != null){
        			aVIP.setLocation(this);
        			attackingTaskForce.getPlayer().addToVIPReport("When you conquered " + name + " you have found a " + aVIP.getName() + " who has joined your service.");
        			attackingTaskForce.getPlayer().addToHighlights(aVIP.getName(),HighlightType.TYPE_VIP_JOINS);
        		}
        	}
        }
    	
    	playerInControl = attackingTaskForce.getPlayer();
    }

    /**
     * 
     * @param troops
     * @param bombardment
     * @param siegeBonusTF
     * @param tf
     */
/*
    public void underLimitedSiege(boolean troops, int bombardment, int siegeBonusTF, TaskForce tf){
        Player oldPlayerInControl = null;
        boolean hasFallen = false;
        open = false;
        if (playerInControl != null){
          playerInControl.addToGeneral("The planet " + name + " is under siege by the forces of Governor " + tf.getPlayer().getGovenorName() + ".");
          tf.getPlayer().addToGeneral("You are laying siege to the planet " + name + ", belonging to Governor " + playerInControl.getGovenorName() + ".");
        }else{
          tf.getPlayer().addToGeneral("You are laying siege to the neutral planet " + name + ".");
        }
        int oldRes = res;
        boolean infectedByAlien = getInfectedByAlien();
        if (troops){
          res = res - 1;
          if (playerInControl != null){
            playerInControl.addToGeneral("While besieging your planet " + name + " " + tf.getPlayer().getFaction().getName() + " Governor " + tf.getPlayer().getGovenorName() + "'s troops have lowered " + name + "'s resistance by one.");
            tf.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your troops have lowered its resistance by one.");
          }else{
            tf.getPlayer().addToGeneral("While besieging the neutral planet " + name + " your troops have lowered its resistance by one.");
          }
          // faction siege bonus
          int siegeBonusFaction = tf.getPlayer().getFaction().getSiegeBonus();
          if (siegeBonusFaction > 0){
            res = res - siegeBonusFaction;
            if (playerInControl != null){
              playerInControl.addToGeneral("While besieging your planet " + name + " Governor " + tf.getPlayer().getGovenorName() + " (" + tf.getPlayer().getFaction().getName() + ") fleet have lowered " + name + "'s resistance by " + siegeBonusFaction + " due to his faction bonus.");
              tf.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your faction bonus have lowered its resistance by " + siegeBonusFaction + ".");
            }else{
              tf.getPlayer().addToGeneral("While besieging the neutral planet " + name + " your faction bonus have lowered its resistance by " + siegeBonusFaction + ".");
            }
          }
          // TaskForce siege bonus
//          int siegeBonusTF = tf.getMaxSiegeBonus();
          LoggingHandler.finer("siege bonus: " + siegeBonusTF);
          if (siegeBonusTF > 0){
            res = res - siegeBonusTF;
            if (playerInControl != null){
              playerInControl.addToGeneral("While besieging your planet " + name + " the siege bonus in Governor " + tf.getPlayer().getGovenorName() + " (" + tf.getPlayer().getFaction().getName() + ") fleet have lowered " + name + "'s resistance by " + siegeBonusTF + ".");
              tf.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") the siege bonus of your fleets ships have lowered its resistance by " + siegeBonusTF + ".");
            }else{
              tf.getPlayer().addToGeneral("While besieging the neutral planet " + name + " the siege bonus of the ships in your fleet have lowered its resistance by " + siegeBonusTF + ".");
            }
          }
          // VIP siege bonus
//          VIP siegeBonusVIP = tf.getSiegeBonusVIP();
          VIP siegeBonusVIP = tf.getPlayer().getGalaxy().getSiegeBonusVIPs(this,tf.getPlayer());
          if (siegeBonusVIP != null){
            res = res - siegeBonusVIP.getSiegeBonus();
            if (playerInControl != null){
              playerInControl.addToGeneral("While besieging your planet " + name + " the precence of a " + siegeBonusVIP.getName() + " in Governor " + tf.getPlayer().getGovenorName() + " (" + tf.getPlayer().getFaction().getName() + ") fleet have lowered " + name + "'s resistance by " + siegeBonusVIP.getSiegeBonus() + ".");
              tf.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your " + siegeBonusVIP.getName() + " have lowered its resistance by " + siegeBonusVIP.getSiegeBonus() + ".");
            }else{
              tf.getPlayer().addToGeneral("While besieging the neutral planet " + name + " your " + siegeBonusVIP.getName() + " have lowered its resistance by " + siegeBonusVIP.getSiegeBonus() + ".");
            }
          }
        }
  	  underBombardment(tf);
*/    	
        /*
        int maxBombardment = tf.getPlayer().getPlanetInfos().getMaxBombardment(name);
        if (bombardment > maxBombardment){
          bombardment = maxBombardment;
        }
        if (bombardment > 0){
      	  if (!infectedByAlien){
      		pop = pop - bombardment;
      	}
          res = res - bombardment;
          if (playerInControl != null){
            playerInControl.addToGeneral("While besieging your planet " + name + " Governor " + tf.getPlayer().getGovenorName() + "'s bombardment have lowered " + name + "'s resistance and population by " + bombardment + ".");
            tf.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your bombardment have lowered its resistance and population by " + bombardment + ".");
          }else{
            tf.getPlayer().addToGeneral("While besieging the neutral planet " + name + " your bombardment have lowered its resistance and population by " + bombardment + ".");
          }
        }
          */
/*        if (oldRes == res){
        	// resistance has not been lowered, write feedback...
          tf.getPlayer().addToGeneral("Your forces has not lowered the resistance of the planet " + name + " in any way.");
          if (playerInControl != null){
              playerInControl.addToGeneral("The emeny forces has not lowered the planets resistance in any way.");
          }
        }
        
        if (((pop < 1) & !infectedByAlien) | ((res < 1) & infectedByAlien)){ // planet razed
          if (playerInControl != null){
            // razeade planeter skall visas som neutrala... ingen spelare has ju kontrollen...
            playerInControl.getPlanetInfos().setLastKnownOwner(name,"Neutral",playerInControl.getGalaxy().turn + 1);
            // razed planets does not have any production or resistance
            playerInControl.getPlanetInfos().setLastKnownProdRes(name,-1,-1); 
          }
          tf.getPlayer().getPlanetInfos().setLastKnownOwner(name,"Neutral",tf.getPlayer().getGalaxy().turn + 1);
          // razed planets does not have any production or resistance
          tf.getPlayer().getPlanetInfos().setLastKnownProdRes(name,-1,-1); 
          if (playerInControl != null){
            playerInControl.addToGeneral("The planet " + name + " has been RAZED by Governor " + tf.getPlayer().getGovenorName() + " forces.");
            playerInControl.addToHighlights(name,Highlight.TYPE_OWN_PLANET_RAZED);
            tf.getPlayer().addToGeneral("The planet " + name + ", formerly belonging to " + playerInControl.getGovenorName() + ", has been RAZED by your forces.");
            tf.getPlayer().addToHighlights(name,Highlight.TYPE_ENEMY_PLANET_RAZED);
          }else{
            tf.getPlayer().addToGeneral("The neutral planet " + name + " has been RAZED by your forces.");
          }
          if (playerInControl != null){
            playerInControl.getGalaxy().checkVIPsOnRazedPlanet(this,playerInControl);
          }else{ // check for govs on neutral planets
          	tf.getPlayer().getGalaxy().checkGovenorsOnRazedPlanet(this);
          }
          pop = 0;
          res = 0;
//          spaceStation = null;
          tf.getPlayer().getGalaxy().checkDestroyWharfs(this,tf,true);
          oldPlayerInControl = playerInControl;
          playerInControl = null;
        }else{
          if ((res + tf.getPlayer().getGalaxy().getVIPResBonus(this,playerInControl)) < 1){ // planet surrenders, can never happen to an alien planet
            res = 1 + tf.getPlayer().getFaction().getResistanceBonus(); // olika typer av spelare f�r olika res p� nyer�vrade planeter
            hasFallen = true;
            if (playerInControl != null){
              playerInControl.getPlanetInfos().setLastKnownOwner(name,tf.getPlayer().getName(),playerInControl.getGalaxy().turn + 1);
              playerInControl.getPlanetInfos().setLastKnownProdRes(name,pop,res);
              playerInControl.addToGeneral("The planet " + name + " has surrendered to Governor " + tf.getPlayer().getGovenorName() + ".");
              playerInControl.addToHighlights(name,Highlight.TYPE_PLANET_LOST);
              tf.getPlayer().addToGeneral("The planet " + name + ", formerly belonging to Governor " + playerInControl.getGovenorName() + ", has surrendered to you.");
              tf.getPlayer().addToHighlights(name,Highlight.TYPE_PLANET_CONQUERED);
//              playerInControl.getGalaxy().checkVIPsOnConqueredPlanet(this,playerInControl);
              playerInControl.getGalaxy().checkVIPsOnConqueredPlanet(this,tf.getPlayer());
              hasNeverSurrendered = false;
              if (hasSpaceStation()){
              	tf.getPlayer().addToGeneral("The space station orbiting the planet " + name + " has been destroyed.");
              	playerInControl.addToGeneral("Your space station orbiting the planet " + name + " has been destroyed.");
              	spaceStation = null;
              }
            }else{
              tf.getPlayer().addToGeneral("The neutral planet " + name + " has surrendered to your forces.");
              tf.getPlayer().addToHighlights(name,Highlight.TYPE_PLANET_CONQUERED);
              if (playerInControl != null){
              	playerInControl.getGalaxy().checkVIPsOnConqueredPlanet(this,tf.getPlayer());
              }
              if (hasNeverSurrendered){
                hasNeverSurrendered = false;
                // l�gg till en slumpvis VIP till denna spelare
                VIP aVIP = tf.getPlayer().getGalaxy().maybeAddVIP(tf.getPlayer());
                if (aVIP != null){
                  aVIP.setLocation(this);
                  tf.getPlayer().addToVIPReport("When you conquered " + name + " you have found a " + aVIP.getName() + " who has joined your service.");
                  tf.getPlayer().addToHighlights(aVIP.getName(),Highlight.TYPE_VIP_JOINS);
                }
              }
            }
            oldPlayerInControl = playerInControl;
            playerInControl = tf.getPlayer();
          }else{ // planeten bel�grad men har ej gett upp �n
            besieged = true;
            if (playerInControl != null){
              tf.getPlayer().addToGeneral(name + " have not surrendered yet.");
              playerInControl.addToGeneral(name + " is holding out and has " + (res + playerInControl.getGalaxy().getVIPResBonus(this,playerInControl)) + " left in resistance.");
            }else{
              tf.getPlayer().addToGeneral(name + " have not surrenderad yet.");
            }
          }
        }
        if (hasFallen | isRazedAndUninfected()){ // planeten har bytt �gare eller �r razad
          if (oldPlayerInControl != null){ // det fanns en spelare som kontrollerade planeten (dvs ej neutral)
            oldPlayerInControl.addToGeneral("");
          }
        }else{ // planeten har ej bytt �gare
          if (playerInControl != null){
            playerInControl.addToGeneral("");
          }
        }
        LoggingHandler.finer("Planet, adding space to general");
        tf.getPlayer().addToGeneral("");
      }
*/
    public void razed(Player aPlayer){
		Logger.finer("Planet.razed() called");
		if (playerInControl != null){
        	// razeade planeter skall visas som neutrala... ingen spelare has ju kontrollen...
        	playerInControl.getPlanetInfos().setLastKnownOwner(name,"Neutral",playerInControl.getGalaxy().turn + 1);
        	// razed planets does not have any production or resistance
        	playerInControl.getPlanetInfos().setLastKnownProdRes(name,-1,-1); 
        }
        if (playerInControl != null){
        	playerInControl.addToGeneral("The planet " + name + " has been RAZED by Governor " + aPlayer.getGovenorName() + " forces.");
        	playerInControl.addToHighlights(name,HighlightType.TYPE_OWN_PLANET_RAZED);
        	aPlayer.addToGeneral("The planet " + name + ", formerly belonging to " + playerInControl.getGovenorName() + ", has been RAZED by your forces.");
        	aPlayer.addToHighlights(name,HighlightType.TYPE_ENEMY_PLANET_RAZED);
        }else{
        	aPlayer.addToGeneral("The neutral planet " + name + " has been RAZED by your forces.");
        }
        if (playerInControl != null){
        	playerInControl.getGalaxy().checkVIPsOnRazedPlanet(this,playerInControl);
        }else{ // check for govs on neutral planets
        	aPlayer.getGalaxy().checkGovenorsOnRazedPlanet(this);
        }
        // razed planets does not have any production or resistance
        pop = 0;
        res = 0;
        //attackingTF.getPlayer().getGalaxy().checkDestroyWharfs(this,attackingTF,true);
        aPlayer.getGalaxy().checkDestroyBuildings(this,aPlayer,true);
        if (playerInControl != null){ // det fanns en spelare som kontrollerade planeten (dvs ej neutral)
        	playerInControl.addToGeneral("");
          }
        playerInControl = null;
    }
/*    
    public void underSiegeByAliens(boolean troops, int bombardment, TaskForce tf){
        Player oldPlayerInControl = null;
        boolean hasFallen = false;
        open = false;
        if (playerInControl != null){
          playerInControl.addToGeneral("The planet " + name + " is under siege by the alien forces of Governor " + tf.getPlayer().getGovenorName() + ".");
          tf.getPlayer().addToGeneral("You are laying siege to the planet " + name + ", belonging to Governor " + playerInControl.getGovenorName() + ".");
        }else{
          tf.getPlayer().addToGeneral("You are laying siege to the neutral planet " + name + ".");
        }
        int oldRes = res;
        boolean infectedByAlien = false;
        if (playerInControl != null){
      	  if (playerInControl.isAlien()){
      		  infectedByAlien = true;
      	  }
        }
        if (bombardment > 0){
          if (!infectedByAlien){
        	  pop = pop - bombardment;
          }
          res = res - bombardment;
          if (playerInControl != null){
            playerInControl.addToGeneral("While besieging your planet " + name + " Governor " + tf.getPlayer().getGovenorName() + "'s bombardment have lowered " + name + "'s resistance and population by " + bombardment + ".");
            tf.getPlayer().addToGeneral("While besieging the planet " + name + " belonging to Governor " + playerInControl.getGovenorName() + " (" + playerInControl.getFaction().getName() + ") your bombardment have lowered its resistance and population by " + bombardment + ".");
          }else{
            tf.getPlayer().addToGeneral("While besieging the neutral planet " + name + " your bombardment have lowered its resistance and population by " + bombardment + ".");
          }
        }
        if (oldRes == res){
        	// resistance has not been lowered, write feedback...
          tf.getPlayer().addToGeneral("Your forces has not lowered the resistance of the planet " + name + " in any way.");
          if (playerInControl != null){
              playerInControl.addToGeneral("The emeny forces has not lowered the planets resistance in any way.");
            }
        }
        if (((pop < 1) & !infectedByAlien) | ((res < 1) & infectedByAlien)){ // planet razed and is ripe for infestation
        	razed(tf);
        }else{ // planet not razed yet
            besieged = true;
            if (playerInControl != null){
            	tf.getPlayer().addToGeneral(name + " have not surrendered yet.");
            	playerInControl.addToGeneral(name + " is holding out and has " + (res + playerInControl.getGalaxy().getVIPResBonus(this,playerInControl)) + " left in resistance.");
            }else{
            	tf.getPlayer().addToGeneral(name + " have not surrenderad yet.");
            }
          //}
        }
        if (hasFallen | isRazed()){ // planeten har bytt �gare eller �r razad
          if (oldPlayerInControl != null){ // det fanns en spelare som kontrollerade planeten (dvs ej neutral)
            oldPlayerInControl.addToGeneral("");
          }
        }else{ // planeten har ej bytt �gare
          if (playerInControl != null){
            playerInControl.addToGeneral("");
          }
        }
        LoggingHandler.finer("Planet, adding space to general");
        tf.getPlayer().addToGeneral("");
      }
*/
    public void infectedByAttacker(Player attacker){
    	attacker.addToHighlights(getName(),HighlightType.TYPE_PLANET_INFESTATED);
    	attacker.addToGeneral("You have infected the planet " + getName());
    	setProd(0);
    	setRes(1 + attacker.getFaction().getResistanceBonus());
    	setPlayerInControl(attacker);
    	if (isHasNeverSurrendered()){
    		setHasNeverSurrendered(false);
    		// l�gg till en slumpvis VIP till infestator spelaren
    		VIP aVIP = attacker.getGalaxy().maybeAddVIP(attacker);
    		if (aVIP != null){
    			aVIP.setLocation(this);
    			attacker.addToVIPReport("When you conquered " + getName() + " you have found a " + aVIP.getName() + " who has joined your service.");
    			attacker.addToHighlights(aVIP.getName(),HighlightType.TYPE_VIP_JOINS);
    		}
    	}
    }
    
    public void joinsVisitingDiplomat(VIP tempVIP, boolean addInfoToPlayer){
      res = res + tempVIP.getBoss().getFaction().getResistanceBonus();  // olika typer av spelare f�r olika res p� nyer�vrade planeter?
      if(addInfoToPlayer){
    	  tempVIP.getBoss().addToGeneral("The neutral planet " + name + " has been convinced by your " + tempVIP.getName() + " to join your forces!");
      	tempVIP.getBoss().addToHighlights(name,HighlightType.TYPE_PLANET_JOINS);
      }
      if (hasNeverSurrendered){
        hasNeverSurrendered = false;
        // l�gg till en slumpvis VIP till denna spelare
        VIP aVIP = tempVIP.getBoss().getGalaxy().maybeAddVIP(tempVIP.getBoss());
        if (aVIP != null){
          aVIP.setLocation(this);
          if(addInfoToPlayer){
          	tempVIP.getBoss().addToVIPReport("When you conquered " + name + " you have found a " + aVIP.getName() + " who has joined your service.");
          	tempVIP.getBoss().addToHighlights(aVIP.getName(),HighlightType.TYPE_VIP_JOINS);
          }
        }
      }
      playerInControl = tempVIP.getBoss();
    }

    public void joinsVisitingInfestator(VIP tempInf){
    	pop = 0;
        res = res + tempInf.getBoss().getFaction().getResistanceBonus();
        // destroy all buildings, when an alien conquers a planet it is always razed in the process
        buildings = new ArrayList<Building>();
       // spaceStation = null;
        tempInf.getBoss().addToGeneral("The planet " + name + " has been infected by your " + tempInf.getName() + " to join your forces!");
        tempInf.getBoss().addToHighlights(name,HighlightType.TYPE_PLANET_JOINS);
        if (hasNeverSurrendered){
        	hasNeverSurrendered = false;
        	// l�gg till en slumpvis VIP till denna spelare
        	VIP aVIP = tempInf.getBoss().getGalaxy().maybeAddVIP(tempInf.getBoss());
        	if (aVIP != null){
        		aVIP.setLocation(this);
        		tempInf.getBoss().addToVIPReport("When you conquered " + name + " you have found a " + aVIP.getName() + " who has joined your service.");
        		tempInf.getBoss().addToHighlights(aVIP.getName(),HighlightType.TYPE_VIP_JOINS);
        	}
        }
        if (playerInControl != null){
            playerInControl.addToGeneral("The planet " + name + " has been infected by Governor " + tempInf.getBoss().getGovenorName() + " and is lost!");
            playerInControl.addToHighlights(name,HighlightType.TYPE_OWN_PLANET_INFESTATED);
        }
        playerInControl = tempInf.getBoss();
    }

    public void peace(){
    	setBesieged(false);
    }

    public int getResistance(){
      return res;
    }

    public boolean isRazed(){
      return pop == 0;
    }

    public boolean isRazedAndUninfected(){
    	boolean empty = false;
    	if (pop == 0){
    		if (playerInControl == null){
    			empty = true;
    		}
    	}
        return empty;
      }

    public void reverseVisibility(){
      open = !open;
      if (open){
        lastKnownPlayerInControl = playerInControl;
      }
    }

    public Player getLastKnownPlayerInControl(){
      return lastKnownPlayerInControl;
    }

    public boolean isOpen(){
      return open;
    }

    public void setReachFrom(Planet newReachFrom){
      reachFrom = newReachFrom;
    }

    public Planet getReachFrom(){
      return reachFrom;
    }
    
    public String getSaveString(int index){
		String retStr = "planet" + index + " = ";
		retStr = retStr + name;
		retStr = retStr + "\t" + x;
		retStr = retStr + "\t" + y;
		retStr = retStr + "\t" + z;
		retStr = retStr + "\t" + posssibleStartplanet;
		return retStr;
    }
    
    public void setX(double newX){
    	x = newX;
    }

    public void setY(double newY){
    	y = newY;
    }

    public void setZ(double newZ){
    	z = newZ;
    }

    public void setName(String newName){
    	name = newName;
    }
  
	public int getBasePop(){
		return basePop;
	}
	
	public String toString(){
		return "Planet " + name + " (" + pop + "/" + res + ")";
	}

	public int getRangeToClosestFriendly() {
		return rangeToClosestFriendly;
	}

	public void setRangeToClosestFriendly(int rangeToClosestFriendly) {
		this.rangeToClosestFriendly = rangeToClosestFriendly;
	}
	
	public void setRazed(){
		setProd(0);
		setRes(0);
		if (isOpen()){
			reverseVisibility();
		}
		setBuildings(new ArrayList<Building>());
		setPlayerInControl(null);
	}
	
	public boolean isHasNeverSurrendered(){
		return hasNeverSurrendered;
	}
	
	public void setHasNeverSurrendered(boolean newHasNeverSurrendered){
		hasNeverSurrendered = newHasNeverSurrendered;
	}

	public List<Building> getBuildings() {
		return buildings;
	}
	
	public List<Building> getBuildingsInOrbit() {
		return getBuildings(true);
	}

	public List<Building> getBuildingsOnSurface() {
		return getBuildings(false);
	}

	private List<Building> getBuildings(boolean orbitOnly) {
		List<Building> buildingsInOrbit = new ArrayList<Building>();
		for (Building aBuilding : buildings) {
			if(orbitOnly == aBuilding.getBuildingType().isInOrbit()){
				buildingsInOrbit.add(aBuilding);
			}
		}
		return buildingsInOrbit;
	}

	public List<Building> getBuildingsByVisibility(boolean visible) {
		Logger.finer("visible, " + name + ": " + visible);
		List<Building> buildingsList = new ArrayList<Building>();
		for (Building aBuilding : buildings) {
			Logger.finer("visible, aBuilding, " + name + ": " + aBuilding.getUniqueName());
			Logger.finer("visible, aBuilding.getBuildingType().isVisibleOnMap(), " + name + ": " + aBuilding.getBuildingType().isVisibleOnMap());
			if(visible == aBuilding.getBuildingType().isVisibleOnMap()){
				Logger.finer("visible, aBuilding, " + name + ", ADDING: " + aBuilding.getUniqueName());
				buildingsList.add(aBuilding);
			}
		}
		return buildingsList;
	}

	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}
	
	public void addBuilding(Building building) {
		this.buildings.add(building);
	}
	
	public void removeBuilding(int buildingId) {
		
		Logger.fine("(Planet.java) aBuilding.getUniqueId() " +  buildingId);
		
		int removeIndex = -1;
		for(int i=0; i < buildings.size();i++){
			Logger.fine("(Planet.java) buildings.get(i).getUniqueId() " + buildings.get(i).getUniqueId());
			if(buildings.get(i).getUniqueId() == buildingId){
				removeIndex = i;
			}
		}
		if(removeIndex > -1){
			Logger.fine("(Planet.java) this.buildings.remove(removeIndex) removeIndex  " + removeIndex);
			this.buildings.remove(removeIndex);
		}
	}
	//  method for buildings 
	
	public int getBuildingTechBonus(){
		int bonus=0;
		for(int i=0; i< buildings.size();i++){
			if(buildings.get(i).getBuildingType().getTechBonus() > bonus){
				if(!buildings.get(i).getBuildingType().isInOrbit() || !isBesieged()){
					bonus = buildings.get(i).getBuildingType().getTechBonus();
				}
				
			}
		}
		return bonus;
	}
	
	public boolean hasSpacePort(){
		for(int i=0; i< buildings.size();i++){
			if(buildings.get(i).getBuildingType().isSpaceport()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param conqueringPlayer if no conquering player (null) do not create any messages
	 */
	public void destroyBuildingsThatCanNotBeOverTaked(Player conqueringPlayer){
		int i=0;
		while(i< buildings.size()){
			if(buildings.get(i).getBuildingType().isAutoDestructWhenConquered()){
				if (conqueringPlayer != null){
					conqueringPlayer.addToGeneral("The " + buildings.get(i).getBuildingType().getName() + " on the planet " + name + " has been destroyed.");
					playerInControl.addToGeneral("Your " + buildings.get(i).getBuildingType().getName() + " on the planet " + name + " has been destroyed.");
				}
				buildings.remove(i);
			}else{
				i++;
			}
		}
	}
	
	public int getMaxWharfsSize(){
		int maxSize = 0;
		for(int i=0; i< buildings.size();i++){
			if(buildings.get(i).getBuildingType().getWharfSize() > maxSize){
				maxSize = buildings.get(i).getBuildingType().getWharfSize();
			}
		}
		return maxSize;
		
	}
	
	public int getMaxRepairTonnage(){
		int maxSize = getMaxWharfsSize();
		if(maxSize == 1){
			return 300;
		}else if(maxSize == 2){
			return 600;
		}else if(maxSize == 3){
			return 900;
		}else if(maxSize == 5){
			return 1200;
		}
		
		return 0;
	}
	
	public boolean isPlanetOwner(Player aPlayer){
		if(playerInControl != null && playerInControl == aPlayer){
			return true;
		}
		return false;
	}
	
	public boolean isPlayerBuildingAtPlanet(Player aPlayer){
		boolean BuildingAtPlanet = false;
	    if(isPlanetOwner(aPlayer) && buildings.size() > 0){
	    	BuildingAtPlanet = true;
	    }
	      
	    return BuildingAtPlanet;
	}
	
	public Building getBuilding(int unigueId){
		Building tempBuilding = null;
		for(int i=0; i < buildings.size();i++){
			if(buildings.get(i).getUniqueId() == unigueId){
				tempBuilding = buildings.get(i);
			}
		}
		return tempBuilding;
	}
	
	
	public boolean hasBuilding(String buildingTypeName){
		for(int i = 0; i < buildings.size();i++){
			if(buildings.get(i).getBuildingType().getName().equalsIgnoreCase(buildingTypeName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return battle sim string on max neutral defender on open planet.
	 * @param aGalaxy
	 * @return
	 */
	public String getNeutralSpaceshipsOnOpenPlanetToBattleSim(Galaxy aGalaxy){
		SpaceshipType aType =  null;
		String shipListString = null;
		if(hasNeverSurrendered && playerInControl == null && open){
			List<Spaceship> tempList = aGalaxy.getSpaceships();
			for (int i = 0; i < tempList.size(); i++){
		        Spaceship tempss = tempList.get(i);
		        if ((tempss.getOwner() == null) & (tempss.getLocation() == this)){
		        	aType = tempss.getSpaceshipType();
		        	break;
		        }
			}
			
			if(aType != null){
				if(aType.getSizeString().equals("small")){
					if(pop < 3){
						shipListString ="[2]" + aType.getName();
					}else{
						shipListString ="[4]" + aType.getName();
					}
				}else if(aType.getSizeString().equals("medium")){
					if(pop < 5){
						shipListString ="[2]" + aType.getName();
					}else{
						shipListString ="[4]" + aType.getName();
					}
				}else{
					shipListString ="[3]" + aType.getName();
				}
				
			}
			
		}
		return shipListString;
	}
	
	/**
	 * Return a string on posible neutral defender on open planet.
	 * @param aGalaxy
	 * @return
	 */
	public String getNeutralSpaceshipsOnOpenPlanetString(Galaxy aGalaxy){
		SpaceshipType aType =  null;
		String shipListString = null;
		
		if(hasNeverSurrendered && playerInControl == null && open){
			List<Spaceship> tempList = aGalaxy.getSpaceships();
			for (int i = 0; i < tempList.size(); i++){
		        Spaceship tempss = tempList.get(i);
		        if ((tempss.getOwner() == null) & (tempss.getLocation() == this)){
		        	aType = tempss.getSpaceshipType();
		        	break;
		        }
			}
			if (aType != null){
				Logger.fine("### aType.getName() ### " + aType.getName());
			}else{
				Logger.fine("### aType.getName() ### No ships on planet");
			}
			if(aType != null){
				if(aType.getSizeString().equals("small")){
					if(pop < 3){
						shipListString ="1 - 2 " + aType.getName() + "s in defence";
					}else{
						shipListString ="2 - 4 " + aType.getName()+ "s in defence";
					}
				}else if(aType.getSizeString().equals("medium")){
					if(pop < 5){
						shipListString ="1 - 2 " + aType.getName()+ "s in defence";
					}else{
						shipListString ="2 - 4 " + aType.getName()+ "s in defence";
					}
				}else{
					shipListString ="1 - 3 " + aType.getName()+ "s in defence";
				}
				
			}
			
		}
		return shipListString;
	}

	public boolean isPosssibleStartplanet() {
		return posssibleStartplanet;
	}

	public void setPosssibleStartplanet(boolean posssibleStartplanet) {
		this.posssibleStartplanet = posssibleStartplanet;
	}
	
	public void pruneDroid(){
		buildings.clear();
	}
	
	/* TODO Fixa detta. Ska det vara en JSON eller klassen?
	public PlanetPlayerInfo getPlanetPlayerInfo(){
		
		
		
		return new PlanetPlayerInfo(String name, int x, int y, int z, int pop, int resistence, int Population, );
		
		
		
	}
*/
}