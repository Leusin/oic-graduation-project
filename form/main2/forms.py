from django import forms
from .models import Blog
 
class BlogModelForm(forms.ModelForm):
    class Meta:
        model = Blog
        # fields = '__all__'
        fields = ['title', 'body']