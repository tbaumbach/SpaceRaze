package sr.enums;

public enum DiplomacyIconState {
	ACTIVE,
	PASSIVE_AND_SELECTED_AND_SUGGESTED,
	PASSIVE_AND_SELECTED, // when a player selects and creates a diplomatic order (offer or change)
	PASSIVE_AND_SUGGESTED, // when the other player have sent an diplomatic offer
	PASSIVE,
	DISABLED;
}
