from django.db import models


class CustomUser(models.Model):
    user_id = models.AutoField(primary_key=True)
    username = models.CharField(unique=True, max_length=255)
    email = models.EmailField(unique=True, max_length=255)
    password = models.CharField(max_length=255)
    height = models.CharField(max_length=255, null=True)
    weight = models.CharField(max_length=255, null=True)
    birth_date = models.DateField(null=True)
    gender = models.CharField(max_length=255, null=True)
    register_day = models.DateField()
    is_anonymous = False
    is_authenticated = True
    USERNAME_FIELD = 'username'

    def __str__(self):
        return self.username

    def to_json_edit_user(self):
        return {
            "username": self.username,
            "email": self.email,
            "height": self.height,
            "weight": self.weight,
            "birth_date": self.birth_date,
            "gender": self.gender
        }


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

    def _to_json_(self):
        return{
            "name": self.name
        }


class Exercise(models.Model):
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=255)
    muscle = models.ManyToManyField(Muscle)
    CATEGORY_CHOICES = (
        ('cardio', 'Cardio'),
        ('strength', 'Strength'),
        ('flexibility', 'Flexibility'),
    )
    category = models.CharField(max_length=255, choices=CATEGORY_CHOICES)
    description = models.CharField(max_length=255, null=True)
    video = models.CharField(max_length=255, null=True)
    image = models.CharField(max_length=255, null=True)

    def __str__(self):
        return self.name

    def _to_json_(self):
        return{
            "name": self.name,
            "muscle": self.muscle,
            "category": self.category,
            "description": self.description,
            "video": self.video,
            "image": self.image,
        }


class Routine(models.Model):
    id = models.AutoField(primary_key=True)
    date = models.DateTimeField()
    exercises = models.ManyToManyField(Exercise, through='ExerciseTraining')
    rating = models.CharField(max_length=255)
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)

    def __str__(self):
        return self.name

    def _to_json_(self):
        return{
            "date": self.date,
            "exercises": self.exercises,
            "rating": self.rating,
            "user": self.user,
        }


class ExerciseTraining(models.Model):
    id = models.AutoField(primary_key=True)
    exercise = models.ForeignKey(Exercise, on_delete=models.CASCADE)
    routine = models.ForeignKey(Routine, on_delete=models.CASCADE)
    planned_sets = models.CharField(max_length=255, null=True)
    actual_sets = models.IntegerField()
    planned_reps = models.CharField(max_length=255, null=True)
    actual_reps = models.IntegerField()
    weight = models.IntegerField()

    def __str__(self):
        return self.exercise + " " + self.routine.date

    def _to_json_(self):
        return{
            "exercise": self.exercise,
            "routine": self.routine,
            "planned_sets": self.planned_sets,
            "actual_sets": self.actual_sets,
            "planned_reps": self.planned_reps,
            "actual_reps": self.actual_reps,
            "weight": self.weight,
        }