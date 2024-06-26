"""
URL configuration for backend project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path

from gymarc import views

urlpatterns = [
    path('admin/', admin.site.urls),
    path('sign-up/', views.sign_up),
    path('sign-in/', views.sign_in),
    path('user-data/', views.user_data),
    path('weekly-training/', views.weekly_training),
    path('create-routine/', views.create_routine),
    path('get-exercises/', views.get_exercise)
]
