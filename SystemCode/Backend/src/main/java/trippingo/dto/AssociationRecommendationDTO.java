package trippingo.dto;

import java.util.ArrayList;
import java.util.List;

public class AssociationRecommendationDTO {
	
	private String[] attractionNames;
	
	private List<String> associatedAttractionNames = new ArrayList<String>();

	public List<String> getAssociatedAttractionNames() {
		if(associatedAttractionNames == null)
			associatedAttractionNames = new ArrayList<String>();
		return associatedAttractionNames;
	}

	public void setAssociatedAttractionNames(List<String> associatedAttractionNames) {
		this.associatedAttractionNames = associatedAttractionNames;
	}

	public String[] getAttractionNames() {
		return attractionNames;
	}

	public void setAttractionNames(String[] attractionNames) {
		this.attractionNames = attractionNames;
	}

	public AssociationRecommendationDTO(String[] attractionNames) {
		super();
		this.attractionNames = attractionNames;
	}

	public AssociationRecommendationDTO() {
		super();
	}
	
	
	
	

	
	

	
	

	
	
	
	
	
	
	

}
