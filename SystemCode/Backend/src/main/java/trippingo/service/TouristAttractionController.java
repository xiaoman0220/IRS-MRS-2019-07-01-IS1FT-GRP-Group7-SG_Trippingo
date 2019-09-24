package trippingo.service;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import trippingo.model.TouristAttraction;
import trippingo.model.TravelDistance;
import trippingo.repository.TouristAttractionRepository;
import trippingo.repository.TravelDistanceRepository;
import trippingo.utils.CommonUtils;
import trippingo.utils.StringUtils;

@RestController
@RequestMapping(path="/attractions")
public class TouristAttractionController {
	
	@Autowired 
	private TouristAttractionRepository attractionRepository;
	
	@Autowired 
	private TravelDistanceRepository distanceRepository;
	
	@Autowired
	private PromotionController promotionService;
	
	private static final Logger logger = LogManager.getLogger(PromotionController.class);
	
	@GetMapping("/{id}")
	public TouristAttraction fetchAttraction(@PathVariable Long id,
											 @RequestParam(value="detailed", defaultValue = "false") boolean detailed) {
		TouristAttraction attraction =  attractionRepository.findById(id).orElse(null);
		//REMOVE DETAILS
		if(!detailed)
			CommonUtils.removeDetails(attraction);
		else {
			attraction.setPromotions(promotionService.fetchPromotions(attraction.getId()));
		}
			
		return attraction;
		
	}
	
	public List<TouristAttraction> fetchAttractionsByIds(Set<Long> ids){
		return (List<TouristAttraction>) attractionRepository.findAllById(ids);
	}
	
	@GetMapping("/{id}/distance/{to}")
	public TravelDistance fetchDistanceBetween(@PathVariable Long id,
										  @PathVariable Long to) {
		
		return distanceRepository.fetchDistanceBetween(id, to);
	}
	
	@GetMapping
	public List<TouristAttraction> fetchAttractions(@RequestParam(value="name", required = false) String name,
													@RequestParam(value="keyword", required = false) String keyword) {
		List<TouristAttraction> attractions;
		if(StringUtils.isNotNull(name)) {
			attractions = attractionRepository.findAllByName(name);
		}
		else if (StringUtils.isNotNull(keyword)) {
			attractions = attractionRepository.findAllByKeyword(keyword);
		}
		else {
			attractions = (List<TouristAttraction>) attractionRepository.findAll();
		}
		attractions.stream().forEach(CommonUtils::removeDetails);
		return attractions;
	}
	
	@PutMapping
	public TouristAttraction saveAttraction(@RequestBody TouristAttraction touristAttraction) {
		attractionRepository.save(touristAttraction);
		return touristAttraction;
	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
		logger.error("\nException in TouristAttractionController:", e);
		return new ResponseEntity<String>(e.getMessage()+". Check application.log", HttpStatus.BAD_REQUEST);
		
    }
}
