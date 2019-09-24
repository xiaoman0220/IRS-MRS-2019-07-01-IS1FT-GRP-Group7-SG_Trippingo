

package trippingo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import trippingo.model.TouristAttraction;

public interface TouristAttractionRepository extends CrudRepository<TouristAttraction, Long> {
	
	@Query("SELECT a FROM TouristAttraction a WHERE a.name = :name")
	public TouristAttraction findByName(@Param("name")String name);
	
	@Query("SELECT a FROM TouristAttraction a WHERE a.keywords LIKE CONCAT('%',LOWER(:keyword),'%')")
	public List<TouristAttraction> findAllByKeyword(@Param("keyword")String keyword);
	
	@Query(value = "SELECT c.attraction_id FROM ATTRACTION_CATEGORY c WHERE c.categories IN :categories", nativeQuery = true)
	public List<Long> findAllByCategory(@Param("categories")List<String> categories);
	
	@Query("SELECT a FROM TouristAttraction a WHERE UPPER(a.name) LIKE CONCAT('%',UPPER(:name),'%')")
	public List<TouristAttraction> findAllByName(@Param("name")String name);

}
