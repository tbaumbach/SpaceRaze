package sr.server;

import spaceraze.servlethelper.game.StatisticsHandler;
import spaceraze.servlethelper.game.player.CostPureFunctions;
import spaceraze.servlethelper.game.player.IncomePureFunctions;
import spaceraze.servlethelper.game.spaceship.SpaceshipPureFunctions;
import spaceraze.util.general.Logger;
import spaceraze.world.*;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class StatisticsUpdater {

    private StatisticsUpdater(){}

    public static void performStatistics(Galaxy galaxy) {
        setStatisticsIncome(galaxy);
        setStatisticsProduction(galaxy);
        setStatisticsVIPs(galaxy);
        setStatisticsShipSize(galaxy);
        setStatisticsShipNumber(galaxy);
        setStatisticsTroopUnits(galaxy);
        setStatisticsPlanetsCount(galaxy);
        setStatisticsShipsKilled(galaxy);
        setStatisticsShipKills(galaxy);
    }

    private static void setStatisticsIncome(Galaxy galaxy) {
        int tempIncome;
        for (Player player : galaxy.getPlayers()) {
            if (!player.isDefeated()) {
                tempIncome = IncomePureFunctions.getPlayerIncome(player, false);
                tempIncome -= CostPureFunctions.getPlayerUpkeepShips(player, galaxy.getPlanets(), galaxy.getSpaceships());
                tempIncome -= CostPureFunctions.getPlayerUpkeepTroops(player, galaxy.getPlanets(), galaxy.getTroops());
                tempIncome -= CostPureFunctions.getPlayerUpkeepVIPs(player, galaxy.getAllVIPs());
                if (tempIncome < 0) { // if broke set net income to 0
                    tempIncome = 0;
                }
                StatisticsHandler.addStatistics(StatisticType.NET_INCOME, player.getName(), tempIncome, false, galaxy);
            } else {
                StatisticsHandler.addStatistics(StatisticType.NET_INCOME, player.getName(), 0, false, galaxy);
            }
        }
    }

    private static void setStatisticsProduction(Galaxy galaxy) {
        // skapa en map för factionernas totala pop
        java.util.Map<String, Integer> factionProductions = new HashMap<>(); // String = faction name
        // nollsätt totalpop för alla factioner
        for (Player aPlayer : galaxy.getPlayers()) {
            aPlayer.setTotalPop(0);
            factionProductions.putIfAbsent(aPlayer.getFaction().getName(), 0);
        }
        int neutralPop = 0; // räkna popen på alla neutrala planeter
        // lägg till factionerna
        // for (Faction aFaction : gw.getFactions()) {
        // Logger.fine(aFaction.getName());
        // factionProductions.put(aFaction.getName(), 0);
        // }
        // räkna popen för alla spelare
        for (Planet planet : galaxy.getPlanets()) {
            if (planet.getPlayerInControl() != null) {
                Faction planetFaction = planet.getPlayerInControl().getFaction();
                if (planet.getPlayerInControl().isAlien()) {
                    planet.getPlayerInControl()
                            .setTotalPop(planet.getPlayerInControl().getTotalPop() + planet.getResistance());
                    factionProductions.put(planetFaction.getName(),
                            factionProductions.get(planetFaction.getName()) + planet.getResistance());
                } else {
                    planet.getPlayerInControl()
                            .setTotalPop(planet.getPlayerInControl().getTotalPop() + planet.getPopulation());
                    factionProductions.put(planetFaction.getName(),
                            factionProductions.get(planetFaction.getName()) + planet.getPopulation());
                    Logger.fine(planetFaction.getName() + " " + factionProductions.get(planetFaction.getName())
                            + planet.getPopulation());
                }
            } else {
                neutralPop = neutralPop + planet.getPopulation();
            }
        }
        // summera all pop i sectorn
        int totalPop = neutralPop;
        for (Player player : galaxy.getPlayers()) {
            totalPop = totalPop + player.getTotalPop();
        }
        // uppdatera player statistiken
        StatisticsHandler.addStatistics(StatisticType.PRODUCTION_PLAYER, "Neutral", neutralPop, false, galaxy);
        StatisticsHandler.addStatistics(StatisticType.PRODUCTION_FACTION, "Neutral", neutralPop, false, galaxy);
        for (Player player : galaxy.getPlayers()) {
            StatisticsHandler.addStatistics(StatisticType.PRODUCTION_PLAYER, player.getName(), player.getTotalPop(), false, galaxy);
        }
        // uppdatera faction statistiken
        Set<String> keys = factionProductions.keySet();
        for (Object aFactionName : keys.toArray()) {
            Faction aFaction = galaxy.getGameWorld().findFaction((String) aFactionName);
            Logger.fine(aFaction.getName());
            //TODO 2020-11-23 Don't use faction name as key, a user can use the same name as faction
            StatisticsHandler.addStatistics(StatisticType.PRODUCTION_FACTION, aFaction.getName(), factionProductions.get(aFactionName), false, galaxy);
        }
    }

    private static void setStatisticsVIPs(Galaxy galaxy) {
        java.util.Map<String, Integer> data = new HashMap<>();
        for (Player aPlayer : galaxy.getPlayers()) {
            data.put(aPlayer.getName(), 0);
        }
        for (VIP aVIP : galaxy.getAllVIPs()) {
            String vipOwner = aVIP.getBoss().getName();
            Integer value = data.get(vipOwner);
            data.put(vipOwner, value + 1);
        }
        for (Player aPlayer : galaxy.getPlayers()) {
            Integer value = data.get(aPlayer.getName());
            StatisticsHandler.addStatistics(StatisticType.VIPS, aPlayer.getName(), value, false, galaxy);
        }
    }

    private static void setStatisticsShipSize(Galaxy galaxy) {
        java.util.Map<String, Integer> dataSize = new HashMap<>();
        for (Player aPlayer : galaxy.getPlayers()) {
            dataSize.put(aPlayer.getName(), 0);
        }
        for (Spaceship aSpaceship : galaxy.getSpaceships()) {
            if (aSpaceship.getOwner() != null) {
                String shipOwner = aSpaceship.getOwner().getName();
                // size
                Integer valueSize = dataSize.get(shipOwner);
                dataSize.put(shipOwner, valueSize + aSpaceship.getType().getSize().getSlots());
            }
        }
        for (Player aPlayer : galaxy.getPlayers()) {
            Integer valueSize = dataSize.get(aPlayer.getName());
            StatisticsHandler.addStatistics(StatisticType.SHIP_SIZE, aPlayer.getName(), valueSize, false, galaxy);
        }
    }

    private static void setStatisticsShipNumber(Galaxy galaxy) {
        java.util.Map<String, Integer> dataNumber = new HashMap<>();
        for (Player aPlayer : galaxy.getPlayers()) {
            dataNumber.put(aPlayer.getName(), 0);
        }
        for (Spaceship aSpaceship : galaxy.getSpaceships()) {
            if (aSpaceship.getOwner() != null) {
                String shipOwner = aSpaceship.getOwner().getName();
                // number
                Integer valueNumber = dataNumber.get(shipOwner);
                dataNumber.put(shipOwner, valueNumber + 1);
            }
        }
        for (Player aPlayer : galaxy.getPlayers()) {
            Integer valueNumber = dataNumber.get(aPlayer.getName());
            StatisticsHandler.addStatistics(StatisticType.SHIP_NUMBER, aPlayer.getName(), valueNumber, false, galaxy);
        }
    }

    private static void setStatisticsTroopUnits(Galaxy galaxy) {
        java.util.Map<String, Integer> dataNumber = new HashMap<>();
        for (Player aPlayer : galaxy.getPlayers()) {
            dataNumber.put(aPlayer.getName(), 0);
        }
        for (Troop aTroop : galaxy.getTroops()) {
            if (aTroop.getOwner() != null) {
                String troopOwner = aTroop.getOwner().getName();
                // number
                Integer valueNumber = dataNumber.get(troopOwner);
                dataNumber.put(troopOwner, valueNumber + 1);
            }
        }
        for (Player aPlayer : galaxy.getPlayers()) {
            Integer valueNumber = dataNumber.get(aPlayer.getName());
            StatisticsHandler.addStatistics(StatisticType.TROOPS_NUMBER, aPlayer.getName(), valueNumber, false, galaxy);
        }
    }

    private static void setStatisticsPlanetsCount(Galaxy galaxy) {
        java.util.Map<String, Integer> dataNumber = new HashMap<>();
        for (Player aPlayer : galaxy.getPlayers()) {
            dataNumber.put(aPlayer.getName(), 0);
        }
        for (Planet aPlanet : galaxy.getPlanets()) {
            if (aPlanet.getPlayerInControl() != null) {
                String planetOwner = aPlanet.getPlayerInControl().getName();
                // number
                Integer valueNumber = dataNumber.get(planetOwner);
                dataNumber.put(planetOwner, valueNumber + 1);
            }
        }
        for (Player aPlayer : galaxy.getPlayers()) {
            Integer valueNumber = dataNumber.get(aPlayer.getName());
            StatisticsHandler.addStatistics(StatisticType.PLANETS, aPlayer.getName(), valueNumber, false, galaxy);
        }
    }

    private static void setStatisticsShipsKilled(Galaxy galaxy) {
        for (Player aPlayer : galaxy.getPlayers()) {
            Report lastReport = aPlayer.getTurnInfo().getLatestGeneralReport();
            String factionName = aPlayer.getFaction().getName();
            List<CanBeLostInSpace> lisOwn = SpaceshipPureFunctions.getShipsLostInSpace(galaxy, lastReport.getLostShips(), factionName, true); // egna förlorade
            // skepp
            StatisticsHandler.addStatistics(StatisticType.SHIPS_LOST, aPlayer.getName(), lisOwn.size(), true, galaxy);
            List<CanBeLostInSpace> lisOther = SpaceshipPureFunctions.getShipsLostInSpace(galaxy, lastReport.getLostShips(), factionName, false);
            StatisticsHandler.addStatistics(StatisticType.SHIPS_KILLED, aPlayer.getName(), lisOther.size(), true, galaxy);
        }
    }


    private static void setStatisticsShipKills(Galaxy galaxy) {
        java.util.Map<String, Integer> dataNumber = new HashMap<>();
        for (Player aPlayer : galaxy.getPlayers()) {
            dataNumber.put(aPlayer.getName(), 0);
        }
        for (Spaceship aSpaceship : galaxy.getSpaceships()) {
            if (aSpaceship.getOwner() != null) {
                String shipOwner = aSpaceship.getOwner().getName();
                // number
                Integer valueNumber = dataNumber.get(shipOwner);
                if (aSpaceship.getKills() > valueNumber) {
                    dataNumber.put(shipOwner, aSpaceship.getKills());
                }
            }
        }
        for (Player aPlayer : galaxy.getPlayers()) {
            Integer valueNumber = dataNumber.get(aPlayer.getName());
            StatisticsHandler.addStatistics(StatisticType.SHIPS_MOST_KILLS, aPlayer.getName(), valueNumber, false, galaxy);
        }
    }
}
