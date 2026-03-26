from django.urls import path
from . import views

urlpatterns = [
    path('memories/', views.memory_list, name='memory-list'),
    path('memories/by-month/', views.memories_by_month, name='memories-by-month'),

    path('memories/<int:pk>/', views.memory_detail, name='memory-detail'),
    path('memories/<int:pk>/images/', views.add_memory_images, name='add-memory-images'),
    path('memories/<int:memory_pk>/images/<int:image_pk>/', views.delete_memory_image, name='delete-memory-image'),
    path('memories/<int:memory_pk>/images/<int:image_pk>/caption/', views.update_image_caption, name='update-image-caption'),
    path('register/', views.user_register, name='user-register'),
    path('login/', views.user_login, name='user-login'), 
    path('memories/<int:pk>/favorite/', views.favorite_memory, name='favourite-memory'),
    path('memories/<int:pk>/unfavorite/', views.unfavorite_memory, name='unfavourite-memory'),
    path('profile/', views.user_profile, name='user-profile'),
    path('profile/change-password/', views.change_password, name='change-password'),

    path('memories/<int:pk>/audio/', views.upload_memory_audio),   # POST
    path('memories/<int:pk>/audio/delete/', views.delete_memory_audio),  # DELETE

]
