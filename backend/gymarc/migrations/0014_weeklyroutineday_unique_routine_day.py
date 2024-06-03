# Generated by Django 5.0.6 on 2024-06-02 16:02

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('gymarc', '0013_alter_customuser_gender'),
    ]

    operations = [
        migrations.AddConstraint(
            model_name='weeklyroutineday',
            constraint=models.UniqueConstraint(fields=('routine', 'day'), name='unique_routine_day'),
        ),
    ]
