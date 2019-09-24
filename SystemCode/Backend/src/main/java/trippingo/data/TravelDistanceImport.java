package trippingo.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import trippingo.model.TravelDistance;
import trippingo.repository.TouristAttractionRepository;
import trippingo.repository.TravelDistanceRepository;

@Component
public class TravelDistanceImport {
	
	
	@Autowired
	private TravelDistanceRepository distanceRepository;
	
	@Autowired
	private TouristAttractionRepository attractionRepository;
	
	
	private static final String RELATIVE_FILE_PATH = "\\data\\distance_matrix.csv";
	private static final Logger logger = LogManager.getLogger(TravelDistanceImport.class);
	
	
	public void importData() {
	    String line = "";
	    String cvsSplitBy = ",";
	    try {
	    	String filePath = new File("").getCanonicalPath() + RELATIVE_FILE_PATH;
	    	BufferedReader br = new BufferedReader(new FileReader(filePath));
	    	int lineNum = 0;
	    	String[] columns = null;
	        while ((line = br.readLine()) != null) {
	        	// use comma as separator
	            String[] elements = line.split(cvsSplitBy);
	            if(lineNum == 0) {
	            	columns = elements;
	            }
	            else {
	            	String rowHeader = elements[0];
	            	for(int i =1; i < elements.length; i++) { // start from 1, 0 = rowHeader
//	            		if(rowHeader == columns[i])//ignore when destination same as source
//	            			continue;
	            		try {
	            		TravelDistance distance = parseDistance(rowHeader,  columns[i], elements[i]);
	            		distanceRepository.save(distance);
	            		}
	            		catch(Exception e) {
	            			logger.error("\n" + rowHeader + "," +columns[i]+","+ elements[i], e);
	            		}
	            	}
	            }
	            lineNum++;
	        }
	        logger.info("Travel Distance Import Completed");
	
	    } catch (Exception e) {
	    	logger.error("\nException for attractionName:" , e);
	    }
	}


	private TravelDistance parseDistance(String sourceName, String destinationName, String value) {
		
		TravelDistance distance = new TravelDistance();
		Long fromAttractionId = attractionRepository.findByName(sourceName).getId();
		distance.setFromAttractionId(fromAttractionId);
		Long toAttractionId = attractionRepository.findByName(destinationName).getId();
		distance.setToAttractionId(toAttractionId);
		distance.setValue(Double.parseDouble(value));
		return distance;
	}

}

