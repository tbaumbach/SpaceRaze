package sr.campaign.ai.enums;

public enum FindPlanetCriterium {
	NEUTRAL_UNTOUCHED, // a neutral planet that either are open or started as closed, target for diplomats that can move by themselves
	HOSTILE_ASSASSIN_OPEN, // target for assassins
	CLOSED, // target for spies, and assassins if there are no open enemy planets
	OWN_PLANET_NOT_BESIEGED, // used by retreating ships
	EMPTY_VIP_TRANSPORT_WITHOUT_ORDERS; // used when finding transport for diplomat VIPs
}
