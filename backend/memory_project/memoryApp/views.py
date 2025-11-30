from rest_framework.response import Response
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework import status
from .models import Memory , MemoryImage
from .serializers import MemorySerializer ,MemoryImageSerializer
from django.contrib.auth import authenticate, login
from django.contrib.auth.models import User
from rest_framework.permissions import IsAuthenticated
from rest_framework.decorators import permission_classes
from rest_framework_simplejwt.tokens import RefreshToken
from django.db import models
from django.db.models import Q
from math import radians, cos, sin, sqrt, atan2


@api_view(['GET', 'POST'])
@permission_classes([IsAuthenticated]) 
def memory_list(request):
    if request.method == 'GET':
        memories = Memory.objects.filter(user=request.user,is_deleted=False)

        search = request.GET.get('search')
        if search:
           memories = memories.filter(
                Q(title__icontains=search) |
                Q(note__icontains=search) |
                Q(tags__icontains=search)
        )

        tags = request.GET.get('tags')
        if tags:
            tag_list = tags.split(',')
            for tag in tag_list:
                memories = memories.filter(tags__contains=[tag])
        
        mood = request.GET.get('mood')
        if mood:
            memories = memories.filter(mood=mood)
        
        favorite = request.GET.get('favorite')
        if favorite == "true":
            memories = memories.filter(is_favorite=True)
            
        media = request.GET.get('media')
        if media == "photo":
            memories = memories.filter(photo__isnull=False)
        elif media == "audio":
            memories = memories.filter(audio__isnull=False)

        start_date = request.GET.get('start')
        end_date = request.GET.get('end')

        if start_date:
            memories = memories.filter(created_at__date__gte=start_date)
        if end_date:
            memories = memories.filter(created_at__date__lte=end_date)
        
        sort = request.GET.get('sort')
        if sort == "latest":
            memories = memories.order_by('-created_at')
        elif sort == "oldest":
            memories = memories.order_by('created_at')
        elif sort == "title":
            memories = memories.order_by('title')

    

        lat = request.GET.get('lat')
        lng = request.GET.get('lng')
        radius = request.GET.get('radius')

        if lat and lng and radius:
            lat = float(lat)
            lng = float(lng)
            radius = float(radius)

        result = []
        for mem in memories:
            if mem.latitude and mem.longitude:
                dlat = radians(mem.latitude - lat)
                dlng = radians(mem.longitude - lng)
                a = sin(dlat/2)**2 + cos(radians(lat)) * cos(radians(mem.latitude)) * sin(dlng/2)**2
                dist = 6371 * (2 * atan2(sqrt(a), sqrt(1-a)))  # KM

                if dist <= radius:
                    result.append(mem)

        memories = result

        serializer = MemorySerializer(memories, many=True)
        return Response(serializer.data)
    

    elif request.method == 'POST':
        serializer = MemorySerializer(data=request.data)
        if serializer.is_valid():
            memory = serializer.save(user=request.user)
            
            image_files = request.FILES.getlist('image_files')
            for image_file in image_files:
                MemoryImage.objects.create(memory=memory, image=image_file)
            
            memories = memories.prefetch_related('images')
            serializer = MemorySerializer(memories, many=True)
            return Response(serializer.data)
        
    

@api_view(['GET', 'PUT', 'DELETE'])
@permission_classes([IsAuthenticated])
def memory_detail(request, pk):
    try:
        mem = Memory.objects.prefetch_related('images').get(pk=pk, user=request.user)
    except Memory.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    if request.method == 'GET':
        serializer = MemorySerializer(mem)
        return Response(serializer.data)

    elif request.method == 'PUT':
        serializer = MemorySerializer(mem, data=request.data, partial=True)
        if serializer.is_valid():
            updated_memory = serializer.save(revision=mem.revision + 1)
            
            # Handle new image uploads during update
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
@permission_classes([IsAuthenticated])
def add_memory_images(request, pk):
    """Add images to an existing memory"""
    try:
        memory = Memory.objects.get(pk=pk, user=request.user)
    except Memory.DoesNotExist:
        return Response({"error": "Memory not found"}, status=status.HTTP_404_NOT_FOUND)
    
    image_files = request.FILES.getlist('images')
    if not image_files:
        return Response({"error": "No images provided"}, status=status.HTTP_400_BAD_REQUEST)
    
    created_images = []
    for image_file in image_files:
        memory_image = MemoryImage.objects.create(memory=memory, image=image_file)
        created_images.append(MemoryImageSerializer(memory_image).data)
    
    memory.revision += 1
    memory.save()
    
    return Response({
        "message": f"{len(created_images)} images added successfully",
        "images": created_images
    }, status=status.HTTP_201_CREATED)

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