# Generated by Django 5.0.6 on 2024-06-01 18:02

import django.db.models.deletion
from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('gymarc', '0008_remove_routine_user_remove_exercisetraining_user_and_more'),
    ]

    operations = [
        migrations.CreateModel(
            name='CustomUser',
            fields=[
                ('user_id', models.AutoField(primary_key=True, serialize=False)),
                ('username', models.CharField(max_length=255, unique=True)),
                ('email', models.EmailField(max_length=255, unique=True)),
                ('password', models.CharField(max_length=255)),
                ('height', models.CharField(max_length=255, null=True)),
                ('weight', models.CharField(max_length=255, null=True)),
                ('birth_date', models.DateField(null=True)),
                ('gender', models.CharField(max_length=255, null=True)),
                ('register_day', models.DateField()),
            ],
        ),
        migrations.CreateModel(
            name='Exercise',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False)),
                ('name', models.CharField(max_length=255)),
                ('category', models.CharField(choices=[('cardio', 'Cardio'), ('strength', 'Strength'), ('flexibility', 'Flexibility')], max_length=255)),
                ('description', models.CharField(max_length=255, null=True)),
                ('video', models.CharField(max_length=255, null=True)),
                ('image', models.CharField(max_length=255, null=True)),
            ],
        ),
        migrations.CreateModel(
            name='Muscle',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False)),
                ('name', models.CharField(max_length=255)),
            ],
        ),
        migrations.CreateModel(
            name='ExerciseTraining',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False)),
                ('planned_sets', models.CharField(max_length=255, null=True)),
                ('actual_sets', models.IntegerField()),
                ('planned_reps', models.CharField(max_length=255, null=True)),
                ('actual_reps', models.IntegerField()),
                ('weight', models.IntegerField()),
                ('date_training', models.DateField(null=True)),
                ('exercise', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='gymarc.exercise')),
                ('user', models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='gymarc.customuser')),
            ],
        ),
        migrations.CreateModel(
            name='DayExercise',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False)),
                ('name', models.CharField(max_length=255)),
                ('date_training', models.DateField()),
                ('rating', models.IntegerField()),
                ('user', models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='gymarc.customuser')),
                ('ExerciseTraining', models.ManyToManyField(to='gymarc.exercisetraining')),
            ],
        ),
        migrations.AddField(
            model_name='exercise',
            name='muscle',
            field=models.ManyToManyField(to='gymarc.muscle'),
        ),
        migrations.CreateModel(
            name='Routine',
            fields=[
                ('id', models.AutoField(primary_key=True, serialize=False)),
                ('name', models.CharField(default='Routine', max_length=255)),
                ('exercises', models.ManyToManyField(to='gymarc.exercise')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='gymarc.customuser')),
            ],
        ),
        migrations.CreateModel(
            name='UserSession',
            fields=[
                ('session_id', models.AutoField(primary_key=True, serialize=False)),
                ('user_token', models.CharField(max_length=255)),
                ('start_time', models.DateTimeField()),
                ('ip_address', models.CharField(max_length=255)),
                ('device_info', models.CharField(max_length=255)),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='gymarc.customuser')),
            ],
        ),
    ]