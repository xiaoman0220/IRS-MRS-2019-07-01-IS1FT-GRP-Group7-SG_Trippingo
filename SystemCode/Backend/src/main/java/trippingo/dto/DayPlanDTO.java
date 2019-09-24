package trippingo.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class DayPlanDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5076851628883381088L;

	private Long id;
	
	private LocalDate travelDate;
	
	private Integer serialNo;
	
	private AttractionVisitDTO[] attractionVisit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getTravelDate() {
		return travelDate;
	}

	public void setTravelDate(LocalDate travelDate) {
		this.travelDate = travelDate;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public AttractionVisitDTO[] getAttractionVisit() {
		return attractionVisit;
	}

	public void setAttractionVisit(AttractionVisitDTO[] attractionVisit) {
		this.attractionVisit = attractionVisit;
	}
	
	
}
