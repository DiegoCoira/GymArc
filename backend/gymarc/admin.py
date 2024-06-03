from django.contrib import admin
from .models import *

admin.site.register(CustomUser)
admin.site.register(UserSession)
admin.site.register(Muscle)
admin.site.register(Exercise)
admin.site.register(WeeklyRoutine)
admin.site.register(WeeklyRoutineDay)
admin.site.register(PlannedExercise)
admin.site.register(PlannedSet)
admin.site.register(CompletedExercise)
admin.site.register(CompletedSet)


