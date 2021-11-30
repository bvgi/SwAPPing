from django.contrib import admin
from .models import *

admin.site.register(Review)
admin.site.register(FollowedUser)
admin.site.register(Voivodeship)
admin.site.register(Category)
admin.site.register(Status)
admin.site.register(Genre)
admin.site.register(Announcement)
admin.site.register(Liked)

# Register your models here.
