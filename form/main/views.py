from django.shortcuts import render, redirect
from django.contrib import messages
from .forms import ContentForm
from .models import Content


# Create your views here.
def home(request):
    if request.method == "POST":
        form = ContentForm(request.POST)
        if form.is_valid():
            content_form = form.save(commit=False) #바로 저장되는거 방지
            content_form.save()
            return redirect('result/')
        else:
            messages.error(request, "error")
            return redirect('main/home.html')

    #1 home.html접속하면 여기 실행
    #처음 실행시 post로 요청을 보내는 것이 아니니까
    else: 
        form = ContentForm()
        context = {
            'form' : form #html 문서에 같이 실어보낼 데이터 양식, 예) DB, form양식
        }
        return render(request, 'main/home.html', context)

def result(request):
    result_list = Content.objects.all()
    context = {
        'result_list' : result_list
    }
    return render(request, 'main/result.html', context)