# Generated by Django 4.1.3 on 2023-01-19 12:03

from django.db import migrations, models
import uuid


class Migration(migrations.Migration):

    dependencies = [
        ('drfapp', '0002_alter_examplemodel_id'),
    ]

    operations = [
        migrations.AlterField(
            model_name='examplemodel',
            name='id',
            field=models.UUIDField(default=uuid.UUID('9c04d590-8a23-4a6e-97c4-cc53dd18a1ee'), primary_key=True, serialize=False),
        ),
    ]
