from rest_framework import serializers
from .models import Memory
from .models import MemoryImage

        
class MemoryImageSerializer(serializers.ModelSerializer):
    class Meta:
        model = MemoryImage
        fields = ['id', 'image', 'caption', 'order', 'created_at']
        read_only_fields = ['id', 'created_at']


class MemorySerializer(serializers.ModelSerializer):

    images = MemoryImageSerializer(many=True, read_only=True)
    image_files = serializers.ListField(
        child=serializers.ImageField(max_length=100000, allow_empty_file=False, use_url=False),
        write_only=True,
        required=False
    )
    class Meta:
        model = Memory
        fields = [
            'id', 'title', 'note', 'latitude', 'longitude',
            'audio', 'music_url', 'tags', 'mood',
            'is_favorite', 'is_deleted', 'revision',
            'created_at', 'updated_at', 'images', 'image_files'
        ]
        read_only_fields = ['id', 'created_at', 'updated_at', 'revision', 'images']

