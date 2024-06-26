# Generated by Django 5.0.6 on 2024-06-01 17:41

import django.db.models.deletion
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('gymarc', '0004_remove_usersession_end_time_customuser_register_day'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='exercisetraining',
            name='routine',
        ),
        migrations.RemoveField(
            model_name='routine',
            name='date',
        ),
        migrations.RemoveField(
            model_name='routine',
            name='rating',
        ),
        migrations.AddField(
            model_name='exercisetraining',
            name='date_training',
            field=models.DateField(null=True),
        ),
        migrations.AddField(
            model_name='exercisetraining',
            name='user',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='gymarc.customuser'),
        ),
        migrations.AddField(
            model_name='routine',
            name='name',
            field=models.CharField(default='Routine', max_length=255),
        ),
        migrations.CreateModel(
            name='DayExercise',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False)),
                ('name', models.CharField(max_length=255)),
                ('date_training', models.DateField()),
                ('rating', models.IntegerField()),
                ('ExerciseTraining', models.ManyToManyField(to='gymarc.exercisetraining')),
                ('user', models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='gymarc.customuser')),
            ],
        ),
    ]
