# Generated by Django 5.0.6 on 2024-05-26 17:16

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('gymarc', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='customuser',
            name='birth_date',
            field=models.DateField(null=True),
        ),
        migrations.AlterField(
            model_name='customuser',
            name='gender',
            field=models.CharField(max_length=255, null=True),
        ),
        migrations.AlterField(
            model_name='customuser',
            name='height',
            field=models.CharField(max_length=255, null=True),
        ),
        migrations.AlterField(
            model_name='customuser',
            name='weight',
            field=models.CharField(max_length=255, null=True),
        ),
    ]
