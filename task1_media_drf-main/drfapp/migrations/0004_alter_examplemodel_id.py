# Generated by Django 4.1.3 on 2023-01-19 12:19

from django.db import migrations, models
import uuid


class Migration(migrations.Migration):

    dependencies = [
        ('drfapp', '0003_alter_examplemodel_id'),
    ]

    operations = [
        migrations.AlterField(
            model_name='examplemodel',
            name='id',
            field=models.UUIDField(default=uuid.UUID('aef1e044-0a0f-4a62-95ae-489706228d17'), primary_key=True, serialize=False),
        ),
    ]