package spaceraze.server.game.update;

import spaceraze.servlethelper.game.BlackMarketPureFunctions;
import spaceraze.servlethelper.game.UniqueIdHandler;
import spaceraze.servlethelper.game.player.PlayerPureFunctions;
import spaceraze.servlethelper.game.spaceship.SpaceshipMutator;
import spaceraze.servlethelper.game.spaceship.SpaceshipPureFunctions;
import spaceraze.servlethelper.game.troop.TroopMutator;
import spaceraze.servlethelper.game.troop.TroopPureFunctions;
import spaceraze.servlethelper.game.vip.VipMutator;
import spaceraze.servlethelper.game.vip.VipPureFunctions;
import spaceraze.servlethelper.handlers.GameWorldHandler;
import spaceraze.util.general.Functions;
import spaceraze.util.general.Logger;
import spaceraze.world.*;
import spaceraze.world.enums.BlackMarketFrequency;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.enums.SpaceShipSize;

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

    public static void newTurn(Galaxy g){
        int nrOffers;
        int nrPlayers = PlayerPureFunctions.getActivePlayers(g).size();
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
            BlackMarketOffer tempOffer = BlackMarketPerformer.createBlackMarketOffer(g, UniqueIdHandler.getUniqueIdCounter(g, CounterType.BLACK_MARKET).getUniqueId());
            g.getCurrentOffers().add(tempOffer);
            g.addBlackMarketMessages(null,"New item for sale: a " + tempOffer.getString() + " is for sale at the Black Market.");
        }
    }

    public static BlackMarketOffer createBlackMarketOffer(Galaxy galaxy, int uniqueId) {
        BlackMarketOffer blackMarketOffer = new BlackMarketOffer(uniqueId);
        int randomType = galaxy.hasTroops() ? Functions.getRandomInt(1,12) : Functions.getRandomInt(1,9);
        blackMarketOffer.setLastTurnAction(galaxy.getTurn());
        if (randomType <= shipIndex){ // ship
            blackMarketOffer.setSpaceshipType(getRandomCommonSpaceshipType(galaxy));
        }else
        if (randomType <= hotStuffIndex){ // hot stuff
            blackMarketOffer.setHotStuffAmount(createHotStuffBid());
        }else
        if (randomType <= vipIndex){ // VIP
            //TODO 2020-05-02 rewrite this.
            boolean canBeUsed = false;
            int tries = 0;
            while (!canBeUsed & (tries < 100)){
                blackMarketOffer.setVipType(VipPureFunctions.getRandomVIPType(galaxy));
                tries++;
                canBeUsed = vipCanBeUsed(blackMarketOffer.getVIPType(), galaxy);
            }
            if(!canBeUsed){ // if no vip was found use a hot stuff instead
                blackMarketOffer.setVipType(null);
                blackMarketOffer.setHotStuffAmount(createHotStuffBid());
            }
        }else
        if (randomType == bluePrintShipIndex){ // shiptype blueprint
            blackMarketOffer.setBlueprint(BlackMarketPerformer.getRandomShipBlueprint(galaxy));
            if (blackMarketOffer.getBlueprint() == null){ // if no shiptype was found use a hot stuff instead
                blackMarketOffer.setHotStuffAmount(createHotStuffBid());
            }
        }else{ //TroopIndex 10-12
            blackMarketOffer.setTroopType(getRandomCommonTroopType(galaxy));
        }

        return blackMarketOffer;
    }

    public static boolean vipCanBeUsed(VIPType aVIPType, Galaxy galaxy) {
        boolean found = false;
        int index = 0;
        List<Player> activePlayers = PlayerPureFunctions.getActivePlayers(galaxy);
        while (!found & (index < activePlayers.size())) {
            Player aPlayer = activePlayers.get(index);
            if (GameWorldHandler.getFactionByKey(aPlayer.getFactionKey(), galaxy.getGameWorld()).getAlignment().canHaveVip(aVIPType.getAlignment().getName())) {
                found = true;
            } else {
                index++;
            }
        }
        return found;
    }

    private static TroopType getRandomCommonTroopType(Galaxy galaxy) {
        TroopType aTroopType = null;
        TroopType tempTroopType = null;
        List<TroopType> allAvailableTroopTypes = getTroopTypesToBlackMarket(galaxy);
        int totalFrequencypoint = 0;
        for (TroopType troopType : allAvailableTroopTypes) {
            totalFrequencypoint += troopType.getBlackMarketFrequency().getFrequency();
        }

        int freqValue = Functions.getRandomInt(0, totalFrequencypoint - 1);
        int counter = 0;
        int tmpFreqSum = 0;
        while (aTroopType == null) {
            tempTroopType = allAvailableTroopTypes.get(counter);
            tmpFreqSum = tmpFreqSum + tempTroopType.getBlackMarketFrequency().getFrequency();
            if (tmpFreqSum > freqValue) {
                aTroopType = tempTroopType;
            }
            counter++;
        }

        return aTroopType;
    }

    /**
     * Searches through all trooptype lists for all factions
     *
     * @return list containing trooptypes. If a trooptype can be build by several
     *         factions it will appear several times in the list
     */
    private static List<TroopType> getTroopTypesToBlackMarket(Galaxy galaxy) {
        Logger.fine("getTroopTypesToBlackMarket() called");
        List<TroopType> troopTypes = new LinkedList<TroopType>();
        for (Faction aFaction : galaxy.getGameWorld().getFactions()) {
            Logger.finer("Faction: " + aFaction.getName());
            List<TroopType> factionTroopTypes = aFaction.getTroopTypes();
            Logger.finer("TroopTypes #: " + factionTroopTypes.size());
            for (TroopType aTroopType : factionTroopTypes) {
                Logger.finer("TT: " + aTroopType.getName());

                if (isReadyToUseInBlackMarket(galaxy, aTroopType)) {
                    if (!troopTypes.contains(aTroopType)) {
                        troopTypes.add(aTroopType);
                    }
                }
            }
        }
        return troopTypes;
    }

    public static boolean isReadyToUseInBlackMarket(Galaxy aGalaxy, TroopType troopType){
        boolean isConstructable =  false;
        if(aGalaxy.getTurn() >= troopType.getBlackmarketFirstTurn()){
            if (troopType.isSpaceshipTravel()){
                if (troopType.isCanAppearOnBlackMarket()){
                    if(!troopType.isPlayerUnique() && !troopType.isFactionUnique()){
                        if(troopType.isWorldUnique() && !TroopPureFunctions.isWorldUniqueBuild(aGalaxy, troopType)){
                            boolean isAlreadyAoffer = false;
                            for (BlackMarketOffer aBlackMarketOffer : aGalaxy.getCurrentOffers()) {
                                if(aBlackMarketOffer.isTroop() && aBlackMarketOffer.getTroopType().getName().equals(troopType.getName())){
                                    isAlreadyAoffer = true;
                                }
                            }

                            if(!isAlreadyAoffer){
                                boolean haveBuildingOrder = false;
                                for (Player tempPlayer : aGalaxy.getPlayers()) {
                                    if(tempPlayer.getOrders().haveTroopTypeBuildOrder(troopType)){
                                        haveBuildingOrder = true;
                                    }
                                }
                                if(!haveBuildingOrder){
                                    isConstructable =  true;
                                }
                            }
                        }else{
                            isConstructable = true;
                        }
                    }
                }
            }
        }
        return isConstructable;
    }

    private static int createHotStuffBid(){
        return Functions.getRandomInt(1,6) + Functions.getRandomInt(1,6) + Functions.getRandomInt(1,6);
    }

    public static void performBlackMarket(Galaxy galaxy){
        galaxy.getCurrentOffers().removeIf(blackMarketOffer -> BlackMarketPerformer.performSelling(blackMarketOffer, galaxy) || blackMarketOffer.removeTooOldAndSendMessage(galaxy));
    }

    public static boolean performSelling(BlackMarketOffer blackMarketOffer, Galaxy galaxy){
        boolean sold = false;
        if (blackMarketOffer.getBlackMarketBids().size() > 0){
            Logger.finest( "performSelling: bids.size(): " + blackMarketOffer.getBlackMarketBids().size());
            BlackMarketBid winningBid = blackMarketOffer.getHighestBidder(galaxy);
            if (winningBid == null){ // no-one won the offer
                blackMarketOffer.sendDrawMessages(galaxy);
                // send messages who failed to win this bidding
                blackMarketOffer.sendRefundingMessages(winningBid, galaxy);
                blackMarketOffer.resetBids();
            }else{
                sold = true;
                Planet destinationPlanet = galaxy.getPlanet(winningBid.getDestination());
                Logger.finest( "performSelling: winningBid: " + BlackMarketBid.getBiddingText(blackMarketOffer, winningBid) + winningBid.getPlayerName());
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
                if (blackMarketOffer.getVipType() != null){ // is vip
                    Logger.finest( "performSelling: vip: ");
                    VIP newVIP = VipMutator.createNewVIP(blackMarketOffer.getVipType(), winningPlayer,destinationPlanet, true);
                    galaxy.allVIPs.add(newVIP);
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToLatestBlackMarketMessages("Your new " + blackMarketOffer.getVipType().getTypeName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
                    winningPlayer.addToHighlights(String.valueOf(blackMarketOffer.getVipType().getTypeName()),HighlightType.TYPE_VIP_BOUGHT);
                }else
                if (blackMarketOffer.getSpaceshipType() != null){ // is spaceship
                    Logger.finest("performSelling: ship: ");
                    Spaceship newShip = SpaceshipMutator.createSpaceShip(blackMarketOffer.getSpaceshipType());
                    newShip.setOwner(winningPlayer);
                    newShip.setLocation(destinationPlanet);
                    galaxy.addSpaceship(newShip);
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToLatestBlackMarketMessages("Your new " + newShip.getName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
                    winningPlayer.addToHighlights(blackMarketOffer.getSpaceshipType().getName(),HighlightType.TYPE_SHIP_WON);
                }else
                if (blackMarketOffer.getBlueprint() != null){ // is spaceship blueprints
                    Logger.finest("performSelling: shiptype blueprints: ");
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    //SpaceshipType aSST = winningPlayer.findOwnSpaceshipType(offeredShiptypeBlueprint.getName());
                    PlayerSpaceshipImprovement ownPlayerSpaceshipImprovement = PlayerPureFunctions.findSpaceshipImprovement(blackMarketOffer.getBlueprint().getName(), winningPlayer);
                    if (ownPlayerSpaceshipImprovement != null){
                        if (ownPlayerSpaceshipImprovement.isAvailableToBuild()){ // check if the player already have the shiptype
                            winningPlayer.addToLatestBlackMarketMessages("You already could build ships of the type " + blackMarketOffer.getBlueprint().getName() + ".");
                        }else{
                            ownPlayerSpaceshipImprovement.setAvailableToBuild(true);
                            winningPlayer.addToLatestBlackMarketMessages("You can now build ships of the type " + blackMarketOffer.getBlueprint().getName() + ".");
                        }
                        winningPlayer.addToHighlights(blackMarketOffer.getBlueprint().getName(),HighlightType.TYPE_SHIPTYPE_WON);
                    }else{
                        winningPlayer.addSpaceshipImprovement(new PlayerSpaceshipImprovement(blackMarketOffer.getBlueprint().getName(), true));
                        winningPlayer.addToLatestBlackMarketMessages("You can now build ships of the type " + blackMarketOffer.getBlueprint().getName() + ".");
                        winningPlayer.addToHighlights(blackMarketOffer.getBlueprint().getName(),HighlightType.TYPE_SHIPTYPE_WON);
                    }
                }else{ // is troop
                    Logger.finest( "performSelling: troop: " + blackMarketOffer.getTroopType().getName());
                    Troop newTroop = TroopMutator.createTroop(winningPlayer.getGalaxy().findTroopType(blackMarketOffer.getTroopType().getName()), winningPlayer.getGalaxy());
                    newTroop.setOwner(winningPlayer);
                    newTroop.setPlanetLocation(destinationPlanet);
                    galaxy.addTroop(newTroop);
                    winningPlayer.removeFromTreasury(winningBid.getCost());
                    winningPlayer.addToLatestBlackMarketMessages("Your new " + newTroop.getName() + " is awaiting your orders at " + winningBid.getDestination() + ".");
                    winningPlayer.addToHighlights(blackMarketOffer.getTroopType().getName(),HighlightType.TYPE_TROOP_WON);
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
            for (Player aPlayer : PlayerPureFunctions.getActivePlayers(galaxy)) {
                if (PlayerPureFunctions.findSpaceshipImprovement(aSpaceshipType.getName(), aPlayer) == null) {
                    allhaveType = false;
                }
            }
            if (!allhaveType) {
                if (isBluePrintReadyToUseInBlackMarket(aSpaceshipType, galaxy.getTurn())) {
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

    public static void addBlackMarketBid(BlackMarketBid aBid, Galaxy galaxy){
        Logger.finer("BlackMarketBid: " + aBid.getOfferUniqueId());
        BlackMarketOffer aOffer = BlackMarketPureFunctions.findBlackMarketOffer(aBid.getOfferUniqueId(), galaxy);
        if(aOffer != null){
            aOffer.addBid(aBid);
        }

    }

    public static  SpaceshipType getRandomCommonSpaceshipType(Galaxy galaxy) {
        SpaceshipType returnType = null;
        SpaceshipType tempShipType = null;
        List<SpaceshipType> allAvailableTypes = getSpaceshipTypesToBlackMarket(galaxy);
        int totalFrequencypoint = 0;
        for (SpaceshipType spaceshipType : allAvailableTypes) {
            totalFrequencypoint += spaceshipType.getBlackMarketFrequency().getFrequency();
        }

        int freqValue = Functions.getRandomInt(0, totalFrequencypoint - 1);
        int counter = 0;
        int tmpFreqSum = 0;
        while (returnType == null) {
            tempShipType = allAvailableTypes.get(counter);
            tmpFreqSum = tmpFreqSum + tempShipType.getBlackMarketFrequency().getFrequency();
            if (tmpFreqSum > freqValue) {
                returnType = tempShipType;
            }
            counter++;
        }
        return returnType;
    }

    /**
     * Searches through all sst lists for all factions, limited by the turn number
     * (no medium+ on turn 1 etc)
     *
     * @return
     */
    private static List<SpaceshipType> getSpaceshipTypesToBlackMarket(Galaxy galaxy) {
        Logger.fine("getSpaceshipTypesToBlackMarket() called");
        List<SpaceshipType> ssTypes = new LinkedList<SpaceshipType>();
        // LoggingHandler.fine("Faction: " + aFaction.getName(),this);
        // LoggingHandler.fine("Ships nr: " + tmpSsTypes.size(),this);
        for (SpaceshipType spaceshipType : galaxy.getGameWorld().getShipTypes()) {
            if (isReadyToUseInBlackMarket(spaceshipType, galaxy)) {
                ssTypes.add(spaceshipType);
            }
        }
        return ssTypes;
    }

    private static boolean isReadyToUseInBlackMarket(SpaceshipType spaceshipType, Galaxy aGalaxy){
        boolean constructible =  false;

        if (aGalaxy.getTurn() >= spaceshipType.getBlackmarketFirstTurn()){
            if (spaceshipType.getRange().canMove() || spaceshipType.getSize() == SpaceShipSize.SQUADRON){
                if (spaceshipType.isCanAppearOnBlackMarket()){
                    if(!spaceshipType.isPlayerUnique() && !spaceshipType.isFactionUnique()){
                        if(spaceshipType.isWorldUnique() && !SpaceshipPureFunctions.isWorldUniqueBuild(aGalaxy, spaceshipType)){
                            boolean isAlreadyAoffer = false;
                            for (BlackMarketOffer aBlackMarketOffer : aGalaxy.getCurrentOffers()) {
                                if(aBlackMarketOffer.isShip() && aBlackMarketOffer.getSpaceshipType().getName().equals(spaceshipType.getName())){
                                    isAlreadyAoffer = true;
                                }
                            }
                            if(!isAlreadyAoffer){
                                boolean haveBuildingOrder = false;
                                for (Player tempPlayer : aGalaxy.getPlayers()) {
                                    if(tempPlayer.getOrders().haveSpaceshipTypeBuildOrder(spaceshipType)){
                                        haveBuildingOrder = true;
                                    }
                                }
                                if(!haveBuildingOrder){
                                    constructible =  true;
                                }
                            }
                        }else{
                            constructible = true;
                        }
                    }
                }
            }
        }
        return constructible;
    }

    public static boolean isBluePrintReadyToUseInBlackMarket(SpaceshipType spaceshipType, int turn){
        boolean constructible =  false;

        if (turn >= spaceshipType.getBluePrintFirstTurn()){
            if (spaceshipType.getBluePrintFrequency() != BlackMarketFrequency.NEVER){
                if(!spaceshipType.isPlayerUnique() && !spaceshipType.isFactionUnique() && !spaceshipType.isWorldUnique()){
                    constructible = true;
                }
            }
        }

        return constructible;
    }
}
