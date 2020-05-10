package spaceraze.server.game.update;

import spaceraze.servlethelper.game.player.PlayerPureFunctions;
import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
import spaceraze.servlethelper.game.troop.TroopMutator;
import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.enums.HighlightType;

import java.util.LinkedList;
import java.util.List;


public class BlackMarketPerformer {

    private static final int shipIndex = 3;
    private static final int hotStuffIndex = 5;
    private static final int vipIndex = 8;
    private static final int bluePrintShipIndex = 9;
    private static final int troopIndex = 10;
    private static final int duelGame = 2;
    private static final int smallGame = 5;

    private BlackMarketPerformer(){}

    public static void newTurn(Galaxy g, BlackMarket  blackMarket){
        int nrOffers;
        int nrPlayers = g.getNrActivePlayers();
        if (nrPlayers == duelGame){
            int tmpRdm = Functions.getRandomInt(1,4);
            if (tmpRdm == 4){ // 25% of 2 offers
                nrOffers = 2;
            }else{ // otherwise 1 offer
                nrOffers = 1;
            }
        }else if (smallGame > 5){ // in big games, can be up to 3 offers
            nrOffers = Functions.getRandomInt(1,3);
        }else{ // otherwise 1-2 offers
            nrOffers = Functions.getRandomInt(1,2);
        }
        for (int i = 0; i < nrOffers; i++){
            BlackMarketOffer tempOffer = BlackMarketPerformer.createBlackMarketOffer(g, blackMarket.getUic().getUniqueId());
            blackMarket.getCurrentOffers().add(tempOffer);
            g.addBlackMarketMessages(null,"New item for sale: a " + tempOffer.getString() + " is for sale at the Black Market.");
        }
    }

    public static BlackMarketOffer createBlackMarketOffer(Galaxy galaxy, int uniqueId) {
        BlackMarketOffer blackMarketOffer = new BlackMarketOffer(uniqueId);
        int randomType = galaxy.hasTroops() ? Functions.getRandomInt(1,12) : Functions.getRandomInt(1,9);
        blackMarketOffer.setLastTurnAction(galaxy.getTurn());
        if (randomType <= shipIndex){ // ship
            blackMarketOffer.setOfferedShiptype(galaxy.getRandomCommonShiptype());
        }else
        if (randomType <= hotStuffIndex){ // hot stuff
            blackMarketOffer.setHotStuffAmount(createHotStuffBid());
        }else
        if (randomType <= vipIndex){ // VIP
            //TODO 2020-05-02 rewrite this.
            boolean canBeUsed = false;
            int tries = 0;
            while (!canBeUsed & (tries < 100)){
                blackMarketOffer.setOfferedVIPType(galaxy.getRandomVIPType());
                tries++;
                canBeUsed = galaxy.vipCanBeUsed(blackMarketOffer.getVIPType());
            }
            if(!canBeUsed){ // if no vip was found use a hot stuff instead
                blackMarketOffer.setOfferedVIPType(null);
                blackMarketOffer.setHotStuffAmount(createHotStuffBid());
            }
        }else
        if (randomType == bluePrintShipIndex){ // shiptype blueprint
            blackMarketOffer.setOfferedShiptypeBlueprint(BlackMarketPerformer.getRandomShipBlueprint(galaxy));
            if (blackMarketOffer.getOfferedShiptypeBlueprint() == null){ // if no shiptype was found use a hot stuff instead
                blackMarketOffer.setHotStuffAmount(createHotStuffBid());
            }
        }else{ //TroopIndex 10-12
            blackMarketOffer.setOfferedTroopType(galaxy.getRandomCommonTroopType());
        }

        return blackMarketOffer;
    }

    private static int createHotStuffBid(){
        return Functions.getRandomInt(1,6) + Functions.getRandomInt(1,6) + Functions.getRandomInt(1,6);
    }

