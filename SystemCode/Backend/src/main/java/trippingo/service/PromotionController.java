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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import trippingo.model.Promotion;
import trippingo.model.TouristAttraction;
import trippingo.repository.PromotionRepository;

@RestController
@RequestMapping(path="/promotions")
public class PromotionController {

	
	@Autowired
	private PromotionRepository repository;
	
	@Autowired
	private TouristAttractionController attractionService;
	
	private static final Logger logger = LogManager.getLogger(PromotionController.class);
	
	
	@GetMapping
	public List<Promotion> fetchPromotions(@RequestParam(value="attractionId") Long attractionId) {
		
		List<Long> promotionIds = repository.findAllIdsByAttractionId(attractionId);
		return (List<Promotion>) repository.findAllById(promotionIds);
	}
	
	@GetMapping({"/id"})
	public Promotion fetchPromotion(@PathVariable Long id) {
		
		return repository.findById(id).orElse(null);
	}
	
	public List<Promotion> fetchPromotionsByIds(Set<Long> ids){
		return (List<Promotion>) repository.findAllById(ids);
	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
		logger.error("\nException in PromotionController:", e);
		return new ResponseEntity<String>(e.getMessage()+". Check application.log", HttpStatus.BAD_REQUEST);
    }
}
