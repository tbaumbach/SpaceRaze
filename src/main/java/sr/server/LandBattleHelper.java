package sr.server;

import spaceraze.battlehandler.landbattle.LandBattle;
import spaceraze.battlehandler.landbattle.TaskForceTroop;
import spaceraze.battlehandler.spacebattle.TaskForceHandler;
import spaceraze.servlethelper.game.DiplomacyPureFunctions;
import spaceraze.servlethelper.game.planet.PlanetMutator;
import spaceraze.servlethelper.game.planet.PlanetOrderStatusPureFunctions;
import spaceraze.servlethelper.game.planet.PlanetPureFunctions;
import spaceraze.servlethelper.game.troop.TroopMutator;
import spaceraze.servlethelper.game.troop.TroopPureFunctions;
import spaceraze.servlethelper.game.vip.VipPureFunctions;
import spaceraze.servlethelper.handlers.GameWorldHandler;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.report.PlanetReport;
import spaceraze.battlehandler.spacebattle.TaskForce;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LandBattleHelper {

    public static void troopFight(Planet aPlanet, Galaxy galaxy){
        if (!PlanetPureFunctions.isRazedAndUninfected(aPlanet)){ // first check that the planet isn't razed and uninhabited. Otherwise there are no siege

            //Get all non planet owner players that have troops on the planet.
            // If more than one attacking = blocking attack on defending troops.
            List<Player> players = getAttackingPlayersWithTroopsOnPlanet(aPlanet, galaxy);
            if(players.size() > 1){

                int index= 0;
                while (players.size() > index +1) {
                    //check i player at index have any hostile troops to fight among the attcking players.
                    Player attacking = players.get(index);
                    int i=index + 1;
                    // if friendly player check next player
                    while(i < players.size() && !DiplomacyPureFunctions.hostileBesiege(attacking, players.get(i), galaxy.getDiplomacyStates())){
                        i++;
                    }
                    // one of the players i hostile.
                    if(i < players.size()){
                        //TODO 2020-01-04 kolla att detta fungerar som det ska, varför används attacking(Player) för att hämta försvarande troops?
                        List<TaskForceTroop> defendingTroops = getPlayerTroopsAndVipsOnPlanet(attacking, aPlanet, galaxy); //g.findTroopsOnPlanet(aPlanet,attacking);
                        List<TaskForceTroop> attackingTroops = getPlayerTroopsAndVipsOnPlanet(players.get(i), aPlanet, galaxy); //g.findTroopsOnPlanet(aPlanet,players.get(i));


                        Logger.finer("perform land battle between " + attacking.getGovernorName() + " and " + players.get(i).getGovernorName());
                        performLandBattle(attacking, defendingTroops, players.get(i), attackingTroops, aPlanet, galaxy);
                        defendingTroops = getPlayerTroopsAndVipsOnPlanet(attacking, aPlanet, galaxy); //g.findTroopsOnPlanet(aPlanet,attacking);
                        attackingTroops = getPlayerTroopsAndVipsOnPlanet(players.get(i), aPlanet, galaxy); //g.findTroopsOnPlanet(aPlanet,players.get(i));

                        //TODO 2020-01-05 Se till att detta läggs in i nya rapporteringen
                        addLandBattleHighlights(defendingTroops.size() > 0,attackingTroops.size() > 0,attacking,players.get(i),aPlanet.getName());
                        // Remove players that have fight this turn.
                        players.remove(i);
                        players.remove(index);
                    }else{
                        // The attacking player don't have any hostile army.
                        index++;
                    }
                }
            }else if (players.size() == 1){
                // Only one attacking player. Check if any defender and perform a battle or change planet owner to attcking player.
                if(galaxy.findTroopsOnPlanet(aPlanet,aPlanet.getPlayerInControl()).size() > 0){// Defening troops perform a battle.
                    Logger.finer("perform land battle aginst defender");
                    Player attacking = players.get(0);
                    // get both defending player/troops and attackning player/troops
                    List<TaskForceTroop> defendingTroops = getPlayerTroopsAndVipsOnPlanet(aPlanet.getPlayerInControl(), aPlanet, galaxy);// g.findTroopsOnPlanet(aPlanet,aPlanet.getPlayerInControl());
                    List<TaskForceTroop> attackingTroops = getPlayerTroopsAndVipsOnPlanet(attacking, aPlanet, galaxy); //g.findTroopsOnPlanet(aPlanet,attacking);
                    LandBattleHelper.performLandBattle(aPlanet.getPlayerInControl(),defendingTroops,attacking,attackingTroops,aPlanet, galaxy);
                    defendingTroops = getPlayerTroopsAndVipsOnPlanet(aPlanet.getPlayerInControl(), aPlanet, galaxy); //g.findTroopsOnPlanet(aPlanet,aPlanet.getPlayerInControl());
                    attackingTroops = getPlayerTroopsAndVipsOnPlanet(attacking, aPlanet, galaxy); //g.findTroopsOnPlanet(aPlanet,attacking);

                    //TODO 2020-01-05 Se till att detta läggs in i nya rapporteringen
                    addLandBattleHighlights(defendingTroops.size() > 0,attackingTroops.size() > 0,aPlanet.getPlayerInControl(),attacking,aPlanet.getName());
                }
            }


            // get all players with troops after the battles.
            players = getAttackingPlayersWithTroopsOnPlanet(aPlanet, galaxy);
            if(TroopPureFunctions.getTroopsOnPlanet(aPlanet,aPlanet.getPlayerInControl(), galaxy.getTroops()).size() == 0){ // Försvarande spelar har inga trupper kvar.
                if(players.size() == 1){// only one attacker and the planet should change owner.
                    if (GameWorldHandler.getFactionByKey(players.get(0).getFactionKey(), galaxy.getGameWorld()).isAlien()){
                        Logger.finer("Attacker is alien");
                        // planet conquered by alien
                        (new PlanetUpdater()).razed(aPlanet, players.get(0));
                        PlanetMutator.infectedByAttacker(aPlanet, players.get(0), galaxy.getGameWorld());
                    }else{ // attacker is not alien
                        Logger.finer("Attacker is not alien");
                        // check if defender is alien
                        if (PlanetPureFunctions.getInfectedByAlien(aPlanet, galaxy)){
                            // planet is razed
                            (new PlanetUpdater()).razed(aPlanet, players.get(0));
                        }else{ // defender is not alien
                            // planet conquered

                            (new PlanetUpdater()).conqueredByTroops(aPlanet, players.get(0), galaxy.getGameWorld());
                            List<TaskForce> taskForces = TaskForceHandler.getTaskForces(aPlanet, false, galaxy);
                            List<TaskForce> countBesiegingTFs = countBesiegingTFs(taskForces, aPlanet, galaxy);
                            if(countBesiegingTFs.size() == 0){
                                aPlanet.setBesieged(false);
                            }else{
                                aPlanet.setBesieged(true);
                            }
                        }
                    }
                }
            }else{
                if(players.size() >= 1){ // at least one player still have troops on the planet.
                    aPlanet.setBesieged(true);
                }
            }

        } // end !aPlanet.isRazedAndUninfected()
    }

    public static List<TaskForceTroop> getPlayerTroopsAndVipsOnPlanet(Player player, Planet planet, Galaxy galaxy){
        return  galaxy.findTroopsOnPlanet(planet, player).stream().map(troop -> new TaskForceTroop(troop, findLandBattleVIPs(troop, galaxy))).collect(Collectors.toList());
    }

    public static List<VIP> findLandBattleVIPs(Troop aTroop, Galaxy galaxy) {
        List<VIP> VIPs = new LinkedList<VIP>();
        for (VIP aVIP : galaxy.getAllVIPs()) {

            if (VipPureFunctions.isLandBattleVip(VipPureFunctions.getVipTypeByKey(aVIP.getTypeKey(), galaxy.getGameWorld())) & (aVIP.getTroopLocation() != null
                    && aVIP.getTroopLocation().getName().equalsIgnoreCase(aTroop.getName()))) {
                VIPs.add(aVIP);
            }
        }
        return VIPs;
    }

    public static void addLandBattleHighlights(boolean defHaveTroops, boolean attHaveTroops, Player defPlayer, Player attPlayer, String planetName){
        if (defHaveTroops){
            if (attHaveTroops){
                // inconclusive battle
                if (defPlayer != null){
                    defPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_INCONCLUSIVE);
                }
                attPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_INCONCLUSIVE);
            }else{
                // defender won
                if (defPlayer != null){
                    defPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_WON);
                }
                attPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_LOST);
            }
        }else{
            if (attHaveTroops){
                // attacker won
                if (defPlayer != null){
                    defPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_LOST);
                }
                attPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_WON);
            }else{
                // both sides destroyed
                if (defPlayer != null){
                    defPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_BOTH_DESTROYED);
                }
                attPlayer.addToHighlights(planetName, HighlightType.TYPE_LANDBATTLE_BOTH_DESTROYED);
            }
        }
    }

    /**
     *
     Artilleri sker också först??? Nix.
     Alla strider kan ge skada åt bägge hållen? Ja.

     För den större styrkan så används reserven för att anfalla motståndare 2-1. Reserven sorteras slumpvis och en efter en allokeras
     trupperna för att anfalla lämpliga motståndare. Detta baseras på anfallsvärden och motståndarens typ (armor/ej armor)
     Trupper som är i strid med 2 motståndare anfaller slumpvis en av dem vid varje attack.
     Flankerande trupper tar strid 1-1 slumpvis om de finns på bägge sidor.
     Om ena sidan har fler flankerare än andra sidan sker något av följande (med de flankers som kommer förbi ev. motståndarens flankers):
     1. Om motståndaren har SLD-trupp(er) så anfalls en av dem.
     2. Om motståndaren saknar SLD:
     2a: Om motståndaren har support troops anfalls slumpvis en av dessa.
     2b: Annars anfalls en first line troop i ryggen. Den som anfalls i ryggen har svårt att skjuta tillbaka. En trupp kan endast anfallas av 1st flanker i ryggen i en strid.
     Rymdskepp som hjälper till läggs till i listan över utslumpade attacker, men de kan inte attackeras (dock skjutas tillbaka på av AA).
     Artilleri läggs också till men får ingen moteld alls.
     Sedan utförs alla strider/attacker i utslumpad ordning. Om en trupp inte längre har en motståndare så gör den inget mer i denna strid.
     För varje nytt drag görs en helt ny uppställning.

     */

    public static void performLandBattle(Player defendingPlayer, List<TaskForceTroop> defendingTroops, Player attackingPlayer, List<TaskForceTroop> attackingTroops, Planet aPlanet, Galaxy galaxy){

        LandBattle battle = new LandBattle(defendingTroops, attackingTroops, aPlanet.getName(), aPlanet.getResistance(), galaxy.getTurn(), galaxy.getGameWorld());
        battle.performBattle();

        if(attackingPlayer!= null) {
            battle.getAttBG().getTroops().stream().map(TaskForceTroop::getTroop).filter(troop -> TroopPureFunctions.isDestroyed(troop)).forEach(troop -> TroopMutator.addToLatestTroopsLostInSpace(troop, attackingPlayer.getTurnInfo(), galaxy.getGameWorld()));
            battle.getDefBG().getTroops().stream().map(TaskForceTroop::getTroop).filter(troop -> TroopPureFunctions.isDestroyed(troop)).forEach(troop -> TroopMutator.addToLatestTroopsLostInSpace(troop, attackingPlayer.getTurnInfo(), galaxy.getGameWorld()));
        }
        if(defendingPlayer != null) {
            battle.getAttBG().getTroops().stream().map(TaskForceTroop::getTroop).filter(troop -> TroopPureFunctions.isDestroyed(troop)).forEach(troop -> TroopMutator.addToLatestTroopsLostInSpace(troop, defendingPlayer.getTurnInfo(), galaxy.getGameWorld()));
            battle.getDefBG().getTroops().stream().map(TaskForceTroop::getTroop).filter(troop -> TroopPureFunctions.isDestroyed(troop)).forEach(troop -> TroopMutator.addToLatestTroopsLostInSpace(troop, defendingPlayer.getTurnInfo(), galaxy.getGameWorld()));
        }

        // Om en VIP var på en troop ska den då dö? eller görs det senare i koden när VIPar gås igenom?
        battle.getAttBG().getTroops().stream().map(TaskForceTroop::getTroop).filter(troop -> TroopPureFunctions.isDestroyed(troop)).forEach(troop -> TroopMutator.removeTroop(troop, galaxy));
        battle.getDefBG().getTroops().stream().map(TaskForceTroop::getTroop).filter(troop -> TroopPureFunctions.isDestroyed(troop)).forEach(troop -> TroopMutator.removeTroop(troop, galaxy));

        addLandBattleReport(attackingPlayer, battle.getAttBG().getReport(), aPlanet, galaxy);
        addLandBattleReport(defendingPlayer, battle.getDefBG().getReport(), aPlanet, galaxy);
    }

    private static void addLandBattleReport(Player player, spaceraze.world.report.landbattle.LandBattleReport landBattleReport, Planet planet, Galaxy galaxy) {
        if(player != null) {
            Optional<PlanetReport> optional = player.getPlayerReports().get(galaxy.getTurn()-1).getChildReportsOfType(PlanetReport.class).stream()
                    .filter(planetReport -> planetReport.getPlanetName().equals(planet.getName())).findAny();
            if(optional.isPresent()) {
                optional.get().getLandBattleReports().add(landBattleReport);
            }else {
                PlanetReport planetReport = new PlanetReport(planet.getName());
                planetReport.getLandBattleReports().add(landBattleReport);
                player.getPlayerReports().get(galaxy.getTurn()-1).getPlanetReports().add(planetReport);

            }
        }
    }

    public static List<TaskForce> countBesiegingTFs(List<TaskForce> taskforces, Planet aPlanet, Galaxy galaxy){
        List<TaskForce> tfsWantinToBesiege = new LinkedList<TaskForce>();
        for (int i = 0; i < taskforces.size(); i++){
            TaskForce temptf = (TaskForce)taskforces.get(i);
            if (hostile(temptf,aPlanet, galaxy) && !PlanetOrderStatusPureFunctions.isDoNotBesiege(aPlanet.getName(), galaxy.getPlayerByGovenorName(temptf.getPlayerName()).getPlanetOrderStatuses())){ // kolla om de är fientligt inställda
                tfsWantinToBesiege.add(temptf);
            }
        }
        return tfsWantinToBesiege;
    }

    public static boolean hostile(TaskForce tf, Planet aPlanet, Galaxy galaxy){
        Logger.finer("hostile (planet): " + aPlanet.getName());
        if (tf.getPlayerName() != null){
            Logger.finer("hostile (governor): " + tf.getPlayerName());
        }else{
            Logger.finer("Taskforce is neutral");
        }
        boolean hostile = false;
        if (tf.getPlayerName() != null && tf.canBesiege()){
            if (aPlanet.getPlayerInControl() == null || !tf.getPlayerName().equalsIgnoreCase(aPlanet.getPlayerInControl().getGovernorName())){
                Logger.finer("Planet does not belong to player: " + aPlanet.getName());
                if (aPlanet.getPlayerInControl() == null){  // kolla om den är neutral
                    Logger.finer("Planet is neutral");
                    hostile = PlanetOrderStatusPureFunctions.isAttackIfNeutral(aPlanet.getName(), galaxy.getPlayerByGovenorName(tf.getPlayerName()).getPlanetOrderStatuses());
                    Logger.finer("Planet is neutral, hostile = " + hostile);
                }else  // kolla om det är fred med planet ägare.
//    				if (aPlanet.getPlayerInControl().getFaction() != tf.getPlayer().getFaction()){
                    if (DiplomacyPureFunctions.hostileBesiege(aPlanet.getPlayerInControl(), galaxy.getPlayerByGovenorName(tf.getPlayerName()), galaxy.getDiplomacyStates())){
                        Logger.finer("Planet belongs to player from another faction");
                        hostile = true;
                        Logger.finer("Hostile = " + hostile);
                    }
            }
        }
        Logger.finer("Hostile finished, returns " + hostile);
        return hostile;
    }

    public static List<Player> getAttackingPlayersWithTroopsOnPlanet(Planet aPlanet, Galaxy galaxy) {
        List<Player> players = new ArrayList<>();
        for (Troop aTroop : galaxy.getTroops()) {
            if (aTroop.getOwner() != null
                    && (aPlanet.getPlayerInControl() == null || (aTroop.getOwner() != aPlanet.getPlayerInControl()
                    && DiplomacyPureFunctions.hostileBesiege(aTroop.getOwner(), aPlanet.getPlayerInControl(), galaxy.getDiplomacyStates())))
                    && aTroop.getPlanetLocation() == aPlanet) {
                if (!players.contains(aTroop.getOwner())) {
                    players.add(aTroop.getOwner());
                }
            }
        }
        return players;
    }
}
