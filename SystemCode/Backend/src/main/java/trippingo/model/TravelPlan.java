package trippingo.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;


@Entity
public class TravelPlan {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "plan_generator")
	private Long id;
	
	@Embedded
	@AttributeOverrides({
		  @AttributeOverride( name = "flexibleTime", column = @Column(name = "flexible_time")),
		  @AttributeOverride( name = "noOfTravelDays", column = @Column(name = "num_travel_days")),
		  @AttributeOverride( name = "travellerType", column = @Column(name = "traveller_type")),
		  @AttributeOverride( name = "travelHoursPerDay", column = @Column(name = "travel_hours_per_day")),
		  @AttributeOverride( name = "mealPreference", column = @Column(name = "meal_pref"))
	})
	private TravellerPreferences travelPreferences;
	
	private String hotelLocation;
	
	private LocalDate travelDate;
	
	private LocalDate travelEndDate;
	
	private String name;
	private String email;
	private Boolean overallValidation;
	private Integer totalDuration;

	public Boolean getOverallValidation() {
		return overallValidation;
	}

	public void setOverallValidation(Boolean overallValidation) {
		this.overallValidation = overallValidation;
	}



	public Integer getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(Integer totalDuration) {
		this.totalDuration = totalDuration;
	}
	
	
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "itinerary_id", referencedColumnName = "id")
	private Itinerary itinerary;
	
    @ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "travel_plan_attractions", joinColumns = @JoinColumn(name = "travel_plan_id"))
	private Set<Long> selectedAttractionIds;
	
    
    @ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "travel_plan_promotions", joinColumns = @JoinColumn(name = "travel_plan_id"))
	private Set<Long> selectedPromotionIds;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TravellerPreferences getTravelPreferences() {
		return travelPreferences;
	}
	public void setTravelPreferences(TravellerPreferences travelPreferences) {
		this.travelPreferences = travelPreferences;
	}
	public String getHotelLocation() {
		return hotelLocation;
	}
	public void setHotelLocation(String hotelLocation) {
		this.hotelLocation = hotelLocation;
	}
	public Itinerary getItinerary() {
		return itinerary;
	}
	public void setItinerary(Itinerary itinerary) {
		this.itinerary = itinerary;
	}
	public Set<Long> getSelectedAttractionIds() {
		return selectedAttractionIds;
	}
	public void setSelectedAttractionIds(Set<Long> selectedAttractionIds) {
		this.selectedAttractionIds = selectedAttractionIds;
	}
	public Set<Long> getSelectedPromotionIds() {
		return selectedPromotionIds;
	}
	public void setSelectedPromotionIds(Set<Long> selectedPromotionIds) {
		this.selectedPromotionIds = selectedPromotionIds;
	}
	public LocalDate getTravelDate() {
		return travelDate;
	}
	public void setTravelDate(LocalDate travelDate) {
		this.travelDate = travelDate;
	}
	public LocalDate getTravelEndDate() {
		return travelEndDate;
	}
	public void setTravelEndDate(LocalDate travelEndDate) {
		this.travelEndDate = travelEndDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
