import json
import secrets
from datetime import datetime

import bcrypt
import traceback
from django.utils import timezone
from django.core.exceptions import ObjectDoesNotExist

from django.views import View
from django.http import HttpResponse
from django.http import JsonResponse
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt

from .models import *


# Create your views here.

@csrf_exempt
def sign_in(request):
    if request.method != 'POST':
        return JsonResponse({'error': 'Not supported HTTP method'}, status=405)
    body_json = json.loads(request.body)
    required_params = ['email', 'password', 'ip_address', 'device_info']
    missing_params = [param for param in required_params if param not in body_json]
    if missing_params:
        return JsonResponse({"error": "You are missing a parameter"}, status=400)
    json_email = body_json['email']
    json_password = body_json['password']
    try:
        db_user = CustomUser.objects.get(email=json_email)
    except CustomUser.DoesNotExist:
        return JsonResponse({"error": "User not in database"}, status=404)

    if bcrypt.checkpw(json_password.encode('utf8'), db_user.password.encode('utf8')):
        token = secrets.token_hex(16)
        ip_address = body_json['ip_address']
        device_info = body_json['device_info']
        current_datetime = timezone.now()
        db_session = UserSession.objects.get(user=db_user);
        if db_session:
            return JsonResponse({"token": db_session.user_token}, status=201)
        user_session = UserSession(user=db_user, user_token=token, start_time=current_datetime,
                                   ip_address=ip_address, device_info=device_info)
        user_session.save()
        return JsonResponse({"token": token}, status=201)
    else:
        return JsonResponse({"error": "Invalid password"}, status=401)


@csrf_exempt
def sign_up(request):
    if request.method != 'POST':
        return JsonResponse({'error': 'Unsupported HTTP method'}, status=405)

    body_json = json.loads(request.body)
    required_params = ['username', 'email', 'password', 'ip_address', 'device_info']

    if not all(param in body_json for param in required_params):
        return JsonResponse({'error': 'Missing parameter in body request'}, status=400)

    json_username = body_json['username']
    json_email = body_json['email']
    json_password = body_json['password']

    if '@' not in json_email or len(json_email) < 5:
        return JsonResponse({'error': 'Not valid email'}, status=400)

    if CustomUser.objects.filter(email=json_email).exists():
        return JsonResponse({'error': 'User already registered.'}, status=401)

    salted_and_hashed_pass = bcrypt.hashpw(json_password.encode('utf8'), bcrypt.gensalt()).decode('utf8')
    current_datetime = timezone.now()

    user_object = CustomUser(username=json_username, email=json_email, password=salted_and_hashed_pass,
                             register_day=current_datetime)
    user_object.save()

    # Generamos un token para el usuario
    token = secrets.token_hex(16)
    ip_address = body_json['ip_address']
    device_info = body_json['device_info']

    # Guardamos la session
    user_session = UserSession(user=user_object, user_token=token, start_time=current_datetime, ip_address=ip_address,
                               device_info=device_info)
    user_session.save()

    return JsonResponse({"token": token}, status=200)


@csrf_exempt
def user_data(request):
    if request.method == "PUT":
        user_token = request.headers.get('Authorization')
        print("User token received:", user_token)
        if not user_token:
            return JsonResponse({'error': 'Missing token parameter in the request'}, status=400)

        body_json = json.loads(request.body)
        required_params = ['height', 'weight', 'birth_date', 'gender', 'activity_factor']

        if not all(param in body_json for param in required_params):
            return JsonResponse({'error': 'Missing parameter in body request'}, status=400)
        try:
            session = UserSession.objects.get(user_token=user_token)
            print("session: ", session)
            user = session.user
            print("user: ", user)
            user_data_put = CustomUser.objects.get(username=user.username);
            print("user_data: ", user_data_put)
        except ObjectDoesNotExist:
            return JsonResponse({'error': 'User not found or invalid token'}, status=404)

        try:
            user_data_put.height = body_json['height']
            user_data_put.weight = body_json['weight']
            user_data_put.birth_date = body_json['birth_date']
            user_data_put.gender = body_json['gender']
            user_data_put.activity_factor = body_json['activity_factor']
            user_data_put.save()
            return JsonResponse({'message': 'User data updated successfully'}, status=200)
        except Exception as e:
            print("Error while saving user data: ", str(e))
            traceback.print_exc()
            return JsonResponse({'error': str(e)}, status=500)
    elif request.method == "GET":
        user_token = request.headers.get('Authorization')
        if not user_token:
            return JsonResponse({'error': 'Missing token parameter in the request'}, status=400)
        try:
            session = UserSession.objects.get(user_token=user_token)
            user = session.user
            user_data_get = CustomUser.objects.get(username=user.username)
            data = {
                'height': user_data_get.height,
                'weight': user_data_get.weight,
                'birth_date': user_data_get.birth_date,
                'gender': user_data_get.gender,
                'activity_factor': user_data_get.activity_factor
            }
            return JsonResponse(data, status=200)
        except ObjectDoesNotExist:
            return JsonResponse({'error': 'User not found or invalid token'}, status=404)

    else:
        return JsonResponse({'error': 'Invalid request'}, status=400)


@csrf_exempt
def weekly_training(request):
    if request.method != "GET":
        return JsonResponse({'error': 'Invalid request'}, status=400)

    user_token = request.headers.get('Authorization')
    if not user_token:
        return JsonResponse({'error': 'Missing token parameter in the request'}, status=400)

    try:
        session = UserSession.objects.get(user_token=user_token)
        user = session.user
    except ObjectDoesNotExist:
        return JsonResponse({'error': 'User not found or invalid token'}, status=404)

    try:
        routines = WeeklyRoutine.objects.filter(user=user)
        routine_data = []

        for routine in routines:
            routine_days = WeeklyRoutineDay.objects.filter(routine=routine)
            routine_days_data = []

            for routine_day in routine_days:
                muscle_names = [muscle.name for muscle in routine_day.muscles.all()]
                routine_day_data = {
                    'day': routine_day.day,
                    'muscles': muscle_names,
                }
                routine_days_data.append(routine_day_data)

            routine_data.append({
                'routine_name': routine.name,
                'routine_days': routine_days_data
            })

        return JsonResponse(routine_data, status=200, safe=False)
    except Exception as e:
        return JsonResponse({'error': str(e)}, status=500)
