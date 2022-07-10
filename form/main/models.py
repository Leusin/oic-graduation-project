from django.db import models

# Create your models here.
class Content(models.Model):
    content = models.TextField(verbose_name='content')

    def __str__(self):
        return self.content
