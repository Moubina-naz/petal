import django
from rest_framework.response import Response
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework import status
from .models import Memory , MemoryImage
from .serializers import MemorySerializer ,MemoryImageSerializer
from django.contrib.auth import authenticate, login
from django.contrib.auth.models import User
from rest_framework.decorators import permission_classes
from rest_framework_simplejwt.tokens import RefreshToken
from rest_framework.pagination import PageNumberPagination
from django.db import models
from django.db.models import Q
from django.shortcuts import get_object_or_404
from rest_framework.decorators import api_view, parser_classes
from rest_framework.parsers import MultiPartParser, FormParser
from math import radians, cos, sin, sqrt, atan2


@api_view(['GET', 'POST'])
@permission_classes([IsAuthenticated])
def memory_list(request):
    if request.method == 'GET':
        memories = Memory.objects.filter(
            user=request.user,
            is_deleted=False
        ).order_by('-memory_datetime')

        search = request.GET.get('search')
        if search:
            memories = memories.filter(
                Q(title__icontains=search) |
                Q(note__icontains=search) |
                Q(tags__icontains=search)
            )

        tags = request.GET.get('tags')
        if tags:
            for tag in tags.split(','):
                memories = memories.filter(tags__contains=[tag])

        mood = request.GET.get('mood')
        if mood:
            memories = memories.filter(mood=mood)

        favorite = request.GET.get('is_favorite')
        if favorite == "true":
            memories = memories.filter(is_favorite=True)

        lat = request.GET.get('lat')
        lng = request.GET.get('lng')
        radius = request.GET.get('radius')

        if lat and lng and radius:
            lat = float(lat)
            lng = float(lng)
            radius = float(radius)

            filtered = []
            for mem in memories:
                if mem.latitude is not None and mem.longitude is not None:
                    dlat = radians(mem.latitude - lat)
                    dlng = radians(mem.longitude - lng)
                    a = sin(dlat/2)**2 + cos(radians(lat)) * cos(radians(mem.latitude)) * sin(dlng/2)**2
                    dist = 6371 * (2 * atan2(sqrt(a), sqrt(1-a)))
                    if dist <= radius:
                        filtered.append(mem)

            memories = filtered  # ← only set here

        paginator = PageNumberPagination()
        paginator.page_size = 20
        page = paginator.paginate_queryset(memories, request)

        serializer = MemorySerializer(
            page,
            many=True,
            context={'request': request}  # 🔥 REQUIRED
      )        
        return paginator.get_paginated_response(serializer.data)

    elif request.method == 'POST':
        serializer = MemorySerializer(data=request.data)
        if serializer.is_valid():
            memory = serializer.save(user=request.user)
            return Response(
                MemorySerializer(memory, context={'request': request}).data,
                status=status.HTTP_201_CREATED
            )

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
from django.utils.timezone import make_aware
from datetime import datetime
import calendar

@api_view(['GET'])
@permission_classes([IsAuthenticated])
def memories_by_month(request):
    year = int(request.GET.get('year', datetime.now().year))
    month = int(request.GET.get('month', datetime.now().month))

    first_day = make_aware(datetime(year, month, 1))
    last_day = make_aware(datetime(year, month, calendar.monthrange(year, month)[1], 23, 59, 59))

    memories = Memory.objects.prefetch_related('images').filter(
        user=request.user,
        is_deleted=False,
        memory_datetime__range=(first_day, last_day)
    )

    # Group by day
    result = {}
    for memory in memories:
        day = memory.memory_datetime.day
        if day not in result:
            result[day] = []
        result[day].append(MemorySerializer(memory, context={'request': request}).data)

    return Response(result)

