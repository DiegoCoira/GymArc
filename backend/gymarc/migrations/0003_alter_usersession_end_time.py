# Generated by Django 5.0.6 on 2024-05-26 17:25

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('gymarc', '0002_alter_customuser_birth_date_alter_customuser_gender_and_more'),
    ]

    operations = [
        migrations.AlterField(
            model_name='usersession',
            name='end_time',
            field=models.DateTimeField(null=True),
        ),
    ]