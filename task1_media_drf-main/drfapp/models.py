from django.db import models
from django.urls import reverse
import uuid

# Create your models here.

class ExampleModel(models.Model):
    id = models.UUIDField(default=uuid.uuid4(), primary_key=True)
    assigned = models.BooleanField(default=False)
    img = models.FileField('img', upload_to='img/')
    date_creation = models.DateTimeField(auto_now_add=True)
    date_update = models.DateTimeField(auto_now=True)
    file_url = models.CharField('url', max_length=1000, null=True, blank=True)

    def get_absolute_url(self):
        return reverse("foto", kwargs={"foto": self.img})
