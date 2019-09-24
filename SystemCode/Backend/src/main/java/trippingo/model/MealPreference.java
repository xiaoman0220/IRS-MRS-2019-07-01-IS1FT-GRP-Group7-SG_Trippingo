package trippingo.model;

public enum MealPreference {
	LongMeal(1.5), 
	ShortMeal(0.5);
	
	private Double mealHours;
	private MealPreference(Double mealHours) {
		this.mealHours = mealHours;
	}
	
	private Double getMealHours() {
		return this.mealHours;
	}

}
