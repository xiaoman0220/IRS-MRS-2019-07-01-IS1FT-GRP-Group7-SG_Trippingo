package trippingo.repository;

import org.springframework.data.repository.CrudRepository;

import trippingo.model.Itinerary;

public interface ItineraryRepository extends CrudRepository<Itinerary, Long>{

}
