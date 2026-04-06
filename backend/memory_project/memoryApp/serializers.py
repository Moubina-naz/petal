from rest_framework import serializers
from .models import Memory
from .models import MemoryImage

        
class MemoryImageSerializer(serializers.ModelSerializer):
    image_url = serializers.SerializerMethodField()

    class Meta:
        model = MemoryImage
        fields = ['id', 'image_url', 'caption', 'order', 'created_at']
        read_only_fields = ['id', 'created_at']

    def get_image_url(self, obj):
        url = obj.image.url
        print(f"IMAGE URL GENERATED: {url}")
        if url.startswith('http'):
            return url
        request = self.context.get('request')
        if request is not None:
            return request.build_absolute_uri(url)
        return url

class MemorySerializer(serializers.ModelSerializer):
    images = MemoryImageSerializer(many=True, read_only=True)
    location_name = serializers.CharField(required=False, allow_blank=True)
    audio_url = serializers.SerializerMethodField()

    class Meta:
        model = Memory
        fields = [
            'id', 'title', 'note', 'latitude', 'longitude','location_name',
            'audio', 'music_url', 'tags', 'mood',
            'is_favorite', 'is_deleted', 'revision','memory_datetime', 
            'created_at', 'updated_at', 'images'
        ]
        read_only_fields = ['id', 'created_at', 'updated_at', 'revision', 'images']

    def get_audio_url(self, obj):
        if not obj.audio:
            return None
        try:
            # cloudinaryField gives a full URL directly
            url = obj.audio.url
            if url.startswith('http'):
                return url
            return None
        except Exception:
            return None