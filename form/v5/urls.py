from django.urls import path
from . import views

app_name = 'v5'


urlpatterns = [
    path('', views.IndexView.as_view(), name='index'),
    path('result', views.ResultView.as_view(), name='result'),
]