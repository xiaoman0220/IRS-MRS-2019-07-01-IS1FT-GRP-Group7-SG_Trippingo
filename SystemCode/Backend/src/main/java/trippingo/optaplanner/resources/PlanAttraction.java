package trippingo.optaplanner.resources;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import trippingo.model.TouristAttraction;
import trippingo.model.TravelDistance;
import trippingo.service.TouristAttractionController;

@PlanningEntity
public class PlanAttraction {
	
	private TouristAttraction attraction;
	private TimeGrain startingTimeGrain;
	private int serialNo;
	
	@PlanningId
	private Long id;
	
	private TouristAttractionController service;
	
	@PlanningVariable(valueRangeProviderRefs = {"timeGrainRange"})
	public TimeGrain getStartingTimeGrain() {
		return startingTimeGrain;
	}

	public void setStartingTimeGrain(TimeGrain startingTimeGrain) {
		this.startingTimeGrain = startingTimeGrain;
	}

	
	public TouristAttraction getAttraction() {
		return attraction;
	}

	public void setAttraction(TouristAttraction attraction) {
		this.attraction = attraction;
		this.id = attraction.getId();
	}

	public int calculateOverlap(PlanAttraction other) {
	    if (startingTimeGrain == null || other.getStartingTimeGrain() == null) {
	        return 0;
	    }
	    int start = startingTimeGrain.getGrainIndex();
	    int end = start + getAttraction().getDurationTimeGrains();
	    int otherStart = other.startingTimeGrain.getGrainIndex();
	    int otherEnd = otherStart + other.getAttraction().getDurationTimeGrains();

	    if (end < otherStart) {
	        return 0;
	    } else if (otherEnd < start) {
	        return 0;
	    }
	    return Math.min(end, otherEnd) - Math.max(start, otherStart);
	}
	
	public int travelHoursPenaltyScore() {
		if(startingTimeGrain == null || startingTimeGrain.getDay() == null)
			return 0;
	    int start = startingTimeGrain.getGrainIndex();
	    int end = start + getAttraction().getDurationTimeGrains();
	    if (start < 30 ) {
	    	int x = 1;
	    }
	    if (end < startingTimeGrain.getDay().getLastTravelSlot() && start > startingTimeGrain.getDay().getFirstTravelSlot()) {
	        return 0;

	    }
	    return (int) Math.pow((end - startingTimeGrain.getDay().getLastTravelSlot() +  startingTimeGrain.getDay().getFirstTravelSlot() - start), 10);
	}

	public void setSerialNo(int i) {
		this.serialNo = i;
		
	}

	public int getSerialNo() {
		return serialNo;
	}
	
	
	
	public int attractionDurationScore(PlanAttraction other) { //DONAL
		if(startingTimeGrain == null || other.startingTimeGrain.getDay() == null) {
			return 0;
		}
		
		int start = startingTimeGrain.getGrainIndex();
	    int otherStart = other.startingTimeGrain.getGrainIndex();
	    TravelDistance distance = service.fetchDistanceBetween(getAttraction().getId(), other.getAttraction().getId());
	    double travelGrains  = distance!=null? (distance.getValue()/15):Double.MAX_VALUE;
	    int traveltime = Double.valueOf(Math.ceil(travelGrains)).intValue();
		int duration = otherStart - start - traveltime;
	    int minTimePenalty;
	    int maxTimePenalty;
	    
	    if (duration > getAttraction().getMaxDurationTimeGrains() || duration < getAttraction().getMinDurationTimeGrains()) {   	//Checks if visiting hours exceeds or below Max Min Durations
	    	if  (duration - getAttraction().getMinDurationTimeGrains() < 0){
	    		minTimePenalty =  duration - getAttraction().getMinDurationTimeGrains();
	    	}
	    	else {
	    		minTimePenalty = 0;
	    	}
	    	if  (getAttraction().getMaxDurationTimeGrains() - duration < 0){
	    		maxTimePenalty =  getAttraction().getMaxDurationTimeGrains() - duration;
	    	}
	    	else {
	    		maxTimePenalty = 0;
	    	}
	    	
	    	return Double.valueOf((minTimePenalty*6 + maxTimePenalty+traveltime*3)).intValue() ;
	    }
	    
	    else { //Penalize duration shorter than mean, Reward duration more than mean
	    	return Double.valueOf((duration - getAttraction().getDurationTimeGrains())).intValue();
	    		    	
	    }
	    
	    
	}

	public TouristAttractionController getService() {
		return service;
	}

	public void setService(TouristAttractionController service) {
		this.service = service;
	}
	
	public String toString() {
		double duration = attraction.getDurationTimeGrains();
		return "Time slot for " + attraction.getName() + ": " + getStartingTimeGrain().getGrainIndex()+
				". Recommened Duration: " + duration + "serialNo:" + getSerialNo();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	
	

	

}
