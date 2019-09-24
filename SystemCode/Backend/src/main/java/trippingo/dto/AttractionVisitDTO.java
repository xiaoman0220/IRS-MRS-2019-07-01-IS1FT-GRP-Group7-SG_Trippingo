package trippingo.dto;

import java.io.Serializable;
import java.time.LocalTime;

import trippingo.model.TouristAttraction;

public class AttractionVisitDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6589741906042708767L;

	private Integer serialNo;
	
	private LocalTime timeofDay;
	
	private TouristAttraction attraction;

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public LocalTime getTimeofDay() {
		return timeofDay;
	}

	public void setTimeofDay(LocalTime timeofDay) {
		this.timeofDay = timeofDay;
	}

	public TouristAttraction getAttraction() {
		return attraction;
	}

	public void setAttraction(TouristAttraction attraction) {
		this.attraction = attraction;
	}
	
	
}
