# Generated by Django 4.1.3 on 2023-02-08 15:44

from django.db import migrations, models
import uuid


class Migration(migrations.Migration):

    dependencies = [
        ('drfapp', '0009_alter_examplemodel_id'),
    ]

    operations = [
        migrations.AlterField(
            model_name='examplemodel',
            name='id',
            field=models.UUIDField(default=uuid.UUID('d31f0e59-bca5-4a96-b43b-5a10aee303e1'), primary_key=True, serialize=False),
        ),
    ]
