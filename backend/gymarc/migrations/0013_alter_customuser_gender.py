# Generated by Django 5.0.6 on 2024-06-02 15:52

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('gymarc', '0012_remove_workout_exercisetraining_and_more'),
    ]

    operations = [
        migrations.AlterField(
            model_name='customuser',
            name='gender',
            field=models.CharField(choices=[('Male', 'Male'), ('Female', 'Female')], max_length=255, null=True),
        ),
    ]
