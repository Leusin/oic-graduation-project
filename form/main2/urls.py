from django.urls import path
from .import views

#app별 관리를 위해 이름 지정
app_name = 'main2'
urlpatterns = [
    path('', views.modelformcreate, name="modelformcreate"),
]