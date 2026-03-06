from django.db import models
from django.contrib.auth.models import User
from .validators import validate_image_size, validate_image_extension, validate_audio_size, validate_audio_extension

MOOD= [
    (1, "CALM"),
    (2, "HAPPY"),
    (3, "SAD"),
    (4, "ANXIOUS"),
    (5, "EXCITED"),
    (6,"REFLECTIVE"),
    (7,"GRATEFUL"),
    (8,"ANGRY"),
    (9,"LONELY"),
    (10,"CONTENT"),
]

class Memory(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    title = models.CharField(max_length=200)
    note = models.TextField()

    latitude = models.FloatField(null=True, blank=True)
    longitude = models.FloatField(null=True, blank=True)
    location_name = models.CharField(blank=True,null=True,default="Unknown location" , max_length=555)
    #photo = models.ImageField(upload_to='photos/', blank=True, null=True)
    audio = models.FileField(upload_to='audio/', 
                             blank=True, null=True,
                             validators=[validate_audio_size, validate_audio_extension] )
    music_url = models.URLField(blank=True)

    tags = models.JSONField(default=list, blank=True) 
    mood = models.IntegerField(choices=MOOD, null=True, blank=True)
    is_favorite = models.BooleanField(default=False)
    is_deleted = models.BooleanField(default=False)
    revision = models.IntegerField(default=1)

    memory_datetime = models.DateTimeField(null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.title} - {self.user.username}"
    

from cloudinary.models import CloudinaryField

class MemoryImage(models.Model):
    memory = models.ForeignKey(Memory, on_delete=models.CASCADE, related_name="images")
    image = CloudinaryField('image')  # ← replace ImageField with this
    caption = models.CharField(max_length=255, blank=True, null=True)
    order = models.IntegerField(default=0)
    created_at = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        ordering = ['order', 'created_at']
    
    def __str__(self):
        return f"Image for {self.memory.title}"
    




