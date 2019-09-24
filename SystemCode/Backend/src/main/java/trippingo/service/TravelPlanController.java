package trippingo.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rules.AssociationRulesRecommender;
import trippingo.dto.AssociationRecommendationDTO;
import trippingo.dto.AttractionVisitDTO;
import trippingo.dto.DayPlanDTO;
import trippingo.dto.ItineraryDTO;
import trippingo.dto.SaveSelectedAttractionRequest;
import trippingo.dto.SaveSelectedPromotionRequest;
import trippingo.model.AttractionCategory;
import trippingo.model.AttractionVisit;
import trippingo.model.DayPlan;
import trippingo.model.Itinerary;
import trippingo.model.Promotion;
import trippingo.model.TouristAttraction;
import trippingo.model.TravelPlan;
import trippingo.model.TravellerPreferences;
import trippingo.optaplanner.resources.PlanAttraction;
import trippingo.optaplanner.resources.TripPlanner;
import trippingo.optaplanner.solver.TripSolver;
import trippingo.repository.TouristAttractionRepository;
import trippingo.repository.TravelPlanRepository;

@RestController
@RequestMapping(path="/travelPlans")
public class TravelPlanController {
	@Autowired
	private TouristAttractionRepository attractionRepository;

	@Autowired
	private TravelPlanRepository repository;
	
	@Autowired
	private TripSolver tripSolver;
	
	@Autowired
	private TouristAttractionController attractionService;
	
	@Autowired
	private PromotionController promotionService;
	
	@Autowired
	private AttractionRecommendationController recommendationService;
	
	private static final Logger logger = LogManager.getLogger(PromotionController.class);
	
	
	@GetMapping("/{id}")
	public TravelPlan fetchTravelPlan(@PathVariable Long id) {
		return repository.findById(id).orElse(null);
	}
	
	@GetMapping("/{id}/itinerary")
	public ItineraryDTO fetchItinerary(@PathVariable Long id) {
		return repository.findById(id).map(TravelPlan::getItinerary).map(this::mapToItineraryDTO).orElseThrow(() -> new EntityNotFoundException());
	}


	private ItineraryDTO mapToItineraryDTO(Itinerary itinerary) {
		ItineraryDTO itineraryDTO = new ItineraryDTO();
		itineraryDTO.setItineraryId(itinerary.getId());
		DayPlanDTO[] dayPlans = itinerary.getDayPlans().stream().map(this::mapToDayPlaDTO).toArray(DayPlanDTO[]::new);
		itineraryDTO.setDayPlans(dayPlans);
		return itineraryDTO;
	}
	
	private DayPlanDTO mapToDayPlaDTO(DayPlan dayPlan) {
		DayPlanDTO dayPlanDTO = new DayPlanDTO();
		dayPlanDTO.setId(dayPlan.getId());
		dayPlanDTO.setTravelDate(dayPlan.getTravelDate());
		dayPlanDTO.setSerialNo(dayPlan.getSerialNo());
		AttractionVisitDTO[] attractionVisits = dayPlan.getAttractionVisits().stream().map(this::mapToAttractionVisitDTO).toArray(AttractionVisitDTO[]::new);
		dayPlanDTO.setAttractionVisit(attractionVisits);
		return dayPlanDTO;
	}
	
	private AttractionVisitDTO mapToAttractionVisitDTO(AttractionVisit attractionVisit) {
		AttractionVisitDTO attactionVisitDTO = new AttractionVisitDTO();
		attactionVisitDTO.setAttraction(attractionService.fetchAttraction(attractionVisit.getAttractionId(), false));
		attactionVisitDTO.setSerialNo(attractionVisit.getSerialNo());
		attactionVisitDTO.setTimeofDay(attractionVisit.getVisitStartTime());
		return attactionVisitDTO;
	}
	
	
	@GetMapping("/{id}/preferences")
	public TravellerPreferences fetchTravelPreferences(@PathVariable Long id) {
		return repository.findById(id).map(TravelPlan::getTravelPreferences).orElse(null);
	}
	
