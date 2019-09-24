package trippingo.model;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import trippingo.datatype.Range;


@Entity
@Table(name="attraction")
public class TouristAttraction {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "attraction_generator")
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, length=4000)
	private String description;
	
	@Column(nullable = false)
	@ElementCollection(targetClass = AttractionCategory.class)
	@Enumerated(EnumType.STRING)
	@JoinTable(name = "attraction_category", joinColumns = @JoinColumn(name = "attraction_id"))
	private Set<AttractionCategory> categories;
	
	private LocalTime openingTime;
	
	private LocalTime closingTime;
	
	@Embedded
	@AttributeOverrides({
		  @AttributeOverride( name = "from", column = @Column(name = "duration_from")),
		  @AttributeOverride( name = "to", column = @Column(name = "duration_to")),
	})
	private Range recommendedDuration;
	
	private BigDecimal price;
	
	private String postalCode;
	@Column(length=4000)
	private String keywords;
	
//	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
//	@JoinColumn(name="ATTRACTION_ID")
	@Transient
	private Set<Review>reviews;
	
	@Embedded
	@AttributeOverrides({
		  @AttributeOverride( name = "familyRank", column = @Column(name = "family_rank")),
		  @AttributeOverride( name = "coupleRank", column = @Column(name = "couple_rank")),
		  @AttributeOverride( name = "friendsRank", column = @Column(name = "friends_rank")),
		  @AttributeOverride( name = "businessRank", column = @Column(name = "business_rank")),
		  @AttributeOverride( name = "soloRank", column = @Column(name = "solo_rank")),
	})
	private AttractionRank attractionRank;
	
	private Boolean isOutdoor;
	
	
	
	@Transient
	private List<Promotion> promotions;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<AttractionCategory> getCategories() {
		return categories;
	}
	public void setCategories(Set<AttractionCategory> categories) {
		this.categories = categories;
	}
	public AttractionRank getAttractionRank() {
		return attractionRank;
	}
	public void setAttractionRank(AttractionRank attractionRank) {
		this.attractionRank = attractionRank;
	}
	public LocalTime getOpeningTime() {
		return openingTime;
	}
	public void setOpeningTime(LocalTime openingTime) {
		this.openingTime = openingTime;
	}
	public LocalTime getClosingTime() {
		return closingTime;
	}
	public void setClosingTime(LocalTime closingTime) {
		this.closingTime = closingTime;
	}
	public Range getRecommendedDuration() {
		return recommendedDuration;
	}
	public void setRecommendedDuration(Range recommendedDuration) {
		this.recommendedDuration = recommendedDuration;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public Set<Review> getReviews() {
		return reviews == null ? Collections.emptySet():reviews;
	}
	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}
	public Boolean getIsOutdoor() {
		return isOutdoor;
	}
	public void setIsOutdoor(Boolean isOutdoor) {
		this.isOutdoor = isOutdoor;
	}
	public List<Promotion> getPromotions() {
		return promotions;
	}
	public void setPromotions(List<Promotion> promotions) {
		this.promotions = promotions;
	}
	
	public int getOpeningTimeGrains() {
		if(openingTime == null )
			return 0;
		return ( getOpeningTime().getHour() * 60 + getOpeningTime().getMinute()) / 15;
	}
	
	public int getClosingTimeGrains() {
		if(closingTime == null )
			return 95;
		return ( getClosingTime().getHour() * 60 + getClosingTime().getMinute()) / 15;
	}
	
	public int getDurationTimeGrains() {
		return (this.getMaxDurationTimeGrains() + this.getMinDurationTimeGrains())/2;
	}
	
	public int getMaxDurationTimeGrains() {
		if(getRecommendedDuration() == null )
			return 1;
		if(getRecommendedDuration().getTo() == -1 )
			return Double.valueOf(getRecommendedDuration().getFrom()).intValue() +1 ;
		return (Double.valueOf(getRecommendedDuration().getTo()).intValue())*4;
	}
	
	public int getMinDurationTimeGrains() {
		if(getRecommendedDuration() == null )
			return 1;
		return (Double.valueOf(getRecommendedDuration().getFrom()).intValue())*4;
	}
}
	
