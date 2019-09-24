from django.urls import path
from . import views

urlpatterns	=	[
					path('',views.landing, name= "landing_page"),
					path('new/',views.new, name= "new_page"),
					path('Flex/',views.FlexyTravel, name = "Flexible_travel"),
					path('NonFlex/',views.NFlexyTravel, name = "Non_Flexible_travel")
				]