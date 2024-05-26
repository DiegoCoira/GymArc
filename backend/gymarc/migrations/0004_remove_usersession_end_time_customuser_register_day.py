# Generated by Django 5.0.6 on 2024-05-26 17:44

import django.utils.timezone
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('gymarc', '0003_alter_usersession_end_time'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='usersession',
            name='end_time',
        ),
        migrations.AddField(
            model_name='customuser',
            name='register_day',
            field=models.DateField(default=django.utils.timezone.now),
            preserve_default=False,
        ),
    ]
