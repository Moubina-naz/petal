from django.core.exceptions import ValidationError
import os

def validate_image_size(value):
    max_size = 5 * 1024 * 1024  
    if value.size > max_size:
        raise ValidationError(f'Image size too large. Max size is {max_size/1024/1024}MB')

def validate_audio_size(value):
    max_size = 5 * 1024 * 1024  
    if value.size > max_size:
        raise ValidationError(f'Audio file too large. Max size is {max_size/1024/1024}MB')

def validate_image_extension(value):
    valid_extensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.webp']
    ext = os.path.splitext(value.name)[1].lower()
    if ext not in valid_extensions:
        raise ValidationError(f'Unsupported file extension. Allowed: {", ".join(valid_extensions)}')

def validate_audio_extension(value):
    """Validate audio file extension"""
    valid_extensions = ['.mp3', '.wav', '.ogg', '.m4a','.mp4']
    ext = os.path.splitext(value.name)[1].lower()
    if ext not in valid_extensions:
        raise ValidationError(f'Unsupported audio format. Allowed: {", ".join(valid_extensions)}')  
def validate_audio_duration(value):
    """Max 5 minutes = 300 seconds. Uses mutagen — no ffmpeg needed."""
    try:
        from mutagen import File as MutagenFile
        audio = MutagenFile(value)
        if audio is not None and audio.info is not None:
            duration = audio.info.length  # seconds
            if duration > 300:
                raise ValidationError(
                    f'Audio too long. Max duration is 5 minutes. Your file is {int(duration // 60)}m {int(duration % 60)}s.'
                )
    except ValidationError:
        raise
    except Exception:
        # incase mutagen can't read the file then size validator is the fallback
        pass