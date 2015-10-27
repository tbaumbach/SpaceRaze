/*
 * Created on 2005-feb-20
 */
package sr.server.ranking;

import java.io.Serializable;

import sr.general.StringTokenizerPlusPlus;
import sr.general.logging.Logger;

/**
 * @author wmpabod
 *
 * Ranking information for one player
 * Rank is equal to nr defeated opponents.
 * Info about wins as empire, rebels etc is just extra info fun to know...
 */
public class RankedPlayer implements Comparable<RankedPlayer>,Serializable{
	private static final long serialVersionUID = 1L;
	private int soloWin;
	private int factionWin;
	private int loss;
	private int nrDefeatedPlayers; // contain total ranking points
	private int survival;
	private String playerLogin;
	private int ranking, rankedPlayersNr; // only used when returning a players data to the Android client

	/**
	 * This method is both called when creating a new player and when creating
	 * a player instance read from file.
	 * @param playerString either a playerLogin or a string read from file
	 */
	public RankedPlayer(String playerString){
		StringTokenizerPlusPlus stpp = new StringTokenizerPlusPlus(playerString,"\t");
		// always read the first token
		playerLogin = stpp.nextToken();
		if (stpp.countTokens() > 1){
			// recreate a player from data from a file
			nrDefeatedPlayers = readInt(stpp);
			soloWin = readInt(stpp);
			factionWin = readInt(stpp);
			loss = readInt(stpp);
			if (stpp.hasMoreTokens()){
				survival = readInt(stpp);
			}
		}
	}
	
	private int readInt(StringTokenizerPlusPlus stpp){
		return Integer.parseInt(stpp.nextToken());
	}
	
	public boolean isPlayer(String aPlayerLogin){
		return playerLogin.equals(aPlayerLogin);
	}
	
	public String getLogin(){
		return playerLogin;
	}
	
	public int getNrDefeatedPlayers(){
		return nrDefeatedPlayers;
	}

	public void addToNrDefeatedPlayers(int addNr){
		nrDefeatedPlayers += addNr;
	}

	public void newWin(int nrDefeatedOpp, boolean aSoloWin){
		nrDefeatedPlayers = nrDefeatedPlayers + nrDefeatedOpp;
		if (aSoloWin){
			soloWin = soloWin + 1;
		}else{
			factionWin = factionWin + 1;
		}
	}
	
	public void newLoss(){
		loss = loss + 1;
	}

	public void newSurvival(){
		survival = survival + 1;
	}

	public String getSaveString(int index){
		String retStr = "user" + index + " = ";
		retStr = retStr + "\t" + playerLogin;
		retStr = retStr + "\t" + nrDefeatedPlayers;
		retStr = retStr + "\t" + soloWin;
		retStr = retStr + "\t" + factionWin;
		retStr = retStr + "\t" + loss;
		Logger.fine("survival, " + playerLogin + ": " + survival);
		retStr = retStr + "\t" + survival;
		return retStr;
	}

	/**
	 * First som on largest nr of points, if equal sort on lowest nr of losses
	 * @param anotherPlayer RankedPlayer instance
	 * @return
	 */
	public int compareTo(RankedPlayer anotherPlayer) {
		int diff = anotherPlayer.getNrDefeatedPlayers() - nrDefeatedPlayers;
		if (diff == 0){
			diff = loss - anotherPlayer.getLoss();
		}
		return diff;
	}

	public int getTotalNrGames() {
		return factionWin + soloWin + loss;
	}

	public int getFactionWin() {
		return factionWin;
	}
	
	public int getLoss() {
		return loss;
	}
	
	public int getSoloWin() {
		return soloWin;
	}
	
	public int getSurvival() {
		return survival;
	}
	
	public String getLossColorString() {
		String retVal = "#FF0000";
		if (loss == 0){
			retVal = "#880000";
		}
		return retVal;
	}

	public String getSoloWinColorString() {
		String retVal = "#00FF00";
		if (soloWin == 0){
			retVal = "#008800";
		}
		return retVal;
	}

	public String getFactionWinColorString() {
		String retVal = "#0000FF";
		if (factionWin == 0){
			retVal = "#000088";
		}
		return retVal;
	}
	
	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public int getRankedPlayersNr() {
		return rankedPlayersNr;
	}

	public void setRankedPlayersNr(int rankedPlayersNr) {
		this.rankedPlayersNr = rankedPlayersNr;
	}
	
	@Override
	public String toString(){
		return "RankedPlayer: " + playerLogin + ", " + ranking;
	}
	
	public String getRankTitleDroid(){
		String rankTitle = "Beginner";
		if ((nrDefeatedPlayers >= 10) & (nrDefeatedPlayers <= 19)){
			rankTitle = "Experienced";
		}else
		if ((nrDefeatedPlayers >= 20) & (nrDefeatedPlayers <= 49)){
			rankTitle = "Veteran";
		}else
		if ((nrDefeatedPlayers >= 50) & (nrDefeatedPlayers <= 99)){
			rankTitle = "Elite";
		}else
		if ((nrDefeatedPlayers >= 100) & (nrDefeatedPlayers <= 199)){
			rankTitle = "Master";
		}else
		if ((nrDefeatedPlayers >= 200) & (nrDefeatedPlayers <= 299)){
			rankTitle = "Grand Master";
		}else
		if ((nrDefeatedPlayers >= 300) & (nrDefeatedPlayers <= 399)){
			rankTitle = "Unstoppable Master";
		}else
		if ((nrDefeatedPlayers >= 400) & (nrDefeatedPlayers <= 499)){
			rankTitle = "Wickedsick Master";
		}else
		if ((nrDefeatedPlayers >= 500) & (nrDefeatedPlayers <= 999)){
			rankTitle = "Ludicrous Master";
		}else{ // 1000+
			rankTitle = "Godlike Master";
		}
		// is the supreme master?
		if ((nrDefeatedPlayers >= 100) & (ranking == 1)){
			rankTitle = "Supreme " + rankTitle;
		}
		return rankTitle;
	}

	public boolean equals(RankedPlayer rp){
		return (soloWin == rp.getSoloWin()) & (factionWin == rp.getFactionWin()) & (loss == rp.getLoss()) & (survival == rp.getSurvival());
	}
}
