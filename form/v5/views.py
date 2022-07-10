from django.shortcuts import render
from django.views.generic import TemplateView
from django.views import View

from v5 import translate
from v5 import dictionary
from . import views
import json
import urllib.request


# Create your views here.
class IndexView(TemplateView):
    template_name = "v5/index.html"

class ResultView(View):
    template_name = "v5/result.html"
    #template_name = "v5/contact.html"

    def post(self, request, *args, **kwargs):
        name = request.POST.get('name')
        api_result = translate.papagoAPI(name)
        dic_result, dic2_result = dictionary.freeDictionaryAPI(name)
        #dic2_result = dictionary.freeDictionary2API(name)
        result = {
            'name': request.POST['name'],
            'api_result' : api_result,
            'dic_result' : dic_result,
            'dic2_result' : dic2_result,
        }
        return render(request, self.template_name, result)

"""
    def post(self, request, *args, **kwargs):
        client_id = "I19UAaH8cIC00uw9Z7W_"
        client_secret = "1VTYCR65Hy"

        name = request.POST.get('name')
        encText = urllib.parse.quote("{}".format(name))
        print(name)

        data = "source=ko&target=en&text=" + encText
        url = "https://openapi.naver.com/v1/papago/n2mt"

        papago_api_request = urllib.request.Request(url)
        papago_api_request.add_header("X-Naver-Client-Id",client_id)
        papago_api_request.add_header("X-Naver-Client-Secret",client_secret)
        response = urllib.request.urlopen(papago_api_request, data=data.encode("utf-8"))
        rescode = response.getcode()
        if(rescode==200):
            response_body = response.read()
            print(response_body.decode('utf-8'))
        else:
            print("Error Code:" + rescode)
            
        result = {
            'name': request.POST['name'],
            #'english' : request.POST['english'],
            #'hobby': request.POST['hobby'],
        }
        return render(request, self.template_name, result)
        """

"""
    #papago api
    def post(self, request, *args, **kwargs):
        name = request.POST.get('name')
        api_result = translate.papagoAPI(name)
        result = {
            'api_result' : api_result
        }
        return render(request, self.template_name, result) """


""" 
  def post(self, request, *args, **kwargs):

        result = {
            'name': request.POST['name'],
            #'hobby': request.POST['hobby'],
        }
        return render(request, self.template_name, result) """