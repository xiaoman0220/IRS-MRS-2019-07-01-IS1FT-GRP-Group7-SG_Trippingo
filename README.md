# Trippingo


This repository contains the Django web application for the SG Travel Planner app [trippingo](https://github.com/vidur6789/trippingo)




## Problem Background
In 2018, Singapore received about 18 million International tourists with an average stay of about 3.7 days per visit, making Singapore the 5th most visited city in the world and 2nd in the Asia-Pacific. 25-34 year olds were the largest (23%) age group of visitors and survey data has shown that most visitors within this age group are likely to have travelled using their own itinerary. With more than 200 attractions in Singapore, significant effort is required to find attractions that fit a traveller’s interests, preferences and context. Our project simplifies and eases the process of itinerary planning by filtering attractions to fit the user preferences. The system enhances these recommendations with market basket analysis on the travel history of TripAdvisor users with similar interests. The selected attractions are then used to create an optimised travel itinerary.




## Run Trippingo (Backend)
  - Download jar file from latest [release](https://github.com/vidur6789/trippingo/releases/tag/v2.0) into a new project directory
  - Download datastore.mv.db into a new sub-directory 'data'
  - Navigate to project directory and execute java -jar trippingo.jar in command line
  - API will be available on 8001 port of the localhost


## Run Trippingo-Web (Frontend)

- Use the package manager [pip](https://pip.pypa.io/en/stable/) to install application dependencies




```bash
pip install -r requirements.txt
```




- Start the Django application from [Trippingo-Web project](https://github.com/vidur6789/trippingo-web) directory in terminal
```bash
python manage.py runserver
```


## Project Video
https://youtu.be/uRw_DnBMakg