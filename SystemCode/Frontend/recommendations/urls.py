from django.urls import path
from . import views

urlpatterns = [
    path("<int:pk>/selectedAttractions", views.selectedAttractions, name="recommendations"),
    path("<int:pk>/itinerary", views.itinerary, name="itinerary"),
    path("<int:pk>/recommendations", views.recommendations_pk, name="recommendations_pk"),
    path("<int:pk>/planning", views.planning, name="planning"),
]