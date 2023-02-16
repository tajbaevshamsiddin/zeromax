from rest_framework import serializers

from .models import *


class ExampleSerializer(serializers.ModelSerializer):
    class Meta:
        model = ExampleModel
        fields = "__all__"
        extra_kwargs = {'img': {'write_only': True}}

