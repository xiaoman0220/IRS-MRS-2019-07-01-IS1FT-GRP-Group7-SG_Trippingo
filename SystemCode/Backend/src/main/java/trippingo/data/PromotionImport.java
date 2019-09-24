package trippingo.data;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import trippingo.model.Promotion;
import trippingo.model.TouristAttraction;
import trippingo.repository.PromotionRepository;
import trippingo.repository.TouristAttractionRepository;

@Component
public class PromotionImport {
	
	@Autowired
	private PromotionRepository repository ;
	
	@Autowired
	private TouristAttractionRepository attractionRepository;
	
	private static final Logger logger = LogManager.getLogger(PromotionImport.class);
	
	private static final String RELATIVE_FILE_PATH = "\\data\\promos_match.json";
	
	public void importData() {
		logger.info("Starting Promotion Import");
		JSONParser parser = new JSONParser();
		int saveCount=0;
		try {
			String filePath = new File("").getCanonicalPath() + RELATIVE_FILE_PATH;
			Reader reader = new FileReader(filePath);
			JSONArray promotions = (JSONArray) parser.parse(reader);
			logger.info("Loaded "+promotions.size()+" attractions from "+ RELATIVE_FILE_PATH);
            Iterator<JSONObject> iterator = promotions.iterator();
            JSONObject object;
            while (iterator.hasNext()) {
				object = iterator.next();
				String name =object.get("name").toString();
				if(!isValidPromotion(object)) {
					logger.info("Ignored " + name);
					continue;
				}
                Promotion promotion;
				try {
					promotion = parsePromotion(object);
					repository.save(promotion);
					saveCount++;
				} catch (Exception e) {
					logger.error("\nException for promoName:"+name , e);
				}
            }

        } catch (Exception e) {
        	logger.error(e);
        }
		logger.info("Imported "+saveCount+" promotions");
		logger.info("Ending Promotion Import");
		
	}
	

	private boolean isValidPromotion(JSONObject object) {
		if(object == null)
			return false;
		return ((List)object.get("matchedAttractions")).size()> 0;
	}

	private Promotion parsePromotion(JSONObject object) {
		Promotion promotion = new Promotion();
		promotion.setAttractions(getAttractionIds((JSONArray)object.get("matchedAttractions")));
		promotion.setName(object.get("name").toString());
		promotion.setUrl(object.get("url").toString());
		promotion.setImageUrl(object.get("image_url").toString());
		promotion.setOriginalPrice(new BigDecimal(object.get("original_price").toString().trim()));
		promotion.setPrice(new BigDecimal(object.get("price").toString()));
		String numBooked = object.get("num_booked").toString().replaceFirst("\\+", "");
		promotion.setNumBooked(Integer.valueOf(numBooked));
		promotion.setNumReviews(Integer.valueOf(object.get("num_reviews").toString()));
		promotion.setRating(Double.valueOf(object.get("rating").toString()));
		promotion.setSeo(object.get("seo").toString());
		return promotion;
	}


	private Set<Long> getAttractionIds(List<String> matchedAttractions) {
		return matchedAttractions.stream()
								 .map(attractionRepository::findByName)
								 .map(TouristAttraction::getId)
								 .collect(Collectors.toSet());
	}


}
