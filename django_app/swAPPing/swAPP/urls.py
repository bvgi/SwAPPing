from django.conf.urls import url
from django.contrib import admin
from django.urls import path, include
from rest_framework import routers
from .views import *

router = routers.DefaultRouter()
router.register(r'Announcements', AnnouncementViewSet)
router.register(r'Users', UserViewSet)
router.register(r'Reviews', ReviewViewSet)
router.register(r'FollowedUsers', FollowedUserViewSet)
router.register(r'LikedPosts', LikedViewSet)
router.register(r'UsersAnnouncements', UserAnnouncements, basename="UsersAnnouncements")
router.register(r'Categories', CategoriesViewSet)

urlpatterns = [
    path('', include(router.urls)),
    path('api-auth/', include('rest_framework.urls', namespace='rest_framework')),
]
