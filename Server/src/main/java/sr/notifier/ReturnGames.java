package sr.notifier;

public enum ReturnGames {
	ALL,
	OWN_AND_OPEN,
	OWN, // games that a player have joined, even if they are still on turn 0
	OPEN, // all games that is on turn 0 and the player have not joined
	NONE;

}
