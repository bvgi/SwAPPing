# Generated by Django 3.2.9 on 2021-11-30 15:53

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('swAPP', '0001_initial'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='user',
            name='permission',
        ),
    ]
