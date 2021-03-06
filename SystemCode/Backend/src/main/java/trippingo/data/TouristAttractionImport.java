package trippingo.data;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import trippingo.datatype.Range;
import trippingo.model.AttractionCategory;
import trippingo.model.Review;
import trippingo.model.TouristAttraction;
import trippingo.model.TravellerType;
import trippingo.repository.TouristAttractionRepository;
import trippingo.utils.StringUtils;

@Component
public class TouristAttractionImport {
	
	@Autowired
	private TouristAttractionRepository repository ;
	private static final Logger logger = LogManager.getLogger(TouristAttractionImport.class);
	
	private static final String RELATIVE_FILE_PATH = "\\data\\All_fixed.json";
	
	public void importData() {
		logger.info("Starting Tourist Attraction Import");
		JSONParser parser = new JSONParser();
		int saveCount=0;
		try {
			String filePath = new File("").getCanonicalPath() + RELATIVE_FILE_PATH;
			Reader reader = new FileReader(filePath);
			JSONArray attractions = (JSONArray) parser.parse(reader);
			logger.info("Loaded "+attractions.size()+" attractions from "+ RELATIVE_FILE_PATH);
            Iterator<JSONObject> iterator = attractions.iterator();
            JSONObject object;
            while (iterator.hasNext()) {
				object = iterator.next();
				String name =object.get("attractions").toString();
				if(!isValidAttraction(object)) {
					logger.info("Ignored " + name);
					continue;
				}
                TouristAttraction attraction;
				try {
					attraction = parseAttraction(object);
					repository.save(attraction);
					saveCount++;
				} catch (Exception e) {
					name =object.get("attractions").toString();
					logger.error("\nException for attractionName:"+name , e);
				}
            }

        } catch (Exception e) {
        	logger.error(e);
        }
		logger.info("Imported "+saveCount+" attractions");
		logger.info("Ending Tourist Attraction Import");
		
	}

	private boolean isValidAttraction(JSONObject object) {
		Predicate<Object> isClosed = hours -> "PERMANENTLY CLOSED".equals(hours.toString().trim()); 
		if(object==null)
			return false;
		if(isClosed.test(object.get("opening hours")) || isClosed.test(object.get("closing hours")))
			return false;
		
		return true;
	}

	private static TouristAttraction parseAttraction(JSONObject object)  throws Exception{
		TouristAttraction attraction = new TouristAttraction();
		/**
		 * NAME, DESCRIPTION, KEYWORDS
		 */
		attraction.setName(object.get("attractions").toString());
		attraction.setDescription(object.get("about").toString());
		Object keywords = object.get("keywords");
		List<String> keywordList = keywords.toString().contentEquals("-") ? Collections.emptyList():(JSONArray)object.get("keywords");
		attraction.setKeywords(keywordList.stream().map(String::trim).collect(Collectors.joining(",")));
		/**
		 * CATEGORY
		 * one or more categories? predefined limited categories, or unlimited categories?
		 */
//		attraction.setCategory(parseCategory(object.get("categories").toString()));
		
		/**
		 * POSTAL CODE
		 * Do postal codes need correction?  Locations without postal codes(Fort Siloso, Tanjong Beach,Sentosa Boardalk)
		 */
		attraction.setPostalCode(parsePostalCode(object.get("postal").toString()));
		/**
		 * OPERATING HOURS
		 */
		attraction.setOpeningTime(parseTime(object.get("opening hours").toString()));
		attraction.setClosingTime(parseTime(object.get("closing hours").toString()));
		/**
		 * DURATION
		 * Take mean?
		 */
		attraction.setRecommendedDuration(parseDuration(object.get("duration").toString()));
		/**
		 * REVIEWS
		 */
		List<JSONObject> reviewsArr = (JSONArray) object.get("review");
		Set<Review> reviews = reviewsArr.stream().map(TouristAttractionImport::parseReview).collect(Collectors.toSet());
		attraction.setReviews(reviews);
			
		return attraction;
	}
	
	private static Review parseReview(JSONObject object) {
		Review review = new Review();
		review.setRating(Integer.valueOf(object.get("rating").toString()));
		review.setUsername(object.get("username").toString());
		review.setVisitDate(parseDate(object.get("visitdate").toString()));
		review.setTravellerType(TravellerType.fromTripadvisorValue(object.get("traveltype").toString()));
		return review;
	}
	
	private static LocalDate parseDate(String dateString) {
		String[] splitDateStr = dateString.split(" ");
    	Date monthDate;
		try {
			monthDate = new SimpleDateFormat("MMMM").parse(splitDateStr[0]);
	    	Calendar cal = Calendar.getInstance();
	    	cal.setTime(monthDate);
	    	int month = cal.get(Calendar.MONTH);
			return LocalDate.of(Integer.parseInt(splitDateStr[1]), month+1, 1);
		} catch (java.text.ParseException e) {
			return LocalDate.now();
		}
    }

	private static Range parseDuration(String duration) {
		duration = duration.trim().toLowerCase();
		if(StringUtils.isNull(duration) || duration.contentEquals("-") || duration == "-")
			return null;
		duration = duration.replaceFirst("<", "0 to ");
		duration = duration.replaceFirst("\\s*(hour|hours)\\s*\\z", "").trim();
		if(duration.contains("to")) {
			String[] durationArr = duration.split("to");
			return new Range(Double.valueOf(durationArr[0].trim()), Double.valueOf(durationArr[1].trim()));
		}
		else if(duration.contains("-")) {
			String[] durationArr = duration.split("-");
			return new Range(Double.valueOf(durationArr[0].trim()), Double.valueOf(durationArr[1].trim()));
		}
		else {
			return new Range(Double.valueOf(duration.trim()), Double.valueOf(duration.trim())); 
		}
		
	}

	private static AttractionCategory parseCategory(String categories) {
		return AttractionCategory.Landmark;
	}

	private static LocalTime parseTime(String time) {
		time = time.trim();
		if(StringUtils.isNull(time) || time.contentEquals("-"))
			return null;
		boolean isPM = time.endsWith("PM");
		int additive = isPM? 12 : 0;
		int middleIndex = time.indexOf(":");
		if (middleIndex == -1)
			middleIndex = time.indexOf("."); // some operating hours use "." instead of ":"
		String hour = time.substring(0,middleIndex);
		String minutes = time.substring(middleIndex+1,middleIndex+3);
		return LocalTime.of(Integer.parseInt(hour)+additive, Integer.parseInt(minutes));
	}
	
	private static String parsePostalCode(String location) {
		String[] postalSplit = location.split(" ");
		String postalCode = postalSplit[postalSplit.length - 1];
		if(postalCode.matches("\\d*") && postalCode.trim().length() == 6) 
			return postalCode;
		
		else 
			return null;
			
	}

}
