package trippingo.datatype;

import javax.persistence.Embeddable;

@Embeddable
public class Range {

	private Double from;
	private Double to;
	public Double getFrom() {
		return from;
	}
	public void setFrom(Double from) {
		this.from = from;
	}
	public Double getTo() {
		return to;
	}
	public void setTo(Double to) {
		this.to = to;
	}
	
	public Range() {
		
	}
	public Range(Double from, Double to) {
		super();
		this.from = from;
		this.to = to;
	}
	
	public String toString() {
		return String.join("", this.from.toString(), "-", this.to.toString());
	}
	public double MeanRange() {
		return (this.from +this.to) / 2;
	}
	
	
	
	
}
