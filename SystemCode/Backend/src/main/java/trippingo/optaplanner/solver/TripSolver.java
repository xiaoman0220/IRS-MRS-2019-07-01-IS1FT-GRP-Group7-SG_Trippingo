/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package trippingo.optaplanner.solver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import trippingo.model.TouristAttraction;
import trippingo.model.TravellerPreferences;
import trippingo.optaplanner.resources.Day;
import trippingo.optaplanner.resources.PlanAttraction;
import trippingo.optaplanner.resources.TimeGrain;
import trippingo.optaplanner.resources.TripPlanner;
import trippingo.service.AttractionRecommendationController;
import trippingo.service.TouristAttractionController;


@Component
public class TripSolver implements  CommandLineRunner {
	
	
	@Autowired
	private TouristAttractionController service;
	
	private static final Logger logger = LogManager.getLogger(TripSolver.class);
	
	
	public TripPlanner optimizeItinerary(List<TouristAttraction> attractions, TravellerPreferences travellerPreferences) {
		 // Build the Solver
        SolverFactory<TripPlanner> solverFactory = SolverFactory.createFromXmlResource("OptaplannerConfig.xml");
        Solver<TripPlanner> solver = solverFactory.buildSolver();
        TripPlanner unsolvedTripSolver = new TripPlanner ();
        List<PlanAttraction> planAttractions=  attractions.stream()
        		.map(this::mapToPlanAttraction)
        		.collect(Collectors.toList());
		unsolvedTripSolver.setAttraction(planAttractions);
		int maxHours = 14;
		int diff = maxHours - travellerPreferences.getTravelHoursPerDay().intValue();
		int start = 32 + (diff/2)*4;
		int end =start + travellerPreferences.getTravelHoursPerDay().intValue()*4;
		unsolvedTripSolver.setTimeGrainSlots(generateTimeGrains(travellerPreferences.getNoOfTravelDays(), start, end));

		// Solve the problem
		try {
			TripPlanner bestTripPlanner=null;
			for (int i=0; i<1;i++) {
				unsolvedTripSolver.setAttraction(planAttractions);
				TripPlanner solvedTripSolver = solver.solve(unsolvedTripSolver);
				logger.info(solvedTripSolver.toString());
				
				if(bestTripPlanner == null || solvedTripSolver.getScore().getHardScore() > bestTripPlanner.getScore().getHardScore()) {
					bestTripPlanner = solvedTripSolver;
					logger.info("Found better score" + bestTripPlanner.getScore().getHardScore() + ",Soft Score:"+bestTripPlanner.getScore().getSoftScore());
				}
			}
			return bestTripPlanner;
		}
		catch(Exception e) {
			logger.error("Error thrown in TripSolver.optimizeItinerary", e);
			return unsolvedTripSolver;
		}
	}
	
	
	private List<TimeGrain> generateTimeGrains(int noOfDays, int startTimeSlot, int lastTravelSlot) {
		List<Day> days  = new ArrayList<Day>();
		for(int i=1; i < noOfDays+1; i++) {
			Day day = new Day();
			day.setDayOfYear(i);
			days.add(day);
		}
		
		days.stream().forEach(d -> {d.setFirstTravelSlot(startTimeSlot); d.setLastTravelSlot(lastTravelSlot);});
		List <TimeGrain> timeGrainSlots = new ArrayList<TimeGrain>();
		for(Day day: days) {
			for(int i=0; i <24*4;i++) {
				if(i >=startTimeSlot && i <=lastTravelSlot) {
					TimeGrain timeGrain = new TimeGrain();
					timeGrain.setDay(day);
					timeGrain.setGrainIndex(i);
					String id = day.getDayOfYear() + "" + i;
					timeGrain.setId(id);
					timeGrainSlots.add(timeGrain);
				}
			}
		}
		return timeGrainSlots;	
	}

	
	private PlanAttraction mapToPlanAttraction(TouristAttraction attraction) {
		PlanAttraction  planAttr = new PlanAttraction();
		planAttr.setAttraction(attraction);
		planAttr.setStartingTimeGrain(new TimeGrain());
		planAttr.getStartingTimeGrain().setGrainIndex(0);
		planAttr.setService(service);
		return planAttr;
		
	}

	
	@Override
	public void run(String... args) throws Exception {
//		List<TouristAttraction> attractions = service.fetchAttractions(null, null).subList(0, 15);
//		TravellerPreferences preferences = new TravellerPreferences();
//		TripPlanner solvedTripPlanner = optimizeItinerary(attractions, preferences);
//		System.out.println(solvedTripPlanner.toString());
	}


}



