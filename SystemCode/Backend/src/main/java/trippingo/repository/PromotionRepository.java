package trippingo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import trippingo.model.Promotion;

public interface PromotionRepository extends CrudRepository<Promotion, Long>{
	@Query(value = "SELECT p.promotion_id FROM PROMO_ATTRACTIONS p WHERE p.attraction_id IN :attractionIds", nativeQuery = true)
	public List<Long> findAllByAttractionIds(@Param("attractionIds")List<String> attractionIds);
	
	@Query(value = "SELECT p.promotion_id FROM PROMO_ATTRACTIONS p  WHERE p.attraction_id = :attractionId", nativeQuery = true)
	public List<Long> findAllIdsByAttractionId(@Param("attractionId")Long attractionId);
}
