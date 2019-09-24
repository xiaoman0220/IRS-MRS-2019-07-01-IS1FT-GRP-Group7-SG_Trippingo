package rules;
import java.util.Arrays;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import trippingo.dto.AssociationRecommendationDTO;
import trippingo.model.TouristAttraction;


public class AssociationRulesRecommender {
	public static AssociationRecommendationDTO[] fetchAssociatedAttractions(AssociationRecommendationDTO[] inputs) {
		if (inputs == null || inputs.length == 0)
			return inputs;

        KieServices kieServices = KieServices.Factory.get();
        KieContainer kContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession_long = kContainer.newKieSession("ksession-long-rules");
		KieSession kieSession_short = kContainer.newKieSession("ksession-short-rules");
        
        TouristAttraction[] attractions = Arrays.stream(inputs).map(AssociationRecommendationDTO::getAttractionNames).flatMap(Arrays::stream)
        		.map(AssociationRulesRecommender::mapToTouristAttraction)
        		.toArray(TouristAttraction[]::new);
        if (attractions.length > 1){
			for (TouristAttraction attractionName: attractions) {
				kieSession_long.insert(attractionName);
			}

			AssociationRecommendationDTO recommendation = new AssociationRecommendationDTO();
			kieSession_long.insert(recommendation);
			kieSession_long.fireAllRules();
			return new AssociationRecommendationDTO[] {recommendation};
		}
		else{
			for (TouristAttraction attractionName: attractions) {
				kieSession_short.insert(attractionName);
			}

			AssociationRecommendationDTO recommendation = new AssociationRecommendationDTO();
			kieSession_short.insert(recommendation);
			kieSession_short.fireAllRules();
			return new AssociationRecommendationDTO[] {recommendation};
		}


    }
	
	private static TouristAttraction mapToTouristAttraction(String name) {
		TouristAttraction attraction  = new TouristAttraction();
		attraction.setName(name);
		return attraction;
	}
		
}
