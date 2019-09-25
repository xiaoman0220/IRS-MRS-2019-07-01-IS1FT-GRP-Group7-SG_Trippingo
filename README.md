
## SECTION 1 : PROJECT TITLE
## SG Trippingo - Singapore Attractions Recommender & Itinerary Planner

<img src="SystemCode/cover.png"
     style="float: left; margin-right: 0px;" />


---
## SECTION 2 : EXECUTIVE SUMMARY / PAPER ABSTRACT
In 2018, Singapore received about 18 million International tourists with an average stay of about 3.7 days per visit, making Singapore the 5th most visited city in the world and 2nd in the Asia-Pacific.

25-34 year olds were the largest (23%) age group of visitors and survey data has shown that most visitors within this age group are likely to have travelled using their own itinerary. With more than 200 attractions in Singapore, significant effort is required to find attractions that fit a traveller’s interests, preferences and context. Besides finding the right attractions, it is extremely inconvenient to find the best route between attractions manually by searching every attraction on Google Maps.

Based on the above considerations, our project aims to simplify and ease the process of itinerary planning by filtering attractions to fit the user preferences and automatically search for the best travel route between attractions.

The system consists of two parts: firstly, attraction recommendation. The system recommends attractions to travellers based on their personal preferences through a ranking mechanism and  enhances these recommendations with market basket analysis on the travel history of  TripAdvisor users with similar interests through Drools Rule Engine. 
Secondly, itinerary optimization. The system allows travellers to select from the recommended attractions and the selected attractions are then used to create an optimised travel itinerary by performing an optimization problem through optaplanner.

For the final result, travellers will get a full picture of how their trips in Singapore will be like: the best combinations of attractions for every day, directions between attractions, and even the promotions.

---
## SECTION 3 : CREDITS / PROJECT CONTRIBUTION

| Official Full Name  | Student ID  | Work Items |
| :------------ |:---------------:| :-----|
| Vidur Puliani | A0198492L | Klook Web Scraping, Promotion-Attraction Matching, Spring Boot Application and Database design, Opta-planner integration, Recommendation User Interface|
| Gong Yifei | A0198495E | Data preprocessing, Prototype design, Diagrams, Implementation of rules, Itinerary interface, Video|
| Li Jingmeng| A0198484J | Data preprocessing, Association Mining, Association Knowledge Representation, Association Recommendation User Interface|
| Jiang Yanni | A0201097M | Data Preprocessing, Ranking Processing, DIstance Matrix Collection, Google Maps Interaction and Interface|
| Ngo Jinze Donal | A0198487A | TripAdvisor Web Scraping, Data Preprocessing, Opta-planner Integration, Promotion-Attraction Matching, Web Input interface|

---
## SECTION 4 : VIDEO OF SYSTEM MODELLING & USE CASE DEMO

[Project Video](https://www.youtube.com/watch?v=uRw_DnBMakg)


---
## SECTION 5 : USER GUIDE

`<Github File Link>` : https://github.com/xiaoman0220/IRS-MRS-2019-07-01-IS1FT-GRP-Group7-SG_Trippingo/blob/master/UserGuide/User%20Guide%20SG%20Trippingo.pdf

### [ 1 ] Run Trippingo (Backend)

> Download jar file from latest [release](https://github.com/vidur6789/trippingo/releases/tag/v2.0) into a new project directory

> Download datastore.mv.db into a new sub-directory 'data'

> Navigate to project directory and execute the command below in command line:

```bash
java -jar trippingo.jar
```

> API will be available on 8001 port of the localhost

### [ 2 ] Run Trippingo-Web (Frontend)

> Use the package manager [pip](https://pip.pypa.io/en/stable/) to install application dependencies

```bash
pip install -r requirements.txt
```

> Start the Django application from [Trippingo-Web project](https://github.com/vidur6789/trippingo-web) directory in terminal

```bash
python manage.py runserver
```

---
## SECTION 6 : PROJECT REPORT / PAPER

`<Github File Link>` : <https://github.com/telescopeuser/Workshop-Project-Submission-Template/blob/master/ProjectReport/Project%20Report%20HDB-BTO.pdf>

---
## SECTION 7 : MISCELLANEOUS

### AttractionsData.xlsx
* Singapore attractions data scraped from Tripadvisor
* Includes basic information and reviews, which were subsequently used in our system
