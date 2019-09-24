package trippingo.dto;

import java.io.Serializable;

public class ItineraryDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9123851831743262448L;

	private Long itineraryId;
	
	private DayPlanDTO[] dayPlans;

	public Long getItineraryId() {
		return itineraryId;
	}

	public void setItineraryId(Long itineraryId) {
		this.itineraryId = itineraryId;
	}

	public DayPlanDTO[] getDayPlans() {
		return dayPlans;
	}

	public void setDayPlans(DayPlanDTO[] dayPlans) {
		this.dayPlans = dayPlans;
	}
	
	

}
