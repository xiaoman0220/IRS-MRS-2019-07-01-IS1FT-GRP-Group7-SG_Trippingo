from django.shortcuts import render, redirect
import requests
import time
from django.http import JsonResponse
from recommendations.models import Recommendation

# Create your views here.


TRIPPINGO_URL="http://localhost:8001"
APIKEY = 'AIzaSyAgU9a5eTrwZP9pIb0eNuNRu3iPE75tR-8'


def recommendations(request):

	print("Entered recommendations")
	recs=trippingoRecommendations(request)
	context = {
		"recs":recs,
		"selectedRecs":[rec for rec in recs if rec.selected],
		"pk":"123"
	}
	return render(request, 'recommendations.html', context)


def recommendations_pk(request, pk):
	print("Entered recommendations_pk")
	recs=trippingoRecommendations(request)
	context = {
		"recs":recs,
		"selectedRecs":[rec for rec in recs if rec.selected],
		"pk":pk
	}
	return render(request, 'recommendations.html', context)


def selectedAttractions(request,pk):

	print("Entered selectedAttractions")
	selected_attractions = [int(key) for key, value in request.POST.dict().items() if value == 'on']
	print(selected_attractions)
	saveSelectedAttractions(1, selected_attractions)
	return redirect('../{pk}/planning'.format(pk=pk))

def planning(request, pk):
	print("Entered planning")
	return render(request, 'planning.html', {"pk":pk})