    public static void performBlackMarket(BlackMarket blackMarket, Galaxy galaxy){
        //TODO 2020-04-22 kolla att detta fungerar
        blackMarket.getCurrentOffers().removeIf(blackMarketOffer -> BlackMarketPerformer.performSelling(blackMarketOffer, galaxy) || blackMarketOffer.removeTooOldAndSendMessage(galaxy));

        /*
        for (int i = blackMarket.getCurrentOffers().size()-1; i >= 0; i--){
            BlackMarketOffer tempOffer = blackMarket.getCurrentOffers().get(i);
            boolean sold = tempOffer.performSelling(galaxy);
            if (sold){
                currentOffers.removeElementAt(i);
            }else
            if (tempOffer.tooOld(galaxy)){
                tempOffer.createRemovedOldMessage(galaxy);
                currentOffers.removeElementAt(i);
            }
        }*/
    }

    public static boolean performSelling(BlackMarketOffer blackMarketOffer, Galaxy galaxy){
        boolean sold = false;
        if (blackMarketOffer.getBids().size() > 0){
            Logger.finest( "performSelling: bids.size(): " + blackMarketOffer.getBids().size());
            BlackMarketBid winningBid = blackMarketOffer.getHighestBidder(galaxy);
            if (winningBid == null){ // no-one won the offer
                blackMarketOffer.sendDrawMessages(galaxy);
                // send messages who failed to win this bidding
                blackMarketOffer.sendRefundingMessages(winningBid, galaxy);
                blackMarketOffer.resetBids();
            }else{
                sold = true;
                Planet destinationPlanet = galaxy.getPlanet(winningBid.getDestination());
                Logger.finest( "performSelling: winningBid: " + winningBid.getText() + winningBid.getPlayerName());
                Player winningPlayer = galaxy.getPlayer(winningBid.getPlayerName());
                Logger.finest( "performSelling: winningPlayer: " + winningPlayer.getName());
                winningPlayer.addToLatestBlackMarketMessages("You have won the bidding for a " + blackMarketOffer.getString() + " at the cost of " + winningBid.getCost() + ".");
                if (blackMarketOffer.isHotStuff()){
                    Logger.finest( "performSelling: hotStuff");
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToTreasury(blackMarketOffer.getHotStuffAmount());
                    winningPlayer.addToLatestBlackMarketMessages("The Hot Stuff have given you +" + blackMarketOffer.getHotStuffAmount() + " extra income this turn.");
                    winningPlayer.addToHighlights(String.valueOf(blackMarketOffer.getHotStuffAmount()), HighlightType.TYPE_HOT_STUFF_WON);
                }else
                if (blackMarketOffer.getOfferedVIPType() != null){ // is vip
                    Logger.finest( "performSelling: vip: ");
                    VIP newVIP = blackMarketOffer.getOfferedVIPType().createNewVIP(winningPlayer,destinationPlanet, true);
                    galaxy.allVIPs.add(newVIP);
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToLatestBlackMarketMessages("Your new " + blackMarketOffer.getOfferedVIPType().getTypeName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
                    winningPlayer.addToHighlights(String.valueOf(blackMarketOffer.getOfferedVIPType().getTypeName()),HighlightType.TYPE_VIP_BOUGHT);
                }else
                if (blackMarketOffer.getOfferedShiptype() != null){ // is spaceship
                    Logger.finest("performSelling: ship: ");
                    Spaceship newShip = SpaceshipMutator.createSpaceShip(blackMarketOffer.getOfferedShiptype(), galaxy);
                    newShip.setOwner(winningPlayer);
                    newShip.setLocation(destinationPlanet);
                    galaxy.addSpaceship(newShip);
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToLatestBlackMarketMessages("Your new " + newShip.getName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
                    winningPlayer.addToHighlights(blackMarketOffer.getOfferedShiptype().getName(),HighlightType.TYPE_SHIP_WON);
                }else
                if (blackMarketOffer.getOfferedShiptypeBlueprint() != null){ // is spaceship blueprints
                    Logger.finest("performSelling: shiptype blueprints: ");
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    //SpaceshipType aSST = winningPlayer.findOwnSpaceshipType(offeredShiptypeBlueprint.getName());
                    PlayerSpaceshipImprovement ownPlayerSpaceshipImprovement = PlayerPureFunctions.findSpaceshipImprovement(blackMarketOffer.getOfferedShiptypeBlueprint().getName(), winningPlayer);
                    if (ownPlayerSpaceshipImprovement != null){
                        if (ownPlayerSpaceshipImprovement.isAvailableToBuild()){ // check if the player already have the shiptype
                            winningPlayer.addToLatestBlackMarketMessages("You already could build ships of the type " + blackMarketOffer.getOfferedShiptypeBlueprint().getName() + ".");
                        }else{
                            ownPlayerSpaceshipImprovement.setAvailableToBuild(true);
                            winningPlayer.addToLatestBlackMarketMessages("You can now build ships of the type " + blackMarketOffer.getOfferedShiptypeBlueprint().getName() + ".");
                        }
                        winningPlayer.addToHighlights(blackMarketOffer.getOfferedShiptypeBlueprint().getName(),HighlightType.TYPE_SHIPTYPE_WON);
                    }else{
                        winningPlayer.addSpaceshipImprovement(new PlayerSpaceshipImprovement(blackMarketOffer.getOfferedShiptypeBlueprint().getName(), true));
                        winningPlayer.addToLatestBlackMarketMessages("You can now build ships of the type " + blackMarketOffer.getOfferedShiptypeBlueprint().getName() + ".");
                        winningPlayer.addToHighlights(blackMarketOffer.getOfferedShiptypeBlueprint().getName(),HighlightType.TYPE_SHIPTYPE_WON);
                    }
                }else{ // is troop
                    Logger.finest( "performSelling: troop: " + blackMarketOffer.getOfferedTroopType().getUniqueName());
                    Troop newTroop = TroopMutator.createTroop(winningPlayer.getGalaxy().findTroopType(blackMarketOffer.getOfferedTroopType().getUniqueName()), winningPlayer.getGalaxy());
                    newTroop.setOwner(winningPlayer);
                    newTroop.setPlanetLocation(destinationPlanet);
                    galaxy.addTroop(newTroop);
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToLatestBlackMarketMessages("Your new " + newTroop.getUniqueName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
                    winningPlayer.addToHighlights(blackMarketOffer.getOfferedTroopType().getUniqueName(),HighlightType.TYPE_TROOP_WON);
                }
                // send messages to everyone except the winner
                galaxy.addBlackMarketMessages(winningPlayer,blackMarketOffer.getString() + " sold to Govenor " + winningPlayer.getGovernorName() + " for cost: " + winningBid.getCost() + ".");
                // send messages who failed to win this bidding
                blackMarketOffer.sendRefundingMessages(winningBid, galaxy);
            }
        }else{
            galaxy.addBlackMarketMessages(null,blackMarketOffer.getString() + " not sold - not bids yet.");
        }
        return sold;
    }

    public static SpaceshipType getRandomShipBlueprint(Galaxy galaxy) {
        List<SpaceshipType> possibleShipTypes = new LinkedList<>();
        for (SpaceshipType aSpaceshipType : galaxy.getGameWorld().getShipTypes()) {
            boolean allhaveType = true;
            for (Player aPlayer : galaxy.getActivePlayers()) {
                if (PlayerPureFunctions.findSpaceshipImprovement(aSpaceshipType.getName(), aPlayer) == null) {
                    allhaveType = false;
                }
            }
            if (!allhaveType) {
                if (aSpaceshipType.isBluePrintReadyToUseInBlackMarket(galaxy.getTurn())) {
                    possibleShipTypes.add(aSpaceshipType);
                }
            }
        }

        SpaceshipType randomShiptype = null;
        SpaceshipType tempShipType;

        if (possibleShipTypes.size() > 0) {
            int totalFrequencypoint = 0;
            for (SpaceshipType spaceshipType : possibleShipTypes) {
                totalFrequencypoint += spaceshipType.getBluePrintFrequency().getFrequency();
            }

            int freqValue = Functions.getRandomInt(0, totalFrequencypoint - 1);
            int counter = 0;
            int tmpFreqSum = 0;
            while (randomShiptype == null) {
                tempShipType = possibleShipTypes.get(counter);
                tmpFreqSum = tmpFreqSum + tempShipType.getBluePrintFrequency().getFrequency();
                if (tmpFreqSum > freqValue) {
                    randomShiptype = tempShipType;
                }
                counter++;
            }
        }
        return randomShiptype;
    }
}
