package trippingo.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

@Entity
public class Promotion {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "promo_generator")
	private Long id;
	
	private String name;
	
	private String url;
	
	private String imageUrl;
	
	@ElementCollection
	@JoinTable(name = "promo_attractions", joinColumns = @JoinColumn(name = "promotion_id"))
	@Column(name = "attraction_id")
	private Set<Long> attractions;
	
	private BigDecimal originalPrice;
	
	private BigDecimal price;
	
	private Integer numBooked;
	
	private Integer numReviews;
	
	private Double rating;
	
	private String seo;
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public Set<Long> getAttractions() {
		return attractions;
	}
	public void setAttractions(Set<Long> attractions) {
		this.attractions = attractions;
	}
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getNumBooked() {
		return numBooked;
	}
	public void setNumBooked(Integer numBooked) {
		this.numBooked = numBooked;
	}
	public Integer getNumReviews() {
		return numReviews;
	}
	public void setNumReviews(Integer numReviews) {
		this.numReviews = numReviews;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public String getSeo() {
		return seo;
	}
	public void setSeo(String seo) {
		this.seo = seo;
	}
	
	
	
	
	
	
	

}
