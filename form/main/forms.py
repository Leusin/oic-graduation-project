from django import forms
from .models import Content

class ContentForm(forms.ModelForm):
    def __init(self, *args, **kwargs):
        super(ContentForm, self).__init__(*args, **kwargs)
        self.fields['content'].widget.attrs.update({
            'class':'form-control',
            'autofocus':True
        })

    class Meta:
        model = Content
        fields = ['content']