from django.db import models
from django.db.models import UniqueConstraint


class CustomUser(models.Model):
    gender_choices = [
        ('Male', 'Male'),
        ('Female', 'Female'),
    ]
    user_id = models.AutoField(primary_key=True)
    username = models.CharField(unique=True, max_length=255)
    email = models.EmailField(unique=True, max_length=255)
    password = models.CharField(max_length=255)
    height = models.CharField(max_length=255, null=True)
    weight = models.CharField(max_length=255, null=True)
    birth_date = models.DateField(null=True)
    gender = models.CharField(max_length=255, null=True, choices=gender_choices)
    activity_factor = models.CharField(max_length=255, null=True)
    register_day = models.DateField()

    def __str__(self):
        return self.username


class UserSession(models.Model):
    session_id = models.AutoField(primary_key=True)
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)
    user_token = models.CharField(max_length=255)
    start_time = models.DateTimeField()
    ip_address = models.CharField(max_length=255)
    device_info = models.CharField(max_length=255)

    def __str__(self):
        return self.user.username


class Muscle(models.Model):
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=255)

    def __str__(self):
        return self.name


class WeeklyRoutine(models.Model):
    name = models.CharField(max_length=255)
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE, null=True)

    def __str__(self):
        return self.name + " " + self.user.username


class WeeklyRoutineDay(models.Model):
    DAY_CHOICES = [
        ('Monday', 'Monday'),
        ('Tuesday', 'Tuesday'),
        ('Wednesday', 'Wednesday'),
        ('Thursday', 'Thursday'),
        ('Friday', 'Friday'),
        ('Saturday', 'Saturday'),
        ('Sunday', 'Sunday'),
    ]

    routine = models.ForeignKey(WeeklyRoutine, related_name='days', on_delete=models.CASCADE)
    day = models.CharField(max_length=10, choices=DAY_CHOICES)
    muscles = models.ManyToManyField(Muscle, blank=True)

    def to_json(self):
        return {
            "name": self.routine.name,
            "day": self.day,
            "muscles": self.muscles,
        }

    class Meta:
        constraints = [
            UniqueConstraint(fields=['routine', 'day'], name='unique_routine_day')
        ]

    def __str__(self):
        muscles_list = ", ".join([muscle.name for muscle in self.muscles.all()])
        return f"{self.day} ({muscles_list})"


class Exercise(models.Model):
    name = models.CharField(max_length=100)
    muscles = models.ManyToManyField(Muscle, blank=True)

    def __str__(self):
        return self.name


class PlannedExercise(models.Model):
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)
    routine_day = models.ForeignKey(WeeklyRoutineDay, related_name='planned_exercises', on_delete=models.CASCADE)
    exercise = models.ForeignKey(Exercise, on_delete=models.CASCADE)

    def __str__(self):
        return self.exercise.name


class PlannedSet(models.Model):
    planned_exercise = models.ForeignKey(PlannedExercise, related_name='planned_sets', on_delete=models.CASCADE)
    set_number = models.IntegerField()
    min_reps = models.IntegerField()
    max_reps = models.IntegerField()
    weight = models.FloatField()

    def __str__(self):
        return f"{self.planned_exercise.exercise.name} - set: {self.set_number}"


class CompletedExercise(models.Model):
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)
    planned_exercise = models.ForeignKey(PlannedExercise, related_name='completed_exercises', on_delete=models.CASCADE)
    date = models.DateField()

    def __str__(self):
        return self.planned_exercise.exercise.name


class CompletedSet(models.Model):
    completed_exercise = models.ForeignKey(CompletedExercise, related_name='completed_sets', on_delete=models.CASCADE)
    set_number = models.IntegerField()
    actual_reps = models.IntegerField()
    actual_weight = models.FloatField()

    def __str__(self):
        return f"{self.completed_exercise.planned_exercise.exercise.name} - set: {self.set_number}"
