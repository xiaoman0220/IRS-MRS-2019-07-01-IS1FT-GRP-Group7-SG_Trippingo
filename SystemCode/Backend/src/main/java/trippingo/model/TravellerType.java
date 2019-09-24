package trippingo.model;

public enum TravellerType {
	Family,
	Couple,
	Solo,
	Business,
	Friends;

	public static TravellerType fromTripadvisorValue(String value) {
		switch(value) {
		case "families": 
			return TravellerType.Family;
		case "couples":
			return TravellerType.Couple;
		case "business":
			return TravellerType.Business;
		case "friends":
			return TravellerType.Friends;
		case "solo":
		default:
			return TravellerType.Solo;
		}
	}
	

}
