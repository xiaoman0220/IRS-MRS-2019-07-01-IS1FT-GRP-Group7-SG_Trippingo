from django.shortcuts import render, redirect
from .forms import SetTravellerInfoBasic,FlexTravel,NonFlexTravel
import requests

TRIPPINGO_URL="http://localhost:8001/"
APIKEY = 'AIzaSyAgU9a5eTrwZP9pIb0eNuNRu3iPE75tR-8'

BformInput = {}
FformInput = {}


# Create your views here.
def landing(request):
	return render(request,'home.html',{'title' : "SG Travel Planner"} )

def new(request):
	global BformInput
	global FformInput
	if request.method == "POST":
		BasicForm = SetTravellerInfoBasic(request.POST)
		if BasicForm.is_valid():
			BformInput = BasicForm.clean()
			if BformInput["Travel_Dates"] == 'True':
				response = redirect('/NonFlex/')
				return response
			else:
				response = redirect('/Flex/')
				return response
	else:

		args = {
				'Form'	: SetTravellerInfoBasic(),   
				'title' : "More About you",
				}

	return render (request,'new.html', args)



def NFlexyTravel(request):
	global BformInput
	global FformInput

	if not BformInput:
		response = redirect('/new/')
		return response

	if request.method == "POST":
		NonFlex = NonFlexTravel(request.POST)
		if NonFlex.is_valid():
			FformInput = NonFlex.clean()

			resp = POSTData (	BformInput['Your_Name'],
						BformInput['Your_Email'] , 
						FformInput['Hotel_Postal'],
						FformInput['Arrival_Date'].strftime('%Y-%m-%d') ,
						FformInput['Departure_Date'].strftime('%Y-%m-%d') ,
						True, 
						BformInput["How_Are_You_Travelling"], 
						'', 
						FformInput["Max_Hours_Per_Day"],
						"LongMeal", 
						BformInput['Interest'] )
			print (resp)
			caseid = resp[0]["id"]
			print (caseid)
			response = redirect('/travelPlans/'+str(caseid)+'/recommendations')
			#req = {}
			return response



	else :
		args = { 	
					'Form' 	: NonFlexTravel(),
					'title' : "Travelling Preferences!"
				}
		



	return render(request, 'new.html',args)





def FlexyTravel(request):
	global BformInput
	global FformInput

	if not BformInput:
		response = redirect('/new/')
		return response


	if request.method == "POST":
		Flex = FlexTravel(request.POST)
		if Flex.is_valid():
			FformInput = Flex.clean()
			resp = POSTData (	BformInput['Your_Name'],
						BformInput['Your_Email'] , 
						FformInput['Hotel_Postal'],
						'' ,
						'' ,
						True, 
						BformInput["How_Are_You_Travelling"], 
						FformInput["Travel_Duration"], 
						FformInput["Max_Hours_Per_Day"],
						"LongMeal", 
						BformInput['Interest'] )

			print (resp)
			caseid = resp[0]["id"]
			print (caseid)
			response = redirect('/travelPlans/'+str(caseid)+'/recommendations')
			#req = {}
			return response


	else :

		args = { 	
					'Form' 	:  FlexTravel(),
					'title' : "Travelling Preferences!"
				}


	return render(request, 'new.html',args)





# Change this if there is any change in database
def POSTData(name, email, hotelLocation, travelDate, travelEndDate, flexibleTime,travellerType, noOfTravelDays,travelHoursPerDay,mealPreference, categories ):
	data =  {
				"name"				:	name,
				"email"				:	email,
				"hotelLocation"		: 	hotelLocation,
				"travelDate"		: 	travelDate,
				"travelEndDate"		:	travelEndDate,
				"travelPreferences"	: {
										"flexibleTime"		: flexibleTime,
										"travellerType"		: travellerType,
										"noOfTravelDays"	: noOfTravelDays,
										"travelHoursPerDay"	: travelHoursPerDay,
										"mealPreference"	: mealPreference,
										"categories"		: categories
										}
			}
	
	resp = requests.post(TRIPPINGO_URL+"travelPlans",json=data)
	#print (resp.text)
	BformInput = {}
	FformInput = {}


	return  resp.json(),