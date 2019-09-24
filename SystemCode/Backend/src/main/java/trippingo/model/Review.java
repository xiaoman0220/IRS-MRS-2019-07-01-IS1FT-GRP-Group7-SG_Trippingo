package trippingo.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="review")
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_generator")
	private Long id;
	private String username;
	private Integer rating;
	private LocalDate visitDate;
	
	@Enumerated(EnumType.STRING)
	private TravellerType travellerType;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public LocalDate getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(LocalDate visitDate) {
		this.visitDate = visitDate;
	}
	public TravellerType getTravellerType() {
		return travellerType;
	}
	public void setTravellerType(TravellerType travellerType) {
		this.travellerType = travellerType;
	}
	
	
	
	
}
