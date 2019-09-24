package trippingo.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Itinerary {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "itinerary_generator")
	private Long id;
	
	@OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name="ITINERARY_ID")
	private Set<DayPlan> dayPlans;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set<DayPlan> getDayPlans() {
		return dayPlans;
	}
	public void setDayPlans(Set<DayPlan> dayPlans) {
		this.dayPlans = dayPlans;
	}
	
	
	
	

}
