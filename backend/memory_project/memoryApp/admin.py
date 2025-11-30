from django.contrib import admin
from .models import Memory

# Register your models here.
@admin.register(Memory)
class MemoryAdmin(admin.ModelAdmin):
    list_display = ['title', 'user', 'created_at']
    list_filter = ['created_at']
    search_fields = ['title']