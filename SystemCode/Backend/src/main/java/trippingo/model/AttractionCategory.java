package trippingo.model;

public enum AttractionCategory {
	Museum,
	Shopping,
	Landmark,
	NaturePark,
	AmusementPark;
	
	public static AttractionCategory fromTripAdvisorValue(String value) {
		switch(value) {
		case "Museums": 
			return AttractionCategory.Museum;
		case "Shopping":
			return AttractionCategory.Shopping;
		case "Sights & Landmarks":
			return AttractionCategory.Landmark;
		case "Nature & Parks":
			return AttractionCategory.NaturePark;
		case "Water & Amusement Parks":
			return AttractionCategory.AmusementPark;
		}
		return null;
	}
}


