package trippingo.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import trippingo.data.PromotionImport;
import trippingo.model.AttractionCategory;
import trippingo.model.TouristAttraction;
import trippingo.model.TravellerType;
import trippingo.repository.TouristAttractionRepository;
import trippingo.utils.CommonUtils;
import trippingo.utils.StringUtils;



@RestController
@RequestMapping(path="/recommendations")
public class AttractionRecommendationController {

	private static final Logger logger = LogManager.getLogger(AttractionRecommendationController.class);
	
	@Autowired 
	private TouristAttractionRepository attractionRepository;
	
	@GetMapping
	public Iterable<TouristAttraction> fetchRecommendations(@RequestParam(value="travellerType", required = false, defaultValue = "Friends") TravellerType travellerType,
															@RequestParam(value="keywords", required = false) List<String> keywords,
															@RequestParam(value="location", required = false) String location,
															@RequestParam(value="count", required = false, defaultValue = "5") int resultCount) {
		
		Set<Long> attractionIds = new HashSet<Long>();
		List<TouristAttraction> attractions;
		if(keywords!=null && !keywords.isEmpty()){
			Predicate<String> isCategory = value -> Arrays.stream(AttractionCategory.values()).anyMatch(cat -> cat.name().equals(value));
			List<String> categories  = keywords.stream().filter(isCategory).collect(Collectors.toList());
			List<String> otherKeywords = keywords.stream().filter(isCategory.negate()).collect(Collectors.toList());
			if(!categories.isEmpty()) {
				attractionIds.addAll(attractionRepository.findAllByCategory(categories));
			}
			if(!otherKeywords.isEmpty()) {
				otherKeywords.stream()
							 .map(attractionRepository::findAllByKeyword)
							 .flatMap(List::stream)
							 .map(TouristAttraction::getId)
							 .forEach(attractionIds::add);
			}
			attractions = (List<TouristAttraction>) attractionRepository.findAllById(attractionIds);
		}
		else {
			attractions = (List<TouristAttraction>) attractionRepository.findAll();
		}
		
		
		//ORDER BY RANK & LOCATION
		Collections.sort(attractions, getComparator(travellerType, location));
		//REMOVE DETAILS
		attractions.stream().forEach(CommonUtils::removeDetails);
		return attractions.size() > resultCount? attractions.subList(0, resultCount) : attractions;
	}

	private Comparator<TouristAttraction> getComparator(TravellerType travellerType, String postalCode) {
		
		switch(travellerType) {
		case  Family:
			return (a1, a2) -> a1.getAttractionRank().getFamilyRank() - a2.getAttractionRank().getFamilyRank() + getProximityScore(a1, a2, postalCode);
		case  Business:
			return (a1, a2) -> a1.getAttractionRank().getBusinessRank() - a2.getAttractionRank().getBusinessRank() + getProximityScore(a1, a2, postalCode);
		case  Solo:
			return (a1, a2) -> a1.getAttractionRank().getSoloRank() - a2.getAttractionRank().getSoloRank() + getProximityScore(a1, a2, postalCode);
		case Couple:
			return (a1, a2) -> a1.getAttractionRank().getCoupleRank() - a2.getAttractionRank().getCoupleRank() + getProximityScore(a1, a2, postalCode);
		case  Friends:
		default:
			return (a1, a2) -> a1.getAttractionRank().getFriendsRank() - a2.getAttractionRank().getFriendsRank() + getProximityScore(a1, a2, postalCode);
		}
	}
	
	
	private int getProximityScore(TouristAttraction a1, TouristAttraction a2, String location) {
		if(StringUtils.isNull(location) || a1.getPostalCode() == null || a2.getPostalCode() == null)
			return 0;
		int score  = getProximityScore(a1.getPostalCode(), location) - getProximityScore(a2.getPostalCode(), location);
		return score;
	}
	
	private int getProximityScore(String postalCode1, String postalCode2) {
		return  Math.abs(Integer.parseInt(postalCode1.substring(0,2)) - Integer.parseInt(postalCode2.substring(0,2))); 
	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
		logger.error("\nException in AttractionRecommendationController", e);
		return new ResponseEntity<String>(e.getMessage()+". Check application.log", HttpStatus.BAD_REQUEST);
    }
	
	

}
