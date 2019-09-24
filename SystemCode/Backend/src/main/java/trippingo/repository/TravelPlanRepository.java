package trippingo.repository;

import org.springframework.data.repository.CrudRepository;

import trippingo.model.TravelPlan;

public interface TravelPlanRepository  extends CrudRepository<TravelPlan, Long>{

	
}