def itinerary(request,pk):
	print("Entered itinerary")
	itinerary_resp = plan_itinerary(pk)
	#itinerary = dict(itinerary_resp.json())

	itinerary = {
		"itineraryId": 3203,
		"dayPlans": [
			{"id": 3205, "travelDate": "2019-09-23", "serialNo": 1,
			 "attractionVisit": [
				 {
					 "serialNo": 200,
					 "timeofDay": "17:15:00",
					 "attraction": {
						 "id": 7,
						 "name": "Singapore Flyer",
						 "description": "At 165 metres tall, Singapore Flyer is a masterpiece of urban architecture and engineering that showcases not only the mesmerizing cosmopolitan cityscape of the tropical Lion City, but even the surrounding islands of Indonesia and parts of Malaysia in all their glory. In addition to offering panoramic views of Singapore's cosmopolitan cityscape, guests can also indulge in a flute of champagne, or savour the iconic Singapore Sling whilst hosted in a special themed capsule. Diners seeking both privacy and luxury can opt for a multi-sensory treat unlike any other with our Premium Sky Dining Flight, complete with a four-course dinner and an in-flight host.",
						 "categories": ["Landmark"], "openingTime": "08:30:00", "closingTime": "22:00:00",
						 "recommendedDuration": {"from": 2.0, "to": 3.0},
						 "price": 22, "postalCode": "039803", "keywords": "", "reviews": [],
						 "attractionRank": {"familyRank": 4, "coupleRank": 6, "friendsRank": 8, "businessRank": 72,
											"soloRank": 6},
						 "isOutdoor": 1, "promotions": 22, "openingTimeGrains": 34, "closingTimeGrains": 88,
						 "durationTimeGrains": 10, "maxDurationTimeGrains": 12,
						 "minDurationTimeGrains": 8}
				 },
				 {
					 "serialNo": 200,
					 "timeofDay": "18:15:00",
					 "attraction": {
						 "id": 7,
						 "name": "Gardens by the Bay",
						 "description": "At 165 metres tall, Singapore Flyer is a masterpiece of urban architecture and engineering that showcases not only the mesmerizing cosmopolitan cityscape of the tropical Lion City, but even the surrounding islands of Indonesia and parts of Malaysia in all their glory. In addition to offering panoramic views of Singapore's cosmopolitan cityscape, guests can also indulge in a flute of champagne, or savour the iconic Singapore Sling whilst hosted in a special themed capsule. Diners seeking both privacy and luxury can opt for a multi-sensory treat unlike any other with our Premium Sky Dining Flight, complete with a four-course dinner and an in-flight host.",
						 "categories": ["Landmark"], "openingTime": "08:30:00", "closingTime": "22:00:00",
						 "recommendedDuration": {"from": 2.0, "to": 3.0},
						 "price": 22, "postalCode": "039803", "keywords": "", "reviews": [],
						 "attractionRank": {"familyRank": 4, "coupleRank": 6, "friendsRank": 8, "businessRank": 72,
											"soloRank": 6},
						 "isOutdoor": 1, "promotions": 22, "openingTimeGrains": 34, "closingTimeGrains": 88,
						 "durationTimeGrains": 10, "maxDurationTimeGrains": 12,
						 "minDurationTimeGrains": 8}
				 }
			 ]},

			{"id": 3204, "travelDate": "2019-09-24", "serialNo": 2, "attractionVisit": [
				{"serialNo": 300, "timeofDay": "09:30:00", "attraction": {"id": 1, "name": "Sentosa",
																		  "description": "An integral part of Singapore's \"City in a Garden\" vision, Gardens by the Bay spans a total of 101 hectares of prime land at the heart of Singapore's new downtown - Marina Bay. Comprising three waterfront gardens - Bay South, Bay East and Bay Central - Gardens by the Bay will be a showcase of horticulture and garden artistry that will bring the world of plants to Singapore and present Singapore to the World.",
																		  "categories": ["NaturePark", "Landmark"],
																		  "openingTime": "05:00:00",
																		  "closingTime": "02:00:00",
																		  "recommendedDuration": {"from": 3.0,
																								  "to": -1.0},
																		  "price": 2222, "postalCode": "018953",
																		  "keywords": "", "reviews": [],
																		  "attractionRank": {"familyRank": 1,
																							 "coupleRank": 1,
																							 "friendsRank": 1,
																							 "businessRank": 1,
																							 "soloRank": 1},
																		  "isOutdoor": 2, "promotions": 2,
																		  "openingTimeGrains": 20,
																		  "closingTimeGrains": 8,
																		  "durationTimeGrains": 8,
																		  "maxDurationTimeGrains": 4,
																		  "minDurationTimeGrains": 12}}]}
		]
	}
	for dayp in itinerary["dayPlans"]:
		for j in dayp["attractionVisit"]:
			daytime = time.strptime(j["timeofDay"], "%H:%M:%S")
			j["timeofDay"] = str(daytime.tm_hour)+":"+str(daytime.tm_min)


	attrName = ''
	hotel = ''
	API_KEY = 'AIzaSyCZ4Q24NuUFkO3sQBdtcwNxSnO3qwZqwW8'

	attrList = [0 for _ in range(len(itinerary['dayPlans']))]
	attrGeometry = [0 for _ in range(len(itinerary['dayPlans'][0]['attractionVisit']))]  # record geometry of each attraction
	travelPlanCoordinates = [0 for _ in range(len(itinerary['dayPlans']))]  # travelPlanCoordinates list with geometry of attractions(the jth attraction of i day)
	a = [0 for _ in range(len(itinerary['dayPlans'][0]['attractionVisit']))]

	centerGeometry = [0 for _ in range(len(itinerary['dayPlans']))]  # #record average Geometry (centre point) of each day

	timeperDay = [0 for _ in range(len(itinerary['dayPlans'][0]['attractionVisit']))]  # record visit time of each attraction
	timeofDays = [0 for _ in range(len(itinerary['dayPlans']))]  ##record visit time of all attractions

	direc_url = [0 for _ in range(len(itinerary['dayPlans'][0]['attractionVisit']))]
	direct = [0 for _ in range(len(itinerary['dayPlans']))]

	attrLatList = [0 for _ in range(len(itinerary['dayPlans']))]
	attrLngList = [0 for _ in range(len(itinerary['dayPlans']))]

	for i in range(len(itinerary['dayPlans'])):  # for i in numverofDays
		attrLat = [_ for _ in range(len(itinerary['dayPlans'][i]['attractionVisit']))]
		attrLng = [_ for _ in range(len(itinerary['dayPlans'][i]['attractionVisit']))]
		lat_sum = 0  # record the sum of attractions latitude per day
		lng_sum = 0  # record the sum of attractions lagnitude per day
		for j in range(len(itinerary['dayPlans'][i]['attractionVisit'])):  # for j in numverofAttracttions per day

			attrName = itinerary['dayPlans'][i]['attractionVisit'][j]['attraction']['name']
			if hotel == '' and j == 0:
				lastAttr = ''
			elif j == 0:
				lastAttr = hotel
			else:
				lastAttr = itinerary['dayPlans'][i]['attractionVisit'][j - 1]['attraction']['name']

			API_ENDPOINT = f"https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input={attrName}&inputtype=textquery&fields=geometry&key={API_KEY}"
			attrlocation = getjson(API_ENDPOINT)
			attrGeometry[j] = attrlocation['candidates'][0]['geometry']['location']
			attrLat[j] = attrGeometry[j]['lat']
			attrLng[j] = attrGeometry[j]['lng']
			lat_sum = lat_sum + attrGeometry[j]['lat']
			lng_sum = lng_sum + attrGeometry[j]['lng']

			timeperDay[j] = itinerary['dayPlans'][i]['attractionVisit'][j]['timeofDay']

			direc_url[j] = f"https://www.google.com/maps/dir/?api=1&origin={lastAttr}&destination={attrName} Singapore&travelmode=transit"

		attrList[i] = attrName
		travelPlanCoordinates[i] = attrGeometry[j]
		attrLatList[i] = attrLat
		attrLngList[i] = attrLng

		centerGeometry[i] = {'lat': lat_sum / len(itinerary['dayPlans'][i]['attractionVisit']),
							 'lng': lng_sum / len(itinerary['dayPlans'][i]['attractionVisit'])}
		timeofDays[i] = timeperDay
		direct[i] = direc_url

	print("attrLatList: ", attrLatList)
	print("attrLngList: ", attrLngList)

	context = {
		"dayPlans": itinerary["dayPlans"],
		"pk": pk,
		"travelPlanCoordinates":travelPlanCoordinates,
		"attrLatList":attrLatList,
		"attrLngList":attrLngList
	}
	return render(request, 'itinerary.html', context)