	@GetMapping("/{id}/recommendations")
	public Iterable<TouristAttraction> fetchRecommendation(@PathVariable Long id, 
														   @RequestParam(value="count", required = false, defaultValue = "5") int resultCount) {
		
		TravellerPreferences preferences = repository.findById(id).map(TravelPlan::getTravelPreferences).orElse(new TravellerPreferences());
		resultCount = preferences.getNoOfTravelDays().intValue() *3; 
		List<String> keywords = preferences.getCategories().stream().map(AttractionCategory::name).collect(Collectors.toList());
		return recommendationService.fetchRecommendations(preferences.getTravellerType(), keywords, null, resultCount);
	}
	
	@GetMapping("/{id}/selectedAttractions")
	public List<TouristAttraction> fetchSelectedAttractions(@PathVariable Long id) {
		return repository.findById(id).map(TravelPlan::getSelectedAttractionIds).map(attractionService::fetchAttractionsByIds).orElse(Collections.emptyList());
	}
	
	@GetMapping("/{id}/selectedPromotions")
	public List<Promotion> fetchSelectedPromotions(@PathVariable Long id) {
		return repository.findById(id).map(TravelPlan::getSelectedPromotionIds).map(promotionService::fetchPromotionsByIds).orElse(Collections.emptyList());
	}
	
	@GetMapping("/{id}/associatedAttractions")
	public List<TouristAttraction> fetchAssociatedAttractions(@PathVariable Long id, @RequestParam(value="attractions", required = false) List<String> attractions) {
		AssociationRecommendationDTO[] inputs = new AssociationRecommendationDTO[] {new AssociationRecommendationDTO(attractions.toArray(new String[0]))};
		AssociationRecommendationDTO[] outputs = AssociationRulesRecommender.fetchAssociatedAttractions(inputs);
		return Arrays.stream(outputs)
					.map(AssociationRecommendationDTO::getAssociatedAttractionNames)
					.flatMap(List::stream)
					.map(attractionRepository::findByName)
					.collect(Collectors.toList());
	}
	
	
	
	@PutMapping("/{id}/preferences")
	public TravelPlan saveTravelPreferences(@PathVariable Long id, @RequestBody TravellerPreferences travellerPreferences) {
		return repository.findById(id).map(tp -> addTravellerPreferences(tp, travellerPreferences)).map(repository::save).orElse(null);
	}
	
	
	@PostMapping
	public TravelPlan createTravelPlan(@RequestBody TravelPlan travelPlan) {
		if(travelPlan == null)
			travelPlan = new TravelPlan();
		return repository.save(travelPlan);
	}
	
	@PutMapping("/{id}")
	public TravelPlan updateTravelPlan(@PathVariable Long id, @RequestBody TravelPlan travelPlan) {
		return repository.save(travelPlan);
	}
	
	
	@PutMapping("/{id}/selectedAttractions")
	public TravelPlan saveSelectedAttractions(@PathVariable Long id, @RequestBody SaveSelectedAttractionRequest request) {
		Set<Long>attractions = Arrays.stream(request.getAttractionIds()).collect(Collectors.toSet());
		return repository.findById(id).map(tp -> addSelectedAttractions(tp, attractions)).map(repository::save).orElse(null);
		
	}
	
	@PutMapping("/{id}/selectedPromotions")
	public TravelPlan saveSelectedPromotions(@PathVariable Long id, @RequestBody SaveSelectedPromotionRequest request) {
		Set<Long> promotionIds = Arrays.stream(request.getPromotionIds()).collect(Collectors.toSet());
		return repository.findById(id).map(tp -> addSelectedPromotions(tp, promotionIds)).map(repository::save).orElse(null);
	}
	
