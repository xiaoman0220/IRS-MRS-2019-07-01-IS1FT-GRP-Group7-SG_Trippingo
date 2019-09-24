package trippingo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class TravelDistance {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "distance_gen")
	private Long id;
	
	private Long fromAttractionId;
	
	private Long toAttractionId;
	
	private Double value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFromAttractionId() {
		return fromAttractionId;
	}

	public void setFromAttractionId(Long fromAttractionId) {
		this.fromAttractionId = fromAttractionId;
	}

	public Long getToAttractionId() {
		return toAttractionId;
	}

	public void setToAttractionId(Long toAttractionId) {
		this.toAttractionId = toAttractionId;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	

}
