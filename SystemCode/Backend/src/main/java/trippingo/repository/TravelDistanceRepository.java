package trippingo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import trippingo.model.TravelDistance;

public interface TravelDistanceRepository extends CrudRepository<TravelDistance, Long>{

	
	@Query("SELECT a FROM TravelDistance a WHERE a.fromAttractionId = :from and a.toAttractionId = :to")
	public TravelDistance fetchDistanceBetween(@Param("from")Long fromId, @Param("to")Long toId);
	
}