	@PutMapping("/{id}/itinerary")
	public ItineraryDTO planItinerary(@PathVariable Long id) {
		
		TravelPlan travelPlan = repository.findById(id).orElse(null);
		List<TouristAttraction> selectedAttractions = fetchSelectedAttractions(id);
		Set<Long> selectedAttractionIds = selectedAttractions.stream().map(TouristAttraction::getId).collect(Collectors.toSet());
		TravellerPreferences preferences = travelPlan.getTravelPreferences();
		int maxAttractions = preferences.getNoOfTravelDays().intValue() * 3;
//		int moreAttractions =maxAttractions - selectedAttractions.size();
		List<TouristAttraction> otherRecommendations= (List<TouristAttraction>) fetchRecommendation(id, maxAttractions);
		otherRecommendations.stream().filter(rec -> !selectedAttractionIds.contains(rec.getId())).forEach(selectedAttractions::add);
		selectedAttractions = selectedAttractions.subList(0, maxAttractions);
		TripPlanner solvedTripPlanner = tripSolver.optimizeItinerary(selectedAttractions, preferences);
		Itinerary itinerary = mapToItinerary(solvedTripPlanner, travelPlan.getTravelDate());
		travelPlan.setItinerary(itinerary);
		repository.save(travelPlan);
		ItineraryDTO itineraryDTO = fetchItinerary(id);
		return itineraryDTO;
		
	}
	
	private Itinerary mapToItinerary(TripPlanner solvedTripPlanner, LocalDate startDate) {
		Itinerary itinerary = new Itinerary();
		Set<DayPlan> dayPlans = solvedTripPlanner.getDayPlans().entrySet().stream().filter(d -> d.getKey() > 0).map(d -> this.mapToDayPlan(d, startDate)).collect(Collectors.toSet());
		itinerary.setDayPlans(dayPlans);
		return itinerary;
	}
	
	private DayPlan mapToDayPlan(Map.Entry<Integer, List<PlanAttraction>> dayPlanEntry, LocalDate startDate) {
		DayPlan dayPlan = new DayPlan();
		dayPlan.setSerialNo(dayPlanEntry.getKey());
		if(dayPlanEntry.getKey()> -1) {
			if(startDate!=null)
				dayPlan.setTravelDate(startDate.plusDays(dayPlanEntry.getKey()- 1));
				List<AttractionVisit> attractionVisits = dayPlanEntry.getValue().stream()
						.map(this::mapToAttractionVisit)
						.sorted((a1,a2) -> a1.getVisitStartTime().compareTo(a2.getVisitStartTime()))
						.collect(Collectors.toList());
				dayPlan.setAttractionVisits(attractionVisits);
		}
		return dayPlan;
		
	}
	
	private AttractionVisit mapToAttractionVisit(PlanAttraction planAttraction) {
		AttractionVisit attractionVisit = new AttractionVisit();
		attractionVisit.setAttractionId(planAttraction.getAttraction().getId());
		attractionVisit.setSerialNo(planAttraction.getSerialNo());
		attractionVisit.setVisitStartTime(planAttraction.getStartingTimeGrain().getTime());
		return attractionVisit;
		
	}

	
	
	private TravelPlan addTravellerPreferences(TravelPlan travelPlan, TravellerPreferences preferences) {
		travelPlan.setTravelPreferences(preferences);
		return travelPlan;
	}
	
	private TravelPlan addSelectedAttractions(TravelPlan travelPlan, Set<Long> attractionIds) {
		travelPlan.setSelectedAttractionIds(attractionIds);
		return travelPlan;
	}
	
	private TravelPlan addSelectedPromotions(TravelPlan travelPlan, Set<Long> promotionIds) {
		travelPlan.setSelectedPromotionIds(promotionIds);
		return travelPlan;
	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
		logger.error("\nException in TravelPlanController:", e);
		return new ResponseEntity<String>(e.getMessage()+". Check application.log", HttpStatus.BAD_REQUEST);
    }


}
