# Generated by Django 4.1.3 on 2023-01-24 15:17

from django.db import migrations, models
import uuid


class Migration(migrations.Migration):

    dependencies = [
        ('drfapp', '0007_alter_examplemodel_id'),
    ]

    operations = [
        migrations.AlterField(
            model_name='examplemodel',
            name='id',
            field=models.UUIDField(default=uuid.UUID('8579feaa-da2a-4491-b7aa-99b44cc95ff0'), primary_key=True, serialize=False),
        ),
    ]
