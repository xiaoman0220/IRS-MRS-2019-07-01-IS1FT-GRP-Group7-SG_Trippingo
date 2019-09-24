package trippingo.utils;

import java.util.Collections;

import trippingo.model.TouristAttraction;

public class CommonUtils {
	
	public static TouristAttraction removeDetails(TouristAttraction attraction) {
		attraction.setReviews(Collections.emptySet());
		attraction.setKeywords("");
		return attraction;
	}
}
