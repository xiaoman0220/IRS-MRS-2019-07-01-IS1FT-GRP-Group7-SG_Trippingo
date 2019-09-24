from django.db import models

# Create your models here.



class TravellerInfo:
	Traveller_Type_C	= 	[	
							("Family"	, 	"Family"),
							("Couple" 	, 	"Couple"),
							("Business"	, 	"Business"),
							("Friends"	, 	"Friends"),
							("Solo"		, 	"Solo")
							]


	Category_C 			=	[		
							("Landmark"		,	"Landmarks"),
							("Shopping"		, 	"Shopping"),
							("Museum"		, 	"Museums"),
							("NaturePark"	,	"Nature Park"),
							("AmusementPark",	"Amusement Parks"),
							]

	def __init__(self, 
		Traveller_Name, 
		Traveller_Email, 
		Flexible_Travel, 
		Min_Time_Day, 
		Max_Time_Day, 
		Quick_Meals,
		Traveller_Type_C,
		Traveller_Type,
		Category_C,
		Category,
		Hotel_Postal_Code,
		Start_Date,
		End_Date,
		Travel_Duration
		):

		self.Traveller_Name 	=		Traveller_Name
		self.Traveller_Email	=		Traveller_Email
		self.Flexible_Travel	= 	 	Flexible_Travel
		self.Min_Time_Day		= 		Min_Time_Day
		self.Max_Time_Day		= 		Max_Time_Day
		self.Quick_Meals 		= 		Quick_Meals
		self.Traveller_Type_C	= 		Traveller_Type_C
		self.Traveller_Type 	=		Traveller_Type
		self.Category_C 		=		Category_C
		self.Category 			=		Category	
		self.Hotel_Postal_Code	=		Hotel_Postal_Code
		self.Start_Date	 		=		Start_Date
		self.End_Date			= 		End_Date
		self.Travel_Duration	= 		Travel_Duration
