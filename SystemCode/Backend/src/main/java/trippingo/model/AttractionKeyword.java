package trippingo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="keyword")
public class AttractionKeyword {
	
	private String keyword;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "keyword_generator")
	private Long id;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public AttractionKeyword(String keyword) {
		super();
		this.keyword = keyword;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	
	
	
}
