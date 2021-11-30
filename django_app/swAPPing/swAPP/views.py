from django.http import HttpResponse, JsonResponse
from rest_framework.generics import ListAPIView
from rest_framework.parsers import JSONParser
from rest_framework import viewsets, generics, permissions
from rest_framework.renderers import JSONRenderer
from rest_framework.views import APIView

from .serializer import *

class AnnouncementViewSet(viewsets.ModelViewSet):
    queryset = Announcement.objects.all()
    serializer_class = AnnouncementSerializer

class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [permissions.IsAuthenticated]


class ReviewViewSet(viewsets.ModelViewSet):
    queryset = Review.objects.all()
    serializer_class = ReviewSerializer
    permission_classes = [permissions.IsAuthenticated]


class FollowedUserViewSet(viewsets.ModelViewSet):
    queryset = FollowedUser.objects.all()
    serializer_class = FollowedUserSerializer
    permission_classes = [permissions.IsAuthenticated]


class LikedViewSet(viewsets.ModelViewSet):
    queryset = Liked.objects.all()
    serializer_class = LikedSerializer
    permission_classes = [permissions.IsAuthenticated]