@api_view(['GET', 'PUT', 'DELETE'])
@permission_classes([IsAuthenticated])
def memory_detail(request, pk):
    try:
        mem = Memory.objects.prefetch_related('images').get(pk=pk, user=request.user)
    except Memory.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    if request.method == 'GET':
      serializer = MemorySerializer(
        mem,
        context={'request': request}  
      )
      return Response(serializer.data)


    elif request.method == 'PUT':
        serializer = MemorySerializer(mem, data=request.data, partial=True)
        if serializer.is_valid():
            updated_memory = serializer.save(revision=mem.revision + 1)
            
            image_files = request.FILES.getlist('image_files')
            for image_file in image_files:
                MemoryImage.objects.create(memory=updated_memory, image=image_file)
            
            return Response(MemorySerializer(updated_memory).data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        mem.is_deleted = True
        mem.revision += 1
        mem.save()
        return Response(status=status.HTTP_204_NO_CONTENT)
    
    
@api_view(['GET', 'PATCH'])
@permission_classes([IsAuthenticated])
def user_profile(request):
    user = request.user
    if request.method == 'GET':
        return Response({
            'id': user.id,
            'username': user.username,
            'email': user.email,
            'first_name': user.first_name,
            'last_name': user.last_name,
        })
    elif request.method == 'PATCH':
        username = request.data.get('username')
        email = request.data.get('email')
        first_name = request.data.get('first_name')
        last_name = request.data.get('last_name')

        if username and username != user.username:
            if User.objects.filter(username=username).exists():
                return Response({'error': 'Username already taken'}, status=400)
            user.username = username
        if email: user.email = email
        if first_name is not None: user.first_name = first_name
        if last_name is not None: user.last_name = last_name
        user.save()

        return Response({
            'id': user.id,
            'username': user.username,
            'email': user.email,
            'first_name': user.first_name,
            'last_name': user.last_name,
        })

@api_view(['POST'])
@permission_classes([IsAuthenticated])
def change_password(request):
    user = request.user
    old_password = request.data.get('old_password')
    new_password = request.data.get('new_password')

    if not user.check_password(old_password):
        return Response({'error': 'Wrong current password'}, status=400)
    
    user.set_password(new_password)
    user.save()
    return Response({'message': 'Password changed successfully'})

@api_view(['POST'])
def user_register(request): 
    username = request.data.get('username')
    password = request.data.get('password')
    email = request.data.get('email' , '')

    if User.objects.filter(username = username).exists():
        return Response({'error': 'Username already exists'},status=status.HTTP_400_BAD_REQUEST)
    user = User.objects.create_user(username=username,password=password,email=email)
    refresh = RefreshToken.for_user(user)
    return Response({'message' : 'User created successfully'}, status=status.HTTP_201_CREATED)


@api_view(['POST'])
def user_login(request):
    username = request.data.get('username')
    password = request.data.get('password')
    
    user = authenticate(request, username=username, password=password)
    if user is not None:
        refresh = RefreshToken.for_user(user)
        return Response({
            'message': 'Login successful',
            'access': str(refresh.access_token), 
            'refresh': str(refresh)   
        })
    else:
        return Response({'error': 'Invalid credentials'}, status=status.HTTP_401_UNAUTHORIZED)
    

@api_view(['PATCH'])
@permission_classes([IsAuthenticated])
def favorite_memory(request,pk):
    try:
        mem = Memory.objects.prefetch_related('images').get(pk=pk, user=request.user, is_deleted=False)
    except Memory.DoesNotExist:
        return Response({"error": "Memory not found"},status=status.HTTP_404_NOT_FOUND) 
    mem.is_favorite= True
    mem.revision +=1
    mem.save()
    return Response({"message": "Marked as favourite"},status=status.HTTP_200_OK)

@api_view(['PATCH'])
@permission_classes([IsAuthenticated])
def unfavorite_memory(request,pk):
    try:
        mem = Memory.objects.prefetch_related('images').get(pk=pk, user=request.user, is_deleted=False)
    except Memory.DoesNotExist:
        return Response({"error": "Memory not found"},status=status.HTTP_404_NOT_FOUND) 
    mem.is_favorite=False
    mem.revision +=1
    mem.save()
    return Response({"message": "Removed from favourite"},status=status.HTTP_200_OK)
 

@api_view(['POST'])
@parser_classes([MultiPartParser, FormParser])
def add_memory_images(request, pk):
    memory = get_object_or_404(Memory, pk=pk, user=request.user)

    files = request.FILES.getlist('image_files')

    if not files:
        return Response({"error": "No images provided"}, status=400)

    for index, image in enumerate(files):
        MemoryImage.objects.create(
            memory=memory,
            image=image,
            order=index
        )

    serializer = MemorySerializer(memory, context={'request': request})
    return Response(serializer.data, status=201)

@api_view(['DELETE'])
@permission_classes([IsAuthenticated])
def delete_memory_image(request, memory_pk, image_pk):
    """Delete a specific image from a memory"""
    try:
        memory = Memory.objects.get(pk=memory_pk, user=request.user)
        memory_image = MemoryImage.objects.get(pk=image_pk, memory=memory)
    except Memory.DoesNotExist:
        return Response({"error": "Memory not found"}, status=status.HTTP_404_NOT_FOUND)
    except MemoryImage.DoesNotExist:
        return Response({"error": "Image not found"}, status=status.HTTP_404_NOT_FOUND)
    
    memory_image.delete()
    memory.revision += 1
    memory.save()
    
    return Response({"message": "Image deleted successfully"}, status=status.HTTP_204_NO_CONTENT)

@api_view(['PATCH'])
@permission_classes([IsAuthenticated])
def update_image_caption(request, memory_pk, image_pk):
    """Update caption for a specific image"""
    try:
        memory = Memory.objects.get(pk=memory_pk, user=request.user)
        memory_image = MemoryImage.objects.get(pk=image_pk, memory=memory)
    except Memory.DoesNotExist:
        return Response({"error": "Memory not found"}, status=status.HTTP_404_NOT_FOUND)
    except MemoryImage.DoesNotExist:
        return Response({"error": "Image not found"}, status=status.HTTP_404_NOT_FOUND)
    
    caption = request.data.get('caption', '')
    memory_image.caption = caption
    memory_image.save()
    
    memory.revision += 1
    memory.save()
    
    return Response(MemoryImageSerializer(memory_image).data)

@api_view(['POST'])
@permission_classes([IsAuthenticated])
@parser_classes([MultiPartParser, FormParser])
def upload_memory_audio(request, pk):
    """Upload or replace audio for a memory. Max 5 min enforced by validator."""
    try:
        memory = Memory.objects.get(pk=pk, user=request.user, is_deleted=False)
    except Memory.DoesNotExist:
        return Response({"error": "Memory not found"}, status=status.HTTP_404_NOT_FOUND)

    audio_file = request.FILES.get('audio')
    if not audio_file:
        return Response({"error": "No audio file provided"}, status=status.HTTP_400_BAD_REQUEST)

    if memory.audio:
        try:
            import cloudinary.uploader
            public_id = memory.audio.public_id
            cloudinary.uploader.destroy(public_id, resource_type='video')
        except Exception:
            pass  

    from .validators import validate_audio_size, validate_audio_extension, validate_audio_duration
    from django.core.exceptions import ValidationError as DjangoValidationError
    try:
        validate_audio_size(audio_file)
        validate_audio_extension(audio_file)
        validate_audio_duration(audio_file)
    except DjangoValidationError as e:
        return Response({"error": e.message}, status=status.HTTP_400_BAD_REQUEST)

    memory.audio = audio_file
    memory.revision += 1
    memory.save()

    serializer = MemorySerializer(memory, context={'request': request})
    return Response(serializer.data, status=status.HTTP_200_OK)


@api_view(['DELETE'])
@permission_classes([IsAuthenticated])
def delete_memory_audio(request, pk):
    try:
        memory = Memory.objects.get(pk=pk, user=request.user, is_deleted=False)
    except Memory.DoesNotExist:
        return Response({"error": "Memory not found"}, status=status.HTTP_404_NOT_FOUND)

    if not memory.audio:
        return Response({"error": "This memory has no audio"}, status=status.HTTP_400_BAD_REQUEST)

    # For FileField, just delete the file and clear the field
    memory.audio.delete(save=False)  # Delete actual file from disk
    memory.audio = None
    memory.revision += 1
    memory.save()

    serializer = MemorySerializer(memory, context={'request': request})
    return Response(serializer.data, status=status.HTTP_200_OK)