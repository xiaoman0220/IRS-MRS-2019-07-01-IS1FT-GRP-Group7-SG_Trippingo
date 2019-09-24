package rules;
import java.util.Arrays;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import trippingo.dto.AssociationRecommendationDTO;
import trippingo.model.DayPlan;
import trippingo.model.TouristAttraction;
import trippingo.model.TravelPlan;
import trippingo.model.TravellerPreferences;


public class DroolsRuleEngineExample {
	public static final void main(String[] args) {

        KieServices kieServices = KieServices.Factory.get();
        KieContainer kContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kContainer.newKieSession("ksession-rules");
        TouristAttraction att1 = new TouristAttraction();
        TouristAttraction att2 = new TouristAttraction();
        att1.setName("The Helix Bridge");
        att2.setName("Gardens by the Bay");
        String[] attractionNames = new String[] {"The Helix Bridge","Gardens by the Bay"};
        TouristAttraction[] attractions = Arrays.stream(attractionNames).map(DroolsRuleEngineExample::mapToTouristAttraction).toArray(TouristAttraction[]::new);
        for (TouristAttraction attractionName: attractions) {
        	TouristAttraction attraction = new TouristAttraction();
        	attraction.setName(attractionName.getName());
    		kieSession.insert(attractionName);
    	}
        
        
        
//        kieSession.insert(att1);
//        kieSession.insert(att2);
        AssociationRecommendationDTO recommendation = new AssociationRecommendationDTO();
        kieSession.insert(recommendation );
        
        TravelPlan plan = new TravelPlan();
        plan.setTotalDuration(20);
        TravellerPreferences pref = new TravellerPreferences();
        pref.setNoOfTravelDays(5);
        pref.setTravelHoursPerDay(8);
        plan.setTravelPreferences(pref);
        //plan.setSetOverallValidation(true);
        DayPlan dp = new DayPlan();
        dp.setDailyTotalDuration(10);
        kieSession.insert(dp);
        kieSession.insert(plan);
        kieSession.fireAllRules();
        System.out.println("\nAssociated Attractions:");
        recommendation.getAssociatedAttractionNames().forEach(System.out::println);

    }
	
	private static TouristAttraction mapToTouristAttraction(String name) {
		TouristAttraction attraction  = new TouristAttraction();
		attraction.setName(name);
		return attraction;
	}
		
}
