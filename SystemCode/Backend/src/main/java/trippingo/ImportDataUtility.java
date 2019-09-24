package trippingo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import trippingo.data.PromotionImport;
import trippingo.data.TouristAttractionImportWithRank;
import trippingo.data.TravelDistanceImport;

@Component
public class ImportDataUtility implements CommandLineRunner {
	
    private static final Logger logger = LogManager.getLogger(ImportDataUtility.class);
    
	@Autowired
	private TouristAttractionImportWithRank attractionUtility;
	
	@Autowired
	private PromotionImport promotionUtility;
	
	@Autowired
	private TravelDistanceImport distanceUtility;
	
	@Value("${trippingo.importdata}")
	private boolean importData;

	public static void main(String[] args) {
		SpringApplication.run(ImportDataUtility.class, args);
		
	}
	@Override
	public void run(String... args) throws Exception {
		if(importData) {
			attractionUtility.importData();	
			promotionUtility.importData();
			distanceUtility.importData();
		}
	}
	
	

}
