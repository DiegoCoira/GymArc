# Generated by Django 5.0.6 on 2024-06-01 18:02

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('gymarc', '0007_alter_exercisetraining_date_training'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='routine',
            name='user',
        ),
        migrations.RemoveField(
            model_name='exercisetraining',
            name='user',
        ),
        migrations.RemoveField(
            model_name='dayexercise',
            name='user',
        ),
        migrations.RemoveField(
            model_name='dayexercise',
            name='ExerciseTraining',
        ),
        migrations.RemoveField(
            model_name='exercise',
            name='muscle',
        ),
        migrations.RemoveField(
            model_name='exercisetraining',
            name='exercise',
        ),
        migrations.RemoveField(
            model_name='routine',
            name='exercises',
        ),
        migrations.DeleteModel(
            name='UserSession',
        ),
        migrations.DeleteModel(
            name='CustomUser',
        ),
        migrations.DeleteModel(
            name='DayExercise',
        ),
        migrations.DeleteModel(
            name='Muscle',
        ),
        migrations.DeleteModel(
            name='Exercise',
        ),
        migrations.DeleteModel(
            name='ExerciseTraining',
        ),
        migrations.DeleteModel(
            name='Routine',
        ),
    ]
