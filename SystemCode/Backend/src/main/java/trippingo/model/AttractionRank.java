package trippingo.model;

import javax.persistence.Embeddable;

@Embeddable
public class AttractionRank {
	
	private Integer familyRank;
	private Integer coupleRank;
	private Integer friendsRank;
	private Integer businessRank;
	private Integer soloRank;
	public Integer getFamilyRank() {
		return familyRank;
	}
	public void setFamilyRank(Integer familyRank) {
		this.familyRank = familyRank;
	}
	public Integer getCoupleRank() {
		return coupleRank;
	}
	public void setCoupleRank(Integer coupleRank) {
		this.coupleRank = coupleRank;
	}
	public Integer getFriendsRank() {
		return friendsRank;
	}
	public void setFriendsRank(Integer friendsRank) {
		this.friendsRank = friendsRank;
	}
	public Integer getBusinessRank() {
		return businessRank;
	}
	public void setBusinessRank(Integer businessRank) {
		this.businessRank = businessRank;
	}
	public Integer getSoloRank() {
		return soloRank;
	}
	public void setSoloRank(Integer soloRank) {
		this.soloRank = soloRank;
	}
	
	

}
