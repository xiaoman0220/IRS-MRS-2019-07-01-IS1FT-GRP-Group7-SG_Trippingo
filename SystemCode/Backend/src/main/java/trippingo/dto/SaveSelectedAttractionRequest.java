package trippingo.dto;

import java.io.Serializable;

public class SaveSelectedAttractionRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2006649141704144974L;
	
	private Long[] attractionIds;
	
	public Long[] getAttractionIds() {
		return attractionIds;
	}

	public void setAttractionIds(Long[] attractionIds) {
		this.attractionIds = attractionIds;
	}
	
	
	
	

}
