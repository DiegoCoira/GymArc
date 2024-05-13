from django.contrib import admin
from .models import UserSession, Muscle, Exercise, Routine, ExerciseTraining, CustomUser

admin.site.register(CustomUser)
admin.site.register(UserSession)
admin.site.register(Muscle)
admin.site.register(Exercise)
admin.site.register(Routine)
admin.site.register(ExerciseTraining)
