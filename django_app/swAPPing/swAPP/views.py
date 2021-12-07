from rest_framework import viewsets, generics, permissions
from rest_framework.authentication import TokenAuthentication, SessionAuthentication
from rest_framework.generics import ListAPIView
from rest_framework.permissions import IsAuthenticated

from .permissions import IsOwnerOrReadOnly
from .serializer import *


class AnnouncementViewSet(viewsets.ModelViewSet):
    queryset = Announcement.objects.all()
    serializer_class = AnnouncementSerializer
    permission_classes = [permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnly]

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)


class UserAnnouncements(viewsets.ModelViewSet):
    authentication_classes = (TokenAuthentication, SessionAuthentication)
    permission_classes = (IsAuthenticated, IsOwnerOrReadOnly)
    serializer_class = UserSerializer

    def get_queryset(self):
        return Announcement.objects.all().filter(user=self.request.user.id)


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


class CategoriesViewSet(viewsets.ModelViewSet):
    queryset = Category.objects.all()
    serializer_class = CategorySerializer

