package trippingo.dto;

import java.io.Serializable;

public class SaveSelectedPromotionRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5423856514852838522L;
	
	private Long[] promotionIds;

	public Long[] getPromotionIds() {
		return promotionIds;
	}

	public void setPromotionIds(Long[] promotionIds) {
		this.promotionIds = promotionIds;
	}
	
	
	
	

}