def trippingoRecommendations(req):
	keywords = 'Landmark'
	travellerType = 'Family'
	url = TRIPPINGO_URL + "/recommendations?keywords={keywords}&travellerType={travellerType}&count=3".format(
	    keywords=keywords, travellerType=travellerType);
	print(url)
	api_response = getjson(url)
	recommendations = [mapToModel(rec) for rec in api_response]
	return recommendations


def saveSelectedAttractions(travel_plan_id, selected_attractions):
	url = TRIPPINGO_URL + "/travelPlans/{id}/selectedAttractions".format(id=travel_plan_id);
	print(url)
	data = {"attractionIds":selected_attractions}
	print(data)
	resp = requests.put(url, json=data)
	print(resp.text)


def plan_itinerary(travel_plan_id):
	url = TRIPPINGO_URL + "/travelPlans/{id}/itinerary".format(id=travel_plan_id);
	print(url)
	data = {}
	resp = requests.put(url, json=data)
	return resp


def mapToModel(rec):
	description = rec['description'][:150] + "..."
	name = rec['name']
	return Recommendation(rec['id'], name, description, getPhoto(name))



def getjson(url):
	resp = requests.get(url)
	return resp.json()


def getPhoto(attrname):
	API_ENDPOINT = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input={attrname}&inputtype=textquery&fields=photos&key={APIKEY}".format(attrname=attrname, APIKEY=APIKEY)
	info = getjson(API_ENDPOINT)
	photo_ref = info['candidates'][0]['photos'][0]['photo_reference']
	photo_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference={photo_ref}&key={APIKEY}".format(photo_ref=photo_ref, APIKEY=APIKEY)
	return photo_url
