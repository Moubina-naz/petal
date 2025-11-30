from django.core.exceptions import ValidationError
import os

def validate_image_size(value):
    max_size = 5 * 1024 * 1024  
    if value.size > max_size:
        raise ValidationError(f'Image size too large. Max size is {max_size/1024/1024}MB')

def validate_audio_size(value):
    max_size = 10 * 1024 * 1024  
    if value.size > max_size:
        raise ValidationError(f'Audio file too large. Max size is {max_size/1024/1024}MB')

def validate_image_extension(value):
    valid_extensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.webp']
    ext = os.path.splitext(value.name)[1].lower()
    if ext not in valid_extensions:
        raise ValidationError(f'Unsupported file extension. Allowed: {", ".join(valid_extensions)}')

def validate_audio_extension(value):
    valid_extensions = ['.mp3', '.wav', '.ogg', '.m4a']
    ext = os.path.splitext(value.name)[1].lower()
    if ext not in valid_extensions:
        raise ValidationError(f'Unsupported audio format. Allowed: {", ".join(valid_extensions)}')