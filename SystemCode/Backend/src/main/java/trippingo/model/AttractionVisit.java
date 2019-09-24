package trippingo.model;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AttractionVisit {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "visit_generator")
	private Long Id;
	
	private Integer serialNo;
	
	private LocalTime visitStartTime; 
	
	private Long attractionId;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public LocalTime getVisitStartTime() {
		return visitStartTime;
	}

	public void setVisitStartTime(LocalTime visitStartTime) {
		this.visitStartTime = visitStartTime;
	}

	public Long getAttractionId() {
		return attractionId;
	}

	public void setAttractionId(Long attractionId) {
		this.attractionId = attractionId;
	}

	
	
	

}
